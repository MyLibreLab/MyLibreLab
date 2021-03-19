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

package elements.circuit.interfacepackage.rstwothreetworuntimeconfig;// *****************************************************************************

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import elements.tools.JVSMain;

import VisualLogic.ElementActionEvent;
import VisualLogic.ExternalIF;
import VisualLogic.MyOpenLabDriverIF;
import VisualLogic.MyOpenLabDriverOwnerIF;
import VisualLogic.variables.VS1DByte;
import VisualLogic.variables.VSBoolean;
import VisualLogic.variables.VSInteger;

public class RS232 extends JVSMain implements MyOpenLabDriverOwnerIF {
    private Image image;

    private MyOpenLabDriverIF driver;

    private VS1DByte inBytes;
    private VSBoolean inSend;

    private final VS1DByte outBytes = new VS1DByte(0);
    private final VSBoolean outReceived = new VSBoolean(false);

    private VSInteger port;
    private VSInteger parity;
    private VSInteger baud;
    private VSInteger bits;
    private VSInteger stopBits;


    public void paint(java.awt.Graphics g) {
        drawImageCentred(g, image);
    }

    public void onDispose() {
        if (image != null) {
            image.flush();
            image = null;
        }
    }

    public void copyFile(File source, File dest) throws IOException {
        FileChannel in = null, out = null;
        try {
            in = new FileInputStream(source).getChannel();
            out = new FileOutputStream(dest).getChannel();

            long size = in.size();
            MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0, size);

            out.write(buf);

        } finally {
            if (in != null) in.close();
            if (out != null) out.close();
        }
    }

    @Override
    public void getSingleByte(int val) {

    }

    public void setPropertyEditor() {


    }


    public void propertyChanged(Object o) {

    }


    public void init() {
        initPins(0, 2, 0, 2 + 5);
        setSize(32 + 22, 32 + 2 + 10 * 5);

        initPinVisibility(false, true, false, true);

        element.jSetInnerBorderVisibility(true);


        setPin(0, ExternalIF.C_ARRAY1D_BYTE, element.PIN_OUTPUT);
        setPin(1, ExternalIF.C_BOOLEAN, element.PIN_OUTPUT);

        setPin(2, ExternalIF.C_ARRAY1D_BYTE, element.PIN_INPUT);
        setPin(3, ExternalIF.C_BOOLEAN, element.PIN_INPUT);

        setPin(4, ExternalIF.C_INTEGER, element.PIN_INPUT);
        setPin(5, ExternalIF.C_INTEGER, element.PIN_INPUT);
        setPin(6, ExternalIF.C_INTEGER, element.PIN_INPUT);
        setPin(7, ExternalIF.C_INTEGER, element.PIN_INPUT);
        setPin(8, ExternalIF.C_INTEGER, element.PIN_INPUT);


        element.jSetPinDescription(0, "Bytes-Out");
        element.jSetPinDescription(1, "Data Received");

        element.jSetPinDescription(2, "Bytes-In");
        element.jSetPinDescription(3, "Send Data");

        element.jSetPinDescription(4, "Port");
        element.jSetPinDescription(5, "Baud");
        element.jSetPinDescription(6, "parity");
        element.jSetPinDescription(7, "Databits");
        element.jSetPinDescription(8, "stopBits");

        String fileName = element.jGetSourcePath() + "icon.gif";
        image = element.jLoadImage(fileName);

        element.jSetCaptionVisible(true);
        element.jSetCaption("RS232");
        setName("RS232");


    }

    public void initInputPins() {
        inBytes = (VS1DByte) element.getPinInputReference(2);
        inSend = (VSBoolean) element.getPinInputReference(3);

        port = (VSInteger) element.getPinInputReference(4);
        baud = (VSInteger) element.getPinInputReference(5);
        parity = (VSInteger) element.getPinInputReference(6);
        bits = (VSInteger) element.getPinInputReference(7);
        stopBits = (VSInteger) element.getPinInputReference(8);

        if (inBytes == null) inBytes = new VS1DByte(0);
        if (inSend == null) inSend = new VSBoolean(false);

        if (port == null) port = new VSInteger(1);
        if (baud == null) baud = new VSInteger(9600);
        if (parity == null) parity = new VSInteger(0);
        if (bits == null) bits = new VSInteger(8);
        if (stopBits == null) stopBits = new VSInteger(2);

    }

    public void initOutputPins() {
        element.setPinOutputReference(0, outBytes);
        element.setPinOutputReference(1, outReceived);
    }

    public boolean started = false;

    public void start() {

    }

    public static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Attention!", JOptionPane.ERROR_MESSAGE);
    }

    public void stop() {
        if (started) {
            started = false;

            String strCom = "COM" + port.getValue();

            driver.sendCommand(strCom + ";CLOSE", null);
            element.jCloseDriver("MyOpenLab.RS232");

            System.out.println("RS232C          CLOSED = " + strCom);
        }
    }

    public void elementActionPerformed(ElementActionEvent evt) {

        int idx = evt.getSourcePinIndex();
        switch (idx) {
            case 3: {
                if (inSend.getValue()) {
                    if (started == false) {
                        ArrayList args = new ArrayList();

                        args.add(new String("COM" + port.getValue()));
                        args.add(new Integer(baud.getValue()));
                        args.add(new Integer(bits.getValue()));
                        args.add(new Integer(stopBits.getValue()));
                        args.add(new Integer(parity.getValue()));

                        driver = element.jOpenDriver("MyOpenLab.RS232", args);
                        driver.registerOwner(this);
                        try {
                            Thread.sleep(20);
                        } catch (Exception ex) {

                        }
                        started = true;
                    }

                    driver.sendCommand("COM" + port.getValue() + ";SENDBYTES", inBytes);
                }
            }
                break;
        }

    }


    boolean changed = false;

    public void destElementCalled() {
        if (changed) {
            outReceived.setValue(false);
            element.notifyPin(1);
            changed = false;
        }
    }

    public void getCommand(String commando, Object value) {
        if (value instanceof VS1DByte) {
            VS1DByte values = (VS1DByte) value;
            System.out.println("COMMAND : " + commando);

            if (commando.equals("BYTESRECEIVED")) {

                int len = values.getLength();

                byte[] dest = values.getValues();
                outBytes.setValues(dest);
                outReceived.setValue(true);


                element.notifyPin(0);
                element.notifyPin(1);

                changed = true;
                element.jNotifyWhenDestCalled(1, element);

            }

        }

    }

    public void loadFromStream(java.io.FileInputStream fis) {

    }

    public void saveToStream(java.io.FileOutputStream fos) {

    }

}
