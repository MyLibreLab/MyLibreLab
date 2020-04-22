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
  private KeyPanel keyPanel;
  private ExternalIF circuitElement;
  //Property
  private VSInteger transparency = new VSInteger(255);
  private VSColor color = new VSColor(Color.GRAY);
  
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
  

  public void paint(java.awt.Graphics g)
  {
  }
   
  public void setPropertyEditor()
  {
    element.jAddPEItem("Color",color, 0,0);
    element.jAddPEItem("Transparenz [0..255]",transparency, 0,255);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";


    element.jSetPEItemLocale(d+0,language,"Color");
    element.jSetPEItemLocale(d+1,language,"Transparency [0..255]");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Color");
    element.jSetPEItemLocale(d+1,language,"Transparency [0..255]");
  }

  public void propertyChanged(Object o)
  {
  
    keyPanel.setTransparency(transparency.getValue());
    keyPanel.color=color.getValue();

  }
  

  public void start()
  {
    circuitElement=element.getCircuitElement();

    keyPanel.circuitElement=circuitElement;
  }

   
  public void init()
  {
    initPins(0,0,0,0);
    initPinVisibility(false,false,false,false);
    setSize(150,150);
    element.jSetInnerBorderVisibility(false);

    //element.jSetResizeSynchron(true);
    element.jSetResizable(true);
    setName("KeyPanel");
  }
  
  public void xOnInit()
  {

    JPanel panel=element.getFrontPanel();

    keyPanel=new KeyPanel();
    panel.setLayout(new BorderLayout());
    //graph.setOpaque(false);
    panel.add(keyPanel,BorderLayout.CENTER);
    //graph.graph.init();
    element.setAlwaysOnTop(true);
    
    keyPanel.setTransparency(transparency.getValue());
    keyPanel.color=color.getValue();


  }


  public void loadFromStream(java.io.FileInputStream fis)
  {

      color.loadFromStream(fis);
      transparency.loadFromStream(fis);
      

  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      color.saveToStream(fos);
      transparency.saveToStream(fos);
  }


}

