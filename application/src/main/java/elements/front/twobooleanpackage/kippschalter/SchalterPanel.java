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

package elements.front.twobooleanpackage.kippschalter;/*
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
import java.awt.event.MouseEvent;

import elements.tools.JVSMain;
import elements.tools.svg.viewer.SVGManager;
import elements.tools.svg.viewer.SVGObject;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSBoolean;

public class SchalterPanel extends JVSMain {
    private SVGManager svgManager = new SVGManager();
    private VSBoolean initValue = new VSBoolean(false);
    private boolean value = false;
    private int sizeW = 32;
    private int sizeH = 45;
    private boolean down = false;
    private ExternalIF circuitElement;



    public void paint(java.awt.Graphics g) {
        if (element != null) {
            Graphics2D g2 = (Graphics2D) g;
            Rectangle bounds = element.jGetBounds();

            SVGObject svgOn1 = svgManager.getSVGObject("on1");
            SVGObject svgOn2 = svgManager.getSVGObject("on2");
            SVGObject svgOn3 = svgManager.getSVGObject("on3");
            SVGObject svgOff1 = svgManager.getSVGObject("off1");
            SVGObject svgOff2 = svgManager.getSVGObject("off2");
            SVGObject svgOff3 = svgManager.getSVGObject("off3");

            if (down) {
                if (svgOn1 != null) svgOn1.setVisible(true);
                if (svgOn2 != null) svgOn2.setVisible(true);
                if (svgOn3 != null) svgOn3.setVisible(true);
                if (svgOff1 != null) svgOff1.setVisible(false);
                if (svgOff2 != null) svgOff2.setVisible(false);
                if (svgOff3 != null) svgOff3.setVisible(false);
            } else {
                if (svgOn1 != null) svgOn1.setVisible(false);
                if (svgOn2 != null) svgOn2.setVisible(false);
                if (svgOn3 != null) svgOn3.setVisible(false);
                if (svgOff1 != null) svgOff1.setVisible(true);
                if (svgOff2 != null) svgOff2.setVisible(true);
                if (svgOff3 != null) svgOff3.setVisible(true);
            }


            svgManager.paint((Graphics2D) g, (int) bounds.getWidth(), (int) bounds.getHeight());
        }
    }

    public void init() {
        initPins(0, 0, 0, 0);
        setSize(sizeW, sizeH);
        initPinVisibility(false, false, false, false);

        element.jSetResizable(true);
        element.jSetResizeSynchron(true);
        element.jSetAspectRatio((double) sizeH / (double) sizeW);
        element.jSetInnerBorderVisibility(false);


        svgManager.loadFromFile(element.jGetSourcePath() + "schalter.svg");
        setName("Schiebeschalter");

    }


    public void start() {
        down = initValue.getValue();
        element.jRepaint();
        setValue(down);
    }


    private void setValue(boolean bol) {
        circuitElement = element.getCircuitElement();
        if (circuitElement != null) circuitElement.Change(0, new VSBoolean(bol));
    }

    public void mousePressed(MouseEvent e) {
        if (down == true) {
            down = false;

            setValue(down);

            element.jRepaint();
        } else {
            down = true;
            setValue(down);
            element.jRepaint();
        }

    }

    public void setPropertyEditor() {
        element.jAddPEItem("Anfangs-Wert", initValue, 0, 0);
        localize();
    }


    private void localize() {
        int d = 6;
        String language;

        language = "en_US";

        element.jSetPEItemLocale(d + 0, language, "Init-Value");

        language = "es_ES";


        element.jSetPEItemLocale(d + 0, language, "Valor Inicial");
    }

    public void propertyChanged(Object o) {
        down = initValue.getValue();
        element.jRepaint();
    }

    public void loadFromStream(java.io.FileInputStream fis) {
        initValue.loadFromStream(fis);
        down = initValue.getValue();
        element.jRepaint();
    }

    public void saveToStream(java.io.FileOutputStream fos) {
        initValue.saveToStream(fos);
    }


}
