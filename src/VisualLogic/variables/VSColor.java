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

import java.awt.*;
import java.io.IOException;

public class VSColor extends VSObject
{
    private Color value=Color.BLACK;

   
    public VSColor(Color color) 
    {
        value=color;
    }
    
    public void setValue(Color value)
    {
       // if (!this.value.equals(value))
        {
          this.value=value;
          setChanged(true);
        }
    }
    
    public Color getValue()
    {
        return value;
    }    
    
    public void copyValueFrom(Object in)
    {
        if (in!=null)
        {        
          VSColor temp =(VSColor)in;
          value=temp.value;
          setChanged(temp.isChanged());
        } else value=Color.BLACK;
    }      
    
    public void copyReferenceFrom(Object in)
    {
      copyValueFrom(in);
    }    
    
    
    
    public void loadFromStream(java.io.FileInputStream fis) 
    {
        try
        {        
            java.io.DataInputStream dis = new java.io.DataInputStream(fis);

            int r=dis.readInt();
            int g=dis.readInt();
            int b=dis.readInt();
            value=new Color(r,g,b);
        } catch(Exception ex)
        {
            
        }

    }
    
    public void saveToStream(java.io.FileOutputStream fos)
    {
        try
        {
            java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);                
            dos.writeInt(value.getRed());
            dos.writeInt(value.getGreen());
            dos.writeInt(value.getBlue());
        
        } catch(Exception ex)
        {
           System.err.println("Fehler in VSColor.saveToStream() : "+ex.toString());
        }                        
    }
    
    public void loadFromXML(String name,org.w3c.dom.Element nodeElement)
    {
        try
        {
            int r=Integer.parseInt(nodeElement.getAttribute("VSColorR"+name));
            int g=Integer.parseInt(nodeElement.getAttribute("VSColorG"+name));
            int b=Integer.parseInt(nodeElement.getAttribute("VSColorB"+name));
            value=new Color(r,g,b);
       } catch(Exception ex)
       {
           
       }
            
    }

    public void saveToXML(String name, org.w3c.dom.Element nodeElement)    
    {        
        nodeElement.setAttribute("VSColorR"+name, ""+value.getRed());
        nodeElement.setAttribute("VSColorG"+name, ""+value.getGreen());
        nodeElement.setAttribute("VSColorB"+name, ""+value.getBlue());
    }
    
}
