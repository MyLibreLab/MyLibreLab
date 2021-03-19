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

package elements.front.twodekoratoren.svgimage;/*
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
                                                                      * This program is distributed in the hope that it
                                                                      * will be useful,
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
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.swing.*;

import elements.tools.JVSMain;
import elements.tools.svg.viewer.SVGManager;

import VisualLogic.variables.VSFile;
import VisualLogic.variables.VSString;

public class Panel extends JVSMain {
    private boolean on = false;
    private SVGManager svgManager = new SVGManager();
    private VSFile file = new VSFile("");
    private byte imageBytes[] = null;
    private VSString xmlText = new VSString("");

    JPanel panel;

    public void paint(java.awt.Graphics g) {
        if (element != null) {
            Rectangle bounds = element.jGetBounds();
            svgManager.paint((Graphics2D) g, (int) bounds.getWidth(), (int) bounds.getHeight());
        }
    }



    public void init() {
        initPins(0, 0, 0, 0);
        initPinVisibility(false, false, false, false);
        setSize(80, 114);
        element.jSetInnerBorderVisibility(false);

        element.jSetResizeSynchron(false);
        element.jSetResizable(true);
        svgManager.loadFromFile(element.jGetSourcePath() + "image.svg");

        setName("SVGImage");

        file.clearExtensions();
        file.addExtension("svg");

        file.setDescription("SVG Image");

    }

    public void xOnInit() {}

    public void setPropertyEditor() {
        element.jAddPEItem("SVG Datei", file, 0, 0);
        localize();
    }

    private void localize() {
        int d = 6;
        String language;

        language = "en_US";

        element.jSetPEItemLocale(d + 0, language, "SVG File");

        language = "es_ES";

        element.jSetPEItemLocale(d + 0, language, "SVG File");
    }

    public void propertyChanged(Object o) {
        if (o.equals(file)) {
            loadImage(file.getValue());
        }

    }

    /*
     * public void readURL(String adress)
     * {
     * URL url = new Url(adress);
     * BufferedReader urlReader = new BufferedReader(new InputStreamReader(url.openStream()));
     * String line;
     * while((line = urlReader.readLine()) != null)
     * {
     * if(line.indexOf("deinSuchWort") >= 0)
     * {
     * writeEmail();
     * break;
     * }
     * }
     * }
     */


    public void loadImage(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String line = "";
            String res = "";
            while ((line = reader.readLine()) != null) {
                res += line;
                // System.out.println(line);

            }
            svgManager.loadFromString(res);
            xmlText.setValue(res);

        } catch (Exception ex) {
            System.out.println("ERROR: SVGIMAGE " + ex);
        }
        // svgManager.loadFromFile(fileName);

        element.jRepaint();
    }

    public void loadFromStream(java.io.FileInputStream fis) {
        xmlText.loadFromStream(fis);
        svgManager.loadFromString(xmlText.getValue());
        element.jRepaint();
    }

    public void saveToStream(java.io.FileOutputStream fos) {
        xmlText.saveToStream(fos);
    }


}
