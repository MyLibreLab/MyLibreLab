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

public class VS2DDouble extends VSObject
{    
    private double value[][] =null;  
    private int columns=0;
    private int rows=0;
   
    
    public void setValues(double[][] values, int n, int m)
    {
        columns=n;
        rows=m;
        value=values;
    }

   public double[][] getValues()
   {
       return value;
   }
   public int getRows()
   {
       return rows;
   }
   
   public int getColumns()
   {
       return columns;
   }
   
    public int getLength()
    {
        return value.length;
    }  
    public void setValue(int x,int y, double value)
    {
        this.value[x][y]=value;
        setChanged(true);
    }
    
    public double getValue(int x, int y)
    {
        return value[x][y];
    }
  
  public VS2DDouble(int columns, int rows) 
  {
        this.columns=columns;
        this.rows=rows;        
        value=new double[columns][rows];
  }
  
  
    public double[][] copyFrom(double[][] value, int cols, int rows)
    {
        double[][] temp = new double[cols][rows] ;
        int j,i;
        for (i=0;i<cols;i++)
        {
            for (j=0;j<rows;j++)
            {
                temp[i][j]=value[i][j];                
            }            
        }
        
        return temp;
    }
    
    public void copyValueFrom(Object in)
    {
        if (in!=null)
        {          
          VS2DDouble temp =(VS2DDouble)in;
          this.columns=temp.columns;
          this.rows=temp.rows;
          
          //value=temp.value.clone();
          value=copyFrom(temp.value,temp.columns,temp.rows);
          setChanged(temp.isChanged());
        } else 
        {
          value=new double[0][0];
          this.columns=0;
          this.rows=0;
        }
    }            
    
    public void copyReferenceFrom(Object in)
    {
        if (in!=null)
        {          
          VS2DDouble temp =(VS2DDouble)in;
          this.columns=temp.columns;
          this.rows=temp.rows;
          
          value=temp.value;          
          setChanged(temp.isChanged());
        } else 
        {
          value=new double[0][0];
          this.columns=0;
          this.rows=0;
        }
    }            
    

    
    public void saveToStream(java.io.FileOutputStream fos)
    {
        try
        {
            java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);
            dos.writeInt(columns);
            dos.writeInt(rows);
        
            for (int i=0;i<columns;i++)
            {
                for (int j=0;j<rows;j++)
                {
                    dos.writeDouble(value[i][j]);
                }                
            }
        } catch(Exception ex)
        {
           System.err.println("Fehler in VS2DDouble.saveToStream() : "+ex.toString());
        }                        
    }        
    
    public void loadFromStream(java.io.FileInputStream fis) 
    {
        try
        {
            java.io.DataInputStream dis = new java.io.DataInputStream(fis);
                        
            columns=dis.readInt();
            rows=dis.readInt();
            
            value=new double[columns][rows];
            for (int i=0;i<columns;i++)
            {
                for (int j=0;j<rows;j++)
                {
                    value[i][j]=dis.readDouble();
                }                
            }
            
        } catch(Exception ex)
        {
           System.err.println("Fehler in VS2DDouble.loadFromStream() : "+ex.toString());
        }
            
    }    
}
