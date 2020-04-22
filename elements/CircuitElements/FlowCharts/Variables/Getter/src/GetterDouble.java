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

import MyParser.OpenVariable;
import VisualLogic.*;
import VisualLogic.variables.*;
import java.awt.*;
import tools.*;
import java.awt.geom.Rectangle2D;

public class GetterDouble extends MainFlow {

    private Image image;
    private VSBasisIF basis;
    private VSObject out = new VSObject();

    @Override
    public void paint(java.awt.Graphics g) {
        if (element != null) {
            g2 = (Graphics2D) g;
            Rectangle bounds = element.jGetBounds();
            g2.setFont(font);
            g2.setColor(Color.black);

            String caption = "get(" + variable.getValue() + ")";
            FontMetrics fm = g2.getFontMetrics();

            g.drawString(caption, bounds.x + 5, ((bounds.height) / 2) + 5);
            g.drawRect(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1);
        }
        super.paint(g);
    }

    @Override
    public void resizeWidth() {
        if (g2 != null) {
            g2.setFont(font);
            String caption = variable.getValue();

            FontMetrics fm = g2.getFontMetrics();
            Rectangle2D r = fm.getStringBounds(caption, g2);
            Rectangle2D r2 = fm.getStringBounds(toInclude, g2);

            int newWidth = (int) (r.getWidth() + r2.getWidth());
            if (newWidth > standardWidth) {
                int diff = (width - newWidth);
                element.jSetSize(newWidth, height);
                element.jSetLeft(element.jGetLeft() + diff);
                width = newWidth;
            } else {
                int diff = (width - standardWidth);
                width = standardWidth;
                element.jSetSize(width, height);
                element.jSetLeft(element.jGetLeft() + diff);
            }
        }

    }

    @Override
    public void init() {
        standardWidth = 60;
        width = standardWidth;
        height = 40;
        toInclude = "____GET()";

        initPins(0, 1, 0, 0);
        setSize(width, height);
        element.jRepaint();
        resizeWidth();
        initPinVisibility(false, true, false, false);

        element.jSetInnerBorderVisibility(true);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");

        setPin(0, element.C_DOUBLE, element.PIN_OUTPUT);

        element.jSetResizable(false);
        element.jSetCaptionVisible(false);
        element.jSetCaption("GetterVar");

        setName("#FLOWCHART_GetterVar#");
    }

    @Override
    public void xOnInit() {
        super.xOnInit();
    }

    @Override
    public void initInputPins() {
    }

    @Override
    public void initOutputPins() {
        element.setPinOutputReference(0, out);
        basis = element.jGetBasis();
    }

    @Override
    public void checkPinDataType() {
        if (element == null) {
            return;
        }

        basis = element.jGetBasis();
        if (basis != null && variable.getValue().trim().length() > 0) {
            int varDT = basis.vsGetVariableDT(variable.getValue());
            if (varDT > -1) {

                int dt = ExternalIF.C_VARIANT;

                switch (varDT) {
                    case OpenVariable.C_DOUBLE:
                        dt = ExternalIF.C_DOUBLE;
                        out = new VSDouble(0);
                        break;
                    case OpenVariable.C_STRING:
                        dt = ExternalIF.C_STRING;
                        out = new VSString("");
                        break;
                    case OpenVariable.C_BOOLEAN:
                        dt = ExternalIF.C_BOOLEAN;
                        out = new VSBoolean(false);
                        break;
                    case OpenVariable.C_INTEGER:
                        dt = ExternalIF.C_INTEGER;
                        out = new VSInteger(0);
                        break;
                    case OpenVariable.C_DOUBLE_1D:
                        dt = ExternalIF.C_ARRAY1D_DOUBLE;
                        out = new VS1DDouble(200);
                        break;
                    case OpenVariable.C_STRING_1D:
                        dt = ExternalIF.C_ARRAY1D_STRING;
                        out = new VS1DString(200);
                        break;
                    case OpenVariable.C_BOOLEAN_1D:
                        dt = ExternalIF.C_ARRAY1D_BOOLEAN;
                        out = new VS1DBoolean(200);
                        break;
                    case OpenVariable.C_INTEGER_1D:
                        dt = ExternalIF.C_ARRAY1D_DOUBLE;
                        out = new VS1DInteger(200);
                        break;
                    case OpenVariable.C_DOUBLE_2D:
                        dt = ExternalIF.C_ARRAY2D_DOUBLE;
                        out = new VS2DDouble(200, 200);
                        break;
                    case OpenVariable.C_STRING_2D:
                        dt = ExternalIF.C_ARRAY2D_STRING;
                        out = new VS2DString(200, 200);
                        break;
                    case OpenVariable.C_BOOLEAN_2D:
                        dt = ExternalIF.C_ARRAY2D_BOOLEAN;
                        out = new VS2DBoolean(200, 200);
                        break;
                    case OpenVariable.C_INTEGER_2D:
                        dt = ExternalIF.C_ARRAY2D_INTEGER;
                        out = new VS2DInteger(200, 200);
                        break;

                }

                element.jSetPinDataType(0, dt);

            }

            element.jRepaint();
        }
    }

    @Override
    public void start() {
        basis.vsNotifyMeWhenVariableChanged(element, variable.getValue());
    }

    @Override
    public void process() {

        //element.jShowMessage("Getter!!!!!!"+basis);
        if (basis != null) {

            if (basis.vsCopyVariableToVSObject(variable.getValue(), out) == true) {
                element.notifyPin(0);
            } else {
                element.jShowMessage("Types unkompatible or variable unknown");
            }

        }
    }

}
