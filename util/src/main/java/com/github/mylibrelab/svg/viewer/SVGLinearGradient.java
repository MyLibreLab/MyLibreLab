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

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

import org.w3c.dom.Element;

public class SVGLinearGradient extends SVGObject {
    double x1;
    double y1;
    double x2;
    double y2;

    public SVGLinearGradient() {}

    public void loadFromXML(Element nodeElement) {
        super.loadFromXML(nodeElement);
    }

    public void paint(Graphics2D g, double zoomX, double zoomY) {
        GradientPaint gp = new GradientPaint(50.0F, 50.0F, Color.blue, 50.0F, 250.0F, Color.green);
        g.setPaint(gp);
        g.fillOval(50, 50, 200, 200);
    }
}
