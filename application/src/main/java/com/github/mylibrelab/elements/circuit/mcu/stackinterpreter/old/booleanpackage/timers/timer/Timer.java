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

package com.github.mylibrelab.elements.circuit.mcu.stackinterpreter.old.booleanpackage.timers.timer;// *****************************************************************************

import java.awt.*;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSBoolean;
import VisualLogic.variables.VSInteger;

public class Timer extends JVSMain {
    private final VSInteger timerNr = new VSInteger(0);
    private final VSInteger intervall = new VSInteger(10);

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
         * code+="  DIM ELEMENT"+id+"_OLDVALUE, BYTE\n";
         * element.jSetTag(4, code); // Globals
         */

        // Initialisierung
        // D.h. das diese Funktion nur 1x aufgerufen wird!
        code = "";
        code += "  PUSHB " + timerNr.getValue() + "// TimerNr \n";
        code += "  PUSHB " + intervall.getValue() + "// Intervall \n";
        code += "  TIMER_SET_INTERVALL\n";

        code += "  PUSHB " + timerNr.getValue() + "// TimerNr \n";
        code += "  TIMER_START\n";

        code += "  PUSHB " + timerNr.getValue() + "// TimerNr \n";
        code += "  GET_LABEL_ADR  ELEMENT" + id + " // PUSH LabelAdresse \n";
        code += "  CALL_WHEN_TIMER_INCREASED\n";
        element.jSetTag(5, code); // im Init Block


        // HauptCode
        code = "";

        code += "  PUSHB " + timerNr.getValue() + "// TimerNr \n";
        code += "  PUSHB 0 // Value \n";
        code += "  TIMER_SET\n";

        code += "  PUSHB %pin0%\n";
        code += "  NOT \n";
        code += "  POPB %pin0%\n";

        code += "  %notify0% \n";

        element.jSetTag(2, code);


    }


    public void init() {
        initPins(0, 1, 0, 0);
        setSize(32 + 13, 23);

        initPinVisibility(false, true, false, false);

        element.jSetInnerBorderVisibility(false);

        setPin(0, ExternalIF.C_BOOLEAN, element.PIN_OUTPUT);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");

        element.jSetPinDescription(0, "out");

        setName("AVR-Timer");

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
        element.jAddPEItem("Timer-Nr", timerNr, 0, 10);
        element.jAddPEItem("Intervall", intervall, 0, 65535);
        localize();
    }

    private void localize() {
        int d = 6;
        String language;

        language = "en_US";

        element.jSetPEItemLocale(d + 0, language, "Timer-Nr");

        language = "es_ES";

        element.jSetPEItemLocale(d + 0, language, "Timer-Nr");
    }

    public void propertyChanged(Object o) {
        generateCode();
    }


    public void loadFromStream(java.io.FileInputStream fis) {
        timerNr.loadFromStream(fis);
        intervall.loadFromStream(fis);
        generateCode();
    }

    public void saveToStream(java.io.FileOutputStream fos) {
        timerNr.saveToStream(fos);
        intervall.saveToStream(fos);
    }


}
