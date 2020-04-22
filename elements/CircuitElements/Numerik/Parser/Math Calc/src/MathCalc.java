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
  private VSBoolean inCalc;
  private VSDouble outValue= new VSDouble();
  private VSString outError= new VSString();
  private Parser parser = new Parser("");

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
    initPins(0,2,0,2);
    setSize(20+26+5,32+3);
    initPinVisibility(false,true,false,true);
    element.jSetInnerBorderVisibility(false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.png");
    
    setPin(0,ExternalIF.C_DOUBLE,element.PIN_OUTPUT); // Out
    setPin(1,ExternalIF.C_STRING,element.PIN_OUTPUT); // Error
    setPin(2,ExternalIF.C_STRING,element.PIN_INPUT);  // Expression
    setPin(3,ExternalIF.C_BOOLEAN,element.PIN_INPUT); // calc


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



    element.jSetCaptionVisible(true);
    element.jSetCaption("MathCalc");
    setName("MathCalc");
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
          outValue.setValue(parser.parseString());
          outValue.setChanged(true);
          element.notifyPin(0);
        }

      }
    }
  }
}

