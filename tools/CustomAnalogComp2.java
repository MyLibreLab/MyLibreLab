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

package tools;

import java.io.IOException;
import VisualLogic.*;
import VisualLogic.variables.*;
import tools.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.text.*;
import java.awt.geom.Rectangle2D;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;



public class CustomAnalogComp2 extends JVSMain
{
  public VSDouble value0=new VSDouble();
  public VSDouble initValue=new VSDouble();
  public Graphics2D gb =null;
  private int oldH=0,oldW=0;
  public double oldValue;
  public VSDouble min=new VSDouble(0);
  public VSDouble max=new VSDouble(100);
  private ArrayList listeBeschriftungen = new ArrayList();
  public VSInteger abschnitte=new VSInteger(10);
  public VSInteger abstand=new VSInteger(15);
  public VSInteger knobSizeInProzent=new VSInteger(30);
  public VSInteger nibbleLenInProzent=new VSInteger(30);
  public VSInteger nibbleCircleSizeInProzent=new VSInteger(5);
  public VSBoolean showText = new VSBoolean(false);
  public VSDouble minGrad = new VSDouble(-45);
  public VSDouble maxGrad = new VSDouble(270);
  public VSColor backColor=new VSColor(Color.WHITE);
  public VSColor buttonColor=new VSColor(new Color(190,250,190));
  public VSColor nibbleColor=new VSColor(Color.BLACK);
  public VSColor lineColor=new VSColor(Color.BLACK);
  public  VSColor fontColor=new VSColor(Color.BLACK);
  public VSString formatierung = new VSString("#0");
  public VSBoolean textInside = new VSBoolean(false);
  public VSBoolean showBackground = new VSBoolean(false);
  public VSBoolean showNibbleAsCircle= new VSBoolean(false);
  public VSFont font = new VSFont(new java.awt.Font("Courier", 1, 10));
  public VS1DString values = new VS1DString(0);
  public VSPropertyDialog captions= new VSPropertyDialog();
  public VSBoolean transparent = new VSBoolean(false);
  public VSBoolean onlyNumbers = new VSBoolean(true);
  private boolean firstTime=true;
  private FontMetrics fm ;
  private Rectangle2D   r ;
  private DecimalFormat df = new DecimalFormat(formatierung.getValue());
  private double theAngle=0.0;
  private ExternalIF circuitElement;
  private VSImage spitze = new VSImage();
  

  public void setValue(double value)
  {
    value0.setValue(value);
    element.jRepaint();
  }

  public void onDispose()
  {
    if (spitze.getImage()!=null)
    {
     spitze.getImage().flush();
    }

  }

  public Point getPointFromAngle(double angle,double vectorLaenge)
  {
    Point p = new Point();
    p.x=(int)( vectorLaenge*Math.cos(angle) );
    p.y=(int)( vectorLaenge*Math.sin(angle) );

    return p;
  }
  
  public void initSubElements()
  {

        double f,grad,angle;
        int counter=0;

        Rectangle bounds=element.jGetBounds();
        int w=bounds.width-2;
        int h=bounds.height-2;
        int x1=w/2;
        int y1=0;
        int mitteX=1+(w/2);
        int mitteY=1+(h/2);

        double vectorLaenge=Math.sqrt((x1*x1)+(y1*y1))-5;
        double vectLen=vectorLaenge+abstand.getValue();
        double vectLen1=vectorLaenge+7.0;

        if (textInside.getValue())
        {
          vectLen=vectorLaenge-abstand.getValue();
          vectLen1=vectorLaenge-7.0;
        }

        
        if (gb!=null)
        {

          double dis=100.0/(double)abschnitte.getValue();
          for (double i=0;i<=100.0;i+=dis)
          {
            if (counter<listeBeschriftungen.size())
            {
              String strValue=values.getValue(counter);

              f=(maxGrad.getValue()/100.0);
              grad = minGrad.getValue()+i*f;
              angle=Math.toRadians(grad);

              Rectangle2D rx = fm.getStringBounds(strValue,gb);

              int strMitteX=(int)rx.getWidth()/2;
              int strMitteY=(int)rx.getHeight()/2;

              Point p3=getPointFromAngle(angle,vectLen);

              //Beschriftung panel=new Beschriftung(this,strValue);
              
              JPanel panel=(JPanel)listeBeschriftungen.get(counter);
              panel.setSize((int)rx.getWidth(),(int)rx.getHeight());
              Point p =element.jGetLocation();
              panel.setLocation(p.x+mitteX-p3.x-strMitteX,  p.y+mitteY-p3.y-strMitteY);
              

              //Beschriftung bes = (Beschriftung) panel;
              //bes.text="as"+strValue;

              //element.jSetSubElementSize(counter,(int)rx.getWidth(),(int)rx.getHeight());
              //element.jSetSubElementPosition(counter,mitteX-p3.x-strMitteX, mitteY-p3.y-strMitteY);
              //element.jSetSubElementVisible(counter,true);

              counter++;
            }
          }
          
        }
  }
  
  
  
