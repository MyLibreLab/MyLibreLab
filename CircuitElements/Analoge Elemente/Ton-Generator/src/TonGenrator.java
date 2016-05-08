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


import javax.sound.sampled.*;
import java.nio.channels.*;
import java.nio.*;
import java.util.*;

import javax.swing.*;
import java.io.*;
import javax.sound.sampled.*;


public class TonGenrator extends JVSMain
{
  private VSGroup in;
  private Image image;
  private int counter=0;
  
  //The following are general instance variables
  // used to create a SourceDataLine object.
  AudioFormat audioFormat;
  AudioInputStream audioInputStream;
  SourceDataLine sourceDataLine;

  //The following are audio format parameters.
  // They may be modified by the signal generator
  // at runtime.  Values allowed by Java
  // SDK 1.4.1 are shown in comments.
  float sampleRate = 16000.0F;
  //Allowable 8000,11025,16000,22050,44100
  int sampleSizeInBits = 16;
  //Allowable 8,16
  int channels = 1;
  //Allowable 1,2
  boolean signed = true;
  //Allowable true,false
  boolean bigEndian = true;
  //Allowable true,false

  //A buffer to hold two seconds monaural and one
  // second stereo data at 16000 samp/sec for
  // 16-bit samples
  byte audioData[] = new byte[16000*4];


  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }

  //This method plays or files the synthetic
  // audio data that has been generated and saved
  // in an array in memory.
  private void playOrFileData()
  {
    try{
      //Get an input stream on the byte array
      // containing the data
      InputStream byteArrayInputStream =new ByteArrayInputStream(audioData);

      //Get the required audio format
      audioFormat = new AudioFormat(sampleRate,sampleSizeInBits,channels,signed,bigEndian);

      //Get an audio input stream from the
      // ByteArrayInputStream
      audioInputStream = new AudioInputStream(byteArrayInputStream,audioFormat,audioData.length/audioFormat.getFrameSize());

      //Get info on the required data line
      DataLine.Info dataLineInfo =new DataLine.Info(SourceDataLine.class,audioFormat);

      //Get a SourceDataLine object
      sourceDataLine = (SourceDataLine)AudioSystem.getLine(dataLineInfo);

        new ListenThread().start();
/*      }else
      {
        //Write the data to an output file with
        // the name provided by the text field
        // in the South of the GUI.
        try{
          AudioSystem.write(
                    audioInputStream,
                    AudioFileFormat.Type.AU,
                    new File(fileName.getText() +
                                         ".au"));
        }catch (Exception e) {
          e.printStackTrace();
          System.exit(0);
        }//end catch
        //Enable buttons for another operation
      }//end else
      */
    }catch (Exception e) {
      e.printStackTrace();
      System.exit(0);
    }//end catch
  }//end playOrFileData
//=============================================//

  
//Inner class to play back the data that was
// saved.
class ListenThread extends Thread{
  //This is a working buffer used to transfer
  // the data between the AudioInputStream and
  // the SourceDataLine.  The size is rather
  // arbitrary.
  byte playBuffer[] = new byte[16384];

  public void run(){
    try{
      //Open and start the SourceDataLine
      sourceDataLine.open(audioFormat);
      sourceDataLine.start();

      int cnt;
      //Get beginning of elapsed time for
      // playback
      long startTime = new Date().getTime();

      //Transfer the audio data to the speakers
      while((cnt = audioInputStream.read(playBuffer, 0,playBuffer.length))!= -1)
      {
        //Keep looping until the input read
        // method returns -1 for empty stream.
        if(cnt > 0){
          //Write data to the internal buffer of
          // the data line where it will be
          // delivered to the speakers in real
          // time
          sourceDataLine.write(
                             playBuffer, 0, cnt);
        }//end if
      }//end while

      //Block and wait for internal buffer of the
      // SourceDataLine to become empty.
      sourceDataLine.drain();


      //Get and display the elapsed time for
      // the previous playback.
      int elapsedTime =(int)(new Date().getTime() - startTime);
      //elapsedTimeMeter.setText("" + elapsedTime);

      //Finish with the SourceDataLine
      sourceDataLine.stop();
      sourceDataLine.close();

    }catch (Exception e) {
      e.printStackTrace();
      System.exit(0);
    }//end catch

  }//end run
}//end inner class ListenThread
//=============================================//

