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

package com.github.mylibrelab.elements.front.input.buttons.taster;// *****************************************************************************

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


import java.awt.*;
import java.awt.event.MouseEvent;

import com.github.mylibrelab.elements.tools.JVSMain;
import com.github.mylibrelab.svg.viewer.SVGManager;
import com.github.mylibrelab.svg.viewer.SVGObject;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSBoolean;

public class TasterPanel extends JVSMain {
    private final SVGManager svgManager = new SVGManager();
    private SVGObject svgOn;
    private ExternalIF circuitElement;

    private final int sizeW = 30;
    private final int sizeH = 30;
    private boolean down = false;


    public void paint(java.awt.Graphics g) {
        if (element != null) {
            Graphics2D g2 = (Graphics2D) g;
            Rectangle bounds = element.jGetBounds();

            if (down) {
                if (svgOn != null) svgOn.setFillColor(Color.red);
            } else {
                if (svgOn != null) svgOn.setFillColor(Color.black);
            }

            svgManager.paint((Graphics2D) g, (int) bounds.getWidth(), (int) bounds.getHeight());

        }
    }

    public void init() {
        initPins(0, 0, 0, 0);
        setSize(sizeW, sizeH);
        initPinVisibility(false, false, false, false);

        element.jSetInnerBorderVisibility(false);
        element.jSetResizable(true);
        element.jSetResizeSynchron(true);
        svgManager.loadFromFile(element.jGetSourcePath() + "taster.svg");
        svgOn = svgManager.getSVGObject("on");

        setName("Taster");
    }

    private void setValue(boolean bol) {
        circuitElement = element.getCircuitElement();
        if (circuitElement != null) circuitElement.Change(0, new VSBoolean(bol));
    }


    public void start() {
        down = false;
        setValue(down);
        element.jRepaint();
    }

    public void mouseReleased(MouseEvent e) {
        down = false;
        setValue(down);
        element.jRepaint();

    }

    public void xonMousePressed(MouseEvent e) {
        down = true;
        setValue(down);
        element.jRepaint();
    }

}
