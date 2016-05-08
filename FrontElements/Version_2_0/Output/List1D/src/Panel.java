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


public class Panel extends JVSMain implements PanelIF
{
  private JPanel panel;
  private JList liste = new JList();
  private VS1DString in=null;
  private VSBoolean multiSelection = new VSBoolean(false);
  
  ExternalIF circuitElement;
  private DefaultListModel model = new DefaultListModel();



  public void proc()
  {

   String str="";
     if (in!=null)
     {
       model.clear();
       for (int i=0;i<in.getLength();i++)
       {
         str=in.getValue(i);
         model.addElement(str);
       }

     }
  }
  // aus PanelIF
  public void processPanel(int pinIndex, double value, Object obj)
  {
     in=(VS1DString)obj;


        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
               proc();
                //new DialogWait().setVisible(true);
            }
        });
  }

    public void ListValueChanged( ListSelectionEvent e )
    {
        Object[] values=liste.getSelectedValues();
        

        if (circuitElement!=null) circuitElement.Change(0,values);


    }


  public void init()
  {
    initPins(0,0,0,0);
    setSize(100,150);

    element.jSetResizable(true);
    initPinVisibility(false,false,false,false);
    liste.setModel(model);
    liste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    

    liste.addListSelectionListener( new ListSelectionListener() {
      public void valueChanged( ListSelectionEvent e )
      {
        ListValueChanged(e);
      }
    } );

  }
  
  public void xOnInit()
  {
    panel=element.getFrontPanel();

    panel.setLayout(new BorderLayout());

    JScrollPane bar = new JScrollPane(liste);
    panel.add(bar,BorderLayout.CENTER);
    circuitElement=element.getCircuitElement();
    element.setAlwaysOnTop(true);
  }

  public void setPropertyEditor()
  {
    element.jAddPEItem("Multiselection",multiSelection, 0,0);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Multiselection");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Multiselectionón");
  }
  
  public void propertyChanged(Object o)
  {
    if (multiSelection.getValue())
    {
      liste.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      //liste.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    } else
    {
      liste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

  }


  
  public void loadFromStream(java.io.FileInputStream fis)
  {
      multiSelection.loadFromStream(fis);
      propertyChanged(null);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      multiSelection.saveToStream(fos);
  }

}

