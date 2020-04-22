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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.sun.jna.*; 
import com.sun.jna.win32.StdCallLibrary;

public interface K8047d extends StdCallLibrary
{         
    K8047d INSTANCE = (K8047d) Native.loadLibrary("K8047d", K8047d.class);

    public void StartDevice();
    public void StopDevice();
    public void LEDon();
    public void LEDoff();
    public void nix();
    public void SetGain(int channel, int gain);
    public void ReadData(int data[]);
    
}
