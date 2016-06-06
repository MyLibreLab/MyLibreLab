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
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


public class StatusEditPathEditMode implements StatusBasisIF
{
    public VMObject vmobject;
    private ArrayList drahtPoints = new ArrayList();
    private static final int HOZ=0;
    private static final int VERT=1;
    private int aktuellesPinType=HOZ;
    public StatusBasisIF status=null;
    public Element element;
    private JPopupMenu popupmenu = new JPopupMenu();
    private JPopupMenu popupBeginEndNode = new JPopupMenu();
    private JMenuItem  jmiDeleteNode, jmiSynchAsynch;
    private JMenuItem  jmiAddLine;
    private JMenuItem  jmiAddCurve;
    
    public boolean addPoint=false;
    private int posX,posY;

        
    public int aktuellerPunkt=-1;
    
    public int selectedParam=-1; // 0 == Punkt, 1=P1, 2=P2;
    
    private int oldX,oldY,oldWidth,oldHeight;          
    
    public StatusEditPathEditMode(VMObject vmobject, Element element)
    {
        this.vmobject=vmobject;
        
        this.element=element;
        
        this.element.setSelected(false);

        oldX=element.getX();
        oldY=element.getY();
        oldWidth=element.getWidth();
        oldHeight=element.getHeight();
              
        int ox=(int)(oldX/element.zoomX);
        int oy=(int)(oldY/element.zoomY);
        
        for (int i=0;i<element.points.size();i++)
        {
            PathPoint path=element.points.get(i);
            Point p=path.p;
            p.x+=ox;
            p.y+=oy;
            
            if (path.p1!=null)
            {
                p=path.p1;
                p.x+=ox;
                p.y+=oy;
            }

            if (path.p2!=null)
            {
                p=path.p2;
                p.x+=ox;
                p.y+=oy;
            }            
        }
        
        element.setLocation(0,0);
        element.setSize(9999,9999);
        element.updateUI();
        
        
        jmiAddLine=new JMenuItem("Add Line");
        jmiAddCurve=new JMenuItem("Add Curve");        
        jmiDeleteNode=new JMenuItem("Delete Point");                
        jmiSynchAsynch=new JMenuItem("Synchrone/asynchrone");
        
        popupmenu.add(jmiAddLine);
        popupmenu.add(jmiAddCurve);        
        popupmenu.add(jmiDeleteNode);
        
        popupmenu.add(jmiSynchAsynch);
        
        //popupBeginEndNode.add(jmiOpenClosePath);
        

        createEvent();
    }
    
