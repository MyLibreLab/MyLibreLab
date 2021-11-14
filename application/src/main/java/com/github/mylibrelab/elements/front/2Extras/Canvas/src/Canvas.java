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

public class Canvas extends JVSMain
{
  private ExternalIF panelElement;
  private Image image;
  private boolean oldValue;
  private int pinsCount =1;
  
  private VSObject in[];
  
  public void paint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }

  public void onDispose()
  {
   if (image!=null)
   {
     image.flush();
     image=null;
   }
  }

  public void init()
  {
    element.jSetInnerBorderVisibility(true);
    initPins(0,0,0,pinsCount);
    setSize(40,20+pinsCount*10);
    initPinVisibility(false,false,false,true);
    
    for (int i=0;i<pinsCount;i++)
    {
      setPin(i,ExternalIF.C_GROUP,element.PIN_INPUT);
    }


    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    setName("Canvas Render Engine 1.0");
    
    element.jSetResizable(false);
  }
  
  public void initInputPins()
  {

    in = new VSObject[pinsCount];
    for (int i=0;i<pinsCount;i++)
    {
      in[i]=(VSObject)element.getPinInputReference(i);
      if (in[i]==null)
      {
       in[i]= new VSCanvas();
      }
      
    }
  }

  public void initOutputPins()
  {

  }

  public void start()
  {
    panelElement=element.getPanelElement();
  }
  
  public void process()
  {
    if (in[0] instanceof VSGroup)
    {
      if (in[0]!=null)
      {
        if (panelElement!=null)
        {
         panelElement.jProcessPanel(0,0.0,in[0]);
         panelElement.jRepaint();
         
        }
      }
    }
  }
  

}
 
