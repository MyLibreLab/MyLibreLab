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
import VisualLogic.PolyPoint;
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
public class StatusLineVertikal extends Object implements StatusBasisIF
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
    public StatusLineVertikal(VMObject vmobject,ArrayList drahtPoints, int sourceElementID,int sourcePin,Point start) 
    {
        this.vmobject=vmobject;
        this.drahtPoints=drahtPoints;
                
        this.sourceElementID=sourceElementID;
       
        this.sourcePin=sourcePin;
        
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
    
    

    public void mouseDragged(MouseEvent e) {
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
            pinA.dataType=JPin.PIN_OUTPUT;
        }
        
        if (destElement.getInternName().equalsIgnoreCase("###NODE###"))
        {
            pinB.dataType=JPin.PIN_INPUT;
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
    public Polygon[] getLRDrahts(Draht draht, Line line, Point p)
    {
        Polygon poly = draht.getPolygon();

        PolyPoint startPoly=draht.getPoint(0);
        PolyPoint endPoly=draht.getPoint(draht.getPolySize()-1);

        Point startP=new Point(startPoly.getX(),startPoly.getY());
        Point endP=new Point(endPoly.getX(),endPoly.getY());

        Polygon leftLine=Tools.copyPolygon(poly, startP, line.getStartPoint());
        Polygon rightLine=Tools.copyPolygon(poly, line.getEndPoint(), endP);


        int[] xvalues=new int[rightLine.npoints+1];
        int[] yvalues=new int[rightLine.npoints+1];

        System.arraycopy(rightLine.xpoints,0,xvalues,1,rightLine.npoints);
        System.arraycopy(rightLine.ypoints,0,yvalues,1,rightLine.npoints);

        xvalues[0]=p.x;
        yvalues[0]=line.getStartPoint().y;
        leftLine.addPoint(p.x,line.getStartPoint().y);

        rightLine=new Polygon(xvalues,yvalues,xvalues.length);
        /*myOut(poly);
        myOut(leftLine);
        myOut(rightLine);*/
        
        Polygon[] result=new Polygon[2];
        result[0]=leftLine;
        result[1]=rightLine;
        
        return result;
}
    
    public Polygon[] getTBDrahts(Draht draht, Line line, Point p)
    {
        Polygon poly = draht.getPolygon();

        PolyPoint startPoly=draht.getPoint(0);
        PolyPoint endPoly=draht.getPoint(draht.getPolySize()-1);

        Point startP=new Point(startPoly.getX(),startPoly.getY());
        Point endP=new Point(endPoly.getX(),endPoly.getY());

        Polygon leftLine=Tools.copyPolygon(poly, startP, line.getStartPoint());
        Polygon rightLine=Tools.copyPolygon(poly, line.getEndPoint(), endP);


        int[] xvalues=new int[rightLine.npoints+1];
        int[] yvalues=new int[rightLine.npoints+1];

        System.arraycopy(rightLine.xpoints,0,xvalues,1,rightLine.npoints);
        System.arraycopy(rightLine.ypoints,0,yvalues,1,rightLine.npoints);

        xvalues[0]=line.getStartPoint().x;
        yvalues[0]=p.y;
        leftLine.addPoint(line.getStartPoint().x,p.y);


        rightLine=new Polygon(xvalues,yvalues,xvalues.length);
        /*myOut(poly);
        myOut(leftLine);
        myOut(rightLine);*/
        
        Polygon[] result=new Polygon[2];
        result[0]=leftLine;
        result[1]=rightLine;
        
        return result;
}
    
    
    public Element addNode()
    {
        Element element;
        element=vmobject.AddDualElement("/CircuitElements/Node","bin", "Node", "", null);
        
        return element;
    }
    
    public void copyPoints(Polygon poly, Draht draht)
    {
        int[] xvalues = poly.xpoints;
        int[] yvalues = poly.ypoints;

        for (int i=0;i<poly.npoints;i++)
        {
            draht.addPoint(xvalues[i],yvalues[i]);
        }        
    }
    
    // Result is the Node-Element!
    public Element addNodeIntoLine(Point p, Line line)
    {        
        Draht draht=line.getDraht();
        Element node=addNode();
        
        if (line.getDirection()==Line.HORIZONTAL)
        {
            Polygon[] lrDrahts=getLRDrahts(draht,line, p);
            Polygon leftPoly=lrDrahts[0];
            Polygon rightPoly=lrDrahts[1];
            node.setLocation(p.x-(node.getWidth()/2),line.myStart.y-(node.getHeight()/2));

            // 1. Delete old Draht
            // 2. verbinde LDraht mit Source und Node
            // 3. verbinde RDraht mit Node und Dest
            vmobject.deleteDraht(draht);

            Draht leftDraht;
            Draht rightDraht;
            
            if (line.myStart.x<line.myEnd.x)
            {
                leftDraht=vmobject.addDrahtIntoCanvas(draht.getSourceElementID(),draht.getSourcePin(), node.getID(),3);
                rightDraht=vmobject.addDrahtIntoCanvas(node.getID(),1, draht.getDestElementID(),draht.getDestPin());
                node.getPin(1).draht=rightDraht;
                node.getPin(3).draht=leftDraht; 
            }else
            {
                leftDraht=vmobject.addDrahtIntoCanvas(draht.getSourceElementID(),draht.getSourcePin(), node.getID(),1);
                rightDraht=vmobject.addDrahtIntoCanvas(node.getID(),3, draht.getDestElementID(),draht.getDestPin());
                node.getPin(3).draht=rightDraht;
                node.getPin(1).draht=leftDraht;                
            }

            copyPoints(leftPoly,leftDraht);
            copyPoints(rightPoly,rightDraht);
            
            
        }else
        {                
            Polygon[] lrDrahts=getTBDrahts(draht,line, p);

            Polygon poly1=lrDrahts[0];
            Polygon poly2=lrDrahts[1];
            
            node.setLocation(line.myStart.x-(node.getWidth()/2), p.y-(node.getHeight()/2));
            
            // 1. Delete old Draht
            // 2. verbinde LDraht mit Source und Node
            // 3. verbinde RDraht mit Node und Dest
            vmobject.deleteDraht(draht);

            
            Draht draht1;
            Draht draht2;
            
            if (line.myStart.y>line.myEnd.y)
            {
                draht1=vmobject.addDrahtIntoCanvas(draht.getSourceElementID(),draht.getSourcePin(), node.getID(),2);
                draht2=vmobject.addDrahtIntoCanvas(node.getID(),0, draht.getDestElementID(),draht.getDestPin());           
                node.getPin(0).draht=draht1;
                node.getPin(2).draht=draht2;
            }else
            {
                draht1=vmobject.addDrahtIntoCanvas(draht.getSourceElementID(),draht.getSourcePin(), node.getID(),0);
                draht2=vmobject.addDrahtIntoCanvas(node.getID(),2, draht.getDestElementID(),draht.getDestPin());           
                node.getPin(0).draht=draht2;
                node.getPin(2).draht=draht1;
            }

            copyPoints(poly1,draht1);
            copyPoints(poly2,draht2);

            
            /*
            // 1. Delete old Draht
            // 2. verbinde LDraht mit Source und Node
            // 3. verbinde RDraht mit Node und Dest
            vmobject.deleteDraht(draht);


            Draht leftDraht=vmobject.addDrahtIntoCanvas(draht.getSourceElementID(),draht.getSourcePin(), node.getID(),3);
            Draht rightDraht=vmobject.addDrahtIntoCanvas(node.getID(),1, draht.getDestElementID(),draht.getDestPin());
            Draht upDraht=null;
            Draht bottonDraht=null;

            Polygon aktuellesPoly=getDrahtPoints();

            copyPoints(leftPoly,leftDraht);
            copyPoints(rightPoly,rightDraht);

            Point lastpoint=getDrahtPoint(drahtPoints.size()-2);
            if(line.myStart.y>lastpoint.y)
            {
                upDraht=vmobject.addDrahtIntoCanvas(sourceElementID,sourcePin, node.getID(),0);
                copyPoints(aktuellesPoly,upDraht);
            }else
            {
                bottonDraht=vmobject.addDrahtIntoCanvas(sourceElementID,sourcePin, node.getID(),2);
                copyPoints(aktuellesPoly,bottonDraht);
            }

            node.getPin(0).draht=upDraht;
            node.getPin(1).draht=rightDraht;
            node.getPin(2).draht=bottonDraht;
            node.getPin(3).draht=leftDraht;
        */
        }
        return node;        
    }
    
    public void mousePressed(MouseEvent e) 
    {
      if(e!=null)
      { 
        try{  
        Point p = vmobject.getMousePosition();

        Line line=vmobject.getLineInNaehe(p);
        /*if (line!=null)
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
                

                if ((sourceDataType==apin.dataType || sourceDataType==ExternalIF.C_VARIANT || apin.dataType==ExternalIF.C_VARIANT) && apin.pinIO==JPin.PIN_INPUT && apin.draht==null)
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
        } else
        {
              startPoint.x=p.x;
              startPoint.y=p.y;
              addStueck(startPoint, startPoint); 
        }
        }catch(NullPointerException eV){
        
        }
      }  
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseDblClick(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
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
                } else
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

        pan=vmobject.getNearstPin(x,y,10);
        if (pan!=null)
        {
            int xx=pan.element.getX();
            int yy=pan.element.getY();
            x=xx+pan.getX()+5;
            y=yy+pan.getY()+5;
            modus=pinSelection;
            
        } else
        {
            modus=NOPE;
        }

        if (hasLastPoint==false)
        {
            Point p=getDrahtPoint(drahtPoints.size()-1);
            p.x=x;
            p.y=y;

            p=getDrahtPoint(drahtPoints.size()-2);
            p.y=y;
        } else
        {
            Point p=getDrahtPoint(drahtPoints.size()-1);
            p.x=x;
            p.y=y;
                    
            p=getDrahtPoint(drahtPoints.size()-2);            
            p.x=x;            
        }
        
        
        pan=vmobject.getNearstPin(x,y,10);
        if (pan!=null)
        {          
           vmobject.owner.frameCircuit.showPinDescription(pan); 
           if (( sourceDataType==pan.dataType || 
                 sourceDataType==ExternalIF.C_VARIANT || 
                 pan.dataType==ExternalIF.C_VARIANT) && 
                 ((pan.pinIO==JPin.PIN_INPUT || pan.pinIO==JPin.PIN_INPUT_OUTPUT) && pan.draht==null))
            {
                vmobject.setCursor(CheckCursor);                
            }else
            {                     
                vmobject.setCursor(CrossCursor);                
            }
            
            if (pan.pinAlign==0) // Oben
            {
                int x1=pan.element.getX()+pan.getX();
                int y1=pan.element.getY()+pan.getY()+5;
                if (hasLastPoint==false)
                {
                    hasLastPoint=true;
                    addDrahtPoint(x,y);
                                        
                    getDrahtPoint(drahtPoints.size()-2).y-=20;
                    getDrahtPoint(drahtPoints.size()-3).y-=20;                    
                }
            }
           
            if (pan.pinAlign==1) // Rechts
            {
                int x1=pan.element.getX()+pan.getX();
                int y1=pan.element.getY()+pan.getY()+5;
                if (hasLastPoint==false)
                {
                    /*hasLastPoint=true;
                    addDrahtPoint(x,y);
                                        
                    getDrahtPoint(drahtPoints.size()-2).x+=20;
                    getDrahtPoint(drahtPoints.size()-3).x+=20;                    */
                }
            }
           if (pan.pinAlign==2) // Unten
            {
                int x1=pan.element.getX()+pan.getX();
                int y1=pan.element.getY()+pan.getY()+5;
                
                if (hasLastPoint==false)
                {
                    hasLastPoint=true;
                    addDrahtPoint(x1,y);
                                        
                    getDrahtPoint(drahtPoints.size()-2).y+=20;
                    getDrahtPoint(drahtPoints.size()-3).y+=20;
                }                
            }
            if (pan.pinAlign==3) // Links
            {
                int x1=pan.element.getX()+pan.getX();
                int y1=pan.element.getY()+pan.getY()+5;
                if (hasLastPoint==false)
                {
                   // hasLastPoint=true;
                    //addDrahtPoint(x1,y);
                                        
                    //getDrahtPoint(drahtPoints.size()-2).x-=20;
                    //getDrahtPoint(drahtPoints.size()-3).x-=20;                    
                }                
            }
 
           
        } else
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
    
    public void processKeyEvent(KeyEvent ke) {
    }

    public void elementPinMouseReleased(MouseEvent e, int elementID, int pin) {
    }

    public void elementPinMousePressed(MouseEvent e, int elementID, int pin) 
    {
        Element sourceElement = (Element)vmobject.getObjectWithID(elementID);                    
        JPin apin = sourceElement.getPin(pin); 
        try{

        if (  (sourceDataType==apin.dataType || 
               sourceDataType==ExternalIF.C_VARIANT || 
               apin.dataType==ExternalIF.C_VARIANT) && 
             ((apin.pinIO==JPin.PIN_INPUT || apin.pinIO==JPin.PIN_INPUT_OUTPUT) && apin.draht==null))
        {                                
          abschliessen(elementID, pin);        
        }
        }catch(Exception er){
         System.out.println(er.getMessage()); 
        }
    }

    public void elementPinMouseMoved(MouseEvent e, int elementID, int pin) {
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
