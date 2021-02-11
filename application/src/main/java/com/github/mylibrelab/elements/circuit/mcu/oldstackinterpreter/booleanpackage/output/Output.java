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

package com.github.mylibrelab.elements.circuit.mcu.oldstackinterpreter.booleanpackage.output;// *****************************************************************************

import java.awt.Image;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSInteger;

public class Output extends JVSMain {
    private Image image;
    private final VSInteger value = new VSInteger(0);

    public void paint(java.awt.Graphics g) {
        if (image != null) drawImageCentred(g, image);
    }

    public void onDispose() {
        if (image != null) {
            image.flush();
            image = null;
        }
    }

    private void generateCode() {
        int id = element.jGetID();

        String code = "";

        code += "  O " + value.getValue() + "\n";

        element.jSetTag(2, code);
    }

    public void init() {
        initPins(0, 0, 0, 1);
        setSize(40, 35);

        initPinVisibility(false, false, false, true);

        element.jSetInnerBorderVisibility(false);

        setPin(0, ExternalIF.C_BOOLEAN, element.PIN_INPUT);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");

        // element.jSetPinDescription(0,"out");

        setName("AVR-Output");


        generateCode();

    }


    public void initInputPins() {}

    public void initOutputPins() {}

    public void start() {}

    public void process() {}

    public void setPropertyEditor() {
        element.jAddPEItem("Out-PinNr", value, 0, 7);

    }

    public void propertyChanged(Object o) {
        generateCode();
    }


    public void loadFromStream(java.io.FileInputStream fis) {
        value.loadFromStream(fis);
        generateCode();
    }

    public void saveToStream(java.io.FileOutputStream fos) {
        value.saveToStream(fos);
    }


}
