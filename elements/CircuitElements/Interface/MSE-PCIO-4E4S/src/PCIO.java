//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2016  Carmelo Salafia (cswi@gmx.de)                         *
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

import java.awt.*;

import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import tools.*;

public class PCIO extends JVSMain implements MyOpenLabDriverOwnerIF {

    private VSInteger pollTime = new VSInteger(350);
    private Image image;
    private VS1DByte inBytes = new VS1DByte(1);
    private ArrayList<String> cmd = new ArrayList<>();

    private MyOpenLabDriverIF driver;
    private MyTimer timer;

    private VSBoolean e0 = new VSBoolean();
    private VSBoolean e1 = new VSBoolean();
    private VSBoolean e2 = new VSBoolean();
    private VSBoolean e3 = new VSBoolean();

    private VSInteger a0 = new VSInteger();
    private VSInteger a1 = new VSInteger();
    private VSInteger a2 = new VSInteger();
    private VSInteger a3 = new VSInteger();

    private VSBoolean s0;
    private VSBoolean s1;
    private VSBoolean s2;
    private VSBoolean s3;

    private int old_e0 = -1;
    private int old_e1 = -1;
    private int old_e2 = -1;
    private int old_e3 = -1;

    private int old_a0 = -100;
    private int old_a1 = -100;
    private int old_a2 = -100;
    private int old_a3 = -100;

    VSObject out[];
    int[] outPinNumbers;
    public String[] outPinType;

    private VSComboBox comport = new VSComboBox();

    public PCIO() {

    }

    @Override
    public void paint(java.awt.Graphics g) {
        if (image != null) {
            Rectangle bounds = element.jGetBounds();
            g.setColor(Color.WHITE);
            g.fillRect(10, 1, bounds.width - 4, bounds.height - 2);

            drawImageCentred(g, image);
        }
    }

    @Override
    public void onDispose() {
        if (image != null) {
            image.flush();
            image = null;
        }
    }

    @Override
    public void setPropertyEditor() {

        String poll = "";
        String com = "";

        String strLocale = Locale.getDefault().toString();

        if (strLocale.equalsIgnoreCase("de_DE")) {
            poll = "Poll Zeit[ms]";
            com = "COM Port";
        }
        if (strLocale.equalsIgnoreCase("en_US")) {
            poll = "Poll Time[ms]";
            com = "COM Port";
        }
        if (strLocale.equalsIgnoreCase("es_ES")) {
            poll = "Tiempo de muestreo[ms]";
            com = "Puerto COM";
        }

        element.jAddPEItem(poll, pollTime, 10, 50000);
        element.jAddPEItem(com, comport, 0, 0);

    }

    @Override
    public void propertyChanged(Object o) {

    }

    @Override
    public void init() {

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
            try {
                ArrayList<String> ports = new ArrayList();
                driver.sendCommand("NULL;GETPORTS", ports);

                System.out.println("size of Ports : " + ports.size());
                for (String port : ports) {
                    System.out.println("-X-------> Port : " + port);
                    comport.addItem(port);
                }

                driver.sendCommand("NULL;CLOSE", null);
                element.jCloseDriver("MyOpenLab.RS232");

            } catch (Exception ex) {
                System.out.println("DRIVER IS NULL!!!");
            }
        } else {
            System.out.println("DRIVER IS NULL!!!");
        }

        initPins(0, 8, 0, 4);
        setSize(90, 128);

        initPinVisibility(false, true, false, true);

        int c = 0;
        setPin(c++, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT); // E0
        setPin(c++, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT); // E1
        setPin(c++, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT); // E2
        setPin(c++, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT); // E3

        setPin(c++, ExternalIF.C_INTEGER, ExternalIF.PIN_OUTPUT);  // A0
        setPin(c++, ExternalIF.C_INTEGER, ExternalIF.PIN_OUTPUT);  // A1
        setPin(c++, ExternalIF.C_INTEGER, ExternalIF.PIN_OUTPUT);  // A3
        setPin(c++, ExternalIF.C_INTEGER, ExternalIF.PIN_OUTPUT);  // A4

