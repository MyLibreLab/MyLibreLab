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

package com.github.mylibrelab.elements.circuit.numerik.randomtwo;// *****************************************************************************

import java.awt.*;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSBoolean;
import VisualLogic.variables.VSDouble;

public class XRandom extends JVSMain {
    private Image image;
    private Random generator = null;
    private VSBoolean in;
    private final VSDouble out = new VSDouble();

    public void onDispose() {
        if (image != null) {
            image.flush();
            image = null;
        }
    }

    public void paint(java.awt.Graphics g) {
        drawImageCentred(g, image);
    }

    public void init() {
        initPins(0, 1, 0, 1);
        setSize(40, 25);

        element.jSetInnerBorderVisibility(false);
        element.jSetTopPinsVisible(false);
        element.jSetBottomPinsVisible(false);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.png");

        setPin(0, ExternalIF.C_DOUBLE, element.PIN_OUTPUT);
        setPin(1, ExternalIF.C_BOOLEAN, element.PIN_INPUT);

        element.jSetPinDescription(0, "Out values[0..1]");
        element.jSetPinDescription(1, "start");

        setName("Random_2.0");
    }


    public void initInputPins() {
        in = (VSBoolean) element.getPinInputReference(1);

        if (in == null) in = new VSBoolean(false);
    }

    public void initOutputPins() {
        element.setPinOutputReference(0, out);
    }

    public void start() {
        generator = new Random();
        out.setValue(0);
        out.setChanged(true);
        element.notifyPin(0);
    }


    public void process() {
        if (in.getValue()) {
            out.setValue(generator.nextDouble());
            out.setChanged(true);
            element.notifyPin(0);
            in.setValue(false);
        }

    }



}
