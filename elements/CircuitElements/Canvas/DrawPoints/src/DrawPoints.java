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
import java.awt.geom.Point2D.Double;
import java.awt.event.*;
import java.awt.geom.*;



public class DrawPoints extends JVSMain
{

  private Image image;
  private VSDouble x1;
  private VSDouble y1;
  private VSBoolean reset;
  private VSBoolean antialising;
  private VSColor  strokeColor;
  private VSInteger strokeWidth;
  private VSInteger pointType;

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
    initPins(0,1,0,7);
    setSize(32+22,20+4+10*7);

    initPinVisibility(false,true,false,true);

    element.jSetInnerBorderVisibility(true);

    setPin(0,ExternalIF.C_CANVAS,element.PIN_OUTPUT); // Canvas
    setPin(1,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // x
    setPin(2,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // y
    setPin(3,ExternalIF.C_BOOLEAN,element.PIN_INPUT);  // reset Points (Deletes all Points!)
    setPin(4,ExternalIF.C_BOOLEAN,element.PIN_INPUT);  // antialising
    setPin(5,ExternalIF.C_COLOR,element.PIN_INPUT);  // strokeColor
    setPin(6,ExternalIF.C_INTEGER,element.PIN_INPUT);  // strokeWidth
    setPin(7,ExternalIF.C_INTEGER,element.PIN_INPUT);  // pointType

    element.jSetPinDescription(0,"out");
    element.jSetPinDescription(1,"point.x");
    element.jSetPinDescription(2,"point.y");
    element.jSetPinDescription(3,"reset Points");
    element.jSetPinDescription(4,"antialising");
    element.jSetPinDescription(5,"stroke color");
    element.jSetPinDescription(6,"stroke width");
    element.jSetPinDescription(7,"point type");

    String fileName=element.jGetSourcePath()+"icon.gif";
    image=element.jLoadImage(fileName);

    setName("DrawPoints 1.0");
  }


  public void start()
  {
     out.points.clear();
  }
  
  public void stop()
  {
     start();
  }


  public void initInputPins()
  {
    x1          = (VSDouble)element.getPinInputReference(1);
    y1          = (VSDouble)element.getPinInputReference(2);
    reset       = (VSBoolean)element.getPinInputReference(3);
    antialising = (VSBoolean)element.getPinInputReference(4);
    strokeColor = (VSColor)element.getPinInputReference(5);
    strokeWidth = (VSInteger)element.getPinInputReference(6);
    pointType   = (VSInteger)element.getPinInputReference(7);

    if (x1==null) x1=new VSDouble(0.0);
    if (y1==null) y1=new VSDouble(0.0);
    if (reset==null) reset=new VSBoolean(false);
    if (pointType==null) pointType=new VSInteger(0);


    if (antialising==null) antialising=new VSBoolean(false);
    if (strokeColor==null) strokeColor=new VSColor(Color.BLACK);
    if (strokeWidth==null) strokeWidth=new VSInteger(1);
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }

  int oldX=-1;
  int oldY=-1;


  public void process()
  {
    if (out!=null)
    {
       if (reset.getValue())
       {
          out.points.clear();
       } else
       if (x1.getValue()!=oldX || y1.getValue()!=oldY)
       {

         oldX=(int)x1.getValue();
         oldY=(int)y1.getValue();

         Point p=new Point((int)x1.getValue(),(int)y1.getValue());
         out.points.add(p);
         out.type=element.C_SHAPE_POINTS;

         out.rotationAngle=0.0;
         out.rotationX=0;
         out.rotationY=0;

         out.translationX=0;
         out.translationY=0;

         out.scaleX=1;
         out.scaleY=1;

         out.shearX=0;
         out.shearY=0;
       }
       out.x1=pointType.getValue();
       out.strokeWidth=strokeWidth.getValue();
       out.strokeColor=strokeColor.getValue();
       out.antialising=antialising.getValue();
       out.setChanged(true);
       element.notifyPin(0);


    }
  }



}


