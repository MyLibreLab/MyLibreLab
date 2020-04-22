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
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import VisualLogic.variables.*;
import tools.*;
import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.app.beans.SVGIcon;
import com.kitfox.svg.app.beans.SVGPanel;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.*;
import java.net.*;
import SVGViewer.*;

public class Panel extends JVSMain
{
  private boolean on=false;
  private SVGManager svgManager = new SVGManager();
  private VSFile file = new VSFile("");
  private byte imageBytes[]=null;
  private SVGPanel pnl  = new  SVGPanel();
  JPanel panel;

   public void paint(java.awt.Graphics g)
   {
     if (element!=null)
     {
        //Rectangle bounds=element.jGetBounds();
        //svgManager.paint((Graphics2D)g,(int)bounds.getWidth(),(int)bounds.getHeight());
     }
   }
   

  private SVGPanel configSVGIcon(String imageFile)
  {
        SVGPanel svgIcon;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imageFile);
        } catch (Exception e) {
             System.out.println("File not found: " + imageFile);
             return null;
        }
        URI uri = SVGCache.getSVGUniverse().loadSVG(fis, imageFile);
        svgIcon = new SVGPanel();
        svgIcon.setAntiAlias(true);
        svgIcon.setSvgURI(uri);
        try {
            fis.close();
        } catch (Exception e) {
             System.out.println("File not found: " + imageFile);
        }
        return svgIcon;

   }
   
  public void init()
  {
    initPins(0,0,0,0);
    initPinVisibility(false,false,false,false);
    setSize(80,80);
    element.jSetInnerBorderVisibility(false);

    element.jSetResizeSynchron(false);
    element.jSetResizable(true);
    //svgManager.loadFromFile(element.jGetSourcePath()+"led.svg");

    setName("SVGImage");
    
    file.clearExtensions();
    file.addExtension("svg");

    file.setDescription("SVG Image");
    
    //pnl=configSVGIcon("C:/Dokumente und Einstellungen/Carmelo/Desktop/liftarn_Red_griffin.svg");

  }
  
  public void xOnInit()
  {
    panel=element.getFrontPanel();
    
    panel.setLayout(new BorderLayout());


    loadImage("C:/Dokumente und Einstellungen/Carmelo/Desktop/liftarn_Red_griffin.svg");
    pnl.setScaleToFit(true);

    pnl.setOpaque(false);
    panel.add(pnl);
    
    
   // System.out.println("getBinDir="+element.jGetProjectPath());
  }
  
  public String getBinDir()
  {
    String projektPath=new File(element.jGetProjectPath()).getParentFile().getPath()+"/";
    return projektPath;
  }

  
  public void setPropertyEditor()
  {
    element.jAddPEItem("SVG Datei",file, 0,0);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"SVG File");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"SVG File");
  }

  public void propertyChanged(Object o)
  {
     if (pnl!=null)
     {
        if (o.equals(file))
        {
         loadImage(file.getValue());
        }
     }
  }
  
  

  
  
  public void loadImage(String fileName)
  {
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
      URI uri = SVGCache.getSVGUniverse().loadSVG(new ByteArrayInputStream(imageBytes), "myImage");

      pnl.setAntiAlias(true);
      pnl.setSvgURI(uri);
      element.jRepaint();
      
  }

  
    public void loadFromStream(java.io.FileInputStream fis)
    {
       java.io.DataInputStream dis = new java.io.DataInputStream(fis);
        try
        {
          int size=dis.readInt();
          if (size>0)
          {
              //imageBytes = new byte[size];
              //fis.read(imageBytes);
              
            //URI uri = SVGCache.getSVGUniverse().loadSVG(new ByteArrayInputStream(imageBytes), "myImage");
            URI uri = SVGCache.getSVGUniverse().loadSVG(fis, "myImage");

            pnl.setAntiAlias(true);
            pnl.setSvgURI(uri);
            element.jRepaint();

          } else
          {

          }

        } catch(Exception ex)
        {
          System.out.println(ex);
        }
        
    }



  public void saveToStream(java.io.FileOutputStream fos)
  {

    java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);

    try
    {
      if (imageBytes!=null)
      {
        dos.writeInt(imageBytes.length);
        if (imageBytes.length>0)
        {
          fos.write(imageBytes);
        }
      } else
      {
        dos.writeInt(0);
      }
    } catch(Exception ex)
    {
      //VisualLogic.Tools.showMessage("Error in VSImage.saveToStream() : "+ex.toString());
    }


  }


}
 
