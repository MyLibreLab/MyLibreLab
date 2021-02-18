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
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class Element extends JVSMain
{
  private Image image;
  private Image img;
  private BufferedImage bufferedImage;
  private VSImage24 in;
  private VS2DDouble matrix;
  private VSDouble factor;
  private VSBoolean convolve;
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
    initPins(0,1,0,4);
    setSize(32+22,56);
    
    initPinVisibility(false,true,false,true);

    element.jSetInnerBorderVisibility(true);

    setPin(0,ExternalIF.C_IMAGE,element.PIN_OUTPUT); // Image
    setPin(1,ExternalIF.C_IMAGE,element.PIN_INPUT); // Image
    setPin(2,ExternalIF.C_ARRAY2D_DOUBLE,element.PIN_INPUT); // Matrix
    setPin(3,ExternalIF.C_BOOLEAN,element.PIN_INPUT); // convolve
    setPin(4,ExternalIF.C_DOUBLE,element.PIN_INPUT); // faktor

    element.jSetPinDescription(0,"out");
    element.jSetPinDescription(1,"in");
    element.jSetPinDescription(2,"Matrix");
    element.jSetPinDescription(3,"convolve");
    element.jSetPinDescription(4,"factor");

    String fileName=element.jGetSourcePath()+"icon.gif";
    image=element.jLoadImage(fileName);

    element.jSetCaptionVisible(true);
    element.jSetCaption("Convolve");
    setName("Convolve");

  }



  public void initInputPins()
  {
    in=(VSImage24)element.getPinInputReference(1);
    matrix=(VS2DDouble)element.getPinInputReference(2);
    convolve=(VSBoolean)element.getPinInputReference(3);
    factor=(VSDouble)element.getPinInputReference(4);
    
    if (in==null) in=new VSImage24(0,0);
    if (matrix==null) matrix=new VS2DDouble(0,0);
    if (convolve==null) convolve=new VSBoolean(false);
    if (factor==null) factor=new VSDouble(1.0);
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }
  
  ConvolveOp convolveOp;
  
  
  private float[] convert2DTo1D(VS2DDouble matrix, int w, int h)
  {
    float[] temp =new float[w*h];

    int x,y;
    
    double f=factor.getValue();

    for (y=0;y<h;y++)
    {
      for (x=0;x<w;x++)
      {
        temp[y+(w*x)]=(float)(matrix.getValue(y,x)/f);
      }

    }
    
    return temp;

  }
  public void ConvolveIt()
  {
    int width = img.getWidth(element.jGetFrame());
    int height = img.getHeight(element.jGetFrame());

    BufferedImage src = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
    bufferedImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
    
    Graphics2D big = src.createGraphics();
    AffineTransform affineTransform = new AffineTransform();
    big.drawImage(img, affineTransform, element.jGetFrame());
    
    Kernel kernel = new Kernel(matrix.getColumns(),matrix.getRows(), convert2DTo1D(matrix,matrix.getColumns(),matrix.getRows()));
    ConvolveOp con = new ConvolveOp(kernel);

    con.filter(src,bufferedImage);

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
  

  public void process()
  {
    if (in!=null && out!=null && matrix!=null)
    {
      if (convolve.getValue())
      {
        img= Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(in.getWidth(),in.getHeight(),in.getPixels(), 0, in.getWidth()) );

        ConvolveIt();

        int w= bufferedImage.getWidth(element.jGetFrame());
        int h = bufferedImage.getHeight(element.jGetFrame());
        int[] pixels=image2Pixel(bufferedImage,w,h);
        out.setPixels(pixels,w,h);

        //out.setPixels(medianFilter(in.getPixels().clone(),3),in.getWidth(),in.getHeight());
        out.setChanged(true);
        element.notifyPin(0);

      }
    }
  }
  
}


