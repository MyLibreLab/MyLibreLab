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

package com.github.mylibrelab.elements.circuit.canvas.transforpackagem;// *****************************************************************************

import java.awt.Image;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSCanvas;
import VisualLogic.variables.VSDouble;
import VisualLogic.variables.VSInteger;

public class Transformation extends JVSMain {
    private Image image;

    private VSDouble angle;
    private VSInteger anglePointX;
    private VSInteger anglePointY;


    private VSDouble translateX;
    private VSDouble translateY;

    private VSDouble scaleX;
    private VSDouble scaleY;

    private VSDouble shearX;
    private VSDouble shearY;

    private VSCanvas in;
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
        initPins(0, 1, 0, 10);
        setSize(32 + 22, 20 + 4 + 10 * 10);

        initPinVisibility(false, true, false, true);

        element.jSetInnerBorderVisibility(true);

        setPin(0, ExternalIF.C_CANVAS, element.PIN_OUTPUT); // Image
        setPin(1, ExternalIF.C_CANVAS, element.PIN_INPUT); // Image

        setPin(2, ExternalIF.C_DOUBLE, element.PIN_INPUT); // Angle
        setPin(3, ExternalIF.C_INTEGER, element.PIN_INPUT); // AngleX
        setPin(4, ExternalIF.C_INTEGER, element.PIN_INPUT); // AngleY

        setPin(5, ExternalIF.C_DOUBLE, element.PIN_INPUT); // Translate X
        setPin(6, ExternalIF.C_DOUBLE, element.PIN_INPUT); // Translate Y

        setPin(7, ExternalIF.C_DOUBLE, element.PIN_INPUT); // Scale X
        setPin(8, ExternalIF.C_DOUBLE, element.PIN_INPUT); // Scale Y

        setPin(9, ExternalIF.C_DOUBLE, element.PIN_INPUT); // Shear X
        setPin(10, ExternalIF.C_DOUBLE, element.PIN_INPUT); // Shear Y

        element.jSetPinDescription(0, "Out");
        element.jSetPinDescription(1, "In");
        element.jSetPinDescription(2, "Rotation angle");
        element.jSetPinDescription(3, "Rotation Point X");
        element.jSetPinDescription(4, "Rotation Point Y");
        element.jSetPinDescription(5, "Translate X");
        element.jSetPinDescription(6, "Translate Y");
        element.jSetPinDescription(7, "Scale X");
        element.jSetPinDescription(8, "Scale Y");
        element.jSetPinDescription(9, "Shear X");
        element.jSetPinDescription(10, "Shear Y");

        String fileName = element.jGetSourcePath() + "icon.gif";
        image = element.jLoadImage(fileName);

        setName("Translate 1.0");
    }



    public void initInputPins() {
        in = (VSCanvas) element.getPinInputReference(1);

        angle = (VSDouble) element.getPinInputReference(2);
        anglePointX = (VSInteger) element.getPinInputReference(3);
        anglePointY = (VSInteger) element.getPinInputReference(4);

        translateX = (VSDouble) element.getPinInputReference(5);
        translateY = (VSDouble) element.getPinInputReference(6);

        scaleX = (VSDouble) element.getPinInputReference(7);
        scaleY = (VSDouble) element.getPinInputReference(8);

        shearX = (VSDouble) element.getPinInputReference(9);
        shearY = (VSDouble) element.getPinInputReference(10);


        if (angle == null) angle = new VSDouble(0.0);
        if (anglePointX == null) anglePointX = new VSInteger(0);
        if (anglePointY == null) anglePointY = new VSInteger(0);

        if (translateX == null) translateX = new VSDouble(0.0);
        if (translateY == null) translateY = new VSDouble(0.0);

        if (scaleX == null) scaleX = new VSDouble(1.0);
        if (scaleY == null) scaleY = new VSDouble(1.0);

        if (shearX == null) shearX = new VSDouble(0.0);
        if (shearY == null) shearY = new VSDouble(0.0);
    }

    public void initOutputPins() {
        element.setPinOutputReference(0, out);
    }



    public void process() {
        if (in != null) {
            out.copyValueFrom(in);

            out.rotationAngle = angle.getValue();
            out.rotationX = anglePointX.getValue();
            out.rotationY = anglePointY.getValue();

            out.translationX = translateX.getValue();
            out.translationY = translateY.getValue();

            out.scaleX = scaleX.getValue();
            out.scaleY = scaleY.getValue();

            out.shearX = shearX.getValue();
            out.shearY = shearY.getValue();


            out.setChanged(true);
            element.notifyPin(0);
        }
    }



}
