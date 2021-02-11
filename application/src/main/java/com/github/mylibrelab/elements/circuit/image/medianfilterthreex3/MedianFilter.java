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

package com.github.mylibrelab.elements.circuit.image.medianfilterthreex3;// *****************************************************************************

import java.awt.Image;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSImage24;

public class MedianFilter extends JVSMain {
    private Image image;
    private VSImage24 in;
    private final VSImage24 out = new VSImage24(1, 1);

    public void onDispose() {
        if (image != null) {
            image.flush();
            image = null;
        }
    }

    public void paint(java.awt.Graphics g) {
        drawImageCentred(g, image);
    }

    public void init() {
        initPins(0, 1, 0, 1);
        setSize(32 + 22, 32 + 4);

        initPinVisibility(false, true, false, true);

        element.jSetInnerBorderVisibility(true);

        setPin(0, ExternalIF.C_IMAGE, element.PIN_OUTPUT); // Image
        setPin(1, ExternalIF.C_IMAGE, element.PIN_INPUT); // Image

        element.jSetPinDescription(0, "Image Out");
        element.jSetPinDescription(1, "Image In");

        String fileName = element.jGetSourcePath() + "icon.gif";
        image = element.jLoadImage(fileName);

        element.jSetCaptionVisible(true);
        element.jSetCaption("Median Filter");
        setName("Median Filter");

    }



    public void initInputPins() {
        in = (VSImage24) element.getPinInputReference(1);
    }

    public void initOutputPins() {
        element.setPinOutputReference(0, out);
    }

    private boolean isABiggerB(RGB a, RGB b) {
        int gray1 = (a.red + a.blue + a.green) / 3;
        int gray2 = (b.red + b.blue + b.green) / 3;

        return gray1 > gray2;
    }

    private void bubblesort(RGB[] feld) {
        int maxIndex = feld.length;

        for (int i = 0; i < maxIndex; i++) {
            for (int k = 0; k < maxIndex - i - 1; k++) {
                if (isABiggerB(feld[k], feld[k + 1]))
                // if (feld[k]>feld[k+1])
                {
                    RGB ablage = feld[k];
                    feld[k] = feld[k + 1];
                    feld[k + 1] = ablage;
                }
            }
        }

    }

    // distance = zb: 3x3
    public int[] medianFilter(int[] pixels, int distance) {
        int w = in.getWidth();
        int h = in.getHeight();
        int pixel1;
        int gray1;
        int c = 0;
        int a = 0;

        RGB rgb = new RGB();
        RGB col = new RGB();

        RGB[] feld = new RGB[distance * distance];

        for (int i = 0; i < feld.length; i++) {
            feld[i] = new RGB();
        }

        int mitte = (distance * distance) / 2;

        int anzahl = distance * distance;

        for (int j = 0; j < h - distance; j++) {
            for (int i = 0; i < w - distance; i++) {
                c = 0;
                for (int y = 0; y < distance; y++) {
                    for (int x = 0; x < distance; x++) {
                        pixel1 = pixels[((j + y + 1) * w) + (i + x + 1)];
                        feld[c++].assign(pixel1);
                    }
                }

                bubblesort(feld);

                pixels[j * w + (i)] = feld[mitte].getPixel();
            }
        }
        return pixels;

    }



    public void process() {
        if (in != null && out != null) {
            out.setPixels(medianFilter(in.getPixels().clone(), 3), in.getWidth(), in.getHeight());
            out.setChanged(true);
            element.notifyPin(0);
        }
    }



}


class RGB extends Object {
    public int red;
    public int green;
    public int blue;
    public int alpha;

    public void assign(int pixel) {
        red = (pixel >> 16) & 0xff;
        green = (pixel >> 8) & 0xff;
        blue = (pixel) & 0xff;
        alpha = (pixel >> 24) & 0xff;
    }

    public void assignRGB(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public int getPixel() {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

}
