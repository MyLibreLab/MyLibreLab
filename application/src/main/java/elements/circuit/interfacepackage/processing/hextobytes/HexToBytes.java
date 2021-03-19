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

package elements.circuit.interfacepackage.processing.hextobytes;// *****************************************************************************

import java.awt.Image;

import elements.tools.JVSMain;

import VisualLogic.ElementActionEvent;
import VisualLogic.ExternalIF;
import VisualLogic.variables.VS1DByte;
import VisualLogic.variables.VSString;

public class HexToBytes extends JVSMain {
    private Image image;

    private VSString inString;

    private final VS1DByte outBytes = new VS1DByte(0);


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
        setSize(32 + 20, 32 + 5);

        initPinVisibility(false, true, false, true);

        element.jSetInnerBorderVisibility(true);


        setPin(0, ExternalIF.C_ARRAY1D_BYTE, element.PIN_OUTPUT);
        setPin(1, ExternalIF.C_STRING, element.PIN_INPUT);

        element.jSetPinDescription(0, "Bytes-Out");
        element.jSetPinDescription(1, "HEX String");


        String fileName = element.jGetSourcePath() + "icon.gif";
        image = element.jLoadImage(fileName);

        element.jSetCaptionVisible(false);
        setName("HEXToBytes");

    }


    public void initInputPins() {
        inString = (VSString) element.getPinInputReference(1);
    }

    public void initOutputPins() {
        element.setPinOutputReference(0, outBytes);
    }


    public void start() {

    }

    public void stop() {}

    public static byte toUnsigned(final short value) {
        return (byte) (0xFF & value);
    }

    public void sendString(String str) {
        // String partString = "01 06 07 29 00 01 98 B6";
        String partString = str;
        String[] strings = partString.split(" ");

        byte[] dest = new byte[strings.length];

        for (int i = 0; i < strings.length; i++) {
            String strX = strings[i];
            dest[i] = toUnsigned((short) Integer.parseInt(strX, 16));
        }

        outBytes.setValues(dest);

        element.notifyPin(0);
    }


    public void elementActionPerformed(ElementActionEvent evt) {

        int idx = evt.getSourcePinIndex();
        switch (idx) {
            case 1:
                sendString(inString.getValue());
                break;
        }

    }


}
