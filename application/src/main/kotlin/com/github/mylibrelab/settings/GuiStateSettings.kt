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

import com.github.mylibrelab.annotations.Service
import com.github.mylibrelab.lifecycle.AppLifecycleAdapter
import com.github.mylibrelab.lifecycle.AppLifecycleListener
import com.github.mylibrelab.settings.api.*
import com.github.mylibrelab.ui.persistent.PersistenceManager
import com.github.mylibrelab.ui.persistent.PersistenceNode
import com.github.mylibrelab.util.andThen
import com.github.mylibrelab.util.propertyParser
import com.github.mylibrelab.util.transformerOf

@Service(AppLifecycleListener::class)
internal class GuiStateAppLifecycleListener : AppLifecycleAdapter() {

    override fun appFrameCreated() {
        PersistenceManager.INSTANCE.loadState(GuiStateSettings.state)
    }

    override fun appFrameClosing() {
        GuiStateSettings.state = PersistenceManager.INSTANCE.saveState()
    }
}

@SettingsProvider
class GuiStateSettingsProvider : SingletonSettingsContainerProvider({ GuiStateSettings })

object GuiStateSettings : DefaultSettingsContainer(identifier = "gui_state") {

    internal var state: PersistenceNode = PersistenceNode()

    init {
        hidden {
            val stateTransformer = transformerOf(
                write = PersistenceNode::fromFlattenedMap,
                read = PersistenceNode::toFlattenedMap,
            ) andThen propertyParser()
            persistentProperty(value = ::state, transformer = stateTransformer)
        }
    }
}
