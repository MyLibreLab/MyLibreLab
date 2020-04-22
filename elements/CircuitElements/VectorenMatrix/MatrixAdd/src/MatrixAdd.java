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
import java.util.*;
import javax.swing.*;


public class MatrixAdd extends JVSMain
{
  private Image image;
  private VS2DDouble out = new VS2DDouble(0,0);
  private VS2DDouble inA = null;
  private VS2DDouble inB = null;

  public void paint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }

  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }

  public void init()
  {
    initPins(0,1,0,2);
    setSize(45+3,36);
    initPinVisibility(false,true,false,true);
    element.jSetInnerBorderVisibility(false);

    image=element.jLoadImage(element.jGetSourcePath()+"image.png");
    
    setPin(0,ExternalIF.C_ARRAY2D_DOUBLE,element.PIN_OUTPUT);  // Out
    setPin(1,ExternalIF.C_ARRAY2D_DOUBLE,element.PIN_INPUT);   // in A
    setPin(2,ExternalIF.C_ARRAY2D_DOUBLE,element.PIN_INPUT);   // in B

    String strLocale=Locale.getDefault().toString();

    element.jSetPinDescription(0,"A*B");
    element.jSetPinDescription(1,"A");
    element.jSetPinDescription(2,"B");

    
    element.jSetCaptionVisible(false);
    element.jSetCaption("Matrix Mul");
    setName("Matrix Mul");
    
  }


  public void initInputPins()
  {
    inA=(VS2DDouble)element.getPinInputReference(1);
    inB=(VS2DDouble)element.getPinInputReference(2);
    
    if (inA==null) inA=new VS2DDouble(0,0);
    if (inB==null) inB=new VS2DDouble(0,0);
  }
  

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }


  public void start()
  {

  }



   /*
   * Addieren einer Matrix b
   */
  VS2DDouble add(VS2DDouble a, VS2DDouble b)
  {
    int m = a.getRows();       // Anzahl Zeilen
    int n = a.getColumns();    // Anzahl Spalten
    int mb = b.getRows();         // Anzahl  Zeilen in b
    int nb = b.getColumns();      // Anzahl  Spalten in b

    if ((m != mb) || (n != nb))
    {
      return null;            // Dimensionen nicht passend
    }

    double val;
    
    // (m x n) - Ergebnismatrix c erzeugen
    // und Addition durchführen
    VS2DDouble c = new VS2DDouble(n,m);
    for (int i = 0; i < m; i++)
    {
      for (int j = 0; j < n; j++)
      {
        val=a.getValue(j,i) + b.getValue(j,i);
        c.setValue(j,i,val);
      }
    }
    return c;
  }
  public void process()
  {
    if (inA.getColumns()>0 || inA.getRows()>0)
    {
      if (inB.getColumns()>0 || inB.getRows()>0)
      {

        VS2DDouble temp= add(inA,inB);
        if (temp!=null)
        {
          out.copyValueFrom(temp);
          element.notifyPin(0);
        }else
        {
          element.jException(" ((A.rows <> B.rows) or (A.cols <> B.cols))");
        }
      }

    }

  }
  
}

