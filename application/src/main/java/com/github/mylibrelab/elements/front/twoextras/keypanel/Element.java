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

package com.github.mylibrelab.elements.front.twoextras.keypanel;/*
                                                                 * Copyright (C) 2020 MyLibreLab
                                                                 * Based on MyOpenLab by Carmelo Salafia
                                                                 * www.myopenlab.de
                                                                 * Copyright (C) 2004 Carmelo Salafia cswi@gmx.de
                                                                 *
                                                                 * This program is free software: you can redistribute
                                                                 * it and/or modify
                                                                 * it under the terms of the GNU General Public License
                                                                 * as published by
                                                                 * the Free Software Foundation, either version 3 of the
                                                                 * License, or
                                                                 * (at your option) any later version.
                                                                 *
                                                                 * This program is distributed in the hope that it will
                                                                 * be useful,
                                                                 * but WITHOUT ANY WARRANTY; without even the implied
                                                                 * warranty of
                                                                 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
                                                                 * See the
                                                                 * GNU General Public License for more details.
                                                                 *
                                                                 * You should have received a copy of the GNU General
                                                                 * Public License
                                                                 * along with this program. If not, see
                                                                 * <http://www.gnu.org/licenses/>.
                                                                 *
                                                                 */

import java.awt.*;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSBoolean;
import VisualLogic.variables.VSInteger;

public class Element extends JVSMain {
    private ExternalIF panelElement;
    private Image image;

    private VSInteger keyCode = new VSInteger(0);
    private VSBoolean keyPressed = new VSBoolean(false);
    private VSBoolean keyReleased = new VSBoolean(false);



    public void paint(java.awt.Graphics g) {
        drawImageCentred(g, image);
    }

    public void onDispose() {
        if (image != null) {
            image.flush();
            image = null;
        }
    }

    public void init() {
        element.jSetInnerBorderVisibility(true);
        initPins(0, 3, 0, 0);
        setSize(45, 32 + 10);
        initPinVisibility(false, true, false, false);

        setPin(0, ExternalIF.C_INTEGER, element.PIN_OUTPUT);
        setPin(1, ExternalIF.C_BOOLEAN, element.PIN_OUTPUT);
        setPin(2, ExternalIF.C_BOOLEAN, element.PIN_OUTPUT);

        element.jSetPinDescription(0, "KeyCode");
        element.jSetPinDescription(1, "Key Pressed");
        element.jSetPinDescription(2, "Key Released");

        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");
        setName("KeyPanel");

        element.jSetResizable(false);
    }

    public void initInputPins() {

    }

    public void initOutputPins() {
        element.setPinOutputReference(0, keyCode);
        element.setPinOutputReference(1, keyPressed);
        element.setPinOutputReference(2, keyReleased);
    }


    boolean changed = false;

    public void changePin(int pinIndex, Object value) {

        if (value instanceof VSInteger) {
            VSInteger val = (VSInteger) value;

            keyCode.setValue(val.getValue());

            if (pinIndex == 0) // Key Pressed
            {
                keyPressed.setValue(true);
                keyReleased.setValue(false);

            }
            if (pinIndex == 1) // Key Released
            {
                keyPressed.setValue(false);
                keyReleased.setValue(true);

            }
            element.notifyPin(0);
            element.notifyPin(1);
            element.notifyPin(2);
            // element.jNotifyWhenDestCalled(0,element);
        }

    }


    public void start() {
        panelElement = element.getPanelElement();
    }

    public void process() {

    }


}
