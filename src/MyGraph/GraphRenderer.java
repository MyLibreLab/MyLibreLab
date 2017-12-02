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
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

/**
 *
 * @author Salafia
 */
public class GraphRenderer extends JPanel
{
    private BackGraphXY back;
    
    public String caption="";
    public Color captionColor=Color.RED;
    public int captionX=5;
    public int captionY=0;
    
    public double[] xValues=null;
    public double[] yValues=null;
    
    public static final int P_POINT=0;
    public static final int P_LINE=1;
    public static final int P_LINE_MED=2;
    public static final int P_LINE_BIG=3;
    public static final int P_LINE_VBIG=4;
    public static final int P_POINT_VOID=5;
    public static final int P_POINT_MED=6;
    public static final int P_POINT_BIG=7;
    public static final int P_POINT_HIST=8;
    public static final int P_POINT_HIST_MED=9;
    public static final int P_POINT_HIST_BIG=10;
    public static final int P_POINT_HIST_INV=11;
    public static final int P_POINT_HIST_MED_INV=12;
    public static final int P_POINT_HIST_BIG_INV=13;
    
    public int bufferLen= 600;
    public int pointType = P_LINE_VBIG; //Default PointType for X/Y Graphs
    
    //private Color lineColor= Color.WHITE;
    private Color lineColor= new Color(255,153,0); //Default Line Color for X/Y Graphs
    private boolean FirstPoint=true;
    
    
    
    public void setbufferLen(int Interval){
     this.bufferLen=Interval;
     
    }
    public int getbufferLen(){
      return this.bufferLen;  
    }
    
    
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
            
            y=Math.cos((double)i/10)*10;
            
