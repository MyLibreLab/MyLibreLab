package com.github.mylibrelab.elements.front.twonumerisch.gauge;/*
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

public class Gauge extends JVSMain {
    private double oldValue;
    private Image image;
    private ExternalIF panelElement;

    private VSObject in;


    public void onDispose() {
        if (image != null) {
            image.flush();
            image = null;
        }
    }


    public void paint(java.awt.Graphics g) {
        if (image != null) drawImageCentred(g, image);
    }

    public void init() {
        initPins(0, 0, 0, 1);
        setSize(40, 25);

        initPinVisibility(false, false, false, true);
        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");

        setPin(0, ExternalIF.C_VARIANT, element.PIN_INPUT);
        element.jSetPinDescription(0, "in");

        setName("Gauge");
    }



    public void initInputPins() {
        in = (VSObject) element.getPinInputReference(0);
    }

    public void initOutputPins() {}


    public void checkPinDataType() {
        boolean pinIn = element.hasPinWire(0);

        int dt = element.C_VARIANT;

        if (pinIn == true) {
            dt = element.jGetPinDrahtSourceDataType(0);

            if (dt == element.C_DOUBLE)
                dt = element.C_DOUBLE;
            else if (dt == element.C_INTEGER)
                dt = element.C_INTEGER;
            else if (dt == element.C_BYTE)
                dt = element.C_BYTE;
            else
                dt = element.C_DOUBLE;
        }

        element.jSetPinDataType(0, dt);

        element.jRepaint();
    }



    public void start() {
        oldValue = -1;
    }

    public void process() {
        if (in != null) {
            double value = 0;

            if (in instanceof VSDouble) {
                VSDouble val = (VSDouble) in;
                value = val.getValue();
            } else if (in instanceof VSInteger) {
                VSInteger val = (VSInteger) in;
                value = val.getValue();
            } else if (in instanceof VSByte) {
                VSByte val = (VSByte) in;
                value = VSByte.toSigned(val.getValue());
            }

            if (value != oldValue) {
                panelElement = element.getPanelElement();
                if (panelElement != null) {
                    panelElement.jProcessPanel(0, value, (Object) this);
                    panelElement.jRepaint();
                }
                oldValue = value;
            }

        }

    }


    /*
     * public void process()
     * {
     *
     * if (in!=null && value0!=in.getValue())
     * {
     * value0=in.getValue();
     * panelElement=element.getPanelElement();
     * panelElement.jProcessPanel(0,value0,this);
     * panelElement.jRepaint();
     * }
     * }
     */

}
