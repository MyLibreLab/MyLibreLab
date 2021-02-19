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

package com.github.mylibrelab.elements.front.twodekoratoren.svgsalamanderimage;/*
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
                                                                                * This program is distributed in the
                                                                                * hope that it will be useful,
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

import java.io.*;
import java.net.URI;

import javax.swing.*;

import com.github.mylibrelab.elements.tools.JVSMain;
import com.kitfox.svg.SVGCache;
import com.kitfox.svg.app.beans.SVGPanel;

import VisualLogic.variables.VSBoolean;
import VisualLogic.variables.VSFile;
import VisualLogic.variables.VSString;

public class Panel extends JVSMain {
    private VSFile file = new VSFile("");
    private VSString xmlText = new VSString("");
    private SVGPanel icon = new SVGPanel();
    private VSBoolean antialising = new VSBoolean(true);

    JPanel panel = null;


    public void init() {
        initPins(0, 0, 0, 0);
        initPinVisibility(false, false, false, false);
        setSize(80, 114);
        element.jSetInnerBorderVisibility(false);

        element.jSetResizeSynchron(false);
        element.jSetResizable(true);

        setName("SVGImage 2.1");

        file.clearExtensions();
        file.addExtension("svg");

        file.setDescription("SVG Image 2.1");

        loadImage(element.jGetSourcePath() + "image.svg");



    }

    public void initSVG(String xmlString) {
        StringReader reader = new StringReader(xmlString);

        SVGCache.getSVGUniverse().clear();
        URI uri = SVGCache.getSVGUniverse().loadSVG(reader, "myImage");

        icon.setScaleToFit(true);
        icon.setSvgURI(uri);
        icon.setOpaque(false);
        icon.setAntiAlias(antialising.getValue());
    }

    private String makeDynamicSVG() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        pw.println("<svg width=\"400\" height=\"400\" style=\"fill:none;stroke-width:4\">");
        pw.println("    <circle cx=\"200\" cy=\"200\" r=\"200\" style=\"stroke:blue\"/>");
        pw.println("    <circle cx=\"140\" cy=\"140\" r=\"40\" style=\"stroke:red\"/>");
        pw.println("    <circle cx=\"260\" cy=\"140\" r=\"40\" style=\"stroke:red\"/>");
        pw.println("    <polyline points=\"100 300 150 340 250 240 300 300\" style=\"stroke:red\"/>");
        pw.println("</svg>");

        pw.close();
        return sw.toString();
    }


    public void xOnInit() {
        panel = element.getFrontPanel();
        panel.setLayout(new java.awt.BorderLayout());
        panel.add(icon, java.awt.BorderLayout.CENTER);
        element.setAlwaysOnTop(true);
    }



    public void setPropertyEditor() {
        element.jAddPEItem("SVG File", file, 0, 0);
        element.jAddPEItem("Antialising", antialising, 0, 0);
        localize();
    }

    private void localize() {
        int d = 6;
        String language;

        language = "en_US";

        element.jSetPEItemLocale(d + 0, language, "SVG File");
        element.jSetPEItemLocale(d + 1, language, "Antialising");

        language = "es_ES";

        element.jSetPEItemLocale(d + 0, language, "SVG File");
        element.jSetPEItemLocale(d + 1, language, "Antialising");
    }

    public void propertyChanged(Object o) {
        if (o.equals(file)) {
            loadImage(file.getValue());
        }
        icon.setAntiAlias(antialising.getValue());
    }


    public void loadImage(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String line = "";
            String res = "";
            while ((line = reader.readLine()) != null) {
                res += line;
            }
            xmlText.setValue(res);
            initSVG(xmlText.getValue());

            reader.close();

        } catch (Exception ex) {
            System.out.println("ERROR: SVGIMAGE " + ex);
        }

        element.jRepaint();
    }

    public void loadFromStream(java.io.FileInputStream fis) {


        try {
            java.io.DataInputStream dis = new java.io.DataInputStream(fis);
            int size = dis.readInt();
            byte[] bytes = new byte[size];
            dis.read(bytes);
            xmlText.setValue(new String(bytes));
        } catch (Exception ex) {
        }

        antialising.loadFromStream(fis);

        initSVG(xmlText.getValue());
        icon.setAntiAlias(antialising.getValue());

        element.jRepaint();
    }

    public void saveToStream(java.io.FileOutputStream fos) {
        try {
            java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);

            dos.writeInt(xmlText.getValue().length());
            dos.writeBytes(xmlText.getValue());

        } catch (Exception ex) {
            // VisualLogic.Tools.showMessage("Fehler in VSDouble.saveToStream() : "+ex.toString());
        }

        antialising.saveToStream(fos);

    }



}
