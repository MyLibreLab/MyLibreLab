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
import java.util.MissingResourceException;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import com.github.mylibrelab.text.DynamicResourceBundle;
import com.github.mylibrelab.text.InternationalizedText;
import com.github.mylibrelab.text.Text;

/**
 * Utility class to provide application level resources.
 */
public class Resources {

    private static final DynamicResourceBundle MESSAGES =
            new DynamicResourceBundle("com.github.mylibrelab.resources.messages");

    private Resources() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Get an error message meant to be displayed to the used.
     *
     * @param errorType the error type.
     * @throws MissingResourceException if no object for the given key can be found.
     * @return the error message.
     */
    @NotNull
    public static String getErrorMessage(@NotNull final ErrorType errorType) {
        return getString(errorType.getKey());
    }

    /**
     * Get a {@link String} resource. The usage of {@link #getResourceText(String)} is recommended where
     * possible.
     *
     * @see #getResourceText(String)
     * @param key the key.
     * @throws MissingResourceException if no object for the given key can be found
     * @return the string value.
     */
    @NotNull
    public static String getString(
            @NotNull @PropertyKey(resourceBundle = "com.github.mylibrelab.resources.messages") final String key) {
        return MESSAGES.getBundle().getString(key);
    }

    /**
     * Returns a {@link Text} instance which automatically resolves it's content from the given key and
     * the current locale. For any current locale the value of {@link Text#getText()} is the same as
     * {@link #getString(String)}.
     * <p>
     * Unlike {@link Text#getText()} this method will not throw any exception if the given key has no
     * associated resource value. But calling {@link Text#getText()} will.
     *
     * @param key the key.
     * @return the {@link Text} instance.
     */
    @NotNull
    public static Text getResourceText(
            @NotNull @PropertyKey(resourceBundle = "com.github.mylibrelab.resources.messages") final String key) {
        return new InternationalizedText(MESSAGES, key);
    }

    /**
     * Read the content of a resource text file with the given name.
     * <p>
     * Note that this method is caller aware and will resolve the resource path relative to the
     * qualified package-name of the caller class.
     * <p>
     * This method assumes a default character encoding of {@link StandardCharsets#UTF_8}.
     *
     * @see #getFileContent(String, Charset)
     * @see #getFileContent(Class, String, Charset)
     * @param fileName the name of the file which content should be read.
     * @throws NullPointerException if the file could not be found.
     * @return the files content or an empty string if the file could not be loaded.
     */
    @NotNull
    public static String getFileContent(@NotNull final String fileName) {
        return getFileContent(StackWalker.getInstance(RETAIN_CLASS_REFERENCE).getCallerClass(), fileName,
                StandardCharsets.UTF_8);
    }

    /**
     * Read the content of a resource text file with the given name.
     * <p>
     * Note that this method is caller aware and will resolve the resource path relative to the
     * qualified package-name of the caller class.
     *
     * @see #getFileContent(String)
     * @see #getFileContent(Class, String, Charset)
     * @param fileName the name of the file which content should be read.
     * @param charset the {@link Charset encoding} of the file.
     * @throws NullPointerException if the file could not be found.
     * @return the files content or an empty string if the file could not be loaded.
     */
    @NotNull
    public static String getFileContent(@NotNull final String fileName, @NotNull final Charset charset) {
        return getFileContent(StackWalker.getInstance(RETAIN_CLASS_REFERENCE).getCallerClass(), fileName, charset);
    }

    /**
     * Read the content of a resource text file with the given name.
     *
     * @see #getFileContent(String, Charset)
     * @see #getFileContent(String)
     * @param callerClass the class to which the files path should be resolved to.
     * @param fileName the name of the file which content should be read.
     * @param charset the {@link Charset encoding} of the file.
     * @throws NullPointerException if the file could not be found.
     * @return the files content or an empty string if the file could not be loaded.
     */
    @NotNull
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
