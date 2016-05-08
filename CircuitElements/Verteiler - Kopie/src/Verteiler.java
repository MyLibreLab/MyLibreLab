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
import javax.swing.*;

public class Verteiler extends JVSMain
{
  private Object valueObj=null;
  private boolean oki=false;
  private int okiPin=-1;
  private int aktuellerPin;
  
  private VSInteger anzPins=new VSInteger(2);

  private VSObject in;
  private VSObject out[];

  private VSInteger outPins = new VSInteger(2);

  public static final byte PIN_INPUT=1;
  public static final byte PIN_OUTPUT=2;
  public static final byte PIN_UNDEFINIED=0;
    
  public void paint(java.awt.Graphics g)
  {
     if (element!=null)
     {
        anzPins.setValue(element.jGetAnzahlPinsRight());
        Rectangle bounds=element.jGetBounds();

        
        int outCount=element.jGetAnzahlPinsRight();
        boolean pinIn=element.hasPinWire(outCount);
        if (pinIn==true)
        {
          int dt=element.jGetPinDataType(outCount);
          Color col=element.jGetDTColor(dt);
          g.setColor(col);
        } else
        {
          g.setColor(new Color(200,50,50));
        }

        int w=bounds.width;
        int h=bounds.height;
        int d=w/2;

        g.drawLine(bounds.x+d,bounds.y+5,bounds.x+d,bounds.height+5);
        g.fillOval(bounds.x+d-3,bounds.y+(h/2)-3,6,6);
        
        g.drawLine(bounds.x,bounds.y+h/2,bounds.x+d,bounds.y+h/2);
        
        for (int i=0;i<anzPins.getValue();i++)
        {
          g.drawLine(bounds.x+d,bounds.y+5+10*i,bounds.x+d+w-5,bounds.y+5+10*i);
        }
     }

  }


  public void init()
  {
    initPins();

    // Die Pins werden Automatisch vom Hauptprogramm gesetzt!
    setName("verteiler");
    
   // element.jSetCaptionVisible(true);
    element.jSetCaption("verteiler");
  }
  
  
  public void initPins()
  {
    initPins(0,anzPins.getValue(),0,1);

    setSize(45,20+(anzPins.getValue()*10));

    element.jSetInnerBorderVisibility(false);

    setPin(anzPins.getValue(),ExternalIF.C_VARIANT,element.PIN_INPUT);
    for (int i=0;i<anzPins.getValue();i++)
    {
      setPin(i,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
    }

  }



  public void checkPinDataType()
  {
    int outCount=element.jGetAnzahlPinsRight();
    
    int C_IN=outCount;
    
    boolean pinIn=element.hasPinWire(C_IN);
    
    int dt=element.C_VARIANT;
    
    if (pinIn==true) dt=element.jGetPinDrahtSourceDataType(C_IN);

    element.jSetPinDataType(C_IN,dt);
    element.jSetPinIO(C_IN, element.PIN_INPUT);

    for (int i=0;i<outCount;i++)
    {
      element.jSetPinDataType(i,dt);
      element.jSetPinIO(i, element.PIN_OUTPUT);
    }

    element.jRepaint();
  }




  public void initInputPins()
  {
    anzPins.setValue(element.jGetAnzahlPinsRight());
    in=(VSObject)element.getPinInputReference(anzPins.getValue());
  }

  public void initOutputPins()
  {
    anzPins.setValue(element.jGetAnzahlPinsRight());
    out=new VSObject[anzPins.getValue()];
    for (int i=0;i<anzPins.getValue();i++)
    {
      out[i]=element.jCreatePinDataType(i);
      element.setPinOutputReference(i,out[i]);
    }
  }

  public void setPropertyEditor()
  {
    element.jAddPEItem("Ausgangs-Pins",outPins, 2,20);
    localize();
  }

  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Output-Pins");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Terminales Salida");
  }


  private void setOutPinsDataTypeAndIO(int anzahl)
  {
    int outPins=anzahl;
    int C_IN=outPins;

    boolean pinIn=element.hasPinWire(C_IN);

    int dt;

    if (pinIn==true)
    {
      dt=element.jGetPinDrahtSourceDataType(C_IN);
    } else dt=element.C_VARIANT;


    element.jSetPinDataType(C_IN,dt);
    element.jSetPinIO(C_IN, element.PIN_INPUT);

    for (int i=0;i<outPins-1;i++)
    {
        element.jSetPinDataType(i,dt);
        element.jSetPinIO(i, element.PIN_OUTPUT);
    }

  }

  public void propertyChanged(Object o)
  {
    if (o == outPins)
    {
      anzPins.setValue(((VSInteger)outPins).getValue());
      
      // 1. die Größe ändern
      // 2. ändere die anzahl der Out-Pins
      // 3. setze für die Out-Pins den Datentyp und den IO-Typ aud Output
      element.jSetSize(element.jGetWidth(), 20+(anzPins.getValue())*10);
      
      
      element.jSetRightPins(anzPins.getValue());
      //element.jSetLeftPins(1);

      //setOutPinsDataTypeAndIO(anzPins);
      element.jRepaint();
      element.jRefreshVM();
    }

  }


  public void process()
  {
    if (in!=null )
    {
      for (int i=0;i<anzPins.getValue();i++)
      {
         if (out[i]!=null)
         {
          out[i].copyValueFrom(in);
          element.notifyPin(i);
         }
      }
    }
  }
  
  

  public void loadFromStream(java.io.FileInputStream fis)
  {
      anzPins.loadFromStream(fis);
      outPins.loadFromStream(fis);
      initPins();
      element.jRepaint();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    anzPins.setValue(element.jGetAnzahlPinsRight());
    anzPins.saveToStream(fos);
    outPins.saveToStream(fos);
  }

}
