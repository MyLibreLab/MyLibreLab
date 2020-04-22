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
import java.util.*;

public class Length1D extends JVSMain
{
  private Image image;

  private VSObject in;
  private VSInteger out= new VSInteger(0);


  public void xpaint(java.awt.Graphics g)
  {
    if (image!=null) drawImageCentred(g,image);
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
    initPins(0,1,0,1);
    setSize(45+3,36);
    initPinVisibility(false,true,false,true);
    element.jSetInnerBorderVisibility(true);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    setPin(0,ExternalIF.C_INTEGER,element.PIN_OUTPUT);   // Out
    setPin(1,ExternalIF.C_VARIANT,element.PIN_INPUT);   // In
    
    setName("Length1D");
  }


  public void initInputPins()
  {
    in=(VSObject)element.getPinInputReference(1);
  }
  

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }
  
  public void checkPinDataType()
  {
   /* boolean pinIn=element.hasPinWire(1);

    int dt=element.C_VARIANT;

    if (pinIn==true) dt=element.jGetPinDrahtSourceDataType(1);

    element.jSetPinDataType(1,dt);*/
  }


  public void setPropertyEditor()
  {
    
  }


  public void propertyChanged(Object o)
  {
  }

  public void start()
  {
  }



  public void process()
  {
     if (in!=null)
     {
       if (in instanceof VS1DString)
       {
         VS1DString values =(VS1DString)in;
         out.setValue(values.getLength());
         element.notifyPin(0);
       }else
       if (in instanceof VS1DBoolean)
       {
         VS1DBoolean values =(VS1DBoolean)in;
         out.setValue(values.getLength());
         element.notifyPin(0);
       }else
       if (in instanceof VS1DDouble)
       {
         VS1DDouble values =(VS1DDouble)in;
         out.setValue(values.getLength());
         element.notifyPin(0);
       }else
       if (in instanceof VS1DByte)
       {
         VS1DByte values =(VS1DByte)in;
         out.setValue(values.getLength());
         element.notifyPin(0);
       }else
       if (in instanceof VS1DInteger)
       {
         VS1DInteger values =(VS1DInteger)in;
         out.setValue(values.getLength());
         element.notifyPin(0);
       }
     }  else
     {
       out.setValue(0);
     }
  }
  
  
  public void loadFromStream(java.io.FileInputStream fis)
  {
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
  }
}

