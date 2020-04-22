//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2014  Carmelo Salafia (cswi@gmx.de)                         *
//* Copyright (C) 2018  Javier Velásquez (javiervelasquez125@gmail.com)       *                                                                         *
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

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import tools.*;

class MyTimer extends Thread {

    public boolean stop = false;
    public Firmata owner;
    public int delay = 200;

    @Override
    public void run() {

        stop = false;
        while (!stop) {
            try {

                //printAnalogInputs();
                processAnalogInputs();
                //printDigitalInputs();                
                processDigitalInputs();

                Thread.sleep(delay);
            } catch (InterruptedException ex) {
            }
        }
    }

    private void processDigitalInputs() {
        for (int i = 0; i < owner.out.length; i++) {

            int pinNumber = owner.outPinNumbers[i];
            String pinType = owner.outPinType[i];

            if (pinType.startsWith("DIGITAL_INPUT") || pinType.startsWith("INPUT_PULLUP")) {
                if (owner.out[i] instanceof VSBoolean) {

                    VSBoolean pin = (VSBoolean) owner.out[i];

                    int portNr = (int) (pinNumber / 8);
                    int port = owner.digitalPort[portNr];

                    int valux = port & (1 << (pinNumber - portNr * 8));

                    if (valux > 0) {
                        pin.setValue(true);
                    } else {
                        pin.setValue(false);
                    }
                    owner.element.notifyPin(i);
                }
            }
        }
    }

    private void processAnalogInputs() {
        for (int i = 0; i < owner.out.length; i++) {

            int pinNo = owner.outPinNumbers[i];
            String pinType = owner.outPinType[i];

            if (pinType.equals("ANALOG_INPUT")) {

                VSInteger pin = (VSInteger) owner.out[i];

                int valuex = owner.oldAnalogValue[pinNo];

                pin.setValue(valuex);
                owner.element.notifyPin(i);
            }
        }
    }

    private void printAnalogInputs() {
        System.out.print("ANALOG IN : ");
        for (int i = 0; i < owner.oldAnalogValue.length; i++) {
            int value = owner.oldAnalogValue[i];
            System.out.print("" + value + ",");
        }
        System.out.println("");
    }

    private void printDigitalInputs() {

        for (int i = 0; i < owner.digitalPort.length; i++) {
            System.out.print("Digital Port : " + i + " : ");

            int port = owner.digitalPort[i];

            for (int j = 0; j < 8; j++) {
                int valux = port & (1 << j);

                if (valux > 0) {
                    System.out.print("1,");
                } else {
                    System.out.print("0,");
                }
            }
            System.out.println();
        }
    }

}

public class Firmata extends JVSMain implements MyOpenLabDriverOwnerIF {

    int inputPins = 0;
    int outputPins = 0;

    int minPinAnalog = 0;

    public int oldDigitalPin[] = new int[100];
    public int oldAnalogPin[] = new int[100];

    public int digitalOutputPort[] = new int[10];

    private Image image;

    String lines[] = null;
    String[] allowedLines = null;

    private MyOpenLabDriverIF driver;
    private MyTimer timer;

    public int digitalPort[] = new int[50];

    private VSPropertyDialog text = new VSPropertyDialog();
    private VSInteger delay_processing_outputs = new VSInteger(200);
    //private VSPropertyDialog capatibilities_button = new VSPropertyDialog();

    private VSString textVar = new VSString("");

    VS1DByte inBytes = new VS1DByte(1);

    private VSObject in[];
    private int[] inPinNumbers;
    private String[] inPinType;

    VSObject out[];
    int[] outPinNumbers;
    public String[] outPinType;

    boolean debug = false;
    long oldMillis = -999;

    int oldAnalogValue[] = new int[70];

    int command = 0;
    int needParams = 0;
    int params[] = new int[1500];
    int counter = 0;

    private int version_1;
    private int version_2;
    private String version_str;

    public ArrayList<PinCapatibilities> capatibilities = new ArrayList();

    private VSComboBox comport = new VSComboBox();
    private VSComboBox baud = new VSComboBox();
    private VSComboBox bits = new VSComboBox();
    private VSComboBox parity = new VSComboBox();
    private VSComboBox stopBits = new VSComboBox();

    public void pinMode(int pin, int mode) {
        System.out.println("pinMode(" + pin + ", " + mode + ");");

        byte bytes[] = new byte[]{
            (byte) (0xF4),
            (byte) (pin),
            (byte) (mode)};

        inBytes.setBytes(bytes);

        if (driver != null) {
            if (debug) {
                System.out.println("PINMODE(" + pin + " , " + mode + ")");
            }
            driver.sendCommand(comport.getItem(comport.selectedIndex) + ";SENDBYTES", inBytes);
        }
    }

    /*public void reportPin(int pin) {
     inBytes.setBytes(new byte[]{
     (byte) (0xD0 + (pin - 2)),
     (byte) (0x01)
     });
     if (driver != null) {
     if (debug) {
     System.out.println("REPORTPIN(" + pin + ")");
     }
     driver.sendCommand(comport.getItem(comport.selectedIndex) + ";SENDBYTES", inBytes);
     }
     }*/
    public void reportPin(int pin) {

        System.out.println("reportPin(" + pin + ")");

        inBytes.setBytes(new byte[]{
            (byte) (0xD0 + (pin - 2)),
            (byte) (0x01)
        });
        if (driver != null) {
            if (debug) {
                System.out.println("REPORTPIN(" + pin + ")");
            }
            driver.sendCommand(comport.getItem(comport.selectedIndex) + ";SENDBYTES", inBytes);
        }
    }

