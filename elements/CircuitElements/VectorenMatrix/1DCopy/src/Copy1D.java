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
import java.util.*;
import javax.swing.*;


public class Copy1D extends JVSMain
{
  private Image image;
  
  private VSObject out;
  private VSObject value;
  
  private VSObject  dest;
  private VSInteger destPos;
  private VSInteger destLen;

  private VSObject source;
  private VSInteger sourcePos;

  private VSBoolean copy;
  

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
    initPins(0,1,0,6);
    setSize(20+32+3,32+40);
    initPinVisibility(false,true,false,true);
    element.jSetInnerBorderVisibility(true);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    setPin(0,ExternalIF.C_VARIANT,element.PIN_OUTPUT);  // Out
    
    setPin(1,ExternalIF.C_VARIANT,element.PIN_INPUT);   // Source
    setPin(2,ExternalIF.C_INTEGER,element.PIN_INPUT);   // Source Begin
    setPin(3,ExternalIF.C_VARIANT,element.PIN_INPUT);   // Dest
    setPin(4,ExternalIF.C_INTEGER,element.PIN_INPUT);   // Dest Begin
    setPin(5,ExternalIF.C_INTEGER,element.PIN_INPUT);   // Dest Len
    setPin(6,ExternalIF.C_BOOLEAN,element.PIN_INPUT);   // copy

    String strLocale=Locale.getDefault().toString();

    element.jSetPinDescription(0,"out");
    
    element.jSetPinDescription(1,"Source");
    element.jSetPinDescription(2,"SourcePos");
    element.jSetPinDescription(3,"Dest ");
    element.jSetPinDescription(4,"DestPos");
    element.jSetPinDescription(5,"SourceLength");
    element.jSetPinDescription(6,"Copy");

