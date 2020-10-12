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

import java.util.Objects;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

/**
 * Implementation for {@link Text} which is based on a {@link Supplier<String>}.
 */
public class DynamicText implements Text {

    private final Supplier<String> textSupplier;

    /**
     * Create a new {@link DynamicText} which determines it's text value based on the given supplier.
     *
     * @param textSupplier the text supplier.
     */
    public DynamicText(@NotNull final Supplier<String> textSupplier) {
        this.textSupplier = textSupplier;
    }

    @NotNull
    @Override
    public String getText() {
        return textSupplier.get();
    }

    @Override
    public String toString() {
        return getText();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamicText that = (DynamicText) o;
        return Objects.equals(textSupplier, that.textSupplier);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(textSupplier);
    }
}
