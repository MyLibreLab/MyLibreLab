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
import javax.swing.*;
import java.awt.event.*;

import java.io.*;



public class Panel extends JVSMain implements PanelIF
{
  private ExternalIF circuitElement;
  
  private int statusCount= 10;
  private VSImage images[] = new VSImage[statusCount];


  private Image image;

  private boolean value=false;
  private int sizeW=47;
  private int sizeH=101;
  private int status=0;

  
  private VSBoolean interpoliert = new VSBoolean();


  public void processPanel(int pinIndex, double value, Object obj)
  {
    status=(int)value;
    element.jRepaint();
  }
  
  public Panel()
  {
    for (int i=0;i<statusCount;i++)
    {
     images[i] = new VSImage();
    }
  }



  public void paint(java.awt.Graphics g)
  {
    if (element!=null)
    {
       Graphics2D g2=(Graphics2D)g;
       Rectangle bounds=element.jGetBounds();
      if (interpoliert.getValue()==true)
      {
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      } else
      {
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
      }

      if (status>=0 && status<images.length)
      {
       VSImage image=images[status];
       g2.drawImage(image.getImage(),0,0,element.jGetWidth()-1,element.jGetHeight()-1,null);
      }
    }
  }
  
  private void setValue(VSBoolean bol)
  {

     circuitElement=element.getCircuitElement();
     if (circuitElement!=null) circuitElement.Change(0,bol);
  }


  public void init()
  {
    initPins(0,0,0,0);
    setSize(sizeW,sizeH);
    initPinVisibility(false,false,false,false);

    element.jSetInnerBorderVisibility(false);

    element.jSetResizable(true);
    
    setName("StatusDisplay 1.0");

    String fn1=element.jGetSourcePath()+"red.png";
    String fn2=element.jGetSourcePath()+"redYellow.png";
    String fn3=element.jGetSourcePath()+"green.png";
    String fn4=element.jGetSourcePath()+"yellow.png";
    String fn5=element.jGetSourcePath()+"red.png";


    images[0].loadImage(fn1);
    images[1].loadImage(fn2);
    images[2].loadImage(fn3);
    images[3].loadImage(fn4);
    images[4].loadImage(fn5);

    interpoliert.setValue(false);

  }

    
  public void setPropertyEditor()
  {

    element.jAddPEItem("Interpoliert",interpoliert, 0,0);
    
    for (int i=0;i<statusCount;i++)
    {
      element.jAddPEItem("Bild "+i,images[i], 0,0);
    }

    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Interpolation");

    for (int i=0;i<statusCount;i++)
    {
      element.jSetPEItemLocale(d+i+1,language,"Image "+i);
    }

    language="es_ES";


    element.jSetPEItemLocale(d+0,language,"Interpolation");
    for (int i=0;i<statusCount;i++)
    {
      element.jSetPEItemLocale(d+i+1,language,"Image "+i);
    }

  }

  public void propertyChanged(Object o)
  {

    element.jRepaint();
  }



  public void loadFromStream(java.io.FileInputStream fis)
  {
      interpoliert.loadFromStream(fis);

      for (int i=0;i<statusCount;i++)
      {
        images[i].loadFromStream(fis);
      }

      element.jRepaint();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {

      interpoliert.saveToStream(fos);
      
      for (int i=0;i<statusCount;i++)
      {
        images[i].saveToStream(fos);
      }
  }



}

