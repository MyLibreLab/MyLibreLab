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
import ExpressionParser.*;
import java.util.ArrayList;
import java.util.Locale;


public class SummExpression extends JVSMain
{
  private Image image;
  private VSString   inExpr;
  private VSBoolean  inCalc;
  private VSDouble   inX;
  private VSInteger  inN;
  private VSInteger inM;
  private VSDouble   inF;
  private VSDouble   inA;
  private VSDouble   inB;
  private VSDouble   inC;

  private VSDouble outValue= new VSDouble();
  private VSString outError= new VSString();
  
  private Parser parser = new Parser("");
  private VSPropertyDialog more= new VSPropertyDialog();


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
  
  public void initPins()
  {
    initPins(0,2,0,9);
    setSize(125,4+(9*10));
    initPinVisibility(false,true,false,true);
    element.jSetInnerBorderVisibility(true);

    setPin(0,ExternalIF.C_DOUBLE,element.PIN_OUTPUT); // y
    setPin(1,ExternalIF.C_STRING,element.PIN_OUTPUT); // Error String

    setPin(2,ExternalIF.C_STRING,element.PIN_INPUT);   // expression
    setPin(3,ExternalIF.C_BOOLEAN,element.PIN_INPUT);  // calc
    setPin(4,ExternalIF.C_DOUBLE,element.PIN_INPUT);   // x
    setPin(5,ExternalIF.C_INTEGER,element.PIN_INPUT);  // n
    setPin(6,ExternalIF.C_INTEGER,element.PIN_INPUT);  // m
    setPin(7,ExternalIF.C_DOUBLE,element.PIN_INPUT);   // F
    setPin(8,ExternalIF.C_DOUBLE,element.PIN_INPUT);   // a
    setPin(9,ExternalIF.C_DOUBLE,element.PIN_INPUT);   // b
    setPin(10,ExternalIF.C_DOUBLE,element.PIN_INPUT);  // c


    element.jSetPinDescription(0,"Y");
    element.jSetPinDescription(1,"Error");
    element.jSetPinDescription(2,"Expression");
    element.jSetPinDescription(3,"calc");
    element.jSetPinDescription(4,"x");
    element.jSetPinDescription(5,"n");
    element.jSetPinDescription(6,"m");
    element.jSetPinDescription(7,"F");
    element.jSetPinDescription(8,"a");
    element.jSetPinDescription(9,"b");
    element.jSetPinDescription(10,"c");



    /*String strLocale=Locale.getDefault().toString();

    if (strLocale.equalsIgnoreCase("de_DE"))
    {
      element.jSetPinDescription(0,"Wert");
      element.jSetPinDescription(1,"Fehler");
      element.jSetPinDescription(2,"Ausdruck");
      element.jSetPinDescription(3,"Berechnen");
    }
    if (strLocale.equalsIgnoreCase("en_US"))
    {
      element.jSetPinDescription(0,"Value");
      element.jSetPinDescription(1,"Error");
      element.jSetPinDescription(2,"Expression");
      element.jSetPinDescription(3,"Calculate");
    }
    if (strLocale.equalsIgnoreCase("es_ES"))
    {
      element.jSetPinDescription(0,"Valor");
      element.jSetPinDescription(1,"Error");
      element.jSetPinDescription(2,"Expresión");
      element.jSetPinDescription(3,"Calcular");
    }  */


  }


  public void init()
  {
    initPins();
    
    image=element.jLoadImage(element.jGetSourcePath()+"image.png");

    element.jSetCaptionVisible(true);
    element.jSetCaption("SummExpression");
    setName("SummExpression");
  }



  public void initInputPins()
  {
    inExpr=(VSString)element.getPinInputReference(2);
    inCalc=(VSBoolean)element.getPinInputReference(3);
    inX=(VSDouble)element.getPinInputReference(4);
    inN=(VSInteger)element.getPinInputReference(5);
    inM=(VSInteger)element.getPinInputReference(6);
    inF=(VSDouble)element.getPinInputReference(7);
    inA=(VSDouble)element.getPinInputReference(8);
    inB=(VSDouble)element.getPinInputReference(9);
    inC=(VSDouble)element.getPinInputReference(10);


    if (inExpr==null) inExpr=new VSString("");
    if (inCalc==null) inCalc=new VSBoolean(false);
    if (inX==null)    inX = new VSDouble(0);
    if (inN==null)    inN = new VSInteger(0);
    if (inM==null)    inM = new VSInteger(0);
    if (inF==null)    inB = new VSDouble(0);
    if (inA==null)    inA = new VSDouble(0);
    if (inB==null)    inB = new VSDouble(0);
    if (inC==null)    inC = new VSDouble(0);
  }


  /*public void setPropertyEditor()
  {
    element.jAddPEItem("Pins",more, 0,0);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+1,language,"Pins");

    language="es_ES";

    element.jSetPEItemLocale(d+1,language,"Pins");
  } */

  public void propertyChanged(Object o)
  {

  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,outValue);
    element.setPinOutputReference(1,outError);
  }

  public void process()
  {
    if (inExpr instanceof VSString && inCalc instanceof VSBoolean)
    {
      if (inCalc.getValue())
      {
        parser.setExpression(inExpr.getValue());

        if (parser.getErrorMessage().length()>0)
        {
          outError.setValue(parser.getErrorMessage());
          outError.setChanged(true);
          element.notifyPin(1);
          parser.delErrorMessage();
        }else
        {


          double summ=0;
          
          for (int i=inN.getValue();i<inM.getValue();i++)
          {
            parser.clearVars();
            parser.addVar("x",inX.getValue());
            parser.addVar("a",inA.getValue());
            parser.addVar("b",inB.getValue());
            parser.addVar("c",inC.getValue());
            parser.addVar("k",i);

            summ+=parser.parseString();
          }
          outValue.setValue(inF.getValue()*summ);
          outValue.setChanged(true);
          element.notifyPin(0);
        }

      }
    }
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {

  }

  public void saveToStream(java.io.FileOutputStream fos)
  {

  }
}
