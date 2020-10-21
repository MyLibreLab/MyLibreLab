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

package com.github.mylibrelab.action;

import java.util.ArrayList;

/**
 * Mutable version of {@link ActionContext}. This should only be used internally. No
 * {@link AnAction} should assume that the context is mutable or if it is mutate it's state.
 */
public class MutableActionContext extends ActionContext {

    /**
     * Save a value for the given type.
     *
     * @param type the type class.
     * @param object the object to save.
     * @param <T> the type.
     */
    public <T> void put(final Class<T> type, final T object) {
        objectMap.computeIfAbsent(type, t -> new ArrayList<>()).add(object);
    }

    /**
     * Remove a value from the given type.
     *
     * @param type the type class.
     * @param object the object to remove.
     * @param <T> the type.
     */
    public <T> void remove(final Class<T> type, final T object) {
        var list = objectMap.get(type);
        if (list != null) list.remove(object);
    }
}
