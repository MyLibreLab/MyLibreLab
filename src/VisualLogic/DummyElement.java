/*
 * DummyElement.java
 *
 * Created on 10. April 2007, 15:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package VisualLogic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import javax.swing.JPanel;

/**
 *
 * @author Salafia
 */
public class DummyElement extends JPanel
{
    private Stroke strokeDick=new BasicStroke(5);
    /** Creates a new instance of DummyElement */
    public DummyElement()
    {
        setOpaque(false);
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        Graphics2D g2 =(Graphics2D)g;
        
        g2.setStroke(strokeDick);
        g2.setColor(Color.RED);
        g2.drawLine(0,0,50,0);
        g2.drawLine(0,0,0,50);
    }
    
}
