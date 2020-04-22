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
import javax.swing.Timer;
import javax.swing.*;



public class RoundRect extends JVSMain
{

  private VSColorAdvanced fillColor = new VSColorAdvanced();
  private VSColorAdvanced strokeColor = new VSColorAdvanced();
  private VSInteger strokeWidth = new VSInteger();
  private VSInteger arcWidth = new VSInteger(20);
  private VSInteger arcHeight = new VSInteger(20);
  private VSInteger colorTransparency = new VSInteger(100);


  public void paint(java.awt.Graphics g)
  {
    if (element!=null)
    {
      Graphics2D g2 = (Graphics2D)g;


       g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

       Rectangle r=element.jGetBounds();
       g2.setStroke(new BasicStroke(strokeWidth.getValue()));
       int c=strokeWidth.getValue()/2;

       //if (fill.getValue())
       {
         //g2.setColor(new Color(fillColor.getValue().getRed(),fillColor.getValue().getGreen(),fillColor.getValue().getBlue(),colorTransparency.getValue()));

         fillColor.setFillColor(g2);
         g2.fillRoundRect(r.x+c,r.y+c,r.width-c*2-1,r.height-c*2-1,arcWidth.getValue(),arcHeight.getValue());
       }

       if (strokeWidth.getValue()>0)
       {
         //g2.setColor(strokeColor.getValue());
         strokeColor.setFillColor(g2);
         g2.drawRoundRect(r.x+c,r.y+c,r.width-c*2-1,r.height-c*2-1,arcWidth.getValue(),arcHeight.getValue());
       }

    }
  }
   
  public void init()
  {
    initPins(0,0,0,0);
    setSize(60,60);
    initPinVisibility(false,false,false,false);
    element.jSetResizable(true);
    element.jSetInnerBorderVisibility(false);

    setName("RoundRect");

    element.jSetResizable(true);
    strokeWidth.setValue(5);
    fillColor.color1=new Color(153,153,153);
    strokeColor.color1=new Color(204,204,204);
  }


  public void setPropertyEditor()
  {
    element.jAddPEItem("Füll-Farbe",fillColor, 0,0);
    element.jAddPEItem("Linien-Farbe",strokeColor, 0,0);
    element.jAddPEItem("Linien-Breite",strokeWidth, 0,100);
    element.jAddPEItem("Fillcolor Transparenz",colorTransparency, 0,255);
    element.jAddPEItem("arcWidth",arcWidth, 0,1000);
    element.jAddPEItem("arcHeight",arcHeight, 0,1000);

    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Fill-Color");
    element.jSetPEItemLocale(d+1,language,"Stroke-Color");
    element.jSetPEItemLocale(d+2,language,"Stroke-Width");
    element.jSetPEItemLocale(d+3,language,"Fillcolor Transparency");
    element.jSetPEItemLocale(d+4,language,"arcWidth");
    element.jSetPEItemLocale(d+5,language,"arcHeight");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Color Interior");
    element.jSetPEItemLocale(d+1,language,"Color Contorno");
    element.jSetPEItemLocale(d+2,language,"Espesor contorno");
    element.jSetPEItemLocale(d+3,language,"Fillcolor Transparency");
    element.jSetPEItemLocale(d+4,language,"arcWidth");
    element.jSetPEItemLocale(d+5,language,"arcHeight");
  }

  public void propertyChanged(Object o)
  {
    element.jRepaint();
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
      fillColor.loadFromStream(fis);
      strokeColor.loadFromStream(fis);
      strokeWidth.loadFromStream(fis);
      arcWidth.loadFromStream(fis);
      arcHeight.loadFromStream(fis);
      colorTransparency.loadFromStream(fis);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      fillColor.saveToStream(fos);
      strokeColor.saveToStream(fos);
      strokeWidth.saveToStream(fos);
      arcWidth.saveToStream(fos);
      arcHeight.saveToStream(fos);
      colorTransparency.saveToStream(fos);
  }
  

}
