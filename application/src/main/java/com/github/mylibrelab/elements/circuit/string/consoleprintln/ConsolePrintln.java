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

package com.github.mylibrelab.elements.circuit.string.consoleprintln;// *****************************************************************************

import java.awt.Image;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSBoolean;
import VisualLogic.variables.VSInteger;
import VisualLogic.variables.VSString;

public class ConsolePrintln extends JVSMain {
    private Image image;
    private VSString in;
    private VSBoolean inPrint;
    private final VSInteger out = new VSInteger();

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
        initPins(0, 0, 0, 2);
        setSize(50, 35);
        element.jSetInnerBorderVisibility(false);
        element.jSetTopPinsVisible(false);
        element.jSetBottomPinsVisible(false);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");

        setPin(0, ExternalIF.C_BOOLEAN, element.PIN_INPUT);
        setPin(1, ExternalIF.C_STRING, element.PIN_INPUT);

        element.jSetPinDescription(0, "write");
        element.jSetPinDescription(1, "in");

        setName("ConsolePrintln");
    }


    public void initInputPins() {
        inPrint = (VSBoolean) element.getPinInputReference(0);
        in = (VSString) element.getPinInputReference(1);

        if (inPrint == null) inPrint = new VSBoolean(false);
        if (in == null) in = new VSString("");
    }

    public void initOutputPins() {

    }

    public void process() {
        if (inPrint.getValue()) {
            element.jConsolePrintln(in.getValue());
            inPrint.setValue(false);
        }
    }


}
