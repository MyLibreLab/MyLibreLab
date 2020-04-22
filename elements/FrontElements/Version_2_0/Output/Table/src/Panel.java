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
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;


public class Panel extends JVSMain implements PanelIF
{
  private JPanel panel;
  private JTable table = new JTable();

  private VSBoolean multiSelection = new VSBoolean(false);
  
  ExternalIF circuitElement;
  private DefaultTableModel model = new DefaultTableModel();

  private VSInteger zeilen=new VSInteger(3);
  private VSInteger spalten=new VSInteger(3);

  private VS2DString in ;
  private VS2DString out = new VS2DString(spalten.getValue(),zeilen.getValue());


  public void fillTable(VS2DString val)
  {
    if (val!=null)
    {
      zeilen.setValue(val.getRows());
      spalten.setValue(val.getColumns());

      System.out.println("Zeilen/Spalten="+zeilen.getValue()+","+spalten.getValue());
      
      model.setColumnCount(0);
      model.setRowCount(0);

      for (int j=0;j<spalten.getValue();j++)
      {
       model.addColumn("a"+j);
      }

      for (int i=0;i<zeilen.getValue();i++)
      {
        String [] data =new String[spalten.getValue()];

        for (int j=0;j<spalten.getValue();j++)
        {
          data[j]= val.getValue(j,i);
          //System.out.println(""+val.getValue(j,i));
        }
        model.addRow(data);
      }
    }
    System.out.println("------------------");
  }
  
  // aus PanelIF
  public void processPanel(int pinIndex, double value, Object obj)
  {
     in=(VS2DString)obj;


     java.awt.EventQueue.invokeLater(new Runnable()
     {
        public void run()
        {
           fillTable(in);
        }
     });
  }




  public void tableValueChanged()
  {
      out = new VS2DString(model.getColumnCount(), model.getRowCount());

//      System.out.println("YYY"+model.getColumnCount()+","+model.getRowCount());
      
      for (int i=0;i<model.getRowCount();i++)
      {
          for (int k=0;k<model.getColumnCount();k++)
          {
              String o=(String)model.getValueAt(i,k);
              
              out.setValue(k,i ,o);
          }
      }

      if (circuitElement!=null) circuitElement.Change(0,out);
  }



  public void init()
  {
    initPins(0,0,0,0);
    setSize(130,130);

    element.jSetResizable(true);
    initPinVisibility(false,false,false,false);
    table.setModel(model);
    
    model.addColumn("a1");
    model.addColumn("a2");
    model.addColumn("a3");

    fillTable(out);

    table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                tableValueChanged();
            }
        });

    /*table.addListSelectionListener( new ListSelectionListener() {
      public void valueChanged( ListSelectionEvent e )
      {
        //ListValueChanged(e);
      }
    } );*/

  }
  
  
  
  public void xOnInit()
  {
    panel=element.getFrontPanel();

    panel.setLayout(new BorderLayout());

    JScrollPane bar = new JScrollPane(table);
    panel.add(bar,BorderLayout.CENTER);
    circuitElement=element.getCircuitElement();
    element.setAlwaysOnTop(true);
  }





  public void setPropertyEditor()
  {
    element.jAddPEItem("Zeilen",zeilen, 1,20);
    element.jAddPEItem("Spalten",spalten, 1,20);

    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Row");
    element.jSetPEItemLocale(d+1,language,"Column");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Row");
    element.jSetPEItemLocale(d+1,language,"Column");
  }
  
  public void propertyChanged(Object o)
  {
    if (multiSelection.getValue())
    {
      //liste.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      //liste.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    } else
    {
    //  liste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    if (o.equals(spalten) || o.equals(zeilen))
    {
      out= new VS2DString(spalten.getValue(),zeilen.getValue());
      fillTable(out);
    }
    


    element.jRefreshVM();

  }


  
  public void loadFromStream(java.io.FileInputStream fis)
  {

      out.loadFromStream(fis);

      fillTable(out);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {

      out.saveToStream(fos);
  }

}

