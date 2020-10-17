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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.StandardLocation;

import com.github.mylibrelab.annotations.Service;
import com.github.mylibrelab.annotations.ServiceSpec;

@SupportedAnnotationTypes("*")
public class ServiceProcessor extends ProcessorBase {

    private static final boolean DEBUG = false;
    public static final String SERVICE_DEFS_FOLDER = "META-INF/serviceDefs/";
    public static final String SERVICES_FOLDER = "META-INF/services/";

    TypeElement dummyElement;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        try {
            processPlainServices(roundEnv);
            if (!checkServiceSpecs(roundEnv)) {
                return false;
            }
            processAnnotations(annotations, roundEnv);
            if (roundEnv.processingOver()) {
                generateConfigFiles();
            }
        } catch (Exception e) {
            // Don't propagate any exceptions to the compiler
            error(e);
        }
        return false;
    }

    private void processAnnotations(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        for (var annotation : annotations) {
            var serviceSpec = annotation.getAnnotation(ServiceSpec.class);
            if (serviceSpec != null) {
                info("Processing service annotation " + annotation);
                processServiceAnnotation(roundEnv, annotation, getServiceInterface(serviceSpec));
            }
        }
    }

    private void processServiceAnnotation(final RoundEnvironment roundEnv, final TypeElement annotation,
            final TypeMirror serviceType) {
        Collection<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
        List<TypeElement> elements = ElementFilter.typesIn(annotatedElements);
        if (!elements.isEmpty()) {
            info("Processing " + elements.size() + " elements annotated with " + annotation.getSimpleName());
            for (var element : elements) {
                if (dummyElement == null) dummyElement = element;
                processElement(element, serviceType);
            }
        }
    }

    private void processPlainServices(final RoundEnvironment roundEnv) {
        Collection<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(Service.class);
        var elements = ElementFilter.typesIn(annotatedElements);
        if (!elements.isEmpty()) {
            info("Processing " + elements.size() + " elements annotated with " + Service.class.getSimpleName());
            for (var element : elements) {
                var service = element.getAnnotation(Service.class);
                var serviceInterface = getServiceInterface(service);
                if (serviceInterface.getKind().isPrimitive()) {
                    var annotationMirror =
                            getAnnotationMirror(asTypeElement(serviceInterface), getTypeMirror(Service.class));
                    error("Primitive type '" + serviceInterface + "' not allowed.", element, annotationMirror,
                            getAnnotationValues(annotationMirror, "value")[0]);
                    continue;
                }
                processElement(element, serviceInterface);
            }
        }
    }

    private void processElement(final TypeElement element, final TypeMirror serviceType) {
        var typeUtils = processingEnv.getTypeUtils();
        if (!typeUtils.isAssignable(element.asType(), serviceType)) {
            error(element + " needs to be of type " + serviceType, element);
            return;
        }
        var interfaceName = getBinaryName(asTypeElement(serviceType));
        var implementationName = getBinaryName(element);
        try {
            var path = getServiceDefsPath(interfaceName, implementationName);
            if (DEBUG) info("Creating " + path);
            var file = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", path, element);
            var outStream = file.openOutputStream();
            try (var write = new BufferedWriter(new OutputStreamWriter(outStream, StandardCharsets.UTF_8))) {
                write.write(implementationName);
            }
            outStream.close();
        } catch (IOException e) {
            error(e);
        }
    }

    private String getServiceDefsPath(final String folderName, final String fileName) {
        return SERVICE_DEFS_FOLDER + folderName + "/" + fileName;
    }

    private void generateConfigFiles() {
        var serviceDefPath = getServiceDefPath();
        if (serviceDefPath != null) {
            try (var files = Files.list(serviceDefPath)) {
                files.filter(Files::isDirectory).forEach(d -> {
                    var serviceName = d.getFileName().toString();
                    try (var serviceImpls = Files.list(d)) {
                        var content = serviceImpls.map(Path::getFileName).map(Objects::toString)
                                .collect(Collectors.joining("\n"));
                        writeServiceFile(serviceName, content);
                    } catch (IOException e) {
                        error(e);
                    }
                });
            } catch (IOException e) {
                error(e);
            }
        }
    }

    private void writeServiceFile(final String service, final String content) throws IOException {
        var serviceName = SERVICES_FOLDER + service;
        try {
            if (DEBUG) info("Checking " + serviceName);
            var file = processingEnv.getFiler().getResource(StandardLocation.CLASS_OUTPUT, "", serviceName);
            var inStr = file.openInputStream();
            inStr.close();
            file.delete();
        } catch (IOException ignored) {
            // The file doesn't exist thus doesn't need to be deleted.
        }
        info("Writing Service file " + serviceName);
        var file =
                processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", serviceName, dummyElement);
        var outStream = file.openOutputStream();
        try (var write = new BufferedWriter(new OutputStreamWriter(outStream, StandardCharsets.UTF_8))) {
            write.write(content);
        }
        outStream.close();
    }

    private Path getServiceDefPath() {
        var filer = processingEnv.getFiler();
        try {
            var resource = filer.getResource(StandardLocation.SOURCE_OUTPUT, "", SERVICE_DEFS_FOLDER + "tmp");
            Path path = Paths.get(resource.toUri()).getParent();
            resource.delete();
            return path;
        } catch (IOException e) {
            error(e);
        }
        return null;
    }

    private TypeMirror getServiceInterface(final Service service) {
        try {
            return processingEnv.getElementUtils().getTypeElement(service.value().getName()).asType();
        } catch (MirroredTypeException mte) {
            return mte.getTypeMirror();
        }
    }

    private TypeMirror getServiceInterface(final ServiceSpec service) {
        try {
            return processingEnv.getElementUtils().getTypeElement(service.value().getName()).asType();
        } catch (MirroredTypeException mte) {
            return mte.getTypeMirror();
        }
    }

    private boolean checkServiceSpecs(final RoundEnvironment roundEnv) {
        Collection<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(ServiceSpec.class);
        var annotatedTypeElements = ElementFilter.typesIn(annotatedElements);
        for (var serviceAnnotationDefinition : annotatedTypeElements) {
            if (!verifyMetaAnnotatedElement(serviceAnnotationDefinition)) return false;
        }
        return true;
    }

    private String getBinaryName(TypeElement element) {
        return getBinaryNameImpl(element, element.getSimpleName().toString());
    }

    private String getBinaryNameImpl(TypeElement element, String className) {
        Element enclosingElement = element.getEnclosingElement();

        if (enclosingElement instanceof PackageElement) {
            PackageElement pkg = (PackageElement) enclosingElement;
            if (pkg.isUnnamed()) {
                return className;
            }
            return pkg.getQualifiedName() + "." + className;
        }

        TypeElement typeElement = (TypeElement) enclosingElement;
        return getBinaryNameImpl(typeElement, typeElement.getSimpleName() + "$" + className);
    }
}
