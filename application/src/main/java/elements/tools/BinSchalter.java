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

package elements.tools;


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

public class BinSchalter extends JVSMain {
    private final boolean value = false;
    private final String fileNameImageON;
    private final String fileNameImageOFF;
    private Image image = null;
    private Image imageON = null;
    private Image imageOFF = null;
    private int sizeW = 30;
    private int sizeH = 30;
    private boolean down = false;

    public BinSchalter(String fileNameImageON, String fileNameImageOFF, int sizeW, int sizeH) {
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
        element.jSetLeftPinsVisible(false);
        element.jSetTopPinsVisible(false);

        element.jSetBottomPinsVisible(false);
        element.jSetSize(sizeW + 10 + 2, sizeH + 2);
        element.jSetTopPins(0);
        element.jSetRightPins(1);
        element.jSetBottomPins(0);
        element.jSetLeftPins(0);

        imageON = element.jLoadImage(element.jGetSourcePath() + fileNameImageON);
        imageOFF = element.jLoadImage(element.jGetSourcePath() + fileNameImageOFF);
        image = imageOFF;
    }

    private void setValue(boolean value) {
        if (down) {
            element.writePin(0, 1.0);
        } else {
            element.writePin(0, 0.0);
        }

    }

    public void process() {
        double pin1 = element.readPin(0);
        setValue(pin1 > 0);
    }


    public void mousePressed(MouseEvent e) {
        if (down == true) {
            down = false;
            element.writePin(0, 0.0);
            image = imageOFF;
            element.jRepaint();
        } else {
            down = true;
            element.writePin(0, 1.0);
            image = imageON;
            element.jRepaint();
        }

    }

}