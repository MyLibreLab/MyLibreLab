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
import javax.swing.*;


public class TickerTimer extends JVSMain
{

  private VSBoolean start;
  private VSBoolean pause;
  private VSBoolean reset;

  private VSBoolean outValue = new VSBoolean(false);

  private VSInteger delay1= new VSInteger(100);
  private VSInteger delay2= new VSInteger(100);
  

  private int counter=Integer.MAX_VALUE;
  
  private int oldImpuse=-1;
  private Image image;
  private boolean oki=false;
  
  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }
  
  private boolean started=false;

  
  public void paint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }
   
  public void init()
  {
    initPins(0,1,0,3);
    setSize(32+20,20+4+3*10);

    initPinVisibility(false,true,false,true);

    setPin(0,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    
    setPin(1,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(2,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(3,ExternalIF.C_BOOLEAN,element.PIN_INPUT);

    element.jSetPinDescription(0,"counter out");
    element.jSetPinDescription(1,"start");
    element.jSetPinDescription(2,"stop");
    element.jSetPinDescription(3,"reset");

    
    String fileName=element.jGetSourcePath()+"icon.gif";
    image=element.jLoadImage(fileName);
    
    setName("TickerTimer");
  }

  public void start()
  {
    started=false;
    counter=0;
    element.jNotifyMeForClock();
  }
  public void stop()
  {
   outValue.setValue(false);
  }



  public void process()
  {
      if (start.getValue()==true)
      {
        started=true;
      }
      if (pause.getValue())
      {
        started=false;
      }

      if (reset.getValue()==true)
      {
         counter=0;
         outValue.setValue(false);
         element.notifyPin(0);
      }

 }


  public void processClock()
  {

    if (started)
    {
      if (oki==false)
      {
        // also negativ
        if (counter>=delay1.getValue())
        {
          oki=!oki;
          outValue.setValue(oki);
          element.notifyPin(0);
          counter=0;
        }
      } else
      {
        // also Positiv
        if (counter>=delay2.getValue())
        {
          oki=!oki;
          outValue.setValue(oki);
          element.notifyPin(0);
          counter=0;
        }
      }
      counter++;
    }
  }


  public void initInputPins()
  {
    start=(VSBoolean)element.getPinInputReference(1);
    pause=(VSBoolean)element.getPinInputReference(2);
    reset=(VSBoolean)element.getPinInputReference(3);
    
    if (start==null) start =new VSBoolean(false);
    if (pause==null) pause =new VSBoolean(false);
    if (reset==null) reset =new VSBoolean(false);
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,outValue);
  }


  
  public void setPropertyEditor()
  {
    element.jAddPEItem("Delay 1",delay1, 0,99999999);
    element.jAddPEItem("Delay 2",delay2, 1,99999999);

    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Delay 1");
    element.jSetPEItemLocale(d+1,language,"Delay 2");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Delay 1");
    element.jSetPEItemLocale(d+1,language,"Delay 2");
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
    delay1.loadFromStream(fis);
    delay2.loadFromStream(fis);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    delay1.saveToStream(fos);
    delay2.saveToStream(fos);
  }


}
