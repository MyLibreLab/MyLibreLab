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

public class AnzeigePanel extends JVSMain implements PanelIF
{
  private int width=50, height=150;
  private double value=0.0;
  private double oldPin;

  private VSColor color1 = new VSColor(Color.WHITE);
  private VSColor color2 = new VSColor(new Color(253,153,0));
  
  private VSColorAdvanced EdgeColor = new VSColorAdvanced();
  private VSColorAdvanced BackColor = new VSColorAdvanced();
  
  private VSColor fontColor = new VSColor(new Color(0,0,0));
  private VSColor StagesColor = new VSColor(Color.RED);
  
  private VSDouble min=new VSDouble();
  private VSDouble max=new VSDouble();
  private VSDouble InitialValue=new VSDouble();
  private VSDouble Stages = new VSDouble();
  private VSBoolean EnableBackground = new VSBoolean(true);
  private VSBoolean EdgeEnable = new VSBoolean(true);
  private VSBoolean StagesDisable = new VSBoolean(false);
  private VSBoolean RoundRect = new VSBoolean(false);
  private VSInteger edgeStroke = new VSInteger(5);
 


  private Font fnt = new Font("Monospaced",Font.BOLD,11);


  public void processPanel(int pinIndex, double value, Object obj)
  {
    this.value=value;
  }

   public void paint(java.awt.Graphics g)
   {
    if (element!=null)
    {
       if (min.getValue()>=max.getValue()) min.setValue(max.getValue()-1);

       
       Rectangle bounds=element.jGetBounds();
       int ArcWidth=(bounds.width-1);
       int ArcHeight=(bounds.height-1);
       
       Graphics2D g2=(Graphics2D)g;
       g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
       
       if(EnableBackground.getValue()){
       BackColor.setFillColor(g2);
         if(RoundRect.getValue()){
         g2.fillRoundRect(bounds.x,bounds.y,bounds.width-1,bounds.height-1,ArcWidth,ArcHeight);    
         }else{
         g2.fillRect(bounds.x,bounds.y,bounds.width-1,bounds.height-1);    
         }
       
       }
       

       double f=(double)bounds.height/(max.getValue()-min.getValue());
       double stand=f*value-min.getValue()*f;

       GradientPaint gp = new GradientPaint(0.0f, 50.0f, color1.getValue(),(float)bounds.width, 50.0f, color2.getValue());
       g2.setPaint(gp);

       if (stand<=0) stand=0;
       if (stand>bounds.height) stand=bounds.height;
       
       if(RoundRect.getValue()){
       g2.fillRoundRect((bounds.x)+3*((bounds.width-1)/8),bounds.height-(int)stand,(bounds.width-1)/4,(int)stand-1,0,0);    
       }else{
       g2.fillRect(bounds.x,bounds.height-(int)stand,bounds.width-1,(int)stand-1);    
       }
       if(EdgeEnable.getValue()){
       
       EdgeColor.setFillColor(g2);
       if(RoundRect.getValue()){
       g2.setStroke(new BasicStroke(edgeStroke.getValue()));
       g2.drawRoundRect(bounds.x,bounds.y,bounds.width-1,bounds.height-1,ArcWidth,ArcHeight);    
       }else{
       g2.setStroke(new BasicStroke(edgeStroke.getValue()));
       g2.drawRect(bounds.x,bounds.y,bounds.width-1,bounds.height-1);
       }
       g2.setStroke(new BasicStroke(1));
       }
       
       g2.setColor(StagesColor.getValue());
       if(RoundRect.getValue()){
          
       g2.drawLine((bounds.x)+3*((bounds.width-1)/8),bounds.height-(int)stand,(bounds.x)+5*((bounds.width-1)/8),bounds.height-(int)stand);
       }else{
           
       g2.drawLine(bounds.x,bounds.height-(int)stand,bounds.width-1,bounds.height-(int)stand);    
       }
       g2.setStroke(new BasicStroke(1));
       if (Stages.getValue()>=bounds.height) Stages.setValue(bounds.height);
       if (Stages.getValue()<1) Stages.setValue(1);
       
       g2.setFont(fnt);

       FontMetrics fm = g2.getFontMetrics();
       Rectangle2D FontB = fm.getStringBounds("", g2);
       double fx=(f*(max.getValue()-min.getValue()))/Stages.getValue();
       
       if(StagesDisable.getValue()==false){
       for (double i=0;i<((double)bounds.height);i+=fx)
       {
         if (i>0.0)
         {
           g2.setColor(StagesColor.getValue());
           if(RoundRect.getValue()){
            //g2.drawLine((bounds.width/2)-5,(int)i,(bounds.width/2)+5,(int)i);   
           }else{
            g2.setStroke(new BasicStroke(2));   
            g2.drawLine(0,(int)i,10,(int)i); 
            g2.setStroke(new BasicStroke(1));
           }
           g2.setColor(fontColor.getValue());
           int x=(int)(min.getValue()+(i/f));
           FontB = fm.getStringBounds(""+x, g2);
           int xTemp=(bounds.width/2)- (int)(FontB.getWidth()/2);
           g2.drawString(""+x,xTemp,((bounds.height-(int)i)+(int)(FontB.getHeight()/3)));
         }
       }
       }
       
       
       
       
    }
   }

  public void init()
  {
    initPins(0,0,0,0);
    
    setSize(width,height);    
    
    
    initPinVisibility(false,false,false,false);
    element.jSetInnerBorderVisibility(false);
    
    element.jSetResizable(true);
    
    min.setValue(0);
    max.setValue(100);
    
    setName("ColorBar_JV");
    
    this.value=InitialValue.getValue();
    BackColor.color1=new Color(255,242,181);
    EdgeColor.color1=new Color(102,102,102);
    Stages.setValue(5);
    element.jSetMinimumSize(30,25);
  }
  
