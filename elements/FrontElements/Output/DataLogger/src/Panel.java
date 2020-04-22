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
import java.util.ArrayList;
import tools.*;
import VisualLogic.variables.*;

public class Panel extends JVSMain implements PanelIF
{
  Rectangle bounds;
  private double xValue=0.0;
  private double yValue=0.0;
  private double oldX=0;
  private int counter=0;
  private VSInteger millis = new VSInteger();
  
  private boolean isNull=false;
  private boolean firstTime=true;

  private boolean reset=false;
  private VSBoolean showBackground = new VSBoolean(true);
  private VSBoolean showGrid = new VSBoolean(true);

  public VSDouble in=null;
  private Font font=new Font("Arial",1,10);
  private int c=0;
  
  private ArrayList values = new ArrayList();

  private boolean xAchseBottom=false;
  private boolean yAchseLeft=true;
  private boolean xAchseLettersBottom=true;
  private boolean yAchseLettersLeft=false;
  
  
  private Color xAchseColor;
  private Color yAchseColor;
  private Color backgroundColor = new Color(50,50,50);
  private Color dunkelGruen = new Color(0,100,0);
  private Color lineColor = new Color(255,255,255);
  
  private boolean showFontXAchse=true;
  private boolean showFontYAchse=true;
  private boolean showHelpLinesXAchse=true;
  private boolean showHelpLinesYAchse=true;

  private double minX=-2000;
  private double zeitPosition=0;
  private double maxX=2000;

  private double minY=-2000;
  private double maxY=2000;
  
  private boolean fadenKreuzVisible=true;
  
  private int darstellungAs=0;  // 0 : Punkt 1 : Linie

  double fX = 1;
  double fY = 1;

  double stepX=20;
  double stepY=20;

  javax.swing.Timer timer;
  
  boolean running=false;


  // aus PanelIF
  public void processPanel(int pinIndex, double value, Object obj)
  {
     if (value==1.0)
     {
       values.clear();
       zeitPosition=0;
     }
     if (value==0.0)
     {
      in=(VSDouble)obj;
     }
  }
  
