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
import java.awt.MediaTracker;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGDecodeParam;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JFrame;
import org.xml.sax.InputSource;



public class JPEGDECODER extends JVSMain
{
  private Image image;
  private VS1DByte in;
  private VSImage24 out=new VSImage24(1,1);

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
    initPins(0,1,0,1);
    setSize(32+22,32+4);
    
    initPinVisibility(false,true,false,true);

    element.jSetInnerBorderVisibility(true);

    setPin(0,ExternalIF.C_IMAGE,element.PIN_OUTPUT); // Image
    setPin(1,ExternalIF.C_ARRAY1D_BYTE,element.PIN_INPUT); // Image

    element.jSetPinDescription(0,"Image Out");
    element.jSetPinDescription(1,"In");

    String fileName=element.jGetSourcePath()+"icon.gif";
    image=element.jLoadImage(fileName);

    element.jSetCaptionVisible(true);
    element.jSetCaption("JPEG Decoder");
    setName("JPED Decoder");
  }



  public void initInputPins()
  {
    in=(VS1DByte)element.getPinInputReference(1);
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
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


  int w=0,h=0;
  public int [] decode()
  {
    try
    {
      byte[] bytes = in.getBytes().clone();
      
      if (bytes.length>1)
      {
        InputStream in = new ByteArrayInputStream(bytes);

        JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
        BufferedImage bi  = decoder.decodeAsBufferedImage();

        w=bi.getWidth();
        h=bi.getHeight();
        return image2Pixel(bi,w,h);
      }

    } catch(Exception e)
    {
      element.jShowMessage("Fehler in Methode decode JPEGDECODER: "+ e.toString());
      return new int[1];
    }
    return new int[1];
  }



  public void process()
  {
    if (in!=null)
    {
       out.setPixels(decode(),w,h);
       out.setChanged(true);
       element.notifyPin(0);
    }
  }
  
  

}




