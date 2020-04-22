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
import java.util.*;
import javax.swing.*;


public class Operator1D extends JVSMain
{
  private Image image;
  private VSObject out = null;
  private VSObject inA = null;
  private VSObject inB = null;
  private VSComboBox operator=new VSComboBox();
  
  private int INT;
  private int DBL;
  private int INT_1D;
  private int DBL_1D;
  

  public Operator1D()
  {
    //out= new VS1DDouble(0);
  }

  public void xpaint(java.awt.Graphics g)
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
    initPins(0,1,0,2);
    setSize(20+32+3,36);
    initPinVisibility(false,true,false,true);
    element.jSetInnerBorderVisibility(true);

    image=element.jLoadImage(element.jGetSourcePath()+"Operator1D.gif");
    
    setPin(0,ExternalIF.C_VARIANT,element.PIN_OUTPUT);   // Out
    setPin(1,ExternalIF.C_VARIANT,element.PIN_INPUT);   // in A
    setPin(2,ExternalIF.C_VARIANT,element.PIN_INPUT);   // in B

    String strLocale=Locale.getDefault().toString();

    element.jSetPinDescription(0,"A<OP>B");
    element.jSetPinDescription(1,"A");
    element.jSetPinDescription(2,"B");

    
    element.jSetCaptionVisible(false);
    element.jSetCaption("Operator1D");
    setName("Operator1D");
    
    INT = element.C_INTEGER;
    DBL = element.C_DOUBLE;
    INT_1D = element.C_ARRAY1D_INTEGER;
    DBL_1D = element.C_ARRAY1D_DOUBLE;

    element.setPinOutputReference(0,out);
    
    
    operator.addItem("ADD");
    operator.addItem("SUB");
    operator.addItem("MUL");
    operator.addItem("DIV");
    
