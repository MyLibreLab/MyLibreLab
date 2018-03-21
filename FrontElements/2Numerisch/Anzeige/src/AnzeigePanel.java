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
import java.text.*;
import tools.*;
import java.awt.geom.Rectangle2D;

public class AnzeigePanel extends JVSMain implements PanelIF
{
  private int width=150, height=25;
  private double value=0.0;
  private double oldPin;
  private VSString formatierung = new VSString("#,##0.00");
  private VSFont font = new VSFont(new Font("Courier",0,12));
  private VSColor textColor = new VSColor(Color.BLACK);
  private DecimalFormat df = new DecimalFormat(formatierung.getValue());



  public void processPanel(int pinIndex, double value, Object obj)
  {
    this.value=value;
  }

   public void paint(java.awt.Graphics g)
   {
    if (element!=null)
    {
       Rectangle bounds=element.jGetBounds();

       g.setFont(font.getValue());
       g.setColor(textColor.getValue());

       Graphics2D g2=(Graphics2D)g;

       FontMetrics fm = g.getFontMetrics();
       
       Rectangle2D   r = fm.getStringBounds(df.format(value),g2);

       g.drawString(df.format(value),bounds.x+5,((bounds.height) /2)+5);

       g.setColor(Color.BLACK);
       g.drawRect(bounds.x,bounds.y,bounds.width-1,bounds.height-1);
    }
   }

  public void init()
  {
    initPins(0,0,0,0);
    setSize(width,height);
    initPinVisibility(false,false,false,false);
    element.jSetInnerBorderVisibility(false);
    
    element.jSetResizable(true);
    
    setName("Anzeige (DBL)");
  }

  public void setPropertyEditor()
  {
    element.jAddPEItem("Format",formatierung, 0,0);
    element.jAddPEItem("Schriftart",font, 0,0);
    element.jAddPEItem("Text-Farbe",textColor, 0,0);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Format");
    element.jSetPEItemLocale(d+1,language,"Font");
    element.jSetPEItemLocale(d+2,language,"Text Color");

    language="es_ES";
    element.jSetPEItemLocale(d+0,language,"Formato");
    element.jSetPEItemLocale(d+1,language,"Fuente");
    element.jSetPEItemLocale(d+2,language,"Color Texto");
  }

  public void propertyChanged(Object o)
  {
    try
    {
      df = new DecimalFormat(formatierung.getValue());
    } catch(Exception ex)
    {
      df = new DecimalFormat("0.00");
    }

    element.jRepaint();
  }


  public void loadFromStream(java.io.FileInputStream fis)
  {
      formatierung.loadFromStream(fis);
      font.loadFromStream(fis);
      textColor.loadFromStream(fis);
      
      df = new DecimalFormat(formatierung.getValue());
      element.jRepaint();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      formatierung.saveToStream(fos);
      font.saveToStream(fos);
      textColor.saveToStream(fos);
  }


}

