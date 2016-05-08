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
import java.util.*;


public class ColorConst extends JVSMain
{
  public VSColor out=new VSColor(java.awt.Color.BLACK);
  public VSColor aColor=new VSColor(java.awt.Color.BLACK);

  private boolean changed=false;

  public void paint(java.awt.Graphics g)
  {
    Rectangle bounds=element.jGetBounds();
    g.setColor(aColor.getValue());
    g.fillRect(bounds.x+1,bounds.y+1,bounds.width-2,bounds.height-2);

  }

  
 public void init()
  {
    initPins(0,1,0,0);
    setSize(40,30);
    initPinVisibility(false,true,false,false);
    
    element.jSetRightPinsVisible(true);
    
    element.jInitPins();
    element.jSetPinDataType(0,ExternalIF.C_COLOR);
    element.jSetPinIO(0,element.PIN_OUTPUT);
    element.jSetCaptionVisible(true);
    element.jSetCaption("ColorConst");
    setName("ColorConst");
  }



  public void setPropertyEditor()
  {
    element.jAddPEItem("Farbe",aColor, 0,0);
    localize();
  }

  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Color");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Color");
  }
  
  public void propertyChanged(Object o)
  {
    element.jRepaint();
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
    out.setValue(aColor.getValue());
    out.setChanged(true);
    element.notifyPin(0);
  }
  
  public void process()
  {
  }
  
    
  public void loadFromStream(java.io.FileInputStream fis)
  {
      aColor.loadFromStream(fis);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      aColor.saveToStream(fos);
  }


}

