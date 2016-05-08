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


public class K8055 extends JVSMain implements MyOpenLabDriverOwnerIF
{
  private boolean isOpen=false;

  private boolean oldInp1,oldInp2,oldInp3,oldInp4,oldInp5;
  public boolean xStop=false;
  private Boolean a0=false,a1=false,a2=false,a3=false,a4=false,a5=false,a6=false;
  private boolean inp1=false;
  private boolean inp2=false;
  private boolean inp3=false;
  private boolean inp4=false;
  private boolean inp5=false;
  private boolean running=false;
  
  private VSBoolean SK5=new VSBoolean(true);
  private VSBoolean SK6=new VSBoolean(true);
  
  
  private VSBoolean inOut1;
  private VSBoolean inOut2;
  private VSBoolean inOut3;
  private VSBoolean inOut4;
  private VSBoolean inOut5;
  private VSBoolean inOut6;
  private VSBoolean inOut7;
  private VSBoolean inOut8;
  
  private VSDouble inAC1;
  private VSDouble inAC2;

  private VSBoolean outInp1 = new VSBoolean();
  private VSBoolean outInp2 = new VSBoolean();
  private VSBoolean outInp3 = new VSBoolean();
  private VSBoolean outInp4 = new VSBoolean();
  private VSBoolean outInp5 = new VSBoolean();
  
  private VSDouble outA1=new VSDouble(0);
  private VSDouble outA2=new VSDouble(0);
  
  private Image image;
  private MyOpenLabDriverIF driver ;


  public void getCommand(String commando, Object value)
  {
    //System.out.println("Commando="+commando);


    if (value instanceof Boolean)
    {
      Boolean val=(Boolean)value;

      if (commando.equals("inp1")) {outInp1.setValue(val.booleanValue());element.notifyPin(0);}else
      if (commando.equals("inp2")) {outInp2.setValue(val.booleanValue());element.notifyPin(1);}else
      if (commando.equals("inp3")) {outInp3.setValue(val.booleanValue());element.notifyPin(2);}else
      if (commando.equals("inp4")) {outInp4.setValue(val.booleanValue());element.notifyPin(3);}else
      if (commando.equals("inp5")) {outInp5.setValue(val.booleanValue());element.notifyPin(4);}
    }else
    if (value instanceof Integer)
    {
      Integer val=(Integer)value;

      if (commando.equals("DAC1")) {outA1.setValue(val.intValue());element.notifyPin(5);}else
      if (commando.equals("DAC2")) {outA2.setValue(val.intValue());element.notifyPin(6);}
     }
  }


  public K8055()
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

      File f1=new File(winDir+"\\system32\\TWUsb.dll");
      File f2=new File(winDir+"\\system32\\K8055D.dll");

