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


public class DrawEllipse extends JVSMain
{
  private Image image;
  private VSDouble x1;
  private VSDouble y1;
  private VSDouble x2;
  private VSDouble y2;
  private VSBoolean antialising;
  private VSColor  strokeColor;
  private VSColor  fillColor;
  private VSInteger strokeWidth;

  private VSCanvas out=new VSCanvas();

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
    initPins(0,1,0,8);
    setSize(32+22,20+4+10*8);

    initPinVisibility(false,true,false,true);

    element.jSetInnerBorderVisibility(true);

    setPin(0,ExternalIF.C_CANVAS,element.PIN_OUTPUT); // Image
    setPin(1,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // x1
    setPin(2,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // y1
    setPin(3,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // x2
    setPin(4,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // y2
    setPin(5,ExternalIF.C_BOOLEAN,element.PIN_INPUT);  // antialising
    setPin(6,ExternalIF.C_COLOR,element.PIN_INPUT);  // strokeColor
    setPin(7,ExternalIF.C_COLOR,element.PIN_INPUT);  // fillColor
    setPin(8,ExternalIF.C_INTEGER,element.PIN_INPUT);  // strokeWidth

    element.jSetPinDescription(0,"Out");
    element.jSetPinDescription(1,"x1");
    element.jSetPinDescription(2,"y1");
    element.jSetPinDescription(3,"x2");
    element.jSetPinDescription(4,"y2");
    element.jSetPinDescription(5,"antialising");
    element.jSetPinDescription(6,"stroke color");
    element.jSetPinDescription(7,"fill color");
    element.jSetPinDescription(8,"stroke width");
    //element.jSetPinDescription(1,"Image In");

    String fileName=element.jGetSourcePath()+"icon.gif";
    image=element.jLoadImage(fileName);

    setName("DrawEllipse 1.0");
  }


  public void start()
  {
   process();
  }


  public void initInputPins()
  {
    x1          = (VSDouble)element.getPinInputReference(1);
    y1          = (VSDouble)element.getPinInputReference(2);
    x2          = (VSDouble)element.getPinInputReference(3);
    y2          = (VSDouble)element.getPinInputReference(4);
    antialising = (VSBoolean)element.getPinInputReference(5);
    strokeColor = (VSColor)element.getPinInputReference(6);
    fillColor   = (VSColor)element.getPinInputReference(7);
    strokeWidth = (VSInteger)element.getPinInputReference(8);

    if (x1==null) x1=new VSDouble(0.0);
    if (y1==null) y1=new VSDouble(0.0);
    if (x2==null) x2=new VSDouble(0.0);
    if (y2==null) y2=new VSDouble(0.0);

    if (antialising==null) antialising=new VSBoolean(true);
    if (strokeColor==null) strokeColor=new VSColor(Color.BLACK);
    if (fillColor==null) fillColor=new VSColor(Color.BLACK);
    if (strokeWidth==null) strokeWidth=new VSInteger(1);

  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }




  public void process()
  {
    if (out!=null)
    {
       out.type=element.C_SHAPE_ELLIPSE;

       out.x1=(int)x1.getValue();
       out.y1=(int)y1.getValue();
       out.x2=(int)x2.getValue();
       out.y2=(int)y2.getValue();
       
       out.strokeWidth=strokeWidth.getValue();
       out.strokeColor=strokeColor.getValue();
       out.fillColor=fillColor.getValue();
       out.antialising=antialising.getValue();
       
       out.rotationAngle=0.0;
       out.rotationX=0;
       out.rotationY=0;

       out.translationX=0;
       out.translationY=0;

       out.scaleX=1;
       out.scaleY=1;

       out.shearX=0;
       out.shearY=0;

       out.setChanged(true);
       element.notifyPin(0);
    }

  }



}


