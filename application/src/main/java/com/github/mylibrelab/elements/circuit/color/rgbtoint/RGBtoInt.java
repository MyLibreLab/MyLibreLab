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

package com.github.mylibrelab.elements.circuit.color.rgbtoint;// *****************************************************************************

import java.awt.Color;
import java.awt.Image;
import java.util.Locale;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSInteger;

public class RGBtoInt extends JVSMain {
    public VSInteger out = new VSInteger(0);
    public VSInteger inR;
    public VSInteger inG;
    public VSInteger inB;

    private Image image;

    private boolean changed = false;

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
        initPins(0, 1, 0, 3);
        setSize(35 + 20, 35);
        initPinVisibility(false, true, false, true);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");

        setPin(0, ExternalIF.C_INTEGER, element.PIN_OUTPUT);
        setPin(1, ExternalIF.C_INTEGER, element.PIN_INPUT);
        setPin(2, ExternalIF.C_INTEGER, element.PIN_INPUT);
        setPin(3, ExternalIF.C_INTEGER, element.PIN_INPUT);

        String strLocale = Locale.getDefault().toString();

        if (strLocale.equalsIgnoreCase("de_DE")) {
            element.jSetPinDescription(0, "Out Color");
            element.jSetPinDescription(1, "In Rot");
            element.jSetPinDescription(2, "In Grün");
            element.jSetPinDescription(3, "In Blau");
        }
        if (strLocale.equalsIgnoreCase("en_US")) {
            element.jSetPinDescription(0, "Out Color");
            element.jSetPinDescription(1, "In Red");
            element.jSetPinDescription(2, "In Green");
            element.jSetPinDescription(3, "In Blue");
        }
        if (strLocale.equalsIgnoreCase("es_ES")) {
            element.jSetPinDescription(0, "Out Color");
            element.jSetPinDescription(1, "rojo-en");
            element.jSetPinDescription(2, "verde-en");
            element.jSetPinDescription(3, "azul-en");
        }



        element.jSetCaptionVisible(true);
        element.jSetCaption("RGB2Int");
        setName("RGB2Int");
    }



    public void setPropertyEditor() {}

    public void propertyChanged(Object o) {}

    public void initInputPins() {
        inR = (VSInteger) element.getPinInputReference(1);
        inG = (VSInteger) element.getPinInputReference(2);
        inB = (VSInteger) element.getPinInputReference(3);

        if (inR == null) {
            inR = new VSInteger(0);
        }

        if (inG == null) {
            inG = new VSInteger(0);
        }

        if (inB == null) {
            inB = new VSInteger(0);
        }

    }

    public void initOutputPins() {
        element.setPinOutputReference(0, out);
    }

    public void start() {
        changed = true;
    }

    public void process() {
        out.setValue(new Color(inR.getValue(), inG.getValue(), inB.getValue()).getRGB());
        out.setChanged(true);
        element.notifyPin(0);
    }

}