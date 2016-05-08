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
import java.awt.geom.*;
import javax.swing.*;

public class AnalogAnzeigePanel extends JVSMain implements PanelIF
{
  public double value0;
  private VSInteger min=new VSInteger(0);
  private VSInteger max=new VSInteger(100);
  private VSInteger abschnitte= new VSInteger(10);
  private VSColor backgroundColor = new VSColor(new Color(250,220,140));
  private VSColor nibbleColor = new VSColor(new Color(190,250,190));
  private VSColor pointerColor = new VSColor(Color.BLACK);
  private VSBoolean transparent = new VSBoolean(false);


  // aus PanelIF
  public void processPanel(int pinIndex, double value, Object obj)
  {
    value0=value;
  }
  

    public void drawAnzeige(java.awt.Graphics g,int x, int y, int w, int h)
    {
        int mitteX=x+(w/2);
        int mitteY=y+h-5;

        
        double value=value0;

        double f;
        double grad;
        double angle;
        
        if (transparent.getValue()==false)
        {
          g.setColor(backgroundColor.getValue());
          g.fillRect(x,y, w,h);
        }


        int x1=h-9;
        int y1=0;

        double c;
        int x2;
        int y2;
        
        int mX=w/2;
        int mY=h;

        if (transparent.getValue()==false)
        {

          double distance=100.0/(double)abschnitte.getValue();
          for (double i=0.0;i<=100.0;i+=distance)
          {
            f=118.0/ 100.0;
            grad=29.0+(i*f);

            angle=Math.toRadians(grad);

            c=Math.sqrt((x1*x1)+(y1*y1));
            x2=(int)( c*Math.cos(angle) );
            y2=(int)( c*Math.sin(angle) );

            g.setColor(Color.BLACK);
            g.drawLine(mX,mY, mX-x2-1, mY-y2);
          }

          x1=h-10;
          y1=0;


          int cc=mX-15;
          g.setColor(backgroundColor.getValue());
          g.fillOval(mX-cc,mY-cc,cc*2,cc*2);

          g.setColor(Color.BLACK);
          g.drawArc(mX-cc,mY-cc,cc*2,cc*2, 33,118);

        }

        value=value0;
        f=(118.0)/ ( (double)max.getValue()-(double)min.getValue() );
        grad=29.0+(value-(double)min.getValue())*f;
        angle=Math.toRadians(grad);


        c=Math.sqrt((x1*x1)+(y1*y1));
        x2=(int)( c*Math.cos(angle) );
        y2=(int)( c*Math.sin(angle) );


        g.setColor(pointerColor.getValue());
        g.drawLine(mX,mY, mX-x2-1, mY-y2);


        int p=h/2;
        
        if (transparent.getValue()==false)
        {
        
          g.setColor(Color.BLACK);
          g.drawRect(x,y, w,h);

          java.awt.Font fnt = new java.awt.Font("Courier", 1, 9);
          g.setFont(fnt);

          FontMetrics fm = g.getFontMetrics();
          Rectangle2D   r = fm.getStringBounds(""+(int)max.getValue(),g);

          g.drawString(""+(int)min.getValue(),x+5,y+p);
          g.drawString(""+(int)max.getValue(),x+w-5-(int)r.getWidth(),y+p);

        }
        double dblProzent15 = ((double)h/100.0)*15;
        p=(int)dblProzent15;

        g.setColor(nibbleColor.getValue());
        g.fillArc(x+w/2-p,y+h-p, p*2,p*2, 0,180);
        g.setColor(java.awt.Color.black);
        g.drawArc(x+w/2-p,y+h-p, p*2,p*2, 0,180);


    }


    public void paint(java.awt.Graphics g)
    {
      if (element!=null)
      {
         Rectangle bounds=element.jGetBounds();
         drawAnzeige(g,0,0,bounds.width-1,bounds.height-1);
      }
    }

    public void init()
    {
      initPins(0,0,0,0);
      setSize(200,100);
      initPinVisibility(false,false,false,false);
      setName("Analog Anzeige (Panel)");
      element.jSetResizable(true);
      element.jSetAspectRatio(100.0/200.0);
      element.jSetResizeSynchron(true);

      element.jSetInnerBorderVisibility(false);
      element.jSetMinimumSize(100,50);
    }
    

  public void setPropertyEditor()
  {
    element.jAddPEItem("Min",min, -9999999,9999999);
    element.jAddPEItem("Max",max, -9999999,9999999);
    element.jAddPEItem("Abschnitte",abschnitte, 1,100);
    element.jAddPEItem("Hintergrung-Farbe",backgroundColor, 0,0);
    element.jAddPEItem("Nibble-Farbe",nibbleColor, 0,0);
    element.jAddPEItem("Zeiger-Farbe",pointerColor, 0,0);
    element.jAddPEItem("Transparent",transparent, 0,0);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Min");
    element.jSetPEItemLocale(d+1,language,"Max");
    element.jSetPEItemLocale(d+2,language,"Stages");
    element.jSetPEItemLocale(d+3,language,"Background Color");
    element.jSetPEItemLocale(d+4,language,"Nibble Color");
    element.jSetPEItemLocale(d+5,language,"Pointer Color");
    element.jSetPEItemLocale(d+6,language,"Transparent");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Min");
    element.jSetPEItemLocale(d+1,language,"Max");
    element.jSetPEItemLocale(d+2,language,"Divisiones");
    element.jSetPEItemLocale(d+3,language,"Color Fondo");
    element.jSetPEItemLocale(d+4,language,"Color Centro");
    element.jSetPEItemLocale(d+5,language,"Color Aguja");
    element.jSetPEItemLocale(d+6,language,"Transparente");
  }
  public void propertyChanged(Object o)
  {
    element.jRepaint();
  }





    public void openPropertyDialog()
    {
          String strMax =  (String)JOptionPane.showInputDialog(
                          null,
                          "Geben Sie den Maximalwert ein?",
                          "Max",
                          JOptionPane.QUESTION_MESSAGE,
                          null,
                          null,
                          null);

          max.setValue(100);

          if (strMax!=null && strMax.length()>0)
          {
              try
              {
                max.setValue(Integer.parseInt(strMax));
              }catch (NumberFormatException nfe)
              {
                max.setValue(100);
                element.jRepaint();
              }
          }


    }

    public void loadFromStream(java.io.FileInputStream fis)
    {
        min.loadFromStream(fis);
        max.loadFromStream(fis);
        abschnitte.loadFromStream(fis);
        backgroundColor.loadFromStream(fis);
        nibbleColor.loadFromStream(fis);
        pointerColor.loadFromStream(fis);
        transparent.loadFromStream(fis);
    }

    public void saveToStream(java.io.FileOutputStream fos)
    {
        min.saveToStream(fos);
        max.saveToStream(fos);
        abschnitte.saveToStream(fos);
        backgroundColor.saveToStream(fos);
        nibbleColor.saveToStream(fos);
        pointerColor.saveToStream(fos);
        transparent.saveToStream(fos);
    }


}

