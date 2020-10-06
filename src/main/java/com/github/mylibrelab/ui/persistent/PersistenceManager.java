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

import java.util.*;

import javax.swing.*;


public enum PersistenceManager {
    INSTANCE;

    private final Set<PersistentComponent> persistentViews = Collections.newSetFromMap(new WeakHashMap<>());
    private final PersistenceInfo stateInfo = new PersistenceInfo();

    public void loadState(final Map<String, String> state) {
        for (var entry : state.entrySet()) {
            stateInfo.putValue(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Register a component to save its state.
     *
     * @param component the persistent component to save.
     */
    public void register(final PersistentComponent component) {
        persistentViews.add(component);
    }

    /**
     * Stop a component from persisting its state.
     *
     * @param component the persistent component to remove.
     */
    public void unregister(final PersistentComponent component) {
        persistentViews.remove(component);
    }

    /**
     * Save all states of components.
     */
    public Map<String, String> saveState() {
        persistentViews.stream().filter(Objects::nonNull).forEach(c -> {
            var info = c.saveState();
            stateInfo.merge(info, c.getIdentifier());
        });
        return new HashMap<>(stateInfo.directMap());
    }

    /**
     * Restore all states of components.
     */
    public void restoreState() {
        SwingUtilities.invokeLater(() -> {
            persistentViews.stream().filter(Objects::nonNull).filter(PersistentComponent::isPersistent).forEach(c -> {
                var state = getState(c);
                c.loadState(state);
            });
        });
    }

    /**
     * Get the state for a specific component.
     *
     * @param component the persistent component.
     * @return the persistence info for the component.
     */
    public PersistenceInfo getState(final PersistentComponent component) {
        return stateInfo.getSubTree(component.getIdentifier());
    }
}
