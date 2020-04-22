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
import javax.swing.*;
import java.awt.*;
import tools.*;
import java.util.*;



public class TasterPanel extends JVSMain
{
  private int sizeW=50;
  private int sizeH=100;
  private VSInteger value=new VSInteger(0);
  private VSInteger initValue=new VSInteger(0);
  private VSInteger anzahlStellungen=new VSInteger(3);
  public VSInteger strokeValue=new VSInteger(10);
  public VSColor fontColor = new VSColor(Color.BLACK);
  public VSFont font = new VSFont(new java.awt.Font("Dialog", 1, 11));
  public VS1DString values = new VS1DString(0);
  
  public VSPropertyDialog captions= new VSPropertyDialog();
  
  public VSColorAdvanced backColor = new VSColorAdvanced();
  public VSColorAdvanced buttonColor = new VSColorAdvanced();
  public VSColorAdvanced strokeColor = new VSColorAdvanced();
  public VSColor linesColor = new VSColor(Color.BLUE);
  public VSColor edgeColor = new VSColor(Color.ORANGE);

  
  private MultiSwitch switcher ;

  private ExternalIF circuitElement;
  
  public void init()
  {
    initPins(0,0,0,0);
    setSize(sizeW,sizeH);
    initPinVisibility(false,false,false,false);

    element.jSetResizable(true);
    element.jSetInnerBorderVisibility(false);
    setName("Taster");
    switcher=new MultiSwitch(element,this);
    switcher.createDescriptions();
    
    backColor.color1= new Color(255,242,181);
    buttonColor.color1=new Color(253,153,0);
    linesColor.setValue(new Color(153,0,0));
    strokeColor.color1=new Color(102,102,102);
    edgeColor.setValue(new Color(0,0,0));
  }
  
    private void formMousePressed(java.awt.event.MouseEvent evt)
    {
        switcher.status++;

        if (switcher.status>=switcher.anzahlStellungen) switcher.status=0;
        value.setValue(switcher.status);
        circuitElement.Change(0,value);
        switcher.repaint();
    }



  public void xOnInit()
  {
    JPanel panel =element.getFrontPanel();
    panel.setLayout(new java.awt.BorderLayout());


    panel.add(switcher, java.awt.BorderLayout.CENTER);
    element.setAlwaysOnTop(true);
    
    switcher.addMouseListener(new java.awt.event.MouseAdapter()
    {
        public void mousePressed(java.awt.event.MouseEvent evt)
        {
            formMousePressed(evt);
        }
    });




  }

  public void initInputPins()
  {
  }

  public void initOutputPins()
  {
  }
  
  
  public void start()
  {
    value.setValue(initValue.getValue());

    element.jRepaint();
    
    circuitElement=element.getCircuitElement();
    circuitElement.Change(0,value);
  }




