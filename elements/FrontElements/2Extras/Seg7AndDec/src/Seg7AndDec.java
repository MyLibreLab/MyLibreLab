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


public class Seg7AndDec extends JVSMain
{
  private VSInteger in;

  private int a0,a1,a2,a3,a4,a5,a6,a7;
  
  private boolean started=false;

  private ExternalIF panelElement;
  private Image image;

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
    initPins(0,0,0,1);
    setSize(40,30);
    initPinVisibility(false,false,false,true);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");

    setPin(0,ExternalIF.C_INTEGER,element.PIN_INPUT);

    element.jSetPinDescription(0,"in");

    element.jSetCaptionVisible(true);
    element.jSetCaption("7Segm&Dec");
    setName("SiebenSegment Anzeige + Decoder");
  }

  public void initInputPins()
  {
    in=(VSInteger)element.getPinInputReference(0);

    if (in==null) in=new VSInteger(0);
  }



  public void start()
  {
    panelElement=element.getPanelElement();
    started=true;
  }

  public void process()
  {

    if (in.isChanged() || started)
    {
      started=false;
      switch(in.getValue())
      {
        case 0 : {a0=1; a1=1; a2=1; a3=1; a4=1; a5=1; a6=0; a7=0;break;}
        case 1 : {a0=0; a1=1; a2=1; a3=0; a4=0; a5=0; a6=0; a7=0;break;}
        case 2 : {a0=1; a1=1; a2=0; a3=1; a4=1; a5=0; a6=1; a7=0;break;}
        case 3 : {a0=1; a1=1; a2=1; a3=1; a4=0; a5=0; a6=1; a7=0;break;}
        case 4 : {a0=0; a1=1; a2=1; a3=0; a4=0; a5=1; a6=1; a7=0;break;}
        case 5 : {a0=1; a1=0; a2=1; a3=1; a4=0; a5=1; a6=1; a7=0;break;}
        case 6 : {a0=1; a1=0; a2=1; a3=1; a4=1; a5=1; a6=1; a7=0;break;}
        case 7 : {a0=1; a1=1; a2=1; a3=0; a4=0; a5=0; a6=0; a7=0;break;}
        case 8 : {a0=1; a1=1; a2=1; a3=1; a4=1; a5=1; a6=1; a7=0;break;}
        case 9 : {a0=1; a1=1; a2=1; a3=1; a4=0; a5=1; a6=1; a7=0;break;}
        case 10 : {a0=1; a1=1; a2=1; a3=0; a4=1; a5=1; a6=1; a7=0;break;}
        case 11 : {a0=0; a1=0; a2=1; a3=1; a4=1; a5=1; a6=1; a7=0;break;}
        case 12 : {a0=1; a1=0; a2=0; a3=1; a4=1; a5=1; a6=0; a7=0;break;}
        case 13 : {a0=0; a1=1; a2=1; a3=1; a4=1; a5=0; a6=1; a7=0;break;}
        case 14 : {a0=1; a1=0; a2=0; a3=1; a4=1; a5=1; a6=1; a7=0;break;}
        case 15 : {a0=1; a1=0; a2=0; a3=0; a4=1; a5=1; a6=1; a7=0;break;}
      }

      if (panelElement!=null)
      {
        panelElement.jProcessPanel(0,0,new Boolean( intToBool(a0) ));
        panelElement.jProcessPanel(1,0,new Boolean(intToBool(a1)));
        panelElement.jProcessPanel(2,0,new Boolean(intToBool(a2)));
        panelElement.jProcessPanel(3,0,new Boolean(intToBool(a3)));
        panelElement.jProcessPanel(4,0,new Boolean(intToBool(a4)));
        panelElement.jProcessPanel(5,0,new Boolean(intToBool(a5)));
        panelElement.jProcessPanel(6,0,new Boolean(intToBool(a6)));
        panelElement.jProcessPanel(7,0,new Boolean(intToBool(a7)));

        panelElement.jRepaint();
      }
    }

  }
  
  private boolean intToBool(int bol)
  {
    if (bol==0) return false; else return true;

  }

}
 
