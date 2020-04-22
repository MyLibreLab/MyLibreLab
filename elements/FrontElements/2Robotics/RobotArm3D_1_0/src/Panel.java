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

public class Panel extends JVSMain implements PanelIF
{
  private boolean on=false;
  private VSBoolean interpolation = new VSBoolean(false);
  private VSGroup in;
  private ExternalIF circuitElement;
  private Font font=new Font("Monospaced",0,13);

  private double aktuellerWinkel=0;// grad!
  private double stepAngle=10; // grad!

  private CanvasRobot3D panelX=null;
  
  private int posX=0;
  private int posY=0;
  
  //Properties
  private VSBoolean drawLine = new VSBoolean(true);

  private VSColor backColor = new VSColor(Color.WHITE);
  private VSBoolean gridVisible = new VSBoolean(true);
  private VSColor gridColor = new VSColor(Color.LIGHT_GRAY);
  private VSInteger gridDX = new VSInteger(20);
  private VSInteger gridDY = new VSInteger(20);

  private int oldWidth=0;
  private int oldHeight=0;


  public void onDispose()
  {
    JPanel panel=element.getFrontPanel();
    panel.remove(panelX);
  }

  public void processPanel(int pinIndex, double value, Object obj)
  {
    if (panelX!=null)
    {
      if (pinIndex==0 && obj instanceof VSDouble)
      {
         VSDouble tmp = (VSDouble)obj;

         panelX.setBasisAngle(tmp.getValue());
      }else
      if (pinIndex==1 && obj instanceof VSDouble)
      {
         VSDouble tmp = (VSDouble)obj;

         panelX.setZ1Angle(tmp.getValue());
      }else
      if (pinIndex==2 && obj instanceof VSDouble)
      {
         VSDouble tmp = (VSDouble)obj;

         panelX.setZ2Angle(tmp.getValue());
      }else
      if (pinIndex==3 && obj instanceof VSDouble)
      {
         VSDouble tmp = (VSDouble)obj;

         panelX.setZ3Angle(tmp.getValue());
      }else
      if (pinIndex==4 && obj instanceof VSDouble)
      {
         VSDouble tmp = (VSDouble)obj;

         panelX.setHandAngle(tmp.getValue());
      }else
      if (pinIndex==5 && obj instanceof VSBoolean)
      {
         VSBoolean tmp = (VSBoolean)obj;

         panelX.setOpenHand(tmp.getValue());
      }

    }

    
    /*
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
       panelX.handle();
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
       panelX.handle();
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
    
    if (pinIndex==17 && obj instanceof VSImage24)
    {
      VSImage24 vsimg=(VSImage24)obj;

      Image img= Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(vsimg.getWidth(),vsimg.getHeight(),vsimg.getPixels(), 0, vsimg.getWidth()) );
      
      panelX.setRobotImage(img);
      panelX.updateUI();
    }    */

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
    /*panelX.setDrawLine(drawLine.getValue());
    panelX.setBackgroundColor(backColor.getValue());
    panelX.setGridVisible(gridVisible.getValue());
    panelX.setGridColor(gridColor.getValue());
    panelX.setGridDX(gridDX.getValue());
    panelX.setGridDY(gridDY.getValue());
    
    panelX.handle();*/
  }
  
  public void setPropertyEditor()
  {
    /*element.jAddPEItem("Weg zeichnen",drawLine, 0,0);
    
    element.jAddPEItem("Background Color",backColor, 0,0);
    element.jAddPEItem("Grid Visible",gridVisible, 0,0);
    element.jAddPEItem("Grid Color",gridColor, 0,0);
    element.jAddPEItem("Grid DX",gridDX, 2,500);
    element.jAddPEItem("Grid DY",gridDY, 2,500);

    localize();*/
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

    if (panelX!=null) panelX.resetView();

  }
  
  public void stop()
  {

  }
   
  public void init()
  {
    initPins(0,0,0,0);
    initPinVisibility(false,false,false,false);
    setSize(150,150);
    element.jSetInnerBorderVisibility(false);
    element.jSetBorderVisibility(true);

    //element.jSetResizeSynchron(true);
    element.jSetResizable(true);
    setName("RobotArmSim v 1.0");
    
    try
    {
      panelX=new CanvasRobot3D();
    } catch(Exception ex)
    {

    }

    
  }
  

  public void xOnInit()
  {
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

    //propertyChanged(null);
  }
  
  
  public void loadFromStream(java.io.FileInputStream fis)
  {
   /*drawLine.loadFromStream(fis);

    backColor.loadFromStream(fis);
    gridVisible.loadFromStream(fis);
    gridColor.loadFromStream(fis);
    gridDX.loadFromStream(fis);
    gridDY.loadFromStream(fis);*/

  }



  public void saveToStream(java.io.FileOutputStream fos)
  {
   /* drawLine.saveToStream(fos);
    
    backColor.saveToStream(fos);
    gridVisible.saveToStream(fos);
    gridColor.saveToStream(fos);
    gridDX.saveToStream(fos);
    gridDY.saveToStream(fos);*/
  }

}

