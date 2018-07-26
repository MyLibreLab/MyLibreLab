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
  private double value=0.0;
  final private static LightBulb myGauge = new LightBulb();
  
  private VSBoolean InitState = new VSBoolean(false);
  
  private VSColor glowColor = new VSColor(new Color(253,153,0));
  

  public void processPanel(int pinIndex, double value, Object obj)
  { 
    this.value=value;
    if(this.value==0.0){
    myGauge.setOn(false);
    }
    if(this.value==1.0){
    myGauge.setOn(true);
    }
    element.jRepaint();
  }

   public void paint(java.awt.Graphics g)
   { 
  
   }

  public void GaugeSet(){
   if (element!=null)
    {
     myGauge.setDirection(1);
     myGauge.setGlowColor(glowColor.getValue());
           
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
    
    
    setName("Ligth_Bulb_JV");
    
    
    element.jSetMinimumSize(20,20);
   
   
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
   element.jAddPEItem("Glow Color",glowColor, 0,0); 
 
   localize();
  }

  private void localize()
  {
    int d=6;
    String language;

    language="en_US"; 

   element.jSetPEItemLocale(d+0,language,"Init State");
   element.jSetPEItemLocale(d+1,language,"Glow Color");

    language="es_ES";

   element.jSetPEItemLocale(d+0,language,"Estado Inicial");
   element.jSetPEItemLocale(d+1,language,"Color Resplandor");
  
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
  glowColor.loadFromStream(fis);
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
  glowColor.saveToStream(fos);    
  }
  
}

