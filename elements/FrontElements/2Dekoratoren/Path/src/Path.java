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
import java.text.*;
import tools.*;
import java.awt.geom.*;
import java.util.*;
import java.io.*;



public class Path extends JVSMain
{

  private VSColorAdvanced fillColor = new VSColorAdvanced();
  private VSColorAdvanced strokeColor = new VSColorAdvanced();

  private VSInteger strokeWidth = new VSInteger();
  private VSBoolean fill = new VSBoolean(true);
  private VSBoolean closePath = new VSBoolean(false);

  private ArrayList<PathPoint> points;
  private Stroke standardStroke=new BasicStroke(1);
  

  public void paint(java.awt.Graphics gx)
  {
    if (element!=null)
    {
        Graphics2D g2 = (Graphics2D)gx.create();

        Shape clip=g2.getClip();
        g2.setClip(null);


       g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

       Rectangle r=element.jGetBounds();

       g2.setStroke(new BasicStroke(strokeWidth.getValue(), BasicStroke.CAP_ROUND , BasicStroke.JOIN_ROUND));
       
       
       if (points.size()>0)
       {
         if (fill.getValue())
         {
           fillColor.setFillColor(g2);
           GeneralPath path=element.jParsePath();
           if (closePath.getValue()) path.closePath();
           g2.fill(path);
         }

         if (strokeWidth.getValue()>0)
         {
           strokeColor.setFillColor(g2);
           GeneralPath path=element.jParsePath();
           if (closePath.getValue()) path.closePath();
           g2.draw(path);
         }
       }

       g2.setClip(clip);
       g2.dispose();

    }
  }
  
  


  public void init()
  {
    initPins(0,0,0,0);
    setSize(100,100);
    element.jSetResizable(true);
    initPinVisibility(false,false,false,false);
    element.jSetInnerBorderVisibility(false);

    setName("###GRAPHICSPATH###");
    
    points=element.jGetPointList();
    points.clear();

    strokeWidth.setValue(1);

    fillColor.color1=Color.LIGHT_GRAY;
    strokeColor.color1=Color.BLACK;
    fill.setValue(false);
    
    element.jSetRepaintDifference(10);
  }
  

  public void propertyChanged(Object o)
  {
    double a=strokeWidth.getValue();
    int c=(int)Math.sqrt(a*a+a*a);
    element.jSetRepaintDifference(c*2);
    element.jRepaint();
  }

  
  public void setPropertyEditor()
  {
    element.jAddPEItem("Füll-Farbe",fillColor, 0,0);
    element.jAddPEItem("Linien-Farbe",strokeColor, 0,0);
    element.jAddPEItem("Linien-Breite",strokeWidth, 0,100);
    element.jAddPEItem("Füllen",fill, 0,0);
    element.jAddPEItem("ClosePath",closePath, 0,0);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Fill-Color");
    element.jSetPEItemLocale(d+1,language,"Stroke-Color");
    element.jSetPEItemLocale(d+2,language,"Stroke-Width");
    element.jSetPEItemLocale(d+3,language,"Fill");
    element.jSetPEItemLocale(d+4,language,"ClosePath");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Color Interior");
    element.jSetPEItemLocale(d+1,language,"Color Contorno");
    element.jSetPEItemLocale(d+2,language,"Espesor Contorno");
    element.jSetPEItemLocale(d+3,language,"Fill");
    element.jSetPEItemLocale(d+4,language,"ClosePath");

  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
      fillColor.loadFromStream(fis);
      strokeColor.loadFromStream(fis);
      strokeWidth.loadFromStream(fis);
      fill.loadFromStream(fis);
      closePath.loadFromStream(fis);
      
      propertyChanged(null);
      try
      {
        DataInputStream dis = new DataInputStream(fis);

        int size=dis.readInt();

        points=element.jGetPointList();
        points.clear();
        
        for (int i=0;i<size;i++)
        {
          PathPoint path=new PathPoint();
          
          path.commando=dis.readUTF();
          path.synchron=dis.readBoolean();

          int anzahpPoints=dis.readInt();
          
          for (int j=0;j<anzahpPoints;j++)
          {
            int x=dis.readInt();
            int y=dis.readInt();

            if (j==0) path.p=new Point(x,y);
            if (j==1) path.p1=new Point(x,y);
            if (j==2) path.p2=new Point(x,y);
          }

          points.add(path);
        }
      } catch(Exception ex)
      {
        System.out.println("Path Load:"+ex);
      }
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      fillColor.saveToStream(fos);
      strokeColor.saveToStream(fos);
      strokeWidth.saveToStream(fos);
      fill.saveToStream(fos);
      closePath.saveToStream(fos);

      try
      {
          DataOutputStream dos = new DataOutputStream(fos);
          dos.writeInt(points.size());

          for (int i=0;i<points.size();i++)
          {
            PathPoint path = points.get(i);
            
            dos.writeUTF(path.commando);
            dos.writeBoolean(path.synchron);
            
            int c=1; // p!
            if (path.p1!=null) c++;
            if (path.p2!=null) c++;
            
            dos.writeInt(c);
            
            dos.writeInt(path.p.x);
            dos.writeInt(path.p.y);
            
            if (path.p1!=null)
            {
              dos.writeInt(path.p1.x);
              dos.writeInt(path.p1.y);
            }

            if (path.p2!=null)
            {
              dos.writeInt(path.p2.x);
              dos.writeInt(path.p2.y);
            }

            
          }
      } catch(Exception ex)
      {
          System.out.println("Path Save:"+ex);
      }

      
  }
}
