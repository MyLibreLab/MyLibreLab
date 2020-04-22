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

public class AnzeigePanel extends JVSMain
{
  private int width=60, height=150;
  private VSDouble initValue=new VSDouble();
  private double value=0.0;
  private double oldPin;
  private ExternalIF circuitElement;
  private VSColor colorBackgroung=new VSColor(Color.WHITE);
  private VSColor colorNibble=new VSColor(Color.LIGHT_GRAY);
  private VSDouble min=new VSDouble();
  private VSDouble max=new VSDouble();
  private VSDouble abschnitte = new VSDouble();
  private Font fnt = new Font("Monospaced",0,10);


   public void paint(java.awt.Graphics g)
   {
    if (element!=null)
    {
       if (min.getValue()>=max.getValue()) min.setValue(max.getValue()-1);
       
       Rectangle bounds=element.jGetBounds();
       Graphics2D g2=(Graphics2D)g;
       
       g2.setColor(colorBackgroung.getValue());
       g2.fillRect(bounds.x+bounds.width-22,bounds.y+5,15-1,bounds.height-10);
       g2.setColor(Color.BLACK);
       g2.drawRect(bounds.x+bounds.width-22,bounds.y+5,15-1,bounds.height-10);

       double f=(double)(bounds.height-10)/(max.getValue()-min.getValue());
       double stand=f*value-min.getValue()*f;


       stand+=5;

       if (stand<5) stand=5;
       if (stand>bounds.height-5) stand=bounds.height-5;

       g2.setColor(colorNibble.getValue());
       g2.fillRect(bounds.x+bounds.width-30,bounds.y+1+bounds.height-(int)stand-5,30-1,8);
       g2.setColor(Color.BLACK);
       g2.drawRect(bounds.x+bounds.width-30,bounds.y+1+bounds.height-(int)stand-5,30-1,8);

       g2.setColor(Color.WHITE);
       g2.drawLine(bounds.x+bounds.width-30+1,bounds.y+bounds.height-(int)stand,bounds.x+bounds.width-2,bounds.y+bounds.height-(int)stand);

       g2.setColor(Color.BLACK);
       //g2.drawRect(bounds.x+bounds.width-30,bounds.y,30-1,bounds.height-1);
       
       
       if (abschnitte.getValue()>=bounds.height) abschnitte.setValue(bounds.height);
       if (abschnitte.getValue()<1) abschnitte.setValue(1);
       
       g2.setFont(fnt);
       double fx=(f*(max.getValue()-min.getValue()))/abschnitte.getValue();
       for (double i=0;i<=(double)(bounds.height-10);i+=fx)
       {
         //if (i>0.0)
         {
           g2.drawLine(bounds.x+bounds.width-40,(int)i+5,bounds.x+bounds.width-30,(int)i+5);
           int x=(int)(min.getValue()+(i/f));
           g2.drawString(""+x,2,bounds.height-(int)i-5+3);
         }
       }
       
    }
   }

  public void init()
  {
    initPins(0,0,0,0);
    setSize(width,height);
    initPinVisibility(false,false,false,false);
    element.jSetInnerBorderVisibility(false);
    
    element.jSetResizable(true);
    
    min.setValue(0);
    max.setValue(100);
    
    setName("Fuellstandanzeige");
    
    abschnitte.setValue(5);
    element.jSetMinimumSize(30,25);
  }
  
    public void start()
    {
      value=initValue.getValue();
      element.jRepaint();
      circuitElement=element.getCircuitElement();
      circuitElement.Change(0,new VSDouble(value));
    }

  public void setPropertyEditor()
  {
    element.jAddPEItem("Min",min, -9999999.0,99999999.0);
    element.jAddPEItem("Max",max, -9999999.0,99999999.0);
    element.jAddPEItem("Abschnitte",abschnitte, 0.0,99999.0);
    element.jAddPEItem("Anfangs-Wert",initValue, -9999999,9999999);
    
    element.jAddPEItem("Hintergrundfarbe",colorBackgroung, 0,0);
    element.jAddPEItem("Nibble-Farbe",colorNibble, 0,0);

    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Min");
    element.jSetPEItemLocale(d+1,language,"Max");
    element.jSetPEItemLocale(d+2,language,"Stages");
    element.jSetPEItemLocale(d+3,language,"Init-Value");
    element.jSetPEItemLocale(d+4,language,"Background-Color");
    element.jSetPEItemLocale(d+5,language,"Nibble-Color");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Min");
    element.jSetPEItemLocale(d+1,language,"Max");
    element.jSetPEItemLocale(d+2,language,"Marcas");
    element.jSetPEItemLocale(d+3,language,"Valor Inicial");
    element.jSetPEItemLocale(d+4,language,"Color Fondo");
    element.jSetPEItemLocale(d+5,language,"Color Contorno");
  }
  

  public void propertyChanged(Object o)
  {
    element.jRepaint();
  }

  public void mousePressed(MouseEvent e)
  {
    mouseDragged(e);
  }
  public void mouseDragged(MouseEvent e)
  {
    int x=e.getX();
    int y=e.getY();
    
    Rectangle bounds=element.jGetBounds();
    
    value=y;

    double f=(double)bounds.height/(max.getValue()-min.getValue());
    
    value=(double)(bounds.height-y)/f;
    
    value+=(double)min.getValue();

    element.jRepaint();
    
    if (value<min.getValue()) value=min.getValue();
    if (value>max.getValue()) value=max.getValue();

    circuitElement=element.getCircuitElement();
    circuitElement.Change(0,new VSDouble(value));
  }


  public void loadFromStream(java.io.FileInputStream fis)
  {
      min.loadFromStream(fis);
      max.loadFromStream(fis);
      initValue.loadFromStream(fis);
      abschnitte.loadFromStream(fis);
      colorBackgroung.loadFromStream(fis);
      colorNibble.loadFromStream(fis);

      value=initValue.getValue();
      circuitElement=element.getCircuitElement();
      if (circuitElement!=null) circuitElement.Change(0,new VSDouble(value));

      if (min.getValue()>=max.getValue()) min.setValue(max.getValue()-1);
      element.jRepaint();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      min.saveToStream(fos);
      max.saveToStream(fos);
      initValue.saveToStream(fos);
      abschnitte.saveToStream(fos);
      colorBackgroung.saveToStream(fos);
      colorNibble.saveToStream(fos);
  }
  
}

