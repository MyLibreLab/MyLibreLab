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

public class Sleep extends JVSMain
{
  private VSInteger time=new VSInteger(); // in ms
  private Image image;
  private javax.swing.Timer timer ;
  
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
    initPins(0,0,0,0);
    setSize(30,30);

    initPinVisibility(false,false,false,false);

    String fileName=element.jGetSourcePath()+"icon.gif";
    image=element.jLoadImage(fileName);
    
    element.jSetCaptionVisible(true);
    element.jSetCaption("Sleep");

    setName("Sleep");
    time.setValue(1);
    

  }

  public void start()
  {
    timer = new javax.swing.Timer(time.getValue(), new ActionListener()
    {
        public void actionPerformed(ActionEvent evt)
        {
          process();
        }
    });

    timer.start();
  }
  public void stop()
  {
   if (timer!=null) timer.stop();
  }

  public void process()
  {
    try
    {
      Thread.sleep(time.getValue());
    } catch(Exception ex){}
  }
  
  

  public void setPropertyEditor()
  {
    element.jAddPEItem("Wartezeit [ms]",time, 1,5000);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"sleep [ms]");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"espera [ms]");
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
      time.loadFromStream(fis);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      time.saveToStream(fos);
  }

}