    public void reportAnalogPin(int pin) {

        inBytes.setBytes(new byte[]{
            (byte) (0xC0),
            (byte) (0x01),});
        if (driver != null) {
            if (debug) {
                System.out.println("reportAnalogPin(" + pin + ")");
            }
            driver.sendCommand(comport.getItem(comport.selectedIndex) + ";SENDBYTES", inBytes);
        }
    }

    /*
        public static int LSB(int value) {
        return value & 0x7F;
    }

    public static int MSB(int value) {
        return (value >> 7) & 0x7F;
    }
     */
 public void analogWriteold(int pin, int value) {

        // Only Send Pin when Changed!
        if (oldAnalogPin[pin] != value) {
            oldAnalogPin[pin] = value;

            inBytes.setBytes(new byte[]{
                (byte) (Constanten.ANALOG_MESSAGE | (pin & 0x0F)),
                (byte) (value & 0x7F),
                (byte) (value >> 7)});

            if (driver != null) {
                driver.sendCommand(comport.getItem(comport.selectedIndex) + ";SENDBYTES", inBytes);
            }
        }
    }
    
    // Extended Analog Write above pin 15!!!
    public void analogWrite(int pin, int value) {

        // Only Send Pin when Changed!
        if (oldAnalogPin[pin] != value) {
            oldAnalogPin[pin] = value;
            
            inBytes.setBytes(new byte[]{
                (byte) (0xF0),
                (byte) (0x6F),
                (byte) pin,
                (byte) (value & 0x7F),
                (byte) (value >> 7),
                (byte) (0xF7)
            });

            if (driver != null) {
                driver.sendCommand(comport.getItem(comport.selectedIndex) + ";SENDBYTES", inBytes);
            }
        }
    }

    public void digitalWrite(int pin, int value) {

        // Only Send Pin when Changed!
        if (oldDigitalPin[pin] != value) {
            oldDigitalPin[pin] = value;

            int portNumber = (pin >> 3) & 0x0F;

            int pinNo = pin - (portNumber * 8);

            //System.out.println("PinNo : "+pinNo);
            if (value == 1) {
                digitalOutputPort[portNumber] = digitalOutputPort[portNumber] | (1 << (pinNo));
            } else {
                digitalOutputPort[portNumber] = digitalOutputPort[portNumber] & ~(1 << (pinNo));
            }

            if (debug) {
                System.out.println("Port = " + (Constanten.DIGITAL_MESSAGE | portNumber));
                System.out.println("byte 1 = " + (digitalOutputPort[portNumber] & 0x7F));
                System.out.println("byte 2 = " + (digitalOutputPort[portNumber] >> 7));
            }

            inBytes.setBytes(new byte[]{
                (byte) (Constanten.DIGITAL_MESSAGE | portNumber),
                (byte) (digitalOutputPort[portNumber] & 0x7F),
                (byte) (digitalOutputPort[portNumber] >> 7),});
            if (driver != null) {
                driver.sendCommand(comport.getItem(comport.selectedIndex) + ";SENDBYTES", inBytes);
            }
        }
    }

    /* capabilities query
     * -------------------------------
     * 0  START_SYSEX (0xF0) (MIDI System Exclusive)
     * 1  capabilities query (0x6B)
     * 2  END_SYSEX (0xF7) (MIDI End of SysEx - EOX)
     */

 /* capabilities response
     * -------------------------------
     * 0  START_SYSEX (0xF0) (MIDI System Exclusive)
     * 1  capabilities response (0x6C)
     * 2  1st mode supported of pin 0
     * 3  1st mode's resolution of pin 0
     * 4  2nd mode supported of pin 0
     * 5  2nd mode's resolution of pin 0
     ...   additional modes/resolutions, followed by a single 127 to mark the
     end of the first pin's modes.  Each pin follows with its mode and
     127, until all pins implemented.
     * N  END_SYSEX (0xF7)
     */
    public void queryCapatibilities() {

        //VS1DByte inBytes = new VS1DByte(1);
        inBytes.setBytes(new byte[]{
            (byte) 0xF0,
            (byte) 0x6B,
            (byte) 0xf7}
        );

        System.out.println("queryCapatibilities");
        if (driver != null) {
            driver.sendCommand(comport.getItem(comport.selectedIndex) + ";SENDBYTES", inBytes);
        }

        // Welche Pins unterstützen Analog Input?
        /* analog mapping query
         * -------------------------------
        * 0  START_SYSEX (0xF0) (MIDI System Exclusive)
        * 1  analog mapping query (0x69)
        * 2  END_SYSEX (0xF7) (MIDI End of SysEx - EOX)
         */
        //VS1DByte inBytes = new VS1DByte(1);
        inBytes.setBytes(new byte[]{
            (byte) 0xF0,
            (byte) 0x69,
            (byte) 0xf7}
        );

        System.out.println("queryCapatibilities");
        if (driver != null) {
            driver.sendCommand(comport.getItem(comport.selectedIndex) + ";SENDBYTES", inBytes);
        }

    }