    public void createEvent()
    {
        
        jmiAddLine.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {                
                if (aktuellerPunkt>-1)
                {                    
                    if (element.points.size()>1 && aktuellerPunkt<element.points.size()-1)
                    {
                        Point p1=element.points.get(aktuellerPunkt).p;
                        Point p2=element.points.get(aktuellerPunkt+1).p;
                    
                        int x=(p1.x+p2.x)/2;
                        int y=(p1.y+p2.y)/2;
                        
                        
                        PathPoint newPath=new PathPoint();                        
                        newPath.commando="LINETO";
                        newPath.p=new Point(x,y);
                        
                        element.points.add(aktuellerPunkt+1,newPath);
                        element.updateUI();
                    }
                }
            }
        });

        jmiAddCurve.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {                
                if (aktuellerPunkt>-1)
                {                    
                    if (element.points.size()>1 && aktuellerPunkt<element.points.size()-1)
                    {
                        Point p1=element.points.get(aktuellerPunkt).p;
                        Point p2=element.points.get(aktuellerPunkt+1).p;
                    
                        int x=(p1.x+p2.x)/2;
                        int y=(p1.y+p2.y)/2;
                        
                        
                        PathPoint newPath=new PathPoint();                        
                        newPath.commando="CURVETO";
                        newPath.p=new Point(x,y);
                        newPath.p1=new Point(x-10,y-10);
                        newPath.p2=new Point(x-20,y-10);
                        
                        element.points.add(aktuellerPunkt+1,newPath);
                        element.updateUI();
                    }
                }
            }
        });
        
               
        
        jmiDeleteNode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {                
                if (aktuellerPunkt>-1)
                {                    
                    element.points.remove(aktuellerPunkt);
                    element.updateUI();
                }
            }
        });
        
        jmiSynchAsynch.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {                
                if (aktuellerPunkt>-1)
                {   
                    element.points.get(aktuellerPunkt).synchron=!element.points.get(aktuellerPunkt).synchron;
                    element.updateUI(); 
                }
            }
        });
                
        
    }
    
    
    public void elementPinMouseMoved(MouseEvent e, int elementID,int pin)
    {        
    }
    
    public void mouseDragged(MouseEvent e)
    {
        if (status!=null) 
        {
            status.mouseDragged(e);
            return;
        }
        
        int x=(int)(e.getX()/element.zoomX);
        int y=(int)(e.getY()/element.zoomY);
                
        
        if (aktuellerPunkt>-1)
        {
            PathPoint path=element.points.get(aktuellerPunkt);
            
            if (path.commando.equalsIgnoreCase("MOVETO"))
            {
              path.p.x=x;
              path.p.y=y;
              
              PathPoint path_1=element.points.get(1); 
              path_1.p.x=x;
              path_1.p.y=y;           
              
              element.updateUI();
              
              
              //return;
            }
            
            if (path.commando.equalsIgnoreCase("CURVETO"))
            {
                if (!path.synchron)
                {
                    if (selectedParam==0)
                    {                                       
                        int x1=path.p.x-path.p1.x;
                        int y1=path.p.y-path.p1.y;
                        int x2=path.p.x-path.p2.x;
                        int y2=path.p.y-path.p2.y;

                        path.p.x=x;
                        path.p.y=y;

                        path.p1.x=path.p.x-x1;
                        path.p1.y=path.p.y-y1;

                        path.p2.x=path.p.x-x2;
                        path.p2.y=path.p.y-y2;
                    }else
                    {
                        Point p=new Point(0,0);
                        if (selectedParam==0) p=path.p;else
                        if (selectedParam==1) p=path.p1;else
                        if (selectedParam==2) p=path.p2;
                        p.x=x;
                        p.y=y;                              
                    }
                }
                if (path.synchron)
                {
                    if (selectedParam==0)
                    {                                       
                        int x1=path.p.x-path.p1.x;
                        int y1=path.p.y-path.p1.y;
                        int x2=path.p.x-path.p2.x;
                        int y2=path.p.y-path.p2.y;

                        path.p.x=x;
                        path.p.y=y;

                        path.p1.x=path.p.x-x1;
                        path.p1.y=path.p.y-y1;

                        path.p2.x=path.p.x-x2;
                        path.p2.y=path.p.y-y2;
                                                
                        
                        if (aktuellerPunkt==1)
                        {
                          PathPoint path_1=element.points.get(0);  
                          path_1.p.x=x;
                          path_1.p.y=y;
                        }

                    }else            
                    if (selectedParam==1)
                    {

                        {
                         int dx=x-path.p.x;
                         int dy=y-path.p.y;
                         path.p2.x=path.p.x-dx;
                         path.p2.y=path.p.y-dy;                            

                         path.p1.x=x;
                         path.p1.y=y;                
                        }
                    }
                    if (selectedParam==2)
                    {
                        if (path.commando.equalsIgnoreCase("CURVETO") && path.synchron)
                        {
                         int dx=x-path.p.x;
                         int dy=y-path.p.y;
                         path.p1.x=path.p.x-dx;
                         path.p1.y=path.p.y-dy;                            

                         path.p2.x=x;
                         path.p2.y=y;                
                        }
                    }
                }
            }else
            {
                Point p=new Point(0,0);
                if (selectedParam==0) p=path.p;else
                if (selectedParam==1) p=path.p1;else
                if (selectedParam==2) p=path.p2;
                p.x=x;
                p.y=y;                            
            }

            element.updateUI();
        }
    }
    
    public void mouseMoved(MouseEvent e)
    {
        if (status!=null) 
        {
            status.mouseMoved(e);
            return;
        }
    }
    
    
    private int getPoint(int x, int y)
    {
        
        int d=element.pointSize;
        double zx=element.zoomX;
        double zy=element.zoomY;
        
        /*x=(int)(xzx);
        y=(int)(y*zy);*/
        
        for (int i=0;i<element.points.size();i++)
        {
            aktuellerPunkt=-1;
            PathPoint path=element.points.get(i);
            
            Point p=path.p;
            String cmd=path.commando;
            
            if (cmd.equalsIgnoreCase("CURVETO"))
            {
                if (x>=(int)(path.p1.x*zx)-d && x<=(int)(path.p1.x*zx)+d && y>=(int)(path.p1.y*zy)-d && y<=(int)(path.p1.y*zy)+d)
                {
                  selectedParam=1;  
                  return i;
                }
                if (x>=(int)(path.p2.x*zx)-d && x<=(int)(path.p2.x*zx)+d && y>=(int)(path.p2.y*zy)-d && y<=(int)(path.p2.y*zy)+d)
                {
                  selectedParam=2;  
                  return i;
                }
                if (x>=(int)(p.x*zx)-d && x<=(int)(p.x*zx)+d && y>=(int)(p.y*zy)-d && y<=(int)(p.y*zy)+d)
                //if (x>=p.x-d && x<=p.x+d && y>=p.y-d && y<=p.y+d)
                {   
                    selectedParam=0;
                    return i;
                }
                
                //path.curveTo(x,y, p.p1.x,p.p1.y, p.p2.x,p.p2.y);
            }else
            if (cmd.equalsIgnoreCase("QUADTO"))
            {                
                //path.quadTo(x,y, p.p1.x,p.p1.y);
                if (x>=path.p1.x-d && x<=path.p1.x+d && y>=path.p1.y-d && y<=path.p1.y+d)
                {
                    selectedParam=1;
                    return i;
                }            
                if (x>=p.x-d && x<=p.x+d && y>=p.y-d && y<=p.y+d)
                {   
                    selectedParam=0;
                    return i;
                }
                
            }else
            
            if (x>=(int)(p.x*zx)-d && x<=(int)(p.x*zx)+d && y>=(int)(p.y*zy)-d && y<=(int)(p.y*zy)+d)
            //if (x>=p.x-d && x<=p.x+d && y>=p.y-d && y<=p.y+d)
            {   
                selectedParam=0;
                return i;
            }
        }
        return -1;
    }
    
    public void mousePressed(MouseEvent e)
    {
        if (status!=null) 
        {
            status.mousePressed(e);
            return;
        }
        int x=e.getX();
        int y=e.getY();
        

        aktuellerPunkt=getPoint(x,y);        
        
        if (aktuellerPunkt>-1)
        {
            if (e.getButton()==e.BUTTON3 && selectedParam==0)
            {                                
                popupmenu.show(element.owner,e.getX(),e.getY());
                return;
            } else return;
        }                
        if (e.getButton()==e.BUTTON3)
        {
            GeneralPath pathX=element.jParsePath();
            Rectangle rec=pathX.getBounds();
            
            element.setLocation((int)rec.getX(),(int)rec.getY());
            element.setSize((int)rec.getWidth(),(int)rec.getHeight());

            rec.x=(int)(rec.x/element.zoomX);
            rec.y=(int)(rec.y/element.zoomY);
            
            for (int i=0;i<element.points.size();i++)
            {
                PathPoint path=element.points.get(i);
            
                Point p=path.p;                            
                p.x-=(int)rec.getX();
                p.y-=(int)rec.getY();
                
                if (path.p1!=null)
                {
                    p=path.p1;
                    p.x-=(int)rec.getX();
                    p.y-=(int)rec.getY();
                }

                if (path.p2!=null)
                {
                    p=path.p2;
                    p.x-=(int)rec.getX();
                    p.y-=(int)rec.getY();
                }
            }
            
            element.setSelected(true);
            
            vmobject.setModusIdle();
            vmobject.processPropertyEditor();
            vmobject.updateUI();
        }
        
    }
    
    private Rectangle getBounds()
    {
        int minX=99999;
        int minY=99999;
        int maxX=-99999;
        int maxY=-99999;
        
        for (int i=0;i<element.points.size();i++)
        {
          PathPoint path=element.points.get(i);
          
          if (path.p.x<minX) minX=path.p.x;
          if (path.p.y<minY) minY=path.p.y;
          
          if (path.p.x>maxX) maxX=path.p.x;
          if (path.p.y>maxY) maxY=path.p.y;
          
          
          if (path.p1!=null)
          {
              
              if (path.p1.x+path.p.x<minX) minX=path.p1.x+path.p.x;
              if (path.p1.y+path.p.y<minY) minY=path.p1.y+path.p.y;
              
              if (path.p1.x+path.p.x>maxX) maxX=path.p1.x+path.p.x;
              if (path.p1.y+path.p.y>maxY) maxY=path.p1.y+path.p.y;
          }

          if (path.p2!=null)
          {
              if (path.p2.x+path.p.x<minX) minX=path.p2.x+path.p.x;
              if (path.p2.y+path.p.y<minY) minY=path.p2.y+path.p.y;
              
              if (path.p2.x+path.p.x>maxX) maxX=path.p2.x+path.p.x;
              if (path.p2.y+path.p.y>maxY) maxY=path.p2.y+path.p.y;
          }
        }
        
        return new Rectangle(minX,minY,maxX,maxY);
                        
    }
    

    public void mouseReleased(MouseEvent e)
    {
       if (status!=null) 
       {
           status.mouseReleased(e);
           return;
       }
    }
    
    
    
    public void draw(Graphics g)
    {
        if (status!=null) 
        {
            status.draw(g);
            return;
        }
    }
    
    
    public void elementPinMouseReleased(MouseEvent e, int elementID,int pin)
    {}
    public void elementPinMousePressed(MouseEvent e, int elementID,int pin)
    {
    }
    public void mouseDblClick(MouseEvent e)
    {}
    public void processKeyEvent(KeyEvent ke)
    {
        //System.out.println("KEY");
        //element.points.add(new Point(100,100));
    }
    
    public void mouseClicked(MouseEvent e)
    {}
    public void mouseEntered(MouseEvent e)
    {}
    public void mouseExited(MouseEvent e)
    {}
    
    Element lastOverElement=null;
    
    public void elementMouseEntered(MouseEvent e)
    {
        
    }
    
    public void elementMouseExited(MouseEvent e)
    {
        
    }
    
    
}

