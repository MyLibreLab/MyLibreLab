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
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.awt.geom.Line2D;

import org.w3c.dom.*;
import java.io.*;

/**
 *
 * @author Homer
 */
public class Draht extends Object implements Serializable
{
    private boolean bolOn=false;
    private VMObject vmobject;
    
    public VMObject getVMObject()
    {
        return vmobject;
    }
    private double zoom;
    public int sourcePin;
    public int destPin;
    public boolean resursive=false;
    public int sourceElementID;
    public int destElementID;
    public ArrayList polyList = new ArrayList();
    public ArrayList lineList = new ArrayList();    
    private Stroke strockeStandard=new BasicStroke(1);
    private Stroke strockeDick=new BasicStroke(3);
    private Stroke strockeExtraDick=new BasicStroke(4);
    
    private boolean selected=false;
    private double aktuellesValue=0.0;
    private int id=-1;
    private boolean valid=true;
    
    public boolean isValid()
    {
        return valid;
    }
    public void setValid(boolean value)
    {
        if (value!=valid)
        {
            valid=value;            
            vmobject.updateUI();
        }
    }

    public JPin pinSouce;
    public JPin pinDest;
    
    
    public Draht clone()
    {        
        Draht draht=new Draht(id,vmobject,sourceElementID, sourcePin,destElementID,destPin);
        draht.polyList =this.polyList;
        draht.lineList =this.lineList;
        
        return draht;
    }
    
    /** Creates a new instance of Draht */
    public Draht(int id,VMObject vmobject,int sourceElementID,int sourcePin,int destElementID,int destPin)
    {
        this.id=id;
        this.vmobject=vmobject;
        this.sourceElementID=sourceElementID;
        this.sourcePin=sourcePin;
        this.destElementID=destElementID;
        this.destPin=destPin; 
        zoom=1.0;
    }
    
    public int getID()
    {
        return id;
    }
    
    public Polygon getPolygon()
    {
      Polygon result = new Polygon();
      int x=0;
      int y=0;
      for (int i=0;i<polyList.size();i++)
      {
          PolyPoint p = (PolyPoint)polyList.get(i);

          result.addPoint(p.getX(),p.getY());
      }
      
      return result;
    }
    
    
    public PolyPoint getPointsWhereInLine(Point p1)
    {
      for (int i=0;i<polyList.size();i++)
      {
          PolyPoint p = (PolyPoint)polyList.get(i);
          
          if (p1.x== p.getX() && p1.y== p.getY() )
          {
              return p;
          }
      }
      return null;
    }

        
    
    public void loadFromStream(DataInputStream stream)
    {                        

      try
      {          
        int size=stream.readInt();
        for (int i = 0; i < size; i++) 
        {            
            //if (getVMObject()!=null)
            {
              PolyPoint p = new PolyPoint(getVMObject());
              p.loadFromStream(stream);
              p.setSelected(false);
              polyList.add(p);
           }
        }                        
      } catch(Exception ex)
      {
          vmobject.owner.showErrorMessage(""+ex.toString());
      }
      this.sourceElementID=sourceElementID;
      this.sourcePin=sourcePin;
      this.destElementID=destElementID;
      this.destPin=destPin;
      /*  VisualLogic.Element sourceElement = (Element)vmobject.getObjectWithID(sourceElementID);        
        JPin pin =sourceElement.getPin(sourcePin);
        if (pin!=null) pin.draht=this;        
        VisualLogic.Element destElement = (Element)vmobject.getObjectWithID(destElementID);       
        pin=destElement.getPin(destPin);
       */
        
        
        //if (pin!=null) pin.draht=this;
    }
    
