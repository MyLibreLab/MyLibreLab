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
import javax.swing.*;
import java.util.*;


public class STOP extends JVSMain
{
  private Image image;

  private VSBoolean in=null;

  public void paint(java.awt.Graphics g)
  {
    if (image!=null) drawImageCentred(g,image);
  }


  public void init()
  {
    initPins(0,0,0,1);
    setSize(45,34);
    initPinVisibility(false,false,false,true);
    
    element.jSetInnerBorderVisibility(true);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    setPin(0,element.C_BOOLEAN,element.PIN_INPUT);
    
    element.jSetResizable(false);
    element.jSetCaptionVisible(true);
    element.jSetCaption("o");
    
    setName("#STOP#");
  }



  public void initInputPins()
  {
    in=(VSBoolean)element.getPinInputReference(0);
  }


  
  private Integer pinIndex=null;
  private boolean changed=false;
  private VSBoolean o;

  ExternalIF el ;
  public void destElementCalled()
  {
    if (changed)
    {
      System.out.println("CANCELLED---------------------<");
      o.setValue(false);
      el.notifyPin(pinIndex);
      changed=false;
    }
  }

  public void process()
  {
    if (in instanceof VSBoolean)
    {
      if (in.getValue())
      {
        Object tag=element.jGetTag();
      
        if (tag instanceof ArrayList)
        {
          ArrayList liste = (ArrayList) tag;
          if (liste.size()==3)
          {
            if (liste.get(0) instanceof VSObject)
            {
              o=(VSBoolean)liste.get(0);
              o.copyValueFrom(in);
            }
            if (liste.get(1) instanceof Integer)
            {
              pinIndex=(Integer)liste.get(1);
            }
            if (liste.get(2) instanceof ExternalIF)
            {
              el = (ExternalIF)liste.get(2);
              //el.elementActionPerformed(new ElementActionEvent(0,el));
              System.out.println("STOPPED");

                VSBasisIF basis = element.jGetBasis();
                basis.vsStop();

              el.notifyPin(pinIndex);
              el.jNotifyWhenDestCalled(pinIndex,element);

              changed=true;
              //el.notifyPin(pinIndex);

            }
          }
        }
      }
    }
  }
  


}

