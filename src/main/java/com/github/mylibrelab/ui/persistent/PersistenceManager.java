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

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;

import org.jetbrains.annotations.NotNull;


public enum PersistenceManager {
    INSTANCE;

    private final Set<PersistentComponent> persistentViews = Collections.newSetFromMap(new WeakHashMap<>());

    public void loadState(@NotNull final PersistenceNode node) {
        persistentViews.stream().filter(Objects::nonNull).filter(PersistentComponent::isPersistent).forEach(c -> {
            var state = node.getSubNode(c.getIdentifier());
            c.loadState(state);
        });
    }

    /**
     * Register a component to save its state.
     *
     * @param component the persistent component to save.
     */
    public void register(@NotNull final PersistentComponent component) {
        persistentViews.add(component);
    }

    /**
     * Stop a component from persisting its state.
     *
     * @param component the persistent component to remove.
     */
    public void unregister(@NotNull final PersistentComponent component) {
        persistentViews.remove(component);
    }

    /**
     * Save all states of components.
     */
    public PersistenceNode saveState() {
        PersistenceNode node = new PersistenceNode();
        persistentViews.stream().filter(Objects::nonNull).forEach(c -> node.insert(c.getIdentifier(), c.saveState()));
        return node;
    }
}
