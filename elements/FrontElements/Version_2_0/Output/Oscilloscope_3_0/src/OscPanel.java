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

  public VSGroup inA=null;
  public VSGroup inB=null;
  public VSGroup inC=null;
  public VSDouble inD=null;
  
  
  


  // aus PanelIF
  public void processPanel(int pinIndex, double value, Object obj)
  {
    //if (obj instanceof VSGroup)
    switch (pinIndex)
    {
      case 0 : inA= (VSGroup)obj; break;
      case 1 : inB= (VSGroup)obj; break;
      case 2 : inC= (VSGroup)obj; break;
      case 3 : inD= (VSDouble)obj; break;
    }

     if (inA instanceof VSGroup)
     {

        if (graph.graph.graphRenderer==null || graph.graph.graphRenderer.length!=inA.list.size())
        {
          graph.graph.generateGraphs(inA.list.size());
        }
     }

     if (inA instanceof VSGroup)
     {

        for (int k=0;k<inA.list.size();k++)
        {
           VSObject ox = (VSObject)inA.list.get(k);

           if (ox instanceof VSGroup)
           {
               VSGroup o =(VSGroup)ox;
               if (o.list.size()==2)
               {
                  VSObject obj1 = (VSObject)o.list.get(0);
                  VSObject obj2 = (VSObject)o.list.get(1);

                  if (obj1 instanceof VS1DDouble && obj2 instanceof VS1DDouble)
                  {
                    VS1DDouble oo1=(VS1DDouble)obj1;
                    VS1DDouble oo2=(VS1DDouble)obj2;

                    graph.graph.graphRenderer[k].xValues=oo1.getValue();
                    graph.graph.graphRenderer[k].yValues=oo2.getValue();
                  }
               } else
               {
                  graph.graph.graphRenderer[k].xValues=new double[0];
                  graph.graph.graphRenderer[k].yValues=new double[0];
               }

           } else
           {
             //processX(g,x,y,mitteX,mitteY,(VSGroup)in);
           }
        }

     }
     
     if (inB instanceof VSGroup)
     {
        for (int k=0;k<inB.list.size();k++)
        {
           VSObject ox = (VSObject)inB.list.get(k);

           if (ox instanceof VSColor)
           {
              VSColor o =(VSColor)ox;

              graph.graph.graphRenderer[k].setLineColor(o.getValue());
           } else
           {
              graph.graph.graphRenderer[k].setLineColor(Color.WHITE);
           }
        }
     } else
     {
      for (int k=0;k<graph.graph.graphRenderer.length;k++)
      {
        graph.graph.graphRenderer[k].setLineColor(Color.WHITE);
      }
     }

     if (inC instanceof VSGroup)
     {
        for (int k=0;k<inC.list.size();k++)
        {
           VSObject ox = (VSObject)inC.list.get(k);

           if (ox instanceof VSInteger)
           {
              VSInteger o =(VSInteger)ox;

              graph.graph.graphRenderer[k].pointType=o.getValue();
           } else
           {
              graph.graph.graphRenderer[k].pointType=0;
           }
        }
     } else
     {
      for (int k=0;k<graph.graph.graphRenderer.length;k++)
      {
        graph.graph.graphRenderer[k].pointType=0;
      }
     }

     if (inD instanceof VSDouble)
     {
       //System.out.println("XADASKDFHJKAGHLKDGHKLADADD");
       VSDouble o = (VSDouble)inD;
       graph.graph.back.positionX=o.getValue();
       graph.graph.init();
       graph.graph.updateUI();
     }


   
     graph.graph.init();
     element.jRepaint();

     
  }

   
  public void init()
  {
    initPins(0,0,0,0);
    setSize(250,250);
    element.jSetInnerBorderVisibility(false);
    element.jSetResizable(true);
    setName("Oscilloscope-X/Y_Version 2.0");


    //graph.graph.generateGraphs(2);
    
    //ddProp("BackgroundTransparent","DE","Background Transparent",0,0);
    //addProp("GridBackgroundColor","DE","Grid Background Color",0,0);
    //addProp("GridLineColor","DE","Grid Line Color",0,0);
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
    //addProp("PointType","DE","Point Type",0,2);
    //addProp("LineColor","DE","Line Color",0,0);

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
      inA=null;
      inB=null;
      inC=null;
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
