//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia (cswi@gmx.de)                         *
//* Copyright (C) 2018  Javier Velasquez (javiervelasquez125@gmail.com)                                                                            *
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

import SVGViewer.SVGManager;
import SVGViewer.SVGObject;
import VisualLogic.*;
import java.awt.*;
import VisualLogic.variables.*;
import java.awt.geom.Rectangle2D;
import tools.*;

public class LEDPanel extends JVSMain implements PanelIF
{
  private boolean on=false;
  private VSInteger strokeValue = new VSInteger(2);
  private BasicStroke stroke = new BasicStroke(1);
  private BasicStroke strokeLine = new BasicStroke(1);
  private SVGManager svgManager = new SVGManager();
  
    

  
  private VSBoolean initValue=new VSBoolean(false);
  private VSBoolean textVisible = new VSBoolean(true);
  private VSString captionON = new VSString("ON");
  private VSString captionOFF = new VSString("OFF");
  private VSBoolean baseVisible = new VSBoolean(true);
  private VSColorAdvanced baseColor = new VSColorAdvanced();
  private VSColorAdvanced strokeBaseColor = new VSColorAdvanced();
  private VSColorAdvanced shadowLigthColor = new VSColorAdvanced();
  private VSColorAdvanced shadowDarkColor = new VSColorAdvanced();
  
  private VSFont font=new VSFont(new Font("Dialog",1,15));
  private VSColor fontColor = new VSColor(Color.WHITE);
  private VSColor onButtonColor = new VSColor(Color.WHITE);
  private VSColor offButtonColor = new VSColor(Color.WHITE);
  private VSColorAdvanced buttonLigthColor = new VSColorAdvanced();
  private VSColorAdvanced buttonDarkColor = new VSColorAdvanced();
  private VSColor edgeColor = new VSColor(Color.BLACK);
  
  
  
  
  private void setOn(boolean value)
  {
    if (value!=on)
    {
      on=value;
      element.jRepaint();
    }
  }

  public void processPanel(int pinIndex, double value, Object obj)
  {
    if (value==0.0) setOn(false); else setOn(true);
  }

   public void paint(java.awt.Graphics g)
   {
      if (element!=null)
      {
         Graphics2D g2=(Graphics2D)g;
         Rectangle r=element.jGetBounds();
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
         stroke = new BasicStroke(strokeValue.getValue());
         
         int X0=0;
         int Y0=0;
         int tWidth=0;
         int tHeight=0;
         
         //----------------------------------------------------------------------------DRAW BUTTON BASE
         if(baseVisible.getValue()){
         tWidth =  (int) ((r.width*0.95)-strokeValue.getValue());
         tHeight = (int) ((r.height*0.95)-strokeValue.getValue());
         X0=(int) ((r.width * 0.05)+(strokeValue.getValue()/2));
         Y0=(int) ((r.height * 0.05)+ (strokeValue.getValue()/2));
         
         g2.setStroke(strokeLine);
         baseColor.setFillColor(g2);
         g2.fillOval(X0, Y0, tWidth, tHeight);
         
         strokeBaseColor.setFillColor(g2);
         g2.setStroke(stroke);
         g2.drawOval(X0, Y0, tWidth, tHeight);
         }
               
         //----------------------------------------------------------------------------DRAW BUTTON BASE SHADOW
         if(on){
         //tWidth =  (int) ((r.width*0.85));
         //tHeight = (int) ((r.height*0.85));
         //X0=(int) ((r.width * 0.1));
         //Y0=(int) ((r.height * 0.1));
         shadowLigthColor.setFillColor(g2);
         
         }else{
         shadowDarkColor.setFillColor(g2);
         }
         
         tWidth =  (int) ((r.width*0.8));
         tHeight = (int) ((r.height*0.8));
         X0=(int) ((r.width * 0.1));
         Y0=(int) ((r.height * 0.1)); 
         //shadowDarkColor.setFillColor(g2);
         //}
         
         g2.setStroke(strokeLine);
         
         g2.fillOval(X0, Y0, tWidth, tHeight);
         
         g2.setColor(edgeColor.getValue());
         //g2.setStroke(strokeLine);
         //g2.drawOval(X0, Y0, tWidth, tHeight);
         
         //----------------------------------------------------------------------------DRAW BUTTON SHADOW
         
         tWidth =  (int) ((r.width*0.8));
         tHeight = (int) ((r.height*0.8));
         X0=(int) ((r.width * 0.05));
         Y0=(int) ((r.height * 0.05));
         
         g2.setStroke(strokeLine);
         buttonDarkColor.setFillColor(g2);
         g2.fillOval(X0, Y0, tWidth, tHeight);
         
         g2.setColor(edgeColor.getValue());
         g2.setStroke(strokeLine);
         g2.drawOval(X0, Y0, tWidth, tHeight);
         
         
         //----------------------------------------------------------------------------DRAW BUTTON FRONT
         tWidth =  (int) ((r.width*0.8)-2-(strokeValue.getValue()));
         tHeight = (int) ((r.height*0.8)-2-(strokeValue.getValue()));
         //if(down.getValue()){
         //buttonDarkColor.setFillColor(g2);    
         //X0=(int) (1+(r.width * 0.125)+(strokeValue.getValue()/2));
         //Y0=(int) (1+(r.height * 0.125)+(strokeValue.getValue()/2));    
         //}else{
         X0=1+(int) ((r.width*0.01));
         Y0=1+(int) ((r.height*0.01));
         
         
         //}
         //g2.setStroke(strokeLine);
         //g2.fillOval(X0, Y0, tWidth, tHeight);
         
         
         int TempStroke = (int) ((element.jGetWidth()*0.1));
         g2.setStroke(new BasicStroke(TempStroke));
         buttonLigthColor.setFillColor(g2);
         g2.drawOval(X0+(TempStroke/2), Y0+(TempStroke/2), tWidth-TempStroke, tHeight-TempStroke);
         
         g2.setStroke(strokeLine);
          
         g2.setColor(edgeColor.getValue());
         g2.drawOval(X0, Y0, tWidth, tHeight);
         
        SVGObject light = svgManager.getSVGObject("light");
        SVGObject  blackim = svgManager.getSVGObject("path19684");
        SVGObject bubble = svgManager.getSVGObject("path2766");
        bubble.setVisible(true);
        blackim.setVisible(false);
        light.setVisible(true);
        
        if (on) light.setFillColor(onButtonColor.getValue()); else light.setFillColor(offButtonColor.getValue());
        
        int X0Im=(int) (r.width*0.1);
        int Y0Im=(int) (r.height*0.1);

        if(light!=null){
            
        light.translate(23, 23);
        bubble.translate(23, 23);
        blackim.translate(23, 23);
        
        svgManager.paint(g2,(int)tWidth-X0Im,(int)tHeight-Y0Im);    
        }
        
     
        
         
         
         //-----------------------------------------------------------------------------DRAW BOOLEN TEXT
         if(textVisible.getValue()){
            
            
            
            g2.setFont(font.getValue());
            g2.setColor(fontColor.getValue());
            
            FontMetrics fm = g2.getFontMetrics();

            String txtTemp = "";
            if(on){
                txtTemp=captionON.getValue();
            }else{
                txtTemp=captionOFF.getValue();  
            }


            Rectangle2D rFont = fm.getStringBounds(txtTemp,g2);
            if(on){
            X0=(int) ((r.width * 0.4) - (rFont.getWidth())/2);
            Y0=(int) ((r.height * 0.4)+ (rFont.getHeight())/4);
            }else{
            X0=(int) ((r.width * 0.4) - (rFont.getWidth())/2);
            Y0=(int) ((r.height * 0.4)+ (rFont.getHeight())/4);    
            }

            g2.drawString(txtTemp, X0, Y0);
         }
         
      }
   }
   
