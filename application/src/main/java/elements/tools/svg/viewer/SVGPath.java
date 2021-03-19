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

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.w3c.dom.Element;

public class SVGPath extends SVGObject {
    ArrayList commandoliste = new ArrayList();
    String strPath = "";
    Rectangle bounds = null;

    public SVGPath() {}

    public void extractPath(GeneralPath path, String strPath) {
        StringTokenizer t = new StringTokenizer(strPath, " ,");

        while (t.hasMoreTokens()) {
            String tempBuffer = t.nextToken();
            float a;
            float b;
            if (tempBuffer.equalsIgnoreCase("M")) {
                a = Float.parseFloat(t.nextToken());
                b = Float.parseFloat(t.nextToken());
                path.moveTo(a, b);
            } else if (tempBuffer.equalsIgnoreCase("L")) {
                a = Float.parseFloat(t.nextToken());
                b = Float.parseFloat(t.nextToken());
                path.lineTo(a, b);
            } else {
                float c;
                float d;
                float e;
                float f;
                if (tempBuffer.equalsIgnoreCase("A")) {
                    a = Float.parseFloat(t.nextToken());
                    b = Float.parseFloat(t.nextToken());
                    c = Float.parseFloat(t.nextToken());
                    d = Float.parseFloat(t.nextToken());
                    e = Float.parseFloat(t.nextToken());
                    f = Float.parseFloat(t.nextToken());
                    float var11 = Float.parseFloat(t.nextToken());
                } else if (tempBuffer.equalsIgnoreCase("C")) {
                    a = Float.parseFloat(t.nextToken());
                    b = Float.parseFloat(t.nextToken());
                    c = Float.parseFloat(t.nextToken());
                    d = Float.parseFloat(t.nextToken());
                    e = Float.parseFloat(t.nextToken());
                    f = Float.parseFloat(t.nextToken());
                    path.curveTo(a, b, c, d, e, f);
                } else if (tempBuffer.equalsIgnoreCase("Z")) {
                    path.closePath();
                }
            }
        }

    }

    public void loadFromXML(Element nodeElement) {
        super.loadFromXML(nodeElement);
        this.strPath = nodeElement.getAttribute("d");
    }

    private AlphaComposite makeComposite(float alpha) {
        int type = 3;
        return AlphaComposite.getInstance(type, alpha);
    }

    public void paint(Graphics2D g, double zoomX, double zoomY) {
        if (this.isVisible()) {
            GeneralPath path = new GeneralPath(0);
            this.extractPath(path, this.strPath);
            AffineTransform saveAt = g.getTransform();
            AffineTransform at = new AffineTransform();
            this.extractTranform(at, this.getTransformString());
            g.transform(at);
            g.translate((double) this.getTransX(), (double) this.getTransY());
            Composite originalComposite = g.getComposite();
            g.setComposite(this.makeComposite(this.getOpacity()));
            if (this.getFillColor() != null) {
                g.setColor(this.getFillColor());
                g.fill(path);
            }

            if (this.getStrokeColor() != null && this.getStrokeWidth() > 0.0F) {
                g.setColor(this.getStrokeColor());
                g.setStroke(new BasicStroke(this.getStrokeWidth()));
                g.draw(path);
            }

            this.bounds = path.getBounds();
            g.setTransform(saveAt);
            g.setComposite(originalComposite);
        }

    }
}
