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


public class Demultiplexer extends JVSMain
{
  private int anzAdr=3;
  private int anzOut=8;

  public VSBoolean[] inAdress;
  public VSBoolean in;
  public VSBoolean[] out = new VSBoolean[anzOut];


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

    initPins(0,anzOut,0,anzAdr+1);
    setSize(57+1+10,34+(10*(anzOut)));

    initPinVisibility(false,true,false,true);
    element.jSetInnerBorderVisibility(false);
    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");


    for (int i=0;i<anzOut;i++)
    {
      setPin(i,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
      element.jSetPinDescription(i,"x"+i);
    }
    for (int i=0;i<anzAdr;i++)
    {
      setPin(anzOut+i,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
      element.jSetPinDescription(anzOut+i,"S"+i);
    }

    setPin(anzOut+anzAdr,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    element.jSetPinDescription(anzOut+anzAdr,"y");
    



    element.jSetCaptionVisible(true);
    element.jSetCaption("Demuxltiplexer");
    setName("Demuxltiplexer");
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
    in= (VSBoolean)element.getPinInputReference(anzOut+anzAdr);
    
    if (in==null)
    {
      in=new VSBoolean(false);
    }
    
    inAdress= new VSBoolean[anzAdr];
    for (int i=0;i<anzAdr;i++)
    {
      inAdress[i]=(VSBoolean)element.getPinInputReference(anzOut+i);
      if (inAdress[i]==null)
      {
        inAdress[i]=new VSBoolean(false);
      }
    }

  }

  public void initOutputPins()
  {
    for (int i=0;i<anzOut;i++)
    {
      out[i]=new VSBoolean(false);
      element.setPinOutputReference(i,out[i]);
    }
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
    
    for (int i=0;i<out.length;i++)
    {
      if (i==adr)
      {
        out[i].setValue(in.getValue());
      }else
      {
        out[i].setValue(false);
      }
      element.notifyPin(i);
    }
//    System.out.println("in="+in.getValue());


  }






}
