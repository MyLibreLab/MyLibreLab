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

import org.jetbrains.annotations.NotNull;

/**
 * Constant value implementation for {@link Text}.
 */
public class ConstantText implements Text {

    private final String text;

    /**
     * Create a new {@link ConstantText} with the given text value. {@link #getText()} will always
     * return this exact value.
     *
     * @param text the text value.
     */
    public ConstantText(@NotNull final String text) {
        this.text = text;
    }

    @NotNull
    @Override
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstantText that = (ConstantText) o;
        return text.equals(that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(text);
    }
}