    public void saveToStream(DataOutputStream dos)
    {
       try
       {                                             
           // Save all Point 
           dos.writeInt(polyList.size()); // save Anzahl der Points
           for (int i=0;i<polyList.size();i++)
           {
                PolyPoint p = getPoint(i);
                p.saveToStream(dos );
           }
           
       } catch (Exception ex)
       {
           vmobject.owner.showErrorMessage(""+ex.toString());
       }             
   }    
    
    
    public void saveToXML(org.w3c.dom.Element root,org.w3c.dom.Document doc,org.w3c.dom.Element nodeDraht)
    {
       nodeDraht.setAttribute("sourceElementID",""+sourceElementID);
       nodeDraht.setAttribute("sourcePin", ""+sourcePin);       
       nodeDraht.setAttribute("destElementID", ""+destElementID);
       nodeDraht.setAttribute("destPin", ""+destPin);   
       
       nodeDraht.setAttribute("id", ""+getID());   
                      
       // Save all Point 
              
       for (int i=0;i<polyList.size();i++)
       {

            org.w3c.dom.Element nodePoint = doc.createElement("Point");
            PolyPoint p = getPoint(i);            
            p.saveToXML(nodePoint);
            nodeDraht.appendChild(nodePoint);           
       }
       
       //nodeDraht.appendChild(nodePoints);
    }
    
    public void loadFromXML(org.w3c.dom.Element nodeDraht)
    {                
        NodeList nodePoints= nodeDraht.getElementsByTagName("Point");      
        for (int i = 0; i < nodePoints.getLength(); i++) 
        {
            org.w3c.dom.Element nodePoint = (org.w3c.dom.Element) nodePoints.item(i);
            
            PolyPoint p = new PolyPoint(getVMObject());
            p.loadFromXML(nodePoint);
            p.setSelected(false);
            polyList.add(p);
        }            
                
        VisualLogic.Element sourceElement = (Element)vmobject.getObjectWithID(sourceElementID);        
        JPin pin =sourceElement.getPin(sourcePin);
        if (pin!=null) pin.draht=this;        
        VisualLogic.Element destElement = (Element)vmobject.getObjectWithID(destElementID);        
        
        pin=destElement.getPin(destPin);
        if (pin!=null) pin.draht=this;
    }
    
    
    public int getSourceElementID()
    {
        return sourceElementID;
    }

    public int getDestElementID()
    {
        return destElementID;
    }
    
    public int getSourcePin()
    {
        return sourcePin;
    }

    public int getDestPin()
    {
        return destPin;
    }
    
