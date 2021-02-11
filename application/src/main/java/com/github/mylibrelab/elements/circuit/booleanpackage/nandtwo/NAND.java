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

package com.github.mylibrelab.elements.circuit.booleanpackage.nandtwo;// *****************************************************************************

import java.awt.*;

import com.github.mylibrelab.elements.tools.Gatter2;

public class NAND extends Gatter2 {
    private boolean oldValue = true;

    public NAND() {
        super(2, "Nand");

    }


    public void start() {
        oldValue = true;
        out.setValue(true);
        element.notifyPin(0);
    }

    public void init() {
        super.init();
        element.jSetInnerBorderVisibility(false);
    }

    public void paint(java.awt.Graphics g) {
        if (element != null) {
            Rectangle r = element.jGetBounds();
            g.setColor(Color.BLACK);
            int mitteY = (r.y + r.height) / 2;

            int d = 7;
            g.drawRect(r.x, r.y, r.width - d, r.height - 1);
            g.drawOval(r.x + r.width - d, r.y + mitteY - d / 2, d - 1, d - 1);

            g.drawString("&", 20, r.y + mitteY + 5);
        }
    }



    public void process() {
        boolean last = false;
        int c = 0;

        for (int i = 0; i < in.length; i++) {
            last = in[i].getValue();
            if (last == true) {
                c++;
            }
        }

        boolean value = false;

        value = c != in.length;

        if (value != oldValue) {
            oldValue = value;
            out.setValue(value);
            element.notifyPin(0);
        }

    }


}
