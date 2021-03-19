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
import java.awt.geom.AffineTransform;

import org.w3c.dom.Element;

public class SVGRect extends SVGObject {
    double x;
    double y;
    double width;
    double height;
    int rx;
    int ry;

    public SVGRect() {}

    public void loadFromXML(Element nodeElement) {
        super.loadFromXML(nodeElement);
        this.x = Double.parseDouble(nodeElement.getAttribute("x"));
        this.y = Double.parseDouble(nodeElement.getAttribute("y"));
        this.width = Double.parseDouble(nodeElement.getAttribute("width"));
        this.height = Double.parseDouble(nodeElement.getAttribute("height"));

        try {
            this.rx = Integer.parseInt(nodeElement.getAttribute("rx"));
        } catch (Exception var4) {
        }

        try {
            this.ry = Integer.parseInt(nodeElement.getAttribute("ry"));
        } catch (Exception var3) {
        }

    }

    public void paint(Graphics2D g, double zoomX, double zoomY) {
        AffineTransform saveAt = g.getTransform();
        AffineTransform at = new AffineTransform();
        this.extractTranform(at, this.getTransformString());
        g.transform(at);
        int x1 = (int) (this.x * zoomX);
        int y1 = (int) (this.y * zoomX);
        int x2 = (int) (this.width * zoomX);
        int y2 = (int) (this.height * zoomX);
        int rx1 = (int) ((double) this.rx * zoomX);
        int ry1 = (int) ((double) this.ry * zoomY);
        if (this.getFillColor() != null) {
            g.setColor(this.getFillColor());
            g.fillRoundRect(x1, y1, x2, y2, rx1, ry1);
        }

        if (this.getStrokeColor() != null) {
            g.setStroke(new BasicStroke(this.getStrokeWidth()));
            g.setColor(this.getStrokeColor());
            g.drawRoundRect(x1, y1, x2, y2, rx1, ry1);
        }

        g.setTransform(saveAt);
    }
}
