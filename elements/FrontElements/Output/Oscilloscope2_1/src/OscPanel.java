//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia (cswi@gmx.de)                         *
//*                                                                           *
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
import tools.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import javax.swing.*;
import java.text.*;
import java.awt.geom.Rectangle2D;

import tools.*;
import VisualLogic.variables.*;

public class OscPanel extends JVSMain implements PanelIF
{
  Rectangle bounds;
  private double xValue=0.0;
  private double yValue=0.0;
  private double oldX=0;
  private boolean reset=false;
  private boolean isNull=false;
  private boolean firstTime=true;

  private VSBoolean showBackground = new VSBoolean(true);

  private Color backgroundColor = new Color(50,50,50);
  private Color lineColor = new Color(255,255,255);

  double fX = 1;
  double fY = 1;
  double stepX=20;
  double stepY=20;

  VSGroup ch1=null;
  VSGroup ch2=null;
  VSGroup ch3=null;
  VSGroup ch4=null;
  VSGroup ch5=null;

  VSColor color1=null;
  VSColor color2=null;
  VSColor color3=null;
  VSColor color4=null;
  VSColor color5=null;



  // aus PanelIF
  public void processPanel(int pinIndex, double value, Object obj)
  {
    if (obj instanceof VSGroup || obj instanceof VSColor)
    {
     switch ((int)value)
     {
       case 1  : {
                   ch1=(VSGroup)obj;
                   if (ch1.isChanged()) element.jRepaint();
                   break;
                 }
       case 2  : {
                   ch2=(VSGroup)obj;
                   if (ch2.isChanged()) element.jRepaint();
                   break;
                 }
       case 3  : {
                   ch3=(VSGroup)obj;
                   if (ch3.isChanged()) element.jRepaint();
                   break;
                 }
       case 4  : {
                   ch4=(VSGroup)obj;
                   if (ch4.isChanged()) element.jRepaint();
                   break;
                 }
       case 5  : {
                   ch5=(VSGroup)obj;
                   if (ch5.isChanged()) element.jRepaint();
                   break;
                 }
       case 6  : {
                   color1=(VSColor)obj;
                   if (color1.isChanged()) element.jRepaint();
                   break;
                 }
       case 7  : {
                   color2=(VSColor)obj;
                   if (color2.isChanged()) element.jRepaint();
                   break;
                 }
       case 8  : {
                   color3=(VSColor)obj;
                   if (color3.isChanged()) element.jRepaint();
                   break;
                 }
       case 9  : {
                   color4=(VSColor)obj;
                   if (color4.isChanged()) element.jRepaint();
                   break;
                 }
       case 10 : {
                   color5=(VSColor)obj;
                   if (color5.isChanged()) element.jRepaint();
                   break;
                 }
     }
    }
     
     /*if (obj instanceof VSGroup)
     {
       VSGroup aaa=(VSGroup)obj;

       if (aaa.list.size()==2)
       {
          VSObject obj1 = (VSObject)aaa.list.get(0);
          VSObject obj2 = (VSObject)aaa.list.get(1);

          if (obj1 instanceof VS1DDouble && obj2 instanceof VS1DDouble)
          {
            if (obj1.isChanged() || obj2.isChanged() || isNull)
            {
              isNull=false;
              firstTime=true;
              element.jRepaint();
            }
          }
       }else
     {
       isNull=true;
       if (firstTime)
       {
         element.jRepaint();
         firstTime=false;
       }

     }
     }*/
     
  }
  


  
  public void paint(java.awt.Graphics g)
  {
      if (element!=null)
      {
         Graphics2D g2 = (Graphics2D)g;
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
         bounds=element.jGetBounds();

         int x=0;
         int y=0;
         int xxx=bounds.x+bounds.width;
         int yyy=bounds.y+bounds.height;

         int mitteX=1;
         int mitteY=(yyy)/2;
         
         // zeichnet den Hintergrund
         if (showBackground.getValue())
         {
           g.setColor(backgroundColor);
           g.fillRect(x,y,bounds.width-x,bounds.height);
           g.setColor(Color.black);
           g.drawRect(x,y,bounds.width-x,bounds.height);
         }

         if (ch1!=null) processChannel(g,x,y,mitteX,mitteY,ch1,color1);
         if (ch2!=null) processChannel(g,x,y,mitteX,mitteY,ch2,color2);
         if (ch3!=null) processChannel(g,x,y,mitteX,mitteY,ch3,color3);
         if (ch4!=null) processChannel(g,x,y,mitteX,mitteY,ch4,color4);
         if (ch5!=null) processChannel(g,x,y,mitteX,mitteY,ch5,color5);
         
      }
  }


    private void processChannel(Graphics g,int x, int y,int mitteX, int mitteY,VSGroup channel, VSColor col)
    {
     if (channel.list.size()==2)
     {
        VSObject obj1 = (VSObject)channel.list.get(0);
        VSObject obj2 = (VSObject)channel.list.get(1);

        if (obj1 instanceof VS1DDouble && obj2 instanceof VS1DDouble)
        {
           drawGraph(g,x,y,mitteX,mitteY,(VS1DDouble)obj1,(VS1DDouble)obj2, col);
        }
     }
  }
  
  
  public void drawGraph(Graphics gx,int x, int y,int mitteX,int mitteY,VS1DDouble inX, VS1DDouble inY, VSColor col)
  {
     if (inX!=null && inY!=null)
     {
      Graphics g = gx.create();
      // Clipping auf
      g.clipRect(x,y,bounds.width-x,bounds.height-10);


      double oldxValue;
      double oldyValue;

      double newxValue;
      double newyValue;
      double x1,y1,x2,y2;

      if (col!=null) g.setColor(col.getValue()); else g.setColor(Color.WHITE);
      
       for (int i=0;i<inX.getValue().length;i++)
       {
         if (i>0 && i<inY.getValue().length)
         {
            oldxValue =inX.getValue(i-1);
            oldyValue =inY.getValue(i-1);

            newxValue =inX.getValue(i);
            newyValue =inY.getValue(i);

            x1=mitteX+(oldxValue*fX);
            y1=mitteY-(oldyValue*fY);

            x2=mitteX+(newxValue*fX);
            y2=mitteY-(newyValue*fY);

            if (y1<y) y1=y;
            if (y1>bounds.height) y1=bounds.height-1;

            if (y2<y) y2=y;
            if (y2>bounds.height) y2=bounds.height-1;


            if (x1<0) break;
            if (x2<0) break;
            if (x1>bounds.width) break;
            if (x2>bounds.width) break;

            if (y1!=y2) g.drawLine((int)(x1), (int)y1, (int)x2, (int)y2);

          }

       }
       g.dispose();
     }

  }
   
  public void init()
  {
    initPins(0,0,0,0);
    setSize(250,150);
    
    element.jSetInnerBorderVisibility(false);
    initPinVisibility(false,false,false,false);

    
    element.jSetResizable(true);

    setName("Oscilloscope");
  }


  public void start()
  {
      element.jRepaint();
  }


  public void stop()
  {
      ch1=null;
      ch2=null;
      ch3=null;
      ch4=null;
      ch5=null;
      element.jRepaint();
  }

  public void setPropertyEditor()
  {
    element.jAddPEItem("Zeige Hintergrund",showBackground, 0,0);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Show Background");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Muestra fondo");
  }
  public void propertyChanged(Object o)
  {
    element.jRepaint();
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
      showBackground.loadFromStream(fis);
      element.jRepaint();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      showBackground.saveToStream(fos);
  }


}