    operator.selectedIndex=0;

  }


  public void initInputPins()
  {
    inA=(VSObject)element.getPinInputReference(1);
    inB=(VSObject)element.getPinInputReference(2);
  }
  

  public void initOutputPins()
  {
      out=element.jCreatePinDataType(0);
      element.setPinOutputReference(0,out);
  }


  
  public void checkPinDataType()
  {

    boolean pinInA=element.hasPinWire(1);
    boolean pinInB=element.hasPinWire(2);

    int dtA=element.C_VARIANT;
    int dtB=element.C_VARIANT;
    int dtC=element.C_VARIANT;

    if (pinInA==true)
    {
      dtA=element.jGetPinDrahtSourceDataType(1);
    }

    if (pinInB==true)
    {
      dtB=element.jGetPinDrahtSourceDataType(2);
    }

    if (dtA==INT    && dtB==INT) dtC=INT; else
    if (dtA==INT    && dtB==DBL) dtC=DBL; else
    if (dtA==DBL    && dtB==INT) dtC=DBL; else
    if (dtA==DBL    && dtB==DBL) dtC=DBL; else
    if (dtA==INT_1D && dtB==INT_1D) dtC=INT_1D;else
    if (dtA==INT_1D && dtB==INT)    dtC=INT_1D;else
    if (dtA==INT    && dtB==INT_1D) dtC=INT_1D;else
    if (dtA==DBL_1D && dtB==DBL_1D) dtC=DBL_1D;else
    if (dtA==DBL_1D && dtB==DBL)    dtC=DBL_1D;else
    if (dtA==DBL    && dtB==DBL_1D) dtC=DBL_1D;else
    {
     // showMessage("Input combination not supported!");
    }


    element.jSetPinDataType(1,dtA);
    element.jSetPinDataType(2,dtB);
    element.jSetPinDataType(0,dtC);
  }


  public void setPropertyEditor()
  {
    element.jAddPEItem("Operator",operator, 0,0);
  }
  
  public void showMessage(String message)
  {
    JOptionPane.showMessageDialog(null,message,"Error",JOptionPane.ERROR_MESSAGE);
  }



  public void propertyChanged(Object o)
  {
  }

  public void start()
  {

  }


  public void INTArrayWithINT(VS1DInteger out, VS1DInteger array1,VSInteger value)
  {

     if (out.getLength()!=array1.getLength())
     {
       out.copyValueFrom(array1);
     }
     for (int i=0;i<out.getLength();i++)
     {
        switch (operator.selectedIndex)
        {
          case 0 : out.setValue(i,array1.getValue(i)+value.getValue());break;
          case 1 : out.setValue(i,array1.getValue(i)-value.getValue());break;
          case 2 : out.setValue(i,array1.getValue(i)*value.getValue());break;
          case 3 : out.setValue(i,array1.getValue(i)/value.getValue());break;
        }
     }

  }


  
  public void INTArrayWithINTX(VS1DInteger out,VS1DInteger array1,VS1DInteger array2)
  {
     // das kürzere Array als Output benutzen!
     
     int a1=array1.getLength();
     int a2=array2.getLength();

     if (a1>a2)
     {
       out.copyValueFrom(array2);
     } else
     {
       out.copyValueFrom(array1);
     }

     for (int i=0;i<out.getLength();i++)
     {
        switch (operator.selectedIndex)
        {
          case 0 : out.setValue(i,array1.getValue(i)+array2.getValue(i));break;
          case 1 : out.setValue(i,array1.getValue(i)-array2.getValue(i));break;
          case 2 : out.setValue(i,array1.getValue(i)*array2.getValue(i));break;
          case 3 : out.setValue(i,array1.getValue(i)/array2.getValue(i));break;
        }
     }

  }

  public void DBLArrayWithDBLX(VS1DDouble out,VS1DDouble array1,VS1DDouble array2)
  {
     // das kürzere Array als Output benutzen!
     
     int a1=array1.getLength();
     int a2=array2.getLength();

     if (a1>a2)
     {
       out.copyValueFrom(array2);
     } else
     {
       out.copyValueFrom(array1);
     }

     for (int i=0;i<out.getLength();i++)
     {
        switch (operator.selectedIndex)
        {
          case 0 : out.setValue(i,array1.getValue(i)+array2.getValue(i));break;
          case 1 : out.setValue(i,array1.getValue(i)-array2.getValue(i));break;
          case 2 : out.setValue(i,array1.getValue(i)*array2.getValue(i));break;
          case 3 : out.setValue(i,array1.getValue(i)/array2.getValue(i));break;
        }

     }
  }

  
  public void DBLArrayWithDBL(VS1DDouble out, VS1DDouble array1,VSDouble value)
  {
     if (out.getLength()!=array1.getLength())
     {
       out.copyValueFrom(array1);
     }
     for (int i=0;i<out.getLength();i++)
     {
        switch (operator.selectedIndex)
        {
          case 0 : out.setValue(i,array1.getValue(i)+value.getValue());break;
          case 1 : out.setValue(i,array1.getValue(i)-value.getValue());break;
          case 2 : out.setValue(i,array1.getValue(i)*value.getValue());break;
          case 3 : out.setValue(i,array1.getValue(i)/value.getValue());break;
        }
     }
  }



  public void process()
  {
    if (inA!=null && inB!=null)
    {
      //if (inA.isChanged() || inB.isChanged())
      {

        int dtA,dtB,dtC;

        dtA=element.jGetPinDrahtSourceDataType(1);
        dtB=element.jGetPinDrahtSourceDataType(2);
        dtC=element.jGetPinDrahtSourceDataType(0);


        if (dtA==INT    && dtB==INT)
        {
          VSInteger a=(VSInteger)inA;
          VSInteger b=(VSInteger)inB;
          VSInteger o=(VSInteger)out;
          
          switch (operator.selectedIndex)
          {
            case 0 : o.setValue(a.getValue()+b.getValue()); break;
            case 1 : o.setValue(a.getValue()-b.getValue()); break;
            case 2 : o.setValue(a.getValue()*b.getValue()); break;
            case 3 : o.setValue(a.getValue()/b.getValue()); break;
           }

        }else
        if (dtA==INT    && dtB==DBL)
        {
          VSInteger a=(VSInteger)inA;
          VSDouble  b=(VSDouble)inB;
          VSDouble  o=(VSDouble)out;

          switch (operator.selectedIndex)
          {
            case 0 : o.setValue((double)a.getValue()+b.getValue());break;
            case 1 : o.setValue((double)a.getValue()-b.getValue());break;
            case 2 : o.setValue((double)a.getValue()*b.getValue());break;
            case 3 : o.setValue((double)a.getValue()/b.getValue());break;
           }

        }else
        if (dtA==DBL    && dtB==INT)
        {
          VSDouble a=(VSDouble)inA;
          VSInteger  b=(VSInteger)inB;
          VSDouble  o=(VSDouble)out;

          switch (operator.selectedIndex)
          {
            case 0 : o.setValue(a.getValue()+(double)b.getValue());break;
            case 1 : o.setValue(a.getValue()-(double)b.getValue());break;
            case 2 : o.setValue(a.getValue()*(double)b.getValue());break;
            case 3 : o.setValue(a.getValue()/(double)b.getValue());break;
          }
        }else
        if (dtA==DBL    && dtB==DBL)
        {
          VSDouble a=(VSDouble)inA;
          VSDouble  b=(VSDouble)inB;
          VSDouble  o=(VSDouble)out;

          switch (operator.selectedIndex)
          {
            case 0 : o.setValue(a.getValue()+b.getValue());break;
            case 1 : o.setValue(a.getValue()-b.getValue());break;
            case 2 : o.setValue(a.getValue()*b.getValue());break;
            case 3 : o.setValue(a.getValue()/b.getValue());break;
           }
        }else
        if (dtA==INT_1D && dtB==INT_1D)
        {
         INTArrayWithINTX( (VS1DInteger)out, (VS1DInteger)inA,  (VS1DInteger)inB );
        }else
        if (dtA==INT_1D && dtB==INT)
        {
         INTArrayWithINT ( (VS1DInteger)out, (VS1DInteger)inA,  (VSInteger)inB  );
        }else
        if (dtA==INT    && dtB==INT_1D)
        {
          INTArrayWithINT ( (VS1DInteger)out, (VS1DInteger)inB , (VSInteger)inA  );
        }else
        if (dtA==DBL_1D && dtB==DBL_1D)
        {
          DBLArrayWithDBLX( (VS1DDouble)out,(VS1DDouble)inA, (VS1DDouble)inB );
        }else
        if (dtA==DBL_1D && dtB==DBL)
        {
          DBLArrayWithDBL ( (VS1DDouble)out,(VS1DDouble)inA, (VSDouble)inB );
        }else
        if (dtA==DBL    && dtB==DBL_1D)
        {
          DBLArrayWithDBL ( (VS1DDouble)out,(VS1DDouble)inB, (VSDouble)inA );
        }

        out.setChanged(true);
        element.notifyPin(0);
      }
    }
  }
  
  
  public void loadFromStream(java.io.FileInputStream fis)
  {
    operator.loadFromStream(fis);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    operator.saveToStream(fos);
  }
}

