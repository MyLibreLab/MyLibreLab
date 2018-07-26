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

/**
 *
 * @author Homer
 */
public class ElementProperty
{
    public String label;
    public Object referenz;
    public double min;
    public double max;
    public boolean editable=true; //editable by Default
    
    /** Creates a new instance of ElementProperty */
    public ElementProperty(String label,Object referenz, double min, double max)
    {
        this.label=label;
        this.referenz=referenz;
        this.min=min;
        this.max=max;
    }
    public ElementProperty(String label,Object referenz, double min, double max, boolean editable)
    {
        this.label=label;
        this.referenz=referenz;
        this.min=min;
        this.max=max;
        this.editable=editable;
    }
    public void setEditableVar(boolean editableIn){
        this.editable=editableIn;
    }
    
}
