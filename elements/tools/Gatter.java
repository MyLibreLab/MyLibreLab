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

package tools;

import VisualLogic.*;
import VisualLogic.variables.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import tools.*;

public class Gatter extends JVSMain
{
  private int anzPins=2;
  private int width, height;
  private Image image;
  private String name="";

  public VSBoolean inA;
  public VSBoolean inB;
  public VSBoolean out=new VSBoolean();
  
  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }


  public Gatter(int anzPins, String name)
  {
    this.anzPins=anzPins;
    this.name=name;
    width=50;
    height=25;
  }

  public void paint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }

  public void init()
  {
    image=element.jLoadImage(element.jGetSourcePath()+"icon.png");
    
    initPins(0,1,0,anzPins);
    initPinVisibility(false,true,false,true);
    width=image.getWidth(null)+22;
    setSize(width,height);

    element.jSetInnerBorderVisibility(false);

    setPin(0,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);

    for (int i=1;i<=anzPins;i++)
    {
      setPin(i,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    }

    setName(name);
  }

  public void initInputPins()
  {
    if (anzPins>=1)inA=(VSBoolean)element.getPinInputReference(1);
    if (anzPins==2)inB=(VSBoolean)element.getPinInputReference(2);
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }


}
 
