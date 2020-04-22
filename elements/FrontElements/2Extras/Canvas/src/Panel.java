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
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import VisualLogic.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import tools.*;
import VisualLogic.variables.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.*;
import java.util.*;

public class Panel extends JVSMain implements PanelIF
{
  private boolean on=false;
  private VSBoolean interpolation = new VSBoolean(false);
  private VSGroup in;

  
  public void onDispose()
  {
  }

  public void processPanel(int pinIndex, double value, Object obj)
  {
    if (obj instanceof VSGroup)
    {
     in=(VSGroup)obj;
    }
    
  }
  
  private boolean firstTime=true;
  
  private void renderString(Graphics2D g2, VSCanvas can)
  {
      setAntialising(g2,can);
      transform(g2,can);

      g2.setFont(can.font);
      g2.setColor(can.strokeColor);

      g2.drawString(can.caption, can.x1,can.y1);

  }

  private void renderRectangle(Graphics2D g2, VSCanvas can)
  {
    setAntialising(g2,can);
    transform(g2,can);

    g2.setStroke(new BasicStroke(can.strokeWidth));

    g2.setColor(can.fillColor);
    g2.fillRect(can.x1,can.y1,can.x2,can.y2);

    if (can.strokeWidth>0)
    {
      g2.setColor(can.strokeColor);
      g2.drawRect(can.x1,can.y1,can.x2,can.y2);
    }
  }

  
  private void renderEllipse(Graphics2D g2, VSCanvas can)
  {
    setAntialising(g2,can);
    transform(g2,can);

    g2.setStroke(new BasicStroke(can.strokeWidth));

    g2.setColor(can.fillColor);
    g2.fillOval(can.x1,can.y1,can.x2,can.y2);

    if (can.strokeWidth>0)
    {
      g2.setColor(can.strokeColor);
      g2.drawOval(can.x1,can.y1,can.x2,can.y2);
    }
  }
  
  private void renderLine(Graphics2D g2, VSCanvas can)
  {
    setAntialising(g2,can);
    transform(g2,can);

    g2.setStroke(new BasicStroke(can.strokeWidth));

    if (can.strokeWidth>0)
    {
      g2.setColor(can.strokeColor);
      g2.drawLine(can.x1,can.y1,can.x2,can.y2);
    }
  }




  private void renderImage(Graphics2D g2, VSCanvas can)
  {
    if (can.antialising)    // Interpolation für mages!
    {
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC );
    } else
    {
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
    }
    
    transform(g2,can);

    g2.drawImage(can.image,can.x1,can.y1,can.x2,can.y2, null);

  }
  
  private void setAntialising(Graphics2D g2, VSCanvas can)
  {
    if (can.antialising)
    {
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    } else
    {
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
    }
  }
  
  private void transform(Graphics2D g2, VSCanvas can)
  {
      double x=can.translationX;
      double y=can.translationY;

      g2.translate(x,y);
      g2.rotate(can.rotationAngle,can.rotationX,can.rotationY);

      g2.scale(can.scaleX,can.scaleY);
      g2.shear(can.shearX,can.shearY);
  }
  
  
  private void renderPoints(Graphics2D g2, VSCanvas can)
  {
    setAntialising(g2,can);
    transform(g2,can);

    g2.setStroke(new BasicStroke(can.strokeWidth));
    g2.setColor(can.strokeColor);

    ArrayList points = can.points;
    firstTime=true;
    
    int pointType=can.x1;
    
    Point p;
    int oldX=0;
    int oldY=0;

    for (int j=0;j<points.size();j++)
    {
      p = (Point)points.get(j);

      if (p!=null)
      {
        if (pointType==0)
        {
          g2.drawLine(p.x,p.y,p.x,p.y);
        }else
        if (pointType==1)
        {
          if (firstTime)
          {
            oldX=p.x;
            oldY=p.y;
            firstTime=false;
          } else
          {
            g2.drawLine(oldX,oldY,p.x,p.y);
            oldX=p.x;
            oldY=p.y;
          }
        }
      }
    }

  }
  
   public void rec(VSObject input, java.awt.Graphics2D g)
   {
   
     if (input instanceof VSGroup)
     {
       VSGroup group = (VSGroup)input;
       for (int i=0;i<group.list.size();i++)
       {
         VSObject o = (VSObject)group.list.get(i);
         rec(o,g);
       }
     } else
     if (input instanceof VSCanvas)
     {
       VSCanvas can = (VSCanvas)input;
       
       Graphics2D g2=(Graphics2D)g.create();

       if (can.type==element.C_SHAPE_POINTS)  renderPoints(g2,can);else
       if (can.type==element.C_SHAPE_ELLIPSE) renderEllipse(g2,can);else
       if (can.type==element.C_SHAPE_STRING) renderString(g2,can);else
       if (can.type==element.C_SHAPE_RECTANGLE) renderRectangle(g2,can);else
       if (can.type==element.C_SHAPE_LINE) renderLine(g2,can);else
       if (can.type==element.C_SHAPE_IMAGE) renderImage(g2,can);
       
       g2.dispose();
     }
   }

   public void paint(java.awt.Graphics g)
   {
     if (element!=null)
     {
       Graphics2D g2 = (Graphics2D)g;
       AffineTransform origTransform = g2.getTransform();

       if (in instanceof VSGroup)
       {
         rec(in,g2);
       }

       g2.setTransform(origTransform);
     }
   }
   
  public void setPropertyEditor()
  {
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    /*language="en_US";

    element.jSetPEItemLocale(d+0,language,"Interpolation");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Interpolación");*/
  }

  public void propertyChanged(Object o)
  {

  }
   
  public void init()
  {
    initPins(0,0,0,0);
    initPinVisibility(false,false,false,false);
    setSize(150,150);
    element.jSetInnerBorderVisibility(false);

    //element.jSetResizeSynchron(true);
    element.jSetResizable(true);
    setName("Canvas Render Engine 1.0");
  }
  


  public void loadFromStream(java.io.FileInputStream fis)
  {

  }

  public void saveToStream(java.io.FileOutputStream fos)
  {

  }



}
 
