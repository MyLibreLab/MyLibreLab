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


public class Circuit extends JVSMain
{
  private Image image;
  private boolean changed=false;
  private VSBoolean in;
  private boolean oldValue=false;
  private ExternalIF panelElement;
  
  public void onDispose()
  {
   image.flush();
   image=null;
  }


  public void paint(java.awt.Graphics g)
  {
    if (image!=null) drawImageCentred(g,image);
  }

  public void init()
  {
    initPins(0,0,0,1);
    setSize(40,30);
    initPinVisibility(false,false,false,true);

    setPin(0,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    
    image=element.jLoadImage(element.jGetSourcePath()+"icon.png");
    setName("UserDefinedBooleanDisplay");
  }
  

  public void initInputPins()
  {
    in=(VSBoolean)element.getPinInputReference(0);
    if (in==null) in=new VSBoolean(false);
  }

  public void initOutputPins()
  {
  }

  public void start()
  {
    panelElement=element.getPanelElement();
    oldValue=!oldValue;
    process();
  }

  public void process()
  {
    if (in.getValue()!=oldValue)
    {
      oldValue=in.getValue();
      
      if (panelElement!=null)
      {
        if (in.getValue()==true)
        {
          panelElement.jProcessPanel(0,1.0,(Object)this);
        } else
        {
          panelElement.jProcessPanel(0,0.0,(Object)this);
        }

      }
    }
  }


}

