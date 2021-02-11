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

package com.github.mylibrelab.elements.circuit.string.message;// *****************************************************************************

import java.awt.*;
import java.util.Locale;

import javax.swing.*;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSString;

public class Message extends JVSMain {
    private Image image;
    private VSString inMessage;
    private final String oldValue = "";


    public void xpaint(java.awt.Graphics g) {
        if (image != null) drawImageCentred(g, image);
    }

    public void onDispose() {
        if (image != null) {
            image.flush();
            image = null;
        }
    }

    public void init() {
        initPins(0, 0, 0, 1);
        setSize(35 + 3, 25 + 3);
        initPinVisibility(false, false, false, true);
        element.jSetInnerBorderVisibility(false);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.png");

        setPin(0, ExternalIF.C_STRING, element.PIN_INPUT); // calc


        String strLocale = Locale.getDefault().toString();

        if (strLocale.equalsIgnoreCase("de_DE")) {
            element.jSetPinDescription(0, "Nachricht");
        }
        if (strLocale.equalsIgnoreCase("en_US")) {
            element.jSetPinDescription(0, "message");
        }
        if (strLocale.equalsIgnoreCase("es_ES")) {
            element.jSetPinDescription(0, "mensaje ES");
        }


        element.jSetCaptionVisible(true);
        element.jSetCaption("Message");
        setName("Message");
    }

    public void initInputPins() {
        inMessage = (VSString) element.getPinInputReference(0);

        if (inMessage == null) {
            inMessage = new VSString("");
        }

    }

    public void initOutputPins() {}

    public void process() {
        if (inMessage instanceof VSString) {
            if (inMessage.getValue().length() > 0) {
                JOptionPane.showMessageDialog(null, inMessage.getValue(), "Message!", JOptionPane.PLAIN_MESSAGE);
            }
        }

    }
}
