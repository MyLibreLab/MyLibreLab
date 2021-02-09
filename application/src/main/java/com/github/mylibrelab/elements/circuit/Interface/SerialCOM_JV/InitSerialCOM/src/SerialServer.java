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

package com.github.mylibrelab.elements.circuit.Interface.SerialCOM_JV.InitSerialCOM.src;

import java.util.logging.Level;
import java.util.logging.Logger;

import VisualLogic.variables.VSserialPort;
import jssc.SerialPortException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author velas
 */

public class SerialServer implements Runnable {


    public Boolean Error = false;
    private VSserialPort vsSerialExternal;
    private Boolean EnableCR = false;
    private Boolean EnableLF = false;
    private Boolean DebugMessages = false;
    private int TimeOut = 125;
    private int TimeBeforeRead = 50;

    public void setTimeBeforeRead(int TimeBeforeRead) {
        this.TimeBeforeRead = TimeBeforeRead;
    }


    volatile String BufferIn = "";
    volatile String COMResponseStr = "";


    public SerialServer() {


    }

    public void setDebudMessagesEnable(Boolean DebugMsjEn) {
        this.DebugMessages = DebugMsjEn;
    }

    public void setEnableCR(Boolean EnCRin) {
        this.EnableCR = EnCRin;
    }

    public void setEnableLF(Boolean EnLFin) {
        this.EnableLF = EnLFin;
    }

    public void SetTimeout(int timeout) {
        this.TimeOut = timeout;
    }


    public void SetJSSC(VSserialPort JSSCIn) {
        this.vsSerialExternal = JSSCIn;
    }

    public Boolean GetErrorStatus() {
        return Error;
    }


    @Override
    public void run() {

        while (true) {

            try {
                while (vsSerialExternal.getSerialPort().getOutputBufferBytesCount() > 0) {

                }
            } catch (SerialPortException ex) {
                Logger.getLogger(SerialServer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }



}
