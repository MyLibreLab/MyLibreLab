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

package com.github.mylibrelab.ui.component;

import java.awt.Color;

import javax.swing.JTextPane;

public class NonEditableTexPane extends JTextPane {

    private Color enabledBackground;

    public NonEditableTexPane() {
        super.setEditable(false);
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public void setEditable(final boolean b) { /* Component is non editable */ }

    @Override
    public void updateUI() {
        super.updateUI();
        boolean isEnabled = isEnabled();
        enabledBackground = super.getBackground();
        setEnabled(isEnabled);
    }

    @Override
    public Color getBackground() {
        return isEnabled() ? enabledBackground : super.getBackground();
    }
}
