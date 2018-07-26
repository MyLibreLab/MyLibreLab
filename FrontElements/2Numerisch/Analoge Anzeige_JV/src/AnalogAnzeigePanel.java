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
import java.awt.geom.*;
import javax.swing.*;

public class AnalogAnzeigePanel extends JVSMain implements PanelIF
{
  public double value0;
  private VSInteger min=new VSInteger(0);
  private VSInteger max=new VSInteger(100);
  private VSInteger stages= new VSInteger(10);
  
  private VSColorAdvanced backgroundColor = new VSColorAdvanced();
  private VSColorAdvanced strokeBaseColor = new VSColorAdvanced();
  private VSColor nibbleColor = new VSColor(new Color(204,51,0));
  private VSColor pointerColor = new VSColor(new Color(253,153,0));
  private VSColor fontColor = new VSColor(Color.BLACK);
  private VSFont font = new VSFont(new Font("Dialog",1,11));
  private VSColor stagesColor = new VSColor(new Color(255,242,181));
  private VSBoolean transparent = new VSBoolean(false);
  
  private VSInteger strokeValueStages= new VSInteger(3);
  private VSInteger strokeValuePointer= new VSInteger(4);
  private VSInteger strokeValueBase= new VSInteger(10);




  // aus PanelIF
  public void processPanel(int pinIndex, double value, Object obj)
  {
    value0=value;
  }
  

    public void drawAnzeige(java.awt.Graphics g,int x, int y, int w, int h)
    {
         Graphics2D g2 = (Graphics2D)g;
        
        int mitteX=x+(w/2);
        int mitteY=y+h-5;
        
        double value=value0;

        double f;
        double grad;
        double angle;

        int x1=h-9;
        int y1=0;

        double c;
        int x2;
        int y2;
        
        int mX=w/2;
        int mY=h;

        if (transparent.getValue()==false)
        {
          backgroundColor.setFillColor(g2);
          g2.fillRoundRect(x,y, w,h,20,20);

          double distance=100.0/(double)stages.getValue();
          for (double i=0.0;i<=100.0;i+=distance)
          {
            f=118.0/ 100.0;
            grad=29.0+(i*f);

            angle=Math.toRadians(grad);

            c=Math.sqrt((x1*x1)+(y1*y1));
            x2=(int)( c*Math.cos(angle) );
            y2=(int)( c*Math.sin(angle) );

            g2.setColor(stagesColor.getValue());
            g2.setStroke(new BasicStroke(strokeValueStages.getValue()));
            g2.drawLine(mX,mY, mX-x2-1, mY-y2);
            g2.setStroke(new BasicStroke(1));
            g2.setColor(Color.BLACK);
            g2.drawLine(mX,mY, mX-x2-1, mY-y2);
            
          }

          x1=h-10;
          y1=0;


          int cc=mX-15;
          backgroundColor.setFillColor(g2);
          g2.fillOval(mX-cc,mY-cc,cc*2,cc*2); // Hide Lines

          g2.setColor(stagesColor.getValue());
          g2.setStroke(new BasicStroke(strokeValueStages.getValue()));
          g2.drawArc(mX-cc,mY-cc,cc*2,cc*2, 33,118);
          g2.setStroke(new BasicStroke(1));
          g2.setColor(Color.BLACK);
          g2.drawArc(mX-cc,mY-cc,cc*2,cc*2, 33,118);

        }

        value=value0;
        f=(118.0)/ ( (double)max.getValue()-(double)min.getValue() );
        grad=29.0+(value-(double)min.getValue())*f;
        angle=Math.toRadians(grad);


        c=Math.sqrt((x1*x1)+(y1*y1));
        x2=(int)( c*Math.cos(angle) );
        y2=(int)( c*Math.sin(angle) );


        g2.setColor(pointerColor.getValue());
        g2.setStroke(new BasicStroke(strokeValuePointer.getValue()));
        g2.drawLine(mX,mY, mX-x2-1, mY-y2);
        g2.setStroke(new BasicStroke(1));
        g2.setColor(Color.BLACK);
        g2.drawLine(mX,mY, mX-x2-1, mY-y2);
        


        int p=h/3;
        
        if (transparent.getValue()==false)
        {
        
          strokeBaseColor.setFillColor(g2);
          g2.setStroke(new BasicStroke(strokeValueBase.getValue()));
          g2.drawRoundRect(x,y, w,h,20,20);
          g2.setStroke(new BasicStroke(1));

          g2.setFont(font.getValue());

          FontMetrics fm = g2.getFontMetrics();
          Rectangle2D   r = fm.getStringBounds(""+(int)max.getValue(),g2);
          g2.setColor(fontColor.getValue());
          g2.drawString(""+(int)min.getValue(),x+5+strokeValueBase.getValue()/2,y+p);
          g2.drawString(""+(int)max.getValue(),x+w-5-(int)r.getWidth()-+strokeValueBase.getValue()/2,y+p);

        }
        double dblProzent15 = ((double)h/100.0)*15;
        p=(int)dblProzent15;

        g2.setColor(nibbleColor.getValue());
        g2.fillArc(x+w/2-p,y+h-p, p*2,p*2, 5,175);
        
        g2.setStroke(new BasicStroke(strokeValuePointer.getValue()));
        g2.setColor(pointerColor.getValue());
        g2.drawArc(x+w/2-p,y+h-p, p*2,p*2, 5,175);
        
        g2.setStroke(new BasicStroke(1));
        g2.setColor(java.awt.Color.black);
        g2.drawArc(x+w/2-p,y+h-p, p*2,p*2, 5,175);
        
        if(transparent.getValue()==false){
        strokeBaseColor.setFillColor(g2);
        g2.setStroke(new BasicStroke(strokeValueBase.getValue()));
        g2.drawRoundRect(x,y, w,h,20,20);
        g2.setStroke(new BasicStroke(1));
        g2.setColor(Color.black);
        int Offset=strokeValueBase.getValue()/2;
        g2.drawRoundRect(x+Offset,y+Offset, w-2*Offset,h-2*Offset,20,20);
        }


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
      setName("Analog Anzeige JV");
      element.jSetResizable(true);
      element.jSetAspectRatio(100.0/200.0);
      element.jSetResizeSynchron(true);

      element.jSetInnerBorderVisibility(false);
      element.jSetMinimumSize(100,50);
      
      backgroundColor.color1= new Color(204,204,204);
      strokeBaseColor.color1=new Color(102,102,102);
      strokeBaseColor.color2=new Color(255,255,255);
      strokeBaseColor.p1=new Point(25,0);
      strokeBaseColor.p2=new Point(100,0);
      strokeBaseColor.modus=1;
      strokeBaseColor.wiederholung=true;
      
    }
    

