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
import java.util.*;
import MyGraph.*;

public class OscPanel extends VSMainWithPropertyManager implements PanelIF
{
  private JPanel panel;
  private MyGraph graph= new MyGraph();

  public VS1DDouble inX=null;
  public VS1DDouble inY=null;
  

  // aus PanelIF
  public void processPanel(int pinIndex, double value, Object obj)
  {
    if (obj instanceof VS1DDouble && pinIndex==0)
    {
     inX= (VS1DDouble)obj;
    }
    if (obj instanceof VS1DDouble && pinIndex==1)
    {
     inY= (VS1DDouble)obj;
    }

     if (inX!=null && inY!=null)
     {
       if (inX.isChanged() || inY.isChanged())
       {
          graph.graph.back.xValues=inX.getValue();
          graph.graph.back.yValues=inY.getValue();
          graph.graph.init();
          element.jRepaint();
       }
     }
  }

   
  public void init()
  {
    initPins(0,0,0,0);
    setSize(250,250);
    element.jSetInnerBorderVisibility(false);
    element.jSetResizable(true);
    setName("2.0 Oscilloscope Version 1.0");


    
    addProp("BackgroundTransparent","DE","Background Transparent",0,0);
    addProp("GridBackgroundColor","DE","Grid Background Color",0,0);
    addProp("GridLineColor","DE","Grid Line Color",0,0);
    addProp("GridSublineColor","DE","Grid Subline Color",0,0);
    addProp("XYAxisFont","DE","x/y Axis Font",0,0);
    addProp("XYAxisFontColor","DE","x/y Axis Font Color",0,0);
    addProp("XYAxisVisible","DE","x/y Axis Visible",0,0);
    addProp("MinX","DE","X-Axis Min",-999999999,999999999);
    addProp("MaxX","DE","X-Axis Max",-999999999,999999999);
    addProp("AutoZoomX","DE","X-Axis Autozoom",0,0);
    addProp("XAxisFormatString","DE","X-Axis Format",0,0);
    addProp("XAxisText","DE","X-Axis Text",0,0);
    addProp("XAxisTextFont","DE","X-Axis Text Font",0,0);
    addProp("XAxisTextFontColor","DE","X-Axis Text Color",0,0);
    addProp("MinY","DE","Y-Axis Min",-999999999,999999999);
    addProp("MaxY","DE","Y-Axis Max",-999999999,999999999);
    addProp("AutoZoomY","DE","Y-Axis Autozoom",0,0);
    addProp("YAxisFormatString","DE","Y-Axis Format",0,0);
    addProp("YAxisText","DE","Y-Axis Text",0,0);
    addProp("YAxisTextFont","DE","Y-Axis Text Font",0,0);
    addProp("YAxisTextFontColor","DE","Y-Axis Text Color",0,0);
    addProp("PointType","DE","Point Type",0,2);
    addProp("LineColor","DE","Line Color",0,0);

    applyComponent(graph);

  }
  
  public void xOnInit()
  {
    panel=element.getFrontPanel();

    panel.setLayout(new BorderLayout());
    graph.setOpaque(false);
    panel.add(graph,BorderLayout.CENTER);
    graph.graph.init();
    element.setAlwaysOnTop(true);
  }



  public void stop()
  {
      inX=null;
      inY=null;
  }


  public void setPropertyEditor()
  {
    super.setPropertyEditor();
  }


  public void setproperties()
  {
     super.setproperties();
     graph.graph.init();
     element.jRepaint();
  }
  
  
  public void propertyChanged(Object o)
  {
    setproperties();
  }

  
  public void loadFromStream(java.io.FileInputStream fis)
  {
    super.loadFromStream(fis);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    super.saveToStream(fos);
  }
  


}
