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

public class BinDec extends JVSMain
{
  private VSInteger anzPins = new VSInteger(4);
  private VSBoolean[] in;
  private VSInteger out=new VSInteger(0);
  private Font font=new Font("Courier",Font.BOLD,8);

    
  public void paint(java.awt.Graphics g)
  {
     if (element!=null)
     {
        Rectangle r=element.jGetBounds();
        g.setColor(Color.BLACK);
        int mitteY=(r.y+r.height)/2;

        g.setFont(font);
        g.drawString("Bin>Dec",12,8);
        
        g.drawString("y",r.width,mitteY+8);
        for (int i=0;i<anzPins.getValue();i++)
        {
          g.drawString("x"+i,13,18+i*10);
        }
     }
  }


  public void init()
  {
    initPins();


    setName("Bin->Dec");
    element.jSetCaption("Bin->Dec");
  }
  
  
  public void initPins()
  {
    initPins(0,1,0,anzPins.getValue());

    setSize(58,20+(anzPins.getValue()*10));

    element.jSetInnerBorderVisibility(true);

    setPin(0,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
    element.jSetPinDescription(0,"y");

    for (int i=0;i<anzPins.getValue();i++)
    {
      setPin(1+i,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
      element.jSetPinDescription(1+i,"x"+i);
    }

  }



  public void initInputPins()
  {
    in = new VSBoolean[anzPins.getValue()];
    for (int i=0;i<anzPins.getValue();i++)
    {
      in[i]=(VSBoolean)element.getPinInputReference(1+i);
      if (in[i]==null) in[i]= new VSBoolean(false);
    }

  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }

  public void setPropertyEditor()
  {
    element.jAddPEItem("Eingangs-Pins",anzPins, 2,20);
    localize();
  }

  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Input-Pins");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Input-Pins");
  }


  public void propertyChanged(Object o)
  {
    if (o == anzPins)
    {
      initPins();
      /*anzPins.setValue(((VSInteger)outPins).getValue());
      element.jSetSize(element.jGetWidth(), 20+(anzPins.getValue())*10);
      element.jSetRightPins(anzPins.getValue());*/
      element.jRepaint();
      element.jRefreshVM();
    }

  }
  

  private int bolToInt()
  {
    int result=0;
    for (int i=0;i<anzPins.getValue();i++)
    {
      boolean x = in[i].getValue();
      
      if (x) result+=Math.pow(2,i);
    }

    return result;
  }


  public void process()
  {
    int val=bolToInt();
    
    //System.out.println("Val="+val);
    
    out.setValue(val);
    element.notifyPin(0);
    
  }
  
  

  public void loadFromStream(java.io.FileInputStream fis)
  {
      anzPins.loadFromStream(fis);
      initPins();
      element.jRepaint();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    anzPins.saveToStream(fos);
  }

}
