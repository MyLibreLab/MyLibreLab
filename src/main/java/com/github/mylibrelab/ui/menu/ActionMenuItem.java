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

package com.github.mylibrelab.ui.menu;

import org.jetbrains.annotations.NotNull;

import com.github.mylibrelab.action.ActionContext;
import com.github.mylibrelab.action.AnAction;
import com.github.mylibrelab.text.component.TextMenuItem;

class ActionMenuItem extends TextMenuItem implements ActionMenuElement {

    private final AnAction action;

    ActionMenuItem(@NotNull final AnAction action) {
        super(action.getPresentation().getDisplayName());
        this.action = action;
        update();
    }

    @Override
    public void updateActions(final ActionContext context, final boolean updateChildren) {
        action.update(context);
        update();
    }

    private void update() {
        var presentation = action.getPresentation();
        setEnabled(presentation.isEnabled());
        setVisible(presentation.isVisible());
        setIcon(presentation.getIcon());
        setDisabledIcon(getIcon());
    }
}
