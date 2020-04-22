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
import java.awt.geom.*;
import javax.swing.*;
import java.text.*;
import java.awt.geom.Rectangle2D;

import VisualLogic.variables.*;
import tools.*;

public class MyTable extends JVSMain
{
    private Image image;
    private VS2DString in;
    private VS2DString out = new VS2DString(1,1);
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
    initPins(0,1,0,1);
    setSize(50,40);

    initPinVisibility(false,true,false,true);
    image=element.jLoadImage(element.jGetSourcePath()+"icon.png");

    setPin(0,ExternalIF.C_ARRAY2D_STRING,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_ARRAY2D_STRING,element.PIN_INPUT);

    setName("Table-Version-1.0");
  }

  public void initInputPins()
  {
    in=(VS2DString)element.getPinInputReference(1);
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }
  
  public void xOnInit()
  {
    panelElement=element.getPanelElement();
  }

    
  public void start()
  {

  }
  
  
  
  public void changePin(int pinIndex, Object value)
  {
    //Object values=(Object[])value;

    if (value instanceof VS2DString)
    {
      VS2DString temp=(VS2DString)value;
      
      out.copyValueFrom(temp);
      
      element.setPinOutputReference(0,out);
      /*for (int i=0;i<values.length;i++)
      {
        out.setValue(i,(String)values[i]);
      } */
      element.notifyPin(0);
    }
  }

  public void process()
  {

    if (in!=null)
    {
      if (panelElement!=null)
      {
        panelElement.jProcessPanel(0,0.0,in);
      }
    }

  }

}
 
