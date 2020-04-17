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

/**
 *
 * @author Homer
 */
public class StatusGummiBand extends Object implements StatusBasisIF {

    private VMObject vmObject;
    private int startX, startY;
    private int oldX, oldY;
    private boolean first = true;

    /**
     * Creates a new instance of GummiBand
     */

    public StatusGummiBand(VMObject vmObject, int startX, int startY) {
        this.vmObject = vmObject;
        this.startX = startX;
        this.startY = startY;
    }

    public void mouseDblClick(MouseEvent e) {

    }

    public void finalize() {
    }

    public void elementPinMousePressed(MouseEvent e, int elementID, int pin) {
    }

    public void elementPinMouseReleased(MouseEvent e, int elementID, int pin) {

    }

    public void elementPinMouseMoved(MouseEvent e, int elementID, int pin) {

    }

    public void processKeyEvent(KeyEvent ke) {

    }

    private Point drawRectangle(Graphics2D g2, int x, int y, int xx, int yy) {
        Point p = new Point();
        int temp = 0;
        p.x = xx;

        p.y = yy;

        if (xx < x) {
            temp = x;
            x = xx;
            xx = temp;
            p.x = x;

        }
        if (yy < y) {
            temp = y;
            y = yy;
            yy = temp;
            p.y = y;
        }

        float dash[] = {1.0f, 1.0f};
        g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 3.0f, dash, 0.0f));

        g2.drawRect(x, y, xx - x, yy - y);

        return p;

    }

    public void mouseDragged(MouseEvent e) {
        Graphics g = vmObject.getGraphics();

        Graphics2D g2 = (Graphics2D) g;

        g.setXORMode(java.awt.Color.white);

        int x = e.getX();
        int y = e.getY();

        Point p;
        if (first == false) {
            drawRectangle(g2, startX, startY, oldX, oldY);
        }
        first = false;
        try {
            p = drawRectangle(g2, startX, startY, x, y);
            oldX = p.x;
            oldY = p.y;
        } catch ( java.lang.InternalError ex) {

        }

    }

    /*
     * wurde aus ein PolyLine Punkt gedrueckt
     **/
    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {
        int x = startX;
        int y = startY;
        int xx = e.getX();
        int yy = e.getY();
        int temp = 0;

        if (xx < x) {
            temp = x;
            x = xx;
            xx = temp;
        }

        if (yy < y) {
            temp = y;
            y = yy;
            yy = temp;
        }
        vmObject.markAllinRect(x, y, xx, yy);
        vmObject.repaint();
        vmObject.setModusIdle();
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {

    }

    public void draw(Graphics g) {

    }

}