  public void drawAnzeige(java.awt.Graphics gx,int x, int y, int w, int h)
  {
      int mitteX=x+(w/2);
      int mitteY=y+(h/2);

      Graphics2D g = (Graphics2D)gx;
      
      

      
      gb = g;
      g.setFont(font.getValue());

      fm = g.getFontMetrics();
      r = fm.getStringBounds(""+(int)max.getValue(),g);
      
      int distance=8;
      double f;
      double grad;
      double angle;
      double c;
      double value;
      int x1,y1,x2,y2;

      x1=w/2;
      y1=0;


      for (int i=0;i<listeBeschriftungen.size();i++)
      {
        Beschriftung2 bes=(Beschriftung2)listeBeschriftungen.get(i);
        bes.visible=showText.getValue();
        //element.jSetSubElementVisible(i,showText.getValue());
      }
      initSubElements();



      distance=w*5/100;

      if (transparent.getValue()==false)
      {
        if (showBackground.getValue())
        {
          g.setColor(backColor.getValue());
          g.fillOval(distance,distance,w-distance*2,h-distance*2);
          g.setColor(Color.BLACK);
          g.drawOval(distance,distance,w-distance*2,h-distance*2);
        }
      }

      g.setColor(lineColor.getValue());
      if (showText.getValue())
      {
        g.drawArc(distance,distance,w-distance*2+2,h-distance*2+2, 180-(int)minGrad.getValue(),-(int)maxGrad.getValue());
        distance=(int) (  ((double)w)/100.0 * (double)knobSizeInProzent.getValue() );
      } else distance=w/2;

      if (transparent.getValue()==false)
      {
        g.setColor(buttonColor.getValue());
        g.fillOval(mitteX-distance,mitteY-distance,2*distance,2*distance);
        //g.setColor(Color.black);
        //g.drawOval(mitteX-distance,mitteY-distance,2*distance,2*distance);
      }
        // Draw Linien
        x1=w/2;
        y1=0;
        int d2=w*5/100;
        double vectorLaenge=Math.sqrt((x1*x1)+(y1*y1))-(double)d2+2.0;


        if (showText.getValue())
        {
          int counter=0;
          double vectLen=vectorLaenge+abstand.getValue();
          double vectLen1=vectorLaenge+(double)d2;

          if (textInside.getValue())
          {
            vectLen=vectorLaenge-abstand.getValue();
            vectLen1=vectorLaenge-(double)d2;
          }

          double disVal=((double)(max.getValue()-min.getValue()) )/(double)abschnitte.getValue();

          double dis=100.0/(double)abschnitte.getValue();
          for (double i=0;i<=100.0;i+=dis)
          {
          f=(-maxGrad.getValue()/100.0);
          grad = (180.0-minGrad.getValue())+i*f;
          angle=Math.toRadians(grad);

          Point p1=getPointFromAngle(angle,vectLen1);
          Point p2=getPointFromAngle(angle,vectorLaenge);

          g.setColor(lineColor.getValue());
          g.drawLine(mitteX+p1.x,mitteY-p1.y,mitteX+p2.x,mitteY-p2.y);

        }
      }

      
      if (firstTime==true || oldW!=w || oldH!=h)
      {
        initSubElements();
        firstTime=false;
      }

      oldH=h;
      oldW=w;

      // Draw Spitze
      value=value0.getValue()-min.getValue();

      f=(maxGrad.getValue())/(max.getValue()-min.getValue());
      grad = (minGrad.getValue())+value*f;
      angle=Math.toRadians(grad);

      distance=(int) ( ((double)w)/100.0 * (double)nibbleLenInProzent.getValue() );

      x1=distance;
      y1=0;
      c=Math.sqrt((x1*x1)+(y1*y1));
      x2=(int)( c*Math.cos(angle) );
      y2=(int)( c*Math.sin(angle) );


      if (value0.getValue()>=min.getValue() && value0.getValue()<=max.getValue())
      {
        g.setColor(nibbleColor.getValue());
        
        if (showNibbleAsCircle.getValue())
        {
          int disX=(int) ( ((double)w)/100.0 * (double)nibbleCircleSizeInProzent.getValue() );
          g.fillOval(mitteX-x2-1-disX, mitteY-y2-disX,disX*2, disX*2);
          g.setColor(Color.BLACK);
          g.drawOval(mitteX-x2-1-disX, mitteY-y2-disX,disX*2, disX*2);
        } else
        {
            AffineTransform origTransform = g.getTransform();

            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            
            g.rotate(-3.14+angle,mitteX,mitteY);

            int ws=spitze.getImage().getWidth(null);
            int hs=spitze.getImage().getHeight(null);

            double z = (double)( (double)w/(double)ws*2)*(double)nibbleLenInProzent.getValue()/100.0 ;


            g.translate(mitteX,mitteY);
            
            g.scale(z,z);


            if (spitze.getImage()!=null) g.drawImage(spitze.getImage(), (int)( (-ws/2.0) ),(int)( (-hs/2) ) ,null);

            g.setTransform(origTransform);
            
          //g.drawLine(mitteX-1,mitteY, mitteX-x2-1, mitteY-y2);
        }


      }
  }


