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

package com.github.mylibrelab.elements.circuit.string.format;// *****************************************************************************

import java.awt.*;
import java.text.DecimalFormat;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSDouble;
import VisualLogic.variables.VSString;

public class Format extends JVSMain {
    private Image image;
    private VSString inA;
    private VSDouble inB;
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
        initPins(0, 1, 0, 2);
        setSize(50, 40);

        initPinVisibility(false, true, false, true);

        element.jSetInnerBorderVisibility(true);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.png");


        setPin(0, ExternalIF.C_STRING, element.PIN_OUTPUT);
        setPin(1, ExternalIF.C_STRING, element.PIN_INPUT);
        setPin(2, ExternalIF.C_DOUBLE, element.PIN_INPUT);

        element.jSetPinDescription(0, "=Format(formatStr,str)   ");
        element.jSetPinDescription(1, "\"formatStr\"   ");
        element.jSetPinDescription(2, "\"value\"   ");

        setName("Format");

    }



    public void initInputPins() {
        inA = (VSString) element.getPinInputReference(1);
        inB = (VSDouble) element.getPinInputReference(2);
    }

    public void initOutputPins() {
        element.setPinOutputReference(0, out);
    }

    private final DecimalFormat df = new DecimalFormat();

    public void process() {
        if (inA != null && inB != null) {
            try {
                if (inA.getValue() != null) df.applyPattern(inA.getValue());

                String temp = df.format(inB.getValue());
                out.setValue(temp);
            } catch (Exception ex) {

            }
            out.setChanged(true);
            element.notifyPin(0);
        }
    }

}
