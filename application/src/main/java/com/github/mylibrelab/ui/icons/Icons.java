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

import java.util.Objects;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;

import com.github.weisj.darklaf.icons.IconLoader;

public class Icons {

    private static final int DEFAULT_SIZE = 16;
    private static final IconLoader LOADER = IconLoader.get(AllIcons.class);
    private static final IconLoader INTERNAL_LOADER = IconLoader.get(IconLoader.class);

    private Icons() {
        throw new IllegalStateException("Utility class");
    }

    @NotNull
    public static Icon load(final String path) {
        return load(path, true);
    }

    @NotNull
    public static Icon load(final String path, final boolean themed) {
        return load(path, DEFAULT_SIZE, DEFAULT_SIZE, themed);
    }

    @NotNull
    public static Icon load(final String path, final int w, final int h, final boolean themed) {
        return load(LOADER, path, w, h, themed);
    }

    @NotNull
    public static Icon load(final IconLoader loader, final String path, final int w, final int h,
            final boolean themed) {
        return Objects.requireNonNull(loader.getIcon(path, w, h, themed));
    }

    @NotNull
    public static Icon internal(final String path) {
        return internal(path, DEFAULT_SIZE, DEFAULT_SIZE);
    }

    @NotNull
    public static Icon internal(final String path, final int w, final int h) {
        return load(INTERNAL_LOADER, path, w, h, true);
    }
}
