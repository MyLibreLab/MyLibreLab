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

public class VSFont extends VSObject
{
    private Font value=new Font("Arial",1,10);

    public VSFont(Font font) 
    {
        value=font;
    }
    
    public void setValue(Font value)
    {
        if (!this.value.equals(value))
        {
          this.value=value;
          setChanged(true);
        }    
    }
    
    public Font getValue()
    {
        return value;
    }    
    
    public void copyValueFrom(Object in)
    {
      if (in!=null)
      {        
        VSFont temp =(VSFont)in;
        value=temp.value;
        setChanged(temp.isChanged());
      } else value= new Font("Arial",1,10);    
    }      
    
    
    public void loadFromStream(java.io.FileInputStream fis) 
    {
        try
        {
            java.io.DataInputStream dis = new java.io.DataInputStream(fis);
                                    
            
            String fnt_Name=dis.readUTF();
            int fnt_Style=dis.readInt();
            int fnt_Size=dis.readInt();            

            value=new Font(fnt_Name, fnt_Style,fnt_Size);         
        } catch(Exception ex)
        {
            
        }
                        
    }

    public void copyReferenceFrom(Object in)
    {
      copyValueFrom(in);
    }    
    
    
    public void saveToStream(java.io.FileOutputStream fos)
    {
        try
        {
            java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);                
            dos.writeUTF(value.getName());
            dos.writeInt(value.getStyle());
            dos.writeInt(value.getSize());        
        } catch(Exception ex)
        {
           System.err.println("Fehler in VSFont.saveToStream() : "+ex.toString());
        }                        
    }            
    
    public void loadFromXML(String name,org.w3c.dom.Element nodeElement)
    {
        try
        {
            String fnt_Name=nodeElement.getAttribute("VSFontName");
            int fnt_Style=Integer.parseInt(nodeElement.getAttribute("VSFontStyle"));
            int fnt_Size=Integer.parseInt(nodeElement.getAttribute("VSFontSize"));
            //int fnt_Color=Integer.parseInt(nodeElement.getAttribute("VSFontColor"));

            value=new Font(fnt_Name, fnt_Style,fnt_Size);         
       } catch(Exception ex)
       {
           
       }
            
    }

    public void saveToXML(String name, org.w3c.dom.Element nodeElement)    
    {   
         nodeElement.setAttribute("VSFontName", value.getName());
         nodeElement.setAttribute("VSFontStyle", ""+value.getStyle());
         nodeElement.setAttribute("VSFontSize", ""+value.getSize());        
    }
    
}
