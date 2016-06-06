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
import VisualLogic.Element;
import java.awt.event.*;
import java.awt.*;

public class StatusNone extends Object implements StatusBasisIF
{
    private VMObject vmObject;    
    public  Element element;
    
    
    public VMObject getBasis()
    {
        return this.vmObject;
    }
    
    
    public StatusNone(VMObject vmObject, Element element)
    {
        this.vmObject=vmObject;
        this.element=element;        
    }
    
    public void mouseDblClick(MouseEvent e)
    {
    }
    
    
    public void showElementInfo()
    {
    }
    
    public void openFile(String fileName)
    {
    }
    
    
    public void elementPinMouseReleased(MouseEvent e, int elementID,int pin)
    {
        
    }
    
    public  void elementPinMousePressed(MouseEvent e, int elementID,int pin)
    {
    }
    
    
    
    public void elementPinMouseMoved(MouseEvent e, int elementID,int pin)
    {
    }
    

    
    public void processKeyEvent(KeyEvent ke)
    {                
    }


    public void mouseDragged(MouseEvent e)
    {
        
        if (e.getSource() instanceof Element)
        {            
            Element el=(Element)e.getSource();
            //e.translatePoint(-e.getX(),-e.getY());
            //e.translatePoint(oldX,oldY);
            //vmObject.setModusMoveElements(e);
            
            element.mouseDragged(e);
        } 
        
    }
    

    private int getResizeRect(Element element, int x, int y)
    {
        return 0;
    }
    
    
    /*
     * wurde aus ein PolyLine Punkt gedrueckt
     **/    
    public void mousePressed(MouseEvent e)
    {                       
        if (e.getSource() instanceof Element)
        {            
            Element el=(Element)e.getSource();
            //e.translatePoint(-e.getX(),-e.getY());
            //e.translatePoint(oldX,oldY);
            //vmObject.setModusMoveElements(e);
            
            //element.mousePressed(e);
        } 
                
    }
    

                
    public void mouseReleased(MouseEvent e)
    {               

    }

    public void mouseClicked(MouseEvent e)   
    {
        
    }
    
    public void mouseEntered(MouseEvent e)
    {
        
    }
    
    public void mouseExited(MouseEvent e)
    {
        
    }
    
    
    public void mouseMoved(MouseEvent e)
    {   
    }
    
    public void draw(Graphics g)
    {
        
    }
    
}
 
