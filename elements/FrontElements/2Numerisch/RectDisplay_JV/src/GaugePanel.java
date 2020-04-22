//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia (cswi@gmx.de)                         *
//* Copyright (C) 2017  Javier Velásquez (javiervelasquez125@gmail.com)                                                                           *
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
import eu.hansolo.steelseries.gauges.DisplayRectangular;
import eu.hansolo.steelseries.tools.FrameDesign;
import eu.hansolo.steelseries.tools.LcdColor;
import eu.hansolo.steelseries.tools.NumberSystem;
import java.awt.*;
import tools.*;
import javax.swing.JPanel;


public class GaugePanel extends JVSMain implements PanelIF
{

  private double value=0.0;

  final private static DisplayRectangular myGauge = new DisplayRectangular();
  private VSDouble InitialValue=new VSDouble(0.0);
  

  private VSBoolean digitalFontEn = new VSBoolean(true);
  private VSComboBox lcdDisplayColor = new VSComboBox();

  private VSBoolean backGroundVisible = new VSBoolean(true);
  private VSBoolean frameVisible = new VSBoolean(true);

  private VSComboBox frameDesign = new VSComboBox();
   
  private VSInteger Decimales = new VSInteger(0);
  private VSComboBox numberSystemCbox = new VSComboBox();
  private VSString unitsString = new VSString("units ");
   private VSBoolean ScientificFormat = new VSBoolean(false);
  private VSBoolean unitsVisible = new VSBoolean(true);
         



  public void processPanel(int pinIndex, double value, Object obj)
  { 
    //if (obj instanceof VSDouble){
    //System.err.println("ProccessPanel() NewVal:"+value);
    this.value=value;
    myGauge.setValue(value);
    //myCompass.repaint();
    element.jRepaint();
   // }
   
  }

   public void paint(java.awt.Graphics g)
   { 
    if(element!=null){
        GaugeSet();
        element.jRepaint();
    //super.paint(g);     
    }     
   }

  public void GaugeSet(){
   if (element!=null)
    {
     myGauge.setDigitalFont(digitalFontEn.getValue());
     myGauge.setLcdScientificFormat(ScientificFormat.getValue());
     myGauge.setLcdUnitStringVisible(unitsVisible.getValue());
     myGauge.setLcdDecimals(Decimales.getValue());
     myGauge.setLcdUnitString(unitsString.getValue());
     myGauge.setFrameVisible(frameVisible.getValue());
     
     
     for(LcdColor colorTemp:LcdColor.values()){
        if(colorTemp.name().equalsIgnoreCase(lcdDisplayColor.getItem(lcdDisplayColor.selectedIndex))){
        myGauge.setLcdColor(colorTemp);    
        }
     }        
     for(NumberSystem numTemp:NumberSystem.values()){
        if(numTemp.name().equalsIgnoreCase(numberSystemCbox.getItem(numberSystemCbox.selectedIndex))){
        myGauge.setLcdNumberSystem(numTemp); 
        myGauge.setLcdVisible(true);
        }
     }  
     for(FrameDesign colorTemp:FrameDesign.values()){
        if(colorTemp.name().equalsIgnoreCase(frameDesign.getItem(frameDesign.selectedIndex))){
        myGauge.setFrameDesign(colorTemp);    
        }
     } 
             
    }    
  }
  
  public void start(){
      if(element!=null){
      myGauge.setValue(InitialValue.getValue());
      element.jRepaint();
      }
  }
  
  public void init()
  {
    initPins(0,0,0,0);
    
    setSize(180,70);    
    
    element.jSetResizeSynchron(false);
    
    initPinVisibility(false,false,false,false);
    element.jSetInnerBorderVisibility(false);
    
    element.jSetResizable(true);
    
    
    setName("RectDisplay_JV");
    
    this.value=InitialValue.getValue();
    
    
    element.jSetMinimumSize(30,25);
   
    for(LcdColor colorTemp:LcdColor.values()){
       if(colorTemp.name().equalsIgnoreCase("CUSTOM")) break; 
       lcdDisplayColor.addItem(colorTemp.name());
    }lcdDisplayColor.selectedIndex=2; //Orange  
    for(NumberSystem numTemp:NumberSystem.values()){ 
       numberSystemCbox.addItem(numTemp.name());
    }numberSystemCbox.selectedIndex=0; // DEC  
    for(FrameDesign colorTemp:FrameDesign.values()){
       if(colorTemp.name().equalsIgnoreCase("CUSTOM")) break; 
       frameDesign.addItem(colorTemp.name());
    }frameDesign.selectedIndex=2; //SHINNY METAL  
    
  }
  
