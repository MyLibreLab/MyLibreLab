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

import org.jetbrains.annotations.NotNull;

/**
 * Topics are used to define a messaging contract for an {@link EventBus}.
 *
 * It specifies a message type a listener type and a {@link Dispatcher} which dispatches the
 * messages to the listeners.
 *
 * @param <MessageType> the message type.
 * @param <ListenerType> the listener type.
 */
public final class Topic<MessageType, ListenerType> {

    private final Dispatcher<MessageType, ListenerType> dispatcher;
    private final String name;

    /**
     * Create a new Topic with the given name and {@link Dispatcher}.
     *
     * @param name the name of the topic.
     * @param dispatcher the {@link Dispatcher}.
     * @param <T> the message type.
     * @param <L> the listener type.
     * @return the {@link Topic}.
     */
    public static <T, L> Topic<T, L> create(@NotNull final String name, @NotNull final Dispatcher<T, L> dispatcher) {
        return new Topic<>(name, dispatcher);
    }

    private Topic(@NotNull final String name, @NotNull final Dispatcher<MessageType, ListenerType> dispatcher) {
        this.dispatcher = dispatcher;
        this.name = name;
    }

    /**
     * Returns a human readable name for this topic. This value should only be used for debugging and
     * logging purposes.
     *
     * @return the name of the {@link Topic}.
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Get the {@link Dispatcher} for this topic.
     *
     * @return the {@link Dispatcher}
     */
    public Dispatcher<MessageType, ListenerType> getDispatcher() {
        return dispatcher;
    }

}
