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

package com.github.mylibrelab.elements.circuit.interfacepackage.compulab.usb;/*
                                                                              * Copyright (C) 2020 MyLibreLab
                                                                              * Based on MyOpenLab by Carmelo Salafia
                                                                              * www.myopenlab.de
                                                                              * Copyright (C) 2004 Carmelo Salafia
                                                                              * cswi@gmx.de
                                                                              *
                                                                              * This program is free software: you can
                                                                              * redistribute it and/or modify
                                                                              * it under the terms of the GNU General
                                                                              * Public License as published by
                                                                              * the Free Software Foundation, either
                                                                              * version 3 of the License, or
                                                                              * (at your option) any later version.
                                                                              *
                                                                              * This program is distributed in the hope
                                                                              * that it will be useful,
                                                                              * but WITHOUT ANY WARRANTY; without even
                                                                              * the implied warranty of
                                                                              * MERCHANTABILITY or FITNESS FOR A
                                                                              * PARTICULAR PURPOSE. See the
                                                                              * GNU General Public License for more
                                                                              * details.
                                                                              *
                                                                              * You should have received a copy of the
                                                                              * GNU General Public License
                                                                              * along with this program. If not, see
                                                                              * <http://www.gnu.org/licenses/>.
                                                                              *
                                                                              */

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import javax.swing.*;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ElementActionEvent;
import VisualLogic.ExternalIF;
import VisualLogic.MyOpenLabDriverIF;
import VisualLogic.MyOpenLabDriverOwnerIF;
import VisualLogic.variables.VSBoolean;
import VisualLogic.variables.VSInteger;

public class CompuLABUSB extends JVSMain implements MyOpenLabDriverOwnerIF {
    private boolean isOpen = false;

    private boolean oldInp1, oldInp2, oldInp3, oldInp4, oldInp5;
    public boolean xStop = false;
    private final Boolean a0 = false;
    private final Boolean a1 = false;
    private final Boolean a2 = false;
    private final Boolean a3 = false;
    private final Boolean a4 = false;
    private final Boolean a5 = false;
    private final Boolean a6 = false;
    private final boolean inp1 = false;
    private final boolean inp2 = false;
    private final boolean inp3 = false;
    private final boolean inp4 = false;
    private final boolean inp5 = false;
    private final boolean inp6 = false;
    private final boolean inp7 = false;
    private final boolean inp8 = false;

    private final boolean running = false;

    private VSBoolean inOut1;
    private VSBoolean inOut2;
    private VSBoolean inOut3;
    private VSBoolean inOut4;
    private VSBoolean inOut5;
    private VSBoolean inOut6;
    private VSBoolean inOut7;
    private VSBoolean inOut8;

    private final VSBoolean SK5 = new VSBoolean(true);
    private final VSBoolean SK6 = new VSBoolean(true);


    private VSInteger inAC1;
    private VSInteger inAC2;

    private final VSBoolean outInp1 = new VSBoolean();
    private final VSBoolean outInp2 = new VSBoolean();
    private final VSBoolean outInp3 = new VSBoolean();
    private final VSBoolean outInp4 = new VSBoolean();
    private final VSBoolean outInp5 = new VSBoolean();
    private final VSBoolean outInp6 = new VSBoolean();
    private final VSBoolean outInp7 = new VSBoolean();
    private final VSBoolean outInp8 = new VSBoolean();


    private final VSInteger outA1 = new VSInteger(0);
    private final VSInteger outA2 = new VSInteger(0);


    private Image image;
    private MyOpenLabDriverIF driver;

    private final int test = 0;

    public void getCommand(String commando, Object value) {

        if (value instanceof Boolean) {
            Boolean val = (Boolean) value;

            if (commando.equals("inp1")) {
                outInp1.setValue(val.booleanValue());
                element.notifyPin(0);
            } else if (commando.equals("inp2")) {
                outInp2.setValue(val.booleanValue());
                element.notifyPin(1);
            } else if (commando.equals("inp3")) {
                outInp3.setValue(val.booleanValue());
                element.notifyPin(2);
            } else if (commando.equals("inp4")) {
                outInp4.setValue(val.booleanValue());
                element.notifyPin(3);
            } else if (commando.equals("inp5")) {
                outInp5.setValue(val.booleanValue());
                element.notifyPin(4);
            } else if (commando.equals("inp6")) {
                outInp6.setValue(val.booleanValue());
                element.notifyPin(5);
            } else if (commando.equals("inp7")) {
                outInp7.setValue(val.booleanValue());
                element.notifyPin(6);
            } else if (commando.equals("inp8")) {
                outInp8.setValue(val.booleanValue());
                element.notifyPin(7);
            }
        } else if (value instanceof Integer) {
            Integer val = (Integer) value;

            if (commando.equals("DAC1")) {
                outA1.setValue(val.intValue());
                element.notifyPin(8);
            } else if (commando.equals("DAC2")) {
                outA2.setValue(val.intValue());
                element.notifyPin(9);
            }
        }

    }



