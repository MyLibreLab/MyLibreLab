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

package com.github.mylibrelab.elements.drivers.velleman.keightzerofourseven;// *****************************************************************************

import java.util.ArrayList;

import javax.swing.*;

import VisualLogic.MyOpenLabDriverIF;
import VisualLogic.MyOpenLabDriverOwnerIF;

public class K8047 implements MyOpenLabDriverIF {
    private boolean isOpen = false;
    private K8047d lib;
    private DataSource dataSource;


    private final int[] data = new int[8]; // Daten aus den Datalogger
    private MyOpenLabDriverOwnerIF owner;

    public void setDigitalChannelValue(int channel, Boolean value) {}


    public void setAnalogChannelValue(int channel, Integer value) {}

    private int numToGain(int number) {
        switch (number) {
            case 0:
                return 10;
            case 1:
                return 5;
            case 2:
                return 2;
            case 3:
                return 1;
        }
        return 10;
    }


    public void sendCommand(String commando, Object value) {
        // System.out.println("Commando="+commando);

        if (lib != null) {
            if (commando.equals("LEDON")) lib.LEDon();
            if (commando.equals("LEDOFF")) lib.LEDoff();
            if (commando.equals("SETINTERVALL") && value instanceof Integer) {
                Integer val = (Integer) value;
                dataSource.setIntervall(val);
            }

            if (value instanceof Integer) {
                Integer val = (Integer) value;
                if (commando.equals("SET_GAIN_CH1")) {
                    dataSource.gain_ch1 = numToGain(val);
                    lib.SetGain(1, dataSource.gain_ch1);
                } else if (commando.equals("SET_GAIN_CH1")) {
                    dataSource.gain_ch2 = numToGain(val);
                    lib.SetGain(2, dataSource.gain_ch2);
                } else if (commando.equals("SET_GAIN_CH3")) {
                    dataSource.gain_ch3 = numToGain(val);
                    lib.SetGain(3, dataSource.gain_ch3);
                } else if (commando.equals("SET_GAIN_CH4")) {
                    dataSource.gain_ch4 = numToGain(val);
                    lib.SetGain(4, dataSource.gain_ch4);
                }
            }
        }

    }

    public void registerOwner(MyOpenLabDriverOwnerIF owner) {
        this.owner = owner;
        dataSource.owner = owner;
    }


    // Gain:
    // 1 = 30V
    // 2 = 15V
    // 5 = 6V
    // 10 = 3V
    // Result ist der Faktor! der immer mit 3V multipliziert werden muss
    private double convertGain(int gain) {
        switch (gain) {
            case 1:
                return 10.0;
            case 2:
                return 5.0;
            case 5:
                return 2.0;
            case 10:
                return 1.0;
        }
        return 10;
    }

    public K8047() {

    }

    public static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Attention!", JOptionPane.ERROR_MESSAGE);
    }

    public void driverStart(ArrayList args) {
        if (isOpen) {
            showMessage("K8047 already Open!");
            return;
        }

        isOpen = false;
        System.out.println("Start:");

        try {
            lib = K8047d.INSTANCE;

            lib.StartDevice();

            isOpen = true;

            dataSource = new DataSource(lib, owner);
            dataSource.start();

        } catch (Exception ex) {
            showMessage("K8047/PSC10 : " + ex.toString());
        }
    }

    public void driverStop() {
        if (isOpen) {
            try {
                if (lib != null) {
                    dataSource.stopMe();
                    lib.StopDevice();
                }
            } catch (Exception ex) {
                showMessage("K8047/PSC10 : " + ex.toString());
            }
            isOpen = false;
        }
    }


}
