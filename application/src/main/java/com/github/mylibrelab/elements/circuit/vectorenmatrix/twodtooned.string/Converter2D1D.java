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

package com.github.mylibrelab.elements.circuit.vectorenmatrix.twodtooned.string;/*
                                                                                 * Copyright (C) 2020 MyLibreLab
                                                                                 * Based on MyOpenLab by Carmelo Salafia
                                                                                 * www.myopenlab.de
                                                                                 * Copyright (C) 2004 Carmelo Salafia
                                                                                 * cswi@gmx.de
                                                                                 *
                                                                                 * This program is free software: you
                                                                                 * can redistribute it and/or modify
                                                                                 * it under the terms of the GNU General
                                                                                 * Public License as published by
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

import java.awt.*;
import java.util.Locale;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VS1DString;
import VisualLogic.variables.VS2DString;

public class Converter2D1D extends JVSMain {
    private Image image;
    private VS2DString in = null;
    private final VS1DString out = new VS1DString(0);

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
        initPins(0, 1, 0, 1);
        setSize(20 + 32 + 3, 36);
        initPinVisibility(false, true, false, true);
        element.jSetInnerBorderVisibility(true);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");

        setPin(0, ExternalIF.C_ARRAY1D_STRING, element.PIN_OUTPUT); // Out
        setPin(1, ExternalIF.C_ARRAY2D_STRING, element.PIN_INPUT); // in


        String strLocale = Locale.getDefault().toString();

        element.jSetPinDescription(0, "out");
        element.jSetPinDescription(1, "in");

        element.jSetCaptionVisible(false);
        element.jSetCaption("Converter2D1D_STRING");
        setName("Converter2D1D_STRING");
    }


    public void initInputPins() {
        in = (VS2DString) element.getPinInputReference(1);

        if (in == null) in = new VS2DString(0, 0);
    }


    public void initOutputPins() {
        element.setPinOutputReference(0, out);
    }


    public void start() {

    }

    private void copy() {

        int col = 0;
        String[] tmp = new String[in.getRows()];

        for (int i = 0; i < in.getRows(); i++) {
            tmp[i] = in.getValue(col, i);
        }

        out.setValues(tmp);
    }

    public void process() {
        copy();
        element.notifyPin(0);
    }

}
