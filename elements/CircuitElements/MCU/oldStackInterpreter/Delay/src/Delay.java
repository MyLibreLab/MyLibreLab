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


public class Delay extends JVSMain
{
  private Image image;
  private VSInteger value=new VSInteger(500);

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
    code+="  DIM ELEMENT"+id+"_LASTVALUE\n";
    element.jSetTag(4, code); // Globals
    
    
    code="";
    code+="  POP R0 \n";
    code+="  PUSH R0\n";
    code+="  CMP R0,ELEMENT"+id+"_LASTVALUE\n";
    code+="  JMP_IF_A=B ELEMENT"+id+"_EXIT\n";
    code+="  MOV ELEMENT"+id+"_LASTVALUE, R0\n";
    code+="  DELAY_MS "+value.getValue()+" \n";
    code+="  ELEMENT"+id+"_EXIT:\n";
    element.jSetTag(2, code);

    // Als Event_handler registrieren
    // D.h. das diese Funktion in der Hauptschleife aufgerufen wird!
    //code="  CALL ELEMENT"+id+"\n";
    //element.jSetTag(1, code);


  /*
    String code="";
    code+="  DELAY_MS "+value.getValue()+"       \n";
    //code+="  MOV %pin0%, %pin1% \n";
    //code+="  %notify0%          \n";
    element.jSetTag(2, code);*/
  }
  
  public void init()
  {
    initPins(0,1,0,1);
    setSize(40,35);

    initPinVisibility(false,true,false,true);
    
    element.jSetInnerBorderVisibility(false);

    setPin(0,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_BOOLEAN,element.PIN_INPUT);

    //element.jSetPinDescription(0,"out");

    image=element.jLoadImage(element.jGetSourcePath()+"delay.gif");

    setName("AVR-DELAY");

    generateCode();
  }
  

  public void initInputPins()
  {
  }
  public void initOutputPins()
  {
  }
  public void start()
  {
  }

  public void process()
  {
  }

  public void setPropertyEditor()
  {
    element.jAddPEItem("Delay[ms]", value, 0,65535);
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



