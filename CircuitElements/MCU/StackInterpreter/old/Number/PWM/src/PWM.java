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
import javax.swing.*;
import java.awt.event.*;


public class PWM extends JVSMain
{
  private Image image;
  private VSInteger value=new VSInteger(0);
  private Font fnt = new Font("Courier",0,12);



public void paint(java.awt.Graphics g)
  {
     if (element!=null)
    {
       Graphics2D g2=(Graphics2D)g;
       Rectangle bounds=element.jGetBounds();
       g2.setColor(Color.YELLOW);
       g.fillRect(bounds.x,bounds.y,bounds.width-1,bounds.height-1);
       g2.setFont(fnt);
       g2.setColor(Color.BLACK);

       String caption="PWM["+value.getValue()+"]";
       FontMetrics fm = g2.getFontMetrics();


       g.drawString(caption,10,15);
       g.drawRect(bounds.x,bounds.y,bounds.width-1,bounds.height-1);
    }
    //if (image!=null) drawImageCentred(g,image);
  }

  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }
  
  private void generateCode()
  {
    int id=element.jGetID();

    String code="";

    code+="  PUSHB "+value.getValue()+" // PIN-NR\n";  // PinNR
    code+="  PUSHI %pin0% // VALUE\n";
    code+="  PWM \n";

    element.jSetTag(2, code);
  }

  public void init()
  {
    initPins(0,0,0,1);
    setSize(70,22);

    initPinVisibility(false,false,false,true);
    
    element.jSetInnerBorderVisibility(false);

    setPin(0,ExternalIF.C_INTEGER,element.PIN_INPUT);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    //element.jSetPinDescription(0,"out");

    setName("AVR-PWM");
    
    
    generateCode();

  }
  

  public void initInputPins()
  {
  }
  public void initOutputPins()
  {
  }
  public void start()
  {
  }

  public void process()
  {
  }

  public void setPropertyEditor()
  {
    element.jAddPEItem("PWM-Nr",value, 0,2);

  }
  public void propertyChanged(Object o)
  {
    generateCode();
    element.jRepaint();
  }


  public void loadFromStream(java.io.FileInputStream fis)
  {
    value.loadFromStream(fis);
    generateCode();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    value.saveToStream(fos);
  }


}



