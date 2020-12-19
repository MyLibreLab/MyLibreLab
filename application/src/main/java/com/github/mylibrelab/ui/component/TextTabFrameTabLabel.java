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

import javax.swing.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.mylibrelab.text.Text;
import com.github.weisj.darklaf.components.tabframe.JTabFrame;
import com.github.weisj.darklaf.components.tabframe.TabFrameTabLabel;
import com.github.weisj.darklaf.util.Alignment;

/**
 * Version of {@link TabFrameTabLabel} which uses a {@link Text} as the title.
 */
public class TextTabFrameTabLabel extends TabFrameTabLabel {

    private final Text title;

    /**
     * Create new TabComponent for the frame of {@link JTabFrame}.
     *
     * @param title the title.
     * @param icon the icon.
     * @param orientation the alignment.
     * @param index the index.
     * @param parent the parent layout manager.
     */
    public TextTabFrameTabLabel(@NotNull final Text title, @Nullable final Icon icon,
            @NotNull final Alignment orientation, final int index, @NotNull final JTabFrame parent) {
        super(title.getText(), icon, orientation, index, parent);
        this.title = title;
    }

    @Override
    public String getTitle() {
        if (title != null) setTitle(title.getText());
        return super.getTitle();
    }
}
