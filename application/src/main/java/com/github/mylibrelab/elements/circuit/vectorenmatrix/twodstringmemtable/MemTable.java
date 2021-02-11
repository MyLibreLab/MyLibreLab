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

package com.github.mylibrelab.elements.circuit.vectorenmatrix.twodstringmemtable;/*
                                                                                  * Copyright (C) 2020 MyLibreLab
                                                                                  * Based on MyOpenLab by Carmelo
                                                                                  * Salafia www.myopenlab.de
                                                                                  * Copyright (C) 2004 Carmelo Salafia
                                                                                  * cswi@gmx.de
                                                                                  *
                                                                                  * This program is free software: you
                                                                                  * can redistribute it and/or modify
                                                                                  * it under the terms of the GNU
                                                                                  * General Public License as published
                                                                                  * by
                                                                                  * the Free Software Foundation, either
                                                                                  * version 3 of the License, or
                                                                                  * (at your option) any later version.
                                                                                  *
                                                                                  * This program is distributed in the
                                                                                  * hope that it will be useful,
                                                                                  * but WITHOUT ANY WARRANTY; without
                                                                                  * even the implied warranty of
                                                                                  * MERCHANTABILITY or FITNESS FOR A
                                                                                  * PARTICULAR PURPOSE. See the
                                                                                  * GNU General Public License for more
                                                                                  * details.
                                                                                  *
                                                                                  * You should have received a copy of
                                                                                  * the GNU General Public License
                                                                                  * along with this program. If not, see
                                                                                  * <http://www.gnu.org/licenses/>.
                                                                                  *
                                                                                  */

import java.awt.Image;
import java.util.Locale;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VS2DBoolean;
import VisualLogic.variables.VS2DDouble;
import VisualLogic.variables.VS2DInteger;
import VisualLogic.variables.VS2DString;
import VisualLogic.variables.VSBoolean;
import VisualLogic.variables.VSObject;

public class MemTable extends JVSMain {
    private Image image;
    private VSObject in = null;
    private VSBoolean read = null;
    private VSBoolean store = null;

    private VSObject out;
    private VSObject memory;


    public void paint(java.awt.Graphics g) {
        drawImageCentred(g, image);
    }

    public void onDispose() {
        if (image != null) {
            image.flush();
            image = null;
        }
    }

    public void init() {
        initPins(0, 1, 0, 3);
        setSize(20 + 32 + 3, 36);
        initPinVisibility(false, true, false, true);
        element.jSetInnerBorderVisibility(true);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");

        setPin(0, ExternalIF.C_VARIANT, element.PIN_OUTPUT); // Out
        setPin(1, ExternalIF.C_VARIANT, element.PIN_INPUT); // in
        setPin(2, ExternalIF.C_BOOLEAN, element.PIN_INPUT); // read
        setPin(3, ExternalIF.C_BOOLEAN, element.PIN_INPUT); // Store

        String strLocale = Locale.getDefault().toString();

        element.jSetPinDescription(0, "out");
        element.jSetPinDescription(1, "in");
        element.jSetPinDescription(2, "read");
        element.jSetPinDescription(3, "store");


        element.jSetCaptionVisible(false);
        element.jSetCaption("2DMemTable");
        setName("2DMemTable");
    }


    public void initInputPins() {
        in = (VSObject) element.getPinInputReference(1);
        read = (VSBoolean) element.getPinInputReference(2);
        store = (VSBoolean) element.getPinInputReference(3);


        if (read == null) read = new VSBoolean(false);
        if (store == null) store = new VSBoolean(false);
    }


    public void initOutputPins() {
        out = element.jCreatePinDataType(0);
        element.setPinOutputReference(0, out);

        if (out instanceof VS2DString) {
            memory = new VS2DString(0, 0);
        }
        if (out instanceof VS2DBoolean) {
            memory = new VS2DBoolean(0, 0);
        }
        if (out instanceof VS2DInteger) {
            memory = new VS2DInteger(0, 0);
        }
        if (out instanceof VS2DDouble) {
            memory = new VS2DDouble(0, 0);
        }
    }


    public void checkPinDataType() {
        boolean pinIn = element.hasPinWire(1);

        int dt = element.C_VARIANT;

        if (pinIn == true) dt = element.jGetPinDrahtSourceDataType(1);

        element.jSetPinDataType(1, dt);
        element.jSetPinIO(1, element.PIN_INPUT);

        element.jSetPinDataType(0, dt);
        element.jSetPinIO(0, element.PIN_OUTPUT);

        element.jRepaint();
    }


    public void start() {

    }

    public void process() {

        if (store.getValue()) {

            if (in instanceof VS2DString) {
                memory.copyValueFrom(in);
            }
            if (in instanceof VS2DBoolean) {
                memory.copyValueFrom(in);
            }
            if (in instanceof VS2DInteger) {
                memory.copyValueFrom(in);
            }
            if (in instanceof VS2DDouble) {
                memory.copyValueFrom(in);
            }


        }

        if (read.getValue()) {
            if (in instanceof VS2DString) {
                out.copyValueFrom(memory);
            }
            if (in instanceof VS2DBoolean) {
                out.copyValueFrom(memory);
            }
            if (in instanceof VS2DInteger) {
                out.copyValueFrom(memory);
            }
            if (in instanceof VS2DDouble) {
                out.copyValueFrom(memory);
            }
            element.notifyPin(0);
        }


    }

}
