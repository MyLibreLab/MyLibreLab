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

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class VS1DBoolean extends VSObject
{
    private boolean value[]=null;
    
    
    public int getLength()
    {
        return value.length;
    }
    public void setValue(int x, boolean value)
    {
        this.value[x]=value;
        setChanged(true);
        
    }
    
    public boolean getValue(int x)
    {
        return value[x];
    }
    
    public VS1DBoolean(int size)
    {
        value=new boolean[size];
    }
    public boolean[] getValues()
    {
        return value;
    }
    
    public void setValues(boolean[] values)
    {
        this.value=new boolean[values.length];
        
        for (int i=0;i<values.length;i++)
        {
            this.value[i]=values[i];
        }
        
    }
    
    public void copyReferenceFrom(Object in)
    {
        if (in!=null)
        {
            VS1DBoolean temp =(VS1DBoolean)in;
            value=temp.value;
        }
        else
        {
            value=new boolean[0];
        }
    }
    
    public void copyValueFrom(Object in)
    {
        if (in!=null)
        {
            VS1DBoolean temp =(VS1DBoolean)in;
            value=temp.value.clone();
            setChanged(temp.isChanged());
        }
        else value=new boolean[0];
    }
    
    public void loadFromStream(java.io.FileInputStream fis)
    {
        try
        {
            DataInputStream dis = new DataInputStream(fis);
            
            int size=dis.readInt();
            value=new boolean [size];
            
            for (int i=0;i<value.length;i++)
            {
                value[i]=dis.readBoolean();
            }
        }
        catch(Exception ex)
        {
            System.err.println("Fehler in VS1DBoolean.loadFromStream() : "+ex.toString());
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
                boolean  val=value[i];
                dos.writeBoolean(val);
            }
        }
        catch(Exception ex)
        {
            System.err.println("Fehler in VS1DBoolean.saveToStream() : "+ex.toString());
        }
    }
    
}
