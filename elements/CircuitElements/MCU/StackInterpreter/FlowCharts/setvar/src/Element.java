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


public class Element extends MCUMainFlow
{
  private Image image;
  private VSBasisIF basis;
  private VSFlowInfo in=null;
  private VSFlowInfo out = new VSFlowInfo();

  private VSString paramVar= new VSString("");
  private VSString paramConst= new VSString("");

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

        String caption=""+paramVar.getValue()+"="+paramConst.getValue();
        
        drawCaption(g2,caption.trim(), 0);

    }
    super.paint(g);
  }

  private void generateCode()
  {

    int id=element.jGetID();

    String code="";

    // Überprüfe ob die Variablen oder Konstanten korrekt sind
    String errors="";
    errors+=checkProperty("var", paramVar.getValue(), MCUMainFlow.ONLY_VAR);
    errors+=checkProperty("var/const", paramConst.getValue(), MCUMainFlow.VAR_OR_CONST);


    if (errors.length()>0) element.jShowMessage(errors);



    String pa="";
    String pb="";

    // Variable oder Konstante
    if (isConst(paramConst.getValue() ))
    {
      pa="PUSH_SI_C "+paramConst.getValue();
    }else
    if (isVariable(paramConst.getValue() ))
    {
      pa="LOAD_I "+paramConst.getValue();
    }
    
    // Variable oder Konstante
    if (isVariable(paramVar.getValue() ))
    {
      pb="STORE_I "+paramVar.getValue();
    }



    code+="\n";
    code+="  ELEMENT"+id+":   // SETVAR\n";
    code+="    "+pa+" \n";
    code+="    "+pb+" \n";
    if (element.hasPinWire(1))
    {
      code+="    JMP %nextElement1%\n";
    }

    code+="\n";
    element.jSetTag(2, code);

  }

  public void xOnInit()
  {
    super.xOnInit();
  }


  public void start()
  {
    generateCode();
  }
  
  public void init()
  {
    standardWidth=130;
    width=standardWidth;
    height=40;
    toInclude="____XXX()";
    
    initPins(1,0,1,0);
    setSize(width,height);
    initPinVisibility(true,true,true,true);
    
    element.jSetInnerBorderVisibility(false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    setPin(0,element.C_FLOWINFO,element.PIN_INPUT);
    setPin(1,element.C_FLOWINFO,element.PIN_OUTPUT);
    

    element.jSetCaptionVisible(false);
    element.jSetCaption("Calculate");

    
    setName("#MCU-FLOWCHART-SETVAR#");

  }

  public void initInputPins()
  {
    in=(VSFlowInfo)element.getPinInputReference(0);
    basis=element.jGetBasis();
  }


  public void initOutputPins()
  {
    element.setPinOutputReference(1,out);
  }
  
  public void setPropertyEditor()
  {
    element.jAddPEItem("var",paramVar,0,0);
    element.jAddPEItem("var/const",paramConst,0,0);

    //localize();
  }


  public void showMessage(String message)
  {
    JOptionPane.showMessageDialog(null, message, "Attention!", JOptionPane.ERROR_MESSAGE);
  }

  public void propertyChanged(Object o)
  {

    element.jRepaint();
  }


  public void process()
  {
  }
  
  public void loadFromStream(java.io.FileInputStream fis)
  {
     paramVar.loadFromStream(fis);
     paramConst.loadFromStream(fis);


  }


  public void saveToStream(java.io.FileOutputStream fos)
  {
    paramVar.saveToStream(fos);
    paramConst.saveToStream(fos);

  }
  
}