  public void init()
  {
    initPins(0,0,0,0);
    initPinVisibility(false,false,false,false);
    setSize(150,150);
    element.jSetInnerBorderVisibility(false);

    element.jSetResizeSynchron(true);
    element.jSetResizable(true);
    svgManager.loadFromFile(element.jGetSourcePath()+"led.svg");    
      
    setName("Led_Ind_JV");
    
    baseColor.color1=new Color(255,204,0);
    
    onButtonColor.setValue(new Color(255,0,0));
    offButtonColor.setValue(new Color(51,0,0));
    
    strokeBaseColor.color1=new Color(102,102,102);
    
    
    
    shadowLigthColor.color1=new Color(153,153,153);
    shadowLigthColor.color1Transparency=128;
    shadowDarkColor.color1=new Color(90,90,90);
    shadowDarkColor.color1Transparency=128;
    
    
    buttonLigthColor.color1=new Color(102,102,102);
    buttonLigthColor.color2=Color.WHITE;
    buttonLigthColor.p1= new Point(30,120);
    buttonLigthColor.p2= new Point(65,70);
    buttonLigthColor.modus=1;
    buttonLigthColor.wiederholung=true;
    
    
    buttonDarkColor.color1 =new Color(70,70,70);
    
   
  }
  
  public void start(){
      super.start();
      if(element!=null){
      on=initValue.getValue();
      element.jRepaint();
      }
      
  }
  
  
  public void setPropertyEditor()
  {
    //element.jAddPEItem("Farbe",col, 0,0);
    element.jAddPEItem("Anfangs-Wert",initValue, 0,0);
    //element.jAddPEItem("Schalter",asSwitch, 0,0);
    element.jAddPEItem("Beschriftung ON",captionON, 0,0);
    element.jAddPEItem("Beschriftung OFF",captionOFF, 0,0);
    element.jAddPEItem("Base Visible",baseVisible, 0,0);
    element.jAddPEItem("Base Farbe",baseColor, 0,0);
    element.jAddPEItem("Base Border Farbe",strokeBaseColor, 0,0);
    element.jAddPEItem("Shadow Light Farbe",shadowLigthColor, 0,0);
    element.jAddPEItem("Shadow Dark Farbe",shadowDarkColor, 0,0);
    element.jAddPEItem("Text Visible",textVisible, 0,0);
    element.jAddPEItem("Schriftart ON-OFF",font, 0,0);
    element.jAddPEItem("Schriftart ON-OFF Color",fontColor, 0,0);
    element.jAddPEItem("Button Ligth Farbe",buttonLigthColor, 0,0);
    element.jAddPEItem("Button Dark Farbe",buttonDarkColor, 0,0);
    element.jAddPEItem("ON Button Farbe",onButtonColor, 0,0);
    element.jAddPEItem("LED OFF Farbe",offButtonColor, 0,0);
    element.jAddPEItem("Button Edge Farbe",edgeColor, 0,0);
    
    localize();
  }


