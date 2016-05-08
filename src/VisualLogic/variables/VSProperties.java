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


public class VSProperties extends VSObject
{
        
    public VSProperties() { }   
    

    public void setValue(int value)
    {
  
    }
    
    public int getValue()
    {
        return 0;
    }    
    
    public void copyValueFrom(Object in)
    {
    }    
    
    public void copyReferenceFrom(Object in)
    {
      copyValueFrom(in);
    }    
        
    
    public boolean equals(VSObject obj)
    {     
        return false;
    }
    
    public boolean isBigger(VSObject obj)
    {     
        return false;
    }

    public boolean isSmaller(VSObject obj)
    {
        return false;
    }            

    
    public void loadFromStream(java.io.FileInputStream fis) 
    {
        /*try
        {
            java.io.DataInputStream dis = new java.io.DataInputStream(fis);
                        
            value=dis.readInt();
            
        } catch(Exception ex)
        {
           System.err.println("Fehler in VSDouble.loadFromStream() : "+ex.toString());
        } */                       
    }
    
    public void saveToStream(java.io.FileOutputStream fos)
    {
       /* try
        {
            java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);                
            dos.writeInt(value);
        
        } catch(Exception ex)
        {
           System.err.println("Fehler in VSDouble.saveToStream() : "+ex.toString());
        }*/                        
    }        
    public void loadFromXML(String name,org.w3c.dom.Element nodeElement)
    {
       /* try
        {
          value=Integer.parseInt(nodeElement.getAttribute("VSInteger"+name));
        } catch(Exception ex)
        {
            
        }*/
    }

    public void saveToXML(String name, org.w3c.dom.Element nodeElement)    
    {        
       // nodeElement.setAttribute("VSInteger"+name, ""+value);
    }
}
