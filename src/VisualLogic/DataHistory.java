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

package VisualLogic;

import java.util.ArrayList;

/**
 *
 * @author Carmelo
 */
public class DataHistory
{    
    private ArrayList<DataEntry> list = new ArrayList<DataEntry>();
            
    /** Creates a new instance of DataHistory */
    public DataHistory()
    {
    }
    
    public boolean entryExist(String name)
    {
        if (getEntry(name)==null)
        {
            return false;
        }else
        {
            return true;
        }
    }
    
    public DataEntry addEntry(String name)
    {
        DataEntry entry = new DataEntry(name);
        list.add(entry);
        
        return entry;
    }
        
    public void clearEntries()
    {
        list.clear();
    }
    
    public DataEntry getEntry(String name)
    {
        name=name.trim();
        
        DataEntry entry;
        for (int i=0;i<list.size();i++)
        {
            entry=list.get(i);
            if (entry.name.trim().equalsIgnoreCase(name))
            {
                return entry;
            }
        }        
        return null;        
    }
    
    public int size()
    {
        return list.size();
    }
    public DataEntry getEntry(int index)
    {
        return list.get(index);   
    }
        
}