    public void onDispose() {
        image.flush();
        image = null;
    }

    public void setPropertyEditor() {
        /*
         * element.jAddPEItem("SK5",SK5, 0,0);
         * element.jAddPEItem("SK6",SK6, 0,0);
         * element.jAddPEItem("Counter 1 Debounce Time [ms]",counterBouncingTime1, 0,5000);
         * element.jAddPEItem("Counter 2 Debounce Time [ms]",counterBouncingTime2, 0,5000);
         */
    }


    public void propertyChanged(Object o) {}


    public void paint(java.awt.Graphics g) {
        drawImageCentred(g, image);
    }

    public boolean dllsInstalled() {
        String winDir = System.getenv("WINDIR");

        File f2 = new File(winDir + "\\system32\\CLUSB.dll");

        return f2.exists();
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

    public void copyDlls() {
        String dll1 = element.jGetSourcePath() + "CLUSB.dll";

        String winDir = System.getenv("WINDIR");

        File src1 = new File(dll1);

        File dest1 = new File(winDir + "\\system32\\CLUSB.dll");

        try {
            copyFile(src1, dest1);
        } catch (IOException ex) {
            showMessage(ex.toString());
        }
    }

    public void init() {
        initPins(0, 10, 0, 8);
        setSize(80, 120 + 2 * 10);

        image = element.jLoadImage(element.jGetSourcePath() + "image.png");

        element.jSetLeftPinsVisible(true);
        element.jSetRightPinsVisible(true);

        int c = 0;

        setPin(c++, ExternalIF.C_BOOLEAN, element.PIN_OUTPUT);
        setPin(c++, ExternalIF.C_BOOLEAN, element.PIN_OUTPUT);
        setPin(c++, ExternalIF.C_BOOLEAN, element.PIN_OUTPUT);
        setPin(c++, ExternalIF.C_BOOLEAN, element.PIN_OUTPUT);
        setPin(c++, ExternalIF.C_BOOLEAN, element.PIN_OUTPUT);
        setPin(c++, ExternalIF.C_BOOLEAN, element.PIN_OUTPUT);
        setPin(c++, ExternalIF.C_BOOLEAN, element.PIN_OUTPUT);
        setPin(c++, ExternalIF.C_BOOLEAN, element.PIN_OUTPUT);

        setPin(c++, ExternalIF.C_INTEGER, element.PIN_OUTPUT);
        setPin(c++, ExternalIF.C_INTEGER, element.PIN_OUTPUT);


        setPin(c++, ExternalIF.C_BOOLEAN, element.PIN_INPUT);
        setPin(c++, ExternalIF.C_BOOLEAN, element.PIN_INPUT);
        setPin(c++, ExternalIF.C_BOOLEAN, element.PIN_INPUT);
        setPin(c++, ExternalIF.C_BOOLEAN, element.PIN_INPUT);
        setPin(c++, ExternalIF.C_BOOLEAN, element.PIN_INPUT);
        setPin(c++, ExternalIF.C_BOOLEAN, element.PIN_INPUT);
        setPin(c++, ExternalIF.C_BOOLEAN, element.PIN_INPUT);
        setPin(c++, ExternalIF.C_BOOLEAN, element.PIN_INPUT);


        c = 0;
        element.jSetPinDescription(c++, "Inp1");
        element.jSetPinDescription(c++, "Inp2");
        element.jSetPinDescription(c++, "Inp3");
        element.jSetPinDescription(c++, "Inp4");
        element.jSetPinDescription(c++, "Inp5");
        element.jSetPinDescription(c++, "Inp6");
        element.jSetPinDescription(c++, "Inp7");
        element.jSetPinDescription(c++, "Inp8");


        element.jSetPinDescription(c++, "Analog Input A");
        element.jSetPinDescription(c++, "Analog Input B");


        element.jSetPinDescription(c++, "Out1");
        element.jSetPinDescription(c++, "Out2");
        element.jSetPinDescription(c++, "Out3");
        element.jSetPinDescription(c++, "Out4");
        element.jSetPinDescription(c++, "Out5");
        element.jSetPinDescription(c++, "Out6");
        element.jSetPinDescription(c++, "Out7");
        element.jSetPinDescription(c++, "Out8");


        element.jSetCaptionVisible(false);
        element.jSetCaption("CompuLAB-USB");

        setName("CompuLAB-USB");
    }



    public void initInputPins() {
        int c = 10;
        inOut1 = (VSBoolean) element.getPinInputReference(c++);
        inOut2 = (VSBoolean) element.getPinInputReference(c++);
        inOut3 = (VSBoolean) element.getPinInputReference(c++);
        inOut4 = (VSBoolean) element.getPinInputReference(c++);
        inOut5 = (VSBoolean) element.getPinInputReference(c++);
        inOut6 = (VSBoolean) element.getPinInputReference(c++);
        inOut7 = (VSBoolean) element.getPinInputReference(c++);
        inOut8 = (VSBoolean) element.getPinInputReference(c++);

        // inAC1=(VSInteger)element.getPinInputReference(c++);
        // inAC2=(VSInteger)element.getPinInputReference(c++);


        if (inOut1 == null) inOut1 = new VSBoolean();
        if (inOut2 == null) inOut2 = new VSBoolean();
        if (inOut3 == null) inOut3 = new VSBoolean();
        if (inOut4 == null) inOut4 = new VSBoolean();
        if (inOut5 == null) inOut5 = new VSBoolean();
        if (inOut6 == null) inOut6 = new VSBoolean();
        if (inOut7 == null) inOut7 = new VSBoolean();
        if (inOut8 == null) inOut8 = new VSBoolean();

        // if (inAC1==null) inAC1= new VSInteger();
        // if (inAC2==null) inAC2= new VSInteger();

    }

    public void initOutputPins() {
        int c = 0;

        element.setPinOutputReference(c++, outInp1);
        element.setPinOutputReference(c++, outInp2);
        element.setPinOutputReference(c++, outInp3);
        element.setPinOutputReference(c++, outInp4);
        element.setPinOutputReference(c++, outInp5);
        element.setPinOutputReference(c++, outInp6);
        element.setPinOutputReference(c++, outInp7);
        element.setPinOutputReference(c++, outInp8);

        element.setPinOutputReference(c++, outA1);
        element.setPinOutputReference(c++, outA2);


    }

    /*
     * private int getAdresse()
     * {
     * int sk5=0;
     * int sk6=0;
     * if (SK5.getValue()==true) sk5=1 ; else sk5=0;
     * if (SK6.getValue()==true) sk6=1 ; else sk6=0;
     * return 3-(sk5+sk6*2);
     * }
     */

    public static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Info!", JOptionPane.INFORMATION_MESSAGE);
    }

