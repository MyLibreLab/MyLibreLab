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

package com.github.mylibrelab.text;

import org.jetbrains.annotations.NotNull;

/**
 * Text that delegates to a resource bundle hence changes its value based on the current locale.
 */
public class InternationalizedText implements Text {

    private final String resourceString;
    private final DynamicResourceBundle bundle;

    /**
     * Creates a new {@link InternationalizedText} which changes its value based on the current locale.
     *
     * @param bundleName the resource bundle name
     * @param resourceString the resource key in the bundle
     */
    public InternationalizedText(@NotNull final String bundleName, @NotNull final String resourceString) {
        this(new DynamicResourceBundle(bundleName), resourceString);
    }

    /**
     * Creates a new {@link InternationalizedText} which changes its value based on the current locale.
     *
     * @param bundle the dynamic resource bundle
     * @param resourceString the resource key in the bundle
     */
    public InternationalizedText(@NotNull final DynamicResourceBundle bundle, @NotNull final String resourceString) {
        this.bundle = bundle;
        this.resourceString = resourceString;
    }

    @NotNull
    @Override
    public String getText() {
        return bundle.getBundle().getString(resourceString);
    }

    @Override
    public String toString() {
        return getText();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InternationalizedText that = (InternationalizedText) o;

        if (!resourceString.equals(that.resourceString)) return false;
        return bundle.equals(that.bundle);
    }

    @Override
    public int hashCode() {
        int result = resourceString.hashCode();
        result = 31 * result + bundle.hashCode();
        return result;
    }
}
