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
import eu.hansolo.steelseries.gauges.Linear;
import eu.hansolo.steelseries.tools.BackgroundColor;
import eu.hansolo.steelseries.tools.ColorDef;
import eu.hansolo.steelseries.tools.FrameDesign;
import eu.hansolo.steelseries.tools.LcdColor;
import eu.hansolo.steelseries.tools.LedColor;
import eu.hansolo.steelseries.tools.NumberFormat;
import eu.hansolo.steelseries.tools.NumberSystem;
import java.awt.*;
import tools.*;
import javax.swing.JPanel;


public class GaugePanel extends JVSMain implements PanelIF
{
  
  private double value=0.0;
  
  final private static Linear myGauge = new Linear();
  private VSDouble InitialValue=new VSDouble();
  
  private VSColor fontColor = new VSColor(Color.red);
  private VSFont font = new VSFont(new Font("Dialog",Font.BOLD,11));
  
  private VSComboBox ledColor = new VSComboBox();
  private VSDouble minMeasured = new VSDouble(0.0);
  private VSBoolean lcdDisplayEn = new VSBoolean(true);
  private VSBoolean digitalFontEn = new VSBoolean(true);
  private VSComboBox lcdDisplayColor = new VSComboBox();
  private VSComboBox pointerColor = new VSComboBox();
  private VSBoolean backGroundVisible = new VSBoolean(true);
  private VSBoolean frameVisible = new VSBoolean(true);
  private VSComboBox backGroundColor = new VSComboBox();
  private VSComboBox frameDesign = new VSComboBox();
  private VSBoolean tresholdIndicatorEn = new VSBoolean(true);
  private VSDouble maxMeasured = new VSDouble(100.0);
  private VSBoolean tresholdLedIndicatorEn = new VSBoolean(true);
  private VSDouble tresholdValue = new VSDouble(50.0);
  private VSBoolean trackEnable = new VSBoolean(true);
  private VSDouble trackStart = new VSDouble(70.0);
  private VSDouble trackSection = new VSDouble(85.0);
  private VSDouble trackStop = new VSDouble(100.0);
  
  private VSBoolean tittleNunitsEnable = new VSBoolean(true);
  private VSString tittle = new VSString("Tittle");
  private VSString subTittle = new VSString("#");
  private VSBoolean unitsEnable = new VSBoolean(true);
  private VSString unitString = new VSString("units");
  private VSBoolean scientificEnable = new VSBoolean(false);
  private VSInteger decimals = new VSInteger(0);
  private VSComboBox numberSystemCbox = new  VSComboBox();
  
  private VSBoolean textVisible = new VSBoolean(true);
  private VSBoolean marksVisible = new VSBoolean(true);
  
