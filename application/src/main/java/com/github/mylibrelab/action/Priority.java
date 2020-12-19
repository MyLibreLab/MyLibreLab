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

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

/**
 * Represents the priority of an {@link AnAction}. This value helps to order actions inside an
 * {@link AnActionGroup}.
 */
public class Priority implements Comparable<Priority> {

    private final int level;

    public static final @NotNull Priority HIGHEST = withLevel(Integer.MAX_VALUE);
    public static final @NotNull Priority HIGH = withLevel(100);
    public static final @NotNull Priority DEFAULT = withLevel(0);
    public static final @NotNull Priority LOW = withLevel(-100);
    public static final @NotNull Priority LOWEST = withLevel(Integer.MIN_VALUE);

    /**
     * Create a new {@link Priority} with the given priority level.
     *
     * @param level the priority level.
     * @return the {@link Priority}.
     */
    public static Priority withLevel(final int level) {
        return new Priority(level);
    }

    private Priority(final int level) {
        this.level = level;
    }

    /**
     * Get the priority level.
     *
     * @return the priority level.
     */
    public int getLevel() {
        return level;
    }

    @Override
    public int compareTo(@NotNull Priority o) {
        return Integer.compare(this.level, o.level);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Priority priority1 = (Priority) o;
        return level == priority1.level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(level);
    }
}
