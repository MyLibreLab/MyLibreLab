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

package com.github.mylibrelab.action;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.mylibrelab.text.Text;

public abstract class BasicAction extends AnAction {

    private final Icon icon;
    private final Icon disabledIcon;

    public BasicAction(@NotNull final Text displayName) {
        this(displayName, null, null, null);
    }

    public BasicAction(@NotNull final Text displayName, @Nullable final Icon icon) {
        this(displayName, icon, null, null);
    }

    public BasicAction(@NotNull final Text displayName, @Nullable final Icon icon, @Nullable final Icon disabledIcon) {
        this(displayName, icon, disabledIcon, null);
    }

    public BasicAction(@NotNull final Text displayName, @Nullable final Icon icon, @Nullable final Icon disabledIcon,
            @Nullable final String identifier) {
        super(identifier);
        this.icon = icon;
        this.disabledIcon = disabledIcon;
        Presentation presentation = getPresentation();
        presentation.setDisplayName(displayName);
        presentation.setIcon(icon);
    }

    @Override
    public final void update(@NotNull final ActionContext context) {
        super.update(context);
        Presentation presentation = getPresentation();
        presentation.setIcon(presentation.isEnabled() ? icon : disabledIcon);
    }

    protected void doUpdate(@NotNull final ActionContext context) {}
}
