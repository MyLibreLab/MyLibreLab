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


public class ReglerPanel extends CustomAnalogComp
{
  public void init()
  {
    super.init();
    knobSizeInProzent.setValue(37);
    nibbleLenInProzent.setValue(30);
    nibbleCircleSizeInProzent.setValue(6);
    showBackground.setValue(false);
    backColor.setValue(new Color(204,204,204));
    nibbleColor.setValue(new Color(253,153,0));
    showNibbleAsCircle.setValue(true);
    font.setValue(new Font("Dialog", Font.BOLD, 11));
    //buttonColor.setValue(new Color(102,102,102));
    lineColor.setValue(new Color(255,242,181));
    
    setSize(140,140);
  }

}


