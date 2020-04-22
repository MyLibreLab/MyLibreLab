//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia (cswi@gmx.de)                         *
//* Copyright (C) 2017  Javier Velásquez (javiervelasquez125@gmail.com)                                                                          *
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


public class Oscilloscope extends JVSMain
{
  private ExternalIF panelElement=null;
  private Image image;

  public VSGroup inA=null;
  public VSGroup inB=null;
  public VSGroup inC=null;
  public VSDouble inD=null;


  public void onDispose()
  {
   image.flush();
   image=null;
  }


  public Oscilloscope()
  {
    super();
  }
  
  public void paint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }

  public void init()
  {
    initPins(0,0,0,4);
    setSize(40,50);
    initPinVisibility(false,false,false,true);

    setPin(0,ExternalIF.C_GROUP,element.PIN_INPUT);
    setPin(1,ExternalIF.C_GROUP,element.PIN_INPUT);
    setPin(2,ExternalIF.C_GROUP,element.PIN_INPUT);
    setPin(3,ExternalIF.C_DOUBLE,element.PIN_INPUT);

    element.jSetPinDescription(0,"Values (X,Y)");
    element.jSetPinDescription(1,"Colors");
    element.jSetPinDescription(2,"Rendertype");
    element.jSetPinDescription(3,"X-Pos.");
    
    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    

    element.jSetCaptionVisible(true);
    element.jSetCaption("osc");

    setName("Oscilloscope-X/Y_Version 3.0 JV");
  }


  public void initInputPins()
  {
    inA=(VSGroup)element.getPinInputReference(0);
    inB=(VSGroup)element.getPinInputReference(1);
    inC=(VSGroup)element.getPinInputReference(2);
    inD=(VSDouble)element.getPinInputReference(3);
    
    if (inA==null) inA=new VSGroup();
    if (inB==null) inB=new VSGroup();
    if (inC==null) inC=new VSGroup();
    if (inD==null) inD=new VSDouble(0);

  }

  public void initOutputPins()
  {

  }

  public void process()
  {
    if (panelElement!=null)
    {
      panelElement.jProcessPanel(0,0,inA);
      panelElement.jProcessPanel(1,0,inB);
      panelElement.jProcessPanel(2,0,inC);
      panelElement.jProcessPanel(3,0,inD);
    }
  }
  
  public void start()
  {
    panelElement=element.getPanelElement();
    if (panelElement!=null) panelElement.jRepaint();
  }


    



}
