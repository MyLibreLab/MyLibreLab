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

package com.github.mylibrelab.ui.persistent;

import org.jetbrains.annotations.NotNull;

/**
 * Object that can be persisted across sessions.
 *
 * @author Jannis Weis
 * @since 2020
 */
public interface PersistentComponent {

    /**
     * Create the persistence node of the object as a snapshot.
     *
     * @return the persistence node.
     */
    @NotNull
    PersistenceNode saveState();

    /**
     * Load the state from given persistence info.
     *
     * @param node the node to load.
     */
    void loadState(@NotNull final PersistenceNode node);

    /**
     * Get the identifier for the object.
     *
     * @return the identifier.
     */
    @NotNull
    String getIdentifier();

    /**
     * Returns whether the given object is enabled for persisting.
     *
     * @return true if enabled.
     */
    boolean isPersistent();

    /**
     * Set whether the object should be persisted.
     *
     * @param persistent true if it should be persisted.
     * @param identifier the identifier to use.
     */
    void setPersistent(final boolean persistent, @NotNull final String identifier);
}
