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
import java.io.Serializable;


/**
 *
 * @author Homer
 */
public class Line  implements Serializable {
    public Point myStart;
    public Point myEnd;
    private Rectangle myBoundingBox = new Rectangle();
    private Draht draht;
    
    public PolyPoint p1;
    public PolyPoint p2;
    
    public static final int HORIZONTAL=0;
    public static final int VERTIKAL=1;
    
    public int index;
    
    /**
     * Construct a line
     * @param start The starting point
     * @param end   The end point
     **/
    public Line(Point start, Point end, Draht draht, int index) {
        this.index = index;
        this.draht=draht;
        myStart = start;
        myEnd = end;
        updateBoundingBox();
    }
    
    public Point getStartPoint() {
        return myStart;
    }
    public Point getEndPoint() {
        return myEnd;
    }
    
    public Draht getDraht() {
        return draht;
    }
    
    
    
    
    
    private void updateBoundingBox() {
        myBoundingBox.setBounds(Math.min(myStart.x, myEnd.x),
                Math.min(myStart.y, myEnd.y),
                Math.abs(myEnd.x - myStart.x),
                Math.abs(myEnd.y - myStart.y));
    }
    
    
    
    public void draw(Graphics2D g) {
        int x,y,xx,yy;
        x=myStart.x;
        y=myStart.y;
        xx=myEnd.x;
        yy=myEnd.y;
        
        g.drawLine(x,y,xx,yy);
    }
    
    public void translateBy(int dx, int dy) {
        myStart.translate(dx, dy);
        myEnd.translate(dx, dy);
        updateBoundingBox();
    }
    
    /*public boolean isPickedBy(Point p)
    {
        return Line.segmentIsPickedBy(myStart, myEnd, p);
    }*/
    
    // Liefert 0 fuer Horizontal und 1 fuer Vertikal
    public int getDirection() {
        int x,y,xx,yy;
        x=myStart.x;
        y=myStart.y;
        xx=myEnd.x;
        yy=myEnd.y;
        
        if (x==xx) return VERTIKAL;   else return HORIZONTAL;
    }
    
    
    public boolean isPointInLine(Point p) {
        Point p1=new Point(myStart.x, myStart.y);
        Point p2=new Point(myEnd.x,myEnd.y);
        
        if (getDirection()==HORIZONTAL) 
        {
            if (p1.x>p2.x) {
                Point temp=new Point(p1.x,p1.y);
                p1.x=p2.x;
                p1.y=p2.y;
                
                p2.x=temp.x;
                p2.y=temp.y;
            }
            
            if (p.x>=p1.x+1 && p.y>=p1.y-2 && p.x<=p2.x-1 && p.y<=p2.y+2) return true;
        } else {
            if (p1.y>p2.y) {
                Point temp=new Point(p1.x,p1.y);
                p1.x=p2.x;
                p1.y=p2.y;
                
                p2.x=temp.x;
                p2.y=temp.y;
            }
            if (p.x>=p1.x-2 && p.y>=p1.y+1 && p.x<=p2.x+2 && p.y<=p2.y-1) return true;
        }
        return false;
    }
    
    public boolean isPointInNaehe(Point p) {
        Point p1=new Point(myStart.x, myStart.y);
        Point p2=new Point(myEnd.x,myEnd.y);
        
        int distance=5;
        if (getDirection()==HORIZONTAL) {
            if (p1.x>p2.x) {
                Point temp=new Point(p1.x,p1.y);
                p1.x=p2.x;
                p1.y=p2.y;
                
                p2.x=temp.x;
                p2.y=temp.y;
            }
            
            if (p.x>=p1.x+1 && p.y>=p1.y-distance && p.x<=p2.x-1 && p.y<=p2.y+distance) return true;
        } else {
            if (p1.y>p2.y) {
                Point temp=new Point(p1.x,p1.y);
                p1.x=p2.x;
                p1.y=p2.y;
                
                p2.x=temp.x;
                p2.y=temp.y;
            }
            if (p.x>=p1.x-distance && p.y>=p1.y+1 && p.x<=p2.x+distance && p.y<=p2.y-1) return true;
        }
        return false;
    }
    
    
    // Liefert >0 wenn eine vertikale Linie in der Nähe ist
    // Liefert -1 wenn nicht!    
    public int isVertikalLineNearPoint(Point p, int distance)
    {
        int result=-1;
        Point p1=new Point(myStart.x, myStart.y);
        Point p2=new Point(myEnd.x,myEnd.y);
                
        if (getDirection()==VERTIKAL)
        {
            
            if (p1.y>p2.y) 
            {
                Point temp=new Point(p1.x,p1.y);
                p1.x=p2.x;
                p1.y=p2.y;
                
                p2.x=temp.x;
                p2.y=temp.y;
            }
            
            int d=p2.x-p.x;
            if (d<distance) //&& p.y>=p1.y+1 && p.y<=p2.y-1)
            {
                return p2.x;
            }
        }
        
        return result;
    }
    
    
/*    public boolean isPickedByNaehe(Point p)
    {
        return Line.segmentIsPickedByNaehe(myStart, myEnd, p);
    }
 */
    
    /**
     * A Utility function that determines whether a point is in a segment.
     * @param A     The starting point of the segment
     * @param B     The end point of the segment
     * @param C     The point to test
     * @return      true if C in AB, false otherwise
     **/
 /*   public static boolean segmentIsPickedBy(Point A, Point B, Point C)
    {
        double a, b;
        double distance; // Distance from C to Line AB
        double dx, dy; // Coordinates of projection of C on AB
        double lambda;
  
        if (A.x != B.x)
        {
            // Calculate the equation y = ax + b of line AB
            a = (B.y - A.y) / (double) (B.x - A.x);
            b = (A.y * B.x - B.y * A.x) / (double) (B.x - A.x);
            // D = Projection of C on AB
            dx = (C.x - a * (b - C.y)) / (1 + a * a);
            dy = (a * C.x + b + a * a * C.y) / (1 + a * a);
            distance = C.distance(dx, dy);
            // or : distance = Math.abs( b - C.y + a * C.x ) / Math.sqrt( 1 + a * a);
            lambda = (dx - A.x) / (double) (B.x - A.x);
        } else
        {
            distance = Math.abs(C.x - A.x);
            lambda = (C.y - A.y) / (double) (B.y - A.y);
        }
        return (distance <= 5) && (lambda >= 0) && (lambda <= 1);
      }
  
    public static boolean segmentIsPickedByNaehe(Point A, Point B, Point C)
    {
        double a, b;
        double distance; // Distance from C to Line AB
        double dx, dy; // Coordinates of projection of C on AB
        double lambda;
  
        if (A.x != B.x)
        {
            // Calculate the equation y = ax + b of line AB
            a = (B.y - A.y) / (double) (B.x - A.x);
            b = (A.y * B.x - B.y * A.x) / (double) (B.x - A.x);
            // D = Projection of C on AB
            dx = (C.x - a * (b - C.y)) / (1 + a * a);
            dy = (a * C.x + b + a * a * C.y) / (1 + a * a);
            distance = C.distance(dx, dy);
            // or : distance = Math.abs( b - C.y + a * C.x ) / Math.sqrt( 1 + a * a);
            lambda = (dx - A.x) / (double) (B.x - A.x);
        } else
        {
            distance = Math.abs(C.x - A.x);
            lambda = (C.y - A.y) / (double) (B.y - A.y);
        }
        return (distance <= 10) && (lambda >= 0) && (lambda <= 1);
      }
  */
}
