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
import javax.swing.*;
import java.awt.event.*;


public class Const extends JVSMain
{
  private VSBoolean value=new VSBoolean();
  private VSBoolean out=new VSBoolean();
  private boolean changed=false;

  public void paint(java.awt.Graphics g)
  {
    if (element!=null)
    {
       Rectangle bounds=element.jGetBounds();

       g.setColor(Color.black);
       int mx=bounds.width/2;
       g.drawRect(bounds.x,bounds.y,bounds.width-1,bounds.height-1);
       g.drawLine(mx,bounds.y,mx,bounds.height-1);
       
       g.setColor(Color.green);
       if (value.getValue()==true)
       {
         g.fillRect(mx+2,bounds.y+2,12,16);
       } else
       {
         g.fillRect(bounds.x+2,bounds.y+2,12,16);
       }
       g.setColor(Color.black);
       g.drawString("T",mx+5,bounds.y+13);
       g.drawString("F",bounds.x+5,bounds.y+13);
       
    }

  }

  public void init()
  {
    initPins(0,1,0,0);
    setSize(40,20);

    initPinVisibility(false,true,false,false);
    
    element.jSetInnerBorderVisibility(false);

    setPin(0,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);

    element.jSetPinDescription(0,"out");

    setName("Boolean Const");

  }
  

  public void initInputPins()
  {
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }



  public void start()
  {
    changed=true;
    out.setValue(value.getValue());
    element.notifyPin(0);
  }

  public void process()
  {
    if (changed)
    {
      changed=false;

    }
  }



  public void setPropertyEditor()
  {
    element.jAddPEItem("Wert",value, 0,0);
    localize();
  }

  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Value");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Value");
  }
  
  public void propertyChanged(Object o)
  {
    element.jRepaint();
  }


  
  
  
  public void loadFromStream(java.io.FileInputStream fis)
  {
    value.loadFromStream(fis);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    value.saveToStream(fos);
  }



}



