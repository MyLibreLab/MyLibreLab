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
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.text.DecimalFormat;

/**
 *
 * @author Homer
 */
public class BackGraphXY extends GraphBackground
{

    public MyGraphX owner;
    public double stepXX;
    public double stepYY;
        
    public boolean autoScaleX=false;
    public boolean FirstTimeScaleX=true;
    public boolean autoScaleY=false;
    public double positionX=0.0;
    private DecimalFormat format = new DecimalFormat("#0.0");
    public Color nullLineColor=new Color(0,0,254);
    public boolean nullLineVisible=true;
    
    public Integer AustoscaleInterval= new Integer(600);
    
    
    public void setAutoScaleInterval(Integer Interval){
     this.AustoscaleInterval=Interval;
    }
    public Integer getAutosCaleInterval(){
      return this.AustoscaleInterval;  
    }
    
    
    public BackGraphXY(MyGraphX owner)
    {
        this.owner=owner;
        
        setOpaque(false);
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
        {
            public void mouseMoved(java.awt.event.MouseEvent evt)
            {
                formMouseMoved(evt);
            }
        });
        
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        
    }
    
    /*private double getYValue(double x)
    {
        double d=0;
        double min=99999999999999999099999.9;
    for (int i=0;i<xValues.length;i++)p
        {
            d=Math.abs(xValues[i]-x);
            if (d<min) 
            {
                min=d;        
            }
        }
        return min;
    }*/
    
    public void formMouseMoved(java.awt.event.MouseEvent evt)
    {
       int x=evt.getX();
       int y=evt.getY();
       double x_=((double)x/stepXX)+minX;              
       double y_=minY+((getHeight()-y)/stepYY);       
       jLabel1.setForeground(nullLineColor);
       jLabel1.setText("(x,y="+format.format(x_)+" , "+format.format(y_)+")");
       FontMetrics fm= jLabel1.getFontMetrics(jLabel1.getFont());
       
       int cc=fm.stringWidth(jLabel1.getText());
       jLabel1.setSize (cc ,fm.getHeight());                 
    }
    
    
    public void init()
    {
        if (autoScaleX) scaleX();
        
        if (autoScaleY) scaleY();
        
        stepXX=(getWidth()/Math.abs(maxX-minX));
        stepYY=getHeight()/(Math.abs(maxY-minY));    
    }
    
    private void scaleX()
    {        
        if (owner.graphRenderer!=null)
        {
            double minX=99999999.0;
            double maxX=-9999999.0;            
            for (int j=0;j<owner.graphRenderer.length;j++)
            {
                double[] xValues = owner.graphRenderer[j].xValues;                
                
                if (xValues!=null)
                {
                    double value=0;
                    for (int i=0;i<xValues.length;i++)
                    {                    
                        value=xValues[i];
                        if (value<minX) minX=value;
                        if (value>maxX) maxX=value;
                    }
                }
            }
            
            if (FirstTimeScaleX) {
                minX=0;
                maxX=600;
                FirstTimeScaleX=false;
            }
            maxX=minX+this.AustoscaleInterval;
            this.maxX=maxX;
            this.minX=minX;
            //this.maxX=minX+(Math.abs(maxY-minY);
            
            
            owner.xaxis.setMin(minX);
            owner.xaxis.setMax(maxX);            
        }
        
    }
    
    
    private void scaleY()
    {
        
        if (owner.graphRenderer!=null)
        {
            double minY=999.0;
            double maxY=-999.0;
            
            for (int j=0;j<owner.graphRenderer.length;j++)
            {                
                double[] yValues = owner.graphRenderer[j].yValues;
                
                if (yValues!=null)
                {
                    double value=0;
                    for (int i=0;i<yValues.length;i++)
                    {                    
                        value=yValues[i];
                        if (value<minY) minY=value;
                        if (value>maxY) maxY=value;
                    }                    
                 }
                
            }
            this.minY=minY;
            this.maxY=maxY;
            owner.yaxis.setMin(minY);
            owner.yaxis.setMax(maxY);            
            
        }
        
    }    
    
    public void convertPoint(double x, double y, Point result)
    {                
        result.x=(int)((x-minX)*(stepXX));
        result.y=(int)(((getHeight()/stepYY)-y+minY)*(stepYY));
    }    
    

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);  
        
        if (nullLineVisible)
        {
            Point p=new Point(0,0);
            convertPoint(0,0,p);
            g.setColor(nullLineColor);
            g.drawLine(0,p.y,getWidth(),p.y);
        }
    }    
    
}
