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
import eu.hansolo.steelseries.tools.Orientation;
import eu.hansolo.steelseries.tools.SymbolType;
import java.awt.*;
import tools.*;
import javax.swing.JPanel;


public class GaugePanel extends JVSMain implements PanelIF
{
  private int width=50, height=150;
  private double value=0.0;
  private double oldPin;
  final private static TrafficLight2 myGauge = new TrafficLight2();
  
  private VSBoolean InitStateRed = new VSBoolean(true);
  private VSBoolean InitStateYellow = new VSBoolean(false);
  private VSBoolean InitStateGreen = new VSBoolean(false);
  private VSBoolean frameVisible = new VSBoolean(true);
  private VSComboBox frameDesign = new VSComboBox();
  private VSBoolean backGroundVisible = new VSBoolean(true);
  private VSComboBox backGroundColor = new VSComboBox();
  
  private VSComboBox onColor= new VSComboBox();
  private VSComboBox offColor= new VSComboBox();
  private VSComboBox icon= new VSComboBox();
  private VSBoolean glow = new VSBoolean(true);
  

  public void processPanel(int pinIndex, double value, Object obj)
  { 
    try{
        this.value=value;
        //System.err.println("PinIndex:"+pinIndex+"Value:"+value);
        if(this.value==0.0){
        if(pinIndex==0){
            myGauge.setRedOn(false);
            myGauge.setRedBlinkEnabled(false);
        }
        if(pinIndex==1){
            myGauge.setYellowOn(false);
            myGauge.setYellowBlinkEnabled(false);
        }
        if(pinIndex==2){
            myGauge.setGreenOn(false);
            myGauge.setGreenBlinkEnabled(false);
        }
        }
        if(this.value==1.0){
        if(pinIndex==0) myGauge.setRedOn(true);
        if(pinIndex==1) myGauge.setYellowOn(true);
        if(pinIndex==2) myGauge.setGreenOn(true);
        }
        if(pinIndex==3 && this.value==1.0){
        if(myGauge.isGreenOn()) myGauge.setGreenBlinkEnabled(true);
        if(myGauge.isRedOn()) myGauge.setRedBlinkEnabled(true);
        if(myGauge.isYellowOn()) myGauge.setYellowBlinkEnabled(true);
        }
        if(pinIndex==3 && this.value==0.0){
        if(myGauge.isGreenOn()) myGauge.setGreenBlinkEnabled(false);
        if(myGauge.isRedOn()) myGauge.setRedBlinkEnabled(false);
        if(myGauge.isYellowOn()) myGauge.setYellowBlinkEnabled(false);
        }
        element.jRepaint();
    
    }catch(Exception e){
        System.out.println("Error:"+e);
    }
  }

   public void paint(java.awt.Graphics g)
   { 
  
   }

  public void GaugeSet(){
   if (element!=null)
    {
     //myGauge.setGlow(glow.getValue());
     //myGauge.setFrameVisible(frameVisible.getValue());
     //myGauge.setBackgroundVisible(backGroundVisible.getValue());
     
     myGauge.setBlinking(true);
     for(ColorDef ColorTemp:ColorDef.values()){
        if(ColorTemp.name().equalsIgnoreCase(onColor.getItem(onColor.selectedIndex))){
        //myGauge.setOnColor(ColorTemp);
        }
        if(ColorTemp.name().equalsIgnoreCase(offColor.getItem(offColor.selectedIndex))){
        //myGauge.setOffColor(ColorTemp);
        }
     }  
     for(SymbolType symbolTemp:SymbolType.values()){
        if(symbolTemp.name().equalsIgnoreCase(icon.getItem(icon.selectedIndex))){
        //myGauge.setSymbolType(symbolTemp);
        }
     }
     for(BackgroundColor colorTemp:BackgroundColor.values()){
        if(colorTemp.name().equalsIgnoreCase(backGroundColor.getItem(backGroundColor.selectedIndex))){
        //myGauge.setBackgroundColor(colorTemp);    
        }
     }
     for(FrameDesign colorTemp:FrameDesign.values()){
        if(colorTemp.name().equalsIgnoreCase(frameDesign.getItem(frameDesign.selectedIndex))){
        //myGauge.setFrameDesign(colorTemp);    
        }
     }
    
             
    }    
  }
  
