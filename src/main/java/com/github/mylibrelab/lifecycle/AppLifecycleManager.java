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

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Consumer;

public enum AppLifecycleManager {
    INSTANCE;

    private final List<AppLifecycleListener> listeners = new ArrayList<>();

    AppLifecycleManager() {
        ServiceLoader.load(AppLifecycleListener.class).forEach(listeners::add);
    }

    public void notifyApplicationStarted() {
        notify(AppLifecycleListener::applicationStarted);
    }

    public void notifyAppFrameCreated() {
        notify(AppLifecycleListener::appFrameCreated);
    }

    public void notifyAppFrameShown() {
        notify(AppLifecycleListener::appFrameOpened);
    }

    public void notifyAppFrameClosing() {
        notify(AppLifecycleListener::appFrameClosing);
    }

    public void notifyApplicationStopping() {
        notify(AppLifecycleListener::applicationStopping);
    }

    private void notify(final Consumer<AppLifecycleListener> callback) {
        listeners.forEach(callback);
    }
}
