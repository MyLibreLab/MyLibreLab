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
package com.github.mylibrelab.ui.layout.miglayout

import net.miginfocom.layout.BoundSize
import net.miginfocom.layout.LC
import net.miginfocom.layout.UnitValue

internal fun createLayoutConstraints(horizontalGap: Int, verticalGap: Int): LC {
    val lc = LC()
    lc.gridGapX = gapToBoundSize(horizontalGap, isHorizontal = true)
    lc.gridGapY = gapToBoundSize(verticalGap, isHorizontal = false)
    lc.setInsets(0)
    return lc
}

internal fun gapToBoundSize(value: Int, isHorizontal: Boolean): BoundSize {
    val unitValue = createUnitValue(value, isHorizontal)
    return BoundSize(unitValue, unitValue, null, false, null)
}

internal fun createLayoutConstraints(): LC {
    val lc = LC()
    lc.gridGapX = gapToBoundSize(0, true)
    lc.setInsets(0)
    return lc
}

internal fun LC.setInsets(value: Int) = setInsets(value, value)

internal fun LC.setInsets(topBottom: Int, leftRight: Int) {
    val h = createUnitValue(leftRight, isHorizontal = true)
    val v = createUnitValue(topBottom, isHorizontal = false)
    insets = arrayOf(v, h, v, h)
}

internal fun createUnitValue(value: Int, isHorizontal: Boolean): UnitValue {
    return UnitValue(value.toFloat(), "px", isHorizontal, UnitValue.STATIC, null)
}
