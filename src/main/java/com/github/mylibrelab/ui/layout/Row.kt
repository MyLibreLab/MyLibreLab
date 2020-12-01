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
import javax.swing.AbstractButton

abstract class Row : Cell(), RowBuilder {
    abstract var enabled: Boolean
    abstract var visible: Boolean

    abstract var subRowsEnabled: Boolean
    abstract var subRowsVisible: Boolean

    /**
     * Indent for child rows of this row, expressed in steps (multiples of [SpacingConfiguration.indentLevel]). Replaces indent
     * calculated from row nesting.
     */
    abstract var subRowIndentationLevel: Int

    internal abstract val builder: LayoutBuilderImpl

    /**
     * Specifies the right alignment for the component if the cell is larger than the component plus its gaps.
     */
    fun right(init: Row.() -> Unit) {
        alignRight()
        init()
    }

    @PublishedApi
    internal abstract fun alignRight()

    abstract fun largeGapAfter()

    @PublishedApi
    internal abstract fun createRow(label: Text?): Row

    fun attachSubRowsEnabled(component: AbstractButton) {
        subRowsEnabled = component.isSelected
        component.addChangeListener {
            subRowsEnabled = component.isSelected
        }
    }
}
