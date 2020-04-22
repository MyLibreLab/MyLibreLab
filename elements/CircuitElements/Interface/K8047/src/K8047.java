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
import java.text.*;

import javax.swing.*;
import java.util.*;
import java.io.*;


public class K8047 extends JVSMain implements MyOpenLabDriverOwnerIF
{
  private boolean isOpen=false;

  public boolean xStop=false;
  private boolean running=false;
  
  private VSInteger gainCH1;
  private VSInteger gainCH2;
  private VSInteger gainCH3;
  private VSInteger gainCH4;
  private VSInteger abtastIntervall;

  private VSDouble outCH1=new VSDouble(0);
  private VSDouble outCH2=new VSDouble(0);
  private VSDouble outCH3=new VSDouble(0);
  private VSDouble outCH4=new VSDouble(0);


  private Image image;
  private MyOpenLabDriverIF driver;


  public void getCommand(String commando, Object value)
  {
    //System.out.println("Commando="+commando);

    if (value instanceof Double)
    {
      Double val=(Double)value;

      if (commando.equals("DAC1")) {outCH1.setValue(val.doubleValue());element.notifyPin(0);}else
      if (commando.equals("DAC2")) {outCH2.setValue(val.doubleValue());element.notifyPin(1);}else
      if (commando.equals("DAC3")) {outCH3.setValue(val.doubleValue());element.notifyPin(2);}else
      if (commando.equals("DAC4")) {outCH4.setValue(val.doubleValue());element.notifyPin(3);}
     }
  }


  public K8047()
  {

  }
  public void onDispose()
  {
   image.flush();
   image=null;
   

  }

  public void paint(java.awt.Graphics g)
  {
      drawImageCentred(g,image);
  }
  
  public boolean dllsInstalled()
  {
      String winDir=System.getenv("WINDIR");

      File f1=new File(winDir+"\\system32\\FASTTime32.dll");
      File f2=new File(winDir+"\\system32\\K8047D.dll");
      File f3=new File(winDir+"\\system32\\K8047e.exe");

      if (!f1.exists() || !f2.exists() || !f3.exists())
      {
        return false;
      }
      return true;
  }
  
  public void init()
  {
    initPins(0,4,0,5);
    setSize(100,100);
    
    image=element.jLoadImage(element.jGetSourcePath()+"psc10.jpg");
    
    element.jSetLeftPinsVisible(true);
    element.jSetRightPinsVisible(true);

    setPin(0,ExternalIF.C_DOUBLE,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_DOUBLE,element.PIN_OUTPUT);
    setPin(2,ExternalIF.C_DOUBLE,element.PIN_OUTPUT);
    setPin(3,ExternalIF.C_DOUBLE,element.PIN_OUTPUT);


    setPin(4,ExternalIF.C_INTEGER,element.PIN_INPUT);
    setPin(5,ExternalIF.C_INTEGER,element.PIN_INPUT);
    setPin(6,ExternalIF.C_INTEGER,element.PIN_INPUT);
    setPin(7,ExternalIF.C_INTEGER,element.PIN_INPUT);
    setPin(8,ExternalIF.C_INTEGER,element.PIN_INPUT);

    element.jSetPinDescription(0,"CH1");
    element.jSetPinDescription(1,"CH2");
    element.jSetPinDescription(2,"CH3");
    element.jSetPinDescription(3,"CH4");

    element.jSetPinDescription(4,"GAIN_CH1");
    element.jSetPinDescription(5,"GAIN_CH2");
    element.jSetPinDescription(6,"GAIN_CH3");
    element.jSetPinDescription(7,"GAIN_CH4");
    element.jSetPinDescription(8,"Intervall");

    element.jSetCaptionVisible(false);
    element.jSetCaption("K8047/PSC10 Board");

    setName("K8047");
  }


  public void initInputPins()
  {
    gainCH1=(VSInteger)element.getPinInputReference(4);
    gainCH2=(VSInteger)element.getPinInputReference(5);
    gainCH3=(VSInteger)element.getPinInputReference(6);
    gainCH4=(VSInteger)element.getPinInputReference(7);
    abtastIntervall=(VSInteger)element.getPinInputReference(8);

    // Verstärkungsfaktor auf 10 also 10*3V = 30V
    if (gainCH1==null) gainCH1= new VSInteger(1);  // 1 = Faktor 10!
    if (gainCH2==null) gainCH2= new VSInteger(1);
    if (gainCH3==null) gainCH3= new VSInteger(1);
    if (gainCH4==null) gainCH4= new VSInteger(1);
    
    if (abtastIntervall==null) abtastIntervall=new VSInteger(1000);

  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,outCH1);
    element.setPinOutputReference(1,outCH2);
    element.setPinOutputReference(2,outCH3);
    element.setPinOutputReference(3,outCH4);
  }


    public static void showMessage(String message)
    {
       JOptionPane.showMessageDialog(null,message,"Attention!",JOptionPane.ERROR_MESSAGE);
    }

    public void start()
    {
    
      if (dllsInstalled())
      {
        isOpen=false;

        driver = element.jOpenDriver("Velleman.K8047", null);
        driver.registerOwner(this);

        if (driver!=null)
        {
          isOpen=true;
        }

      }else
      {
        String winDir=System.getenv("WINDIR")+"\\system32";
        showMessage("Please copy K8047e.exe, K8047D.dll and FASTTime32.dll from your Driver CD/DVD in "+winDir+" Directory!");
      }
    

        if (isOpen)
        {
           driver.sendCommand("LEDON",null);
        }



    }

  public void stop()
  {
    if (isOpen)
    {
      try
      {
        driver.sendCommand("LEDOFF",null);
        element.jCloseDriver("Velleman.K8047");
        isOpen=false;
      }catch (Exception ex)
      {
        System.out.println(ex);
        isOpen=false;
      }
    }
  }
  
  
  public void elementActionPerformed(ElementActionEvent evt)
  {

    int idx=evt.getSourcePinIndex();
    
    switch (idx)
    {
      case 4:  driver.sendCommand("SET_GAIN_CH1",new Integer(gainCH1.getValue())); break;
      case 5:  driver.sendCommand("SET_GAIN_CH2",new Integer(gainCH2.getValue())); break;
      case 6:  driver.sendCommand("SET_GAIN_CH3",new Integer(gainCH3.getValue())); break;
      case 7:  driver.sendCommand("SET_GAIN_CH4",new Integer(gainCH4.getValue())); break;
      case 8:
      {
        driver.sendCommand("SETINTERVALL",new Integer(abtastIntervall.getValue()));
        break;
      }


    }

  }


}
 
 

