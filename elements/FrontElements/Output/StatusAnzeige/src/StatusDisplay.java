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
import java.awt.*;
import java.awt.event.*;
import tools.*;


public class StatusDisplay extends JVSMain
{
  private Image image;
  private boolean changed=false;
  private VSObject in;
  private int oldValue;
  private ExternalIF panelElement;
  
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

    setPin(0,ExternalIF.C_INTEGER,element.PIN_INPUT);
    
    image=element.jLoadImage(element.jGetSourcePath()+"StatusDisplay.gif");
    setName("StatusDisplay");
  }
  

  public void initInputPins()
  {
    in=(VSObject)element.getPinInputReference(0);
  }

  public void initOutputPins()
  {
    //element.setPinOutputReference(0,out);
  }

  public void start()
  {
    panelElement=element.getPanelElement();
  }

  public void process()
  {
    if (in instanceof VSInteger)
    {
      VSInteger inX=(VSInteger)in;

      if (in!=null && inX.getValue()!=oldValue)
      {
        if (panelElement!=null)
        {
         panelElement.jProcessPanel(0,inX.getValue(),(Object)this);
        }
        oldValue=inX.getValue();
      }
    }

  }


}

