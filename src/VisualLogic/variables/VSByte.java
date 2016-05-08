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


public class VSByte extends VSObject
{
    private byte value;
    
    public VSByte()
    { }
    
    public VSByte(byte value)
    {
        this.value=value;
    }
    
    public String toString()
    {
        return ""+value;
    }
    
    public void setValue(byte value)
    {
        //if (this.value!=value)
        {
            this.value=value;
            setChanged(true);
        }
    }
    
    public byte getValue()
    {
        return value;
    }
    
    
    public static byte toUnsigned(final short value)
    {
        return (byte) (0xFF&value);
    }
    
    /**
     * Converts an unsigned byte to a signed short.
     * @param value an unsigned byte value
     * @return a signed short that represents the unsigned byte's value.
     */
    public static short toSigned( byte value)
    {
        return (short)copyBits(value, (byte)8);
    }
    
    /**
     * Returns a long that contains the same n bits as the given long,with cleared upper rest.
     * @param value the value which lowest bits should be copied.
     * @param bits the number of lowest bits that should be copied.
     * @return a long value that shares the same low bits as the given value.
     */
    private static long copyBits(final long value, byte bits)
    {
        final boolean logging = false; //turn off logging here
        long converted = 0;
        long comp =1L << bits;
        while (--bits != -1)
        {
            if(((comp >>= 1) & value) != 0)
            {
                converted |= comp;
            }
            if(logging)
            {System.out.print((comp & value)!=0?"1":"0");}
        }
        if(logging)
        {System.out.println();
        }
        return converted;
    }
    
    
    public void copyValueFrom(Object in)
    {
        if (in!=null)
        {
            VSByte temp =(VSByte)in;
            value=temp.value;
            setChanged(temp.isChanged());
        }
        else value=0;
    }
    
    public void copyReferenceFrom(Object in)
    {
      copyValueFrom(in);
    }    
    
    public boolean equals(VSObject obj)
    {
        VSByte temp =(VSByte)obj;
        if (temp.value==value)  return true;
        else return false;
    }
    
    public boolean isBigger(VSObject obj)
    {
        VSByte temp =(VSByte)obj;
        if (value>temp.value)  return true;
        else return false;
    }
    
    public boolean isSmaller(VSObject obj)
    {
        VSByte temp =(VSByte)obj;
        if (value<temp.value)  return true;
        else return false;
    }
    
    
    public void loadFromStream(java.io.FileInputStream fis)
    {
        try
        {
            java.io.DataInputStream dis = new java.io.DataInputStream(fis);
            
            value=dis.readByte();
        }
        catch(Exception ex)
        {
            
        }
        
    }
    
    public void saveToStream(java.io.FileOutputStream fos)
    {
        try
        {
            java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);
            dos.writeByte(value);
            
        }
        catch(Exception ex)
        {
            VisualLogic.Tools.showMessage("Fehler in VSByte.saveToStream() : "+ex.toString());
        }
    }
    public void loadFromXML(String name,org.w3c.dom.Element nodeElement)
    {
        try
        {
            value=Byte.parseByte(nodeElement.getAttribute("VSByte"+name));
        }
        catch(Exception ex)
        {
            
        }
    }
    
    public void saveToXML(String name, org.w3c.dom.Element nodeElement)
    {
        nodeElement.setAttribute("VSByte"+name, ""+value);
    }
}
