//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia  (cswi@gmx.de)                         *
//* Copyright (C) 2017  Javier Velásquez (javiervelasquez125@gmail.com)                         *
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



public class CustomAnalogComp_2_JV extends JVSMain
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
  public VSInteger abstand=new VSInteger(5);
  public VSInteger knobSizeInProzent=new VSInteger(5);
  public VSInteger nibbleLenInProzent=new VSInteger(25);
  public VSInteger nibbleCircleSizeInProzent=new VSInteger(5);
  public VSBoolean showText = new VSBoolean(false);
  public VSDouble minGrad = new VSDouble(-45);
  public VSDouble maxGrad = new VSDouble(270);
  //public VSColor buttonColor=new VSColor(new Color(204,204,204));
  public VSColorAdvanced buttonColor=new VSColorAdvanced();//new VSColor(new Color(153,153,153));
  public VSColorAdvanced StrokeColor=new VSColorAdvanced(); //(new Color(204,204,204)); //G
  public VSColorAdvanced backColor=new VSColorAdvanced();
  
  public VSComboBox nibbleColor=new VSComboBox();
  public VSColor lineColor=new VSColor(new Color(253,153,0)); //255 242 181
  public  VSColor fontColor=new VSColor(new Color(0,0, 51)); //
  
  public VSString formatierung = new VSString("#0");
  public VSBoolean textInside = new VSBoolean(false);
  public VSBoolean CircleComplete = new VSBoolean(false);
  public VSBoolean SetKnobStroke = new VSBoolean(true);
  public VSBoolean showBackground = new VSBoolean(false);
  public VSBoolean showNibbleAsCircle= new VSBoolean(false);
  public VSFont font = new VSFont(new java.awt.Font("Dialog", Font.BOLD, 11));
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

  public int ArcGap=-2;
  public int diameterBack=140;
  public int diameterLine=140;
  public int TextGap=0;
  public Color NibbleColorTemp;

  public VSInteger borderStroke= new VSInteger(7);
  public VSInteger lineStroke= new VSInteger(2);
  
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
        
        if(nibbleColor.selectedIndex==1){
        spitze.loadImage(element.jGetSourcePath()+"SpitzeBlack.png");    
        NibbleColorTemp=Color.BLACK;    
        }
        if(nibbleColor.selectedIndex==0){
        spitze.loadImage(element.jGetSourcePath()+"SpitzeRed.png");
        NibbleColorTemp=Color.RED;
        }
        if(nibbleColor.selectedIndex==2){
        spitze.loadImage(element.jGetSourcePath()+"SpitzeYellow.png");
        NibbleColorTemp=Color.YELLOW;
        }
        
        //TextGap=(int) (w*0.20); // Gap of 20% of Total Width
        //TextGap=(int) (w*0.10); // Gap of 20% of Total Width
        TextGap=(int) (w*0.20); // Gap of 20% of Total Width

          
        double vectorLaenge=(Math.sqrt((x1*x1)+(y1*y1))-TextGap);
        double vectLen=vectorLaenge+abstand.getValue(); // Magnitud Vector Textos
        //double vectLen=(double) (vectorLaenge+abstand.getValue()+ w*5/100 +((w-(w*5/100)*2)*0.3)); //(Distancia circulo + Distancia texto)
                
