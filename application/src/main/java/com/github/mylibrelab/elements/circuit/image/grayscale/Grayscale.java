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

package com.github.mylibrelab.elements.circuit.image.grayscale;// *****************************************************************************

import java.awt.*;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSImage24;

public class Grayscale extends JVSMain {
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
        setSize(32 + 20 + 4, 24 + 5);

        initPinVisibility(false, true, false, true);

        element.jSetInnerBorderVisibility(true);

        setPin(0, ExternalIF.C_IMAGE, element.PIN_OUTPUT); // Image
        setPin(1, ExternalIF.C_IMAGE, element.PIN_INPUT); // Image

        element.jSetPinDescription(0, "Image Out");
        element.jSetPinDescription(1, "Image In");


        String fileName = element.jGetSourcePath() + "icon.jpg";
        image = element.jLoadImage(fileName);

        element.jSetCaptionVisible(true);
        element.jSetCaption("Grayscale");
        setName("Grayscale");
    }



    public void initInputPins() {
        in = (VSImage24) element.getPinInputReference(1);
    }

    public void initOutputPins() {
        element.setPinOutputReference(0, out);
    }


    public int[] grayscale(int[] pixels) {
        int w = in.getWidth();
        int h = in.getHeight();
        int pixel1;
        int gray1;

        RGB rgb = new RGB();


        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                pixel1 = pixels[((j) * w) + (i)];

                rgb.assign(pixel1);
                gray1 = (rgb.red + rgb.blue + rgb.green) / 3;

                rgb.assignRGB(gray1, gray1, gray1, 255);

                pixels[j * w + i] = rgb.getPixel();
            }
        }
        return pixels;

    }



    public void process() {
        if (in != null) {

            out.setPixels(grayscale(in.getPixels().clone()), in.getWidth(), in.getHeight());
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
