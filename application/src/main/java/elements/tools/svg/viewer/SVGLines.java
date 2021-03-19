/*
 *
 *  * Copyright (C) 2020 MyLibreLab
 *  * Based on MyOpenLab by Carmelo Salafia www.myopenlab.de
 *  * Copyright (C) 2004  Carmelo Salafia cswi@gmx.de
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  *
 *
 */

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package elements.tools.svg.viewer;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

import org.w3c.dom.Element;

public class SVGLines extends SVGObject {
    double x1;
    double y1;
    double x2;
    double y2;

    public SVGLines() {}

    public void loadFromXML(Element nodeElement) {
        super.loadFromXML(nodeElement);
        this.x1 = Double.parseDouble(nodeElement.getAttribute("x1"));
        this.y1 = Double.parseDouble(nodeElement.getAttribute("y1"));
        this.x2 = Double.parseDouble(nodeElement.getAttribute("x2"));
        this.y2 = Double.parseDouble(nodeElement.getAttribute("y2"));
    }

    public void paint(Graphics2D g, double zoomX, double zoomY) {
        int a1 = (int) (this.x1 * zoomX);
        int a2 = (int) (this.y1 * zoomX);
        int a3 = (int) (this.x2 * zoomX);
        int a4 = (int) (this.y2 * zoomX);
        if (this.getStrokeColor() != null) {
            g.setStroke(new BasicStroke(this.getStrokeWidth()));
            g.setColor(this.getStrokeColor());
            g.drawLine(a1, a2, a3, a4);
        }

    }
}
