package com.github.mylibrelab.elements.front.Input.Regler2.src;//*****************************************************************************
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

import tools.*;


public class ReglerPanel extends CustomAnalogComp
{
  public void init()
  {
    super.init();
    knobSizeInProzent.setValue(36);
    nibbleLenInProzent.setValue(36);
    showBackground.setValue(false);

    setSize(80,80);
  }

 /* public void propertyChanged(Object o)
  {
    super.propertyChanged(o);
  }

  public void setPropertyEditor()
  {
    super.setPropertyEditor();
  }*/
}


