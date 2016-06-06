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




public class StatusEditPath extends Object implements StatusBasisIF
{
   
    StatusBasisIF status=null;
    public Element element;
    
    public StatusEditPath(VMObject vmobject, Element element)
    {
        this.element=element;
        
        if (element.points.size()==0) 
        {
            status=new StatusEditPathAddMode(vmobject,element);            
        }else
        {
            status=new StatusEditPathEditMode(vmobject,element);
        }

    }
    
    public void elementPinMouseMoved(MouseEvent e, int elementID,int pin)
    {        
    }
    
    public void mouseDragged(MouseEvent e)
    {
        if (status!=null) status.mouseDragged(e);
    }
    
    public void mouseMoved(MouseEvent e)
    {
        if (status!=null) status.mouseMoved(e);
    }
    
    
    
    public void mousePressed(MouseEvent e)
    {
        if (status!=null) status.mousePressed(e);
    }
    
    public void mouseReleased(MouseEvent e)
    {
     if (status!=null) status.mouseReleased(e);
    }
    
    
    
    public void draw(Graphics g)
    {
        if (status!=null) status.draw(g);
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

