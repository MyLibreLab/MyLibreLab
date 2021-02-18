package com.github.mylibrelab.elements.front.twobooleanpackage.ligthbulb.jv;/*
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

public class GaugeCircuit extends JVSMain {
    private boolean oldValue;
    private ExternalIF panelElement;
    private Image image;
    private VSObject in = null;
    private boolean firstTime = false;



    public void onDispose() {
        image.flush();
        image = null;
    }

    public void paint(java.awt.Graphics g) {
        if (image != null) drawImageCentred(g, image);
    }

    public void init() {
        initPins(0, 0, 0, 1);
        setSize(50, 45);

        element.jSetInnerBorderVisibility(false);
        element.jSetTopPinsVisible(false);
        element.jSetRightPinsVisible(false);
        element.jSetBottomPinsVisible(false);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");


        setPin(0, ExternalIF.C_BOOLEAN, element.PIN_INPUT);
        element.jSetPinDescription(0, "in");

        element.jSetCaptionVisible(true);
        element.jSetCaption("Ligth_Bulb_JV");

        setName("Ligth_Bulb_JV");
    }


    public void initInputPins() {
        in = (VSObject) element.getPinInputReference(0);
    }

    public void initOutputPins() {}


    public void checkPinDataType() {
        boolean pinIn = element.hasPinWire(0);

        int dt = element.C_BOOLEAN;

        element.jSetPinDataType(0, dt);

        element.jRepaint();
    }

    public void start() {
        firstTime = true;
        panelElement = element.getPanelElement();
        panelElement.jProcessPanel(0, 0.0, (Object) this);
    }



    public void process() {
        if (in instanceof VSBoolean) {
            VSBoolean inX = (VSBoolean) in;

            if ((in != null) && ((inX.getValue() != oldValue) || (firstTime))) {
                firstTime = false;
                if (inX.getValue()) {
                    if (panelElement != null) panelElement.jProcessPanel(0, 1.0, (Object) this);
                } else {
                    if (panelElement != null) panelElement.jProcessPanel(0, 0.0, (Object) this);
                }
                panelElement.jRepaint();
                oldValue = inX.getValue();
            }
        }
    }


}
