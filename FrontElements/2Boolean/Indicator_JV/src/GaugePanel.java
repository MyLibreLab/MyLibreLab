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
  private double oldPin;
  final private static Indicator myGauge = new Indicator();
  
  private VSBoolean InitState = new VSBoolean(false);
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
    this.value=value;
    if(this.value==0.0){
    myGauge.setOn(false);
    }
    if(this.value==1.0){
    myGauge.setOn(true);
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
     //myGauge.init(element.jGetWidth(), element.jGetHeight());
     myGauge.setGlow(glow.getValue());
     myGauge.setFrameVisible(frameVisible.getValue());
     myGauge.setBackgroundVisible(backGroundVisible.getValue());
     
     
     for(ColorDef ColorTemp:ColorDef.values()){
        if(ColorTemp.name().equalsIgnoreCase(onColor.getItem(onColor.selectedIndex))){
        myGauge.setOnColor(ColorTemp);
        }
        if(ColorTemp.name().equalsIgnoreCase(offColor.getItem(offColor.selectedIndex))){
        myGauge.setOffColor(ColorTemp);
        }
     }  
     for(SymbolType symbolTemp:SymbolType.values()){
        if(symbolTemp.name().equalsIgnoreCase(icon.getItem(icon.selectedIndex))){
        myGauge.setSymbolType(symbolTemp);
        }
     }
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
      myGauge.setOn(InitState.getValue());
      element.jRepaint();
      }
  }
  
  public void init()
  {
    initPins(0,0,0,0);
    
    setSize(100,100);    
    
    element.jSetResizeSynchron(true);
    
    initPinVisibility(false,false,false,false);
    element.jSetInnerBorderVisibility(false);
    
    element.jSetResizable(true);
    
    
    setName("Indicator_JV");
    
    
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
   element.jAddPEItem("Init_State",InitState, 0,0); 
   element.jAddPEItem("Frame_Visible",frameVisible, 0,0);
   element.jAddPEItem("Frame_Desing",frameDesign, 0,50); 
   element.jAddPEItem("Background_Visible",backGroundVisible, 0,0);
   element.jAddPEItem("Background_Color",backGroundColor, 0,50); 
   element.jAddPEItem("Icono",icon, 0,50); 
   element.jAddPEItem("On Color",onColor, 0,50); 
   element.jAddPEItem("Off Color",offColor, 0,50); 
   element.jAddPEItem("Glow",glow, 0,0); 
 
   localize();
  }

  private void localize()
  {
    int d=6;
    String language;

    language="en_US"; 

   element.jSetPEItemLocale(d+0,language,"Init State");
   element.jSetPEItemLocale(d+1,language,"Frame Visible");
   element.jSetPEItemLocale(d+2,language,"Frame Desing");
   element.jSetPEItemLocale(d+3,language,"Background Visible");
   element.jSetPEItemLocale(d+4,language,"Background Color");
   element.jSetPEItemLocale(d+5,language,"Indicator Icon");
   element.jSetPEItemLocale(d+6,language,"ON Color");
   element.jSetPEItemLocale(d+7,language,"OFF Color");
   element.jSetPEItemLocale(d+8,language,"Glow Visible");

    language="es_ES";

   element.jSetPEItemLocale(d+0,language,"Estado Inicial");
   element.jSetPEItemLocale(d+1,language,"Marco Visible");
   element.jSetPEItemLocale(d+2,language,"Diseño del marco");
   element.jSetPEItemLocale(d+3,language,"Fondo Visible");
   element.jSetPEItemLocale(d+4,language,"Color de Fondo");
   element.jSetPEItemLocale(d+5,language,"Icono del Indicador");
   element.jSetPEItemLocale(d+6,language,"Color Activado");
   element.jSetPEItemLocale(d+7,language,"Color Desactivado");
   element.jSetPEItemLocale(d+8,language,"Ver Resplandor");
  
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
  onColor.loadFromStream(fis);
  offColor.loadFromStream(fis);
  icon.loadFromStream(fis);
  glow.loadFromStream(fis);
 
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
  onColor.saveToStream(fos);
  offColor.saveToStream(fos);
  icon.saveToStream(fos);
  glow.saveToStream(fos);
     
      
  }
  
}

