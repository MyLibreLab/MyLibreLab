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


public class XCos extends JVSMain
{
  private Image image;
  private VSDouble in;
  private VSDouble out= new VSDouble();

  public void xpaint(java.awt.Graphics g)
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
    setSize(40,30);
    initPinVisibility(false,true,false,true);
    element.jSetInnerBorderVisibility(false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");

    setPin(0,ExternalIF.C_DOUBLE,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_DOUBLE,element.PIN_INPUT);

    element.jSetPinDescription(0,"f(x)");
    element.jSetPinDescription(1,"x");

    element.jSetCaptionVisible(true);
    element.jSetCaption("cos");

    setName("cos");
  }

  public void initInputPins()
  {
    in=(VSDouble)element.getPinInputReference(1);
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }

  public void process()
  {
    if (in instanceof VSDouble)
    {
        out.setValue(Math.cos(in.getValue()));
        out.setChanged(true);
        element.notifyPin(0);
    }

  }
}

