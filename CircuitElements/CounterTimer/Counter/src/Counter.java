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

public class Counter extends JVSMain
{
  private VSDouble min=new VSDouble();
  private VSDouble max=new VSDouble();
  private VSDouble step=new VSDouble();
  private VSDouble out = new VSDouble();
  
  private boolean oldON=true;
  private boolean oldReset=true;
  
  private javax.swing.Timer timer;
  private VSBoolean inReset;
  private VSBoolean inON;
  
  boolean started=false;
  
  private double counter=0;
  
  private Image image;
  
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
    initPins(0,1,0,2);
    setSize(32+20,32);

    initPinVisibility(false,true,false,true);

    setPin(0,ExternalIF.C_DOUBLE,element.PIN_OUTPUT);
    
    setPin(1,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(2,ExternalIF.C_BOOLEAN,element.PIN_INPUT);

    element.jSetPinDescription(0,"Out");
    element.jSetPinDescription(1,"Reset");
    element.jSetPinDescription(2,"On/Off");
    
    String fileName=element.jGetSourcePath()+"icon.gif";
    image=element.jLoadImage(fileName);
    
    setName("Counter");
    min.setValue(0);
    max.setValue(1000);
    step.setValue(1);
    


  }

  public void start()
  {

    timer = new javax.swing.Timer(1, new ActionListener()
    {
        public void actionPerformed(ActionEvent evt)
        {
          processX();
        }
    });
    timer.start();


    counter=min.getValue();
    out.setValue(counter);
    element.notifyPin(0);
    started=true;
    //element.jNotifyMeForClock();
    
  }
  public void stop()
  {
   if (timer!=null) timer.stop();


   started=false;
  }



  
  public void processX()
  {
    if (started)
    {
      if (counter>max.getValue()) counter=min.getValue();

      if (inON.getValue()==true)
      {
         out.setValue(counter);
         counter=counter+step.getValue();

         out.setChanged(true);
         element.notifyPin(0);
      }

      if (inReset.getValue()==true)
      {
        counter=0;
        out.setValue(counter);
        out.setChanged(true);
        element.notifyPin(0);
      }

    }

  }


  public void initInputPins()
  {
    inReset=(VSBoolean)element.getPinInputReference(1);
    inON=(VSBoolean)element.getPinInputReference(2);
    
    if (inReset==null) inReset=new VSBoolean(false);
    if (inON==null) inON=new VSBoolean(false);
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }


  
  public void setPropertyEditor()
  {
    element.jAddPEItem("Min",min, 0,0);
    element.jAddPEItem("Max",max, 1,99999999);
    element.jAddPEItem("Schritt",step, 0,99999);
    localize();
  }


  private void localize()
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
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
      min.loadFromStream(fis);
      max.loadFromStream(fis);
      step.loadFromStream(fis);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      min.saveToStream(fos);
      max.saveToStream(fos);
      step.saveToStream(fos);
  }



}
