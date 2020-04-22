//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia (cswi@gmx.de)                         *
//* Copyright (C) 2018  Javier Velasquez (javiervelasquez125@gmail.com)                                                                          *
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

import VisualLogic.variables.*;
import tools.*;
import java.awt.*;
import static java.lang.Math.cos;
import static java.lang.Math.sin;



public class XLabel extends JVSMain
{
  private VSColorAdvanced lineColor = new VSColorAdvanced();
  private VSInteger lineStroke = new VSInteger(5);
  private VSInteger angle = new VSInteger(45);
  //private VSBoolean vertical = new VSBoolean(false);

  public XLabel()
  {
    //strText.setValue("Line_JV");
    lineColor.color1=Color.DARK_GRAY;
    myInit();
  }
  
  public void myInit()
  {


  }


  public void paint(java.awt.Graphics g)
  {
      if(element!=null){
         
        Graphics2D g2 = (Graphics2D)g;
        //g2.setColor(lineColor.getValue());
        lineColor.setFillColor(g2);
        Stroke restore = g2.getStroke();
        g2.setStroke(new BasicStroke(lineStroke.getValue()));
        
//        if(vertical.getValue()){
//        int X0=(lineStroke.getValue()/2) + ((element.jGetWidth()/2)-(lineStroke.getValue()/2));
//        int Y0=(lineStroke.getValue()/2);
//        g2.drawLine(X0,Y0,X0,element.jGetHeight());    
//        }else{
//        int X0=(lineStroke.getValue()/2);
//        int Y0=(lineStroke.getValue()/2) + ((element.jGetHeight()/2)-(lineStroke.getValue()/2));    
//        
//        
        int xC = element.jGetWidth()/2;
        int yC = element.jGetHeight()/2;
        int maxY = element.jGetHeight()/2;
        int maxX = element.jGetWidth()/2;
        int angle = this.angle.getValue();
        
        int X2 =(int) (maxX*cos(Math.toRadians(angle)));
        int Y2 =(int) (maxY*sin(Math.toRadians(angle)));
        
        g2.setStroke(new BasicStroke(lineStroke.getValue()));
        int off=lineStroke.getValue()/2;
        g2.drawLine(xC-X2,yC+Y2, xC+X2,yC-Y2);
 
        }
        
      
      
  }
   
  public void init()
  {
    initPins(0,0,0,0);
    setSize(100,100);
    initPinVisibility(false,false,false,false);
    element.jSetResizable(true);
    element.jSetInnerBorderVisibility(false);
    //setSize(180,20);
    
    setName("Line_JV");
    

  }


  public void xOnInit()
  {
    try
    {
      //JPanel panel =element.getFrontPanel();
      //panel.setLayout(new java.awt.BorderLayout());

      //panel.add(label, java.awt.BorderLayout.CENTER);
      element.setAlwaysOnTop(true);
    } catch(Exception ex)
    {
      System.out.println(ex);
    }

  }


  public void setPropertyEditor()
  {
    
    element.jAddPEItem("Line Color",lineColor, 0,0);
    element.jAddPEItem("Line Stroke",lineStroke, 0,50);
    element.jAddPEItem("Angle (0-360)",angle, -360,360);
    //element.jAddPEItem("Vertical",vertical, 0,0);
    
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Line Color");
    element.jSetPEItemLocale(d+1,language,"Line Stroke");
    element.jSetPEItemLocale(d+2,language,"Angle (0-360)");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Color Linea");
    element.jSetPEItemLocale(d+1,language,"Espesor Linea");
    element.jSetPEItemLocale(d+2,language,"Angulo (0-360)");

  }
  public void propertyChanged(Object o)
  {
//      if(o.equals(vertical)){
//      int WidthTemp = element.jGetWidth();
//      int HeighTemp = element.jGetHeight(); 
//        
//      setSize(HeighTemp,WidthTemp);
//        
//      }
      element.jRepaint();
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
     lineColor.loadFromStream(fis);
     lineStroke.loadFromStream(fis);
angle.loadFromStream(fis);
     //vertical.loadFromStream(fis);
    if(angle.getValue()>360 || angle.getValue()<-360){
     angle.setValue(90);
    }

     element.jRepaint();

  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      lineColor.saveToStream(fos);
      lineStroke.saveToStream(fos);
angle.saveToStream(fos);
      //vertical.saveToStream(fos);
  }
}
