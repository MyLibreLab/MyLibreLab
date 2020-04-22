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
import javax.swing.Timer;
import javax.swing.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import java.io.*;
import javax.swing.*;


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

public class XImage extends JVSMain
{
  private Image image=null;
  private Image noImage=null;

  private VSFile file = new VSFile("");
  private VSBoolean interpoliert = new VSBoolean();
  private VSBoolean keepAcpect = new VSBoolean();
  private byte imageBytes[]=null;
  
  private double aspect=1.0;
  private VSBoolean tiled=new VSBoolean(false);
  private VSInteger tiledWidth=new VSInteger(100);
  private VSInteger tiledHeight=new VSInteger(100);

  public void loadImage(String fileName)
  {
    try
    {
       ImageLoader il=new ImageLoader();
       image=il.loadImage(Toolkit.getDefaultToolkit().getImage(fileName));


    } catch(Exception ex)
    {
      System.out.println(ex.getMessage());
    }
      File f = new File(fileName);
      int fileSize = (int) f.length();
      try
      {
          BufferedInputStream input = new BufferedInputStream( new FileInputStream( fileName ) );
          imageBytes = new byte[(int)fileSize];

          input.read(imageBytes);
          input.close();
          input=null;
      } catch(Exception ex)
      {
         System.out.println(ex);
      }
  }
  
  
  public void drawTiled(java.awt.Graphics2D g, Image image, Rectangle r)
  {
    for (int y=0;y<r.height;y+=tiledHeight.getValue())
    {
      for (int x=0;x<r.width;x+=tiledWidth.getValue())
      {
        g.drawImage(image,r.x+x,r.y+y,tiledWidth.getValue(),tiledHeight.getValue(),null);
      }
    }

  }

  public void paint(java.awt.Graphics g)
  {
    if (element!=null)
    {
      Graphics2D g2 = (Graphics2D)g;

      if (interpoliert.getValue()==true)
      {
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      } else
      {
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
      }

      Rectangle r=element.jGetBounds();
      
      if (image!=null)
      {
        if (tiled.getValue())
        {
          drawTiled(g2,image,r);
        } else
        {
          g2.drawImage(image,0,0,element.jGetWidth()-1,element.jGetHeight()-1,null);
        }

      } else
      {
        if (noImage!=null)
        {
          g2.drawImage(noImage,0,0,element.jGetWidth()-1,element.jGetHeight()-1,null);

        }
      }
      //g.dispose();
    }
  }
   
  public void init()
  {
    initPins(0,0,0,0);
    setSize(100,100);
    initPinVisibility(false,false,false,false);
    element.jSetResizable(true);
    element.jSetInnerBorderVisibility(false);
    

    image=element.jLoadImage(element.jGetSourcePath()+"noImage.png");

    interpoliert.setValue(false);
    keepAcpect.setValue(true);
    processAspect();
    setName("Image");
    element.jSetRasterized(true);

    file.clearExtensions();
    file.addExtension("jpg");
    file.addExtension("gif");
    file.addExtension("png");

    file.setDescription("GIF, JPG, PNG Image");
  }

  public void openPropertyDialog()
  {
  }
  
  
  public void propertyChanged(Object o)
  {
     if (o ==file)
     {
      image.flush();
      image=null;
      loadImage(file.getValue());
      processAspect();
      if (tiled.getValue()==true)
      {
        element.jSetResizeSynchron(false);
        tiledWidth.setValue(100);
        tiledHeight.setValue((int)(100*aspect));
      }

     }

     if (o==keepAcpect)
     {
       processAspect();
     }
     
     if (o==tiled)
     {
       if (tiled.getValue()==true)
       {
         element.jSetResizeSynchron(false);
         tiledWidth.setValue(100);
         tiledHeight.setValue((int)(100*aspect));
       }
     }

    element.jRepaint();
  }

  public void processAspect()
  {
    if (image!=null)
    {
      double w=image.getWidth(null);
      double h=image.getHeight(null);
      aspect=h/w;
      element.jSetAspectRatio(aspect);
      w=element.jGetWidth();
      h=w*aspect; 
      element.jSetSize((int)w,(int)h);
    }else
    {
       if (noImage!=null)
       {
        double w=noImage.getWidth(null);
        double h=noImage.getHeight(null);
        aspect=h/w;
        element.jSetAspectRatio(aspect);
       }
    }

    element.jSetResizeSynchron(keepAcpect.getValue());
  }

  public void setPropertyEditor()
  {
    element.jAddPEItem("Bild",file, 0,0);
    element.jAddPEItem("Interpoliert",interpoliert, 0,0);
    element.jAddPEItem("Keep Aspect",keepAcpect, 0,0);
    element.jAddPEItem("Gekachelt",tiled, 0,0);
    element.jAddPEItem("Kachel-Breite",tiledWidth, 10,1000);
    element.jAddPEItem("Kachen-Höhe",tiledHeight, 10,1000);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Image");
    element.jSetPEItemLocale(d+1,language,"Interpolation");
    element.jSetPEItemLocale(d+2,language,"Keep Aspect");
    element.jSetPEItemLocale(d+3,language,"Tiled");
    element.jSetPEItemLocale(d+4,language,"Tiled-Width");
    element.jSetPEItemLocale(d+5,language,"Tiled-Height");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Imagen");
    element.jSetPEItemLocale(d+1,language,"Interpolación");
    element.jSetPEItemLocale(d+2,language,"Mantiene Proporciones");
    element.jSetPEItemLocale(d+3,language,"Mosaico");
    element.jSetPEItemLocale(d+4,language,"Anchura Mosaico");
    element.jSetPEItemLocale(d+5,language,"Altura Mosaico");
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
    java.io.DataInputStream dis = new java.io.DataInputStream(fis);
    
    interpoliert.loadFromStream(fis);
    keepAcpect.loadFromStream(fis);
    
    tiled.loadFromStream(fis);
    tiledWidth.loadFromStream(fis);
    tiledHeight.loadFromStream(fis);
    

    try
    {
      int size=dis.readInt();
      imageBytes = new byte[size];
      
      fis.read(imageBytes);

      image = java.awt.Toolkit.getDefaultToolkit().createImage(imageBytes);


    } catch(Exception ex)
    {
      System.out.println(ex);
    }
    
    ImageLoader il=new ImageLoader();
    il.loadImage(image);

    processAspect();
    element.jRepaint();
  }
  
  public void onDispose()
  {
   imageBytes=null;
   image.flush();
   image=null;
   noImage=null;
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);
    
    interpoliert.saveToStream(fos);
    keepAcpect.saveToStream(fos);
    
    tiled.saveToStream(fos);
    tiledWidth.saveToStream(fos);
    tiledHeight.saveToStream(fos);
    
    try
    {
      if (imageBytes!=null)
      {
        dos.writeInt(imageBytes.length);
        fos.write(imageBytes);
      }
    } catch(Exception ex)
    {
      element.jException(ex.toString());
    }

  }

}
