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

public class ProcessSetter extends MainFlow {

    private Image image;
    private VSFlowInfo inStart = null;
    private VSObject input = null;
    private VSFlowInfo out = new VSFlowInfo();
    private VSBasisIF basis;
    private VSObject value;

    @Override
    public void paint(java.awt.Graphics g) {
        if (element != null) {
            g2 = (Graphics2D) g;
            Rectangle bounds = element.jGetBounds();
            g2.setFont(font);

            int mitteX = bounds.x + (bounds.width) / 2;
            int mitteY = bounds.y + (bounds.height) / 2;

            String caption = "-> " + variable.getValue();
            FontMetrics fm = g2.getFontMetrics();
            Rectangle2D r = fm.getStringBounds(caption, g2);

            g2.setColor(new Color(204, 204, 255));
            Polygon p = new Polygon();
            p.addPoint(bounds.x + 10, bounds.y);
            p.addPoint(bounds.x + bounds.width - 1, bounds.y);
            p.addPoint(bounds.x + bounds.width - 11, bounds.y + bounds.height);
            p.addPoint(bounds.x, bounds.y + bounds.height);
            g2.fillPolygon(p);
            g2.setColor(Color.black);
            g2.drawPolygon(p);

            g.drawString(caption, mitteX - (int) (r.getWidth() / 2), (int) (mitteY + fm.getHeight() / 2) - 5);
            //g.drawString(caption,bounds.x+5,bounds.y+((bounds.height) /2)+3);
        }
        super.paint(g);
    }

    @Override
    public void init() {
        standardWidth = 130;
        width = standardWidth;
        height = 40;

        initPins(1, 0, 1, 1);
        setSize(width, height);
        initPinVisibility(true, true, true, true);

        element.jSetInnerBorderVisibility(false);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");

        setPin(0, ExternalIF.C_FLOWINFO, ExternalIF.PIN_INPUT);
        setPin(1, ExternalIF.C_FLOWINFO, ExternalIF.PIN_OUTPUT);
        setPin(2, ExternalIF.C_VARIANT, ExternalIF.PIN_INPUT);

        element.jSetResizable(false);
        element.jSetCaptionVisible(false);
        element.jSetCaption("ProcessSetter");

        setName("#FLOWCHART_ProcessSetter#");
    }

    @Override
    public void xOnInit() {
        super.xOnInit();
        basis = element.jGetBasis();
    }

    @Override
    public void initInputPins() {
        inStart = (VSFlowInfo) element.getPinInputReference(0);
        input = (VSObject) element.getPinInputReference(2);

        if (inStart == null) {
            inStart = new VSFlowInfo();
        }
        
        if (input != null) {

                if (input instanceof VSBoolean) {
                    value = new VSBoolean(false);
                } else {
                    if (input instanceof VSString) {
                        value = new VSString();
                    } else {
                        if (input instanceof VSDouble) {
                            value = new VSDouble();
                        }else{
                          if (input instanceof VSInteger) {
                            value = new VSInteger();  
                          }
                        }
                    }
                }
                if (value == null) {
                    element.jShowMessage("Types unkompatible or variable unknown in expression : " + variable.getValue());
                } else {
                    value.copyValueFrom(input);
                }

            } else {
                element.jShowMessage("Problems with Input : " + variable.getValue());
            }

    }

    @Override
    public void initOutputPins() {
        element.setPinOutputReference(1, out);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void checkPinDataType() {
        if (element == null) {
            return;
        }

        if (basis != null && variable.getValue().trim().length() > 0) {
            int varDT = basis.vsGetVariableDT(variable.getValue());
            if (varDT > -1) {

                int dt = ExternalIF.C_VARIANT;

                if (varDT == 0) {
                    dt = ExternalIF.C_DOUBLE;
                }
                if (varDT == 1) {
                    dt = ExternalIF.C_STRING;
                }
                if (varDT == 2) {
                    dt = ExternalIF.C_BOOLEAN;
                }
                if (varDT == 3) {
                    dt = ExternalIF.C_INTEGER;
                }
                //System.err.println("DT="+varDT);

                element.jSetPinDataType(2, dt);

            }
            element.jRepaint();
        }

    }


    @Override
    public void elementActionPerformed(ElementActionEvent evt) {
        if (evt.getSourcePinIndex() == 0) {
            if (basis != null) {

                if (basis.vsCopyVSObjectToVariable(value, variable.getValue()) == false) {
                    element.jShowMessage("Types unkompatible or variable unknown");
                }

                out.copyValueFrom(inStart);
                element.notifyPin(1);
            }
        }

        if (evt.getSourcePinIndex() == 2) {
            if (input != null) {

                if (input instanceof VSBoolean) {
                    value = new VSBoolean(false);
                } else {
                    if (input instanceof VSString) {
                        value = new VSString();
                    } else {
                        if (input instanceof VSDouble) {
                            value = new VSDouble();
                        }
                    }
                }
                if (value == null) {
                    element.jShowMessage("Types unkompatible or variable unknown in expression : " + variable.getValue());
                } else {
                    value.copyValueFrom(input);
                }

            } else {
                element.jShowMessage("Problems with Input : " + variable.getValue());
            }
        }
    }
}
