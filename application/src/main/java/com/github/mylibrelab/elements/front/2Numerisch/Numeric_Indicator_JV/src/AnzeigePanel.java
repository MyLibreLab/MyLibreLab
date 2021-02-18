//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia (cswi@gmx.de)                         *
//* Copyright (C) 2017  Javier Velásquez (javiervelasquez125@gmail.com)                                                                          *
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
  private int width=120, height=30;
  private double value=0.0;
  private double oldPin;
  private VSString formatierung = new VSString("#,##0.00");
  private VSFont font = new VSFont(new Font("Dialog",Font.BOLD,12));
  private VSColor textColor = new VSColor(new Color(0,0,51));  
  private DecimalFormat df = new DecimalFormat(formatierung.getValue());
  private VSColor EdgeColor = new VSColor(new Color(253,153,0));
  private VSBoolean EdgeVisible = new VSBoolean(true);
  
  private VSInteger AlignNumber= new VSInteger(0);
  
  private VSComboBox Align = new VSComboBox();
  private VSBoolean Transparent = new VSBoolean(false);
  private VSColor BackGroundColor = new VSColor(new Color(255,242,181));
  private VSString UnitsStr = new VSString("");
  private VSBoolean UnitsVisible = new VSBoolean(false);



  public void processPanel(int pinIndex, double value, Object obj)
  {
    this.value=value;
  }

   public void paint(java.awt.Graphics g)
   {
    if (element!=null)
    {
       Rectangle bounds=element.jGetBounds();
       
       String StrNumOut ="0";
       if(UnitsVisible.getValue()){
          StrNumOut = df.format(value)+" "+UnitsStr.getValue();
       }else{
           StrNumOut = df.format(value);
       }
       
       g.setColor(BackGroundColor.getValue());
       if(Transparent.getValue()==false){
       g.fillRect(bounds.x,bounds.y,bounds.width-1,bounds.height-1);
       }
       if(EdgeVisible.getValue()){
       g.setColor(EdgeColor.getValue());
       g.drawRect(bounds.x,bounds.y,bounds.width-1,bounds.height-1);
       }
       
       g.setFont(font.getValue());
       g.setColor(textColor.getValue());
       FontMetrics fm = g.getFontMetrics();
       Graphics2D g2=(Graphics2D)g;
       g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
       Rectangle2D   r = fm.getStringBounds(StrNumOut,g2);

       // //-df.toString().length()
       int widthText = (int) r.getWidth();
       
       if(AlignNumber.getValue()==1){
       g.drawString(StrNumOut,bounds.x+5,((bounds.height) /2)+5);   
       }
       if(AlignNumber.getValue()==0){
       g.drawString(StrNumOut,((bounds.width)/2)-(widthText/2),((bounds.height) /2)+5);
       }
       if(AlignNumber.getValue()==2){
       g.drawString(StrNumOut,(bounds.width)-(widthText+5),((bounds.height) /2)+5);   
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
    
    Align.addItem("CENTER");
    Align.addItem("LEFT");
    Align.addItem("RIGHT");
    setName("Numeric Indicator J.V. (DBL)");
    value=0.0;
    
  }

  public void setPropertyEditor()
  {
    element.jAddPEItem("Format",formatierung, 0,0);
    element.jAddPEItem("Schriftart",font, 0,0);
    element.jAddPEItem("Text-Farbe",textColor, 0,0);
    element.jAddPEItem("Edge Visible?",EdgeVisible, 0,0);
    element.jAddPEItem("Edge Color",EdgeColor, 0,0);
    element.jAddPEItem("Align",Align, 0,2);
    element.jAddPEItem("Units String",UnitsStr, 0,0);
    element.jAddPEItem("Units Visible?",UnitsVisible, 0,0);
    element.jAddPEItem("Transparent Back?",Transparent, 0,0);
    element.jAddPEItem("Background Color",BackGroundColor, 0,0);
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
    element.jSetPEItemLocale(d+3,language,"Edge Visible?");
    element.jSetPEItemLocale(d+4,language,"Edge Color");
    element.jSetPEItemLocale(d+5,language,"Align");
    element.jSetPEItemLocale(d+6,language,"Units String");
    element.jSetPEItemLocale(d+7,language,"Units Visible?");
    element.jSetPEItemLocale(d+8,language,"Transparent Back?");
    element.jSetPEItemLocale(d+9,language,"Background Color");

    language="es_ES";
    element.jSetPEItemLocale(d+0,language,"Formato");
    element.jSetPEItemLocale(d+1,language,"Fuente");
    element.jSetPEItemLocale(d+2,language,"Color Texto");
    element.jSetPEItemLocale(d+3,language,"Borde Visible?");
    element.jSetPEItemLocale(d+4,language,"Color del Borde");
    element.jSetPEItemLocale(d+5,language,"Alineacion");
    element.jSetPEItemLocale(d+6,language,"Texto Unidades");
    element.jSetPEItemLocale(d+7,language,"Unidades Visibles?");
    element.jSetPEItemLocale(d+8,language,"Fondo Transparente?");
    element.jSetPEItemLocale(d+9,language,"Color de Fondo");
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
    if(o.equals(Align)){
    AlignNumber.setValue(Align.selectedIndex);
    }

    element.jRepaint();
  }


  public void loadFromStream(java.io.FileInputStream fis)
  {
      formatierung.loadFromStream(fis);
      font.loadFromStream(fis);
      textColor.loadFromStream(fis);
      EdgeColor.loadFromStream(fis);
      EdgeVisible.loadFromStream(fis);
      Align.loadFromStream(fis);
      UnitsStr.loadFromStream(fis);
      UnitsVisible.loadFromStream(fis);
      BackGroundColor.loadFromStream(fis);
      Transparent.loadFromStream(fis);
      df = new DecimalFormat(formatierung.getValue());
      AlignNumber.loadFromStream(fis);
      
      element.jRepaint();
      
      
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      formatierung.saveToStream(fos);
      font.saveToStream(fos);
      textColor.saveToStream(fos);
      EdgeColor.saveToStream(fos);
      EdgeVisible.saveToStream(fos);
      Align.saveToStream(fos);
      UnitsStr.saveToStream(fos);
      UnitsVisible.saveToStream(fos);
      BackGroundColor.saveToStream(fos);
      Transparent.saveToStream(fos);
      AlignNumber.setValue(Align.selectedIndex);
      AlignNumber.saveToStream(fos);
  }


}

