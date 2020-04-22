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



public class PID extends JVSMain
{
  private Image image;
  private VSDouble inE;
  private VSDouble inTa;
  private VSDouble inKp;
  private VSDouble inKi;
  private VSDouble inKd;

  private VSDouble out= new VSDouble();

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
    initPins(0,1,0,5);
    setSize(50,25+10*5);
    element.jSetInnerBorderVisibility(true);
    element.jSetTopPinsVisible(false);
    element.jSetBottomPinsVisible(false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.png");
    
    element.jInitPins();
    
    setPin(0,ExternalIF.C_DOUBLE,element.PIN_OUTPUT); // y
    setPin(1,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // e (Regelabweichung)
    setPin(2,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // Ta (Abtastzeit)
    setPin(3,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // Kp
    setPin(4,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // Ki
    setPin(5,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // Kd
    
    element.jSetPinDescription(0,"y");
    element.jSetPinDescription(1,"e (Regelabweichung)");
    element.jSetPinDescription(2,"Ta (Abtastzeit)");
    element.jSetPinDescription(3,"Kp");
    element.jSetPinDescription(4,"Ki");
    element.jSetPinDescription(5,"Kd");
    
    setName("PID-Regulator");

  }



  public void initInputPins()
  {
    inE=(VSDouble)element.getPinInputReference(1);
    inTa=(VSDouble)element.getPinInputReference(2);
    inKp=(VSDouble)element.getPinInputReference(3);
    inKi=(VSDouble)element.getPinInputReference(4);
    inKd=(VSDouble)element.getPinInputReference(5);
    
    if (inE==null) inE=new VSDouble(0);
    if (inTa==null) inTa=new VSDouble(1);
    if (inKp==null) inKp=new VSDouble(1);
    if (inKi==null) inKi=new VSDouble(0);
    if (inKd==null) inKd=new VSDouble(0);
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }


  double esum;
  double ealt;

  public void start()
  {
    esum=0;
    ealt=0;
  }


  public void process()
  {
    double e = inE.getValue();
    double Ta = inTa.getValue();
    double Kp = inKp.getValue();
    double Ki = inKi.getValue();
    double Kd = inKd.getValue();
    double y=0;
    
    if (Ta>0)
    {
      esum+=e;
      y=Kp*e + Ki*Ta*esum + Kd*(e-ealt)/Ta;
      ealt=e;
    }

    //System.out.println("y="+y);
    out.setValue(y);

    element.notifyPin(0);
  }


}

