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

package com.github.mylibrelab.ui.border;

import java.awt.*;

import javax.swing.border.Border;

import org.jetbrains.annotations.NotNull;

import com.github.weisj.darklaf.util.AlignmentExt;

public class MaskedBorder implements Border {

    private final Border border;
    private final AlignmentExt mask;
    private final int maskValue;

    /**
     * Create a new masked border.
     *
     * @param border the border to mask.
     * @param mask the mask.
     * @param maskValue the new value for the sides where the insets have been masked.
     */
    public MaskedBorder(@NotNull final Border border, @NotNull final AlignmentExt mask, final int maskValue) {
        this.border = border;
        this.mask = mask;
        this.maskValue = maskValue;
    }

    @Override
    public void paintBorder(final Component c, final Graphics g, final int x, final int y, final int width,
            final int height) {
        Insets ins = getBorderInsets(c);
        g.translate(-ins.left, -ins.top);
        border.paintBorder(c, g, x, y, width + ins.left + ins.left, height + ins.top + ins.bottom);
        g.translate(ins.left, ins.top);
    }

    @Override
    public Insets getBorderInsets(final Component c) {
        return mask.maskInsets(border.getBorderInsets(c), maskValue);
    }

    @Override
    public boolean isBorderOpaque() {
        return border.isBorderOpaque();
    }
}
