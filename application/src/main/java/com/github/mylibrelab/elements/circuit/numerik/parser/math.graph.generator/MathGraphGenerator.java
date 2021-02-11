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

package com.github.mylibrelab.elements.circuit.numerik.parser.math.graph.generator;/*
                                                                                    * Copyright (C) 2020 MyLibreLab
                                                                                    * Based on MyOpenLab by Carmelo
                                                                                    * Salafia www.myopenlab.de
                                                                                    * Copyright (C) 2004 Carmelo Salafia
                                                                                    * cswi@gmx.de
                                                                                    *
                                                                                    * This program is free software: you
                                                                                    * can redistribute it and/or modify
                                                                                    * it under the terms of the GNU
                                                                                    * General Public License as
                                                                                    * published by
                                                                                    * the Free Software Foundation,
                                                                                    * either version 3 of the License,
                                                                                    * or
                                                                                    * (at your option) any later
                                                                                    * version.
                                                                                    *
                                                                                    * This program is distributed in the
                                                                                    * hope that it will be useful,
                                                                                    * but WITHOUT ANY WARRANTY; without
                                                                                    * even the implied warranty of
                                                                                    * MERCHANTABILITY or FITNESS FOR A
                                                                                    * PARTICULAR PURPOSE. See the
                                                                                    * GNU General Public License for
                                                                                    * more details.
                                                                                    *
                                                                                    * You should have received a copy of
                                                                                    * the GNU General Public License
                                                                                    * along with this program. If not,
                                                                                    * see
                                                                                    * <http://www.gnu.org/licenses/>.
                                                                                    *
                                                                                    */

import java.awt.*;
import java.util.Locale;

import com.github.mylibrelab.elements.circuit.numerik.parser.math.graph.generator.src.expressionparser.Parser;
import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.*;

public class MathGraphGenerator extends JVSMain {
    private Image image;
    private final VSGroup group = new VSGroup();
    private VS1DDouble outX = null;
    private VS1DDouble outY = null;
    private VSString inExpr;
    private VSBoolean inCalc;

    private boolean oldValue;

    private final VSString outError = new VSString();
    private final Parser parser = new Parser("");

    private final VSDouble step = new VSDouble(1);
    private final VSDouble minX = new VSDouble(-500);
    private final VSDouble maxX = new VSDouble(+500);



    public MathGraphGenerator() {
        outX = new VS1DDouble(0);
        outY = new VS1DDouble(0);
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
        initPins(0, 2, 0, 2);
        setSize(20 + 32 + 3, 32 + 3);
        initPinVisibility(false, true, false, true);
        element.jSetInnerBorderVisibility(false);

        image = element.jLoadImage(element.jGetSourcePath() + "MathGraphGenerator.gif");

        setPin(0, ExternalIF.C_GROUP, element.PIN_OUTPUT); // Out
        setPin(1, ExternalIF.C_STRING, element.PIN_OUTPUT); // Error
        setPin(2, ExternalIF.C_STRING, element.PIN_INPUT); // Expression
        setPin(3, ExternalIF.C_BOOLEAN, element.PIN_INPUT); // calc

        String strLocale = Locale.getDefault().toString();

        if (strLocale.equalsIgnoreCase("de_DE")) {
            element.jSetPinDescription(0, "Graph");
            element.jSetPinDescription(1, "Fehler");
            element.jSetPinDescription(2, "Ausdruck");
            element.jSetPinDescription(3, "Berechnen");
        }
        if (strLocale.equalsIgnoreCase("en_US")) {
            element.jSetPinDescription(0, "Graph");
            element.jSetPinDescription(1, "Error");
            element.jSetPinDescription(2, "Expression");
            element.jSetPinDescription(3, "Calculate");
        }
        if (strLocale.equalsIgnoreCase("es_ES")) {
            element.jSetPinDescription(0, "Gráfico");
            element.jSetPinDescription(1, "Error");
            element.jSetPinDescription(2, "Expresión");
            element.jSetPinDescription(3, "Calcular");
        }


        element.jSetCaptionVisible(true);
        element.jSetCaption("MathCalc");
        setName("MathCalc");

        group.list.clear();
        group.list.add(outX);
        group.list.add(outY);

        element.setPinOutputReference(0, group);

    }


    public void initInputPins() {
        inExpr = (VSString) element.getPinInputReference(2);
        inCalc = (VSBoolean) element.getPinInputReference(3);

        if (inExpr == null) {
            inExpr = new VSString("");
        }
        if (inCalc == null) {
            inCalc = new VSBoolean(false);
        }

    }


    public void initOutputPins() {

        group.list.clear();
        group.list.add(outX);
        group.list.add(outY);

        element.setPinOutputReference(0, group);
        element.setPinOutputReference(1, outError);
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

        outX = new VS1DDouble(buflen);
        outY = new VS1DDouble(buflen);

        element.jRepaint();
    }



    public void processor() {
        double y = 0;
        int c = 0;
        double x = -(outX.getValue().length / 2) * step.getValue();
        for (int i = 0; i < outX.getValue().length; i++) {
            y = 0;
            parser.clearVars();
            parser.addVar("x", x);
            y = parser.parseString();
            outX.setValue(c, x);
            outY.setValue(c, y);
            c++;
            x += step.getValue();
        }
        group.setChanged(true);
        element.notifyPin(0);
    }


    public void process() {
        if (inExpr instanceof VSString && inCalc instanceof VSBoolean) {
            if (inCalc.getValue() == true) {
                oldValue = inCalc.getValue();
                parser.setExpression(inExpr.getValue());

                if (parser.getErrorMessage().length() > 0) {
                    outError.setValue(parser.getErrorMessage());
                    outError.setChanged(true);
                    parser.delErrorMessage();
                    element.notifyPin(1);
                } else
                    processor();

            }
        }
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
