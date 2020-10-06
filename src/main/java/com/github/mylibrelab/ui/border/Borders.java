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

import javax.swing.*;
import javax.swing.border.Border;

import com.github.weisj.darklaf.components.border.DarkBorders;
import com.github.weisj.darklaf.util.AlignmentExt;

public class Borders {

    public static final int DEFAULT_SPACING = 5;
    public static final int DIALOG_SPACING = 10;

    public static Border emptyTopBorder() {
        return emptyTopBorder(DEFAULT_SPACING);
    }

    public static Border emptyTopBorder(final int spacing) {
        return emptyBorder(spacing, 0, 0, 0);
    }

    public static Border emptyBottomBorder() {
        return emptyBottomBorder(DEFAULT_SPACING);
    }

    public static Border emptyBottomBorder(final int spacing) {
        return emptyBorder(0, 0, spacing, 0);
    }

    public static Border emptyLeftBorder() {
        return emptyLeftBorder(DEFAULT_SPACING);
    }

    public static Border emptyLeftBorder(final int spacing) {
        return emptyBorder(0, spacing, 0, 0);
    }

    public static Border emptyRightBorder() {
        return emptyRightBorder(DEFAULT_SPACING);
    }

    public static Border emptyRightBorder(final int spacing) {
        return emptyBorder(0, 0, 0, spacing);
    }

    public static Border emptyTopBottomBorder() {
        return emptyTopBottomBorder(DEFAULT_SPACING);
    }

    public static Border emptyTopBottomBorder(final int spacing) {
        return emptyBorder(spacing, 0, spacing, 0);
    }

    public static Border emptyLeftRightBorder() {
        return emptyLeftRightBorder(DEFAULT_SPACING);
    }

    public static Border emptyLeftRightBorder(final int spacing) {
        return emptyBorder(0, spacing, 0, spacing);
    }

    public static Border topLineBorder() {
        return lineBorder(1, 0, 0, 0);
    }

    public static Border bottomLineBorder() {
        return lineBorder(1, 0, 0, 0);
    }

    public static Border leftLineBorder() {
        return lineBorder(0, 1, 0, 0);
    }

    public static Border rightLineBorder() {
        return lineBorder(0, 0, 0, 1);
    }

    public static Border topBottomLineBorder() {
        return lineBorder(1, 0, 1, 0);
    }

    public static Border leftRightLineBorder() {
        return lineBorder(0, 1, 0, 1);
    }

    public static MaskedBorder mask(final Border border, final AlignmentExt mask) {
        return mask(border, mask, 0);
    }

    public static MaskedBorder mask(final Border border, final AlignmentExt mask, final int replacement) {
        return new MaskedBorder(border, mask, replacement);
    }

    public static Border dialogBorder() {
        return emptyBorder(DIALOG_SPACING);
    }

    public static Insets insets(final int spacing) {
        return new Insets(spacing, spacing, spacing, spacing);
    }

    /**
     * Create a line border with the specified thickness on each side.
     *
     * @param top an integer specifying the width of the top, in pixels
     * @param left an integer specifying the width of the left side, in pixels
     * @param bottom an integer specifying the width of the bottom, in pixels
     * @param right an integer specifying the width of the right side, in pixels
     * @return the <code>Border</code> object
     */
    public static Border lineBorder(final int top, final int left, final int bottom, final int right) {
        return DarkBorders.createLineBorder(top, left, bottom, right);
    }

    /**
     * Creates an empty border that takes up space but which does no drawing, specifying the width of
     * the top, left, bottom, and right sides.
     *
     * @param spacing an integer specifying the spacing on each side, in pixels
     * @return the <code>Border</code> object
     */
    public static Border emptyBorder(final int spacing) {
        return emptyBorder(spacing, spacing, spacing, spacing);
    }

    /**
     * Creates an empty border that takes up space but which does no drawing, specifying the width of
     * the top, left, bottom, and right sides.
     *
     * @param insets The insets specifying the widths of the border.
     * @return the <code>Border</code> object
     */
    public static Border emptyBorder(final Insets insets) {
        if (insets == null) return emptyBorder();
        return BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right);
    }

    /**
     * Creates an empty border that takes up space but which does no drawing, specifying the width of
     * the top, left, bottom, and right sides.
     *
     * @param top an integer specifying the width of the top, in pixels
     * @param left an integer specifying the width of the left side, in pixels
     * @param bottom an integer specifying the width of the bottom, in pixels
     * @param right an integer specifying the width of the right side, in pixels
     * @return the <code>Border</code> object
     */
    public static Border emptyBorder(final int top, final int left, final int bottom, final int right) {
        return BorderFactory.createEmptyBorder(top, left, bottom, right);
    }

    /**
     * Creates an empty border that takes up no space. (The width of the top, bottom, left, and right
     * sides are all zero.)
     *
     * @return the <code>Border</code> object
     */
    public static Border emptyBorder() {
        return BorderFactory.createEmptyBorder();
    }

    /**
     * Creates a compound border specifying the border objects to use for the outside and inside edges.
     *
     * @param outside a <code>Border</code> object for the outer edge of the compound border
     * @param inside a <code>Border</code> object for the inner edge of the compound border
     * @return the <code>CompoundBorder</code> object
     */
    public static Border compoundBorder(final Border outside, final Border inside) {
        return BorderFactory.createCompoundBorder(outside, inside);
    }
}