  private void localize()
  {
    int d=6;
    int i=0;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+ i++,language,"Init State");
    //element.jSetPEItemLocale(d+ i++,language,"Switch");
    element.jSetPEItemLocale(d+ i++,language,"ON Caption");
    element.jSetPEItemLocale(d+ i++,language,"OFF Caption");
    element.jSetPEItemLocale(d+ i++,language,"Base Visible");
    element.jSetPEItemLocale(d+ i++,language,"Base Color");
    element.jSetPEItemLocale(d+ i++,language,"Base Stroke Color");
    element.jSetPEItemLocale(d+ i++,language,"Shadow Ligth Color");
    element.jSetPEItemLocale(d+ i++,language,"Shadow Dark Color");
    element.jSetPEItemLocale(d+ i++,language,"Text Visible");
    element.jSetPEItemLocale(d+ i++,language,"ON-OFF Caption Font");
    element.jSetPEItemLocale(d+ i++,language,"ON-OFF Font Color");
    element.jSetPEItemLocale(d+ i++,language,"Button Ligth Color");
    element.jSetPEItemLocale(d+ i++,language,"Button Dark Color");
    element.jSetPEItemLocale(d+ i++,language,"LED ON Color");
    element.jSetPEItemLocale(d+ i++,language,"LED OFF Color");
    element.jSetPEItemLocale(d+ i++,language,"Button Edge Color");
    
    language="es_ES";

    
    i=0;

    element.jSetPEItemLocale(d+ i++,language,"Estado Inicial");
    //element.jSetPEItemLocale(d+ i++,language,"Interruptor");
    element.jSetPEItemLocale(d+ i++,language,"Texto Activado");
    element.jSetPEItemLocale(d+ i++,language,"Texto Desactivado");
    element.jSetPEItemLocale(d+ i++,language,"Base Visible");
    element.jSetPEItemLocale(d+ i++,language,"Color de Base");
    element.jSetPEItemLocale(d+ i++,language,"Color Borde Base");
    element.jSetPEItemLocale(d+ i++,language,"Color Sombra Desactivado");
    element.jSetPEItemLocale(d+ i++,language,"Color Sombra Activado");
    element.jSetPEItemLocale(d+ i++,language,"Texto Visible");
    element.jSetPEItemLocale(d+ i++,language,"Fuente Texto ON-OFF");
    element.jSetPEItemLocale(d+ i++,language,"Color Fuente ON-OFF");
    
    element.jSetPEItemLocale(d+ i++,language,"Color Claro del Boton");
    element.jSetPEItemLocale(d+ i++,language,"Color Obscuro del Boton");
    element.jSetPEItemLocale(d+ i++,language,"Color LED ON");
    element.jSetPEItemLocale(d+ i++,language,"Color LED OFF");
    element.jSetPEItemLocale(d+ i++,language,"Color Bordes");;
  
  }

  public void propertyChanged(Object o)
  { on=initValue.getValue();
    element.jRepaint();
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
    initValue.loadFromStream(fis);
    captionON.loadFromStream(fis);
    captionOFF.loadFromStream(fis);
    baseVisible.loadFromStream(fis);
    baseColor.loadFromStream(fis);
    strokeBaseColor.loadFromStream(fis);
    shadowLigthColor.loadFromStream(fis);
    shadowDarkColor.loadFromStream(fis);
    font.loadFromStream(fis);
    fontColor.loadFromStream(fis);
    onButtonColor.loadFromStream(fis);
    buttonLigthColor.loadFromStream(fis);
    buttonDarkColor.loadFromStream(fis);
    offButtonColor.loadFromStream(fis);
    edgeColor.loadFromStream(fis);
    textVisible.loadFromStream(fis);
      
      
      on=initValue.getValue();
      element.jRepaint();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    initValue.saveToStream(fos);
    captionON.saveToStream(fos);
    captionOFF.saveToStream(fos);
    baseVisible.saveToStream(fos);
    baseColor.saveToStream(fos);
    strokeBaseColor.saveToStream(fos);
    shadowLigthColor.saveToStream(fos);
    shadowDarkColor.saveToStream(fos);
    font.saveToStream(fos);
    fontColor.saveToStream(fos);
    onButtonColor.saveToStream(fos);
    buttonLigthColor.saveToStream(fos);
    buttonDarkColor.saveToStream(fos);
    offButtonColor.saveToStream(fos);
    edgeColor.saveToStream(fos);
    textVisible.saveToStream(fos);
    
  }


}
 
