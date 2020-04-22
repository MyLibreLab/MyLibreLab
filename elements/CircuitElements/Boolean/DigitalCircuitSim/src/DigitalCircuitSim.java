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
import java.io.BufferedReader;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;


public class DigitalCircuitSim extends JVSMain
{
  private int width, height;
  private String fileName;
  
  private Image image=null;
  
  private VSPropertyDialog more = new VSPropertyDialog();

  
  private VSString inputColumns = new VSString("");
  private VSString outputColumns = new VSString("");
  private VS1DString inputString = new VS1DString(4);
  private VS1DString outputString = new VS1DString(4);
  private VSBoolean in[];
  private VSBoolean out[];

  private VSInteger inputs=new VSInteger(2);
  private VSInteger outputs=new VSInteger(1);
  


  public void parseString(String line, boolean array[])
  {
      for (int i=0;i<line.length();i++)
      {
          char c=line.charAt(i);
          if (c=='0') array[i]=false;
          if (c=='1') array[i]=true;
      }
  }

 public void onDispose()
  {
   if (image!=null)
   {
     image.flush();
     image=null;
   }
  }

  public void paint(java.awt.Graphics g)
  {
    if (image!=null) drawImageCentred(g,image);
  }
  
  
  public void init()
  {


    element.jSetInnerBorderVisibility(true);
    element.jSetTopPinsVisible(false);
    element.jSetBottomPinsVisible(false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.png");
    
    element.jSetCaptionVisible(true);

    setName("DigitalCiucuitSimulator");
    
    inputColumns.setValue("A,B");
    outputColumns.setValue("S");

    inputString.setValue(0,"00");
    inputString.setValue(1,"01");
    inputString.setValue(2,"10");
    inputString.setValue(3,"11");
    
    outputString.setValue(0,"1");
    outputString.setValue(1,"0");
    outputString.setValue(2,"0");
    outputString.setValue(3,"1");

  }
  
  public void xOnInit()
  {
   initAllPins();
  }
  
  
  private void initAllPins()
  {
    initPins(0,outputs.getValue(),0,inputs.getValue());
    width=60;
    if (outputs.getValue()>inputs.getValue()) height=20+10*outputs.getValue(); else height=20+10*inputs.getValue();
    setSize(width,height);


    String[] col1=inputColumns.getValue().split(",");
    String[] col2=outputColumns.getValue().split(",");


    int c=0;
    for (int i=0;i<outputs.getValue();i++)
    {
      setPin(c,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
      //element.jSetPinDescription(c,col2[i]);
      c++;
    }

    for (int i=0;i<inputs.getValue();i++)
    {
      setPin(c,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
      //element.jSetPinDescription(c,col1[i]);
      c++;
    }

  }
  
  
  public void initInputPins()
  {
    in=new VSBoolean[inputs.getValue()];
    for (int i=0;i<inputs.getValue();i++)
    {
      in[i]=(VSBoolean)element.getPinInputReference(outputs.getValue()+i);
      if (in[i]==null)
      {
       in[i]=new VSBoolean(false);
      }
    }
  }
  
  public void initOutputPins()
  {
    out = new VSBoolean[outputs.getValue()];
    for (int i=0;i<outputs.getValue();i++)
    {
      out[i]=new VSBoolean();
      element.setPinOutputReference(i,out[i]);
    }
  }
  
  
  private String[] copyVSArrayToStringArray(VS1DString values)
  {
      String str;
      String[] result= new String[values.getLength()];
      for (int i=0;i<values.getLength();i++)
      {
          str=values.getValue(i);
          result[i] = new String(str);
      }

      return result;
  }
  
  public void openDataDialog()
  {

      MyTableEditor frm = new MyTableEditor(element.jGetFrame(),true);

      frm.setInputColumns(inputColumns.getValue());
      frm.setOutputColumns(outputColumns.getValue());

      String[] Xinputs= copyVSArrayToStringArray(inputString);
      String[] Xoutputs= copyVSArrayToStringArray(outputString);

      frm.setInputs(Xinputs);
      frm.setOutputs(Xoutputs);
      frm.setVisible(true);
      
      if (frm.result)
      {
        String[] resI=frm.resultInputs;
        String[] resO=frm.resultOutputs;


        inputColumns.setValue(frm.resultInputColumns);
        outputColumns.setValue(frm.resultOutputColumns);

        inputString.resize(resI.length);
        for (int i=0;i<resI.length;i++)
        {
         inputString.setValue(i,resI[i]);
        }

        outputString.resize(resO.length);
        for (int i=0;i<resO.length;i++)
        {
         outputString.setValue(i,resO[i]);
        }

        inputs.setValue(frm.inputColumnsCount);
        outputs.setValue(frm.outputColumnsCount);
        initAllPins();

      }
  }



  public void setPropertyEditor()
  {
    element.jAddPEItem("Data...",more, 0,0);
  }

  public void propertyChanged(Object o)
  {
     if (o.equals(more))
     {
       openDataDialog();
     }


  }


  public int sucheInpuitString(String val)
  {
     String str;
     val=val.trim();
     for (int i=0;i<inputString.getLength();i++)
     {
       str=inputString.getValue(i).trim();
       if (val.equals(str))
       {
         return i;
       }
     }
     return -1;
  }
  
  public void process()
  {

    String val="";
    boolean value=false;
    
    for (int i=0;i<inputs.getValue();i++)
    {
      if (in[i]!=null)
      {
        value=in[i].getValue();
      }
      if (value==true) val+="1"; else val+="0";
    }

    int index=sucheInpuitString(val);
    //int index=Integer.parseInt(val,2);
    //System.out.println("Index="+index);
    
    if (index<outputString.getLength())
    {
      String zeile=outputString.getValue(index);
      
      boolean array[] = new boolean[outputs.getValue()];

      parseString(zeile,array);


      for (int i=0;i<outputs.getValue();i++)
      {
        if (out[i]!=null)
        {
          out[i].setValue(array[i]);
          element.notifyPin(i);
        }

      }
   }

  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
   inputs.loadFromStream(fis);
   outputs.loadFromStream(fis);

   inputString.loadFromStream(fis);
   outputString.loadFromStream(fis);

   inputColumns.loadFromStream(fis);
   outputColumns.loadFromStream(fis);

   initAllPins();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
   inputs.saveToStream(fos);
   outputs.saveToStream(fos);
   inputString.saveToStream(fos);
   outputString.saveToStream(fos);
   inputColumns.saveToStream(fos);
   outputColumns.saveToStream(fos);
  }

}

