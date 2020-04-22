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
import java.awt.*;
import tools.*;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class AnzeigePanel extends JVSMain implements PanelIF
{
  private int width=180, height=180;
  private double value=0.0;
 
  final private JSlider slider = new JSlider(JSlider.VERTICAL);
  private Boolean overlay = new Boolean(false);
  private VSInteger arcValue = new VSInteger(10);
  
  
  private VSDouble InitialValue=new VSDouble(0.0);
  private VSColorAdvanced tankExternalColor = new VSColorAdvanced();
  private VSColorAdvanced tankInternalColor = new VSColorAdvanced();
  private VSColorAdvanced UpperFillColor = new VSColorAdvanced();
  private VSColor fillColor2 = new VSColor(new Color(153,204,255));
  private VSColor fillColor1 = new VSColor(new Color(0,0,102)); 
  private VSInteger strokeValue = new VSInteger(5);
  private VSColorAdvanced strokeColor = new VSColorAdvanced();
  private VSColorAdvanced overlayColor = new VSColorAdvanced();
  private VSBoolean borderVisible = new VSBoolean(true);
  private VSColor borderColor = new VSColor(Color.BLACK);
  private VSBoolean StagesVisible = new VSBoolean(true);
  private VSInteger majorStages = new VSInteger(20);
  private VSFont fnt = new VSFont(new Font("Dialog",Font.PLAIN,10));
  private VSColor fontColor = new VSColor(new Color(0,0,0));
  
  private boolean started=false;
  
  
  
  public void processPanel(int pinIndex, double value, Object obj)
  {
    if(pinIndex==0){
    this.value=value;  
    started=true;
    }
    if(pinIndex==1){
     if(value==1.0) overlay=true; else overlay=false;    
    }
    //element.jConsolePrintln("Notificado"+pinIndex+"_"+value);
    //element.jRepaint();
  }

   public void paint(java.awt.Graphics g)
   {
    try{   
    if (element!=null)
    {  
       slider.setVisible(StagesVisible.getValue());
       slider.setMajorTickSpacing(majorStages.getValue());
       slider.setFont(fnt.getValue());
       slider.setForeground(fontColor.getValue());
    
       Rectangle bounds=element.jGetBounds();
       int ArcWidth=0;
       ArcWidth=(bounds.width-(8*strokeValue.getValue()));    
       
       int ArcHeight=(bounds.height-(2*strokeValue.getValue()));
       int tWidth= 0;
       int tHeight= 0;
       int X0=0;
       int Y0=0;
       
       
       
       Graphics2D g2=(Graphics2D)g;
       g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
       g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
       g2.setFont(fnt.getValue());

       FontMetrics fm = g2.getFontMetrics();
       Rectangle2D FontB = fm.getStringBounds("", g2);
       
       
       //------------------------------------------------------------------------------FONDO BASE DEL TANQUE
       tWidth= (int) (ArcWidth *0.8);
       tHeight= (int) (ArcHeight*0.75);
       X0=(ArcWidth  -  ((int) (ArcWidth *1))) +(strokeValue.getValue());
       Y0=(ArcHeight -  ((int) (ArcHeight *0.86)))+(strokeValue.getValue());
       
       tankExternalColor.setFillColor(g2);
       g2.fillRoundRect(X0, Y0,tWidth ,tHeight, arcValue.getValue(), arcValue.getValue());
       
       g2.setStroke(new BasicStroke(strokeValue.getValue()));
       if(overlay) overlayColor.setFillColor(g2); else strokeColor.setFillColor(g2);
       g2.drawRoundRect(X0, Y0,tWidth ,tHeight, arcValue.getValue(), arcValue.getValue());
       
       if(borderVisible.getValue()){
       g2.setStroke(new BasicStroke(1));
       g2.setColor(borderColor.getValue());
       g2.drawRoundRect(X0, Y0,tWidth ,tHeight, arcValue.getValue(), arcValue.getValue());
       }
       
       
       //------------------------------------------------------------------------------ARCO SUPERIOR EXTERNO
       tWidth= (int) (ArcWidth *0.8);
       tHeight= (int) (ArcHeight*0.25);
       X0=(ArcWidth  -  ((int) (ArcWidth *1))) +(strokeValue.getValue());
       Y0=(ArcHeight -  ((int) (ArcHeight *0.98)))+(strokeValue.getValue());
       
       tankExternalColor.setFillColor(g2);
       g2.fillArc(X0, Y0,tWidth ,tHeight, 0, 180);
       
       g2.setStroke(new BasicStroke(strokeValue.getValue()));
       if(overlay) overlayColor.setFillColor(g2); else strokeColor.setFillColor(g2);
       g2.drawArc(X0, Y0,tWidth ,tHeight, 0, 180);
       
       if(borderVisible.getValue()){
       g2.setStroke(new BasicStroke(1));
       g2.setColor(borderColor.getValue());
       g2.drawArc(X0, Y0,tWidth ,tHeight, 0,180);
       }
       
       
       
       
       //----------------------------------------------------------------------------FONDO INTERIOR DEL TANQUE
       tWidth= (int) (ArcWidth *0.6);
       tHeight= (int) (ArcHeight*0.67);
       X0=(ArcWidth  -  ((int) (ArcWidth *0.9))) +(strokeValue.getValue());
       Y0=(ArcHeight -  ((int) (ArcHeight *0.85)))+(strokeValue.getValue());
       
       tankInternalColor.setFillColor(g2);
       g2.fillRoundRect(X0, Y0,tWidth ,tHeight, arcValue.getValue(), arcValue.getValue());
       
       g2.setStroke(new BasicStroke(strokeValue.getValue()));
       if(overlay) overlayColor.setFillColor(g2); else strokeColor.setFillColor(g2);
       g2.drawRoundRect(X0, (Y0-2),tWidth ,tHeight, arcValue.getValue(), arcValue.getValue());
       
       if(borderVisible.getValue()){
       g2.setStroke(new BasicStroke(1));
       g2.setColor(borderColor.getValue());
       g2.drawRoundRect(X0, Y0,tWidth ,tHeight, arcValue.getValue(), arcValue.getValue());
       }
       
       //---------------------------------------------------------------------------------ARCO INFERIOR EXTERNO 
       tWidth= (int) (ArcWidth *0.8);
       tHeight= (int) (ArcHeight*0.25);
       X0=(ArcWidth  -  ((int) (ArcWidth *1))) +(strokeValue.getValue());
       Y0=(ArcHeight -  ((int) (ArcHeight *0.25)))+(strokeValue.getValue());
       
       tankExternalColor.setFillColor(g2);
       g2.fillArc(X0, Y0,tWidth ,tHeight, 180, 180);
       
       g2.setStroke(new BasicStroke(strokeValue.getValue()));
       if(overlay) overlayColor.setFillColor(g2); else strokeColor.setFillColor(g2);
       g2.drawArc(X0, Y0,tWidth ,tHeight, 180, 180);
       
       if(borderVisible.getValue()){
       g2.setStroke(new BasicStroke(1));
       g2.setColor(borderColor.getValue());
       g2.drawArc(X0, Y0,tWidth ,tHeight, 180,180);
       }
       
       
       //--------------------------------------------------------------------------ARCO INTERIOR INTERNO INFERIOR
       tWidth= (int) (ArcWidth *0.6);
       tHeight= (int) (ArcHeight*0.2);
       X0=(ArcWidth  -  ((int) (ArcWidth *0.9))) +(strokeValue.getValue());
       Y0=(ArcHeight -  ((int) (ArcHeight *0.3)))+(strokeValue.getValue());
       
       tankInternalColor.setFillColor(g2);
       g2.fillArc(X0, Y0,tWidth ,tHeight, 180, 180);
       
       g2.setStroke(new BasicStroke(strokeValue.getValue()));
       if(overlay) overlayColor.setFillColor(g2); else strokeColor.setFillColor(g2);
       g2.drawArc(X0, Y0,tWidth ,tHeight, 180, 360);
       
       if(borderVisible.getValue()){
       g2.setStroke(new BasicStroke(1));
       g2.setColor(borderColor.getValue());
       g2.drawArc(X0, Y0,tWidth ,tHeight, 0,360);
       }
       
       
       //--------------------------------------------------------------------------ARCO INTERIOR INTERNO SUPERIOR
       tWidth= (int) (ArcWidth *0.6);
       tHeight= (int) (ArcHeight*0.2);
       X0=(ArcWidth  -  ((int) (ArcWidth *0.9))) +(strokeValue.getValue());
       Y0=(ArcHeight -  ((int) (ArcHeight *0.94)))+(strokeValue.getValue());
       
       tankInternalColor.setFillColor(g2);
       g2.fillArc(X0, Y0,tWidth ,tHeight, 0, 180);
       
       g2.setStroke(new BasicStroke(strokeValue.getValue()));
       if(overlay) overlayColor.setFillColor(g2); else strokeColor.setFillColor(g2);
       g2.drawArc(X0, Y0,tWidth ,tHeight, 0, 360);
       
       if(borderVisible.getValue()){
       g2.setStroke(new BasicStroke(1));
       g2.setColor(borderColor.getValue());
       g2.drawArc(X0, Y0,tWidth ,tHeight, 0,360);
       }
       
       
       
       
       //----------------------------------------------------------ARCO INTERIOR INTERNO MOVIL
       tWidth= (int) (ArcWidth *0.57);
       tHeight= (int) (ArcHeight*0.2);
       X0=(ArcWidth  -  ((int) (ArcWidth *0.89))) +(strokeValue.getValue());
       //Y0=(ArcHeight -  ((int) (ArcHeight *0.31)))+(strokeValue.getValue());
       int YMin=(ArcHeight -  ((int) (ArcHeight *0.31)))+(strokeValue.getValue()); //Minimo
       int YMax=(ArcHeight -  ((int) (ArcHeight *0.94)))+(strokeValue.getValue()); //Maximo
       double m = (YMin-YMax)/(0.0 - 100.0);
       double b = YMin;
       double y = (m * value) + b;
       Y0=(int)(y);
       //System.err.println("Valor EC: value="+value+"_m:"+m+"_b:"+b+"_Result:"+y +"_Ymin="+YMin+"_YMax"+YMax);
       if(Y0<YMax) Y0=YMax;
       if(Y0>YMin) Y0=YMin;
       int X0F1=(ArcWidth  -  ((int) (ArcWidth *0.85)));
       int Y0F1=(ArcHeight -  ((int) (ArcHeight *0.85)))+(strokeValue.getValue());
       int MaxHeigth=(YMin-YMax)+(tHeight/2);
       //YMin=206  YMax=23
       
       if(element.jGetWidth()<=250.0){
       slider.setMinimumSize(new Dimension(50, 110));
       slider.setSize((60+(int) (ArcWidth*0.4)),MaxHeigth);
       slider.setLocation((int) (ArcWidth*0.5), Y0F1);
       slider.setFont(new Font(fnt.getValue().getFontName(),fnt.getValue().getStyle(),(fnt.getValue().getSize())));
       }
       if(element.jGetWidth()>250.0){
       slider.setMinimumSize(new Dimension(50, 110));
       slider.setSize((60+(int) (ArcWidth*0.35)),MaxHeigth);
       slider.setLocation((int) (ArcWidth*0.54), Y0F1);   
       slider.setFont(new Font(fnt.getValue().getFontName(),fnt.getValue().getStyle(),(fnt.getValue().getSize()+2)));
       }
       if(element.jGetWidth()>300.0){
       slider.setMinimumSize(new Dimension(50, 110));
       slider.setSize((60+(int) (ArcWidth*0.37)),MaxHeigth);
       slider.setLocation((int) (ArcWidth*0.56), Y0F1);  
       
       slider.setFont(new Font(fnt.getValue().getFontName(),fnt.getValue().getStyle(),(fnt.getValue().getSize()+4)));
       }
       
       
        slider.setValue((int)value);
       
       if(value>=0){
           GradientPaint gp = new GradientPaint(0.0f, 50.0f, fillColor1.getValue(),(float)bounds.width, 50.0f, fillColor2.getValue());
           g2.setPaint(gp);
           g2.fillArc((X0+strokeValue.getValue()/2), YMin,tWidth ,tHeight, 180, 180);
           g2.fillRoundRect((X0+strokeValue.getValue()/2),Y0+(tHeight/2),tWidth ,(YMin-Y0)+strokeValue.getValue(), arcValue.getValue(), arcValue.getValue());
       }
       if(value==0) overlayColor.setFillColor(g2); else UpperFillColor.setFillColor(g2);
       
       g2.fillArc((X0+strokeValue.getValue()/4), Y0,tWidth ,tHeight, 180, 360);
       
       g2.setStroke(new BasicStroke(strokeValue.getValue()));
       if(overlay) overlayColor.setFillColor(g2); else strokeColor.setFillColor(g2);
       g2.drawArc((X0+strokeValue.getValue()/4), Y0,tWidth ,tHeight, 180, 360);
       
       if(borderVisible.getValue()){
       g2.setStroke(new BasicStroke(1));
       g2.setColor(borderColor.getValue());
       g2.drawArc((X0+strokeValue.getValue()/4), Y0,tWidth ,tHeight, 0,360);
       }
       
       
       
       //-
       
       
    }
    }catch(Exception e){
        System.out.println("Error:"+e);   
    }
   }
public void start(){
    super.start();
    if(started==false){
        this.value=InitialValue.getValue();
        element.jRepaint();
    }
    
}

public void stop(){
    super.stop();
    started=false;
}

  public void init()
  {
    initPins(0,0,0,0);
    started=false;
    setSize(width,height);    
    
    
    initPinVisibility(false,false,false,false);
    element.jSetInnerBorderVisibility(false);
    
    element.jSetResizable(true);
    
    
    setName("Tank_JV");
    
    this.value=InitialValue.getValue();
    
  
    element.jSetMinimumSize(110,110);
    
    strokeColor.color1=Color.LIGHT_GRAY;
    overlayColor.color1=new Color(153,204,255);
    
    tankExternalColor.color1= new Color(102,102,102);
    tankExternalColor.color2=Color.WHITE;
    tankExternalColor.p1=new Point(40,0);
    tankExternalColor.p2=new Point(130,0);
    tankExternalColor.wiederholung=true;
    tankExternalColor.modus=1;
    
    
    tankInternalColor.color1= new Color(102,102,102);
    tankInternalColor.color2=Color.WHITE;
    tankInternalColor.p1=new Point(65,0);
    tankInternalColor.p2=new Point(140,0);
    tankInternalColor.wiederholung=true;
    tankInternalColor.modus=1;
    
    UpperFillColor.color1=new Color(0,0,102);
    //slider.setEnabled(false);
    slider.setFocusable(false);
    slider.setRequestFocusEnabled(false);
    
  }
  
  public void xOnInit()
  {
    
    JPanel panel =element.getFrontPanel();
    panel.setLayout(new java.awt.BorderLayout());
    slider.setMinorTickSpacing(5);
    slider.setMajorTickSpacing(majorStages.getValue());
    slider.setMaximum(100);
    slider.setMinimum(0);
    slider.setOpaque(false);
    slider.setForeground(Color.BLACK);
    slider.setFont(fnt.getValue());
    slider.setForeground(fontColor.getValue());
    slider.setExtent(0);
    slider.setPaintLabels(StagesVisible.getValue());
    slider.setPaintTicks(StagesVisible.getValue());
    slider.setPaintTrack(false);
    slider.setVisible(StagesVisible.getValue());
    slider.setFocusable(false);
    panel.add(slider, java.awt.BorderLayout.EAST);
    element.setAlwaysOnTop(true); 

  }
  
  public void setPropertyEditor()
  {
    
    element.jAddPEItem("Init Value",InitialValue, 0,100);
    element.jAddPEItem("Tank Farbe External",tankExternalColor, 0,0);
    element.jAddPEItem("Tank Farbe Internal",tankInternalColor, 0,0);
    element.jAddPEItem("Top Fill Farbe",UpperFillColor, 0,0);
    element.jAddPEItem("Gradient Farbe A",fillColor1, 0,0);
    element.jAddPEItem("Gradient Farbe B",fillColor2, 0,0);
    element.jAddPEItem("Stroke Value",strokeValue, 0,10);
    element.jAddPEItem("Stroke Farbe",strokeColor, 0,0);
    element.jAddPEItem("Overlay Farbe",overlayColor, 0,0);
    element.jAddPEItem("Edge Visible",borderVisible, 0,0);
    element.jAddPEItem("Edge Color",borderColor, 0,0);
    element.jAddPEItem("Stages Visible",StagesVisible, 0,0);
    element.jAddPEItem("Major Tickspacing",majorStages, 5,100.0);
    element.jAddPEItem("Font",fnt,0,0);
    element.jAddPEItem("Font Farbe",fontColor , 0,0);
  
    
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";
    int i =0;
    element.jSetPEItemLocale(d+ i++,language,"Init Value");
    element.jSetPEItemLocale(d+ i++,language,"External Tank Color");
    element.jSetPEItemLocale(d+ i++,language,"Internal Tank Color");
    element.jSetPEItemLocale(d+ i++,language,"Top Fill Color");
    element.jSetPEItemLocale(d+ i++,language,"Fill 1 Tank Color");
    element.jSetPEItemLocale(d+ i++,language,"Fill 2 Tank Color");
    element.jSetPEItemLocale(d+ i++,language,"Stroke Value");
    element.jSetPEItemLocale(d+ i++,language,"Stroke Color");
    element.jSetPEItemLocale(d+ i++,language,"Overlay Color");
    element.jSetPEItemLocale(d+ i++,language,"Edge Visible");
    element.jSetPEItemLocale(d+ i++,language,"Edge Color");
    element.jSetPEItemLocale(d+ i++,language,"Stages Visible");
    element.jSetPEItemLocale(d+ i++,language,"Major Tickspacing");
    element.jSetPEItemLocale(d+ i++,language,"Font");
    element.jSetPEItemLocale(d+ i++,language,"Font Color");
   
    language="es_ES";

    i =0;
    element.jSetPEItemLocale(d+ i++,language,"Valor Inicial");
    element.jSetPEItemLocale(d+ i++,language,"Color Externo");
    element.jSetPEItemLocale(d+ i++,language,"Color Interno");
    element.jSetPEItemLocale(d+ i++,language,"Color Superior");
    element.jSetPEItemLocale(d+ i++,language,"Color 1 Llenado");
    element.jSetPEItemLocale(d+ i++,language,"Color 2 Llenado");
    element.jSetPEItemLocale(d+ i++,language,"Valor Espesor");
    element.jSetPEItemLocale(d+ i++,language,"Color Espesor");
    element.jSetPEItemLocale(d+ i++,language,"Color de resaltado");
    element.jSetPEItemLocale(d+ i++,language,"Borde Visible");
    element.jSetPEItemLocale(d+ i++,language,"Color del Borde");
    element.jSetPEItemLocale(d+ i++,language,"Divisiones Visibles");
    element.jSetPEItemLocale(d+ i++,language,"Espaciado Divisiones");
    element.jSetPEItemLocale(d+ i++,language,"Fuente");
    element.jSetPEItemLocale(d+ i++,language,"Color de Fuente");
  }
  
  
  public void propertyChanged(Object o)
  { 
    this.value=InitialValue.getValue();  
    element.jRepaint();
  }



  public void loadFromStream(java.io.FileInputStream fis)
  {
    InitialValue.loadFromStream(fis);
    tankExternalColor.loadFromStream(fis);
    tankInternalColor.loadFromStream(fis);
    UpperFillColor.loadFromStream(fis);
    fillColor1.loadFromStream(fis);
    fillColor2.loadFromStream(fis);
    strokeValue.loadFromStream(fis);
    strokeColor.loadFromStream(fis);
    overlayColor.loadFromStream(fis);
    borderVisible.loadFromStream(fis);
    borderColor.loadFromStream(fis);
    StagesVisible.loadFromStream(fis);
    majorStages.loadFromStream(fis);
    fnt.loadFromStream(fis);
    fontColor.loadFromStream(fis);
    if(InitialValue.getValue()>100 || InitialValue.getValue()<0) InitialValue.setValue(0);
    value=InitialValue.getValue();
    element.jRepaint();
 
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
  InitialValue.saveToStream(fos);
  tankExternalColor.saveToStream(fos);
  tankInternalColor.saveToStream(fos);
  UpperFillColor.saveToStream(fos);
  fillColor1.saveToStream(fos);
  fillColor2.saveToStream(fos);
  strokeValue.saveToStream(fos);
  strokeColor.saveToStream(fos);
  overlayColor.saveToStream(fos);
  borderVisible.saveToStream(fos);
  borderColor.saveToStream(fos);
  StagesVisible.saveToStream(fos);
  majorStages.saveToStream(fos);
  fnt.saveToStream(fos);
  fontColor.saveToStream(fos);
      
     
  }
  
}




