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
import java.util.*;
import javax.swing.*;


public class Converter2D1D extends JVSMain
{
  private Image image;
  private VS2DDouble in = null;
  private VS1DDouble out = new VS1DDouble(0);

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
    setSize(20+32+3,36);
    initPinVisibility(false,true,false,true);
    element.jSetInnerBorderVisibility(true);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    setPin(0,ExternalIF.C_ARRAY1D_DOUBLE,element.PIN_OUTPUT);  // Out
    setPin(1,ExternalIF.C_ARRAY2D_DOUBLE,element.PIN_INPUT);   // in


    String strLocale=Locale.getDefault().toString();

    element.jSetPinDescription(0,"out");
    element.jSetPinDescription(1,"in");

    
    element.jSetCaptionVisible(false);
    element.jSetCaption("Converter2D1D");
    setName("Converter2D1D");
  }


  public void initInputPins()
  {
    in=(VS2DDouble)element.getPinInputReference(1);

    if (in==null) in=new VS2DDouble(0,0);
  }
  

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }


  public void start()
  {

  }

  private void copy()
  {

    int col=0;
    double[] tmp=new double[in.getRows()];
    
    for (int i=0;i<in.getRows();i++)
    {
      tmp[i]=in.getValue(col,i);
    }
    
    out.setValue(tmp);
  }

  public void process()
  {
    copy();
    element.notifyPin(0);
  }
  
}

