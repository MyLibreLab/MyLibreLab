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

package com.github.mylibrelab.elements.circuit.numerik.trigonometrie.asin;// *****************************************************************************

import java.awt.*;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSDouble;

public class XASin extends JVSMain {
    private Image image;
    private VSDouble in;
    private final VSDouble out = new VSDouble();

    public void xpaint(java.awt.Graphics g) {
        drawImageCentred(g, image);
    }

    public void onDispose() {
        if (image != null) {
            image.flush();
            image = null;
        }
    }

    public void init() {
        initPins(0, 1, 0, 1);
        setSize(40, 30);
        initPinVisibility(false, true, false, true);
        element.jSetInnerBorderVisibility(false);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");

        setPin(0, ExternalIF.C_DOUBLE, element.PIN_OUTPUT);
        setPin(1, ExternalIF.C_DOUBLE, element.PIN_INPUT);

        element.jSetPinDescription(0, "f(x)");
        element.jSetPinDescription(1, "x");

        element.jSetCaptionVisible(true);
        element.jSetCaption("asin");

        setName("asin");
    }

    public void initInputPins() {
        in = (VSDouble) element.getPinInputReference(1);
    }

    public void initOutputPins() {
        element.setPinOutputReference(0, out);
    }

    public void process() {
        if (in instanceof VSDouble) {
            out.setValue(Math.asin(in.getValue()));
            out.setChanged(true);
            element.notifyPin(0);
        }

    }
}
