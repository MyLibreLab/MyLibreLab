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
import eu.hansolo.steelseries.tools.BackgroundColor;
import eu.hansolo.steelseries.tools.ColorDef;
import eu.hansolo.steelseries.tools.FrameDesign;
import eu.hansolo.steelseries.tools.LcdColor;
import eu.hansolo.steelseries.tools.LedColor;

import java.awt.*;
import tools.*;
import javax.swing.JPanel;


public class GaugePanel extends JVSMain implements PanelIF
{
  private int width=50, height=150;
  private double value=0.0;
  private double oldPin;
  final private static Horizon myGauge = new Horizon();
  private VSDouble InitialPitch=new VSDouble(0.0);
  private VSDouble InitialRoll=new VSDouble(0.0);
  
  private VSColor fontColor = new VSColor(Color.red);
  private VSFont font = new VSFont(new Font("Dialog",Font.BOLD,11));
  
  private VSComboBox ledColor = new VSComboBox();
  private VSComboBox barColor = new VSComboBox();
  private VSDouble minMeasured = new VSDouble(-360.0);
  private VSBoolean lcdDisplayEn = new VSBoolean(false);
  private VSBoolean digitalFontEn = new VSBoolean(true);
  private VSComboBox lcdDisplayColor = new VSComboBox();
  private VSComboBox pointerColor = new VSComboBox();
  private VSBoolean backGroundVisible = new VSBoolean(true);
  private VSBoolean frameVisible = new VSBoolean(true);
  private VSComboBox backGroundColor = new VSComboBox();
  private VSComboBox frameDesign = new VSComboBox();
  private VSBoolean tresholdIndicatorEn = new VSBoolean(true);
  private VSDouble maxMeasured = new VSDouble(360.0);
  private VSBoolean tresholdLedIndicatorEn = new VSBoolean(true);
  private VSDouble tresholdValue = new VSDouble(50.0);
  private VSBoolean trackEnable = new VSBoolean(true);
  
  private VSDouble trackStart = new VSDouble(70.0);
  private VSDouble trackSection = new VSDouble(85.0);
  private VSDouble trackStop = new VSDouble(100.0);
  
  //private VSColor skyColor = new VSColor(new Color(153,153,255)); //
  private VSColor skyColor = new VSColor(new Color(204,204,255)); //
  private VSColor groundColor = new VSColor(new Color(0,0,51));
  
  



  public void processPanel(int pinIndex, double value, Object obj)
  { 
    //if (obj instanceof VSDouble){
    //System.err.println("ProccessPanel() NewVal:"+value);
    this.value=value;
    
    if(pinIndex==0){
    myGauge.setPitch(this.value);    
    }
    if(pinIndex==1){
    myGauge.setRoll(this.value);    
    }
    //myCompass.repaint();
    //
   // }
   
  }

   public void paint(java.awt.Graphics g)
   { 
    if(element!=null){
    //super.paint(g); 
    element.jRepaint();    
    }     
   }

