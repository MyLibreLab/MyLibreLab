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

package com.github.mylibrelab.elements.drivers.velleman.k8047;// *****************************************************************************

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.sun.jna.*;
import com.sun.jna.win32.StdCallLibrary;

public interface K8047d extends StdCallLibrary {
    K8047d INSTANCE = (K8047d) Native.loadLibrary("K8047d", K8047d.class);

    void StartDevice();

    void StopDevice();

    void LEDon();

    void LEDoff();

    void nix();

    void SetGain(int channel, int gain);

    void ReadData(int[] data);

}