      if (!f1.exists() || !f2.exists())
      {
        return false;
      }
      return true;
  }
  
  public void init()
  {
    initPins(0,7,0,10);
    setSize(80,120);
    
    image=element.jLoadImage(element.jGetSourcePath()+"image.png");
    
    element.jSetLeftPinsVisible(true);
    element.jSetRightPinsVisible(true);

    setPin(0,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    setPin(2,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    setPin(3,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    setPin(4,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    
    setPin(5,ExternalIF.C_DOUBLE,element.PIN_OUTPUT);
    setPin(6,ExternalIF.C_DOUBLE,element.PIN_OUTPUT);
    
    setPin(7,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(8,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(9,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(10,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(11,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(12,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(13,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(14,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    
    setPin(15,ExternalIF.C_DOUBLE,element.PIN_INPUT);
    setPin(16,ExternalIF.C_DOUBLE,element.PIN_INPUT);
    

    element.jSetPinDescription(0,"Inp1");
    element.jSetPinDescription(1,"Inp2");
    element.jSetPinDescription(2,"Inp3");
    element.jSetPinDescription(3,"Inp4");
    element.jSetPinDescription(4,"Inp5");
    
    element.jSetPinDescription(5,"A1");
    element.jSetPinDescription(6,"A2");
    
    element.jSetPinDescription(7,"Out1");
    element.jSetPinDescription(8,"Out2");
    element.jSetPinDescription(9,"Out3");
    element.jSetPinDescription(10,"Out4");
    element.jSetPinDescription(11,"Out5");
    element.jSetPinDescription(12,"Out6");
    element.jSetPinDescription(13,"Out7");
    element.jSetPinDescription(14,"Out8");
    
    element.jSetPinDescription(15,"DAC1");
    element.jSetPinDescription(16,"DAC2");

    element.jSetCaptionVisible(false);
    element.jSetCaption("K8055 Board");

    setName("K8055");
  }





  public void initInputPins()
  {
    inOut1=(VSBoolean)element.getPinInputReference(7);
    inOut2=(VSBoolean)element.getPinInputReference(8);
    inOut3=(VSBoolean)element.getPinInputReference(9);
    inOut4=(VSBoolean)element.getPinInputReference(10);
    inOut5=(VSBoolean)element.getPinInputReference(11);
    inOut6=(VSBoolean)element.getPinInputReference(12);
    inOut7=(VSBoolean)element.getPinInputReference(13);
    inOut8=(VSBoolean)element.getPinInputReference(14);
    
    inAC1=(VSDouble)element.getPinInputReference(15);
    inAC2=(VSDouble)element.getPinInputReference(16);

    if (inOut1==null) inOut1= new VSBoolean();
    if (inOut2==null) inOut2= new VSBoolean();
    if (inOut3==null) inOut3= new VSBoolean();
    if (inOut4==null) inOut4= new VSBoolean();
    if (inOut5==null) inOut5= new VSBoolean();
    if (inOut6==null) inOut6= new VSBoolean();
    if (inOut7==null) inOut7= new VSBoolean();
    if (inOut8==null) inOut8= new VSBoolean();
    
    if (inAC1==null) inAC1= new VSDouble();
    if (inAC2==null) inAC2= new VSDouble();
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,outInp1);
    element.setPinOutputReference(1,outInp2);
    element.setPinOutputReference(2,outInp3);
    element.setPinOutputReference(3,outInp4);
    element.setPinOutputReference(4,outInp5);
    element.setPinOutputReference(5,outA1);
    element.setPinOutputReference(6,outA2);
  }


    public static void showMessage(String message)
    {
       JOptionPane.showMessageDialog(null,message,"Attention!",JOptionPane.ERROR_MESSAGE);
    }

	private int getAdresse()
    {
		int sk5=0;
		int sk6=0;
		if (SK5.getValue()==true) sk5=1 ; else sk5=0;
		if (SK6.getValue()==true) sk6=1 ; else sk6=0;
		return 3-(sk5+sk6*2);  
    }
    public void start()
    {
    
      if (dllsInstalled())
      {
        isOpen=false;
        ArrayList args=new ArrayList();
		

        args.add(new Integer(getAdresse()));
        args.add(new Integer(50));
        args.add(new Integer(50));

		driver = element.jOpenDriver("Velleman.K8055_v1.1", args);
        driver.registerOwner(this);

        if (driver!=null)
        {
          isOpen=true;
        }

        if (isOpen)
        {
           driver.sendCommand("ClearAllDigital",null);
           driver.sendCommand("ClearAllAnalog",null);
        }

      }else
      {
      
        String winDir=System.getenv("WINDIR")+"\\system32";

        showMessage("Please copy \"K8055D.dll\" \"TWUsb.dll\" from your Driver CD/DVD in "+winDir+" Directory!");
      }
    


    }

  public void stop()
  {
    if (isOpen)
    {
      try
      {
        driver.sendCommand("ClearAllDigital",null);
        driver.sendCommand("ClearAllAnalog",null);
        element.jCloseDriver("Velleman.K8055");
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
      case 7:  driver.sendCommand("out1",new Boolean(inOut1.getValue())); break;  // Out1
      case 8:  driver.sendCommand("out2",new Boolean(inOut2.getValue())); break;  // Out2
      case 9:  driver.sendCommand("out3",new Boolean(inOut3.getValue())); break;  // Out3
      case 10: driver.sendCommand("out4",new Boolean(inOut4.getValue())); break;  // Out4
      case 11: driver.sendCommand("out5",new Boolean(inOut5.getValue())); break;  // Out5
      case 12: driver.sendCommand("out6",new Boolean(inOut6.getValue())); break;  // Out6
      case 13: driver.sendCommand("out7",new Boolean(inOut7.getValue())); break;  // Out7
      case 14: driver.sendCommand("out8",new Boolean(inOut8.getValue())); break;  // Out8
      
      case 15: driver.sendCommand("ADC1",new Integer((int)inAC1.getValue())); break;  // ADC1
      case 16: driver.sendCommand("ADC2",new Integer((int)inAC2.getValue())); break;  // ADC2
    }

  }


}
 
 

