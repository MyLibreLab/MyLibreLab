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
import com.github.mylibrelab.text.component.TextLabel
import javax.swing.JComponent
import javax.swing.JLabel

interface RowBuilder : BaseBuilder {

    fun visible(isVisible: Boolean)
    fun visibleIf(predicate: Condition): Row

    fun enabled(isEnabled: Boolean)
    fun enableIf(predicate: Condition): Row

    // JvmOverload
    fun row(init: Row.() -> Unit): Row = row(null as JLabel?, false, init)

    // JvmOverload
    fun row(label: Text?, init: Row.() -> Unit): Row = row(label, false, init)

    fun row(label: JLabel? = null, separated: Boolean = false, init: Row.() -> Unit): Row {
        return createChildRow(label = label, isSeparated = separated).apply(init)
    }

    fun row(label: Text?, separated: Boolean = false, init: Row.() -> Unit): Row {
        return createChildRow(label?.let { TextLabel(it) }, isSeparated = separated).apply(init)
    }

    fun createChildRow(
        label: JLabel? = null,
        isSeparated: Boolean = false,
        noGrid: Boolean = false,
        title: Text? = null
    ): Row

    fun titledRow(title: Text, init: Row.() -> Unit): Row
    /**
     * Creates row with a huge gap after it, that can be used to group related components.
     * Think of [titledRow] without a title and additional indent.
     */
    fun blockRow(init: Row.() -> Unit): Row

    fun createCommentRow(component: JComponent): Row
    fun commentRow(text: Text)

    fun onGlobalApply(callback: () -> Unit): Row
    fun onGlobalReset(callback: () -> Unit): Row
    fun onGlobalIsModified(callback: () -> Boolean): Row
}
