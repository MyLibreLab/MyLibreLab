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

package com.github.mylibrelab.elements.circuit.image.jpeg.decoder;/*
                                                                   * Copyright (C) 2020 MyLibreLab
                                                                   * Based on MyOpenLab by Carmelo Salafia
                                                                   * www.myopenlab.de
                                                                   * Copyright (C) 2004 Carmelo Salafia cswi@gmx.de
                                                                   *
                                                                   * This program is free software: you can redistribute
                                                                   * it and/or modify
                                                                   * it under the terms of the GNU General Public
                                                                   * License as published by
                                                                   * the Free Software Foundation, either version 3 of
                                                                   * the License, or
                                                                   * (at your option) any later version.
                                                                   *
                                                                   * This program is distributed in the hope that it
                                                                   * will be useful,
                                                                   * but WITHOUT ANY WARRANTY; without even the implied
                                                                   * warranty of
                                                                   * MERCHANTABILITY or FITNESS FOR A PARTICULAR
                                                                   * PURPOSE. See the
                                                                   * GNU General Public License for more details.
                                                                   *
                                                                   * You should have received a copy of the GNU General
                                                                   * Public License
                                                                   * along with this program. If not, see
                                                                   * <http://www.gnu.org/licenses/>.
                                                                   *
                                                                   */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VS1DByte;
import VisualLogic.variables.VSImage24;

public class JPEGDECODER extends JVSMain {
    private Image image;
    private VS1DByte in;
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
        setPin(1, ExternalIF.C_ARRAY1D_BYTE, element.PIN_INPUT); // Image

        element.jSetPinDescription(0, "Image Out");
        element.jSetPinDescription(1, "In");

        String fileName = element.jGetSourcePath() + "icon.gif";
        image = element.jLoadImage(fileName);

        element.jSetCaptionVisible(true);
        element.jSetCaption("JPEG Decoder");
        setName("JPED Decoder");
    }



    public void initInputPins() {
        in = (VS1DByte) element.getPinInputReference(1);
    }

    public void initOutputPins() {
        element.setPinOutputReference(0, out);
    }


    public int[] image2Pixel(Image imgx, int w, int h) {
        int[] pixels = new int[w * h];
        PixelGrabber pg = new PixelGrabber(imgx, 0, 0, w, h, pixels, 0, w);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            element.jException("Fehler in Methode image2Pixel: " + e.toString());
        }

        return pixels;
    }


    int w = 0, h = 0;

    public int[] decode() {
        try {
            byte[] bytes = in.getBytes().clone();

            if (bytes.length > 1) {
                InputStream in = new ByteArrayInputStream(bytes);

                JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
                BufferedImage bi = decoder.decodeAsBufferedImage();

                w = bi.getWidth();
                h = bi.getHeight();
                return image2Pixel(bi, w, h);
            }

        } catch (Exception e) {
            element.jShowMessage("Fehler in Methode decode JPEGDECODER: " + e.toString());
            return new int[1];
        }
        return new int[1];
    }



    public void process() {
        if (in != null) {
            out.setPixels(decode(), w, h);
            out.setChanged(true);
            element.notifyPin(0);
        }
    }



}