    public void initAllPins() {

        String str = "";

        // report analog pin     0xC0
        // report digital port   0xD0
        str += "C0 01 D0 01 C1 01 D1 01 C2 01 ";
        str += "D2 01 C3 01 D3 01 C4 01 D4 01 C5 01 D5 01 C6 01 ";
        str += "D6 01 C7 01 D7 01 C8 01 D8 01 C9 01 D9 01 CA 01 ";
        str += "DA 01 CB 01 DB 01 CC 01 DC 01 CD 01 DD 01 CE 01 ";
        str += "DE 01 CF 01 DF 01";

        String splitted[] = str.split(" ");

        byte arr[] = new byte[splitted.length];
        for (int i = 0; i < splitted.length; i++) {
            arr[i] = (byte) (Integer.parseInt(splitted[i], 16));
        }

        inBytes.setBytes(arr);
        if (driver != null) {
            driver.sendCommand(comport.getItem(comport.selectedIndex) + ";SENDBYTES", inBytes);
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
        }

        str = "";
        str += "F0 6D 02 F7 F0 6D 03 F7 F0 6D 04 F7 F0 6D 05 F7";
        str += " F0 6D 06 F7 F0 6D 07 F7 F0 6D 08 F7 F0 6D 09 F7";
        str += " F0 6D 0A F7 F0 6D 0B F7 F0 6D 0C F7 F0 6D 0D F7";
        str += " F0 6D 0E F7 F0 6D 0F F7 F0 6D 10 F7 F0 6D 11 F7";
        str += " F0 6D 12 F7 F0 6D 13 F7 F0 6D 14 F7 F0 6D 15 F7";
        str += " F0 6D 16 F7";

        splitted = str.split(" ");
        arr = new byte[splitted.length];
        for (int i = 0; i < splitted.length; i++) {

            if (splitted[i].trim().length() > 0) {
                arr[i] = (byte) (Integer.parseInt(splitted[i], 16));
            }
        }

        inBytes.setBytes(arr);
        if (driver != null) {
            driver.sendCommand(comport.getItem(comport.selectedIndex) + ";SENDBYTES", inBytes);
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
        }

    }

    //
    //
    // Servo Utils : compound commands for servo piloting ...
    //
    //
    /**
     * Return less significant byte
     *
     * @param value value
     * @return less significant byte
     */
    public static int LSB(int value) {
        return value & 0x7F;
    }

    /**
     * Return most significant byte
     *
     * @param value value
     * @return most significant byte
     */
    public static int MSB(int value) {
        return (value >> 7) & 0x7F;
    }

    /* 
     * Servo config
     * --------------------
     * 0  START_SYSEX (0xF0)
     * 1  SERVO_CONFIG (0x70)
     * 2  pin number (0-127)
     * 3  minPulse LSB (0-6)
     * 4  minPulse MSB (7-13)
     * 5  maxPulse LSB (0-6)
     * 6  maxPulse MSB (7-13)
     * 7  angle LSB (0-6)
     * 8  angle MSB (7-13)
     * 9  END_SYSEX (0xF7)
     */
    public void setServoConfig(int pinNumber, int minPulse, int maxPulse) {

        byte pin = (byte) (pinNumber & 0xff);

        byte minPulseLSB = (byte) LSB(minPulse);
        byte minPulseMSB = (byte) MSB(minPulse);

        byte maxPulseLSB = (byte) LSB(maxPulse);
        byte maxPulseMSB = (byte) MSB(maxPulse);

        inBytes.setBytes(new byte[]{
            (byte) 0xF0,
            (byte) 0x70,
            pin,
            minPulseLSB,
            minPulseMSB,
            maxPulseLSB,
            maxPulseMSB,
            (byte) 0xF7
        });
        if (driver != null) {
            driver.sendCommand(comport.getItem(comport.selectedIndex) + ";SENDBYTES", inBytes);
        }

    }

    public Firmata() {
        for (int i = 0; i < oldDigitalPin.length; i++) {
            oldDigitalPin[i] = -999;
        }

        for (int i = 0; i < oldAnalogPin.length; i++) {
            oldAnalogPin[i] = -999;
        }

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

        baud.selectedIndex = 11;
        bits.selectedIndex = 3;

    }

    public void start_sendCommands() {

        /*try {

         out = serialPort.getOutputStream();
         (new Thread(new SerialWriter(out, this))).start();

         } catch (IOException ex) {
         Logger.getLogger(Firmata.class.getName()).log(Level.SEVERE, null, ex);
         } finally {
         try {
         out.close();
         } catch (IOException ex) {
         Logger.getLogger(Firmata.class.getName()).log(Level.SEVERE, null, ex);
         }
         }*/
    }

