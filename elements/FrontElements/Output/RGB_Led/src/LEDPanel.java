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

import VisualLogic.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import tools.*;

import SVGViewer.*;

public class LEDPanel extends JVSMain implements PanelIF
{
  private Color color=new Color(0,0,0);
  private SVGManager svgManager = new SVGManager();
  
  
  public void processPanel(int pinIndex, double value, Object obj)
  {
    if (obj instanceof Color)
    {
      color= (Color)obj;
      element.jRepaint();
    }
  }

   public void paint(java.awt.Graphics g)
   {
     if (element!=null)
     {
        Rectangle bounds=element.jGetBounds();

        g.setColor(color);
        SVGObject light = svgManager.getSVGObject("light");

        light.setFillColor(color);

        svgManager.paint((Graphics2D)g,(int)bounds.getWidth(),(int)bounds.getHeight());
     }
   }
   
  public void init()
  {
    initPins(0,0,0,0);
    setSize(25,25);
    element.jSetInnerBorderVisibility(false);
    
    initPinVisibility(false,false,false,false);
    
    element.jSetResizeSynchron(true);
    element.jSetResizable(true);
    svgManager.loadFromFile(element.jGetSourcePath()+"led.svg");
    
    setName("RGB-LED");
  }


}
 
