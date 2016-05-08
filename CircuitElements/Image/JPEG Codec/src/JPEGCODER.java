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


public class JPEGCODER extends JVSMain
{
  private Image image;
  private VSImage24 in;
  private VS1DByte out=new VS1DByte(1);
  private VSDouble quality = new VSDouble(0.5);

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

    setPin(0,ExternalIF.C_ARRAY1D_BYTE,element.PIN_OUTPUT); // Image
    setPin(1,ExternalIF.C_IMAGE,element.PIN_INPUT); // Image


    element.jSetPinDescription(0,"Out");
    element.jSetPinDescription(1,"Image In");

    String fileName=element.jGetSourcePath()+"icon.gif";
    image=element.jLoadImage(fileName);

    element.jSetCaptionVisible(true);
    element.jSetCaption("JPEG Coder");
    setName("JPEG Coder");
  }



  public void initInputPins()
  {
    in=(VSImage24)element.getPinInputReference(1);
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }



  public byte [] code()
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream( 0xfff );
    try
    {
        Image img= Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(in.getWidth(),in.getHeight(),in.getPixels().clone(), 0, in.getWidth()) );

        BufferedImage imgX = new BufferedImage( in.getWidth(),in.getHeight(),BufferedImage.TYPE_INT_RGB );
        
        imgX.getGraphics().drawImage(img,0,0,null);
        

        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder( out );

        JPEGEncodeParam param;
        param = encoder.getDefaultJPEGEncodeParam( imgX );
        param.setQuality( (float)quality.getValue(), true );
        encoder.encode( imgX, param );

     } catch(Exception e)
     {
        element.jException("Fehler in Methode code JPEGDECODER: "+ e.toString());
     }
     return out.toByteArray();
  }


  public void setPropertyEditor()
  {
    element.jAddPEItem("Quality",quality, 0.0,1.0);
  }

  public void propertyChanged(Object o)
  {
    element.jRepaint();
  }


  public void process()
  {
    if (in!=null)
    {
     out.setBytes(code());
     out.setChanged(true);
     element.notifyPin(0);
    }
  }
  
  

  public void loadFromStream(java.io.FileInputStream fis)
  {
      quality.loadFromStream(fis);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      quality.saveToStream(fos);
  }


}


