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
import javax.swing.*;

import gnu.io.*;

public class RS232 implements MyOpenLabDriverIF {

    private VS1DByte outBytes = new VS1DByte(0);
    private ArrayList<Driver> liste = new ArrayList<Driver>();

    private MyOpenLabDriverOwnerIF owner;

    public RS232() {
        System.out.println("*************** PORTS ---------------- ");
    }

    public Driver getDriver(String port) {
        for (int i = 0; i < liste.size(); i++) {
            Driver driver = liste.get(i);
            if (driver.port.equalsIgnoreCase(port)) {
                return driver;
            }
        }

        return null;
    }

    public void sendCommand(String commando, Object value) {

        String[] strings = commando.split(";");

        //System.out.println("COMMAND : "+commando);
        String strPort = strings[0];
        commando = strings[1];

        if (strings.length >= 2) {
            //System.out.println("Port : "+strings[0]);
            //System.out.println("cmd : "+strings[1]);
        }

        if (commando.equals("GETPORTS")) {
            
            Driver driver = getDriver(strPort);
            
            System.out.println("--------> GETPORTS");
            String[] list = driver.listSerialPorts();
            
            //ArrayList<String>
            for (int i = 0; i < list.length; i++) {                
                ((ArrayList<String>)value).add(list[i]);
            }
                        
        }
        if (commando.equals("TIMEOUT")) {
            if (value instanceof VSInteger) {
                VSInteger tmp = (VSInteger) value;

                Driver driver = getDriver(strPort);
                System.out.println("TimeOut = " + tmp.getValue());

                driver.setTimeOut(tmp.getValue());
            }
        }

        if (commando.equals("CLOSE")) {
            Driver driver = getDriver(strPort);
            if (driver != null) {
                driver.close();
                liste.remove(driver);

                System.out.println("liste.size()=" + liste.size());
            }
        }

        if (value instanceof VS1DByte) {
            VS1DByte values = (VS1DByte) value;

            int len = values.getLength();

            byte[] dest = values.getValues();

            /*byte[] dest = new byte[len];
             for (int i=0;i<len;i++)
             {
             dest[i]=(byte)values.getValue(i);
             }*/
            if (commando.equals("SENDBYTES")) {
                Driver driver = getDriver(strPort);
                if (driver != null) {
                    driver.sendBytes(dest);
                }
            }

            if (commando.equals("RTSON")) {
                Driver driver = getDriver(strPort);
                driver.setRTS(true);
            }
            if (commando.equals("RTSOFF")) {
                Driver driver = getDriver(strPort);
                driver.setRTS(false);
            }
            if (commando.equals("DTRON")) {
                Driver driver = getDriver(strPort);
                driver.setDTR(false);
            }
            if (commando.equals("DTROFF")) {
                Driver driver = getDriver(strPort);
                driver.setDTR(false);
            }

            try {
                Thread.sleep(20);
            } catch (Exception ex) {
            }

        }
    }

    public String lastPort = "";

    public void registerOwner(MyOpenLabDriverOwnerIF owner) {
        Driver driver = getDriver(lastPort);
        driver.owner = owner;
        //this.owner=owner;

    }

    public void driverStart(ArrayList args) {
        

        if (args instanceof ArrayList && args.size() >= 5) {

            String strCOM = (String) args.get(0);
            int intBaud = (Integer) args.get(1);
            int bits = (Integer) args.get(2);
            int stopBits = (Integer) args.get(3);
            int parity = (Integer) args.get(4);
            
            

            boolean useOwnInHandler = false;
            if (args.size() == 6) {
                useOwnInHandler = (Boolean) args.get(5);
            }

            int intBits = 8;
            int intStopBits = 0;
            int intParity = 0;

            if (bits == 5) {
                intBits = SerialPort.DATABITS_5;
            }
            if (bits == 6) {
                intBits = SerialPort.DATABITS_6;
            }
            if (bits == 7) {
                intBits = SerialPort.DATABITS_7;
            }
            if (bits == 8) {
                intBits = SerialPort.DATABITS_8;
            }

            if (stopBits == 1) {
                intStopBits = SerialPort.STOPBITS_1;
            }
            if (stopBits == 2) {
                intStopBits = SerialPort.STOPBITS_2;
            }

            if (parity == 0) {
                intParity = SerialPort.PARITY_NONE;
            }
            if (parity == 1) {
                intParity = SerialPort.PARITY_EVEN;
            }
            if (parity == 2) {
                intParity = SerialPort.PARITY_MARK;
            }
            if (parity == 3) {
                intParity = SerialPort.PARITY_ODD;
            }
            if (parity == 4) {
                intParity = SerialPort.PARITY_SPACE;
            }

            System.out.println("PORT : " + strCOM);

            lastPort = strCOM;

            if (getDriver(strCOM) == null) {
                Driver driver = new Driver(strCOM, intBaud, intBits, intStopBits, intParity);
                driver.useOwnInHandler = useOwnInHandler;
                driver.start();

                liste.add(driver);
            } else {
                showMessage("Port is already busy! : " + strCOM);
            }

        }

    }

    /*  private void sendBytes(Driver driver, byte[] bytes)
     {

     }*/
    public void driverStop() {

    }

    public static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Attention!", JOptionPane.ERROR_MESSAGE);
    }

}
