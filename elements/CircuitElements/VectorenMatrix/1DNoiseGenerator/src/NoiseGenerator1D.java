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
import java.util.*;
import java.util.Random;


public class NoiseGenerator1D extends JVSMain
{
  private Image image;
  private VS1DDouble out = null;
  
  private Random generator = new Random( 19580427 );
    
  private VSDouble step=new VSDouble(1);
  private VSDouble minX=new VSDouble(-500);
  private VSDouble maxX=new VSDouble(+500);



  public NoiseGenerator1D()
  {
    out= new VS1DDouble(0);
  }

  public void xpaint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }
  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }

  public void init()
  {
    initPins(0,1,0,0);
    setSize(20+32+3,32+3);
    initPinVisibility(false,true,false,true);
    element.jSetInnerBorderVisibility(false);

    image=element.jLoadImage(element.jGetSourcePath()+"NoiseGenerator1D.gif");
    
    setPin(0,ExternalIF.C_ARRAY1D_DOUBLE,element.PIN_OUTPUT);   // Out

    String strLocale=Locale.getDefault().toString();

    if (strLocale.equalsIgnoreCase("de_DE"))
    {
      element.jSetPinDescription(0,"1D Array");
    }
    if (strLocale.equalsIgnoreCase("en_US"))
    {
      element.jSetPinDescription(0,"1D Array");
    }
    if (strLocale.equalsIgnoreCase("es_ES"))
    {
      element.jSetPinDescription(0,"1D Array");
    }

    
    element.jSetCaptionVisible(true);
    element.jSetCaption("NoiseGenerator1D");
    setName("NoiseGenerator1D");
    
    element.setPinOutputReference(0,out);

  }


  public void initInputPins()
  {
  }
  

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }
  


  public void setPropertyEditor()
  {
    element.jAddPEItem("Min-X",minX, -99999999.0,99999999.0);
    element.jAddPEItem("Max-X",maxX, -99999999.0,99999999.0);
    element.jAddPEItem("Schritt",step, 0.000000001,1000.0);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Min-X");
    element.jSetPEItemLocale(d+1,language,"Max-X");
    element.jSetPEItemLocale(d+2,language,"Step");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Min-X");
    element.jSetPEItemLocale(d+1,language,"Max-X");
    element.jSetPEItemLocale(d+2,language,"Paso");
  }

  public void propertyChanged(Object o)
  {

    int buflen=(int) ( (Math.abs(minX.getValue())+Math.abs(maxX.getValue())) / step.getValue());

    out= new VS1DDouble(buflen);

    element.jRepaint();
  }

  public void start()
  {
    processor();
  }


  public void processor()
  {
      int c=0;
      double x=-(out.getValue().length/2)*step.getValue();
      for (int i=0;i<out.getValue().length;i++)
      {
        out.setValue(c,generator.nextDouble());
        c++;
        x+=step.getValue();
      }
      out.setChanged(true);
  }


  public void process()
  {
      out.setChanged(false);
      processor();
  }
  
  
  public void loadFromStream(java.io.FileInputStream fis)
  {
      step.loadFromStream(fis);
      minX.loadFromStream(fis);
      maxX.loadFromStream(fis);
      
      propertyChanged(null);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    step.saveToStream(fos);
    minX.saveToStream(fos);
    maxX.saveToStream(fos);
  }
}

