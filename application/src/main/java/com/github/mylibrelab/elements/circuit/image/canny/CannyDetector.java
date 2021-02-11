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

package com.github.mylibrelab.elements.circuit.image.canny;// *****************************************************************************

import java.awt.Image;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSDouble;
import VisualLogic.variables.VSImage24;
import VisualLogic.variables.VSInteger;

public class CannyDetector extends JVSMain {
    private Image image;
    private VSImage24 in;
    private VSDouble theta;
    private VSInteger lowthresh;
    private VSInteger highthresh;


    private final VSImage24 out = new VSImage24(1, 1);

    public void onDispose() {
        if (image != null) {
            image.flush();
            image = null;
        }
    }

    public void paint(java.awt.Graphics g) {
        try {
            drawImageCentred(g, image);
        } catch (Exception e) {
        }
    }

    public void init() {
        initPins(0, 1, 0, 4);
        setSize(32 + 22, 50);

        initPinVisibility(false, true, false, true);

        element.jSetInnerBorderVisibility(true);

        setPin(0, ExternalIF.C_IMAGE, element.PIN_OUTPUT); // Image
        setPin(1, ExternalIF.C_IMAGE, element.PIN_INPUT); // Image

        setPin(2, ExternalIF.C_DOUBLE, element.PIN_INPUT); // Image
        setPin(3, ExternalIF.C_INTEGER, element.PIN_INPUT); // Image
        setPin(4, ExternalIF.C_INTEGER, element.PIN_INPUT); // Image

        element.jSetPinDescription(0, "Image Out");
        element.jSetPinDescription(1, "Image In");
        element.jSetPinDescription(2, "theta");
        element.jSetPinDescription(3, "lowthresh");
        element.jSetPinDescription(4, "highthresh");


        String fileName = element.jGetSourcePath() + "icon.gif";
        image = element.jLoadImage(fileName);

        element.jSetCaptionVisible(true);
        element.jSetCaption("Canny");
        setName("Canny Detector");

    }



    public void initInputPins() {
        in = (VSImage24) element.getPinInputReference(1);

        theta = (VSDouble) element.getPinInputReference(2);
        lowthresh = (VSInteger) element.getPinInputReference(3);
        highthresh = (VSInteger) element.getPinInputReference(4);

        if (theta == null) {
            theta = new VSDouble(3.0);
        } else {
            theta.setValue(3.0);
        }

        if (lowthresh == null) {
            lowthresh = new VSInteger(1);
        } else {
            lowthresh.setValue(1);
        }

        if (highthresh == null) {
            highthresh = new VSInteger(5);
        } else {
            highthresh.setValue(5);
        }


    }

    public void initOutputPins() {
        element.setPinOutputReference(0, out);
    }


    public int[] handlepixels(VSImage24 img) {
        try {
            int w = img.getWidth();
            int h = img.getHeight();

            int[] pixBuff = new int[w * h];

            Canny c = new Canny();
            pixBuff = c.apply_canny(img.getPixels(), w, h, w * h, (float) theta.getValue(), lowthresh.getValue(),
                    highthresh.getValue(), (float) 1.0, 0);

            return pixBuff;
        } catch (Exception e) {
            return new int[1 * 1];
        }

    }



    public void process() {
        if (in != null) {
            try {
                if (in.getWidth() > 1 && in.getHeight() > 1) {
                    out.setPixels(handlepixels(in), in.getWidth(), in.getHeight());
                    out.setChanged(true);
                    element.notifyPin(0);
                    Thread.sleep(5);
                }
            } catch (Exception e) {
            }
        }
    }



}
