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


public class LoadImage extends JVSMain
{
  private Image image;
  private VSString path;
  private VSBoolean oki;
  private VSImage24 out=new VSImage24(1,1);


  public void paint(java.awt.Graphics g)
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
    initPins(0,1,0,2);
    setSize(32+24,32+4);
    initPinVisibility(false,true,false,true);

    element.jSetInnerBorderVisibility(true);

    setPin(0,ExternalIF.C_IMAGE,element.PIN_OUTPUT); // Image
    setPin(1,ExternalIF.C_STRING,element.PIN_INPUT); // Path
    setPin(2,ExternalIF.C_BOOLEAN,element.PIN_INPUT); // OKI

    element.jSetPinDescription(0,"image");
    element.jSetPinDescription(1,"path");
    element.jSetPinDescription(2,"load image");

    String fileName=element.jGetSourcePath()+"icon.png";
    image=element.jLoadImage(fileName);

    element.jSetCaptionVisible(false);
    element.jSetCaption("Image Loader 2");
    setName("Image Loader 2");
    
  }



  public void initInputPins()
  {
    path=(VSString)element.getPinInputReference(1);
    oki=(VSBoolean)element.getPinInputReference(2);
    
    if (path==null) path=new VSString("");
    if (oki==null) oki=new VSBoolean(false);
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
  


  public void process()
  {
    if (path!=null )
    {
      if (oki.getValue())
      {
          String fileName=path.getValue();
          try
          {
            Image img=element.jLoadImage(fileName);
            System.out.println(fileName);
            if (img!=null)
            {

              if (img.getWidth(null)>0 && img.getHeight(null)>0)
              {
                out.resize(img.getWidth(null),img.getHeight(null));

                int w=img.getWidth(null);
                int h=img.getHeight(null);
                int[] pixels=image2Pixel(img,w,h);
                out.setPixels(pixels,w,h);
              } else
              {
                out.resize(5,5);
                element.jShowMessage("Image Loading failed!");
              }
              
              img.flush();
              img=null;
            } else
            {
              out.resize(5,5);
              element.jShowMessage("Image Loading failed!");
            }
          } catch(Exception ex)
          {
            System.out.println(ex);
          }

         //out.setValue(temp);
         out.setChanged(true);
         element.notifyPin(0);

      }
    }
  }

}

