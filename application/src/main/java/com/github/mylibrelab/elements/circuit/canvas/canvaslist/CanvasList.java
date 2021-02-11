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

package com.github.mylibrelab.elements.circuit.canvas.canvaslist;// *****************************************************************************

import java.awt.Image;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSBoolean;
import VisualLogic.variables.VSCanvas;
import VisualLogic.variables.VSGroup;

public class CanvasList extends JVSMain {

    private Image image;

    private VSCanvas inCanvas;
    private VSBoolean inAdd;
    private VSBoolean inClear;
    private final VSGroup out = new VSGroup();


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
        initPins(0, 1, 0, 3);
        setSize(32 + 22, 40);

        initPinVisibility(false, true, false, true);

        element.jSetInnerBorderVisibility(true);

        setPin(0, ExternalIF.C_GROUP, element.PIN_OUTPUT); // Out
        setPin(1, ExternalIF.C_CANVAS, element.PIN_INPUT); // Canvas
        setPin(2, ExternalIF.C_BOOLEAN, element.PIN_INPUT); // Add
        setPin(3, ExternalIF.C_BOOLEAN, element.PIN_INPUT); // Clear List

        element.jSetPinDescription(0, "out");
        element.jSetPinDescription(1, "Object");
        element.jSetPinDescription(2, "Add");
        element.jSetPinDescription(3, "Clear");

        String fileName = element.jGetSourcePath() + "icon.gif";
        image = element.jLoadImage(fileName);

        setName("CanvasList");
    }


    public void start() {
        out.list.clear();
        out.list.add(inCanvas);
    }

    public void stop() {

    }


    public void initInputPins() {
        inCanvas = (VSCanvas) element.getPinInputReference(1);
        inAdd = (VSBoolean) element.getPinInputReference(2);
        inClear = (VSBoolean) element.getPinInputReference(3);

        if (inCanvas == null) inCanvas = new VSCanvas();
        if (inAdd == null) inAdd = new VSBoolean(false);
        if (inClear == null) inClear = new VSBoolean(false);

    }

    public void initOutputPins() {
        element.setPinOutputReference(0, out);
    }


    public void process() {
        if (inClear.getValue() == true) {
            start();
            out.setChanged(true);
            element.notifyPin(0);
        } else {
            if (inAdd.getValue() == true) {
                if (inCanvas instanceof VSCanvas) {
                    VSCanvas tmp = new VSCanvas();

                    tmp.copyValueFrom(inCanvas);
                    out.list.add(tmp);

                    out.setChanged(true);
                    element.notifyPin(0);
                }
            } else {
                out.setChanged(true);
                element.notifyPin(0);
            }
        }
    }

}
