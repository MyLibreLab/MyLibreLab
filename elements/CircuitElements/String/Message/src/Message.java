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
import javax.swing.*;
import java.util.*;


public class Message extends JVSMain
{
  private Image image;
  private VSString inMessage;
  private String oldValue="";


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
    initPins(0,0,0,1);
    setSize(35+3,25+3);
    initPinVisibility(false,false,false,true);
    element.jSetInnerBorderVisibility(false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.png");

    setPin(0,ExternalIF.C_STRING,element.PIN_INPUT); // calc


    String strLocale=Locale.getDefault().toString();

    if (strLocale.equalsIgnoreCase("de_DE"))
    {
       element.jSetPinDescription(0,"Nachricht");
    }
    if (strLocale.equalsIgnoreCase("en_US"))
    {
       element.jSetPinDescription(0,"message");
    }
    if (strLocale.equalsIgnoreCase("es_ES"))
    {
      element.jSetPinDescription(0,"mensaje ES");
    }


    element.jSetCaptionVisible(true);
    element.jSetCaption("Message");
    setName("Message");
  }

  public void initInputPins()
  {
    inMessage=(VSString)element.getPinInputReference(0);

    if (inMessage==null)
    {
      inMessage=new VSString("");
    }

  }

  public void initOutputPins()
  {
  }

  public void process()
  {
    if (inMessage instanceof VSString)
    {
      if (inMessage.getValue().length()>0 )
      {
        JOptionPane.showMessageDialog(null,inMessage.getValue(),"Message!",JOptionPane.PLAIN_MESSAGE);
      }
    }

  }
}

