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
import javax.swing.table.*;


class DoubleModel extends javax.swing.table.DefaultTableModel
{
    public Class getColumnClass(int columnIndex)
    {
        return java.lang.Double.class;
    }
}


public class MatrixNXM extends JVSMain
{
  private Object valueObj=null;

  private JTable table = new JTable();
  private JScrollPane bar = new JScrollPane(table);
  
  private VSInteger zeilen=new VSInteger(3);

  
  private DefaultTableModel model = new DoubleModel();
  
  private VS1DDouble out = new VS1DDouble(zeilen.getValue());

  public void paint(java.awt.Graphics g)
  {
     if (element!=null)
     {
        Rectangle bounds=element.jGetBounds();
        
        int w=bounds.width;
        int h=bounds.height;

        bar.setLocation(8,10);
        bar.setSize(w,h);

     }
  }


  public void init()
  {
    initPins(0,1,0,0);

    element.jSetInnerBorderVisibility(true);

    setPin(0,ExternalIF.C_ARRAY1D_DOUBLE,element.PIN_OUTPUT);


    // Die Pins werden Automatisch vom Hauptprogramm gesetzt!
    setName("1DMatrix");
    
    element.jSetResizable(true);
    
    setSize(100,100);
   // element.jSetCaptionVisible(true);

    out = new VS1DDouble(zeilen.getValue());
  }
  
  public void xOnInit()
  {
    JPanel panel =element.getFrontPanel();
    //panel.setLayout(new java.awt.BorderLayout());
    panel.setLayout(null);

    panel.add(bar, java.awt.BorderLayout.CENTER);
    element.setAlwaysOnTop(false);
    //text.setEditable(false);
    table.setModel(model);
    table.setCellSelectionEnabled(true);
    
    
    table.addPropertyChangeListener(new java.beans.PropertyChangeListener()
    {
        public void propertyChange(java.beans.PropertyChangeEvent evt)
        {
            jTable1PropertyChange(evt);
        }
    });

    //table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    
    fillTable();
  }

    private void jTable1PropertyChange(java.beans.PropertyChangeEvent evt)
    {
      copyValuesToOut();
      //System.out.println("CHANGED!");
    }


  
  public void fillTable()
  {

    model.setColumnCount(0);
    model.setRowCount(0);

    model.addColumn("vector");

    for (int i=0;i<zeilen.getValue();i++)
    {
      Double [] data =new Double[1];

      data[0]= new Double(out.getValue(i));
      model.addRow(data);
    }

  }
  


  public void initInputPins()
  {

  }

  public void initOutputPins()
  {
    
    element.setPinOutputReference(0,out);

    //element.notifyPin(0);

      //element.setPinOutputReference(0,out);
  }

  public void setPropertyEditor()
  {
    element.jAddPEItem("Zeilen",zeilen, 1,20);
    localize();
  }

  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Row");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Row");

  }


 /* private void setOutPinsDataTypeAndIO(int anzahl)
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

  }*/

  public void propertyChanged(Object o)
  {
    /*if (o.equals(more))
    {

    } else*/
    {
      if (o.equals(zeilen))
      {
        out=new VS1DDouble(zeilen.getValue());

        fillTable();
        element.jRefreshVM();
      }

    }
  }
  

  private void copyValuesToOut()
  {
    //VS1DDouble temp=new VS1DDouble(zeilen.getValue());

    for (int i=0;i<zeilen.getValue();i++)
    {
      Double o=(Double)model.getValueAt(i,0);
      out.setValue(i,o.doubleValue());
    }
    //return temp;
  }
  
  public void start()
  {
    copyValuesToOut();
    element.notifyPin(0);
  }

  public void process()
  {

  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
      zeilen.loadFromStream(fis);

      //out=new VS1DDouble(zeilen.getValue());
      out.loadFromStream(fis);
      //System.out.println("LOAD");
      

      fillTable();
      element.jRepaint();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    zeilen.saveToStream(fos);

    out.saveToStream(fos);
    
    
  }

}
