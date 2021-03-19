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

package elements.circuit.numerik.abs;// *****************************************************************************

import java.awt.Image;

import elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSByte;
import VisualLogic.variables.VSDouble;
import VisualLogic.variables.VSInteger;
import VisualLogic.variables.VSObject;

public class Abs extends JVSMain {
    private Image image;

    private VSObject in;
    private VSObject out;

    public void xpaint(java.awt.Graphics g) {
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
        setSize(40, 25);

        element.jSetInnerBorderVisibility(false);
        element.jSetTopPinsVisible(false);
        element.jSetBottomPinsVisible(false);
        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");

        setPin(0, ExternalIF.C_VARIANT, element.PIN_OUTPUT);
        setPin(1, ExternalIF.C_VARIANT, element.PIN_INPUT);

        element.jSetPinDescription(0, "out");
        element.jSetPinDescription(1, "in");

        setName("Abs");
    }


    public void initInputPins() {
        in = (VSObject) element.getPinInputReference(1);
    }

    public void initOutputPins() {
        out = element.jCreatePinDataType(0);
        element.setPinOutputReference(0, out);
    }

    public void checkPinDataType() {
        boolean pinIn = element.hasPinWire(1);

        int dt = element.C_VARIANT;

        if (pinIn == true) {
            dt = element.jGetPinDrahtSourceDataType(1);

            if (dt == element.C_VARIANT)
                dt = element.C_VARIANT;
            else if (dt == element.C_DOUBLE)
                dt = element.C_DOUBLE;
            else if (dt == element.C_INTEGER)
                dt = element.C_INTEGER;
            else if (dt == element.C_BYTE)
                dt = element.C_BYTE;
            else
                dt = element.C_DOUBLE;
        } else
            dt = element.C_VARIANT;

        element.jSetPinDataType(0, dt);
        element.jSetPinDataType(1, dt);

        element.jRepaint();
    }

    double value = 0;

    public void process() {
        if (in == null) value = 0;

        if (in instanceof VSDouble) {
            VSDouble valA = (VSDouble) in;
            value = valA.getValue();
        } else if (in instanceof VSInteger) {
            VSInteger valA = (VSInteger) in;
            value = valA.getValue();
        } else if (in instanceof VSByte) {
            VSByte valA = (VSByte) in;
            value = VSByte.toSigned(valA.getValue());
        }

        value = Math.abs(value);


        if (out instanceof VSDouble) {
            VSDouble outX = (VSDouble) out;
            outX.setValue(value);
        } else if (out instanceof VSInteger) {
            VSInteger outX = (VSInteger) out;
            outX.setValue((int) (value));
        } else if (out instanceof VSByte) {
            VSByte outX = (VSByte) out;
            outX.setValue(VSByte.toUnsigned((short) (value)));
        }

        element.notifyPin(0);
    }



}
