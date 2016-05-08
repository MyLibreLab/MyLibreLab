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
import java.text.*;
import javax.swing.*;
import tools.*;
import java.awt.geom.Rectangle2D;
import java.util.*;

public class Panel extends JVSMain
{
  private int width=150, height=25;
  private String value="";
  private VSString initValue=new VSString("");
  private double oldPin;
  private Font fnt = new Font("Courier",0,12);
  private ExternalIF circuitElement;
  private JTextField text = new JTextField();



  public void init()
  {
    initPins(0,0,0,0);
    setSize(width,height);
    initPinVisibility(false,false,false,false);
    element.jSetInnerBorderVisibility(false);
    
    element.jSetResizable(true);
    setName("Eingabe");
  }


  public void xOnInit()
  {
    JPanel panel =element.getFrontPanel();
    panel.setLayout(new java.awt.BorderLayout());

    panel.add(text, java.awt.BorderLayout.CENTER);
    element.setAlwaysOnTop(true);


    text.addKeyListener(new java.awt.event.KeyAdapter()
    {
        public void keyReleased(java.awt.event.KeyEvent evt)
        {
          value=((JTextField)evt.getSource()).getText();
          circuitElement=element.getCircuitElement();
          circuitElement.Change(0,new VSString(value));
        }
    });


  }
  
  public void setProps()
  {

    /*if (passwd.getValue()==true)
    {
      textfield.setEchoChar('#');
    } else
    {
      textfield.setEchoChar('');
    }*/

  }
  
  public void setPropertyEditor()
  {
    element.jAddPEItem("Anfangs-Wert",initValue, 0,0);
    //element.jAddPEItem("Passwort",passwd, 0,0);

    setProps();
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Init-Value");
    //element.jSetPEItemLocale(d+1,language,"Password");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Valor Inicial");
    //element.jSetPEItemLocale(d+1,language,"Password");
  }
  
  
  public void propertyChanged(Object o)
  {
    value=initValue.getValue();    
    text.setText(value);
    element.jRepaint();
  }
  



  
  public void start()
  {
    value=initValue.getValue();
    element.jRepaint();
    
    circuitElement=element.getCircuitElement();
    circuitElement.Change(0,new VSString(value));
    
    
    text.setText(value);
  }
  
  public void loadFromStream(java.io.FileInputStream fis)
  {
      initValue.loadFromStream(fis);
      value=initValue.getValue();    
      text.setText(value);

      element.jRepaint();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      initValue.saveToStream(fos);
  }



}

