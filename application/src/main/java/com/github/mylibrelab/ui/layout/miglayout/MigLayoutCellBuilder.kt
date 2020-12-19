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

// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.github.mylibrelab.ui.layout.miglayout

import com.github.mylibrelab.settings.api.Condition
import com.github.mylibrelab.settings.api.registerListener
import com.github.mylibrelab.text.Text
import com.github.mylibrelab.ui.layout.*
import com.github.mylibrelab.ui.layout.CCFlags
import com.github.mylibrelab.ui.layout.CheckboxCellBuilder
import com.github.mylibrelab.ui.layout.ScrollPaneCellBuilder
import javax.swing.JComponent

internal class MigLayoutCellBuilder<T : JComponent>(
    private val builder: MigLayoutBuilder,
    private val row: MigLayoutRow,
    override val component: T
) : CellBuilder<T>, CheckboxCellBuilder, ScrollPaneCellBuilder {
    private var applyIfEnabled = false

    override fun comment(text: Text, maxLineLength: Int, forComponent: Boolean): CellBuilder<T> {
        row.addCommentRow(text, maxLineLength, forComponent)
        return this
    }

    override fun commentComponent(component: JComponent, forComponent: Boolean): CellBuilder<T> {
        row.addCommentRow(component, forComponent)
        return this
    }

    override fun onApply(callback: () -> Unit): CellBuilder<T> {
        builder.applyCallbacks.getOrPut(component, { mutableListOf() }).add(callback)
        return this
    }

    override fun onReset(callback: () -> Unit): CellBuilder<T> {
        builder.resetCallbacks.getOrPut(component, { mutableListOf() }).add(callback)
        return this
    }

    override fun onIsModified(callback: () -> Boolean): CellBuilder<T> {
        builder.isModifiedCallbacks.getOrPut(component, { mutableListOf() }).add(callback)
        return this
    }

    override fun enabled(isEnabled: Boolean) {
        component.isEnabled = isEnabled
    }

    override fun visible(isVisible: Boolean) {
        component.isVisible = isVisible
    }

    override fun enableIf(predicate: Condition): CellBuilder<T> {
        component.isEnabled = predicate()
        predicate.registerListener(Condition::value) { _, it -> component.isEnabled = it }
        return this
    }

    override fun visibleIf(predicate: Condition): CellBuilder<T> {
        component.isVisible = predicate()
        predicate.registerListener(Condition::value) { _, it -> component.isVisible = it }
        return this
    }

    override fun shouldSaveOnApply(): Boolean {
        return !(applyIfEnabled && !component.isEnabled)
    }

    override fun actsAsLabel() {
        builder.updateComponentConstraints(component) { spanX = 1 }
    }

    override fun noGrowY() {
        builder.updateComponentConstraints(component) {
            growY(0.0f)
            pushY(0.0f)
        }
    }

    override fun sizeGroup(name: String): MigLayoutCellBuilder<T> {
        builder.updateComponentConstraints(component) {
            sizeGroup(name)
        }
        return this
    }

    override fun growPolicy(growPolicy: GrowPolicy): CellBuilder<T> {
        builder.updateComponentConstraints(component) {
            builder.defaultComponentConstraintCreator.applyGrowPolicy(this, growPolicy)
        }
        return this
    }

    override fun constraints(vararg constraints: CCFlags): CellBuilder<T> {
        builder.updateComponentConstraints(component) {
            overrideFlags(this, constraints)
        }
        return this
    }

    override fun withLargeLeftGap(): CellBuilder<T> {
        builder.updateComponentConstraints(component) {
            horizontal.gapBefore = gapToBoundSize(builder.spacing.largeHorizontalGap, true)
        }
        return this
    }

    override fun withLeftGap(): CellBuilder<T> {
        builder.updateComponentConstraints(component) {
            horizontal.gapBefore = gapToBoundSize(builder.spacing.horizontalGap, true)
        }
        return this
    }
}
