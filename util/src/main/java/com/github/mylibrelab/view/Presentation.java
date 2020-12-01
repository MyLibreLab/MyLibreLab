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

package com.github.mylibrelab.view;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.mylibrelab.text.Text;

public class Presentation {

    protected Icon icon;
    protected Text displayName;

    /**
     * Creates a new default {@link Presentation}.
     */
    public Presentation() {
        this(Text.of(""));
    }

    /**
     * Creates a new {@link Presentation} with the given name.
     *
     * @param displayName the display name.
     */
    public Presentation(@NotNull final Text displayName) {
        this.displayName = displayName;
    }

    /**
     * Creates a new {@link Presentation} with the given name.
     *
     * @param displayName the display name.
     */
    public Presentation(@NotNull final Text displayName, @Nullable final Icon icon) {
        this.displayName = displayName;
        this.icon = icon;
    }

    /**
     * Returns the display name of this action.
     *
     * @return the display name.
     */
    @NotNull
    public Text getDisplayName() {
        return displayName;
    }

    /**
     * Set the display name of this action.
     *
     * @param displayName the display name.
     */
    public void setDisplayName(@NotNull final Text displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the icon of this action.
     *
     * @return the icon.
     */
    @Nullable
    public Icon getIcon() {
        return icon;
    }

    /**
     * Set the icon of this action.
     *
     * @param icon the icon.
     */
    public void setIcon(@Nullable final Icon icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Presentation{" +
                "icon=" + icon +
                ", displayName=" + displayName +
                '}';
    }
}
