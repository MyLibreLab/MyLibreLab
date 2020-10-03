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
import javax.swing.*;
import java.util.*;
import java.net.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.spi.AudioFileReader;

public class PlaySound extends JVSMain
{
  public VSBoolean inPlay;
  public VSBoolean inStop;
  public VSFile  inFilename = new VSFile("");
  private Clip clip=null;
  private byte[] data=null;

  private Image image;


  public void paint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }


 public void init()
  {
    initPins(0,0,0,2);
    setSize(35+20,35+10);
    initPinVisibility(false,false,false,true);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");

    setPin(0,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(1,ExternalIF.C_BOOLEAN,element.PIN_INPUT);

    String strLocale=Locale.getDefault().toString();

    if (strLocale.equalsIgnoreCase("de_DE"))
    {
      element.jSetPinDescription(0,"Abspielen");
      element.jSetPinDescription(1,"Stop");
    }
    if (strLocale.equalsIgnoreCase("en_US"))
    {
      element.jSetPinDescription(0,"Play");
      element.jSetPinDescription(1,"Stop");
    }
    if (strLocale.equalsIgnoreCase("es_ES"))
    {
      element.jSetPinDescription(0,"comienzo");
      element.jSetPinDescription(1,"parada");
    }



    element.jSetCaptionVisible(true);
    element.jSetCaption("PlaySound");
    setName("PlaySound");
    
    inFilename.clearExtensions();
    
    inFilename.addExtension("aiff");
    inFilename.addExtension("au");
    inFilename.addExtension("wav");

    inFilename.setDescription("AIFF, AU, WAV");
    
  }
  
  
  public void loadSoundFile(String filename)
  {
    try
    {
      FileInputStream fis = new FileInputStream(new File(filename));

      DataInputStream dis=new DataInputStream(fis);

      int size=(int)(new File(filename)).length();
      data=new byte[size];
      dis.read(data);
    } catch(Exception ex)
    {
      System.out.println(ex.toString());
    }
  }
  
  public void loadFromBytes()
  {
    if (data.length>1)
    try
    {
      ByteArrayInputStream dis2=new ByteArrayInputStream(data);

      AudioInputStream stream = AudioSystem.getAudioInputStream(dis2);

      // From URL
      // stream = AudioSystem.getAudioInputStream(new URL("http://hostname/audiofile"));

      // At present, ALAW and ULAW encodings must be converted
      // to PCM_SIGNED before it can be played
      AudioFormat format = stream.getFormat();
      if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED)
      {
          format = new AudioFormat(
                  AudioFormat.Encoding.PCM_SIGNED,
                  format.getSampleRate(),
                  format.getSampleSizeInBits()*2,
                  format.getChannels(),
                  format.getFrameSize()*2,
                  format.getFrameRate(),
                  true);        // big endian
          stream = AudioSystem.getAudioInputStream(format, stream);
      }
      DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat(), ((int)stream.getFrameLength()*format.getFrameSize()));
      clip = (Clip) AudioSystem.getLine(info);
      clip.open(stream);
      //clip.start();

      
      dis2.close();
      
    } catch (MalformedURLException e) {
    } catch (IOException e) {
    } catch (LineUnavailableException e) {
    } catch (UnsupportedAudioFileException e) {
    }
  }

  public void propertyChanged(Object o)
  {
     if (o ==inFilename)
     {
        // Laedt die Datei in einen
        loadSoundFile(inFilename.getValue());
        loadFromBytes();
     }

  }

  public void setPropertyEditor()
  {
    element.jAddPEItem("Sound-Datei",inFilename, 0,0);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Sound-File");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Fichero de Sonido");
  }


  public void initInputPins()
  {
    inPlay=(VSBoolean)element.getPinInputReference(0);
    inStop=(VSBoolean)element.getPinInputReference(1);

    if (inPlay==null)
    {
      inPlay=new VSBoolean(false);
    }
    if (inStop==null)
    {
      inStop=new VSBoolean(false);
    }
  }

  public void initOutputPins()
  {
  }

  public void start()
  {
//    if (data.length>0)loadFromBytes();
  }
  
  public void stop()
  {
    if (clip!=null) clip.stop();
    //clip=null;
  }

  public void process()
  {
      if (inPlay.getValue()==true)
      {
        if (clip!=null)
        {
         clip.stop();
         clip.setFramePosition(0);
         clip.start();
        }
      }


      if (inStop.getValue()==true)
      {
        if (clip!=null) clip.stop();
      }
      
  }

  public void onDispose()
  {
    if (clip!=null)clip.close();
    clip=null;

    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }


  public void loadFromStream(java.io.FileInputStream fis)
  {
    java.io.DataInputStream dis = new java.io.DataInputStream(fis);

    try
    {
      int size=dis.readInt();
      data=new byte[size];
      dis.read(data);
      loadFromBytes();
    } catch(Exception ex)
    {
      element.jException("Fehler in PlaySound Methode loadFromStream() : "+ex.toString());
    }

  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);
    try
    {
      if (data==null) data=new byte[1];
      
      if (data.length>0)
      {
        dos.writeInt(data.length);
        dos.write(data);
      }
    } catch(Exception ex)
    {
      element.jException("Fehler in PlaySound Methode saveToStream() : "+ex.toString());
    }

  }



}