  public void setPropertyEditor()
  {
    element.jAddPEItem("Min",min, -9999999,9999999);
    element.jAddPEItem("Max",max, -9999999,9999999);
    element.jAddPEItem("Abschnitte",stages, 1,100);
    element.jAddPEItem("Font",font, 0,0);
    element.jAddPEItem("Font-Farbe",fontColor, 0,0);
    element.jAddPEItem("Hintergrung-Farbe",backgroundColor, 0,0);
    element.jAddPEItem("Stages-Farbe",stagesColor, 0,0);
    element.jAddPEItem("Nibble-Farbe",nibbleColor, 0,0);
    element.jAddPEItem("Zeiger-Farbe",pointerColor, 0,0);
    element.jAddPEItem("Zeiger-Stroke",strokeValuePointer, 0,50);
    
    element.jAddPEItem("Base Stroke-Value",strokeValueBase, 0,50);
    element.jAddPEItem("Base Stroke-Farbe",strokeBaseColor, 0,0);
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
    element.jSetPEItemLocale(d+3,language,"Font");
    element.jSetPEItemLocale(d+4,language,"Font Color");
    element.jSetPEItemLocale(d+5,language,"Background Color");
    element.jSetPEItemLocale(d+6,language,"Stages Color");
    element.jSetPEItemLocale(d+7,language,"Nibble Color");
    element.jSetPEItemLocale(d+8,language,"Pointer Color");
    element.jSetPEItemLocale(d+9,language,"Pointer Stroke");
    element.jSetPEItemLocale(d+10,language,"Base Stroke");
    element.jSetPEItemLocale(d+11,language,"Base Stroke Color");
    element.jSetPEItemLocale(d+12,language,"Transparent");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Min");
    element.jSetPEItemLocale(d+1,language,"Max");
    element.jSetPEItemLocale(d+2,language,"Divisiones");
    element.jSetPEItemLocale(d+3,language,"Fuente");
    element.jSetPEItemLocale(d+4,language,"Color Texto");
    element.jSetPEItemLocale(d+5,language,"Color Fondo");
    element.jSetPEItemLocale(d+6,language,"Color Divisiones");
    element.jSetPEItemLocale(d+7,language,"Color Centro");
    element.jSetPEItemLocale(d+8,language,"Color Aguja");
    element.jSetPEItemLocale(d+9,language,"Espesor Aguja");
    element.jSetPEItemLocale(d+10,language,"Espesor Borde");
    element.jSetPEItemLocale(d+11,language,"Color Espesor Borde");
    element.jSetPEItemLocale(d+12,language,"Transparente");
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
        stages.loadFromStream(fis);
        backgroundColor.loadFromStream(fis);
        nibbleColor.loadFromStream(fis);
        pointerColor.loadFromStream(fis);
        transparent.loadFromStream(fis);
        
    stagesColor.loadFromStream(fis);
    strokeValuePointer.loadFromStream(fis);
    strokeValueBase.loadFromStream(fis);
    strokeBaseColor.loadFromStream(fis);
    font.loadFromStream(fis);
    fontColor.loadFromStream(fis);
        
    }

    public void saveToStream(java.io.FileOutputStream fos)
    {
        min.saveToStream(fos);
        max.saveToStream(fos);
        stages.saveToStream(fos);
        backgroundColor.saveToStream(fos);
        nibbleColor.saveToStream(fos);
        pointerColor.saveToStream(fos);
        transparent.saveToStream(fos);
        
        stagesColor.saveToStream(fos);
        strokeValuePointer.saveToStream(fos);
        strokeValueBase.saveToStream(fos);
        strokeBaseColor.saveToStream(fos);
        font.saveToStream(fos);
        fontColor.saveToStream(fos);
    
    }
    


}

