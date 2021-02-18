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

import tools.*;
import VisualLogic.variables.*;


public class Oscilloscope extends JVSMain
{
  private ExternalIF panelElement=null;
  private Image image;

  public VSDouble  inA=null;
  public VSBoolean inB=null;
  public VSBoolean inC=null;
  public VSBoolean inD=null;
  
  //public VSColor   color=new VSColor(Color.WHITE);
  public VSColor   color=new VSColor(new Color(255, 153, 0));
  
  public VSComboBox pointTypeList=new VSComboBox();
  public VSInteger pointType = new VSInteger(4); 
  
  public VSInteger delay = new VSInteger(10); // 100 Samples each Second
  public VSInteger bufferLen = new VSInteger(600); // 600 Samples Buffer (Last 1 minute)

  long time1=0;
  long time2=0;
  boolean started=false;

  public void onDispose()
  {
   image.flush();
   image=null;
  }


  public void setPropertyEditor()
  {
    element.jAddPEItem("Delay",delay, 0,5000000);
    element.jAddPEItem("Buffer",bufferLen, 0,50000000);
    element.jAddPEItem("Line Color",color, 0,50000000);
    
    pointTypeList.addItem("RECT_T1");
    pointTypeList.addItem("LINE_T1");
    pointTypeList.addItem("LINE_T2");
    pointTypeList.addItem("LINE_T3");
    pointTypeList.addItem("LINE_T4");
    pointTypeList.addItem("OVAL_T0");
    pointTypeList.addItem("OVAL_T1");
    pointTypeList.addItem("OVAL_T2");
    pointTypeList.addItem("BARS_T1");
    pointTypeList.addItem("BARS_T2");
    pointTypeList.addItem("BARS_T3");
    pointTypeList.addItem("IBAR_T1");
    pointTypeList.addItem("IBAR_T2");
    pointTypeList.addItem("IBAR_T3");
    
    pointTypeList.setPinIndex(4);
    pointTypeList.setPin(pointType.getValue());
    
    
    element.jAddPEItem("Pointtype",pointTypeList, 0,20);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Delay");
    element.jSetPEItemLocale(d+1,language,"Buffer");
    element.jSetPEItemLocale(d+2,language,"Line Color");
    element.jSetPEItemLocale(d+3,language,"Pointtype");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Delay");
    element.jSetPEItemLocale(d+1,language,"Buffer");
    element.jSetPEItemLocale(d+2,language,"Line Color");
    element.jSetPEItemLocale(d+3,language,"Pointtype");
  }

  public Oscilloscope()
  {
    super();
  }
  
  public void paint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }

  public void init()
  {
    initPins(0,0,0,4);
    setSize(40,50);
    initPinVisibility(false,false,false,true);

    setPin(0,ExternalIF.C_DOUBLE,element.PIN_INPUT);
    setPin(1,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(2,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(3,ExternalIF.C_BOOLEAN,element.PIN_INPUT);


    element.jSetPinDescription(0,"Y-Value");
    element.jSetPinDescription(1,"Start");
    element.jSetPinDescription(2,"Stop");
    element.jSetPinDescription(3,"Reset");
    
    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    

    element.jSetCaptionVisible(true);
    element.jSetCaption("Time Graph X JV");

    setName("Time Graph X JV");
  }


  public void initInputPins()
  {
    inA=(VSDouble)element.getPinInputReference(0);
    inB=(VSBoolean)element.getPinInputReference(1);
    inC=(VSBoolean)element.getPinInputReference(2);
    inD=(VSBoolean)element.getPinInputReference(3);

    if (inA==null) inA=new VSDouble(0);
    if (inB==null) inB=new VSBoolean(false);
    if (inC==null) inC=new VSBoolean(false);
    if (inD==null) inD=new VSBoolean(false);


  }

  public void process()
  {
    if (inB.getValue()) started=true;
    if (inC.getValue()) started=false;
    if (inD.getValue()) reset();
  }
  
  private void reset()
  {
    if (panelElement!=null) panelElement.jProcessPanel(-1,0,null);
  }
  
  public void processClock()
  {

    if (started)
    {
      time2 = System.nanoTime();
      long diff=time2-time1;
      if (diff>=delay.getValue()*1000000)
      {
        time1 = System.nanoTime();
        if (panelElement!=null)
        {
          panelElement.jProcessPanel(0,0,inA);
        }
      }
    }
  }

  
  public void start()
  {
    panelElement=element.getPanelElement();

    

    // Setze BufferLen in FrontPanel!
    if (panelElement!=null) panelElement.jProcessPanel(-2,bufferLen.getValue(),null);

    // Setze Line Color in FrontPanel!
    if (panelElement!=null) panelElement.jProcessPanel(-3,0,color);
    
    // Setze Pointtype in FrontPanel!
    
    if (panelElement!=null){
        pointType=new VSInteger(pointTypeList.selectedIndex);
        panelElement.jProcessPanel(-4,0,pointType);
        //System.out.println("PinIndex="+pointTypeList.selectedIndex);
    }
    
    // Setze Interval in FrontPanel!
    if (panelElement!=null) panelElement.jProcessPanel(-5,0,bufferLen);

    // Reset im FrontPanel
    reset();
    
    element.jNotifyMeForClock();
    started=false;
    if (panelElement!=null) panelElement.jRepaint();
  }
  
  private void setBufferLen(int len)
  {

  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
    delay.loadFromStream(fis);
    bufferLen.loadFromStream(fis);
    
    color.loadFromStream(fis);
    pointType.loadFromStream(fis);
    pointTypeList.loadFromStream(fis);

  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    delay.saveToStream(fos);
    bufferLen.saveToStream(fos);
    
    color.saveToStream(fos);
    pointType.saveToStream(fos);
    pointTypeList.saveToStream(fos);

  }
    



}
