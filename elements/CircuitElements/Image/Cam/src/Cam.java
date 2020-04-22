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

// Requires JMF!
import javax.media.Buffer;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Manager;
import javax.media.Player;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.VideoFormat;
import javax.media.util.BufferToImage;


public class Cam extends JVSMain
{
  private Image image;
  private Player player=null;
  private VSBoolean open;
  private VSImage24 out=new VSImage24(1,1);
  private VSInteger delay=new VSInteger(2500);
  private VSString captureDevice=new VSString("vfw:Microsoft WDM Image Capture (Win32):0");
  private GRABB grabb = null;

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
    if (image!=null) drawImageCentred(g,image);
  }

  public void init()
  {
    initPins(0,1,0,1);
    setSize(32+24,32+4);

    initPinVisibility(false,true,false,true);

    element.jSetInnerBorderVisibility(true);

    setPin(0,ExternalIF.C_IMAGE,element.PIN_OUTPUT); // Image
    setPin(1,ExternalIF.C_BOOLEAN,element.PIN_INPUT); // open

    element.jSetPinDescription(0,"Image");
    element.jSetPinDescription(1,"Open");

    String fileName=element.jGetSourcePath()+"icon.gif";
    image=element.jLoadImage(fileName);

    element.jSetCaptionVisible(true);
    element.jSetCaption("Cam");
    setName("Cam");
  }

  public void xOnInit()
  {

  }


  public void initInputPins()
  {
    open=(VSBoolean)element.getPinInputReference(1);
    
    if (open==null)
    {
      open = new VSBoolean(false);
    }
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }
  
  
  public void start()
  {
  }


  public void stop()
  {

    if (player!=null)
    {
      player.stop();
      player.close();
      player.deallocate();
      player=null;
    }
    if (grabb!=null)
    {
     grabb.stopCam();
     grabb=null;
    }

  }
  
  public void setPropertyEditor()
  {
    element.jAddPEItem("Capture Device",captureDevice, 0,0);
    element.jAddPEItem("Start Delay",delay, 1,6000);
  }

  public void propertyChanged(Object o)
  {
   // element.jRepaint();
  }

  

  public void loadFromStream(java.io.FileInputStream fis)
  {
    captureDevice.loadFromStream(fis);
    delay.loadFromStream(fis);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    captureDevice.saveToStream(fos);
    delay.saveToStream(fos);
  }

  
  private boolean hasBufferToImage()
  {
    FrameGrabbingControl fg = (FrameGrabbingControl)player.getControl("javax.media.control.FrameGrabbingControl");
    Buffer buf = fg.grabFrame();   // take a snap
    if (buf == null) {
      //System.out.println("No grabbed frame");
      return false;
    }

    // there is a buffer, but check if it's empty
    VideoFormat vf = (VideoFormat) buf.getFormat();
    if (vf == null) {
      //System.out.println("No video format");
      return false;
    }

    System.out.println("Video format: " + vf);
    // extract the image's dimensions
    //width = vf.getSize().width;
    //height = vf.getSize().height;

    // initialize bufferToImage with video format
    //bufferToImage = new BufferToImage(vf);
    return true;
  }

  public void waitForBufferToImage()
  {
    while(hasBufferToImage()==false)
    {
        try
        {
          Thread.sleep(10);
        } catch (Exception ex) {  }
    }
  }
  

  public void process()
  {
    if (open!=null)
    {
         if (open.getValue()==true && player==null)
         {
            // Create capture device
            try
            {
              CaptureDeviceInfo deviceInfo = CaptureDeviceManager.getDevice(captureDevice.getValue());
              player = Manager.createRealizedPlayer(deviceInfo.getLocator());
              player.start();
            }
            catch (Exception ex)
            {
              element.jException("Cam : exception in Methode process() "+ex);
              player=null;
            }

            waitForBufferToImage();
            grabb= new GRABB(player,element,out);
            grabb.startCam();


      } else
      {
          stop();
      }
    }
  }

}


class GRABB extends Thread
{
  private Player player;
  private ExternalIF element;
  private VSImage24 out;
  public boolean stop=false;
  
  public GRABB(Player player, ExternalIF element, VSImage24 out)
  {
    this.player=player;
    this.element=element;
    this.out=out;
    stop=true;
  }
  
  public void startCam()
  {
    stop=false;
    start();
  }

  public void stopCam()
  {
    stop=true;
  }

   public int[] image2Pixel(Image imgx,int w, int h)
   {
    int[] pixels = new int[w * h];
    PixelGrabber pg = new PixelGrabber(imgx, 0, 0, w, h, pixels, 0, w);
    try {
        pg.grabPixels();
    } catch (InterruptedException e)
    {
     element.jException("Cam : Fehler in Methode image2Pixel: "+ e.toString());
    }

    return pixels;
  }



  public void run()
  {
    while (stop==false && player!=null)
    {
      try
      {
        // Grab a frame from the capture device
        FrameGrabbingControl frameGrabber = (FrameGrabbingControl)player.getControl("javax.media.control.FrameGrabbingControl");
        Buffer buf = frameGrabber.grabFrame();

        // Convert frame to an buffered image so it can be processed and saved
        Image img = (new BufferToImage((VideoFormat)buf.getFormat()).createImage(buf));
        BufferedImage buffImg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);


        try
        {
          Thread.sleep(10);
        } catch (Exception ex) { element.jException("Cam : Thread exception "+ex); }

        int[] pixels=image2Pixel(img,img.getWidth(null), img.getHeight(null));
        out.setPixels(pixels,img.getWidth(null), img.getHeight(null));
        out.setChanged(true);
        element.notifyPin(0);
        img.flush();
        img=null;

       } catch (Exception ex) { element.jException("Cam : exception "+ex); }
    }

  }

}
