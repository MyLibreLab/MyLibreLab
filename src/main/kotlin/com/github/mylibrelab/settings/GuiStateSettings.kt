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
import com.github.mylibrelab.settings.api.*
import com.github.mylibrelab.ui.persistent.PersistenceManager
import com.google.auto.service.AutoService

@AutoService(AppLifecycleListener::class)
internal class GuiStateAppLifecycleListener : AppLifecycleAdapter() {

    override fun appFrameCreated() {
        PersistenceManager.INSTANCE.restoreState()
    }

    override fun appFrameClosing() {
        GuiStateSettings.state = PersistenceManager.INSTANCE.saveState()
    }
}

@AutoService(SettingsContainerProvider::class)
class GuiStateSettingsProvider : SingletonSettingsContainerProvider({ GuiStateSettings })

object GuiStateSettings : DefaultSettingsContainer(identifier = "gui_state") {

    internal var state: MutableMap<String, String> = mutableMapOf()

    init {
        hidden {
            persistentProperty(value = ::state, transformer = propertyParser())
        }
    }

    override fun onSettingsUpdate(loaded: Boolean) {
        if (loaded) {
            // If another instance of the application is closed we don't want to overwrite the current gui layout.
            PersistenceManager.INSTANCE.loadState(state)
        }
    }
}
