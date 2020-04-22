//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia (cswi@gmx.de)                         *
//* Copyright (C) 2018  Javier Velásquez (javiervelasquez125@gmail.com)                                                                         *
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
import java.util.*;
import java.awt.event.*;
import java.io.*;
import java.nio.channels.*;
import java.nio.*;
import javax.swing.*;



public class Arduino extends JVSMain implements MyOpenLabDriverOwnerIF
{
  private Image image;

  private MyOpenLabDriverIF driver ;

  VS1DByte inBytes=new VS1DByte(1);
  
  private javax.swing.Timer timer;
  
  private VSInteger pollTime=new VSInteger(200);
  

  boolean t1=false;
  boolean t2=false;
  boolean t3=false;
  boolean t4=false;
  boolean t5=false;
  boolean t6=false;

  private VSString Port=new VSString("/dev/ttyUSB1");
  private VSBoolean taste1=new VSBoolean(false);
  private VSBoolean taste2=new VSBoolean(false);
  private VSBoolean taste3=new VSBoolean(false);
  private VSBoolean taste4=new VSBoolean(false);
  private VSBoolean taste5=new VSBoolean(false);
  private VSBoolean taste6=new VSBoolean(false);

  private VSInteger adc1=new VSInteger(0);
  private VSInteger adc2=new VSInteger(0);
  private VSInteger adc3=new VSInteger(0);
  private VSInteger adc4=new VSInteger(0);
  private VSInteger adc5=new VSInteger(0);
  private VSInteger adc6=new VSInteger(0);

  private VSComboBox comport = new VSComboBox();
  private VSBoolean comStart;
  private VSBoolean led1;
  private VSBoolean led2;
  private VSBoolean led3;
  private VSBoolean led4;
  private VSBoolean led5;
  private VSBoolean led6;



