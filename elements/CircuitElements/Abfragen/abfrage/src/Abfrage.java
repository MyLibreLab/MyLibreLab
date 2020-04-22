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
import java.util.*;



public class Abfrage extends JVSMain
{
  private Image image;
  private VSObject inA;
  private VSBoolean inBool;
  private VSObject inB;
  private VSObject out;
  private VSObject theNull=new VSObject();

  private boolean changed=false;


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
    initPins(0,1,0,3);
    setSize(62,40);
    element.jSetInnerBorderVisibility(false);
    element.jSetTopPinsVisible(false);
    element.jSetBottomPinsVisible(false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    setPin(0,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_VARIANT,element.PIN_INPUT);
    setPin(2,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(3,ExternalIF.C_VARIANT,element.PIN_INPUT);


    String strLocale=Locale.getDefault().toString();

    if (strLocale.equalsIgnoreCase("de_DE"))
    {
      element.jSetPinDescription(0,"Out");
      element.jSetPinDescription(1,"a");
      element.jSetPinDescription(2,"if (true) out=a else out=b");
      element.jSetPinDescription(3,"b");
    }
    if (strLocale.equalsIgnoreCase("en_US"))
    {
      element.jSetPinDescription(0,"Out");
      element.jSetPinDescription(1,"a");
      element.jSetPinDescription(2,"if (true) out=a else out=b");
      element.jSetPinDescription(3,"b");
    }
    if (strLocale.equalsIgnoreCase("es_ES"))
    {
      element.jSetPinDescription(0,"Salida");
      element.jSetPinDescription(1,"a");
      element.jSetPinDescription(2,"Si (cierto) salida=a si no salida=b");
      element.jSetPinDescription(3,"b");
    }


    setName("Abfrage");

  }
  
  int C_A=1;
  int C_B=3;
  int C_OUT=0;
  
  public void checkPinDataType()
  {
  
    boolean pinAWire=element.hasPinWire(1);
    boolean pinBWire=element.hasPinWire(3);
    



    if (pinAWire==false && pinBWire==false)
    {
      element.jSetPinDataType(C_OUT,element.C_VARIANT);
      element.jSetPinDataType(C_A,element.C_VARIANT);
      element.jSetPinDataType(C_B,element.C_VARIANT);
      
    } else
    if (pinAWire==true && pinBWire==false)
    {
      int aDataType=element.jGetPinDrahtSourceDataType(C_A);
      element.jSetPinDataType(C_A,aDataType);
      element.jSetPinDataType(C_B,aDataType);
      element.jSetPinDataType(C_OUT,aDataType);
      //System.out.println("typ A ="+aDataType);
    } else
    if (pinAWire==false && pinBWire==true)
    {
      int bDataType=element.jGetPinDrahtSourceDataType(C_B);
      element.jSetPinDataType(C_A,bDataType);
      element.jSetPinDataType(C_B,bDataType);
      element.jSetPinDataType(C_OUT,bDataType);
      //System.out.println("typ B ="+bDataType);
    } else
    if (pinAWire==true && pinBWire==true)
    {
      int aDataType=element.jGetPinDrahtSourceDataType(C_A);
      element.jSetPinDataType(C_A,aDataType);
      element.jSetPinDataType(C_B,aDataType);
      element.jSetPinDataType(C_OUT,aDataType);
      //System.out.println("typ B ="+bDataType);
    } else

    element.jRepaint();
  }
  
  public void initInputPins()
  {
    inA=(VSObject)element.getPinInputReference(1);
    inBool=(VSBoolean)element.getPinInputReference(2);
    inB=(VSObject)element.getPinInputReference(3);
    
    /*if (inA==null) inA=VSObject();
    if (inBool==null) inBool=VSBoolean(false);
    if (inB==null) inB=VSObject();*/
  }

  public void initOutputPins()
  {

    out=element.jCreatePinDataType(1);

    element.setPinOutputReference(0,out);
    
    theNull=element.jCreatePinDataType(1);
  
    if (out==null)
    {
      element.jShowMessage("out==null");
    }

  }


  public void start()
  {

  }


  public void process()
  {

      if (inBool==null)
      {
        element.jShowMessage("inBool==null");
        return;
      }



      if (inBool.getValue()==true)
      {
        if (inA!=null )
        {
          out.copyValueFrom(inA);
        } else
        {
          out.copyValueFrom(theNull);
        }
      }else
      {
        if (inB!=null )
        {
          out.copyValueFrom(inB);
        } else
        {
         out.copyValueFrom(theNull);
        }
      }

      out.setChanged(true);
      element.setPinOutputReference(0,out);
      element.notifyPin(0);
  }
}



