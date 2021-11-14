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

package com.github.mylibrelab.ui.view;

import javax.swing.JComponent;

import org.jetbrains.annotations.NotNull;

import com.github.mylibrelab.service.ServiceManager;

/**
 * Factory for creating views for domain model objects.
 */
public class ViewFactory {

    private ViewFactory() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Create the view for the given model.
     *
     * Views are provided by classes annotated with {@link ViewProvider} and which implement
     * {@link ViewProviderFor}.
     *
     * @param model the model object.
     * @param <T> the model type.
     * @return the created view.
     */
    @NotNull
    public static <T> JComponent createView(@NotNull final T model) {
        return createView(model, JComponent.class);
    }

    /**
     * Create the view for the given model.
     *
     * Views are provided by classes annotated with {@link ViewProvider} and which implement
     * {@link ViewProviderFor}.
     *
     * @param model the model object.
     * @param <T> the model type.
     * @param <V> the view type.
     * @return the created view.
     */
    @NotNull
    public static <T, V extends JComponent> V createView(@NotNull final T model, @NotNull Class<V> viewType) {
        return ServiceManager.getProvidedServiceFor(viewType, model);
    }
}
