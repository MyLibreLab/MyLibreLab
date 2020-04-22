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
  private double oldValue;
  private ExternalIF panelElement;
  private Image image;
  private VSObject in=null;
  

  public void onDispose()
  {
   image.flush();
   image=null;
  }

   public void paint(java.awt.Graphics g)
   {
      drawImageCentred(g,image);
   }

  public void init()
  {
    initPins(0,0,0,1);
    setSize(50,45);
    
    element.jSetInnerBorderVisibility(false);
    element.jSetTopPinsVisible(false);
    element.jSetRightPinsVisible(false);
    element.jSetBottomPinsVisible(false);
    
    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    

    setPin(0,ExternalIF.C_VARIANT,element.PIN_INPUT);
    element.jSetPinDescription(0,"in");

    element.jSetCaptionVisible(true);
    element.jSetCaption("Compass_JV");

    setName("Compass_JV");
  }


  public void initInputPins()
  {
    in=(VSObject)element.getPinInputReference(0);
  }

  public void initOutputPins(){}


  public void checkPinDataType()
  {
    boolean pinIn=element.hasPinWire(0);

    int dt=element.C_VARIANT;

    if (pinIn==true)
    {
      dt=element.jGetPinDrahtSourceDataType(0);
      
      if (dt == element.C_DOUBLE  ) dt=element.C_DOUBLE;else
      if (dt == element.C_INTEGER ) dt=element.C_INTEGER;else
      if (dt == element.C_BYTE    ) dt=element.C_BYTE;else
      dt=element.C_DOUBLE;
    }

    element.jSetPinDataType(0,dt);

    element.jRepaint();
  }



  public void process()
  {
    if (in!=null)
    {
      double value=0;

      if (in instanceof VSDouble)
      {
        VSDouble val = (VSDouble)in;
        value=val.getValue();
      }else
      if (in instanceof VSInteger)
      {
        VSInteger val = (VSInteger)in;
        value=(double)val.getValue();
      }else
      if (in instanceof VSByte)
      {
        VSByte val = (VSByte)in;
        value=(double)val.toSigned(val.getValue());
      }

      if (value!=oldValue)
      {
        panelElement=element.getPanelElement();
        if (panelElement!=null)
        {
           panelElement.jProcessPanel(0,value,(Object)this);
           panelElement.jRepaint();
        }
        oldValue=value;
      }

    }

  }


}

