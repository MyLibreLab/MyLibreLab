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

package com.github.mylibrelab.elements.circuit.canvas.drawimage;// *****************************************************************************

import java.awt.*;
import java.awt.image.MemoryImageSource;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ElementActionEvent;
import VisualLogic.ExternalIF;
import VisualLogic.variables.VSBoolean;
import VisualLogic.variables.VSCanvas;
import VisualLogic.variables.VSImage24;
import VisualLogic.variables.VSInteger;

public class DrawImage extends JVSMain {
    private Image image;
    private VSImage24 img;
    private VSInteger x;
    private VSInteger y;
    private VSInteger xwidth;
    private VSInteger xheight;
    private VSBoolean interpolation;
    private final VSCanvas out = new VSCanvas();

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
        initPins(0, 1, 0, 6);
        setSize(32 + 22, 20 + 4 + 6 * 10);

        initPinVisibility(false, true, false, true);

        element.jSetInnerBorderVisibility(true);

        setPin(0, ExternalIF.C_CANVAS, element.PIN_OUTPUT); // Image
        setPin(1, ExternalIF.C_IMAGE, element.PIN_INPUT); // Image
        setPin(2, ExternalIF.C_INTEGER, element.PIN_INPUT); // X
        setPin(3, ExternalIF.C_INTEGER, element.PIN_INPUT); // Y
        setPin(4, ExternalIF.C_INTEGER, element.PIN_INPUT); // WIDTH
        setPin(5, ExternalIF.C_INTEGER, element.PIN_INPUT); // HEIGHT
        setPin(6, ExternalIF.C_BOOLEAN, element.PIN_INPUT); // interpolation

        element.jSetPinDescription(0, "Image");
        element.jSetPinDescription(1, "Image");
        element.jSetPinDescription(2, "X");
        element.jSetPinDescription(3, "Y");
        element.jSetPinDescription(4, "width");
        element.jSetPinDescription(5, "height");
        element.jSetPinDescription(6, "interpolation");


        String fileName = element.jGetSourcePath() + "icon.gif";
        image = element.jLoadImage(fileName);

        setName("DrawImage 1.0");
    }


    public void start() {
        process();
    }


    public void initInputPins() {
        img = (VSImage24) element.getPinInputReference(1);
        x = (VSInteger) element.getPinInputReference(2);
        y = (VSInteger) element.getPinInputReference(3);
        xwidth = (VSInteger) element.getPinInputReference(4);
        xheight = (VSInteger) element.getPinInputReference(5);
        interpolation = (VSBoolean) element.getPinInputReference(6);

        if (img == null) img = new VSImage24(1, 1);
        if (x == null) x = new VSInteger(0);
        if (y == null) y = new VSInteger(0);
        if (xwidth == null) xwidth = new VSInteger(10);
        if (xheight == null) xheight = new VSInteger(100);
        if (interpolation == null) interpolation = new VSBoolean(false);

    }

    public void initOutputPins() {
        element.setPinOutputReference(0, out);
    }



    public void elementActionPerformed(ElementActionEvent evt) {

        if (out != null) {

            // Image Input
            if (evt.getSourcePinIndex() == 1) {
                VSImage24 vsimg = img;

                Image gf = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(vsimg.getWidth(),
                        vsimg.getHeight(), vsimg.getPixels(), 0, vsimg.getWidth()));
                out.image = gf;
                xwidth.setValue(vsimg.getWidth());
                xheight.setValue(vsimg.getHeight());
                out.type = element.C_SHAPE_IMAGE;
                out.x1 = x.getValue();
                out.y1 = y.getValue();
                out.x2 = xwidth.getValue();
                out.y2 = xheight.getValue();
                // Rectangle2D shape = new Rectangle2D.Double(xwidth.getValue(),xheight.getValue());

                out.antialising = interpolation.getValue();
                out.setChanged(true);
                element.notifyPin(0);
            } else {
                // Rectangle2D shape = new
                // Rectangle2D.Double(x.getValue(),y.getValue(),xwidth.getValue(),xheight.getValue());
                out.type = element.C_SHAPE_IMAGE;
                out.x1 = x.getValue();
                out.y1 = y.getValue();
                out.x2 = xwidth.getValue();
                out.y2 = xheight.getValue();
                out.antialising = interpolation.getValue();
                out.setChanged(true);
                element.notifyPin(0);
            }

        }
    }


}
