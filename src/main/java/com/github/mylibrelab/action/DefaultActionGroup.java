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

import org.jetbrains.annotations.NotNull;

/**
 * Top level default {@link AnActionGroup}s.
 */
public enum DefaultActionGroup {
    TOOLBAR("groups.toolbar", false), FILE_MENU("groups.file_menu", true), EDIT_MENU("groups.edit_menu", true),
    VM_MENU("groups.vm_menu", true), WINDOW_MENU("groups.window_menu", true), HELP_MENU("groups.help_menu", true);

    private final boolean inMenuBar;
    private final AnActionGroup actionGroup;

    DefaultActionGroup(@NotNull final String identifier, final boolean inMenuBar) {
        this.inMenuBar = inMenuBar;
        this.actionGroup = new AnActionGroup(identifier);
    }

    /**
     * Returns the corresponding action group.
     *
     * @return the {@link AnActionGroup}.
     */
    public AnActionGroup getActionGroup() {
        return actionGroup;
    }

    /**
     * Returns whether the corresponding action group belongs to the menubar.
     *
     * @return true if it belongs to the menubar.
     */
    public boolean isInMenuBar() {
        return inMenuBar;
    }
}
