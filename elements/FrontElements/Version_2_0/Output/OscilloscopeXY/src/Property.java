//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia (cswi@gmx.de)                         *
//*                                                                           *
//* This library is free software; you can redistribute it and/or modify      *
//* it under the terms of the GNU Lesser General Public License as published  *
//* by the Free Software Foundation; either version 2.1 of the License,       *
//* or (at your option) any later version.                                    *
//* http://www.gnu.org/licenses/lgpl.html                                     *
//*                                                                           *
//* This library is distributed in the hope that it will be useful,           *
//* but WITHOUTANY WARRANTY; without even the implied warranty of             *
//* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                      *
//* See the GNU Lesser General Public License for more details.               *
//*                                                                           *
//* You should have received a copy of the GNU Lesser General Public License  *
//* along with this library; if not, write to the Free Software Foundation,   *
//* Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA                  *
//*****************************************************************************

 



import java.util.ArrayList;
import VisualLogic.*;
import VisualLogic.variables.*;

/**
 *
 * @author Carmelo
 */
public class Property
{
    public String name;
    public double min;
    public double max;
    
    public VSObject vsProperty;
    
    private ArrayList list = new ArrayList();
    
    public void addLocation(String language, String text)
    {
        list.add(new PropertyLanguage(language,text));
    }
    
    public String getLocation(String language)
    {
        PropertyLanguage prop;
        for (int i=0;i<list.size();i++)
        {
            prop=(PropertyLanguage)list.get(i);
            if (prop.language.equalsIgnoreCase(language))
            {
                return prop.text;                
            }
        }

        return "";
    }
    
    
}
