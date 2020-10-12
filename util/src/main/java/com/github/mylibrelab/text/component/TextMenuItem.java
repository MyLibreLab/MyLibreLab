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

package com.github.mylibrelab.text.component;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.mylibrelab.text.Text;

/**
 * MenuItem which takes {@link Text} instead of a {@link String}.
 */
public class TextMenuItem extends JMenuItem {

    private Text text;

    /**
     * Create a new {@link TextMenuItem}.
     *
     * @param text the text of the menuitem.
     */
    public TextMenuItem(@NotNull final Text text) {
        this(text, null);
    }

    /**
     * Create a new {@link TextMenuItem}.
     *
     * @param text the text of the menuitem.
     * @param icon the icon of the menuitem.
     */
    public TextMenuItem(@NotNull final Text text, @Nullable final Icon icon) {
        super(text.getText(), icon);
        this.text = text;
    }

    @Override
    public String getText() {
        return text != null ? text.getText() : "";
    }

    /**
     * Set the text.
     *
     * @param text the text
     */
    public void setText(@NotNull final Text text) {
        this.text = text;
        setText(text.getText());
    }
}