  public void generateFromNumbersValues()
  {
    int counter=0;

    values.resize(abschnitte.getValue()+1);
    double dblVal=min.getValue();
    double disVal=((double)(max.getValue()-min.getValue()) )/(double)abschnitte.getValue();

    double value=0;
    double dis=100.0/(double)abschnitte.getValue();

    for (double i=0;i<=100.0;i+=dis)
    {
      String strValue = df.format(dblVal);
      dblVal+=disVal;

      values.setValue(counter,strValue);
      counter++;
    }
  }

    public void paint(java.awt.Graphics g)
    {
     if (element!=null)
     {
        Rectangle bounds=element.jGetBounds();
        //g.setColor(Color.BLACK);
        //g.drawString("X_"+value0.getValue(),20,20);
        drawAnzeige(g,1,1,bounds.width-2,bounds.height-2);

     }
     
    }

    public void init()
    {
      initPins(0,0,0,0);
      setSize(180,180);
      initPinVisibility(false,false,false,false);
      element.jSetInnerBorderVisibility(false);

      element.jSetResizeSynchron(true);
      element.jSetResizable(true);
      setName("Regler");
      showText.setValue(true);
      element.jSetMinimumSize(10,10);


      generateFromNumbersValues();
      generateFromValuesSubElements();
      captions.setText(getTextWithKomma());
      
      firstTime=true;

      spitze.loadImage(element.jGetSourcePath()+"Spitze.png");
      
      element.jSetInfo("Carmelo Salafia","Open Source 2006 & Freeware","");
      
    }
    
    public void xOnInit()
    {
      generateFromValuesSubElements();
      initSubElements();

    }
    
  public String getTextWithKomma()
  {
      String str="";

      for (int i=0;i<values.getLength();i++)
      {
        str+=values.getValue(i)+",";
      }

      return str;
  }

  private String getTextWithN()
  {
      String str="";

      for (int i=0;i<values.getLength();i++)
      {
        str+=values.getValue(i)+"\n";
      }

      return str;
  }



    public void initInputPins()
    {
    value0.setValue(initValue.getValue());
    }

    public void initOutputPins()
    {
    }

    public void resetValues()
    {
    }

   
    public void start()
    {

      oldValue=value0.getValue()+1;

      circuitElement=element.getCircuitElement();
      circuitElement.Change(0,(Object)value0);
      element.jRepaint();
    }
    
    public void stop()
    {
      oldValue=value0.getValue()+1;
    }


