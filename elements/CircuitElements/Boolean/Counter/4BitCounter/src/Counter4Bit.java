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
import java.awt.geom.Rectangle2D;
import java.util.*;


public class Counter4Bit extends JVSMain
{
  private int width=35+20, height=45;
  private Image image;
  private String imagePath;
  private boolean oldPin4=false;
  private boolean oldPin5=false;
  private boolean oki=true;
  
  private VSBoolean inTimer;
  private VSBoolean inReset;
  
  private VSBoolean out1=new VSBoolean();
  private VSBoolean out2=new VSBoolean();
  private VSBoolean out3=new VSBoolean();
  private VSBoolean out4=new VSBoolean();

  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }

  private int counter=1;


  public void paint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }
  
  public void init()
  {
    initPins(0,4,0,2);
    setSize(width,height);
    element.jSetTopPinsVisible(false);
    element.jSetBottomPinsVisible(false);


    setPin(0,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    setPin(2,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    setPin(3,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    
    setPin(4,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(5,ExternalIF.C_BOOLEAN,element.PIN_INPUT);


    String strLocale=Locale.getDefault().toString();

    if (strLocale.equalsIgnoreCase("de_DE"))
    {
      element.jSetPinDescription(0,"D1");
      element.jSetPinDescription(1,"D2");
      element.jSetPinDescription(2,"D3");
      element.jSetPinDescription(3,"D4");
      element.jSetPinDescription(4,"Timer");
      element.jSetPinDescription(5,"Reset");
    }
    if (strLocale.equalsIgnoreCase("en_US"))
    {
      element.jSetPinDescription(0,"D1");
      element.jSetPinDescription(1,"D2");
      element.jSetPinDescription(2,"D3");
      element.jSetPinDescription(3,"D4");
      element.jSetPinDescription(4,"Timer");
      element.jSetPinDescription(5,"Reset");
    }
    if (strLocale.equalsIgnoreCase("es_ES"))
    {
      element.jSetPinDescription(0,"D1");
      element.jSetPinDescription(1,"D2");
      element.jSetPinDescription(2,"D3");
      element.jSetPinDescription(3,"D4");
      element.jSetPinDescription(4,"Reloj");
      element.jSetPinDescription(5,"Reset");
    }


    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    element.jSetCaptionVisible(true);
    element.jSetCaption("4 Bit Counter");

    setName("4 Bit Counter");
    
  }
  

  public void initInputPins()
  {
    inTimer=(VSBoolean)element.getPinInputReference(4);
    inReset=(VSBoolean)element.getPinInputReference(5);

    if (inTimer==null) inTimer= new VSBoolean();
    if (inReset==null) inReset= new VSBoolean();
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out1);
    element.setPinOutputReference(1,out2);
    element.setPinOutputReference(2,out3);
    element.setPinOutputReference(3,out4);
  }


  private void setPins(int d1, int d2, int d3, int d4)
  {
     boolean a1=false;
     boolean a2=false;
     boolean a3=false;
     boolean a4=false;
     
     if (d1==1.0) a1=true;
     if (d2==1.0) a2=true;
     if (d3==1.0) a3=true;
     if (d4==1.0) a4=true;

     out1.setValue(a1);
     out2.setValue(a2);
     out3.setValue(a3);
     out4.setValue(a4);
     element.notifyPin(0);
     element.notifyPin(1);
     element.notifyPin(2);
     element.notifyPin(3);
  }

  public void process()
  {

     boolean pin4=inTimer.getValue();
     boolean pin5=inReset.getValue();

     if (pin5==true)
     {
      counter=1;
      setPins(0,0,0,0);
     }else
    if (pin4!=oldPin4)
    {
       if (pin4==true)
       {

         if (counter>15) counter=0;
         switch(counter)
         {
           case 0:  setPins(0,0,0,0);break;
           case 1:  setPins(0,0,0,1);break;
           case 2:  setPins(0,0,1,0);break;
           case 3:  setPins(0,0,1,1);break;
           case 4:  setPins(0,1,0,0);break;
           case 5:  setPins(0,1,0,1);break;
           case 6:  setPins(0,1,1,0);break;
           case 7:  setPins(0,1,1,1);break;
           case 8:  setPins(1,0,0,0);break;
           case 9:  setPins(1,0,0,1);break;
           case 10: setPins(1,0,1,0);break;
           case 11: setPins(1,0,1,1);break;
           case 12: setPins(1,1,0,0);break;
           case 13: setPins(1,1,0,1);break;
           case 14: setPins(1,1,1,0);break;
           case 15: setPins(1,1,1,1);break;
         }
         counter++;
       }
       oldPin4=pin4;
    }

  }


}

