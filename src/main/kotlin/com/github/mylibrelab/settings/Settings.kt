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

package com.github.mylibrelab.settings

import com.github.mylibrelab.lifecycle.AppLifecycleAdapter
import com.github.mylibrelab.lifecycle.AppLifecycleListener
import com.github.mylibrelab.settings.api.SettingsStorage
import com.google.auto.service.AutoService

@AutoService(AppLifecycleListener::class)
internal class SettingsAppLifecycleListener : AppLifecycleAdapter() {

    override fun applicationStarted() {
        Settings.reset()
        Settings.loadState()
    }

    override fun applicationStopping() = Settings.saveState()
}

object Settings {

    /**
     * Load the settings from storage. Automatically populates any active SettingsContainer.
     */
    @JvmStatic
    fun loadState() = SettingsStorage.loadState()

    /**
     * Save the settings to the storage.
     */
    @JvmStatic
    fun saveState() = SettingsStorage.saveState()

    /**
     * Reset all settings. This will not restore the default values of the current session and has no effect
     * until the application has restarted.
     */
    @JvmStatic
    fun reset() = SettingsStorage.reset()
}
