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

package com.github.mylibrelab.ui.icons;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;


/**
 * Container class for all available icons.
 */
public class AllIcons {

    private AllIcons() {
        throw new IllegalStateException("Static data holder");
    }

    public static final @NotNull Icon LOGO = Icons.load("mylibrelab.svg");

    public static final class Actions {
        private Actions() {
            throw new IllegalStateException("Static data holder");
        }

        public static final @NotNull Icon Undo = Icons.internal("menu/undo.svg");
        public static final @NotNull Icon UndoDisabled = Icons.internal("menu/undoDisabled.svg");
        public static final @NotNull Icon Redo = Icons.internal("menu/redo.svg");
        public static final @NotNull Icon RedoDisabled = Icons.internal("menu/redoDisabled.svg");
        public static final @NotNull Icon Cut = Icons.internal("menu/cut.svg");
        public static final @NotNull Icon CutDisabled = Icons.internal("menu/cutDisabled.svg");
        public static final @NotNull Icon Copy = Icons.internal("menu/copy.svg");
        public static final @NotNull Icon CopyDisabled = Icons.internal("menu/copyDisabled.svg");
        public static final @NotNull Icon Paste = Icons.internal("menu/paste.svg");
        public static final @NotNull Icon PasteDisabled = Icons.internal("menu/pasteDisabled.svg");
        public static final @NotNull Icon Delete = Icons.internal("menu/delete.svg");
        public static final @NotNull Icon DeleteDisabled = Icons.internal("menu/deleteDisabled.svg");
        public static final @NotNull Icon Settings = Icons.internal("menu/settings.svg");
    }
}
