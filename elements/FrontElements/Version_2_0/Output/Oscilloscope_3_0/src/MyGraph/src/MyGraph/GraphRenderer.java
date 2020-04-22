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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.text.DecimalFormat;
import javax.swing.JPanel;

/**
 *
 * @author Salafia
 */
public class GraphRenderer extends JPanel
{
    private BackGraphXY back;
    
    public double[] xValues=null;
    public double[] yValues=null;
    
    public static final int P_POINT=0;
    public static final int P_LINE=1;
    public static final int P_POINTBIG=2;
    public int pointType = P_POINT;
    
    
    private Color lineColor= Color.WHITE;
    
    public Color getLineColor()
    {
        return lineColor;
    }
    public void setLineColor(Color color)
    {
        lineColor=color;
    }    

    
    /** Creates a new instance of Renderer */
    public GraphRenderer(BackGraphXY back)
    {
        this.back=back;
        setOpaque(false);
    }
    
    public void genTestValues()
    {
        int maxGen=25;
        xValues=new double[maxGen];
        yValues=new double[maxGen];
        
        double yy=Math.random()+100;
        double y;                
        double x=back.minX;
        for (int i=0;i<maxGen;i++)
        {                    
            x=Math.sin((double)i/30)*30;
            y=Math.cos((double)i/30)*30;

            xValues[i]=x;
            yValues[i]=y+yy;
            x+=0.1;
        }      
        lineColor=Color.RED;
        pointType=1;
    }
    
    public void init()
    {

        
        //genTestValues();
        
          //if (autoScaleX) scaleX();
        //if (autoScaleY) scaleY();
        
        //stepXX=(getWidth()/Math.abs(maxX-minX));
        //stepYY=getHeight()/(Math.abs(maxY-minY));    
    }    
    
    private void convertPoint(double x, double y, Point result)
    {                
        result.x=(int)((x-back.minX)*(back.stepXX));
        result.y=(int)(((back.getHeight()/back.stepYY)-y+back.minY)*(back.stepYY));                        
    }
    
    
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        
        double x,y,oldX,oldY;
        
        Graphics2D g2 = (Graphics2D)g;
        //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        x=0;
        y=0;
        oldX=x;
        oldY=y;
        int cx,cy;
        int oldCX=0;
        int oldCY=0;
        Point p=new Point();;
        
        boolean firstTime=true;
        g.setColor(lineColor);
        
        if (xValues!=null && yValues!=null && xValues.length==yValues.length)
        {
            for (int i=0;i<xValues.length;i++)
            {                    
              x=xValues[i]+back.positionX;
              y=yValues[i];
              if (x!=oldX || y!=oldY)
              {
                convertPoint(x,y,p);
                cx=p.x;
                cy=p.y;
                
                if (pointType==P_POINT)
                {
                    g.drawRect(cx,cy,0,0);
                }else
                if (pointType==P_POINTBIG)
                {
                    int d=2;
                    g.drawOval(cx-d,cy-d,d*2,d*2);
                }else                    
                if (pointType==P_LINE)
                {                   
                    if (firstTime)
                    {
                        firstTime=false;
                        oldCX=cx;
                        oldCY=cy;                                            
                    }else
                    {
                        g.drawLine(oldCX,oldCY,cx,cy);
                        oldCX=cx;
                        oldCY=cy;                    
                    }
                }
                oldY=y;
                oldX=x;
              }
            }
        }
        
        
    }    
    
    
    
    
}
