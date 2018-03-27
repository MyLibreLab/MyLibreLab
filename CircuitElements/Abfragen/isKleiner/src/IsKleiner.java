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


public class IsKleiner extends JVSMain
{
  private Image image;
  private VSObject inA;
  private VSObject inB;
  private VSBoolean out=new VSBoolean();


  public void onDispose()
  {
   if (image!=null)
   {
     image.flush();
     image=null;
   }
  }

  public void paint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }

  public void init()
  {
    initPins(0,1,0,2);
    setSize(40,30);

    initPinVisibility(false,true,false,true);

    element.jSetInnerBorderVisibility(false);
    image=element.jLoadImage(element.jGetSourcePath()+"icon.png");


    setPin(0,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_VARIANT,element.PIN_INPUT);
    setPin(2,ExternalIF.C_VARIANT,element.PIN_INPUT);

    String strLocale=Locale.getDefault().toString();
    if (strLocale.equalsIgnoreCase("de_DE"))
    {
      element.jSetPinDescription(0,"if (a<b) out=true");
      element.jSetPinDescription(1,"a");
      element.jSetPinDescription(2,"b");
    }
    if (strLocale.equalsIgnoreCase("en_US"))
    {
      element.jSetPinDescription(0,"if (a<b) out=true");
      element.jSetPinDescription(1,"a");
      element.jSetPinDescription(2,"b");
    }
    if (strLocale.equalsIgnoreCase("es_ES"))
    {
      element.jSetPinDescription(0,"si (a<b) salida=cierto");
      element.jSetPinDescription(1,"a");
      element.jSetPinDescription(2,"b");
    }

    setName("isKleiner");
  }



  public void initInputPins()
  {
    inA=(VSObject)element.getPinInputReference(1);
    inB=(VSObject)element.getPinInputReference(2);
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }

  public void process()
  {
    if (inA!=null && inB!=null && out!=null)
    {
      if (inA.isSmaller(inB))
      {
        out.setValue(true);
      } else out.setValue(false);
      element.notifyPin(0);
    }
  }


  public void onChangeElement()
  {
    int dt=element.jGetPinDrahtSourceDataType(1);


    setPin(1,dt,element.PIN_INPUT);
    setPin(2,dt,element.PIN_INPUT);
    element.jRepaint();


  }
}