  public void mousePressed(MouseEvent e)
  {
    proc(e);
  }
  public void mouseReleased(MouseEvent e)
  {

  }
  public void mouseMoved(MouseEvent e)
  {

  }
  
  public void mouseDragged(MouseEvent e)
  {
    proc(e);
  }
  
  public void proc(MouseEvent e)
  {
      Rectangle bounds=element.jGetBounds();

      int mitteX=(bounds.width/2);
      int mitteY=(bounds.height/2);

      int x=e.getX()-mitteX;
      int y=bounds.height-e.getY()-mitteY;

      double grad = Math.abs(Math.atan((double)y/(double)x));
      double alpha=Math.toDegrees(grad);

      double angle=0;

      double q=45;
      if (x<0 && y<=0) angle=0+90-q-alpha;else
      if (x<=0 && y>0) angle=90-q+alpha;else
      if (x>0 && y>=0) angle=180-q+90-alpha;else
      if (x>=0 && y<0) angle=270-q+alpha;



      double mitteRest=(360.0+maxGrad.getValue())/2.0;
      
      angle=angle-45-minGrad.getValue();
      if (angle<0) angle=(360.0-90)+ ( 90-Math.abs(angle));



      double f = 360.0/maxGrad.getValue();
      value0.setValue(angle*f);

      f=(max.getValue()-min.getValue())/360;
      value0.setValue(min.getValue()+value0.getValue()*f);

      if (value0.getValue()>max.getValue())
      {
        if (angle<=mitteRest) value0.setValue(max.getValue());
        if (angle>mitteRest) value0.setValue(0.0);
      }

     if (value0.getValue()!=oldValue)
     {
        processProc();
        circuitElement.Change(0,(Object)value0);
        oldValue=value0.getValue();
        try
        {
         Thread.sleep(1);
        } catch(Exception ex){ }
        element.jRepaint();
     }
}

  public void processProc()
  {

  }

  public void propertyChanged(Object o)
  {

    try
    {
      df = new DecimalFormat(formatierung.getValue());
    } catch(Exception ex)
    {
      df = new DecimalFormat("#0");
    }
    
    if (onlyNumbers.getValue())
    {
      generateFromNumbersValues();
      generateFromValuesSubElements();
      captions.setText(getTextWithKomma());
    }


    
    if (o.equals(captions))
    {
      String str=getTextWithN();

      Properties frm = new Properties(element.jGetFrame(),str);
      frm.setSize(200,200);
      frm.setModal(true);
      frm.setVisible(true);

      if (frm.result==true)
      {
        onlyNumbers.setValue(false);
        String res=frm.strText;

        ArrayList liste=new ArrayList();

        int lastI=0;
        // Generiere neues Array!
        for (int i=0;i<res.length();i++)
        {
          char ch=res.charAt(i);

          if (ch=='\n')
          {
           liste.add(res.substring(lastI,i));
           lastI=i+1;
          }
        }
        if (lastI>0)liste.add(res.substring(lastI));

        values.resize(liste.size());

        for (int i=0;i<liste.size();i++)
        {
          values.setValue(i,(String)liste.get(i));
        }

        captions.setText(getTextWithKomma());

        // Generate from values subElements!
        generateFromValuesSubElements();

      }

    }
    
    if (abschnitte.getValue()+1<values.getLength())
    {
      VS1DString temp = new VS1DString(values.getLength());
      temp.copyValueFrom(values);
      values.resize(abschnitte.getValue()+1);
      
      for (int i=0;i<abschnitte.getValue()+1;i++)
      {
        values.setValue(i,temp.getValue(i));
      }

      generateFromValuesSubElements();
    }

    
    if (maxGrad.getValue()<0) maxGrad.setValue(0);
    initSubElements();
    element.jRepaint();
  }
  
  
  public void generateFromValuesSubElements()
  {
    element.jClearSubElements();

    listeBeschriftungen.clear();
    for (int i=0;i<values.getLength();i++)
    {
      String strValue=values.getValue(i);
      Beschriftung2 bes=new Beschriftung2(this,strValue);
      listeBeschriftungen.add(bes);
      element.jAddSubElement2(bes);
    }
    initSubElements();
  }



