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

package com.github.mylibrelab.elements.tools;


import java.awt.*;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSBoolean;
import VisualLogic.variables.VSInteger;

public class Gatter2 extends JVSMain {

    private final int height;
    public VSBoolean[] in;
    public VSInteger delay = new VSInteger(0); // = 0 micro sec.
    public VSBoolean out = new VSBoolean();
    public VSInteger anzPins = new VSInteger(2);
    private int width;
    private Image image;
    private String name = "";

    public Gatter2(int anzPins, String name) {
        this.anzPins.setValue(anzPins);
        // this.anzPins=anzPins;
        this.name = name;
        width = 50;
        height = 25;
    }

    public void onDispose() {
        if (image != null) {
            image.flush();
            image = null;
        }
    }

    public void paint(java.awt.Graphics g) {
        drawImageCentred(g, image);
    }

    public void initPins() {
        initPins(0, 1, 0, anzPins.getValue());
        setSize(width, 20 + (anzPins.getValue() * 10));

        setPin(0, ExternalIF.C_BOOLEAN, element.PIN_OUTPUT);

        for (int i = 1; i <= anzPins.getValue(); i++) {
            setPin(i, ExternalIF.C_BOOLEAN, element.PIN_INPUT);
        }
    }

    public void init() {
        element.jSetInnerBorderVisibility(true);

        // initPins(0,1,0,anzPins);
        initPinVisibility(false, true, false, true);
        image = element.jLoadImage(element.jGetSourcePath() + "icon.png");
        width = image.getWidth(null) + 22;

        initPins();

        setName(name);
    }

    public void propertyChanged(Object o) {
        if (o == anzPins) {
            initPins();
            element.jRepaint();
            element.jRefreshVM();
        }
    }


    public void setPropertyEditor() {
        element.jAddPEItem("Input-Pins", anzPins, 2, 20);

        localize();
    }

    private void localize() {
        int d = 6;
        String language;

        language = "en_US";

        element.jSetPEItemLocale(d + 0, language, "Input-Pins");

        language = "es_ES";

        element.jSetPEItemLocale(d + 0, language, "Input-Pins");
    }


    public void initInputPins() {
        in = new VSBoolean[anzPins.getValue()];
        for (int i = 1; i <= anzPins.getValue(); i++) {
            in[i - 1] = (VSBoolean) element.getPinInputReference(i);
        }
    }

    public void initOutputPins() {
        element.setPinOutputReference(0, out);
    }


    public void loadFromStream(java.io.FileInputStream fis) {
        anzPins.loadFromStream(fis);
        initPins();
        element.jRepaint();
        element.jRefreshVM();
    }

    public void saveToStream(java.io.FileOutputStream fos) {
        anzPins.saveToStream(fos);
    }


}
