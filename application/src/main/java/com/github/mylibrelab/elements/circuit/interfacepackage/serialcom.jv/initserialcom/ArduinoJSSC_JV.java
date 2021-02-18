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

package com.github.mylibrelab.elements.circuit.interfacepackage.serialcom.jv.initserialcom;/*
                                                                                            * To change this license
                                                                                            * header, choose License
                                                                                            * Headers in Project
                                                                                            * Properties.
                                                                                            * To change this template
                                                                                            * file,
                                                                                            * choose Tools | Templates
                                                                                            * and open the template in
                                                                                            * the
                                                                                            * editor.
                                                                                            */


import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

/**
 *
 * @author velas
 */
public class ArduinoJSSC_JV {
    public static SerialPort serialPort;
    private static Boolean ErrorOpenningPort = false;
    private static Boolean ErrorWrittingPort = false;
    private static Boolean ErrorSettingPort = false;
    private static Boolean ErrorReadingPort = false;
    private static Boolean ErrorClosingPort = false;
    private static final Boolean DebugMsjEnable = true;
    private static Boolean PortOpened = false;
    private static Boolean ArduinoAutoReset = true; // Autoreset Arduino ON?
    private static String PortName = "";
    private static Thread ThreadIn;
    public static final Boolean ArduinoAutoResetON = true;
    public static final Boolean ArduinoAutoResetOFF = false;
    public Boolean EnableCR = false;
    public Boolean EnableLF = false;

    public void ArduinoJSSC_JV() {

    }

    public void SetEnableCR(Boolean EnableCRin) {
        this.EnableCR = EnableCRin;
    }

    public void SetEnableLF(Boolean EnableLFin) {
        this.EnableLF = EnableLFin;
    }

    public void DisposeSerialPort() {
        // Method 'DisposeSerialPort()' is never used, thus I'm commenting it out
        /*
         * serialPort.onDispose();
         * serialPort = null;
         */
    }

    public void OpenPort(String PortName) throws Exception {

        ArduinoJSSC_JV.PortName = PortName;
        serialPort = new SerialPort(PortName);
        if (serialPort.isOpened()) serialPort.closePort();
        serialPort.openPort();
        ErrorOpenningPort = false;
        PortOpened = true;
    }

    public void setDefaultParameters(Boolean ArduinoAutoReset, Thread FatherThreadIn)
            throws SerialPortException, InterruptedException {
        if (PortOpened) {
            ThreadIn = FatherThreadIn;
            ArduinoJSSC_JV.ArduinoAutoReset = ArduinoAutoReset;

            serialPort.setParams(9600, 8, 1, 0);
            System.out.println("Parameters Configured OK");
            ErrorSettingPort = false;
            if (ArduinoAutoReset) {
                Thread.sleep(5000);
            } // Si el Arduino tiene AutoRest se debe esperar 5 Segundos mientras inicia.

        }
    }

    public void setParameters(Boolean ArduinoAutoReset, Thread FatherThreadIn, int time, int BaudR, int dataBits,
            int StopBits, int Parity) throws SerialPortException, InterruptedException {
        if (PortOpened) {
            ThreadIn = FatherThreadIn;
            ArduinoJSSC_JV.ArduinoAutoReset = ArduinoAutoReset;

            // serialPort.setParams(9600, 8, 1, 0);
            serialPort.setParams(BaudR, dataBits, StopBits, Parity);
            // System.out.println("Parameters Configured OK");
            ErrorSettingPort = false;
            if (ArduinoAutoReset) {
                Thread.sleep(time);
            } // Si el Arduino tiene AutoRest se debe esperar 5 Segundos mientras inicia.

        }
    }

    public void WriteString(String BufferStr) throws SerialPortException {

        if (BufferStr == null) {
            BufferStr = "";
        }
        if (EnableLF) BufferStr += "\n";
        if (EnableCR) BufferStr += "\r";
        if (PortOpened) {
            serialPort.writeBytes(BufferStr.getBytes());
            ErrorWrittingPort = false;
        }
    }

    public String ReadFromPort(Thread FatherThreadIn, long TimeSleep)
            throws SerialPortException, InterruptedException, NullPointerException {
        String BufferTemp = "";
        if (PortOpened) {
            ThreadIn = FatherThreadIn;
            serialPort.purgePort(SerialPort.PURGE_RXCLEAR);
            serialPort.purgePort(SerialPort.PURGE_TXCLEAR);
            Thread.sleep(TimeSleep);
            // Thread.sleep(TimeSleep);
            BufferTemp = new String((serialPort.readBytes()));
            ErrorReadingPort = false;
            if (BufferTemp == null) {
                BufferTemp = "";
            }
            return BufferTemp;
        }
        return "";
    }

    public String ReadBytesFromPort(int Bytes, int TimeOut)
            throws SerialPortException, InterruptedException, SerialPortTimeoutException, NullPointerException {
        String BufferTemp = "";
        if (PortOpened) {

            BufferTemp = new String((serialPort.readBytes(Bytes, TimeOut)));
            ErrorReadingPort = false;
            if (DebugMsjEnable) System.out.println("Reading Port " + PortName + " OK");
            if (BufferTemp == null) {
                BufferTemp = "";
                throw new SerialPortTimeoutException(serialPort.getPortName(), "ReadBytesFromPort", TimeOut);
            }
            return BufferTemp;
        }
        return "";
    }

    public void ClosePort() throws SerialPortException {

        if (PortOpened) {

            serialPort.closePort();
            ErrorClosingPort = false;
            PortOpened = false;
        }
    }

    public boolean GetPortOpened() {

        return serialPort.isOpened();
    }

    public String getPortName() {
        if (PortName == null) {
            PortName = "";
        }
        return PortName;
    }


}
