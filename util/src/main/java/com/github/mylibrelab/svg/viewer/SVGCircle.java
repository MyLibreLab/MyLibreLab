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

import org.w3c.dom.Element;

public class SVGCircle extends SVGObject {
    double cx;
    double cy;
    double r;

    public SVGCircle() {}

    public void loadFromXML(Element nodeElement) {
        super.loadFromXML(nodeElement);
        this.cx = Double.parseDouble(nodeElement.getAttribute("cx"));
        this.cy = Double.parseDouble(nodeElement.getAttribute("cy"));
        this.r = Double.parseDouble(nodeElement.getAttribute("r"));
    }

    public void paint(Graphics2D g, double zoomX, double zoomY) {
        int x1 = (int) ((this.cx - this.r) * zoomX);
        int y1 = (int) ((this.cy - this.r) * zoomX);
        int x2 = (int) (this.r * 2.0D * zoomX);
        int y2 = (int) (this.r * 2.0D * zoomX);
        if (this.getFillColor() != null) {
            g.setColor(this.getFillColor());
            g.fillOval(x1, y1, x2, y2);
        }

        if (this.getStrokeColor() != null) {
            g.setStroke(new BasicStroke(this.getStrokeWidth()));
            g.setColor(this.getStrokeColor());
            g.drawOval(x1, y1, x2, y2);
        }

    }
}
