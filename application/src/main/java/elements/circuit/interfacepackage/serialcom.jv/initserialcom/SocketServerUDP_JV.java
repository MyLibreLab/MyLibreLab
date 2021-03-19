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

package elements.circuit.interfacepackage.serialcom.jv.initserialcom;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import VisualLogic.variables.VSserialPort;
import jssc.SerialPortException;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author velasquez Javier
 */
public class SocketServerUDP_JV implements Runnable {

    public ServerSocket serverSocket;
    public Socket socketOut = new Socket();
    public Boolean Error = false;
    private VSserialPort vsSerialExternal;

    private Boolean DebugMessages = false;
    private final int TimeOut = 125;
    private int TimeBeforeRead = 50;
    public boolean socketOpened = false;

    public void setTimeBeforeRead(int TimeBeforeRead) {
        this.TimeBeforeRead = TimeBeforeRead;
    }

    private int Port = 9876;

    volatile String BufferIn = "";
    volatile String COMResponseStr = "";
    volatile DatagramPacket sendPacket;
    volatile DatagramPacket receivePacket;
    protected DataOutputStream salidaCliente;
    protected DataInputStream RespuestaCliente;
    BufferedReader entradaCliente;


    InetAddress IPAddress;

    public SocketServerUDP_JV() {


    }

    public void setDebudMessagesEnable(Boolean DebugMsjEn) {
        this.DebugMessages = DebugMsjEn;
    }



    public void SetPort(int PortIn) {
        this.Port = PortIn;
    }

    public void SetVSSerialP(VSserialPort JSSCIn) {
        this.vsSerialExternal = JSSCIn;
    }

    public Boolean GetErrorStatus() {
        return Error;
    }

    public void OpenSocket() throws IOException {

        serverSocket = new ServerSocket(Port);
        IPAddress = InetAddress.getLocalHost();
        Error = false;
        socketOpened = true;
        if (DebugMessages) System.err.println("SERVER|SOCKET_SERVER_Opened_at_Port:" + Port);

    }

    public void CloseSocket() throws IOException {
        socketOpened = false;
        if (serverSocket.isClosed() == false) {
            serverSocket.close();
        }
    }

    @Override
    public void run() {

        do {
            try {
                socketOut = serverSocket.accept(); // Esperar Conección
                if (DebugMessages) System.err.println("SERVER|SOCKET_SERVER_INCOMMING_CNX");
                Error = false;
                RespuestaCliente = new DataInputStream(socketOut.getInputStream());
                salidaCliente = new DataOutputStream(socketOut.getOutputStream());
                // entradaCliente = new BufferedReader(new InputStreamReader(socketOut.getInputStream()));
                BufferIn = "";
                salidaCliente.writeUTF("200_OK"); // Send Confirmation
                BufferIn = RespuestaCliente.readUTF();
                if (DebugMessages) System.err.println("SERVER|" + BufferIn.length() + "|RECEIVED: " + BufferIn + "|");

                vsSerialExternal.writeBufferStr(BufferIn);
                // Thread.sleep(150);
                COMResponseStr = "";
                vsSerialExternal.setTimeBeforeRead(TimeBeforeRead);
                COMResponseStr = vsSerialExternal.ReadStrBuffer(Thread.currentThread());
                if (COMResponseStr == null) COMResponseStr = "ERROR";
                salidaCliente.writeUTF(COMResponseStr);
                if (DebugMessages) System.err.println("SERVER|RespuestaEnviada" + COMResponseStr + "|");
                Error = false;

            } catch (SocketException ex) {
                // Logger.getLogger(SocketServerUDP_JV.class.getName()).log(Level.SEVERE, null, ex);
                Error = true;
            } catch (IOException E) {
                Error = true;
            } catch (SerialPortException E) {
                Error = true;
            } catch (InterruptedException E) {
                Error = true;
            } catch (Exception E) {
                Error = true;
            }
        } while (socketOpened);
    }


}
