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

package com.github.mylibrelab.elements.circuit.vectorenmatrix.vectordouble;// *****************************************************************************

import java.awt.*;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VS1DDouble;
import VisualLogic.variables.VSPropertyDialog;

public class Vector extends JVSMain {
    private final VSPropertyDialog more = new VSPropertyDialog();
    private Image image;
    private final VS1DDouble vector = new VS1DDouble(3);

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
        setSize(34 + 10, 36);
        initPins(0, 1, 0, 0);

        element.jSetInnerBorderVisibility(false);

        setPin(0, ExternalIF.C_ARRAY1D_DOUBLE, element.PIN_OUTPUT);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.png");

        setName("VectorDouble");

        element.jSetResizable(false);

    }

    public void xOnInit() {}


    public void initInputPins() {

    }

    public void initOutputPins() {
        VS1DDouble ref = new VS1DDouble(0);
        ref.copyValueFrom(vector);
        element.setPinOutputReference(0, ref);
        // Später nur noch mit Referenzen Arbeiten!
    }

    public void setPropertyEditor() {
        element.jAddPEItem("Werte..", more, 0, 0);
        localize();
    }

    private void localize() {
        int d = 6;
        String language;

        language = "en_US";
        element.jSetPEItemLocale(d, language, "Values...");


        language = "es_ES";
        element.jSetPEItemLocale(d, language, "Values...");
    }



    public void propertyChanged(Object o) {
        if (o.equals(more)) {
            MyTableEditor frm = new MyTableEditor(null, true);

            frm.setInputs(vector.getValues());
            frm.setVisible(true);


            if (frm.result) {
                double[] data = frm.getInputs();
                vector.setValues(data);
            }

        }
    }


    public void start() {

        element.notifyPin(0);
    }

    public void process() {

    }

    public void loadFromStream(java.io.FileInputStream fis) {
        vector.loadFromStream(fis);
    }

    public void saveToStream(java.io.FileOutputStream fos) {
        vector.saveToStream(fos);
    }

}
