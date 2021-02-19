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

package com.github.mylibrelab.elements.front.twobooleanpackage.userdefinedbooleanpackagedisplay;/*
                                                                                                 * Copyright (C) 2020
                                                                                                 * MyLibreLab
                                                                                                 * Based on MyOpenLab by
                                                                                                 * Carmelo Salafia
                                                                                                 * www.myopenlab.de
                                                                                                 * Copyright (C) 2004
                                                                                                 * Carmelo Salafia
                                                                                                 * cswi@gmx.de
                                                                                                 *
                                                                                                 * This program is free
                                                                                                 * software: you can
                                                                                                 * redistribute it
                                                                                                 * and/or modify
                                                                                                 * it under the terms of
                                                                                                 * the GNU General
                                                                                                 * Public License as
                                                                                                 * published by
                                                                                                 * the Free Software
                                                                                                 * Foundation, either
                                                                                                 * version 3 of the
                                                                                                 * License, or
                                                                                                 * (at your option) any
                                                                                                 * later version.
                                                                                                 *
                                                                                                 * This program is
                                                                                                 * distributed in the
                                                                                                 * hope that it will be
                                                                                                 * useful,
                                                                                                 * but WITHOUT ANY
                                                                                                 * WARRANTY; without
                                                                                                 * even the implied
                                                                                                 * warranty of
                                                                                                 * MERCHANTABILITY or
                                                                                                 * FITNESS FOR A
                                                                                                 * PARTICULAR PURPOSE.
                                                                                                 * See the
                                                                                                 * GNU General Public
                                                                                                 * License for more
                                                                                                 * details.
                                                                                                 *
                                                                                                 * You should have
                                                                                                 * received a copy of
                                                                                                 * the GNU General
                                                                                                 * Public License
                                                                                                 * along with this
                                                                                                 * program. If not, see
                                                                                                 * <http://www.gnu.org/
                                                                                                 * licenses/>.
                                                                                                 *
                                                                                                 */

import java.awt.*;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSBoolean;

public class Circuit extends JVSMain {
    private Image image;
    private boolean changed = false;
    private VSBoolean in;
    private boolean oldValue = false;
    private ExternalIF panelElement;

    public void onDispose() {
        image.flush();
        image = null;
    }


    public void paint(java.awt.Graphics g) {
        if (image != null) drawImageCentred(g, image);
    }

    public void init() {
        initPins(0, 0, 0, 1);
        setSize(40, 30);
        initPinVisibility(false, false, false, true);

        setPin(0, ExternalIF.C_BOOLEAN, element.PIN_INPUT);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.png");
        setName("UserDefinedBooleanDisplay");
    }


    public void initInputPins() {
        in = (VSBoolean) element.getPinInputReference(0);
        if (in == null) in = new VSBoolean(false);
    }

    public void initOutputPins() {}

    public void start() {
        panelElement = element.getPanelElement();
        oldValue = !oldValue;
        process();
    }

    public void process() {
        if (in.getValue() != oldValue) {
            oldValue = in.getValue();

            if (panelElement != null) {
                if (in.getValue() == true) {
                    panelElement.jProcessPanel(0, 1.0, (Object) this);
                } else {
                    panelElement.jProcessPanel(0, 0.0, (Object) this);
                }

            }
        }
    }


}
