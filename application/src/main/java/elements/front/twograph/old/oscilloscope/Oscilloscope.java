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

package elements.front.twograph.old.oscilloscope;/*
                                                                        * Copyright (C) 2020 MyLibreLab
                                                                        * Based on MyOpenLab by Carmelo Salafia
                                                                        * www.myopenlab.de
                                                                        * Copyright (C) 2004 Carmelo Salafia cswi@gmx.de
                                                                        *
                                                                        * This program is free software: you can
                                                                        * redistribute it and/or modify
                                                                        * it under the terms of the GNU General Public
                                                                        * License as published by
                                                                        * the Free Software Foundation, either version 3
                                                                        * of the License, or
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

import elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSGroup;

public class Oscilloscope extends JVSMain {
    public VSGroup in = null;
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
        initPins(0, 0, 0, 1);
        setSize(40, 30);
        initPinVisibility(false, false, false, true);

        setPin(0, ExternalIF.C_GROUP, element.PIN_INPUT);

        element.jSetPinDescription(0, "In");

        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");


        element.jSetCaptionVisible(true);
        element.jSetCaption("osc");

        setName("Oscilloscope");
    }


    public void initInputPins() {
        in = (VSGroup) element.getPinInputReference(0);
    }

    public void initOutputPins() {

    }

    public void process() {
        if (in != null && panelElement != null) {
            panelElement.jProcessPanel(0, 0, in);
        }
    }

    public void start() {
        panelElement = element.getPanelElement();
        if (panelElement != null) panelElement.jRepaint();
    }


}
