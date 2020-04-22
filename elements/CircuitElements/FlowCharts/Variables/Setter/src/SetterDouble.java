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


import MyParser.OpenVariable;
import VisualLogic.*;
import VisualLogic.variables.*;
import java.awt.*;
import java.awt.event.*;
import tools.*;
import javax.swing.*;
import java.util.*;
import tools.*;
import java.text.*;
import java.awt.geom.Rectangle2D;


public class SetterDouble extends MainFlow
{
  private Image image;
  private VSObject in=null;
  private VSBasisIF basis;


   public void paint(java.awt.Graphics g)
   {
    if (element!=null)
    {
       g2=(Graphics2D)g;
       Rectangle bounds=element.jGetBounds();
       g2.setFont(font);
       g2.setColor(Color.black);

       String caption="set("+variable.getValue()+")";
       FontMetrics fm = g2.getFontMetrics();
       Rectangle2D   r = fm.getStringBounds(variable.getValue(),g2);

       g.drawString(caption,bounds.x+5,((bounds.height) /2)+5);
       g.drawRect(bounds.x,bounds.y,bounds.width-1,bounds.height-1);
    }
    super.paint(g);
   }
   public void resizeWidth()
   {
     if (g2!=null)
     {
       g2.setFont(font);
       String caption=variable.getValue();

       FontMetrics fm = g2.getFontMetrics();
       Rectangle2D   r = fm.getStringBounds(caption,g2);
       Rectangle2D   r2 = fm.getStringBounds(toInclude,g2);

       int newWidth=(int)(r.getWidth()+r2.getWidth());
       if (newWidth>standardWidth)
       {
         int diff=(width-newWidth);
         element.jSetSize(newWidth,height);
         element.jSetLeft(element.jGetLeft()+diff);
         width=newWidth;
       } else
       {
         int diff=(width-standardWidth);
         width=standardWidth;
         element.jSetSize(width,height);
         element.jSetLeft(element.jGetLeft()+diff);
       }
     }

   }

  public void init()
  {
    standardWidth=60;
    width=standardWidth;
    height=40;
    toInclude="____SET()";


    initPins(0,0,0,1);
    setSize(width,height);
    initPinVisibility(false,false,false,true);
    
    element.jSetInnerBorderVisibility(true);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    setPin(0,element.C_DOUBLE,element.PIN_INPUT);

    element.jSetResizable(false);
    element.jSetCaptionVisible(false);
    element.jSetCaption("Setter");
    
    setName("#FLOWCHART_Setter #");
  }
  public void xOnInit()
  {
    super.xOnInit();
  }

  public void initInputPins()
  {
    in=(VSObject)element.getPinInputReference(0);
    basis=element.jGetBasis();
  }


  public void checkPinDataType()
  {
    if (element==null) return;

    basis=element.jGetBasis();
    //element.jShowMessage("Type:"+ basis.vsGetVariableDT(variable.getValue()));
    if (basis!=null && variable.getValue().trim().length()>0)
    {
      int varDT=basis.vsGetVariableDT(variable.getValue());
      if (varDT>-1)
      {

        int dt=element.C_VARIANT;

        if (varDT==0)
        {
          dt=element.C_DOUBLE;
          in=new VSDouble(0);
        }
        if (varDT==1)
        {
          dt=element.C_STRING;
          in=new VSString("");
        }
        if (varDT==2)
        {
          dt=element.C_BOOLEAN;
          in=new VSBoolean(false);
        }
        if (varDT==3)
        {
          dt=element.C_INTEGER;
          in=new VSInteger(0);
        }

        element.jSetPinDataType(0,dt);

      }
      

     element.jRepaint();
     }
  }
  

  public void process()
  {
    if (basis!=null)
    {
        if (basis.vsCopyVSObjectToVariable(in, variable.getValue())==false)
        {
          element.jShowMessage("Types unkompatible or variable unknown");
        }
        element.notifyPin(0);
    }
    
  }


}

