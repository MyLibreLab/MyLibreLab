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



import com.sun.jna.*; 
import com.sun.jna.win32.StdCallLibrary;
/**
 *
 * @author Salafia
 */
public interface K8055 extends StdCallLibrary
{
    K8055 INSTANCE = (K8055) Native.loadLibrary("K8055D", K8055.class);

    public NativeLong OpenDevice(NativeLong CardAddress);
    public void CloseDevice();
    public NativeLong ReadAnalogChannel(NativeLong Channel);    
    //public void  ReadAllAnalog(com.sun.jna.Pointer Data1, com.sun.jna.Pointer Data2);
    public void  OutputAnalogChannel(NativeLong Channel, NativeLong Data);
    public void  OutputAllAnalog(NativeLong Data1, NativeLong Data2);
    public void  ClearAnalogChannel(NativeLong Channel); 
    public void  ClearAllAnalog();
    public void  SetAnalogChannel(NativeLong Channel); 
    public void  SetAllAnalog();
    public void  WriteAllDigital(NativeLong Data);
    public void  ClearDigitalChannel(NativeLong Channel);
    public void  ClearAllDigital();
    public void  SetDigitalChannel(NativeLong Channel);
    public void  SetAllDigital();
    public boolean ReadDigitalChannel(NativeLong Channel);
    public NativeLong ReadAllDigital();
    public NativeLong ReadCounter(NativeLong CounterNr);
    public void ResetCounter(NativeLong CounterNr);
    public void SetCounterDebounceTime(NativeLong CounterNr, NativeLong DebounceTime);

    public void Version();
    public NativeLong SearchDevices();
    public NativeLong SetCurrentDevice(NativeLong lngCardAddress);
    
    
    /*FUNCTION long __stdcall OpenDevice(long CardAddress);
    FUNCTION __stdcall CloseDevice();
    FUNCTION long __stdcall ReadAnalogChannel(long Channel);
    FUNCTION __stdcall ReadAllAnalog(long *Data1, long *Data2);
    FUNCTION __stdcall OutputAnalogChannel(long Channel, long Data);
    FUNCTION __stdcall OutputAllAnalog(long Data1, long Data2);
    FUNCTION __stdcall ClearAnalogChannel(long Channel); 
    FUNCTION __stdcall ClearAllAnalog();
    FUNCTION __stdcall SetAnalogChannel(long Channel); 
    FUNCTION __stdcall SetAllAnalog();
    FUNCTION __stdcall WriteAllDigital(long Data);
    FUNCTION __stdcall ClearDigitalChannel(long Channel);
    FUNCTION __stdcall ClearAllDigital();
    FUNCTION __stdcall SetDigitalChannel(long Channel);
    FUNCTION __stdcall SetAllDigital();
    FUNCTION bool __stdcall ReadDigitalChannel(long Channel);
    FUNCTION long __stdcall ReadAllDigital();
    FUNCTION long __stdcall ReadCounter(long CounterNr);
    FUNCTION __stdcall ResetCounter(long CounterNr);
    FUNCTION __stdcall SetCounterDebounceTime(long CounterNr, long DebounceTime);

    FUNCTION __stdcall Version();
    FUNCTION long __stdcall SearchDevices();
    FUNCTION long __stdcall SetCurrentDevice(long lngCardAddress);*/
}
