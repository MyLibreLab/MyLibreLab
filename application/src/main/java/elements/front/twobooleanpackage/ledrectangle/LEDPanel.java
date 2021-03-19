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

package elements.front.twobooleanpackage.ledrectangle;/*
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
                                                                             * but WITHOUT ANY WARRANTY; without even
                                                                             * the implied warranty of
                                                                             * MERCHANTABILITY or FITNESS FOR A
                                                                             * PARTICULAR PURPOSE. See the
                                                                             * GNU General Public License for more
                                                                             * details.
                                                                             *
                                                                             * You should have received a copy of the
                                                                             * GNU General Public License
                                                                             * along with this program. If not, see
                                                                             * <http://www.gnu.org/licenses/>.
                                                                             *
                                                                             */

import java.awt.*;

import elements.tools.JVSMain;
import elements.tools.svg.viewer.SVGManager;
import elements.tools.svg.viewer.SVGObject;

import VisualLogic.PanelIF;
import VisualLogic.variables.VSColor;

public class LEDPanel extends JVSMain implements PanelIF {
    private boolean on = false;
    private SVGManager svgManager = new SVGManager();
    private VSColor col = new VSColor(Color.RED);


    private void setOn(boolean value) {
        if (value != on) {
            on = value;
            element.jRepaint();
        }
    }

    public void processPanel(int pinIndex, double value, Object obj) {
        if (value == 0.0)
            setOn(false);
        else
            setOn(true);
    }

    public void paint(java.awt.Graphics g) {
        if (element != null) {
            Rectangle bounds = element.jGetBounds();

            SVGObject light = svgManager.getSVGObject("light");

            if (on)
                light.setFillColor(col.getValue());
            else
                light.setFillColor(Color.black);

            svgManager.paint((Graphics2D) g, (int) bounds.getWidth(), (int) bounds.getHeight());
        }
    }

    public void init() {
        initPins(0, 0, 0, 0);
        initPinVisibility(false, false, false, false);
        setSize(30, 30);
        element.jSetInnerBorderVisibility(false);

        element.jSetResizeSynchron(true);
        element.jSetResizable(true);
        svgManager.loadFromFile(element.jGetSourcePath() + "led.svg");
        System.out.println("LED->>>" + element.jGetSourcePath());
        setName("led");
    }


    public void setPropertyEditor() {
        element.jAddPEItem("Farbe", col, 0, 0);
        localize();
    }


    private void localize() {
        int d = 6;
        String language;

        language = "en_US";

        element.jSetPEItemLocale(d + 0, language, "Color");

        language = "es_ES";

        element.jSetPEItemLocale(d + 0, language, "Color");
    }

    public void propertyChanged(Object o) {
        element.jRepaint();
    }

    public void loadFromStream(java.io.FileInputStream fis) {
        col.loadFromStream(fis);
    }

    public void saveToStream(java.io.FileOutputStream fos) {
        col.saveToStream(fos);
    }


}
