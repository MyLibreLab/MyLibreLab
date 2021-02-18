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
import java3drobot.*;
import java.awt.*;


class FrameRobot extends javax.swing.JDialog
{
  public CanvasRobot3D robot;
  public FrameRobot(CanvasRobot3D robot)
  {
    this.robot=robot;
    this.setLayout(new BorderLayout());
    this.getContentPane().add(robot);
    
    setResizable(true);
    this.setSize(500,500);
    this.setVisible(true);
  }
}


public class Panel extends JVSMain implements PanelIF
{
  private boolean on=false;
  private VSBoolean interpolation = new VSBoolean(false);
  private VSGroup in;
  private VSBasisIF basis;
  private ExternalIF circuitElement;
  private Font font=new Font("Monospaced",0,13);

  private double aktuellerWinkel=0;// grad!
  private double stepAngle=10; // grad!

  private CanvasRobot3D panelX=null;
  private FrameRobot frm=null;
  
  private int posX=0;
  private int posY=0;
  
  //Properties
  private VSBoolean drawLine = new VSBoolean(true);

  private VSColor backColor = new VSColor(Color.WHITE);
  private VSBoolean gridVisible = new VSBoolean(true);
  private VSColor gridColor = new VSColor(Color.LIGHT_GRAY);
  //private VSInteger gridDX = new VSInteger(20);
  //private VSInteger gridDY = new VSInteger(20);

  private int oldWidth=0;
  private int oldHeight=0;


  public void onDispose()
  {
   if (frm!=null)
    {
      frm.robot=null;
      frm.dispose();
      frm=null;
    }

    panelX=null;
  }

  public void processPanel(int pinIndex, double value, Object obj)
  {
  
    if (panelX!=null)
    {

      
      if (pinIndex==-1) // Create new Robot
      {
        if (obj instanceof VSInteger)
        {
          VSInteger tmpX = (VSInteger)obj;

          if (panelX.robotExist((int)value))
          {
            tmpX.setValue(-1);
          }else
          {
            panelX.createNewRobot((int)value);
            tmpX.setValue((int)value);
          }

        }
      }else
      if (pinIndex==0) // Reinit
      {
        panelX.reinit();
      }else
      if (pinIndex==1 && obj instanceof Double) // Go Forward /Back
      {
        Double tmp = (Double)obj;
        int robotNr=(int)value;
        panelX.setBasisPos(robotNr,tmp.doubleValue(), 0, 0, 0);
      }else
      if (pinIndex==2 && obj instanceof Double)  // Turn Left / Right
      {
        Double tmp = (Double)obj;
        int robotNr=(int)value;
        panelX.setBasisPos(robotNr, 0, 0, 0, tmp.doubleValue());
        
        //System.out.println("-Turn : Nr"+robotNr);
      }else
      if (pinIndex==3 && obj instanceof ArrayList )  // Get Robot Info
      {
        ArrayList liste = (ArrayList)obj;
        if (liste.size()==2)
        {
          Integer getNr=(Integer)liste.get(0);
          VSDouble result=(VSDouble)liste.get(1);
          
          //Double tmp = (Double)obj;
          int robotNr=(int)value;

          double res=0;

          switch(getNr)
          {
            case 0 : res=panelX.getPositionX(robotNr);break;
            case 1 : res=panelX.getPositionY(robotNr);break;
            case 2 : res=panelX.getNearstPickableObjectDistance(robotNr);break;
            case 3 : res=panelX.getSensorValue(robotNr, 1); break;
            case 4 : res=panelX.getSensorValue(robotNr, 2); break;
            case 5 : res=panelX.getSensorValue(robotNr, 3); break;
          }

          result.setValue(res);
        }


      }
    }
  }
  

   public void paint(java.awt.Graphics g)
   {
     if (element!=null && panelX!=null)
     {
        Rectangle rec=element.jGetBounds();
        
        if (rec.width>50 | rec.height>50)
        {

          if (rec.width!=oldWidth || rec.height!=oldHeight)
          {
            panelX.setLocation(10,10);
            panelX.setSize(rec.width-10-10,rec.height-10-10);

            oldWidth=rec.width;
            oldHeight=rec.height;
          }
        }  else
        {
          g.setColor(Color.BLACK);
          g.setFont(font);
          g.drawString("Resize", 7,30);
          panelX.setSize(0,0);
        }
     }else
     {
        g.setColor(Color.BLACK);
        g.setFont(font);
        g.drawString("Please install Java 3D", 7,30);
        g.drawString("from https://java3d.dev.java.net/binary-builds.html", 7,45);
        g.drawString("!* Attention: if you have updated your Java Version ", 7,60);
        g.drawString("!* you have to reinstall java 3d.", 7,75);
     }
   }
   


  public void propertyChanged(Object o)
  {

  }
  
  public void setPropertyEditor()
  {

  }

  private void localize()
  {
  
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Draw path");
    element.jSetPEItemLocale(d+1,language,"Background Color");
    element.jSetPEItemLocale(d+2,language,"Grid Visible");
    element.jSetPEItemLocale(d+3,language,"Grid Color");
    element.jSetPEItemLocale(d+4,language,"Grid DX");
    element.jSetPEItemLocale(d+5,language,"Grid DY");
    

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Draw path");
    element.jSetPEItemLocale(d+1,language,"Background Color");
    element.jSetPEItemLocale(d+2,language,"Grid Visible");
    element.jSetPEItemLocale(d+3,language,"Grid Color");
    element.jSetPEItemLocale(d+4,language,"Grid DX");
    element.jSetPEItemLocale(d+5,language,"Grid DY");
    
  }



  public void start()
  {
    circuitElement=element.getCircuitElement();
    stepAngle=10;
    aktuellerWinkel=0;

    if (panelX!=null) panelX.reinit();
  }
  
  public void stop()
  {

  }
   
  public void init()
  {
    initPins(0,0,0,0);
    initPinVisibility(false,false,false,false);
    setSize(300,180);
    element.jSetInnerBorderVisibility(false);
    element.jSetBorderVisibility(true);

    element.jSetResizable(true);

    try
    {
      panelX=new CanvasRobot3D("");

      //frm = new FrameRobot(panelX);
    } catch(Exception ex)
    {
    }
    JPanel panel=element.getFrontPanel();

    panel.setLayout(null);
    //graph.setOpaque(false);
    if (panelX!=null)
    {
      panel.add(panelX);
      panelX.setLocation(30,30);
      panelX.setSize(0,0);
    }
    //graph.graph.init();
    element.setAlwaysOnTop(true);

    setName("SimpleRobot 3D v 1.0");
  }
  

  
  
  public void loadFromStream(java.io.FileInputStream fis)
  {

  }



  public void saveToStream(java.io.FileOutputStream fos)
  {

  }

}

