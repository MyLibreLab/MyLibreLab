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

package com.github.mylibrelab.elements.circuit.mcu.oldstackinterpreter.booleanpackage.input;// *****************************************************************************

import java.awt.*;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSBoolean;
import VisualLogic.variables.VSInteger;

public class Input extends JVSMain {
    private final VSInteger value = new VSInteger(0);
    private final VSBoolean out = new VSBoolean();
    private boolean changed = false;
    private Image image;

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
        /*
         * code+="  DIM ELEMENT"+id+"_OLDVALUE\n";
         * element.jSetTag(4, code); // Globals
         *
         * code="";
         * code+="  GET_BIT PORTB,"+value.getValue()+"  \n";
         * code+="  POP R0  \n";
         * code+="  CMP R0, ELEMENT"+id+"_OLDVALUE\n";
         * code+="  JMP_IF_A=B ELEMENT"+id+"_EXIT\n";
         * code+="  GET_BIT PORTB,"+value.getValue()+"\n";
         * code+="  POP ELEMENT"+id+"_OLDVALUE  \n";
         * code+="  MOV %pin0%,ELEMENT"+id+"_OLDVALUE \n";
         * code+="  %notify0%\n";
         * code+="ELEMENT"+id+"_EXIT: \n";
         * element.jSetTag(2, code);
         *
         * // Als Event_handler registrieren
         * // D.h. das diese Funktion in der Hauptschleife aufgerufen wird!
         * code="  CALL ELEMENT"+id+"\n";
         * element.jSetTag(1, code);
         */



        code = "";
        code += "  E " + value.getValue() + "  \n";
        element.jSetTag(2, code);

    }


    public void init() {
        initPins(0, 1, 0, 0);
        setSize(32 + 11, 30);

        initPinVisibility(false, true, false, false);

        element.jSetInnerBorderVisibility(false);

        setPin(0, ExternalIF.C_BOOLEAN, element.PIN_OUTPUT);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");

        element.jSetPinDescription(0, "out");


        setName("Input-Pin[0]");

        generateCode();
    }


    public void initInputPins() {}

    public void initOutputPins() {
        element.setPinOutputReference(0, out);
    }



    public void start() {
        changed = true;
        // out.setValue(value.getValue());
        element.notifyPin(0);

    }

    public void process() {
        if (changed) {
            changed = false;

        }
    }



    public void setPropertyEditor() {
        element.jAddPEItem("Pin-Nr", value, 0, 7);
        localize();
    }

    private void localize() {
        int d = 6;
        String language;

        language = "en_US";

        element.jSetPEItemLocale(d + 0, language, "Pin-Nr");

        language = "es_ES";

        element.jSetPEItemLocale(d + 0, language, "Pin-Nr");
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
