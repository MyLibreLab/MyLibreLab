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

import javax.swing.*;

import com.github.mylibrelab.action.AnAction;
import com.github.mylibrelab.action.AnActionGroup;
import com.github.mylibrelab.action.DefaultActionGroup;
import com.github.mylibrelab.action.Place;

public class ActionMenuBar extends JMenuBar {

    public ActionMenuBar() {
        for (var defaultGroup : DefaultActionGroup.values()) {
            if (defaultGroup.isInMenuBar()) {
                var group = defaultGroup.getActionGroup();
                add(new ActionMenu(group));
            }
        }
    }

    private String prettyPrint(final AnAction action) {
        var pres = action.getPresentation();
        var sb = new StringBuilder(pres.getDisplayName().getText());
        sb.append(" (visible=").append(pres.isVisible()).append(", enabled=").append(pres.isEnabled()).append(")");
        if (action instanceof AnActionGroup) {
            var group = (AnActionGroup) action;
            sb.append("(popup=").append(group.isPopup(Place.MENUBAR)).append(")");
            for (var a : group) {
                sb.append("\n");
                var str = prettyPrint(a);
                sb.append(str.replace("\n", "\n    "));
            }
        }
        return sb.toString();
    }

}
