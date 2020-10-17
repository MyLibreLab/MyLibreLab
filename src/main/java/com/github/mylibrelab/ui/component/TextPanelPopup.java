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

package com.github.mylibrelab.ui.component;

import java.awt.*;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.mylibrelab.text.Text;
import com.github.weisj.darklaf.components.tabframe.PanelPopup;

/**
 * Version of {@link PanelPopup} which uses a {@link Text} as the title.
 */
public class TextPanelPopup extends PanelPopup {

    private final Text title;

    /**
     * Creates a new Popup that holds one component.
     *
     * @param title the title of the component.
     * @param content the content of the popup.
     */
    public TextPanelPopup(@NotNull final Text title, @NotNull final Component content) {
        super(title.getText(), content);
        this.title = title;
    }

    /**
     * Creates a new Popup that holds one component.
     *
     * @param title the title of the component.
     * @param icon the icon of the popup.
     * @param content the content of the popup.
     */
    public TextPanelPopup(@NotNull final Text title, @Nullable final Icon icon, @NotNull final Component content) {
        super(title.getText(), icon, content);
        this.title = title;
    }

    @Override
    public String getTitle() {
        if (title != null) setTitle(title.getText());
        return super.getTitle();
    }
}
