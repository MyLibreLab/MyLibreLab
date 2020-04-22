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
import java.util.*;
import java.awt.event.*;
import javax.swing.*;



public class BytesToString extends JVSMain
{
  private Image image;
  
  private VS1DByte inBytes;
  private VSString outString = new VSString("");


  public void paint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }
  
  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }

  public void init()
  {
    initPins(0,1,0,1);
    setSize(32+20,32+5);
    
    initPinVisibility(false,true, false,true);

    element.jSetInnerBorderVisibility(true);

    
    setPin(0,ExternalIF.C_STRING,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_ARRAY1D_BYTE,element.PIN_INPUT);

    element.jSetPinDescription(0,"String-OUT");
    element.jSetPinDescription(1,"Bytes-IN");


    String fileName=element.jGetSourcePath()+"icon.gif";
    image=element.jLoadImage(fileName);

    element.jSetCaptionVisible(false);
    setName("BytesToString");

  }


  public void initInputPins()
  {
    inBytes=(VS1DByte)element.getPinInputReference(1);

  }

  public void initOutputPins()
  {

    element.setPinOutputReference(0,outString);

  }


  public void start()
  {

  }

  public void stop()
  {
  }
  
  public void sendString(VS1DByte input)
  {
  
    byte[] dest= input.getValues();

    outString.setValue(new String(dest));

    element.notifyPin(0);
  }


  public void elementActionPerformed(ElementActionEvent evt)
  {

    int idx=evt.getSourcePinIndex();
    switch (idx)
    {
      case 1: sendString(inBytes); break;
    }

  }


}

