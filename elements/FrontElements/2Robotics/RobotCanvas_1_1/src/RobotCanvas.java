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

  private VSInteger out1=new VSInteger(0);   // Sensor distance 1
  private VSInteger out2=new VSInteger(0);   // Sensor distance 2
  private VSInteger out3=new VSInteger(0);   // Sensor distance 3
  private VSInteger outX=new VSInteger(0);   // X
  private VSInteger outY=new VSInteger(0);   // Y
  private VSDouble outAlpha=new VSDouble(0); // angle
  private VSBoolean outPosMoving=new VSBoolean(false); //outPosMoving(x,y,alpha)
  private VS2DDouble outMatrix= new VS2DDouble(3,1);    // Matrix Out

  private VSBoolean inGoForward;     // goForward
  private VSBoolean inGoBack;        // goBack
  private VSBoolean inStop;          // stop
  private VSDouble  inAngle;         // angle

  private VSBoolean inRotateLeft;    // RotateLeft
  private VSBoolean inRotateRight;   // RotateRight
  private VSDouble  inRotateStep;    // RotateStep

  private int rowCounter=0;
  private double[][] oldData=new double[3][0];
  
  private VSInteger  inX;    // inX
  private VSInteger  inY;    // inY
  private VSBoolean  inGoto; // Goto(X,Y)
  private VSBoolean  inSetPosition;// SetPosition(X,Y)
  
  private VSBoolean inOpen;          // Greifer Open/Close
  private VSGroup   inImages;        // image (Group)
  private VSGroup   inPickableObjs;  // PickableImages (Group) // nur Koordinated, keine Bilder!

  private VSBoolean inRecord;      // Weg aufzeichnen!
  private VSInteger inSpeed;       // Speed
  private VSBoolean inClearTable;  // Clear 2D Table
  private VSImage24  inRobotImage;


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
    initPins(0,8,0,18);
    setSize(60,20+18*10);
    initPinVisibility(false,true,false,true);
    
    setPin(0,ExternalIF.C_INTEGER,element.PIN_OUTPUT); // Sensor distance 1
    setPin(1,ExternalIF.C_INTEGER,element.PIN_OUTPUT); // Sensor distance 2
    setPin(2,ExternalIF.C_INTEGER,element.PIN_OUTPUT); // Sensor distance 3
    setPin(3,ExternalIF.C_INTEGER,element.PIN_OUTPUT);  // X
    setPin(4,ExternalIF.C_INTEGER,element.PIN_OUTPUT);  // Y
    setPin(5,ExternalIF.C_DOUBLE,element.PIN_OUTPUT);  // Alpha
    setPin(6,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);  // outPosMoving(x,y,Alpha)
    setPin(7,ExternalIF.C_ARRAY2D_DOUBLE,element.PIN_OUTPUT);  // Matrix Out

    setPin(8,ExternalIF.C_BOOLEAN,element.PIN_INPUT); // GOFORWARD
    setPin(9,ExternalIF.C_BOOLEAN,element.PIN_INPUT); // GOBACK
    setPin(10,ExternalIF.C_BOOLEAN,element.PIN_INPUT); // STOP
    setPin(11,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // SETANGLE

    setPin(12,ExternalIF.C_BOOLEAN,element.PIN_INPUT);// RotateLeft
    setPin(13,ExternalIF.C_BOOLEAN,element.PIN_INPUT);// RotateRight
    setPin(14,ExternalIF.C_DOUBLE,element.PIN_INPUT); // RotateStep
    
    setPin(15,ExternalIF.C_INTEGER,element.PIN_INPUT); // inX
    setPin(16,ExternalIF.C_INTEGER,element.PIN_INPUT); // inY
    setPin(17,ExternalIF.C_BOOLEAN,element.PIN_INPUT); // inGoto(X,Y)
    setPin(18,ExternalIF.C_BOOLEAN,element.PIN_INPUT); // inSetPosition(X,Y)


    setPin(19,ExternalIF.C_BOOLEAN,element.PIN_INPUT);// OpenCLose Greifer
    setPin(20,ExternalIF.C_GROUP,  element.PIN_INPUT);// Images (GROUP)
    setPin(21,ExternalIF.C_GROUP,  element.PIN_INPUT);// PickageObjects (GROUP)
    setPin(22,ExternalIF.C_BOOLEAN,element.PIN_INPUT);// Record
    setPin(23,ExternalIF.C_INTEGER,element.PIN_INPUT);// inSpeed
    setPin(24,ExternalIF.C_BOOLEAN,element.PIN_INPUT);// inClearTable
    setPin(25,ExternalIF.C_IMAGE,element.PIN_INPUT);// RobotImage

    element.jSetPinDescription(0,"Sensor 1 distance");
    element.jSetPinDescription(1,"Sensor 2 distance");
    element.jSetPinDescription(2,"Sensor 3 distance");
    element.jSetPinDescription(3,"X-Out");
    element.jSetPinDescription(4,"Y-Out");
    element.jSetPinDescription(5,"Angle");
    element.jSetPinDescription(6,"is Moving [Event]");
    element.jSetPinDescription(7,"2DMatrix Out");
    
    element.jSetPinDescription(8,"GoForward");
    element.jSetPinDescription(9,"GoBack");
    element.jSetPinDescription(10,"Stop");
    element.jSetPinDescription(11,"SetAngle");
    
    element.jSetPinDescription(12,"Rotate Left");
    element.jSetPinDescription(13,"Rotate Right");
    element.jSetPinDescription(14,"Rotate Step (default =10)°");
    
    element.jSetPinDescription(15,"X-In");
    element.jSetPinDescription(16,"Y-In");
    element.jSetPinDescription(17,"Goto(X-In,Y-In)");
    element.jSetPinDescription(18,"inSetPosition(X-In,Y-In)");
    
    element.jSetPinDescription(19,"Picker Open/Close");
    element.jSetPinDescription(20,"Images");
    element.jSetPinDescription(21,"Pickables Objects Coordinates");
    element.jSetPinDescription(22,"Record way");
    element.jSetPinDescription(23,"Speed [ms] (default = 50ms)");
    element.jSetPinDescription(24,"Clear Table Data");
    element.jSetPinDescription(25,"Robot Image [40x40]Pixel");
    

    image=element.jLoadImage(element.jGetSourcePath()+"icon.png");
    setName("RobotCanvas");
    
    element.jSetResizable(false);
  }
  
  public void initInputPins()
  {
      inGoForward=(VSBoolean)element.getPinInputReference(8);
      inGoBack=(VSBoolean)element.getPinInputReference(9);
      inStop=(VSBoolean)element.getPinInputReference(10);
      inAngle=(VSDouble)element.getPinInputReference(11);
      
      inRotateLeft=(VSBoolean)element.getPinInputReference(12);
      inRotateRight=(VSBoolean)element.getPinInputReference(13);
      inRotateStep=(VSDouble)element.getPinInputReference(14);

      inX=(VSInteger)element.getPinInputReference(15);
      inY=(VSInteger)element.getPinInputReference(16);
      inGoto=(VSBoolean)element.getPinInputReference(17);
      inSetPosition=(VSBoolean)element.getPinInputReference(18);
      
      inOpen=(VSBoolean)element.getPinInputReference(19);
      inImages=(VSGroup)element.getPinInputReference(20);
      inPickableObjs=(VSGroup)element.getPinInputReference(21);
      inRecord=(VSBoolean)element.getPinInputReference(22);
      inSpeed=(VSInteger)element.getPinInputReference(23);
      inClearTable=(VSBoolean)element.getPinInputReference(24);
      inRobotImage=(VSImage24)element.getPinInputReference(25);
      
      if (inRecord==null) inRecord=new VSBoolean(false);
      if (inSpeed==null) inSpeed=new VSInteger(50);
      if (inClearTable==null) inClearTable=new VSBoolean(false);

  }
  
  public void start()
  {
    panelElement=element.getPanelElement();
    rowCounter=0;
    oldData=new double[3][0];
    double[][] values=new double[3][0];
    outMatrix.setValues(values, 3, 0);

    element.notifyPin(7);
  }
  
  public void stop()
  {

  }


  public void initOutputPins()
  {
    element.setPinOutputReference(0,out1);
    element.setPinOutputReference(1,out2);
    element.setPinOutputReference(2,out3);
    element.setPinOutputReference(3,outX);
    element.setPinOutputReference(4,outY);
    element.setPinOutputReference(5,outAlpha);
    element.setPinOutputReference(6,outPosMoving);
    element.setPinOutputReference(7,outMatrix);
  }


  public void changePin(int pinIndex, Object value)
  {

    if (pinIndex==0 && value instanceof VSInteger)
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
    }
    
    if (pinIndex==6 && value instanceof VSBoolean)
    {
      /*VSBoolean tmp = (VSBoolean)value;
      outPosChanged.setValue(tmp.getValue());

      element.notifyPin(6);*/
      
      if (inRecord.getValue())
      {
        double[][] values =addToData(outX.getValue(), outY.getValue(), outAlpha.getValue(), inSpeed.getValue()) ;

        outMatrix.setValues(values, 3, rowCounter);
        element.notifyPin(7);
      }

    }
    
    if (pinIndex==7 && value instanceof VSBoolean)
    {
      VSBoolean tmp = (VSBoolean)value;
      outPosMoving.setValue(tmp.getValue());

      element.notifyPin(6);
    }

  }
  
  
  private double[][] addToData(double x, double y, double alpha, int speed)
  {

      double[][] newData=new double[3][rowCounter+1];

      int i,j;
      
      for (j=0;j<rowCounter;j++)
      {
        for (i=0;i<3;i++)
        {
          newData[i][j]=oldData[i][j];
        }
      }
      
      newData[0][rowCounter]=x;
      newData[1][rowCounter]=y;
      newData[2][rowCounter]=alpha;
      //newData[3][rowCounter]=speed;
      
      oldData=newData;

      rowCounter++;
      
      return newData;
  }

  public void process()
  {
  }
  

  
  public void elementActionPerformed(ElementActionEvent evt)
  {

      int index = evt.getSourcePinIndex();

       if (index==8 && inGoForward!=null)
       {
         if (panelElement!=null) panelElement.jProcessPanel(0,0,inGoForward);
       }
       if (index==9 && inGoBack!=null)
       {
         if (panelElement!=null) panelElement.jProcessPanel(1,0,inGoBack);
       }
       if (index==10 && inStop!=null)
       {
         if (panelElement!=null) panelElement.jProcessPanel(2,0,inStop);
       }
       if (index==11 && inAngle!=null)
       {
         if (panelElement!=null) panelElement.jProcessPanel(3,0,inAngle);
       }


       if (index==12 && inRotateLeft!=null)
       {
         if (panelElement!=null) panelElement.jProcessPanel(4,0,inRotateLeft);
       }
       if (index==13 && inRotateRight!=null)
       {
         if (panelElement!=null) panelElement.jProcessPanel(5,0,inRotateRight);
       }
       if (index==14 && inRotateStep!=null)
       {
         if (panelElement!=null) panelElement.jProcessPanel(6,0,inRotateStep);
       }


       if (index==15 && inX!=null)
       {
         if (panelElement!=null) panelElement.jProcessPanel(7,0,inX);
       }
       if (index==16 && inY!=null)
       {
         if (panelElement!=null) panelElement.jProcessPanel(8,0,inY);
       }
       if (index==17 && inGoto!=null)
       {
         if (panelElement!=null) panelElement.jProcessPanel(9,0,inGoto);
       }
       if (index==18 && inSetPosition!=null)
       {
         if (panelElement!=null) panelElement.jProcessPanel(10,0,inSetPosition);
       }


       if (index==19 && inOpen!=null)
       {
         if (panelElement!=null) panelElement.jProcessPanel(11,0,inOpen);
       }


       if (index==20 && inImages!=null)
       {
         if (panelElement!=null) panelElement.jProcessPanel(12,0,inImages);
       }
       if (index==21 && inPickableObjs!=null)
       {
         if (panelElement!=null) panelElement.jProcessPanel(13,0,inPickableObjs);
       }
       if (index==22 && inRecord!=null)
       {
         if (panelElement!=null) panelElement.jProcessPanel(14,0,inRecord);
       }
       if (index==23 && inSpeed!=null)
       {
         if (panelElement!=null) panelElement.jProcessPanel(15,0,inSpeed);
       }
       if (index==24 && inClearTable!=null)
       {
         if (panelElement!=null) panelElement.jProcessPanel(16,0,inClearTable);
         if (inClearTable.getValue())
         {
          oldData=new double[3][0];
          rowCounter=0;
          double[][] values=new double[3][0];
          outMatrix.setValues(values, 3, 0);
          element.notifyPin(7);
         }

       }
       
       if (index==25 && inRobotImage!=null)
       {
          if (panelElement!=null) panelElement.jProcessPanel(17,0,inRobotImage);
       }


  }
  

}

