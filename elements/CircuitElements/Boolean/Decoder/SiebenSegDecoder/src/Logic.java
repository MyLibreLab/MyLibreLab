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


public class Logic extends JVSMain
{
  private int width, height;
  private String fileName;
  
  private Image image=null;
  
  private VSBoolean in[];
  private VSBoolean out[];

  private int inputs=4;
  private int outputs=7;
  
  private String table[]=
  {
    "1111110",
    "0110000",
    "1101101",
    "1111001",
    "0110011",
    "1011011",
    "1011111",
    "1110000",
    "1111111",
    "1111011",
    "1110111",
    "0011111",
    "1001110",
    "0111101",
    "1001111",
    "1000111"
  };

  public void parseString(String line, boolean array[])
  {
      for (int i=0;i<line.length();i++)
      {
          char c=line.charAt(i);
          if (c=='0') array[i]=false;
          if (c=='1') array[i]=true;
      }
  }

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
    if (image!=null) drawImageCentred(g,image);
  }
  
  
  public void init()
  {
    initPins(0,outputs,0,inputs);

    element.jSetInnerBorderVisibility(true);
    element.jSetTopPinsVisible(false);
    element.jSetBottomPinsVisible(false);

    image=element.jLoadImage(element.jGetSourcePath()+"Logic.gif");
    
    width=60;
    if (outputs>inputs) height=20+10*outputs; else height=20+10*inputs;
    setSize(width,height);

    int c=0;
    for (int i=0;i<outputs;i++)
    {
      setPin(c++,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    }
    
    element.jSetPinDescription(0,"a");
    element.jSetPinDescription(1,"b");
    element.jSetPinDescription(2,"c");
    element.jSetPinDescription(3,"d");
    element.jSetPinDescription(4,"e");
    element.jSetPinDescription(5,"f");
    element.jSetPinDescription(6,"g");

    for (int i=0;i<inputs;i++)
    {
      setPin(c,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
      element.jSetPinDescription(c,"D"+(i+1));
      c++;
    }
    element.jSetCaptionVisible(true);
    element.jSetCaption("7 Segm Decoder");
    setName("Siebensegment-Decoder");
  }
  
  public void initInputPins()
  {
    in=new VSBoolean[inputs];
    for (int i=0;i<inputs;i++)
    {
      in[i]=(VSBoolean)element.getPinInputReference(outputs+i);
    }
  }
  
  public void initOutputPins()
  {
    out = new VSBoolean[outputs];
    for (int i=0;i<outputs;i++)
    {
      out[i]=new VSBoolean();
      element.setPinOutputReference(i,out[i]);
    }
  }


  public void process()
  {

    String val="";
    boolean value=false;
    
    for (int i=0;i<inputs;i++)
    {
      if (in[i]!=null)
      {
        value=in[i].getValue();
      }
      if (value==true) val+="1"; else val+="0";
    }

    int index=Integer.parseInt(val,2);
    
    
    if (index<table.length)
    {
      String zeile=table[index];
      
      boolean array[] = new boolean[outputs];

      parseString(zeile,array);


      for (int i=0;i<outputs;i++)
      {
        out[i].setValue(array[i]);
        element.notifyPin(i);
      }

   }


  }


}

