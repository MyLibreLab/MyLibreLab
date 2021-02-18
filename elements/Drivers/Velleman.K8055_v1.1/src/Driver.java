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
import com.sun.jna.*;
import com.sun.jna.win32.StdCallLibrary;


public class Driver implements MyOpenLabDriverIF
{
  private boolean isOpen=false;
  private K8055 lib;

  private boolean oldInp1,oldInp2,oldInp3,oldInp4,oldInp5;
  public boolean xStop=false;
  private double A1,A2;
  private double oldA1,oldA2;
  private int oldCounter1;
  private int oldCounter2;
  private Boolean a0=false,a1=false,a2=false,a3=false,a4=false,a5=false,a6=false;
  
  private boolean inp1=false;
  private boolean inp2=false;
  private boolean inp3=false;
  private boolean inp4=false;
  private boolean inp5=false;
  private boolean running=false;
  
  private boolean inOut1;
  private boolean inOut2;
  private boolean inOut3;
  private boolean inOut4;
  private boolean inOut5;
  private boolean inOut6;
  private boolean inOut7;
  private boolean inOut8;
  
  private double inAC1;
  private double inAC2;

  private boolean outInp1;
  private boolean outInp2;
  private boolean outInp3;
  private boolean outInp4;
  private boolean outInp5;
  
  private VSDouble outA1=new VSDouble(0);
  private VSDouble outA2=new VSDouble(0);
  
  private javax.swing.Timer timer;
  
  private MyOpenLabDriverOwnerIF owner;
  private boolean firstTime=true;
  
  
  public void setDigitalChannelValue(int channel, Boolean value)
  {
    if (value.booleanValue())
    {
      lib.SetDigitalChannel(new NativeLong(channel));
    }else
    {
      lib.ClearDigitalChannel(new NativeLong(channel));
    }
  }
  

  public void setAnalogChannelValue(int channel, Integer value)
  {
      lib.OutputAnalogChannel(new NativeLong(channel), new NativeLong(value.intValue()));
  }
  
  
  public void sendCommand(String commando, Object value)
  {
    //System.out.println("Commando="+commando);
    
    if (commando.equals("ClearAllDigital")) lib.ClearAllDigital();
    if (commando.equals("ClearAllAnalog")) lib.ClearAllAnalog();

    if (value==null)
    {
      if (commando.equals("RESET_COUNTER_1")) lib.ResetCounter(new NativeLong(1));else
      if (commando.equals("RESET_COUNTER_2")) lib.ResetCounter(new NativeLong(2));
    }else
    if (value instanceof Boolean)
    {
      Boolean val=(Boolean)value;

      if (commando.equals("out1")) setDigitalChannelValue(1, val);else
      if (commando.equals("out2")) setDigitalChannelValue(2, val);else
      if (commando.equals("out3")) setDigitalChannelValue(3, val);else
      if (commando.equals("out4")) setDigitalChannelValue(4, val);else
      if (commando.equals("out5")) setDigitalChannelValue(5, val);else
      if (commando.equals("out6")) setDigitalChannelValue(6, val);else
      if (commando.equals("out7")) setDigitalChannelValue(7, val);else
      if (commando.equals("out8")) setDigitalChannelValue(8, val);
      
    }else
    if (value instanceof Integer)
    {
      Integer val=(Integer)value;

      if (commando.equals("ADC1")) setAnalogChannelValue(1, val);else
      if (commando.equals("ADC2")) setAnalogChannelValue(2, val);
     }
  }
  
  public void registerOwner(MyOpenLabDriverOwnerIF owner)
  {
    this.owner=owner;
    firstTime=true;
  }

