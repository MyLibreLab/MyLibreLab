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

import java.net.URL;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;

// import com.github.weisj.darklaf.icons.IconLoader; // Replaced with simple implementation

public class Icons {

    private static final int DEFAULT_SIZE = 16;
    
    /**
     * Simple replacement for darklaf IconLoader
     */
    private static class SimpleIconLoader {
        private final Class<?> baseClass;
        
        public SimpleIconLoader(Class<?> baseClass) {
            this.baseClass = baseClass;
        }
        
        public Icon getIcon(String path, int w, int h, boolean themed) {
            try {
                URL url = baseClass.getResource("/" + path);
                if (url != null) {
                    ImageIcon icon = new ImageIcon(url);
                    if (w != icon.getIconWidth() || h != icon.getIconHeight()) {
                        return new ImageIcon(icon.getImage().getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH));
                    }
                    return icon;
                }
            } catch (Exception e) {
                // Fallback to empty icon
            }
            return new EmptyIcon(Math.max(w, h));
        }
        
        public static SimpleIconLoader get(Class<?> clazz) {
            return new SimpleIconLoader(clazz);
        }
    }
    
    /**
     * Simple empty icon implementation
     */
    private static class EmptyIcon implements Icon {
        private final int size;

        public EmptyIcon(int size) {
            this.size = size;
        }

        @Override
        public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
            // Empty implementation - draws nothing
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }

    private static final SimpleIconLoader LOADER = SimpleIconLoader.get(AllIcons.class);
    private static final SimpleIconLoader INTERNAL_LOADER = SimpleIconLoader.get(Icons.class);

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
    public static Icon load(final SimpleIconLoader loader, final String path, final int w, final int h,
            final boolean themed) {
        Icon icon = loader.getIcon(path, w, h, themed);
        return icon != null ? icon : new EmptyIcon(Math.max(w, h));
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