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
import java.util.*;



public class XLabel extends JVSMain
{
  private Image image=null;
  private VSString strText = new VSString();
  private VSInteger ausrichtungH = new VSInteger();
  private VSInteger ausrichtungV = new VSInteger();

  private VSFont font=new VSFont(new Font("Monospaced",0,11));
  private VSColor fontColor = new VSColor(Color.BLACK);
  private String[] values= new String[3];
  private JLabel label = new JLabel("Label");


  public XLabel()
  {
    strText.setValue("Text");

    myInit();
  }
  
  public void myInit()
  {

    strText.setValue(label.getText());
    font.setValue(label.getFont());
    fontColor.setValue(label.getForeground());
    //strText.setText(label.getValue());
    int hali=label.getHorizontalAlignment();
    if (hali==label.LEFT)
    {
      ausrichtungH.setValue(0);
    }
    if (hali==label.CENTER)
    {
      ausrichtungH.setValue(1);
    }
    if (hali==label.RIGHT)
    {
      ausrichtungH.setValue(2);
    }

    int vali=label.getVerticalAlignment();
    if (vali==label.TOP)
    {
      ausrichtungV.setValue(0);
    }
    if (vali==label.CENTER)
    {
      ausrichtungV.setValue(1);
    }
    if (vali==label.BOTTOM)
    {
      ausrichtungV.setValue(2);
    }

  }


  public void paint(java.awt.Graphics g)
  {
  }
   
  public void init()
  {
    initPins(0,0,0,0);
    setSize(40,40);
    initPinVisibility(false,false,false,false);
    element.jSetResizable(true);
    element.jSetInnerBorderVisibility(false);
    setSize(125,30);
    
    setName("XLabel Version 1.0");
    

  }


  public void xOnInit()
  {
    try
    {
      JPanel panel =element.getFrontPanel();
      panel.setLayout(new java.awt.BorderLayout());

      panel.add(label, java.awt.BorderLayout.CENTER);
      element.setAlwaysOnTop(true);
    } catch(Exception ex)
    {
      System.out.println(ex);
    }

  }


  public void setPropertyEditor()
  {
    element.jAddPEItem("Schriftart",font, 0,0);
    element.jAddPEItem("Beschreibung",strText, 0,0);
    element.jAddPEItem("Farbe",fontColor, 0,0);
    element.jAddPEItem("Ausrichtung Hoz",ausrichtungH, 0,2);
    element.jAddPEItem("Ausrichtung Vert",ausrichtungV, 0,2);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Font");
    element.jSetPEItemLocale(d+1,language,"Text");
    element.jSetPEItemLocale(d+2,language,"Color");
    element.jSetPEItemLocale(d+3,language,"Align Hoz");
    element.jSetPEItemLocale(d+4,language,"Align Vert");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Fuente");
    element.jSetPEItemLocale(d+1,language,"Nombre");
    element.jSetPEItemLocale(d+2,language,"Color");
    element.jSetPEItemLocale(d+3,language,"Alineación Hoz");
    element.jSetPEItemLocale(d+4,language,"Alineación Vert");

  }
  public void propertyChanged(Object o)
  {
    //if (o.equals(strText))
    {
      label.setText(strText.getValue());
    }
    //if (o.equals(font))
    {
      label.setFont(font.getValue());
    }
    //if (o.equals(fontColor))
    {
      label.setForeground(fontColor.getValue());
    }
    //if (o.equals(ausrichtungH))
    {
      int hali=ausrichtungH.getValue();
      if (hali==0)
      {
        label.setHorizontalAlignment(label.LEFT);
      }
      if (hali==1)
      {
        label.setHorizontalAlignment(label.CENTER);
      }
      if (hali==2)
      {
        label.setHorizontalAlignment(label.RIGHT);
      }
    }

    //if (o.equals(ausrichtungV))
    {
      int vali=ausrichtungV.getValue();
      if (vali==0)
      {
        label.setVerticalAlignment(label.TOP);
      }
      if (vali==1)
      {
        label.setVerticalAlignment(label.CENTER);
      }
      if (vali==2)
      {
        label.setVerticalAlignment(label.BOTTOM);
      }
    }
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
      font.loadFromStream(fis);
      strText.loadFromStream(fis);
      fontColor.loadFromStream(fis);
      ausrichtungH.loadFromStream(fis);
      ausrichtungV.loadFromStream(fis);

     propertyChanged(null);

  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      font.saveToStream(fos);
      strText.saveToStream(fos);
      fontColor.saveToStream(fos);
      ausrichtungH.saveToStream(fos);
      ausrichtungV.saveToStream(fos);
  }
}