//double vectLen1=vectorLaenge+7.0;
        double vectLen1=vectorLaenge;

        if (textInside.getValue())
        { //vectorLaenge=(Math.sqrt((x1*x1)+(y1*y1))-3*TextGap+5);
          TextGap=(int) (w*0.28);  
          vectorLaenge=(w/2-TextGap);
          vectLen=vectorLaenge-abstand.getValue();
          //vectLen1=vectorLaenge-7.0;
          vectLen1=vectorLaenge;
        }else{
          //vectorLaenge=(Math.sqrt((x1*x1)+(y1*y1))-TextGap+15);
          int TextGapEx=(int) (w*0.20);
          vectorLaenge=(w/2-TextGapEx);
          vectLen=vectorLaenge+abstand.getValue();
          //vectLen1=vectorLaenge-7.0;
          vectLen1=vectorLaenge;
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
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
      
      Stroke StrokeRestore = g.getStroke();

      
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
      int Stroke=borderStroke.getValue();

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
      int InsideGapW=(int)((w-distance*2)*0.3);
      int InsideGapH=(int)((w-distance*2)*0.3);
      
      int TempW=(int)((w-distance*2)-InsideGapW)+abstand.getValue();
      int TempH=(int)((h-distance*2)-InsideGapH)+abstand.getValue();
      int Tam=((3*TempW/4)-15)+abstand.getValue(); // Tamaño del circulo cuando se selecciona Texto Externo
      if(textInside.getValue()){
      TempW=(int)((w-distance*2)-InsideGapW)-abstand.getValue();
      TempH=(int)((h-distance*2)-InsideGapH)-abstand.getValue();
      Tam=((3*TempW/4)-15)+abstand.getValue(); // Tamaño del circulo cuando se selecciona Texto Externo    
      }
      if (transparent.getValue()==false)
      {
        if (showBackground.getValue())
        {
          
          g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);  
        //g.setColor(StrokeColor.getValue()); //GRAY
          StrokeColor.setFillColor(g);
         
          g.fillOval(Stroke,Stroke,(w-2*Stroke)+1,(h-2*Stroke)+1); // Fill Color Circle 2 (Between Stroke and BackGround)
          
        //g.setColor(backColor.getValue());
          backColor.setFillColor(g);
          g.fillOval((2*Stroke),(2*Stroke),(w-(4*Stroke)),(h-(4*Stroke))); // Fill Background (White)
          //g.fillOval(distance,distance,(w-distance*2)+3,((h-distance*2)+3));
          //g.setColor(Color.ORANGE);
          g.setColor(lineColor.getValue());
          g.setStroke(new BasicStroke(lineStroke.getValue())); // Stroke Default 10
          
          if(CircleComplete.getValue())
          {
            //g.setColor(Color.ORANGE);
            if(showText.getValue() && textInside.getValue()){
                g.drawOval(w/2-(TempW/2),h/2-(TempH/2),TempW,TempH); // Completar Circulo
            }else{
                g.drawOval(w/2-(Tam/2),h/2-(Tam/2),Tam,Tam); // Completar Circulo
            }
          }
          
          if(SetKnobStroke.getValue())
          {
          //g.setColor(new Color(153,153,153)); //GRAY
        //g.setColor(buttonColor.getValue());
          buttonColor.setFillColor(g);
          g.setStroke(new BasicStroke(Stroke)); // Stroke Default 10
          //g.drawOval(distance-5,distance-5,((w-distance*2)+10),((h-distance*2)+10)); //Stroke Border
          g.drawOval(Stroke/2,Stroke/2,w-Stroke,h-Stroke); //Stroke Border
          }
          g.setStroke(StrokeRestore);
          //g.drawOval(distance,distance,w-distance*2,h-distance*2);
          
        }
      }
      //g.drawLine(0, 0, w, h);
      //g.drawLine(w, 0, 0, h);
      //g.drawOval(w/2-60, h/2-60,120,120);
      

      g.setColor(lineColor.getValue());
      if (showText.getValue()) // Dibujar Arco sobre el que van las líneas de división
      { 
        //g.setColor(Color.ORANGE);
        g.setStroke(new BasicStroke(lineStroke.getValue()));  
        //g.drawArc(distance,distance,w-distance*2+2,h-distance*2+2, 180-(int)minGrad.getValue(),-(int)maxGrad.getValue());
        if(showText.getValue() && textInside.getValue()){
          g.drawArc(w/2-(TempW/2),h/2-(TempH/2),TempW,TempH, 180-(int)minGrad.getValue(),-(int)maxGrad.getValue());     
          }else{
          
          g.drawArc(w/2-(Tam/2),h/2-(Tam/2),Tam,Tam, 180-(int)minGrad.getValue(),-(int)maxGrad.getValue());
    
          }
        
        
        distance=(int) (  ((double)w)/100.0 * (double)knobSizeInProzent.getValue() );
        g.setStroke(StrokeRestore);
      } //else distance=w/2;

      if (transparent.getValue()==false)
      { 
        
        //g.setColor(lineColor.getValue());
        g.setColor(NibbleColorTemp);
        if(showNibbleAsCircle.getValue()==false){
        g.fillOval(mitteX-distance,mitteY-distance,2*distance,2*distance); // Circulo central 
        }
        //g.setColor(Color.black);
        //g.drawOval(mitteX-distance,mitteY-distance,2*distance,2*distance);
      }
        // Draw Linien
        x1=w/2;
        y1=0;
        //TextGap = (int) ((w/2)*0.3);
        int d2=w*5/100;
        
        
        double vectorLaenge=((Math.sqrt((x1*x1)+(y1*y1))-(double)d2)-(InsideGapW/2)+3); // Vector sobre el que quedarán las rayitas polares del gráfico
        //System.out.println("LenW="+w+"Vector Magnitude: "+vectorLaenge);
        


        if (showText.getValue())
        {
          int counter=0;
          
          double vectLen=(vectorLaenge+abstand.getValue()+(InsideGapW)+(double)d2); //(Distancia circulo + Distancia texto)
          double vectLen1=vectorLaenge+(double)d2; // Tamaño rayas indicadoras
          

          if (textInside.getValue())
          { d2-=1;
            vectorLaenge=(vectorLaenge-2)-abstand.getValue();
            vectLen=vectorLaenge-abstand.getValue();
            vectLen1=vectorLaenge-(double)d2;
          }else{
            vectorLaenge=((Math.sqrt((x1*x1)+(y1*y1))-(double)d2)-(InsideGapW/2)-12)+abstand.getValue();
            vectLen=vectorLaenge-abstand.getValue();
            vectLen1=vectorLaenge-(double)d2;  
          }

          double disVal=((double)(max.getValue()-min.getValue()) )/(double)abschnitte.getValue();

          double dis=100.0/(double)abschnitte.getValue();
          g.setStroke(new BasicStroke(lineStroke.getValue())); // Stroke for 
          for (double i=0;i<=100.0;i+=dis)
          {
          f=(-maxGrad.getValue()/100.0);
          grad = (180.0-minGrad.getValue())+i*f;
          angle=Math.toRadians(grad);

          Point p1=getPointFromAngle(angle,vectLen1);
          Point p2=getPointFromAngle(angle,vectorLaenge);

          g.setColor(lineColor.getValue());
          g.drawLine(mitteX+p1.x,mitteY-p1.y,mitteX+p2.x,mitteY-p2.y);
          //System.out.println("mitteX="+mitteX+"mitteY="+mitteY+"p1.x="+p1.x+"p1.y="+p1.y+"p2.x"+p2.x+"p2.y"+p2.y);

          }
          g.setStroke(StrokeRestore); // Original Stroke 
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
        g.setColor(NibbleColorTemp);
        
        if (showNibbleAsCircle.getValue())
        {
          int disX=(int) ( ((double)w)/100.0 * (double)nibbleCircleSizeInProzent.getValue() );
          g.fillOval(mitteX-x2-1-disX, mitteY-y2-disX,disX*2, disX*2);
          g.setColor(Color.BLACK);
          g.drawOval(mitteX-x2-1-disX, mitteY-y2-disX,disX*2, disX*2);
        } else
        {
            //g.setColor(nibbleColor.getValue());
            //g.setColor(Color.RED);
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
    double limit =0.0;
    if(abschnitte.getValue()%10==0){
    limit=100.0;    
    }else{
    limit=100.0+dis;    
    }
    for (double i=0;i<=limit;i+=dis)
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
        drawAnzeige(g,0,0,bounds.width-2,bounds.height-2);

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
      setName("Gauge JV");
      showText.setValue(true);
      element.jSetMinimumSize(10,10);


      generateFromNumbersValues();
      generateFromValuesSubElements();
      captions.setText(getTextWithKomma());
      
      firstTime=true;
      
      nibbleColor.addItem("RED");
      nibbleColor.addItem("BLACK");
      nibbleColor.addItem("YELLOW");
      nibbleColor.setPinIndex(1);

      backColor.color1= new Color(255,255,255);
      backColor.color2= new Color(204,204,204);
      backColor.p1=new Point(90, 90);
      backColor.p2=new Point(326, 18);
      backColor.modus=VSColorAdvanced.MODE_RADIAL;
      
      
      StrokeColor.color1= new Color(51,51,51);
      StrokeColor.color2= new Color(255,255,255);
      StrokeColor.p2=new Point(49, 189);
      StrokeColor.p1=new Point(142, 30);
      StrokeColor.modus=VSColorAdvanced.MODE_LINEAR;
      
      buttonColor.color1= new Color(51,51,51);
      buttonColor.color2= new Color(255,255,255);
      buttonColor.p1=new Point(49, 189);
      buttonColor.p2=new Point(142, 30);
      buttonColor.modus=VSColorAdvanced.MODE_LINEAR;      
      
      spitze.loadImage(element.jGetSourcePath()+"SpitzeRed.png");
      
      element.jSetInfo("Javier Velasquez","","");
      
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
    element.jAddPEItem("Äußere Kante",SetKnobStroke, 0,0);
    element.jAddPEItem("Äußere Breite",borderStroke, 0,50);
    
    element.jAddPEItem("Knopf-Farbe 1",buttonColor, 0,0);
    element.jAddPEItem("Knopf-Farbe 2",StrokeColor, 0,0);
    element.jAddPEItem("Hintergrund-Farbe",backColor, 0,0);
    element.jAddPEItem("Nibble-Farbe",nibbleColor, 0,0);
    element.jAddPEItem("Schrift-Farbe",fontColor, 0,0);
    element.jAddPEItem("Linien-Farbe",lineColor, 0,0);
    element.jAddPEItem("Linien-Breite",lineStroke, 0,50);
    
    element.jAddPEItem("Transparent",transparent, 0,0);
    element.jAddPEItem("Min Grad",minGrad, -360,360);
    element.jAddPEItem("Max Grad",maxGrad, -360,360);
    element.jAddPEItem("Vollständiger Kreis",CircleComplete, 0,0);
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
    element.jSetPEItemLocale(d+6,language,"Show Button Edge?");
    element.jSetPEItemLocale(d+7,language,"Button Edge Width");
    element.jSetPEItemLocale(d+8,language,"Button Edge Color");
    element.jSetPEItemLocale(d+9,language,"Button Mid Color");
    element.jSetPEItemLocale(d+10,language,"Background Color");
    element.jSetPEItemLocale(d+11,language,"Nibble Color");
    element.jSetPEItemLocale(d+12,language,"Font Color");
    element.jSetPEItemLocale(d+13,language,"Line Color");
    element.jSetPEItemLocale(d+14,language,"Line Width");
    element.jSetPEItemLocale(d+15,language,"Transparent");
    element.jSetPEItemLocale(d+16,language,"Min Grad");
    element.jSetPEItemLocale(d+17,language,"Max Grad");
    element.jSetPEItemLocale(d+18,language,"Full Circle?");
    element.jSetPEItemLocale(d+19,language,"Format");
    element.jSetPEItemLocale(d+20,language,"Captions");
    element.jSetPEItemLocale(d+21,language,"Text Inside");
    element.jSetPEItemLocale(d+22,language,"Show Background");
    element.jSetPEItemLocale(d+23,language,"Text-Distance");
    element.jSetPEItemLocale(d+24,language,"Font");
    element.jSetPEItemLocale(d+25,language,"Knobsize[%]");
    element.jSetPEItemLocale(d+26,language,"Nibblesize[%]");
    element.jSetPEItemLocale(d+27,language,"Nibble-Circlesize [%]");
    element.jSetPEItemLocale(d+28,language,"Show Nibble As Circle");
    element.jSetPEItemLocale(d+29,language,"Only Numbers");
    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Minimo");
    element.jSetPEItemLocale(d+1,language,"Maximo");
    element.jSetPEItemLocale(d+2,language,"Cantidad Items");
    element.jSetPEItemLocale(d+3,language,"Posicion");
    element.jSetPEItemLocale(d+4,language,"Mostar Texto?");
    element.jSetPEItemLocale(d+5,language,"Valor Inicial");
    element.jSetPEItemLocale(d+6,language,"Mostrar Borde Externo?");
    element.jSetPEItemLocale(d+7,language,"Grososr Borde Externo");
    element.jSetPEItemLocale(d+8,language,"Color Borde Externo");
    element.jSetPEItemLocale(d+9,language,"Color Medio");
    element.jSetPEItemLocale(d+10,language,"Color de fondo");
    element.jSetPEItemLocale(d+11,language,"Color Aguja");
    element.jSetPEItemLocale(d+12,language,"Color Letra");
    element.jSetPEItemLocale(d+13,language,"Color Linea");
    element.jSetPEItemLocale(d+14,language,"Grosor Linea");
    element.jSetPEItemLocale(d+15,language,"Transparente?");
    element.jSetPEItemLocale(d+16,language,"Angulo Min");
    element.jSetPEItemLocale(d+17,language,"Angulo Max");
    element.jSetPEItemLocale(d+18,language,"Circulo Completo?");
    element.jSetPEItemLocale(d+19,language,"Formato Numeros");
    element.jSetPEItemLocale(d+20,language,"Captions");
    element.jSetPEItemLocale(d+21,language,"Texto Interno?");
    element.jSetPEItemLocale(d+22,language,"Mostrar Fondo?");
    element.jSetPEItemLocale(d+23,language,"Distancia Texto");
    element.jSetPEItemLocale(d+24,language,"Fuente");
    element.jSetPEItemLocale(d+25,language,"Tamaño Perilla Central[%]");
    element.jSetPEItemLocale(d+26,language,"Distancia Aguja-Circulo[%]");
    element.jSetPEItemLocale(d+27,language,"Diametro Aguja-Circulo [%]");
    element.jSetPEItemLocale(d+28,language,"Show Nibble As Circle");
    element.jSetPEItemLocale(d+29,language,"Only Numbers");

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
      StrokeColor.loadFromStream(fis);
      CircleComplete.loadFromStream(fis);
      SetKnobStroke.loadFromStream(fis);

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
      StrokeColor.saveToStream(fos);
      CircleComplete.saveToStream(fos);
      SetKnobStroke.saveToStream(fos);
  }

}
 
 
class Beschriftung2 extends JPanel
{
    public String text="";
    private CustomAnalogComp_2_JV owner=null;
    public boolean visible=false;
    
    public Beschriftung2(CustomAnalogComp_2_JV owner, String text)
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


