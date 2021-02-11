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

package com.github.mylibrelab.elements.circuit.interfacepackage.firmata;/*
                                                                         * To change this license header, choose License
                                                                         * Headers in Project Properties.
                                                                         * To change this template file, choose Tools |
                                                                         * Templates
                                                                         * and open the template in the editor.
                                                                         */



/**
 *
 * @author carmelo
 */
public final class Constanten {



    // public static static int digitalInputData;
    public static int[] analogInputData = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public final static int PIN_INPUT = 0;
    public final static int PIN_OUTPUT = 1;
    public final static int PIN_ANALOG = 2;
    public final static int PIN_PWM = 3;
    public final static int PIN_SERVO = 4;
    public final static int INPUT_PULLUP = 11;

    public static int LOW = 0;
    public static int HIGH = 1;

    public static int DIGITAL_MESSAGE = 0x90; // send data for a digital port
    public static int ANALOG_MESSAGE = 0xE0; // send data for an analog pin (or PWM)
    public static int REPORT_ANALOG = 0xC0; // enable analog input by pin #
    public static int REPORT_DIGITAL = 0xD0; // enable digital input by port
    public static int SET_PIN_MODE = 0xF4; // set a pin to INPUT/OUTPUT/PWM/etc
    public static int REPORT_VERSION = 0xF9; // report firmware version
    public static int SYSTEM_RESET = 0xFF; // reset from MIDI
    public static int START_SYSEX = 0xF0; // start a MIDI SysEx message
    public static int END_SYSEX = 0xF7; // end a MIDI SysEx message

}
