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

public class StrConst1D extends JVSMain
{
  private Image image;

  private VS1DString out= new VS1DString(0);


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
    initPins(0,1,0,0);
    setSize(32+3,32+3);
    initPinVisibility(false,true,false,false);
    element.jSetInnerBorderVisibility(false);

    image=element.jLoadImage(element.jGetSourcePath()+"StrConst1D.gif");
    
    setPin(0,ExternalIF.C_ARRAY1D_STRING,element.PIN_OUTPUT);   // Out
    
    setName("StrConst1D");
  }


  public void initInputPins()
  {
  }
  

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }
  


  public void setPropertyEditor()
  {
    
  }


  public void propertyChanged(Object o)
  {
  }

  public void start()
  {
    out.resize(10);
    for (int i=0;i<10;i++)
    {
     out.setValue(i,""+i);
    }
    out.setChanged(true);
    element.notifyPin(0);

  }



  public void process()
  {

  }
  
  
  public void loadFromStream(java.io.FileInputStream fis)
  {
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
  }
}

