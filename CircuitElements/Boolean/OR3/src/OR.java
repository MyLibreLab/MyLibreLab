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
import java.awt.geom.Rectangle2D;

import tools.*;

public class OR extends Gatter3
{
  private boolean oldValue;

  public OR()
  {
    super(2,"OR");
  }

  
  public void start()
  {
    super.start();
    oldValue=false;
    out.setValue(false);
    element.notifyPin(0);
  }


  public void process()
  {
  
    boolean last=false;
    int c=0;

    for (int i=0;i<in.length;i++)
    {
      if (in[i]!=null)
      {
        last=in[i].getValue();
        if (last==true)
        {
          c++;
        }
       }
    }
    
    boolean value=false ;

    if (c>0)
    {
      value=true;
    } else
    {
      value=false;
    }

    if (value!=oldValue)
    {
      oldValue=value;
      started=true;
      time1 = System.nanoTime();
    }
  }
  

  public void processClock()
  {
    if (started)
    {
      time2 = System.nanoTime();
      long diff=time2-time1;
      if (diff>=delay.getValue()*1000)
      {
        started=false;
        out.setValue(oldValue);
        element.notifyPin(0);
      }
    }
  }

}
 
