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


package MyGraph;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import javax.swing.JPanel;


/**
 *
 * @author Carmelo
 */
public class XAxis
{
    private int x;
    private int y;
    private int width;
    private double stepInProzent;
    private double step=0.0;
    private String points[] = null;
    private FontMetrics fm=null;
    public Font stdFont = new Font("Arial",0,10);
    private int xDistance=5;
    public double min=0;
    public double max=100;
    private String formatString="#0";
    private JPanel owner;
    public final int TOP=1;
    public final int BOTTOM=0;
    public BackGraphXY back;
    
    private DecimalFormat format = new DecimalFormat();
    private int textOrientation=BOTTOM;
    
    public void setTextOrientation(int value)
    {
        textOrientation=value;
    }
    
    private Color fontColor=Color.BLACK;

    public Color getFontColor()
    {
        return fontColor;          
    }
    
    public void setFontColor(Color color)
    {
        fontColor=color;                
    }
    public void setFont(Font font)
    {
        this.stdFont=font;
    }
    
    public double getMin() {return min;};
    public double getMax() {return max;};
    
    public void setMin(double min)
    {
        this.min=min;        
    }
    
     public void setMax(double max)
    {
        this.max=max;        
    }
     
    public void setStartPoint(int x, int y)
    {
        this.x=x;
        this.y=y;
    }
    public void setWidth(int width)
    {
        this.width=width;
    }
    public double getStepInProzent()
    {
        return stepInProzent;
    }
    public void setStepInProzent(double stepInProzent)
    {
        this.stepInProzent=stepInProzent;
    }
    
    public String getFormatString()
    {
        return formatString;
    }
    public void setFormatString(String formatString)
    {
        this.formatString=formatString;        
    }
    
    /** Creates a new instance of XAxis */
    public XAxis(JPanel owner,BackGraphXY back,int x, int y, int width, double stepInProzent)
    {
        this.owner=owner;
        this.back=back;
        this.x=x;
        this.y=y;
        this.width=width;
        this.stepInProzent=stepInProzent;
        
        step=((double)width*stepInProzent)/100.0;
    }

    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }
    
    public int getWidth()
    {
        return width;
    }

    
    public int getD1()
    {
        int result=0;
        
        if (points.length>0)
        {
            result= (int) (getWidth(points[0])/2.0);
        }
        return result;
    }
    
    public int getD2()
    {
        int result=0;
        
        if (points.length>=1)
        {
            int L=points.length-1;
            result= (int) (getWidth(points[L])/2.0);
        }
        return result;
    }    
    
    public int getHeight()
    {        
        fm = owner.getFontMetrics(stdFont);
        return fm.getHeight()+fm.getDescent()+4;
    }
    
    public void init()
    {
    
        format.applyPattern(formatString);
                        
        points=new String[(int) (1.0+(100.00/stepInProzent))];
        
        double val=min-back.positionX;
        double s=(max-min)/(points.length-1);
        for (int i=0;i<points.length;i++)
        {
            points[i]=""+format.format(val);
            val+=s;
        }
    }    

    private void drawLines(Graphics g)
    {
        for (double v=0;v<=(double)width+0.5;v+=step)
        {
            if (textOrientation==TOP)
            {
                g.drawLine((int)(x+v),y,(int)(x+v),y-xDistance);
            }else
            if (textOrientation==BOTTOM)
            {
                g.drawLine((int)(x+v),y,(int)(x+v),y+xDistance);
            }
        }        
    }
    
    private int getWidth(String value)
    {
        int val=0;
        if (value!=null)
        {
            fm = owner.getFontMetrics(stdFont);
            val=fm.stringWidth(value);
        }
        return val;
    }
    

    
    private void drawString(Graphics g, int index)
    {
        if (width>1)
        {
            double v=step*(double)index;
            String value=points[index];

            if (value!=null)
            {
                double cc=(double)x+v- getWidth(value)/2.0;       

                if (cc>0)
                {
                    fm = owner.getFontMetrics(stdFont);
                    Rectangle2D r= fm.getStringBounds(value,g);                    
                    if (textOrientation==TOP)
                    {
                        g.drawString(value,(int)cc,(int)(y-5-fm.getDescent()) );
                    }else
                    if (textOrientation==BOTTOM)
                    {                        
                        g.drawString(value,(int)cc,(int)(y+10+fm.getAscent()) );
                    }
                }
            }
        }
    }
    
    private void rekursion(Graphics g, int a, int b)
    {
        
        if (Math.abs(b-a)>1)
        {
            int m = (a+b)/2;

            int dd1= (int) ((step*((double)a))+getWidth(points[a])/2.0);
            int dd2= (int) ((step*((double)m))-getWidth(points[m])/2.0);

            int dd3= (int) ((step*((double)m))+getWidth(points[m])/2.0);
            int dd4= (int) ((step*((double)b))-getWidth(points[b])/2.0);

            int d1=dd2-dd1;
            int d2=dd4-dd3;
            
            int f=fm.stringWidth("8");
            if (d1>f && d2>f)
            {
                drawString(g, m);
                                
                rekursion(g,a,m);
                rekursion(g,m,b);
            }
        }
    }

    
    public void paint(Graphics g)
    {
        step=((double)width*stepInProzent)/100.0;
        
        g.setFont(stdFont);
        
        
        g.setColor(fontColor);
        g.drawLine(x,y,x+width-1,y);
        
        drawLines(g);

        int l=points.length-1;
        int d1= (int) ((step*0)+getWidth(points[0])/2);
        int d2= (int) ((step*l)-getWidth(points[l])/2);
        
        
        drawString(g, 0);
        drawString(g, points.length-1);
        
        rekursion(g,0,points.length-1);
        
    }
    
}
