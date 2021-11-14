package com.github.mylibrelab.elements.front.Output.Scrollmage.src;//*****************************************************************************
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
import java.awt.image.MemoryImageSource;

import VisualLogic.*;
import java.awt.*;
import javax.swing.*;

import tools.*;
import VisualLogic.variables.*;




public class Panel extends JVSMain implements PanelIF
{
  private boolean on=false;
   private VSBoolean interpolation = new VSBoolean(false);
  private VSImage24 in;
  private Image img=null;
  private MyImage image=null;
  private JPanel panel;

  public void onDispose()
  {
   if (img!=null)
   {
     img.flush();
     img=null;
   }
  }

  public void processPanel(int pinIndex, double value, Object obj)
  {


     if (obj!=null)
     {
       try{
       in=(VSImage24)obj;
       img= Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(in.getWidth(),in.getHeight(),in.getPixels(), 0, in.getWidth()) );
       image.setImage(img);

       }catch(Exception e){

       }
     } else
     {

     }

  }

   public void paint(java.awt.Graphics g)
   {

   }

  public void setPropertyEditor()
  {
    element.jAddPEItem("Interpolation",interpolation, 0,0);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Interpolation");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Interpolaci�n");
  }

  public void propertyChanged(Object o)
  {
    element.jRepaint();
  }

  public void init()
  {
    initPins(0,0,0,0);
    initPinVisibility(false,false,false,false);
    setSize(150,150);
    element.jSetInnerBorderVisibility(false);

    //element.jSetResizeSynchron(true);
    element.jSetResizable(true);
    setName("ScrollImage");
  }

  public void xOnInit()
  {
    panel=element.getFrontPanel();

    panel.setLayout(new BorderLayout());

    image=new MyImage();
    JScrollPane bar = new JScrollPane(image);
    panel.add(bar,BorderLayout.CENTER);
    element.setAlwaysOnTop(true);


  }



  public void loadFromStream(java.io.FileInputStream fis)
  {
      interpolation.loadFromStream(fis);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      interpolation.saveToStream(fos);
  }



}