    element.jSetCaptionVisible(false);
    element.jSetCaption("Copy1D");
    setName("Copy1D");
  }


  public void initInputPins()
  {
    source=(VSObject)element.getPinInputReference(1);
    sourcePos=(VSInteger)element.getPinInputReference(2);
    dest=(VSObject)element.getPinInputReference(3);
    destPos=(VSInteger)element.getPinInputReference(4);
    destLen=(VSInteger)element.getPinInputReference(5);

    copy=(VSBoolean)element.getPinInputReference(6);

    if (sourcePos==null) sourcePos=new VSInteger(0);
    if (destPos==null) destPos=new VSInteger(0);
    if (destLen==null) destLen=new VSInteger(0);
    if (copy==null) copy=new VSBoolean(false);
  }
  

  public void initOutputPins()
  {

    out=element.jCreatePinDataType(0);
    element.setPinOutputReference(0,out);

    /*if (out instanceof VS1DString)
    {
      source = new VS1DString(0);
    }
    if (out instanceof VS1DBoolean)
    {
      source = new VS1DBoolean(0);
    }
    if (out instanceof VS1DInteger)
    {
      source = new VS1DInteger(0);
    }
    if (out instanceof VS1DByte)
    {
      source = new VS1DByte(0);
    }
    if (out instanceof VS1DDouble)
    {
      source = new VS1DDouble(0);
    } */
  }


  public void checkPinDataType()
  {
    boolean pinIn=element.hasPinWire(1);

    int dt=element.C_VARIANT;

    if (pinIn==true) dt=element.jGetPinDrahtSourceDataType(1);

    element.jSetPinDataType(1,dt);
    element.jSetPinIO(1, element.PIN_INPUT);

    element.jSetPinDataType(0,dt);
    element.jSetPinIO(0, element.PIN_OUTPUT);

    if (dt==element.C_ARRAY1D_STRING)
    {
      element.jSetPinDataType(3,element.C_ARRAY1D_STRING);
      element.jSetPinIO(3, element.PIN_INPUT);
    }
    if (dt==element.C_ARRAY1D_BOOLEAN)
    {
      element.jSetPinDataType(3,element.C_ARRAY1D_BOOLEAN);
      element.jSetPinIO(3, element.PIN_INPUT);
    }
    if (dt==element.C_ARRAY1D_INTEGER)
    {
      element.jSetPinDataType(3,element.C_ARRAY1D_INTEGER);
      element.jSetPinIO(3, element.PIN_INPUT);
    }
    if (dt==element.C_ARRAY1D_BYTE)
    {
      element.jSetPinDataType(3,element.C_ARRAY1D_BYTE);
      element.jSetPinIO(3, element.PIN_INPUT);
    }
    if (dt==element.C_ARRAY1D_DOUBLE)
    {
      element.jSetPinDataType(3,element.C_ARRAY1D_DOUBLE);
      element.jSetPinIO(3, element.PIN_INPUT);
    }
    element.jRepaint();
  }

  public void start()
  {
  }
  
  
  public void mcCopy()
  {

    if (source instanceof VS1DString)
    {
      VS1DString src=(VS1DString)source;
      VS1DString dst=(VS1DString)dest;

      String[] srcValues=src.getValues();
      String[] dstValues=dst.getValues();

      VS1DString outX=(VS1DString)out;

      System.arraycopy(srcValues, sourcePos.getValue(), dstValues, destPos.getValue(), destLen.getValue());

      outX.setValues(dstValues);
      element.notifyPin(0);
    }

    if (source instanceof VS1DBoolean)
    {
      VS1DBoolean src=(VS1DBoolean)source;
      VS1DBoolean dst=(VS1DBoolean)dest;

      boolean[] srcValues=src.getValues();
      boolean[] dstValues=dst.getValues();

      VS1DBoolean outX=(VS1DBoolean)out;

      System.arraycopy(srcValues, sourcePos.getValue(), dstValues, destPos.getValue(), destLen.getValue());

      outX.setValues(dstValues);
      element.notifyPin(0);
    }
    if (source instanceof VS1DByte)
    {
      VS1DByte src=(VS1DByte)source;
      VS1DByte dst=(VS1DByte)dest;

      byte[] srcValues=src.getValues();
      byte[] dstValues=dst.getValues();
      
      VS1DByte outX=(VS1DByte)out;

      System.arraycopy(srcValues, sourcePos.getValue(), dstValues, destPos.getValue(), destLen.getValue());
      
      outX.setValues(dstValues);
      element.notifyPin(0);
    }
    
    if (source instanceof VS1DInteger)
    {
      VS1DInteger src=(VS1DInteger)source;
      VS1DInteger dst=(VS1DInteger)dest;

      int[] srcValues=src.getValues();
      int[] dstValues=dst.getValues();

      VS1DInteger outX=(VS1DInteger)out;

      System.arraycopy(srcValues, sourcePos.getValue(), dstValues, destPos.getValue(), destLen.getValue());

      outX.setValues(dstValues);
      element.notifyPin(0);
    }
    

    if (source instanceof VS1DDouble)
    {
      VS1DDouble src=(VS1DDouble)source;
      VS1DDouble dst=(VS1DDouble)dest;

      double[] srcValues=src.getValues();
      double[] dstValues=dst.getValues();

      VS1DDouble outX=(VS1DDouble)out;

      System.arraycopy(srcValues, sourcePos.getValue(), dstValues, destPos.getValue(), destLen.getValue());

      outX.setValues(dstValues);
      element.notifyPin(0);
    }


  }
  
  
  public void process()
  {

    if (dest==null) return;
    if (source==null) return;
    
    if (copy.getValue()) mcCopy();
    

    /*out.copyReferenceFrom(in);

    if (store.getValue() && value!=null)
    {
      int r=row.getValue();

      if (out instanceof VS1DString)
      {
        VS1DString outX=(VS1DString)out;

        if (r>=outX.getLength()) element.jException("Out of Bounds!");

        VSString inValue = (VSString)value;
        outX.setValue(r,inValue.getValue());
      }
      if (out instanceof VS1DBoolean)
      {
        VS1DBoolean outX=(VS1DBoolean)out;

        if (r>=outX.getLength()) element.jException("Out of Bounds!");
        
        VSBoolean inValue = (VSBoolean)value;
        outX.setValue(r,inValue.getValue());
      }
      if (out instanceof VS1DInteger)
      {
        VS1DInteger outX=(VS1DInteger)out;

        if (r>=outX.getLength()) element.jException("Out of Bounds!");

        VSInteger inValue = (VSInteger)value;
        outX.setValue(r,inValue.getValue());
      }
      if (out instanceof VS1DByte)
      {
        VS1DByte outX=(VS1DByte)out;

        System.out.println("out=VS1DByte");

        if (r>=outX.getLength()) element.jException("Out of Bounds!");

        VSByte inValue = (VSByte)value;
        outX.setValue(r,inValue.getValue());
      }
      if (out instanceof VS1DDouble)
      {
        VS1DDouble outX=(VS1DDouble)out;

        if (r>=outX.getLength()) element.jException("Out of Bounds!");

        VSDouble inValue = (VSDouble)value;
        
        outX.setValue(r,inValue.getValue());
      }
    }     */

  }
  
}

