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
//import com.sun.swing.internal.plaf.basic.resources.basic;
import java.awt.event.*;
import java.awt.*;
import VisualLogic.*;

/**
 *
 * @author Homer
 */
public class StatusResizeBasis  extends Object implements StatusBasisIF
{    
    private VMObject vmObject;
    private int startX, startY;
    private int oldX,oldY;
    private boolean first=true;
    /** Creates a new instance of GummiBand */
    
    public StatusResizeBasis(VMObject vmObject, int startX, int startY) 
    {
        this.vmObject=vmObject;
        this.startX=startX;
        this.startY=startY;        
    }      
       
    public void processKeyEvent(KeyEvent ke) {}    
    public  void elementPinMousePressed(MouseEvent e, int elementID,int pin)
    {
    }    
    
    public  void elementPinMouseReleased(MouseEvent e, int elementID,int pin) {}
    public void elementPinMouseMoved(MouseEvent e, int elementID,int pin) {}     
    public void mouseDblClick(MouseEvent e) {}
    public void mouseDragged(MouseEvent e)
    {
        int x=e.getX();
        int y=e.getY();
        
        if (x<=30) x=30;
        if (y<=30) y=30;
        vmObject.setSize(x,y);
    }
    
    public void mousePressed(MouseEvent e)
    {        
    }
    
    public void mouseReleased(MouseEvent e)
    {                
        vmObject.reorderWireFrames();
        vmObject.setModusIdle();
        vmObject.owner.saveForUndoRedo();
        vmObject.processPropertyEditor();
    }

    public void mouseClicked(MouseEvent e) {}     
    public void mouseEntered(MouseEvent e){} 
    public void mouseExited(MouseEvent e){} 
    public void mouseMoved(MouseEvent e){} 
    public void draw(Graphics g){} 
    
}
