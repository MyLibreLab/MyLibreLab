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

public class LED extends JVSMain
{
  private ExternalIF panelElement;
  private Image image;
  private boolean oldValue;
  private boolean firstTime=false;
  
  private VSObject in;
  
  public void paint(java.awt.Graphics g)
  {
    if (image!=null)drawImageCentred(g,image);
  }

  public void onDispose()
  {
   image.flush();
   image=null;
  }


  public void init()
  {
    element.jSetInnerBorderVisibility(true);
    initPins(0,0,0,1);
    setSize(40,30);
    initPinVisibility(false,false,false,true);
    
    setPin(0,ExternalIF.C_BOOLEAN,element.PIN_INPUT);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    setName("led");
    
    element.jSetResizable(false);
  }
  
  public void initInputPins()
  {
    in=(VSObject)element.getPinInputReference(0);
  }

  public void initOutputPins()
  {

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
 
