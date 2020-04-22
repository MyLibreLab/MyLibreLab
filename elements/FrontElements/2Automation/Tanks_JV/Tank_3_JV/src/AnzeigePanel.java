//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia (cswi@gmx.de)                         *
//* Copyright (C) 2017  Javier Velásquez (javiervelasquez125@gmail.com)                                                                          *
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
import java.awt.event.*;
import java.text.*;
import tools.*;
import java.awt.geom.Rectangle2D;

public class AnzeigePanel extends JVSMain implements PanelIF
{
  private int width=180, height=180;
  private double value=0.0;
 
  private VSString formatierung = new VSString("#0");
  private DecimalFormat df = new DecimalFormat(formatierung.getValue());
  
  private VSInteger arcValue= new VSInteger(10);
  
  
  
  
  
  private VSDouble InitialValue = new VSDouble(0.0);
  private VSBoolean TextVisible = new VSBoolean(true);
  private VSFont textFont = new VSFont(new Font("Dialog",Font.BOLD,18));
  private VSComboBox textAlign = new VSComboBox();
  private VSColor textColor = new VSColor(new Color(0,0,51));
  private VSBoolean UnitsVisible = new VSBoolean(true);
  private VSString UnitsStr = new VSString("%");
  private VSBoolean displayEdgeVisible = new VSBoolean(true);
  private VSColor displayEdgeColor = new VSColor(new Color(253,153,0));
  private VSBoolean displayFill = new VSBoolean(true);
  private VSColor displayFillColor = new VSColor(new Color(255,242,181));  
  private VSBoolean tankfillExpand = new VSBoolean(false);
  private VSColor UpperFillColor = new VSColor(new Color(0,0,102));
  private VSColor fillColor1 = new VSColor(new Color(0,0,102));
  private VSColor fillColor2 = new VSColor(new Color(153,204,255));
  private VSInteger strokeValue = new VSInteger(5);
  private VSColorAdvanced strokeColor = new VSColorAdvanced();
  private VSColorAdvanced overlayColor = new VSColorAdvanced();
  private VSBoolean tankEdgeVisible = new VSBoolean(true);
  private VSColor tankEdgeColor = new VSColor(Color.BLACK);
  private VSColorAdvanced tankExternalColor = new VSColorAdvanced();
  private VSColorAdvanced tankInternalColor = new VSColorAdvanced();
  private VSColorAdvanced tankBottomColor = new VSColorAdvanced();
  
