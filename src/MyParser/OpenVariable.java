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
package MyParser;

public class OpenVariable {
   
    
    public String name;
    public int datatype;
    public Object value;
    public boolean global;
    
    public static final int C_DOUBLE =0;
    public static final int C_STRING =1;
    public static final int C_BOOLEAN =2;
    
    @Override
    public String toString()
    {
        String dt = "";
        
        if (datatype==0) dt="Double";
        if (datatype==1) dt="String";
        if (datatype==2) dt="Boolean";
        
        return name+" ("+dt+")";
    }
    public OpenVariable()
    {
        
    }
    
    public OpenVariable(String name, Object value)
    {
        this.name=name;
        this.value=value;
    }
}    
    
