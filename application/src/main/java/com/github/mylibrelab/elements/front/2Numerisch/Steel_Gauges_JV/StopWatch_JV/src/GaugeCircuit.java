//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia (cswi@gmx.de)                         *
//* Copyright (C) 2017  Javier Velásquez (javiervelasquez125@gmail.com)                                                                           *
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
import java.text.*;
import java.awt.geom.Rectangle2D;

public class GaugeCircuit extends JVSMain
{
  private boolean oldValue;
  private ExternalIF panelElement;
  private Image image;
  private VSObject in=null;
  private VSString out= new VSString("0");
  private VSInteger Minutes= new VSInteger(0);
  private VSInteger Seconds= new VSInteger(0);
  private VSInteger Millis= new VSInteger(0);
  private boolean firstTime=false;
  private VSString val;
  private boolean changed=false;
  
  

  public void onDispose()
  {
   image.flush();
   image=null;
  }

   public void paint(java.awt.Graphics g)
  {
    if (image!=null)drawImageCentred(g,image);
  }
  public void init()
  {
    initPins(0,4,0,1);
    setSize(50,45);
    
    element.jSetInnerBorderVisibility(false);
    element.jSetTopPinsVisible(false);
    element.jSetRightPinsVisible(true);
    element.jSetLeftPinsVisible(true);
    element.jSetBottomPinsVisible(false);
    
    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    

    
    setPin(0,ExternalIF.C_STRING,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
    setPin(2,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
    setPin(3,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
    setPin(4,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    
    element.jSetPinDescription(0,"out MIN:SEG:MILLIS");
    element.jSetPinDescription(1,"out MIN");
    element.jSetPinDescription(2,"out SEG");
    element.jSetPinDescription(3,"out MILLIS");
    element.jSetPinDescription(4,"In Run/Stop");

    element.jSetCaptionVisible(true);
    element.jSetCaption("StopWatch_JV");

    setName("StopWatch_JV");
  }

   public void changePin(int pinIndex, Object value)
  {
    changed=true;
    
    if(pinIndex==0){
        val=(VSString)value;
        if (val!=null)
        {
          out.setValue(val.getValue());
          element.notifyPin(0);
        }
    }else{
    try{
    
        VSInteger val2=(VSInteger) value;
        if (val2!=null && pinIndex==1)
        { Minutes.setValue(val2.getValue());
          element.notifyPin(1);
        }
        if (val2!=null && pinIndex==2)
        { Seconds.setValue(val2.getValue());
          element.notifyPin(2);
        }
        if (val2!=null && pinIndex==3)
        { Millis.setValue(val2.getValue());
          element.notifyPin(3);
        }  
        
    }catch(Exception e){
        System.out.println("Error:"+e);
    }
        
    }
    
  }

  public void initInputPins()
  {
    in=(VSBoolean)element.getPinInputReference(4);
  }

  public void initOutputPins(){
  
      element.setPinOutputReference(0,out);
      element.setPinOutputReference(1,Minutes);
      element.setPinOutputReference(2,Seconds);
      element.setPinOutputReference(3,Millis);

  
  }


  public void checkPinDataType()
  {
    
    element.jSetPinDataType(0,element.C_STRING);
    element.jSetPinDataType(1,element.C_INTEGER);
    element.jSetPinDataType(2,element.C_INTEGER);
    element.jSetPinDataType(3,element.C_INTEGER);
    element.jSetPinDataType(4,element.C_BOOLEAN);

    element.jRepaint();
  }

public void start()
  {
    firstTime=true;
    panelElement=element.getPanelElement();
    panelElement.jProcessPanel(0,0.0,(Object)this);
  }
  

  
  public void process()
  {
    if (in instanceof VSBoolean)
    {
      VSBoolean inX=(VSBoolean)in;
      
      if ((in!=null) && ((inX.getValue()!=oldValue) || (firstTime)))
      {
        firstTime=false;
        if (inX.getValue())
        {
          if (panelElement!=null) panelElement.jProcessPanel(0,1.0,(Object)this);
        } else
        {
          if (panelElement!=null) panelElement.jProcessPanel(0,0.0,(Object)this);
        }
        panelElement.jRepaint();
        oldValue=inX.getValue();
      }
    }
  }
  

}




