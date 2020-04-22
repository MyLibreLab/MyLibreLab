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
import java.util.*;
import java.awt.event.*;
import javax.swing.*;



public class BytesToStrings extends JVSMain
{
  private Image image;
  
  private VS1DByte inBytes;
  private VS1DString outString = new VS1DString(0);


  public void paint(java.awt.Graphics g)
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
    setSize(32+20,32+5);
    
    initPinVisibility(false,true, false,true);

    element.jSetInnerBorderVisibility(true);

    
    setPin(0,ExternalIF.C_ARRAY1D_STRING,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_ARRAY1D_BYTE,element.PIN_INPUT);

    element.jSetPinDescription(0,"HEX List");
    element.jSetPinDescription(1,"Bytes-IN");


    String fileName=element.jGetSourcePath()+"icon.gif";
    image=element.jLoadImage(fileName);

    element.jSetCaptionVisible(false);
    setName("BytesToHex");

  }


  public void initInputPins()
  {
    inBytes=(VS1DByte)element.getPinInputReference(1);
    if (inBytes==null) inBytes=new VS1DByte(0);

  }

  public void initOutputPins()
  {

    element.setPinOutputReference(0,outString);

  }


  public void start()
  {

  }

  public void stop()
  {
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
   * Converts an unsigned short to a signed int.
   * @param value an unsigned short value
   * @return a signed int that represents the unsigned short's value.
   */
   public static int toSigned(final short value)
   {
     return (int)copyBits(value, (byte)16);
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
  
  
   public static byte toUnsigned(final short value)
   {
        return (byte) (0xFF&value);
   }
   
   
  public void sendString(VS1DByte input)
  {
    String str="";
    String[] dest= new String[input.getLength()];
    
    for (int i=0;i<input.getLength();i++)
    {
      //dest[i]=(byte)input.getValue(i);
      
      byte b = input.getValue(i);
      //String xx=""+toSigned(b);
      String xx=""+b;
      dest[i]=xx.toUpperCase();
      //str+=
      
      
      //System.out.println("XXX="+input.getValue(i));
    }
    
    outString.setValues(dest);

    element.notifyPin(0);
  }


  public void elementActionPerformed(ElementActionEvent evt)
  {

    int idx=evt.getSourcePinIndex();
    switch (idx)
    {
      case 1: sendString(inBytes); break;
    }

  }


}

