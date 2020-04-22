//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia (cswi@gmx.de)                         *
//* Copyright (C) 2018  Javier Velasquez (javiervelasquez125@gmail.com)                                                                              *
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
import java.awt.*;
import VisualLogic.variables.*;
import tools.*;

public class LEDPanel extends JVSMain implements PanelIF
{
  private boolean on=false;
  //private SVGManager svgManager = new SVGManager();
  private VSColorAdvanced onColor = new VSColorAdvanced();
  private VSColorAdvanced offColor = new VSColorAdvanced();
 
  private VSInteger stroke = new VSInteger(5);
  
  private VSColorAdvanced StrokeColor = new VSColorAdvanced();
  
  private VSBoolean borderVisible = new VSBoolean(true);
  private VSColor borderColor = new VSColor(Color.BLACK);
  private VSBoolean fillAllPipe = new VSBoolean(false);
  
  private VSBoolean InitState = new VSBoolean(false);
 
  private VSInteger cnxSize = new VSInteger(5);
  
  
  private VSInteger valveSize = new VSInteger(15); //Percent of Width
  private VSInteger circleSizePerc = new VSInteger(25); //Percent of Width
  private VSBoolean Diaphragm = new VSBoolean(true);
  private VSBoolean manualValve = new VSBoolean(false);
  private VSBoolean roundValve = new VSBoolean(false);
  private VSBoolean controlValve = new VSBoolean(true);
  private VSColorAdvanced ValveOnColor = new VSColorAdvanced();
  private VSColorAdvanced ValveOffColor = new VSColorAdvanced();
   
  
  
