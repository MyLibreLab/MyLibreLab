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

public interface AppLifecycleListener {

    /**
     * Called after the application has been started. At this point the app frame hasn't been created.
     */
    void applicationStarted();

    /**
     * Called after the application frame has been created.
     */
    void appFrameCreated();

    /**
     * Called after the application frame has been made visible.
     */
    void appFrameOpened();

    /**
     * Called after the application frame has been disposed.
     */
    void appFrameClosing();

    /**
     * Called before the application is stopped.
     */
    void applicationStopping();
}
