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


public class Weichzeichnen extends JVSMain
{
  private Image image;
  private VSImage24 in;
  private VSImage24 out=new VSImage24(1,1);
  private VSInteger regionWidth = new VSInteger(2);

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
    initPins(0,1,0,1);
    setSize(32+22,32+4);
    
    initPinVisibility(false,true,false,true);

    element.jSetInnerBorderVisibility(true);

    setPin(0,ExternalIF.C_IMAGE,element.PIN_OUTPUT); // Image
    setPin(1,ExternalIF.C_IMAGE,element.PIN_INPUT); // Image

    element.jSetPinDescription(0,"Image Out");
    element.jSetPinDescription(1,"Image In");

    String fileName=element.jGetSourcePath()+"icon.gif";
    image=element.jLoadImage(fileName);

    element.jSetCaptionVisible(true);
    element.jSetCaption("Weichzeichnen");
    setName("Weichzeichnen");

  }



  public void initInputPins()
  {
    in=(VSImage24)element.getPinInputReference(1);
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }


  // distance = zb: 3x3
  public int [] weichzeichnen(int[] pixels,int distance)
  {
    int w=in.getWidth();
    int h=in.getHeight();
    int pixel;
    int gray1;
    int c=0;
    int a=0;


    int anzahl=distance*distance;
    int feld[] = new int[anzahl];

    int r,g,b;
    int cR,cG,cB;
    
    for (int j = 0; j <h-distance; j++)
    {
        for (int i = 0; i <w-distance; i++)
        {
          cR=0;
          cG=0;
          cB=0;
          for (int y=0;y<distance;y++)
          {
            for (int x=0;x<distance;x++)
            {
              pixel=pixels[((j+y+1)*w)+(i+x+1)];

              r = (pixel >> 16) & 0xff;
              g = (pixel >>  8) & 0xff;
              b = (pixel      ) & 0xff;
              cR+=r;
              cG+=g;
              cB+=b;
            }
          }
          
          r=cR/anzahl;
          g=cG/anzahl;
          b=cB/anzahl;

          pixels[(j)*w+(i)]=(255 << 24) | (r << 16) | (g<< 8) | b;
        }
    }
   return pixels;

  }


  public void setPropertyEditor()
  {
    element.jAddPEItem("Region Width",regionWidth, 2,10);
  }

  public void propertyChanged(Object o)
  {
    element.jRepaint();
  }


  public void process()
  {
    if (in!=null)
    {
         if (regionWidth.getValue()>=2 && regionWidth.getValue()<=10)
         {

         } else
         {
           regionWidth.setValue(2);
         }
         out.setPixels(weichzeichnen(in.getPixels().clone(),regionWidth.getValue()),in.getWidth(),in.getHeight());

         out.setChanged(true);
         element.notifyPin(0);
    }
  }
  
  
  public void loadFromStream(java.io.FileInputStream fis)
  {
      regionWidth.loadFromStream(fis);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      regionWidth.saveToStream(fos);
  }
  



}



