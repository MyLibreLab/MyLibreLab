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



public class ShiftRight extends JVSMain
{
  private Image image;
  
  private VSObject  inA;
  private VSObject inB;
  private VSObject out;

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
    initPins(0,1,0,2);
    setSize(40,25);
    element.jSetInnerBorderVisibility(false);
    element.jSetTopPinsVisible(false);
    element.jSetBottomPinsVisible(false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    element.jInitPins();
    
    setPin(0,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_VARIANT,element.PIN_INPUT);
    setPin(2,ExternalIF.C_VARIANT,element.PIN_INPUT);
    
    element.jSetPinDescription(0,"out");
    element.jSetPinDescription(1,"inA");
    element.jSetPinDescription(2,"inB");
    
    setName("Shift-Right-Bitweise");

  }

  public void initInputPins()
  {
    inA=(VSObject)element.getPinInputReference(1);
    inB=(VSObject)element.getPinInputReference(2);
  }

  public void initOutputPins()
  {
    out=element.jCreatePinDataType(0);
    element.setPinOutputReference(0,out);
  }
  
  public void checkPinDataType()
  {
    boolean pinInA=element.hasPinWire(1);
    boolean pinInB=element.hasPinWire(2);

    int dtA=element.C_VARIANT;
    int dtB=element.C_VARIANT;
    int dtC=element.C_VARIANT;

    if (pinInA==true) dtA=element.jGetPinDrahtSourceDataType(1);
    if (pinInB==true) dtB=element.jGetPinDrahtSourceDataType(2);

    element.jSetPinDataType(1,dtA);
    element.jSetPinDataType(2,dtB);
    
    dtC=element.C_VARIANT;
    if (dtA == element.C_INTEGER && dtB == element.C_INTEGER) dtC=element.C_INTEGER;
    if (dtA == element.C_BYTE    && dtB == element.C_BYTE) dtC=element.C_BYTE;


    element.jSetPinDataType(0,dtC);

    element.jRepaint();
  }


  //double a,b;

  public void process()
  {
  
    if (inA instanceof VSInteger && inB instanceof VSInteger)
    {
      VSInteger valA=(VSInteger)inA;
      VSInteger valB=(VSInteger)inB;
      VSInteger outX=(VSInteger)out;
      outX.setValue(valA.getValue() >> valB.getValue());
    };
    
    /*if (inA instanceof VSDouble && inB instanceof VSDouble)
    {
      VSDouble valA=(VSDouble)inA;
      VSDouble valB=(VSDouble)inB;
      VSDouble outX=(VSDouble)out;
      outX.setValue(valA.getValue() & valB.getValue());
    }; */
    
    if (inA instanceof VSByte && inB instanceof VSByte)
    {
      VSByte valA=(VSByte)inA;
      VSByte valB=(VSByte)inB;
      VSByte outX=(VSByte)out;
      short a=toSigned(valA.getValue());
      short b=toSigned(valB.getValue());
      byte c=toUnsigned( (short) (a >> b));
      outX.setValue( c);
    };

    
    //if (inA == null) a=0;
    //if (inB == null) b=0;
  
    /*if (inA instanceof VSDouble)
    {
      VSDouble valA=(VSDouble)inA;
      a=valA.getValue();
    };
    
    if (inB instanceof VSDouble)
    {
      VSDouble valB=(VSDouble)inB;
      b=valB.getValue();
    };
    

    if (inB instanceof VSInteger)
    {
      VSInteger valB=(VSInteger)inB;
      b=valB.getValue();
    };
    
    if (inA instanceof VSByte)
    {
      VSByte valA=(VSByte)inA;
      a=toSigned(valA.getValue());
    };

    if (inB instanceof VSByte)
    {
      VSByte valB=(VSByte)inB;
      b=toSigned(valB.getValue());
    };


    if (out instanceof VSDouble)
    {
      VSDouble outX=(VSDouble)out;
      outX.setValue(a+b);
    };
    if (out instanceof VSInteger)
    {
      VSInteger outX=(VSInteger)out;
      outX.setValue((int)( a+b ) );
    };
    if (out instanceof VSByte)
    {
      VSByte outX=(VSByte)out;
      outX.setValue(toUnsigned( (short) (a+b) ) );
    };
    */
    element.notifyPin(0);
  }
  

   public static byte toUnsigned(final short value)
   {
        return (byte) (0xFF&value);
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


}

