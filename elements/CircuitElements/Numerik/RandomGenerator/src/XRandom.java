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
import tools.*;
import java.util.Random;


public class XRandom extends JVSMain
{
  private Image image;
  private Random generator=null;
  private VSDouble in;
  private VSDouble out= new VSDouble();
  private VSDouble inFaktor;

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
    initPins(0,1,0,2);
    setSize(40,25);
    
    element.jSetInnerBorderVisibility(false);
    element.jSetTopPinsVisible(false);
    element.jSetBottomPinsVisible(false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    setPin(0,ExternalIF.C_DOUBLE,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_DOUBLE,element.PIN_INPUT);
    setPin(2,ExternalIF.C_DOUBLE,element.PIN_INPUT);
    
    element.jSetPinDescription(0,"out");
    element.jSetPinDescription(1,"in");
    element.jSetPinDescription(2,"faktor");

    setName("Random_Generator");
  }
  

  public void initInputPins()
  {
    in=(VSDouble)element.getPinInputReference(1);
    inFaktor=(VSDouble)element.getPinInputReference(2);

    if (in==null) in=new VSDouble(0);
    if (inFaktor==null) inFaktor=new VSDouble(1);
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }
  
  public void start()
  {
    generator= new Random();
    out.setValue(0);
    out.setChanged(true);
    element.notifyPin(0);
  }
  


  public void process()
  {
    out.setValue(in.getValue()+(generator.nextDouble()*inFaktor.getValue()/2.0));
    out.setChanged(true);
    element.notifyPin(0);
  }


  public void loadFromStream(java.io.FileInputStream fis)
  {

  }


  public void saveToStream(java.io.FileOutputStream fos)
  {

  }

}


