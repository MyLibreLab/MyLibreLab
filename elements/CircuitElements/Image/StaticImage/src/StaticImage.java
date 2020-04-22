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
import javax.swing.Timer;
import javax.swing.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import java.io.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;


import java.util.*;

public class StaticImage extends JVSMain
{
  private Image icon=null;
  
  private VSImage outImage = new VSImage();
  private VSInteger outWidth= new VSInteger();
  private VSInteger outHeight= new VSInteger();
  private VSImage24 out=new VSImage24(1,1);
  
  private VSFile file = new VSFile("");
  private byte imageBytes[]=null;
  

  public void paint(java.awt.Graphics g)
  {
    drawImageCentred(g,icon);
  }
   
  public void init()
  {
    initPins(0,3,0,0);
    setSize(55,50);
    initPinVisibility(false,true,false,false);
    element.jSetResizable(false);
    element.jSetInnerBorderVisibility(false);


    setPin(0,ExternalIF.C_IMAGE,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
    setPin(2,ExternalIF.C_INTEGER,element.PIN_OUTPUT);


   String strLocale=Locale.getDefault().toString();

    /*if (strLocale.equalsIgnoreCase("de_DE"))
    {
      element.jSetPinDescription(0,"Out Color");
      element.jSetPinDescription(1,"In Rot");
      element.jSetPinDescription(2,"In Grün");
    }
    if (strLocale.equalsIgnoreCase("en_US"))
    {
      element.jSetPinDescription(0,"Out Color");
      element.jSetPinDescription(1,"In Red");
      element.jSetPinDescription(2,"In Green");
    }
    if (strLocale.equalsIgnoreCase("es_ES"))
    {
      element.jSetPinDescription(0,"Out Color");
      element.jSetPinDescription(1,"rojo-en");
      element.jSetPinDescription(2,"verde-en");
    } */

    icon=element.jLoadImage(element.jGetSourcePath()+"icon.png");

    setName("ScaticImage");
    element.jSetRasterized(true);

    file.clearExtensions();
    file.addExtension("jpg");
    file.addExtension("gif");
    file.addExtension("png");

    file.setDescription("GIF, JPG, PNG Image");
  }

  public void openPropertyDialog()
  {
  }
  
  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
    element.setPinOutputReference(1,outWidth);
    element.setPinOutputReference(2,outHeight);
  }

  
  public void propertyChanged(Object o)
  {
     if (o ==file)
     {
      outImage.loadImage(file.getValue());
      //loadImage(file.getValue());
     }

  }

   public int[] image2Pixel(Image imgx,int w, int h)
   {
    int[] pixels = new int[w * h];
    PixelGrabber pg = new PixelGrabber(imgx, 0, 0, w, h, pixels, 0, w);
    try {
        pg.grabPixels();
    } catch (InterruptedException e)
    {
      element.jException("Fehler in Methode image2Pixel: "+ e.toString());
    }

    return pixels;
  }

  public void start()
  {
  
    if (outImage.getImage()!=null)
    {
      int w=outImage.getImage().getWidth(null);
      int h=outImage.getImage().getHeight(null);
      int[] pixels=image2Pixel(outImage.getImage(),w,h);
      out.setPixels(pixels,w,h);

      outWidth.setValue(w);
      outHeight.setValue(h);
    } else
    {
      outWidth.setValue(0);
      outHeight.setValue(0);
    }

    out.setChanged(true);
    outWidth.setChanged(true);
    outHeight.setChanged(true);
    element.notifyPin(0);
    element.notifyPin(1);
    element.notifyPin(2);
  }

  public void setPropertyEditor()
  {
    element.jAddPEItem("Bild",file, 0,0);
    localize();
  }



  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Image");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Imagen");
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
    outImage.loadFromStream(fis);
  }
  
  public void onDispose()
  {
    if (outImage.getImage()!=null)
    {
      outImage.getImage().flush();
    }
   icon.flush();
   icon=null;
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    outImage.saveToStream(fos);
  }

}
