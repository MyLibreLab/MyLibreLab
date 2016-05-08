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
import java.awt.event.*;

/**
 *
 * @author Homer
 */
public interface VMObjectIF 
{
    public abstract void elementPinMousePressed(MouseEvent e, int elementID,int pin);
    public abstract void elementPinMouseReleased(MouseEvent e, int elementID,int pin);
    public abstract void elementPinMouseClicked(MouseEvent e, int elementID,int pin);
    public abstract void elementPinMouseEntered(MouseEvent e, int elementID,int pin);
    public abstract void elementPinMouseExited(MouseEvent e, int elementID,int pin);    
    public abstract void elementPinMouseDragged(MouseEvent e, int elementID,int pin);
    public abstract void elementPinMouseMoved(MouseEvent e, int elementID,int pin);
    
    public abstract void elementMouseClicked(MouseEvent e);
    public abstract void elementMouseDblClick(MouseEvent e);    
    public abstract void elementMouseEntered(MouseEvent e);
    public abstract void elementMouseExited(MouseEvent e);
    public abstract void elementMouseReleased(MouseEvent e);
    public abstract void elementMousePressed(MouseEvent e) ;
    public abstract void elementMouseDragged(MouseEvent e); 
    public abstract void elementMouseMoved(MouseEvent e) ;
    public abstract void elementProcessKeyEvent(KeyEvent ke);
        
    public abstract void reorderWireFrames();
}
