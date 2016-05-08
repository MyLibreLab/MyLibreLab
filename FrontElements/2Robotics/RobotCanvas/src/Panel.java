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
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import tools.*;
import VisualLogic.variables.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.*;
import java.util.*;
import java.awt.image.MemoryImageSource;
import RobotSimulator1.*;


public class Panel extends JVSMain implements PanelIF, RobotSensorIF
{
  private boolean on=false;
  private VSBoolean interpolation = new VSBoolean(false);
  private VSGroup in;
  private ExternalIF circuitElement;

  private double aktuellerWinkel=0;// grad!
  private double stepAngle=10; // grad!

  private PanelSimul panelX;
  
  private int posX=0;
  private int posY=0;
  
  //Properties
  private VSBoolean drawLine = new VSBoolean(true);


  public void onDispose()
  {
  }

  public void processPanel(int pinIndex, double value, Object obj)
  {
    if (pinIndex==0 && obj instanceof VSBoolean)
    {
       VSBoolean tmp = (VSBoolean)obj;
       if (tmp.getValue()) panelX.goForward();
    }
    if (pinIndex==1 && obj instanceof VSBoolean)
    {
       VSBoolean tmp = (VSBoolean)obj;
       if (tmp.getValue()) panelX.goBack();
    }
    if (pinIndex==2 && obj instanceof VSBoolean)
    {
       VSBoolean tmp = (VSBoolean)obj;
       if (tmp.getValue()) panelX.stop();
    }
    if (pinIndex==3 && obj instanceof VSDouble)
    {
       VSDouble tmp = (VSDouble)obj;
       panelX.setAngle(tmp.getValue());
       aktuellerWinkel=tmp.getValue();
    }
    
    if (pinIndex==4 && obj instanceof VSBoolean)
    {
       VSBoolean tmp = (VSBoolean)obj;
       if (tmp.getValue())
       {
         aktuellerWinkel+=stepAngle;
         panelX.setAngle(aktuellerWinkel);
       }
    }
    if (pinIndex==5 && obj instanceof VSBoolean)
    {
       VSBoolean tmp = (VSBoolean)obj;
       if (tmp.getValue())
       {
         aktuellerWinkel-=stepAngle;
         panelX.setAngle(aktuellerWinkel);
       }

    }
    if (pinIndex==6 && obj instanceof VSDouble)
    {
       VSDouble tmp = (VSDouble)obj;
       stepAngle=tmp.getValue();
    }


    if (pinIndex==7 && obj instanceof VSInteger)
    {
       VSInteger tmp = (VSInteger)obj;
       posX=tmp.getValue();
    }
    if (pinIndex==8 && obj instanceof VSInteger)
    {
       VSInteger tmp = (VSInteger)obj;
       posY=tmp.getValue();
    }
    
    if (pinIndex==9 && obj instanceof VSBoolean)
    {
       VSBoolean tmp = (VSBoolean)obj;
       if (tmp.getValue()) panelX.gotoCoordinated(posX,posY);
    }

    if (pinIndex==10 && obj instanceof VSBoolean)
    {
       VSBoolean tmp = (VSBoolean)obj;
       if (tmp.getValue()) panelX.setPosition(posX,posY);
    }

    if (pinIndex==11 && obj instanceof VSBoolean)
    {
       VSBoolean tmp = (VSBoolean)obj;
       panelX.setGreiferStatus(tmp.getValue());
    }
    if (pinIndex==12 && obj instanceof VSGroup)
    {
       VSGroup group = (VSGroup)obj;
       
       panelX.clearImages();
       for (int i=0;i<group.list.size();i++)
       {
         VSObject o = (VSObject)group.list.get(i);
         
         if (o instanceof VSGroup)
         {
           VSGroup tmp =(VSGroup)o;
           if (tmp.list.size()==3)
           {
             Image imgX=null;
             int pX=0;
             int pY=0;

             if (tmp.list.get(0) instanceof VSImage24)
             {
                VSImage24 vsimg=(VSImage24)tmp.list.get(0);
                
                imgX= Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(vsimg.getWidth(),vsimg.getHeight(),vsimg.getPixels(), 0, vsimg.getWidth()) );

             }
             if (tmp.list.get(1) instanceof VSInteger)
             {
                VSInteger posX=(VSInteger)tmp.list.get(1);
                pX=posX.getValue();
             }
             if (tmp.list.get(2) instanceof VSInteger)
             {
                VSInteger posY=(VSInteger)tmp.list.get(2);
                pY=posY.getValue();
             }
             
             panelX.addImage(imgX,pX,pY);
           }
         }
       }
    }
    
