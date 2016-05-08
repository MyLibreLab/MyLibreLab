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
import VisualLogic.variables.VSBoolean;
import VisualLogic.variables.VSInteger;
import VisualLogic.variables.VSString;
import java.awt.*;
import javax.swing.JPanel;
/**
 *
 * @author Carmelo
 */
public class VisualObject extends JPanel{
    
    /** Creates a new instance of VisualObject */
    public VisualObject() 
    {    
    }

    public VSBoolean vShowCaption = new VSBoolean();
    public VSString vCaption=new VSString();
    public VSBoolean vVisible=new VSBoolean ();
    public VSInteger vLeft=new VSInteger();
    public VSInteger vTop=new VSInteger();
    public VSInteger vWidth=new VSInteger();
    public VSInteger vHeight=new VSInteger();
      
    public void setLeft(int value)
    {        
        setLocation(value,getY());
    }
    
    public void setTop(int value)
    {  
        setLocation(getX(),value);
    }
    
    public int getRealX() {return getX();}
    public int getRealY() {return getY();}
    
    public int getRealWidth() {return getWidth();}
    public int getRealHeight() {return getHeight();}
    
    
    public int getX() {return getLocation().x;}
    public int getY() {return getLocation().y;}
       
}