  public void paint(java.awt.Graphics g)
  {
    if (image!=null) drawImageCentred(g,image);
  }
  
  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }

 

  public  void copyFile(File source, File dest) throws IOException
  {
      FileChannel in = null, out = null;
      try
      {
          in = new FileInputStream(source).getChannel();
          out = new FileOutputStream(dest).getChannel();

          long size = in.size();
          MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0, size);

          out.write(buf);

      }
      finally
      {
          if (in != null)  in.close();
          if (out != null) out.close();
      }
  }


  public void setPropertyEditor()
  {
    element.jAddPEItem("Serial_Port", comport, 0, 0);
    element.jAddPEItem("Poll Time[ms]",pollTime, 1,5000);
    
    /*element.jAddPEItem("Baud",baud, 1,999999);
    element.jAddPEItem("DataBits",bits, 5,8);
    element.jAddPEItem("Partity",parity, 1,20);
    element.jAddPEItem("stopBits",stopBits, 1,2);*/

  }


  public void propertyChanged(Object o)
  {

  }


  public void init()
  {
    initPins(0,12,0,8);
    setSize(32+22,10+(12*10));
    
    initPinVisibility(false,true, false,true);

    element.jSetInnerBorderVisibility(true);
    ArrayList<Object> args = new ArrayList<>();
        args.add("NULL");
        args.add(0);
        args.add(0);
        args.add(0);
        args.add(0);
        args.add(false);

        driver = element.jOpenDriver("MyOpenLab.RS232", args);
        
        System.out.println("driver="+driver.toString());
        
        comport.clear();
        comport.addItem("NULL");
      
        if (driver != null) {

          ArrayList<String> ports = new ArrayList();
          driver.sendCommand("NULL;GETPORTS", ports);

          System.out.println("size of Ports : " + ports.size());
          for (String port : ports) {
              System.out.println("-X-------> Port : " + port);
              comport.addItem(port);
          }

          driver.sendCommand("NULL;CLOSE", null);
          element.jCloseDriver("MyOpenLab.RS232");

      } else {
          System.out.println("DRIVER IS NULL!!!");
      }

    int c=0;
    
    setPin(c++,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT); // Taste1
    setPin(c++,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    setPin(c++,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    setPin(c++,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    setPin(c++,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    setPin(c++,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT); // Taste6

    setPin(c++,ExternalIF.C_INTEGER,element.PIN_OUTPUT); // ADC-1
    setPin(c++,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
    setPin(c++,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
    setPin(c++,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
    setPin(c++,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
    setPin(c++,ExternalIF.C_INTEGER,element.PIN_OUTPUT); // ADC-6
    
    //setPin(c++,ExternalIF.C_STRING,element.PIN_INPUT); // COM Port 
    //setPin(c++,ExternalIF.C_BOOLEAN,element.PIN_INPUT); // start
    
    setPin(c++,ExternalIF.C_BOOLEAN,element.PIN_INPUT); // LED 1
    setPin(c++,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(c++,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(c++,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(c++,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(c++,ExternalIF.C_BOOLEAN,element.PIN_INPUT); // LED 4

    c=0;
    element.jSetPinDescription(c++,"In-1 (Arduino Pin 2)");
    element.jSetPinDescription(c++,"In-2 (Arduino Pin 3)");
    element.jSetPinDescription(c++,"In-3 (Arduino Pin 4)");
    element.jSetPinDescription(c++,"In-4 (Arduino Pin 5)");
    element.jSetPinDescription(c++,"In-5 (Arduino Pin 6)");
    element.jSetPinDescription(c++,"In-6 (Arduino Pin 7)");
    
    element.jSetPinDescription(c++,"ADC-1 (Arduino Pin A0)");
    element.jSetPinDescription(c++,"ADC-2 (Arduino Pin A1)");
    element.jSetPinDescription(c++,"ADC-3 (Arduino Pin A2)");
    element.jSetPinDescription(c++,"ADC-4 (Arduino Pin A3)");
    element.jSetPinDescription(c++,"ADC-5 (Arduino Pin A4)");
    element.jSetPinDescription(c++,"ADC-6 (Arduino Pin A5)");

    //element.jSetPinDescription(c++,"/dev/ttyACM0");
    //element.jSetPinDescription(c++,"start");

    element.jSetPinDescription(c++,"Out-1 (Arduino Pin 8)");
    element.jSetPinDescription(c++,"Out-2 (Arduino Pin 9)");
    element.jSetPinDescription(c++,"Out-3 (Arduino Pin 10)");
    element.jSetPinDescription(c++,"Out-4 (Arduino Pin 11)");
    element.jSetPinDescription(c++,"Out-5 (Arduino Pin 12)");
    element.jSetPinDescription(c++,"Out-6 (Arduino Pin 13)");

    String fileName=element.jGetSourcePath()+"icon.png";
    image=element.jLoadImage(fileName);

    element.jSetCaptionVisible(true);
    element.jSetCaption("Arduino I/O JV");
    setName("Arduino I/O JV");
      
      
  }

  public void initInputPins()
  {
    int c=12;
    
    
    //comPort=(VSComboBox)element.getPinInputReference(c++);
    //comStart=(VSBoolean)element.getPinInputReference(c++);
    
    led1=(VSBoolean)element.getPinInputReference(c++);
    led2=(VSBoolean)element.getPinInputReference(c++);
    led3=(VSBoolean)element.getPinInputReference(c++);
    led4=(VSBoolean)element.getPinInputReference(c++);
    led5=(VSBoolean)element.getPinInputReference(c++);
    led6=(VSBoolean)element.getPinInputReference(c++);

    //if (comPort==null) comPort = new VSComboBox("null");
    
    if (comStart==null) comStart = new VSBoolean(false);

    if (led1==null) led1 = new VSBoolean(false);
    if (led2==null) led2 = new VSBoolean(false);
    if (led3==null) led3 = new VSBoolean(false);
    if (led4==null) led4 = new VSBoolean(false);
    if (led5==null) led5 = new VSBoolean(false);
    if (led6==null) led6 = new VSBoolean(false);
  }

  public void initOutputPins()
  {
      int c=0;
      element.setPinOutputReference(c++,taste1);
      element.setPinOutputReference(c++,taste2);
      element.setPinOutputReference(c++,taste3);
      element.setPinOutputReference(c++,taste4);
      element.setPinOutputReference(c++,taste5);
      element.setPinOutputReference(c++,taste6);

      element.setPinOutputReference(c++,adc1);
      element.setPinOutputReference(c++,adc2);
      element.setPinOutputReference(c++,adc3);
      element.setPinOutputReference(c++,adc4);
      element.setPinOutputReference(c++,adc5);
      element.setPinOutputReference(c++,adc6);
}

  public boolean started=false;
  public boolean firstUpdate=false;

  public void start()
  {

    t1=false;
    t2=false;
    t3=false;
    t4=false;
    t5=false;
    t6=false;
  

    timer = new javax.swing.Timer(pollTime.getValue(), new ActionListener() {

          public void actionPerformed(ActionEvent evt)
          {
            if (started && driver!=null)
            {
              byte value=0;
              
              if (led1.getValue() || firstUpdate) value|=(1<<0); else value&=~(1<<0);
              if (led2.getValue() || firstUpdate) value|=(1<<1); else value&=~(1<<1);
              if (led3.getValue() || firstUpdate) value|=(1<<2); else value&=~(1<<2);
              if (led4.getValue() || firstUpdate) value|=(1<<3); else value&=~(1<<3);
              if (led5.getValue() || firstUpdate) value|=(1<<4); else value&=~(1<<4);
              if (led6.getValue() || firstUpdate) value|=(1<<5); else value&=~(1<<5);
              System.out.println("valorCmd:"+value);
              inBytes.setValue(0,value);
              //if (driver!=null) driver.sendCommand("COM"+comPort.getValue()+";SENDBYTES", inBytes);
              //if (driver!=null) driver.sendCommand(Port.getValue()+";SENDBYTES", inBytes);
              if (driver!=null) driver.sendCommand(comport.getItem(comport.selectedIndex)+";SENDBYTES", inBytes);
                  //System.out.println(comport.getItem(comport.selectedIndex)+"SEND"+value);
            }
          }
    });
    


  }

  public static void showMessage(String message)
  {
     JOptionPane.showMessageDialog(null,message,"Attention!",JOptionPane.ERROR_MESSAGE);
  }
  
  public void stop()
  {
    if (started)
    {
      started=false;
      
      if (timer!=null) timer.stop();
      
      //String strCom="COM"+comPort.getValue();
      //String strCom=Port.getValue();
      String strCom=Port.getValue();
      
      driver.sendCommand(comport.getItem(comport.selectedIndex)+";CLOSE", null);
      element.jCloseDriver("MyOpenLab.RS232");
    }
  }

  public void elementActionPerformed(ElementActionEvent evt)
  {
    int idx=evt.getSourcePinIndex();
    System.out.println("Event: "+idx);
    switch (idx)
    {
      case 12:
      {

        //if (!started && comStart.getValue()==true)
        if (started==false)
        {
          started=true;
          firstUpdate=true;
          ArrayList<Object> args = new ArrayList<Object>();


          //args.add(new String("COM"+comPort.getValue()));
          args.add(new String(comport.getItem(comport.selectedIndex)));
          System.out.println("Port_Loaded:"+comport.getItem(comport.selectedIndex));
          args.add(new Integer(115200));
          args.add(new Integer(8));
          args.add(new Integer(1));
          args.add(new Integer(0)); // No Parity

          driver = element.jOpenDriver("MyOpenLab.RS232", args);
          driver.registerOwner(this);
          timer.start();

        }
      }break;
    }
  }
  
  
  boolean changed=false;
  
  public void destElementCalled()
  {
    if (changed)
    {
      //outReceived.setValue(false);
      element.notifyPin(1);
      changed=false;
    }
  }

  
  public void getCommand(String commando, Object value)
  {
    if (value instanceof VS1DByte)
    {
      VS1DByte values=(VS1DByte)value;
      //System.out.println("COMMAND : "+commando);
      
      if (commando.equals("BYTESRECEIVED"))
      {

        int len=values.getLength();
        
        if (len==13)
        {
          byte[] dest = values.getValues();
          System.out.println("destLen="+dest.length);
          byte inputs=dest[0]; // Inputs 1-6
          
          if ((inputs & 1)>0)  t1=true; else t1=false;
          if ((inputs & 2)>0)  t2=true; else t2=false;
          if ((inputs & 4)>0)  t3=true; else t3=false;
          if ((inputs & 8)>0)  t4=true; else t4=false;
          if ((inputs & 16)>0) t5=true; else t5=false;
          if ((inputs & 32)>0) t6=true; else t6=false;

          if (t1!=taste1.getValue() || firstUpdate) {taste1.setValue(t1);element.notifyPin(0);}
          if (t2!=taste2.getValue() || firstUpdate) {taste2.setValue(t2);element.notifyPin(1);}
          if (t3!=taste3.getValue() || firstUpdate) {taste3.setValue(t3);element.notifyPin(2);}
          if (t4!=taste4.getValue() || firstUpdate) {taste4.setValue(t4);element.notifyPin(3);}
          if (t5!=taste5.getValue() || firstUpdate) {taste5.setValue(t5);element.notifyPin(4);}
          if (t6!=taste6.getValue() || firstUpdate) {taste6.setValue(t6);element.notifyPin(5);}
          firstUpdate=false;

          byte highByte=dest[1];
          byte lowByte=dest[2];

          int xadc1=highByte<<8;
          xadc1|=lowByte & 0xFF;

          highByte=dest[3];
          lowByte=dest[4];

          int xadc2=highByte<<8;
          xadc2|=lowByte & 0xFF;


          highByte=dest[5];
          lowByte=dest[6];

          int xadc3=highByte<<8;
          xadc3|=lowByte & 0xFF;


          highByte=dest[7];
          lowByte=dest[8];

          int xadc4=highByte<<8;
          xadc4|=lowByte & 0xFF;

          highByte=dest[9];
          lowByte=dest[10];

          int xadc5=highByte<<8;
          xadc5|=lowByte & 0xFF;

          highByte=dest[11];
          lowByte=dest[12];

          int xadc6=highByte<<8;
          xadc6|=lowByte & 0xFF;

          
          if (xadc1!=adc1.getValue()){ adc1.setValue(xadc1);element.notifyPin(6);}
          if (xadc2!=adc2.getValue()){ adc2.setValue(xadc2);element.notifyPin(7);}
          if (xadc3!=adc3.getValue()){ adc3.setValue(xadc3);element.notifyPin(8);}
          if (xadc4!=adc4.getValue()){ adc4.setValue(xadc4);element.notifyPin(9);}
          if (xadc5!=adc5.getValue()){ adc5.setValue(xadc5);element.notifyPin(10);}
          if (xadc6!=adc6.getValue()){ adc6.setValue(xadc6);element.notifyPin(11);}

          changed=true;
          //element.jNotifyWhenDestCalled(1,element);*/

        }

      }
    }
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
     comport.loadFromStream(fis);
     pollTime.loadFromStream(fis);
     //init();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
     comport.saveToStream(fos);
     pollTime.saveToStream(fos);

  }

    @Override
    public void getSingleByte(int i) {        
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
  
}


