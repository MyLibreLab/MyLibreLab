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

import java.util.ArrayList;

public class VSComboBox  extends VSObject
{
    private ArrayList liste=new ArrayList();
    public int selectedIndex=0;
    
    public VSComboBox() { }    
    
    public void clear()
    {
        liste.clear();
    }
    
    public void addItem(String value)
    {
        liste.add(value);
    }
        
    public int getSize()
    {
        return liste.size();        
    }
    
    public String getItem(int index)
    {
        return (String)liste.get(index);
    }    
    
    public void copyReferenceFrom(Object in)
    {
      copyValueFrom(in);
    }    
    
    public void copyValueFrom(Object in)
    {
    }        
    
    
    public void loadFromStream(java.io.FileInputStream fis) 
    {
        try
        {
        java.io.DataInputStream dis = new java.io.DataInputStream(fis);

        selectedIndex=dis.readInt();            
        } catch(Exception ex)
        {
            
        }

    }
    
    public void saveToStream(java.io.FileOutputStream fos)
    {
        try
        {
            java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);                
            dos.writeInt(selectedIndex);
        
        } catch(Exception ex)
        {
           System.err.println("Fehler in VSComboBox.saveToStream() : "+ex.toString());
        }                        
    }    
    
    
   public void loadFromXML(String name,org.w3c.dom.Element nodeElement)
   {
        try
        {
            selectedIndex=Integer.parseInt(nodeElement.getAttribute("VSComboBoxSelectedIndex"+name));
            
            /*int count=Integer.parseInt(nodeElement.getAttribute("VSComboBoxCount"+name));
            
            clear();
            
            for (int i=0;i<count;i++)
            {          
              String str=nodeElement.getAttribute("VSComboBox"+name+""+i);
              addItem(str);
            }*/
        } catch(Exception ex)
        {
            
        }
   }

    public void saveToXML(String name, org.w3c.dom.Element nodeElement)    
    {   
        nodeElement.setAttribute("VSComboBoxSelectedIndex"+name, ""+selectedIndex);
        
       /* nodeElement.setAttribute("VSComboBoxCount"+name, ""+liste.size());
        
        for (int i=0;i<liste.size();i++)
        {
          String str=getItem(i);
          nodeElement.setAttribute("VSComboBox"+name+""+i, str);
        }*/        
    }        
}
