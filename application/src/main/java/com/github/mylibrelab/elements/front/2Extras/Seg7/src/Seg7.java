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


public class Seg7 extends JVSMain
{
  private VSBoolean inA;
  private VSBoolean inB;
  private VSBoolean inC;
  private VSBoolean inD;
  private VSBoolean inE;
  private VSBoolean inF;
  private VSBoolean inG;
  private VSBoolean inH;

  private Boolean a0=false,a1=false,a2=false,a3=false,a4=false,a5=false,a6=false,a7=false;
  private Boolean value0=false,value1=false,value2=false,value3=false,value4=false,value5=false,value6=false,value7=false;
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
    initPins(0,0,0,8);
    setSize(40,90);
    initPinVisibility(false,false,false,true);

    //panelElement=element.setPanelElement("Panel");
    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");

    for (int i=0;i<=7;i++)
    {
      setPin(i,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    }

    element.jSetPinDescription(0,"a");
    element.jSetPinDescription(1,"b");
    element.jSetPinDescription(2,"c");
    element.jSetPinDescription(3,"d");
    element.jSetPinDescription(4,"e");
    element.jSetPinDescription(5,"f");
    element.jSetPinDescription(6,"g");
    element.jSetPinDescription(7,"h");
    
    
    element.jSetCaptionVisible(true);
    element.jSetCaption("Siebensegm. Anzeie");
    setName("SiebenSegment Anzeige");
  }

  public void initInputPins()
  {
    inA=(VSBoolean)element.getPinInputReference(0);
    inB=(VSBoolean)element.getPinInputReference(1);
    inC=(VSBoolean)element.getPinInputReference(2);
    inD=(VSBoolean)element.getPinInputReference(3);
    inE=(VSBoolean)element.getPinInputReference(4);
    inF=(VSBoolean)element.getPinInputReference(5);
    inG=(VSBoolean)element.getPinInputReference(6);
    inH=(VSBoolean)element.getPinInputReference(7);
    
    if (inA==null) inA=new VSBoolean();
    if (inB==null) inB=new VSBoolean();
    if (inC==null) inC=new VSBoolean();
    if (inD==null) inD=new VSBoolean();
    if (inE==null) inE=new VSBoolean();
    if (inF==null) inF=new VSBoolean();
    if (inG==null) inG=new VSBoolean();
    if (inH==null) inH=new VSBoolean();

  }

  public boolean istEinValueChanged()
  {
    if (value0!=a0) return true;
    if (value1!=a1) return true;
    if (value2!=a2) return true;
    if (value3!=a3) return true;
    if (value4!=a4) return true;
    if (value5!=a5) return true;
    if (value6!=a6) return true;
    if (value7!=a7) return true;
    return false;
  }
  
  

  public void start()
  {
    panelElement=element.getPanelElement();
  }

  public void process()
  {
    a0=inA.getValue();
    a1=inB.getValue();
    a2=inC.getValue();
    a3=inD.getValue();
    a4=inE.getValue();
    a5=inF.getValue();
    a6=inG.getValue();
    a7=inH.getValue();
    

    if (istEinValueChanged())
    {
      value0=a0;
      value1=a1;
      value2=a2;
      value3=a3;
      value4=a4;
      value5=a5;
      value6=a6;
      value7=a7;

      if (panelElement!=null)
      {
        panelElement.jProcessPanel(0,0,new Boolean(a0));
        panelElement.jProcessPanel(1,0,new Boolean(a1));
        panelElement.jProcessPanel(2,0,new Boolean(a2));
        panelElement.jProcessPanel(3,0,new Boolean(a3));
        panelElement.jProcessPanel(4,0,new Boolean(a4));
        panelElement.jProcessPanel(5,0,new Boolean(a5));
        panelElement.jProcessPanel(6,0,new Boolean(a6));
        panelElement.jProcessPanel(7,0,new Boolean(a7));

        panelElement.jRepaint();
      }
    }

  }

}
 