            xValues[i]=x;
            yValues[i]=y+yy;
            x+=0.1;
        }
        //lineColor=Color.RED;
        lineColor=new Color(255,153,0);
        
        pointType=P_LINE_VBIG;
    }
    
    public int getBufferLen()
    {
        if (xValues==null) return 0;
        int BufferLen=xValues.length;
        return BufferLen;
    }
    public void init()
    {
        
        
        //genTestValues();
       
        
        //if (autoScaleX) scaleX();
        //if (autoScaleY) scaleY();
        
        //stepXX=(getWidth()/Math.abs(maxX-minX));
        //stepYY=getHeight()/(Math.abs(maxY-minY));
    }
    
    
    
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        double x,y,oldX,oldY;
        double x1=0.0;
        
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        
        x=0;
        y=0;
        oldX=x;
        oldY=y;
        int cx,cy;
        int refx,refy, refX1;
        int refMaxX,refMaxY;
        int refMinX,refMinY;
        int oldCX=0;
        int oldCY=0;
        int StepProm=0;
        Point p=new Point();
        Point refPoint=new Point(0,0);
        Point MaxRefPoint=new Point(0,0);
        Point MinRefPoint=new Point(0,0);
        
        boolean firstTime=true;
        FirstPoint=true;
        g.setColor(lineColor);
        
        
        if (xValues!=null && yValues!=null && xValues.length==yValues.length)
        {   
            for (int i=0;i<xValues.length;i++)
            {   
                back.convertPoint(0,0,refPoint);
                refx=refPoint.x;
                refy=refPoint.y;
                back.convertPoint(back.maxX,back.maxY,MaxRefPoint);
                refMaxX=MaxRefPoint.x;
                refMaxY=MaxRefPoint.y;

                back.convertPoint(back.minX,back.minY,MinRefPoint);
                refMinX=MinRefPoint.x;
                refMinY=MinRefPoint.y;
                x=xValues[i]+back.positionX;
                y=yValues[i];
                if(i==0 && xValues.length>1){
                firstTime=true;
                FirstPoint=true;
                x1=xValues[1];
                back.convertPoint(x1,0,refPoint);
                refX1=refPoint.x;
                StepProm=(int) back.stepXX;
                } 
                
                
                if (x!=oldX || y!=oldY ||FirstPoint==true)
                {   FirstPoint=false;
                    back.convertPoint(x,y,p);
                    cx=p.x;
                    cy=p.y;
                    if(y==0){
                    cy=refy;    
                    }
                    
                    
                    if (cx>back.getWidth() && back.owner.autoscroll==true)
                    {
                        back.positionX--;
                        back.owner.init();
                    }
                    float RelPx=(refMaxX/xValues.length); //Pixel Relation MaxPixels/Buffer
                    //System.out.println("PointType="+pointType+"X="+cx+"_Y"+cy+"_RefX"+refx+"_RefY"+refy+"_Rel="+RelPx);
                    if (pointType>=14){
                        pointType=P_LINE_VBIG;
                    }
                    //if ((xValues.length/refMaxX)<0.0 && pointType==P_POINT_HIST_BIG) pointType=P_POINT_HIST;
                    
                    if (pointType==P_POINT) //Point Type = 0
                    {
                        g.fillRect(cx-3,cy-3,6,6);
                    }else
                       {
                            if (pointType==P_LINE || pointType==P_LINE_MED || pointType==P_LINE_BIG ||pointType==P_LINE_VBIG) ////Point Type = 1
                            {
                                if (firstTime)
                                {
                                    firstTime=false;
                                    oldCX=cx;
                                    oldCY=cy;
                                }
                                else
                                {
                                    if (oldCX!=cx || oldCY!=cy)
                                    {

                                        g.drawLine(oldCX,oldCY,cx,cy);
                                        if (pointType==P_LINE_MED){
                                            g.drawLine(oldCX,oldCY+1,cx,cy+1);
                                        }
                                        if (pointType==P_LINE_BIG){
                                            g.drawLine(oldCX,oldCY+1,cx,cy+1);
                                            g.drawLine(oldCX,oldCY-1,cx,cy-1);
                                        }
                                        if (pointType==P_LINE_VBIG){
                                            g.drawLine(oldCX,oldCY+1,cx,cy+2);
                                            g.drawLine(oldCX,oldCY+1,cx,cy+1);
                                            g.drawLine(oldCX,oldCY-1,cx,cy-1);
                                            g.drawLine(oldCX,oldCY-1,cx,cy-2);
                                        }

                                        oldCX=cx;
                                        oldCY=cy;
                                    }
                                }
                            }else
                               {
                                    if (pointType==P_POINT_VOID)
                                    {
                                    int d=2;
                                    g.drawOval(cx-d,cy-d,d*2,d*2);
                                    }else
                                       {
                                            if (pointType==P_POINT_MED || pointType==P_POINT_BIG)
                                            {
                                                int d=0;
                                                if(pointType==P_POINT_MED){
                                                    d=2;
                                                }
                                                if(pointType==P_POINT_BIG){
                                                    d=4;
                                                }
                                                g.fillOval(cx-d,cy-d,d*2,d*2);
                                            }else{
                                                    if(pointType==P_POINT_HIST || pointType==P_POINT_HIST_MED || pointType==P_POINT_HIST_BIG)
                                                    {
                                                        if (firstTime)
                                                        {
                                                        firstTime=false;
                                                        oldCX=(cx-(StepProm/2));
                                                        //cx+=(StepProm/2);
                                                        oldCY=refy;
                                                        } 
                                                        int HistWidth=0;
                                                        int HistHeigh=0;
                                                        if (pointType==P_POINT_HIST) HistWidth=2;
                                                        if (pointType==P_POINT_HIST_MED) HistWidth=4;
                                                        //if (pointType==P_POINT_HIST_BIG) HistWidth=((cx-oldCX)-2);
                                                        if (pointType==P_POINT_HIST_BIG){
                                                            HistWidth=((StepProm)-2);
                                                            if (HistWidth<10){
                                                                pointType=P_POINT_HIST;
                                                                HistWidth=2;
                                                            }
                                                        }
                                                        HistHeigh=Math.abs(refy-cy);
                                                        if(HistHeigh==0)HistHeigh=2;
                                                        //if (oldCX!=cx || oldCY!=cy || FirstPoint)
                                                        //{ 
                                                            if(cy<refy) g.fillRect((cx-(HistWidth/2)),(refy-HistHeigh),HistWidth,HistHeigh);
                                                            if(cy==refy)g.fillRect((cx-(HistWidth/2)),refy,HistWidth,2);
                                                            if(cy>refy) g.fillRect((cx-(HistWidth/2)),(refy),HistWidth,HistHeigh);

                                                            //System.out.println("Iteration"+i+"_Point"+pointType+"_Cx"+cx+"_OldCx="+oldCX+"refX"+refx+"_Cy"+cy+"_OldCy"+oldCY+"refy"+refy+"_Altura"+HistHeigh);

                                                            oldCX=cx;
                                                            oldCY=cy;
                                                            //}
                                                    }else {
                                                                if(pointType==P_POINT_HIST_INV || pointType==P_POINT_HIST_MED_INV || pointType==P_POINT_HIST_BIG_INV)
                                                                {
                                                                    if (firstTime)
                                                                        {
                                                                        firstTime=false;
                                                                        oldCX=(cx-(StepProm/2));
                                                                        //cx+=(StepProm/2);
                                                                        oldCY=refy;
                                                                        } 
                                                                        int HistWidth=0;
                                                                        
                                                                        if (pointType==P_POINT_HIST_INV) HistWidth=2;
                                                                        if (pointType==P_POINT_HIST_MED_INV) HistWidth=4;
                                                                        //if (pointType==P_POINT_HIST_BIG) HistWidth=((cx-oldCX)-2);
                                                                        if (pointType==P_POINT_HIST_BIG_INV) {
                                                                            HistWidth=((StepProm)-2);
                                                                            if (HistWidth<10){
                                                                                pointType=P_POINT_HIST_INV;
                                                                                HistWidth=2;
                                                                            }
                                                                        }
                                                                        
                                                                        //HistHeigh=Math.abs(refy-cy);
                                                                        int HistHeighP=Math.abs(cy-1);
                                                                        int HistHeighN=Math.abs(refMinY-cy);
                                                                        //if(HistHeighP==0)HistHeighP=2;
                                                                        //if(HistHeighN==0)HistHeighN=2;
                                                                        //if (oldCX!=cx || oldCY!=cy || FirstPoint)
                                                                        //{ 
                                                                            if(cy<refy) g.fillRect((cx-(HistWidth/2)),1,HistWidth,HistHeighP);
                                                                            if(cy==refy)g.fillRect((cx-(HistWidth/2)),refy,HistWidth,2);
                                                                            if(cy>refy) g.fillRect((cx-(HistWidth/2)),cy,HistWidth,HistHeighN);

                                                                            //System.out.println("Iteration"+i+"_Point"+pointType+"_Cx"+cx+"_OldCx="+oldCX+"refX"+refx+"_Cy"+cy+"_OldCy"+oldCY+"refy"+refy+"_AlturaP"+HistHeighP+"_AlturaN"+HistHeighN);

                                                                            oldCX=cx;
                                                                            oldCY=cy;

                                                                   


                                                                }
                                                            }
                                                }
                                        }
                                }
                       }
                          
     
                    oldY=y;
                    oldX=x;
                }
            }
        }
        
        if (!caption.equalsIgnoreCase(""))
        {
            Point captionP=new Point(0,0);
            back.convertPoint(captionX,captionY,captionP);
            
            FontMetrics fm= getFontMetrics(g.getFont());
            Rectangle2D r=fm.getStringBounds(caption,g);
            
            g.setColor(Color.BLACK);
            g.fillRect(captionX,captionP.y-(int)r.getHeight() ,(int)r.getWidth()+3,(int)r.getHeight()+3);
            g.setColor(captionColor);
            g.drawString(caption,captionX,captionP.y);
        }
        
        
        
    }
    
    
    
    
}