  private VSBoolean defaultColorSettings = new VSBoolean(true);
  private VSInteger AlignNumber= new VSInteger(0);
  
  
  
  
  private boolean overlay=false;
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
    
  }

  
  
  
   public void paint(java.awt.Graphics g)
   {
    if (element!=null)
    {
       int X0=0;
       int Y0=0;
       int tWidth=0;
       int tHeight=0;
       
       if(defaultColorSettings.getValue()){
       tankInternalColor.p2=new Point((element.jGetWidth()/2),0);
       tankBottomColor.p2=new Point((element.jGetWidth()/2),0);
       tankExternalColor.p2=new Point((element.jGetWidth()/2),0);    
       }
       
       Rectangle bounds=new Rectangle(
               (int) (element.jGetWidth()*0.3), (int) (element.jGetHeight()*0.5),
               (int) (element.jGetWidth()*0.4), (int) (element.jGetHeight()*0.2));
       
       String StrNumOut ="0";
       if(UnitsVisible.getValue()){
          StrNumOut = df.format(value)+" "+UnitsStr.getValue();
       }else{
           StrNumOut = df.format(value);
       }
              
       Graphics2D g2=(Graphics2D)g;
       g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
       
       
       
       //----------------------------------------------------------------TANK BACKGROUND
       
       tWidth=(int)(element.jGetWidth()*0.75);
       tHeight=(int)((element.jGetHeight()*0.8)+(strokeValue.getValue()/2));
       X0=(int)(element.jGetWidth()*0.5) - (tWidth/2);
       Y0=(int)(((element.jGetHeight()*0.15))+strokeValue.getValue());
       
       tankExternalColor.setFillColor(g2);
      
       g2.fillRoundRect(X0, Y0,tWidth ,tHeight, arcValue.getValue(), arcValue.getValue());
       
       g2.setStroke(new BasicStroke(strokeValue.getValue()));
       if(overlay) overlayColor.setFillColor(g2); else strokeColor.setFillColor(g2);
       g2.drawRoundRect(X0, Y0,tWidth ,tHeight, arcValue.getValue(), arcValue.getValue());
       
       if(tankEdgeVisible.getValue()){
       g2.setStroke(new BasicStroke(1));
       g2.setColor(tankEdgeColor.getValue());
       g2.drawRoundRect(X0, Y0,tWidth ,tHeight, arcValue.getValue(), arcValue.getValue());
       }
       
       //----------------------------------------------------------------TANK INVERTED TRIANGLE
       tWidth=(int)(element.jGetWidth()*0.75);
       tHeight=(int)(element.jGetHeight()*0.30);
       X0=(int)(element.jGetWidth()*0.5) - (tWidth/2);
       Y0=(strokeValue.getValue()/2);
       int X0F1=(int)(element.jGetWidth()*0.5) - (tWidth/2); // x0 fondo
       int Y0F1=(int)(((element.jGetHeight()*0.15))+strokeValue.getValue()); //y0 fondo
       
       Polygon p = new Polygon();
       Point p1=new Point(X0,Y0F1);
       Point p2=new Point((X0+tWidth),Y0F1);
       Point p3=new Point((X0+(tWidth/2)),Y0);
       
       p.addPoint(p1.x,p1.y);
       p.addPoint(p2.x,p2.y);
       p.addPoint(p3.x,p3.y);
       
       tankBottomColor.setFillColor(g2);
       g2.fillPolygon(p);
       
       int xOffset = (strokeValue.getValue()/2);
       int yOffset = (strokeValue.getValue()/2);
       g2.setStroke(new BasicStroke(strokeValue.getValue()));
       if(overlay) overlayColor.setFillColor(g2); else strokeColor.setFillColor(g2);
       //g2.setColor(Color.ORANGE);
       g2.drawLine(p1.x+xOffset, p1.y,p2.x-xOffset,p2.y); // LINE DIVISION
       g2.drawLine(p1.x+xOffset, p1.y,p3.x,p3.y-yOffset); // LINE /
       g2.drawLine(p2.x-xOffset, p2.y,p3.x,p3.y-yOffset); // LINE \
       
       
       if(tankEdgeVisible.getValue()){
       g2.setStroke(new BasicStroke(1));
       g2.setColor(tankEdgeColor.getValue());

       //g2.drawLine(p1.x+xOffset, p1.y,p2.x-xOffset,p2.y); // LINE DIVISION
       g2.drawLine(p1.x+xOffset, p1.y,p3.x,p3.y-yOffset); // LINE /
       g2.drawLine(p2.x-xOffset, p2.y,p3.x,p3.y-yOffset); // LINE \
       }

       //----------------------------------------------------------------FILL PANEL
       
       double wMin=0.06;
       double wMax=0.6;
       double x0min=0.22;
       double x0max=0.5;
       double wPercent=0.0;
       double x0Percent=0.0;
       if(tankfillExpand.getValue()==false){
       wPercent=wMin;
       x0Percent=x0min;
       }else{
       wPercent=wMax;
       x0Percent=x0max;    
       }
       
       tWidth=(int)(element.jGetWidth()*wPercent);
       tHeight=(int)(element.jGetHeight()*0.7);
       X0=(int)(element.jGetWidth()*x0Percent) - (tWidth/2);
       //Y0=(int)((element.jGetHeight()*0.01));
       Y0=(int) (element.jGetHeight()*0.25);
       int YMin=(Y0+tHeight)-strokeValue.getValue();
       int YMax=Y0;
       double m = (YMin-YMax)/(0.0 - 100.0);
       double b = YMin;
       double y = (m * value) + b;
       //System.err.println("Valor EC: value="+value+"_m:"+m+"_b:"+b+"_Result:"+y +"_Ymin="+YMin+"_YMax"+YMax);
       //Valor EC: value=0.0_m:-0.9_b:104.0_Result:104.0_Ymin=104_YMax14
       //Valor EC: value=100.0_m:-0.9_b:104.0_Result:14.0_Ymin=104_YMax14
       
       g2.setStroke(new BasicStroke(1));
       tankInternalColor.setFillColor(g2);
       g2.fillRoundRect(X0, Y0,tWidth ,tHeight, 5, 5); 
       
       if(value==0){
           overlayColor.setFillColor(g2);
           g2.fillRoundRect(X0,YMin,tWidth ,strokeValue.getValue(), arcValue.getValue(), arcValue.getValue());
       
       } else{
           GradientPaint gp = new GradientPaint(X0, X0+(tWidth/2), fillColor1.getValue(),(float)tWidth, X0+(tWidth/2), fillColor2.getValue());
           g2.setPaint(gp);
           //g2.setColor(fillColor1.getValue());
           g2.fillRoundRect(X0,(int) (y),tWidth ,(int) (YMin-y)+strokeValue.getValue(), 5, 5);
           g2.setColor(UpperFillColor.getValue());
           g2.drawLine(X0,(int) (y),X0+tWidth ,(int) (y));
           
       
       }
       
       //g2.setStroke(new BasicStroke(strokeValue.getValue()));
       //if(overlay) overlayColor.setFillColor(g2); else strokeColor.setFillColor(g2);
       //g2.drawRoundRect(X0, Y0,tWidth ,tHeight, arcValue.getValue(), arcValue.getValue());
       
       if(tankEdgeVisible.getValue()){
       g2.setStroke(new BasicStroke(1));
       g2.setColor(tankEdgeColor.getValue());
       g2.drawRoundRect(X0, Y0,tWidth ,tHeight, arcValue.getValue(), arcValue.getValue());
       }
       
       
       g2.setStroke(new BasicStroke(1));
       //----------------------------------------------------------------NUMERIC INDICATOR BACKGROUND
       g2.setColor(displayFillColor.getValue());
       if(displayFill.getValue()){
       g2.fillRoundRect(bounds.x,bounds.y,bounds.width-1,bounds.height-1,5,5);
       }
       //---------------------------------------------------------------------------------EDGE
       if(displayEdgeVisible.getValue()){
       g2.setColor(displayEdgeColor.getValue());
       g2.drawRoundRect(bounds.x,bounds.y,bounds.width-1,bounds.height-1,5,5);
       }       
       //--------------------------------------------------------------------------VALUE STRING
       if(TextVisible.getValue()){
           
            g2.setFont(textFont.getValue());
            FontMetrics fm = g2.getFontMetrics();
            Rectangle2D   r = fm.getStringBounds(StrNumOut,g2);
            int widthText = (int) r.getWidth();
            int heightText = (int) r.getHeight();
            X0 =bounds.x +((bounds.width)/2);
            Y0 =bounds.y + ((bounds.height)/2);
            g2.setColor(textColor.getValue());
            if(AlignNumber.getValue()==0){ // CENTER
            g2.drawString(StrNumOut,X0-(widthText/2),Y0+(heightText/4));
            }
            if(AlignNumber.getValue()==1){ //  LEFT
            g2.drawString(StrNumOut,bounds.x+5,Y0+(heightText/4));   
            }
            if(AlignNumber.getValue()==2){ //  RIGHT
            g2.drawString(StrNumOut,(bounds.x+bounds.width)-(widthText+5),Y0+(heightText/4));   
            }

        }
       
            
       
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
    setSize(width,height);
    initPinVisibility(false,false,false,false);
    element.jSetInnerBorderVisibility(false);
    
    element.jSetResizable(true);
    
    textAlign.addItem("CENTER");
    textAlign.addItem("LEFT");
    textAlign.addItem("RIGHT");
    textAlign.selectedIndex=0;
    
    setName("Tank Indicator J.V.");
    value=InitialValue.getValue();
    
    
    strokeColor.color1=Color.LIGHT_GRAY;
    overlayColor.color1=new Color(153,204,255);
    
    tankExternalColor.color1= new Color(102,102,102);
    tankExternalColor.color2=Color.WHITE;
    tankExternalColor.p1=new Point(20,0);
    tankExternalColor.p2=new Point((element.jGetWidth()/2),0);
    tankExternalColor.wiederholung=true;
    tankExternalColor.modus=1;
    
    
    tankInternalColor.color1= new Color(102,102,102);
    tankInternalColor.color2=Color.WHITE;
    tankInternalColor.p1=new Point(20,0);
    tankInternalColor.p2=new Point((element.jGetWidth()/2),0);
    tankInternalColor.wiederholung=true;
    tankInternalColor.modus=1;
    
    tankBottomColor.color1= new Color(102,102,102);
    tankBottomColor.color2=Color.WHITE;
    tankBottomColor.p1=new Point(20,0);
    tankBottomColor.wiederholung=true;
    tankBottomColor.modus=1;
    
    
    
  }

  public void setPropertyEditor()
  {
    
    element.jAddPEItem("Initial Value",InitialValue, 0.0,100.0);
    element.jAddPEItem("Text Visible",TextVisible, 0,0);
    element.jAddPEItem("Text Font",textFont, 0,0);
    element.jAddPEItem("Text Align",textAlign, 0,10);
    element.jAddPEItem("Text Color",textColor, 0,0);
    element.jAddPEItem("Units Str Visible",UnitsVisible, 0,0);
    element.jAddPEItem("Units Str",UnitsStr, 0,0);
    element.jAddPEItem("Display Edge Visible",displayEdgeVisible, 0,0);
    element.jAddPEItem("Display Edge Farbe",displayEdgeColor, 0,0);
    element.jAddPEItem("Fill Display",displayFill, 0,0);
    element.jAddPEItem("Fill Display Farbe",displayFillColor, 0,0);
    element.jAddPEItem("Tank Fill Expand",tankfillExpand, 0,0);
    element.jAddPEItem("Upper Fill Farbe",UpperFillColor, 0,0);
    element.jAddPEItem("Fill Farbe A",fillColor1, 0,0);
    element.jAddPEItem("Fill Farbe B",fillColor2, 0,0);
    element.jAddPEItem("Stroke Value",strokeValue, 0,15);
    element.jAddPEItem("Stroke Farbe",strokeColor, 0,0);
    element.jAddPEItem("Overlay Farbe",overlayColor, 0,0);
    element.jAddPEItem("Tank Border Visible",tankEdgeVisible, 0,0);
    element.jAddPEItem("Tank Border Farbe",tankEdgeColor, 0,0);
    element.jAddPEItem("Tank External Farbe",tankExternalColor, 0,0);
    element.jAddPEItem("Tank Internal Farbe",tankInternalColor, 0,0);
    element.jAddPEItem("Tank Bottom Farbe",tankBottomColor, 0,0);
    
    
    localize();
  }

  public void xOnInit()
  {
   //this.value=InitialValue.getValue();
  }

  private void localize()
  {
    int d=6;
    int i=1;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+i,language,"Initial Value");
    element.jSetPEItemLocale(d+ i++,language,"Text Visible");
    element.jSetPEItemLocale(d+ i++,language,"Text Font");
    element.jSetPEItemLocale(d+ i++,language,"Text Align");
    element.jSetPEItemLocale(d+ i++,language,"Text Color");
    element.jSetPEItemLocale(d+ i++,language,"Units String Visible");
    element.jSetPEItemLocale(d+ i++,language,"Units String");
    element.jSetPEItemLocale(d+ i++,language,"Display Edge Visible");
    element.jSetPEItemLocale(d+ i++,language,"Display Edge Color");
    element.jSetPEItemLocale(d+ i++,language,"Fill Display ");
    element.jSetPEItemLocale(d+ i++,language,"Fill Display Color");
    element.jSetPEItemLocale(d+ i++,language,"Tank Fill Expand");
    element.jSetPEItemLocale(d+ i++,language,"Upper Fill Color");
    element.jSetPEItemLocale(d+ i++,language,"Fill Color A");
    element.jSetPEItemLocale(d+ i++,language,"Fill Color B");
    element.jSetPEItemLocale(d+ i++,language,"Stroke Value");
    element.jSetPEItemLocale(d+ i++,language,"Stroke Color");
    element.jSetPEItemLocale(d+ i++,language,"Overlay Color");
    element.jSetPEItemLocale(d+ i++,language,"Tank Edge Visible");
    element.jSetPEItemLocale(d+ i++,language,"Tank Edge Color");
    element.jSetPEItemLocale(d+ i++,language,"Tank External Color");
    element.jSetPEItemLocale(d+ i++,language,"Tank Internal Color");
    element.jSetPEItemLocale(d+ i++,language,"Tank Bottom Color");
    
    i=1;
    
    language="es_ES";
    element.jSetPEItemLocale(d+i,language,"Valor Inicial");
    element.jSetPEItemLocale(d+ i++,language,"Texto Visible");
    element.jSetPEItemLocale(d+ i++,language,"Fuente del Texto");
    element.jSetPEItemLocale(d+ i++,language,"Alineacion Texto");
    element.jSetPEItemLocale(d+ i++,language,"Color del Texto");
    element.jSetPEItemLocale(d+ i++,language,"Unidades Visibles");
    element.jSetPEItemLocale(d+ i++,language,"Texto Unidades");
    element.jSetPEItemLocale(d+ i++,language,"Borde Visible");
    element.jSetPEItemLocale(d+ i++,language,"Color del Borde");
    element.jSetPEItemLocale(d+ i++,language,"Rellenar Display");
    element.jSetPEItemLocale(d+ i++,language,"Color Fondo Display");
    element.jSetPEItemLocale(d+ i++,language,"Expandir Relleno Indicador");
    element.jSetPEItemLocale(d+ i++,language,"Color Linea Superior");
    element.jSetPEItemLocale(d+ i++,language,"Color Indicador A");
    element.jSetPEItemLocale(d+ i++,language,"Color Indicador B");
    element.jSetPEItemLocale(d+ i++,language,"Espesor Lineas");
    element.jSetPEItemLocale(d+ i++,language,"Color del Espesor");
    element.jSetPEItemLocale(d+ i++,language,"Color de Resaltado");
    element.jSetPEItemLocale(d+ i++,language,"Borde del tanque Visible");
    element.jSetPEItemLocale(d+ i++,language,"Color Bordes");
    element.jSetPEItemLocale(d+ i++,language,"Color Externo Tanque");
    element.jSetPEItemLocale(d+ i++,language,"Color Interno Tanque");
    element.jSetPEItemLocale(d+ i++,language,"Color Inferior Tanque");
    
  }

  public void propertyChanged(Object o)
  {
    if(o.equals(textAlign)){
    AlignNumber.setValue(textAlign.selectedIndex);
    }
    if(o.equals(tankExternalColor) ||o.equals(tankBottomColor) || o.equals(tankInternalColor)){
    defaultColorSettings.setValue(false);
    }
    
    this.value=InitialValue.getValue();
   

    element.jRepaint();
  }


  public void loadFromStream(java.io.FileInputStream fis)
  {
    InitialValue.loadFromStream(fis);
    TextVisible.loadFromStream(fis);
    textFont.loadFromStream(fis);
    textAlign.loadFromStream(fis);
    textColor.loadFromStream(fis);
    UnitsVisible.loadFromStream(fis);
    UnitsStr.loadFromStream(fis);
    displayEdgeVisible.loadFromStream(fis);
    displayEdgeColor.loadFromStream(fis);
    displayFill.loadFromStream(fis);
    displayFillColor.loadFromStream(fis);
    tankfillExpand.loadFromStream(fis);
    UpperFillColor.loadFromStream(fis);
    fillColor1.loadFromStream(fis);
    fillColor2.loadFromStream(fis);
    strokeValue.loadFromStream(fis);
    strokeColor.loadFromStream(fis);
    overlayColor.loadFromStream(fis);
    tankEdgeVisible.loadFromStream(fis);
    tankEdgeColor.loadFromStream(fis);
    tankExternalColor.loadFromStream(fis);
    tankInternalColor.loadFromStream(fis);
    tankBottomColor.loadFromStream(fis);
  
  
    AlignNumber.loadFromStream(fis);
    defaultColorSettings.loadFromStream(fis);
    this.value=InitialValue.getValue();
    
      
    element.jRepaint();
      
      
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    InitialValue.saveToStream(fos);
    TextVisible.saveToStream(fos);
    textFont.saveToStream(fos);
    textAlign.saveToStream(fos);
    textColor.saveToStream(fos);
    UnitsVisible.saveToStream(fos);
    UnitsStr.saveToStream(fos);
    displayEdgeVisible.saveToStream(fos);
    displayEdgeColor.saveToStream(fos);
    displayFill.saveToStream(fos);
    displayFillColor.saveToStream(fos);
    tankfillExpand.saveToStream(fos);
    UpperFillColor.saveToStream(fos);
    fillColor1.saveToStream(fos);
    fillColor2.saveToStream(fos);
    strokeValue.saveToStream(fos);
    strokeColor.saveToStream(fos);
    overlayColor.saveToStream(fos);
    tankEdgeVisible.saveToStream(fos);
    tankEdgeColor.saveToStream(fos);
    tankExternalColor.saveToStream(fos);
    tankInternalColor.saveToStream(fos);
    tankBottomColor.saveToStream(fos);
    
    AlignNumber.saveToStream(fos);
    defaultColorSettings.saveToStream(fos);
  }


}

