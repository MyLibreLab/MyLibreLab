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

package com.github.mylibrelab.ui.component

import com.github.mylibrelab.text.Text
import com.github.mylibrelab.ui.UIStyle
import com.github.mylibrelab.ui.UIUtil
import com.github.mylibrelab.ui.icons.AllIcons
import com.github.weisj.darklaf.components.tooltip.ToolTipContext
import com.github.weisj.darklaf.extensions.kotlin.tooltipContext
import com.github.weisj.darklaf.util.Alignment
import com.github.weisj.darklaf.util.StringUtil
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JLabel
import javax.swing.ToolTipManager

/**
 * Label which displays information text in it's tooltip. The tooltip will be instantly displayed if
 * the mouse hovers over the component.
 * Html text is supported.
 */
class ContextInfoLabel(val infoText: Text) : JLabel() {

    init {
        UIStyle.withDynamic(this) {
            it.toolTipText = StringUtil.toHtml(infoText.text)
        }
        icon = AllIcons.CONTEXT_HELP
        disabledIcon = AllIcons.CONTEXT_HELP_DISABLED
        val label = this
        addMouseListener(object : MouseAdapter() {
            override fun mouseEntered(e: MouseEvent?) {
                UIUtil.showTooltip(label)
            }
        })
        tooltipContext = ToolTipContext.createDefaultContext().apply {
            alignment = Alignment.NORTH
            isAlignInside = false
        }
    }

    private var tooltipManagerListener: MouseListener? = null

    override fun addMouseListener(l: MouseListener?) {
        super.addMouseListener(
            if (l is ToolTipManager) {
                tooltipManagerListener = object : MouseListener by l {
                    override fun mousePressed(e: MouseEvent?) = e?.consume() ?: Unit
                }
                tooltipManagerListener
            } else {
                l
            }
        )
    }

    override fun removeMouseListener(l: MouseListener?) {
        super.removeMouseListener(
            if (l is ToolTipManager) {
                tooltipManagerListener
            } else {
                l
            }
        )
    }
}
