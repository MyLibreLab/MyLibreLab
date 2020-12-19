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

import com.github.mylibrelab.ui.layout.CCFlags
import com.github.mylibrelab.ui.layout.GrowPolicy
import com.github.mylibrelab.ui.layout.SpacingConfiguration
import net.miginfocom.layout.BoundSize
import net.miginfocom.layout.CC
import net.miginfocom.layout.ConstraintParser
import java.awt.Component
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.text.JTextComponent

internal fun overrideFlags(cc: CC, flags: Array<out CCFlags>) {
    for (flag in flags) {
        when (flag) {
            CCFlags.grow -> cc.grow()
            CCFlags.growX -> {
                cc.growX(1000f)
            }
            CCFlags.growY -> cc.growY(1000f)

            // If you have more than one component in a cell the alignment keywords will not work since the behavior would be Nondeterministic.
            // You can however accomplish the same thing by setting a gap before and/or after the components.
            // That gap may have a minimum size of 0 and a preferred size of a really large value to create a "pushing" gap.
            // There is even a keyword for this: "push". So "gapleft push" will be the same as "align right" and work for multi-component cells as well.
            // CCFlags.right -> horizontal.gapBefore = BoundSize(null, null, null, true, null)

            CCFlags.push -> cc.push()
            CCFlags.pushX -> cc.pushX()
            CCFlags.pushY -> cc.pushY()
        }
    }
}

internal class DefaultComponentConstraintCreator(spacing: SpacingConfiguration) {
    private val shortTextSizeSpec =
        ConstraintParser.parseBoundSize("${spacing.shortTextWidth}px!", false, true)
    private val mediumTextSizeSpec =
        ConstraintParser.parseBoundSize("${spacing.shortTextWidth}px::${spacing.maxShortTextWidth}px", false, true)

    val vertical1pxGap: BoundSize = ConstraintParser.parseBoundSize("${1}px!", true, false)

    fun addGrowIfNeeded(cc: CC, component: Component) {
        when {
            component is JTextField && component.columns != 0 -> return
            component is JTextComponent -> cc.growX()
            component is JScrollPane -> {
                cc.grow().pushY()
                val view = component.viewport.view
                if (view is JTextArea && view.rows == 0) {
                    // set min size to 2 lines (yes, for some reasons it means that rows should be set to 3)
                    view.rows = 3
                }
            }
        }
    }

    fun applyGrowPolicy(cc: CC, growPolicy: GrowPolicy) {
        cc.horizontal.size = when (growPolicy) {
            GrowPolicy.SHORT_TEXT -> shortTextSizeSpec
            GrowPolicy.MEDIUM_TEXT -> mediumTextSizeSpec
        }
    }
}
