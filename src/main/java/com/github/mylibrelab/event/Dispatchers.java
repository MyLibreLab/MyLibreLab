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

package com.github.mylibrelab.event;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

/**
 * Utility class for creating dispatchers.
 */
public class Dispatchers {

    private Dispatchers() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Creates a {@link Dispatcher} which simply forwards the message to the listener.
     *
     * @param <T> the message type.
     * @return the {@link Dispatcher}.
     */
    @NotNull
    public static <T> Dispatcher<T, Consumer<T>> forwardingDispatcher() {
        return (o, c) -> c.accept(o);
    }

    /**
     * Creates a {@link Dispatcher} which using the given dispatch function.
     *
     * @param <T> the message type.
     * @param <L> the listener type
     * @param dispatchFunction the dispatch function.
     * @return the {@link Dispatcher}.
     */
    public static <T, L> Dispatcher<T, L> create(final BiConsumer<T, L> dispatchFunction) {
        return dispatchFunction::accept;
    }

    /**
     * Creates a new {@link Dispatcher} which does not consume the given listeners. The used listener
     * type is {@link Void} to prevent listeners from registering to the events.
     *
     * @param dispatchFunction the dispatcher function.
     * @param <T> the message type.
     * @return the {@link Dispatcher}
     */
    public static <T> Dispatcher<T, Void> createVoidDispatcher(final Consumer<T> dispatchFunction) {
        return new Dispatcher<>() {
            @Override
            public void dispatch(final T message, final Void listener) {
                dispatchFunction.accept(message);
            }

            @Override
            public boolean consumeListeners() {
                return false;
            }
        };
    }
}
