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


public class AbzugsVerz extends JVSMain
{
  private VSInteger timerNr=new VSInteger(0);
  private VSInteger intervall=new VSInteger(10);
  
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
    code+="  DIM ELEMENT"+id+"_OLDVALUE\n";
    element.jSetTag(4, code); // Globals

    // Initialisierung
    // D.h. das diese Funktion nur 1x aufgerufen wird!
    code="";
    code+="  PUSH "+timerNr.getValue()+"// TimerNr \n";
    code+="  PUSH "+intervall.getValue()+"// Intervall \n";
    code+="  TIMER_SET_INTERVALL\n";

    code+="  PUSH "+timerNr.getValue()+"// TimerNr \n";
    code+="  GET_LABEL_ADR  ELEMENT"+id+"_TIMER // PUSH LabelAdresse \n";
    code+="  CALL_WHEN_TIMER_INCREASED\n";
    element.jSetTag(5, code); // im Init Block


    // HauptCode
    code="";
    
    code+="  MOV %pin0%,%pin1%\n";
    code+="  \n";
    code+="  GOTO ELEMENT"+id+"_BEGIN\n";
    code+="ELEMENT"+id+"_TIMER:\n";
    code+="  PUSH "+timerNr.getValue()+"// TimerNr \n";
    code+="  TIMER_STOP\n";
    code+="  %notify0%\n";
    code+="  RETURN\n";
    code+="  \n";
    code+="ELEMENT"+id+"_BEGIN:\n";
    code+="  CMP %pin1%,1 \n";
    code+="  JMP_IF_A=B ELEMENT"+id+"_SUB \n";
    code+="  PUSH "+timerNr.getValue()+"// TimerNr \n";
    code+="  TIMER_START\n";
    code+="  GOTO ELEMENT"+id+"_EXIT\n";
    code+="ELEMENT"+id+"_SUB:\n";
    code+="  %notify0%\n";
    code+="ELEMENT"+id+"_EXIT: \n";


    element.jSetTag(2, code);
  }


  public void init()
  {
    initPins(0,1,0,1);
    setSize(32+22,30);

    initPinVisibility(false,true,false,true);
    
    element.jSetInnerBorderVisibility(false);

    setPin(0,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_BOOLEAN,element.PIN_INPUT);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    element.jSetPinDescription(0,"out");
    element.jSetPinDescription(1,"in");
    
    setName("AVR-AbzugsVerzögerung");
    
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
    element.jAddPEItem("Timer-Nr",timerNr, 0,10);
    element.jAddPEItem("Intervall",intervall, 0,65535);
    localize();
  }

  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Timer-Nr");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Timer-Nr");
  }
  
  public void propertyChanged(Object o)
  {
    generateCode();
  }


  
  
  
  public void loadFromStream(java.io.FileInputStream fis)
  {
    timerNr.loadFromStream(fis);
    intervall.loadFromStream(fis);
    generateCode();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    timerNr.saveToStream(fos);
    intervall.saveToStream(fos);
  }



}



