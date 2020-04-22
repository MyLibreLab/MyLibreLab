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
import javax.swing.*;
import tools.*;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class Panel extends JVSMain
{
  private int width=120, height=30;
  private int index;
  private VSDouble initValue=new VSDouble(0);
  private VSDouble value=new VSDouble(0);
  private VSDouble min=new VSDouble(-99999999);
  private VSDouble max=new VSDouble(99999999);
  private VSDouble step=new VSDouble(1);
  
  private VSFont fnt = new VSFont(new Font("Dialog",1,15));
  private VSColor fontColor = new VSColor(new Color(253,153,0));
  private VSColor BackColor = new VSColor(new Color(255,242,181));
  private VSBoolean opaque = new VSBoolean(true);
  private VSBoolean centerText = new VSBoolean(true);
  
  
  private ExternalIF circuitElement;
  private javax.swing.JSpinner jSpinner;
  

   public void paint(java.awt.Graphics g)
   {
   }
   
   private boolean isLoading=false;
   

    private void cmbProcessingActionPerformed(java.awt.event.ActionEvent evt) {

        /*if (!isLoading)
        {
           int index=combo.getSelectedIndex();

           if (circuitElement!=null)
           {
             value.setValue(index);
             circuitElement.Change(0,value);
           }

        } */

    }

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt)
    {
           if (circuitElement!=null)
           {
             value.setValue( ((Double)jSpinner.getValue()).doubleValue());
             circuitElement.Change(0,value);
           }
    }


  public void init()
  {
    initPins(0,0,0,0);
    setSize(width,height);
    initPinVisibility(false,false,false,false);
    element.jSetInnerBorderVisibility(false);
    
    
    element.jSetResizable(true);
    setName("Spinner_JV (DBL)");
    


    /*combo.addActionListener(new java.awt.event.ActionListener()
    {
        public void actionPerformed(java.awt.event.ActionEvent evt)
        {
            cmbProcessingActionPerformed(evt);
        }
    }); */
  }
  
  public void xOnInit()
  {
    JPanel panel=element.getFrontPanel();
    jSpinner=new javax.swing.JSpinner();
    panel.setLayout(new BorderLayout());
    panel.add(jSpinner,BorderLayout.CENTER);
    //circuitElement=element.getCircuitElement();
    element.setAlwaysOnTop(true);

    make();
    
    jSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
        public void stateChanged(javax.swing.event.ChangeEvent evt) {
            jSpinner1StateChanged(evt);
        }
    });

  }
  
  private void make()
  {
    SpinnerNumberModel model = new SpinnerNumberModel(new Double(initValue.getValue()), new Double(min.getValue()), new Double(max.getValue()), new Double(step.getValue()));
    jSpinner.setModel(model);
    JSpinner.NumberEditor editor = new JSpinner.NumberEditor(jSpinner);
    if(centerText.getValue()){
    editor.getTextField().setHorizontalAlignment(JTextField.CENTER);    
    }else{
    editor.getTextField().setHorizontalAlignment(JTextField.LEADING);      
    }
    
    editor.getTextField().setFont(fnt.getValue());
    editor.getTextField().setForeground(fontColor.getValue());
    editor.getTextField().setBackground(BackColor.getValue());
    editor.getTextField().setOpaque(opaque.getValue());
    
    jSpinner.setFont(fnt.getValue());
    jSpinner.setForeground(fontColor.getValue());
    jSpinner.setBackground(BackColor.getValue());
    jSpinner.setOpaque(opaque.getValue());
    
    jSpinner.setEditor(editor);
  }
  

  public void setPropertyEditor()
  {
    element.jAddPEItem("Anfangswert",initValue, -999999,999999);
    element.jAddPEItem("Min",min, -999999,999999);
    element.jAddPEItem("Max",max, -999999,999999);
    element.jAddPEItem("Schritt",step, 0,999999);
    element.jAddPEItem("Font",fnt, 0,0);
    
    element.jAddPEItem("Foreground",fontColor, 0,0);
    element.jAddPEItem("Background",BackColor, 0,0);
    element.jAddPEItem("Opaque",opaque, 0,0);
    element.jAddPEItem("Center Text",centerText, 0,0);

    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Init-Value");
    element.jSetPEItemLocale(d+1,language,"Min");
    element.jSetPEItemLocale(d+2,language,"Max");
    element.jSetPEItemLocale(d+3,language,"Step");
    
    element.jSetPEItemLocale(d+4,language,"Font");
    element.jSetPEItemLocale(d+5,language,"Foreground");
    element.jSetPEItemLocale(d+6,language,"Background Color");
    element.jSetPEItemLocale(d+7,language,"Opaque");
    element.jSetPEItemLocale(d+8,language,"Center Text");


    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Valor Inicial");
    element.jSetPEItemLocale(d+1,language,"Min");
    element.jSetPEItemLocale(d+2,language,"Max");
    element.jSetPEItemLocale(d+3,language,"Step");
    
    element.jSetPEItemLocale(d+4,language,"Fuente");
    element.jSetPEItemLocale(d+5,language,"Color Fuente");
    element.jSetPEItemLocale(d+6,language,"Color Fondo");
    element.jSetPEItemLocale(d+7,language,"Opaco");
    element.jSetPEItemLocale(d+8,language,"Centrar Texto");
  }
  
  

  
  public void propertyChanged(Object o)
  {
    make();
    element.jRepaint();
  }


  public void mouseMoved(MouseEvent e)
  {
  }
  
  public void mousePressed(MouseEvent e)
  {
  }


  public void start()
  {
    jSpinner.setValue(new Double(initValue.getValue()));
    circuitElement=element.getCircuitElement();
    circuitElement.Change(0,initValue);
  }
  
  public void process()
 {

  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
      initValue.loadFromStream(fis);
      min.loadFromStream(fis);
      max.loadFromStream(fis);
      step.loadFromStream(fis);
      fnt.loadFromStream(fis);
      fontColor.loadFromStream(fis);
      BackColor.loadFromStream(fis);
      opaque.loadFromStream(fis);
      centerText.loadFromStream(fis);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      initValue.saveToStream(fos);
      min.saveToStream(fos);
      max.saveToStream(fos);
      step.saveToStream(fos);
      fnt.saveToStream(fos);
      fontColor.saveToStream(fos);
      BackColor.saveToStream(fos);
      opaque.saveToStream(fos);
      centerText.saveToStream(fos);
  }



}