    if (pinIndex==13 && obj instanceof VSGroup)
    {
       VSGroup group = (VSGroup)obj;

       panelX.clearPickableObjects();
       
       for (int i=0;i<group.list.size();i++)
       {
         VSObject o = (VSObject)group.list.get(i);

         if (o instanceof VSGroup)
         {
           VSGroup tmp =(VSGroup)o;
           if (tmp.list.size()==2)
           {
             Image imgX=null;
             int pX=0;
             int pY=0;

             if (tmp.list.get(0) instanceof VSInteger)
             {
                VSInteger posX=(VSInteger)tmp.list.get(0);
                pX=posX.getValue();
             }
             if (tmp.list.get(1) instanceof VSInteger)
             {
                VSInteger posY=(VSInteger)tmp.list.get(1);
                pY=posY.getValue();
             }

             panelX.addPickableObject(pX,pY);
           }
         }
       }
    }
    
    if (pinIndex==14 && obj instanceof VSBoolean)
    {
      VSBoolean tmp=(VSBoolean)obj;
      panelX.setRecordWay(tmp.getValue());
    }
    if (pinIndex==15 && obj instanceof VSInteger)
    {
      VSInteger tmp=(VSInteger)obj;
      panelX.setDelay(tmp.getValue());
    }
    if (pinIndex==16 && obj instanceof VSBoolean)
    {
      VSBoolean tmp=(VSBoolean)obj;
      
      if (tmp.getValue())
      {
        panelX.clearWeg();
        
      }
      //panelX.setDelay(tmp.getValue());
    }
  }
  

   public void paint(java.awt.Graphics g)
   {

   }
   

  public void robotSensor0Changed(int distance)
  {
     if (circuitElement!=null) circuitElement.Change(0,new VSInteger(distance));
  }

  public void robotSensor1Changed(int distance)
  {
    if (circuitElement!=null) circuitElement.Change(1,new VSInteger(distance));
  }

  public void robotSensor2Changed(int distance)
  {
    if (circuitElement!=null) circuitElement.Change(2,new VSInteger(distance));
  }


  public void robotPositionChanged(int x, int y, double alpha)
  {
    if (circuitElement!=null) circuitElement.Change(3,new VSInteger(x));
    if (circuitElement!=null) circuitElement.Change(4,new VSInteger(y));
    if (circuitElement!=null) circuitElement.Change(5,new VSDouble(alpha));
    if (circuitElement!=null) circuitElement.Change(6,new VSBoolean(true));
  }



  public void propertyChanged(Object o)
  {
     if (o ==drawLine)
     {
      panelX.setDrawLine(drawLine.getValue());
     }
  }
  
  public void setPropertyEditor()
  {
    element.jAddPEItem("Weg zeichnen",drawLine, 0,0);
    localize();
  }

  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Draw line");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Draw line");
  }



  public void start()
  {
    circuitElement=element.getCircuitElement();
    stepAngle=10;
    aktuellerWinkel=0;
    panelX.setDrawLine(drawLine.getValue());
    panelX.stop();
    if (panelX!=null) panelX.startProcess();
  }
  
  public void stop()
  {
    if (panelX!=null) panelX.endProcess();
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
  
  public void xOnInit()
  {

    JPanel panel=element.getFrontPanel();

    panelX=new PanelSimul(this);
    panel.setLayout(new BorderLayout());
    //graph.setOpaque(false);
    panel.add(panelX,BorderLayout.CENTER);
    //graph.graph.init();
    element.setAlwaysOnTop(true);

  }
  
  
  public void loadFromStream(java.io.FileInputStream fis)
  {
    drawLine.loadFromStream(fis);
  }



  public void saveToStream(java.io.FileOutputStream fos)
  {
    drawLine.saveToStream(fos);
  }



}

