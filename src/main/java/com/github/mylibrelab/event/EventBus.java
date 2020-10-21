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

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

/**
 * The {@link EventBus} is responsible for application messaging. It is specified by a MessageType
 * and a ListenerType which correspond to a given {@link Topic}.
 *
 * @param <MessageType> the type of the messages.
 * @param <ListenerType> the type of the listeners.
 */
public class EventBus<MessageType, ListenerType> {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final List<Object> references = new ArrayList<>();
    private static final Map<Topic<?, ?>, EventBus<?, ?>> eventBusMap = new HashMap<>();

    private final Topic<MessageType, ListenerType> topic;
    private final Set<ListenerType> listeners = Collections.newSetFromMap(new WeakHashMap<>());

    private EventBus(@NotNull final Topic<MessageType, ListenerType> topic) {
        this.topic = topic;
    }

    /**
     * Returns the {@link Topic} corresponding to this {@link EventBus}.
     *
     * @return the {@link Topic}
     */
    @NotNull
    public Topic<MessageType, ListenerType> getTopic() {
        return topic;
    }

    /**
     * Returns the default {@link EventBus} corresponding to {@code Topics.DEFAULT}. This event bus can
     * be used to dispatch tasks which need to be run on the dispatch thread.
     *
     * @return the default {@link EventBus}.
     */
    @NotNull
    public static EventBus<Runnable, Void> get() {
        return get(Topics.DEFAULT);
    }

    /**
     * Returns the {@link EventBus} for the given {@link Topic}.
     *
     * @param topic the {@link Topic}.
     * @param <T> the message type.
     * @param <K> the listener type.
     * @return the {@link EventBus}.
     */
    @NotNull
    public static <T, K> EventBus<T, K> get(@NotNull final Topic<T, K> topic) {
        // noinspection unchecked
        return (EventBus<T, K>) eventBusMap.computeIfAbsent(topic, EventBus::new);
    }

    /**
     * Post a new message to the subscribers.
     *
     * @param message the message.
     */
    public void post(final @Nullable MessageType message) {
        post(message, false);
    }

    /**
     * Post a new message to the subscribers.
     *
     * @param message the message.
     * @param waitForCompletion whether the current thread should block until the task is completed.
     */
    public void post(final @Nullable MessageType message, final boolean waitForCompletion) {
        DispatchThread.INSTANCE.execute(() -> {
            var dispatcher = topic.getDispatcher();
            if (!dispatcher.consumeListeners()) {
                dispatcher.dispatch(message, null);
                return;
            }
            synchronized (listeners) {
                listeners.forEach(l -> dispatcher.dispatch(message, l));
            }
        }, waitForCompletion);
    }

    /**
     * Subscribe to this {@link EventBus}.
     *
     * @param listener the listener to subscribe.
     */
    public final void subscribe(@NotNull final ListenerType listener) {
        subscribe(listener, false);
    }

    /**
     * Subscribe to this {@link EventBus}.
     *
     * @param listener the listener.
     * @param autoDispose whether the listener should be automatically removed if it is no longer
     *        referenced anywhere.
     */
    public final void subscribe(@NotNull final ListenerType listener, final boolean autoDispose) {
        synchronized (listeners) {
            listeners.add(listener);
            if (!autoDispose) {
                references.add(listener);
            }
        }
    }

    /**
     * Unsubscribe from this {@link EventBus}.
     *
     * @param listener the listener to unsubscribe.
     */
    public final void unsubscribe(@Nullable final ListenerType listener) {
        synchronized (listeners) {
            listeners.remove(listener);
            references.remove(listener);
        }
    }

    /**
     * Shuts down the event bus, completing any outstanding tasks first.
     */
    public static void shutDown() throws InterruptedException {
        DispatchThread.INSTANCE.shutDown();
    }

    private enum DispatchThread {
        INSTANCE;

        private final ExecutorService executor;

        DispatchThread() {
            executor = Executors.newSingleThreadExecutor();
        }

        private void execute(final Runnable task, final boolean waitForCompletion) {
            if (executor.isShutdown()) {
                Logger.warn("Tasks submitted after shutdown.");
                return;
            }
            var future = executor.submit(task);
            if (waitForCompletion) {
                try {
                    future.get();
                } catch (InterruptedException e) {
                    Logger.error(e);
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    Logger.error(e);
                }
            }
        }

        private void shutDown() throws InterruptedException {
            Logger.info("Shutting down event thread.");
            executor.shutdown();
            executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
            Logger.info("Event thread shutdown.");
        }
    }
}
