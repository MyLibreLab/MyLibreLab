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


package tools;

import VisualLogic.*;
import VisualLogic.variables.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import tools.*;

public class Gatter2 extends JVSMain
{

  private int width, height;
  private Image image;
  private String name="";
  public VSBoolean in[];
  public VSInteger delay = new VSInteger(0);    // = 0 micro sec.
  public VSBoolean out=new VSBoolean();
  public VSInteger anzPins=new VSInteger(2);

  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }



  public Gatter2(int anzPins, String name)
  {
    this.anzPins.setValue(anzPins);
    //this.anzPins=anzPins;
    this.name=name;
    width=50;
    height=25;
  }

  public void paint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }

  public void initPins()
  {
    initPins(0,1,0,anzPins.getValue());
    setSize(width,20+(anzPins.getValue()*10));

    setPin(0,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);

    for (int i=1;i<=anzPins.getValue();i++)
    {
      setPin(i,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    }
  }

  public void init()
  {
    element.jSetInnerBorderVisibility(true);
    
    //initPins(0,1,0,anzPins);
    initPinVisibility(false,true,false,true);
    image=element.jLoadImage(element.jGetSourcePath()+"icon.png");
    width=image.getWidth(null)+22;

    initPins();

    setName(name);
  }

  public void propertyChanged(Object o)
  {
    if (o == anzPins)
    {
      initPins();
      element.jRepaint();
      element.jRefreshVM();
    }
  }



  public void setPropertyEditor()
  {
    element.jAddPEItem("Input-Pins",anzPins, 2,20);

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


  public void initInputPins()
  {
    in = new VSBoolean[anzPins.getValue()];
    for (int i=1;i<=anzPins.getValue();i++)
    {
      in[i-1]=(VSBoolean)element.getPinInputReference(i);
    }
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }


  public void loadFromStream(java.io.FileInputStream fis)
  {
      anzPins.loadFromStream(fis);
      initPins();
      element.jRepaint();
      element.jRefreshVM();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    anzPins.saveToStream(fos);
  }


}
 
