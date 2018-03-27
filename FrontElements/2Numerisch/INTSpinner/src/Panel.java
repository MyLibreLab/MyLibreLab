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
import java.util.*;
import tools.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class Panel extends JVSMain
{
  private int width=80, height=25;
  private int index;
  private VSInteger initValue=new VSInteger(0);
  private VSInteger value=new VSInteger(0);
  private VSInteger min=new VSInteger(-99999999);
  private VSInteger max=new VSInteger(99999999);
  private VSInteger step=new VSInteger(1);
  private Font fnt = new Font("Dialog",0,12);
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
             value.setValue( ((Integer)jSpinner.getValue()).intValue());
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
    setName("Spinner (INT)");
    


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
    
    //JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor)editor;
    
    
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
    SpinnerNumberModel model = new SpinnerNumberModel(new Integer(initValue.getValue()), new Integer(min.getValue()), new Integer(max.getValue()), new Integer(step.getValue()));
    jSpinner.setModel(model);
    JSpinner.NumberEditor editor = new JSpinner.NumberEditor(jSpinner);
    editor.getTextField().setHorizontalAlignment(JTextField.CENTER);
    jSpinner.setEditor(editor);
  }
  

  public void setPropertyEditor()
  {
    element.jAddPEItem("Anfangswert",initValue, -999999,999999);
    element.jAddPEItem("Min",min, -999999,999999);
    element.jAddPEItem("Max",max, -999999,999999);
    element.jAddPEItem("Schritt",step, 0,999999);

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


    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Valor Inicial");
    element.jSetPEItemLocale(d+1,language,"Min");
    element.jSetPEItemLocale(d+2,language,"Max");
    element.jSetPEItemLocale(d+3,language,"Step");
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
    jSpinner.setValue(new Integer(initValue.getValue()));
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
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      initValue.saveToStream(fos);
      min.saveToStream(fos);
      max.saveToStream(fos);
      step.saveToStream(fos);
  }



}

