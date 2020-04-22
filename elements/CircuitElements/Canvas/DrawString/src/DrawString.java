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


public class DrawString extends JVSMain
{
  private Image image;
  private VSDouble x1;
  private VSDouble y1;
  private VSString value;
  private VSBoolean antialising;
  private VSColor  fontColor;
  private VSFont font;


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
    initPins(0,1,0,6);
    setSize(32+22,20+4+10*6);

    initPinVisibility(false,true,false,true);

    element.jSetInnerBorderVisibility(true);

    setPin(0,ExternalIF.C_CANVAS,element.PIN_OUTPUT); // Image
    setPin(1,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // x
    setPin(2,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // y
    setPin(3,ExternalIF.C_STRING,element.PIN_INPUT);  // value String
    setPin(4,ExternalIF.C_BOOLEAN,element.PIN_INPUT); // antialising
    setPin(5,ExternalIF.C_COLOR,element.PIN_INPUT);   // fontcolor
    setPin(6,ExternalIF.C_FONT,element.PIN_INPUT);    // font


    element.jSetPinDescription(0,"Out");
    element.jSetPinDescription(1,"x");
    element.jSetPinDescription(2,"y");
    element.jSetPinDescription(3,"Value");
    element.jSetPinDescription(4,"antialising");
    element.jSetPinDescription(5,"font color");
    element.jSetPinDescription(6,"font");

    String fileName=element.jGetSourcePath()+"icon.gif";
    image=element.jLoadImage(fileName);

    setName("DrawString");
  }


  public void start()
  {
   process();
  }


  public void initInputPins()
  {
    x1          = (VSDouble)element.getPinInputReference(1);
    y1          = (VSDouble)element.getPinInputReference(2);
    value       = (VSString)element.getPinInputReference(3);
    antialising = (VSBoolean)element.getPinInputReference(4);
    fontColor   = (VSColor)element.getPinInputReference(5);
    font        = (VSFont)element.getPinInputReference(6);

    if (x1==null) x1=new VSDouble(0.0);
    if (y1==null) y1=new VSDouble(0.0);
    if (value==null) value=new VSString("TEST");

    if (antialising==null) antialising=new VSBoolean(true);
    if (fontColor==null) fontColor=new VSColor(Color.BLACK);
    if (font==null) font=new VSFont(new Font("Arial",Font.PLAIN,10));
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }


  public void process()
  {
    if (out!=null)
    {
       out.type=element.C_SHAPE_LINE;

       out.x1=(int)x1.getValue();
       out.y1=(int)y1.getValue();
       out.x2=0;
       out.y2=0;

       out.type=element.C_SHAPE_STRING;
       out.caption=value.getValue();
       out.font=font.getValue();

       out.strokeColor=fontColor.getValue();

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

  /*public void process()
  {
    if (out!=null)
    {
       //Line2D shape = new Line2D.Double(x1.getValue(),y1.getValue(),0,0);
       out.shape=null;
       out.strokeColor=fontColor.getValue();
       out.antialising=antialising.getValue();
       
       out.other.clear();
       out.other.add(value);
       out.other.add(font);
       out.other.add(x1);
       out.other.add(y1);
       
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
  }  */



}


