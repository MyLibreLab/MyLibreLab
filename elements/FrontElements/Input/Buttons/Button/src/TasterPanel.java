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
import javax.swing.*;
import java.awt.event.*;
import tools.*;
import java.awt.geom.Rectangle2D;



public class TasterPanel extends JVSMain
{
  private boolean value=false;
  private int sizeW=30;
  private int sizeH=30;
  private VSBoolean down=new VSBoolean(false);;
  private VSBoolean initValue=new VSBoolean(false);
  private boolean oldDown=false;

  
  private ExternalIF circuitElement;
  
  private VSString caption = new VSString();
  private VSFont font=new VSFont(new Font("Arial",1,10));
  private VSColor fontColor = new VSColor(Color.BLACK);
  private VSColor backColor = new VSColor(Color.LIGHT_GRAY);
  private VSBoolean asSchalter = new VSBoolean();




   public void paint(java.awt.Graphics g)
   {
      if (element!=null)
      {
         Graphics2D g2=(Graphics2D)g;

         Rectangle r=element.jGetBounds();
         
         if (down.getValue()) g.setColor(Color.red); else g.setColor(backColor.getValue());
         
         g.fillRect(r.x+1,r.y+1,r.width-2,r.height-2);
         g.setColor(Color.black);
         g.drawRect(r.x+1,r.y+1,r.width-2,r.height-2);
         
         g.setColor(Color.darkGray);
         g.drawLine(r.x+2,r.y+r.height-2,r.x+r.width-2,r.y+r.height-2);
         g.drawLine(r.x+r.width-2,r.y+2,r.x+r.width-2,r.y+r.height-2);
         g.setColor(Color.white);
         g.drawLine(r.x+2,r.y+2,r.x+r.width-2,r.y+2);
         g.drawLine(r.x+2,r.y+2,r.x+2,r.y+r.height-2);
         
         
         g2.setColor(fontColor.getValue());
         g2.setFont(font.getValue());
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

         FontMetrics fm = g.getFontMetrics();
         Rectangle2D rFont = fm.getStringBounds(caption.getValue(),g);

         g.drawString(caption.getValue(),(int)(r.x+(r.width/2)-(rFont.getWidth()/2))  ,(int)(r.y+(r.height/2)+font.getValue().getSize()/2));
      }
   }

  public void init()
  {
    initPins(0,0,0,0);
    setSize(sizeW,sizeH);
    initPinVisibility(false,false,false,false);

    element.jSetResizable(true);
    element.jSetInnerBorderVisibility(false);
    setName("Taster");
    asSchalter.setValue(false);
  }

  public void initInputPins()
  {
  }

  public void initOutputPins()
  {
  }
  
  
  public void start()
  {
    down.setValue(initValue.getValue());
    element.jRepaint();
    
    circuitElement=element.getCircuitElement();
    circuitElement.Change(0,down);
  }




  public void setPropertyEditor()
  {
    element.jAddPEItem("Schriftart",font, 0,0);
    element.jAddPEItem("Beschriftung",caption, 0,0);
    element.jAddPEItem("Font Color",fontColor, 0,0);
    element.jAddPEItem("Hintergrund-Farbe",backColor, 0,0);
    element.jAddPEItem("Schalter",asSchalter, 0,0);
    element.jAddPEItem("Anfangs-Wert",initValue, 0,0);

    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Font");
    element.jSetPEItemLocale(d+1,language,"Caption");
    element.jSetPEItemLocale(d+2,language,"Font Color");
    element.jSetPEItemLocale(d+3,language,"Background Color");
    element.jSetPEItemLocale(d+4,language,"Switch");
    element.jSetPEItemLocale(d+5,language,"Init-Value");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Fuente");
    element.jSetPEItemLocale(d+1,language,"Texto");
    element.jSetPEItemLocale(d+2,language,"Color del texto");
    element.jSetPEItemLocale(d+3,language,"Color del fondo");
    element.jSetPEItemLocale(d+4,language,"Interruptor");
    element.jSetPEItemLocale(d+5,language,"Init-Valor");
  }

  public void propertyChanged(Object o)
  {
    down.setValue(initValue.getValue());
    element.jRepaint();
  }


  public void mouseReleased(MouseEvent e)
  {

    if (asSchalter.getValue())
    {
      if (down.getValue()) down.setValue(false); else down.setValue(true);
      circuitElement.Change(0,down);
    } else
    {
      down.setValue(false);
      circuitElement.Change(0,down);
    }

    element.jRepaint();
  }




  public void mousePressed(MouseEvent e)
  {
    if (asSchalter.getValue())
    {
    } else
    {
      down.setValue(true);
      circuitElement.Change(0,down);
      element.jRepaint();
    }
  }


  public void loadFromStream(java.io.FileInputStream fis)
  {
      asSchalter.loadFromStream(fis);
      initValue.loadFromStream(fis);
      font.loadFromStream(fis);
      caption.loadFromStream(fis);
      fontColor.loadFromStream(fis);
      backColor.loadFromStream(fis);
      
      down.setValue(initValue.getValue());
      element.jRepaint();

  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      asSchalter.saveToStream(fos);
      initValue.saveToStream(fos);
      font.saveToStream(fos);
      caption.saveToStream(fos);
      fontColor.saveToStream(fos);
      backColor.saveToStream(fos);
  }
}