  public void setPropertyEditor()
  { 
    element.jAddPEItem("init-Wert",initValue,0,20);
    element.jAddPEItem("Schriftart",font, 2,20);
    element.jAddPEItem("Schrift-Farbe",fontColor, 2,20);
    element.jAddPEItem("Schalterstellungen",anzahlStellungen, 2,20);
    element.jAddPEItem("Texte",captions, 0,0);
    element.jAddPEItem("Lines Farbe",linesColor, 0,0);
    element.jAddPEItem("Button Color",buttonColor, 0,0);
    element.jAddPEItem("Hintergrunt Farbe",backColor, 0,0);
    element.jAddPEItem("Stroke Value",strokeValue, 0,50);
    element.jAddPEItem("Stroke Color",strokeColor, 0,0);
    element.jAddPEItem("Edge Color",edgeColor, 0,0);
    
    
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";
    element.jSetPEItemLocale(d+0,language,"Init-Value");
    element.jSetPEItemLocale(d+1,language,"Font");
    element.jSetPEItemLocale(d+2,language,"Font-Color");
    element.jSetPEItemLocale(d+3,language,"Switches");
    element.jSetPEItemLocale(d+4,language,"Captions");
    element.jSetPEItemLocale(d+5,language,"Lines Color");
    element.jSetPEItemLocale(d+6,language,"Button Color");
    element.jSetPEItemLocale(d+7,language,"Background Color");
    element.jSetPEItemLocale(d+8,language,"Stroke Value");
    element.jSetPEItemLocale(d+9,language,"Stroke Color");
    element.jSetPEItemLocale(d+10,language,"Edge Color");
    

    language="es_ES";
    element.jSetPEItemLocale(d+0,language,"Valor Inicial");
    element.jSetPEItemLocale(d+1,language,"Fuente");
    element.jSetPEItemLocale(d+2,language,"Color de Texto");
    element.jSetPEItemLocale(d+3,language,"Interruptores");
    element.jSetPEItemLocale(d+4,language,"Etiquetas");
    element.jSetPEItemLocale(d+5,language,"Color de Lineas");
    element.jSetPEItemLocale(d+6,language,"Color del Boton");
    element.jSetPEItemLocale(d+7,language,"Color de Fondo");
    element.jSetPEItemLocale(d+8,language,"Espesor Marco");
    element.jSetPEItemLocale(d+9,language,"Color Marco");
    element.jSetPEItemLocale(d+10,language,"Color bordes");
    
  }
  
  private String getTextWithN()
  {
      String str="";

      for (int i=0;i<values.getLength();i++)
      {
        str+=values.getValue(i)+"\n";
      }

      return str;
  }
    public String getTextWithKomma()
  {
      String str="";

      for (int i=0;i<values.getLength();i++)
      {
        str+=values.getValue(i)+",";
      }

      return str;
  }


  public void propertyChanged(Object o)
  {
    switcher.anzahlStellungen=anzahlStellungen.getValue();
    switcher.status=initValue.getValue();
    
    
    if (o.equals(captions))
    {
      String str=getTextWithN();

      Properties frm = new Properties(element.jGetFrame(),str);
      frm.setSize(200,200);
      frm.setModal(true);
      frm.setVisible(true);

      if (frm.result==true)
      {
        String res=frm.strText;

        ArrayList liste=new ArrayList();

        int lastI=0;
        // Generiere neues Array!
        for (int i=0;i<res.length();i++)
        {
          char ch=res.charAt(i);

          if (ch=='\n')
          {
           liste.add(res.substring(lastI,i));
           lastI=i+1;
          }
        }
        if (lastI>0)liste.add(res.substring(lastI));

        values.resize(liste.size());

        for (int i=0;i<liste.size();i++)
        {
          values.setValue(i,(String)liste.get(i));
        }

        captions.setText(getTextWithKomma());

        // Generate from values subElements!
        switcher.createFromCaptions();

      }

    } else
    {
      //values.resize(anzahlStellungen.getValue());
      switcher.createFromCaptions();
    }

    
    element.jRepaint();
  }



  public void loadFromStream(java.io.FileInputStream fis)
  {
    initValue.loadFromStream(fis);
    anzahlStellungen.loadFromStream(fis);
    fontColor.loadFromStream(fis);
    font.loadFromStream(fis);
    values.loadFromStream(fis);
    
    backColor.loadFromStream(fis);
    linesColor.loadFromStream(fis);
    edgeColor.loadFromStream(fis);
    buttonColor.loadFromStream(fis);
    strokeColor.loadFromStream(fis);
    strokeValue.loadFromStream(fis);
    
    switcher.anzahlStellungen=anzahlStellungen.getValue();
    switcher.status=initValue.getValue();
    switcher.createFromCaptions();
    element.jRepaint();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    initValue.saveToStream(fos);
    anzahlStellungen.saveToStream(fos);
    fontColor.saveToStream(fos);
    font.saveToStream(fos);
    values.saveToStream(fos);
    
    backColor.saveToStream(fos);
    linesColor.saveToStream(fos);
    edgeColor.saveToStream(fos);
    buttonColor.saveToStream(fos);
    strokeColor.saveToStream(fos);
    strokeValue.saveToStream(fos);
    
    
  }
}