    public void start() {

        if (dllsInstalled()) {
            isOpen = false;

            ArrayList args = new ArrayList();


            /*
             * args.add(new Integer(getAdresse()));
             * args.add(new Integer(counterBouncingTime1.getValue()));
             * args.add(new Integer(counterBouncingTime2.getValue()));
             */

            driver = element.jOpenDriver("modusbus.CompuLab", args);
            driver.registerOwner(this);

            if (driver != null) {
                isOpen = true;
            }

            if (isOpen) {
            }

        } else {
            showMessage(
                    "Dll's driver for communication are not in Windows\\System32 directory. \nI will copy now the Dll.\nPlease restart the VM.");
            copyDlls();
        }

    }

    public void stop() {
        if (isOpen) {
            try {
                driver.sendCommand("ClearAllDigital", null);
                element.jCloseDriver("modusbus.CompuLab");
            } catch (Exception ex) {
                System.out.println(ex);
                isOpen = false;
            }
        }
    }


    public void elementActionPerformed(ElementActionEvent evt) {

        int idx = evt.getSourcePinIndex();

        switch (idx) {
            case 10:
                driver.sendCommand("out1", new Boolean(inOut1.getValue()));
                break; // Out1
            case 11:
                driver.sendCommand("out2", new Boolean(inOut2.getValue()));
                break; // Out2
            case 12:
                driver.sendCommand("out3", new Boolean(inOut3.getValue()));
                break; // Out3
            case 13:
                driver.sendCommand("out4", new Boolean(inOut4.getValue()));
                break; // Out4
            case 14:
                driver.sendCommand("out5", new Boolean(inOut5.getValue()));
                break; // Out5
            case 15:
                driver.sendCommand("out6", new Boolean(inOut6.getValue()));
                break; // Out6
            case 16:
                driver.sendCommand("out7", new Boolean(inOut7.getValue()));
                break; // Out7
            case 17:
                driver.sendCommand("out8", new Boolean(inOut8.getValue()));
                break; // Out8
        }
    }

    public void loadFromStream(java.io.FileInputStream fis) {
        /*
         * VSInteger adresse = new VSInteger();
         * adresse.loadFromStream(fis);
         *
         * switch(adresse.getValue())
         * {
         * case 0 : SK5.setValue(true); SK6.setValue(true); break;
         * case 1 : SK5.setValue(false); SK6.setValue(true); break;
         * case 2 : SK5.setValue(true); SK6.setValue(false); break;
         * case 3 : SK5.setValue(false); SK6.setValue(false); break;
         * }
         *
         * counterBouncingTime1.loadFromStream(fis);
         * counterBouncingTime2.loadFromStream(fis);
         */
    }

    public void saveToStream(java.io.FileOutputStream fos) {
        /*
         * VSInteger adresse = new VSInteger();
         * adresse.setValue(getAdresse());
         * adresse.saveToStream(fos);
         *
         * counterBouncingTime1.saveToStream(fos);
         * counterBouncingTime2.saveToStream(fos);
         */
    }

}
