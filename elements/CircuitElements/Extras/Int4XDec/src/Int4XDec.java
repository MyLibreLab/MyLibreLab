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
import java.io.BufferedReader;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;


public class Int4XDec extends JVSMain
{
  private VSInteger in;
  private Image image;
  
  private VSInteger out1=new VSInteger(0);
  private VSInteger out2=new VSInteger(0);
  private VSInteger out3=new VSInteger(0);
  private VSInteger out4=new VSInteger(0);

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
    initPins(0,4,0,1);
    setSize(36+20,45);
    initPinVisibility(false,true,false,true);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");

    setPin(0,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
    setPin(2,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
    setPin(3,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
    setPin(4,ExternalIF.C_INTEGER,element.PIN_INPUT);

    element.jSetPinDescription(0,"Out1");
    element.jSetPinDescription(1,"Out2");
    element.jSetPinDescription(2,"Out3");
    element.jSetPinDescription(3,"Out4");
    element.jSetPinDescription(4,"in");

    element.jSetCaptionVisible(true);
    element.jSetCaption("Int4XDec");
    setName("Int4XDec");
  }
  
  public void initInputPins()
  {
    in=(VSInteger)element.getPinInputReference(4);
  }
  
  public void initOutputPins()
  {
    element.setPinOutputReference(0,out1);
    element.setPinOutputReference(1,out2);
    element.setPinOutputReference(2,out3);
    element.setPinOutputReference(3,out4);
  }

  public void process()
  {
    if (in!=null)
    {
      String val=""+in.getValue();

      int c=0;
      
      if (val.length()==0)
      {
        out1.setValue(0);
        out2.setValue(0);
        out3.setValue(0);
        out4.setValue(0);
      }else
      if (val.length()==1)
      {
        out1.setValue(0);
        out2.setValue(0);
        out3.setValue(0);
      }else
      if (val.length()==2)
      {
        out1.setValue(0);
        out2.setValue(0);
      }else
      if (val.length()==3)
      {
        out1.setValue(0);
      }

      //System.out.println("val.length()="+val.length());

      for (int i=val.length()-1;i>=0;i--)
      {
        String ch = val.substring(i,i+1);

        switch (c)
        {
          case 0 : out4.setValue(Integer.parseInt(ch)); break;
          case 1 : out3.setValue(Integer.parseInt(ch)); break;
          case 2 : out2.setValue(Integer.parseInt(ch)); break;
          case 3 : out1.setValue(Integer.parseInt(ch)); break;
        }
        c++;

      }
      
      element.notifyPin(0);
      element.notifyPin(1);
      element.notifyPin(2);
      element.notifyPin(3);
      
    }
  }


}

