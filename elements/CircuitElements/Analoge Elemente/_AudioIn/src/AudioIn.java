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
import java.util.Random;


import javax.swing.*;
import java.io.*;
import javax.sound.sampled.*;


public class AudioIn extends JVSMain
{
  private VSGroup in;
  private VSGroup out = new VSGroup();
  int buffLen=256;
  private VS1DDouble outX = new VS1DDouble(buffLen);
  private VS1DDouble outY = new VS1DDouble(buffLen);
  
  private VSDouble zoomX;
  private VSDouble zoomY;

  private Image image;
  
  
  private double value=0.0;
  private double oldValue=0.0;

  private boolean stopCapture = false;
  private ByteArrayOutputStream byteArrayOutputStream;
  private AudioFormat audioFormat;
  private TargetDataLine targetDataLine=null;
  private AudioInputStream audioInputStream;
  private SourceDataLine sourceDataLine;
  

  private byte tempBuffer[] = new byte[buffLen];
  int cnt=0;
  

  public void onDispose()
  {
    stopCapture=true;
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }

    boolean bigEndian;
  
  private AudioFormat getAudioFormat()
  {
    float sampleRate = 8000.0F;
    //8000,11025,16000,22050,44100
    int sampleSizeInBits = 8;
    //8,16
    int channels = 1;
    //1,2
    boolean signed = true;
    //true,false
    boolean bigEndian = false;
    //true,false
    return new AudioFormat(sampleRate,sampleSizeInBits,channels,signed,bigEndian);

  }//end getAudioFormat

  private void captureAudio()
  {
    try{
      //Get everything set up for capture

      audioFormat = getAudioFormat();
      DataLine.Info dataLineInfo =new DataLine.Info(TargetDataLine.class,audioFormat);
      targetDataLine = (TargetDataLine)AudioSystem.getLine(dataLineInfo);
      targetDataLine.open(audioFormat);
      targetDataLine.start();

      // Create a thread to capture the
      // microphone data and start it
      // running.  It will run until
      // the Stop button is clicked.
      Thread captureThread =new Thread(new CaptureThread(element));
      captureThread.start();

    } catch (Exception e)
    {
      System.out.println(e);
      //System.exit(0);
    }//end catch
  }//end captureAudio method

  
  
//Inner class to capture data from
// microphone
class CaptureThread extends Thread
{
  //An arbitrary-size temporary holding
  // buffer
  
  private ExternalIF element;

  public CaptureThread(ExternalIF element)
  {
    super();
    this.element=element;
  }

  
  public void run()
  {
    stopCapture = false;
    try{//Loop until stopCapture is set
        // by another thread that
        // services the Stop button.
      while(!stopCapture)
      {

        if (targetDataLine!=null)
        {
          int x=0;

          try
          {
              cnt = targetDataLine.read(tempBuffer,0,tempBuffer.length);
              Thread.sleep(100);
          }catch (Exception e) { System.out.println(e);  }
          if (cnt>0)
          {
            int level;
            for (int i=0;i<cnt;i++)
            {

              //level = (tempBuffer[i]<<16)&tempBuffer[i+1];
              //level = (tempBuffer[i+1]<<16)&tempBuffer[i];

             // level = (tempBuffer[i+1]>>8)&tempBuffer[i];
              //level = (tempBuffer[i]<<8)&tempBuffer[i+1];
              //level = (tempBuffer[i+1]<<8)&tempBuffer[i];
              //level=tempBuffer[i];
              
               //if (bigEndian)
               //level = (tempBuffer[i]<<16)&tempBuffer[i+1];
               //else
               //level = (tempBuffer[i+1]<<16)&tempBuffer[i];
               level = tempBuffer[i];

              value=(double)level*zoomY.getValue();
              outX.setValue(x,(double)x*zoomX.getValue());
              outY.setValue(x,value);
              oldValue=outY.getValue(x);

              out.setChanged(true);
              element.notifyPin(0);

              x++;
            }
          }
        }

      }//end while

    }catch (Exception e) {
      System.out.println(e);
      System.exit(0);
    }//end catch
  }//end run
}//end inner class CaptureThread
//===================================//

  
  public AudioIn()
  {
    outX= new VS1DDouble(buffLen);
    outY= new VS1DDouble(buffLen);
  }

  public void paint(java.awt.Graphics g)
  {
   if (element!=null)
    {
       Rectangle r=element.jGetBounds();
       g.drawImage(image,r.x,r.y,r.width,r.height,null);
    }
  }

  public void init()
  {
  
    initPins(0,1,0,2);
    setSize(50,55);

    element.jSetInnerBorderVisibility(true);


    String fileName=element.jGetSourcePath()+"XAudioIn.GIF";
    System.out.println("imageFileName="+fileName);
    image=element.jLoadImage(fileName);

    
    setPin(0,ExternalIF.C_GROUP,element.PIN_OUTPUT);

    setPin(1,ExternalIF.C_DOUBLE,element.PIN_INPUT);
    setPin(2,ExternalIF.C_DOUBLE,element.PIN_INPUT);
    
    
    element.jSetPinDescription(0,"Out");
    element.jSetPinDescription(1,"zoomX");
    element.jSetPinDescription(2,"zoomY");

    element.jSetResizable(false);
    element.jSetCaptionVisible(true);
    element.jSetCaption("audio-in");

    // Für den Degruppierer (Nur für den Datentyp)
    out.list.clear();
    out.list.add(outX);
    out.list.add(outY);
    element.setPinOutputReference(0,out);

    setName("Audio-In");
  }
  

  public void initInputPins()
  {
    zoomX=(VSDouble)element.getPinInputReference(1);
    zoomY=(VSDouble)element.getPinInputReference(2);

    if (zoomX==null)
    {
      zoomX=new VSDouble();
      zoomX.setValue(1.0);
    }
    if (zoomY==null)
    {
      zoomY=new VSDouble();
      zoomY.setValue(1.0);
    }

  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }


  public void process()
  {

  }
  
  public void start()
  {
    stopCapture=false;
    captureAudio();
  }
  
  public void stop()
  {
    stopCapture=true;
  }

}