  //private VSBoolean AutoOverlay = new VSBoolean(true);
  private VSColorAdvanced overlayColor = new VSColorAdvanced();
  Boolean overlay=false;
  
  
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
    if(pinIndex==0){
      if (value==0.0) setOn(false); else setOn(true);
    }
    if(pinIndex==1){
      if (value==0.0)  overlay=false; else overlay=true;
    }
  }

   public void paint(java.awt.Graphics g)
   {
     if (element!=null)
     {
      Graphics2D g2 = (Graphics2D)g;
      
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
      //g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      
      g2.setStroke(new BasicStroke(stroke.getValue()));
      
      int BorderSize=cnxSize.getValue();
      int ValveSize= (int) (element.jGetWidth()*(valveSize.getValue()/100.0));
      int circleSize= (int) (element.jGetWidth()*(circleSizePerc.getValue()/100.0));
      //System.err.println("Valve Size:"+ValveSize);
      int X0= 0;
      int Y0= 0;
      int tWidth =0;
      int tHeight =0;
      
      
       
      
      //------------------------------------------------------------------------INICIO SECCIÓN DE TUBO DERECHA
       
     if(manualValve.getValue()==false)
     {  
       //X0= element.jGetWidth()/2;
       X0= stroke.getValue();
       Y0= ((element.jGetHeight()/4) + (element.jGetHeight()/16))+ (stroke.getValue()/2);
       tWidth =((element.jGetWidth() - ((ValveSize+stroke.getValue())))-stroke.getValue())/2;
       tHeight =(element.jGetHeight()-(2*Y0));
       //X0= element.jGetWidth() - ((stroke.getValue()/2) -ValveSize+stroke.getValue());
       //X0-=tWidth;
        if(on){
            onColor.setFillColor(g2);
        } else{
            offColor.setFillColor(g2);
        }
        g2.setStroke(new BasicStroke(stroke.getValue()));
        g2.fillRect(X0,Y0+(3*tHeight/8),tWidth,tHeight/4);

      if(fillAllPipe.getValue() && on)onColor.setFillColor(g2); else offColor.setFillColor(g2);
      
      g2.setStroke(new BasicStroke(stroke.getValue()));
      g2.fillRect(X0,Y0+(3*tHeight/8),tWidth,tHeight/4);
      
      if(overlay) overlayColor.setFillColor(g2); else StrokeColor.setFillColor(g2);
      g2.drawRect(X0,Y0+(3*tHeight/8),tWidth,tHeight/4);
      
      if(borderVisible.getValue()){
      g2.setStroke(new BasicStroke(1));
      g2.setColor(borderColor.getValue());
      g2.drawRect(X0,Y0+(3*tHeight/8),tWidth,tHeight/4);
      }
     }
      //----------------------------------------------------------------------FINAL SECCIÓN DE TUBO DERECHA
       
      
      
      //-------------------------------------------------------------------------------------LEFT CONNECTOR
    if(manualValve.getValue()==false){
        BorderSize=cnxSize.getValue();
        X0= stroke.getValue();
        Y0= stroke.getValue()/2 + (element.jGetHeight()/4);
        tWidth =ValveSize-(stroke.getValue()/2);
        tHeight =(element.jGetHeight() - stroke.getValue()) - (element.jGetHeight()/2);
        //X0-=tWidth/2;

        if(on)ValveOnColor.setFillColor(g2); else ValveOffColor.setFillColor(g2);

        g2.setStroke(new BasicStroke(stroke.getValue()));
        
        if(Diaphragm.getValue()){
          g2.fillArc(X0, Y0, tWidth*2, tHeight, 90, 180);
          }else{
           if(roundValve.getValue()){
            g2.fillRoundRect(X0,Y0,tWidth,tHeight,20,20);
            }else{
            g2.fillRect(X0,Y0,tWidth,tHeight);    
            }  
          }
        

        if(overlay) overlayColor.setFillColor(g2); else StrokeColor.setFillColor(g2);

        if(Diaphragm.getValue()){
          g2.drawArc(X0, Y0, tWidth*2, tHeight, 90, 180);
          }else{
           if(roundValve.getValue()){
            g2.drawRoundRect(X0,Y0,tWidth,tHeight,20,20);
            }else{
            g2.drawRect(X0,Y0,tWidth,tHeight);    
            }  
          }
        
        if(borderVisible.getValue()){
        g2.setStroke(new BasicStroke(1));
        g2.setColor(borderColor.getValue());
        if(Diaphragm.getValue()){
          g2.drawArc(X0, Y0, tWidth*2, tHeight, 90, 180);
          }else{
           if(roundValve.getValue()){
            g2.drawRoundRect(X0,Y0,tWidth,tHeight,20,20);
            }else{
            g2.drawRect(X0,Y0,tWidth,tHeight);    
            }  
          }
        }

      }
      
      //--------------------------------------------------------------------INICIO SECCIÓN DE TUBO DE ARRIBA - ABAJO
      
       X0=((element.jGetWidth()/4) + (element.jGetWidth()/16))+ (stroke.getValue()/2);
       tHeight=((element.jGetHeight()- ((BorderSize+stroke.getValue())))-stroke.getValue());
       Y0=  ((stroke.getValue()/2) + BorderSize+ stroke.getValue());

       tWidth =(element.jGetWidth()-(2*X0));
       
        if(on){
            onColor.setFillColor(g2);
        } else{
            offColor.setFillColor(g2);
        }
        g2.setStroke(new BasicStroke(stroke.getValue()));
        //g2.fillRect(X0,Y0,tWidth,tHeight);

      if(on)onColor.setFillColor(g2); else offColor.setFillColor(g2);
      
        int XC=(int) (X0+(tWidth/2));
        int YC=(int)  Y0+(tHeight/2)-(stroke.getValue());
        int tWidth1 =  (int) ((element.jGetWidth()*0.02));
        int tWidthCircle  = (int) ((element.jGetWidth()*(circleSizePerc.getValue()/100.0)));
        int tHeigthCircle = (int) ((element.jGetHeight()*(circleSizePerc.getValue()/100.0)));
        
        Polygon p = new Polygon();
        p.addPoint(X0+(tWidth/2)+tWidth1,Y0+(tHeight/2)-(stroke.getValue()));
        p.addPoint(X0+(tWidth/2)-tWidth1,Y0+(tHeight/2)-(stroke.getValue()));
        p.addPoint(X0,Y0+(tHeight-(2*stroke.getValue())));
        p.addPoint(X0+(tWidth),Y0+(tHeight-(2*stroke.getValue())));
        
        g2.fillPolygon(p);
        
        Polygon p1 = new Polygon();
        p1.addPoint(X0+(tWidth/2)+tWidth1,Y0+(tHeight/2)-(stroke.getValue()));
        p1.addPoint(X0+(tWidth/2)-tWidth1,Y0+(tHeight/2)-(stroke.getValue()));
        p1.addPoint(X0,Y0-(stroke.getValue()/2));
        p1.addPoint(X0+(tWidth),Y0-(stroke.getValue()/2));
        
        g2.fillPolygon(p1);
        
        g2.setStroke(new BasicStroke(stroke.getValue()));
        //g2.fillRect(X0,Y0,tWidth,tHeight);
        g2.drawPolygon(p);
        g2.drawPolygon(p1);
      
      if(overlay) overlayColor.setFillColor(g2); else StrokeColor.setFillColor(g2);
      g2.drawPolygon(p);
      g2.drawPolygon(p1);
      
      if(borderVisible.getValue()){
      g2.setStroke(new BasicStroke(1));
      g2.setColor(borderColor.getValue());
      g2.drawPolygon(p);
      g2.drawPolygon(p1);
      }  
      if(controlValve.getValue()){
        if(on)onColor.setFillColor(g2); else offColor.setFillColor(g2);
        g2.fillOval(XC-(tWidthCircle/2), YC-(tHeigthCircle/2), tWidthCircle, tHeigthCircle);

        if(overlay) overlayColor.setFillColor(g2); else StrokeColor.setFillColor(g2);
        g2.setStroke(new BasicStroke(stroke.getValue()));
        g2.drawOval(XC-(tWidthCircle/2), YC-(tHeigthCircle/2), tWidthCircle, tHeigthCircle);

        if(borderVisible.getValue()){
        g2.setStroke(new BasicStroke(1));
        g2.setColor(borderColor.getValue());
        g2.drawOval(XC-(tWidthCircle/2), YC-(tHeigthCircle/2), tWidthCircle, tHeigthCircle);
        }  
      }
     //---------------------------------------------------------------------FINAL SECCIÓN DE TUBO DE ARRIBA
     
      
     
         
     //---------------------------------------------------------------------------INICIO CONECTOR DE ARRIBA
       Y0= (stroke.getValue()/2);
       X0= stroke.getValue()/2 + (element.jGetWidth()/4);
       tHeight =  BorderSize+stroke.getValue();
       tWidth =(element.jGetWidth()- stroke.getValue()) - (element.jGetWidth()/2);
       
       if(fillAllPipe.getValue() && on)onColor.setFillColor(g2); else offColor.setFillColor(g2);
       
       g2.setStroke(new BasicStroke(stroke.getValue()));
       g2.fillRect(X0,Y0,tWidth,tHeight);
       
       if(overlay) overlayColor.setFillColor(g2); else StrokeColor.setFillColor(g2);
       
       g2.drawRect(X0,Y0,tWidth,tHeight);
       if(borderVisible.getValue()){
       g2.setStroke(new BasicStroke(1));
       g2.setColor(borderColor.getValue());
       g2.drawRect(X0,Y0,tWidth,tHeight);
       }
       //----------------------------------------------------------------------------FINAL CONECTOR DE ARRIBA
       
       //---------------------------------------------------------------------------INICIO CONECTOR DE ABAJO
       Y0= element.jGetHeight()-(BorderSize+2*stroke.getValue());
       X0= stroke.getValue()/2 + (element.jGetWidth()/4);
       tHeight =BorderSize+stroke.getValue();
       tWidth =(element.jGetWidth()- stroke.getValue()) - (element.jGetWidth()/2);
       
       if(fillAllPipe.getValue() && on)onColor.setFillColor(g2); else offColor.setFillColor(g2);
       
       g2.setStroke(new BasicStroke(stroke.getValue()));
       g2.fillRect(X0,Y0,tWidth,tHeight);
       
       if(overlay) overlayColor.setFillColor(g2); else StrokeColor.setFillColor(g2);
       
       g2.drawRect(X0,Y0,tWidth,tHeight);
       if(borderVisible.getValue()){
       g2.setStroke(new BasicStroke(1));
       g2.setColor(borderColor.getValue());
       g2.drawRect(X0,Y0,tWidth,tHeight);
       }
       //----------------------------------------------------------------------------FINAL CONECTOR DE ABAJO


     }
     
       //System.out.println("Color 1:"+offColor.color1);
       //System.out.println("Color 2:"+offColor.color2);
       //System.out.println("Modo Color:"+offColor.modus);
       //System.out.println("P1 Color:"+offColor.p1);
       //System.out.println("P2 Color:"+offColor.p2);
   }
   
  public void init()
  {
    initPins(0,0,0,0);
    initPinVisibility(false,false,false,false);
    setSize(120,120);
    element.jSetInnerBorderVisibility(false);

    element.jSetResizeSynchron(false);
    element.jSetResizable(true);
    //svgManager.loadFromFile(element.jGetSourcePath()+"led.svg");    
    setName("Left_Valve_JV");
    
    
    onColor.color1= new Color(0,0,102);
    //onColor.color1= Color.white;//new Color(255,242,181);
    //onColor.color2=new Color(253,153,0);
    //onColor.modus=1;
    //onColor.p1=new Point(0, 10);
    //onColor.p2=new Point(0, 25);
    
    offColor.color1=Color.WHITE;
    offColor.color2=new Color(102,102,102);
    offColor.modus=1;
    offColor.p1=new Point(0, 25);
    offColor.p2=new Point(45, 25);
    offColor.wiederholung=true;
    
    StrokeColor.color1=Color.LIGHT_GRAY;
    overlayColor.color1=new Color(153,204,255);
    
    ValveOnColor.color1=new Color(153,0,0);
    ValveOffColor.color1=new Color(102,0,0);
    
    on = InitState.getValue();
    overlay=false;
    
  }
  
  public void start(){
      super.start();
      if(element!=null){
      overlay=false;
      on = InitState.getValue();
      element.jRepaint();
      }
  }
  
  
  
  public void setPropertyEditor()
  {
    
    element.jAddPEItem("Init State",InitState, 0,0);
    element.jAddPEItem("Internal ON Farbe",onColor, 0,0);
    element.jAddPEItem("Internal OFF Farbe",offColor, 0,0);
    
    element.jAddPEItem("ValveSize Percent (1-50)",valveSize, 1,50);
    element.jAddPEItem("Round Valve",roundValve, 0,0);
    element.jAddPEItem("Diaphragm",Diaphragm, 0,0);
    element.jAddPEItem("Manual Valve",manualValve, 0,0);
    element.jAddPEItem("Control Valve",controlValve, 0,0);
    element.jAddPEItem("Circle Size Percent (1-50%)",circleSizePerc, 1,50);
    element.jAddPEItem("Valve ON Farbe",ValveOnColor, 0,0);
    element.jAddPEItem("Valve OFF Farbe",ValveOffColor, 0,0);
    
    element.jAddPEItem("Stroke Value",stroke, 0,100);
    element.jAddPEItem("Stroke Farbe",StrokeColor, 0,0);
    element.jAddPEItem("Border Visible",borderVisible, 0,0);
    element.jAddPEItem("Border Farbe",borderColor, 0,0);
    element.jAddPEItem("Fill All Pipe",fillAllPipe, 0,0);
    element.jAddPEItem("Union Size",cnxSize, 0,100);
    
    element.jAddPEItem("Overlay Color",overlayColor, 0,0);
  
    
    localize();
   
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Init State");
    element.jSetPEItemLocale(d+1,language,"Internal ON Color");
    element.jSetPEItemLocale(d+2,language,"Internal OFF Color");
    
    element.jSetPEItemLocale(d+3,language,"ValveSize Percent (1-50%)");
    
    element.jSetPEItemLocale(d+4,language,"Round Valve");
    element.jSetPEItemLocale(d+5,language,"Diaphragm");
    
    element.jSetPEItemLocale(d+6,language,"Manual Valve");
    element.jSetPEItemLocale(d+7,language,"Control Valve");
    element.jSetPEItemLocale(d+8,language,"Circle Size Percent (1-50%)");
    element.jSetPEItemLocale(d+9,language,"Valve ON Color");
    element.jSetPEItemLocale(d+10,language,"Valve OFF Color");
    
    element.jSetPEItemLocale(d+11,language,"Line Stroke Value");
    element.jSetPEItemLocale(d+12,language,"Stroke Color");
    element.jSetPEItemLocale(d+13,language,"Border Visible");
    element.jSetPEItemLocale(d+14,language,"Border Color");
    element.jSetPEItemLocale(d+15,language,"Fill All Pipe");
    element.jSetPEItemLocale(d+16,language,"Union Size");
    
    element.jSetPEItemLocale(d+17,language,"Overlay Color");
    

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Estado Inicial");
    element.jSetPEItemLocale(d+1,language,"Color Encendido Interno");
    element.jSetPEItemLocale(d+2,language,"Color Apagado Interno");
    
    element.jSetPEItemLocale(d+3,language,"Tamaño Valvula (1-50%)");
    
    element.jSetPEItemLocale(d+4,language,"Redondear Valvula");
    element.jSetPEItemLocale(d+5,language,"Diafragma");
    
    element.jSetPEItemLocale(d+6,language,"Valvula Manual");
    element.jSetPEItemLocale(d+7,language,"Valvula de Control");
    element.jSetPEItemLocale(d+8,language,"Porcentaje del Circulo (1-50%)");
    element.jSetPEItemLocale(d+9,language,"Color Valvula Activado");
    element.jSetPEItemLocale(d+10,language,"Color Valvula Desactivado");
    
    element.jSetPEItemLocale(d+11,language,"Espesor de Linea ");
    element.jSetPEItemLocale(d+12,language,"Color de Linea");
    element.jSetPEItemLocale(d+13,language,"Borde Visible");
    element.jSetPEItemLocale(d+14,language,"Color del Borde");
    element.jSetPEItemLocale(d+15,language,"Rellenar Todo");
    element.jSetPEItemLocale(d+16,language,"Ancho de la Union");
    
    element.jSetPEItemLocale(d+17,language,"Color de resaltador");
  }

  public void propertyChanged(Object o)
  { 
    on=InitState.getValue();
    element.jRepaint();
  }
  public void loadFromStream(java.io.FileInputStream fis)
  {
      //onColor.loadFromStream(fis);
   
  InitState.loadFromStream(fis);
  onColor.loadFromStream(fis);
  offColor.loadFromStream(fis);
  stroke.loadFromStream(fis);
  StrokeColor.loadFromStream(fis);
  borderVisible.loadFromStream(fis);
  borderColor.loadFromStream(fis);
  fillAllPipe.loadFromStream(fis);
  cnxSize.loadFromStream(fis);
  overlayColor.loadFromStream(fis);
  
  valveSize.loadFromStream(fis);
  Diaphragm.loadFromStream(fis);
  manualValve.loadFromStream(fis);
  roundValve.loadFromStream(fis);
  controlValve.loadFromStream(fis);
  ValveOnColor.loadFromStream(fis);
  ValveOffColor.loadFromStream(fis);
  circleSizePerc.loadFromStream(fis);

  
  if(InitState!=null){
  on=InitState.getValue();
  }else{
   on=false;    
  }
  element.jRepaint();

  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      //onColor.saveToStream(fos);
  InitState.saveToStream(fos);
  onColor.saveToStream(fos);
  offColor.saveToStream(fos);
  stroke.saveToStream(fos);
  StrokeColor.saveToStream(fos);
  borderVisible.saveToStream(fos);
  borderColor.saveToStream(fos);
  fillAllPipe.saveToStream(fos);
  cnxSize.saveToStream(fos);
  
  overlayColor.saveToStream(fos);
  
  
  valveSize.saveToStream(fos);
  Diaphragm.saveToStream(fos);
  manualValve.saveToStream(fos);
  roundValve.saveToStream(fos);
  controlValve.saveToStream(fos);
  ValveOnColor.saveToStream(fos);
  ValveOffColor.saveToStream(fos);
  circleSizePerc.saveToStream(fos);
  }


}
 
