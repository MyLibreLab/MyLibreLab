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


public class ReglerPanel extends CustomAnalogComp
{

  private ExternalIF circuitElement;

  // aus PanelIF
  public void processPanel(int pinIndex, double value, Object obj)
  {
    setValue(value);
  }

  public void init()
  {
    super.init();

    nibbleColor.setValue(Color.RED);
    buttonColor.setValue(new Color(200,150,150));
    max.setValue(100);
    showBackground.setValue(false);
    font.setValue(new Font("Dialog", Font.BOLD, 11));
    generateFromNumbersValues();
    generateFromValuesSubElements();
    captions.setText(getTextWithKomma());
    knobSizeInProzent.setValue(37);
    nibbleLenInProzent.setValue(30);
    nibbleCircleSizeInProzent.setValue(6);
    lineColor.setValue(new Color(255,242,181));
    showNibbleAsCircle.setValue(true);
    nibbleColor.setValue(new Color(0,0,255));
    setSize(140,140);
  }


  public void start()
  {
    super.start();
    circuitElement=element.getCircuitElement();
  }



  public void processProc()
  {
      int val=(int)Math.round(value0.getValue());
      if (value0.getValue()>(((double)val)-0.2) && value0.getValue()<((double)val)+0.2)
      {
         value0.setValue(val);
      } else value0.setValue(oldValue);

  }


}

