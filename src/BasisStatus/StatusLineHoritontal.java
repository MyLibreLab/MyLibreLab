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

import VisualLogic.Draht;
import VisualLogic.Element;
import VisualLogic.ExternalIF;
import VisualLogic.JPin;
import VisualLogic.Line;
import VisualLogic.Tools;
import VisualLogic.VMObject;
import VisualLogic.VSDataType;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 *
 * @author Carmelo
 */
public class StatusLineHoritontal extends Object implements StatusBasisIF
{
    private ArrayList drahtPoints;
    private VMObject vmobject;
    private Point startPoint = new Point(0,0);
    private Cursor CheckCursor;
    private Cursor CrossCursor;
    private JPin pan=null;
    private boolean hasLastPoint=false;
    private final int NOPE=0;
    private final int pinSelection=1;
    private int modus=NOPE;
    private int sourceDataType=-1;
    private int sourcePin;
    private int sourceElementID;
    
    
    /** Creates a new instance of StatusHoritontalLine */
    public StatusLineHoritontal(VMObject vmobject,ArrayList drahtPoints, int sourceElementID,int sourcePin,Point start)
    {
        this.vmobject=vmobject;
        this.drahtPoints=drahtPoints;
        
        this.sourcePin=sourcePin;
        
        this.sourceElementID=sourceElementID;
        //createCursors();
        vmobject.disableAllElements();
        
        Element sourceElement = vmobject.getElementWithID(sourceElementID);
        JPin pin = sourceElement.getPin(sourcePin);
        
        createCursors();
        addDrahtPoint(start.x, start.y);
        addStueck(start, start);
        sourceDataType=pin.dataType;
    }
    
    
    private void copyPoints(ArrayList source, ArrayList dest)
    {
        dest.clear();
        dest.addAll(source);
    }
    private void drawPoints(java.awt.Graphics g)
    {
        if (g!=null)
        {
            int xx[]=new int [drahtPoints.size()];
            int yy[]=new int [drahtPoints.size()];

            for (int i=0;i<drahtPoints.size();i++)
            {
                Point p=getDrahtPoint(i);
                xx[i]=p.x;
                yy[i]=p.y;
            }

            Element sourceElement= (Element)vmobject.getObjectWithID(sourceElementID);
            JPin pinA=sourceElement.getPin(sourcePin);

            VSDataType.setColorStrokeFromDataType((Graphics2D)g,pinA.dataType);
            //g.setColor(Color.BLACK);
            g.drawPolyline(xx,yy, drahtPoints.size());

            if (vmobject!=null && Tools.settings!=null)
            {
              if (Tools.settings.isCircuitCrossVisible())
              {
                  Point p =vmobject.getMousePosition();
                  if (p!=null)
                  {

                    Graphics2D g2 = (Graphics2D)g;
                    g2.setColor(new Color(10,10,10,60));
                    g2.setStroke(new BasicStroke(1));

                    g.drawLine(p.x, 0, p.x,  vmobject.getHeight());
                    g.drawLine(0, p.y, vmobject.getWidth(), p.y);
                  }
              }
            }
        }
               
    }
    private void createCursors()
    {
        Image img=null;
        
        img = vmobject.getToolkit().createImage(getClass().getResource("/Bilder/Check.gif"));
        CheckCursor = vmobject.getToolkit().createCustomCursor(img, new Point(4,4), "CheckOK");
        
        img = vmobject.getToolkit().createImage(getClass().getResource("/Bilder/Cross.gif"));
        CrossCursor = vmobject.getToolkit().createCustomCursor(img, new Point(4,4), "KreuzX");
    }
    
    
    private void addDrahtPoint(int x, int y)
    {
        drahtPoints.add(new Point(x,y));
    }
    
    private void addStueck(Point start, Point ende)
    {
        addDrahtPoint(ende.x, start.y);
        addDrahtPoint(ende.x, ende.y);
    }
    
    
    
    public void mouseDragged(MouseEvent e)
    {
    }
    
