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


package VisualLogic.variables;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;


/**
 *
 * @author Homer
 */
public class VSCanvas  extends VSObject
{    
    
    public Image image;
    public int type;
    public int x1;
    public int y1;
    public int x2;
    public int y2;
    
    public String caption="";
    public Font font=null;
            
    public ArrayList points = new ArrayList(); // Reserved for Future!
        
    public double translationX=0;
    public double translationY=0;
    
    public double rotationX=0.0;
    public double rotationY=0.0;
    public double rotationAngle=0.0;
    
    public double scaleX=1.0;
    public double scaleY=1.0;

    public double shearX=0.0;
    public double shearY=0.0;
    
    //public AffineTransform transform;
    
    //public int stroke;
    public Color strokeColor;
    public int strokeWidth=0;
    public Color fillColor;
    public boolean antialising = true;
    
        
    public VSCanvas() 
    {        
        
        
    }
    
    
    public java.awt.Image cloneImage(java.awt.Image imageToClone) 
    { 
        java.awt.image.ImageProducer originalProducer = imageToClone.getSource(); 
        java.awt.Toolkit defaultToolkit = java.awt.Toolkit.getDefaultToolkit(); 
        java.awt.Image newImage = defaultToolkit.createImage(originalProducer); 

        return newImage; 
    } 
    
  
    private void clonePoints(ArrayList points)
    {
        this.points.clear();
        Point p;
        for (int i=0;i<points.size();i++)
        {
            p = (Point)points.get(i);
            this.points.add(new Point(p.x,p.y));
        }
    }
    
    public void copyReferenceFrom(Object in)
    {
      copyValueFrom(in);
    }    
    
    public void copyValueFrom(Object in)
    {
        VSCanvas obj=(VSCanvas)in;
        
        if (obj.image!=null)
        {
            image=cloneImage(obj.image);
        }
        x1=obj.x1;
        y1=obj.y1;
        x2=obj.x2;
        y2=obj.y2;
        
        
        caption=obj.caption;
        
        if (obj.font!=null) 
        {
            font=new Font(obj.font.getFontName(),obj.font.getStyle(),obj.font.getSize());
        } else font=null;
        
        type=obj.type;
        
        clonePoints(obj.points);        

        translationX=obj.translationX;
        translationY=obj.translationY;

        rotationX=obj.rotationX;
        rotationY=obj.rotationY;
        rotationAngle=obj.rotationAngle;

        scaleX=obj.scaleX;
        scaleY=obj.scaleY;

        shearX=obj.shearX;
        shearY=obj.shearY;

        //transform=obj.transform;

       // stroke=obj.stroke;
        strokeColor=obj.strokeColor;
        strokeWidth=obj.strokeWidth;
        //fillColor=new Color(obj.fillColor.getRGB());
        fillColor=obj.fillColor;
        antialising=obj.antialising;
    }               
}
