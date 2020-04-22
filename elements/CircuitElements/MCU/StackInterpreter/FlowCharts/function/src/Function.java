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


class Function_Item
{
  String functionName="";
  int paramCount=0;
  boolean hasResult=false;
  int paramAType=MCUMainFlow.ONLY_VAR;
  int paramBType=MCUMainFlow.ONLY_VAR;
  boolean directParam=false;
  String codeBevore="";
  String codeAfter="";
}


public class Function extends MCUMainFlow
{
  private Image image;
  private VSBasisIF basis;
  private VSFlowInfo in=null;
  private VSFlowInfo out = new VSFlowInfo();
  
  private ArrayList<Function_Item> liste = new ArrayList<Function_Item>();



  private VSComboBox function = new VSComboBox();
  private VSString paramA = new VSString("");
  private VSString paramB = new VSString("");
  private VSString result = new VSString("");

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


        String funcName=function.getItem(function.selectedIndex);

        String caption="";
        
        Function_Item item=liste.get(function.selectedIndex);

        if (item.hasResult)
        {
          caption+=result.getValue()+"=";
        }
        if (item.paramCount==0)
        {
          caption+=funcName+"()";
        } else
        if (item.paramCount==1)
        {
          caption+=funcName+"("+paramA.getValue()+")";
        } else
        if (item.paramCount==2)
        {
          caption+=funcName+"("+paramA.getValue()+","+paramB.getValue()+")";
        }

