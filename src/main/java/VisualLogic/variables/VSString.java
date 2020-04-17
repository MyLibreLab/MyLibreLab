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

public class VSString extends VSObject
{
    private String value="";
    public VSString()
    {
    }
    
    @Override
    public String toString()
    {
        return value;
    }
    
    
    public void setValue(String value)
    {
        // if (this.value!=value)
        {
            this.value=value;
            setChanged(true);
        }
    }
    
    public String getValue()
    {
        return value;
    }
    public VSString(String value)
    {
        this.value=value;
    }
    
    @Override
    public void copyValueFrom(Object in)
    {
        if (in!=null)
        {
            VSString temp =(VSString)in;
            value=temp.value;
            setChanged(temp.isChanged());
        }
        else value="";
    }
    @Override
    public void copyReferenceFrom(Object in)
    {
      copyValueFrom(in);
    }    
        
    
    @Override
    public boolean equals(VSObject obj)
    {
        VSString temp =(VSString)obj;
        if (temp.value.equals(value))  return true;
        else return false;
    }
    
    @Override
    public boolean isBigger(VSObject obj)
    {
        VSString temp =(VSString)obj;
        if (value.length()>temp.value.length())  return true;
        else return false;
    }
    
    @Override
    public boolean isSmaller(VSObject obj)
    {
        VSString temp =(VSString)obj;
        if (value.length()<temp.value.length())  return true;
        else return false;
    }
    
    
    @Override
    public void loadFromStream(java.io.FileInputStream fis)
    {
        try
        {
            java.io.DataInputStream dis = new java.io.DataInputStream(fis);
            
            value=dis.readUTF();
        }
        catch(Exception ex)
        {
            
        }
        
    }
    
    @Override
    public void saveToStream(java.io.FileOutputStream fos)
    {
        try
        {
            java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);
            dos.writeUTF(value);
            
        }
        catch(Exception ex)
        {
           System.err.println("Fehler in VSDouble.saveToStream() : "+ex.toString());
        }
    }
    public void loadFromXML(String name,org.w3c.dom.Element nodeElement)
    {
        value=nodeElement.getAttribute("VSString"+name);
    }
    
    public void saveToXML(String name, org.w3c.dom.Element nodeElement)
    {
        nodeElement.setAttribute("VSString"+name, value);
    }
}
