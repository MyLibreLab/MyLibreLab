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
import VisualLogic.Element;
import java.awt.event.*;
import java.awt.*;


public class StatusRun extends Object implements StatusBasisIF
{
    private VMObject basis;
    
    public StatusRun(VMObject basis) 
    {
       this.basis=basis; 
    }
    
    public void start()
    {                                
        for (int i=0;i<basis.getElementCount();i++)
        {
            Element element=basis.getElement(i);
                    
            element.setVisible(element.isVisibleWhenRun());
            element.layeredPane.start();
            
            if (element.classRef!=null)
            {
                try
                {
                  element.classRef.xonStart();                      
                } catch(Exception ex)  
                {
                    //basis.owner.stop();                    
                    String err="Error in Method xonStart() : \""+ex+"\" in Element : \""+element.getInternName()+"\". Application interrupted!";
                    basis.owner.showErrorMessage(err);
                    
                    if (basis.owner.frameCircuit!=null)
                    {
                        basis.owner.frameCircuit.setVisible(true);
                    }                    
                    
                }
                element.oldBorderVisibility=element.borderVisibility;
                element.setBorderVisibility(false);
            }
        }
        
        //basis.processAllElements();
        
    }
    
    
    public void stop()
    {
        for (int i=0;i<basis.getElementCount();i++)
        {
            Element element=basis.getElement(i);

            element.layeredPane.stop();
            element.setVisible(true);
            
            if (element.classRef!=null)
            {                                
                try
                {
                  element.classRef.xonStop();
                  element.mouseEventsTo=null;
                } catch(Exception ex)  
                {
                    basis.owner.stop();
                    basis.owner.showErrorMessage("Error in Method xonStop() :\n"+ex+"\n im Element : "+element.getName()+"\napplication interrupted abgebrochen!");
                }
                
                
                element.setBorderVisibility(element.oldBorderVisibility);
            }            
                        
        }    
        
        for (int i=0;i<basis.getDrahtCount();i++)
        {
            Draht draht = basis.getDraht(i);
            draht.setOff();                
        }
        basis.repaint();        
    }
    
    public void processKeyEvent(KeyEvent ke)
    {
        
    }
    
    public void mouseDblClick(MouseEvent e)
    {
    
    }
    
    
    public void mouseDragged(MouseEvent e)
    {
        if (e.getSource() instanceof SelectionPane)
        {
            SelectionPane pane = (SelectionPane)e.getSource();
            Element element=pane.getElement();
            
            try
            {               
                
                if (element.mouseEventsTo==null)
                {
                    element.classRef.xonMouseDragged(e);
                } else
                {
                    element.mouseEventsTo.xonMouseDragged(e);
                }
                
            } catch (Exception ex)
            {
                
            }
        } else
        {        
        }

    }
    
    public synchronized void mousePressed(MouseEvent e)
    {
        if (e.getSource() instanceof SelectionPane)
        {
            SelectionPane pane = (SelectionPane)e.getSource();
            Element element=pane.getElement();
            
            if (element.mouseEventsTo==null)
            {
                element.classRef.xonMousePressed(e);                                  
            } else
            {
                element.mouseEventsTo.xonMousePressed(e);
            }
        } else
        {        
        }                
    }
    
    public  void elementPinMousePressed(MouseEvent e, int elementID,int pin)
    {
    }    
    
    public  void elementPinMouseReleased(MouseEvent e, int elementID,int pin)
    {
        
    }
    public void elementPinMouseMoved(MouseEvent e, int elementID,int pin)
    {
        
    }
    
    
    public synchronized void mouseReleased(MouseEvent e)
    {        
        if (e.getSource() instanceof SelectionPane)
        {
            SelectionPane pane = (SelectionPane)e.getSource();
            Element element=pane.getElement();
            
            
            if (element.classRef!=null)  
            {
                if (element.mouseEventsTo==null)
                {
                    element.classRef.xonMouseReleased(e);
                } else
                {
                    element.mouseEventsTo.xonMouseReleased(e);
                }                                
            }
                                   
        } else
        {        
        }
        
    }

    public void mouseClicked(MouseEvent e)   
    {
        
    }
    
    public void mouseEntered(MouseEvent e)
    {
        
    }
    
    public void mouseExited(MouseEvent e)
    {
        
    }
    
    public void mouseMoved(MouseEvent e)
    {
        
        if (e.getSource() instanceof SelectionPane)
        {
            SelectionPane pane = (SelectionPane)e.getSource();
            Element element=pane.getElement();
            if (element.classRef!=null)  
            {
                if (element.mouseEventsTo==null)
                {
                    element.classRef.xonMouseMoved(e);
                } else
                {
                    element.mouseEventsTo.xonMouseMoved(e);
                }
            }
        } else
        {                    
            basis.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
               
    }
    public void draw(java.awt.Graphics g)
    {
        
    }
}
 
