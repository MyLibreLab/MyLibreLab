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


public class Concat extends JVSMain
{
  private Image image;
  private VSString inA;
  private VSString inB;
  private VSString out=new VSString();
  private String oldValue="";

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
    initPins(0,1,0,2);
    setSize(45,30);

    initPinVisibility(false,true,false,true);

    element.jSetInnerBorderVisibility(true);
    
    image=element.jLoadImage(element.jGetSourcePath()+"icon.png");


    setPin(0,ExternalIF.C_STRING,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
    setPin(2,ExternalIF.C_STRING,element.PIN_INPUT);

    element.jSetPinDescription(0,"=\"AB\"");
    element.jSetPinDescription(1,"\"A\"");
    element.jSetPinDescription(2,"\"B\"");

    setName("Concat");

  }



  public void initInputPins()
  {
    inA=(VSString)element.getPinInputReference(1);
    inB=(VSString)element.getPinInputReference(2);
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }

  public void process()
  {
    if (inA!=null && inB!=null)
    {
      String temp=inA.getValue()+inB.getValue();
      if (temp!=oldValue)
      {
        oldValue=temp;
        out.setValue(temp);
        out.setChanged(true);
        element.notifyPin(0);
      }
    }
  }

}

