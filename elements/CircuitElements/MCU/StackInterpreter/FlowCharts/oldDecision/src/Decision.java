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
import java.text.*;
import javax.swing.*;
import java.util.*;
import tools.*;
import java.awt.geom.Rectangle2D;

public class Decision extends MCUMainFlow
{
  private Image image;
  private VSBasisIF basis;
  private VSFlowInfo in=null;
  private VSFlowInfo outY = new VSFlowInfo();
  private VSFlowInfo outN = new VSFlowInfo();
  private String[] strElements=null;

  private VSString _var = new VSString("");
  private VSComboBox _op = new VSComboBox();

  private VSString _varConst = new VSString("");


  public void paint(java.awt.Graphics g)
  {
     if (element!=null)
     {
        Rectangle bounds=element.jGetBounds();
        g2 = (Graphics2D) g;

        g2.setFont(font);
        
        int mitteX=bounds.x+(bounds.width)/2;
        int mitteY=bounds.y+(bounds.height)/2;

        int distanceY=6;

        Polygon p2 = new Polygon();
        p2.addPoint(mitteX,bounds.y);
        p2.addPoint(bounds.x-2+bounds.width,mitteY-distanceY);
        p2.addPoint(bounds.x-2+bounds.width,mitteY+distanceY);
        p2.addPoint(mitteX, bounds.y+bounds.height);
        p2.addPoint(bounds.x,mitteY+distanceY);
        p2.addPoint(bounds.x,mitteY-distanceY);


        g2.setColor(new Color(204,255,204));
        g2.fillPolygon(p2);
        g2.setColor(Color.BLACK);
        g2.drawPolygon(p2);


        String strNot="";
        String strOP="";
        
        strOP=_op.getItem(_op.selectedIndex);
        String caption="if("+_var.getValue()+strOP+_varConst.getValue()+")";

        drawCaption(g2,caption,40);


        g2.setColor(new Color(180,0,0));


        g.drawString("N",bounds.x+bounds.width,mitteY-2);
        g2.setColor(new Color(0,170,0));
        g.drawString("Y",mitteX+8,bounds.y+bounds.height+9);
     }
     
     super.paint(g);

  }
  
  private void generateCode()
  {

    int id=element.jGetID();

    String code="";

    String pa="";
    String pb="";
    String res="";
    

    // Überprüfe ob die Variablen oder Konstanten korrekt sind
    String errors="";
    errors+=checkProperty("var", _var.getValue(), MCUMainFlow.ONLY_VAR);
    errors+=checkProperty("var/const", _varConst.getValue(), MCUMainFlow.VAR_OR_CONST);
    
    if (errors.length()>0) element.jShowMessage(errors);
    

    // Nur Variable!!
    if (isVariable(_var.getValue() ))
    {
      pa="LOAD_I "+_var.getValue();
    }
    
    // Variable oder Konstante
    if (isConst(_varConst.getValue() ))
    {
      pb="PUSH_SI_C "+_varConst.getValue();
    }else
    if (isVariable(_varConst.getValue() ))
    {
      pb="LOAD_I "+_varConst.getValue();
    }

    String op=_op.getItem(_op.selectedIndex);


    code="";
    code+="\n";
    code+="  ELEMENT"+id+":   //DECISION\n";
    
    code+="    "+pa+" \n";
    code+="    "+pb+" \n";
    
    code+="    CMP_SI \n";


    if (element.hasPinWire(2))
    {
      code+="    JMP_IF_A"+op+"B  %nextElement2%\n";
    }
    code+="    //ELSE\n";
    if (element.hasPinWire(1))
    {
      code+="    JMP %nextElement1%\n";
    }

    code+="\n";
    element.jSetTag(2, code);

  }
  

  public void init()
  {
    standardWidth=130;
    width=standardWidth;
    height=50;

    initPins(1,1,1,0);
    setSize(width,height);
    initPinVisibility(true,true,true,true);
    
    element.jSetInnerBorderVisibility(false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    setPin(0,element.C_FLOWINFO,element.PIN_INPUT);
    setPin(1,element.C_FLOWINFO,element.PIN_OUTPUT);
    setPin(2,element.C_FLOWINFO,element.PIN_OUTPUT);
    
    element.jSetCaption("DECISION");
    

    setName("#MCU-FLOWCHART-DECISION#");
    
    _op.addItem("=");
    _op.addItem("<");
    _op.addItem(">");
    _op.addItem("<=");
    _op.addItem(">=");
    _op.addItem("!=");
    

  }
  

  public void xOnInit()
  {
    super.xOnInit();
  }
  
  public void setPropertyEditor()
  {

    element.jAddPEItem("var",_var,0,0);
    element.jAddPEItem("op",_op,0,0);
    element.jAddPEItem("var/const",_varConst,0,0);

    //localize();
  }


  public void propertyChanged(Object o)
  {


    element.jRepaint();
  }

  public void initInputPins()
  {
    in=(VSFlowInfo)element.getPinInputReference(0);
    basis=element.jGetBasis();
  }


  public void initOutputPins()
  {
    element.setPinOutputReference(1,outN);
    element.setPinOutputReference(2,outY);
  }

  public void start()
  {
    generateCode();
  }
  


  public void process()
  {

  }
  
  public void loadFromStream(java.io.FileInputStream fis)
  {
    _var.loadFromStream(fis);
    _op.loadFromStream(fis);
    _varConst.loadFromStream(fis);
  }


  public void saveToStream(java.io.FileOutputStream fos)
  {
    _var.saveToStream(fos);
    _op.saveToStream(fos);
    _varConst.saveToStream(fos);

  }

}



