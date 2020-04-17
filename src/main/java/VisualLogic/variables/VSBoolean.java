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

public class VSBoolean extends VSObject
{
    private boolean value;

    public VSBoolean()  {}
    public VSBoolean(boolean value)  {this.value=value;}
    
    public String toString()
    {
        return ""+value;
    }
    
    public void setValue(boolean value)
    {
        //if (this.value!=value)
        {
          this.value=value;
          setChanged(true);  
          
        }
    }
    
    public boolean getValue()
    {
        return value;
    }
    
    public void copyValueFrom(Object in)
    {
        if (in!=null)
        {
          VSBoolean temp =(VSBoolean)in;
          value=temp.value;      
          setChanged(temp.isChanged());
        } else value=false;
    }   
    
    public void copyReferenceFrom(Object in)
    {
      copyValueFrom(in);
    }    
    
    
    public boolean equals(VSObject obj)
    {
        VSBoolean temp =(VSBoolean)obj;
        if (temp.value==value)  return true; else return false;        
    }    
        
    public boolean isBigger(VSObject obj)
    {
        VSBoolean temp =(VSBoolean)obj;
        if (value==true && temp.value==false)  return true; else return false;
    }

    public boolean isSmaller(VSObject obj)
    {
        VSBoolean temp =(VSBoolean)obj;
        if (value==false && temp.value==true)  return true; else return false;
    }           
    
    
    public void loadFromStream(java.io.FileInputStream fis) 
    {
        try
        {
        java.io.DataInputStream dis = new java.io.DataInputStream(fis);

        value=dis.readBoolean();
        } catch(Exception ex)
        {
            
        }
        
                      
    }
    
    public void saveToStream(java.io.FileOutputStream fos)
    {
        try
        {
            java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);                
            dos.writeBoolean(value);
        
        } catch(Exception ex)
        {
           //System.err.println("Fehler in VSBoolean.saveToStream() : "+ex.toString());
        }                        
    }
        
   public void loadFromXML(String name,org.w3c.dom.Element nodeElement)
    {
       try
       {
        value=Boolean.parseBoolean(nodeElement.getAttribute("VSBoolean"+name));
       } catch(Exception ex)
       {
           
       }
    }

    public void saveToXML(String name, org.w3c.dom.Element nodeElement)    
    {        
        nodeElement.setAttribute("VSBoolean"+name, ""+value);
    }    
    
}