  public void setPropertyEditor()
  {
    element.jAddPEItem("Min",min, -9999999,9999999);
    element.jAddPEItem("Max",max, -9999999,9999999);
    element.jAddPEItem("Abschnitte",abschnitte, 1,10000);
    element.jAddPEItem("Position",value0, 0,9999999);
    element.jAddPEItem("Text anzeigen",showText, 0,0);
    element.jAddPEItem("Anfangswert",initValue, 0,9999999);
    element.jAddPEItem("Knopf-Farbe",buttonColor, 0,0);
    element.jAddPEItem("Hintergrund-Farbe",backColor, 0,0);
    element.jAddPEItem("Nibble-Farbe",nibbleColor, 0,0);
    element.jAddPEItem("Schrift-Farbe",fontColor, 0,0);
    element.jAddPEItem("Linien-Farbe",lineColor, 0,0);
    element.jAddPEItem("Transparent",transparent, 0,0);
    element.jAddPEItem("Min Grad",minGrad, -360,360);
    element.jAddPEItem("Max Grad",maxGrad, -360,360);
    element.jAddPEItem("Formatierung",formatierung, 0,0);
    element.jAddPEItem("Texte",captions, 0,0);
    element.jAddPEItem("Text innen",textInside, 0,0);
    element.jAddPEItem("Zeige Hintergrund",showBackground, 0,0);
    element.jAddPEItem("Text Distanz",abstand, 0,500);
    element.jAddPEItem("Text Schriftart",font, 0,0);
    element.jAddPEItem("Knopf-Größe[%]",knobSizeInProzent, 0,100);
    element.jAddPEItem("Nibble-Größe[%]",nibbleLenInProzent, 0,100);
    element.jAddPEItem("Nibble-Kreis-Größe [%]",nibbleCircleSizeInProzent, 0,100);
    element.jAddPEItem("Zeige Nibble als Kreis",showNibbleAsCircle, 0,0);
    element.jAddPEItem("Nur Nummern",onlyNumbers, 0,0);
    element.jAddPEItem("Nibble (Bitmap)",spitze, 0,0);

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
    element.jSetPEItemLocale(d+3,language,"Position");
    element.jSetPEItemLocale(d+4,language,"Show Text");
    element.jSetPEItemLocale(d+5,language,"Init-Value");
    element.jSetPEItemLocale(d+6,language,"Button Color");
    element.jSetPEItemLocale(d+7,language,"Background Color");
    element.jSetPEItemLocale(d+8,language,"Nibble Color");
    element.jSetPEItemLocale(d+9,language,"Font Color");
    element.jSetPEItemLocale(d+10,language,"Line Color");
    element.jSetPEItemLocale(d+11,language,"Transparent");
    element.jSetPEItemLocale(d+12,language,"Min Grad");
    element.jSetPEItemLocale(d+13,language,"Max Grad");
    element.jSetPEItemLocale(d+14,language,"Format");
    element.jSetPEItemLocale(d+15,language,"Captions");
    element.jSetPEItemLocale(d+16,language,"Text Inside");
    element.jSetPEItemLocale(d+17,language,"Show Background");
    element.jSetPEItemLocale(d+18,language,"Text-Distance");
    element.jSetPEItemLocale(d+19,language,"Font");
    element.jSetPEItemLocale(d+20,language,"Knobsize[%]");
    element.jSetPEItemLocale(d+21,language,"Nibblesize[%]");
    element.jSetPEItemLocale(d+22,language,"Nibble-Circlesize [%]");
    element.jSetPEItemLocale(d+23,language,"Show Nibble As Circle");
    element.jSetPEItemLocale(d+24,language,"Only Numbers");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Min");
    element.jSetPEItemLocale(d+1,language,"Max");
    element.jSetPEItemLocale(d+2,language,"Stages");
    element.jSetPEItemLocale(d+3,language,"Position");
    element.jSetPEItemLocale(d+4,language,"Show Text");
    element.jSetPEItemLocale(d+5,language,"Init-Value");
    element.jSetPEItemLocale(d+6,language,"Button Color");
    element.jSetPEItemLocale(d+7,language,"Background Color");
    element.jSetPEItemLocale(d+8,language,"Nibble Color");
    element.jSetPEItemLocale(d+9,language,"Font Color");
    element.jSetPEItemLocale(d+10,language,"Line Color");
    element.jSetPEItemLocale(d+11,language,"Transparent");
    element.jSetPEItemLocale(d+12,language,"Min Grad");
    element.jSetPEItemLocale(d+13,language,"Max Grad");
    element.jSetPEItemLocale(d+14,language,"Format");
    element.jSetPEItemLocale(d+15,language,"Captions");
    element.jSetPEItemLocale(d+16,language,"Text Inside");
    element.jSetPEItemLocale(d+17,language,"Show Background");
    element.jSetPEItemLocale(d+18,language,"Text-Distance");
    element.jSetPEItemLocale(d+19,language,"Font");
    element.jSetPEItemLocale(d+20,language,"Knobsize[%]");
    element.jSetPEItemLocale(d+21,language,"Nibblesize[%]");
    element.jSetPEItemLocale(d+22,language,"Nibble-Circlesize [%]");
    element.jSetPEItemLocale(d+23,language,"Show Nibble As Circle");
    element.jSetPEItemLocale(d+24,language,"Only Numbers");

  }



