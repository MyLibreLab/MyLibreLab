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
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.geom.*;
/**
 *
 * @author Homer
 */
public class JPin extends VisualObject implements MouseListener, MouseMotionListener
{    
    public static final byte PIN_UNDEFINIED=0;
    public static final byte PIN_INPUT=1;
    public static final byte PIN_OUTPUT=2;
    public static final byte PIN_INPUT_OUTPUT=3;
    
    private Font fnt = new Font("Arial",0,9);
    
    public Element element;
    public byte pinIO=PIN_UNDEFINIED;
    
    public PinIF pinIF;    // enthaelt das Element andem die Events/Steuerbefehle gehen -> fuer die Events
    public int pinIndex;   // jeder Pin hat einen Index
    public  Draht draht;    // liefert die Draht Referenz das mit einem anderen Pin verbunden ist
    public int pinAlign;
    
    
            
    private boolean highlighted=false;
    private boolean pointPin=false;
    private boolean visible=true;
    
    public int ownerElementID=-1; // wird nur fuer Gruppierung verwendet
    public int ownerSrcPinID=-1; // wird nur fuer Gruppierung verwendet
                    
    // enthaelt der Wert fuer den I/O
    public double value;    
    
    public int dataType = -1;  // nur in Verbindung zu object
    public Object object=null;
    
    private Stroke strockeStandard=new BasicStroke(1);
    private String description="";
    public void setDescription(String value)
    {        
        description=value;         
    }
    
    public void setPointPin()
    {
        pointPin=true;
    }
    
    public String getDescription() {return description;}
    
    /** Creates a new instance of JMainElement */
    // pinAlign liefert 
    // 0 fuer Top
    // 1 fuer Right
    // 2 fuer Bottom
    // 3 fuer left
    public JPin(PinIF pinIF, int pinIndex, int pinAlign) 
    {        
        super();
        this.element=(Element)pinIF;
        this.pinIF = pinIF;
        this.pinIndex=pinIndex;
        this.pinAlign=pinAlign;
        // Standard Werte setzen  
        
        setOpaque(false);
        setBackground(nonSelectedColor);
        
        value=0;
    }   
    
    //private Color selectedColor = new Color(255,0,0,200);
    private Color nonSelectedColor = new Color(150,0,0,0);
    
    public boolean isHighlighted()
    {
        return highlighted;
    }
    public void setHighlighted(boolean value)
    {
        highlighted=value;
        
        /*if (isHighlighted())
        {
            Color color =VSDataType.getCoorFromDataType(this.dataType);
            setBackground(color);
        } else
        {
            setBackground(nonSelectedColor);
        }*/
        repaint();
    }
    
    
    public void setVisible(boolean value)
    {
        if (visible!=value)
        {
            visible=value;
        }
    }
    public int getPinAlign()
    {
        return pinAlign;
    }
    public void mouseClicked(MouseEvent e) 
    {        
        if (pinIF!=null) pinIF.PinMouseClicked(e,this,pinIndex);
    }
    public void mouseEntered(MouseEvent e) 
    {
        if (pinIF!=null) pinIF.PinMouseEntered(e,this, pinIndex);  
    }
            
    public void mouseExited(MouseEvent e) 
    {
        if (pinIF!=null) pinIF.PinMouseExited(e,this, pinIndex);  
    }

    public void mousePressed(MouseEvent e) 
    {
        if (pinIF!=null) pinIF.PinMousePressed(e,this,pinIndex);  
    }

    public void mouseReleased(MouseEvent e) 
    {
        if (pinIF!=null) pinIF.PinMouseReleased(e,this, pinIndex);
    }
    
    public void mouseDragged(MouseEvent e) 
    {
        if (pinIF!=null) pinIF.PinMouseDragged(e,this, pinIndex);
    }
    