  public void start(){
      super.start();
      if(element!=null){
      GaugeSet();
      myGauge.setRedOn(InitStateRed.getValue());
      myGauge.setYellowOn(InitStateYellow.getValue());
      myGauge.setGreenOn(InitStateGreen.getValue());
      element.jRepaint();
      }
  }
  public void stop(){
      super.stop();
      if(element!=null){
       GaugeSet();
       myGauge.setRedBlinkEnabled(false);   
       myGauge.setYellowBlinkEnabled(false);   
       myGauge.setGreenBlinkEnabled(false); 
       myGauge.setRedOn(InitStateRed.getValue());
       myGauge.setYellowOn(InitStateYellow.getValue());
       myGauge.setGreenOn(InitStateGreen.getValue());
       element.jRepaint(); 
      }
  }
  public void init()
  {
    initPins(0,0,0,0);
    
    setSize(100,150);    
    
    element.jSetResizeSynchron(true);
    
    initPinVisibility(false,false,false,false);
    element.jSetInnerBorderVisibility(false);
    
    element.jSetResizable(true);
    
    
    setName("Battery_JV");
    
    
    element.jSetMinimumSize(30,30);
   
    for(ColorDef colorTemp:ColorDef.values()){
        if(colorTemp.name().equalsIgnoreCase("CUSTOM")) break;
        onColor.addItem(colorTemp.name());
        offColor.addItem(colorTemp.name());
    }
    onColor.selectedIndex=3; // ORANGE
    offColor.selectedIndex=9; // BLACK
    
    for(SymbolType symbolTemp:SymbolType.values()){
        icon.addItem(symbolTemp.name());
     }icon.selectedIndex=6; //BATTERY ICON
     
     for(BackgroundColor colorTemp:BackgroundColor.values()){
       if(colorTemp.name().equalsIgnoreCase("CUSTOM")) break; 
       backGroundColor.addItem(colorTemp.name());
    }backGroundColor.selectedIndex=18; // noisy plastic
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
   element.jAddPEItem("Init_State_RED",InitStateRed, 0,0); 
   element.jAddPEItem("Init_State_YELLOW",InitStateYellow, 0,0); 
   element.jAddPEItem("Init_State_GREEN",InitStateGreen, 0,0); 
   //element.jAddPEItem("Frame_Visible",frameVisible, 0,0);
   //element.jAddPEItem("Frame_Desing",frameDesign, 0,50); 
   //element.jAddPEItem("Background_Visible",backGroundVisible, 0,0);
   //element.jAddPEItem("Background_Color",backGroundColor, 0,50); 
   //element.jAddPEItem("Icono",icon, 0,50); 
   //element.jAddPEItem("On Color",onColor, 0,50); 
   //element.jAddPEItem("Off Color",offColor, 0,50); 
   //element.jAddPEItem("Glow",glow, 0,0); 
 
   localize();
  }

  private void localize()
  {
    int d=6;
    String language;

    language="en_US"; 

   element.jSetPEItemLocale(d+0,language,"Init State RED");
   element.jSetPEItemLocale(d+1,language,"Init State YELLOW");
   element.jSetPEItemLocale(d+2,language,"Init State GREEN");

    language="es_ES";

   element.jSetPEItemLocale(d+0,language,"Estado Inicial ROJO");
   element.jSetPEItemLocale(d+1,language,"Estado Inicial AMARILLO");
   element.jSetPEItemLocale(d+2,language,"Estado Inicial VERDE");
  
  }
  
  
  public void propertyChanged(Object o)
  { 
    
    GaugeSet();
    element.jRepaint();
  }



  public void loadFromStream(java.io.FileInputStream fis)
  {
    
  InitStateRed.loadFromStream(fis);
  InitStateYellow.loadFromStream(fis);
  InitStateGreen.loadFromStream(fis);
 
    
    element.jRepaint();
    
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {  
  
  InitStateRed.saveToStream(fos);
  InitStateYellow.saveToStream(fos);
  InitStateGreen.saveToStream(fos);
  
     
      
  }
  
}

