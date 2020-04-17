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

import CustomColorPicker.RoundGradientPaint;
import java.awt.*;
import java.awt.geom.Point2D;

public class VSColorAdvanced extends VSObject
{
    
    public static final int MODE_FLAT=0;
    public static final int MODE_LINEAR=1;
    public static final int MODE_RADIAL=2;
        
    public int modus=MODE_FLAT;
    public Point p1=new Point(10,10);
    public Point p2=new Point(100,100);
    public Color color1=new Color(0,0,0);
    public Color color2=new Color(255,255,255);
    public int color1Transparency=255;
    public int color2Transparency=255;
    public boolean wiederholung=false;
   
    public VSColorAdvanced() 
    {
    
    }
    
    public void setValue(int modus,Point p1,Point p2, Color color1, Color color2, int color1Transparency, int color2Transparency, boolean wiederholung)
    {
        this.modus=modus;
        this.p1=p1;
        this.p2=p2;
        this.color1=color1;        
        this.color2=color2;
        this.color1Transparency=color1Transparency;
        this.color2Transparency=color2Transparency;
        this.wiederholung=wiederholung;
        setChanged(true);
    }
    
    public void copyReferenceFrom(Object in)
    {
      copyValueFrom(in);
    }    
    
    public void copyValueFrom(Object in)
    {
        if (in!=null)
        {        
          VSColorAdvanced temp =(VSColorAdvanced)in;
          this.modus=temp.modus;
          this.p1=temp.p1;
          this.p2=temp.p2;
          this.color1=temp.color1;
          this.color2=temp.color2;
          this.color1Transparency=temp.color1Transparency;
          this.color2Transparency=temp.color2Transparency;
          this.wiederholung=temp.wiederholung;
          
          
          setChanged(temp.isChanged());
        } else 
        {
            this.modus=MODE_FLAT;
            this.p1=new Point(10,10);
            this.p2=new Point(100,100);
            this.color1=new Color(0,0,0);
            this.color2=new Color(255,255,255);
            this.color1Transparency=255;
            this.color2Transparency=255;
            this.wiederholung=false;
        }
    }      
    
   public Color withTransparency(Color a, int transp)
    {
        return new Color(a.getRed(),a.getGreen(),a.getBlue(),transp);
    }
        
    
    public void setFillColor(Graphics2D g)
    {
        if (modus==MODE_FLAT)
        {
            g.setColor(withTransparency(color1,color1Transparency));
         
        }else
        if (modus==MODE_LINEAR)
        {
            GradientPaint gp = new GradientPaint
                    ( p1.x,p1.y, withTransparency(color1,color1Transparency),
                      p2.x,p2.y, withTransparency(color2,color2Transparency),
                      wiederholung );
         
            g.setPaint( gp );
         
        }else
        if (modus==MODE_RADIAL)
        {
            RoundGradientPaint rgp = new RoundGradientPaint(p1.x,p1.y, withTransparency(color1,color1Transparency),
                new Point2D.Double(p2.x,p2.y), withTransparency(color2,color2Transparency));
            g.setPaint(rgp);
        }
        
    }
    
    
    public void loadFromStream(java.io.FileInputStream fis) 
    {
        try
        {        
            java.io.DataInputStream dis = new java.io.DataInputStream(fis);

            modus=dis.readInt();
            
            int r=dis.readInt();
            int g=dis.readInt();
            int b=dis.readInt();            
            this.color1Transparency=dis.readInt();
            color1=new Color(r,g,b,color1Transparency);
            
            r=dis.readInt();
            g=dis.readInt();
            b=dis.readInt();            
            this.color2Transparency=dis.readInt();            
            color2=new Color(r,g,b,color2Transparency);
            
            int x=dis.readInt();
            int y=dis.readInt();            
            this.p1=new Point(x,y);

            x=dis.readInt();
            y=dis.readInt();            
            this.p2=new Point(x,y);
            
            wiederholung=dis.readBoolean();
            
            
        } catch(Exception ex)
        {
            
        }

    }
    
    public void saveToStream(java.io.FileOutputStream fos)
    {
        try
        {
            java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);                
            
            dos.writeInt(this.modus);
            
            dos.writeInt(color1.getRed());
            dos.writeInt(color1.getGreen());
            dos.writeInt(color1.getBlue());
            dos.writeInt(color1Transparency);
            
            dos.writeInt(color2.getRed());
            dos.writeInt(color2.getGreen());
            dos.writeInt(color2.getBlue());
            dos.writeInt(color2Transparency);

            dos.writeInt(p1.x);
            dos.writeInt(p1.y);
            
            dos.writeInt(p2.x);
            dos.writeInt(p2.y);
            
            dos.writeBoolean(this.wiederholung);
        
        } catch(Exception ex)
        {
           System.err.println("Fehler in VSColorAdvanced.saveToStream() : "+ex.toString());
        }                        
    }
    
    public void loadFromXML(String name,org.w3c.dom.Element nodeElement)
    {
     
    }

    public void saveToXML(String name, org.w3c.dom.Element nodeElement)    
    {        
     
    }
    
}
