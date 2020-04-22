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




public class Node extends JVSMain
{

   int pin0_dt=ExternalIF.C_VARIANT;
   int pin0_io=ExternalIF.PIN_INPUT_OUTPUT;

   int pin1_dt=ExternalIF.C_VARIANT;
   int pin1_io=ExternalIF.PIN_INPUT_OUTPUT;

   int pin2_dt=ExternalIF.C_VARIANT;
   int pin2_io=ExternalIF.PIN_INPUT_OUTPUT;

   int pin3_dt=ExternalIF.C_VARIANT;
   int pin3_io=ExternalIF.PIN_INPUT_OUTPUT;
   
   VSObject inOut[] = new VSObject[4];
   
   private boolean isLoading=false;
   
   public void paint(java.awt.Graphics g)
   {
    if (element!=null)
    {
       Rectangle bounds=element.jGetBounds();
       g.setColor(Color.BLACK);
       //g.drawRect(bounds.x,bounds.y,bounds.width,bounds.height);
       int d=0;
       g.fillOval(bounds.x+d,bounds.y+d,bounds.width+1-d*2,bounds.height+1-d*2);
    }
   }
  public void init()
  {
    initPins(1,1,1,1);
    setSize(22,22+4);

    initPinVisibility(true,true,true,true);

    element.jSetInnerBorderVisibility(false);
    element.jSetBorderVisibility(false);
    

    setPin(0,element.C_VARIANT,ExternalIF.PIN_INPUT_OUTPUT);
    setPin(1,element.C_VARIANT,ExternalIF.PIN_INPUT_OUTPUT);
    setPin(2,element.C_VARIANT,ExternalIF.PIN_INPUT_OUTPUT);
    setPin(3,element.C_VARIANT,ExternalIF.PIN_INPUT_OUTPUT);
    
    setName("###NODE###");
  }


  public void xOnInit()
  {

  }


  public void initInputPins()
  {
    int io;
    for (int i=0;i<4;i++)
    {
      io=element.jGetPinIO(i);
      if (io==ExternalIF.PIN_INPUT)
      {
        inOut[i]=(VSObject)element.getPinInputReference(i);
      }
    }

  }

  public void initOutputPins()
  {
    int io;
    for (int i=0;i<4;i++)
    {
      io=element.jGetPinIO(i);
      if (io==ExternalIF.PIN_OUTPUT)
      {
        inOut[i]=element.jCreatePinDataType(i);
        element.setPinOutputReference(i,inOut[i]);
      }
    }
  }


  public void checkPinDataType()
  {
    if (isLoading) return;
    
    boolean pinA=element.hasPinWire(0);
    boolean pinB=element.hasPinWire(1);
    boolean pinC=element.hasPinWire(2);
    boolean pinD=element.hasPinWire(3);

    //int dt=element.C_VARIANT;

    /*if (pinA==false) element.jSetPinIO(0, element.PIN_INPUT_OUTPUT);
    if (pinB==false) element.jSetPinIO(1, element.PIN_INPUT_OUTPUT);
    if (pinC==false) element.jSetPinIO(2, element.PIN_INPUT_OUTPUT);
    if (pinD==false) element.jSetPinIO(3, element.PIN_INPUT_OUTPUT);*/

    if (pinA==false) element.jSetPinVisible(0,false); else element.jSetPinVisible(0,true);
    if (pinB==false) element.jSetPinVisible(1,false); else element.jSetPinVisible(1,true);
    if (pinC==false) element.jSetPinVisible(2,false); else element.jSetPinVisible(2,true);
    if (pinD==false) element.jSetPinVisible(3,false); else element.jSetPinVisible(3,true);

    element.jRepaint();
  }


  private boolean isOnePinTrue()
  {
    int io;
    for (int i=0;i<4;i++)
    {
      io=element.jGetPinIO(i);
      if (io==ExternalIF.PIN_INPUT)
      {
        if (inOut[i] instanceof VSBoolean)
        {
          VSBoolean inX=(VSBoolean)inOut[i];
          if (inX.getValue()==true) return true;
        }
      }
    }
    return false;
  }


  public void elementActionPerformed(ElementActionEvent evt)
  {

    int idx=evt.getSourcePinIndex();
    
    VSObject vv=inOut[idx];
    int nodeDT=element.jGetPinDataType(0);
    
    int io;
    
    if (nodeDT==ExternalIF.C_BOOLEAN)
    {
      // Wenn ein Pin True ist müssen alle Ausgänge True sein!
      // 1. Analyse ob ein anderer Pin true hat

      boolean ok=isOnePinTrue();

      for (int i=0;i<4;i++)
      {
        io=element.jGetPinIO(i);

        if (io==ExternalIF.PIN_OUTPUT)
        {
          if (inOut[i] instanceof VSBoolean)
          {
            VSBoolean outX=(VSBoolean)inOut[i];
            outX.setValue(ok);
            inOut[i].setChanged(true);
            element.notifyPin(i);
          }
        }
      }

    } else
    {

      for (int i=0;i<4;i++)
      {
        io=element.jGetPinIO(i);

        if (io==ExternalIF.PIN_OUTPUT)
        {
          inOut[i].copyValueFrom(vv);
          inOut[i].setChanged(true);
          element.notifyPin(i);
        }
      }
    }




  }


  public void initX()
  {
    setPin(0,pin0_dt,(byte)pin0_io);
    setPin(1,pin1_dt,(byte)pin1_io);
    setPin(2,pin2_dt,(byte)pin2_io);
    setPin(3,pin3_dt,(byte)pin3_io);
  }
  
  public void loadFromStream(java.io.FileInputStream fis)
  {
    try
    {
        isLoading=true;
        java.io.DataInputStream dis = new java.io.DataInputStream(fis);

        int dt,io;
        
        dt=dis.readInt();
        io=dis.readInt();
        pin0_dt=dt;
        pin0_io=io;
        
        dt=dis.readInt();
        io=dis.readInt();
        pin1_dt=dt;
        pin1_io=io;

        dt=dis.readInt();
        io=dis.readInt();
        pin2_dt=dt;
        pin2_io=io;

        dt=dis.readInt();
        io=dis.readInt();
        pin3_dt=dt;
        pin3_io=io;

        initX();
        isLoading=false;
        
        /*for (int i=0;i<4;i++)
        {
          System.out.println("Read Pin["+i+"] dt="+element.jGetPinDataType(i));
          System.out.println("Read Pin["+i+"] io="+element.jGetPinIO(i));
        } */
        

    } catch(Exception ex)
    {

    }
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    try
    {
        java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);

        dos.writeInt(element.jGetPinDataType(0));
        dos.writeInt(element.jGetPinIO(0));

        dos.writeInt(element.jGetPinDataType(1));
        dos.writeInt(element.jGetPinIO(1));

        dos.writeInt(element.jGetPinDataType(2));
        dos.writeInt(element.jGetPinIO(2));

        dos.writeInt(element.jGetPinDataType(3));
        dos.writeInt(element.jGetPinIO(3));
        
        
        /*for (int i=0;i<4;i++)
        {
          System.out.println("Write Pin["+i+"] dt="+element.jGetPinDataType(i));
          System.out.println("Write Pin["+i+"] io="+element.jGetPinIO(i));
        } */


    } catch(Exception ex)
    {
    }

  }

}



