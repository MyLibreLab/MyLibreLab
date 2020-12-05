/*
 * Copyright (C) 2020 MyLibreLab
 * Based on MyOpenLab by Carmelo Salafia www.myopenlab.de
 * Copyright (C) 2004  Carmelo Salafia cswi@gmx.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.github.mylibrelab.annotations.processor;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import com.github.mylibrelab.annotations.ProviderFor;
import com.github.mylibrelab.annotations.ProviderForProvider;
import com.github.mylibrelab.annotations.ProviderSpec;
import com.github.mylibrelab.annotations.Service;
import com.squareup.javapoet.*;


@SupportedAnnotationTypes("*")
public class ProviderProcessor extends ProcessorBase {

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        try {
            Collection<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(ProviderSpec.class);
            var annotatedTypeElements = ElementFilter.typesIn(annotatedElements);
            var providerSpecMirror = getTypeMirror(ProviderSpec.class);
            for (var providerAnnotationDefinition : annotatedTypeElements) {
                if (!verifyMetaAnnotatedElement(providerAnnotationDefinition)) return false;
                var providerSpec = providerAnnotationDefinition.getAnnotation(ProviderSpec.class);
                var mirror = getAnnotationMirror(providerAnnotationDefinition, providerSpecMirror);
                if (!checkProviderSpec(providerSpec, providerAnnotationDefinition, mirror)) return false;
            }
            for (var annotation : annotations) {
                var providerSpec = annotation.getAnnotation(ProviderSpec.class);
                if (providerSpec != null) {
                    info("Processing provider annotation " + annotation);
                    processAnnotation(roundEnv, annotation, providerSpec);
                }
            }
        } catch (Exception e) {
            // Don't propagate any exceptions to the compiler
            error(e);
        }
        return ANNOTATIONS_UNCLAIMED;
    }

    private boolean checkProviderSpec(final ProviderSpec providerSpec, final TypeElement element,
            final AnnotationMirror annotation) {
        var values = getAnnotationValues(annotation, "inputBound", "outputBound");
        return checkType(getBound(providerSpec, ProviderSpec::inputBound), true, element, annotation, values[0])
                && checkType(getBound(providerSpec, ProviderSpec::outputBound), false, element, annotation, values[1]);
    }

    private TypeMirror getBound(final ProviderSpec providerSpec, final Function<ProviderSpec, Class<?>> getter) {
        try {
            return processingEnv.getElementUtils().getTypeElement(getter.apply(providerSpec).getName()).asType();
        } catch (MirroredTypeException mte) {
            return mte.getTypeMirror();
        }
    }

    private boolean checkType(final TypeMirror typeMirror, final boolean allowVoid, final TypeElement element,
            final AnnotationMirror annotation, final AnnotationValue value) {
        boolean violated;
        if (allowVoid) {
            violated = !isVoid(typeMirror) && typeMirror.getKind().isPrimitive();
        } else {
            violated = typeMirror.getKind().isPrimitive();
        }
        if (violated) {
            error("Primitive type " + typeMirror + " is not allowed.", element, annotation, value);
            return false;
        }
        return true;
    }

    private void processAnnotation(final RoundEnvironment roundEnv, final TypeElement annotation,
            final ProviderSpec providerSpec) {
        Collection<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
        List<TypeElement> types = ElementFilter.typesIn(annotatedElements);
        processAnnotation(types, providerSpec);
    }

    private void processAnnotation(final List<TypeElement> types, final ProviderSpec providerSpec) {
        for (TypeElement typeElement : types) {
            info("Processing element " + typeElement);
            processElement(typeElement, providerSpec);
        }
    }

    private void processElement(final TypeElement typeElement, final ProviderSpec providerSpec) {
        var typeUtils = processingEnv.getTypeUtils();

        TypeMirror providerForType = typeUtils.erasure(getTypeMirror(ProviderFor.class));
        Predicate<TypeMirror> providerFilter = tm -> typeUtils.isSameType(typeUtils.erasure(tm), providerForType);

        findInterface(typeElement.asType(), providerFilter).ifPresentOrElse(declaredType -> {
            var typeArguments = declaredType.getTypeArguments();
            TypeMirror receiverType = typeArguments.get(0);
            TypeMirror returnType = typeArguments.get(1);
            if (!checkTypeBounds(receiverType, returnType, providerSpec, typeElement)) {
                return;
            }

            TypeElement erasedSuperType = getTypeElement(ProviderForProvider.class);
            createProviderClass(typeElement, receiverType, returnType, erasedSuperType);
        }, () -> error("Provider needs to implement ProviderFor.", typeElement));
    }

    private boolean checkTypeBounds(final TypeMirror receiverType, final TypeMirror returnType,
            final ProviderSpec providerSpec, final TypeElement typeElement) {
        if (providerSpec == null) return true;
        var typeUtils = processingEnv.getTypeUtils();
        TypeMirror inputBoundType = getBound(providerSpec, ProviderSpec::inputBound);
        if (!typeUtils.isSameType(inputBoundType, typeUtils.getNoType(TypeKind.VOID))
                && !typeUtils.isAssignable(inputBoundType, receiverType)) {
            error("Type " + receiverType + " can't be assigned from input bound " + inputBoundType, typeElement);
            return false;
        }
        TypeMirror outputTypeBounds = getBound(providerSpec, ProviderSpec::outputBound);
        // Object is the top type thus any type trivially is accepted.
        if (!typeUtils.isSameType(outputTypeBounds, getTypeMirror(Object.class))
                && !typeUtils.isAssignable(returnType, outputTypeBounds)) {
            error("Type " + returnType + " can't be assigned to output bound " + outputTypeBounds, typeElement);
            return false;
        }
        return true;
    }

    private void createProviderClass(final TypeElement typeElement, final TypeMirror receiverType,
            final TypeMirror returnType, final TypeElement erasedSuperType) {

        var typeUtils = processingEnv.getTypeUtils();
        var elementUtils = processingEnv.getElementUtils();

        FieldSpec providerField =
                FieldSpec.builder(TypeName.get(typeElement.asType()), "provider", Modifier.PRIVATE).build();

        MethodSpec getProviderMethod =
                MethodSpec.methodBuilder("getProvider").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
                        .returns(ParameterizedTypeName.get(ClassName.get(ProviderFor.class), TypeName.get(receiverType),
                                TypeName.get(returnType)))
                        .beginControlFlow("if (provider == null)").addStatement("provider = new $T()", typeElement)
                        .endControlFlow().addStatement("return provider").build();

        MethodSpec constructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
                .addStatement("super($T.class, $T.class)", receiverType, returnType).build();

        AnnotationSpec service =
                AnnotationSpec.builder(Service.class).addMember("value", "$T.class", ProviderForProvider.class).build();

        var providerTypeName = typeElement.getQualifiedName().toString().replace(".", "_") + "Provider";
        var packageName = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();

        TypeSpec providerForProvider = TypeSpec.classBuilder(providerTypeName)
                .superclass(typeUtils.getDeclaredType(erasedSuperType, receiverType, returnType)).addAnnotation(service)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL).addMethod(constructor).addField(providerField)
                .addMethod(getProviderMethod).addOriginatingElement(typeElement).build();

        JavaFile file = JavaFile.builder(packageName, providerForProvider).build();

        try {
            file.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Optional<DeclaredType> findInterface(final TypeMirror typeMirror, final Predicate<TypeMirror> predicate) {
        if (predicate.test(typeMirror)) {
            return Optional.of((DeclaredType) typeMirror);
        }
        for (var interfaceElement : processingEnv.getTypeUtils().directSupertypes(typeMirror)) {
            var optTypeMirror = findInterface(interfaceElement, predicate);
            if (optTypeMirror.isPresent()) {
                return optTypeMirror;
            }
        }
        return Optional.empty();
    }
}
