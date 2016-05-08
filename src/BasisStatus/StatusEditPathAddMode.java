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
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


public class StatusEditPathAddMode implements StatusBasisIF
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
    private JMenuItem  jmiDeleteNode, jmiOpenClosePath;
    private JMenuItem  jmiAddLine;
    private JMenuItem  jmiAddCurve;
    private JMenuItem  jmiAddQuad;
    public boolean addPoint=false;
    private int posX,posY;

        
    public int aktuellerPunkt=-1;
    
    public int selectedParam=-1; // 0 == Punkt, 1=P1, 2=P2;
    
    private int oldX,oldY,oldWidth,oldHeight;          
    
    public StatusEditPathAddMode(VMObject vmobject, Element element)
    {
        this.vmobject=vmobject;
        
        this.element=element;
        
        this.element.setSelected(false);
        
        //oldX=(int)(element.getX()*element.zoomX);
        //oldY=(int)(element.getY()*element.zoomY);
        oldX=element.getX();
        oldY=element.getY();
        oldWidth=element.getWidth();
        oldHeight=element.getHeight();
               
        for (int i=0;i<element.points.size();i++)
        {
            PathPoint path=element.points.get(i);
            Point p=path.p;
            p.x+=oldX;
            p.y+=oldY;
            
            if (path.p1!=null)
            {
                p=path.p1;
                p.x+=oldX;
                p.y+=oldY;
            }

            if (path.p2!=null)
            {
                p=path.p2;
                p.x+=oldX;
                p.y+=oldY;
                //System.out.println("");
            }            
        }
        
        element.setLocation(0,0);
        element.setSize(9999,9999);
        element.updateUI();
        
        
        jmiAddLine=new JMenuItem("Add Line");
        jmiAddCurve=new JMenuItem("Add Curve");
        jmiAddQuad=new JMenuItem("Add Quad");
        jmiDeleteNode=new JMenuItem("Delete Point");                
        jmiOpenClosePath=new JMenuItem("open/close Path");
        
        popupmenu.add(jmiAddLine);
        popupmenu.add(jmiAddCurve);
        popupmenu.add(jmiAddQuad);
        popupmenu.add(jmiDeleteNode);
        
        popupmenu.add(jmiOpenClosePath);

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
        
        int x=e.getX();
        int y=e.getY();
                
        if (addPoint)
        {
            
            PathPoint newPath=new PathPoint();
            newPath.commando="CURVETO";

            //newPath.synchron=false;
            newPath.p=new Point(posX, posY);

            newPath.p1=new Point(x,y);
            newPath.p2=new Point(x,y);
            element.points.add(newPath);
            element.updateUI();

            status=new StatusEditPath_ADD_CurveTo(this,vmobject,element,element.points.size()-1);
            return;

            
        }
        
        if (aktuellerPunkt>-1)
        {
            PathPoint path=element.points.get(aktuellerPunkt);
        
            Point p=new Point(0,0);
            if (selectedParam==0) p=path.p;else
            if (selectedParam==1) p=path.p1;else
            if (selectedParam==2) p=path.p2;
            
            p.x=x;
            p.y=y;
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
        for (int i=0;i<element.points.size();i++)
        {
            aktuellerPunkt=-1;
            PathPoint path=element.points.get(i);
            
            Point p=path.p;
            String cmd=path.commando;
            
            if (cmd.equalsIgnoreCase("CURVETO"))
            {
                if (x>=path.p1.x-d && x<=path.p1.x+d && y>=path.p1.y-d && y<=path.p1.y+d)
                {
                  selectedParam=1;  
                  return i;
                }
                if (x>=path.p2.x-d && x<=path.p2.x+d && y>=path.p2.y-d && y<=path.p2.y+d)
                {
                  selectedParam=2;  
                  return i;
                }
                if (x>=p.x-d && x<=p.x+d && y>=p.y-d && y<=p.y+d)
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
            
            if (x>=p.x-d && x<=p.x+d && y>=p.y-d && y<=p.y+d)
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

        

        if (e.getButton()==e.BUTTON3)
        {
            GeneralPath pathX=element.jParsePath();
            Rectangle2D rec=pathX.getBounds2D();
            
            element.setLocation((int)rec.getX(),(int)rec.getY());
            element.setSize((int)rec.getWidth(),(int)rec.getHeight());
            
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
        }else
        {
            addPoint=true;
            posX=x;
            posY=y;            
        }        
        
    }
    
    public void mouseReleased(MouseEvent e)
    {
       if (status!=null) 
       {
           status.mouseReleased(e);
           return;
       }
        int x=e.getX();
        int y=e.getY();
        if (e.getButton()!=e.BUTTON3)
        {
            PathPoint newPath=new PathPoint();
            newPath.commando="LINETO";

            newPath.p=new Point(x,y);

            element.points.add(newPath); 
            element.updateUI();
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

