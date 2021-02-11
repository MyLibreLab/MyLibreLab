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

package com.github.mylibrelab.elements.circuit.vectorenmatrix.twodbooleanpackagetotwodstring;/*
                                                                                              * Copyright (C) 2020
                                                                                              * MyLibreLab
                                                                                              * Based on MyOpenLab by
                                                                                              * Carmelo
                                                                                              * Salafia www.myopenlab.de
                                                                                              * Copyright (C) 2004
                                                                                              * Carmelo
                                                                                              * Salafia cswi@gmx.de
                                                                                              *
                                                                                              * This program is free
                                                                                              * software:
                                                                                              * you can redistribute it
                                                                                              * and/or
                                                                                              * modify
                                                                                              * it under the terms of
                                                                                              * the
                                                                                              * GNU
                                                                                              * General Public License
                                                                                              * as
                                                                                              * published by
                                                                                              * the Free Software
                                                                                              * Foundation,
                                                                                              * either version 3 of the
                                                                                              * License,
                                                                                              * or
                                                                                              * (at your option) any
                                                                                              * later
                                                                                              * version.
                                                                                              *
                                                                                              * This program is
                                                                                              * distributed in
                                                                                              * the hope that it will be
                                                                                              * useful,
                                                                                              * but WITHOUT ANY
                                                                                              * WARRANTY;
                                                                                              * without
                                                                                              * even the implied
                                                                                              * warranty
                                                                                              * of
                                                                                              * MERCHANTABILITY or
                                                                                              * FITNESS
                                                                                              * FOR A
                                                                                              * PARTICULAR PURPOSE. See
                                                                                              * the
                                                                                              * GNU General Public
                                                                                              * License
                                                                                              * for
                                                                                              * more details.
                                                                                              *
                                                                                              * You should have received
                                                                                              * a
                                                                                              * copy
                                                                                              * of the GNU General
                                                                                              * Public
                                                                                              * License
                                                                                              * along with this program.
                                                                                              * If not,
                                                                                              * see
                                                                                              * <http://www.gnu.org/
                                                                                              * licenses/>.
                                                                                              *
                                                                                              */

import java.awt.Image;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VS2DBoolean;
import VisualLogic.variables.VS2DString;

public class Element extends JVSMain {
    private Image image;

    private VS2DBoolean in;
    private final VS2DString out = new VS2DString(1, 1);

    public void xpaint(java.awt.Graphics g) {
        if (image != null) drawImageCentred(g, image);
    }

    public void onDispose() {
        if (image != null) {
            image.flush();
            image = null;
        }
    }

    public void init() {
        initPins(0, 1, 0, 1);
        setSize(32 + 20, 32 + 4);
        initPinVisibility(false, true, false, true);
        element.jSetInnerBorderVisibility(true);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");

        setPin(0, ExternalIF.C_ARRAY2D_STRING, element.PIN_OUTPUT); // OUT
        setPin(1, ExternalIF.C_ARRAY2D_BOOLEAN, element.PIN_INPUT); // IN

        setName("2DBooleanTo2DString");
    }

    public void initInputPins() {
        in = (VS2DBoolean) element.getPinInputReference(1);
        if (in == null) in = new VS2DBoolean(0, 0);
    }

    public void initOutputPins() {
        element.setPinOutputReference(0, out);
    }


    public void start() {}


    private void convert_2DBoolean_to_2DString(VS2DBoolean in) {

        VS2DString temp = new VS2DString(in.getColumns(), in.getRows());

        out.copyValueFrom(temp);

        for (int i = 0; i < in.getRows(); i++) {
            for (int j = 0; j < in.getColumns(); j++) {
                out.setValue(j, i, "" + in.getValue(j, i));
            }
        }
    }



    public void process() {

        if (in instanceof VS2DBoolean) {
            convert_2DBoolean_to_2DString(in);
            System.out.println("Converter : Zeilen/Spalten=" + out.getColumns() + "," + out.getRows());
            element.notifyPin(0);
        }

    }


}
