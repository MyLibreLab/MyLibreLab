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
public interface PinIF 
{
    public abstract void PinMousePressed(MouseEvent e, JPin pin,int pinIndex);
    public abstract void PinMouseReleased(MouseEvent e, JPin pin,int pinIndex);
    public abstract void PinMouseClicked(MouseEvent e, JPin pin,int pinIndex);     
    public abstract void PinMouseEntered(MouseEvent e, JPin pin,int pinIndex);     
    public abstract void PinMouseExited(MouseEvent e, JPin pin,int pinIndex);
    public abstract void PinMouseDragged(MouseEvent e, JPin pin,int pinIndex);
    public abstract void PinMouseMoved(MouseEvent e, JPin pin,int pinIndex);
}
