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
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;



public class Starter extends JVSMain
{
  private Image image;
  private VSString inApplication;
  private VSString inParameters;
  private VSBoolean inRun;
  private boolean changed=false;


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
    initPins(0,0,0,3);
    setSize(40,40);
    element.jSetInnerBorderVisibility(true);
    element.jSetTopPinsVisible(false);
    element.jSetRightPinsVisible(false);
    element.jSetBottomPinsVisible(false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.png");

    setPin(0,ExternalIF.C_STRING,element.PIN_INPUT);
    setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
    setPin(2,ExternalIF.C_BOOLEAN,element.PIN_INPUT);


    String strLocale=Locale.getDefault().toString();

    if (strLocale.equalsIgnoreCase("de_DE"))
    {
      element.jSetPinDescription(0,"applikation");
      element.jSetPinDescription(1,"parameter");
      element.jSetPinDescription(2,"starten");
    }
    if (strLocale.equalsIgnoreCase("en_US"))
    {
      element.jSetPinDescription(0,"application");
      element.jSetPinDescription(1,"parameter");
      element.jSetPinDescription(2,"run");
    }
    if (strLocale.equalsIgnoreCase("es_ES"))
    {
      element.jSetPinDescription(0,"application");
      element.jSetPinDescription(1,"parameter");
      element.jSetPinDescription(2,"run");
    }

    setName("Starter");
  }

  public void initInputPins()
  {
    inApplication=(VSString)element.getPinInputReference(0);
    inParameters=(VSString)element.getPinInputReference(1);
    inRun=(VSBoolean)element.getPinInputReference(2);
  }

  public void initOutputPins()
  {
  }


    public static void showMessage(String message)
    {
        JOptionPane.showMessageDialog(null,message,"Error",JOptionPane.ERROR_MESSAGE);
    }

  public void process()
  {
      if (inRun!=null && inRun.isChanged() && inApplication!=null)
      {
        if (inRun.getValue()==true)
        {
           String app=inApplication.getValue();
           try
           {

             if (inParameters!=null)
             {
               Runtime.getRuntime().exec(app+" "+inParameters.getValue());
             } else
             {
               Runtime.getRuntime().exec(app);
             }


           } catch(Exception ex)
           {
              showMessage(ex.toString());
           }
        }
      }
  }
}



