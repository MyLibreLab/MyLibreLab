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
import java.awt.*;
import java.awt.event.*;
import tools.*;
import javax.swing.*;


public class CElement extends JVSMain
{
  private Image image;
  private VSComboBox dtPin=new VSComboBox();
  private VSObject out;
  private int pinDTX=-1;
  
  public void paint(java.awt.Graphics g)
  {
    if (image!=null)drawImageCentred(g,image);
  }

  public void beforeInit(String[] args)
  {
    if (args!=null)
    {
       if (args.length>=1)
       {
         pinDTX=Integer.parseInt(args[0]);
       }
    }
  }

  public void init()
  {
    initPins(0,1,0,0);
    setSize(35,27);
    initPinVisibility(false,true,false,false);
    
    element.jSetInnerBorderVisibility(true);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    setPin(0,element.C_VARIANT,element.PIN_OUTPUT);
    
    String liste[]=element.jGetDataTypeList();

    for (int i=0;i<liste.length;i++)
    {
      dtPin.addItem(liste[i]);
    }

    dtPin.selectedIndex=0;
    
    if (pinDTX>-1)
    {
      dtPin.selectedIndex=pinDTX;
    }

    element.jSetResizable(false);
    element.jSetCaptionVisible(true);
    element.jSetCaption("i");

    setName("#PININPUT_INTERNAL#");
   }


  public void setPropertyEditor()
  {
    element.jAddPEItem("DT Input-Pin",dtPin, 0,0);

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
    setPinDT(0,o,dtPin);

    element.jRepaint();
  }

  
  public void process()
  {
  }

  public void initOutputPins()
  {
    /*  out=element.jCreatePinDataType(0);
      element.setPinOutputReference(0,out);*/
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
    try
    {
     dtPin.loadFromStream(fis);
     setPinDT2(0,dtPin);
     element.jRepaint();
     
    } catch (Exception ex){  }

  }


  public void saveToStream(java.io.FileOutputStream fos)
  {
    dtPin.saveToStream(fos);
  }



}

