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


import VisualLogic.*;
import VisualLogic.variables.*;
import tools.*;

import java.awt.*;
import java.awt.event.*;


public class ByteToInt extends JVSMain
{
  private Image image;

  private VSByte in;
  private VSInteger out= new VSInteger();

  public void xpaint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }

  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }
  
  public void init()
  {
    initPins(0,1,0,1);
    setSize(40,25);

    element.jSetInnerBorderVisibility(false);
    element.jSetTopPinsVisible(false);
    element.jSetBottomPinsVisible(false);
    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");

    setPin(0,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_BYTE,element.PIN_INPUT);

    setName("Byte2Int");

  }


  public void initInputPins()
  {
    in=(VSByte)element.getPinInputReference(1);
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }



  /**
  * Converts an unsigned byte to a signed short.
  * @param value an unsigned byte value
  * @return a signed short that represents the unsigned byte's value.
  */
  public static short toSigned(byte value)
  {
    return (short)copyBits(value, (byte)8);
  }

   /**
   * Returns a long that contains the same n bits as the given long,with cleared upper rest.
   * @param value the value which lowest bits should be copied.
   * @param bits the number of lowest bits that should be copied.
   * @return a long value that shares the same low bits as the given value.
   */
   private static long copyBits(final long value, byte bits)
   {
     final boolean logging = false; //turn off logging here
     long converted = 0;
     long comp =1L << bits;
     while (--bits != -1)
     {
       if(((comp >>= 1) & value) != 0)
       {
          converted |= comp;
       }
       if(logging){System.out.print((comp & value)!=0?"1":"0");}
     }
       if(logging){System.out.println();
     }
      return converted;
  }


  public void process()
  {

    if (in instanceof VSByte && out instanceof VSInteger)
    {
        out.setValue(toSigned(in.getValue()) );
        element.notifyPin(0);
    }

  }


}

