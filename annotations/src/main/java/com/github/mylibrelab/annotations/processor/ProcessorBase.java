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

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Target;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import javax.annotation.processing.AbstractProcessor;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

public abstract class ProcessorBase extends AbstractProcessor {

    protected static final boolean ANNOTATIONS_UNCLAIMED = false;

    protected boolean verifyMetaAnnotatedElement(final Element element) {
        if (element.getAnnotation(Inherited.class) != null) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "cannot be @Inherited");
            return false;
        }
        Target target = element.getAnnotation(Target.class);
        if (target == null) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "must be marked with @Target");
            return false;
        }
        if (target.value().length == 0) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "must have at least one element type in @Target");
            return false;
        }
        for (ElementType type : target.value()) {
            if (type != ElementType.TYPE) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        "should not be permitted on element type " + type);
                return false;
            }
        }
        return true;
    }

    protected boolean isVoid(final TypeMirror typeMirror) {
        var voidMirror = processingEnv.getTypeUtils().getNoType(TypeKind.VOID);
        return processingEnv.getTypeUtils().isSameType(voidMirror, typeMirror);
    }

    protected AnnotationValue[] getAnnotationValues(final AnnotationMirror annotation, final String... names) {
        Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues =
                annotation != null ? annotation.getElementValues() : Collections.emptyMap();
        var values = new AnnotationValue[names.length];
        for (var entry : elementValues.entrySet()) {
            var name = entry.getKey().getSimpleName().toString();
            for (int i = 0; i < names.length; i++) {
                if (names[i].equals(name)) {
                    values[i] = entry.getValue();
                }
            }
        }
        return values;
    }

    protected <T> TypeElement getTypeElement(final Class<T> classType) {
        return processingEnv.getElementUtils().getTypeElement(classType.getName());
    }

    protected <T> TypeMirror getTypeMirror(final Class<T> classType) {
        return getTypeElement(classType).asType();
    }

    protected TypeElement asTypeElement(final TypeMirror typeMirror) {
        return (TypeElement) processingEnv.getTypeUtils().asElement(typeMirror);
    }

    protected AnnotationMirror getAnnotationMirror(final TypeElement element, final TypeMirror annotationType) {
        return element
                .getAnnotationMirrors().stream().filter(mir -> processingEnv.getTypeUtils()
                        .isSameType(mir.getAnnotationType().asElement().asType(), annotationType))
                .findFirst().orElse(null);
    }

    protected void info(final String info) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, info + "\r\n");
    }

    protected void error(final String error, final Element e, final AnnotationMirror a, final AnnotationValue v) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, error + "\r\n", e, a, v);
    }

    protected void error(final String error, final Element e, final AnnotationMirror a) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, error + "\r\n", e, a);
    }

    protected void error(final String error, final Element e) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, error + "\r\n", e);
    }

    protected void error(final String error) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, error + "\r\n");
    }

    protected void error(final Exception e) {
        var out = new ByteArrayOutputStream();
        e.printStackTrace(new PrintWriter(out));
        error(e.toString() + "\r\n" + out.toString(StandardCharsets.UTF_8));
    }

    protected void warning(final String warning, final Element e, final AnnotationMirror a, final AnnotationValue v) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, warning + "\r\n", e, a, v);
    }

    protected void warning(final String warning, final Element e, final AnnotationMirror a) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, warning + "\r\n", e, a);
    }

    protected void warning(final String warning, final Element e) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, warning + "\r\n", e);
    }

    protected void warning(final String warning) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, warning + "\r\n");
    }
}
