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

package com.github.mylibrelab.elements.circuit.interfacepackage.serialcom.jv.initserialcom;


import java.util.LinkedList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author velas
 */
class WriteAndReadThread extends Thread {

    public static LinkedList colaMensajeSerial = new LinkedList();
    private MessageSerial Message;

    public WriteAndReadThread() {

    }

    @Override
    public void run() {
        while (true) {

            while (colaMensajeSerial.peekFirst() != null) {
                Message = (MessageSerial) colaMensajeSerial.getFirst();
                System.out.println("MessageReceived");
            }

        }
    }

    public class MessageSerial extends Object {
        private String Task;
        private String BufferOut;
        private String BufferIn;

    }
}
