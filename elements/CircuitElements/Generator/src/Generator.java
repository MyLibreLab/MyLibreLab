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
import java.text.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import VisualLogic.variables.*;


public class Generator extends JVSMain
{
  private VSGroup groupA = new VSGroup();
  private VSGroup groupB = new VSGroup();
  
  private VS1DDouble outXA = null;
  private VS1DDouble outYA = null;
  private VS1DDouble outXB = null;
  private VS1DDouble outYB = null;
  private Image image;
  
  private double oldZoomXValue;
  private double oldZoomYValue;
  private int    oldfreqTyp;
  private double oldOffset;

  
  private VSDouble zoomX;
  private VSDouble zoomY;
  private VSInteger freqTyp; // Sinus

  
  private VSInteger buffLen=new VSInteger(3000);
  private VSDouble  step=new VSDouble(0.1);
  private VSDouble  precision=new VSDouble(10.0);
  private VS1DByte inBytes;

  public Generator()
  {
    outXA= new VS1DDouble(buffLen.getValue());
    outYA= new VS1DDouble(buffLen.getValue());
    
    outXB= new VS1DDouble(buffLen.getValue());
    outYB= new VS1DDouble(buffLen.getValue());
  }

  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }

  public void paint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }

  public void init()
  {
    initPins(0,2,0,2);
    setSize(65,55);
    initPinVisibility(false,true,false,true);

    element.jSetInnerBorderVisibility(true);
    
    setPin(0,ExternalIF.C_GROUP,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_GROUP,element.PIN_OUTPUT);
    setPin(2,ExternalIF.C_ARRAY1D_BYTE,element.PIN_INPUT);
    setPin(3,ExternalIF.C_DOUBLE,element.PIN_INPUT);
    
    String strLocale=Locale.getDefault().toString();


    String fileName=element.jGetSourcePath()+"XGenerator.gif";
    image=element.jLoadImage(fileName);
    
    element.jSetCaptionVisible(true);
    element.jSetCaption("frequenzgenerator");
    setName("Frequenzgenerator");
    
    
    groupA.list.clear();
    groupA.list.add(outXA);
    groupA.list.add(outYA);

    groupB.list.clear();
    groupB.list.add(outXB);
    groupB.list.add(outYB);

    element.setPinOutputReference(0,groupA);
    element.setPinOutputReference(1,groupB);

    groupA.setPin(0);
    groupB.setPin(1);
    
  }

  public void initInputPins()
  {
    inBytes=(VS1DByte)element.getPinInputReference(2);
    zoomX=(VSDouble)element.getPinInputReference(3);
    
    if (inBytes==null)
    {
      inBytes=new VS1DByte(0);
      inBytes.setChanged(false);
    }
    if (zoomX==null)
    {
      zoomX=new VSDouble(1);

    }
  }
  

  

  public void setPropertyEditor()
  {
    element.jAddPEItem("Buffer-Länge",buffLen, 1.0,20000.0);
    element.jAddPEItem("Schritt",step, 0.000000001,10.0);
    element.jAddPEItem("Genauigkeit",precision, 0.000001,100);
    localize();
  }


  private void localize()
  {

  }

  public void propertyChanged(Object o)
  {
    if (o==buffLen)
    {
      outXA= new VS1DDouble(buffLen.getValue());
      outYA= new VS1DDouble(buffLen.getValue());
      groupA.list.clear();
      groupA.list.add(outXA);
      groupA.list.add(outYA);
      element.setPinOutputReference(0,groupA);
      
      outXB= new VS1DDouble(buffLen.getValue());
      outYB= new VS1DDouble(buffLen.getValue());
      groupB.list.clear();
      groupB.list.add(outXB);
      groupB.list.add(outYB);
      element.setPinOutputReference(1,groupB);
    }
    element.jRepaint();
  }



  public void initOutputPins()
  {
    element.setPinOutputReference(0,groupA);
    element.setPinOutputReference(1,groupB);
  }
  

  public void start()
  {

  }

  public void process()
  {
      processor();
  }

  public void processor()
  {
      double x=0;
      double y=0;


      if (inBytes!=null)
      {
        System.out.println("Byte.len="+inBytes.getLength());

        if (inBytes.getLength()>=800)
        {

          for (int i=0;i<outXA.getValue().length;i++)
          {
            if (i<400)
            {
              y=inBytes.getValue(i);
              
              outXA.setValue(i,x*zoomX.getValue());
              outYA.setValue(i,y);
              outXB.setValue(i,x*zoomX.getValue() );
              outYB.setValue(i,-2+inBytes.getValue(400+i));
            }
            x+=step.getValue();
          }
          

          groupA.setChanged(true);
          element.notifyPin(0);
          groupB.setChanged(true);
          element.notifyPin(1);
          }
        /*for (int i=1;i<outXB.getValue().length;i++)
        {
          if (i<inBytes.getLength())
          {
            y=inBytes.getValue((i*2)-1);

            outXB.setValue(i,x);
            outYB.setValue(i,y);
          }
          x+=step.getValue();
        }
        groupB.setChanged(true);
        element.notifyPin(1);*/

      }
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
      buffLen.loadFromStream(fis);
      precision.loadFromStream(fis);
      step.loadFromStream(fis);
      
      outXA= new VS1DDouble(buffLen.getValue());
      outYA= new VS1DDouble(buffLen.getValue());

      groupA.list.clear();
      groupA.list.add(outXA);
      groupA.list.add(outYA);
      
      outXB= new VS1DDouble(buffLen.getValue());
      outYB= new VS1DDouble(buffLen.getValue());

      groupB.list.clear();
      groupB.list.add(outXB);
      groupB.list.add(outYB);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    buffLen.saveToStream(fos);
    precision.saveToStream(fos);
    step.saveToStream(fos);
  }


}