  public void paint(java.awt.Graphics g)
  {
      if (element!=null)
      {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
        bounds=element.jGetBounds();

         //trigger(0);
         
         int x=bounds.x+10;
         int y=bounds.y+10;
         
         int xxx=bounds.x+bounds.width;
         int yyy=bounds.y+bounds.height-10;


         int mitteX=(xxx)/2;
         int mitteY=(yyy)/2;
         
         if (xAchseBottom)
         {
           mitteY=yyy-y;
           xAchseLettersBottom=true;
         }
         if (yAchseLeft)
         {
          mitteX=x+15;
          yAchseLettersLeft=true;
         }

         if (xAchseLettersBottom) bounds.height-=20;
         if (yAchseLettersLeft) x+=15;

         
         if (showBackground.getValue())
         {
           // zeichnet den Hintergrund
           g.setColor(backgroundColor);
           g.fillRect(x,y,bounds.width-x,bounds.height-10);
           g.setColor(Color.black);
           g.drawRect(x,y,bounds.width-x,bounds.height-10);
         }


         if (xAchseLettersBottom) xAchseColor=Color.black; else xAchseColor=Color.white;
         if (yAchseLettersLeft) yAchseColor=Color.black; else yAchseColor=Color.white;

         if (fadenKreuzVisible && showGrid.getValue())
         {
           g.setColor(dunkelGruen);
           if (!xAchseBottom)
           {
             // zeichnet die X-Achse
             g.drawLine(x,mitteY,x+bounds.width-x,mitteY);
           }

           if (!yAchseLeft)
           {
             // zeichnet die Y-Achse
             g.drawLine(mitteX,bounds.y+5, mitteX,y+bounds.height-y);
           }
         }
         g.setColor(Color.white);

         double xx,yy;
         
         FontMetrics fm = g.getFontMetrics();


         Rectangle2D rx;

         
         Rectangle2D rx1;
         Rectangle2D rx2;


         g.setFont(font);
         
         double miniX=minX;
         if (yAchseLeft)miniX=0;

         if (showGrid.getValue())
         {

           // Linien und Beschriftung für die X-Achse
           maxX=99999999;
           for (xx=miniX;xx<=maxX;xx+=stepX)
           {
             double xx1=(xx-zeitPosition)*fX;
             
             if (xx1>bounds.width) break;
             int xc=mitteX+(int)xx1;
             g.setColor(dunkelGruen);
             //g.drawLine(xc,mitteY,xc,mitteY+5);

             if (xc>x && xc<bounds.width)
             {

               if (showHelpLinesXAchse)  g.drawLine(xc,y,xc,yyy-y);

               g.setColor(xAchseColor);

               String strValue=""+(int)xx;

               rx = fm.getStringBounds(strValue,g);

               int mx=(int)rx.getWidth()/2;
               if (showFontXAchse)
               {
                 if (xAchseLettersBottom)
                 {
                   g.drawString(strValue,xc-mx,bounds.y+bounds.height+10);
                 } else
                 {
                   g.drawString(strValue,xc-mx,mitteY+20);
                 }
               }
             }

           }

           String strValue="";

           // Linien und Beschriftung für die Y-Achse
           for (yy=minY;yy<=maxY;yy+=stepY)
           {
             double yy1=yy*fY;
             int yc=mitteY+(int)yy1;

             if (yc>y && yc<bounds.height)
             {
               g.setColor(dunkelGruen);
               //g.drawLine(mitteX-5,yc,mitteX,yc);
               if (showHelpLinesYAchse) g.drawLine(x,yc,xxx-10,yc);

               g.setColor(yAchseColor);
               strValue=""+(int)(0-yy);
               rx = fm.getStringBounds(strValue,g);

               if (showFontYAchse)
               {
                 if (yAchseLettersLeft)
                 {
                   g.drawString(strValue,bounds.x+8,yc);
                 } else
                 {
                   g.drawString(strValue,mitteX-(int)rx.getWidth()-8,yc);
                 }
               }
             }
           }
           }
           
           drawGraph(g,x,y,mitteX,mitteY);

      }
  }
  

  
  public void drawGraph(Graphics g,int x, int y,int mitteX,int mitteY)
  {

     if (in!=null)
     {
       g.clipRect(x,y,bounds.width-x,bounds.height-10);
       
       double oldxValue;
       double oldyValue;

       double newxValue;
       double newyValue;
       double x1,y1,x2,y2;

       g.setColor(lineColor);
       for (int i=0;i<values.size();i++)
       {
         if (i>0)
         {
            oldyValue =( (Double)values.get(i-1) ).doubleValue();
            newyValue =((Double)values.get(i)).doubleValue();

            x1=mitteX+(i-1*fX);
            y1=mitteY-(oldyValue*fY);

            x2=mitteX+(i*fX);
            y2=mitteY-(newyValue*fY);

            if (y1<y) y1=y;
            if (y1>bounds.height) y1=bounds.height-1;

            if (y2<y) y2=y;
            if (y2>bounds.height) y2=bounds.height-1;


            
            if (x1<bounds.x+10) break;
            if (x2<bounds.x+10) break;
            if (x1>bounds.width)break;
            if (x2>bounds.width)
            {
              values.remove(0);
              zeitPosition++;
              break;
            }


            if (darstellungAs==0)
            {
              g.drawLine((int)(x1), (int)y1,(int)(x1), (int)y1);
            } else
            {
              g.drawLine((int)(x1), (int)y1, (int)x2, (int)y2);
            }


          }

       }
       g.setClip(null);
     }

  }
   
  public void init()
  {
    initPins(0,0,0,0);
    setSize(250,150);
    
    element.jSetInnerBorderVisibility(true);
    counter=0;
    zeitPosition=0;
    
    element.jSetResizable(true);

    setName("DataLogger");
    
    millis.setValue(100);
  }

  public void process()
  {
  }
  
  public void openPropertyDialog()
  {
     FrameProperties.zoomX=(int)fX;
     FrameProperties.zoomY=(int)fY;
     FrameProperties.stepX=(int)stepX;
     FrameProperties.stepY=(int)stepY;
     
     FrameProperties.minX=(int)minX;
     FrameProperties.maxX=(int)maxX;
     FrameProperties.minY=(int)minY;
     FrameProperties.maxY=(int)maxY;

     FrameProperties.xAchseBottom=xAchseBottom;
     FrameProperties.yAchseLeft=yAchseLeft;
     FrameProperties.xAchseLettersBottom=xAchseLettersBottom;
     FrameProperties.yAchseLettersLeft=yAchseLettersLeft;
     
     FrameProperties.darstellungAs=darstellungAs;
     
     FrameProperties.fadenKreuzVisible=fadenKreuzVisible;

     FrameProperties.showFontXAchse=showFontXAchse;
     FrameProperties.showFontYAchse=showFontYAchse;
     FrameProperties.showHelpLinesXAchse=showHelpLinesXAchse;
     FrameProperties.showHelpLinesYAchse=showHelpLinesYAchse;

     FrameProperties.execute();
     if (FrameProperties.result==true)
     {
       fX=(double)FrameProperties.zoomX;
       fY=(double)FrameProperties.zoomY;
       stepX=(double)FrameProperties.stepX;
       stepY=(double)FrameProperties.stepY;
       
       minX=(double)FrameProperties.minX;
       maxX=(double)FrameProperties.maxX;
       minY=(double)FrameProperties.minY;
       maxY=(double)FrameProperties.maxY;

       darstellungAs=FrameProperties.darstellungAs;

       showFontXAchse=FrameProperties.showFontXAchse;
       showFontYAchse=FrameProperties.showFontYAchse;
       showHelpLinesXAchse=FrameProperties.showHelpLinesXAchse;
       showHelpLinesYAchse=FrameProperties.showHelpLinesYAchse;
     
       xAchseBottom=FrameProperties.xAchseBottom;
       yAchseLeft=FrameProperties.yAchseLeft;
       xAchseLettersBottom=FrameProperties.xAchseLettersBottom;
       yAchseLettersLeft=FrameProperties.yAchseLettersLeft;
       fadenKreuzVisible=FrameProperties.fadenKreuzVisible;
       
       element.jRepaint();
     }
  }

