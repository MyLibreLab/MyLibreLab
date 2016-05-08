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
import MyParser.*;


public class Start extends MCUMainFlow
{
  private Image image;
  private VSBasisIF basis;
  private VSBoolean in ;
  private VSFlowInfo out = new VSFlowInfo();

  private String methodName="";
  private String varDef;
  private String[] defs;

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
        
        g2.setColor(new Color(150,255,150));
        g2.fillRoundRect(bounds.x,mitteY-distanceY,bounds.width,2*distanceY,20,20);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(bounds.x,mitteY-distanceY,bounds.width,2*distanceY,20,20);

        String caption=variable.getValue();

        FontMetrics fm = g2.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(caption,g2);

        g2.setColor(Color.BLACK);
        g.drawString(caption,mitteX-(int)(r.getWidth()/2),(int)(mitteY+fm.getHeight()/2)-3);

     }
     super.paint(g);
  }
  
  
  private void evalMethodExpression(String expression)
  {
    //String expression = "Test ( double a, string salafia, boolean c   )   ";

    if (expression.length()==0) return;
    
    int idx1=0;
    int idx2=0;

    idx1=expression.indexOf("(");
    if (idx1>-1)
    {
      methodName=expression.substring(0,idx1);
      System.out.println("MethodName="+methodName);

      idx2=expression.indexOf(")");
      if (idx2>-1)
      {
        varDef=expression.substring(idx1+1,idx2);
        varDef=varDef.trim();
        System.out.println("Definitionen : "+varDef);

        defs=varDef.split(",");

      } else element.jShowMessage("ERROR : coud not find \")\"");
    } else element.jShowMessage("ERROR : coud not find \"(\"");

  }

  private void generateCode()
  {

    int id=element.jGetID();

    String code="";
    /*code+="  DIM ELEMENT"+id+"_OLDVALUE\n";
    element.jSetTag(4, code); // Globals

    code="";
    code+="  GET_BIT PORTB,"+value.getValue()+"  \n";
    code+="  POP R0  \n";
    code+="  CMP R0, ELEMENT"+id+"_OLDVALUE\n";
    code+="  JMP_IF_A=B ELEMENT"+id+"_EXIT\n";
    code+="  GET_BIT PORTB,"+value.getValue()+"\n";
    code+="  POP ELEMENT"+id+"_OLDVALUE  \n";
    code+="  MOV %pin0%,ELEMENT"+id+"_OLDVALUE \n";
    code+="  %notify0%\n";
    code+="ELEMENT"+id+"_EXIT: \n";
    element.jSetTag(2, code);

    // Als Event_handler registrieren
    // D.h. das diese Funktion in der Hauptschleife aufgerufen wird!
    code="  CALL ELEMENT"+id+"\n";
    element.jSetTag(1, code);*/


    code="";
    code+="  ELEMENT"+id+":   //START \n";
    /*code+="  POP R0\n";
    code+="  CMP R0,1\n";
    code+="  JMP_IF_A=B %pin1%\n";
    code+="  goto DONOTING\n";*/
    element.jSetTag(2, code);

  }



  public void init()
  {
    standardWidth=130;
    width=standardWidth;
    height=40;
    toInclude="----";
    
    initPins(1,0,1,0);
    setSize(width,height);
    initPinVisibility(true,true,true,true);
    
    element.jSetInnerBorderVisibility(false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    setPin(0,element.C_BOOLEAN,element.PIN_INPUT);
    setPin(1,element.C_FLOWINFO,element.PIN_OUTPUT);

    element.jSetResizable(false);
    element.jSetCaptionVisible(false);
    element.jSetCaption("START");
    
    setName("#MCU-FLOWCHART-START#");
    variable.setValue("");
    
    generateCode();
    
  }

  public void xOnInit()
  {
    super.xOnInit();
  }
  
  public void checkPinDataType()
  {
     evalMethodExpression(variable.getValue());
  }

  public void start()
  {
    out.returnValue=null;
    out.source=null;

    out.variablenListe.clear();
    out.parameterDefinitions.clear();
    

    element.jSetTag(0,methodName);
    element.jSetTag(1,out);
    
    
    if (defs!=null)
    {
      String val;
      
      for (int i=0;i<defs.length;i++)
      {
        val=defs[i].trim();

        if (val.length()>0)
        {
          String[] defs2=val.split(" ");

          if (defs2.length==2)
          {
              String dataType=defs2[0];
              String varName=defs2[1];

              System.out.println("DT : "+dataType);
              System.out.println("VN : "+varName);

              int dt=out.getDataType(dataType);
              if (dt>-1)
              {
                out.addParamter(varName,dt);
                System.out.println("------------*** "+i+"   ->"+varName+" - "+dataType);
              } else element.jShowMessage("ERROR : coud not find datatype : "+dataType);


          }else element.jShowMessage("ERROR : in expression!");

        }
      }
    }
  }


  public void initInputPins()
  {
    in=(VSBoolean)element.getPinInputReference(0);
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(1,out);
  }

  public void process()
  {
    if (in instanceof VSBoolean && in.getValue())
    {

       System.out.println(in.getValue());
      // Lokale Variablen generieren, wenn mit dem Boolean Eingan gestartet wird.


        for (int j=0;j<out.parameterDefinitions.size();j++)
        {
            OpenVariable var= (OpenVariable)out.parameterDefinitions.get(j);
            if (var!=null)
            {
              out.addVariable(var.name,var.datatype);
            }

        }


      element.notifyPin(1);
    }
  }
  
  public void processMethod(VSFlowInfo flowInfo)
  {
    //this.out=flowInfo;
    //System.out.println("----------------------->>>>>>>>>>>>>><"+flowInfo.);
    //element.setPinOutputReference(1,flowInfo);
    element.notifyPin(1);
  }

  public void propertyChanged(Object o)
  {
    generateCode();
  }
  
  public void loadFromStream(java.io.FileInputStream fis)
  {
     variable.loadFromStream(fis);
     evalMethodExpression(variable.getValue());
  }


  public void saveToStream(java.io.FileOutputStream fos)
  {
    variable.saveToStream(fos);
  }

}

