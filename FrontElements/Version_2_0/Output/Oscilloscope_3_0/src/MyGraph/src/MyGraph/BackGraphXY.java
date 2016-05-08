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
        
    public boolean autoScaleX=true;
    public boolean autoScaleY=true;
    public double positionX=0.0;
    private DecimalFormat format = new DecimalFormat("#0.0");
    
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
            double minX=9999999999999999999.0;
            double maxX=-9999999999999999999.0;            
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
            this.minX=minX;
            this.maxX=maxX;
            owner.xaxis.setMin(minX);
            owner.xaxis.setMax(maxX);            
        }
        
    }
    
    
    private void scaleY()
    {
        
        if (owner.graphRenderer!=null)
        {
            double minY=9999999999999999999.0;
            double maxY=-9999999999999999999.0;
            
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
    
    

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);        
    }    
    
}