  public void mousePressed(MouseEvent e)
  {
  }


  public void start()
  {
    counter=0;
    zeitPosition=0;
    values.clear();
    running=true;

    ActionListener taskPerformer = new ActionListener()
    {
        public void actionPerformed(ActionEvent evt)
        {
          if (running==true)
          {
            if (in!=null)
            {
              values.add(new Double(in.getValue()));
              element.jRepaint();
            }

          }
        }
    };
    if (millis.getValue()<0) millis.setValue(0);
    System.out.println("Millis = "+millis.getValue());
    if (timer!=null)timer.stop();
    timer = new javax.swing.Timer(millis.getValue(), taskPerformer);
    timer.start();
  }


  public void stop()
  {
    running=false;
    try
    {
      Thread.sleep(100);
    } catch(Exception ex)
    {

    }
    if (timer!=null)timer.stop();
    //timer=null;
  }

  public void setPropertyEditor()
  {
    element.jAddPEItem("Zeige Hintergrund",showBackground, 0,0);
    element.jAddPEItem("Zeige Gitter",showGrid, 0,0);
    element.jAddPEItem("Aktualisierung [ms]",millis, 0,3600000);  // also bis zu einer Stunde!
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Show Background");
    element.jSetPEItemLocale(d+1,language,"Show Grid");
    element.jSetPEItemLocale(d+2,language,"Refresh Time [ms]");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Mostrar Fondo");
    element.jSetPEItemLocale(d+1,language,"Mostrar Rejilla");
    element.jSetPEItemLocale(d+2,language,"Tiempo Refresco [ms]");
  }

  public void propertyChanged(Object o)
  {
    element.jRepaint();
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
    java.io.DataInputStream stream= new java.io.DataInputStream (fis);
    
    try
    {
      showBackground.loadFromStream(fis);
      millis.loadFromStream(fis);
      showGrid.loadFromStream(fis);

      minX=stream.readDouble();
      maxX=stream.readDouble();
      minY=stream.readDouble();
      maxY=stream.readDouble();

      fX=stream.readDouble();
      fY=stream.readDouble();
      stepX=stream.readDouble();
      stepY=stream.readDouble();

      darstellungAs=stream.readInt();

      fadenKreuzVisible=stream.readBoolean();

      showFontXAchse=stream.readBoolean();
      showFontYAchse=stream.readBoolean();
      showHelpLinesXAchse=stream.readBoolean();
      showHelpLinesYAchse=stream.readBoolean();


      xAchseBottom=stream.readBoolean();
      yAchseLeft=stream.readBoolean();
      xAchseLettersBottom=stream.readBoolean();
      yAchseLettersLeft=stream.readBoolean();

    } catch(Exception ex)
    {
      element.jException(ex.toString());
    }

    element.jRepaint();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    try
    {
      java.io.DataOutputStream dos= new java.io.DataOutputStream(fos);

      showBackground.saveToStream(fos);
      millis.saveToStream(fos);
      showGrid.saveToStream(fos);

      dos.writeDouble(minX);
      dos.writeDouble(maxX);
      dos.writeDouble(minY);
      dos.writeDouble(maxY);

      dos.writeDouble(fX);
      dos.writeDouble(fY);
      dos.writeDouble(stepX);
      dos.writeDouble(stepY);

      dos.writeInt(darstellungAs);
      dos.writeBoolean(fadenKreuzVisible);

      dos.writeBoolean(showFontXAchse);
      dos.writeBoolean(showFontYAchse);
      dos.writeBoolean(showHelpLinesXAchse);
      dos.writeBoolean(showHelpLinesYAchse);


      dos.writeBoolean(xAchseBottom);
      dos.writeBoolean(yAchseLeft);
      dos.writeBoolean(xAchseLettersBottom);
      dos.writeBoolean(yAchseLettersLeft);

    } catch(Exception ex)
    {
      element.jException(ex.toString());
    }

  }

}
