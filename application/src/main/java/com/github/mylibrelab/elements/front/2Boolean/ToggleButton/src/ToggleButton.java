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
import tools.*;


public class ToggleButton extends JVSMain
{
  private Image image;
  private VSBoolean out=new VSBoolean();
  private VSBoolean val;
  private boolean valuegesendet=false;
  private boolean changed=false;

  public void onDispose()
  {
   image.flush();
   image=null;
  }
  
  public void paint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }
  
  
 public void init()
  {
    initPins(0,1,0,0);
    setSize(40,30);
    initPinVisibility(false,true,false,false);
    
    element.jSetRightPinsVisible(true);
    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    element.jInitPins();
    element.jSetPinDataType(0,ExternalIF.C_BOOLEAN);
    element.jSetPinIO(0,element.PIN_OUTPUT);
    setName("ToggleButton");

  }



  public void changePin(int pinIndex, Object value)
  {

    val=(VSBoolean)value;
    if (val!=null )
    {
      out.setChanged(true);
      out.setValue(val.getValue());

      element.notifyPin(0);
    }
  }

  public void initInputPins()
  {
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }


  public void process()
  {
    /*if (out!=null)
    {
      if (changed)
      {

          changed=false;
          if (val!=null) out.setValue(val.getValue());
          valuegesendet=true;
          System.out.println("Taster Info Gesendet");
          out.setChanged(true);
      } else
      {

        if (valuegesendet && val!=null)
        {
          val.setValue(false);
          out.setValue(val.getValue());
          out.setChanged(true);
          valuegesendet=false;
          System.out.println("Taster Info wieder aufgehoben!");
        } else  out.setChanged(false);
      } 
    }  */
  }


}

