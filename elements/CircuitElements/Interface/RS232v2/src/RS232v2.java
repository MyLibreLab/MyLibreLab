//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia (cswi@gmx.de)                         *
//*                                                                           *
//* This library is free software; you can redistribute it and/or modify      *
//* it under the terms of the GNU Lesser General Public License as published  *
//* by the Free Software Foundation; either version 2.1 of the License,       *
//* or (at your option) any later version.                                    *
//* http://www.gnu.org/licenses/lgpl.html                                     *
//*                                                                           *
//* This library is distributed in the hope that it will be useful,           *
//* but WITHOUTANY WARRANTY; without even the implied warranty of             *
//* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                      *
//* See the GNU Lesser General Public License for more details.               *
//*                                                                           *
//* You should have received a copy of the GNU Lesser General Public License  *
//* along with this library; if not, write to the Free Software Foundation,   *
//* Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA                  *
//*****************************************************************************

import VisualLogic.*;
import VisualLogic.variables.*;
import tools.*;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.nio.channels.*;
import java.nio.*;
import javax.swing.*;

public class RS232v2 extends JVSMain implements MyOpenLabDriverOwnerIF {

    private Image image;

    private MyOpenLabDriverIF driver;

    private VS1DByte inBytes;
    private VSBoolean inSend;

    // Property
    private final VSInteger timeOut = new VSInteger(50);

    private VS1DByte outBytes = new VS1DByte(0);
    private VSBoolean outReceived = new VSBoolean(false);

    private VSComboBox comport = new VSComboBox();
    private VSComboBox baud = new VSComboBox();
    private VSComboBox bits = new VSComboBox();
    private VSComboBox parity = new VSComboBox();
    private VSComboBox stopBits = new VSComboBox();
    public VSInteger message_timeout = new VSInteger();

    private MyTimer timer;

    private ArrayList<Byte> array = new ArrayList<>();

    public RS232v2() {

        message_timeout.setValue(100);
        
        baud.addItem("300");
        baud.addItem("600");
        baud.addItem("1200");
        baud.addItem("2400");
        baud.addItem("4800");
        baud.addItem("9600");
        baud.addItem("14400");
        baud.addItem("19200");
        baud.addItem("28800");
        baud.addItem("31250");
        baud.addItem("38400");
        baud.addItem("57600");
        baud.addItem("115200");

        bits.addItem("5");
        bits.addItem("6");
        bits.addItem("7");
        bits.addItem("8");

        stopBits.addItem("1");
        stopBits.addItem("2");

        parity.addItem("0");
        parity.addItem("1");
        
        baud.selectedIndex = 5;
        parity.selectedIndex=0;        
        bits.selectedIndex = 3;
        stopBits.selectedIndex = 0;
    }

    @Override
    public void paint(java.awt.Graphics g) {
        drawImageCentred(g, image);
    }

    @Override
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
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    @Override
    public void setPropertyEditor() {

        element.jAddPEItem("COM Port", comport, 0, 0);
        element.jAddPEItem("Baud", baud, 1, 999999);
        element.jAddPEItem("DataBits", bits, 5, 8);
        element.jAddPEItem("Partity", parity, 1, 20);
        element.jAddPEItem("stopBits", stopBits, 1, 2);
        element.jAddPEItem("TimeOut[ms]", timeOut, 1, 5000);
        
        element.jAddPEItem("Message Timeout[ms]", message_timeout, 1, 50000);        
        
    }

    @Override
    public void propertyChanged(Object o) {
    }

    @Override

    public void init() {
        initPins(0, 2, 0, 2);
        setSize(32 + 22, 32 + 2);

        ArrayList<Object> args = new ArrayList<>();
        args.add("NULL");
        args.add(0);
        args.add(0);
        args.add(0);
        args.add(0);
        args.add(false);

        driver = element.jOpenDriver("MyOpenLab.RS232", args);

        comport.clear();
        comport.addItem("");

        if (driver != null) {

            ArrayList<String> ports = new ArrayList();
            driver.sendCommand("NULL;GETPORTS", ports);

            System.out.println("size of Ports : " + ports.size());
            for (String port : ports) {
                System.out.println("Port : " + port);
                comport.addItem(port);
            }

            driver.sendCommand("NULL;CLOSE", null);
            element.jCloseDriver("MyOpenLab.RS232");

        } else {
            System.out.println("DRIVER IS NULL!!!");
        }

        initPinVisibility(false, true, false, true);

        element.jSetInnerBorderVisibility(true);

        setPin(0, ExternalIF.C_ARRAY1D_BYTE, ExternalIF.PIN_OUTPUT);
        setPin(1, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);

        setPin(2, ExternalIF.C_ARRAY1D_BYTE, ExternalIF.PIN_INPUT);
        setPin(3, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);

        element.jSetPinDescription(0, "Bytes-Out");
        element.jSetPinDescription(1, "Data Received");

        element.jSetPinDescription(2, "Bytes-In");
        element.jSetPinDescription(3, "Send Data");

        String fileName = element.jGetSourcePath() + "icon.gif";
        image = element.jLoadImage(fileName);

        element.jSetCaptionVisible(true);
        element.jSetCaption("RS232");
        setName("RS232V2");

        /*parity.addItem("PARITY_NONE");
        parity.addItem("PARITY_EVEN");
        parity.addItem("PARITY_MARK");
        parity.addItem("PARITY_ODD");
        parity.addItem("PARITY_SPACE");*/
        parity.selectedIndex = 1;

    }

