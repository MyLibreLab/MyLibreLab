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


public class NumberGenerator extends JVSMain
{

  private VSBoolean start;
  private VSBoolean reset;
  private VSBoolean stop;
  
  private VSDouble from;
  private VSDouble to;
  private VSDouble step;
  
  private VSInteger delay;

  private VSDouble outValue = new VSDouble(0);
  private VSBoolean outImpulse = new VSBoolean(false);

  
  private javax.swing.Timer timer;

  private double counter=0;
  
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
    initPins(0,2,0,7);
    setSize(32+20,34+7*10);

    initPinVisibility(false,true,false,true);

    setPin(0,ExternalIF.C_DOUBLE,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    
    setPin(2,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(3,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(4,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(5,ExternalIF.C_DOUBLE,element.PIN_INPUT);
    setPin(6,ExternalIF.C_DOUBLE,element.PIN_INPUT);
    setPin(7,ExternalIF.C_DOUBLE,element.PIN_INPUT);
    setPin(8,ExternalIF.C_INTEGER,element.PIN_INPUT);

    element.jSetPinDescription(0,"value");
    element.jSetPinDescription(1,"impulse");
    element.jSetPinDescription(2,"start");
    element.jSetPinDescription(3,"reset");
    element.jSetPinDescription(4,"stop");
    element.jSetPinDescription(5,"(default = 0)  from");
    element.jSetPinDescription(6,"(default = 100)  to");
    element.jSetPinDescription(7,"(default = 1)  step");
    element.jSetPinDescription(8,"(default = 1) delay");

    
    String fileName=element.jGetSourcePath()+"icon.gif";
    image=element.jLoadImage(fileName);
    
    setName("NumberGenerator");
  }

  public void start()
  {
    timer = new javax.swing.Timer(delay.getValue(), new ActionListener()
    {
        public void actionPerformed(ActionEvent evt)
        {
          processX();
        }
    });

    started=false;

    
    counter=from.getValue();
    outValue.setValue(counter);
    outValue.setChanged(true);
    element.notifyPin(0);
    
    timer.start();

  }
  public void stop()
  {
   if (timer!=null) timer.stop();
  }



  public void process()
  {
      if (start.getValue()==true)
      {
        if (counter>=to.getValue())
        {
          counter=from.getValue();
        }
        started=true;

      }
      
      if (stop.getValue())
      {
        started=false;
      }
      
      if (delay.getValue()!=oldImpuse)
      {
       oldImpuse=delay.getValue();
       if (timer!=null) timer.setDelay(delay.getValue());
      }
      
      
      if (reset.getValue()==true)
      {
        counter=from.getValue();
        outValue.setValue(counter);
        outValue.setChanged(true);
        element.notifyPin(0);
      }

 }
 
  private boolean changed=false;

  public void destElementCalled()
  {
    if (changed)
    {
      //System.out.println("CANCELLED---------------------<");
      outImpulse.setValue(false);
      element.notifyPin(1);
      changed=false;
    }
  }

  public void processX()
  {
      if (started)
      {
        outValue.setValue(counter);
        outValue.setChanged(true);
        element.notifyPin(0);

        //if (step.getValue()<=0.0000000000001) step.setValue(0.0000000000001);
        //if (to.getValue()>=99999999999999.0) to.setValue(99999999999999.0);

        counter=counter+step.getValue();
        if (counter>=to.getValue())
        {
         started=false;
        }
        
        outImpulse.setValue(true);
        element.notifyPin(1);
        element.jNotifyWhenDestCalled(1,element);
        changed=true;

      }
  }
  

  public void initInputPins()
  {

    start=(VSBoolean)element.getPinInputReference(2);
    reset=(VSBoolean)element.getPinInputReference(3);
    stop=(VSBoolean)element.getPinInputReference(4);
    
    from=(VSDouble)element.getPinInputReference(5);
    to=(VSDouble)element.getPinInputReference(6);
    step=(VSDouble)element.getPinInputReference(7);

    delay=(VSInteger)element.getPinInputReference(8);
    
    if (start==null) start =new VSBoolean(false);
    if (stop==null)  stop =new VSBoolean(false);
    if (reset==null) reset =new VSBoolean(false);
    
    if (from==null) from =new VSDouble(0);
    if (to==null)  to =new VSDouble(100);
    if (step ==null) step =new VSDouble(1);

    if (delay==null) delay=new VSInteger(1);
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,outValue);
    element.setPinOutputReference(1,outImpulse);
  }


  
  /*public void setPropertyEditor()
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
  } */




}
