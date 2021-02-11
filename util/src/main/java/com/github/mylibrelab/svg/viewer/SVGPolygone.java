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

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.mylibrelab.svg.viewer;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.StringTokenizer;

import org.w3c.dom.Element;

public class SVGPolygone extends SVGObject {
    private Polygon poly = new Polygon();
    private int i = 0;

    public SVGPolygone() {}

    private void geheBisZumLeerZeichen() {}

    private void fillPolygon(String points) {
        StringTokenizer t = new StringTokenizer(points, " ,");

        while (t.hasMoreTokens()) {
            String tempBuffer = t.nextToken();
            int x = (short) (new Double(tempBuffer)).intValue();
            tempBuffer = t.nextToken();
            int y = (short) (new Double(tempBuffer)).intValue();
            this.poly.addPoint(x, y);
        }

    }

    public void loadFromXML(Element nodeElement) {
        super.loadFromXML(nodeElement);
        String points = nodeElement.getAttribute("points");
        this.fillPolygon(points);
    }

    public void paint(Graphics2D g, double zoomX, double zoomY) {
        if (this.getFillColor() != null) {
            g.setColor(this.getFillColor());
            g.fillPolygon(this.poly);
        }

        if (this.getStrokeColor() != null) {
            g.setStroke(new BasicStroke(this.getStrokeWidth()));
            g.setColor(this.getStrokeColor());
            g.drawPolygon(this.poly);
        }

    }
}
