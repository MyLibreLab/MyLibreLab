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

import tools.*;
import VisualLogic.variables.*;


public class DataLogger extends JVSMain
{
  private ExternalIF panelElement=null;
  private Image image;

  public VSDouble in=null;
  public VSBoolean reset=null;

  public void onDispose()
  {
   image.flush();
   image=null;
  }


  public DataLogger()
  {
    super();
  }
  
  public void paint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }

  public void init()
  {
    initPins(0,0,0,2);
    setSize(40,30);
    initPinVisibility(false,false,false,true);


    setPin(0,ExternalIF.C_DOUBLE,element.PIN_INPUT);
    setPin(1,ExternalIF.C_BOOLEAN,element.PIN_INPUT);

    element.jSetPinDescription(0,"In");
    element.jSetPinDescription(1,"Reset");
    
    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    

    element.jSetCaptionVisible(true);
    element.jSetCaption("Logger");

    setName("DataLogger");
  }


  public void initInputPins()
  {
    in=(VSDouble)element.getPinInputReference(0);
    reset=(VSBoolean)element.getPinInputReference(1);
    
    if (in==null) in=new VSDouble(0);
    if (reset==null) reset=new VSBoolean(false);
  }

  public void initOutputPins()
  {

  }

  public void process()
  {
    if (in!=null && panelElement!=null)
    {
      if (reset!=null && reset.getValue()==true)
      {
        panelElement.jProcessPanel(0,1,in);
      } else
      {
        panelElement.jProcessPanel(0,0,in);
      }

    }
  }
  
  public void start()
  {
    panelElement=element.getPanelElement();
    if (panelElement!=null) panelElement.jRepaint();
  }


    



}
