package com.github.mylibrelab.elements.front.twonumerisch.dblspinner.jv;/*
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

public class Spinner extends JVSMain {
    private Image image;
    private boolean changed = false;
    private VSDouble out = new VSDouble();
    private VSDouble val;

    public void onDispose() {
        image.flush();
        image = null;
    }

    public void paint(java.awt.Graphics g) {
        drawImageCentred(g, image);
    }

    public void init() {
        initPins(0, 1, 0, 0);
        setSize(50, 45);

        element.jSetInnerBorderVisibility(true);
        initPinVisibility(false, true, false, false);


        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");

        setPin(0, ExternalIF.C_DOUBLE, element.PIN_OUTPUT);

        element.jSetCaptionVisible(true);
        element.jSetCaption("Spinner_JV (DBL)");

        setName("Spinner_JV (DBL)");
    }


    public void changePin(int pinIndex, Object value) {
        changed = true;
        val = (VSDouble) value;
        if (val != null) {
            out.setValue(val.getValue());
            element.notifyPin(0);
        }
    }

    public void initInputPins() {}

    public void initOutputPins() {
        element.setPinOutputReference(0, out);
    }


    public void process() {}


}