  public void GaugeSet(){
   if (element!=null)
    {
    
     //myGauge.setMinValue(minMeasured.getValue());
     //myGauge.setMaxValue(maxMeasured.getValue());
     myGauge.setMinorTickmarkVisible(false);
     myGauge.setCustomGroundColor(groundColor.getValue());
     myGauge.setCustomSkyColor(skyColor.getValue());
     myGauge.setCustomColors(true);
     
     myGauge.setLcdVisible(lcdDisplayEn.getValue());
     myGauge.setDigitalFont(digitalFontEn.getValue());
     myGauge.setBackgroundVisible(backGroundVisible.getValue());
     myGauge.setThresholdVisible(tresholdIndicatorEn.getValue());
     myGauge.setLedVisible(tresholdLedIndicatorEn.getValue());
     myGauge.setThreshold(tresholdValue.getValue());
     myGauge.setTrackVisible(trackEnable.getValue());
     myGauge.setTrackStart(trackStart.getValue());
     myGauge.setTrackSection(trackSection.getValue());
     myGauge.setTrackStop(trackStop.getValue());
     myGauge.setFont(font.getValue());
     myGauge.setForeground(fontColor.getValue());
     myGauge.setFrameVisible(frameVisible.getValue());
     
     for(LcdColor colorTemp:LcdColor.values()){
        if(colorTemp.name().equalsIgnoreCase(lcdDisplayColor.getItem(lcdDisplayColor.selectedIndex))){
        myGauge.setLcdColor(colorTemp);    
        }
     }  
     for(LedColor colorTemp:LedColor.values()){
        if(colorTemp.name().equalsIgnoreCase(ledColor.getItem(ledColor.selectedIndex))){
        myGauge.setLedColor(colorTemp);    
        } 
     }  
     for(BackgroundColor colorTemp:BackgroundColor.values()){
        if(colorTemp.name().equalsIgnoreCase(backGroundColor.getItem(backGroundColor.selectedIndex))){
        myGauge.setBackgroundColor(colorTemp);    
        }
     }  
     for(ColorDef colorTemp:ColorDef.values()){
        if(colorTemp.name().equalsIgnoreCase(pointerColor.getItem(pointerColor.selectedIndex))){
        myGauge.setPointerColor(colorTemp);    
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
      myGauge.setPitch(InitialPitch.getValue());
      myGauge.setRoll(InitialRoll.getValue());
      element.jRepaint();
      }
  }
  
  public void init()
  {
    initPins(0,0,0,0);
    
    setSize(180,180);    
    
    element.jSetResizeSynchron(true);
    
    initPinVisibility(false,false,false,false);
    element.jSetInnerBorderVisibility(false);
    
    element.jSetResizable(true);
    
    
    setName("Horizon_JV");
    
    this.value=InitialPitch.getValue();
    
    
    element.jSetMinimumSize(30,25);
   
    for(LcdColor colorTemp:LcdColor.values()){
       if(colorTemp.name().equalsIgnoreCase("CUSTOM")) break; 
       lcdDisplayColor.addItem(colorTemp.name());
    }lcdDisplayColor.selectedIndex=0;  
    for(LedColor colorTemp:LedColor.values()){
       if(colorTemp.name().equalsIgnoreCase("CUSTOM")) break; 
       ledColor.addItem(colorTemp.name());
    }ledColor.selectedIndex=0; 
    for(BackgroundColor colorTemp:BackgroundColor.values()){
       if(colorTemp.name().equalsIgnoreCase("CUSTOM")) break; 
       backGroundColor.addItem(colorTemp.name());
    }backGroundColor.selectedIndex=17; // LINEN  
    for(ColorDef colorTemp:ColorDef.values()){
       if(colorTemp.name().equalsIgnoreCase("CUSTOM")) break; 
       pointerColor.addItem(colorTemp.name());
    }pointerColor.selectedIndex=3; // Init Value ORANGE  
    for(FrameDesign colorTemp:FrameDesign.values()){
       if(colorTemp.name().equalsIgnoreCase("CUSTOM")) break; 
       frameDesign.addItem(colorTemp.name());
    }frameDesign.selectedIndex=2; //SHINNY METAL  

  
  barColor.addItem("");
    
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

   //element.jAddPEItem("Min",minMeasured, -360.0,360.0); 
   //element.jAddPEItem("Max",maxMeasured, -360.0,360.0); 
   //element.jAddPEItem("LCD_Visible",lcdDisplayEn, 0,0); 
   //element.jAddPEItem("Seven_Segment_Font",digitalFontEn, 0,0); 
   //element.jAddPEItem("LCD_Color",lcdDisplayColor, 0,50); 
   element.jAddPEItem("Init_Picht_Value",InitialPitch, -360.0,360.0); 
   element.jAddPEItem("Init_Roll_Value",InitialRoll, -360.0,360.0); 
   element.jAddPEItem("Pointer_Color",pointerColor, 0,50); 
   element.jAddPEItem("Sky_Color",skyColor, 0,0); 
   element.jAddPEItem("Ground_Color",groundColor, 0,0); 
   element.jAddPEItem("Frame_Visible",frameVisible, 0,0);
   element.jAddPEItem("Frame_Design",frameDesign, 0,50); 
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
   //element.jSetPEItemLocale(d+0,language,"Min Value");
   //element.jSetPEItemLocale(d+1,language,"Max Value");
   element.jSetPEItemLocale(d+0,language,"Init Pitch Value");
   element.jSetPEItemLocale(d+1,language,"Init Roll Value");
   element.jSetPEItemLocale(d+2,language,"Pointer Color");
   element.jSetPEItemLocale(d+3,language,"Sky Color");
   element.jSetPEItemLocale(d+4,language,"Ground Color");
   element.jSetPEItemLocale(d+5,language,"Frame_Visible");
   element.jSetPEItemLocale(d+6,language,"Frame_Design");
   //element.jAddPEItem("Treshold_Indicator",tresholdIndicatorEn, 0,0); 
   //element.jAddPEItem("Treshold_LED_Visible",tresholdLedIndicatorEn, 0,0);
   //element.jAddPEItem("LED_Color",ledColor, 0,0);
   //element.jAddPEItem("Treshold_Value",tresholdValue,-9999999.0,99999999.0);
   //element.jAddPEItem("Track_Enable",trackEnable,0,0);
   //element.jAddPEItem("Track_Start_Value",trackStart,-9999999.0,99999999.0);
   //element.jAddPEItem("Track_Section_Value",trackSection,-9999999.0,99999999.0);
   //element.jAddPEItem("Track_Stop_Value",trackStop,-9999999.0,99999999.0);
 

    language="es_ES";

   //element.jSetPEItemLocale(d+0,language,"Valor Minimo");
   //element.jSetPEItemLocale(d+1,language,"Valor Maximo");
   element.jSetPEItemLocale(d+0,language,"Valor Inicial");
   element.jSetPEItemLocale(d+1,language,"Angulo Inicial");
   element.jSetPEItemLocale(d+2,language,"Color del Apuntador");
   element.jSetPEItemLocale(d+3,language,"Color del Cielo");
   element.jSetPEItemLocale(d+4,language,"Color de la Tierra");
   element.jSetPEItemLocale(d+5,language,"Ver Marco");
   element.jSetPEItemLocale(d+6,language,"Diseño del Marco");
  }
  
  
  public void propertyChanged(Object o)
  { 
    this.value=InitialPitch.getValue();  
    GaugeSet();
    element.jRepaint();
  }



  public void loadFromStream(java.io.FileInputStream fis)
  {
    ledColor.loadFromStream(fis);
    barColor.loadFromStream(fis);
    minMeasured.loadFromStream(fis);
    lcdDisplayEn.loadFromStream(fis);
    digitalFontEn.loadFromStream(fis);
    lcdDisplayColor.loadFromStream(fis);
    pointerColor.loadFromStream(fis);
    backGroundVisible.loadFromStream(fis);
    backGroundColor.loadFromStream(fis);
    frameDesign.loadFromStream(fis);
    tresholdIndicatorEn.loadFromStream(fis);
    maxMeasured.loadFromStream(fis);
    tresholdLedIndicatorEn.loadFromStream(fis);
    tresholdValue.loadFromStream(fis);
    trackEnable.loadFromStream(fis);
    trackStart.loadFromStream(fis);
    trackSection.loadFromStream(fis);
    trackStop.loadFromStream(fis);
    InitialPitch.loadFromStream(fis);
    InitialRoll.loadFromStream(fis);
    frameVisible.loadFromStream(fis);

         
    element.jRepaint();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {  
  ledColor.saveToStream(fos);
  barColor.saveToStream(fos);
  minMeasured.saveToStream(fos);
  lcdDisplayEn.saveToStream(fos);
  digitalFontEn.saveToStream(fos);
  lcdDisplayColor.saveToStream(fos);
  pointerColor.saveToStream(fos);
  backGroundVisible.saveToStream(fos);
  backGroundColor.saveToStream(fos);
  frameDesign.saveToStream(fos);
  tresholdIndicatorEn.saveToStream(fos);
  maxMeasured.saveToStream(fos);
  tresholdLedIndicatorEn.saveToStream(fos);
  tresholdValue.saveToStream(fos);
  trackEnable.saveToStream(fos);
  trackStart.saveToStream(fos);
  trackSection.saveToStream(fos);
  trackStop.saveToStream(fos);
  InitialPitch.saveToStream(fos);
  InitialRoll.saveToStream(fos);
 
  frameVisible.saveToStream(fos);
     
  }
  
}

