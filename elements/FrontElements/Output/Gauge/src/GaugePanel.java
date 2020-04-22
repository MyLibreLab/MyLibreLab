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
import tools.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;


public class GaugePanel extends CustomAnalogComp implements PanelIF
{

  // aus PanelIF
  public void processPanel(int pinIndex, double value, Object obj)
  {
    setValue(value);
    //System.out.println("value1 = "+value);
  }
  
  public void init()
  {
    super.init();
    backColor.setValue(Color.WHITE);
    showBackground.setValue(true);
    textInside.setValue(true);
    element.jSetMinimumSize(50,50);
    knobSizeInProzent.setValue(3);
    nibbleLenInProzent.setValue(45);
    nibbleColor.setValue(Color.RED);
    buttonColor.setValue(Color.BLACK);
  }


  public void mousePressed(MouseEvent e)
  {

  }
  public void mouseReleased(MouseEvent e)
  {

  }
  public void mouseMoved(MouseEvent e)
  {
  }

  public void mouseDragged(MouseEvent e)
  {

  }

}

