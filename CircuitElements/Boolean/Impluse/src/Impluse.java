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


public class Impluse extends JVSMain
{
  public VSBoolean in;
  public VSBoolean out=new VSBoolean(false);
  private Image image;
  private boolean valuegesendet=false;
  private boolean oki=false;


  public void paint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }


 public void init()
  {
    initPins(0,1,0,1);
    setSize(40,25);
    initPinVisibility(false,true,false,true);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");

    setPin(0,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_BOOLEAN,element.PIN_INPUT);

    element.jSetPinDescription(0,"Out");
    element.jSetPinDescription(1,"In");

    element.jSetCaptionVisible(true);
    element.jSetCaption("Impluse");
    setName("Impluse");
    element.jSetInfo("Carmelo Salafia","Open Source 2006","");
  }
  
  

  public void propertyChanged(Object o)
  {
  }

  public void setPropertyEditor()
  {
  }


  public void initInputPins()
  {
    in=(VSBoolean)element.getPinInputReference(1);
    if (in==null)
    {
      in=new VSBoolean(false);
    }
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }

  public void start()
  {
  }
  
  public void stop()
  {
  }

  public void process()
  {
    if (in.getValue()==true && oki==false)
    {
          out.setValue(true);
          valuegesendet=true;
          oki=true;
          System.out.println("Info Gesendet");
          out.setChanged(true);
          element.notifyPin(0);

    } else
    if (in.getValue()==false&& oki==true)
    {
          oki=false;
        if (valuegesendet)
        {
          out.setValue(false);
          out.setChanged(true);
          valuegesendet=false;
          //oki=false;
          System.out.println("Info wieder aufgehoben!");
          element.notifyPin(0);
        }
    }

  }

  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }


  public void loadFromStream(java.io.FileInputStream fis)
  {
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
  }



}
