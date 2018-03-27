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
  private VSGroup group = new VSGroup();
  private VS1DDouble outX = null;
  private VS1DDouble outY = null;
  private Image image;
  
  private double oldZoomXValue;
  private double oldZoomYValue;
  private int    oldfreqTyp;
  private double oldOffset;

  
  private VSDouble zoomX;
  private VSDouble zoomY;
  private VSInteger freqTyp; // Sinus
  private VSDouble offset; // offset
  
  private VSInteger buffLen=new VSInteger(3000);
  private VSDouble  step=new VSDouble(0.1);
  private VSDouble  precision=new VSDouble(10.0);

  public Generator()
  {
    outX= new VS1DDouble(buffLen.getValue());
    outY= new VS1DDouble(buffLen.getValue());
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
    initPins(0,1,0,4);
    setSize(65,55);
    initPinVisibility(false,true,false,true);

    element.jSetInnerBorderVisibility(true);
    
    setPin(0,ExternalIF.C_GROUP,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // ZoomX
    setPin(2,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // ZoomY
    setPin(3,ExternalIF.C_INTEGER,element.PIN_INPUT); // Frequenttyp
    setPin(4,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // Offset
    
    String strLocale=Locale.getDefault().toString();

    if (strLocale.equalsIgnoreCase("de_DE"))
    {
      element.jSetPinDescription(0,"Out");
      element.jSetPinDescription(1,"ZoomX");
      element.jSetPinDescription(2,"ZoomY");
      element.jSetPinDescription(3,"Freq-Typ");
      element.jSetPinDescription(4,"Offset"); 
    }
    if (strLocale.equalsIgnoreCase("en_US"))
    {
      element.jSetPinDescription(0,"Out");
      element.jSetPinDescription(1,"ZoomX");
      element.jSetPinDescription(2,"ZoomY");
      element.jSetPinDescription(3,"Freq-Type");
      element.jSetPinDescription(4,"Offset");
    }
    if (strLocale.equalsIgnoreCase("es_ES"))
    {
      element.jSetPinDescription(0,"Salida");
      element.jSetPinDescription(1,"ZoomX");
      element.jSetPinDescription(2,"ZoomY");
      element.jSetPinDescription(3,"Tipo Frecuencia");
      element.jSetPinDescription(4,"Offset");
    }


    
    String fileName=element.jGetSourcePath()+"XGenerator.gif";
    image=element.jLoadImage(fileName);
    
    element.jSetCaptionVisible(true);
    element.jSetCaption("frequenzgenerator");
    setName("Frequenzgenerator");
    
    
    group.list.clear();
    group.list.add(outX);
    group.list.add(outY);
    
    element.setPinOutputReference(0,group);

    group.setPin(0);
    
  }

  public void initInputPins()
  {
    zoomX=(VSDouble)element.getPinInputReference(1);
    zoomY=(VSDouble)element.getPinInputReference(2);
    freqTyp=(VSInteger)element.getPinInputReference(3);
    offset=(VSDouble)element.getPinInputReference(4);
    
    if (zoomX==null)
    {
      zoomX=new VSDouble();
      zoomX.setValue(1.0);
      zoomX.setChanged(false);
    }
    if (zoomY==null)
    {
      zoomY=new VSDouble();
      zoomY.setValue(1.0);
      zoomY.setChanged(false);
    }
    
    if (freqTyp==null)
    {
      freqTyp=new VSInteger();
      freqTyp.setValue(0);
      freqTyp.setChanged(false);
    }

    if (offset==null)
    {
      offset=new VSDouble();
      offset.setValue(0);
      offset.setChanged(false);
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
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Buffer length");
    element.jSetPEItemLocale(d+1,language,"Step");
    element.jSetPEItemLocale(d+2,language,"Precision");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Longitud de Buffer");
    element.jSetPEItemLocale(d+1,language,"Paso");
    element.jSetPEItemLocale(d+2,language,"Precision");
  }

  public void propertyChanged(Object o)
  {
    if (o==buffLen)
    {
      outX= new VS1DDouble(buffLen.getValue());
      outY= new VS1DDouble(buffLen.getValue());
      group.list.clear();
      group.list.add(outX);
      group.list.add(outY);
      element.setPinOutputReference(0,group);
    }
    element.jRepaint();
  }



  public void initOutputPins()
  {
    element.setPinOutputReference(0,group);
  }
  
  private double dreieck(double x)
  {
      double summ=0;
      double num=0.0;

      for (int j=1;j<10;j++)
      {
          num=(j*2)-1;
          summ+=Math.pow(-1, j-1)*Math.sin(num*x)/(num*num);
      }
      return (8/Math.pow(Math.PI,2))*summ;
  }

  private double saegezahn(double x)
  {
      double summ=0;

      for (int j=1;j<precision.getValue();j++)
      {
          summ+=Math.sin(j*x)/j;
      }
      return 0.58*summ;
  }
  
  private double rechteck(double x)
  {
      double summ=0;

      for (int j=1;j<precision.getValue();j++)
      {
          summ+=(1/(2.0*j-1.0))* Math.sin( (2.0*j-1.0)*x);
      }
      double U=1;
      return (4.0*U/Math.PI)*summ;
  }


  public void start()
  {
    processor();
  }

  public void process()
  {
    if (zoomX.isChanged() || zoomY.isChanged() || freqTyp.isChanged() || offset.isChanged())
    {
      processor();
    } else
    {
      group.setChanged(false);
    }
  }

  public void processor()
  {
      /*if (zoomX.isChanged()) System.out.println("zoomX.isChanged()");
      if (zoomY.isChanged()) System.out.println("zoomY.isChanged()");
      if (freqTyp.isChanged()) System.out.println("freqTyp.isChanged()");
      if (offset.isChanged()) System.out.println("offset.isChanged()");*/

      double x=0;
      double y=0;

      for (int i=0;i<outX.getValue().length;i++)
      {
        y=0;
        switch(freqTyp.getValue())
        {
          case 0 : y=Math.sin(x*zoomX.getValue()); break;
          case 1 : y=dreieck(x*zoomX.getValue()); break;
          case 2 : y=rechteck(x*zoomX.getValue());break;
          case 3 : y=saegezahn(x*zoomX.getValue());break;
        }

        outX.setValue(i,x);
        outY.setValue(i,offset.getValue()+(y*zoomY.getValue()));
        x+=step.getValue();
      }
      group.setChanged(true);
      element.notifyPin(0);
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
      buffLen.loadFromStream(fis);
      precision.loadFromStream(fis);
      step.loadFromStream(fis);
      
      outX= new VS1DDouble(buffLen.getValue());
      outY= new VS1DDouble(buffLen.getValue());

      group.list.clear();
      group.list.add(outX);
      group.list.add(outY);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    buffLen.saveToStream(fos);
    precision.saveToStream(fos);
    step.saveToStream(fos);
  }


}

