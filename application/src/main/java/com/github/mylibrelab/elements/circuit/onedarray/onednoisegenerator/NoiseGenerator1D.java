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

package com.github.mylibrelab.elements.circuit.onedarray.onednoisegenerator;/*
                                                                             * Copyright (C) 2020 MyLibreLab
                                                                             * Based on MyOpenLab by Carmelo Salafia
                                                                             * www.myopenlab.de
                                                                             * Copyright (C) 2004 Carmelo Salafia
                                                                             * cswi@gmx.de
                                                                             *
                                                                             * This program is free software: you can
                                                                             * redistribute it and/or modify
                                                                             * it under the terms of the GNU General
                                                                             * Public License as published by
                                                                             * the Free Software Foundation, either
                                                                             * version 3 of the License, or
                                                                             * (at your option) any later version.
                                                                             *
                                                                             * This program is distributed in the hope
                                                                             * that it will be useful,
                                                                             * but WITHOUT ANY WARRANTY; without even
                                                                             * the implied warranty of
                                                                             * MERCHANTABILITY or FITNESS FOR A
                                                                             * PARTICULAR PURPOSE. See the
                                                                             * GNU General Public License for more
                                                                             * details.
                                                                             *
                                                                             * You should have received a copy of the
                                                                             * GNU General Public License
                                                                             * along with this program. If not, see
                                                                             * <http://www.gnu.org/licenses/>.
                                                                             *
                                                                             */

import java.awt.*;
import java.util.Locale;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VS1DDouble;
import VisualLogic.variables.VSDouble;

public class NoiseGenerator1D extends JVSMain {
    private Image image;
    private VS1DDouble out = null;

    private final Random generator = new Random(19580427);

    private final VSDouble step = new VSDouble(1);
    private final VSDouble minX = new VSDouble(-500);
    private final VSDouble maxX = new VSDouble(+500);



    public NoiseGenerator1D() {
        out = new VS1DDouble(0);
    }

    public void xpaint(java.awt.Graphics g) {
        drawImageCentred(g, image);
    }

    public void onDispose() {
        if (image != null) {
            image.flush();
            image = null;
        }
    }

    public void init() {
        initPins(0, 1, 0, 0);
        setSize(20 + 32 + 3, 32 + 3);
        initPinVisibility(false, true, false, true);
        element.jSetInnerBorderVisibility(false);

        image = element.jLoadImage(element.jGetSourcePath() + "NoiseGenerator1D.gif");

        setPin(0, ExternalIF.C_ARRAY1D_DOUBLE, element.PIN_OUTPUT); // Out

        String strLocale = Locale.getDefault().toString();

        if (strLocale.equalsIgnoreCase("de_DE")) {
            element.jSetPinDescription(0, "1D Array");
        }
        if (strLocale.equalsIgnoreCase("en_US")) {
            element.jSetPinDescription(0, "1D Array");
        }
        if (strLocale.equalsIgnoreCase("es_ES")) {
            element.jSetPinDescription(0, "1D Array");
        }


        element.jSetCaptionVisible(true);
        element.jSetCaption("NoiseGenerator1D");
        setName("NoiseGenerator1D");

        element.setPinOutputReference(0, out);

    }


    public void initInputPins() {}


    public void initOutputPins() {
        element.setPinOutputReference(0, out);
    }



    public void setPropertyEditor() {
        element.jAddPEItem("Min-X", minX, -99999999.0, 99999999.0);
        element.jAddPEItem("Max-X", maxX, -99999999.0, 99999999.0);
        element.jAddPEItem("Schritt", step, 0.000000001, 1000.0);
        localize();
    }


    private void localize() {
        int d = 6;
        String language;

        language = "en_US";

        element.jSetPEItemLocale(d + 0, language, "Min-X");
        element.jSetPEItemLocale(d + 1, language, "Max-X");
        element.jSetPEItemLocale(d + 2, language, "Step");

        language = "es_ES";

        element.jSetPEItemLocale(d + 0, language, "Min-X");
        element.jSetPEItemLocale(d + 1, language, "Max-X");
        element.jSetPEItemLocale(d + 2, language, "Paso");
    }

    public void propertyChanged(Object o) {

        int buflen = (int) ((Math.abs(minX.getValue()) + Math.abs(maxX.getValue())) / step.getValue());

        out = new VS1DDouble(buflen);

        element.jRepaint();
    }

    public void start() {
        processor();
    }


    public void processor() {
        int c = 0;
        double x = -(out.getValue().length / 2) * step.getValue();
        for (int i = 0; i < out.getValue().length; i++) {
            out.setValue(c, generator.nextDouble());
            c++;
            x += step.getValue();
        }
        out.setChanged(true);
    }


    public void process() {
        out.setChanged(false);
        processor();
    }


    public void loadFromStream(java.io.FileInputStream fis) {
        step.loadFromStream(fis);
        minX.loadFromStream(fis);
        maxX.loadFromStream(fis);

        propertyChanged(null);
    }

    public void saveToStream(java.io.FileOutputStream fos) {
        step.saveToStream(fos);
        minX.saveToStream(fos);
        maxX.saveToStream(fos);
    }
}
