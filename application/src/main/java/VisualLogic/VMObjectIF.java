/*
 * Copyright (C) 2020 MyLibreLab
 * Based on MyOpenLab by Carmelo Salafia www.myopenlab.de
 * Copyright (C) 2004  Carmelo Salafia cswi@gmx.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package VisualLogic;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * @author Homer
 */
public interface VMObjectIF {
    void elementPinMousePressed(MouseEvent e, int elementID, int pin);

    void elementPinMouseReleased(MouseEvent e, int elementID, int pin);

    void elementPinMouseClicked(MouseEvent e, int elementID, int pin);

    void elementPinMouseEntered(MouseEvent e, int elementID, int pin);

    void elementPinMouseExited(MouseEvent e, int elementID, int pin);

    void elementPinMouseDragged(MouseEvent e, int elementID, int pin);

    void elementPinMouseMoved(MouseEvent e, int elementID, int pin);

    void elementMouseClicked(MouseEvent e);

    void elementMouseDblClick(MouseEvent e);

    void elementMouseEntered(MouseEvent e);

    void elementMouseExited(MouseEvent e);

    void elementMouseReleased(MouseEvent e);

    void elementMousePressed(MouseEvent e);

    void elementMouseDragged(MouseEvent e);

    void elementMouseMoved(MouseEvent e);

    void elementProcessKeyEvent(KeyEvent ke);

    void reorderWireFrames();
}
