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
import java.awt.*;
import VisualLogic.*;

public interface StatusBasisIF 
{    
    public abstract void mouseDragged(MouseEvent e);
    public abstract void mousePressed(MouseEvent e);
    public abstract void mouseReleased(MouseEvent e);   
    public abstract void mouseDblClick(MouseEvent e);       
    public abstract void mouseClicked(MouseEvent e);    
    public abstract void mouseEntered(MouseEvent e);
    public abstract void mouseExited(MouseEvent e);
    public abstract void mouseMoved(MouseEvent e);
    
    public abstract void processKeyEvent(KeyEvent ke);
    
    // fuer das Draht ziehen ist nur diese Methode notwendig!
    public abstract void elementPinMouseReleased(MouseEvent e, int elementID,int pin);
    public abstract void elementPinMousePressed(MouseEvent e, int elementID,int pin);
    public abstract void elementPinMouseMoved(MouseEvent e, int elementID,int pin);
    
    
    public abstract void draw(Graphics g);
}
 
