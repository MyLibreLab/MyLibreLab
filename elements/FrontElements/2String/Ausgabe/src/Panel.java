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
import VisualLogic.variables.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import tools.*;
import java.awt.geom.Rectangle2D;

public class Panel extends JVSMain implements PanelIF
{
  private int width=150, height=25;

  private JTextField text = new JTextField();



  public void processPanel(int pinIndex, double value, Object obj)
  {
    if (obj instanceof VSString)
    {
      String str=((VSString)obj).getValue();
      text.setText(str);
      element.jRepaint();
    }
  }
  

   public void paint(java.awt.Graphics g)
   {
   }


  public void init()
  {
    initPins(0,0,0,0);
    setSize(width,height);
    element.jSetInnerBorderVisibility(false);
    initPinVisibility(false,false,false,false);
    
    element.jSetResizable(true);

    
    setName("Ausgabe2");
  }
  
  public void xOnInit()
  {
    JPanel panel =element.getFrontPanel();
    panel.setLayout(new java.awt.BorderLayout());

    panel.add(text, java.awt.BorderLayout.CENTER);
    element.setAlwaysOnTop(true);
    text.setEditable(false);
    //Code added to avoid JText Focus Lost Error
    //text.setEnabled(false);
    text.setFocusable(false);
    
  }

  public void stop()
  {
    text.setText("");
  }


}

