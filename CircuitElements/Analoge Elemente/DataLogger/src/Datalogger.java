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


public class Datalogger extends JVSMain
{
  private VSGroup group = new VSGroup();
  private VS1DDouble outX = null;
  private VS1DDouble outY = null;
  private Image image;

  private VSDouble inX;
  private VSDouble inY;
  private VSBoolean inStore;
  private VSBoolean inReset;


  private int counter=0;

  public Datalogger()
  {
    outX= new VS1DDouble(1);
    outY= new VS1DDouble(1);
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
    setSize(65,65);
    initPinVisibility(false,true,false,true);

    element.jSetInnerBorderVisibility(true);

    setPin(0,ExternalIF.C_GROUP,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // inX
    setPin(2,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // inY
    setPin(3,ExternalIF.C_BOOLEAN,element.PIN_INPUT); // inStore
    setPin(4,ExternalIF.C_BOOLEAN,element.PIN_INPUT); // inReset


    String strLocale=Locale.getDefault().toString();

    if (strLocale.equalsIgnoreCase("de_DE"))
    {
      element.jSetPinDescription(0,"Out");
      element.jSetPinDescription(1,"inX");
      element.jSetPinDescription(2,"inY");
      element.jSetPinDescription(3,"speichern");
      element.jSetPinDescription(4,"reset");
    }
    if (strLocale.equalsIgnoreCase("en_US"))
    {
      element.jSetPinDescription(0,"Out");
      element.jSetPinDescription(1,"inX");
      element.jSetPinDescription(2,"inY");
      element.jSetPinDescription(3,"store");
      element.jSetPinDescription(4,"reset");
    }
    if (strLocale.equalsIgnoreCase("es_ES"))
    {
      element.jSetPinDescription(0,"Salida");
      element.jSetPinDescription(1,"inX");
      element.jSetPinDescription(2,"inY");
      element.jSetPinDescription(3,"store");
      element.jSetPinDescription(4,"reset");

    }



    String fileName=element.jGetSourcePath()+"icon.png";
    image=element.jLoadImage(fileName);

    element.jSetCaptionVisible(true);
    setName("Datalogger v. 1.0");


    group.list.clear();
    group.list.add(outX);
    group.list.add(outY);

    element.setPinOutputReference(0,group);

    group.setPin(0);

  }

  public void initInputPins()
  {
    inX=(VSDouble)element.getPinInputReference(1);
    inY=(VSDouble)element.getPinInputReference(2);
    inStore=(VSBoolean)element.getPinInputReference(3);
    inReset=(VSBoolean)element.getPinInputReference(4);


    if (inX==null)
    {
      inX=new VSDouble();
      inX.setValue(1.0);
      inX.setChanged(false);
    }
    if (inY==null)
    {
      inY=new VSDouble();
      inY.setValue(1.0);
      inY.setChanged(false);
    }
    if (inStore==null)
    {
      inStore=new VSBoolean();
      inStore.setValue(false);
      inStore.setChanged(false);
    }

    if (inReset==null)
    {
      inReset=new VSBoolean();
      inReset.setValue(false);
      inReset.setChanged(false);
    }

  }




  public void setPropertyEditor()
  {
    //element.jAddPEItem("Buffer-Länge",buffLen, 1.0,20000.0);
    //localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Buffer length");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Longitud de Buffer");
  }

  public void propertyChanged(Object o)
  {
    /*if (o==buffLen)
    {
      outX= new VS1DDouble(1);
      outY= new VS1DDouble(1);
      group.list.clear();
      group.list.add(outX);
      group.list.add(outY);
      element.setPinOutputReference(0,group);
    }
    element.jRepaint();*/
  }



  public void initOutputPins()
  {
    element.setPinOutputReference(0,group);
  }


  private void reset()
  {

    redim(1);
    
    group.setChanged(true);
    element.notifyPin(0);

    counter=0;
  }

  public void start()
  {
    reset();
    process();
  }


  public void redim(int newSize)
  {
   VS1DDouble newX= new VS1DDouble(newSize);
   VS1DDouble newY= new VS1DDouble(newSize);
   
    for (int i=0;i<outX.getLength();i++)
   {
     if (i<newX.getLength())
     {
       newX.setValue(i,outX.getValue(i));
       newY.setValue(i,outY.getValue(i));
     }
   }

   outX=new VS1DDouble(newSize);
   outY=new VS1DDouble(newSize);

   outX.copyValueFrom(newX);
   outY.copyValueFrom(newY);
   

  }

  public void process()
  {
      double x=0;
      double y=0;

      if (inReset.getValue()==true)
      {
        reset();
      }
      
      if (counter>=10000)
      {
        reset();
      }
      
      if (inStore.getValue())
      {
        x=inX.getValue();
        y=inY.getValue();

        redim(counter+1);
        
        //System.out.println("counter="+counter);

        outX.setValue(counter,x);
        outY.setValue(counter,y);
        counter++;
        group.list.clear();
        group.list.add(outX);
        group.list.add(outY);
        element.setPinOutputReference(0,group);


        group.setChanged(true);
        element.notifyPin(0);

      }
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
      //buffLen.loadFromStream(fis);


      /*outX= new VS1DDouble(buffLen.getValue());
      outY= new VS1DDouble(buffLen.getValue());

      group.list.clear();
      group.list.add(outX);
      group.list.add(outY);*/
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    //buffLen.saveToStream(fos);
  }


}

