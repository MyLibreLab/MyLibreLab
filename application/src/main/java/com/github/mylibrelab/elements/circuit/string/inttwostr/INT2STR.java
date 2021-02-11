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

package com.github.mylibrelab.elements.circuit.string.inttwostr;// *****************************************************************************

import java.awt.*;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSInteger;
import VisualLogic.variables.VSString;

public class INT2STR extends JVSMain {
    private Image image;
    private VSInteger in;
    private final VSString out = new VSString();

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

        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");

        setPin(0, ExternalIF.C_STRING, element.PIN_OUTPUT);
        setPin(1, ExternalIF.C_INTEGER, element.PIN_INPUT);
        element.jSetPinDescription(0, "Out");
        element.jSetPinDescription(1, "In");

        setName("INT->Str");
    }


    public void initInputPins() {
        in = (VSInteger) element.getPinInputReference(1);
    }

    public void initOutputPins() {
        element.setPinOutputReference(0, out);
    }



    public void process() {
        if (in != null) {
            out.setValue("" + in.getValue());
            out.setChanged(true);
            element.notifyPin(0);
        }
    }


}
