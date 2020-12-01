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
import com.github.mylibrelab.ui.layout.miglayout.MigLayoutBuilder
import java.awt.Container
import java.awt.event.ActionListener
import javax.swing.ButtonGroup
import javax.swing.JComponent

open class LayoutBuilder @PublishedApi internal constructor(@PublishedApi internal val builder: LayoutBuilderImpl) :
    RowBuilder by builder.rootRow {
    override fun withButtonGroup(title: Text?, buttonGroup: ButtonGroup, body: () -> Unit) {
        builder.withButtonGroup(buttonGroup, body)
    }

    inline fun buttonGroup(
        crossinline elementActionListener: () -> Unit,
        crossinline init: LayoutBuilder.() -> Unit
    ): ButtonGroup {
        val group = ButtonGroup()

        builder.withButtonGroup(group) {
            LayoutBuilder(builder).init()
        }

        val listener = ActionListener { elementActionListener() }
        for (button in group.elements) {
            button.addActionListener(listener)
        }
        return group
    }
}

@PublishedApi
internal interface LayoutBuilderImpl {
    val rootRow: Row
    fun withButtonGroup(buttonGroup: ButtonGroup, body: () -> Unit)

    fun build(container: Container, layoutConstraints: Array<out LCFlags>)

    val applyCallbacks: Map<JComponent?, List<() -> Unit>>
    val resetCallbacks: Map<JComponent?, List<() -> Unit>>
    val isModifiedCallbacks: Map<JComponent?, List<() -> Boolean>>
}

@PublishedApi
internal fun createLayoutBuilder(): LayoutBuilder {
    return LayoutBuilder(MigLayoutBuilder(createDefaultSpacingConfiguration()))
}

/**
 * Claims all available space in the container for the columns ([LCFlags.fillX], if `constraints` is
 * passed, `fillX` will be not applied - add it explicitly if need).
 * At least one component need to have a [Row.grow] constraint for it to fill the container.
 */
@JvmOverloads
fun panel(vararg constraints: LCFlags = emptyArray(), title: Text? = null, init: LayoutBuilder.() -> Unit): DialogPanel {
    val builder = createLayoutBuilder()
    builder.init()

    val panel = DialogPanel(title, layout = null)
    builder.builder.build(panel, constraints)
    initPanel(builder, panel)
    return panel
}

@PublishedApi
internal fun initPanel(builder: LayoutBuilder, panel: DialogPanel) {
    panel.applyCallbacks = builder.builder.applyCallbacks
    panel.resetCallbacks = builder.builder.resetCallbacks
    panel.isModifiedCallbacks = builder.builder.isModifiedCallbacks
}
