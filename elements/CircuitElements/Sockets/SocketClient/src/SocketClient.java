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
import java.net.*;
import java.io.*;
import javax.swing.*;
import java.util.*;



public class SocketClient extends JVSMain
{
  public  ObjectInputStream serverIn;
  private ObjectOutputStream serverOut;
  private Socket clientSocket;
  

  private VSString ipAdresse=new VSString("localhost");
  private VSInteger port=new VSInteger(4444);
  
  private Image image=null;

  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }


  private boolean connected=false;


  private VSComboBox dtPin0=new VSComboBox();
  private VSComboBox dtPin1=new VSComboBox();
  private VSComboBox dtPin2=new VSComboBox();
  private VSComboBox dtPin3=new VSComboBox();
  private VSComboBox dtPin4=new VSComboBox();
  private VSComboBox dtPin5=new VSComboBox();
  private VSComboBox dtPin6=new VSComboBox();
  private VSComboBox dtPin7=new VSComboBox();

  private VSBoolean inConnect;
  private VSBoolean inDisconnect;
  private VSObject in0;
  private VSObject in1;
  private VSObject in2;
  private VSObject in3;
  private VSObject in4;
  private VSObject in5;
  private VSObject in6;
  private VSObject in7;
  
  private VSObject out0;
  private VSObject out1;
  private VSObject out2;
  private VSObject out3;
  private VSObject out4;
  private VSObject out5;
  private VSObject out6;
  private VSObject out7;

  private boolean started=false;

  public WarteAufObject warteAufObject;

  private int STATUS_NOT_CONNECTED=0;
  private int STATUS_CONNECTED=1;
  private int STATUS_ERROR=2;

  private int status=STATUS_NOT_CONNECTED;

  public void paint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }

  public void init()
  {
    initPins(0,8,0,10);
    element.jSetSize(72,110);
    
    image=element.jLoadImage(element.jGetSourcePath()+"icon.png");
    
    initPinVisibility(false,true,false,true);
    
    //element.jProcessAutomatic(true);

    setPin(0,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
    setPin(2,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
    setPin(3,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
    setPin(4,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
    setPin(5,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
    setPin(6,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
    setPin(7,ExternalIF.C_VARIANT,element.PIN_OUTPUT);

    setPin(8,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(9,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(10,ExternalIF.C_VARIANT,element.PIN_INPUT);
    setPin(11,ExternalIF.C_VARIANT,element.PIN_INPUT);
    setPin(12,ExternalIF.C_VARIANT,element.PIN_INPUT);
    setPin(13,ExternalIF.C_VARIANT,element.PIN_INPUT);
    setPin(14,ExternalIF.C_VARIANT,element.PIN_INPUT);
    setPin(15,ExternalIF.C_VARIANT,element.PIN_INPUT);
    setPin(16,ExternalIF.C_VARIANT,element.PIN_INPUT);
    setPin(17,ExternalIF.C_VARIANT,element.PIN_INPUT);

    element.jSetPinDescription(0,"Out");
    element.jSetPinDescription(1,"Out");
    element.jSetPinDescription(2,"Out");
    element.jSetPinDescription(3,"Out");
    element.jSetPinDescription(4,"Out");
    element.jSetPinDescription(5,"Out");
    element.jSetPinDescription(6,"Out");
    element.jSetPinDescription(7,"Out");
    

    String strLocale=Locale.getDefault().toString();

    if (strLocale.equalsIgnoreCase("de_DE"))
    {
      element.jSetPinDescription(8 ,"Connect");
      element.jSetPinDescription(9 ,"Disconnect");
      element.jSetPinDescription(10 ,"In0");
      element.jSetPinDescription(11 ,"In1");
      element.jSetPinDescription(12,"In2");
      element.jSetPinDescription(13,"In3");
      element.jSetPinDescription(14,"In4");
      element.jSetPinDescription(15,"In5");
      element.jSetPinDescription(16,"In6");
      element.jSetPinDescription(17,"In7");
    }
    if (strLocale.equalsIgnoreCase("en_US"))
    {
      element.jSetPinDescription(8 ,"Connect");
      element.jSetPinDescription(9 ,"Disconnect");
      element.jSetPinDescription(10 ,"In0");
      element.jSetPinDescription(11 ,"In1");
      element.jSetPinDescription(12,"In2");
      element.jSetPinDescription(13,"In3");
      element.jSetPinDescription(14,"In4");
      element.jSetPinDescription(15,"In5");
      element.jSetPinDescription(16,"In6");
      element.jSetPinDescription(17,"In7");
    }
    if (strLocale.equalsIgnoreCase("a"))
    {
      element.jSetPinDescription(8 ,"Conectar");
      element.jSetPinDescription(9 ,"Desconectar");
      element.jSetPinDescription(10 ,"En0");
      element.jSetPinDescription(11 ,"En1");
      element.jSetPinDescription(12,"En2");
      element.jSetPinDescription(13,"En3");
      element.jSetPinDescription(14,"En4");
      element.jSetPinDescription(15,"En5");
      element.jSetPinDescription(16,"En6");
      element.jSetPinDescription(17,"En7");
    }


    String liste[]=element.jGetDataTypeList();

    for (int i=0;i<liste.length;i++)
    {
      dtPin0.addItem(liste[i]);
      dtPin1.addItem(liste[i]);
      dtPin2.addItem(liste[i]);
      dtPin3.addItem(liste[i]);
      dtPin4.addItem(liste[i]);
      dtPin5.addItem(liste[i]);
      dtPin6.addItem(liste[i]);
      dtPin7.addItem(liste[i]);
    }

    dtPin0.selectedIndex=0;
    dtPin1.selectedIndex=0;
    dtPin2.selectedIndex=0;
    dtPin3.selectedIndex=0;
    dtPin4.selectedIndex=0;
    dtPin5.selectedIndex=0;
    dtPin6.selectedIndex=0;
    dtPin7.selectedIndex=0;

    element.jSetCaptionVisible(true);
    element.jSetCaption("Socket Client");

    setName("Socket Client");
  }
  
  public void initInputPins()
  {
    inConnect=(VSBoolean)element.getPinInputReference(8);
    inDisconnect=(VSBoolean)element.getPinInputReference(9);
    in0=(VSObject)element.getPinInputReference(10);
    in1=(VSObject)element.getPinInputReference(11);
    in2=(VSObject)element.getPinInputReference(12);
    in3=(VSObject)element.getPinInputReference(13);
    in4=(VSObject)element.getPinInputReference(14);
    in5=(VSObject)element.getPinInputReference(15);
    in6=(VSObject)element.getPinInputReference(16);
    in7=(VSObject)element.getPinInputReference(17);
  }

  public void initOutputPins()
  {
      out0=element.jCreatePinDataType(0);
      out1=element.jCreatePinDataType(1);
      out2=element.jCreatePinDataType(2);
      out3=element.jCreatePinDataType(3);
      out4=element.jCreatePinDataType(4);
      out5=element.jCreatePinDataType(5);
      out6=element.jCreatePinDataType(6);
      out7=element.jCreatePinDataType(7);

      element.setPinOutputReference(0,out0);
      element.setPinOutputReference(1,out1);
      element.setPinOutputReference(2,out2);
      element.setPinOutputReference(3,out3);
      element.setPinOutputReference(4,out4);
      element.setPinOutputReference(5,out5);
      element.setPinOutputReference(6,out6);
      element.setPinOutputReference(7,out7);

      if (out0==null) out0=new VSObject();
      if (out1==null) out1=new VSObject();
      if (out2==null) out2=new VSObject();
      if (out3==null) out3=new VSObject();
      if (out4==null) out4=new VSObject();
      if (out5==null) out5=new VSObject();
      if (out6==null) out6=new VSObject();
      if (out7==null) out7=new VSObject();
      

      out0.setPin(0);
      out1.setPin(1);
      out2.setPin(2);
      out3.setPin(3);
      out4.setPin(4);
      out5.setPin(5);
      out6.setPin(6);
      out7.setPin(7);
  }
  
  
  public void sendeDaten(int pinIndex,VSObject obj)
  {
    try
    {
      obj.setPinIndex(pinIndex);
      serverOut.reset();
      serverOut.writeObject(obj);
      serverOut.flush();

      //Thread.sleep(100);
      
      System.out.println("sendeDaten:"+pinIndex+"started="+(new Boolean(started)).toString());
    } catch(Exception e)
    {
      System.out.println("Socketclient : SocketSender.sendeDaten:"+e.toString());
    }
   }
   
   
   public void processOutputs()
   {
    if (serverIn!=null && connected==true)
    {
      try
      {
         if (warteAufObject.getObject()!=null)
         {
           Object obj = warteAufObject.getObject();

           if (obj!=null)
           {
             if (obj instanceof VSObject)
             {
               VSObject vs=(VSObject)obj;
               System.out.println("Pin received : PinIndex = "+vs.getPinIndex());
               switch(vs.getPinIndex())
               {
                 case 8  : out0.copyValueFrom(vs); element.addToProcesslist(out0);break;
                 case 9  : out1.copyValueFrom(vs); element.addToProcesslist(out1);break;
                 case 10 : out2.copyValueFrom(vs); element.addToProcesslist(out2);break;
                 case 11 : out3.copyValueFrom(vs); element.addToProcesslist(out3);break;
                 case 12 : out4.copyValueFrom(vs); element.addToProcesslist(out4);break;
                 case 13 : out5.copyValueFrom(vs); element.addToProcesslist(out5);break;
                 case 14 : out6.copyValueFrom(vs); element.addToProcesslist(out6);break;
                 case 15 : out7.copyValueFrom(vs); element.addToProcesslist(out7);break;
               }

             }

           } else
           {
            System.out.println("OBJECT =NULL!!!!");
           }
           warteAufObject.resetObject();
         } else
         {
            /*if (out0!=null) out0.setChanged(false);
            if (out1!=null) out1.setChanged(false);
            if (out2!=null) out2.setChanged(false);
            if (out3!=null) out3.setChanged(false);
            if (out4!=null) out4.setChanged(false);
            if (out5!=null) out5.setChanged(false);
            if (out6!=null) out6.setChanged(false);
            if (out7!=null) out7.setChanged(false);*/
         }

      }catch(Exception ex)
      {
         System.out.println("Fehler in Process : "+ex);
      }
      }

   }

  public void process()
  {
  
    if(inConnect!=null && inConnect.getValue()==true && connected==false)
    {
        try
        {
            int intPort=port.getValue();

            System.out.println("Client gestartet");
            clientSocket = new Socket(ipAdresse.getValue(),intPort);

            serverOut= new ObjectOutputStream (clientSocket.getOutputStream());
            serverIn = new ObjectInputStream(clientSocket.getInputStream());

            System.out.println(clientSocket.getRemoteSocketAddress());


            Thread thread= new WarteAufObject(serverIn,this);
            thread.start();
            warteAufObject=(WarteAufObject)thread;

            started=true;

            System.out.println("Client OK");

        } catch (Exception ex)
        {
          System.out.println(ex.toString());
          status=STATUS_ERROR;
        }
        connected=true;
    }
    
    if (serverOut!=null)
    {
      if (inDisconnect!=null && inDisconnect.isChanged() && inDisconnect.getValue()==true)
      {
       started=false;
       if (clientSocket!=null) disconnectServer();
       connected=false;
      }

      if (connected==true)
      {
        if (in0!=null && (in0.isChanged() || started==true) ) sendeDaten(0,in0);
        if (in1!=null && (in1.isChanged() || started==true) ) sendeDaten(1,in1);
        if (in2!=null && (in2.isChanged() || started==true) ) sendeDaten(2,in2);
        if (in3!=null && (in3.isChanged() || started==true) ) sendeDaten(3,in3);
        if (in4!=null && (in4.isChanged() || started==true) ) sendeDaten(4,in4);
        if (in5!=null && (in5.isChanged() || started==true) ) sendeDaten(5,in5);
        if (in6!=null && (in6.isChanged() || started==true) ) sendeDaten(6,in6);
        if (in7!=null && (in7.isChanged() || started==true) ) sendeDaten(7,in7);
        started=false;
      }
    }

    

  }

  public void setPropertyEditor()
  {
    element.jAddPEItem("ipAdresse",ipAdresse, 0,0);
    element.jAddPEItem("port",port, 0,65535);

    element.jAddPEItem("DT OutPin0",dtPin0, 0,0);
    element.jAddPEItem("DT OutPin1",dtPin1, 0,0);
    element.jAddPEItem("DT OutPin2",dtPin2, 0,0);
    element.jAddPEItem("DT OutPin3",dtPin3, 0,0);
    element.jAddPEItem("DT OutPin4",dtPin4, 0,0);
    element.jAddPEItem("DT OutPin5",dtPin5, 0,0);
    element.jAddPEItem("DT OutPin6",dtPin6, 0,0);
    element.jAddPEItem("DT OutPin7",dtPin7, 0,0);
  }

  private void setPinDT2(int pinIndex,VSComboBox dtPin)
  {
    int dt=element.jGetDataType(dtPin.getItem(dtPin.selectedIndex));
    setPin(pinIndex,dt,element.PIN_OUTPUT);
  }


  private void setPinDT(int pinIndex,Object o,VSComboBox dtPin)
  {
    if (o.equals(dtPin))
    {
      setPinDT2(pinIndex,dtPin);
    }
  }

  public void propertyChanged(Object o)
  {
    setPinDT(0,o,dtPin0);
    setPinDT(1,o,dtPin1);
    setPinDT(2,o,dtPin2);
    setPinDT(3,o,dtPin3);
    setPinDT(4,o,dtPin4);
    setPinDT(5,o,dtPin5);
    setPinDT(6,o,dtPin6);
    setPinDT(7,o,dtPin7);

    element.jRepaint();
  }



  public void start()
  {
    connected=false;
  }
  
  private void disconnectServer()
  {

    try
    {
      serverOut.writeObject(new String("exit"));
      serverOut.flush();
      serverOut.reset();

      clientSocket.close();

      serverOut.close();
    } catch(IOException ioe)
    {
      System.out.println(ioe.toString());
    }
  }

  public void stop()
  {
     started=false;
     if (clientSocket!=null) disconnectServer();
  }

  
  public void checkPinDataType()
  {
    for (int i=10;i<17;i++)
    {
      boolean pinIn=element.hasPinWire(i);

      int dt=element.C_VARIANT;

      if (pinIn==true)dt=element.jGetPinDrahtSourceDataType(i);

      element.jSetPinDataType(i,dt);
      element.jSetPinIO(i, element.PIN_INPUT);
    }

    element.jRepaint();
  }

  
  public void loadFromStream(java.io.FileInputStream fis)
  {
    ipAdresse.loadFromStream(fis);
    port.loadFromStream(fis);
    dtPin0.loadFromStream(fis);
    dtPin1.loadFromStream(fis);
    dtPin2.loadFromStream(fis);
    dtPin3.loadFromStream(fis);
    dtPin4.loadFromStream(fis);
    dtPin5.loadFromStream(fis);
    dtPin6.loadFromStream(fis);
    dtPin7.loadFromStream(fis);

    setPinDT2(0,dtPin0);
    setPinDT2(1,dtPin1);
    setPinDT2(2,dtPin2);
    setPinDT2(3,dtPin3);
    setPinDT2(4,dtPin4);
    setPinDT2(5,dtPin5);
    setPinDT2(6,dtPin6);
    setPinDT2(7,dtPin7);

  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    ipAdresse.saveToStream(fos);
    port.saveToStream(fos);
    
    dtPin0.saveToStream(fos);
    dtPin1.saveToStream(fos);
    dtPin2.saveToStream(fos);
    dtPin3.saveToStream(fos);
    dtPin4.saveToStream(fos);
    dtPin5.saveToStream(fos);
    dtPin6.saveToStream(fos);
    dtPin7.saveToStream(fos);
  }

}




  class WarteAufObject extends Thread
  {
    private Object obj=null;
    public ObjectInputStream serverIn;
    private SocketClient client;

    public WarteAufObject(ObjectInputStream serverIn,  SocketClient client)
    {
      this.serverIn=serverIn;
      this.client=client;
    }

    public void resetObject()
    {
      obj=null;
    }

    public Object getObject()
    {
      return obj;
    }

    public void run()
    {
      while(serverIn!=null)
      {
        try
        {
          obj = (Object)serverIn.readObject();
          if (obj!=null)
          {
            client.processOutputs();
          }

        }catch(Exception ex)
        {
           System.out.println("Fehler in Process : "+ex);
           break;
        }

      }
    }
  }

