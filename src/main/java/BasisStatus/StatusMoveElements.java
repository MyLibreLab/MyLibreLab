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

public class StatusMoveElements extends Object implements StatusBasisIF
{
    private VMObject vmobject;    
    private int oldX,oldY;    
    private int x=0;
    private int y=0;
    
    
    public StatusMoveElements(VMObject vmobject,MouseEvent e)
    {
        this.vmobject=vmobject;
        mousePressed(e);
        mouseDragged(e);
    }
    
    public void processKeyEvent(KeyEvent ke){}    
    public  void elementPinMousePressed(MouseEvent e, int elementID,int pin)
    {
      
    }        
    public  void elementPinMouseReleased(MouseEvent e, int elementID,int pin)
    {
      mouseReleased(e);
    }
    public void elementPinMouseMoved(MouseEvent e, int elementID,int pin){}        
    public void mouseDblClick(MouseEvent e){}

    
    public void mouseDragged(MouseEvent e)
    {
        if (e.getSource() instanceof SelectionPane)
        {
            SelectionPane pane = (SelectionPane)e.getSource();
            Element el=pane.getElement();
            int eX=0;
            int eY=0;
            
            if (vmobject!=null)
            {
                try
                {
                    eX=vmobject.getMousePosition().x;
                    eY=vmobject.getMousePosition().y;                    
                } catch(Exception ex)
                {                    
                    return;
                }
            }
                    

            int x=eX;
            int y=eY;
            
                        
            // Move All Selected Elements
            for (int i=0;i<vmobject.getElementCount();i++) 
            {
                el = vmobject.getElement(i);
                if (el.isSelected()==true)
                {
                    int nX,nY;
                    
                    nX=x+el.oldX-oldX;
                    nY=y+el.oldY-oldY;

                    Point pb=vmobject.pointToRaster(el,nX,nY);
                    nX=pb.x+el.oldDX;
                    nY=pb.y+el.oldDY;                                        
                    
                   
                    if (!el.getInternName().equalsIgnoreCase("###NODE###"))
                    {
                      el.setLocation(nX, nY);    
                    }
                                        
                    if (vmobject==vmobject.owner.getCircuitBasis())
                    {                                            
                      boolean onlyHoz=false;
                      boolean onlyVert=false;
                      boolean oki=false;
                      if (el.getInternName().equalsIgnoreCase("###NODE###"))
                      {
                          vmobject.linksOK=false;
                          vmobject.rechtsOK=false;
                          vmobject.isNodeVerschiebbarL(el);
                          vmobject.isNodeVerschiebbarR(el);
                          if (vmobject.linksOK && vmobject.rechtsOK)
                          {                            
                            
                            el.setLocation(el.getLocation().x, nY);
                            vmobject.orderNodesL(el,el.getLocation().y);
                            vmobject.orderNodesR(el,el.getLocation().y);                            
                            //vmobject.reorderWireFrames();
                            //oki=true;
                          }else
                          {
                              // also nicht in Vertikaler Richtung verschiebbar!
                              //el.setLocation(nX, el.getLocation().y);         
                              
                              onlyHoz=true;
                          }
                              
                          
                          vmobject.topOK=false;
                          vmobject.bottomOK=false;
                          vmobject.isNodeVerschiebbarT(el);
                          vmobject.isNodeVerschiebbarB(el);
                          if (vmobject.topOK && vmobject.bottomOK)
                          {                            
                            el.setLocation(nX, el.getLocation().y);
                            
                            vmobject.orderNodesT(el,el.getLocation().x);
                            vmobject.orderNodesB(el,el.getLocation().x);
                            //vmobject.reorderWireFrames();
                            //oki=true;
                          }else
                          {
                              onlyVert=true;
                          }
                          
                          //if (oki==false)
                          {
                              if (onlyHoz && el.getPin(0).draht==null && el.getPin(2).draht==null )
                              {
                                  el.setLocation(nX, el.getLocation().y);                                  
                              }
                              if (onlyHoz && el.getPin(0).draht==null && el.getPin(2).draht!=null )
                              {
                                  el.setLocation(nX, el.getLocation().y);                                                          
                              }
                              if (onlyHoz && el.getPin(0).draht!=null && el.getPin(2).draht==null )
                              {
                                  el.setLocation(nX, el.getLocation().y);                                                          
                              }                              
                              if (!onlyHoz && el.getPin(0).draht==null && el.getPin(2).draht==null )
                              {
                                  el.setLocation(nX, el.getLocation().y);                                                          
                              }
                              
                              if (onlyVert && el.getPin(1).draht==null && el.getPin(3).draht==null )
                              {
                                  el.setLocation(el.getLocation().x,nY);                                  
                              }
                              if (onlyVert && el.getPin(1).draht==null && el.getPin(3).draht!=null )
                              {
                                  el.setLocation(el.getLocation().x,nY);                                  
                              }                              
                              if (onlyVert && el.getPin(1).draht!=null && el.getPin(3).draht==null )
                              {
                                  el.setLocation(el.getLocation().x,nY);                                  
                              }                              
                              if (!onlyVert && el.getPin(1).draht==null && el.getPin(3).draht==null )
                              {
                                  el.setLocation(el.getLocation().x,nY);                                  
                              }
                              
                          }
                          
                      }else
                      {
                          el.setLocation(nX, nY);
                          for (int j=0;j<el.getPinCount();j++)
                          {
                              JPin pin=el.getPin(j);
                              
                              if (pin.draht!=null && pin.draht.getPolySize()==2)
                              {
                                  Element node=null;
                                  if (pin.pinIO==JPin.PIN_INPUT)
                                  {                                      
                                    node=vmobject.getElementWithID(pin.draht.getSourceElementID());
                                  }                                  
                                  if (pin.pinIO==JPin.PIN_OUTPUT)
                                  {
                                    node=vmobject.getElementWithID(pin.draht.getDestElementID());
                                  }                                  

                                  if (node!=null && node.getInternName().equalsIgnoreCase("###NODE###"))
                                  {
                                      if (pin.getPinAlign()==1 || pin.getPinAlign()==3)
                                      {
                                        int yy=el.getLocation().y+pin.getLocation().y-pin.getHeight()/2-3;
                                        vmobject.orderNodesL(node,yy);
                                        vmobject.orderNodesR(node,yy);
                                      }else
                                      {                                      
                                        int xx=el.getLocation().x+pin.getLocation().x-pin.getWidth()/2-3;
                                        vmobject.orderNodesT(node,xx);
                                        vmobject.orderNodesB(node,xx);
                                      }
                                      //vmobject.reorderWireFrames();
                                  }                                      
                                  
                              }
                              
                          }
                      }
                      
                    }
                }
            }

            if (vmobject==vmobject.owner.getCircuitBasis())
            {
                // Move All Selected Draehte
                Draht draht;
                for (int i=0;i<vmobject.getDrahtCount();i++) 
                {
                    draht = vmobject.getDraht(i);

                    for (int j=0;j<draht.getPolySize();j++)
                    {

                        PolyPoint p = draht.getPoint(j);
                        if (p.isSelected()==true)
                        {
                            int nX,nY;

                            nX=x+p.oldX-oldX;
                            nY=y+p.oldY-oldY;                    
                            draht.setPoint(j,nX,nY);
                        }
                    }
                }
                vmobject.reorderWireFrames();

            }
        }
    }
    