  public void xOnInit(){
    GaugeSet();
    JPanel panel =element.getFrontPanel();
    panel.setLayout(new java.awt.BorderLayout());
    panel.add(myGauge, BorderLayout.CENTER); 
    element.setAlwaysOnTop(true);
    //myCompass.repaint();
    element.jRepaint();
  }
  
  public void setPropertyEditor()
  {
   element.jAddPEItem("Init_Value",InitialValue, -9999999.0,99999999.0); 
   element.jAddPEItem("LCD_Color",lcdDisplayColor, 0,50); 
   element.jAddPEItem("Scientific Format",ScientificFormat,0,0);
   element.jAddPEItem("Decimals",Decimales,0,6);
   element.jAddPEItem("Units Visible",unitsVisible, 0,0); 
   element.jAddPEItem("Units String",unitsString, 0,0); 
   element.jAddPEItem("Seven_Segment_Font",digitalFontEn, 0,0); 
   //element.jAddPEItem("Number System",numberSystemCbox,0,50); 
   element.jAddPEItem("Frame_Visible",frameVisible, 0,0);
   element.jAddPEItem("Frame_Design",frameDesign, 0,50); 
   localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";  
   element.jSetPEItemLocale(d+0,language,"Init Value");
   element.jSetPEItemLocale(d+1,language,"LCD Color");
   element.jSetPEItemLocale(d+2,language,"Scientific Format");
   element.jSetPEItemLocale(d+3,language,"Decimals");
   element.jSetPEItemLocale(d+4,language,"Units Visible");
   element.jSetPEItemLocale(d+5,language,"Units String");
   element.jSetPEItemLocale(d+6,language,"Seven Segment Font");
   //element.jSetPEItemLocale(d+7,language,"Number System");
   element.jSetPEItemLocale(d+7,language,"Frame_Visible");
   element.jSetPEItemLocale(d+8,language,"Frame_Design");
   
   language="es_ES";

   element.jSetPEItemLocale(d+0,language,"Valor Inicial");
   element.jSetPEItemLocale(d+1,language,"Color del LCD");
   element.jSetPEItemLocale(d+2,language,"Formato Cientifico");
   element.jSetPEItemLocale(d+3,language,"Decimales");
   element.jSetPEItemLocale(d+4,language,"Unidades Visible");
   element.jSetPEItemLocale(d+5,language,"Texto Unidades");
   element.jSetPEItemLocale(d+6,language,"Fuente Siete Segmentos");
   //element.jSetPEItemLocale(d+7,language,"Sistema Numerico");
   element.jSetPEItemLocale(d+7,language,"Marco Visible");
   element.jSetPEItemLocale(d+8,language,"Diseño del marco");
  }
  
  
  public void propertyChanged(Object o)
  { 
    this.value=InitialValue.getValue();  
    GaugeSet();
    element.jRepaint();
  }



  public void loadFromStream(java.io.FileInputStream fis)
  {
    digitalFontEn.loadFromStream(fis);
    lcdDisplayColor.loadFromStream(fis);
    backGroundVisible.loadFromStream(fis);
    frameDesign.loadFromStream(fis);
    ScientificFormat.loadFromStream(fis);
    InitialValue.loadFromStream(fis);
    frameVisible.loadFromStream(fis);
    
    Decimales.loadFromStream(fis);
    //numberSystemCbox.loadFromStream(fis);
    unitsString.loadFromStream(fis);
    ScientificFormat.loadFromStream(fis);
    unitsVisible.loadFromStream(fis);
 
    element.jRepaint();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {  
  digitalFontEn.saveToStream(fos);
  lcdDisplayColor.saveToStream(fos);
  backGroundVisible.saveToStream(fos);
  frameDesign.saveToStream(fos);
  ScientificFormat.saveToStream(fos);
  InitialValue.saveToStream(fos);
  frameVisible.saveToStream(fos);
  
  Decimales.saveToStream(fos);
  //numberSystemCbox.saveToStream(fos);
  unitsString.saveToStream(fos);
  ScientificFormat.saveToStream(fos);
  unitsVisible.saveToStream(fos);
  
  
     
  }
  
}

