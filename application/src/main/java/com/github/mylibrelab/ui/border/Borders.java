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

import org.jetbrains.annotations.NotNull;

import com.github.weisj.darklaf.components.border.DarkBorders;
import com.github.weisj.darklaf.layout.LayoutHelper;
import com.github.weisj.darklaf.util.AlignmentExt;

public class Borders {

    public static final int DEFAULT_SPACING = LayoutHelper.getDefaultSpacing();
    public static final int DIALOG_SPACING = 2 * DEFAULT_SPACING;

    private Borders() {
        throw new IllegalStateException("Utility class");
    }

    @NotNull
    public static Insets createSpacingInsets() {
        return new Insets(DEFAULT_SPACING, DEFAULT_SPACING, DEFAULT_SPACING, DEFAULT_SPACING);
    }

    @NotNull
    public static Border emptySpacerBorder() {
        return emptyBorder(DEFAULT_SPACING, DEFAULT_SPACING, DEFAULT_SPACING, DEFAULT_SPACING);
    }

    @NotNull
    public static Border emptyTopSpacerBorder() {
        return emptyTopSpacerBorder(DEFAULT_SPACING);
    }

    @NotNull
    public static Border emptyTopSpacerBorder(final int spacing) {
        return emptyBorder(spacing, 0, 0, 0);
    }

    @NotNull
    public static Border emptyBottomSpacerBorder() {
        return emptyBottomSpacerBorder(DEFAULT_SPACING);
    }

    @NotNull
    public static Border emptyBottomSpacerBorder(final int spacing) {
        return emptyBorder(0, 0, spacing, 0);
    }

    @NotNull
    public static Border emptyLeftSpacerBorder() {
        return emptyLeftSpacerBorder(DEFAULT_SPACING);
    }

    @NotNull
    public static Border emptyLeftSpacerBorder(final int spacing) {
        return emptyBorder(0, spacing, 0, 0);
    }

    @NotNull
    public static Border emptyRightSpacerBorder() {
        return emptyRightSpacerBorder(DEFAULT_SPACING);
    }

    @NotNull
    public static Border emptyRightSpacerBorder(final int spacing) {
        return emptyBorder(0, 0, 0, spacing);
    }

    @NotNull
    public static Border emptyTopBottomSpacerBorder() {
        return emptyTopBottomSpacerBorder(DEFAULT_SPACING);
    }

    @NotNull
    public static Border emptyTopBottomSpacerBorder(final int spacing) {
        return emptyBorder(spacing, 0, spacing, 0);
    }

    @NotNull
    public static Border emptyLeftRightSpacerBorder() {
        return emptyLeftRightSpacerBorder(DEFAULT_SPACING);
    }

    @NotNull
    public static Border emptyLeftRightSpacerBorder(final int spacing) {
        return emptyBorder(0, spacing, 0, spacing);
    }

    @NotNull
    public static Border topLineBorder() {
        return lineBorder(1, 0, 0, 0);
    }

    @NotNull
    public static Border bottomLineBorder() {
        return lineBorder(1, 0, 0, 0);
    }

    @NotNull
    public static Border leftLineBorder() {
        return lineBorder(0, 1, 0, 0);
    }

    @NotNull
    public static Border rightLineBorder() {
        return lineBorder(0, 0, 0, 1);
    }

    @NotNull
    public static Border topBottomLineBorder() {
        return lineBorder(1, 0, 1, 0);
    }

    @NotNull
    public static Border leftRightLineBorder() {
        return lineBorder(0, 1, 0, 1);
    }

    @NotNull
    public static Border withSpacing(@NotNull final Border border) {
        return compoundBorder(border, emptyBorder(DEFAULT_SPACING, DEFAULT_SPACING, DEFAULT_SPACING, DEFAULT_SPACING));
    }

    @NotNull
    public static MaskedBorder mask(final Border border, final AlignmentExt mask) {
        return mask(border, mask, 0);
    }

    @NotNull
    public static MaskedBorder mask(final Border border, final AlignmentExt mask, final int replacement) {
        return new MaskedBorder(border, mask, replacement);
    }

    @NotNull
    public static Border dialogBorder() {
        return emptyBorder(DIALOG_SPACING);
    }

    @NotNull
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
    @NotNull
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
    @NotNull
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
    @NotNull
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
    @NotNull
    public static Border emptyBorder(final int top, final int left, final int bottom, final int right) {
        return BorderFactory.createEmptyBorder(top, left, bottom, right);
    }

    /**
     * Creates an empty border that takes up no space. (The width of the top, bottom, left, and right
     * sides are all zero.)
     *
     * @return the <code>Border</code> object
     */
    @NotNull
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
    @NotNull
    public static Border compoundBorder(final Border outside, final Border inside) {
        return BorderFactory.createCompoundBorder(outside, inside);
    }

    /**
     * Adds a title to an existing border,
     * with default positioning (determined by the current look and feel),
     * default justification (leading) and the default
     * font and text color (determined by the current look and feel).
     *
     * @param border the <code>Border</code> object to add the title to
     * @param title a <code>String</code> containing the text of the title
     * @return the <code>TitledBorder</code> object
     */
    public static Border createTitledBorder(final Border border, final String title) {
        return BorderFactory.createTitledBorder(border, title);
    }

    /**
     * Adds a title to an existing border,
     * with default positioning (determined by the current look and feel),
     * default justification (leading) and the default
     * font and text color (determined by the current look and feel).
     *
     * @param border the <code>Border</code> object to add the title to
     * @return the <code>TitledBorder</code> object
     */
    public static Border createTitledBorder(final Border border) {
        return BorderFactory.createTitledBorder(border);
    }

    /**
     * Creates a new titled border with the specified title,
     * the default border type (determined by the current look and feel),
     * the default text position (determined by the current look and feel),
     * the default justification (leading), and the default
     * font and text color (determined by the current look and feel).
     *
     * @param title a <code>String</code> containing the text of the title
     * @return the <code>TitledBorder</code> object
     */
    @NotNull
    public static Border createTitledBorder(final String title) {
        return BorderFactory.createTitledBorder(title);
    }
}
