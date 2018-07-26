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
import eu.hansolo.steelseries.tools.SymbolType;
import java.awt.*;
import tools.*;
import javax.swing.JPanel;


public class GaugePanel extends JVSMain implements PanelIF
{
  private int width=50, height=150;
  private double value=0.0;
  final private static StopWatch myGauge = new StopWatch();
  
  private VSBoolean InitState = new VSBoolean(false);
  private VSBoolean frameVisible = new VSBoolean(true);
  private VSComboBox frameDesign = new VSComboBox();
  private VSBoolean backGroundVisible = new VSBoolean(true);
  private VSComboBox backGroundColor = new VSComboBox();
  
  private VSComboBox onColor= new VSComboBox();
  private VSComboBox offColor= new VSComboBox();
  private VSComboBox icon= new VSComboBox();
  private VSBoolean FlatNeedle = new VSBoolean(true);
   private ExternalIF circuitElement;
  

  public void processPanel(int pinIndex, double value, Object obj)
  { 
    //this.value=value;
    if(value==0.0){
    myGauge.stop();
    circuitElement=element.getCircuitElement();
    String ValueTime= myGauge.getMeasuredTime();
    int PinIndexTemp=1; //Pin 1,2 y 3
    try{    
       for(String numTemp:ValueTime.split(":")){
        Integer IntTemp = Integer.valueOf(numTemp);     
        circuitElement.Change(PinIndexTemp,new VSInteger(IntTemp));
        PinIndexTemp++;
        }
    }catch(Exception e){
        System.out.println("Error:"+e);   
    }   
    circuitElement.Change(0,new VSString(ValueTime));
    }
    if(value==1.0){    
    //myGauge.stop();
    myGauge.start();
    }
    
  }

   public void paint(java.awt.Graphics g)
   { 
    GaugeSet();
    element.jRepaint();
   }

  public void GaugeSet(){
   if (element!=null)
    {
     myGauge.setFlatNeedle(FlatNeedle.getValue());
     myGauge.setFrameVisible(frameVisible.getValue());
     myGauge.setBackgroundVisible(backGroundVisible.getValue());
    
     for(BackgroundColor colorTemp:BackgroundColor.values()){
        if(colorTemp.name().equalsIgnoreCase(backGroundColor.getItem(backGroundColor.selectedIndex))){
        myGauge.setBackgroundColor(colorTemp);    
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
      super.start();
      if(element!=null){
      GaugeSet();
      myGauge.setRunning(false);
      element.jRepaint();
      }
  }
  public void stop(){
      super.stop();
      if(element!=null){
      GaugeSet();
      myGauge.setRunning(false);
      myGauge.stop();
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
    
    
    setName("Battery_JV");
    
    
    element.jSetMinimumSize(30,25);
   
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
    
   element.jAddPEItem("Frame_Visible",frameVisible, 0,0);
   element.jAddPEItem("Frame_Desing",frameDesign, 0,50); 
   element.jAddPEItem("Background_Visible",backGroundVisible, 0,0);
   element.jAddPEItem("Background_Color",backGroundColor, 0,50); 
   element.jAddPEItem("Flat_Needle",FlatNeedle, 0,0); 
 
   localize();
  }

  private void localize()
  {
    int d=6;
    String language;

    language="en_US"; 
   element.jSetPEItemLocale(d+0,language,"Frame Visible");
   element.jSetPEItemLocale(d+1,language,"Frame Desing");
   element.jSetPEItemLocale(d+2,language,"Background Visible");
   element.jSetPEItemLocale(d+3,language,"Background Color");
   element.jSetPEItemLocale(d+4,language,"Flat Needle");;

    language="es_ES";
   element.jSetPEItemLocale(d+0,language,"Marco Visible");
   element.jSetPEItemLocale(d+1,language,"Diseño del marco");
   element.jSetPEItemLocale(d+2,language,"Fondo Visible");
   element.jSetPEItemLocale(d+3,language,"Color de Fondo");
   element.jSetPEItemLocale(d+4,language,"Aguja Plana");
  
  }
  
  
  public void propertyChanged(Object o)
  { 
    if(InitState.getValue()) {
        value=1.0;
    }else{
        value=0.0;
    }
    GaugeSet();
    element.jRepaint();
  }



  public void loadFromStream(java.io.FileInputStream fis)
  {
    
  InitState.loadFromStream(fis);
  frameVisible.loadFromStream(fis);
  frameDesign.loadFromStream(fis);
  backGroundVisible.loadFromStream(fis);
  backGroundColor.loadFromStream(fis);
  FlatNeedle.loadFromStream(fis);
 
    if(InitState.getValue()) {
        value=1.0;
    }else{
        value=0.0;
    }
    
    element.jRepaint();
    
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {  
  
  InitState.saveToStream(fos);
  frameVisible.saveToStream(fos);
  frameDesign.saveToStream(fos);
  backGroundVisible.saveToStream(fos);
  backGroundColor.saveToStream(fos);
  FlatNeedle.saveToStream(fos);
     
      
  }
  
}

