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

  private VS1DDouble in;
  private VS1DInteger out= new VS1DInteger(0);

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
    
    setPin(0,ExternalIF.C_ARRAY1D_INTEGER,element.PIN_OUTPUT); // OUT
    setPin(1,ExternalIF.C_ARRAY1D_DOUBLE,element.PIN_INPUT);         // IN
    
    setName("1DDoubleTo1DString");
  }

  public void initInputPins()
  {
    in=(VS1DDouble)element.getPinInputReference(1);
    if (in==null) in=new VS1DDouble(0);
  }
  
  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }


  public void start()
  {
  }


  private void convert_1DDouble_to_1DInteger(VS1DDouble in)
  {

    //VS1DString temp = new VS1DString(in.getLength());
    
    //out.copyValueFrom(temp);

    int[] dest = new int[in.getLength()];
    for (int i=0;i<in.getLength();i++)
    {
      dest[i]=(int)in.getValue(i);
       //out.setValue(i,""+in.getValue(i));
    }
    out.setValues(dest);
  }



  public void process()
  {

    if (in instanceof VS1DDouble)
    {
      convert_1DDouble_to_1DInteger((VS1DDouble)in);
      //System.out.println("Converter : Length="+out.getLength());
      element.notifyPin(0);
    }

  }


}

