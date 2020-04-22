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
import java.text.*;
import tools.*;
import java.awt.geom.Rectangle2D;

public class AnzeigePanel extends JVSMain implements PanelIF
{
  private int width=100, height=200;
  private double value=0.0;
 
  
  
  
  
  
  
  private VSDouble InitialValue = new VSDouble(0.0);
  
  private VSDouble MinValue = new VSDouble(0.0);
  private VSDouble MaxValue = new VSDouble(100.0);
  private VSString formatierung = new VSString("#0");
  
  private VSBoolean TextVisible = new VSBoolean(true);
  private VSFont textFont = new VSFont(new Font("Dialog",Font.BOLD,12));
  private VSComboBox textAlign = new VSComboBox();
  private VSColor textColor = new VSColor(new Color(0,0,51));
  private VSBoolean UnitsVisible = new VSBoolean(true);
  private VSString UnitsStr = new VSString("°C");
  private VSBoolean displayEdgeVisible = new VSBoolean(true);
  private VSColor displayEdgeColor = new VSColor(new Color(253,153,0));
  private VSBoolean displayFill = new VSBoolean(true);
  private VSColor displayFillColor = new VSColor(new Color(255,242,181));  
  private VSBoolean tankfillExpand = new VSBoolean(true);
  private VSColor UpperFillColor = new VSColor(new Color(255,255,0));
  private VSColor fillColor1 = new VSColor(new Color(255,0,0));
  private VSColor fillColor2 = new VSColor(new Color(102,0,0));
  private VSInteger strokeValue = new VSInteger(5);
  private VSColorAdvanced strokeColor = new VSColorAdvanced();
  private VSColor overlayColor = new VSColor(new Color(253,153,0));
  private VSBoolean tankEdgeVisible = new VSBoolean(true);
  private VSColor tankEdgeColor = new VSColor(Color.BLACK);
  private VSColorAdvanced tankExternalColor = new VSColorAdvanced();
  private VSColorAdvanced tankInternalColor = new VSColorAdvanced();
  private VSColorAdvanced tankBottomColor = new VSColorAdvanced();
  
  
  private VSBoolean OverlayTresholds = new VSBoolean(true);
  private VSDouble LowValue = new VSDouble(25.0);
  
  private VSColor LowColor = new VSColor(new Color(153,255,153)); //lIGTH BLUE
  private VSDouble MediumValue = new VSDouble(50.0);
  private VSColor MediumColor = new VSColor(new Color(255,255,153)); //YELLOW 
  private VSDouble HighValue = new VSDouble(75.0);
  private VSColor HighColor = new VSColor(new Color(255,153,153)); //RED
  
  private VSColor OverlayTemp = new VSColor(Color.LIGHT_GRAY);
  
  private VSBoolean defaultColorSettings = new VSBoolean(true);
  private VSInteger AlignNumber= new VSInteger(0);
  private DecimalFormat df = new DecimalFormat(formatierung.getValue());
  private VSInteger arcValue= new VSInteger(5);
  private boolean overlay=false;



