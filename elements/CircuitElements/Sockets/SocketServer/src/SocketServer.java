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

  class WarteAufObject extends Thread
  {
    private Object obj=null;
    public ObjectInputStream serverIn;
    SocketServer client;

    public WarteAufObject(ObjectInputStream serverIn,  SocketServer client)
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


class Server extends Thread
{

  public ObjectInputStream serverIn;
  public ObjectOutputStream serverOut;

  public boolean started=false;
  
  public Socket clientSocket;
  public ServerSocket serverSocket;
  public WarteAufObject warteAufObject;
  
  private SocketServer server;

  public void init(SocketServer server)
  {
    this.server=server;
    try
    {
      serverSocket = new ServerSocket(4444);
    } catch(Exception ex)
    {
      System.out.println("SocketServer.init(): "+ex.toString());
    }
  }

  public void run()
  {
     while (true)
     {
        try
        {
          System.out.println("Auf Verbindung Warten...");

          clientSocket=serverSocket.accept();

          serverIn = new ObjectInputStream(clientSocket.getInputStream());
          serverOut= new ObjectOutputStream (clientSocket.getOutputStream());

          System.out.println("Verbindung zu Socket " + clientSocket.getRemoteSocketAddress()+ " aufgenommen");

          server.processOutputs();

          Thread thread= new WarteAufObject(serverIn,server);
          thread.start();
          warteAufObject=(WarteAufObject)thread;
          
          started=true;

        } catch(IOException ioe)
        {
          System.out.println("SocketServer.run(): "+ioe.toString());
          break;
        }

     }
  }
  

  public void disconnectServer()
  {
    if (clientSocket!=null)
    {
      try
      {
        if (serverIn!=null)
        {
          serverIn.close();
          serverIn=null;
        }

        if (serverOut!=null)
        {
          serverOut.close();
          serverOut=null;
        }
        
        if (clientSocket!=null)
        {
         if (clientSocket.isClosed()==false) clientSocket.close();
        }

        //serverOut=null;
        //clientSocket=null;
      } catch(IOException ioe)
      {
        System.out.println(ioe.toString());
      }
    }
    
  }

}





public class SocketServer extends JVSMain
{
  private VSInteger port=new VSInteger(4444);
  
  private Image image=null;

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
  

  
  private VSComboBox dtPin0=new VSComboBox();
  private VSComboBox dtPin1=new VSComboBox();
  private VSComboBox dtPin2=new VSComboBox();
  private VSComboBox dtPin3=new VSComboBox();
  private VSComboBox dtPin4=new VSComboBox();
  private VSComboBox dtPin5=new VSComboBox();
  private VSComboBox dtPin6=new VSComboBox();
  private VSComboBox dtPin7=new VSComboBox();
  
  private Server server=null;

  private int STATUS_NOT_CONNECTED=0;
  private int STATUS_CONNECTED=1;
  private int STATUS_ERROR=2;

  private int status=STATUS_NOT_CONNECTED;

  public void paint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }
  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }


  public void init()
  {
    initPins(0,8,0,8);
    element.jSetSize(72,92);
    
    image=element.jLoadImage(element.jGetSourcePath()+"icon.png");
    
    //element.jProcessAutomatic(true);
    
    initPinVisibility(false,true,false,true);

    setPin(0,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
    setPin(2,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
    setPin(3,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
    setPin(4,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
    setPin(5,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
    setPin(6,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
    setPin(7,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
    
    setPin(8,ExternalIF.C_VARIANT,element.PIN_INPUT);
    setPin(9,ExternalIF.C_VARIANT,element.PIN_INPUT);
    setPin(10,ExternalIF.C_VARIANT,element.PIN_INPUT);
    setPin(11,ExternalIF.C_VARIANT,element.PIN_INPUT);
    setPin(12,ExternalIF.C_VARIANT,element.PIN_INPUT);
    setPin(13,ExternalIF.C_VARIANT,element.PIN_INPUT);
    setPin(14,ExternalIF.C_VARIANT,element.PIN_INPUT);
    setPin(15,ExternalIF.C_VARIANT,element.PIN_INPUT);



    element.jSetPinDescription(0,"Out");
    element.jSetPinDescription(1,"Out");
    element.jSetPinDescription(2,"Out");
    element.jSetPinDescription(3,"Out");
    element.jSetPinDescription(4,"Out");
    element.jSetPinDescription(5,"Out");
    element.jSetPinDescription(6,"Out");
    element.jSetPinDescription(7,"Out");
    
    element.jSetPinDescription(8 ,"In0");
    element.jSetPinDescription(9 ,"In1");
    element.jSetPinDescription(10,"In2");
    element.jSetPinDescription(11,"In3");
    element.jSetPinDescription(12,"In4");
    element.jSetPinDescription(13,"In5");
    element.jSetPinDescription(14,"In6");
    element.jSetPinDescription(15,"In7");


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
    element.jSetCaption("Socket Server");

    setName("Socket Server");
  }
  
  public void initInputPins()
  {
    in0=(VSObject)element.getPinInputReference(8);
    in1=(VSObject)element.getPinInputReference(9);
    in2=(VSObject)element.getPinInputReference(10);
    in3=(VSObject)element.getPinInputReference(11);
    in4=(VSObject)element.getPinInputReference(12);
    in5=(VSObject)element.getPinInputReference(13);
    in6=(VSObject)element.getPinInputReference(14);
    in7=(VSObject)element.getPinInputReference(15);
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


  public void closeAll()
  {
    server.disconnectServer();
  }

  public void sendeDaten(ObjectOutputStream serverOut,int pinIndex,VSObject obj)
  {
    try
    {
      System.out.println("sendeDaten:"+pinIndex);
      obj.setPinIndex(pinIndex);
      serverOut.reset();
      serverOut.writeObject(obj);
      serverOut.flush();

      //Thread.sleep(1);

    } catch(Exception e)
    {
      System.out.println("Socketserver : SocketSender.sendeDaten:"+e.toString());
    }
   }



   public void processOutputs()
   {
    if (server!=null && server.serverIn!=null)
    {
      try
      {
         ObjectInputStream serverIn=server.serverIn;

         if (server.warteAufObject.getObject()!=null)
         {
           Object obj = server.warteAufObject.getObject();
           server.warteAufObject.resetObject();

           if (obj!=null)
           {
             if (obj instanceof VSObject)
             {
               VSObject vs=(VSObject)obj;
               System.out.println("Empfangen am PinIndex = "+vs.getPinIndex());
               switch(vs.getPinIndex())
               {
                 case 0 : out0.copyValueFrom(vs); out0.setChanged(true);element.addToProcesslist(out0);break;
                 case 1 : out1.copyValueFrom(vs); out1.setChanged(true);element.addToProcesslist(out1);break;
                 case 2 : out2.copyValueFrom(vs); out2.setChanged(true);element.addToProcesslist(out2);break;
                 case 3 : out3.copyValueFrom(vs); out3.setChanged(true);element.addToProcesslist(out3);break;
                 case 4 : out4.copyValueFrom(vs); out4.setChanged(true);element.addToProcesslist(out4);break;
                 case 5 : out5.copyValueFrom(vs); out5.setChanged(true);element.addToProcesslist(out5);break;
                 case 6 : out6.copyValueFrom(vs); out6.setChanged(true);element.addToProcesslist(out6);break;
                 case 7 : out7.copyValueFrom(vs); out7.setChanged(true);element.addToProcesslist(out7);break;
               }

             }

             if (obj instanceof String)
             {
               System.out.println("Commando : exit");
               closeAll();
             }
           } else
           {
            System.out.println("OBJECT =NULL!!!!");
           }


         } else
         {
            /*if (out0!=null) {out0.setChanged(false);element.addToProcesslist(out0);}
            if (out1!=null) {out1.setChanged(false);element.addToProcesslist(out1);}
            if (out2!=null) {out2.setChanged(false);element.addToProcesslist(out2);}
            if (out3!=null) {out3.setChanged(false);element.addToProcesslist(out3);}
            if (out4!=null) {out4.setChanged(false);element.addToProcesslist(out4);}
            if (out5!=null) {out5.setChanged(false);element.addToProcesslist(out5);}
            if (out6!=null) {out6.setChanged(false);element.addToProcesslist(out6);}
            if (out7!=null) {out7.setChanged(false);element.addToProcesslist(out7);}*/
         }

      }catch(Exception ex)
      {
         System.out.println("Fehler in Process : "+ex);
      }
    } else
    {
      if (out0!=null) out0.setChanged(false);
      if (out1!=null) out1.setChanged(false);
      if (out2!=null) out2.setChanged(false);
      if (out3!=null) out3.setChanged(false);
      if (out4!=null) out4.setChanged(false);
      if (out5!=null) out5.setChanged(false);
      if (out6!=null) out6.setChanged(false);
      if (out7!=null) out7.setChanged(false);
   }

   }


  public synchronized void process()
  {

    if (server!=null && server.serverOut!=null)
    {
      ObjectOutputStream serverOut=server.serverOut;


      if (serverOut!=null)
      {
        if (in0!=null && (in0.isChanged() || server.started) ) sendeDaten(serverOut,8,in0);
        if (in1!=null && (in1.isChanged() || server.started) ) sendeDaten(serverOut,9,in1);
        if (in2!=null && (in2.isChanged() || server.started) ) sendeDaten(serverOut,10,in2);
        if (in3!=null && (in3.isChanged() || server.started) ) sendeDaten(serverOut,11,in3);
        if (in4!=null && (in4.isChanged() || server.started) ) sendeDaten(serverOut,12,in4);
        if (in5!=null && (in5.isChanged() || server.started) ) sendeDaten(serverOut,13,in5);
        if (in6!=null && (in6.isChanged() || server.started) ) sendeDaten(serverOut,14,in6);
        if (in7!=null && (in7.isChanged() || server.started) ) sendeDaten(serverOut,15,in7);
        if (server!=null) server.started=false;
      }
    }
  
  }


  public void checkPinDataType()
  {
  
    for (int i=8;i<16;i++)
    {
      boolean pinIn=element.hasPinWire(i);

      int dt=element.C_VARIANT;

      if (pinIn==true)dt=element.jGetPinDrahtSourceDataType(i);

      element.jSetPinDataType(i,dt);
      element.jSetPinIO(i, element.PIN_INPUT);

    }

    element.jRepaint();
  }


  public void setPropertyEditor()
  {
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
    Thread thread= new Server();

    
    server=(Server)thread;
    server.init(this);
    thread.start();
    started=true;

  }
  
  


  public void stop()
  {
     started=false;
     if (server!=null)server.started=false;
     if (server!=null)
     {
       if (server.warteAufObject!=null) server.warteAufObject.serverIn=null;
       if (server.warteAufObject!=null) server.warteAufObject=null;
       server.disconnectServer();
       try
       {
         server.serverSocket.close();
       } catch(Exception ex)
       {
         System.out.println("SocketServer.stop() :"+ex);
       }

     }
  }

  
  public void loadFromStream(java.io.FileInputStream fis)
  {
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
