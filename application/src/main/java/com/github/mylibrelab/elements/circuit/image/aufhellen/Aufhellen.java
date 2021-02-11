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

package com.github.mylibrelab.elements.circuit.image.aufhellen;// *****************************************************************************

import java.awt.*;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSDouble;
import VisualLogic.variables.VSImage24;

public class Aufhellen extends JVSMain {
    private Image image;
    private VSImage24 in;
    private VSDouble inThreshold;
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
        initPins(0, 1, 0, 2);
        setSize(32 + 22, 32 + 4);

        initPinVisibility(false, true, false, true);

        element.jSetInnerBorderVisibility(true);

        setPin(0, ExternalIF.C_IMAGE, element.PIN_OUTPUT); // Image
        setPin(1, ExternalIF.C_IMAGE, element.PIN_INPUT); // Image
        setPin(2, ExternalIF.C_DOUBLE, element.PIN_INPUT); // Image

        element.jSetPinDescription(0, "Image Out");
        element.jSetPinDescription(1, "Image In");
        element.jSetPinDescription(2, "Helligkeit (0-255)");

        String fileName = element.jGetSourcePath() + "icon.gif";
        image = element.jLoadImage(fileName);

        element.jSetCaptionVisible(true);
        element.jSetCaption("Helligkeit");
        setName("Helligkeit");

    }



    public void initInputPins() {
        in = (VSImage24) element.getPinInputReference(1);
        inThreshold = (VSDouble) element.getPinInputReference(2);

        if (in == null) {
            in = new VSImage24(1, 1);
        }
        if (inThreshold == null) {
            inThreshold = new VSDouble(128);
        }
    }

    public void initOutputPins() {
        element.setPinOutputReference(0, out);
    }


    public int[] aufhellen(int[] pixels, double threshold) {
        int w = in.getWidth();
        int h = in.getHeight();
        int pixel1;
        int grayR;
        int grayG;
        int grayB;

        RGB rgb = new RGB();

        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                pixel1 = pixels[((j) * w) + (i)];

                rgb.assign(pixel1);

                grayR = (int) ((double) rgb.red * inThreshold.getValue());
                grayG = (int) ((double) rgb.green * inThreshold.getValue());
                grayB = (int) ((double) rgb.blue * inThreshold.getValue());

                if (grayR > 255) grayR = 255;
                if (grayG > 255) grayG = 255;
                if (grayB > 255) grayB = 255;

                rgb.assignRGB(grayR, grayG, grayB, 255);

                pixels[j * w + i] = rgb.getPixel();
            }
        }
        return pixels;

    }



    public void process() {
        if (in != null) {
            out.setPixels(aufhellen(in.getPixels().clone(), inThreshold.getValue()), in.getWidth(), in.getHeight());

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
