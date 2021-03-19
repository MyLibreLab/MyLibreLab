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

package elements.front.twonumerisch.rgb.led;/*
                                                                   * Copyright (C) 2020 MyLibreLab
                                                                   * Based on MyOpenLab by Carmelo Salafia
                                                                   * www.myopenlab.de
                                                                   * Copyright (C) 2004 Carmelo Salafia cswi@gmx.de
                                                                   *
                                                                   * This program is free software: you can redistribute
                                                                   * it and/or modify
                                                                   * it under the terms of the GNU General Public
                                                                   * License as published by
                                                                   * the Free Software Foundation, either version 3 of
                                                                   * the License, or
                                                                   * (at your option) any later version.
                                                                   *
                                                                   * This program is distributed in the hope that it
                                                                   * will be useful,
                                                                   * but WITHOUT ANY WARRANTY; without even the implied
                                                                   * warranty of
                                                                   * MERCHANTABILITY or FITNESS FOR A PARTICULAR
                                                                   * PURPOSE. See the
                                                                   * GNU General Public License for more details.
                                                                   *
                                                                   * You should have received a copy of the GNU General
                                                                   * Public License
                                                                   * along with this program. If not, see
                                                                   * <http://www.gnu.org/licenses/>.
                                                                   *
                                                                   */

import java.awt.*;

import elements.tools.JVSMain;
import elements.tools.svg.viewer.SVGManager;
import elements.tools.svg.viewer.SVGObject;

import VisualLogic.PanelIF;

public class LEDPanel extends JVSMain implements PanelIF {
    private Color color = new Color(0, 0, 0);
    private SVGManager svgManager = new SVGManager();


    public void processPanel(int pinIndex, double value, Object obj) {
        if (obj instanceof Color) {
            color = (Color) obj;
            element.jRepaint();
        }
    }

    public void paint(java.awt.Graphics g) {
        if (element != null) {
            Rectangle bounds = element.jGetBounds();

            g.setColor(color);
            SVGObject light = svgManager.getSVGObject("light");

            light.setFillColor(color);

            svgManager.paint((Graphics2D) g, (int) bounds.getWidth(), (int) bounds.getHeight());
        }
    }

    public void init() {
        initPins(0, 0, 0, 0);
        setSize(50, 50);
        element.jSetInnerBorderVisibility(false);

        initPinVisibility(false, false, false, false);

        element.jSetResizeSynchron(true);
        element.jSetResizable(true);
        svgManager.loadFromFile(element.jGetSourcePath() + "led.svg");

        setName("RGB-LED");
    }


}
