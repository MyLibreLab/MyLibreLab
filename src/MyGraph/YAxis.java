/*
MyOpenLab by Carmelo Salafia www.myopenlab.de
Copyright (C) 2004  Carmelo Salafia cswi@gmx.de
Copyright (C) 2017  Javier Velásquez (javiervelasquez125@gmail.com)
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


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
public class YAxis
{
    private int x;
    private int y;
    private int height;
    private double stepInProzent;
    private double step=0.0;
    private String points[] = null;
    private int heights[] = null;
    private FontMetrics fm=null;
    public Font stdFont = new Font("Arial Bold",Font.BOLD,11);
    private int yDistance=5;
    public double min=0;
    public double max=10000;
    private String formatString="#";
    private JPanel owner;
    
    public final int LEFT=1;
    public final int RIGHT=0;    
    private int textOrientation=LEFT;
    
    public void setTextOrientation(int value)
    {
        textOrientation=value;
    }
    
    //private Color fontColor=Color.BLACK;
    private Color fontColor=new Color(0, 0, 51); //Dark Blue Default
    
    public void setFontColor(Color color)
    {
        fontColor=color;                
    }    
    
    private DecimalFormat format = new DecimalFormat();
    
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
    public void setHeight(int height)
    {
        this.height=height;
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
    public YAxis(JPanel owner,int x, int y, int height, double stepInProzent)
    {
        this.owner=owner;
        this.x=x;
        this.y=y;
        this.height=height;
        this.stepInProzent=stepInProzent;
        
        step=((double)height*stepInProzent)/100.0;                      
    }

    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }
    
    public int getHeight()
    {
        return height;
    }

    
    public int getD2()
    {
        return getD1();
    }
    
    public int getD1()
    {
        fm = owner.getFontMetrics(stdFont);
        return fm.getHeight()/2;
    }    
    
    public int getWidth()
    {
        // Return Max Width
        fm = owner.getFontMetrics(stdFont);
        int max=0;
        int w=0;
        for (int i=0;i<points.length;i++)
        {            
            if (points[i]!=null)
            {
                w=fm.stringWidth(points[i])+yDistance;
                if (w>max)
                {
                    max=w;
                }
            }
        }
        
        return max;        
    }
    
    public void init()
    {
    
        format.applyPattern(formatString);
                        
        points=new String[(int) (1.0+(100.00/stepInProzent))];
        heights=new int[points.length];
        
        double val=max;
        double s=(max-min)/(points.length-1);
        for (int i=0;i<points.length;i++)
        {
            points[i]=""+format.format(val);
            val-=s;
        }
    }    

    private void drawLines(Graphics g)
    {
        for (double v=0;v<(double)height+0.5;v+=step)
        {
            if (textOrientation==LEFT)
            {
                g.drawLine(x,(int)(y+v),x-yDistance,(int)(y+v));
            }else
            if (textOrientation==RIGHT)
            {
                g.drawLine(x,(int)(y+v),x+yDistance,(int)(y+v));
            }                        
        }
    }
    
    private int getWidth(String value)
    {        
        if (value!=null)
        {
            fm = owner.getFontMetrics(stdFont);
            return fm.stringWidth(value);
        } else return 0;
    }
    

    private int getHeight(Graphics g, String value)
    {
        if (value!=null)
        {
            fm = owner.getFontMetrics(stdFont);
            Rectangle2D r =fm.getStringBounds(value,g);

            return (int)r.getHeight();
        } else return 0;
    }
    
    private void drawString(Graphics g, int index)
    {
        double v=step*(double)index;
        String value=points[index];
        
        if (value!=null)
        {
            fm = owner.getFontMetrics(stdFont);
            Rectangle2D r =fm.getStringBounds(value,g);

            double cc=(double)(y+v+(fm.getDescent()*2));

            if (cc>0)
            {                
                fm = owner.getFontMetrics(stdFont);
                if (textOrientation==LEFT)
                {
                    g.drawString(value,(int)(x-r.getWidth()-yDistance),(int)cc);
                }else
                if (textOrientation==RIGHT)
                {
                    g.drawString(value,(int)(x+yDistance+2),(int)cc);
                }
            }                        
            heights[index]=getHeight(g,value);        
        }
    }
    
    private void rekursion(Graphics g, int a, int b)
    {
        
        if (Math.abs(b-a)>1)
        {
            int m = (a+b)/2;
            
            int dd1= (int) ((step*((double)a))+getHeight(g,points[a])/2);
            int dd2= (int) ((step*((double)m))-getHeight(g,points[m])/2);

            int dd3= (int) ((step*((double)m))+getHeight(g,points[m])/2);
            int dd4= (int) ((step*((double)b))-getHeight(g,points[b])/2);
            
            int d1=dd2-dd1;
            int d2=dd4-dd3;
                        
            if (d1>0 && d2>0)
            {
                drawString(g, m);
                                
                rekursion(g,a,m);
                rekursion(g,m,b);  
            }
        }
    }
    

   
    public void paint(Graphics g)
    {
        step=((double)height*stepInProzent)/100.0;
        
        
        g.setFont(stdFont);
        fm = g.getFontMetrics();
        
        g.setColor(fontColor);
        g.drawLine(x,y,x,y+height);
        
        drawLines(g);


        int l=points.length-1;
        int d1= (int) ((step*0)+getWidth(points[0])/2);
        int d2= (int) ((step*l)-getWidth(points[l])/2);
        
        
        drawString(g, 0);
        drawString(g, points.length-1);
        
        rekursion(g,0,points.length-1);
        
    }
    
}
