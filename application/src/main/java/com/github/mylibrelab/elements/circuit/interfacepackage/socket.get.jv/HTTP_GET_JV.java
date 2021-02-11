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

package com.github.mylibrelab.elements.circuit.interfacepackage.socket.get.jv;// *****************************************************************************

// *****************************************************************************


import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSBoolean;
import VisualLogic.variables.VSInteger;
import VisualLogic.variables.VSString;

public class HTTP_GET_JV extends JVSMain {
    private Image image;
    private VSString IPAddress = new VSString("192.168.0.2");
    private VSInteger ServerPort = new VSInteger(8888);
    private final VSString ServerAnswer = new VSString();
    private VSBoolean enableVMin = new VSBoolean(false);
    private VSBoolean enableVMout = new VSBoolean(false);
    private final String oldValue = "";
    private boolean oldEnableValue = false;

    public void onDispose() {
        if (image != null) {
            image.flush();
            image = null;
        }
    }

    public void paint(java.awt.Graphics g) {
        drawImageCentred(g, image);
    }

    public void init() {
        initPins(0, 2, 0, 3);
        setSize(50, 50);

        initPinVisibility(false, true, false, true);

        element.jSetInnerBorderVisibility(true);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.png");


        setPin(0, ExternalIF.C_BOOLEAN, element.PIN_OUTPUT);
        setPin(1, ExternalIF.C_STRING, element.PIN_OUTPUT);

        setPin(2, ExternalIF.C_BOOLEAN, element.PIN_INPUT);
        setPin(3, ExternalIF.C_STRING, element.PIN_INPUT);
        setPin(4, ExternalIF.C_INTEGER, element.PIN_INPUT);

        element.jSetPinDescription(0, "Enable VM out");
        element.jSetPinDescription(1, "Server Answer");

        element.jSetPinDescription(2, "Enable VM in");
        element.jSetPinDescription(3, "IP Address");
        element.jSetPinDescription(4, "PORT");

        setName("HTTP GET JV");

    }



    public void initInputPins() {
        // enableVMout=(VSBoolean)element.getPinInputReference(0);
        // ServerAnswer=(VSString)element.getPinInputReference(1);
        enableVMin = (VSBoolean) element.getPinInputReference(2);
        IPAddress = (VSString) element.getPinInputReference(3);
        ServerPort = (VSInteger) element.getPinInputReference(4);
    }

    public void initOutputPins() {
        element.setPinOutputReference(0, enableVMout);
        element.setPinOutputReference(1, ServerAnswer);

    }

    @Override
    public void process() {
        if (enableVMin == null || enableVMout == null) {
            enableVMin = new VSBoolean(false);
            enableVMout = new VSBoolean(false);
        }

        enableVMout.setValue(enableVMin.getValue());

        if (IPAddress != null && ServerPort != null && enableVMin.getValue() && oldEnableValue == false) {
            Socket s;
            BufferedReader input;
            String serverAddress = IPAddress.getValue();

            String response = "";
            boolean AnswerOk = false;
            int counter = 0;
            PrintWriter out;

            try {
                s = new Socket(serverAddress, ServerPort.getValue());
                s.setReceiveBufferSize(512);
                s.setSendBufferSize(512);
                s.setSoTimeout(1500);
                s.setKeepAlive(true);

                input =
                        new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new PrintWriter(s.getOutputStream(), true);
                String GetTemp = "";

                // out.print("GET /");
                GetTemp += ("GET /");
                // out.print(" HTTP/1.1"+"\r"+"\n");
                GetTemp += (" HTTP/1.1" + "\r" + "\n");
                // out.print("Host: "+"192.168.0.13"+"\r"+"\n"+"\r"+"\n");
                GetTemp += ("Host: " + serverAddress + "\r" + "\n" + "\r" + "\n");

                // out.print("GET / HTTP/1.1\n\r\n");
                // out.println("Host: 192.168.0.13\n\r\n");
                out.println(GetTemp);


                // out.flush();
                response = "";
                // out.print(GetTemp);
                System.out.print(GetTemp);
                String line = "";
                while ((line = input.readLine()) != null) {
                    response += line;
                    counter++;

                    if (response.contains("<html>") && response.contains("</html>")) {
                        AnswerOk = true;
                        System.out.println(response);
                        // System.out.println(counter);
                        // System.exit(0);
                    }
                }
                // JOptionPane.showMessageDialog(null, response);


                input.close();
                out.flush();
                out.close();
                s.close();

            } catch (Exception e) {
                System.out.println("Error Comm" + e.toString());
                System.out.println(response);
                System.out.println(
                        "Wrong Server IP? -Ping Android Phone to verify network access-Slow Network? -FirewallConfig?");
                try {
                    Thread.sleep(25);
                } catch (Exception et) {

                }
            }

            ServerAnswer.setValue(response);

            ServerAnswer.setChanged(true);
            element.notifyPin(0);
            element.notifyPin(1);

        }
        oldEnableValue = enableVMin.getValue();
    }

}
