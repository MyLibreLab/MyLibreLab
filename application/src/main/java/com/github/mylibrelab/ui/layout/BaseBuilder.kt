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
import javax.swing.ButtonGroup

interface BaseBuilder {
    fun withButtonGroup(title: Text?, buttonGroup: ButtonGroup, body: () -> Unit)

    fun withButtonGroup(buttonGroup: ButtonGroup, body: () -> Unit) {
        withButtonGroup(null, buttonGroup, body)
    }

    fun buttonGroup(init: () -> Unit) {
        buttonGroup(null, init)
    }

    fun buttonGroup(title: Text? = null, init: () -> Unit) {
        withButtonGroup(title, ButtonGroup(), init)
    }
}