        drawCaption(g2,caption.trim(), 0);
    }
    super.paint(g);
  }

  private String parseDirectParamA(Function_Item item)
  {
    if (item.paramAType==MCUMainFlow.ONLY_VAR)
    {
      if (isVariable(paramA.getValue() ))
      {
        return paramA.getValue();
      }
    }

    return "";
  }

  
  private String parseParamA(Function_Item item)
  {
    if (item.paramAType==MCUMainFlow.ONLY_VAR)
    {
      if (isVariable(paramA.getValue() ))
      {
        return "LOAD_I "+paramA.getValue();
      }
    }else
    if (item.paramAType==MCUMainFlow.VAR_OR_CONST)
    {
      // Variable oder Konstante
      if (isConst(paramA.getValue() ))
      {
        return "PUSH_SI_C "+paramA.getValue();
      }else
      if (isVariable(paramA.getValue() ))
      {
        return "LOAD_I "+paramA.getValue();
      }
    }

    return "";
  }
  
  private String parseParamB(Function_Item item)
  {

    if (item.paramAType==MCUMainFlow.ONLY_VAR)
    {
      if (isVariable(paramB.getValue() ))
      {
        return "LOAD_I "+paramB.getValue();
      }
    }else
    if (item.paramAType==MCUMainFlow.VAR_OR_CONST)
    {
      // Variable oder Konstante
      if (isConst(paramA.getValue() ))
      {
        return "PUSH_SI_C "+paramB.getValue();
      }else
      if (isVariable(paramA.getValue() ))
      {
        return "LOAD_I "+paramB.getValue();
      }
    }

    return "";
  }

  private void generateCode(Function_Item item)
  {
    String code="";
    String pa="";
    String pb="";
    String res="";

    int id=element.jGetID();

    // Überprüfe ob die Variablen oder Konstanten korrekt sind
    /*String errors="";
    if (item.paramCount>=1) errors+=checkProperty("paramA", paramA.getValue(), item.paramAType);
    if (item.paramCount>=2) errors+=checkProperty("paramB", paramB.getValue(), item.paramBType);
    if (item.hasResult) errors+=checkProperty("result", result.getValue(), MCUMainFlow.ONLY_VAR);

    if (errors.length()>0) element.jShowMessage(errors);*/

    String functionName=function.getItem(function.selectedIndex);

    String varX="";
    
    /*if (item.paramCount==1)
    {
      if (item.directParam)
      {
        pa=parseDirectParamA(item);
      } else
      {
        pa=parseParamA(item);
      }
    }else
    if (item.paramCount==2)
    {
      pa=parseParamA(item);
      pb=parseParamB(item);
    } */
    
    
    if (item.paramCount==1)
    {
      if (item.directParam)
      {
        pa=parseDirectParamA(item);
      } else
      {
        pa=generateMCUCodeFromExpression(paramA.getValue());
      }
    }else
    if (item.paramCount==2)
    {
      pa=generateMCUCodeFromExpression(paramA.getValue());
      pb=generateMCUCodeFromExpression(paramB.getValue());
    }


    if (item.hasResult)
    {
      // nur Variable
      if (isVariable(result.getValue() ))
      {
        res="STORE_I "+result.getValue();
      }
    }

    code+="\n";
    code+="  ELEMENT"+id+":   //Function "+functionName+"\n";
    
    if (item.directParam)
    {
      code+="    "+functionName+" "+pa+" \n";
    }else
    {
      if (pa.length()>0) code+="    "+pa+" \n";
      if (pb.length()>0) code+="    "+pb+" \n";

      if (item.codeBevore.length()>0) code+="    "+item.codeBevore+" \n";
      code+="    "+functionName+" \n";
      if (item.codeAfter.length()>0) code+="    "+item.codeAfter+" \n";

      if (res.length()>0) code+="    "+res+" \n";
    }
    
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
      generateCode(liste.get(function.selectedIndex));
  }
  private Function_Item addFunction(String functionName, int paramCount, int paramAType, int paramBType, boolean hasResult, boolean directParam)
  {
    Function_Item item = new Function_Item();
    item.functionName=functionName;
    item.paramCount=paramCount;
    item.paramAType=paramAType;
    item.paramBType=paramBType;
    item.hasResult=hasResult;
    item.directParam=directParam;

    function.addItem(functionName);
    liste.add(item);
    
    return item;
  }
  
  
  public void init()
  {
    standardWidth=130;
    width=standardWidth;
    height=40;

    initPins(1,0,1,0);
    setSize(width,height);
    initPinVisibility(true,true,true,true);
    
    element.jSetInnerBorderVisibility(false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    setPin(0,element.C_FLOWINFO,element.PIN_INPUT);
    setPin(1,element.C_FLOWINFO,element.PIN_OUTPUT);
    
    element.jSetCaptionVisible(false);
    element.jSetCaption("Function");
    
    Function_Item item=null;
    
    //addFunction(String functionName, int paramCount, int paramAType, int paramBType, boolean hasResult, boolean directParam)
    
    addFunction("AND", 2, MCUMainFlow.ONLY_VAR, MCUMainFlow.VAR_OR_CONST, true, false);
    addFunction("OR",  2, MCUMainFlow.ONLY_VAR, MCUMainFlow.VAR_OR_CONST, true, false);
    addFunction("XOR", 2, MCUMainFlow.ONLY_VAR, MCUMainFlow.VAR_OR_CONST, true, false);
    addFunction("NOT", 1, MCUMainFlow.ONLY_VAR, MCUMainFlow.SIMPLE, false, false);
    
    addFunction("INC_I", 1, MCUMainFlow.ONLY_VAR, MCUMainFlow.SIMPLE, false, true);
    addFunction("DEC_I", 1, MCUMainFlow.ONLY_VAR, MCUMainFlow.SIMPLE, false, true);

    item=addFunction("SET_LED", 1, MCUMainFlow.VAR_OR_CONST, MCUMainFlow.SIMPLE, false, false);
    item.codeBevore="INT_TO_BYTE";
    
    item=addFunction("CLR_LED", 1, MCUMainFlow.VAR_OR_CONST, MCUMainFlow.SIMPLE, false, false);
    item.codeBevore="INT_TO_BYTE";

    addFunction("DELAY_MS", 1, MCUMainFlow.VAR_OR_CONST, MCUMainFlow.SIMPLE, false, false);
    
    item=addFunction("GET_BUTTONS", 0, MCUMainFlow.SIMPLE, MCUMainFlow.SIMPLE, true, false);
    item.codeAfter="BYTE_TO_INT";
    
    item=addFunction("D_INPUT", 1, MCUMainFlow.VAR_OR_CONST, MCUMainFlow.SIMPLE, true, false);
    item.codeBevore="INT_TO_BYTE";
    item.codeAfter="BYTE_TO_INT";

    item=addFunction("A_INPUT", 1, MCUMainFlow.VAR_OR_CONST, MCUMainFlow.SIMPLE, true, false);
    item.codeBevore="INT_TO_BYTE";


    addFunction("LCD_CLEAR", 0, MCUMainFlow.SIMPLE, MCUMainFlow.SIMPLE, false, false);
    addFunction("LCD_PRINT_SI", 1, MCUMainFlow.VAR_OR_CONST, MCUMainFlow.SIMPLE, false, false);

    item=addFunction("SET_RELAIS", 1, MCUMainFlow.ONLY_VAR, MCUMainFlow.SIMPLE, false,false);
    item.codeBevore="INT_TO_BYTE";
    
    item=addFunction("CLR_RELAIS", 1, MCUMainFlow.ONLY_VAR, MCUMainFlow.SIMPLE, false,false);
    item.codeBevore="INT_TO_BYTE";
    
    item=addFunction("IS_BIT_SET", 2, MCUMainFlow.VAR_OR_CONST, MCUMainFlow.VAR_OR_CONST, true,false);
    //item.codeBevore="INT_TO_BYTE";



    setName("#MCU-FLOWCHART-FUNCTION#");
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
    element.jAddPEItem("function",function,0,0);
    
    element.jAddPEItem("paramA",paramA,0,0);
    element.jAddPEItem("paramB",paramB,0,0);
    element.jAddPEItem("result",result,0,0);

    //localize();
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
     VSString func= new VSString("");
     
     func.loadFromStream(fis);
     paramA.loadFromStream(fis);
     paramB.loadFromStream(fis);
     result.loadFromStream(fis);
     
     for (int i=0;i<function.getSize();i++)
     {
       if ( function.getItem(i).equalsIgnoreCase(func.getValue()) )
       {
         function.selectedIndex=i;
       }
     }
  }


  public void saveToStream(java.io.FileOutputStream fos)
  {
    VSString func= new VSString(function.getItem(function.selectedIndex));
    func.saveToStream(fos);
    paramA.saveToStream(fos);
    paramB.saveToStream(fos);
    result.saveToStream(fos);
  }
  
}

