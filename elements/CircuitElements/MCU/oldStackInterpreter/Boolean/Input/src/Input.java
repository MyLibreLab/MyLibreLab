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
import javax.swing.*;
import java.awt.event.*;


public class Input extends JVSMain
{
  private VSInteger value=new VSInteger(0);
  private VSBoolean out=new VSBoolean();
  private boolean changed=false;
  private Image image;

  public void paint(java.awt.Graphics g)
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
  
  
  private void generateCode()
  {

    int id=element.jGetID();

    String code="";
    /*code+="  DIM ELEMENT"+id+"_OLDVALUE\n";
    element.jSetTag(4, code); // Globals

    code="";
    code+="  GET_BIT PORTB,"+value.getValue()+"  \n";
    code+="  POP R0  \n";
    code+="  CMP R0, ELEMENT"+id+"_OLDVALUE\n";
    code+="  JMP_IF_A=B ELEMENT"+id+"_EXIT\n";
    code+="  GET_BIT PORTB,"+value.getValue()+"\n";
    code+="  POP ELEMENT"+id+"_OLDVALUE  \n";
    code+="  MOV %pin0%,ELEMENT"+id+"_OLDVALUE \n";
    code+="  %notify0%\n";
    code+="ELEMENT"+id+"_EXIT: \n";
    element.jSetTag(2, code);

    // Als Event_handler registrieren
    // D.h. das diese Funktion in der Hauptschleife aufgerufen wird!
    code="  CALL ELEMENT"+id+"\n";
    element.jSetTag(1, code);*/



    code="";
    code+="  E "+value.getValue()+"  \n";
    element.jSetTag(2, code);

  }


  public void init()
  {
    initPins(0,1,0,0);
    setSize(32+11,30);

    initPinVisibility(false,true,false,false);
    
    element.jSetInnerBorderVisibility(false);

    setPin(0,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    element.jSetPinDescription(0,"out");
    

    setName("Input-Pin[0]");
    
    generateCode();
  }
  

  public void initInputPins()
  {
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }



  public void start()
  {
    changed=true;
    //out.setValue(value.getValue());
    element.notifyPin(0);
    
  }

  public void process()
  {
    if (changed)
    {
      changed=false;

    }
  }



  public void setPropertyEditor()
  {
    element.jAddPEItem("Pin-Nr",value, 0,7);
    localize();
  }

  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Pin-Nr");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Pin-Nr");
  }
  
  public void propertyChanged(Object o)
  {
    generateCode();
  }


  
  
  
  public void loadFromStream(java.io.FileInputStream fis)
  {
    value.loadFromStream(fis);
    generateCode();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    value.saveToStream(fos);
  }



}



