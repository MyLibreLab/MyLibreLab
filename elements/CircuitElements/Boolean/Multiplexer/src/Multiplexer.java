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
import javax.swing.*;


public class Multiplexer extends JVSMain
{
  public VSBoolean[] inAdress;
  public VSBoolean[] in;
  public VSBoolean outQ1=new VSBoolean(false);
  public VSBoolean outQ2=new VSBoolean(false);
  
  
  private int anzAdr=3;
  private int anzIn=8;
  
  private boolean setted=false;

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
    int anzPins=anzAdr+anzIn;
    initPins(0,2,0,anzPins);
    setSize(57+1+10,34+(10*anzPins));
    initPinVisibility(false,true,false,true);
    element.jSetInnerBorderVisibility(false);
    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");

    setPin(0,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    element.jSetPinDescription(0,"Y1");
    element.jSetPinDescription(1,"Y2");
    
    for (int i=0;i<anzAdr;i++)
    {
      setPin(2+i,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
      element.jSetPinDescription(2+i,"S"+i);
    }

    for (int i=0;i<anzIn;i++)
    {
      setPin(anzAdr+2+i,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
      element.jSetPinDescription(anzAdr+2+i,"x"+i);
    }


    element.jSetCaptionVisible(true);
    element.jSetCaption("Muxltiplexer");
    setName("Muxltiplexer");
    //element.jSetInfo("Carmelo Salafia","Open Source 2007","");
  }
  
  

  public void propertyChanged(Object o)
  {
  }

  public void setPropertyEditor()
  {
  }


  public void initInputPins()
  {
     inAdress= new VSBoolean[anzAdr];
     in= new VSBoolean[anzIn];
     
    for (int i=0;i<anzAdr;i++)
    {
      inAdress[i]=(VSBoolean)element.getPinInputReference(2+i);
      if (inAdress[i]==null)
      {
        inAdress[i]=new VSBoolean(false);
      }
    }

    for (int i=0;i<anzIn;i++)
    {
      in[i]=(VSBoolean)element.getPinInputReference(anzAdr+2+i);
      if (in[i]==null)
      {
        in[i]=new VSBoolean(false);
      }
    }
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,outQ1);
    element.setPinOutputReference(1,outQ2);
  }

  public void start()
  {
  }
  
  public void stop()
  {
  }
  
  private int bolToInt(boolean d, boolean c, boolean b, boolean a)
  {
    int result=0;
    if (a) result+=1;
    if (b) result+=2;
    if (c) result+=4;
    if (d) result+=8;
    
    return result;
  }

  public void process()
  {
  

    int adr = bolToInt(false,inAdress[2].getValue(),inAdress[1].getValue(),inAdress[0].getValue());
    
    //System.out.println("Adresse="+adr);
    
    if (in[adr].getValue()==true)
    {
     outQ1.setValue(true);
     outQ2.setValue(false);
    } else
    {
     outQ1.setValue(false);
     outQ2.setValue(true);
    }
    element.notifyPin(0);
    element.notifyPin(1);

  }






}
