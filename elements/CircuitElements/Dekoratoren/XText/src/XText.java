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



public class XText extends JVSMain
{
  private Image image=null;
  private VSString strText = new VSString();
  private VSPropertyDialog ausrichtung = new VSPropertyDialog();
  private VSFont font=new VSFont(new Font("Monospaced",0,11));
  private VSColor fontColor = new VSColor(Color.BLACK);
  private String[] values= new String[3];
  private VSInteger ausrichtungIndex=new VSInteger(0);


  public XText()
  {
    String strLocale=Locale.getDefault().toString();

    if (strLocale.equalsIgnoreCase("de_DE"))
    {
      strText.setValue("Text");
      values[0]="Links";
      values[1]="Mitte";
      values[2]="Rechts";
    }
    if (strLocale.equalsIgnoreCase("en_US"))
    {
      strText.setValue("Text");
      values[0]="Left";
      values[1]="Center";
      values[2]="Right";
    }
    if (strLocale.equalsIgnoreCase("es_ES"))
    {
      strText.setValue("Text");
      values[0]="Left";
      values[1]="Center";
      values[2]="Right";
    }


  }

  public void paint(java.awt.Graphics g)
  {
    if (element!=null)
    {
       Graphics2D g2 = (Graphics2D)g;

       g2.setColor(fontColor.getValue());
       g2.setFont(font.getValue());
       g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
       
       Rectangle r=element.jGetBounds();
       FontMetrics fm = g2.getFontMetrics(font.getValue());
       
       int x=r.x;
       
       int strLen=(int)fm.getStringBounds(strText.getValue(),g).getWidth();

       switch(ausrichtungIndex.getValue())
       {
         case 0 : x=r.x;break;
         case 1 : x=r.width/2-strLen/2  ; break;
         case 2 : x=r.width-strLen; break;
       }
       
       g2.drawString(strText.getValue(),x,r.y+fm.getHeight());
    }
  }
   
  public void init()
  {
    initPins(0,0,0,0);
    setSize(125,30);
    initPinVisibility(false,false,false,false);
    element.jSetResizable(true);
    element.jSetInnerBorderVisibility(false);

    
    setName("Text");
    
    ausrichtung.setText(values[ausrichtungIndex.getValue()]);
    
  }

  public void setPropertyEditor()
  {
    element.jAddPEItem("Schriftart",font, 0,0);
    element.jAddPEItem("Beschreibung",strText, 0,0);
    element.jAddPEItem("Farbe",fontColor, 0,0);
    element.jAddPEItem("Ausrichtung",ausrichtung, 0,0);
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
    element.jSetPEItemLocale(d+3,language,"Align");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Fuente");
    element.jSetPEItemLocale(d+1,language,"Nombre");
    element.jSetPEItemLocale(d+2,language,"Color");
    element.jSetPEItemLocale(d+3,language,"Alineación");
  }
  public void propertyChanged(Object o)
  {
    if (o.equals(ausrichtung))
    {

    String strAuswahl="Auswahl";
    String strAlign="Ausrichtung";

    String strLocale=Locale.getDefault().toString();

    if (strLocale.equalsIgnoreCase("de_DE"))
    {
    }
    if (strLocale.equalsIgnoreCase("en_US"))
    {
      strAuswahl="Choice";
      strAlign="Align";
    }
    if (strLocale.equalsIgnoreCase("es_ES"))
    {
      strAuswahl="Choice";
      strAlign="Align";
    }

      String res=(String)JOptionPane.showInputDialog (null, strAuswahl,strAlign, JOptionPane.QUESTION_MESSAGE, null,values ,values[ausrichtungIndex.getValue()]);      

      if (res!=null)
      {
          for (int i=0;i<values.length;i++)
          {
            if (values[i].equals(res))
            {
              ausrichtungIndex.setValue(i);
              break;
            }
          }
          ausrichtung.setText(values[ausrichtungIndex.getValue()]);
      }

    }
    element.jRepaint();
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
      font.loadFromStream(fis);
      strText.loadFromStream(fis);
      fontColor.loadFromStream(fis);
      ausrichtungIndex.loadFromStream(fis);
      
      ausrichtung.setText(values[ausrichtungIndex.getValue()]);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      font.saveToStream(fos);
      strText.saveToStream(fos);
      fontColor.saveToStream(fos);
      ausrichtungIndex.saveToStream(fos);

  }
}