  public void processPanel(int pinIndex, double value, Object obj)
  {
    if(pinIndex==0){
    this.value=value;    
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
       
       if(OverlayTresholds.getValue()){
           if(value>MediumValue.getValue()) OverlayTemp.setValue(HighColor.getValue());
           if(value<= LowValue.getValue()) OverlayTemp.setValue(LowColor.getValue());
           if(value> LowValue.getValue() && value<= MediumValue.getValue()) OverlayTemp.setValue(MediumColor.getValue());
           
       }else{
       OverlayTemp.setValue(overlayColor.getValue());    
       }
       
       if(defaultColorSettings.getValue()){
       tankInternalColor.p2=new Point((element.jGetWidth()/2),0);
       tankBottomColor.p2=new Point((element.jGetWidth()/2),0);
       tankExternalColor.p2=new Point((element.jGetWidth()/2),0);    
       }
       
       Rectangle bounds=new Rectangle(
               (int) (element.jGetWidth()*0.3), (int) (element.jGetHeight()*0.7),
               (int) (element.jGetWidth()*0.4), (int) (element.jGetHeight()*0.09));
       
       String StrNumOut ="0";
       if(UnitsVisible.getValue()){
          StrNumOut = df.format(value)+" "+UnitsStr.getValue();
       }else{
           StrNumOut = df.format(value);
       }
              
       Graphics2D g2=(Graphics2D)g;
       g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
       
       
       //--------------------------------------------------------BACKGROUND EXTERNAL CIRCLE
       tWidth=(int)(element.jGetWidth()*0.7);
       tHeight=(int)((element.jGetHeight()*0.35));
       X0=(int)(element.jGetWidth()*0.5) - (tWidth/2);
       Y0=(int)(element.jGetHeight()*0.75)  - (tHeight/2);
       
       tankExternalColor.setFillColor(g2);
       g2.fillOval(X0, Y0, tWidth, tHeight);
       
       g2.setStroke(new BasicStroke(strokeValue.getValue()));
       if(overlay) g2.setColor(OverlayTemp.getValue()); else strokeColor.setFillColor(g2);
       g2.drawOval(X0, Y0, tWidth, tHeight);
       
       if(tankEdgeVisible.getValue()){
       g2.setStroke(new BasicStroke(1));
       g2.setColor(tankEdgeColor.getValue());
       g2.drawOval(X0, Y0, tWidth, tHeight);
       }
       
       
       //----------------------------------------------------------------TANK BACKGROUND
       
       tWidth=(int)(element.jGetWidth()*0.4);
       tHeight=(int)((element.jGetHeight()*0.58)+(strokeValue.getValue()));
       X0=(int)(element.jGetWidth()*0.5) - (tWidth/2);
       Y0=(int)(strokeValue.getValue());
       
       tankExternalColor.setFillColor(g2);
      
       g2.fillRoundRect(X0, Y0,tWidth ,tHeight, arcValue.getValue(), arcValue.getValue());
       
       g2.setStroke(new BasicStroke(strokeValue.getValue()));
       if(overlay) g2.setColor(OverlayTemp.getValue()); else strokeColor.setFillColor(g2);
       g2.drawRoundRect(X0, Y0,tWidth ,tHeight, arcValue.getValue(), arcValue.getValue());
       //g2.drawLine(X0, Y0, X0,Y0+tHeight);
       //g2.drawLine(X0,Y0+tHeight,X0+tWidth,Y0+tHeight);
       //g2.drawLine(X0+tWidth,Y0+tHeight,X0+tWidth,Y0);
       
       if(tankEdgeVisible.getValue()){
       g2.setStroke(new BasicStroke(1));
       g2.setColor(tankEdgeColor.getValue());
       //g2.drawRoundRect(X0, Y0,tWidth ,tHeight, arcValue.getValue(), arcValue.getValue());
       
       Rectangle rect= new Rectangle(X0, Y0,tWidth ,tHeight);
       
       g2.drawLine(X0, Y0, X0+tWidth,Y0);
       g2.drawLine(X0, Y0, X0,Y0+tHeight);
       //g2.drawLine(X0,Y0+tHeight,X0+tWidth,Y0+tHeight);
       g2.drawLine(X0+tWidth,Y0+tHeight,X0+tWidth,Y0);
       
       }
       
       
       //--------------------------------------------------------BACKGROUND INTERNAL CIRCLE
       tWidth=(int)(element.jGetWidth()*0.5);
       tHeight=(int)((element.jGetHeight()*0.25));
       X0=(int)(element.jGetWidth()*0.5) - (tWidth/2);
       Y0=(int)(element.jGetHeight()*0.75)  - (tHeight/2);
       
       tankInternalColor.setFillColor(g2);
       g2.fillOval(X0, Y0, tWidth, tHeight);
       
       g2.setStroke(new BasicStroke(strokeValue.getValue()));
       if(overlay) g2.setColor(OverlayTemp.getValue()); else strokeColor.setFillColor(g2);
       g2.drawOval(X0, Y0, tWidth, tHeight);
       
       if(tankEdgeVisible.getValue()){
       g2.setStroke(new BasicStroke(1));
       g2.setColor(tankEdgeColor.getValue());
       g2.drawOval(X0, Y0, tWidth, tHeight);
       }
       
       
       
       //----------------------------------------------------------------FILL PANEL
       
       double wMin=0.055;
       double wMax=0.25;
       double x0min=0.22;
       double x0max=0.5;
       double wPercent=0.15;
       double x0Percent=0.0;
       if(tankfillExpand.getValue()==false){
       wPercent=wMin;
       x0Percent=x0min;
       }else{
       wPercent=wMax;
       x0Percent=x0max;    
       }
       
       tWidth=(int)(element.jGetWidth()*wPercent);
       tHeight=(int)((element.jGetHeight()*0.62)-(strokeValue.getValue()/2));
       X0=(int)(element.jGetWidth()*x0Percent) - (tWidth/2);
       //Y0=(int)((element.jGetHeight()*0.01));
       Y0=(int) (2*(strokeValue.getValue()));
       int YMin=(Y0+tHeight)-strokeValue.getValue();
       int YMax=Y0;
       double m = (YMin-YMax)/(MinValue.getValue() - MaxValue.getValue());
       double b = YMin;
       double y = (m * value) + b;
       //System.err.println("Valor EC: value="+value+"_m:"+m+"_b:"+b+"_Result:"+y +"_Ymin="+YMin+"_YMax"+YMax);
       //Valor EC: value=0.0_m:-0.9_b:104.0_Result:104.0_Ymin=104_YMax14
       //Valor EC: value=100.0_m:-0.9_b:104.0_Result:14.0_Ymin=104_YMax14
       
       g2.setStroke(new BasicStroke(1));
       tankInternalColor.setFillColor(g2);
       g2.fillRoundRect(X0, Y0,tWidth ,tHeight, 5, 5); 
       
       GradientPaint gp = new GradientPaint(0f, 50f, fillColor1.getValue(),(float) element.jGetBounds().width, 50f, fillColor2.getValue());
       int tWidthF1=(int)(element.jGetWidth()*0.5)-(strokeValue.getValue()/2);
       int tHeightF1=(int)((element.jGetHeight()*0.25) -(strokeValue.getValue()/2));
       int X0F1=(int)((element.jGetWidth()*0.5) - (tWidthF1/2));
       int Y0F1=(int)((element.jGetHeight()*0.75)  - (tHeightF1/2)) ;
       
       g2.setPaint(gp);
           //g2.setColor(fillColor1.getValue());
       g2.fillRoundRect(X0,(int) (y),tWidth ,(int) (YMin-y)+strokeValue.getValue(), 5, 5);
       g2.fillOval(X0F1, Y0F1, tWidthF1, tHeightF1);
       g2.setStroke(new BasicStroke(2));
       g2.setColor(UpperFillColor.getValue());
       g2.drawLine(X0,(int) (y-1),X0+tWidth ,(int) (y-1));
       
       
       //g2.setStroke(new BasicStroke(strokeValue.getValue()));
       //if(overlay) overlayColor.setFillColor(g2); else strokeColor.setFillColor(g2);
       //g2.drawRoundRect(X0, Y0,tWidth ,tHeight, arcValue.getValue(), arcValue.getValue());
       
       if(tankEdgeVisible.getValue()){
       g2.setStroke(new BasicStroke(1));
       g2.setColor(tankEdgeColor.getValue());
       //g2.drawRoundRect(X0, Y0,tWidth ,tHeight, arcValue.getValue(), arcValue.getValue());
       Rectangle rect= new Rectangle(X0, Y0,tWidth ,tHeight);
       g2.drawLine(X0, Y0, X0+tWidth,Y0 ); //  -
       //offset=(strokeValue.getValue());
       g2.drawLine(X0, Y0, X0,Y0+tHeight-3); // | Left
       //g2.drawLine(X0,Y0+tHeight,X0+tWidth,Y0+tHeight);
       g2.drawLine(X0+tWidth,Y0+tHeight-3,X0+tWidth,Y0); // Right |
       
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
  if(element!=null){
  value=InitialValue.getValue();
  }
  
  } 
   
  public void init()
  {
    initPins(0,0,0,0);
    setSize(width,height);
    initPinVisibility(false,false,false,false);
    element.jSetInnerBorderVisibility(false);
    
    element.jSetResizable(true);
    element.jSetResizeSynchron(true);
    
    textAlign.addItem("CENTER");
    textAlign.addItem("LEFT");
    textAlign.addItem("RIGHT");
    textAlign.selectedIndex=0;
    
    setName("Tank Indicator J.V.");
    value=InitialValue.getValue();
    
    
    strokeColor.color1=Color.LIGHT_GRAY;
    //overlayColor.color1=new Color(253,153,0);
    
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
    
    element.jAddPEItem("Initial Value",InitialValue, -99999.0,99999.0);
    
    element.jAddPEItem("Max Value",MinValue, -99999.0,99999.0);
    element.jAddPEItem("Min Value",MaxValue, -99999.0,99999.0);
    element.jAddPEItem("Formatierung",formatierung, 0,0);
    
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
    //element.jAddPEItem("Tank Fill Expand",tankfillExpand, 0,0);
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
    
    element.jAddPEItem("Overlay Treshshold",OverlayTresholds, 0,0);
    element.jAddPEItem("Low Treshshold",LowValue,-99999.0,99999.0);
    element.jAddPEItem("Low Farbe",LowColor, 0,0);
    element.jAddPEItem("Low Treshshold",MediumValue,-99999.0,99999.0);
    element.jAddPEItem("Medium Farbe",MediumColor, 0,0);
    element.jAddPEItem("Low Treshshold",HighValue,-99999.0,99999.0);
    element.jAddPEItem("High Farbe",HighColor, 0,0);
    
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
    element.jSetPEItemLocale(d+ i++,language,"Min Value");
    element.jSetPEItemLocale(d+ i++,language,"Max Value");
    element.jSetPEItemLocale(d+ i++,language,"Format String");
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
    //element.jSetPEItemLocale(d+ i++,language,"Tank Fill Expand");
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
    
    element.jSetPEItemLocale(d+ i++,language,"Overlay Tresholds");
    element.jSetPEItemLocale(d+ i++,language,"Low Treshold");
    element.jSetPEItemLocale(d+ i++,language,"Low Color");
    element.jSetPEItemLocale(d+ i++,language,"Medium Treshold");
    element.jSetPEItemLocale(d+ i++,language,"Medium Color");
    element.jSetPEItemLocale(d+ i++,language,"High Treshold");
    element.jSetPEItemLocale(d+ i++,language,"High Color");
    
    //element.jSetPEItemLocale(d+ i++,language,"Tank Bottom Color");
    
    i=1;
    
    language="es_ES";
    element.jSetPEItemLocale(d+i,language,"Valor Inicial");
    element.jSetPEItemLocale(d+ i++,language,"Valor Maximo");
    element.jSetPEItemLocale(d+ i++,language,"Valor Minimo");
    element.jSetPEItemLocale(d+ i++,language,"Formato");
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
    //element.jSetPEItemLocale(d+ i++,language,"Expandir Relleno Indicador");
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
    
    element.jSetPEItemLocale(d+ i++,language,"Limites de Resaltado");
    element.jSetPEItemLocale(d+ i++,language,"Limite Bajo");
    element.jSetPEItemLocale(d+ i++,language,"Color Limite Bajo");
    element.jSetPEItemLocale(d+ i++,language,"Limite Medio");
    element.jSetPEItemLocale(d+ i++,language,"Color Limite Medio");
    element.jSetPEItemLocale(d+ i++,language,"Limite Alto");
    element.jSetPEItemLocale(d+ i++,language,"Color Limite Color");
    //element.jSetPEItemLocale(d+ i++,language,"Color Inferior Tanque");
    
  }

  public void propertyChanged(Object o)
  {
    try
    {
      df = new DecimalFormat(formatierung.getValue());
    } catch(Exception ex)
    {
      df = new DecimalFormat("#0");
    }
      
    if(o.equals(textAlign)){
    AlignNumber.setValue(textAlign.selectedIndex);
    }
    if(o.equals(tankExternalColor) ||o.equals(tankBottomColor) || o.equals(tankInternalColor)){
    defaultColorSettings.setValue(false);
    }
    
    if(InitialValue.getValue()>MaxValue.getValue()) {
    InitialValue.setValue(MaxValue.getValue());
    }
    if(InitialValue.getValue()<MinValue.getValue()) {
    InitialValue.setValue(MinValue.getValue());
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
    //tankfillExpand.loadFromStream(fis);
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
    //tankBottomColor.loadFromStream(fis);
  
  
    AlignNumber.loadFromStream(fis);
    defaultColorSettings.loadFromStream(fis);
    
    MinValue.loadFromStream(fis);
    MaxValue.loadFromStream(fis);
    formatierung.loadFromStream(fis);
    
    OverlayTresholds.loadFromStream(fis);
    LowValue.loadFromStream(fis);
    LowColor.loadFromStream(fis);
    MediumValue.loadFromStream(fis);
    MediumColor.loadFromStream(fis);
    HighValue.loadFromStream(fis);
    HighColor.loadFromStream(fis);
    
    if(InitialValue.getValue()>MaxValue.getValue()) {
    InitialValue.setValue(MaxValue.getValue());
    }
    if(InitialValue.getValue()<MinValue.getValue()) {
    InitialValue.setValue(MinValue.getValue());
    }
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
    //tankfillExpand.saveToStream(fos);
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
    //tankBottomColor.saveToStream(fos);
    
    
    AlignNumber.saveToStream(fos);
    defaultColorSettings.saveToStream(fos);
    
    MinValue.saveToStream(fos);
    MaxValue.saveToStream(fos);
    formatierung.saveToStream(fos);
    
    OverlayTresholds.saveToStream(fos);
    LowValue.saveToStream(fos);
    LowColor.saveToStream(fos);
    MediumValue.saveToStream(fos);
    MediumColor.saveToStream(fos);
    HighValue.saveToStream(fos);
    HighColor.saveToStream(fos);
  }


}

