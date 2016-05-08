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

import SimpleFileSystem.FileSystemOutput;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 *
 * @author Homer
 */
public class PolyPoint extends VisualObject
{
    public int oldX;
    public int oldY;    
    private int pWidth;
    private boolean selected;    
    private VMObject basis;
    
    public boolean deleted=false;
        
    
    public void setSelected(boolean value)
    {
        selected=value;        
    }
    public boolean isSelected(){return selected;}
    
    /** Creates a new instance of PolyPoint */
    public PolyPoint(VMObject basis) 
    {                
        this.basis=basis;
        pWidth=6;   
        setSize(pWidth,pWidth);
    }
    
    
    public void loadFromStream(DataInputStream stream)
    {
        try
        {
          int x= stream.readInt();
          int y= stream.readInt();
          setLocation(x,y);        
        } catch(Exception ex)
        {
          basis.owner.showErrorMessage(""+ex.toString());
        }
    }
        
    public void saveToStream(DataOutputStream dos)
    {
       try
       {                      
           dos.writeInt(getX());
           dos.writeInt(getY());
           
        } catch (Exception ex)
       {
           basis.owner.showErrorMessage(""+ex.toString());
       }
    }
    
    public void saveToXML(org.w3c.dom.Element node)
    {
       node.setAttribute("x", ""+getX());
       node.setAttribute("y", ""+getY());              
    }

    
    public void loadFromXML(org.w3c.dom.Element node)
    {                
        int x= Integer.parseInt(node.getAttribute("x"));
        int y= Integer.parseInt(node.getAttribute("y"));        
        setLocation(x,y);
    }
    
    public void setLocation(int x, int y)
    {
        super.setLocation(x,y);
    }
    

    public void setLocation(Point p)
    {
        setLocation(p.y,p.y);        
    }
    
    
    public void paint(Graphics2D g)
    {
        int pw=pWidth;
        int pw2 = pw/2;
        
        if (selected) g.setColor(Color.red); else g.setColor(Color.black);
        
        int x=getX();
        int y=getY();
        int w=getWidth();
        int h=getHeight();
        //if (!(basis.getStatus() instanceof VisualLogic.basisStatus.StatusRun))
        {
            // zeichne den PolyPoint
            //g.fillRect(x-pw2,y-pw2, w,h);
        }
    }

    
    
}
