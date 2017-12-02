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
import tools.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.text.*;
import java.awt.geom.Rectangle2D;
import tools.*;
import java.util.*;
//import MyGraph.*;
import MyGraph.*;

public class OscPanel extends VSMainWithPropertyManager implements PanelIF
{ 
  int bufferLen=600;
  private JPanel panel;
  public MyGraph graph= new MyGraph();
  
  private int oldXValues=0;
  private int oldYValues=0;
  private boolean changed=false;
  private double oldPosX=0;

  
  private boolean started=false;
  
  
  private double[] xValues = new double[bufferLen];
  private double[] yValues = new double[bufferLen];
  
  private int counter=0;
  private int counterTimer=0;
  
  public void onDispose()
  {
    JPanel panel=element.getFrontPanel();
    panel.remove(graph);
  }
  

  private void processGraph(double y)
  {

    if (counter<bufferLen)
    {

      for (int i=counter;i<bufferLen;i++)  xValues[i]=counter;
      for (int i=counter;i<bufferLen;i++)  yValues[i]=y;


      graph.graph.graphRenderer[0].xValues=xValues;
      graph.graph.graphRenderer[0].yValues=yValues;
    } else
    {
      System.arraycopy( xValues, 1, xValues, 0, bufferLen-1 );
      System.arraycopy( yValues, 1, yValues, 0, bufferLen-1 );

      xValues[bufferLen-1]=counter;
      yValues[bufferLen-1]=y;

      graph.graph.graphRenderer[0].xValues=xValues;
      graph.graph.graphRenderer[0].yValues=yValues;
    }
    //graph.graph.graphRenderer[0].
   
    counter++;

  }
  

  private void reset()
  {
    graph.graph.back.positionX=0;
    counter=0;
    for (int i=0;i<bufferLen;i++)
    {
      xValues[i]=0;
      yValues[i]=0;
    }
    
    SwingUtilities.invokeLater(new Runnable()
    {
        public void run()
        {
            graph.graph.init();
            graph.updateUI();
        }
    });
  }


  // aus PanelIF
  public void processPanel(int pinIndex, double value, Object obj)
  {
     if (pinIndex==-1)
     {
       reset();
     }else
     if (pinIndex==-2)
     {
       int len=(int)value;
       if (len!=bufferLen)
       {
         bufferLen=len;
         //graph.graph.graphRenderer[0].setAutoScaleInterval(((VSInteger)obj).getValue());
         //graph.graph.graphRenderer[0].
         xValues = new double[bufferLen];
         yValues = new double[bufferLen];
         graph.setMinX(0.0);
         double bufferLenTemp=(double)bufferLen;
         graph.setMaxX(bufferLenTemp);
       }
     }else
     if (pinIndex==-3) // setze Linecolor
     {
       graph.graph.graphRenderer[0].setLineColor(((VSColor)obj).getValue());
     }else
     if (pinIndex==-4) // setze Pointtype
     {
       graph.graph.graphRenderer[0].pointType=((VSInteger)obj).getValue();
       
     }else
     if (pinIndex==-5) // setze Interval
     {
         //graph.graph.graphRenderer..bufferLen=((VSInteger)obj).getValue();
     }
     else
     {
       VSDouble y = (VSDouble)obj;
       processGraph(y.getValue());
     }
     changed=true;
  }

  public void start()
  {
    graph.setAutoScroll(true);
    graph.setAutoZoomX(false);
    
    graph.graph.back.positionX=0;
    graph.graph.init();
    graph.graph.updateUI();

    counter=0;
    counterTimer=0;
    
    changed=true;

    oldPosX=0;

    started=true;
    
    element.jNotifyMeForClock();

  }
  
  public void processClock()
  {
    if (started)
    {
      if (counterTimer>=70)
      {
        counterTimer=0;
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                graph.graph.init();
                graph.updateUI();
            }
        });
      }
      counterTimer++;
    }
  }

   
  public void init()
  {
    initPins(0,0,0,0);
    setSize(450,250);
    element.jSetInnerBorderVisibility(false);
    element.jSetResizable(true);
    setName("Oscilloscope-X/Y_Version 3.0 JV");

    //graph.graph.generateGraphs(2);
    

    graph.setMinX(0.0);
    double bufferLenTemp=(double)bufferLen;
    graph.setMaxX(bufferLenTemp);
    
    
    addProp("BackgroundTransparent","DE","Background Transparent",0,0);
    addProp("GridBackgroundColor","DE","Grid Background Color",0,0);
    addProp("GridLineColor","DE","Grid Line Color",0,0);
    addProp("GridSublineColor","DE","Grid Subline Color",0,0);

    addProp("NullLineVisible","DE","Null Line Visible",0,0);
    addProp("NullLineColor","DE","Null Line Color",0,0);

    addProp("XYAxisFont","DE","x/y Axis Font",0,0);
    addProp("XYAxisFontColor","DE","x/y Axis Font Color",0,0);
    addProp("XYAxisVisible","DE","x/y Axis Visible",0,0);
    addProp("MinX","DE","X-Axis Min",-999999999,999999999);
    addProp("MaxX","DE","X-Axis Max",-999999999,999999999);
    //addProp("AutoZoomX","DE","X-Axis Autozoom",0,0);
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
    //addProp("AutoScroll","DE","Auto scroll",0,0);
    addProp("CoordinatesVisible","DE","Auto Show Coordinates",0,0);
    //addProp("PointType","DE","Point Type",0,2);
    //addProp("LineColor","DE","Line Color",0,0);
    //addProp("AutoZoomX","DE","X-Axis Autozoom",0,0);

    applyComponent(graph);
    
    if (graph.graph.graphRenderer.length==0) graph.graph.generateGraphs(1);
    

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
    started=false;
    //if (timer!=null) timer.stop();
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