    public void mouseMoved(MouseEvent e) 
    {                
        if (pinIF!=null) pinIF.PinMouseMoved(e,this, pinIndex);
        
    }  
    
    
    /*public void paint(int x,int y,Graphics2D g, boolean drawFrame)
    {                                
        int mitteX=getWidth()/2;
        int mitteY=getHeight()/2;
        int x1=x+getX();
        int y1=y+getY();

        g.setColor(Color.white);
        g.fillRect(x1+1,y1+1, getWidth()-2,getHeight()-1);
                                      
        g.setFont(fnt);                        
        FontMetrics fm = g.getFontMetrics();       
        Rectangle2D r = fm.getStringBounds(getDescription(),g);        
        

        int dis=1;
        if (!visible) return;
        
            //description=""+value;
            if (pinAlign==1 || pinAlign==3) // Horizontal
            {
            
               VSDataType.setColorStrokeFromDataType(g,this.dataType);
               g.drawLine(x+getX()+1, y+getY()+mitteY, x+getX()+getWidth()-1,y+getY()+mitteY);
               g.setColor(Color.black);
               if (pinAlign==1)
               {
                   // Pins Rechts
                   int py=4;
                   //g.drawString(description,x+getX()-(int)r.getWidth()-dis,y+getY()+(int)r.getHeight()-getHeight()/2 +py);
               } else 
               {
                   // Pins links                                            
                   int py=4;
                   //g.drawString(description,x+getX()+getWidth()+dis,y+getY()+(int)r.getHeight()-getHeight()/2+py);   
               }
            } else
            {                
                VSDataType.setColorStrokeFromDataType(g,this.dataType);
                g.drawLine(x+getX()+mitteX, y+getY()+1, x+getX()+mitteY, y+getY()+getHeight()-1);
                g.setColor(Color.black);
                
               int py=6;
               if (pinAlign==0)
               {
                   // Pins Oben
                   //g.drawString(description,x+getX()+2,y+getY()+(int)r.getHeight()-getHeight()/2+py);
               } else 
               {
                   // Pins Unten                    
                   //g.drawString(description,x+getX()+2,y+getY()+(int)r.getHeight()-getHeight()/2-py);   
               }

            }            
    }*/

    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        
        
        if (element!=null)
        {
            if (element.owner.owner.vmProtected) 
            {
                if (element.owner.owner.getCircuitBasis()==element.owner)
                {
                    return;
                }
            }        
        }
        
        
        Graphics2D g2 = (Graphics2D)g;
        
        int mitteX=getWidth()/2;
        int mitteY=getHeight()/2;
        
        
        if (isHighlighted())
        {
            Color color =VSDataType.getCoorFromDataType(this.dataType);
            setBackground(color);
        } else
        {
            setBackground(nonSelectedColor);
        }        
        g.setColor(getBackground());
        g.fillRect(0,0,getWidth(),getHeight());            
        
        if (isHighlighted())        
        {
            g.setColor(Color.BLACK);
            g.drawRect(0,0,getWidth()-1,getHeight()-1);
        }
        
        //g.setColor(Color.RED);
        //g.drawRect(0,0,getWidth()-1,getHeight()-1);
        
        int dis=1;
        if (!visible) return;
        
        //description=""+value;
        if (pinAlign==1 || pinAlign==3) // Horizontal
        {

           VSDataType.setColorStrokeFromDataType(g2,this.dataType);
           g.drawLine(1,mitteY, getWidth()-1,mitteY);
           //g.setColor(Color.black);
        } else
        {                
            VSDataType.setColorStrokeFromDataType(g2,this.dataType);
            g.drawLine(mitteX, 1, mitteX, getHeight()-1);
            //g.setColor(Color.black);                
        }

           g2.setStroke(strockeStandard);
           JPin bb=this;

           int x,y;
           if (bb.pinIO==JPin.PIN_INPUT )
           {
               int d=2;
               if (bb.pinAlign==0)  // TOP PIN
               {                   
                   x=getWidth()/2;
                   y=getHeight()-d;
                   Polygon p = new Polygon();
                   p.addPoint(x,y);
                   p.addPoint(x-d,y-d);
                   p.addPoint(x+d,y-d);

                   //g2.setColor(Color.BLACK);
                   g2.drawPolygon(p);
                   g2.fillPolygon(p);

               } else
               if (bb.pinAlign==1) // RIGHT PIN
               {                
                   x=d;
                   y=getHeight()/2;
                   Polygon p = new Polygon();
                   p.addPoint(x,y);
                   p.addPoint(x+d,y-d);
                   p.addPoint(x+d,y+d);

                   //g2.setColor(Color.BLACK);
                   g2.drawPolygon(p);
                   g2.fillPolygon(p);
               }else
               if (bb.pinAlign==2) // BOTTOM PIN
               {
                   x=getWidth()/2;
                   y=d;
                   Polygon p = new Polygon();
                   p.addPoint(x,y);
                   p.addPoint(x+d,y+d);
                   p.addPoint(x-d,y+d);

                   //g2.setColor(Color.BLACK);
                   g2.drawPolygon(p);
                   g2.fillPolygon(p);
               }else
               if (bb.pinAlign==3 ) // LEFT PIN
               {
                   x=getWidth()-d;
                   y=getHeight()/2;
                   Polygon p = new Polygon();
                   p.addPoint(x,y);
                   p.addPoint(x-d,y-d);
                   p.addPoint(x-d,y+d);
                   //g2.setColor(Color.BLACK);
                   g2.drawPolygon(p);
                   g2.fillPolygon(p);
               }
           }
          

    }


}

