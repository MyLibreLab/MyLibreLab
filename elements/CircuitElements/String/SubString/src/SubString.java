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
import  java.text.*;


public class SubString extends JVSMain
{
  private Image image;
  private VSString inValue;
  private VSInteger inBegin;
  private VSInteger inEnd;
  private VSString out=new VSString();


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
    initPins(0,1,0,3);
    setSize(45,40);

    initPinVisibility(false,true,false,true);

    element.jSetInnerBorderVisibility(true);
    
    image=element.jLoadImage(element.jGetSourcePath()+"icon.png");


    setPin(0,ExternalIF.C_STRING,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
    setPin(2,ExternalIF.C_INTEGER,element.PIN_INPUT);
    setPin(3,ExternalIF.C_INTEGER,element.PIN_INPUT);

    element.jSetPinDescription(0,"=SubStr(value,begin,end)   ");
    element.jSetPinDescription(1,"\"Value\"   ");
    element.jSetPinDescription(2,"\"begin\"   ");
    element.jSetPinDescription(3,"\"end\"   ");

    setName("SubString");

  }



  public void initInputPins()
  {
    inValue=(VSString)element.getPinInputReference(1);
    inBegin=(VSInteger)element.getPinInputReference(2);
    inEnd=(VSInteger)element.getPinInputReference(3);
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }

  private DecimalFormat df = new DecimalFormat();
  
  public void process()
  {
    if (inValue!=null && inBegin!=null && inEnd!=null )
    {
         try
         {
           String temp= inValue.getValue().substring(inBegin.getValue(),inEnd.getValue());
           out.setValue(temp);
         } catch(Exception ex)
         {

         }
         out.setChanged(true);
         element.notifyPin(0);
    }
  }

}

