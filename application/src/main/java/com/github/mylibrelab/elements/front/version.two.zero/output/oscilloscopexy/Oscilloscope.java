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

package com.github.mylibrelab.elements.front.version.two.zero.output.oscilloscopexy;// *****************************************************************************


import java.awt.*;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VS1DDouble;

public class Oscilloscope extends JVSMain {
    public VS1DDouble inX = null;
    public VS1DDouble inY = null;
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
        initPins(0, 0, 0, 2);
        setSize(40, 40);
        initPinVisibility(false, false, false, true);

        setPin(0, ExternalIF.C_ARRAY1D_DOUBLE, element.PIN_INPUT);
        setPin(1, ExternalIF.C_ARRAY1D_DOUBLE, element.PIN_INPUT);

        element.jSetPinDescription(0, "1D-Array X");
        element.jSetPinDescription(1, "1D-Array Y");

        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");


        element.jSetCaptionVisible(true);
        element.jSetCaption("osc");

        setName("Oscilloscope-X/Y_Version 1.0");
    }


    public void initInputPins() {
        inX = (VS1DDouble) element.getPinInputReference(0);
        inY = (VS1DDouble) element.getPinInputReference(1);
    }

    public void initOutputPins() {

    }

    public void process() {
        if (inX != null && inY != null && panelElement != null) {
            panelElement.jProcessPanel(0, 0, inX);
            panelElement.jProcessPanel(1, 0, inY);
        }
    }

    public void start() {
        panelElement = element.getPanelElement();
        if (panelElement != null) panelElement.jRepaint();
    }


}
