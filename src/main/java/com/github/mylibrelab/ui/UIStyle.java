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

package com.github.mylibrelab.ui;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.UIResource;

public class UIStyle {

    private static final Map<JComponent, List<Consumer<JComponent>>> listeners = new WeakHashMap<>();

    static {
        UIManager.addPropertyChangeListener(e -> {
            String key = e.getPropertyName();
            if ("lookAndFeel".equals(key)) {
                listeners.keySet().forEach(UIStyle::updateComponent);
            }
        });
    }

    /**
     * Attaches a configuration action that is executed when Look and Feel changes.
     * <p>
     * Note: the action is executed when {@code withDynamic} is called, and the action is executed even
     * if the new and the old LaFs are the same.
     * </p>
     *
     * @param component component to update
     * @param onUpdateUi action to run (immediately and when look and feel changes)
     * @param <T> type of the component
     * @return input component
     */
    public static <T extends JComponent> T withDynamic(final T component, final Consumer<T> onUpdateUi) {
        // Explicit component update is required since the component already exists
        // and we can't want to wait for the next LaF change
        onUpdateUi.accept(component);
        synchronized (listeners) {
            listeners.compute(component, (k, v) -> {
                if (v == null) {
                    // noinspection unchecked
                    return Collections.singletonList((Consumer<JComponent>) onUpdateUi);
                }
                List<Consumer<JComponent>> res = v.size() == 1 ? new ArrayList<>(v) : v;
                // noinspection unchecked
                res.add((Consumer<JComponent>) onUpdateUi);
                return res;
            });
        }
        return component;
    }

    public static <T extends JComponent> T withMonospacedFont(final T component) {
        return withFontFamily(component, Font.MONOSPACED);
    }

    public static <T extends JComponent> T withFontFamily(final T component, final String fontName) {
        return withTransformedFont(component, f -> {
            var attrs = f.getAttributes();
            // noinspection unchecked
            ((Map<TextAttribute, String>) attrs).put(TextAttribute.FAMILY, fontName);
            return f.deriveFont(attrs);
        });
    }

    public static <T extends JComponent> T withTransformedFont(final T component,
            final Function<Font, Font> fontMapper) {
        // Ensure the font is a UIResource to guarantee it gets replaced when changing the LaF.
        return withDynamic(component, c -> c.setFont(makeUIResource(fontMapper.apply(c.getFont()))));
    }

    private static Font makeUIResource(final Font font) {
        if (font instanceof UIResource) return font;
        return new FontUIResource(font);
    }

    private static void updateComponent(JComponent component) {
        synchronized (listeners) {
            List<Consumer<JComponent>> list = listeners.get(component);
            if (list == null) {
                return;
            }
            for (Consumer<JComponent> action : list) {
                action.accept(component);
            }
        }
    }
}
