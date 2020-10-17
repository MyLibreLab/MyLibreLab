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

package com.github.mylibrelab.lifecycle;

import java.util.ServiceLoader;

import org.jetbrains.annotations.NotNull;

import com.github.mylibrelab.event.EventBus;
import com.github.mylibrelab.event.Topic;
import com.github.mylibrelab.event.Topics;

/**
 * Dispatcher to notify {@link AppLifecycleListener} about various stages of the application
 * lifecycle.
 */
public enum AppLifecycleManager {
    INSTANCE;

    public enum EventType {
        APP_STARTED, APP_FRAME_CREATED, APP_FRAME_OPENED, APP_FRAME_CLOSING, APP_STOPPING
    }

    @NotNull
    public static Topic<EventType, AppLifecycleListener> createTopic() {
        return Topic.create(AppLifecycleListener.class.getName(), (e, l) -> {
            switch (e) {
                case APP_STARTED:
                    l.applicationStarted();
                    break;
                case APP_FRAME_CREATED:
                    l.appFrameCreated();
                    break;
                case APP_FRAME_OPENED:
                    l.appFrameOpened();
                    break;
                case APP_FRAME_CLOSING:
                    l.appFrameClosing();
                    break;
                case APP_STOPPING:
                    l.applicationStopping();
                    break;
            }
        });
    }

    private final EventBus<EventType, AppLifecycleListener> eventBus;

    AppLifecycleManager() {
        eventBus = EventBus.get(Topics.APP_LIFECYCLE);
        ServiceLoader.load(AppLifecycleListener.class).forEach(eventBus::subscribe);
    }

    /**
     * Notify listeners that the application has started.
     */
    public void notifyApplicationStarted() {
        eventBus.post(EventType.APP_STARTED, true);
    }

    /**
     * Notify listeners that the application frame has been created.
     */
    public void notifyAppFrameCreated() {
        eventBus.post(EventType.APP_FRAME_CREATED, true);
    }

    /**
     * Notify listeners that the application frame has been opened.
     */
    public void notifyAppFrameOpened() {
        eventBus.post(EventType.APP_FRAME_OPENED, true);
    }

    /**
     * Notify listeners that the application frame is closing.
     */
    public void notifyAppFrameClosing() {
        eventBus.post(EventType.APP_FRAME_CLOSING, true);
    }

    /**
     * Notify listeners that the application is stopping.
     */
    public void notifyApplicationStopping() {
        eventBus.post(EventType.APP_STOPPING, true);
    }
}
