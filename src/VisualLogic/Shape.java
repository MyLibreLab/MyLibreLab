/*
MyOpenLab by Carmelo Salafia www.myopenlab.de
Copyright (C) 2004  Carmelo Salafia cswi@gmx.de

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

package VisualLogic;

import java.awt.*;
import java.util.*;
import java.awt.geom.*;
import javax.swing.JLabel;
/**
 *
 * @author Homer
 */



public class Shape extends VisualObject
{        
    private VMObject basis;
    private int id;
    public boolean selected=false;
    public boolean resizable=false;
    private Rectangle2D nameBounds=null;
    public boolean rasterized=true;
    public JLabel lblName = new JLabel();
    private int minWidth=10;
    private int minHeight=10;
    

    public ArrayList subElemente = new ArrayList(); 
        
    private String internalCaption="";
    public String getInternalCaption() {return internalCaption;}
    public void setInterNalCaption(String value) {internalCaption=value;}
    
    
    private String caption="";    
    public String getCaption() { return caption; }
    public void setCaption(String value) { 
        
        caption=value;        
        lblName.setText(value);
        
        FontMetrics fm = lblName.getFontMetrics(lblName.getFont());
        
        int w=fm.stringWidth(value);
        int h=fm.getHeight();                 

        lblName.setSize(w,h);
        //getParent().remove(lblName);
        //getParent().add(lblName);
        //lblName.setVisible(false);                
    }
    
    private boolean captionVisible=false;
    
    public boolean isCaptionVisible() {return captionVisible;}
    public void setCaptionVisible(boolean value)
    {        
        lblName.setVisible(value);
        captionVisible=value;
    }
    private boolean visibleWhenRun=true;
    public boolean isVisibleWhenRun()
    {
        return visibleWhenRun;
    }
    public void setVisibleWhenRun(boolean value)
    {
        visibleWhenRun=value;
    }
    

    public void initVars()
    {
       vCaption.setValue(getCaption());
       vShowCaption.setValue(isCaptionVisible());
       vLeft.setValue(getLocation().x); 
       vTop.setValue(getLocation().y); 
       vWidth.setValue(getWidth()); 
       vHeight.setValue(getHeight());
       vVisible.setValue(isVisibleWhenRun());
    }    
    
    public final int NAME_POSITION_OBEN=1;
    public final int NAME_POSITION_UNTEN=0;
    
    private int namePosition=NAME_POSITION_UNTEN; // also Unten
    public void setNamePosition(int position)
    {
        namePosition=position;
    }
    
    private Font stdFont = new Font("Arial",0,10);
    
    public boolean bolTag;
    public int numTag;
                
    
    private int nameID=-1;
    public int getNameID(){return nameID;}
    public void setNameID(int value)
    {
        nameID=value;
    }
            

    

    public Shape(int id,VMObject basis) 
    {
        super();
        
        this.id=id;                       
        this.basis=basis;
        
        setSize(100,100);       
        
    }
    
    public VMObject getVMObject()
    {
        return basis;
    }
    
    
   public void setLocation(Point p)
   {
       /*if (p.x<0) p.x=500;
       if (p.y<0) p.y=0;*/
       
       //if (p.x+getWidth()/2> basis.getWidth()) p.x=basis.getWidth()-getWidth()/2;
       //if (p.y+getHeight()/2> basis.getHeight()) p.y=basis.getHeight()-getHeight()/2;
       super.setLocation(p.x,p.y);
   }
    

   public void setLocation(int x, int y)
   {     
       setLocation(new Point(x,y));       
   }
   
   public void redimLabel()
   {
       if (lblName!=null)
       {
           int elementMitteX=getX()+getWidth()/2;
           int elementMitteY=getY()+getHeight()/2;

           int labelMitteX=lblName.getWidth()/2;
           int labelMitteY=lblName.getHeight()/2;

           lblName.setLocation(elementMitteX-labelMitteX,getY()+getHeight()+5);
       }       
   }
   