  public void setPropertyEditor()
  {
    //System.out.println("1");
    element.jAddPEItem("Min",min, -9999999.0,99999999.0);
    //System.out.println("2");
    element.jAddPEItem("Max",max, -9999999.0,99999999.0);
    //System.out.println("3");
    element.jAddPEItem("Gradient-Farbe A",color1, 0,0);
    element.jAddPEItem("Gradient-Farbe B",color2, 0,0);
    element.jAddPEItem("Font-Color",fontColor, 0,0);
    element.jAddPEItem("Abschnitte Disable",StagesDisable, 0,0);
    element.jAddPEItem("Abschnitte",Stages, 0.0,99999.0);
    element.jAddPEItem("Abschnitte Color",StagesColor, 0,0);
    element.jAddPEItem("Background Visible",EnableBackground, 0,0);
    element.jAddPEItem("BackgroundColor",BackColor, 0,0);
    element.jAddPEItem("Edge Visible",EdgeEnable, 0,0);
    element.jAddPEItem("Edge Color",EdgeColor, 0,0);
    element.jAddPEItem("Edge Stroke",edgeStroke, 0,15);
    element.jAddPEItem("Init Value",InitialValue, -9999999.0,99999999.0);
    element.jAddPEItem("Round Rect En",RoundRect, 0,0);
    //element.jAddPEItem("Vertical?",Theta, 0,360);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Min");
    element.jSetPEItemLocale(d+1,language,"Max");
    element.jSetPEItemLocale(d+2,language,"Gradient-Color A");
    element.jSetPEItemLocale(d+3,language,"Gradient-Color B");
    element.jSetPEItemLocale(d+4,language,"Font-Color");
    element.jSetPEItemLocale(d+5,language,"Stages Disable?");
    element.jSetPEItemLocale(d+6,language,"Number of Stages");
    element.jSetPEItemLocale(d+7,language,"Stages Color");
    element.jSetPEItemLocale(d+8,language,"Background Visible");
    element.jSetPEItemLocale(d+9,language,"Background Color");
    element.jSetPEItemLocale(d+10,language,"Edge Enable?");
    element.jSetPEItemLocale(d+11,language,"Edge Color");
    element.jSetPEItemLocale(d+12,language,"Edge Stroke");
    element.jSetPEItemLocale(d+13,language,"Init Value");
    element.jSetPEItemLocale(d+14,language,"Round Rect En");
    //element.jSetPEItemLocale(d+12,language,"Rotation (Theta)");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Min");
    element.jSetPEItemLocale(d+1,language,"Max");
    element.jSetPEItemLocale(d+2,language,"Color Mezcla A");
    element.jSetPEItemLocale(d+3,language,"Color Mezcla B");
    element.jSetPEItemLocale(d+4,language,"Color Letra");
    element.jSetPEItemLocale(d+5,language,"Deshabilitar Marcas?");
    element.jSetPEItemLocale(d+6,language,"Cantidad de Marcas");
    element.jSetPEItemLocale(d+7,language,"Color de marcas");
    element.jSetPEItemLocale(d+8,language,"Fondo Visible?");
    element.jSetPEItemLocale(d+9,language,"Color de fondo");
    element.jSetPEItemLocale(d+10,language,"Marco Visible?");
    element.jSetPEItemLocale(d+11,language,"Color del marco");
    element.jSetPEItemLocale(d+12,language,"Espesor del marco");
    element.jSetPEItemLocale(d+13,language,"Valor Inicial");
    element.jSetPEItemLocale(d+14,language,"Redondear Forma");
    //element.jSetPEItemLocale(d+12,language,"Angulo Rotacion");
  }
  
  
  public void propertyChanged(Object o)
  { 
    this.value=InitialValue.getValue(); 
    
    if(o.equals(RoundRect)){
    if(RoundRect.getValue()) edgeStroke.setValue(2); else edgeStroke.setValue(5);
    }
    
    element.jRepaint();
  }



  public void loadFromStream(java.io.FileInputStream fis)
  {
      min.loadFromStream(fis);
      max.loadFromStream(fis);
      
      fontColor.loadFromStream(fis);
      
      color1.loadFromStream(fis);
      color2.loadFromStream(fis);
      
      
      Stages.loadFromStream(fis);
      
      StagesDisable.loadFromStream(fis);
      
      StagesColor.loadFromStream(fis);
      EnableBackground.loadFromStream(fis);
      BackColor.loadFromStream(fis);
      EdgeEnable.loadFromStream(fis);
      EdgeColor.loadFromStream(fis);
      InitialValue.loadFromStream(fis);
      edgeStroke.loadFromStream(fis);
      
      if(InitialValue.getValue()>=max.getValue()){
          InitialValue.setValue(0);
      }
          

      
      if (min.getValue()>=max.getValue()) min.setValue(max.getValue()-1);
      element.jRepaint();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      min.saveToStream(fos);
      max.saveToStream(fos);
      
      fontColor.saveToStream(fos);
      
      color1.saveToStream(fos);
      color2.saveToStream(fos);

      Stages.saveToStream(fos);
      
      StagesDisable.saveToStream(fos);
      
      StagesColor.saveToStream(fos);
      EnableBackground.saveToStream(fos);
      BackColor.saveToStream(fos);
      EdgeEnable.saveToStream(fos);
      EdgeColor.saveToStream(fos);
      InitialValue.saveToStream(fos);
      edgeStroke.saveToStream(fos);
     
  }
  
}

