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

package elements.circuit.gruppe.gruppierer;// *****************************************************************************

import java.awt.Image;

import elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSComboBox;
import VisualLogic.variables.VSGroup;
import VisualLogic.variables.VSInteger;
import VisualLogic.variables.VSObject;

public class Gruppierer extends JVSMain {
    private final VSGroup out = new VSGroup();
    private final VSInteger anzPins = new VSInteger(2);
    private Image image;


    public void xpaint(java.awt.Graphics g) {
        drawImageCentred(g, image);
    }

    public void init() {
        initPins(0, 1, 0, anzPins.getValue());
        setSize(50, 15 + (anzPins.getValue() * 10));
        initPinVisibility(false, true, false, true);

        element.jSetInnerBorderVisibility(true);

        element.jSetResizable(false);
        element.jInitPins();
        setPin(0, ExternalIF.C_GROUP, element.PIN_OUTPUT);

        for (int i = 0; i < anzPins.getValue(); i++) {
            setPin(i + 1, ExternalIF.C_VARIANT, element.PIN_INPUT);
        }

        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");
        setName("#GRUPPIERER#");
    }

    public void initInputPins() {
        out.list.clear();
        for (int i = 1; i < anzPins.getValue() + 1; i++) {
            Object o = element.getPinInputReference(i);
            if (o instanceof VSObject) {
                out.list.add(o);
            } else {
                out.list.add(new VSObject());
            }
        }
    }

    public void initOutputPins() {
        element.setPinOutputReference(0, out);
    }



    public void setPropertyEditor() {
        element.jAddPEItem("Anzahl Pins", anzPins, 2, 50);
        localize();
    }


    private void localize() {
        int d = 6;
        String language;

        language = "en_US";

        element.jSetPEItemLocale(d + 0, language, "Pins Count");

        language = "es_ES";

        element.jSetPEItemLocale(d + 0, language, "Pins Count");
    }

    private void setPinDT2(int pinIndex, VSComboBox dtPin) {
        int dt = element.jGetDataType(dtPin.getItem(dtPin.selectedIndex));
        setPin(pinIndex, dt, element.PIN_INPUT);
    }


    public void propertyChanged(Object o) {
        if (o.equals(anzPins)) {
            init();
        }

        element.jRepaint();
    }

    public void checkPinDataType() {
        boolean pinIn;

        for (int i = 1; i < element.jGetAnzahlPinsLeft() + 1; i++) {
            pinIn = element.hasPinWire(i);
            if (pinIn) {
                int dtA = element.C_VARIANT;
                dtA = element.jGetPinDrahtSourceDataType(i);

                element.jSetPinDataType(i, dtA);
                element.jSetPinIO(i, element.PIN_INPUT);

            } else {
                element.jSetPinDataType(i, element.C_VARIANT);
                element.jSetPinIO(i, element.PIN_INPUT);
            }
        }

        element.jRepaint();
    }


    public void start() {}

    public void stop() {}

    public void process() {

        element.notifyPin(0);
    }


    public void loadFromStream(java.io.FileInputStream fis) {
        anzPins.loadFromStream(fis);
        init();
    }

    public void saveToStream(java.io.FileOutputStream fos) {
        anzPins.saveToStream(fos);
    }

}
