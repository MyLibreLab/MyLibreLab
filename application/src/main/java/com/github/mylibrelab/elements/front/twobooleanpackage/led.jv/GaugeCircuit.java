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

package com.github.mylibrelab.elements.front.twobooleanpackage.led.jv;/*
                                                                       * Copyright (C) 2020 MyLibreLab
                                                                       * Based on MyOpenLab by Carmelo Salafia
                                                                       * www.myopenlab.de
                                                                       * Copyright (C) 2004 Carmelo Salafia cswi@gmx.de
                                                                       *
                                                                       * This program is free software: you can
                                                                       * redistribute it and/or modify
                                                                       * it under the terms of the GNU General Public
                                                                       * License as published by
                                                                       * the Free Software Foundation, either version 3
                                                                       * of the License, or
                                                                       * (at your option) any later version.
                                                                       *
                                                                       * This program is distributed in the hope that it
                                                                       * will be useful,
                                                                       * but WITHOUT ANY WARRANTY; without even the
                                                                       * implied warranty of
                                                                       * MERCHANTABILITY or FITNESS FOR A PARTICULAR
                                                                       * PURPOSE. See the
                                                                       * GNU General Public License for more details.
                                                                       *
                                                                       * You should have received a copy of the GNU
                                                                       * General Public License
                                                                       * along with this program. If not, see
                                                                       * <http://www.gnu.org/licenses/>.
                                                                       *
                                                                       */

import java.awt.*;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSBoolean;
import VisualLogic.variables.VSObject;

public class GaugeCircuit extends JVSMain {
    private boolean oldValue;
    private boolean oldValue2;
    private ExternalIF panelElement;
    private Image image;
    private VSObject in = null;
    private VSObject in2 = null;



    public void onDispose() {
        image.flush();
        image = null;
    }

    public void paint(java.awt.Graphics g) {
        if (image != null) drawImageCentred(g, image);
    }

    public void init() {
        initPins(0, 0, 0, 2);
        setSize(45, 40);

        element.jSetInnerBorderVisibility(true);
        element.jSetTopPinsVisible(false);
        element.jSetRightPinsVisible(false);
        element.jSetBottomPinsVisible(false);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");


        setPin(0, ExternalIF.C_BOOLEAN, element.PIN_INPUT);
        setPin(1, ExternalIF.C_BOOLEAN, element.PIN_INPUT);

        element.jSetPinDescription(0, "On-Off");
        element.jSetPinDescription(1, "Blink");

        element.jSetCaptionVisible(false);
        element.jSetCaption("Led_JV");

        setName("Led_JV");
    }


    public void initInputPins() {
        in = (VSObject) element.getPinInputReference(0);
        in2 = (VSObject) element.getPinInputReference(1);
        if (in == null) {
            in = new VSBoolean(false);
        }
        if (in2 == null) {
            in2 = new VSBoolean(false);
        }
    }

    public void initOutputPins() {}


    public void checkPinDataType() {
        int dt = element.C_BOOLEAN;

        element.jSetPinDataType(0, dt);
        element.jSetPinDataType(1, dt);

        element.jRepaint();
    }

    public void start() {
        panelElement = element.getPanelElement();
        oldValue = false;
        oldValue2 = false;
        panelElement.jProcessPanel(0, 0.0, (Object) this);
        panelElement.jProcessPanel(1, 0.0, (Object) this);
    }



    public void process() {
        if (in instanceof VSBoolean) {
            VSBoolean inX = (VSBoolean) in;
            if ((in != null) && ((inX.getValue() != oldValue))) {
                if (inX.getValue()) {
                    if (panelElement != null) panelElement.jProcessPanel(0, 1.0, (Object) this);
                } else {
                    if (panelElement != null) panelElement.jProcessPanel(0, 0.0, (Object) this);
                }
                panelElement.jRepaint();
                oldValue = inX.getValue();
            }
        }
        if (in2 instanceof VSBoolean) {
            VSBoolean inX = (VSBoolean) in2;
            if ((in2 != null) && ((inX.getValue() != oldValue2))) {
                if (inX.getValue()) {
                    if (panelElement != null) panelElement.jProcessPanel(1, 1.0, (Object) this);
                } else {
                    if (panelElement != null) panelElement.jProcessPanel(1, 0.0, (Object) this);
                }
                panelElement.jRepaint();
                oldValue2 = inX.getValue();
            }
        }
    }


}
