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


package Peditor;

import VisualLogic.Element;
import VisualLogic.ElementProperty;
import VisualLogic.VMObject;

/**
 *
 * @author Carmelo
 */
public class BasisProperty {
    
    public int elementID=-1;
    public int propertyIndex=-1;
    public VMObject vmobject=null;
    
    /** Creates a new instance of BasisProperty */
    public BasisProperty(VMObject vmobject, int elementID, int propertyIndex) 
    {
        this.vmobject=vmobject;
        this.elementID=elementID;
        this.propertyIndex=propertyIndex;
    }

    public String toString()
    {
        Element element = vmobject.getElementWithID(elementID);
                
        ElementProperty elProp=(ElementProperty)element.propertyList.get(propertyIndex);
                    
        //jAddPEItem(elProp.label,elProp.referenz,elProp.min,elProp.max);
                
        return "elementID="+element.toString()+ ", property="+elProp.label;
    }
}
