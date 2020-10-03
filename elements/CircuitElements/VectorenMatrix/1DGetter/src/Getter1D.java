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


public class Getter1D extends JVSMain
{
  private Image image;
  private VSObject in;
  private VSInteger row;
  private VSBoolean read;
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
    initPins(0,2,0,3);
    setSize(20+32+3,32+20);
    initPinVisibility(false,true,false,true);
    element.jSetInnerBorderVisibility(true);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    setPin(0,ExternalIF.C_VARIANT,element.PIN_OUTPUT);  // Out
    setPin(1,ExternalIF.C_VARIANT,element.PIN_OUTPUT);  // Value
    
    setPin(2,ExternalIF.C_VARIANT,element.PIN_INPUT);   // in
    setPin(3,ExternalIF.C_INTEGER,element.PIN_INPUT);   // row
    setPin(4,ExternalIF.C_BOOLEAN,element.PIN_INPUT);   // read

    String strLocale=Locale.getDefault().toString();

    element.jSetPinDescription(0,"out");
    element.jSetPinDescription(1,"Value");
    element.jSetPinDescription(2,"in");
    element.jSetPinDescription(3,"row");
    element.jSetPinDescription(4,"read");

    element.jSetCaptionVisible(false);
    element.jSetCaption("Getter2D");
    setName("Getter1D");
    
  }


  public void initInputPins()
  {
    in=(VSObject)element.getPinInputReference(2);
    row=(VSInteger)element.getPinInputReference(3);
    read=(VSBoolean)element.getPinInputReference(4);

    
    if (row==null) row=new VSInteger(0);
    if (read==null) read=new VSBoolean(false);
    
  }
  

  public void initOutputPins()
  {
    out=element.jCreatePinDataType(0);
    element.setPinOutputReference(0,out);

    value=element.jCreatePinDataType(1);
    element.setPinOutputReference(1,value);
  }


  public void checkPinDataType()
  {
    boolean pinIn=element.hasPinWire(2);

    int dt=element.C_VARIANT;

    if (pinIn==true) dt=element.jGetPinDrahtSourceDataType(2);

    element.jSetPinDataType(2,dt);
    element.jSetPinIO(2, element.PIN_INPUT);

    element.jSetPinDataType(0,dt);
    element.jSetPinIO(0, element.PIN_OUTPUT);

    if (dt==element.C_ARRAY1D_STRING)
    {
      element.jSetPinDataType(1,element.C_STRING);
      element.jSetPinIO(1, element.PIN_OUTPUT);
    }
    if (dt==element.C_ARRAY1D_BOOLEAN)
    {
      element.jSetPinDataType(1,element.C_BOOLEAN);
      element.jSetPinIO(1, element.PIN_OUTPUT);
    }
    if (dt==element.C_ARRAY1D_INTEGER)
    {
      element.jSetPinDataType(1,element.C_INTEGER);
      element.jSetPinIO(1, element.PIN_OUTPUT);
    }
    if (dt==element.C_ARRAY1D_BYTE)
    {
      element.jSetPinDataType(1,element.C_BYTE);
      element.jSetPinIO(1, element.PIN_OUTPUT);
    }
    if (dt==element.C_ARRAY1D_DOUBLE)
    {
      element.jSetPinDataType(1,element.C_DOUBLE);
      element.jSetPinIO(1, element.PIN_OUTPUT);
    }
    element.jRepaint();
  }


  public void process()
  { 
    if (in==null) return;
    //out.copyReferenceFrom(in);

    //if (read.getValue())
    //{
      int r=row.getValue();
      
      if (in instanceof VS1DString)
      {
        VS1DString inX=(VS1DString)in;
        
        if (r>=inX.getLength()) element.jException("Out of Bounds!");
        String val=inX.getValue(r);
        VSString outValue = (VSString)value;
        outValue.setValue(val);
        outValue.setChanged(true);
        //element.jConsolePrintln("Proccess"+ val);
      }
      if (in instanceof VS1DBoolean)
      {
        VS1DBoolean inX=(VS1DBoolean)in;

        if (r>=inX.getLength()) element.jException("Out of Bounds!");
        boolean val=inX.getValue(r);
        
        VSBoolean outValue = (VSBoolean)value;
        outValue.setValue(val);
      }
      if (in instanceof VS1DInteger)
      {
        VS1DInteger inX=(VS1DInteger)in;

        if (r>=inX.getLength()) element.jException("Out of Bounds!");
        int val=inX.getValue(r);

        VSInteger outValue = (VSInteger)value;
        outValue.setValue(val);
      }
      if (in instanceof VS1DByte)
      {
        VS1DByte inX=(VS1DByte)in;

        if (r>=inX.getLength()) element.jException("Out of Bounds!");
        byte val=inX.getValue(r);

        VSByte outValue = (VSByte)value;
        outValue.setValue(val);
      }
      if (in instanceof VS1DDouble)
      {
        VS1DDouble inX=(VS1DDouble)in;

        if (r>=inX.getLength()) element.jException("Out of Bounds!");
        double val=inX.getValue(r);
        
        VSDouble outValue = (VSDouble)value;
        outValue.setValue(val);
      }
      element.notifyPin(1);
    //}
    element.notifyPin(0);
  }
  
}

