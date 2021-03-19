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

package elements.circuit.interfacepackage.serialcom.jv.initserialcom;/*
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


/**
 *
 * @author velas
 */



public class TestArduinoCOM_JV {

    public static void main(String[] args)
            throws Exception {
        // SerialPort serialPort = new SerialPort("/dev/ttyACM0");
        String javaLibPath = System.getProperty("java.library.path");
        System.out.println(javaLibPath);

        ArduinoJSSC_JV ComArduino = new ArduinoJSSC_JV();
        ArduinoJSSC_JV ComArduino2 = new ArduinoJSSC_JV();

        ComArduino.OpenPort("COM3");
        ComArduino.setDefaultParameters(ComArduino.ArduinoAutoResetON, Thread.currentThread());

        ComArduino.WriteString("FUNCIONA!;\n");

        System.out.println("RESPONSE 1: " + ComArduino.ReadFromPort(Thread.currentThread(), 50));

        ComArduino.WriteString("FUNCIONA!;\n");

        System.out.println("RESPONSE 2: " + ComArduino.ReadBytesFromPort(12, 50));

        ComArduino.WriteString("FUNCIONA!;\n");

        System.out.println("RESPONSE 3: " + ComArduino.ReadBytesFromPort(13, 50));

        ComArduino.ClosePort();
    }

}