    public void mousePressed(MouseEvent e)
    {       
        /*x = e.getX(); 
        y = e.getY();         */

        if (e.getSource() instanceof SelectionPane)
        {
            SelectionPane pane = (SelectionPane)e.getSource();
            Element el=pane.getElement();

            int eX=0;
            int eY=0;
            if (vmobject!=null)
            {
                try
                {
                    eX=vmobject.getMousePosition().x;
                    eY=vmobject.getMousePosition().y;
                } catch(Exception ex)
                {
                    
                }
            }

            int x=eX;
            int y=eY;
            
            int elX=0;
            int elY=0;

            oldX=x;
            oldY=y;


            for (int i=0;i<vmobject.getElementCount();i++) 
            {
                el = vmobject.getElement(i);
                if (el.isSelected()==true)
                {
                    el.oldX=el.getX();
                    el.oldY=el.getY();     
                    
                    el.setSimplePaintModus(true);
                    Point p=vmobject.pointToRaster(el,el.oldX,el.oldY);
                    
                    el.oldDX=el.oldX-p.x;
                    el.oldDY=el.oldY-p.y;
                }
            }


            Draht draht;
            for (int i=0;i<vmobject.getDrahtCount();i++) 
            {
                draht = vmobject.getDraht(i);

                for (int j=0;j<draht.getPolySize();j++)
                {
                    PolyPoint p = draht.getPoint(j);
                    if (p.isSelected()==true)
                    {                                  
                        p.oldX=p.getX();
                        p.oldY=p.getY();
                    }
                }
            }
        }    
    }
    
    public void mouseReleased(MouseEvent e)
    {   
        if (vmobject==vmobject.owner.getCircuitBasis())
        {
          vmobject.reorderWireFrames();
        }        
        
        
        boolean firstTime=true;
        
        Element el;
        for (int i=0;i<vmobject.getElementCount();i++) 
        {
            el = vmobject.getElement(i);
            if (el.isSelected()==true)
            {   
                el.setSimplePaintModus(false);
                if (firstTime)
                {
                  firstTime=false;                  
                  el.processPropertyEditor();
                }
            }
        }
       
        vmobject.owner.saveForUndoRedo();
        vmobject.setModusIdle();
        
    }

    public void mouseClicked(MouseEvent e){}    
    public void mouseEntered(MouseEvent e){}    
    public void mouseExited(MouseEvent e){}    
    public void mouseMoved(MouseEvent e){}
    public void draw(Graphics g){}
}
 
