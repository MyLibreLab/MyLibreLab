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

package com.github.mylibrelab.elements.circuit.MCU.oldStackInterpreter.FlowCharts.Node.src;// *****************************************************************************

// * Element of MyOpenLab Library *
// * *
// * Copyright (C) 2004 Carmelo Salafia (cswi@gmx.de) *
// * *
// * This library is free software; you can redistribute it and/or modify *
// * it under the terms of the GNU Lesser General Public License as published *
// * by the Free Software Foundation; either version 2.1 of the License, *
// * or (at your option) any later version. *
// * http://www.gnu.org/licenses/lgpl.html *
// * *
// * This library is distributed in the hope that it will be useful, *
// * but WITHOUTANY WARRANTY; without even the implied warranty of *
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. *
// * See the GNU Lesser General Public License for more details. *
// * *
// * You should have received a copy of the GNU Lesser General Public License *
// * along with this library; if not, write to the Free Software Foundation, *
// * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA *
// *****************************************************************************



public class Node extends MainFlow {
    private Image image;
    private VSFlowInfo inA;
    private VSFlowInfo inB;
    private VSFlowInfo inC;

    private VSFlowInfo out = new VSFlowInfo();
    private Color colorLinks = new Color(220, 220, 220);
    private Color colorRechts = new Color(220, 220, 220);

    public void paint(java.awt.Graphics g) {
        if (element != null) {
            Rectangle bounds = element.jGetBounds();

            Graphics2D g2 = (Graphics2D) g;
            // g.setColor(color);

            int mitteX = bounds.x + bounds.width / 2;
            int mitteY = bounds.y + bounds.height / 2;


            g.setColor(Color.GREEN);
            g.drawLine(mitteX, bounds.y, mitteX, bounds.y + bounds.height);

            g.setColor(colorLinks);

            Polygon p2 = new Polygon();
            p2.addPoint(mitteX, mitteY);
            p2.addPoint(mitteX - 5, mitteY - 5);
            p2.addPoint(mitteX - 5, mitteY + 5);

            g2.fillPolygon(p2);

            g.drawLine(bounds.x, mitteY, mitteX, mitteY);
            g.setColor(colorRechts);

            p2 = new Polygon();
            p2.addPoint(mitteX, mitteY);
            p2.addPoint(mitteX + 5, mitteY - 5);
            p2.addPoint(mitteX + 5, mitteY + 5);

            g2.fillPolygon(p2);

            g.drawLine(mitteX, mitteY, bounds.x + bounds.width, mitteY);

            /*
             * g.fillOval(bounds.x+d-3,bounds.y+(h/2)-3,6,6);
             *
             * g.drawLine(bounds.x+d,bounds.y+h/2,bounds.x+w,bounds.y+h/2);
             *
             * for (int i=0;i<2;i++)
             * {
             * int yy=bounds.y+6+(i*11);
             * g.drawLine(bounds.x,yy,bounds.x+d,yy);
             * }
             */
        }

    }

    public void onDispose() {
        if (image != null) {
            image.flush();
            image = null;
        }
    }

    private void generateCode() {

        int id = element.jGetID();

        String code = "";
        /*
         * code+="  DIM ELEMENT"+id+"_OLDVALUE\n";
         * element.jSetTag(4, code); // Globals
         *
         * code="";
         * code+="  GET_BIT PORTB,"+value.getValue()+"  \n";
         * code+="  POP R0  \n";
         * code+="  CMP R0, ELEMENT"+id+"_OLDVALUE\n";
         * code+="  JMP_IF_A=B ELEMENT"+id+"_EXIT\n";
         * code+="  GET_BIT PORTB,"+value.getValue()+"\n";
         * code+="  POP ELEMENT"+id+"_OLDVALUE  \n";
         * code+="  MOV %pin0%,ELEMENT"+id+"_OLDVALUE \n";
         * code+="  %notify0%\n";
         * code+="ELEMENT"+id+"_EXIT: \n";
         * element.jSetTag(2, code);
         *
         * // Als Event_handler registrieren
         * // D.h. das diese Funktion in der Hauptschleife aufgerufen wird!
         * code="  CALL ELEMENT"+id+"\n";
         * element.jSetTag(1, code);
         */


        code = "";
        code += "  ELEMENT" + id + ":   //Node\n";
        code += "    GOTO %pin2%\n";
        element.jSetTag(2, code);

    }


    public void init() {
        standardWidth = 130;
        width = standardWidth;
        height = 30;

        initPins(1, 1, 1, 1);
        setSize(width, height);
        element.jSetInnerBorderVisibility(false);
        // element.jSetTopPinsVisible(true);
        // element.jSetBottomPinsVisible(true);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");

        element.jInitPins();


        setPin(0, ExternalIF.C_FLOWINFO, element.PIN_INPUT);
        setPin(1, ExternalIF.C_FLOWINFO, element.PIN_INPUT);
        setPin(2, ExternalIF.C_FLOWINFO, element.PIN_OUTPUT);
        setPin(3, ExternalIF.C_FLOWINFO, element.PIN_INPUT);

        generateCode();

        setName("#MCU-FLOWCHART-NODE#");
    }

    public void xOnInit() {
        super.xOnInit();
    }



    public void initInputPins() {
        inA = (VSFlowInfo) element.getPinInputReference(0);
        inB = (VSFlowInfo) element.getPinInputReference(1);
        inC = (VSFlowInfo) element.getPinInputReference(3);

        if (inA == null) inA = new VSFlowInfo();
        if (inB == null) inB = new VSFlowInfo();
        if (inC == null) inC = new VSFlowInfo();
    }

    public void initOutputPins() {
        element.setPinOutputReference(2, out);
    }


    public void checkPinDataType() {
        boolean pinInA = element.hasPinWire(0);
        boolean pinInB = element.hasPinWire(1);
        boolean pinInC = element.hasPinWire(3);

        if (pinInB) {
            colorRechts = Color.GREEN;
        } else {
            colorRechts = new Color(220, 220, 220);
        }

        if (pinInC) {
            colorLinks = Color.GREEN;
        } else {
            colorLinks = new Color(220, 220, 220);
        }


        element.jRepaint();
    }


    private boolean changed = false;

    /*
     * public void destElementCalled()
     * {
     * if (changed)
     * {
     * element.notifyPin(2);
     * changed=false;
     * }
     * }
     */

    VSObject vv = null;

    public void elementActionPerformed(ElementActionEvent evt) {

        if (inA instanceof VSObject && evt.getSourcePinIndex() == 0) {
            vv = (VSObject) inA;

            callOut();
        }

        if (inB instanceof VSObject && evt.getSourcePinIndex() == 1) {
            vv = (VSObject) inB;

            callOut();
        }

        if (inC instanceof VSObject && evt.getSourcePinIndex() == 3) {
            vv = (VSObject) inC;

            callOut();
        }
    }

    private void callOut() {
        out.copyValueFrom(vv);
        element.notifyPin(2);
        changed = true;
    }


}