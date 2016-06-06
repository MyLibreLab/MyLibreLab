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


import VisualLogic.*;
import java.awt.event.*;
import java.awt.*;

public class StatusEditPath_ADD_CurveTo extends Object implements StatusBasisIF
{
    public VMObject vmobject;
    public Element element;
    public PathPoint path;
    public StatusEditPathAddMode owner;
    public int pathIndex=-1;

    public StatusEditPath_ADD_CurveTo(StatusEditPathAddMode owner,VMObject vmobject, Element element, int pathIndex)
    {
        this.owner=owner;
        this.vmobject=vmobject;
        this.element=element;
        this.pathIndex=pathIndex;
        
    }
    
    public void elementPinMouseMoved(MouseEvent e, int elementID,int pin)
    {        
    }
    
    public void mouseDragged(MouseEvent e)
    {       
        PathPoint path=element.points.get(pathIndex);
        if (path!=null)
        {
            int x=e.getX();
            int y=e.getY();

            int dx=x-path.p.x;
            int dy=y-path.p.y;
            path.p1.x=path.p.x-dx;
            path.p1.y=path.p.y-dy;                            

            path.p2.x=x;
            path.p2.y=y;

            element.updateUI();
        }
    }
    
    public void mouseMoved(MouseEvent e)
    {
        
    }
    
    
    
    public void mousePressed(MouseEvent e)
    {
        
    }
    
    public void mouseReleased(MouseEvent e)
    {
      owner.status=null;  
    }
    
    
    
    public void draw(Graphics g)
    {
        
    }
    
    
    public void elementPinMouseReleased(MouseEvent e, int elementID,int pin)
    {}
    public void elementPinMousePressed(MouseEvent e, int elementID,int pin)
    {
    }
    public void mouseDblClick(MouseEvent e)
    {}
    public void processKeyEvent(KeyEvent ke)
    {}
    
    public void mouseClicked(MouseEvent e)
    {}
    public void mouseEntered(MouseEvent e)
    {}
    public void mouseExited(MouseEvent e)
    {}
    
    
    public void elementMouseEntered(MouseEvent e)
    {
        
    }
    
    public void elementMouseExited(MouseEvent e)
    {
        
    }
    
    
}

