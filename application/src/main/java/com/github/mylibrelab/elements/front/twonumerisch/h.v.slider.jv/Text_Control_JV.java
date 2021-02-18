package com.github.mylibrelab.elements.front.twonumerisch.h.v.slider.jv;/*
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

public class Text_Control_JV extends JVSMain {
    private ExternalIF panelElement;
    private Image image;

    private String oldValue;
    private boolean firstTime = true;
    private VSInteger out = new VSInteger();
    private VSString outStr = new VSString();
    private VSInteger val;
    private boolean changed = false;

    public void paint(java.awt.Graphics g) {
        // drawImageCentred(g,image);
        drawImageCentred(g, image);
    }

    public void init() {
        initPins(0, 2, 0, 0);
        setSize(70, 45);

        element.jSetInnerBorderVisibility(true);
        initPinVisibility(false, false, false, true);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");

        // setPin(0,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(0, ExternalIF.C_INTEGER, ExternalIF.PIN_OUTPUT);
        setPin(1, ExternalIF.C_STRING, ExternalIF.PIN_OUTPUT);

        element.jSetCaptionVisible(true);
        element.jSetCaption("JSlider_JV");

        setName("JSlider_JV");
    }

    public void changePin(int pinIndex, Object value) {
        changed = true;
        val = (VSInteger) value;
        if (val != null) {
            out.setValue(val.getValue());
            outStr.setValue(String.valueOf(val.getValue()));

            element.notifyPin(0);
            element.notifyPin(1);
        }
    }

    public void initInputPins() {}

    public void initOutputPins() {
        element.setPinOutputReference(0, out);
        element.setPinOutputReference(1, outStr);
    }



    public void process() {}


}
