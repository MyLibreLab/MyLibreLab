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

package com.github.mylibrelab.elements.tools;


import java.awt.*;

public class BinAnzeige extends JVSMain {
    private final String fileNameImageON;
    private final String fileNameImageOFF;
    private boolean value = false;
    private Image image = null;
    private Image imageON = null;
    private Image imageOFF = null;
    private int sizeW = 30;
    private int sizeH = 30;

    public BinAnzeige(String fileNameImageON, String fileNameImageOFF, int sizeW, int sizeH) {
        this.fileNameImageON = fileNameImageON;
        this.fileNameImageOFF = fileNameImageOFF;
        this.sizeW = sizeW;
        this.sizeH = sizeH;
    }

    public void paint(java.awt.Graphics g) {
        if (element != null) {
            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            Rectangle r = element.jGetBounds();
            if (image != null) g2.drawImage(image, r.x + 1, r.y + 1, r.width - 2, r.height - 2, null);

        }
    }

    public void init() {
        element.jSetInnerBorderVisibility(false);
        element.jSetTopPinsVisible(false);
        element.jSetRightPinsVisible(false);
        element.jSetBottomPinsVisible(false);
        element.jSetSize(sizeW + 10 + 2, sizeH + 2);
        element.jSetTopPins(0);
        element.jSetRightPins(0);
        element.jSetBottomPins(0);
        element.jSetLeftPins(1);

        imageON = element.jLoadImage(element.jGetSourcePath() + fileNameImageON);
        imageOFF = element.jLoadImage(element.jGetSourcePath() + fileNameImageOFF);
        image = imageOFF;
    }

    private void setValue(boolean value) {
        if (this.value != value) {
            this.value = value;
            if (value == false) {
                image = imageOFF;
            } else {
                image = imageON;
            }

            element.jRepaint();
        }
    }

    public void process() {
        double pin1 = element.readPin(0);
        setValue(pin1 > 0);
    }

}
