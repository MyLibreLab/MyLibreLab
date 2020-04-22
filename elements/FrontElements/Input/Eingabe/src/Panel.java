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
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import tools.*;
import java.awt.geom.Rectangle2D;
import java.util.*;

public class Panel extends JVSMain
{
  private int width=150, height=25;
  private String value="";
  private VSString initValue=new VSString("");
  private VSBoolean passwd=new VSBoolean();
  private double oldPin;
  private Font fnt = new Font("Courier",0,12);
  private ExternalIF circuitElement;


   public void drawBorder(Graphics g,Rectangle bounds)
   {
     g.setColor(Color.WHITE);
     g.fillRect(bounds.x,bounds.y,bounds.width,bounds.height);

     g.setColor(Color.DARK_GRAY);
     g.drawLine(bounds.x+1,bounds.y+1,bounds.width-1,bounds.y+1);
     g.drawLine(bounds.x+1,bounds.y+1,bounds.x+1,bounds.height-1);

     g.setColor(Color.LIGHT_GRAY);
     g.drawLine(bounds.x+1,bounds.height-1,bounds.width-1,bounds.height-1);
     g.drawLine(bounds.width-1,bounds.y+1,bounds.width-1,bounds.height-1);

     g.setColor(Color.BLACK);
     g.drawRect(bounds.x,bounds.y,bounds.width,bounds.height);
   }
   
   public void paint(java.awt.Graphics g)
   {
    if (element!=null)
    {
       Rectangle bounds=element.jGetBounds();

       g.setFont(fnt);
       g.setColor(Color.black);

       Graphics2D g2=(Graphics2D)g;

       FontMetrics fm = g.getFontMetrics();
       Rectangle2D   r = fm.getStringBounds(value,g2);

       drawBorder(g,bounds);
       if (passwd.getValue()==true)
       {
         String str="";
         for (int i=0;i<value.length();i++) str+="*";
         g.drawString(str,bounds.x+5,((bounds.height) /2)+5);
       } else
       {
         g.drawString(value,bounds.x+5,((bounds.height) /2)+5);
       }

    }
   }
   



  public void init()
  {
    initPins(0,0,0,0);
    setSize(width,height);
    initPinVisibility(false,false,false,false);
    element.jSetInnerBorderVisibility(false);
    
    element.jSetResizable(true);
    setName("Eingabe");
  }

  public void setPropertyEditor()
  {
    element.jAddPEItem("Anfangs-Wert",initValue, 0,0);
    element.jAddPEItem("Passwort",passwd, 0,0);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Init-Value");
    element.jSetPEItemLocale(d+1,language,"Password");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Valor Inicial");
    element.jSetPEItemLocale(d+1,language,"Password");
  }
  
  
  public void propertyChanged(Object o)
  {
    value=initValue.getValue();
    element.jRepaint();
  }
  

  public void mousePressed(MouseEvent e)
  {
    String strTitle="Text";
    String strTitle2="Wert";

    String strLocale=Locale.getDefault().toString();

    if (strLocale.equalsIgnoreCase("en_US"))
    {
        strTitle="Text";
        strTitle2="Value";
    }
    if (strLocale.equalsIgnoreCase("es_ES"))
    {
        strTitle="Texto";
        strTitle2="Valor";
    }


    if (e.getClickCount()==2)
    {
      String str =  (String)JOptionPane.showInputDialog(
                            null,
                            strTitle2,
                            strTitle,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            null,
                            value);
      if (str!=null)
      {
        value=str;
        element.jRepaint();

        circuitElement=element.getCircuitElement();
        circuitElement.Change(0,new VSString(value));

      }

    }

  }

  
  public void start()
  {
    value=initValue.getValue();
    element.jRepaint();
    
    circuitElement=element.getCircuitElement();
    circuitElement.Change(0,new VSString(value));
  }
  
  public void loadFromStream(java.io.FileInputStream fis)
  {
      initValue.loadFromStream(fis);
      value=initValue.getValue();
      element.jRepaint();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      initValue.saveToStream(fos);
  }



}

