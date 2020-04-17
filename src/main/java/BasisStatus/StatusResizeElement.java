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


package BasisStatus;
import java.awt.event.*;
import VisualLogic.*;
import java.awt.*;

/**
 *
 * @author Carmelo
 */
public class StatusResizeElement extends Object implements StatusBasisIF
{    
    private VMObject vmobject;
    private Element element;
    private boolean firstTime=true;
    private int rect; // gibt eines der Rand Rectecke an um die Groesse zu aendern!
    Point oldP;
    int oWidth;
    int oHeight;
    
    int dx;
    int dy;
    
    int dxx;
    int dyy;
    
    /** Creates a new instance of ResizeElement */
    public StatusResizeElement(VMObject vmobject, Element element, int rect)
    {
        this.rect=rect;
        this.vmobject=vmobject;
        this.element=element;
        firstTime=true;
                        
        oldP=element.getLocation();
        oWidth=element.getWidth();
        oHeight=element.getHeight();  
               
        int x1=element.getX();
        int y1=element.getY();
        
        int x2=element.getWidth();
        int y2=element.getHeight();

        Point p=vmobject.pointToRaster(element,x1,y1);
        dx=x1-p.x;
        dy=y1-p.y;

        p=vmobject.pointToRaster(element,x2,y2);
        dxx=x2-p.x;
        dyy=y2-p.y;
        
        //setOriginalWidthHeight();
    }

    
    private Point setLocationX(Element element,int x, int y)
    {
       Point p=vmobject.pointToRaster(element,x,y);              
       element.setLocation(p.x+dx,p.y+dy);
       return p;
    }

    private void setSizeX(Element element,int x, int y)
    {
       Point p=vmobject.pointToRaster(element,x,y);
       element.setSize(p.x-dxx,p.y-dyy);
    }    
    
    public void mouseDragged(MouseEvent e)
    {
        if ((e.getSource() instanceof SelectionPane))
        {
            
            if (firstTime)
            {
                //element.setXorMode();
                firstTime=false;
            }
            //element.repaint();           
            int dist=6;
            int x=e.getX()+oldP.x;
            int y=e.getY()+oldP.y;
            
            
            int xx=0;
            int yy=0;
            
            if (vmobject!=null )
            {
                try
                {
                    xx=vmobject.getMousePosition().x;
                    yy=vmobject.getMousePosition().y;
                } catch(Exception ex)
                {
                   return; 
                }
            }
                        

            Point p;
            
            if (element.isResizeSynchron())
            {                
                double asp=element.getAspect();
                y=(int)((float)(x-element.getX())*asp);
                switch (rect)
                {                    
                    case 8 : 
                    {
                        xx=x-element.getX();
                        yy=y+dist;
                        p=vmobject.pointToRaster(element,xx,yy);
                        element.setSize(p.x+dxx,p.y+dyy); 
                        break;
                    }
                }
            }else
            {
                switch (rect)
                {
                   case 1 : 
                   {
                       p=setLocationX(element,xx,yy);
                       setSizeX(element,oWidth+(oldP.x-p.x),oHeight+(oldP.y-p.y)); 
                       break;
                   }
                   case 2 : 
                   {
                       p=setLocationX(element,element.getX(),yy);                       
                       element.setSize(oWidth,oHeight+(oldP.y-(p.y)));
                       break;
                   }
                   case 3 : 
                   {
                       p=setLocationX(element,element.getX(),yy);                                       
                       setSizeX(element, xx-element.getX()+dist,oHeight+(oldP.y-p.y));
                       break;
                   }
                   case 4 : 
                   {
                       p=setLocationX(element,xx,element.getY()); 
                       element.setSize(oWidth+(oldP.x-p.x),element.getHeight());
                       break;
                   }
                   case 5 : 
                   {
                       setSizeX(element,xx-element.getX()+dist  ,oHeight);
                       break;
                   }
                   case 6 : 
                   {
                       p=setLocationX(element,xx,element.getY()); 
                       setSizeX(element,oWidth+(oldP.x-p.x),y-element.getY()+dist);
                       break;
                   }
                   case 7 : 
                   {
                       //setSizeX(element,element.getWidth(), yy-element.getY()+dist);
                       element.setSize(oWidth,yy-element.getY()+dist);
                       break;
                   }
                   case 8 : 
                   {
                       setSizeX(element,xx-element.getX()+dist ,yy-element.getY()+dist); 
                       break;
                   }
                }
            }
            
            //resizeBasisElements();

            element.updateUI();
                        
        }
    }
    
    
    public void mouseReleased(MouseEvent e)
    {      
      element.setNormalMode();
      element.setSize(element.getWidth(),element.getHeight());
      vmobject.reorderWireFrames();
      vmobject.setModusIdle(); 
      element.processPropertyEditor();
      element.updateUI();
      vmobject.owner.saveForUndoRedo();
      
    }
    
    public  void elementPinMousePressed(MouseEvent e, int elementID,int pin)
    {
    }    
    
    public void elementPinMouseReleased(MouseEvent e, int elementID,int pin){}
    public void elementPinMouseMoved(MouseEvent e, int elementID,int pin){}
    public void processKeyEvent(KeyEvent ke){}
    public void mousePressed(MouseEvent e){}
    public void mouseDblClick(MouseEvent e){}
    public void mouseClicked(MouseEvent e){}    
    public void mouseEntered(MouseEvent e){}    
    public void mouseExited(MouseEvent e){}    
    public void mouseMoved(MouseEvent e){}
    public void draw(java.awt.Graphics g){}
}
 

