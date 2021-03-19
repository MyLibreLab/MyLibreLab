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

package elements.circuit.countertimer.timer;// *****************************************************************************

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSBoolean;
import VisualLogic.variables.VSInteger;

public class XTimer extends JVSMain {
    private boolean oki = false;
    private final int counter = 0;
    private final VSInteger timerA = new VSInteger(100);
    private final VSInteger timerB = new VSInteger(100);
    private Image image;
    private final VSBoolean out = new VSBoolean();

    private javax.swing.Timer timer;

    public void onDispose() {
        if (image != null) {
            image.flush();
            image = null;
        }
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }


    public void paint(java.awt.Graphics g) {
        drawImageCentred(g, image);
    }

    public void init() {
        initPins(0, 1, 0, 0);
        setSize(40, 30);

        initPinVisibility(false, true, false, false);


        String fileName = element.jGetSourcePath() + "icon.gif";
        image = element.jLoadImage(fileName);

        setPin(0, ExternalIF.C_BOOLEAN, element.PIN_OUTPUT);
        setName("Timer");
        out.setPin(0);
    }


    public void initOutputPins() {
        element.setPinOutputReference(0, out);
    }

    public void process() {

        if (oki) {
            oki = false;
            out.setValue(true);
        } else {
            oki = true;
            out.setValue(false);
        }
        element.notifyPin(0);
    }


    public void start() {

        timer = new javax.swing.Timer(timerA.getValue(), new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                process();
            }
        });

        timer.start();
    }

    public void stop() {
        if (timer != null) timer.stop();
    }

    public void setPropertyEditor() {
        element.jAddPEItem("High-Pegel", timerA, 0, 3600000);
        element.jAddPEItem("Low-Pegel", timerB, 0, 3600000);
        localize();
    }


    private void localize() {
        int d = 6;
        String language;

        language = "en_US";

        element.jSetPEItemLocale(d + 0, language, "High-Level");
        element.jSetPEItemLocale(d + 1, language, "Low-Level");

        language = "es_ES";

        element.jSetPEItemLocale(d + 0, language, "Tiempo activado");
        element.jSetPEItemLocale(d + 1, language, "Tiempo desactivado");

    }

    public void propertyChanged(Object o) {
        element.jRepaint();
    }

    public void loadFromStream(java.io.FileInputStream fis) {
        timerA.loadFromStream(fis);
        timerB.loadFromStream(fis);
    }

    public void saveToStream(java.io.FileOutputStream fos) {
        timerA.saveToStream(fos);
        timerB.saveToStream(fos);
    }

}
