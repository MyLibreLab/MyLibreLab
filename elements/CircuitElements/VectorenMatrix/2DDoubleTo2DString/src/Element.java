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

public class Element extends JVSMain
{
  private Image image;

  private VS2DDouble in;
  private VS2DString out= new VS2DString(1,1);

  public void xpaint(java.awt.Graphics g)
  {
    if (image!=null) drawImageCentred(g,image);
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
    initPins(0,1,0,1);
    setSize(32+20,32+4);
    initPinVisibility(false,true,false,true);
    element.jSetInnerBorderVisibility(true);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    setPin(0,ExternalIF.C_ARRAY2D_STRING,element.PIN_OUTPUT); // OUT
    setPin(1,ExternalIF.C_ARRAY2D_DOUBLE,element.PIN_INPUT);         // IN
    
    setName("2DDoubleTo2DString");
  }

  public void initInputPins()
  {
    in=(VS2DDouble)element.getPinInputReference(1);
    if (in==null) in=new VS2DDouble(0,0);
  }
  
  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }


  public void start()
  {
  }


  private void convert_2DDouble_to_2DString(VS2DDouble in)
  {

    VS2DString temp = new VS2DString(in.getColumns(),in.getRows());
    
    out.copyValueFrom(temp);

    for (int i=0;i<in.getRows();i++)
    {
      for (int j=0;j<in.getColumns();j++)
      {
        out.setValue(j,i,""+in.getValue(j,i));
      }
    }
  }



  public void process()
  {

    if (in instanceof VS2DDouble)
    {
      convert_2DDouble_to_2DString((VS2DDouble)in);
      //System.out.println("Converter : Zeilen/Spalten="+out.getColumns()+","+out.getRows());
      element.notifyPin(0);
    }

  }


}