    public void paint(java.awt.Graphics g) {
        if (image != null) {
            drawImageCentred(g, image);
        }
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
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    public void setPropertyEditor() {

        element.jAddPEItem("Output refresh interval", delay_processing_outputs, 10, 10000);

        //element.jAddPEItem("Poll Time[ms]", pollTime, 1, 5000);
        //element.jAddPEItem("Capatibilities", capatibilities_button, 0, 0);
        element.jAddPEItem("COM Port", comport, 0, 0);

        element.jAddPEItem("Baud", baud, 1, 999999);
        element.jAddPEItem("DataBits", bits, 5, 8);
        element.jAddPEItem("stopBits", stopBits, 1, 2);
        element.jAddPEItem("Partity", parity, 0, 1);

        element.jAddPEItem("Config Pins", text, 0, 0);
    }

    private String getCapatibilities() {

        String str = "";

        ArrayList<Object> args = new ArrayList<>();
        args.add(comport.getItem(comport.selectedIndex));
        args.add(new Integer(baud.getItem(baud.selectedIndex)));
        args.add(new Integer(bits.getItem(bits.selectedIndex)));
        args.add(new Integer(stopBits.getItem(stopBits.selectedIndex)));
        args.add(new Integer(parity.getItem(parity.selectedIndex)));
        args.add(true);

        driver = element.jOpenDriver("MyOpenLab.RS232", args);
        driver.registerOwner(this);

        try {

            Thread.sleep(4000);
            queryCapatibilities();
            Thread.sleep(1000);

            String strCom = comport.getItem(comport.selectedIndex);
            driver.sendCommand(strCom + ";CLOSE", null);
            element.jCloseDriver("MyOpenLab.RS232");

            str += "Firmata Protocol Version: " + version_1 + "." + version_2 + " (" + version_str + ")\n";

            for (int i = 0; i < capatibilities.size() - 1; i++) {

                PinCapatibilities items = capatibilities.get(i);
                str += "" + i + "=";
                if (items.items.isEmpty()) {
                    str += "NOT SUPPORTED\n";
                } else {
                    for (PinCapatibilityItem item : items.items) {
                        String type;

                        switch (item.pinCapatibility) {
                            case Constanten.PIN_INPUT:
                                type = "DIGITAL_INPUT";
                                break;
                            case Constanten.INPUT_PULLUP:
                                type = "INPUT_PULLUP";
                                break;
                            case Constanten.PIN_OUTPUT:
                                type = "DIGITAL_OUTPUT";
                                break;
                            case Constanten.PIN_ANALOG:
                                type = "ANALOG_INPUT";
                                break;
                            case Constanten.PIN_PWM:
                                type = "PWM_OUTPUT";
                                break;
                            case Constanten.PIN_SERVO:
                                type = "SERVO_OUTPUT";
                                break;
                            default:
                                type = "UNKNOWN";
                                break;
                        }

                        str += "" + type + ";";
                    }
                    str += "\n";
                }
            }
            if (debug) {
                System.out.println(str);
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(Firmata.class.getName()).log(Level.SEVERE, null, ex);

        }

        return str;
    }

    @Override
    public void propertyChanged(Object o) {
        /*if (o.equals(text)) {
         NewJDialog frm = new NewJDialog(null, true);
         frm.jEditorPane1.setText(textVar.getValue());
         frm.setTitle("Config-Pins");
         frm.setVisible(true);

         if (frm.result) {
         textVar.setValue(frm.text);

         init();
         }
         //loadImage(file.getValue());
         }*/

        if (o.equals(text)) {

            if (comport.getSize() == 0) {
                showMessage("Kein Com Port");
                return;
            }

            if (comport.getItem(comport.selectedIndex).trim().length() == 0) {
                showMessage("No Port Selected");
                return;
            }

            final FormWait formwait = new FormWait();

            formwait.setVisible(true);
            formwait.repaint();

            String xcapatibilities = getCapatibilities();
            String xsettings = textVar.getValue();
            DialogPins dialogPins = new DialogPins(null, true);
            dialogPins.init(xcapatibilities, xsettings);

            formwait.dispose();
            dialogPins.setVisible(true);

            if (dialogPins.result.length() > 0) {

                textVar.setValue(dialogPins.result);
                init();
                
            }
        }

        /*if (o.equals(capatibilities_button)) {

         ArrayList<Object> args = new ArrayList<>();
         args.add(comport.getItem(comport.selectedIndex));
         args.add(new Integer(baud.getItem(baud.selectedIndex)));
         args.add(new Integer(bits.getItem(bits.selectedIndex)));
         args.add(new Integer(stopBits.getItem(stopBits.selectedIndex)));
         args.add(new Integer(parity.getItem(parity.selectedIndex)));
         args.add(true);

         driver = element.jOpenDriver("MyOpenLab.RS232", args);
         driver.registerOwner(this);

         try {

         Thread.sleep(6000);
         queryCapatibilities();
         Thread.sleep(1000);

         String strCom = comport.getItem(comport.selectedIndex);
         driver.sendCommand(strCom + ";CLOSE", null);
         element.jCloseDriver("MyOpenLab.RS232");

         String str = "";

         str += "Firmata Protocol Version: " + version_1 + "." + version_2 + " (" + version_str + ")\n";

         for (int i = 0; i < capatibilities.size() - 1; i++) {

         PinCapatibilities items = capatibilities.get(i);
         str += "" + i + "=";
         if (items.items.isEmpty()) {
         str += "NOT SUPPORTED\n";
         } else {
         for (PinCapatibilityItem item : items.items) {
         String type;

         switch (item.pinCapatibility) {
         case Constanten.PIN_INPUT:
         type = "DIGITAL_INPUT";
         break;
         case Constanten.PIN_OUTPUT:
         type = "DIGITAL_OUTPUT";
         break;
         case Constanten.PIN_ANALOG:
         type = "ANALOG_INPUT";
         break;
         case Constanten.PIN_PWM:
         type = "PWM_OUTPUT";
         break;
         case Constanten.PIN_SERVO:
         type = "SERVO_OUTPUT";
         break;
         default:
         type = "UNKNOWN";
         break;
         }

         str += "" + type + ";";
         }
         str += "\n";
         }
         }
         if (debug) {
         System.out.println(str);
         }

         NewJDialog frm = new NewJDialog(null, true);
         frm.setTitle("Capatibilities");
         frm.jEditorPane1.setText(str);
         frm.setVisible(true);

         if (frm.result) {
         }

         } catch (InterruptedException ex) {
         Logger.getLogger(Firmata.class.getName()).log(Level.SEVERE, null, ex);

         }
         }*/
    }

    public void init() {

        ArrayList<Object> args = new ArrayList<>();
        args.add("NULL");
        args.add(0);
        args.add(0);
        args.add(0);
        args.add(0);
        args.add(false);

        driver = element.jOpenDriver("MyOpenLab.RS232", args);
        
        System.out.println("driver="+driver.toString());

        comport.clear();
        comport.addItem("");

        if (driver != null) {

            ArrayList<String> ports = new ArrayList();
            driver.sendCommand("NULL;GETPORTS", ports);

            System.out.println("size of Ports : " + ports.size());
            for (String port : ports) {
                System.out.println("-X-------> Port : " + port);
                comport.addItem(port);
            }

            driver.sendCommand("NULL;CLOSE", null);
            element.jCloseDriver("MyOpenLab.RS232");

        } else {
            System.out.println("DRIVER IS NULL!!!");
        }

        inputPins = 0;
        outputPins = 0;

        // Allow only Active Pins
        String str = textVar.getValue().trim();

        str = str.replaceAll("\n", "");
        lines = str.split(";");

        ArrayList<String> tempLines = new ArrayList();
        for (String line : lines) {
            if (line.trim().length() > 1) {
                if (line.substring(0, 1).equals("a")) {
                    tempLines.add(line.substring(1));
                }
            }
        }

        allowedLines = new String[tempLines.size()];

        int counterc = 0;
        for (String line : tempLines) {
            allowedLines[counterc++] = line.trim();
            System.out.println("line X " + line);
        }

        for (String line : allowedLines) {

            if (line.length() > 0) {

                System.out.println("line : " + line);
                String cols[] = line.split("=");

                if (cols.length == 2) {
                    String pinType = cols[1].trim().replace("\n", "");

                    if (pinType.trim().startsWith("DIGITAL_OUTPUT") || pinType.trim().startsWith("PWM_OUTPUT") || pinType.trim().startsWith("SERVO_OUTPUT")) {
                        inputPins++;
                    }
                    if (pinType.trim().startsWith("DIGITAL_INPUT") || pinType.trim().startsWith("INPUT_PULLUP") || pinType.trim().startsWith("ANALOG_INPUT")) {
                        outputPins++;
                    }
                }
            }
        }

        initPins(0, outputPins, 0, inputPins);
        //setSize(32 + 22, 10 + (12 * 10));
        int minSize=0;
        if(inputPins>outputPins) minSize=inputPins;
        if(outputPins>inputPins) minSize=outputPins;
        minSize=(12 * minSize);
        setSize(32 + 22, 76 + minSize);
        //System.err.println("OutPut Pins="+outputPins+" InputPins="+inputPins);

        initPinVisibility(false, true, false, true);

        element.jSetInnerBorderVisibility(true);

        int c = 0;

        for (String line : allowedLines) {
            String cols[] = line.split("=");

            if (cols.length == 2) {
                String pinNum = cols[0].trim().replace("\n", "");
                String pinType = cols[1].trim().replace("\n", "");

                if (pinType.trim().startsWith("DIGITAL_INPUT") || pinType.trim().startsWith("INPUT_PULLUP")) {
                    setPin(c, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);
                    element.jSetPinDescription(c, pinType +" pin(" + pinNum +") ");
                    c++;
                }

                if (pinType.trim().startsWith("ANALOG_INPUT")) {
                    setPin(c, ExternalIF.C_INTEGER, ExternalIF.PIN_OUTPUT);
                    element.jSetPinDescription(c, pinType +" pin(" + pinNum +") ");
                    c++;
                }

            }
        }
        for (String line : allowedLines) {
            String cols[] = line.split("=");

            if (cols.length == 2) {
                String pinNum = cols[0].trim().replace("\n", "");
                String pinType = cols[1].trim().replace("\n", "");

                if (pinType.startsWith("DIGITAL_OUTPUT")) {
                    setPin(c, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
                    element.jSetPinDescription(c, pinType +" pin(" + pinNum +") ");
                    c++;
                }

                if (pinType.startsWith("PWM_OUTPUT")) {
                    setPin(c, ExternalIF.C_INTEGER, ExternalIF.PIN_INPUT);
                    element.jSetPinDescription(c, pinType +" pin(" + pinNum +") ");
                    c++;
                }

                if (pinType.startsWith("SERVO_OUTPUT")) {
                    setPin(c, ExternalIF.C_INTEGER, ExternalIF.PIN_INPUT);
                    element.jSetPinDescription(c, pinType +" pin(" + pinNum +") ");
                    c++;
                }

            }
        }

        String fileName = element.jGetSourcePath() + "iconCircuit.png";
        image = element.jLoadImage(fileName);

        element.jSetCaptionVisible(true);
        element.jSetCaption("Standard Firmata Interface v1.2 JV");
        setName("Standard Firmata Interface JV");

    }

    @Override
    public void initInputPins() {

        int c = 0;
        in = new VSObject[inputPins];
        inPinNumbers = new int[inputPins];
        inPinType = new String[inputPins];

        //lines = textVar.getValue().split(";");
        for (String line : allowedLines) {
            String cols[] = line.split("=");

            if (cols.length == 2) {
                String pinNum = cols[0].trim().replace("\n", "");
                String pinType = cols[1].trim().replace("\n", "").trim();

                if (pinType.startsWith("DIGITAL_OUTPUT")) {
                    in[c] = (VSBoolean) element.getPinInputReference(outputPins + c);

                    inPinNumbers[c] = Integer.parseInt(pinNum);
                    inPinType[c] = pinType;

                    System.out.println("in[" + c + "]=" + pinType);

                    c++;
                }

                if (pinType.startsWith("PWM_OUTPUT")) {
                    in[c] = (VSInteger) element.getPinInputReference(outputPins + c);

                    inPinNumbers[c] = Integer.parseInt(pinNum);
                    inPinType[c] = pinType;

                    System.out.println("in[" + c + "]=" + pinType);

                    c++;

                }

                if (pinType.startsWith("SERVO_OUTPUT")) {
                    in[c] = (VSInteger) element.getPinInputReference(outputPins + c);

                    inPinNumbers[c] = Integer.parseInt(pinNum);
                    inPinType[c] = pinType;

                    System.out.println("in[" + c + "]=" + pinType);

                    c++;
                }

            }
        }

    }

    @Override
    public void initOutputPins() {
        int c = 0;
        out = new VSObject[outputPins];
        outPinNumbers = new int[outputPins];
        outPinType = new String[outputPins];

        //lines = textVar.getValue().split(";");
        for (String line : allowedLines) {
            if (line.length() > 0) {
                String cols[] = line.trim().split("=");

                if (cols.length == 2) {
                    String pinNum = cols[0].trim().replace("\n", "");
                    String pinType = cols[1].trim().replace("\n", "").trim();

                    if (pinType.startsWith("DIGITAL_INPUT")||pinType.startsWith("INPUT_PULLUP") ) {
                        //element.setPinOutputReference(c++,taste1);
                        out[c] = new VSBoolean();
                        element.setPinOutputReference(c, out[c]);

                        outPinNumbers[c] = Integer.parseInt(pinNum);
                        outPinType[c] = pinType;

                        System.out.println("out[" + c + "]=" + pinType);

                        c++;
                    }

                    if (pinType.startsWith("ANALOG_INPUT")) {
                        out[c] = new VSInteger();
                        element.setPinOutputReference(c, out[c]);

                        outPinNumbers[c] = Integer.parseInt(pinNum);
                        outPinType[c] = pinType;

                        System.out.println("out[" + c + "]=" + pinType);

                        c++;
                    }
                }
            }
        }
    }

    public boolean started = false;

    public void start() {

        try {
            ArrayList<Object> args = new ArrayList<>();

            args.add(comport.getItem(comport.selectedIndex));
            args.add(new Integer(baud.getItem(baud.selectedIndex)));
            args.add(new Integer(bits.getItem(bits.selectedIndex)));
            args.add(new Integer(stopBits.getItem(stopBits.selectedIndex)));
            args.add(new Integer(parity.getItem(parity.selectedIndex)));
            args.add(true); // No Parity

            driver = element.jOpenDriver("MyOpenLab.RS232", args);
            driver.registerOwner(this);
            started = true;

            Thread.sleep(4000);

            queryCapatibilities();

            //init();
            initAllPins();

            Thread.sleep(100);

            for (String line : allowedLines) {
                String cols[] = line.split("=");

                if (cols.length == 2) {
                    int pinNum = Integer.parseInt(cols[0].trim().replace("\n", ""));
                    String pinType = cols[1].trim().replace("\n", "");

                    if (pinType.trim().startsWith("DIGITAL_OUTPUT")) {
                        pinMode(pinNum, Constanten.PIN_OUTPUT);
                    }
                    if (pinType.trim().startsWith("PWM_OUTPUT")) {
                        pinMode(pinNum, Constanten.PIN_PWM);
                    }
                    if (pinType.trim().startsWith("SERVO_OUTPUT")) {

                        //System.out.println("XXX : "+pinType.substring("SERVO_OUTPUT".length()+1, pinType.length()-1));
                        String splitted[] = pinType.substring("SERVO_OUTPUT".length() + 1, pinType.length() - 1).split(",");

                        if (splitted.length == 2) {
                            setServoConfig(pinNum, Integer.parseInt(splitted[0].trim()), Integer.parseInt(splitted[1].trim()));
                        }
                        pinMode(pinNum, Constanten.PIN_SERVO);
                    }
                    if (pinType.trim().startsWith("DIGITAL_INPUT")) {
                        pinMode(pinNum, Constanten.PIN_INPUT);
                        //reportPin(pinNum);
                    }
                    if (pinType.trim().startsWith("INPUT_PULLUP")) {
                        pinMode(pinNum, Constanten.INPUT_PULLUP);
                        //reportPin(pinNum);
                    }
                    if (pinType.trim().startsWith("ANALOG_INPUT")) {
                        pinMode(pinNum, Constanten.PIN_ANALOG);
                        //reportAnalogPin(pinNum);
                    }
                }
            }

            timer = new MyTimer();
            timer.delay = delay_processing_outputs.getValue();
            timer.owner = this;
            timer.start();
        } catch (InterruptedException ex) {
            Logger.getLogger(Firmata.class.getName()).log(Level.SEVERE, null, ex);
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

    @Override
    public void elementActionPerformed(ElementActionEvent evt) {
        int idx = evt.getSourcePinIndex();
        switch (idx) {
            case 13: {

                /*if (!started && comStart.getValue() == true) {
                 started = true;
                 ArrayList<Object> args = new ArrayList<Object>();

                 //args.add(new String("COM" + comPort.getValue()));
                 args.add(new String(comport.getItem(comport.selectedIndex)));
                 args.add(new Integer(57600));
                 args.add(new Integer(8));
                 args.add(new Integer(1));
                 args.add(new Integer(0)); // No Parity
                    
                 driver = element.jOpenDriver("MyOpenLab.RS232", args);
                 driver.registerOwner(this);


                 }*/
            }
            break;
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

    private void processIn(int input_byte) {
        //int len = values.getLength();

        int cc = input_byte & 0b11111111;

        try {
            throw new Exception("empfangen: " + cc);
        } catch (Exception ex) {
            //throw new Exception("empfangen: " + cc);
            //Logger.getLogger(Firmata.class.getName()).log(Level.SEVERE, null, ex);
        }

        //int cc = this.in.read();
        if (needParams == -1) {
            params[counter++] = cc;

            if (cc == 0xF7) {

                if (params[0] == 0x6A) {

                    minPinAnalog = -1;
                    for (int i = 0; i < capatibilities.size() - 1; i++) {

                        PinCapatibilities items = capatibilities.get(i);

                        for (int j = 0; j < items.items.size(); j++) {
                            PinCapatibilityItem item = items.items.get(j);
                            item.analog_pin = params[i + 1];

                            if (minPinAnalog == -1 && item.analog_pin < 127) {
                                minPinAnalog = i;
                            }
                        }
                    }
                    // element.jConsolePrintln("Min Analog Pin No=" + minPinAnalog);

                    command = 0;
                    counter = 0;
                    needParams = 0;
                } else if (params[0] == 0x6C) {

                    capatibilities.clear();

                    PinCapatibilities pin = new PinCapatibilities();
                    capatibilities.add(pin);

                    for (int i = 1; i < counter - 1; i++) {

                        if (params[i] == 127) {
                            pin = new PinCapatibilities();
                            capatibilities.add(pin);

                            continue;
                        }

                        PinCapatibilityItem capatibility = new PinCapatibilityItem();
                        capatibility.pinCapatibility = params[i];
                        capatibility.pinResolution = params[i + 1];
                        i++;
                        pin.items.add(capatibility);

                    }

                    for (int i = 0; i < capatibilities.size() - 1; i++) {

                        PinCapatibilities items = capatibilities.get(i);
                        System.out.println("PIN(" + i + "):");
                        for (int j = 0; j < items.items.size(); j++) {
                            PinCapatibilityItem item = items.items.get(j);

                            System.out.println("Capatibility : " + item.pinCapatibility);

                        }
                    }
                    command = 0;
                    counter = 0;
                    needParams = 0;
                } else {

                    // ENDE des Strings                                
                    if (counter > 5) {
                        version_1 = params[0];
                        version_2 = params[1];
                        version_str = "";

                        for (int i = 4; i < counter - 1; i += 2) {

                            version_str += (char) params[i];
                        }

                        version_str = version_str.substring(1);
                        System.out.println("Firmata Protocol Version: " + version_1 + "." + version_2 + " (" + version_str + ")");
                    }

                    command = 0;
                    counter = 0;
                    needParams = 0;
                }

            }
        } else if (needParams > 0) {

            needParams--;
            params[needParams] = cc;

            if (needParams == 0) {

                if (command >= 0x90 && command <= 0x9F) {

                    int PortNr = (command - 0x90);

                    int value = (params[0] << 7) + params[1];

                    digitalPort[PortNr] = value;

                    //isSetPins = new boolean[24];
                    /*System.out.println("DIGITAL_INPUT("+PortNr+","+ value+")");
                     for (int i = 0; i < 24; i++) {
                     int valux = value & (1 << i);

                     if (valux > 0) {
                     isSetPins[i + (PortNr * 8)] = true;
                     }else {
                     isSetPins[i + (PortNr * 8)] = false;
                     }
                     }*/

 /*System.out.print("Pin Input Value Pins: ");
                     for (int i = 0; i < 24; i++) {

                     if (isSetPins[i]) {
                     System.out.print("1,");
                     } else {
                     System.out.print("0,");
                     }

                     }
                     System.out.println();*/
 /*for (int i = 0; i < out.length; i++) {
                        
                     int pinNumber = outPinNumbers[i];
                     String pinType = outPinType[i];

                     //System.out.println("Pin.type=" + pinType);
                     if (pinType.startsWith("DIGITAL_INPUT")) {
                     if (out[i] instanceof VSBoolean) {
                                
                     VSBoolean pin = (VSBoolean) out[i];
                     boolean isSet = isSetPins[pinNumber];
                                
                     if (isSet) {
                     pin.setValue(true);
                     } else {
                     pin.setValue(false);
                     }
                     element.notifyPin(i);
                     //System.out.println("DIGITAL_INPUT(" + pinNumber + ", "+isSet+")");

                     }
                     }
                     }*/
                    //System.out.println("Pin Input Value: " + params[0] + " - " + params[1]);
                    //System.out.println("Pin Input Value: " + params[0] + " - " + params[1]);
                    command = 0;
                    counter = 0;
                    needParams = 0;
                } else if (command >= 0xE0 && command <= (0xEF)) {

                    int valuex = (params[0] << 7) + params[1];

                    int pinNr = minPinAnalog + (command - 0xE0);

                    //element.jConsolePrintln("pin=" + pinNr + "-> value=" + valuex);
                    oldAnalogValue[pinNr] = valuex;

                    command = 0;
                    counter = 0;
                    needParams = 0;
                }
            }

        } else if (cc >= 0x90 && cc <= 0x9F) {
            // Digital Input
            command = cc;
            needParams = 2;
            counter = 0;
        } else if (cc == 0xF0) {
            // START_SYSEX
            command = cc;
            needParams = -1;
            counter = 0;
            //System.out.println("START_SYSEX");
        } else if (cc >= 0xE0 && cc <= (0xEF)) {
            // Analog Input
            command = cc;
            needParams = 2;
            counter = 0;
        } else if (cc == 0xF9) {
            // Version
            command = cc;
            needParams = -1;
            counter = 0;
        } //System.out.println("Empfangen: " + cc + " - " + (char) cc);

    }

    @Override
    public void getCommand(String commando, Object value
    ) {

        if (value instanceof VS1DByte) {
            VS1DByte values = (VS1DByte) value;

            if (commando.equals("BYTESRECEIVED")) {

            }
        }

    }

    @Override
    public void getSingleByte(int val
    ) {
        processIn(val);
    }

    @Override
    public void process() {

        long millis = System.currentTimeMillis();

        long diff = Math.abs(oldMillis - millis);
        // System.out.println("diff" + diff);

        if (diff < 100) {
            return;
        }
        //System.out.println("Processing " + Math.random());
        oldMillis = millis;

        for (int i = 0; i < in.length; i++) {

            int pinNumber = inPinNumbers[i];
            String pinType = inPinType[i];

            if (pinType.startsWith("DIGITAL_OUTPUT")) {
                if (in[i] instanceof VSBoolean) {
                    VSBoolean value = (VSBoolean) in[i];
                    boolean inputPin = value.getValue();

                    if (inputPin == true) {
                        digitalWrite(pinNumber, 1);
                    } else {
                        digitalWrite(pinNumber, 0);
                    }
                }
            }

            if (pinType.startsWith("SERVO_OUTPUT") || pinType.startsWith("PWM_OUTPUT")) {
                if (in[i] instanceof VSInteger) {
                    VSInteger value = (VSInteger) in[i];
                    int inputPin = value.getValue();

                    analogWrite(pinNumber, inputPin);
                }
            }
        }
    }

    public void getPorts(String[] ports) {

    }

    @Override
    public void loadFromStream(java.io.FileInputStream fis) {

        /*ArrayList<Object> args = new ArrayList<>();
         args.add("NULL");
         args.add(0);
         args.add(0);
         args.add(0);
         args.add(0);
         args.add(false);

         driver = element.jOpenDriver("MyOpenLab.RS232", args);

         comport.addItem("CCC");

         if (driver != null) {

         ArrayList<String> ports = new ArrayList();
         driver.sendCommand("NULL;GETPORTS", ports);

         System.out.println("size of Ports : "+ports.size());
         for (String port : ports) {
         System.out.println("-X-------> Port : " + port);
         comport.addItem(port);
         }

         driver.sendCommand("NULL;CLOSE", null);
         element.jCloseDriver("MyOpenLab.RS232");

         } else {
         System.out.println("DRIVER IS NULL!!!");
         }*/

 /*for (String list1 : (String[]) args.get(0)) {
         System.out.println("XXXPORT : " + list1);
         comport.addItem(list1);
         }*/
        textVar.loadFromStream(fis);
        comport.loadFromStream(fis);
        baud.loadFromStream(fis);
        bits.loadFromStream(fis);
        parity.loadFromStream(fis);
        stopBits.loadFromStream(fis);
        delay_processing_outputs.loadFromStream(fis);

        init();

    }

    @Override
    public void saveToStream(java.io.FileOutputStream fos) {

        textVar.saveToStream(fos);
        comport.saveToStream(fos);
        baud.saveToStream(fos);
        bits.saveToStream(fos);
        parity.saveToStream(fos);
        stopBits.saveToStream(fos);
        delay_processing_outputs.saveToStream(fos);
    }
}
