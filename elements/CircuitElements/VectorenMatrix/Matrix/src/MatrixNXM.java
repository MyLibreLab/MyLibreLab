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



public class MatrixNXM extends JVSMain
{
  private VSPropertyDialog more = new VSPropertyDialog();
  private Image image;
  private VS2DDouble matrix= new VS2DDouble(3,3);

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
    drawImageCentred(g,image);
  }


  public void init()
  {
    initPins(0,1,0,0);

    element.jSetInnerBorderVisibility(false);

    setPin(0,ExternalIF.C_ARRAY2D_DOUBLE,element.PIN_OUTPUT);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.png");
    
    setName("Matrix");
    
    element.jSetResizable(false);
    
    setSize(34+10,36);
    element.jSetCaption("Matrix");
  }
  
  public void xOnInit()
  {
  }


  public void initInputPins()
  {

  }

  public void initOutputPins()
  {
    VS2DDouble ref= new VS2DDouble(0,0);
    ref.copyValueFrom(matrix);
    element.setPinOutputReference(0,ref);
    // Später nur noch mit Referenzen Arbeiten!
  }

  public void setPropertyEditor()
  {
    element.jAddPEItem("Werte..",more, 0,0);
    localize();
  }

  private void localize()
  {
    int d=6;
    String language;

    language="en_US";
    element.jSetPEItemLocale(d,language,"Values...");


    language="es_ES";
    element.jSetPEItemLocale(d,language,"Values...");
  }



  public void propertyChanged(Object o)
  {
    if (o.equals(more))
    {
      MyTableEditor frm = new MyTableEditor(null,true);

      frm.setInputs(matrix.getValues(),matrix.getColumns(),matrix.getRows());
      frm.setVisible(true);


      if (frm.result)
      {
        double[][] data=frm.getInputs();
        matrix.setValues(data,frm.resN, frm.resM);
      }

    }
  }
  

  public void start()
  {
  
    element.notifyPin(0);
  }

  public void process()
  {

  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
    matrix.loadFromStream(fis);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    matrix.saveToStream(fos);
  }

}
