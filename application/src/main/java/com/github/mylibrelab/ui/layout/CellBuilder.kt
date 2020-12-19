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

import com.github.mylibrelab.settings.api.Condition
import com.github.mylibrelab.text.Text
import javax.swing.AbstractButton
import javax.swing.JComponent
import javax.swing.JTextField

@DslMarker
annotation class CellMarker

interface CellBuilder<out T : JComponent> {
    val component: T

    fun onApply(callback: () -> Unit): CellBuilder<T>
    fun onReset(callback: () -> Unit): CellBuilder<T>
    fun onIsModified(callback: () -> Boolean): CellBuilder<T>

    fun shouldSaveOnApply(): Boolean

    fun <V> withBinding(
        componentGet: (T) -> V,
        componentSet: (T, V) -> Unit,
        modelBinding: PropertyBinding<V>
    ): CellBuilder<T> {
        onApply { if (shouldSaveOnApply()) modelBinding.set(componentGet(component)) }
        onReset { componentSet(component, modelBinding.get()) }
        onIsModified { shouldSaveOnApply() && componentGet(component) != modelBinding.get() }
        return this
    }

    fun comment(
        text: Text,
        maxLineLength: Int = 70,
        forComponent: Boolean = false
    ): CellBuilder<T>

    fun commentComponent(component: JComponent, forComponent: Boolean = false): CellBuilder<T>

    fun visible(isVisible: Boolean)
    fun visibleIf(predicate: Condition): CellBuilder<T>

    fun enabled(isEnabled: Boolean)
    fun enableIf(predicate: Condition): CellBuilder<T>

    fun withLargeLeftGap(): CellBuilder<T>
    fun withLeftGap(): CellBuilder<T>

    /**
     * All components of the same group share will get the same BoundSize (min/preferred/max),
     * which is that of the biggest component in the group
     */
    fun sizeGroup(name: String): CellBuilder<T>
    fun growPolicy(growPolicy: GrowPolicy): CellBuilder<T>
    fun constraints(vararg constraints: CCFlags): CellBuilder<T>

    fun applyToComponent(task: T.() -> Unit): CellBuilder<T> {
        return also { task(component) }
    }
}

internal interface ScrollPaneCellBuilder {
    fun noGrowY()
}

internal interface CheckboxCellBuilder {
    fun actsAsLabel()
}

fun <T : JTextField> CellBuilder<T>.withTextBinding(modelBinding: PropertyBinding<String>): CellBuilder<T> {
    return withBinding(JTextField::getText, JTextField::setText, modelBinding)
}

fun <T : AbstractButton> CellBuilder<T>.withSelectedBinding(modelBinding: PropertyBinding<Boolean>): CellBuilder<T> {
    return withBinding(AbstractButton::isSelected, AbstractButton::setSelected, modelBinding)
}
