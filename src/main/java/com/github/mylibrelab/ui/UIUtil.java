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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

public class UIUtil {

    private UIUtil() {}

    /**
     * Binds a given action to the component.
     *
     * @param component The component.
     * @param condition The condition for the InputMap. One of WHEN_IN_FOCUSED_WINDOW, WHEN_FOCUSED, *
     *        WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
     * @param actionName the name of the action.
     * @param keyStroke the key stroke associated with the action.
     * @param action the action to run.
     */
    public static void bindAction(@Nullable final JComponent component, final int condition,
            @NotNull final String actionName, @NotNull final KeyStroke keyStroke,
            @Nullable final ActionListener action) {
        if (component == null || action == null) return;
        bindAction(component.getInputMap(condition), component.getActionMap(), actionName, keyStroke, action);
    }

    /**
     * Binds a given action to the given Input- and Action-maps.
     *
     * @param inputMap the input map.
     * @param actionMap the action map.
     * @param name the name of the action.
     * @param keyStroke the key stroke associated with the action.
     * @param action the action to run.
     */
    public static void bindAction(@NotNull final InputMap inputMap, @NotNull final ActionMap actionMap,
            @NotNull final String name, @NotNull final KeyStroke keyStroke, @NotNull final ActionListener action) {
        inputMap.put(keyStroke, name);
        actionMap.put(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.actionPerformed(e);
            }
        });
    }

    /**
     * Runs the given function on the EventThread and waits for the value to be retrieved.
     *
     * @param supplier the function to execute.
     * @param <T> the return value type.
     * @return The result of the executed function.
     */
    public static <T> T runAndWait(@NotNull final Supplier<T> supplier) {
        if (EventQueue.isDispatchThread()) {
            return supplier.get();
        } else {
            AtomicReference<T> value = new AtomicReference<>();
            try {
                SwingUtilities.invokeAndWait(() -> value.set(supplier.get()));
            } catch (InvocationTargetException e) {
                Logger.error(e);
                value.set(null);
            } catch (InterruptedException e) {
                Logger.error(e);
                Thread.currentThread().interrupt();
            }
            return value.get();
        }
    }

    /**
     * Runs the given action on the EventThread and waits for the value to be retrieved.
     *
     * @param action the action to execute.
     */
    public static void runAndWait(@NotNull final Runnable action) {
        if (EventQueue.isDispatchThread()) {
            action.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(action);
            } catch (InterruptedException | InvocationTargetException e) {
                Logger.error(e);
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Forces the component to display it's tooltip.
     * Note: If this method is invoked while the mouse is outside of the component, the tooltip
     * will disappear as soon as the mouse is moved.
     *
     * @param component the component.
     */
    public static void showTooltip(@NotNull final JComponent component) {
        component.dispatchEvent(new KeyEvent(component, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), InputEvent.CTRL_DOWN_MASK,
                KeyEvent.VK_F1, KeyEvent.CHAR_UNDEFINED));
    }
}
