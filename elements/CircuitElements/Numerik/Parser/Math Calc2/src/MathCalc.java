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
import java.util.*;


public class MathCalc extends JVSMain
{
  private Image image;
  private VSString inExpr;
  private VSDouble inX[];
  private VSBoolean inCalc;
  private VSDouble outValue= new VSDouble();
  private VSString outError= new VSString();
  private Parser parser = new Parser("");
  private VSInteger anzPins = new VSInteger(2);

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
    initPins(0,2,0,2+anzPins.getValue());
    setSize(20+26+5,22+2+(anzPins.getValue()*10));
    initPinVisibility(false,true,false,true);
    element.jSetInnerBorderVisibility(false);

    setPin(anzPins.getValue(),ExternalIF.C_VARIANT,element.PIN_INPUT);
    for (int i=0;i<anzPins.getValue();i++)
    {
      setPin(i,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
    }


    setPin(0,ExternalIF.C_DOUBLE,element.PIN_OUTPUT); // Out
    setPin(1,ExternalIF.C_STRING,element.PIN_OUTPUT); // Error
    setPin(2,ExternalIF.C_STRING,element.PIN_INPUT);  // Expression
    setPin(3,ExternalIF.C_BOOLEAN,element.PIN_INPUT); // calc

    for (int i=0;i<anzPins.getValue();i++)
    {
      setPin(4+i,ExternalIF.C_DOUBLE,element.PIN_INPUT); // a,b,c,....z
    }



    String strLocale=Locale.getDefault().toString();

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
    }

    for (int i=0;i<anzPins.getValue();i++)
    {
      element.jSetPinDescription(4+i,""+(char)(97+i));
    }

  }


  public void init()
  {
    initPins();

    image=element.jLoadImage(element.jGetSourcePath()+"icon.png");

    element.jSetCaptionVisible(true);
    element.jSetCaption("MathCalc2");
    setName("MathCalc2");
  }



  public void initInputPins()
  {
    inExpr=(VSString)element.getPinInputReference(2);
    inCalc=(VSBoolean)element.getPinInputReference(3);

    if (inExpr==null)
    {
      inExpr=new VSString("");
    }
    if (inCalc==null)
    {
      inCalc=new VSBoolean(false);
    }

    inX = new VSDouble[anzPins.getValue()];


    for (int i=0;i<anzPins.getValue();i++)
    {
      inX[i]=(VSDouble)element.getPinInputReference(4+i);
      if (inX[i]==null)
      {
        inX[i]=new VSDouble(0.0);
      }

    }

  }



  public void setPropertyEditor()
  {
    element.jAddPEItem("Pins",anzPins, 1,26);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Pins");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Pins");
  }

  public void propertyChanged(Object o)
  {
    if (o.equals(anzPins))
    {
      init();
      element.jRepaint();
    }

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

          parser.clearVars();
          
        for (int i=0;i<anzPins.getValue();i++)
        {
          if (inX[i]!=null)
          {
           parser.addVar(""+(char)(97+i),inX[i].getValue());
          }
        }

          outValue.setValue(parser.parseString());
          outValue.setChanged(true);
          element.notifyPin(0);
        }

      }
    }
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
      anzPins.loadFromStream(fis);
      initPins();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    anzPins.saveToStream(fos);
  }
}
