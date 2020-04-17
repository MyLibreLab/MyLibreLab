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
import java.util.ArrayList;

class MyLine
{
    private Point start;
    private Point end;
    
    public MyLine(Point start,Point end)
    {
        this.start=start;
        this.end=end;        
    }
}

public class StatusAddWire extends Object implements StatusBasisIF
{
    public VMObject vmobject;         
    private ArrayList drahtPoints = new ArrayList();
    private static final int HOZ=0;
    private static final int VERT=1;
    private int aktuellesPinType=HOZ;
    private StatusBasisIF status=null;
    
        
    public StatusAddWire(VMObject vmobject,Graphics g, int sourceElementID,int sourcePin)
    {
        this.vmobject=vmobject;        
        
        //createCursors();
        vmobject.disableAllElements();
        
        Element sourceElement = vmobject.getElementWithID(sourceElementID);        
        JPin pin = sourceElement.getPin(sourcePin);
        
        if (pin.pinAlign==1 || pin.pinAlign==3)
        aktuellesPinType=HOZ; else aktuellesPinType=VERT;
                
        Point start = sourceElement.getPinPosition(sourcePin);
                       
        if (aktuellesPinType==HOZ)
        {
            status=new StatusLineHoritontal(vmobject,drahtPoints,sourceElementID, sourcePin,start);
        } else
        {
            status=new StatusLineVertikal(vmobject,drahtPoints,sourceElementID, sourcePin,start);
        }
    }
    

    
    
    public void elementPinMouseMoved(MouseEvent e, int elementID,int pin)
    {
        Element element= (Element)vmobject.getObjectWithID(elementID); 
        //e.translatePoint(e.getX()+element.getX(),e.getY()+element.getY());
        if (status!=null) status.mouseMoved(e); 
    }
    

    
        
    public void mouseMoved(MouseEvent e)
    {    
       if (vmobject==null)  return;
       
       if (lastOverElement!=null)
       {         
         lastOverElement=null;
       }        
       Point p =vmobject.getMousePosition();
       if (p==null) return;
       
       int x = p.x;
       int y = p.y;
       Element element=vmobject.getNearstElementInMouse(x,y,20);
       if (element instanceof Element && vmobject==vmobject.owner.getCircuitBasis())
       {           
                  
         element.inDenVordergrund();
         lastOverElement=element;
       } 
        
        if (status!=null) status.mouseMoved(e);
    }
    
    public void mousePressed(MouseEvent e)
    {   
        if (status!=null) status.mousePressed(e);        
    }
    
    public void mouseReleased(MouseEvent e) { }
    

    
    public void draw(Graphics g)
    {                
        if (status!=null) status.draw(g);
    }
    

    public void elementPinMouseReleased(MouseEvent e, int elementID,int pin){}
    public void elementPinMousePressed(MouseEvent e, int elementID,int pin) 
    {
       if (status!=null) status.elementPinMousePressed(e,elementID,pin);
    }
    public void mouseDblClick(MouseEvent e){}
    public void processKeyEvent(KeyEvent ke){}        
    public void mouseDragged(MouseEvent e){}                       
    public void mouseClicked(MouseEvent e){}    
    public void mouseEntered(MouseEvent e){}    
    public void mouseExited(MouseEvent e){}    

    Element lastOverElement=null;
    
    public void elementMouseEntered(MouseEvent e)
    {
        
    }

    public void elementMouseExited(MouseEvent e)
    {

    }
    

}
 
