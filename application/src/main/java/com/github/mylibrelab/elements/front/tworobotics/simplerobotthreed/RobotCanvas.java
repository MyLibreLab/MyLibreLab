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

package com.github.mylibrelab.elements.front.tworobotics.simplerobotthreed;/*
                                                                            * Copyright (C) 2020 MyLibreLab
                                                                            * Based on MyOpenLab by Carmelo Salafia
                                                                            * www.myopenlab.de
                                                                            * Copyright (C) 2004 Carmelo Salafia
                                                                            * cswi@gmx.de
                                                                            *
                                                                            * This program is free software: you can
                                                                            * redistribute it and/or modify
                                                                            * it under the terms of the GNU General
                                                                            * Public License as published by
                                                                            * the Free Software Foundation, either
                                                                            * version 3 of the License, or
                                                                            * (at your option) any later version.
                                                                            *
                                                                            * This program is distributed in the hope
                                                                            * that it will be useful,
                                                                            * but WITHOUT ANY WARRANTY; without even the
                                                                            * implied warranty of
                                                                            * MERCHANTABILITY or FITNESS FOR A
                                                                            * PARTICULAR PURPOSE. See the
                                                                            * GNU General Public License for more
                                                                            * details.
                                                                            *
                                                                            * You should have received a copy of the GNU
                                                                            * General Public License
                                                                            * along with this program. If not, see
                                                                            * <http://www.gnu.org/licenses/>.
                                                                            *
                                                                            */

import java.awt.*;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ElementActionEvent;
import VisualLogic.ExternalIF;

public class RobotCanvas extends JVSMain {
    private ExternalIF panelElement;
    private Image image;



    public void paint(java.awt.Graphics g) {
        drawImageCentred(g, image);
    }

    public void onDispose() {
        if (image != null) {
            image.flush();
            image = null;
        }
    }

    public void init() {
        element.jSetInnerBorderVisibility(true);
        initPins(0, 0, 0, 0);
        setSize(60, 60);
        initPinVisibility(false, false, false, false);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.png");
        setName("SimpleRobot3D v. 1.0");

        element.jSetResizable(false);
    }

    public void initInputPins() {

    }

    public void start() {
        panelElement = element.getPanelElement();

    }

    public void stop() {

    }


    public void initOutputPins() {}


    public void changePin(int pinIndex, Object value) {}

    public void process() {}


    public void elementActionPerformed(ElementActionEvent evt) {
        int index = evt.getSourcePinIndex();

    }


}
