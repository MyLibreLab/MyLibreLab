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

import javax.swing.JPanel;
public class SubElement 
{    
    
    public ElementIF classRef;
    public int x=0;    
    public int y=0;
    public int width=20;
    public int height=15;
    public boolean visible=true;
    
    public SubElement()
    {
        x=0; y=0; width=20; height=15;
        classRef=null;
    }
}
