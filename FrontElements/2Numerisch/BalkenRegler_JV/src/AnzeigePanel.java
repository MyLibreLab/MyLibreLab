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
import java.awt.geom.Rectangle2D;
import tools.*;

public class AnzeigePanel extends JVSMain
{
  private int width=70, height=150;

  private double value=0.0;
  private double oldPin;
  private ExternalIF circuitElement;
  
  
  private VSDouble initValue=new VSDouble();
  private VSBoolean TextVisible = new VSBoolean(true);
  private VSFont fnt = new VSFont(new Font("Dialog",1,11));
  private VSColor textColor= new VSColor(Color.BLACK);
  private VSDouble min=new VSDouble();
  private VSDouble max=new VSDouble();
  private VSDouble numStages = new VSDouble(); 
  private VSColor stagesColor= new VSColor(Color.BLACK);
  private VSInteger strokeStages = new VSInteger(1);
  private VSBoolean circularNibble = new VSBoolean(false);
  private VSColorAdvanced colorNibble=new VSColorAdvanced();
  private VSBoolean RoundBase = new VSBoolean(false);
  private VSInteger arcValue = new VSInteger(20);
  private VSInteger strokeValue = new VSInteger(3);
  private VSColorAdvanced strokeColor=new VSColorAdvanced(); 
  private VSBoolean backgroundVisible = new VSBoolean(true);
  private VSColorAdvanced colorBackgroung=new VSColorAdvanced();
  private VSBoolean EdgeVisible = new VSBoolean(true);
  private VSColor edgeColor= new VSColor(Color.BLACK);
 
 
  



