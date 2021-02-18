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


public class TEST extends JVSMain
{

  public void init()
  {
    initPins(5,5,5,5);
    setSize(100,100);
    element.jSetPinDescription(0,"AAAAAA");
    element.jSetPinDescription(1,"BBBBB");
    element.jSetPinDescription(2,"CCCC");
    element.jSetPinDescription(3,"DD");
    element.jSetPinDescription(4,"E");
    
    
    element.jSetPinDescription(5,"FFF");
    element.jSetPinDescription(6,"GGG");
    element.jSetPinDescription(7,"HHH");
    element.jSetPinDescription(8,"III");
    element.jSetPinDescription(9,"JJJ");
    
    element.jSetPinDescription(10,"KKK");
    element.jSetPinDescription(11,"LLL");
    element.jSetPinDescription(12,"MMM");
    element.jSetPinDescription(13,"NNN");
    element.jSetPinDescription(14,"OOO");
    
    element.jSetPinDescription(15,"PPP");
    element.jSetPinDescription(16,"QQQ");
    element.jSetPinDescription(17,"RRR");
    element.jSetPinDescription(18,"SSS");
    element.jSetPinDescription(19,"TTT");
  }

}



