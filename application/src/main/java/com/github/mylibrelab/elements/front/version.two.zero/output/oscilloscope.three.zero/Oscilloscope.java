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

package com.github.mylibrelab.elements.front.version.two.zero.output.oscilloscope.three.zero;// *****************************************************************************


import java.awt.*;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSDouble;
import VisualLogic.variables.VSGroup;

public class Oscilloscope extends JVSMain {
    public VSGroup inA = null;
    public VSGroup inB = null;
    public VSGroup inC = null;
    public VSDouble inD = null;
    private ExternalIF panelElement = null;
    private Image image;


    public Oscilloscope() {
        super();
    }

    public void onDispose() {
        image.flush();
        image = null;
    }

    public void paint(java.awt.Graphics g) {
        drawImageCentred(g, image);
    }

    public void init() {
        initPins(0, 0, 0, 4);
        setSize(40, 50);
        initPinVisibility(false, false, false, true);

        setPin(0, ExternalIF.C_GROUP, element.PIN_INPUT);
        setPin(1, ExternalIF.C_GROUP, element.PIN_INPUT);
        setPin(2, ExternalIF.C_GROUP, element.PIN_INPUT);
        setPin(3, ExternalIF.C_DOUBLE, element.PIN_INPUT);

        element.jSetPinDescription(0, "Values (X,Y)");
        element.jSetPinDescription(1, "Colors");
        element.jSetPinDescription(2, "Rendertype");
        element.jSetPinDescription(3, "X-Pos.");

        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");


        element.jSetCaptionVisible(true);
        element.jSetCaption("osc");

        setName("Oscilloscope-X/Y_Version 2.0");
    }


    public void initInputPins() {
        inA = (VSGroup) element.getPinInputReference(0);
        inB = (VSGroup) element.getPinInputReference(1);
        inC = (VSGroup) element.getPinInputReference(2);
        inD = (VSDouble) element.getPinInputReference(3);

        if (inA == null) inA = new VSGroup();
        if (inB == null) inB = new VSGroup();
        if (inC == null) inC = new VSGroup();
        if (inD == null) inD = new VSDouble(0);

    }

    public void initOutputPins() {

    }

    public void process() {
        if (panelElement != null) {
            panelElement.jProcessPanel(0, 0, inA);
            panelElement.jProcessPanel(1, 0, inB);
            panelElement.jProcessPanel(2, 0, inC);
            panelElement.jProcessPanel(3, 0, inD);
        }
    }

    public void start() {
        panelElement = element.getPanelElement();
        if (panelElement != null) panelElement.jRepaint();
    }


}
