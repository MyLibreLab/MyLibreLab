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
package tools;

import VisualLogic.*;
import java.awt.*;
import java.awt.event.*;

public class BinAnzeige extends JVSMain
{
  private boolean value=false;
  private Image image=null;
  private Image imageON=null;
  private Image imageOFF=null;
  private String fileNameImageON;
  private String fileNameImageOFF;
  private int sizeW=30;
  private int sizeH=30;

  public BinAnzeige(String fileNameImageON, String fileNameImageOFF, int sizeW, int sizeH)
  {
   this.fileNameImageON=fileNameImageON;
   this.fileNameImageOFF=fileNameImageOFF;
   this.sizeW=sizeW;
   this.sizeH=sizeH;
  }
  
   public void paint(java.awt.Graphics g)
   {
      if (element!=null)
      {
         Graphics2D g2=(Graphics2D)g;

         g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);

         Rectangle r=element.jGetBounds();
         if (image!=null)   g2.drawImage(image,r.x+1,r.y+1,r.width-2,r.height-2,null);

      }
   }
   
  public void init()
  {
    element.jSetInnerBorderVisibility(false);
    element.jSetTopPinsVisible(false);
    element.jSetRightPinsVisible(false);
    element.jSetBottomPinsVisible(false);
    element.jSetSize(sizeW+10+2,sizeH+2);
    element.jSetTopPins(0);
    element.jSetRightPins(0);
    element.jSetBottomPins(0);
    element.jSetLeftPins(1);

    imageON=element.jLoadImage(element.jGetSourcePath()+fileNameImageON);
    imageOFF=element.jLoadImage(element.jGetSourcePath()+fileNameImageOFF);
    image=imageOFF;
  }

  private void setValue(boolean value)
  {
    if (this.value!=value)
    {
      this.value=value;
      if (value==false)
      {
        image=imageOFF;
      } else
      {
       image=imageON;
      }

      element.jRepaint();
    }
  }

  public void process()
  {
    double pin1 = element.readPin(0);
    if (pin1>0)
    {
       setValue(true);
    } else
    {
       setValue(false);
    }
  }

}
 