    public void abschliessen(int destElementID,int destPin)
    {
        Draht draht = vmobject.addDrahtIntoCanvas(sourceElementID,sourcePin,destElementID,destPin);
        
        Element sourceElement= (Element)vmobject.getObjectWithID(sourceElementID);
        JPin pinA=sourceElement.getPin(sourcePin);
        
        Element destElement= (Element)vmobject.getObjectWithID(destElementID);
        JPin pinB=destElement.getPin(destPin);
        
        pinA.draht=draht;
        pinB.draht=draht;
        
        if (pinA.pinIO==JPin.PIN_INPUT_OUTPUT) pinA.pinIO=JPin.PIN_OUTPUT;
        if (pinB.pinIO==JPin.PIN_INPUT_OUTPUT) pinB.pinIO=JPin.PIN_INPUT;
        
        /*if (sourceElement.getInternName().equalsIgnoreCase("###NODE###"))
        {
            pinA.pinIO=JPin.PIN_OUTPUT;
        }
         
        if (destElement.getInternName().equalsIgnoreCase("###NODE###"))
        {
            pinB.pinIO=JPin.PIN_INPUT;
        }*/
        
        
        int x;
        int y;
        for (int i=0;i<drahtPoints.size();i++)
        {
            x = ((Point)drahtPoints.get(i)).x;
            y = ((Point)drahtPoints.get(i)).y;
            draht.addPoint(x,y);
        }
        
        vmobject.setCursor(Cursor.getDefaultCursor());
        vmobject.setModusIdle();
        vmobject.reorderWireFrames();
        vmobject.owner.saveForUndoRedo();
    }
    
    private void myOut(Polygon poly)
    {
        int[] xvalues=poly.xpoints;
        int[] yvalues=poly.ypoints;
        
        System.out.println("Size="+poly.npoints);
        for (int i=0;i<poly.npoints;i++)
        {
            System.out.println("("+xvalues[i]+","+yvalues[i]+")");
        }
    }
    
    
    public void make(int destElementID,int destPin)
    {
        Draht draht = vmobject.addDrahtIntoCanvas(sourceElementID,sourcePin,destElementID,destPin);
        
        Element sourceElement= (Element)vmobject.getObjectWithID(sourceElementID);
        JPin pinA=sourceElement.getPin(sourcePin);
        
        Element destElement= (Element)vmobject.getObjectWithID(destElementID);
        JPin pinB=destElement.getPin(destPin);
        
        pinA.draht=draht;
        pinB.draht=draht;
        
        int x;
        int y;
        for (int i=0;i<drahtPoints.size();i++)
        {
            x = ((Point)drahtPoints.get(i)).x;
            y = ((Point)drahtPoints.get(i)).y;
            draht.addPoint(x,y);
            
        }
        vmobject.setCursor(Cursor.getDefaultCursor());
        vmobject.setModusIdle();
        vmobject.reorderWireFrames();
        vmobject.owner.saveForUndoRedo();
        
    }
    
    
    public Polygon getDrahtPoints()
    {
        Polygon result=new Polygon();
        int x;
        int y;
        for (int i=0;i<drahtPoints.size();i++)
        {
            x = ((Point)drahtPoints.get(i)).x;
            y = ((Point)drahtPoints.get(i)).y;
            result.addPoint(x,y);
        }
        
        return result;
    }
    
    
    public void mousePressed(MouseEvent e)
    {
     if(e!=null)
     {
         
        try{    
        Point p = vmobject.getMousePosition();
        
        /*Line line=vmobject.getLineInNaehe(p);
        if (line!=null)
        {
            Polygon aktuellesPoly=getDrahtPoints();
            Draht drahtX;
         
            Draht draht=line.getDraht();
            Element node=addNodeIntoLine(p,line);
         
            if (line.getDirection()==Line.HORIZONTAL)
            {
                Point lastpoint=getDrahtPoint(drahtPoints.size()-2);
                if(line.myStart.y>lastpoint.y)
                {
                    drahtX=vmobject.addDrahtIntoCanvas(sourceElementID,sourcePin, node.getID(),0);
                    copyPoints(aktuellesPoly,drahtX);
                    node.getPin(0).draht=drahtX;
                }else
                {
                    drahtX=vmobject.addDrahtIntoCanvas(sourceElementID,sourcePin, node.getID(),2);
                    copyPoints(aktuellesPoly,drahtX);
                    node.getPin(2).draht=drahtX;
                }
            }else
            {
                Point lastpoint=getDrahtPoint(drahtPoints.size()-2);
                if(line.myStart.x>lastpoint.x)
                {
                    drahtX=vmobject.addDrahtIntoCanvas(sourceElementID,sourcePin, node.getID(),3);
                    copyPoints(aktuellesPoly,drahtX);
                    node.getPin(3).draht=drahtX;
                }else
                {
                    drahtX=vmobject.addDrahtIntoCanvas(sourceElementID,sourcePin, node.getID(),1);
                    copyPoints(aktuellesPoly,drahtX);
                    node.getPin(1).draht=drahtX;
                }
            }
         
         
            vmobject.setCursor(Cursor.getDefaultCursor());
            vmobject.setModusIdle();
            vmobject.reorderWireFrames();
            vmobject.owner.saveForUndoRedo();
        }*/
        
        
        if (modus==pinSelection)
        {
            pan=vmobject.getNearstPin(p.x,p.y,10);
            if (pan!=null)
            {
                
                JPin apin = pan;
                //int xx=pan.element.getX();
                //int yy=pan.element.getY();
                
                
                if ((sourceDataType==apin.dataType || sourceDataType==ExternalIF.C_VARIANT || apin.dataType==ExternalIF.C_VARIANT) && (apin.pinIO==JPin.PIN_INPUT || apin.pinIO==JPin.PIN_INPUT_OUTPUT) && apin.draht==null)
                {
                    abschliessen(pan.element.getID(), pan.pinIndex);
                }
                return;
            }
        }
        
        if (e.getButton()==e.BUTTON3)
        {
            vmobject.setModusIdle();
            vmobject.repaint();
        }
        else
        {
            startPoint.x=p.x;
            startPoint.y=p.y;
            addStueck(startPoint, startPoint);
        }
        }catch(NullPointerException eV){
        
        }
     }   
    }
    
