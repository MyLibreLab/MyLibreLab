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


public class XFor extends MainFlow
{
  private Image image;
  private VSBasisIF basis;
  private VSFlowInfo in=null;
  private VSFlowInfo out = new VSFlowInfo();

  private VSString p_var = new VSString("i");
  private VSString p_initValue = new VSString("0");
  private VSString p_ToValue = new VSString("10");

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



        String caption="FOR("+p_var.getValue()+"="+p_initValue.getValue()+" TO "+p_ToValue.getValue()+")";
        //setValue(caption);
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
    toInclude="_____=";
    
    initPins(1,0,1,0);
    setSize(width,height);
    initPinVisibility(true,true,true,true);
    
    element.jSetInnerBorderVisibility(false);

    image=element.jLoadImage(element.jGetSourcePath()+"image.png");
    
    setPin(0,element.C_FLOWINFO,element.PIN_INPUT);
    setPin(1,element.C_FLOWINFO,element.PIN_OUTPUT);
    
    element.jSetResizable(false);
    element.jSetCaptionVisible(false);
    element.jSetCaption("FOR");
    
    setName("#FLOWCHART_FOR#");
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


  public void setPropertyEditor()
  {
    element.jAddPEItem("Variable",p_var, 0,0);
    element.jAddPEItem("InitValue",p_initValue, 0,0);
    element.jAddPEItem("ToValue",p_ToValue, 0,0);
    
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Variable");
    element.jSetPEItemLocale(d+1,language,"InitValue");
    element.jSetPEItemLocale(d+2,language,"ToValue");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Variable");
    element.jSetPEItemLocale(d+1,language,"InitValue");
    element.jSetPEItemLocale(d+2,language,"ToValue");
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
  
  
  private void firstTime()
  {
    // For zum ersten mal!
    VSInteger id= new VSInteger(element.jGetID());
    VSString varName = new VSString(p_var.getValue());
    VSString max= new VSString(p_ToValue.getValue());
    VSString expr= new VSString(varName.getValue()+"<"+max.getValue());

    ArrayList obj = new ArrayList();
    obj.add(id);
    obj.add(varName);
    obj.add(expr);
    
    stack.push(obj);

    String var=p_var.getValue();

    Object ox =basis.vsEvaluate(in,var+"="+p_initValue.getValue());
    if (ox==null)
    {
      element.jException("Error in Expression");
    }

  }

  public void process()
  {
    if (stack!=null)
    {
       if (stack.size()==0)
       {
         firstTime();
       } else
       {
         Object o = stack.lastElement();
         if (o instanceof ArrayList) // Element.ID
         {
           ArrayList obj = (ArrayList)o;

           if (obj.size()==3)
           {
           
             VSInteger id=(VSInteger)obj.get(0);
             if (id.getValue() != element.jGetID())
             {
               firstTime();
             }
             
           }
         }
       }
     }
     out.copyValueFrom(in);
     element.notifyPin(1);

  }
  
  public void loadFromStream(java.io.FileInputStream fis)
  {
     p_var.loadFromStream(fis);
     p_initValue.loadFromStream(fis);
     p_ToValue.loadFromStream(fis);
  }


  public void saveToStream(java.io.FileOutputStream fos)
  {
    p_var.saveToStream(fos);
    p_initValue.saveToStream(fos);
    p_ToValue.saveToStream(fos);
  }

}

