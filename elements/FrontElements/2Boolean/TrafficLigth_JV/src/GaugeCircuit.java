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
  private boolean oldValue2;
  private boolean oldValue3;
  private boolean oldValue4;
  private ExternalIF panelElement;
  private Image image;
  private VSObject in=null;
  private VSObject in2=null;
  private VSObject in3=null;
  private VSObject in4=null;
  
  
  

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
    initPins(0,0,0,4);
    setSize(50,45);
    
    element.jSetInnerBorderVisibility(false);
    element.jSetTopPinsVisible(false);
    element.jSetRightPinsVisible(false);
    element.jSetBottomPinsVisible(false);
    
    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    

    setPin(0,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(1,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(2,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(3,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    element.jSetPinDescription(0,"Red_Enable");
    element.jSetPinDescription(1,"Yellow_Enable");
    element.jSetPinDescription(2,"Green_Enable");
    element.jSetPinDescription(3,"Blinking_Enable");

    element.jSetCaptionVisible(true);
    element.jSetCaption("TrafficLifgt_JV");

    setName("TrafficLifgt_JV");
  }


  public void initInputPins()
  {
    in=(VSObject)element.getPinInputReference(0);
    in2=(VSObject)element.getPinInputReference(1);
    in3=(VSObject)element.getPinInputReference(2);
    in4=(VSObject)element.getPinInputReference(3);
    if(in==null)   in= new VSBoolean(false);
    if(in2==null)  in2= new VSBoolean(false);
    if(in3==null)  in3= new VSBoolean(false);
    if(in4==null)  in4= new VSBoolean(false);
    
    
  }

  public void initOutputPins(){}


  public void checkPinDataType()
  {
    element.jSetPinDataType(0,ExternalIF.C_BOOLEAN);
    element.jSetPinDataType(1,ExternalIF.C_BOOLEAN);
    element.jSetPinDataType(2,ExternalIF.C_BOOLEAN);
    element.jSetPinDataType(3,ExternalIF.C_BOOLEAN);

    element.jRepaint();
  }

public void start()
  {
    panelElement=element.getPanelElement();
    panelElement.jProcessPanel(0,0.0,(Object)this);
    panelElement.jProcessPanel(1,0.0,(Object)this);
    panelElement.jProcessPanel(2,0.0,(Object)this);
    panelElement.jProcessPanel(3,0.0,(Object)this);
  }
  

  
  public void process()
  {
    if (in instanceof VSBoolean)
    {
      VSBoolean inX=(VSBoolean)in;
      
      if ((in!=null) && ((inX.getValue()!=oldValue) ))
      {
        
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
    
    if (in2 instanceof VSBoolean)
    {
      VSBoolean inX=(VSBoolean)in2;
      
      if ((in2!=null) && ((inX.getValue()!=oldValue2) ))
      {
        
        if (inX.getValue())
        {
          if (panelElement!=null) panelElement.jProcessPanel(1,1.0,(Object)this);
        } else
        {
          if (panelElement!=null) panelElement.jProcessPanel(1,0.0,(Object)this);
        }
        panelElement.jRepaint();
        oldValue2=inX.getValue();
      }
    }
    
    if (in3 instanceof VSBoolean)
    {
      VSBoolean inX=(VSBoolean)in3;
      
      if ((in3!=null) && ((inX.getValue()!=oldValue3) ))
      {
        
        if (inX.getValue())
        {
          if (panelElement!=null) panelElement.jProcessPanel(2,1.0,(Object)this);
        } else
        {
          if (panelElement!=null) panelElement.jProcessPanel(2,0.0,(Object)this);
        }
        panelElement.jRepaint();
        oldValue3=inX.getValue();
      }
    }
    
    if (in4 instanceof VSBoolean)
    {
      VSBoolean inX=(VSBoolean)in4;
      
      if ((in4!=null) && ((inX.getValue()!=oldValue4)))
      {
        
        if (inX.getValue())
        {
          if (panelElement!=null) panelElement.jProcessPanel(3,1.0,(Object)this);
        } else
        {
          if (panelElement!=null) panelElement.jProcessPanel(3,0.0,(Object)this);
        }
        panelElement.jRepaint();
        oldValue4=inX.getValue();
      }
    }
    
    
  }
  

}




