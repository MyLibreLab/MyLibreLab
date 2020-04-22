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

public class RobotCanvas extends JVSMain
{
  private ExternalIF panelElement;
  private Image image;

  private VSDouble   inBasis;
  private VSDouble   inZ1;
  private VSDouble   inZ2;
  private VSDouble   inZ3;
  private VSDouble   inHand;
  private VSBoolean  inHandClose;
  

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
    element.jSetInnerBorderVisibility(true);
    initPins(0,0,0,6);
    setSize(60,20+6*10);
    initPinVisibility(true,true,true,true);
    
    /*setPin(0,ExternalIF.C_INTEGER,element.PIN_OUTPUT); // Sensor distance 1
    setPin(1,ExternalIF.C_INTEGER,element.PIN_OUTPUT); // Sensor distance 2
    setPin(2,ExternalIF.C_INTEGER,element.PIN_OUTPUT); // Sensor distance 3
    setPin(3,ExternalIF.C_INTEGER,element.PIN_OUTPUT);  // X
    setPin(4,ExternalIF.C_INTEGER,element.PIN_OUTPUT);  // Y
    setPin(5,ExternalIF.C_DOUBLE,element.PIN_OUTPUT);  // Alpha
    setPin(6,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);  // outPosMoving(x,y,Alpha)
    setPin(7,ExternalIF.C_ARRAY2D_DOUBLE,element.PIN_OUTPUT);  // Matrix Out*/

    setPin(0,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // Basis
    setPin(1,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // Z1
    setPin(2,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // Z2
    setPin(3,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // Z3
    setPin(4,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // Hand
    setPin(5,ExternalIF.C_BOOLEAN,element.PIN_INPUT); // Hand Close
    //setPin(6,ExternalIF.C_BOOLEAN,element.PIN_INPUT); // inResetCamera


    element.jSetPinDescription(0,"Basis");
    element.jSetPinDescription(1,"Z1");
    element.jSetPinDescription(2,"Z2");
    element.jSetPinDescription(3,"Z3");
    element.jSetPinDescription(4,"Hand");
    element.jSetPinDescription(5,"Hand Open Close");
    //element.jSetPinDescription(6,"Reset Camera");

    image=element.jLoadImage(element.jGetSourcePath()+"icon.png");
    setName("RobotArm3D v. 1.0");
    
    element.jSetResizable(false);
  }
  
  public void initInputPins()
  {
      inBasis=(VSDouble)element.getPinInputReference(0);
      inZ1=(VSDouble)element.getPinInputReference(1);
      inZ2=(VSDouble)element.getPinInputReference(2);
      inZ3=(VSDouble)element.getPinInputReference(3);
      inHand=(VSDouble)element.getPinInputReference(4);
      inHandClose=(VSBoolean)element.getPinInputReference(5);
      //inResetCamera=(VSBoolean)element.getPinInputReference(6);

      if (inBasis==null) inBasis=new VSDouble(0);
      if (inZ1==null) inZ1=new VSDouble(0);
      if (inZ2==null) inZ2=new VSDouble(0);
      if (inZ3==null) inZ3=new VSDouble(0);
      if (inHand==null) inHand=new VSDouble(0);
      if (inHandClose==null) inHandClose=new VSBoolean(false);
     // if (inResetCamera==null) inResetCamera=new VSBoolean(false);

  }
  
  public void start()
  {
    panelElement=element.getPanelElement();

    // Reinit Panel!
    //if (panelElement!=null) panelElement.jProcessPanel(0,0,null);

  }
  
  public void stop()
  {

  }


  public void initOutputPins()
  {
    /*element.setPinOutputReference(0,out1);
    element.setPinOutputReference(1,out2);
    element.setPinOutputReference(2,out3);
    element.setPinOutputReference(3,outX);
    element.setPinOutputReference(4,outY);
    element.setPinOutputReference(5,outAlpha);
    element.setPinOutputReference(6,outPosMoving);
    element.setPinOutputReference(7,outMatrix);*/
  }


  public void changePin(int pinIndex, Object value)
  {

    /*if (pinIndex==0 && value instanceof VSInteger)
    {
      VSInteger tmp = (VSInteger)value;
      out1.setValue(tmp.getValue());
      element.notifyPin(0);
    }
    if (pinIndex==1 && value instanceof VSInteger)
    {
      VSInteger tmp = (VSInteger)value;
      out2.setValue(tmp.getValue());
      element.notifyPin(1);
    }
    if (pinIndex==2 && value instanceof VSInteger)
    {
      VSInteger tmp = (VSInteger)value;
      out3.setValue(tmp.getValue());
      element.notifyPin(2);
    }
    
    if (pinIndex==3 && value instanceof VSInteger)
    {
      VSInteger tmp = (VSInteger)value;
      outX.setValue(tmp.getValue());
      element.notifyPin(3);
    }
    if (pinIndex==4 && value instanceof VSInteger)
    {
      VSInteger tmp = (VSInteger)value;
      outY.setValue(tmp.getValue());
      element.notifyPin(4);
    }
    if (pinIndex==5 && value instanceof VSDouble)
    {
      VSDouble tmp = (VSDouble)value;
      outAlpha.setValue(tmp.getValue());
      element.notifyPin(5);
    }*/
    

  }
  
  public void process()
  {
  }
  
  
  public void elementActionPerformed(ElementActionEvent evt)
  {
     int index = evt.getSourcePinIndex();

     if (index==0)
     {
       if (panelElement!=null) panelElement.jProcessPanel(0,0,inBasis);
     }else
     if (index==1)
     {
       if (panelElement!=null) panelElement.jProcessPanel(1,0,inZ1);
     }else
     if (index==2)
     {
       if (panelElement!=null) panelElement.jProcessPanel(2,0,inZ2);
     }else
     if (index==3)
     {
       if (panelElement!=null) panelElement.jProcessPanel(3,0,inZ3);
     }else
     if (index==4)
     {
       if (panelElement!=null) panelElement.jProcessPanel(4,0,inHand);
     }else
     if (index==5)
     {
       if (panelElement!=null) panelElement.jProcessPanel(5,0,inHandClose);
     }


  }
  

}

