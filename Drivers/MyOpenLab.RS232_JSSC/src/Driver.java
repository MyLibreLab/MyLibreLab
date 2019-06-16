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
import java.util.*;
import jssc.*;

public class Driver {

    //public static gnu.io.CommPortIdentifier portID;
    //public InputStream ins;
    //public OutputStream out;
    public static SerialPort serss;
    //public DataOutputStream dos;
    public int timeOut = 50;
    public boolean error;

    public MyOpenLabDriverOwnerIF owner;

    private VS1DByte outBytes = new VS1DByte(0);

    private SerialReader serialThread;

    public boolean useOwnInHandler = false;
    public boolean portConfigured = false;

    public String port;

    public String[] listSerialPorts() {

        //Enumeration ports = CommPortIdentifier.getPortIdentifiers();
        ArrayList portList = new ArrayList();
        
        String portArray[] = SerialPortList.getPortNames();
        
        return portArray;
    }

    public Driver(String port, int baud, int bits, int stopBits, int parity) {

        this.port = port;
        portConfigured = false;

        error = true;

        System.out.println("DRIVER INIT");

        if (port.trim().equalsIgnoreCase("NULL")) {

            System.out.println("NULL OK");

        } else {
            try {
                System.out.println("PORT :" + port);

                //portID =  getPortIdentifier(port);
                //serss = (SerialPort) portID.open("MYOPENLAB", 2000);
                serss = new SerialPort(port);
                
                if(!serss.isOpened()){
                  serss.openPort();
                  System.out.println("Port Opened OK");
                }
                
                //serss.setSerialPortParams(baud, bits, stopBits, parity);
                if(serss.isOpened() && portConfigured == false){
                    portConfigured = true;
                    serss.setParams(baud, bits, stopBits, parity);
                    serss.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
                    
                } // setSerialPortParams(baud, bits, stopBits, parity);
                System.out.println("Port Baud:"+baud+"_Bits:"+bits+"_StopBits:"+stopBits+"_Parity:"+parity);
                //Thread.currentThread().sleep(2000);
                //dos = new DataOutputStream();
                error = false;
            } catch (Exception ex) {
                System.out.println("Fehler in RS232 Driver : " + ex);
            }
        }

        System.out.println("DRIVER INIT END");

    }

    public void start() {
        try {
            //ins = serss.getInputStream();
            //out = serss.getOutputStream();
            //useOwnInHandler=false;
            if (useOwnInHandler) {
                System.out.println("useOwnInHandler");
                serialThread = new SerialReader();
                serialThread.PortIn = serss;
                serialThread.start();
            } else {
                System.out.println("not useOwnInHandler");
                
                int mask = SerialPort.MASK_RXCHAR;
                if(serss.isOpened()){
                 serss.setEventsMask(mask);
                 serss.addEventListener(new commListener());
                }
                
                //serss.addEventListener(new SerialReader());
                //serss.notifyOnDataAvailable(true);
                
            }

        } catch (Exception e) {
            System.out.println("Fehler: " + e);
        }

    }

    public void setTimeOut(int millis) {
        timeOut = millis;
    }

    public void close() {

        if (serialThread != null) {
            serialThread.stop = true;
            System.out.println("Closing Driver");
        }

        
        if (serss != null) {
            try{
                if(serss.isOpened()){
                    serss.closePort();
                    portConfigured = false;
                }
                System.out.println("Port closed OK");
            }catch(Exception e){
                System.out.println("Error Closing Port "+e);   
            }
        }

    }

    public void sendCommand(String commando, Object value) {

        System.out.println("RS232 COMMAND : " + commando);

        if (value instanceof VS1DByte) {
            VS1DByte values = (VS1DByte) value;

            //int len = values.getLength();
            byte[] dest = values.getValues();

            /*if (commando.equals("RTSON")) serss.setRTS(true);
             if (commando.equals("RTSOFF")) serss.setRTS(false);
             if (commando.equals("DTSON")) serss.setDTR(true);
             if (commando.equals("DTSOFF")) serss.setDTR(false);*/
            if (commando.equals("SENDBYTES")) {
                sendBytes(dest);
            }

        }
    }

    public void registerOwner(MyOpenLabDriverOwnerIF owner) {
        this.owner = owner;
        //firstTime=true;
    }

