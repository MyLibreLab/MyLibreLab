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
import eu.hansolo.steelseries.tools.LedColor;
import eu.hansolo.steelseries.tools.LedType;
import java.awt.*;
import tools.*;
import javax.swing.JPanel;


public class GaugePanel extends JVSMain implements PanelIF
{
  private double value=0.0;;
  final private static Led myGauge = new Led();
  
  private VSBoolean InitState = new VSBoolean(false);
  private VSBoolean frameVisible = new VSBoolean(true);
  private VSComboBox frameDesign = new VSComboBox();
  private VSBoolean backGroundVisible = new VSBoolean(true);
  private VSColor CustomColor = new VSColor(new Color(253,153,0));
  
  private VSComboBox ledColor= new VSComboBox();
  private VSComboBox offColor= new VSComboBox();
  private VSComboBox ledType= new VSComboBox();
  private VSBoolean glow = new VSBoolean(true);
  private Boolean Running=false;
  private Boolean Blinking=false;
  

  public void processPanel(int pinIndex, double value, Object obj)
  { 
    this.value=value;
    if(pinIndex==0){
        if(this.value==0.0){
        myGauge.setLedOn(false);
        }
        if(this.value==1.0){
        myGauge.setLedOn(true);
        }
    }    
    if(pinIndex==1){
        if(this.value==0.0){
        myGauge.setLedBlinking(false);
        }
        if(this.value==1.0){
        myGauge.setLedBlinking(true);
        }
    }    
    element.jRepaint();
  }

   public void paint(java.awt.Graphics g)
   { 
  
   }

  public void GaugeSet(){
   if (element!=null)
    {
     myGauge.setLedBlinking(Blinking && Running); //Glow(glow.getValue());
     myGauge.setCustomLedColor(CustomColor.getValue());
     
     for(LedColor ColorTemp:LedColor.values()){
        if(ColorTemp.name().equalsIgnoreCase(ledColor.getItem(ledColor.selectedIndex))){
        myGauge.setLedColor(ColorTemp);
        }
     }  
     for(LedType ledTemp:LedType.values()){
        if(ledTemp.name().equalsIgnoreCase(ledType.getItem(ledType.selectedIndex))){
        myGauge.setLedType(ledTemp);
        }
     }
             
    }    
  }
  
  public void start(){
      super.start();
      if(element!=null){
      Running=true;    
      GaugeSet();
      myGauge.setLedOn(InitState.getValue());
      element.jRepaint();
      }
  }
  
  public void stop(){
      super.stop();
      if(element!=null){
      Running=false;    
      GaugeSet();
      myGauge.setLedOn(InitState.getValue());
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
    
    
    setName("Led_JV");
    
    
    element.jSetMinimumSize(10,10);
   
    for(LedColor colorTemp:LedColor.values()){
        //if(colorTemp.name().equalsIgnoreCase("CUSTOM")) break;
        ledColor.addItem(colorTemp.name());  
    }ledColor.selectedIndex=3; // ORANGE
    
    
    for(LedType symbolTemp:LedType.values()){
        ledType.addItem(symbolTemp.name());
     }ledType.selectedIndex=0; //ROUND
     
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
   element.jAddPEItem("LED Type",ledType, 0,50); 
   element.jAddPEItem("Led Color",ledColor, 0,50); 
   element.jAddPEItem("Led Custom Color",CustomColor, 0,50); 
    
 
   localize();
  }

  private void localize()
  {
    int d=6;
    String language;

    language="en_US"; 
   element.jSetPEItemLocale(d+0,language,"Init State");
   element.jSetPEItemLocale(d+1,language,"LED Type");
   element.jSetPEItemLocale(d+2,language,"LED Color");
   element.jSetPEItemLocale(d+3,language,"LED Custom Color");

    language="es_ES";

   element.jSetPEItemLocale(d+0,language,"Estado Inicial");
   element.jSetPEItemLocale(d+1,language,"Tipo de LED");
   element.jSetPEItemLocale(d+2,language,"Color de LED");
   element.jSetPEItemLocale(d+3,language,"Color Personalizado");
  
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
  CustomColor.loadFromStream(fis);
  ledColor.loadFromStream(fis);
  offColor.loadFromStream(fis);
  ledType.loadFromStream(fis);
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
  CustomColor.saveToStream(fos);
  ledColor.saveToStream(fos);
  offColor.saveToStream(fos);
  ledType.saveToStream(fos);
  glow.saveToStream(fos);
     
      
  }
  
}

