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

import VisualLogic.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.swing.JList;


public class VS1DString extends VSObject
{
    private String value[]=null;

    
    public int getLength()
    {
        return value.length;
    }        
    
    public void resize(int newLength)
    {
        value=new String[newLength];
    }        
    
    public void setValues(String[] values)
    {
        this.value=new String[values.length];
        
        for (int i=0;i<values.length;i++)
        {
          this.value[i]=values[i];
        }
        
    }    
    public void setValue(int x, String value)
    {
        this.value[x]=value;
        setChanged(true);
    }
    
    public String getValue(int x)
    {
        return value[x];
    }

    public String[] getValues()
    {
        return value;
    }            
    
    public VS1DString(int size) 
    {
        value=new String[size];
    }
    
    public void copyReferenceFrom(Object in)
    {
      if (in!=null)
      {
        VS1DString temp =(VS1DString)in;
        value=temp.value;
      } else 
      {
          value=new String[0];
      }      
    }               
    public void copyValueFrom(Object in)
    {
      if (in!=null)
      {
        VS1DString temp =(VS1DString)in;
        value=temp.value.clone();
        setChanged(temp.isChanged());
      } else value=new String[0];
    }           
    
    
    public void loadFromStream(java.io.FileInputStream fis) 
    {
        try
        {
        DataInputStream dis = new DataInputStream(fis);

        int size=dis.readInt();
        value=new String[size];

        for (int i=0;i<value.length;i++)
        {                        
          value[i]=dis.readUTF();
        }
        } catch(Exception ex)
        {
           System.err.println("Fehler in VS1DString.loadFromStream() : "+ex.toString());  
        }
                    
    }
    
    public void saveToStream(java.io.FileOutputStream fos)
    {
        try
        {            
            DataOutputStream dos = new DataOutputStream(fos);                
            dos.writeInt(value.length);
        
            for (int i=0;i<value.length;i++)
            {
              String str=value[i];
              dos.writeUTF(str);          
            }        
        } catch(Exception ex)
        {
            System.err.println("Fehler in VS1DString.saveToStream() : "+ex); 
        }                        
    }
    
    
    public void loadFromXML(String name,org.w3c.dom.Element nodeElement)
    {
        try
        {
            int count=Integer.parseInt(nodeElement.getAttribute("VS1DStringCount"+name));
            value=new String[count];

            for (int i=0;i<value.length;i++)
            {          
              String str=nodeElement.getAttribute("VS1DString"+name+""+i);
              value[i]=str;
            }
        } catch(Exception ex)
        {
            
        }                
    }

    public void saveToXML(String name, org.w3c.dom.Element nodeElement)    
    {             
        nodeElement.setAttribute("VS1DStringCount"+name, ""+value.length);
        for (int i=0;i<value.length;i++)
        {
          String str=value[i];
          nodeElement.setAttribute("VS1DString"+name+""+i, str);
        }
    }    
    
}