    public void repaint()
    {
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                vmobject.repaint(getBounds());
            }
        });
    }
    
    public void setOn()
    {
        if (bolOn!=true)
        {
            bolOn=true;   
            
            repaint();

        }
    }
    private boolean highlighted=false;
    public void setHighLight(boolean value)
    {
        if (highlighted!=value)
        {
            highlighted=value;
            
            repaint();                        
        }
    }
    
    public synchronized void setOff()
    {        
        if (bolOn!=false)
        {
            bolOn=false;
            
            repaint();            
        }
    }
    
     public Line pruefeLinie(int x1, int y1, int x2, int y2)
     {
        for (int i=0;i<lineList.size();i++)
        {
            Line line = (Line)lineList.get(i);
            
            if (Line2D.linesIntersect(line.getStartPoint().x, line.getStartPoint().y, line.getEndPoint().x, line.getEndPoint().y, x1, y1, x2, y2))
            {
                return line;
            }
        }
        return null;
     }
    
    public Line schneidetLinieDenDraht(int x1, int y1, int x2, int y2)
    {
        Line line=null;
        
        line=pruefeLinie(x1,y1,x1,y2);
        if (line!=null)// LINKS
        {
            return line;
        }
        
        line=pruefeLinie(x2,y1,x2,y2);
        if (line!=null) // RECHTS
        {
            return line;
        }
        
        line=pruefeLinie(x1,y1,x2,y1);
        if (line!=null) // OBEN
        {
            return line;
        }
        
        line=pruefeLinie(x1,y2,x2,y2);
        if (line!=null) // UNTEN
        {
            return line;
        }
        
        return null;

    }    
    
    
    public Rectangle getBounds()
    {
        int minX=19999999;
        int minY=19999999;
        int maxX=0;
        int maxY=0;
        for (int i=0;i<lineList.size();i++)
        {
            Line line = (Line)lineList.get(i);
            if (line.getStartPoint().x<minX) minX=line.getStartPoint().x;
            if (line.getStartPoint().y<minY) minY=line.getStartPoint().y;
            
            if (line.getEndPoint().x<minX) minX=line.getEndPoint().x;
            if (line.getEndPoint().y<minY) minY=line.getEndPoint().y;
            
            if (line.getStartPoint().x>maxX) maxX=line.getStartPoint().x;
            if (line.getStartPoint().y>maxY) maxY=line.getStartPoint().y;
            
            if (line.getEndPoint().x>maxX) maxX=line.getEndPoint().x;
            if (line.getEndPoint().y>maxY) maxY=line.getEndPoint().y;            
        }
        
        return new Rectangle(minX-10,minY-10,maxX+10,maxY+10);
    }
    
    public void refresh()
    {
        Rectangle b=getBounds();
        //vmobject.paintImmediately(b); 
    }
    
    public void setSelected(boolean value)
    {
        if (value!=selected)
        {
            selected=value;
            vmobject.repaint();
        }
    }
    
   /*
    * laedt die Pins schon mal im Vorraus damit das processing schneller ist
    */
    public void setRunMode()
    {
        Element destElement = (Element)getVMObject().getObjectWithID(getDestElementID());
        Element sourceElement = (Element)getVMObject().getObjectWithID(getSourceElementID());
        int destPin = getDestPin();
        int sourcePin = getSourcePin(); 
        
        if (sourceElement!=null)
        {
            pinSouce = sourceElement.getPin(sourcePin);
        }
        if (destElement!=null)
        {
            pinDest =destElement.getPin(destPin);
        }
    }    
    
    public void setStopMode()
    {
     /*   Element destElement = (Element)getVMObject().getObjectWithID(getDestElementID());
        Element sourceElement = (Element)getVMObject().getObjectWithID(getSourceElementID());
        int destPin = getDestPin();
        int sourcePin = getSourcePin(); 
        
         pinSouce = null;
         pinDest = null;*/
    }    
    

    public void leiteInformation(double value)
    {                           
        if (pinSouce!=null) pinSouce.value=value;
        if (pinDest!=null) pinDest.value=value;
    }
    
    public void leiteInformation(Object object)
    {                           
        if (pinSouce!=null) pinSouce.object=object;
        if (pinDest!=null) pinDest.object=object;
        
        /*if (object instanceof Boolean)
        {            
            if (((Boolean)object).booleanValue()==true) setOn(); else  setOff(); 
        }*/
                
    }
    
    
    public void selectAnyPoints(boolean value)
    {
        for (int i=0;i<getPolySize();i++)
        {
            PolyPoint p= getPoint(i);
            p.setSelected(value);
        }        
    }
    
    public void scrambleAllPoints()
    {
        for (int i=0;i<getPolySize();i++)
        {
            PolyPoint p= getPoint(i);            
            p.setLeft(1);
            p.setTop(1);
        }        
    }
    
    
   /* public void leiteInformationII(double value)
    {
        for (int i=0;i<getPolySize();i++)
        {
            PolyPoint p= getPoint(i);
            if (p.nodeID!=-1)
            {
                p.leiteInformationII(value);
            }
        }
        
    }*/
    

    public boolean isSelected()
    {
        return selected;
    }
    
    public PolyPoint insertPoint(int index, int x, int y)
    {
        PolyPoint p=new PolyPoint(getVMObject());
        p.setLeft(x);
        p.setTop(y);
        p.setSelected(false);
        polyList.add(index,p);
        return p;
    }
    
    public void setDestElementID(int newID)
    {
        destElementID=newID;
    }

    public void setSourceElementID(int newID)
    {
        sourceElementID=newID;
    }
    
    public void setDestPin(int newPin)
    {
        destPin=newPin;
    }
    
    
    
    public PolyPoint addPoint(int x, int y)
    {
        PolyPoint p=new PolyPoint(getVMObject());        
        p.setLocation(x,y);
        p.setSelected(false);        
        polyList.add(p);
        
        return p;
    }
    
    public int getPolySize()
    {
        return polyList.size();
    }
    
    public void setPoint(int index, int x, int y)
    {
        if (index>=0 && index<getPolySize())
        {
            //getPoint(index).setLeft((int)((double)x/zoom));
            //getPoint(index).setTop((int)((double)y/zoom));
            getPoint(index).setLocation(x,y);            
        }
    }
    

    public void deselectAllPoints()
    {
        for (int i=0;i<getPolySize();i++)
        {
            getPoint(i).setSelected(false);
        }
    }
    
    public void deletePoint(int index)
    {
        polyList.remove(index);
    }
            
    /*
     *  Liefert fuer null wenn der Index auserhalt des Bereiches liegt
     */
    
    
            
    public PolyPoint getFirstPoint()
    {
        for (int i=1;i<getPolySize();i++)
        {
            PolyPoint p= getPoint(i);
            return p;
        }

        return null; 
    }
    
    public PolyPoint getLastPoint()
    {
        for (int i=getPolySize()-2;i>0;i--)
        {
            PolyPoint p= getPoint(i);
            return p;            
        }

        return null; 
    }
    
            
    public PolyPoint getPoint(int index)
    {
        PolyPoint res=null;
        
        if (index>=0 && index<getPolySize())           
        {
            res=(PolyPoint)polyList.get(index);
        }
        return res;                    
    }
    
    /*
     *  Liefert -1 wenn der Index auserhalt des Bereiches liegt
     */            
    

    public void mouseClicked(MouseEvent e)
    {
    
    }
    
    public void mouseEntered(MouseEvent e)
    {
	
    }
        
    public void mouseExited(MouseEvent e)
    {

    }    
       
    public void mouseReleased(MouseEvent e)
    {
        
    }   
    
    public void mousePressed(MouseEvent e) 
    {        

    }
    


    public void reorganize()
    {
        lineList.clear();
        
        int lastX=0;
        int lastY=0;
        
        Point start=new Point();        
        // Wandle nun die Punkte in Lines um
        for (int i=0;i<getPolySize();i++)
        {   
            PolyPoint p = getPoint(i);
                
            Point point = new Point();                
            point.x=p.getX();
            point.y=p.getY();


            if (i==0)
            {
                start.x=point.x;
                start.y=point.y;
            }                
            else
            {                

                 PolyPoint lastP = getPoint(i-1);

                   if (Math.abs(start.y-point.y)==0) // Hoz
                   {
                        lastP.setLocation(lastP.getX(),start.y);                   
                   } else
                   {
                        lastP.setLocation(start.x,lastP.getY());
                   }                          

                Line line=new Line(start,point,this, i);
                line.p1=lastP;
                line.p2=p;

                start=point;
                lineList.add(line);
            }
            
        }
        
        
      
    }
    
    

    public void copyPointsNegiert(int from,int bis,Draht dest)
    {
        for (int i=getPolySize()-1-from;i>=bis;i--)
        {   
            PolyPoint p = getPoint(i);
            dest.addPoint(p.getX(),p.getY());
        }              
    }
            
    public void copyPoints(int von ,int bis, Draht dest)
    {
        for (int i=von;i<bis;i++)
        {   
            PolyPoint p = getPoint(i);
            int x=p.getX();
            int y=p.getY();
            dest.addPoint(x,y);
        }              
    }
    
    // liefert null bei nicht gelingen!
    public Line isOverLine(Point p)
    {
        for (int i=0;i<lineList.size();i++)
        {
            Line line = (Line)lineList.get(i);                        
            if (line.isPointInLine(p))
            {
                return line;
            }
        }
        return null;
    }
    
    
    // Liefert >0 wenn eine vertikale Linie in der Nähe ist
    // Liefert -1 wenn nicht!
    public int isVertikalLineNearPoint(Point p)
    {
        int minX=99999;
        
        for (int i=0;i<lineList.size();i++)
        {
            Line line = (Line)lineList.get(i);
            int res=line.isVertikalLineNearPoint(p,20);

            if (res>-1 && res<minX)
            {
                minX=res;
            }
        }   
        if (minX<99999)
        {
            return minX;
        }
        return -1;
    }
    
    public Line getLineInDerNaehe(Point p)
    {
        for (int i=0;i<lineList.size();i++)
        {
            Line line = (Line)lineList.get(i);
            if (line.isPointInNaehe(p))
            {
                return line;
            }
        }
        return null;
    }
    
    public void setZoom(double value)
    {
        if (zoom!=value) 
        {
            zoom=value;  
            reorganize();
        }
    }
    /*
     * Zeichnet des Draht
     */
    public void draw(Graphics g)
    {
        if (vmobject==null) return;
        
        if (!vmobject.isGraphicLocked())
        {
            Graphics2D g2 = (Graphics2D)g;

            //if (vmobject.getGraphics()!=null) 
            {
                reorganize();

                g.setColor(Color.black);

                int dataType = -1;
                
                Element elementSrc = (Element)vmobject.getObjectWithID(getSourceElementID());                                
                Element elementDst = (Element)vmobject.getObjectWithID(getDestElementID());                
                
                int dataTypeSrc =-1;
                int dataTypeDest = -1;
                
                try
                {
                    JPin aa=elementSrc.getPin(getSourcePin());
                    JPin bb=elementDst.getPin(getDestPin());                    
                    if (aa!=null) dataTypeSrc = aa.dataType;
                    if (bb!=null) dataTypeDest = bb.dataType;
                }catch(Exception ex)
                {
                    return ;
                }
                
                if (dataTypeSrc==-1)
                    dataType=dataTypeDest; else dataType = dataTypeSrc;
                
                if (dataTypeSrc==ExternalIF.C_VARIANT)
                {
                    dataType=dataTypeDest;
                } else dataType=dataTypeSrc;
                                
                                         
                VSDataType.setColorStrokeFromDataType(g2,dataType);
                if (resursive) g.setColor(Color.lightGray);
                
                if (highlighted)
                {                    
                    //g2.setColor(Color.red);
                    g2.setStroke(strockeExtraDick);
                }
                
                if (selected) 
                {
                    g2.setStroke(strockeExtraDick);
                    g2.setColor(Color.RED);
                }
                if (bolOn) 
                {
                    g2.setStroke(strockeExtraDick);
                    
                    //g2.setColor(Color.red);
                    //g2.setStroke(strockeDick);
                }
                
                
                if (valid==false) 
                {
                    g2.setStroke(strockeDick);
                    g2.setColor(Color.RED);
                }
                
                int x=0;
                int y=0;
                for (int i=0;i<lineList.size();i++)
                {
                   Line line = (Line)lineList.get(i);
                   line.draw(g2);   
                   
                   JPin bb=elementDst.getPin(getDestPin());
                   
                  /* if (dataType==ExternalIF.C_FLOWINFO)
                   {
                       if (bb!=null && bb.pinAlign==0 && i==lineList.size()-1)
                       {
                           g2.setStroke(strockeStandard);
                           x=line.myEnd.x;
                           y=line.myEnd.y-2;
                           Polygon p = new Polygon();
                           p.addPoint(x,y);
                           p.addPoint(x-5,y-5);
                           p.addPoint(x+5,y-5);
                           g.setColor(Color.GREEN);
                           g2.fillPolygon(p);
                           g.setColor(Color.BLACK);
                           g2.drawPolygon(p);
                       }
                   }*/
                   
                }
                
        
              /*  int id1=getSourceElementID();
                int id2=getDestElementID();

                g.setColor(Color.BLACK);
                Font font = new Font("Courier",0,10);
                g.setFont(font);

                g.drawString("src="+id1+" dst="+id2,x,y);
                g2.setStroke(strockeStandard);*/
                 
            }
        }
    }
    
}
