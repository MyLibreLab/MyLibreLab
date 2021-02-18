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

package com.github.mylibrelab.elements.tools;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

class Beschriftung<T extends AbstractCustomAnalogComp> extends JPanel {
    public String text = "";
    public boolean visible = false;
    private T owner = null;

    public Beschriftung(T owner, String text) {
        this.text = text;
        this.owner = owner;
        this.setBackground(new Color(100, 100, 100, 0));
        this.setOpaque(false);
    }

    public void paint(Graphics g) {
        // super.paintComponent(g);

        if (visible) {
            g.setFont(owner.font.getValue());
            g.setColor(owner.fontColor.getValue());

            FontMetrics fm = g.getFontMetrics(owner.font.getValue());

            g.drawString(text, 1, 0 + fm.getMaxAscent());
        }
    }
}
