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
package com.github.mylibrelab.ui.layout

interface SpacingConfiguration {
    /**
     * Horizontal space between two components (in terms of layout grid - cells).
     *
     * It is space between associated components (somehow relates to each other) - for example, combobox and button to delete items from combobox.
     * Since in most cases components in cells will be associated, it is not a space between two independent components.
     * Horizontal subgroups of components is not supported yet, that's why there is no property to define such space.
     */
    val horizontalGap: Int

    /**
     * Vertical space between two components (in terms of layout grid - rows).
     */
    val verticalGap: Int get() = componentVerticalGap * 2
    val componentVerticalGap: Int

    /**
     * Horizontal gap after label column.
     */
    val labelColumnHorizontalGap: Int

    val largeHorizontalGap: Int
    val largeVerticalGap: Int
    val radioGroupTitleVerticalGap: Int

    val shortTextWidth: Int
    val maxShortTextWidth: Int

    val commentVerticalTopGap: Int

    val dialogTopBottom: Int
    val dialogLeftRight: Int

    /**
     * The size of one indent level (when not overridden by specific control type, e.g. indent of checkbox comment row
     * is defined by checkbox icon size)
     */
    val indentLevel: Int
}

fun createDefaultSpacingConfiguration(): SpacingConfiguration {
    return object : SpacingConfiguration {
        override val horizontalGap = 6
        override val componentVerticalGap = 6
        override val labelColumnHorizontalGap = 6
        override val largeHorizontalGap = 16
        override val largeVerticalGap = 20
        override val radioGroupTitleVerticalGap = 8

        override val shortTextWidth = 250
        override val maxShortTextWidth = 350

        override val dialogTopBottom = 10
        override val dialogLeftRight = 12

        override val commentVerticalTopGap = 6

        override val indentLevel: Int = 15
    }
}
