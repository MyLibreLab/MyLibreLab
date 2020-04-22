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
import javax.swing.*;
import java.awt.event.*;

import java.io.*;


class ImageLoader extends Component
{
  public ImageLoader()
  {
  }

  public Image loadImage(Image img)
  {
    MediaTracker mc = new MediaTracker(this);
    mc.addImage(img,0);

    try
    {
     mc.waitForID(0);
    } catch (InterruptedException ex)
    {
    }
    return img;
  }
}
  

public class SchalterPanel extends JVSMain
{

  private VSBoolean initValue=new VSBoolean(false);
  private ExternalIF circuitElement;
  
  private VSFile file1 = new VSFile("");
  private VSFile file2 = new VSFile("");
  
  private boolean down=false;
  
  private Image imageOn;
  private Image image;
  private Image imageOff;

  private boolean value=false;
  private int sizeW=27;
  private int sizeH=35;

  private byte[] buffImageOn;
  private byte[] buffImageOff;
  
  private VSBoolean interpoliert = new VSBoolean();



  public byte[] loadImage(String fileName)
  {
      byte[] result=null;
      image=element.jLoadImage(fileName);
     
      File f = new File(fileName);
      int fileSize = (int) f.length();
      try
      {
          BufferedInputStream input = new BufferedInputStream( new FileInputStream( fileName ) );
          result = new byte[(int)fileSize];
          input.read(result);
          input.close();
          input=null;
      } catch(Exception ex)
      {
         System.out.println(ex);
      }
      
      return result;
  }


  public void paint(java.awt.Graphics g)
  {
    if (element!=null)
    {
       Graphics2D g2=(Graphics2D)g;
       Rectangle bounds=element.jGetBounds();
      if (interpoliert.getValue()==true)
      {
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      } else
      {
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
      }


       g2.drawImage(image,0,0,element.jGetWidth()-1,element.jGetHeight()-1,null);
    }
  }
  
  private void setValue(VSBoolean bol)
  {
     down=bol.getValue();
     circuitElement=element.getCircuitElement();
     if (circuitElement!=null) circuitElement.Change(0,bol);
  }


  public void init()
  {
    initPins(0,0,0,0);
    setSize(sizeW,sizeH);
    initPinVisibility(false,false,false,false);

    element.jSetInnerBorderVisibility(false);

    element.jSetResizable(true);
    
    setName("Schiebeschalter2");
    
    String fn1=element.jGetSourcePath()+"on.png";
    String fn2=element.jGetSourcePath()+"off.png";

    imageOn=element.jLoadImage(fn1);
    imageOff=element.jLoadImage(fn2);

    buffImageOn=loadImage(fn1);
    buffImageOff=loadImage(fn2);
    
    interpoliert.setValue(false);
    
    file1.clearExtensions();
    file1.addExtension("jpg");
    file1.addExtension("gif");
    file1.addExtension("png");
    file1.setDescription("GIF, JPG, PNG Image");

    file2.clearExtensions();
    file2.addExtension("jpg");
    file2.addExtension("gif");
    file2.addExtension("png");
    file2.setDescription("GIF, JPG, PNG Image");
  }
  
    public void start()
    {
      down=initValue.getValue();
      if (down) image=imageOn; else image=imageOff;
      setValue(initValue);
      
      element.jRepaint();
    }
    
    
  public void setPropertyEditor()
  {
    element.jAddPEItem("Bild ON",file1, 0,0);
    element.jAddPEItem("Bild OFF",file2, 0,0);
    element.jAddPEItem("Interpoliert",interpoliert, 0,0);
    element.jAddPEItem("Anfangs-Wert",initValue, 0,0);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Image ON");
    element.jSetPEItemLocale(d+1,language,"Image OFF");
    element.jSetPEItemLocale(d+2,language,"Interpolation");
    element.jSetPEItemLocale(d+3,language,"Init-Value");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Image ON");
    element.jSetPEItemLocale(d+1,language,"Image OFF");
    element.jSetPEItemLocale(d+2,language,"Interpolation");
    element.jSetPEItemLocale(d+3,language,"Init-Value");

  }

  public void propertyChanged(Object o)
  {
     if (o ==file1)
     {
      imageOn=element.jLoadImage(file1.getValue());
      buffImageOn=loadImage(file1.getValue());
     }
     if (o ==file2)
     {
      imageOff=element.jLoadImage(file2.getValue());
      buffImageOff=loadImage(file2.getValue());
     }

    setValue(initValue);
    element.jRepaint();
  }

  

  public void mousePressed(MouseEvent e)
  {
     if (down==true)
     {
        image=imageOff;
        setValue(new VSBoolean(false));
        element.jRepaint();
      } else
      {
        image=imageOn;
        setValue(new VSBoolean(true));
        element.jRepaint();
      }
  }


  public void loadFromStream(java.io.FileInputStream fis)
  {
      java.io.DataInputStream dis = new java.io.DataInputStream(fis);
      
      initValue.loadFromStream(fis);
      interpoliert.loadFromStream(fis);

      try
      {
        int size=dis.readInt();
        buffImageOn = new byte[size];

        fis.read(buffImageOn);

        imageOn = java.awt.Toolkit.getDefaultToolkit().createImage(buffImageOn);


      } catch(Exception ex)
      {
        System.out.println(ex);
      }

      try
      {
        int size=dis.readInt();
        buffImageOff = new byte[size];

        fis.read(buffImageOff);

        imageOff = java.awt.Toolkit.getDefaultToolkit().createImage(buffImageOff);


      } catch(Exception ex)
      {
        System.out.println(ex);
      }

      down=initValue.getValue();
      
      
      ImageLoader il=new ImageLoader();
      il.loadImage(imageOn);
      il.loadImage(imageOff);

      if (down) image=imageOn; else image=imageOff;
      element.jRepaint();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);
      
      
      initValue.saveToStream(fos);
      interpoliert.saveToStream(fos);
      
      try
      {
        if (buffImageOn!=null)
        {
          dos.writeInt(buffImageOn.length);
          fos.write(buffImageOn);
        }
      } catch(Exception ex)
      {
        element.jException(ex.toString());
      }

      try
      {
        if (buffImageOff!=null)
        {
          dos.writeInt(buffImageOff.length);
          fos.write(buffImageOff);
        }
      } catch(Exception ex)
      {
        element.jException(ex.toString());
      }

  }



}