    @Override
    public void initInputPins() {
        inBytes = (VS1DByte) element.getPinInputReference(2);
        inSend = (VSBoolean) element.getPinInputReference(3);

        if (inBytes == null) {
            inBytes = new VS1DByte(0);
        }
        if (inSend == null) {
            inSend = new VSBoolean(false);
        }
    }

    @Override
    public void initOutputPins() {
        element.setPinOutputReference(0, outBytes);
        element.setPinOutputReference(1, outReceived);
    }

    public boolean started = false;

    @Override
    public void start() {

        started = true;

        ArrayList<Object> args = new ArrayList<>();

        args.add(comport.getItem(comport.selectedIndex));
        args.add(new Integer(baud.getItem(baud.selectedIndex)));
        args.add(new Integer(bits.getItem(bits.selectedIndex)));
        args.add(new Integer(stopBits.getItem(stopBits.selectedIndex)));
        args.add(new Integer(parity.getItem(parity.selectedIndex)));
        args.add(true); // No Parity

        driver = element.jOpenDriver("MyOpenLab.RS232", args);
        driver.registerOwner(this);

        String strCom = comport.getItem(comport.selectedIndex);
        driver.sendCommand(strCom + ";TIMEOUT", timeOut);

        timer = new MyTimer();
        timer.owner = this;        
        timer.start();

    }

    public static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Attention!", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void stop() {
        if (started) {
            started = false;

            timer.stop = true;

            array.clear();

            String strCom = comport.getItem(comport.selectedIndex);

            driver.sendCommand(strCom + ";CLOSE", null);
            element.jCloseDriver("MyOpenLab.RS232");

        }
    }

    @Override
    public void elementActionPerformed(ElementActionEvent evt) {

        int idx = evt.getSourcePinIndex();
        switch (idx) {
            case 3: {
                if (inSend.getValue()) {
                    String strCom = comport.getItem(comport.selectedIndex);

                    driver.sendCommand(strCom + ";SENDBYTES", inBytes);
                }
            }
            break;
        }
    }

    boolean changed = false;

    @Override
    public void destElementCalled() {
        if (changed) {
            outReceived.setValue(false);
            element.notifyPin(1);
            changed = false;
        }
    }

    public void sendBytesNow() {
        byte[] bytes = new byte[array.size()];

        for (int i = 0; i < array.size(); i++) {
            bytes[i] = array.get(i);
        }

        array.clear();
        outBytes.setValues(bytes);
        outReceived.setValue(true);

        element.notifyPin(0);
        element.notifyPin(1);

        changed = true;
        element.jNotifyWhenDestCalled(1, element);
    }

    @Override
    public void getSingleByte(int val) {
        System.out.println("getSingleByte(" + val + ")");

        array.add((byte) val);

        if (timer != null) {
            timer.gesendet = false;
            timer.sendenEnde = System.currentTimeMillis();
        }

    }

    @Override
    public void getCommand(String commando, Object value) {

        /*System.out.println("getCommand(" + commando + ", " + value + ")");

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

        }*/
    }

    @Override
    public void loadFromStream(java.io.FileInputStream fis) {
        comport.loadFromStream(fis);
        baud.loadFromStream(fis);
        bits.loadFromStream(fis);
        stopBits.loadFromStream(fis);
        parity.loadFromStream(fis);
        timeOut.loadFromStream(fis);
        message_timeout.loadFromStream(fis);
    }

    @Override
    public void saveToStream(java.io.FileOutputStream fos) {
        comport.saveToStream(fos);
        baud.saveToStream(fos);
        bits.saveToStream(fos);
        stopBits.saveToStream(fos);
        parity.saveToStream(fos);
        timeOut.saveToStream(fos);
        
        message_timeout.saveToStream(fos);
    }

}
