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
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.StringTokenizer;

import org.w3c.dom.Element;

public class SVGObject {
    private Color fillColor = new Color(255, 255, 255);
    private Color strokeColor = new Color(0, 0, 0);
    private float strokeWidth = 0.0F;
    private String strTransForm = "";
    private float opacity = 1.0F;
    private String id = "";
    private boolean visible = true;
    private float translateX;
    private float translateY;

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean value) {
        this.visible = value;
    }

    public float getTransX() {
        return this.translateX;
    }

    public float getTransY() {
        return this.translateY;
    }

    public void translate(float x, float y) {
        this.translateX = x;
        this.translateY = y;
    }

    public String getTransformString() {
        return this.strTransForm;
    }

    public String getID() {
        return this.id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public float getOpacity() {
        return this.opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public float getStrokeWidth() {
        return this.strokeWidth;
    }

    public void setStrokeWidth(float width) {
        this.strokeWidth = width;
    }

    public Color getStrokeColor() {
        return this.strokeColor;
    }

    public void setStrokeColor(Color color) {
        this.strokeColor = color;
    }

    public Color getFillColor() {
        return this.fillColor;
    }

    public void setFillColor(Color color) {
        this.fillColor = color;
    }

    public SVGObject() {}

    public void extractTranform(AffineTransform at, String trans) {
        StringTokenizer t = new StringTokenizer(trans, " (),");

        while (t.hasMoreTokens()) {
            String tempBuffer = t.nextToken();
            float a;
            float b;
            if (tempBuffer.equalsIgnoreCase("translate")) {
                a = Float.parseFloat(t.nextToken());
                b = Float.parseFloat(t.nextToken());
                at.translate((double) a, (double) b);
            } else if (tempBuffer.equalsIgnoreCase("scale")) {
                a = Float.parseFloat(t.nextToken());
                at.scale((double) a, (double) a);
            } else if (tempBuffer.equalsIgnoreCase("rotate")) {
                a = Float.parseFloat(t.nextToken());
                at.rotate((double) a);
            } else if (tempBuffer.equalsIgnoreCase("matrix")) {
                a = Float.parseFloat(t.nextToken());
                b = Float.parseFloat(t.nextToken());
                float c = Float.parseFloat(t.nextToken());
                float d = Float.parseFloat(t.nextToken());
                float e = Float.parseFloat(t.nextToken());
                float f = Float.parseFloat(t.nextToken());
                at.setTransform((double) a, (double) b, (double) c, (double) d, (double) e, (double) f);
            }
        }

    }

    public void extractStyle(String style) {
        StringTokenizer t = new StringTokenizer(style, " ;");

        while (t.hasMoreTokens()) {
            String subString = t.nextToken();
            StringTokenizer sub = new StringTokenizer(subString, " :");

            while (sub.hasMoreTokens()) {
                String tempBuffer = sub.nextToken();
                if (tempBuffer.equalsIgnoreCase("stroke")) {
                    this.strokeColor = this.extractFillColor(sub.nextToken());
                } else if (tempBuffer.equalsIgnoreCase("stroke-width")) {
                    this.strokeWidth = this.extractFloatValue(sub.nextToken());
                } else if (tempBuffer.equalsIgnoreCase("fill")) {
                    this.fillColor = this.extractFillColor(sub.nextToken());
                } else if (tempBuffer.equalsIgnoreCase("opacity")) {
                    this.opacity = Float.parseFloat(sub.nextToken());
                }
            }
        }

    }

    public float extractFloatValue(String strokeW) {
        if (strokeW.length() > 1) {
            strokeW = strokeW.substring(0, strokeW.length() - 2);

            try {
                return Float.parseFloat(strokeW);
            } catch (Exception var3) {
            }
        }

        return 1.0F;
    }

    public Color extractFillColor(String col) {
        if (col.equalsIgnoreCase("")) {
            return null;
        } else if (col.equalsIgnoreCase("none")) {
            return null;
        } else if (col.equalsIgnoreCase("darkblue")) {
            return new Color(0, 0, 150);
        } else if (col.equalsIgnoreCase("BLACK")) {
            return Color.BLACK;
        } else if (col.equalsIgnoreCase("BLUE")) {
            return Color.BLUE;
        } else if (col.equalsIgnoreCase("CYAN")) {
            return Color.CYAN;
        } else if (col.equalsIgnoreCase("DARK_GRAY")) {
            return Color.DARK_GRAY;
        } else if (col.equalsIgnoreCase("GRAY")) {
            return Color.GRAY;
        } else if (col.equalsIgnoreCase("GREEN")) {
            return Color.GREEN;
        } else if (col.equalsIgnoreCase("LIGHT_GRAY")) {
            return Color.LIGHT_GRAY;
        } else if (col.equalsIgnoreCase("MAGENTA")) {
            return Color.MAGENTA;
        } else if (col.equalsIgnoreCase("ORANGE")) {
            return Color.ORANGE;
        } else if (col.equalsIgnoreCase("PINK")) {
            return Color.PINK;
        } else if (col.equalsIgnoreCase("RED")) {
            return Color.RED;
        } else if (col.equalsIgnoreCase("WHITE")) {
            return Color.WHITE;
        } else if (col.equalsIgnoreCase("YELLOW")) {
            return Color.YELLOW;
        } else {
            String c = col.substring(1);

            try {
                return new Color(Integer.parseInt(c, 16));
            } catch (Exception var4) {
                return null;
            }
        }
    }

    public void loadFromXML(Element nodeElement) {
        this.id = nodeElement.getAttribute("id");
        this.fillColor = this.extractFillColor(nodeElement.getAttribute("fill"));
        this.strokeColor = this.extractFillColor(nodeElement.getAttribute("stroke"));
        this.strokeWidth = this.extractFloatValue(nodeElement.getAttribute("stroke-width"));
        this.extractStyle(nodeElement.getAttribute("style"));
        this.strTransForm = nodeElement.getAttribute("transform");
    }

    public void paint(Graphics2D g, double zoomX, double zoomY) {}
}