  int test=0;
  public Driver()
  {

    ActionListener taskPerformer = new ActionListener()
    {
     public void actionPerformed(ActionEvent evt)
     {
        if (isOpen)
        {
         try
         {
           inp1 = lib.ReadDigitalChannel(new NativeLong(1));
           inp2 = lib.ReadDigitalChannel(new NativeLong(2));
           inp3 = lib.ReadDigitalChannel(new NativeLong(3));
           inp4 = lib.ReadDigitalChannel(new NativeLong(4));
           inp5 = lib.ReadDigitalChannel(new NativeLong(5));

           NativeLong a1 =lib.ReadAnalogChannel(new NativeLong(1));
           NativeLong a2 =lib.ReadAnalogChannel(new NativeLong(2));
           
           NativeLong counter1=lib.ReadCounter(new NativeLong(1));
           NativeLong counter2=lib.ReadCounter(new NativeLong(2));
           
           A1 = a1.intValue();
           A2 = a2.intValue();

           
           if (inp1!=oldInp1) { oldInp1=inp1; owner.getCommand("inp1",inp1); }
           if (inp2!=oldInp2) { oldInp2=inp2; owner.getCommand("inp2",inp2); }
           if (inp3!=oldInp3) { oldInp3=inp3; owner.getCommand("inp3",inp3); }
           if (inp4!=oldInp4) { oldInp4=inp4; owner.getCommand("inp4",inp4); }
           if (inp5!=oldInp5) { oldInp5=inp5; owner.getCommand("inp5",inp5); }
           

           if (counter1.intValue()!=oldCounter1) { oldCounter1=counter1.intValue(); owner.getCommand("COUNTER1",new Integer(counter1.intValue())); }
           if (counter2.intValue()!=oldCounter2) { oldCounter2=counter2.intValue(); owner.getCommand("COUNTER2",new Integer(counter2.intValue())); }
           
           if (A1!=oldA1) { oldA1=A1; owner.getCommand("DAC1",new Integer((int)A1)); }
           if (A2!=oldA2) { oldA2=A2; owner.getCommand("DAC2",new Integer((int)A2)); }

           if (firstTime==true)
           {
             oldInp1=inp1; owner.getCommand("inp1",inp1);
             oldInp2=inp2; owner.getCommand("inp2",inp2);
             oldInp3=inp3; owner.getCommand("inp3",inp3);
             oldInp4=inp4; owner.getCommand("inp4",inp4);
             oldInp5=inp5; owner.getCommand("inp5",inp5);
             oldA1=A1; owner.getCommand("DAC1",new Integer((int)A1));
             oldA2=A2; owner.getCommand("DAC2",new Integer((int)A2));
             oldCounter1=counter1.intValue();  owner.getCommand("COUNTER1",new Integer(counter1.intValue()));
             oldCounter2=counter2.intValue(); owner.getCommand("COUNTER2",new Integer(counter2.intValue()));
             firstTime=false;
           }
         }
         catch (Exception ex)
         {
          System.out.println(ex);
         }
        }
      }
    };

    timer = new javax.swing.Timer(1, taskPerformer);





  }

  public static void showMessage(String message)
  {
     JOptionPane.showMessageDialog(null,message,"Attention!",JOptionPane.ERROR_MESSAGE);
  }

  public void driverStart(ArrayList args)
  {
    if (isOpen)
    {
      showMessage("K8055 already Open!");
      return;
    }
    isOpen=false;
    try
    {
      System.out.println("OpenDevice: K8055");
      lib=K8055.INSTANCE;
      if (args!=null && args.size()==3)
      {
        Integer adresse=(Integer)args.get(0);
        Integer dbt1=(Integer)args.get(1);
        Integer dbt2=(Integer)args.get(2);
        
		
        lib.OpenDevice(new NativeLong(adresse.intValue()));
        lib.SetCounterDebounceTime(new NativeLong(1),new NativeLong(dbt1));
        lib.SetCounterDebounceTime(new NativeLong(2),new NativeLong(dbt2));

        System.out.println("Adresse = "+adresse.intValue());
        System.out.println("DeboundTime C-1 = "+dbt1.intValue());
        System.out.println("DeboundTime C-2 = "+dbt2.intValue());
      } else
      {
        lib.OpenDevice(new NativeLong(0));
        System.out.println("Adresse = >0");
      }
      isOpen=true;
    } catch (RuntimeException pvException)
    {
       showMessage("K8055 : " + pvException.getLocalizedMessage());
    }
    if (isOpen)
    {
      lib.ClearAllDigital();
      lib.ClearAllAnalog();
    }

    timer.start();
  }

  public void driverStop()
  {
    if (timer!=null)
    {
      timer.stop();
    }
     if (isOpen)
     {
      try
      {
        lib.ClearAllDigital();
        lib.ClearAllAnalog();

        lib.CloseDevice();
      }catch (Exception ex)
      {
        System.out.println(ex);
      }
      isOpen=false;
    }
  }


}
 
 

