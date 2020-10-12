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

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import org.jetbrains.annotations.NotNull;

/**
 * Wrapper class to dynamically update a {@link ResourceBundle} based on the current {@link Locale}
 */
public class DynamicResourceBundle {

    private final Object lock = new Object();
    private final String bundleName;
    private ResourceBundle bundle;
    private Locale currentLocale;

    /**
     * Create a new {@link DynamicResourceBundle}.
     *
     * @param bundleName the bundle name.
     */
    public DynamicResourceBundle(@NotNull final String bundleName) {
        this.bundleName = bundleName;
        this.currentLocale = Locale.getDefault();
        this.bundle = ResourceBundle.getBundle(bundleName, currentLocale);
    }

    /**
     * Returns the bundle for the current {@link Locale} as determined by {@link Locale#getDefault()}.
     *
     * @return the resource bundle for the current {@link Locale}.
     */
    @NotNull
    public ResourceBundle getBundle() {
        synchronized (lock) {
            Locale defaultLocale = Locale.getDefault();
            if (!Objects.equals(defaultLocale, currentLocale)) {
                bundle = ResourceBundle.getBundle(bundleName, defaultLocale);
                currentLocale = defaultLocale;
            }
            return bundle;
        }
    }

    /**
     * Returns the name of the resource bundle.
     *
     * @return the bundle name.
     */
    @NotNull
    public String getBundleName() {
        return bundleName;
    }

    @Override
    public String toString() {
        return "DynamicResourceBundle{" + "bundleName='" + bundleName + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamicResourceBundle that = (DynamicResourceBundle) o;
        return bundleName.equals(that.bundleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bundleName);
    }
}
