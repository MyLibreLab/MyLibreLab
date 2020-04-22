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
  private double oRed, oBlue, oGreen;
  
  private VSInteger inR;
  private VSInteger inG;
  private VSInteger inB;
  
  
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
    initPins(0,0,0,3);
    setSize(40,35);
    
    initPinVisibility(false,false,false,true);
    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    setPin(0,ExternalIF.C_INTEGER,element.PIN_INPUT);
    setPin(1,ExternalIF.C_INTEGER,element.PIN_INPUT);
    setPin(2,ExternalIF.C_INTEGER,element.PIN_INPUT);
    element.jSetPinDescription(0,"red");
    element.jSetPinDescription(1,"green");
    element.jSetPinDescription(2,"blue");
    setName("RGB-LED");
  }
  
  public void initInputPins()
  {
    inR=(VSInteger)element.getPinInputReference(0);
    inG=(VSInteger)element.getPinInputReference(1);
    inB=(VSInteger)element.getPinInputReference(2);
    
    if (inR==null) inR=new VSInteger(0);
    if (inG==null) inG=new VSInteger(0);
    if (inB==null) inB=new VSInteger(0);
  }

  public void start()
  {
    panelElement=element.getPanelElement();
  }


  public void process()
  {
  
    if (inR!=null && inG!=null && inB!=null)
    {
      if (inR.getValue()!=oRed || inG.getValue()!=oGreen || inB.getValue()!=oBlue)
      {

        if (panelElement!=null) panelElement.jProcessPanel(0,0.0,new Color((char)inR.getValue(),(char)inG.getValue(),(char)inB.getValue()));
        oRed=inR.getValue();
        oGreen=inG.getValue();
        oBlue=inB.getValue();
      }
    }

  }

}
 
