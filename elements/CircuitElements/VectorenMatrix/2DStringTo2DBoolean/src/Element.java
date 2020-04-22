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

  private VS2DString in;
  private VS2DBoolean out= new VS2DBoolean(0,0);

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
    
    setPin(0,ExternalIF.C_ARRAY2D_BOOLEAN,element.PIN_OUTPUT); // OUT
    setPin(1,ExternalIF.C_ARRAY2D_STRING,element.PIN_INPUT);         // IN
    
    setName("2DStringTo2DBoolean");
  }

  public void initInputPins()
  {
    in=(VS2DString)element.getPinInputReference(1);
    if (in==null) in=new VS2DString(0,0);
  }
  
  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }


  public void start()
  {
  }


  private void convert_2DString_to_2DBoolean(VS2DString in)
  {

    VS2DBoolean temp = new VS2DBoolean(in.getColumns(),in.getRows());
    
    out.copyValueFrom(temp);
    
    boolean val=false;

    for (int i=0;i<in.getRows();i++)
    {
      for (int j=0;j<in.getColumns();j++)
      {
        try
        {
          val=Boolean.valueOf(in.getValue(j,i));
          out.setValue(j,i,val);
        }catch(Exception ex)
        {
        }


      }
    }
  }



  public void process()
  {

      convert_2DString_to_2DBoolean(in);
     // System.out.println("Converter : Zeilen/Spalten="+out.getColumns()+","+out.getRows());
      element.notifyPin(0);

  }


}