//Inner signal generator class.

  

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
    initPins(0,0,0,1);
    setSize(50,50);

    element.jSetInnerBorderVisibility(true);


    String fileName=element.jGetSourcePath()+"tone.gif";
    System.out.println("imageFileName="+fileName);
    image=element.jLoadImage(fileName);

    
    setPin(0,ExternalIF.C_GROUP,element.PIN_INPUT);

    element.jSetPinDescription(0,"in");

    element.jSetResizable(false);
    element.jSetCaptionVisible(true);
    element.jSetCaption("tone generator");

    // Für den Degruppierer (Nur für den Datentyp)
    /*out.list.clear();
    out.list.add(outX);
    out.list.add(outY);
    element.setPinOutputReference(0,out);*/

    setName("tone generator");
  }
  

  public void initInputPins()
  {
    in=(VSGroup)element.getPinInputReference(0);
  }

  public void initOutputPins()
  {
  }


  public void process()
  {
  
    if (in !=null && in.list.size()==2)
    {
      VS1DDouble arrayX=(VS1DDouble)in.list.get(0);
      VS1DDouble arrayY=(VS1DDouble)in.list.get(1);


      //if (in.isChanged() )
      {
        if (arrayY.getValue().length>0)
        {

          channels = 1;//Java allows 1 or 2  //Each channel requires two 8-bit bytes per 16-bit sample.
          int bytesPerSamp = 2;
          sampleRate = 16000.0F; // Allowable 8000,11025,16000,22050,44100

          int byteLength = audioData.length;
          int sampLength = byteLength/bytesPerSamp;

          /*for(int cnt = 0; cnt < sampLength; cnt++)
          {
            double sinValue =  Math.sin(((double)cnt)/20.0)*20;
            audioData[cnt]=((byte)(sinValue));
          }//end for loop*/

          for (int i=0;i<arrayY.getValue().length;i++)
          {
           audioData[i]=(byte)arrayY.getValue(i);
          }
          counter++;
          if (counter>100)
          {
           playOrFileData();
           counter=0;
          }
        }
      }
    }

  }
  
  public void start()
  {
    //SynGen generator new SynGen();
    
    //generator.getSyntheticData(audioData);

    //genTone();
    
  }
  
  public void stop()
  {
  }
  
  
   void genTone()
  {
    channels = 1;//Java allows 1 or 2
    //Each channel requires two 8-bit bytes per 16-bit sample.
    int bytesPerSamp = 2;
    sampleRate = 16000.0F; // Allowable 8000,11025,16000,22050,44100

    int byteLength = audioData.length;
    int sampLength = byteLength/bytesPerSamp;

    for(int cnt = 0; cnt < sampLength; cnt++)
    {
      double sinValue =  Math.sin(((double)cnt)/20.0)*20;
      audioData[cnt]=((byte)(sinValue));
    }//end for loop
  }//end method tones


//Inner signal generator class.

//An object of this class can be used to
// generate a variety of different synthetic
// audio signals.  Each time the getSyntheticData
// method is called on an object of this class,
// the method will fill the incoming array with
// the samples for a synthetic signal.
class SynGen
{
  //Note:  Because this class uses a ByteBuffer
  // asShortBuffer to handle the data, it can
  // only be used to generate signed 16-bit
  // data.
  ByteBuffer byteBuffer;
  ShortBuffer shortBuffer;
  int byteLength;

  void getSyntheticData(byte[] synDataBuffer)
  {
    //Prepare the ByteBuffer and the shortBuffer for use
    byteBuffer = ByteBuffer.wrap(synDataBuffer);
    shortBuffer = byteBuffer.asShortBuffer();

    byteLength = synDataBuffer.length;

    tones();
  }//end getSyntheticData method
  //-------------------------------------------//

  //This method generates a monaural tone
  // consisting of the sum of three sinusoids.
  void tones()
  {
    channels = 1;//Java allows 1 or 2
    //Each channel requires two 8-bit bytes per 16-bit sample.
    int bytesPerSamp = 2;
    sampleRate = 22050.0F; // Allowable 8000,11025,16000,22050,44100

    int sampLength = byteLength/bytesPerSamp;
    
    for(int cnt = 0; cnt < sampLength; cnt++)
    {
      double time = cnt/sampleRate;
      double freq = 950.0;//arbitrary frequency
      //double sinValue =  (Math.sin(2*Math.PI*freq*time) + Math.sin(2*Math.PI*(freq/1.8)*time) + Math.sin(2*Math.PI*(freq/1.5)*time))/3.0;

      double sinValue =  Math.sin(2*Math.PI*freq*time);
      shortBuffer.put((short)(sampleRate*sinValue));
    }//end for loop
  }//end method tones
  //-------------------------------------------//

  //-------------------------------------------//
}//end SynGen class
//=============================================//




}

