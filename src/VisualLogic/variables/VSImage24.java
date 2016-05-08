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


/**
 *
 * @author Homer
 */
public class VSImage24  extends VSObject
{    
    private int value[]=null;   
    private int width=0;
    private int height=0;
        
    public VSImage24(int width, int height) 
    {        
        this.width=width;
        this.height=height;
                
        value=new int[width*height];
    }
    
    public void resize(int width, int height) 
    {        
        this.width=width;
        this.height=height;
        
        value=new int[width*height];
    }    
    
    public int[] getPixels()
    {
        return value;
    }
    public void setPixels(int[] pixels, int w, int h)
    {
        value=pixels;
        width=w;
        height=h;        
    }
    public int getWidth()
    {
        return width;
    }
    public int getHeight()
    {
        return height;
    }
    
    
    
    public int getLength()
    {
        return value.length;
    }    
    
    public void setValue(int x, int value)
    {
        this.value[x]=value;
        setChanged(true);
    }
    
    public int getValue(int x)
    {
        return value[x];
    }

    public void copyReferenceFrom(Object in)
    {
      copyValueFrom(in);
    }    
        
    
    public void copyValueFrom(Object in)
    {
      if (in!=null)
      {
        VSImage24 temp =(VSImage24)in;
        value=new int[temp.getPixels().length];
        
        for (int i=0;i<temp.getPixels().length;i++) value[i]=temp.getPixels()[i];
        
        width=temp.getWidth();
        height=temp.getHeight();
        setChanged(temp.isChanged());
      } else 
      {
          value=new int[0];
          width=0;
          height=0;
      }
    }               
}
