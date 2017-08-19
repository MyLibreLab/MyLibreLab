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

import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

/**
 *
 * @author  Carmelo
 */
public class SelectionPane extends JPanel implements MouseListener, MouseMotionListener, KeyListener
{    
    private Element element;
    private boolean alwaysOnTop=false;
    private Color colorSelected=new Color(255,0,0,110);
    private Color colorInavtive=new Color(150,150,150,0);
    
    public Element getElement()
    {
        return element;
    }
    public void start()
    {
        super.setVisible(false);
    }
    
    public void stop()
    {
        if (alwaysOnTop)
        {
            setVisible(true);
        }
    }
    
    public void setVisible(boolean value)
    {
        if (alwaysOnTop)
        {
            super.setVisible(true);
            
            if (element.isSelected())
            {
                setBackground(colorSelected);
            }
            else
            {
                setBackground(colorInavtive);
            }
            
        }
        else
        {
            super.setVisible(value);
        }
        
    }
    /** Creates new form SelectionPane */
    public SelectionPane(Element element)
    {
        this.element=element;
        initComponents();
        setOpaque(false);
        setLayout(null);
        setBackground(colorSelected);
        setLocation(0,0);
        setSize(550,550);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        
        enableEvents(AWTEvent.FOCUS_EVENT_MASK);        // catch Focus-Events
        enableEvents(AWTEvent.KEY_EVENT_MASK);          // catch KeyEvents
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);        // catch MouseEvents
        enableEvents(AWTEvent.COMPONENT_EVENT_MASK);    // catch ComponentEvents
    }
    
//--------------------------------------------------------------
    
    @Override
    //public boolean isFocusTraversable()
    public boolean isFocusable()
    {
        return true;
    }
    
    @Override
    protected void processComponentEvent(ComponentEvent event)
    {
        if (event.getID() == ComponentEvent.COMPONENT_SHOWN)
        {
            requestFocus();
        }
        super.processComponentEvent(event);
    }
    
    @Override
    protected void processFocusEvent(FocusEvent event)
    {
        if (event.getID() == FocusEvent.FOCUS_GAINED)
        {
            //hasfocus = true;
            //repaint();
        }
        else if (event.getID() == FocusEvent.FOCUS_LOST)
        {
            //hasfocus = false;
            //repaint();
        }
        super.processFocusEvent(event);
    }
    
    
  /* protected void processComponentEvent(ComponentEvent co)
   {
      if (co.getID() == ComponentEvent.COMPONENT_SHOWN)   requestFocus();
   
      super.processComponentEvent(co);
   }*/
    
    // with that Method you can set Focus when
    // the you clicked with Mouse into Canvas!
    
    protected void processMouseEvent(MouseEvent event)
    {
        if (event.getID() == MouseEvent.MOUSE_PRESSED)
        {
            requestFocus();
        }
        super.processMouseEvent(event);
    }
    
    // with that Method you can handle your Keys
    
    protected void processKeyEvent(KeyEvent ke)
    {
        element.processKeyEvent(ke);
        
        //  super.processKeyEvent(ke);
    }
    
    
//--------------------------------------------------------------
    
    
    public void setAlwaysOnTop(boolean value)
    {
        alwaysOnTop=value;
        if (value)
        {
            setBackground(colorInavtive);
        }
        else
        {
            setBackground(colorSelected);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    

    private void paintSelection(Graphics2D g2)
    {
        //g2.setColor(new Color(100,100,100,128));
        
       /* BufferedImage image=element.owner.owner.selectionImage;
        if (image!=null)
        {
            
            Rectangle2D tr = new Rectangle2D.Double(0, 0, image.getWidth(), image.getHeight());
            TexturePaint tp = new TexturePaint(image, tr);
                        
            g2.setPaint(tp);                        
        }*/
        g2.fillRect(0,0,getWidth(), getHeight());            
    }   
    
    private int strokeWidth=10;
    private Stroke standardStroke=new BasicStroke(strokeWidth);
    
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        int w=getWidth()-2;
        int h=getHeight()-2;        
        int d=6;
        int w2=(w/2)-(d/2);
        int h2=(h/2)-(d/2);
        
        //g.setColor(getBackground());                        
        //paintSelection((Graphics2D)g);
        
        g.setColor(getBackground());      
        paintSelection((Graphics2D)g);
        /*if (element.owner.owner.debugMode==false)
        {            
            paintSelection((Graphics2D)g);
        } else
        {            
            g2.setStroke(standardStroke);
            g.drawRect(strokeWidth,strokeWidth,getWidth()-strokeWidth*2, getHeight()-strokeWidth*2);
        }*/
        
                
        if (element.isSelected() && element.owner.owner.debugMode==false)
        {
                        
            if (element.isResizeSynchron())
            {
                drawSelectionPin(g,w-d, h-d,d); // Right-Bottom
            }
            else
                if (element.isResizable())
                {                
                    drawSelectionPin(g,0,   0,d); // Left-Top
                    drawSelectionPin(g,w2,  0,d); // Center-Top
                    drawSelectionPin(g,w-d, 0,d); // Right-Top
                    drawSelectionPin(g,0,   h2,d); // Left-Center
                    drawSelectionPin(g,w-d, h2,d); // Right-Center
                    drawSelectionPin(g,0,   h-d,d); // Left-Bottom
                    drawSelectionPin(g,w2,  h-d,d); // Center-Bottom
                    drawSelectionPin(g,w-d, h-d,d); // Right-Bottom
                }
        }
    }
    
    public void mouseClicked(MouseEvent e)
    {
        if (element!=null) element.mouseClicked(e);
    }
    
    public void mousePressed(MouseEvent e)
    {
        if (element!=null) element.mousePressed(e);
    }
    
    public void mouseReleased(MouseEvent e)
    {
        if (element!=null) element.mouseReleased(e);
    }
    
    public void mouseEntered(MouseEvent e)
    {
        if (element!=null) element.mouseEntered(e);
    }
    
    public void mouseExited(MouseEvent e)
    {
        if (element!=null) element.mouseExited(e);
    }
    
    public void mouseDragged(MouseEvent e)
    {        
        if (element!=null) 
        {            
            element.mouseDragged(e);
        }
    }
    
    public void mouseMoved(MouseEvent e)
    {
        if (element!=null) element.mouseMoved(e);
    }
    
    private void drawSelectionPin(Graphics g,int x, int y, int w)
    {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, w, w);
        g.setColor(Color.BLACK);
        g.fillRect(x+1, y+1, w-2, w-2);;       
    }
    
    public void keyTyped(KeyEvent e)
    {
    }
    
    public void keyPressed(KeyEvent e)
    {
    }
    
    public void keyReleased(KeyEvent e)
    {
    }
    
}
