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

import javax.swing.*;

import com.github.mylibrelab.annotations.ProviderFor;

/**
 * View specific refinement of the {@link ProviderFor} interface. The output type is always an
 * {@link ViewType ui component}.
 *
 * @param <ModelType> the model type.
 * @param <ViewType> the view type.
 */
public interface ViewOfTypeProviderFor<ModelType, ViewType extends JComponent>
        extends ProviderFor<ModelType, ViewType> {

    @Override
    default ViewType provide(final ModelType target) {
        return createView(target);
    }

    /**
     * Create a view for the given model.
     *
     * @param model the model.
     * @return the created view.
     */
    ViewType createView(final ModelType model);
}
