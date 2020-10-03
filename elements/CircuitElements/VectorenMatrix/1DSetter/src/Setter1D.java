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


public class Setter1D extends JVSMain
{
  private Image image;
  private VSObject in;
  private VSInteger row;
  private VSBoolean store;
  private VSObject value;
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
    initPins(0,1,0,4);
    setSize(20+32+3,32+20);
    initPinVisibility(false,true,false,true);
    element.jSetInnerBorderVisibility(true);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    setPin(0,ExternalIF.C_VARIANT,element.PIN_OUTPUT);  // Out
    setPin(1,ExternalIF.C_VARIANT,element.PIN_INPUT);   // in
    setPin(2,ExternalIF.C_INTEGER,element.PIN_INPUT);   // row
    setPin(3,ExternalIF.C_VARIANT,element.PIN_INPUT);   // value
    setPin(4,ExternalIF.C_BOOLEAN,element.PIN_INPUT);   // store

    String strLocale=Locale.getDefault().toString();

    element.jSetPinDescription(0,"out");
    element.jSetPinDescription(1,"in");
    element.jSetPinDescription(2,"row");
    element.jSetPinDescription(3,"value");
    element.jSetPinDescription(4,"store");

    element.jSetCaptionVisible(false);
    element.jSetCaption("Setter1D");
    setName("Setter1D");
  }


  public void initInputPins()
  {
    in=(VSObject)element.getPinInputReference(1);
    row=(VSInteger)element.getPinInputReference(2);
    value=(VSObject)element.getPinInputReference(3);
    store=(VSBoolean)element.getPinInputReference(4);

    
    if (row==null) row=new VSInteger(0);
    if (store==null) store=new VSBoolean(false);

  }
  

  public void initOutputPins()
  {

    out=element.jCreatePinDataType(0);
    element.setPinOutputReference(0,out);

    if (out instanceof VS1DString)
    {
      value = new VSString();
    }
    if (out instanceof VS1DBoolean)
    {
      value = new VSBoolean(false);
    }
    if (out instanceof VS1DInteger)
    {
      value = new VSInteger(0);
    }
    if (out instanceof VS1DByte)
    {
      value = new VS1DByte(0);
    }
    if (out instanceof VS1DDouble)
    {
      value = new VSDouble(0);
    }
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
      element.jSetPinDataType(3,element.C_STRING);
      element.jSetPinIO(3, element.PIN_INPUT);
    }
    if (dt==element.C_ARRAY1D_BOOLEAN)
    {
      element.jSetPinDataType(3,element.C_BOOLEAN);
      element.jSetPinIO(3, element.PIN_INPUT);
    }
    if (dt==element.C_ARRAY1D_INTEGER)
    {
      element.jSetPinDataType(3,element.C_INTEGER);
      element.jSetPinIO(3, element.PIN_INPUT);
    }
    if (dt==element.C_ARRAY1D_BYTE)
    {
      element.jSetPinDataType(3,element.C_BYTE);
      element.jSetPinIO(3, element.PIN_INPUT);
    }
    if (dt==element.C_ARRAY1D_DOUBLE)
    {
      element.jSetPinDataType(3,element.C_DOUBLE);
      element.jSetPinIO(3, element.PIN_INPUT);
    }
    element.jRepaint();
  }

  public void start()
  {
  }
  
  public void process()
  {
    //if (in!=null) System.out.println("in="+in.toString());

    
    if (in==null) return;
    

    out.copyReferenceFrom(in);

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
    }
    element.notifyPin(0);
  }
  
}

