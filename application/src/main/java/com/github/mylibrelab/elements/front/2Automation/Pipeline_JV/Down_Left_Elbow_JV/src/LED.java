//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia (cswi@gmx.de)                         *
//* Copyright (C) 2018  Javier Velasquez (javiervelasquez125@gmail.com)                                                                            *
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
import tools.*;

public class LED extends JVSMain
{
  private ExternalIF panelElement;
  private Image image;
  private boolean oldValue;
  private boolean oldValue2;
  private boolean firstTime=false;
  private boolean firstTime2=false;
  
  private VSObject in;
  private VSObject in2;
  
  public void paint(java.awt.Graphics g)
  {
    if (image!=null)drawImageCentred(g,image);
  }

  public void onDispose()
  {
   image.flush();
   image=null;
  }


  public void init()
  {
    element.jSetInnerBorderVisibility(true);
    initPins(0,0,0,2);
    setSize(50,45);
    initPinVisibility(false,false,false,true);
    
    setPin(0,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(1,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    
    element.jSetPinDescription(0, "State In");
    element.jSetPinDescription(1, "Overlay In");

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    setName("Down_Left_Elbow_JV");
    
    element.jSetResizable(false);
  }
  
  public void initInputPins()
  {
    in=(VSObject)element.getPinInputReference(0);
    in2=(VSObject)element.getPinInputReference(1);
    if(in==null) in= new VSBoolean(false);
    if(in2==null) in2= new VSBoolean(false);
            
  }

  public void initOutputPins()
  {

  }

  public void start()
  {
    firstTime=true;
    firstTime2=true;
    panelElement=element.getPanelElement();
    panelElement.jProcessPanel(0,0.0,(Object)this);
    panelElement.jProcessPanel(1,0.0,(Object)this);
  }
  
  public void process()
  {
    if (in instanceof VSBoolean)
    {
      VSBoolean inX=(VSBoolean)in;
      
      if ((in!=null) && ((inX.getValue()!=oldValue) || (firstTime)))
      {
        firstTime=false;
        if (inX.getValue())
        {
          if (panelElement!=null) panelElement.jProcessPanel(0,1.0,(Object)this);
        } else
        {
          if (panelElement!=null) panelElement.jProcessPanel(0,0.0,(Object)this);
        }
        panelElement.jRepaint();
        oldValue=inX.getValue();
      }
    }
    if (in2 instanceof VSBoolean)
    {
      VSBoolean inX=(VSBoolean)in2;
      
      if ((in2!=null) && ((inX.getValue()!=oldValue2) || (firstTime2)))
      {
        firstTime2=false;
        if (inX.getValue())
        {
          if (panelElement!=null) panelElement.jProcessPanel(1,1.0,(Object)this);
        } else
        {
          if (panelElement!=null) panelElement.jProcessPanel(1,0.0,(Object)this);
        }
        panelElement.jRepaint();
        oldValue2=inX.getValue();
      }
    }
  }
  

}
 