   public void paint(java.awt.Graphics g)
   {
    if (element!=null)
    {
       if (min.getValue()>=max.getValue()) min.setValue(max.getValue()-1);
       
       Rectangle bounds=element.jGetBounds();
       Graphics2D g2=(Graphics2D)g;
       
       if(backgroundVisible.getValue()){
       colorBackgroung.setFillColor(g2);
       if(RoundBase.getValue()){
        g2.setStroke(new BasicStroke(1));
        g2.fillRoundRect(bounds.x+bounds.width-22,bounds.y+5,15-1,bounds.height-10,arcValue.getValue(),arcValue.getValue());
        g2.setStroke(new BasicStroke(strokeValue.getValue()));
        strokeColor.setFillColor(g2);
        g2.drawRoundRect(bounds.x+bounds.width-22,bounds.y+5,15-1,bounds.height-10,arcValue.getValue(),arcValue.getValue());
        if(EdgeVisible.getValue()){
        g2.setColor(edgeColor.getValue());    
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(bounds.x+bounds.width-22,bounds.y+5,15-1,bounds.height-10,arcValue.getValue(),arcValue.getValue());
        }
       }else{
            colorBackgroung.setFillColor(g2);
            g2.setStroke(new BasicStroke(1));
            g2.fillRect(bounds.x+bounds.width-22,bounds.y+5,15-1,bounds.height-10);
            g2.setStroke(new BasicStroke(strokeValue.getValue()));
            strokeColor.setFillColor(g2);
            g2.drawRect(bounds.x+bounds.width-22,bounds.y+5,15-1,bounds.height-10);
            if(EdgeVisible.getValue()){
            g2.setColor(edgeColor.getValue());    
            g2.setStroke(new BasicStroke(1));
            g2.drawRect(bounds.x+bounds.width-22,bounds.y+5,15-1,bounds.height-10);
            }
        }
       
        
       }
       g2.setStroke(new BasicStroke(1));
       double f=(double)(bounds.height-10)/(max.getValue()-min.getValue());
       double stand=f*value-min.getValue()*f;

       stand+=5;

       if (stand<5) stand=5;
       if (stand>bounds.height-5) stand=bounds.height-5;

       
       if(circularNibble.getValue()){
       colorNibble.setFillColor(g2);
       g2.fillOval(bounds.x+bounds.width-30,bounds.y+1+bounds.height-(int)stand-5,30-1,8);
       
       strokeColor.setFillColor(g2);
       g2.setStroke(new BasicStroke(strokeValue.getValue()));
       g2.drawOval(bounds.x+bounds.width-30,bounds.y+1+bounds.height-(int)stand-5,30-1,8);

       if(EdgeVisible.getValue()){
       g2.setStroke(new BasicStroke(1));
       g2.setColor(edgeColor.getValue());
       g2.drawOval(bounds.x+bounds.width-30,bounds.y+1+bounds.height-(int)stand-5,30-1,8);
       }
       g2.setStroke(new BasicStroke(2));
       g2.setColor(Color.WHITE);
       g2.drawLine(bounds.x+bounds.width-30+1,bounds.y+bounds.height-(int)stand,bounds.x+bounds.width-2,bounds.y+bounds.height-(int)stand);
       
           
       }else{
       colorNibble.setFillColor(g2);
       g2.fillRect(bounds.x+bounds.width-30,bounds.y+1+bounds.height-(int)stand-5,30-1,8);
       
       g2.setStroke(new BasicStroke(1));
       //g2.setStroke(new BasicStroke(strokeValue.getValue()));
       g2.drawRect(bounds.x+bounds.width-30,bounds.y+1+bounds.height-(int)stand-5,30-1,8);

       if(EdgeVisible.getValue()){
       g2.setStroke(new BasicStroke(1));
       g2.setColor(edgeColor.getValue());
       g2.drawRect(bounds.x+bounds.width-30,bounds.y+1+bounds.height-(int)stand-5,30-1,8);
       }
       g2.setStroke(new BasicStroke(2));
       g2.setColor(Color.WHITE);
       g2.drawLine(bounds.x+bounds.width-30+1,bounds.y+bounds.height-(int)stand,bounds.x+bounds.width-2,bounds.y+bounds.height-(int)stand);
       }
       
       if(TextVisible.getValue()){
        
        if (numStages.getValue()>=bounds.height) numStages.setValue(bounds.height);
        if (numStages.getValue()<1) numStages.setValue(1);
      
        g2.setFont(fnt.getValue());
        g2.setStroke(new BasicStroke(strokeStages.getValue()));
        FontMetrics fm = g2.getFontMetrics();
        Rectangle2D   r = fm.getStringBounds("",g2);
        double fx=(f*(max.getValue()-min.getValue()))/numStages.getValue();
        for (double i=0;i<=(double)(bounds.height-10);i+=fx)
        {
        g2.setColor(stagesColor.getValue());
        int x=(int)(min.getValue()+(i/f));
        r = fm.getStringBounds(""+x,g2);
        g2.drawLine(bounds.x+bounds.width-40,(int)i+5,bounds.x+bounds.width-30,(int)i+5);
        
        g2.setColor(textColor.getValue());

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
    
    
    colorBackgroung.color1=new Color(255,242,181);
    colorNibble.color1=new Color(253,153,0);
    strokeColor.color1=new Color(153,153,153);
    
    setName("Fuellstandanzeige_JV");
    
    numStages.setValue(5);
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
    
    element.jAddPEItem("Anfangs-Wert",initValue, -9999999,9999999);  
    element.jAddPEItem("Min",min, -9999999.0,99999999.0);
    element.jAddPEItem("Max",max, -9999999.0,99999999.0);
    element.jAddPEItem("Text Visible",TextVisible, 0,0);
    element.jAddPEItem("Font",fnt, 0,0);
    element.jAddPEItem("Font Farbe",textColor, 0,0);
    element.jAddPEItem("Abschnitte",numStages, 0.0,99999.0);
    element.jAddPEItem("Stages Farbe",stagesColor, 0,0);
    element.jAddPEItem("Stages Stroke",strokeStages, 0,5);
    //element.jAddPEItem("Circular Nibble",circularNibble, 0,0);
    element.jAddPEItem("Nibble Farbe",colorNibble, 0,0);
    element.jAddPEItem("Round Base",RoundBase, 0,0);
    element.jAddPEItem("Round Arc Value",arcValue, 0,100);
    element.jAddPEItem("Base Stroke Value",strokeValue, 0,20);
    element.jAddPEItem("Base Stroke Color",strokeColor, 0,20);
    element.jAddPEItem("Hintergrund Visible",backgroundVisible, 0,0);
    element.jAddPEItem("Hintergrundfarbe",colorBackgroung, 0,0);
    element.jAddPEItem("Edge Visible",EdgeVisible, 0,0);
    element.jAddPEItem("Edge Farbe",edgeColor, 0,0);  
    

    localize();
  }


  private void localize()
  {
    int d=6;
    int i=0;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+ i++,language,"Init Value");  
    element.jSetPEItemLocale(d+ i++,language,"Min");
    element.jSetPEItemLocale(d+ i++,language,"Max");
    element.jSetPEItemLocale(d+ i++,language,"Text Visible");
    element.jSetPEItemLocale(d+ i++,language,"Font");
    element.jSetPEItemLocale(d+ i++,language,"Font Color");
    element.jSetPEItemLocale(d+ i++,language,"Stages");
    element.jSetPEItemLocale(d+ i++,language,"Stages Color");
    element.jSetPEItemLocale(d+ i++,language,"Stages Stroke");
    //element.jSetPEItemLocale(d+ i++,language,"Circular Nibble");
    element.jSetPEItemLocale(d+ i++,language,"Nibble Color");
    element.jSetPEItemLocale(d+ i++,language,"Round Base");
    element.jSetPEItemLocale(d+ i++,language,"Round Arc Value");
    element.jSetPEItemLocale(d+ i++,language,"Base Stroke Value");
    element.jSetPEItemLocale(d+ i++,language,"Base Stroke Color");
    element.jSetPEItemLocale(d+ i++,language,"Background  Visible");
    element.jSetPEItemLocale(d+ i++,language,"Background Color");
    element.jSetPEItemLocale(d+ i++,language,"Edge Visible");
    element.jSetPEItemLocale(d+ i++,language,"Edge Color");
    

    language="es_ES";
    i=0;
    element.jSetPEItemLocale(d+ i++,language,"Valor Inicial");  
    element.jSetPEItemLocale(d+ i++,language,"Minimo");
    element.jSetPEItemLocale(d+ i++,language,"Maximo");
    element.jSetPEItemLocale(d+ i++,language,"Texto Visible");
    element.jSetPEItemLocale(d+ i++,language,"Fuente");
    element.jSetPEItemLocale(d+ i++,language,"Color Fuente");
    element.jSetPEItemLocale(d+ i++,language,"Divisiones");
    element.jSetPEItemLocale(d+ i++,language,"Color Divisiones");
    element.jSetPEItemLocale(d+ i++,language,"Espesor Divisiones");
    //element.jSetPEItemLocale(d+ i++,language,"Indicador Circular");
    element.jSetPEItemLocale(d+ i++,language,"Color Indicador");
    element.jSetPEItemLocale(d+ i++,language,"Redondear Base");
    element.jSetPEItemLocale(d+ i++,language,"Angulo Redondeo");
    element.jSetPEItemLocale(d+ i++,language,"Valor Espesor");
    element.jSetPEItemLocale(d+ i++,language,"Color Espesor Base");
    element.jSetPEItemLocale(d+ i++,language,"Fondo Visible");
    element.jSetPEItemLocale(d+ i++,language,"Color Fondo");
    element.jSetPEItemLocale(d+ i++,language,"Bordes Visibles");
    element.jSetPEItemLocale(d+ i++,language,"Color Bordes");
  }
  

  public void propertyChanged(Object o)
  {
    value=initValue.getValue();
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
    initValue.loadFromStream(fis);
    min.loadFromStream(fis);
    max.loadFromStream(fis);
    TextVisible.loadFromStream(fis);
    fnt.loadFromStream(fis);
    textColor.loadFromStream(fis);
    numStages.loadFromStream(fis);
    stagesColor.loadFromStream(fis);
    strokeStages.loadFromStream(fis);
    circularNibble.loadFromStream(fis);
    colorNibble.loadFromStream(fis);
    RoundBase.loadFromStream(fis);
    arcValue.loadFromStream(fis);
    strokeValue.loadFromStream(fis);
    strokeColor.loadFromStream(fis);
    backgroundVisible.loadFromStream(fis);
    colorBackgroung.loadFromStream(fis);
    EdgeVisible.loadFromStream(fis);
    edgeColor.loadFromStream(fis);

      value=initValue.getValue();
      circuitElement=element.getCircuitElement();
      if (circuitElement!=null) circuitElement.Change(0,new VSDouble(value));

      if (min.getValue()>=max.getValue()) min.setValue(max.getValue()-1);
      element.jRepaint();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    initValue.saveToStream(fos);
    min.saveToStream(fos);
    max.saveToStream(fos);
    TextVisible.saveToStream(fos);
    fnt.saveToStream(fos);
    textColor.saveToStream(fos);
    numStages.saveToStream(fos);
    stagesColor.saveToStream(fos);
    strokeStages.saveToStream(fos);
    circularNibble.saveToStream(fos);
    colorNibble.saveToStream(fos);
    RoundBase.saveToStream(fos);
    arcValue.saveToStream(fos);
    strokeValue.saveToStream(fos);
    strokeColor.saveToStream(fos);
    backgroundVisible.saveToStream(fos);
    colorBackgroung.saveToStream(fos);
    EdgeVisible.saveToStream(fos);
    edgeColor.saveToStream(fos);
    }
  
}

