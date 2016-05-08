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



public class VS1DByte extends VSObject
{
    private byte value[]=null;
    
    
    public VS1DByte(int size)
    {
        value=new byte[size];
    }
    
    public int getLength()
    {
        return value.length;
    }
    public void setValue(int x, byte value)
    {
        this.value[x]=value;
        setChanged(true);
    }
    
    public void setBytes(byte[] values)
    {
        value=values.clone();
    }
    
    public void setValues(byte[] values)
    {
        this.value=new byte[values.length];
        
        for (int i=0;i<values.length;i++)
        {
            this.value[i]=values[i];
        }
        
    }
    
    public byte getValue(int x)
    {
        return value[x];
    }
    
    public byte[] getBytes()
    {
        return value;
    }
    
    public byte[] getValues()
    {
        return value;
    }
    
    
    public void copyReferenceFrom(Object in)
    {
        if (in!=null)
        {
            VS1DByte temp =(VS1DByte)in;
            value=temp.value;
        }
        else
        {
            value=new byte[0];
        }
    }
    
    public void copyValueFrom(Object in)
    {
        if (in!=null)
        {
            VS1DByte temp =(VS1DByte)in;
            value=temp.value.clone();
            setChanged(temp.isChanged());
        }
        else value=new byte[0];
    }
    
    public void loadFromStream(java.io.FileInputStream fis)
    {
        try
        {
            DataInputStream dis = new DataInputStream(fis);
            
            int size=dis.readInt();
            value=new byte[size];
            
            for (int i=0;i<value.length;i++)
            {
                value[i]=dis.readByte();
            }
        }
        catch(Exception ex)
        {
            System.err.println("Fehler in VS1DByte.loadFromStream() : "+ex.toString());
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
                byte val=value[i];
                dos.writeByte(val);
            }
        }
        catch(Exception ex)
        {
            System.err.println("Fehler in VS1DByte.saveToStream() : "+ex.toString());
        }
    }
}