    public void setMinimumSize(int width, int height)
    {
      this.minWidth=width;
      this.minHeight=height;    
    }   

   /* public void setSize(Dimension d)
    {
        super.setSize(d);
        if (wrapper!=null)
        {
            wrapper.setSize(120,120);
        }
    }*/
   
    public void setSize(int width, int height)
    {        
        if (width<minWidth) width=minWidth;
        if (height<minHeight) height=minHeight;
       
        super.setSize(width,height);
        
    }
    
    public int getID()
    {
        return id;
    }
    
    public boolean isResizable() {return resizable;}
    public void setResizable(boolean value)
    {
        if (value!=resizable) resizable=value;                
    }

    public void repaint()
    {
        redimLabel();
        
        super.repaint();                
    }
      
    
    public void setSelected(boolean value)
    {
        if (selected!=value)
        {
            selected=value;            
            repaint();
        }
    }
    
    public boolean isSelected()
    {
        return selected;
    }    
    
    
            
    public Rectangle getSubElementsBounds()
    {
        int minX=99999;
        int minY=99999;
        int maxX=-99999;
        int maxY=-99999;
        
        for (int i=0;i<subElemente.size();i++)
        {
          SubElement el=(SubElement)subElemente.get(i);
          
          if (el.x<minX) minX=el.x;
          if (el.y<minY) minY=el.y;
          
          if (el.x+el.width>maxX) maxX=el.x+el.width;
          if (el.y+el.height>maxY) maxY=el.y+el.height;
        }
        
        return new Rectangle(minX,minY,maxX,maxY);
    }    
 

    
   /* public void repaint()
    {
        if (getVMObject().panel!=null )
        {
            
            int ax=getX();
            int ay=getY();
            int axx=getX()+getWidth();
            int ayy=getY()+getHeight();

            Rectangle rx=getSubElementsBounds();
            rx.x+=getX();
            rx.y+=getY();
            rx.width+=getX();
            rx.height+=getY();
            
            if (rx.x<ax) ax=rx.x-5;
            if (rx.y<ay) ay=rx.y-5;
            if (rx.width>axx) axx=rx.width+5;
            if (rx.height>ayy) ayy=rx.height+5;
            
            if (rx.x<0) rx.x=0;
            if (rx.y<0) rx.y=0;

            getVMObject().panel.repaint(ax-5,ay-15,axx+15,ayy+30);

            if (nameBounds!=null)
            {
                Rectangle r=nameBounds.getBounds();

                int y2=0; 
                if (namePosition==NAME_POSITION_OBEN)  y2=getY()-(int)nameBounds.getHeight()+10;
                if (namePosition==NAME_POSITION_UNTEN) y2=getY()+getHeight()+20+(int)nameBounds.getHeight()-10; 

                // ermittle MinXY und MaxXy Bounds von alles SubElementen
                
                
                ax=-10+getX()+(int) ( ((getWidth()) /2-nameBounds.getWidth()/2) );
                ay=y2;
                axx=(int)r.getWidth()+20;
                ayy=(int)r.getHeight();
                                
                if (rx.x<ax) ax=rx.x;
                if (rx.y<ay) ay=rx.y;
                if (rx.width>axx) axx=rx.width;
                if (rx.height>ayy) ayy=rx.height;
                
                if (rx.x<0) rx.x=0;
                if (rx.y<0) rx.y=0;
                
                
                //getVMObject().panel.repaint(ax-10,ay-10,axx+10,ayy+10);
                getVMObject().panel.repaint(ax-5,ay-15,axx+15,ayy+30);
                
                //getVMObject().panel.repaint(-10+getX()+(int) ( ((getWidth()) /2-nameBounds.getWidth()/2) ),y2,(int)r.getWidth()+20,(int)r.getHeight());                                
            }
        }        
        
    }*/
   
   
    

    
    public void paintComponent(Graphics2D g)
    {          
        super.paintComponent(g);        
    }
    
    public void paintComponent(Graphics g)
    {          
        super.paintComponent(g);        
    }
    
}
