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

package com.github.mylibrelab.elements.front.twonumerisch.statusanzeige;/*
                                                                         * Copyright (C) 2020 MyLibreLab
                                                                         * Based on MyOpenLab by Carmelo Salafia
                                                                         * www.myopenlab.de
                                                                         * Copyright (C) 2004 Carmelo Salafia
                                                                         * cswi@gmx.de
                                                                         *
                                                                         * This program is free software: you can
                                                                         * redistribute it and/or modify
                                                                         * it under the terms of the GNU General Public
                                                                         * License as published by
                                                                         * the Free Software Foundation, either version
                                                                         * 3 of the License, or
                                                                         * (at your option) any later version.
                                                                         *
                                                                         * This program is distributed in the hope that
                                                                         * it will be useful,
                                                                         * but WITHOUT ANY WARRANTY; without even the
                                                                         * implied warranty of
                                                                         * MERCHANTABILITY or FITNESS FOR A PARTICULAR
                                                                         * PURPOSE. See the
                                                                         * GNU General Public License for more details.
                                                                         *
                                                                         * You should have received a copy of the GNU
                                                                         * General Public License
                                                                         * along with this program. If not, see
                                                                         * <http://www.gnu.org/licenses/>.
                                                                         *
                                                                         */

import java.awt.*;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSInteger;
import VisualLogic.variables.VSObject;

public class StatusDisplay extends JVSMain {
    private Image image;
    private boolean changed = false;
    private VSObject in;
    private int oldValue;
    private ExternalIF panelElement;

    public void onDispose() {
        image.flush();
        image = null;
    }


    public void paint(java.awt.Graphics g) {
        drawImageCentred(g, image);
    }

    public void init() {
        initPins(0, 0, 0, 1);
        setSize(40, 30);
        initPinVisibility(false, false, false, true);

        setPin(0, ExternalIF.C_INTEGER, element.PIN_INPUT);

        image = element.jLoadImage(element.jGetSourcePath() + "StatusDisplay.gif");
        setName("StatusDisplay");
    }


    public void initInputPins() {
        in = (VSObject) element.getPinInputReference(0);
    }

    public void initOutputPins() {
        // element.setPinOutputReference(0,out);
    }

    public void start() {
        panelElement = element.getPanelElement();
    }

    public void process() {
        if (in instanceof VSInteger) {
            VSInteger inX = (VSInteger) in;

            if (in != null && inX.getValue() != oldValue) {
                if (panelElement != null) {
                    panelElement.jProcessPanel(0, inX.getValue(), (Object) this);
                }
                oldValue = inX.getValue();
            }
        }

    }


}
