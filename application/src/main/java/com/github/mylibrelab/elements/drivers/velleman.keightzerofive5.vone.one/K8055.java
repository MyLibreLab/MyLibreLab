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

package com.github.mylibrelab.elements.drivers.velleman.keightzerofive5.vone.one;/*
                                                                                  * Copyright (C) 2020 MyLibreLab
                                                                                  * Based on MyOpenLab by Carmelo
                                                                                  * Salafia
                                                                                  * www.myopenlab.de
                                                                                  * Copyright (C) 2004 Carmelo Salafia
                                                                                  * cswi@gmx.de
                                                                                  *
                                                                                  * This program is free software: you
                                                                                  * can
                                                                                  * redistribute it and/or modify
                                                                                  * it under the terms of the GNU
                                                                                  * General Public
                                                                                  * License as published by
                                                                                  * the Free Software Foundation, either
                                                                                  * version 3
                                                                                  * of the License, or
                                                                                  * (at your option) any later version.
                                                                                  *
                                                                                  * This program is distributed in the
                                                                                  * hope that
                                                                                  * it will be useful,
                                                                                  * but WITHOUT ANY WARRANTY; without
                                                                                  * even the
                                                                                  * implied warranty of
                                                                                  * MERCHANTABILITY or FITNESS FOR A
                                                                                  * PARTICULAR
                                                                                  * PURPOSE. See the
                                                                                  * GNU General Public License for more
                                                                                  * details.
                                                                                  *
                                                                                  * You should have received a copy of
                                                                                  * the GNU
                                                                                  * General Public License
                                                                                  * along with this program. If not, see
                                                                                  * <http://www.gnu.org/licenses/>.
                                                                                  *
                                                                                  */

/**
 *
 * @author Salafia
 */
public interface K8055 extends StdCallLibrary {
    K8055 INSTANCE = (K8055) Native.loadLibrary("K8055D", K8055.class);

    NativeLong OpenDevice(NativeLong CardAddress);

    void CloseDevice();

    NativeLong ReadAnalogChannel(NativeLong Channel);

    // public void ReadAllAnalog(com.sun.jna.Pointer Data1, com.sun.jna.Pointer Data2);
    void OutputAnalogChannel(NativeLong Channel, NativeLong Data);

    void OutputAllAnalog(NativeLong Data1, NativeLong Data2);

    void ClearAnalogChannel(NativeLong Channel);

    void ClearAllAnalog();

    void SetAnalogChannel(NativeLong Channel);

    void SetAllAnalog();

    void WriteAllDigital(NativeLong Data);

    void ClearDigitalChannel(NativeLong Channel);

    void ClearAllDigital();

    void SetDigitalChannel(NativeLong Channel);

    void SetAllDigital();

    boolean ReadDigitalChannel(NativeLong Channel);

    NativeLong ReadAllDigital();

    NativeLong ReadCounter(NativeLong CounterNr);

    void ResetCounter(NativeLong CounterNr);

    void SetCounterDebounceTime(NativeLong CounterNr, NativeLong DebounceTime);

    void Version();

    NativeLong SearchDevices();

    NativeLong SetCurrentDevice(NativeLong lngCardAddress);


    /*
     * FUNCTION long __stdcall OpenDevice(long CardAddress);
     * FUNCTION __stdcall CloseDevice();
     * FUNCTION long __stdcall ReadAnalogChannel(long Channel);
     * FUNCTION __stdcall ReadAllAnalog(long *Data1, long *Data2);
     * FUNCTION __stdcall OutputAnalogChannel(long Channel, long Data);
     * FUNCTION __stdcall OutputAllAnalog(long Data1, long Data2);
     * FUNCTION __stdcall ClearAnalogChannel(long Channel);
     * FUNCTION __stdcall ClearAllAnalog();
     * FUNCTION __stdcall SetAnalogChannel(long Channel);
     * FUNCTION __stdcall SetAllAnalog();
     * FUNCTION __stdcall WriteAllDigital(long Data);
     * FUNCTION __stdcall ClearDigitalChannel(long Channel);
     * FUNCTION __stdcall ClearAllDigital();
     * FUNCTION __stdcall SetDigitalChannel(long Channel);
     * FUNCTION __stdcall SetAllDigital();
     * FUNCTION bool __stdcall ReadDigitalChannel(long Channel);
     * FUNCTION long __stdcall ReadAllDigital();
     * FUNCTION long __stdcall ReadCounter(long CounterNr);
     * FUNCTION __stdcall ResetCounter(long CounterNr);
     * FUNCTION __stdcall SetCounterDebounceTime(long CounterNr, long DebounceTime);
     *
     * FUNCTION __stdcall Version();
     * FUNCTION long __stdcall SearchDevices();
     * FUNCTION long __stdcall SetCurrentDevice(long lngCardAddress);
     */
}
