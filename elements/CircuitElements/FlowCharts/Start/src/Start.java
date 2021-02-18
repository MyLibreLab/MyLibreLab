//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia (cswi@gmx.de)                         *
//*                                                                           *
//* This library is free software; you can redistribute it and/or modify      *
//* it under the terms of the GNU Lesser General Public License as published  *
//* by the Free Software Foundation; either version 2.1 of the License,       *
//* or (at your option) any later version.                                    *
//* http://www.gnu.org/licenses/lgpl.html                                     *
//*                                                                           *
//* This library is distributed in the hope that it will be useful,           *
//* but WITHOUTANY WARRANTY; without even the implied warranty of             *
//* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                      *
//* See the GNU Lesser General Public License for more details.               *
//*                                                                           *
//* You should have received a copy of the GNU Lesser General Public License  *
//* along with this library; if not, write to the Free Software Foundation,   *
//* Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA                  *
//*****************************************************************************

import VisualLogic.*;
import VisualLogic.variables.*;
import java.awt.*;
import tools.*;
import java.awt.geom.Rectangle2D;
import MyParser.*;

public class Start extends MainFlow {

    private Image image;
    private VSBasisIF basis;
    private VSBoolean in;
    private VSFlowInfo out = new VSFlowInfo();

    private String methodName = "";
    private String varDef;
    private String[] defs;

    @Override
    public void paint(java.awt.Graphics g) {
        if (element != null) {
            Rectangle bounds = element.jGetBounds();
            Graphics2D ggg;
            ggg = (Graphics2D) g;

            ggg.setFont(font);

            int mitteX = bounds.x + (bounds.width) / 2;
            int mitteY = bounds.y + (bounds.height) / 2;

            int distanceY = 10;

            ggg.setColor(new Color(150, 255, 150));
            ggg.fillRoundRect(bounds.x, mitteY - distanceY, bounds.width, 2 * distanceY, 20, 20);
            ggg.setColor(Color.BLACK);
            ggg.drawRoundRect(bounds.x, mitteY - distanceY, bounds.width, 2 * distanceY, 20, 20);

            String caption = variable.getValue();

            FontMetrics fm = ggg.getFontMetrics();
            Rectangle2D r = fm.getStringBounds(caption, ggg);

            ggg.setColor(Color.BLACK);
            g.drawString(caption, mitteX - (int) (r.getWidth() / 2), (int) (mitteY + fm.getHeight() / 2) - 3);

        }
        super.paint(g);
    }

    private void evalMethodExpression(String expression) {
        //String expression = "Test ( double a, string salafia, boolean c   )   ";

        if (expression.length() == 0) {
            return;
        }

        int idx1 = expression.indexOf("(");
        if (idx1 > -1) {
            methodName = expression.substring(0, idx1);
            //System.out.println("MethodName=" + methodName);

            int idx2 = expression.indexOf(")");
            if (idx2 > -1) {
                varDef = expression.substring(idx1 + 1, idx2);
                varDef = varDef.trim();
               // System.out.println("Definitionen : " + varDef);

                defs = varDef.split(",");

            } else {
                element.jShowMessage("ERROR : coud not find \")\"  example: main(String args)");
            }
        } else {
            element.jShowMessage("ERROR : coud not find \"(\" example: main(String args)");
        }

    }

    @Override
    public void init() {
        standardWidth = 130;
        width = standardWidth;
        height = 40;
        toInclude = "----";

        initPins(1, 0, 1, 0);
        setSize(width, height);
        initPinVisibility(true, true, true, true);

        element.jSetInnerBorderVisibility(false);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");

        setPin(0, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
        setPin(1, ExternalIF.C_FLOWINFO, ExternalIF.PIN_OUTPUT);

        element.jSetResizable(false);
        element.jSetCaptionVisible(false);
        element.jSetCaption("START");

        setName("#FLOWCHART_START#");
        variable.setValue("");
    }

    @Override
    public void xOnInit() {
        super.xOnInit();
    }

    @Override
    public void checkPinDataType() {
        evalMethodExpression(variable.getValue());
    }

    @Override
    public void start() {
        out.returnValue = null;
        out.source = null;

        out.variablenListe.clear();
        out.parameterDefinitions.clear();
        
     
        element.jSetTag(0, methodName);
        element.jSetTag(1, out);

        if (defs != null) {
            String val;

            for (int i = 0; i < defs.length; i++) {
                val = defs[i].trim();

                if (val.length() > 0) {
                    String[] defs2 = val.split(" ");

                    if (defs2.length == 2) {
                        String dataType = defs2[0];
                        String varName = defs2[1];

                        System.out.println("DT : " + dataType);
                        System.out.println("VN : " + varName);

                        int dt = out.getDataType(dataType);
                        if (dt > -1) {
                            out.addParamter(varName, dt);
                            System.out.println("------------*** " + i + "   ->" + varName + " - " + dataType);
                        } else {
                            element.jShowMessage("ERROR : coud not find datatype : " + dataType);
                        }

                    } else {
                        element.jShowMessage("ERROR : in expression!");
                    }

                }
            }
        }
    }

    @Override
    public void initInputPins() {
        in = (VSBoolean) element.getPinInputReference(0);
    }

    @Override
    public void initOutputPins() {
        element.setPinOutputReference(1, out);
    }

    @Override
    public void process() {
        if (in instanceof VSBoolean && in.getValue()) {

            //System.out.println(in.getValue());
            // Lokale Variablen generieren, wenn mit dem Boolean Eingan gestartet wird.

            for (int j = 0; j < out.parameterDefinitions.size(); j++) {
                OpenVariable var = (OpenVariable) out.parameterDefinitions.get(j);
                if (var != null) {
                    out.addVariable(var.name, var.datatype);
                }

            }

            element.notifyPin(1);
        }
    }

    @Override
    public void processMethod(VSFlowInfo flowInfo) {
       
        element.notifyPin(1);
    }

    @Override
    public void loadFromStream(java.io.FileInputStream fis) {
        variable.loadFromStream(fis);
        evalMethodExpression(variable.getValue());
    }

    @Override
    public void saveToStream(java.io.FileOutputStream fos) {
        variable.saveToStream(fos);
    }

}
