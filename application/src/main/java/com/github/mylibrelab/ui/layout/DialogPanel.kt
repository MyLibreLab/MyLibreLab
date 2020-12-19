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

// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.github.mylibrelab.ui.layout

import com.github.mylibrelab.text.Text
import java.awt.BorderLayout
import java.awt.LayoutManager
import javax.swing.JComponent
import javax.swing.JPanel

class DialogPanel(val title: Text? = null, layout: LayoutManager? = BorderLayout()) : JPanel(layout) {

    companion object {
        internal const val DIALOG_CONTENT_PANEL_PROPERTY = "dialogContentPanel"
    }

    init {
        putClientProperty(DIALOG_CONTENT_PANEL_PROPERTY, true)
    }

    var applyCallbacks: Map<JComponent?, List<() -> Unit>> = emptyMap()
    var resetCallbacks: Map<JComponent?, List<() -> Unit>> = emptyMap()
    var isModifiedCallbacks: Map<JComponent?, List<() -> Boolean>> = emptyMap()

    fun apply() {
        for ((component, callbacks) in applyCallbacks.entries) {
            if (component == null) continue

            val modifiedCallbacks = isModifiedCallbacks[component]
            if (modifiedCallbacks.isNullOrEmpty() || modifiedCallbacks.any { it() }) {
                callbacks.forEach { it() }
            }
        }
        applyCallbacks[null]?.forEach { it() }
    }

    fun reset() {
        for ((component, callbacks) in resetCallbacks.entries) {
            if (component == null) continue

            callbacks.forEach { it() }
        }
        resetCallbacks[null]?.forEach { it() }
    }

    fun isModified(): Boolean {
        return isModifiedCallbacks.values.any { list -> list.any { it() } }
    }
}
