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

package com.github.mylibrelab.resources;

import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public class Resources {

    private static final ResourceBundle MESSAGES =
            ResourceBundle.getBundle("com.github.mylibrelab.resources.messages", Locale.getDefault());

    private Resources() {
        throw new IllegalStateException("Utility class");
    }

    public static String getErrorMessage(@NotNull final ErrorType errorType) {
        return getString(errorType.getKey());
    }

    public static String getString(
            @NotNull @PropertyKey(resourceBundle = "com.github.mylibrelab.resources.messages") final String key) {
        return MESSAGES.getString(key);
    }

    public static @NotNull String getFileContent(@NotNull final String fileName) {
        return getFileContent(StackWalker.getInstance(RETAIN_CLASS_REFERENCE).getCallerClass(), fileName,
                StandardCharsets.UTF_8);
    }

    public static @NotNull String getFileContent(@NotNull final String fileName, @NotNull final Charset charset) {
        return getFileContent(StackWalker.getInstance(RETAIN_CLASS_REFERENCE).getCallerClass(), fileName, charset);
    }

    public static String getFileContent(@NotNull final Class<?> callerClass, @NotNull final String fileName,
            @NotNull final Charset charset) {
        try {
            URL resourceUrl = Objects.requireNonNull(callerClass.getResource(fileName));
            return Files.readString(Path.of(resourceUrl.toURI()), charset);
        } catch (IOException | URISyntaxException e) {
            return "";
        }
    }

}