        setPin(c++, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);  // S0
        setPin(c++, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);  // S1
        setPin(c++, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);  // S3
        setPin(c++, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);  // S4

        c = 0;
        element.jSetPinDescription(c++, "E0");
        element.jSetPinDescription(c++, "E1");
        element.jSetPinDescription(c++, "E2");
        element.jSetPinDescription(c++, "E3");

        element.jSetPinDescription(c++, "A0");
        element.jSetPinDescription(c++, "A1");
        element.jSetPinDescription(c++, "A2");
        element.jSetPinDescription(c++, "A3");

        element.jSetPinDescription(c++, "S0");
        element.jSetPinDescription(c++, "S1");
        element.jSetPinDescription(c++, "S2");
        element.jSetPinDescription(c++, "S3");

        element.jSetInnerBorderVisibility(true);

        String fileName = element.jGetSourcePath() + "icon_64.png";
        image = element.jLoadImage(fileName);

        element.jSetCaptionVisible(true);
        element.jSetCaption("MSE-PCIO-4E4S");
        setName("MSE-PCIO-4E4S");

    }

    @Override
    public void initInputPins() {

        s0 = (VSBoolean) element.getPinInputReference(8);
        s1 = (VSBoolean) element.getPinInputReference(9);
        s2 = (VSBoolean) element.getPinInputReference(10);
        s3 = (VSBoolean) element.getPinInputReference(11);

    }

    @Override
    public void initOutputPins() {

        element.setPinOutputReference(0, e0);
        element.setPinOutputReference(1, e1);
        element.setPinOutputReference(2, e2);
        element.setPinOutputReference(3, e3);

        element.setPinOutputReference(4, a0);
        element.setPinOutputReference(5, a1);
        element.setPinOutputReference(6, a2);
        element.setPinOutputReference(7, a3);

    }

    public boolean started = false;

    @Override
    public void start() {

        old_e0 = -1;
        old_e1 = -1;
        old_e2 = -1;
        old_e3 = -1;

        old_a0 = -100;
        old_a1 = -100;
        old_a2 = -100;
        old_a3 = -100;

        try {
            ArrayList<Object> args = new ArrayList<>();

            args.add(comport.getItem(comport.selectedIndex));
            args.add(15000);
            args.add(8);
            args.add(1);
            args.add(0);
            args.add(true); // No Parity

            driver = element.jOpenDriver("MyOpenLab.RS232", args);
            driver.registerOwner(this);
            started = true;

            Thread.sleep(100);

            timer = new MyTimer();
            timer.delay = pollTime.getValue();
            timer.owner = this;
            timer.start();
        } catch (InterruptedException ex) {
            Logger.getLogger(PCIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Attention!", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void stop() {
        if (started) {
            started = false;

            timer.stop = true;
            //String strCom = "COM" + comPort.getValue();
            String strCom = comport.getItem(comport.selectedIndex);

            driver.sendCommand(strCom + ";CLOSE", null);
            element.jCloseDriver("MyOpenLab.RS232");
        }
    }

    public void send(byte[] bytes) {

        inBytes.setBytes(bytes);

        driver.sendCommand(comport.getItem(comport.selectedIndex) + ";SENDBYTES", inBytes);
    }

    @Override
    public void elementActionPerformed(ElementActionEvent evt) {
        int idx = evt.getSourcePinIndex();
        //System.out.println("idx=" + idx);

        switch (idx) {
            case 8: {
                // S0

                if (s0.getValue()) {
                    send(new byte[]{'S', '0'});
                } else {
                    send(new byte[]{'C', '0'});
                }

                break;
            }

            case 9: {
                // S1

                if (s1.getValue()) {
                    send(new byte[]{'S', '1'});
                } else {
                    send(new byte[]{'C', '1'});
                }

                break;
            }
            case 10: {
                // S2

                if (s2.getValue()) {
                    send(new byte[]{'S', '2'});
                } else {
                    send(new byte[]{'C', '2'});
                }

                break;
            }
            case 11: {
                // S3

                if (s3.getValue()) {
                    send(new byte[]{'S', '3'});
                } else {
                    send(new byte[]{'C', '3'});
                }

                break;
            }

        }
    }

    boolean changed = false;

    @Override
    public void destElementCalled() {
        if (changed) {
            //outReceived.setValue(false);
            element.notifyPin(1);
            changed = false;
        }
    }

    @Override
    public void getCommand(String commando, Object value) {

        if (value instanceof VS1DByte) {
            VS1DByte values = (VS1DByte) value;

            if (commando.equals("BYTESRECEIVED")) {

            }
        }

    }

    @Override
    public void process() {

    }

    @Override
    public void loadFromStream(java.io.FileInputStream fis) {

        comport.loadFromStream(fis);
        pollTime.loadFromStream(fis);

        init();
    }

    @Override
    public void saveToStream(java.io.FileOutputStream fos) {

        comport.saveToStream(fos);
        pollTime.saveToStream(fos);
    }

    private int getEPin(String value) {
        int val = 0;
        if (value.equalsIgnoreCase("1")) {
            val = 1;
        }
        return val;
    }

    private void verarbeiteCMD() {

        /*for (String val : cmd) {
            System.out.print(val);
        }
        System.out.println("");*/
        if (cmd.get(0).equalsIgnoreCase("R") && cmd.get(1).equalsIgnoreCase("I") && cmd.get(2).equalsIgnoreCase("A")) {

            String value0 = cmd.get(8);
            String value1 = cmd.get(7);
            String value2 = cmd.get(6);
            String value3 = cmd.get(5);

            //  System.out.println("values=" + value0 + ", " + value1 + ", " + value2 + ", " + value3);
            int e0_val = getEPin(value0);
            int e1_val = getEPin(value1);
            int e2_val = getEPin(value2);
            int e3_val = getEPin(value3);

            if (e0_val != old_e0) {

                if (e0_val == 1) {
                    e0.setValue(true);
                } else {
                    e0.setValue(false);
                }

                element.notifyPin(0);
                old_e0 = e0_val;
            }

            if (e1_val != old_e1) {
                if (e1_val == 1) {
                    e1.setValue(true);
                } else {
                    e1.setValue(false);
                }

                element.notifyPin(1);
                old_e1 = e1_val;
            }

            if (e2_val != old_e2) {
                if (e2_val == 1) {
                    e2.setValue(true);
                } else {
                    e2.setValue(false);
                }
                element.notifyPin(2);
                old_e2 = e2_val;
            }

            if (e3_val != old_e3) {
                if (e3_val == 1) {
                    e3.setValue(true);
                } else {
                    e3.setValue(false);
                }
                element.notifyPin(3);
                old_e3 = e3_val;
            }

        }

        if (cmd.get(0).equalsIgnoreCase("A")) {

            int pinNo = Integer.parseInt(cmd.get(1));

            String value0 = cmd.get(4);
            String value1 = cmd.get(5);
            String value2 = cmd.get(6);
            String value3 = cmd.get(7);

            String ad_value = value0 + value1 + value2 + value3;

            int int_value = Integer.parseInt(ad_value.trim());

            //System.out.println("pin=" + pinNo + " value(int)=" + int_value + " value(str)" + ad_value);
            if (pinNo == 0) {

                if (int_value != old_a0) {
                    a0.setValue(int_value);
                    element.notifyPin(4);
                    old_a0 = int_value;
                }
            }
            if (pinNo == 1) {
                if (int_value != old_a1) {
                    a1.setValue(int_value);
                    element.notifyPin(5);
                    old_a1 = int_value;
                }
            }
            if (pinNo == 2) {
                if (int_value != old_a2) {
                    a2.setValue(int_value);
                    element.notifyPin(6);
                    old_a2 = int_value;
                }
            }
            if (pinNo == 3) {
                if (int_value != old_a3) {
                    a3.setValue(int_value);
                    element.notifyPin(7);
                    old_a3 = int_value;
                }
            }

        }

    }

    @Override
    public void getSingleByte(int i) {

        char ch = (char) i;

        if (ch == '>') {
            verarbeiteCMD();
            cmd.clear();

        } else {
            cmd.add("" + ch);
        }

    }
}