    public void mouseReleased(MouseEvent e)
    {
    }
    
    public void mouseDblClick(MouseEvent e)
    {
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
    
    
    // Result = -1 wenn keine Vertikale Linie in der Nähe
    private int getDistanceToNearstVerticalLine(Point p)
    {
        int minX=999999;
        for (int i=0;i<vmobject.drahtLst.size();i++)
        {
            Draht draht = vmobject.getDraht(i);
            
            int res=draht.isVertikalLineNearPoint(p);
            if (res>-1 && res<minX)
            {
                if (Math.abs(res-minX)<10)
                {
                    minX=res;
                }
                else
                {
                    return res;
                }
                
            }
        }
        if (minX<99999)
        {
            return minX;
        }
        return -1;
    }
    public void mouseMoved(MouseEvent e)
    {
        if (vmobject==null) return;
        
        Point mouseKo = vmobject.getMousePosition();
        vmobject.setCursor(Cursor.getDefaultCursor());
        if (mouseKo==null) return;
        int x=mouseKo.x;
        int y=mouseKo.y;
        
       /* int dis=getDistanceToNearstVerticalLine(new Point(x,y));
        
        if (dis>-1)
        {
            x=dis-10;
            //System.out.println("dis="+dis);
        }*/
        
        pan=vmobject.getNearstPin(x,y,10);
        
        if (pan!=null)
        {
            int xx=pan.element.getX();
            int yy=pan.element.getY();
            x=xx+pan.getX()+5;
            y=yy+pan.getY()+5;
            modus=pinSelection;
            
        }
        else
        {
            modus=NOPE;
        }
        
        if (hasLastPoint==false)
            //if (aktuellesPinType==HOZ )
        {
            Point p=getDrahtPoint(drahtPoints.size()-1);
            p.x=x;
            p.y=y;
            
            p=getDrahtPoint(drahtPoints.size()-2);
            p.x=x;
        }
        else
        {
            Point p=getDrahtPoint(drahtPoints.size()-1);
            p.x=x;
            p.y=y;
            
            p=getDrahtPoint(drahtPoints.size()-2);
            p.y=y;
        }
        
        boolean isIn=false;
        
        Line line=vmobject.getLineInNaehe(new Point(x,y));
        
        if (line!=null)
        {
            
            if (line.getDirection()==Line.VERTIKAL) // Vertikal
            {
                if (hasLastPoint==false)
                {
                    hasLastPoint=true;
                    int x1=line.myStart.x;
                    hasLastPoint=true;
                    addDrahtPoint(x1,y);
                    
                    if (x<line.myStart.x)
                    {
                        getDrahtPoint(drahtPoints.size()-2).x-=20;
                        getDrahtPoint(drahtPoints.size()-3).x-=20;
                    }
                    else
                    {
                        getDrahtPoint(drahtPoints.size()-2).x+=20;
                        getDrahtPoint(drahtPoints.size()-3).x+=20;
                    }
                }
            }
            
            isIn=true;
        }
        
        pan=vmobject.getNearstPin(x,y,10);
        if (pan!=null)
        {
            vmobject.owner.frameCircuit.showPinDescription(pan);
            
            if ( ( sourceDataType==pan.dataType     ||
                    sourceDataType==ExternalIF.C_VARIANT ||
                    pan.dataType==ExternalIF.C_VARIANT) &&
                    ( ( pan.pinIO==JPin.PIN_INPUT || pan.pinIO==JPin.PIN_INPUT_OUTPUT ) && pan.draht==null))
            {
                vmobject.setCursor(CheckCursor);
            }
            else
            {
                vmobject.setCursor(CrossCursor);
            }
            
            if (pan.pinAlign==1) // Rechts
            {
                int x1=pan.element.getX()+pan.getX();
                int y1=pan.element.getY()+pan.getY()+5;
                if (hasLastPoint==false)
                {
                    hasLastPoint=true;
                    addDrahtPoint(x1,y);
                    
                    getDrahtPoint(drahtPoints.size()-2).x+=20;
                    getDrahtPoint(drahtPoints.size()-3).x+=20;
                }
            }
            if (pan.pinAlign==3) // Links
            {
                int x1=pan.element.getX()+pan.getX();
                int y1=pan.element.getY()+pan.getY()+5;
                if (hasLastPoint==false)
                {
                    hasLastPoint=true;
                    addDrahtPoint(x1,y);
                    
                    getDrahtPoint(drahtPoints.size()-2).x-=20;
                    getDrahtPoint(drahtPoints.size()-3).x-=20;
                }
            }
            
            isIn=true;
        }
        
        if (isIn==false)
        {
            
            vmobject.owner.frameCircuit.removePinDescription();
            if (hasLastPoint==true)
            {
                hasLastPoint=false;
                drahtPoints.remove(drahtPoints.size()-1);
            }
        }
        
        vmobject.repaint();
        
    }
    
    private Point getDrahtPoint(int index)
    {
        return (Point)drahtPoints.get(index);
    }
    
    public void processKeyEvent(KeyEvent ke)
    {
        
    }
    
    public void elementPinMouseReleased(MouseEvent e, int elementID, int pin)
    {
    }
    
    public void elementPinMousePressed(MouseEvent e, int elementID, int pin)
    {
        Element sourceElement = (Element)vmobject.getObjectWithID(elementID);
        JPin apin = sourceElement.getPin(pin);
        try{
        if (( sourceDataType==apin.dataType        ||
                sourceDataType==ExternalIF.C_VARIANT ||
                apin.dataType==ExternalIF.C_VARIANT) && ( (apin.pinIO==JPin.PIN_INPUT || pan.pinIO==JPin.PIN_INPUT_OUTPUT ) && apin.draht==null))
        {
            abschliessen(elementID, pin);
        }
        }catch(Exception er){
            System.out.println(er.getMessage());    
        }
    }
    
    public void elementPinMouseMoved(MouseEvent e, int elementID, int pin)
    {
    }
    
    public void draw(Graphics g)
    {
        drawPoints(g);
    }
    
    public void elementMouseEntered(MouseEvent e)
    {
    }
    
    public void elementMouseExited(MouseEvent e)
    {
    }
    
}
