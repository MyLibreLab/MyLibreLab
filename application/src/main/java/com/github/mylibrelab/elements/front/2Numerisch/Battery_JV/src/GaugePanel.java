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
import eu.hansolo.steelseries.extras.*;
import eu.hansolo.steelseries.tools.Orientation;
import java.awt.*;
import tools.*;
import javax.swing.JPanel;


public class GaugePanel extends JVSMain implements PanelIF
{
  private int width=50, height=150;
  private double value=0.0;
  private double oldPin;
  final private static Battery myGauge = new Battery();
  private VSDouble InitialValue=new VSDouble();
  private VSBoolean orientation = new VSBoolean(false);
  private VSComboBox orientationLight = new VSComboBox();
  

  public void processPanel(int pinIndex, double value, Object obj)
  { 
    this.value=value;
    if(this.value>100.0) value=100.0;
    if(this.value<0.0) value=0.0;
    myGauge.setValue((int)value);
    element.jRepaint();
   
  }

   public void paint(java.awt.Graphics g)
   { 
  
   }

  public void GaugeSet(){
   if (element!=null)
    {
     myGauge.setBackground(Color.yellow);
     
     if(orientation.getValue()){
     myGauge.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
     }else{
     myGauge.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);    
     } 
     
     for(Orientation orTemp:Orientation.values()){
        if(orTemp.name().equalsIgnoreCase(orientationLight.getItem(orientationLight.selectedIndex))){
        myGauge.setLightPosition(orTemp);    
        }
     }  
    
             
    }    
  }
  
  public void start(){
      if(element!=null){
      myGauge.setValue((int)InitialValue.getValue());
      element.jRepaint();
      }
  }
  
  public void init()
  {
    initPins(0,0,0,0);
    
    setSize(180,90);    
    
    element.jSetResizeSynchron(false);
    
    initPinVisibility(false,false,false,false);
    element.jSetInnerBorderVisibility(false);
    
    element.jSetResizable(true);
    
    
    setName("Battery_JV");
    
    this.value=InitialValue.getValue();
    
    
    element.jSetMinimumSize(30,25);
   
    for(Orientation orTemp:Orientation.values()){
        orientationLight.addItem(orTemp.name());
     }orientationLight.selectedIndex=6; //WEST
    
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
   //element.jAddPEItem("Font",font,0,0); 
   //element.jAddPEItem("Font_Color",fontColor,0,0); 

   //element.jAddPEItem("Min",minMeasured, -9999999.0,99999999.0); 
   //element.jAddPEItem("Max",maxMeasured, -9999999.0,99999999.0); 
   //element.jAddPEItem("LCD_Visible",lcdDisplayEn, 0,0); 
   //element.jAddPEItem("Seven_Segment_Font",digitalFontEn, 0,0); 
   //element.jAddPEItem("LCD_Color",lcdDisplayColor, 0,50); 
   element.jAddPEItem("Init_Value",InitialValue, -0,100.0); 
   //element.jAddPEItem("Pointer_Color",pointerColor, 0,50); 
   //element.jAddPEItem("Componet Orientation",orientation, 0,0);
   element.jAddPEItem("Orientation Light",orientationLight, 0,50); 
   //element.jAddPEItem("Treshold_Indicator",tresholdIndicatorEn, 0,0); 
   //element.jAddPEItem("Treshold_LED_Visible",tresholdLedIndicatorEn, 0,0);
   //element.jAddPEItem("LED_Color",ledColor, 0,0);
   //element.jAddPEItem("Treshold_Value",tresholdValue,-9999999.0,99999999.0);
   //element.jAddPEItem("Track_Enable",trackEnable,0,0);
   //element.jAddPEItem("Track_Start_Value",trackStart,-9999999.0,99999999.0);
   //element.jAddPEItem("Track_Section_Value",trackSection,-9999999.0,99999999.0);
   //element.jAddPEItem("Track_Stop_Value",trackStop,-9999999.0,99999999.0);
 
   localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US"; 
   //element.jAddPEItem("Font",font,0,0); 
   //element.jAddPEItem("Font_Color",fontColor,0,0); 

   //element.jAddPEItem("Min",minMeasured, -9999999.0,99999999.0); 
   //element.jAddPEItem("Max",maxMeasured, -9999999.0,99999999.0); 
   //element.jAddPEItem("LCD_Visible",lcdDisplayEn, 0,0); 
   //element.jAddPEItem("Seven_Segment_Font",digitalFontEn, 0,0); 
   //element.jAddPEItem("LCD_Color",lcdDisplayColor, 0,50); 
   element.jSetPEItemLocale(d+0,language,"Init Value");
   //element.jSetPEItemLocale(d+1,language,"Pointer Color");
   //element.jSetPEItemLocale(d+1,language,"Component Orientation");
   element.jSetPEItemLocale(d+1,language,"Light Orientation");
  
   //element.jAddPEItem("Treshold_Indicator",tresholdIndicatorEn, 0,0); 
   //element.jAddPEItem("Treshold_LED_Visible",tresholdLedIndicatorEn, 0,0);
   //element.jAddPEItem("LED_Color",ledColor, 0,0);
   //element.jAddPEItem("Treshold_Value",tresholdValue,-9999999.0,99999999.0);
   //element.jAddPEItem("Track_Enable",trackEnable,0,0);
   //element.jAddPEItem("Track_Start_Value",trackStart,-9999999.0,99999999.0);
   //element.jAddPEItem("Track_Section_Value",trackSection,-9999999.0,99999999.0);
   //element.jAddPEItem("Track_Stop_Value",trackStop,-9999999.0,99999999.0);
 

    language="es_ES";

   element.jSetPEItemLocale(d+0,language,"Valor Inicial");
   //element.jSetPEItemLocale(d+1,language,"Color del Apuntador");
   //element.jSetPEItemLocale(d+1,language,"Orientacion Componente");
   element.jSetPEItemLocale(d+1,language,"Iluminacion");
  
  }
  
  
  public void propertyChanged(Object o)
  { 
    this.value=InitialValue.getValue();  
    GaugeSet();
    element.jRepaint();
  }



  public void loadFromStream(java.io.FileInputStream fis)
  {
    InitialValue.loadFromStream(fis);
    orientation.loadFromStream(fis);
    orientationLight.loadFromStream(fis);
         
    element.jRepaint();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {  
  InitialValue.saveToStream(fos);
  orientation.saveToStream(fos);
  orientationLight.saveToStream(fos);
     
  }
  
}

