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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

/**
 * The context passed to {@link AnAction}s when they are updated or executed. It provides available
 * model objects to perform the actions on.
 */
public class ActionContext {

    protected final Map<Class<?>, List<Object>> objectMap = new HashMap<>();

    /**
     * Get a value of given type.
     *
     * @param type the type class.
     * @param <T> the type.
     * @return the value if available.
     */
    @Nullable
    public <T> T get(@NotNull final Class<T> type) {
        var list = objectMap.get(type);
        if (list != null) {
            if (list.size() != 1) {
                Logger.warn("Multiple values available for type '" + type + "' but only one requested.");
            }
            return list.isEmpty() ? null : type.cast(list.get(0));
        }
        return null;
    }

    /**
     * Get all values of given type.
     *
     * @param type the type class.
     * @param <T> the type.
     * @return the value if available.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> List<T> getAll(@NotNull final Class<T> type) {
        return Collections.unmodifiableList((List<T>) objectMap.get(type));
    }
}
