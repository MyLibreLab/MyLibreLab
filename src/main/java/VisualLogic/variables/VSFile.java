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

import java.io.IOException;
import java.util.ArrayList;

public class VSFile extends VSObject
{
    private String value;
    private ArrayList extensions = new ArrayList();
    private String description="";

    public VSFile(String fileName) 
    {
        value=fileName;
    }
        
    public void clearExtensions()
    {
        extensions.clear();
    }
    
    public int getExtensionsCount()
    {
        return extensions.size();
    }
    
    public void addExtension(String extension)
    {
        extensions.add(extension);
    }

    public String getExtension(int index)
    {
        return (String)extensions.get(index);
    }

    public void setDescription(String description)
    {
      this.description=description;
    }
    
    public String getDescription()
    {
      return description;    
    }
    
    public void setValue(String value)
    {
        if (this.value!=value)
        {
          this.value=value;
          setChanged(true);
        }    
    }
    
    public String getValue()
    {
        return value;
    }    
    
    public void copyReferenceFrom(Object in)
    {
      copyValueFrom(in);
    }    
    
    public void copyValueFrom(Object in)
    {
      if (in!=null)
      {
        VSFile temp =(VSFile)in;
        value=temp.value;
        setChanged(temp.isChanged());
      } else value="";
    }      
    
    
    public void loadFromStream(java.io.FileInputStream fis) 
    {
        try
        {
        java.io.DataInputStream dis = new java.io.DataInputStream(fis);

        value=dis.readUTF();                                 
        } catch(Exception ex)
        {
            
        }
        
    }
    
    public void saveToStream(java.io.FileOutputStream fos)
    {
        try
        {
            java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);                
            dos.writeUTF(value);
        
        } catch(Exception ex)
        {
           System.err.println("Fehler in VSFile.saveToStream() : "+ex.toString());
        }                        
    }        
    public void loadFromXML(String name,org.w3c.dom.Element nodeElement)
    {
        String temp=nodeElement.getAttribute("VSFileName"+name);
        value=temp;
    }

    public void saveToXML(String name, org.w3c.dom.Element nodeElement)    
    {        
        nodeElement.setAttribute("VSFileName"+name, ""+value);
    }
    
}
