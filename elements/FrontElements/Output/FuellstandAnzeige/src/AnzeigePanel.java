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
  private int width=50, height=150;
  private double value=0.0;
  private double oldPin;

  private VSColor color1 = new VSColor(new Color(255,50,50));
  private VSColor color2 = new VSColor(new Color(150,0,0));
  private VSColor fontColor = new VSColor(new Color(0,0,0));
  
  private VSDouble min=new VSDouble();
  private VSDouble max=new VSDouble();
  private VSDouble abschnitte = new VSDouble();


  private Font fnt = new Font("Monospaced",0,10);


  public void processPanel(int pinIndex, double value, Object obj)
  {
    this.value=value;
  }

   public void paint(java.awt.Graphics g)
   {
    if (element!=null)
    {
       if (min.getValue()>=max.getValue()) min.setValue(max.getValue()-1);

       
       Rectangle bounds=element.jGetBounds();
       Graphics2D g2=(Graphics2D)g;
       
       g2.setColor(Color.white);
       g2.fillRect(bounds.x,bounds.y,bounds.width-1,bounds.height-1);


       double f=(double)bounds.height/(max.getValue()-min.getValue());
       double stand=f*value-min.getValue()*f;

       GradientPaint gp = new GradientPaint(0.0f, 50.0f, color1.getValue(),(float)bounds.width, 50.0f, color2.getValue());
       g2.setPaint(gp);

       if (stand<=0) stand=0;
       if (stand>bounds.height) stand=bounds.height;
       

       g2.fillRect(bounds.x,bounds.height-(int)stand,bounds.width-1,(int)stand-1);
       g2.setColor(Color.BLACK);
       g2.drawRect(bounds.x,bounds.y,bounds.width-1,bounds.height-1);
       g2.drawLine(bounds.x,bounds.height-(int)stand,bounds.width-1,bounds.height-(int)stand);
       
       
       if (abschnitte.getValue()>=bounds.height) abschnitte.setValue(bounds.height);
       if (abschnitte.getValue()<1) abschnitte.setValue(1);
       
       g2.setFont(fnt);

       g2.setColor(fontColor.getValue());
       
       double fx=(f*(max.getValue()-min.getValue()))/abschnitte.getValue();
       for (double i=0;i<(double)bounds.height;i+=fx)
       {
         if (i>0.0)
         {
           g2.drawLine(0,(int)i,10,(int)i);
           int x=(int)(min.getValue()+(i/f));
           g2.drawString(""+x,15,bounds.height-(int)i);
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
  
  public void setPropertyEditor()
  {
    element.jAddPEItem("Min",min, -9999999.0,99999999.0);
    element.jAddPEItem("Max",max, -9999999.0,99999999.0);
    element.jAddPEItem("Gradient-Farbe A",color1, 0,0);
    element.jAddPEItem("Gradient-Farbe B",color2, 0,0);
    element.jAddPEItem("Font-Color",fontColor, 0,0);
    element.jAddPEItem("Abschnitte",abschnitte, 0.0,99999.0);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Min");
    element.jSetPEItemLocale(d+1,language,"Max");
    element.jSetPEItemLocale(d+2,language,"Gradient-Color A");
    element.jSetPEItemLocale(d+3,language,"Gradient-Color B");
    element.jSetPEItemLocale(d+4,language,"Font-Color");
    element.jSetPEItemLocale(d+5,language,"Stages");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Min");
    element.jSetPEItemLocale(d+1,language,"Max");
    element.jSetPEItemLocale(d+2,language,"Color Mezcla A");
    element.jSetPEItemLocale(d+3,language,"Color Mezcla B");
    element.jSetPEItemLocale(d+4,language,"Color Letra");
    element.jSetPEItemLocale(d+5,language,"Marcas");
  }
  
  
  public void propertyChanged(Object o)
  {
    element.jRepaint();
  }



  public void loadFromStream(java.io.FileInputStream fis)
  {
      min.loadFromStream(fis);
      max.loadFromStream(fis);
      
      fontColor.loadFromStream(fis);
      
      color1.loadFromStream(fis);
      color2.loadFromStream(fis);

      abschnitte.loadFromStream(fis);
      
      if (min.getValue()>=max.getValue()) min.setValue(max.getValue()-1);
      element.jRepaint();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      min.saveToStream(fos);
      max.saveToStream(fos);
      
      fontColor.saveToStream(fos);
      
      color1.saveToStream(fos);
      color2.saveToStream(fos);

      abschnitte.saveToStream(fos);
  }
  
}

