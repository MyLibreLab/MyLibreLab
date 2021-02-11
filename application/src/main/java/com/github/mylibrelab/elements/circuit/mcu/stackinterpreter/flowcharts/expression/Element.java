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

package com.github.mylibrelab.elements.circuit.mcu.stackinterpreter.flowcharts.expression;// *****************************************************************************

import java.awt.*;

import com.github.mylibrelab.elements.tools.MCUMainFlow;

import VisualLogic.VSBasisIF;
import VisualLogic.variables.VSFlowInfo;
import VisualLogic.variables.VSString;

public class Element extends MCUMainFlow {
    private final VSFlowInfo out = new VSFlowInfo();
    private final VSString paramA = new VSString("");
    private Image image;
    private VSBasisIF basis;
    private VSFlowInfo in = null;

    public void paint(java.awt.Graphics g) {
        if (element != null) {
            Rectangle bounds = element.jGetBounds();
            Graphics2D g2 = (Graphics2D) g;

            g2.setFont(font);

            int mitteX = bounds.x + (bounds.width) / 2;
            int mitteY = bounds.y + (bounds.height) / 2;

            int distanceY = 10;

            g2.setColor(new Color(204, 204, 255));
            g2.fillRect(bounds.x, mitteY - distanceY, bounds.width, 2 * distanceY);
            g2.setColor(Color.BLACK);
            g2.drawRect(bounds.x, mitteY - distanceY, bounds.width, 2 * distanceY);

            String caption = "";

            caption += paramA.getValue();

            drawCaption(g2, caption.trim(), 0);
        }
        super.paint(g);
    }


    private void generateCode() {
        String code = "";
        String pa = "";
        String pb = "";
        String res = "";

        int id = element.jGetID();

        // Überprüfe ob die Variablen oder Konstanten korrekt sind
        // String errors="";
        /*
         * if (item.paramCount>=1) errors+=checkProperty("paramA", paramA.getValue(), item.paramAType);
         * if (item.paramCount>=2) errors+=checkProperty("paramB", paramB.getValue(), item.paramBType);
         */
        // errors+=checkProperty("result", result.getValue(), MCUMainFlow.ONLY_VAR);
        // if (errors.length()>0) element.jShowMessage(errors);


        String varX = "";

        pa = generateMCUCodeFromExpression(paramA.getValue());

        // nur Variable
        /*
         * if (isVariable(result.getValue() ))
         * {
         * res="STORE_I "+result.getValue();
         * }
         */

        code += "\n";
        code += "  ELEMENT" + id + ":   //Expression " + paramA.getValue() + "\n";

        if (pa.length() > 0) code += "    " + pa + " \n";
        if (res.length() > 0) code += "    " + res + " \n";

        if (element.hasPinWire(1)) {
            code += "    JMP %nextElement1%\n";
        }

        code += "\n";
        element.jSetTag(2, code);

    }

    public void xOnInit() {
        super.xOnInit();
    }


    public void start() {
        generateCode();
    }


    public void init() {
        standardWidth = 130;
        width = standardWidth;
        height = 40;

        initPins(1, 0, 1, 0);
        setSize(width, height);
        initPinVisibility(true, true, true, true);

        element.jSetInnerBorderVisibility(false);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");

        setPin(0, element.C_FLOWINFO, element.PIN_INPUT);
        setPin(1, element.C_FLOWINFO, element.PIN_OUTPUT);

        element.jSetCaptionVisible(false);
        element.jSetCaption("Expression");


        setName("#MCU-FLOWCHART-EXPRESSION#");
    }

    public void initInputPins() {
        in = (VSFlowInfo) element.getPinInputReference(0);
        basis = element.jGetBasis();
    }


    public void initOutputPins() {
        element.setPinOutputReference(1, out);
    }

    public void setPropertyEditor() {
        element.jAddPEItem("expression", paramA, 0, 0);
        // element.jAddPEItem("result",result,0,0);

        // localize();
    }

    public void propertyChanged(Object o) {
        element.jRepaint();
    }


    public void process() {

    }

    public void loadFromStream(java.io.FileInputStream fis) {
        paramA.loadFromStream(fis);
        // result.loadFromStream(fis);
    }


    public void saveToStream(java.io.FileOutputStream fos) {
        paramA.saveToStream(fos);
        // result.saveToStream(fos);
    }

}
