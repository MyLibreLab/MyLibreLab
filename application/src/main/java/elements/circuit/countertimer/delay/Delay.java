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

package elements.circuit.countertimer.delay;// *****************************************************************************

import java.awt.Color;
import java.awt.Image;

import elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSInteger;
import VisualLogic.variables.VSObject;

public class Delay extends JVSMain {
    private Image image;
    private VSObject in;
    private VSObject out = new VSObject();
    private Color color = Color.BLACK;

    private boolean started = false;
    private final VSInteger interval = new VSInteger(500);
    private VSObject vv = null;

    private int counter = 0;

    public Delay() {}

    public void onDispose() {
        if (image != null) {
            image.flush();
            image = null;
        }
    }


    public void paint(java.awt.Graphics g) {
        drawImageCentred(g, image);
    }

    public void init() {
        initPins(0, 1, 0, 1);
        setSize(50, 34);
        element.jSetInnerBorderVisibility(true);
        element.jSetTopPinsVisible(false);
        element.jSetBottomPinsVisible(false);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.png");

        element.jInitPins();

        setPin(0, ExternalIF.C_VARIANT, element.PIN_OUTPUT);
        setPin(1, ExternalIF.C_VARIANT, element.PIN_INPUT);

        setName("Delay");
        out.setPin(0);


    }

    public void start() {
        // timer.setDelay(interval.getValue());
        element.jNotifyMeForClock();
        started = false;
        counter = 0;
    }

    public void stop() {
        // if (timer!=null) timer.stop();
        started = false;
    }



    // Diese Methode ist Impulse sicher (zb: Boolean Taster)
    // Deshalb das out Array!
    public void process() {
        if (in instanceof VSObject) {
            vv = in;
            out.copyValueFrom(vv);
            started = true;
        }
    }


    public void processClock() {
        if (started) {
            if (counter >= interval.getValue()) {
                counter = 0;

                out.setChanged(true);
                element.notifyPin(0);
                started = false;
            }
            counter++;
        }
    }

    /*
     * public void processX()
     * {
     * if (started)
     * {
     * if (in instanceof VSObject)
     * {
     * out.setChanged(true);
     * element.notifyPin(0);
     * started=false;
     * }
     * }
     * }
     */



    public void setPropertyEditor() {
        element.jAddPEItem("Delay [Ticks]", interval, 0, 1000000);
        localize();
    }


    private void localize() {
        int d = 6;
        String language;

        language = "en_US";

        element.jSetPEItemLocale(d + 0, language, "Delay [Ticks]");

        language = "es_ES";

        element.jSetPEItemLocale(d + 0, language, "Delay [Ticks]");
    }


    public void initInputPins() {
        in = (VSObject) element.getPinInputReference(1);
    }

    public void initOutputPins() {
        out = element.jCreatePinDataType(0);
        element.setPinOutputReference(0, out);
    }


    public void checkPinDataType() {
        boolean pinIn = element.hasPinWire(1);

        int dtA = element.C_VARIANT;

        if (pinIn) dtA = element.jGetPinDrahtSourceDataType(1);


        element.jSetPinIO(1, element.PIN_INPUT);

        color = Color.BLACK;

        if (pinIn) {
            element.jSetPinDataType(1, dtA);
            element.jSetPinDataType(0, dtA);
            color = element.jGetDTColor(dtA);
        } else {
            element.jSetPinDataType(1, element.C_VARIANT);
            element.jSetPinDataType(0, element.C_VARIANT);
        }

        element.jRepaint();
    }

    public void loadFromStream(java.io.FileInputStream fis) {
        interval.loadFromStream(fis);
    }

    public void saveToStream(java.io.FileOutputStream fos) {
        interval.saveToStream(fos);
    }

}
