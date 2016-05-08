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
import javax.swing.*;


public class DFlipFlop extends JVSMain
{
  public VSBoolean inSet;
  public VSBoolean outQ1=new VSBoolean(false);
  public VSBoolean outQ2=new VSBoolean(false);
  
  private boolean setted=false;

  private Image image;


  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }

  public void paint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }


 public void init()
  {
    initPins(0,2,0,1);
    setSize(55,34);
    initPinVisibility(false,true,false,true);

    image=element.jLoadImage(element.jGetSourcePath()+"image.png");

    setPin(0,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    setPin(2,ExternalIF.C_BOOLEAN,element.PIN_INPUT);


    element.jSetPinDescription(0,"Q1");
    element.jSetPinDescription(1,"Q2");
    element.jSetPinDescription(2,"T");

    element.jSetCaptionVisible(true);
    element.jSetCaption("DFLIPFLOP");
    setName("DFLIPFLOP");
    element.jSetInfo("Carmelo Salafia","Open Source 2007","");
  }
  
  

  public void propertyChanged(Object o)
  {
  }

  public void setPropertyEditor()
  {
  }


  public void initInputPins()
  {
    inSet=(VSBoolean)element.getPinInputReference(2);

    if (inSet==null)
    {
      inSet=new VSBoolean(false);
    }
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,outQ1);
    element.setPinOutputReference(1,outQ2);
  }

  public void start()
  {
    setted=false;
    outQ1.setValue(false);
    outQ2.setValue(true);


    outQ1.setChanged(true);
    outQ2.setChanged(true);
    element.notifyPin(0);
    element.notifyPin(1);
  }
  
  public void stop()
  {
  }

  public void process()
  {
    if (inSet.getValue()==true && setted==false)
    {
          outQ1.setValue(true);
          outQ2.setValue(false);

          setted=true;

          outQ1.setChanged(true);
          outQ2.setChanged(true);
          element.notifyPin(0);
          element.notifyPin(1);

    } else
    if (inSet.getValue()==true && setted==true)
    {
          outQ1.setValue(false);
          outQ2.setValue(true);

          setted=false;

          outQ1.setChanged(true);
          outQ2.setChanged(true);
          element.notifyPin(0);
          element.notifyPin(1);
    }

  }






}
