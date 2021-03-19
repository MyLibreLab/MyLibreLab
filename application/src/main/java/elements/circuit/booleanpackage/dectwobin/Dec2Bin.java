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

package elements.circuit.booleanpackage.dectwobin;// *****************************************************************************

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

import elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSBoolean;
import VisualLogic.variables.VSInteger;

public class Dec2Bin extends JVSMain {
    private final VSInteger anzPins = new VSInteger(4);
    private VSInteger in;
    private VSBoolean[] out;
    private final Font font = new Font("Courier", Font.BOLD, 8);


    public void paint(java.awt.Graphics g) {
        if (element != null) {
            Rectangle r = element.jGetBounds();
            g.setColor(Color.BLACK);
            int mitteY = (r.y + r.height) / 2;

            g.setFont(font);
            g.drawString("Dec>Bin", 12, 8);

            g.drawString("y", 10, mitteY + 8);
            for (int i = 0; i < anzPins.getValue(); i++) {
                g.drawString("x" + i, r.width - 7, 18 + i * 10);
            }
        }
    }


    public void init() {
        initPins();


        setName("Dec->Bin");
        element.jSetCaption("Dec->Bin");
    }


    public void initPins() {
        initPins(0, anzPins.getValue(), 0, 1);

        setSize(58, 20 + (anzPins.getValue() * 10));

        element.jSetInnerBorderVisibility(true);


        for (int i = 0; i < anzPins.getValue(); i++) {
            setPin(i, ExternalIF.C_BOOLEAN, element.PIN_OUTPUT);
            element.jSetPinDescription(i, "x" + i);
        }

        setPin(anzPins.getValue(), ExternalIF.C_INTEGER, element.PIN_INPUT);
        element.jSetPinDescription(0, "y");


    }



    public void initInputPins() {
        in = (VSInteger) element.getPinInputReference(anzPins.getValue());
        if (in == null) in = new VSInteger(0);

    }

    public void initOutputPins() {
        out = new VSBoolean[anzPins.getValue()];
        for (int i = 0; i < anzPins.getValue(); i++) {
            out[i] = new VSBoolean(false);
            element.setPinOutputReference(i, out[i]);
        }

    }

    public void setPropertyEditor() {
        element.jAddPEItem("Ausgabgs-Pins", anzPins, 2, 20);
        localize();
    }

    private void localize() {
        int d = 6;
        String language;

        language = "en_US";

        element.jSetPEItemLocale(d + 0, language, "Output-Pins");

        language = "es_ES";

        element.jSetPEItemLocale(d + 0, language, "Output-Pins");
    }


    public void propertyChanged(Object o) {
        if (o == anzPins) {
            initPins();
            /*
             * anzPins.setValue(((VSInteger)outPins).getValue());
             * element.jSetSize(element.jGetWidth(), 20+(anzPins.getValue())*10);
             * element.jSetRightPins(anzPins.getValue());
             */
            element.jRepaint();
            element.jRefreshVM();
        }

    }


    private void intToBol(int value) {
        int val = value;
        for (int i = 0; i < anzPins.getValue(); i++) {
            if (val % 2 != 0) {
                out[i].setValue(true);
            } else {
                out[i].setValue(false);
            }
            element.notifyPin(i);
            val = val / 2;
        }


    }


    public void process() {
        intToBol(in.getValue());
    }



    public void loadFromStream(java.io.FileInputStream fis) {
        anzPins.loadFromStream(fis);
        initPins();
        element.jRepaint();
    }

    public void saveToStream(java.io.FileOutputStream fos) {
        anzPins.saveToStream(fos);
    }

}