  private VSInteger majorTicks = new VSInteger(10);
  private VSInteger minorTicks = new VSInteger(10);
  private VSBoolean logScale = new VSBoolean(false);
  private VSComboBox labelNumberF = new VSComboBox();
  
  

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
    //super.paint(g);     
    }     
   }

  public void GaugeSet(){
   if (element!=null)
    {
     if(minMeasured.getValue()>=maxMeasured.getValue()){
     minMeasured.setValue(0);
     maxMeasured.setValue(100);
     
     }
     myGauge.setMinValue(minMeasured.getValue());
     myGauge.setMaxValue(maxMeasured.getValue());
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
     
     myGauge.setTicklabelsVisible(textVisible.getValue());
     myGauge.setTickmarksVisible(marksVisible.getValue());
     
     
     
     
     
     myGauge.setTitleAndUnitFontEnabled(tittleNunitsEnable.getValue());
     if(tittleNunitsEnable.getValue()){
     myGauge.setTitle(tittle.getValue());
     myGauge.setUnitString(subTittle.getValue());   
     }else{
     myGauge.setTitle("");
     myGauge.setUnitString("");    
     }
     
     myGauge.setLcdUnitStringVisible(unitsEnable.getValue());
     myGauge.setLcdUnitString(unitString.getValue());
     myGauge.setLcdScientificFormat(scientificEnable.getValue());
     myGauge.setLcdDecimals(decimals.getValue());
     
     myGauge.setLogScale(logScale.getValue());
     myGauge.setMaxNoOfMajorTicks(majorTicks.getValue());
     myGauge.setMaxNoOfMinorTicks(minorTicks.getValue());
     

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
        myGauge.setValueColor(colorTemp);    
        }
     }  
     for(FrameDesign colorTemp:FrameDesign.values()){
        if(colorTemp.name().equalsIgnoreCase(frameDesign.getItem(frameDesign.selectedIndex))){
        myGauge.setFrameDesign(colorTemp);    
        }
     }
     for(NumberSystem numTemp:NumberSystem.values()){
        if(numTemp.name().equalsIgnoreCase(numberSystemCbox.getItem(numberSystemCbox.selectedIndex))){
        myGauge.setLcdNumberSystem(numTemp);    
        }
     } 
     for(NumberFormat numTemp:NumberFormat.values()){
        if(numTemp.name().equalsIgnoreCase(labelNumberF.getItem(labelNumberF.selectedIndex))){
        myGauge.setLabelNumberFormat(numTemp);    
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
    
    setSize(250,120);    
    
    element.jSetResizeSynchron(false);
    
    initPinVisibility(false,false,false,false);
    element.jSetInnerBorderVisibility(false);
    
    element.jSetResizable(true);
    
    
    setName("Linaer_Indicator_JV");
    
    this.value=InitialValue.getValue();
    
    
    element.jSetMinimumSize(10,10);
   
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
    for(NumberSystem numTemp:NumberSystem.values()){ 
       numberSystemCbox.addItem(numTemp.name());
    }numberSystemCbox.selectedIndex=0; // DEC 
    for(NumberFormat numTemp:NumberFormat.values()){ 
       labelNumberF.addItem(numTemp.name());
    }numberSystemCbox.selectedIndex=0; // AUTO
      
    
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
   element.jAddPEItem("Init_Value",InitialValue, -9999999.0,99999999.0); 
   element.jAddPEItem("Min Value",minMeasured, -9999999.0,99999999.0); 
   element.jAddPEItem("Max Value",maxMeasured, -9999999.0,99999999.0);
   element.jAddPEItem("Tittles Enable",tittleNunitsEnable,0,0); 
   element.jAddPEItem("Tittle String",tittle,0,0); 
   element.jAddPEItem("Subtittle String",subTittle,0,0); 
   element.jAddPEItem("LCD_Visible",lcdDisplayEn, 0,0);
   element.jAddPEItem("LCD units Visible",unitsEnable,0,0); 
   element.jAddPEItem("LCD units String",unitString,0,0); 
   element.jAddPEItem("LCD Scientific Format",scientificEnable,0,0);
   element.jAddPEItem("LCD Decimals",decimals,0,15);
   element.jAddPEItem("LCD_Color",lcdDisplayColor, 0,50);
   element.jAddPEItem("Seven_Segment_Font",digitalFontEn, 0,0);  
   element.jAddPEItem("LCD Number System",numberSystemCbox,0,50);
   element.jAddPEItem("Tick Labels Visible",textVisible,0,0);
   element.jAddPEItem("Tick Marks Visible",marksVisible,0,0);
   
   element.jAddPEItem("Major Ticks",majorTicks,0,100);
   element.jAddPEItem("Minor Ticks",minorTicks,0,100);
   element.jAddPEItem("LogScale",logScale,0,0);
   element.jAddPEItem("Label Number Format",labelNumberF,0,0);
 
   
   element.jAddPEItem("Pointer_Color",pointerColor, 0,50); 
   element.jAddPEItem("Background_Visible",backGroundVisible, 0,0); 
   element.jAddPEItem("Background_Color",backGroundColor, 0,50); 
   element.jAddPEItem("Frame_Visible",frameVisible, 0,0);
   element.jAddPEItem("Frame_Design",frameDesign, 0,50); 
   element.jAddPEItem("Threshold_Indicator",tresholdIndicatorEn, 0,0); 
   element.jAddPEItem("Threshold_LED_Visible",tresholdLedIndicatorEn, 0,0);
   element.jAddPEItem("LED_Color",ledColor, 0,0);
   element.jAddPEItem("Threshold_Value",tresholdValue,-9999999.0,99999999.0);
   element.jAddPEItem("Track_Enable",trackEnable,0,0);
   element.jAddPEItem("Track_Start_Value",trackStart,-9999999.0,99999999.0);
   element.jAddPEItem("Track_Section_Value",trackSection,-9999999.0,99999999.0);
   element.jAddPEItem("Track_Stop_Value",trackStop,-9999999.0,99999999.0);
 
   localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US"; 
    element.jSetPEItemLocale(d+0,language,"Init_Value");
    element.jSetPEItemLocale(d+1,language,"Min Value");
    element.jSetPEItemLocale(d+2,language,"Max Value");
    element.jSetPEItemLocale(d+3,language,"Tittles Enable");
    element.jSetPEItemLocale(d+4,language,"Tittle String");
    element.jSetPEItemLocale(d+5,language,"Subtittle String");
    element.jSetPEItemLocale(d+6,language,"LCD_Visible");
    element.jSetPEItemLocale(d+7,language,"LCD units Visible");
    element.jSetPEItemLocale(d+8,language,"LCD units String");
    element.jSetPEItemLocale(d+9,language,"LCD Scientific Format");
    element.jSetPEItemLocale(d+10,language,"LCD Decimals");
    element.jSetPEItemLocale(d+11,language,"LCD_Color");
    element.jSetPEItemLocale(d+12,language,"Seven_Segment_Font");
    element.jSetPEItemLocale(d+13,language,"LCD Number System");
    element.jSetPEItemLocale(d+14,language,"Tick Labels Visible");
    element.jSetPEItemLocale(d+15,language,"Tick Marks Visible");
    
    element.jSetPEItemLocale(d+16,language,"Major Ticks");
    element.jSetPEItemLocale(d+17,language,"Minor Ticks");
    element.jSetPEItemLocale(d+18,language,"LogScale");
    element.jSetPEItemLocale(d+19,language,"Label Number Format");

    element.jSetPEItemLocale(d+20,language,"Pointer_Color");
    element.jSetPEItemLocale(d+21,language,"Background_Visible");
    element.jSetPEItemLocale(d+22,language,"Background_Color");
    element.jSetPEItemLocale(d+23,language,"Frame_Visible");
    element.jSetPEItemLocale(d+24,language,"Frame_Design");
    element.jSetPEItemLocale(d+25,language,"Threshold_Indicator");
    element.jSetPEItemLocale(d+26,language,"Threshold_LED_Visible");
    element.jSetPEItemLocale(d+27,language,"LED_Color");
    element.jSetPEItemLocale(d+28,language,"Threshold_Value");
    element.jSetPEItemLocale(d+29,language,"Track_Enable");
    element.jSetPEItemLocale(d+30,language,"Track_Start_Value");
    element.jSetPEItemLocale(d+31,language,"Track_Section_Value");
    element.jSetPEItemLocale(d+32,language,"Track_Stop_Value");
    
    language="es_ES";
    element.jSetPEItemLocale(d+0,language,"Valor Inicial");
    element.jSetPEItemLocale(d+1,language,"Valor Minimo");
    element.jSetPEItemLocale(d+2,language,"Valor Maximo");
    element.jSetPEItemLocale(d+3,language,"Titulos Visibles");
    element.jSetPEItemLocale(d+4,language,"Titulo");
    element.jSetPEItemLocale(d+5,language,"Subtitulo");
    element.jSetPEItemLocale(d+6,language,"LCD Visible");
    element.jSetPEItemLocale(d+7,language,"Texto unidades Visible");
    element.jSetPEItemLocale(d+8,language,"Texto unidades");
    element.jSetPEItemLocale(d+9,language,"Formato Cientifico");
    element.jSetPEItemLocale(d+10,language,"Decimales");
    element.jSetPEItemLocale(d+11,language,"LCD Color");
    element.jSetPEItemLocale(d+12,language,"Fuente 7 Segmentos");
    element.jSetPEItemLocale(d+13,language,"LCD Systema Numerico");
    element.jSetPEItemLocale(d+14,language,"Etiquetas Visibles");
    element.jSetPEItemLocale(d+15,language,"Marquillas Visibles");

    element.jSetPEItemLocale(d+16,language,"Divisones Mayores");
    element.jSetPEItemLocale(d+17,language,"Divisiones Menores");
    element.jSetPEItemLocale(d+18,language,"Escala Logaritmica");
    element.jSetPEItemLocale(d+19,language,"Formato de Etiquetas");

    element.jSetPEItemLocale(d+20,language,"Color de Aguja");
    element.jSetPEItemLocale(d+21,language,"Fondo Visible");
    element.jSetPEItemLocale(d+22,language,"Color de Fondo");
    element.jSetPEItemLocale(d+23,language,"Marco Visible");
    element.jSetPEItemLocale(d+24,language,"Estilo de marco");
    element.jSetPEItemLocale(d+25,language,"Indicator de Umbral");
    element.jSetPEItemLocale(d+26,language,"LED de Umbral Visible");
    element.jSetPEItemLocale(d+27,language,"Color del LED");
    element.jSetPEItemLocale(d+28,language,"Valor de Umbral");
    element.jSetPEItemLocale(d+29,language,"Habilitar Pista");
    element.jSetPEItemLocale(d+30,language,"Valor Inicio de Pista");
    element.jSetPEItemLocale(d+31,language,"Valor Seccion de pista");
    element.jSetPEItemLocale(d+32,language,"Valor final de Pista");

  }
  
  
  public void propertyChanged(Object o)
  { 
    this.value=InitialValue.getValue();  
    GaugeSet();
    element.jRepaint();
  }



  public void loadFromStream(java.io.FileInputStream fis)
  {
    ledColor.loadFromStream(fis);
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
    InitialValue.loadFromStream(fis);
    frameVisible.loadFromStream(fis);
    tittleNunitsEnable.loadFromStream(fis);
    tittle.loadFromStream(fis);
    subTittle.loadFromStream(fis);
    unitsEnable.loadFromStream(fis);
    unitString.loadFromStream(fis);
    scientificEnable.loadFromStream(fis);
    decimals.loadFromStream(fis);
    numberSystemCbox.loadFromStream(fis);
    textVisible.loadFromStream(fis);
    
    marksVisible.loadFromStream(fis);
    
    majorTicks.loadFromStream(fis);
    minorTicks.loadFromStream(fis);
    logScale.loadFromStream(fis);
    labelNumberF.loadFromStream(fis);

    if(InitialValue.getValue()>=maxMeasured.getValue()){
       InitialValue.setValue(0);
    }
         
      element.jRepaint();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {  
  ledColor.saveToStream(fos);
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
  InitialValue.saveToStream(fos);
  frameVisible.saveToStream(fos);
 
  tittleNunitsEnable.saveToStream(fos);
  tittle.saveToStream(fos);
  subTittle.saveToStream(fos);
  unitsEnable.saveToStream(fos);
  unitString.saveToStream(fos);
  scientificEnable.saveToStream(fos);
  decimals.saveToStream(fos);
  numberSystemCbox.saveToStream(fos);
  textVisible.saveToStream(fos);
 
  marksVisible.saveToStream(fos);
  majorTicks.saveToStream(fos);
  minorTicks.saveToStream(fos);
  logScale.saveToStream(fos);
  labelNumberF.saveToStream(fos);
  
  font.saveToStream(fos);
  fontColor.saveToStream(fos);
  frameVisible.saveToStream(fos);
     
  }
  
}

