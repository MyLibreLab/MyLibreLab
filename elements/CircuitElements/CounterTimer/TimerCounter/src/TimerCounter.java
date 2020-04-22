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
import javax.swing.Timer;
import javax.swing.*;


public class TimerCounter extends JVSMain
{
  private VSInteger impulse;
  private VSBoolean start;
  private VSBoolean pause;
  private VSBoolean reset;

  private VSInteger outValue = new VSInteger(0);
  
  private javax.swing.Timer timer;

  private int counter=Integer.MAX_VALUE;
  
  private int oldImpuse=-1;
  private Image image;
  
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
    initPins(0,1,0,4);
    setSize(32+20,20+4+3*10);

    initPinVisibility(false,true,false,true);

    setPin(0,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
    
    setPin(1,ExternalIF.C_INTEGER,element.PIN_INPUT);
    setPin(2,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(3,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(4,ExternalIF.C_BOOLEAN,element.PIN_INPUT);

    element.jSetPinDescription(0,"counter out");
    element.jSetPinDescription(1,"impulse [ms]");
    element.jSetPinDescription(2,"play");
    element.jSetPinDescription(3,"pause");
    element.jSetPinDescription(4,"reset");

    
    String fileName=element.jGetSourcePath()+"icon.gif";
    image=element.jLoadImage(fileName);
    
    setName("Timer/Counter 1.0");
  }

  public void start()
  {
    timer = new javax.swing.Timer(impulse.getValue(), new ActionListener()
    {
        public void actionPerformed(ActionEvent evt)
        {
          processX();
        }
    });

    started=false;
    timer.start();

  }
  public void stop()
  {
   if (timer!=null) timer.stop();
   
   counter=0;
   outValue.setValue(counter);
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
      if (impulse.getValue()!=oldImpuse)
      {
       oldImpuse=impulse.getValue();
       if (timer!=null) timer.setDelay(impulse.getValue());
      }
      
      if (reset.getValue()==true)
      {
         counter=0;
         //if (started==false)
         {
          outValue.setValue(counter);
          outValue.setChanged(true);
          element.notifyPin(0);

         }
      }

 }

  public void processX()
  {
      if (started)
      {
        outValue.setValue(counter);
        outValue.setChanged(true);
        element.notifyPin(0);

        counter=counter+1;
        if (counter>=Integer.MAX_VALUE-1) counter=0;
      }
  }
  

  public void initInputPins()
  {
    impulse=(VSInteger)element.getPinInputReference(1);
    start=(VSBoolean)element.getPinInputReference(2);
    pause=(VSBoolean)element.getPinInputReference(3);
    reset=(VSBoolean)element.getPinInputReference(4);
    

    if (impulse==null) impulse=new VSInteger(0);
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
    /*element.jAddPEItem("Min",min, 0,0);
    element.jAddPEItem("Max",max, 1,99999999);
    element.jAddPEItem("Schritt",step, 0,99999);
    localize();*/
  }


  /*private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Min");
    element.jSetPEItemLocale(d+1,language,"Max");
    element.jSetPEItemLocale(d+2,language,"Step");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Min");
    element.jSetPEItemLocale(d+1,language,"Max");
    element.jSetPEItemLocale(d+2,language,"Paso");
  } */

  public void loadFromStream(java.io.FileInputStream fis)
  {
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
  }



}
