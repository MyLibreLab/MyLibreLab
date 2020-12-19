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

/**
 * Dispatcher for a {@link Topic}. The dispatcher converts the given message to the contract of the
 * {@link ListenerType}.
 *
 * @param <MessageType> the message type.
 * @param <ListenerType> the listener type.
 */
public interface Dispatcher<MessageType, ListenerType> {

    /**
     * Dispatch the given message to the listener.
     *
     * @param message the message.
     * @param listener the listener.
     */
    void dispatch(final MessageType message, final ListenerType listener);

    /**
     * Indicates whether this dispatcher consumes it's listener or uses an internal dispatching
     * mechanism.
     *
     * @return true if the listener is consumed.
     */
    default boolean consumeListeners() {
        return true;
    }
}