    public void setRTS(boolean value) {
        try {
            if(serss.isOpened()) serss.setRTS(value);
        } catch (SerialPortException ex) {
            System.out.println(ex);
            //Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setDTR(boolean value) {
        try {
            if(serss.isOpened()) serss.setDTR(value);
        } catch (SerialPortException ex) {
            System.out.println(ex);
            //Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendBytes(byte[] bytes) {
        try {
            
            serss.writeBytes(bytes);

        } catch (Exception ex) {
            System.out.println(ex);
            //Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
            //element.jShowMessage("Error sending Bytes  : "+ex.toString());
        }
    }

    /**
     * Returns a long that contains the same n bits as the given long,with
     * cleared upper rest.
     *
     * @param value the value which lowest bits should be copied.
     * @param bits the number of lowest bits that should be copied.
     * @return a long value that shares the same low bits as the given value.
     */
    private static long copyBits(final long value, byte bits) {
        final boolean logging = false; //turn off logging here
        long converted = 0;
        long comp = 1L << bits;
        while (--bits != -1) {
            if (((comp >>= 1) & value) != 0) {
                converted |= comp;
            }
            if (logging) {
                System.out.print((comp & value) != 0 ? "1" : "0");
            }
        }
        if (logging) {
            System.out.println();
        }
        return converted;
    }

    /**
     * Converts an unsigned byte to a signed short.
     *
     * @param value an unsigned byte value
     * @return a signed short that represents the unsigned byte's value.
     */
    public static short toSigned(final byte value) {
        return (short) copyBits(value, (byte) 8);
    }

    class MyThread extends Thread {

        public void run() {
            System.out.println("RXTX: STARTT");
            while (true) {
                long time2 = System.currentTimeMillis();

                long diff = time2 - time;
                //System.out.println("Time="+diff);

                if (diff > timeOut) {
                    System.out.println("timeout");
                    break;
                }
                try {
                    Thread.sleep(2);
                } catch (Exception ex) {
                }
            }
            thread = null;

            byte buff[] = new byte[strBuffer.size()];
            for (int i = 0; i < strBuffer.size(); i++) {
                buff[i] = strBuffer.get(i).byteValue();
            }
            System.out.println("RXTX:" + new String(strBuffer.toString()));

            strBuffer.clear();

            outBytes.setValues(buff);
            owner.getCommand("BYTESRECEIVED", (Object) outBytes);
        }
    }

    MyThread thread;
    long time = 0;

    public void make() {
        if (thread == null) {
            thread = new MyThread();
            thread.start();
        }
        time = System.currentTimeMillis();
    }

    ArrayList<Byte> strBuffer = new ArrayList<>();

    public class SerialReader extends Thread {

        private SerialPort PortIn;
        public boolean stop = false;

        @Override
        public void run() {
            stop = false;
            try {
                while (true) {

                    if (stop || PortIn.equals(null)) {
                        return;
                    }
                    while (this.PortIn.getInputBufferBytesCount() > 0) {
                        byte[] bufferIn = PortIn.readBytes(1);
                        int cc = bufferIn[0];
                        owner.getSingleByte(cc);

                        if (stop) {
                            return;
                        }
                    }
                }
            } catch (SerialPortException ex) {
                System.out.println(ex);
                //Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        
        }
    }

    class commListener implements SerialPortEventListener {

        int dato = 0;

        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.getEventType() == SerialPortEvent.TXEMPTY) { //.OUTPUT_BUFFER_EMPTY
                System.out.println("Ignored event TX Empty");
            }

            if (event.getEventType() == SerialPortEvent.RXCHAR) {//DATA_AVAILABLE

                try {
 
                    int BytesAvalaible=event.getEventValue();
                    byte[] buffer = new byte[BytesAvalaible];
                    
                    buffer = serss.readBytes(BytesAvalaible);
                    

                    for (int i = 0; i < buffer.length; i++) {
                        strBuffer.add(new Byte(buffer[i]));
                    }

                    make();

                } catch (Exception e) {
                    System.out.println("Fehler: " + e);
                }

            }else if(event.isCTS()){
                if(event.getEventValue() == 1){
                    System.out.println("CTS - ON");
                }
                else {
                    System.out.println("CTS - OFF");
                }
            }
            else if(event.isDSR()){
                if(event.getEventValue() == 1){
                    System.out.println("DSR - ON");
                }
                else {
                    System.out.println("DSR - OFF");
                }
            }
        }

    }

}
