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
import java.awt.*;
import java.awt.event.*;
import tools.*;
import javax.swing.*;
import java.util.*;
import java.awt.geom.Rectangle2D;


public class XWend extends MainFlow
{
  private Image image;
  private VSBasisIF basis;
  private VSFlowInfo in=null;
  private VSFlowInfo out = new VSFlowInfo();


  private Stack stack;

  public void paint(java.awt.Graphics g)
  {
     if (element!=null)
     {
        Rectangle bounds=element.jGetBounds();
        Graphics2D g2 = (Graphics2D) g;

        g2.setFont(font);

        int mitteX=bounds.x+(bounds.width)/2;
        int mitteY=bounds.y+(bounds.height)/2;

        int distanceY=10;
        
        g2.setColor(new Color(204,204,255));
        g2.fillRect(bounds.x,mitteY-distanceY,bounds.width,2*distanceY);
        g2.setColor(Color.BLACK);
        g2.drawRect(bounds.x,mitteY-distanceY,bounds.width,2*distanceY);


        String caption="WEND";
        variable.setValue(caption);

        FontMetrics fm = g2.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(caption,g2);

        g2.setColor(Color.BLACK);
        g.drawString(caption,mitteX-(int)(r.getWidth()/2),(int)(mitteY+fm.getHeight()/2)-3);


     }
     super.paint(g);
  }

  public String getValue()
  {
    return variable.getValue();
  }

  public void init()
  {
    standardWidth=130;
    width=standardWidth;
    height=40;
    toInclude="______=";
    
    initPins(1,0,1,0);
    setSize(width,height);
    initPinVisibility(true,true,true,true);
    
    element.jSetInnerBorderVisibility(false);

    image=element.jLoadImage(element.jGetSourcePath()+"image.png");
    
    setPin(0,element.C_FLOWINFO,element.PIN_INPUT);
    setPin(1,element.C_FLOWINFO,element.PIN_OUTPUT);
    
    element.jSetResizable(false);
    element.jSetCaptionVisible(false);
    element.jSetCaption("WEND");
    
    setName("#FLOWCHART_WEND#");
  }

  public void xOnInit()
  {
    super.xOnInit();
    propertyChanged(null);
  }
  
  

  public void propertyChanged(Object o)
  {
      element.jRepaint();
      resizeWidth();
      element.jRepaint();
      element.jRefreshVM();
      checkPinDataType();

  }


  public void initInputPins()
  {
    in=(VSFlowInfo)element.getPinInputReference(0);
    basis=element.jGetBasis();
    stack = basis.getStack();
  }

  // Override!
  public void mousePressedOnIdle(MouseEvent e)
  {

  }

  public void initOutputPins()
  {
    element.setPinOutputReference(1,out);
  }

  public void process()
  {

    if (stack!=null && basis!=null)
    {
       if (stack.size()>0 )
       {
         Object o = stack.lastElement();
         if (o instanceof ArrayList) // Element.ID
         {
           ArrayList obj = (ArrayList)o;

           if (obj.size()==2)
           {
             VSInteger id   = (VSInteger)obj.get(0);
             VSString  expr = (VSString)obj.get(1);

             if (basis.vsCompareExpression(in, expr.getValue())==true)
             {
               basis.vsStartElement(id.getValue());
               return;
             }else
             {
               if (stack.size()>0) stack.pop();
             }

           }
         }
       }
     }

     element.notifyPin(1);
  }
  

  public void loadFromStream(java.io.FileInputStream fis)
  {
  }


  public void saveToStream(java.io.FileOutputStream fos)
  {
  }

  
}