  public void loadFromStream(java.io.FileInputStream fis)
  {
      min.loadFromStream(fis);
      max.loadFromStream(fis);
      abschnitte.loadFromStream(fis);
      initValue.loadFromStream(fis);
      showText.loadFromStream(fis);
      buttonColor.loadFromStream(fis);
      nibbleColor.loadFromStream(fis);
      fontColor.loadFromStream(fis);
      lineColor.loadFromStream(fis);
      transparent.loadFromStream(fis);
      minGrad.loadFromStream(fis);
      maxGrad.loadFromStream(fis);
      formatierung.loadFromStream(fis);
      textInside.loadFromStream(fis);
      showBackground.loadFromStream(fis);
      abstand.loadFromStream(fis);
      font.loadFromStream(fis);
      knobSizeInProzent.loadFromStream(fis);
      onlyNumbers.loadFromStream(fis);
      values.loadFromStream(fis);
      nibbleLenInProzent.loadFromStream(fis);
      backColor.loadFromStream(fis);
      showNibbleAsCircle.loadFromStream(fis);
      nibbleCircleSizeInProzent.loadFromStream(fis);
      spitze.loadFromStream(fis);

      if (min.getValue()>=max.getValue()) min.setValue(max.getValue()-1);
      oldValue=value0.getValue()+1;

      generateFromValuesSubElements();
      captions.setText(getTextWithKomma());
      //element.jRepaint();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      min.saveToStream(fos);
      max.saveToStream(fos);
      abschnitte.saveToStream(fos);
      initValue.saveToStream(fos);
      showText.saveToStream(fos);
      buttonColor.saveToStream(fos);
      nibbleColor.saveToStream(fos);
      fontColor.saveToStream(fos);
      lineColor.saveToStream(fos);
      transparent.saveToStream(fos);
      minGrad.saveToStream(fos);
      maxGrad.saveToStream(fos);
      formatierung.saveToStream(fos);
      textInside.saveToStream(fos);
      showBackground.saveToStream(fos);
      abstand.saveToStream(fos);
      font.saveToStream(fos);
      knobSizeInProzent.saveToStream(fos);
      onlyNumbers.saveToStream(fos);
      values.saveToStream(fos);
      nibbleLenInProzent.saveToStream(fos);
      backColor.saveToStream(fos);
      showNibbleAsCircle.saveToStream(fos);
      nibbleCircleSizeInProzent.saveToStream(fos);
      spitze.saveToStream(fos);
  }

}
 
 
class Beschriftung2 extends JPanel
{
    public String text="";
    private CustomAnalogComp2 owner=null;
    public boolean visible=false;
    
    public Beschriftung2(CustomAnalogComp2 owner, String text)
    {
      this.text=text;
      this.owner=owner;
      this.setBackground(new Color(100,100,100,0));
      this.setOpaque(false);
    }
    
    public void paint(java.awt.Graphics g)
    {
      //super.paintComponent(g);
      
      if (visible)
      {
        g.setFont(owner.font.getValue());
        g.setColor(owner.fontColor.getValue());

        java.awt.FontMetrics fm = g.getFontMetrics(owner.font.getValue());

        g.drawString(text, 1, 0 + fm.getMaxAscent());

      }
    }
}


