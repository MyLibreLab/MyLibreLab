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
import java.awt.*;
import java.awt.event.*;
import tools.*;
import javax.swing.*;
import java.util.*;
import java.awt.geom.Rectangle2D;


public class Get extends MainFlow
{
  private Image image;
  private VSBasisIF basis;
  private VSFlowInfo in=null;
  private VSFlowInfo out = new VSFlowInfo();
  
  private ExternalIF robotCircuitElement;
  private ExternalIF robotFrontElement;

  private VSString resultVar = new VSString();
  private VSComboBox sensorNr = new VSComboBox();

  public void onDispose()
  {
    robotCircuitElement=null;
    robotFrontElement=null;
  }


  public void paint(java.awt.Graphics g)
  {
     if (element!=null)
     {
        Rectangle bounds=element.jGetBounds();
        Graphics2D g2 = (Graphics2D) g;

        g2.setFont(font);

        int mitteX=bounds.x+(bounds.width)/2;
        int mitteY=bounds.y+(bounds.height)/2;

        int distanceY=10;
        
        g2.setColor(new Color(204,204,255));
        g2.fillRect(bounds.x,mitteY-distanceY,bounds.width,2*distanceY);
        g2.setColor(Color.BLACK);
        g2.drawRect(bounds.x,mitteY-distanceY,bounds.width,2*distanceY);

        String caption=resultVar.getValue()+"=Get("+sensorNr.getItem(sensorNr.selectedIndex)+")";
        variable.setValue(caption);

        FontMetrics fm = g2.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(caption,g2);

        g2.setColor(Color.BLACK);
        g.drawString(caption,mitteX-(int)(r.getWidth()/2),(int)(mitteY+fm.getHeight()/2)-3);
        g.drawImage(image,10,16,null);

     }
     super.paint(g);
  }


  public void init()
  {
    standardWidth=130;
    width=standardWidth;
    height=40;
    toInclude="";
    
    initPins(1,0,1,0);
    setSize(width,height);
    initPinVisibility(true,true,true,true);
    
    element.jSetInnerBorderVisibility(false);

    image=element.jLoadImage(element.jGetSourcePath()+"image.png");
    
    setPin(0,element.C_FLOWINFO,element.PIN_INPUT);
    setPin(1,element.C_FLOWINFO,element.PIN_OUTPUT);
    
    element.jSetResizable(false);
    element.jSetCaptionVisible(false);
    element.jSetCaption("Turn");
    
    setName("#FLOWCHART_GET#");
    
    resultVar.setValue("i");
    
    sensorNr.addItem("position x");
    sensorNr.addItem("position y");
    sensorNr.addItem("Signal Strength");
    sensorNr.addItem("value left sensor");
    sensorNr.addItem("value middle sensor");
    sensorNr.addItem("value right sensor");
  }

  public void xOnInit()
  {
    super.xOnInit();
  }

  public void initInputPins()
  {
    in=(VSFlowInfo)element.getPinInputReference(0);
    basis=element.jGetBasis();
  }

  public void propertyChanged(Object o)
  {

    resizeWidth();
  }

  // Override!
  public void mousePressedOnIdle(MouseEvent e)
  {

  }
  
  public void setPropertyEditor()
  {
    element.jAddPEItem("Resultvar",resultVar, 0,0);
    element.jAddPEItem("SensorNr",sensorNr, 0,2);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Min");
    element.jSetPEItemLocale(d+1,language,"Max");
    element.jSetPEItemLocale(d+2,language,"Step");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Min");
    element.jSetPEItemLocale(d+1,language,"Max");
    element.jSetPEItemLocale(d+2,language,"Paso");
  }
  
  public void start()
  {
    basis=element.jGetBasis();
    robotCircuitElement =element.getElementByhName(basis, "SimpleRobot3D v. 1.0");
    if (robotCircuitElement!=null)
    {
      robotFrontElement= robotCircuitElement.getPanelElement();
    }
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(1,out);
  }


  
  public void process()
  {
    if (robotFrontElement!=null)
    {
      Object obj=in.tags.get("ROBOTNR");

      if (obj instanceof Integer)
      {
        Integer robotNr=(Integer)obj;
        
        VSDouble result = new VSDouble(0);
        
        //liste.clear();
        ArrayList liste = new ArrayList();
        liste.add(new Integer(sensorNr.selectedIndex));
        liste.add(result);

        
        robotFrontElement.jProcessPanel(3, (double)robotNr, liste); // Get Sensor
        
        basis.vsEvaluate(in,""+resultVar.getValue()+"="+result.getValue());
      }
    }

    out.copyValueFrom(in);
    element.notifyPin(1);
  }
  
  public void loadFromStream(java.io.FileInputStream fis)
  {
    resultVar.loadFromStream(fis);
    sensorNr.loadFromStream(fis);
  }


  public void saveToStream(java.io.FileOutputStream fos)
  {
    resultVar.saveToStream(fos);
    sensorNr.saveToStream(fos);
  }
  
}

