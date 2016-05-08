//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia (cswi@gmx.de)                         *
//*                                                                           *
//* This library is free software; you can redistribute it and/or modify      *
//* it under the terms of the GNU Lesser General Public License as published  *
//* by the Free Software Foundation; either version 2.1 of the License,       *
//* or (at your option) any later version.                                    *
//* http://www.gnu.org/licenses/lgpl.html                                     *
//*                                                                           *
//* This library is distributed in the hope that it will be useful,           *
//* but WITHOUTANY WARRANTY; without even the implied warranty of             *
//* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                      *
//* See the GNU Lesser General Public License for more details.               *
//*                                                                           *
//* You should have received a copy of the GNU Lesser General Public License  *
//* along with this library; if not, write to the Free Software Foundation,   *
//* Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA                  *
//*****************************************************************************
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import VisualLogic.*;
import VisualLogic.variables.*;

/*
 * NewJFrame.java
 *
 * Created on 19. Mai 2007, 07:52
 */
public class KeyPanel extends JPanel
{
    private boolean hasfocus=false;

    public Color color=new Color(100,100,100);
    private int transparency=255;
    public ExternalIF circuitElement;

    public KeyPanel()
    {
        setFocusable(true);
        setOpaque(false);
        enableEvents(AWTEvent.FOCUS_EVENT_MASK);        // catch Focus-Events
        enableEvents(AWTEvent.KEY_EVENT_MASK);          // catch KeyEvents
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);        // catch MouseEvents
        enableEvents(AWTEvent.COMPONENT_EVENT_MASK);    // catch ComponentEvents
    }
    
    
    public void setTransparency(int value)
    {
      transparency=value;
      repaint();
    }

    protected void processComponentEvent(ComponentEvent co)
    {
        if (co.getID() == ComponentEvent.COMPONENT_SHOWN)   requestFocus();

        super.processComponentEvent(co);
    }

    protected void processMouseEvent(MouseEvent me)
    {
        switch(me.getID())
        {
            case MouseEvent.MOUSE_PRESSED: requestFocus();
        }
        super.processMouseEvent(me);
    }

    protected void processKeyEvent(KeyEvent event)
    {
        int code = event.getKeyCode();

        if (event.getID() == KeyEvent.KEY_RELEASED)
        {
          if (circuitElement!=null)circuitElement.Change(1,new VSInteger(code));
        }

        if (event.getID() == KeyEvent.KEY_PRESSED)
        {
            //System.out.println("CODE="+code);
            if (circuitElement!=null)circuitElement.Change(0,new VSInteger(code));
        }


        super.processKeyEvent(event); // diese Zeile nicht mehr hinzufügen!!!
    }

    protected void processFocusEvent(FocusEvent event)
    {
        if (event.getID() == FocusEvent.FOCUS_GAINED)
        {
            hasfocus = true;
            repaint();
        }
        else if (event.getID() == FocusEvent.FOCUS_LOST)
        {
            hasfocus = false;
            repaint();
        }
        super.processFocusEvent(event);
    }


    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        g.setColor(new Color(color.getRed() ,color.getGreen(), color.getBlue(), transparency));
        g.fillRect(0,0,getWidth(),getHeight());

        g.setColor(Color.BLACK);
        if (hasfocus)
        {
            g.drawRect(0,0,getWidth()-1,getHeight()-1);
        }
    }

}


