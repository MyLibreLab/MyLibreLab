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

package com.github.mylibrelab.observable;

import org.jetbrains.annotations.NotNull;

public final class ObservableDouble {

    private final Observable<?> observable;
    private final String propertyName;
    private double value;

    /**
     * Creates a new observable property.
     *
     * @param observable the parent observable object.
     * @param propertyName the name of the property.
     */
    public ObservableDouble(@NotNull final Observable<?> observable, @NotNull final String propertyName) {
        this(observable, propertyName, 0);
    }

    /**
     * Creates a new observable property.
     *
     * @param observable the parent observable object.
     * @param propertyName the name of the property.
     * @param initial the initial value of the property.
     */
    public ObservableDouble(@NotNull final Observable<?> observable, @NotNull final String propertyName,
            final double initial) {
        this.observable = observable;
        this.propertyName = propertyName;
        this.value = initial;
        observable.getManager().setValue(propertyName, value);
    }

    /**
     * Get the value of the property.
     *
     * @return the value.
     */
    public double get() {
        return value;
    }

    /**
     * The name of the property.
     *
     * @return the name.
     */
    @NotNull
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Set the value of the property.
     *
     * @param value the new value.
     */
    public void set(final double value) {
        this.value = value;
        observable.getManager().updateValue(propertyName, value);
    }
}
