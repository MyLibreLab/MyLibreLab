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

import java.io.Serializable;

public class VSObject extends Object implements Serializable
{
    private boolean changed=false;
    private int pinIndex=0;// nur für SocketServer und SocketClient Elemente!
    private int pin=-1;
    
    public void setPin(int index)
    {
        pin=index;
    }
    public int getPin()
    {
        return pin;
    }
    
    /*public void setElement(ExternalIF element)
    {
        this.element=element;
    }
    public ExternalIF getElement()
    {
        return element;
    }*/
    
    
    
    public void setPinIndex(int val)
    {
        pinIndex=val;
    }
    public int getPinIndex()
    {
        return pinIndex;
    }
    
    public boolean isChanged()
    {
        return changed;
    }
    
    public void setChanged(boolean value)
    {                
        changed=value;
    }
    
    public VSObject()
    {
        
    }
    
    
    public void copyReferenceFrom(Object in)
    {
         
    }
    
    public void copyValueFrom(Object in)
    {
        
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
    }
    
    public void saveToStream(java.io.FileOutputStream fos)
    {
        
    }
    
}
