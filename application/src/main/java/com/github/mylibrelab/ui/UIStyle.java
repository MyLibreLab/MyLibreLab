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
import java.util.function.UnaryOperator;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.UIResource;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.mylibrelab.text.Text;
import com.github.mylibrelab.ui.border.Borders;

public class UIStyle {

    private static final Map<Component, List<Consumer<Component>>> listeners = new WeakHashMap<>();

    static {
        UIManager.addPropertyChangeListener(e -> {
            String key = e.getPropertyName();
            if ("lookAndFeel".equals(key)) {
                listeners.keySet().forEach(UIStyle::updateComponent);
            }
        });
    }

    private UIStyle() {
        throw new IllegalStateException("Utility class");
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
    @NotNull
    public static <T extends Component> T withDynamic(@NotNull final T component,
            @NotNull final Consumer<T> onUpdateUi) {
        // Explicit component update is required since the component already exists
        // and we can't want to wait for the next LaF change
        onUpdateUi.accept(component);
        synchronized (listeners) {
            listeners.compute(component, (k, v) -> {
                if (v == null) {
                    // noinspection unchecked
                    return Collections.singletonList((Consumer<Component>) onUpdateUi);
                }
                List<Consumer<Component>> res = v.size() == 1 ? new ArrayList<>(v) : v;
                // noinspection unchecked
                res.add((Consumer<Component>) onUpdateUi);
                return res;
            });
        }
        return component;
    }

    @NotNull
    public static <T extends Component> T withMonospacedFont(@NotNull final T component) {
        return withFontFamily(component, Font.MONOSPACED);
    }

    @NotNull
    public static <T extends Component> T withFontFamily(@NotNull final T component, @NotNull final String fontName) {
        return withTransformedFont(component, f -> {
            var attrs = f.getAttributes();
            // noinspection unchecked
            ((Map<TextAttribute, String>) attrs).put(TextAttribute.FAMILY, fontName);
            return f.deriveFont(attrs);
        });
    }

    @NotNull
    public static <T extends Component> T withBoldFont(@NotNull final T component) {
        return withTransformedFont(component, f -> f.deriveFont(Font.BOLD));
    }

    @NotNull
    public static <T extends Component> T withTransformedFont(@NotNull final T component,
            final @NotNull UnaryOperator<Font> fontMapper) {
        // Ensure the font is a UIResource to guarantee it gets replaced when changing the LaF.
        return withDynamic(component, c -> c.setFont(makeUIResource(fontMapper.apply(c.getFont()))));
    }

    @NotNull
    public static <T extends AbstractButton> T withText(@NotNull final T component, @NotNull final Text text) {
        return withDynamic(component, c -> c.setText(text.getText()));
    }

    @NotNull
    public static <T extends JComponent> T withTooltipText(@NotNull final T component, @NotNull final Text text) {
        return withDynamic(component, c -> c.setToolTipText(text.getText()));
    }

    @NotNull
    public static <T extends Frame> T withTitle(@NotNull final T component, @NotNull final Text text) {
        return withDynamic(component, c -> c.setTitle(text.getText()));
    }

    @NotNull
    public static <T extends Dialog> T withTitle(@NotNull final T component, @NotNull final Text text) {
        return withDynamic(component, c -> c.setTitle(text.getText()));
    }

    private static Font makeUIResource(@NotNull final Font font) {
        if (font instanceof UIResource) return font;
        return new FontUIResource(font);
    }

    private static void updateComponent(@NotNull final Component component) {
        synchronized (listeners) {
            List<Consumer<Component>> list = listeners.get(component);
            if (list == null) {
                return;
            }
            for (var action : list) {
                action.accept(component);
            }
        }
    }

    @NotNull
    public static <T extends JComponent> T withTitledBorder(@NotNull final T comp, @Nullable final Text title) {
        return withTitledBorder(comp, title, null);
    }

    @NotNull
    public static <T extends JComponent> T withTitledBorder(@NotNull final T comp, @Nullable final Text title,
            @Nullable final Border border) {
        return withDynamic(comp, c -> {
            if (title != null) {
                String text = title.getText();
                if (!text.isEmpty()) {
                    c.setBorder(Borders.createTitledBorder(border, text));
                    return;
                }
            }
            c.setBorder(Borders.createTitledBorder(Borders.topLineBorder()));
        });
    }
}
