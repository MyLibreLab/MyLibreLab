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



public class AnalogAnzeige extends JVSMain
{
  private ExternalIF panelElement;
  private Image image;
  private double oldValue;
  private VSDouble in=null;

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
    setSize(40,30);

    initPinVisibility(false,false,false,true);

      
    setPin(0,ExternalIF.C_DOUBLE,element.PIN_INPUT);
    element.jSetPinDescription(0,"In");

    //panelElement=element.setPanelElement("AnalogAnzeigePanel");

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    setName("Analog Anzeige");
  }

  public void initInputPins()
  {
    in=(VSDouble)element.getPinInputReference(0);
  }

  public void initOutputPins()
  {

  }

  public void process()
  {

    if (in!=null && in.getValue()!=oldValue)
    {
      panelElement=element.getPanelElement();
      if (panelElement!=null)
      {
         panelElement.jProcessPanel(0,in.getValue(),(Object)this);
         panelElement.jRepaint();
      }
      oldValue=in.getValue();
    }

  }

}
 
