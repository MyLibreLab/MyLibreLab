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
  private VS1DDouble bufferOutX = null;
  private VS1DDouble bufferOutY = null;
  private Image image;
  
  private int oldSamplesX =0;
  private double oldAmplitudeY=0.0;
  private int    oldfreqValue=0;
  private double oldOffset=0.0;

  
  private VSInteger samplesExt = new VSInteger(100);
  private VSDouble amplitudeY = new VSDouble(1.0);
  private VSInteger freqValue = new VSInteger(1); // 1 Hz
  private VSDouble offset = new VSDouble(0.0); // offset
  
  //private VSInteger buffLen=new VSInteger(3000);
  //private VSDouble  step=new VSDouble(0.1);
  //private VSDouble  precision=new VSDouble(10.0);

  public Generator()
  {
    
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
    setPin(1,ExternalIF.C_INTEGER,element.PIN_INPUT);  // Samples X
    setPin(2,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // Amplitude Y
    setPin(3,ExternalIF.C_INTEGER,element.PIN_INPUT); // Freq
    setPin(4,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // Offset Y
    
    String strLocale=Locale.getDefault().toString();

    if (strLocale.equalsIgnoreCase("de_DE"))
    {
      element.jSetPinDescription(0,"Out");
      element.jSetPinDescription(1,"Samples X");
      element.jSetPinDescription(2,"Amplitude Y");
      element.jSetPinDescription(3,"Freq (Hz)");
      element.jSetPinDescription(4,"Offset Y"); 
    }
    if (strLocale.equalsIgnoreCase("en_US"))
    {
      element.jSetPinDescription(0,"Out");
      element.jSetPinDescription(1,"Samples X");
      element.jSetPinDescription(2,"Amplitude Y");
      element.jSetPinDescription(3,"Freq (Hz)");
      element.jSetPinDescription(4,"Offset Y");
    }
    if (strLocale.equalsIgnoreCase("es_ES"))
    {
      element.jSetPinDescription(0,"Salida");
      element.jSetPinDescription(1,"Muestras X");
      element.jSetPinDescription(2,"Amplitud Y");
      element.jSetPinDescription(3,"Frecuencia (Hz)");
      element.jSetPinDescription(4,"Desfase Y");
    }


    
    String fileName=element.jGetSourcePath()+"XGenerator.gif";
    image=element.jLoadImage(fileName);
    
    element.jSetCaptionVisible(true);
    element.jSetCaption("Freq Generator JV");
    setName("Freq Generator JV");
    
    bufferOutX= new VS1DDouble(samplesExt.getValue());
    bufferOutY= new VS1DDouble(samplesExt.getValue());
    group.list.clear();
    
    group.list.add(bufferOutX);
    group.list.add(bufferOutY);
    
    element.setPinOutputReference(0,group);

    group.setPin(0);
    
    
  }

  public void initInputPins()
  {
    samplesExt=(VSInteger)element.getPinInputReference(1);
    amplitudeY=(VSDouble)element.getPinInputReference(2);
    freqValue=(VSInteger)element.getPinInputReference(3);
    offset=(VSDouble)element.getPinInputReference(4);
    
    if (samplesExt==null)
    {
      samplesExt=new VSInteger(100);
      samplesExt.setChanged(false);
    }
    if (amplitudeY==null)
    {
      amplitudeY=new VSDouble();
      amplitudeY.setValue(1.0);
      amplitudeY.setChanged(false);
    }
    
    if (freqValue==null)
    {
      freqValue=new VSInteger(1);
     
      freqValue.setChanged(false);
    }

    if (offset==null)
    {
      offset=new VSDouble(0.0);
      offset.setChanged(false);
    }
    
  }
  

  

  public void setPropertyEditor()
  {
    //element.jAddPEItem("Samples X",buffLen, 1.0,20000.0);
    //element.jAddPEItem("Schritt",step, 0.000000001,10.0);
    //element.jAddPEItem("Genauigkeit",precision, 0.000001,100);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    //element.jSetPEItemLocale(d+0,language,"Samples");
    //element.jSetPEItemLocale(d+1,language,"Step");
    //element.jSetPEItemLocale(d+2,language,"Precision");

    language="es_ES";

    //element.jSetPEItemLocale(d+0,language,"Longitud de Buffer");
    //element.jSetPEItemLocale(d+1,language,"Paso");
    //element.jSetPEItemLocale(d+2,language,"Precision");
  }

  public void propertyChanged(Object o)
  {
//    if (o==buffLen)
//    {
//      bufferOutX= new VS1DDouble(samplesExt.getValue());
//      bufferOutY= new VS1DDouble(samplesExt.getValue());
//      group.list.clear();
//      group.list.add(bufferOutX);
//      group.list.add(bufferOutY);
//      element.setPinOutputReference(0,group);
//    }
    //element.jRepaint();
  }



  public void initOutputPins()
  {
    element.setPinOutputReference(0,group);
  }
  
  
  public void start()
  { 
    
    processor();
      
  }

  public void process()
  { //System.err.println("PROCESS"+samplesExt.getValue()+amplitudeY.getValue()+freqValue.getValue()+offset.getValue());
    if (samplesExt.getValue()!=oldSamplesX || amplitudeY.getValue()!=oldAmplitudeY || freqValue.getValue()!=oldfreqValue || offset.getValue()!=oldOffset)
    {
      processor();
      //System.err.println("UPDATED");
      oldSamplesX=samplesExt.getValue();
      oldAmplitudeY=amplitudeY.getValue();
      oldfreqValue=freqValue.getValue();
      oldOffset=offset.getValue();
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
      
    if (samplesExt!=null && amplitudeY!=null && freqValue!=null && offset!=null)
    { //System.err.println("PROCESSED");
      bufferOutX= new VS1DDouble(samplesExt.getValue());
      bufferOutY= new VS1DDouble(samplesExt.getValue());
      group.list.clear();
    
      group.list.add(bufferOutX);
      group.list.add(bufferOutY);
      double x=0.0;
      double y=0.0;
      
      double freqTemp= freqValue.getValue();
      //1 Hz = 2π rad/s = 6.2831853 rad/s
      double xIncrement=((1.0)/(samplesExt.getValue()));

      for (double i=0.0;i<bufferOutX.getValue().length;i+=1.0)
      {
        y=0.0;
        y=amplitudeY.getValue()*(Math.sin(2*Math.PI*freqTemp*x));
        //switch(freqValue.getValue())
        //{
          //case 0 : y=Math.sin(x*zoomX.getValue()); break;
          //case 1 : y=dreieck(x*zoomX.getValue()); break;
          //case 2 : y=rechteck(x*zoomX.getValue());break;
          //case 3 : y=saegezahn(x*zoomX.getValue());break;
        //}

        bufferOutX.setValue((int)i,x);
        bufferOutY.setValue((int)i,offset.getValue()+y);
       
        //System.err.println("X="+x+" Y="+y+" Freq="+freqTemp+" offset=" +offset+ " Inc="+xIncrement+" Amp Y="+amplitudeY);
        
        x+=xIncrement;
          
      }
      group.setChanged(true);
      element.notifyPin(0);
    }
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
      //buffLen.loadFromStream(fis);
      //precision.loadFromStream(fis);
      //step.loadFromStream(fis);
      
      //bufferOutX= new VS1DDouble(samplesExt.getValue());
      //bufferOutY= new VS1DDouble(samplesExt.getValue());

      //group.list.clear();
      //group.list.add(bufferOutX);
      //group.list.add(bufferOutY);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    //buffLen.saveToStream(fos);
    //precision.saveToStream(fos);
    //step.saveToStream(fos);
  }


}

