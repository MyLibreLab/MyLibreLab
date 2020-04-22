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
import java.io.*;
import java.util.*;
import java.awt.geom.Rectangle2D;


public class Procedure extends MainFlow
{
  private Image image;
  private VSBasisIF basis;
  private VSBasisIF basis2;
  
  private VSFlowInfo in=null;
  private VSFlowInfo out = new VSFlowInfo();
  private VSString vmFilename = new VSString("");
  private boolean gesperrt=false;
  private String projektPath="";

  private String methodName;
  private String resultVar="";
  private String varDef;
  private String[] defs=null;



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
        
        g2.setColor(new Color(255,170,80));
        g2.fillRect(bounds.x,mitteY-distanceY,bounds.width,2*distanceY);
        g2.setColor(Color.BLACK);
        g2.drawRect(bounds.x,mitteY-distanceY,bounds.width,2*distanceY);
        
        g2.drawLine(bounds.x+10,bounds.y,bounds.x+10,bounds.y+bounds.height);
        g2.drawLine(bounds.x+bounds.width-10,bounds.y,bounds.x+bounds.width-10,bounds.y+bounds.height);

        String caption=variable.getValue();

        FontMetrics fm = g2.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(caption,g2);

        g2.setColor(Color.BLACK);
        g.drawString(caption,mitteX-(int)(r.getWidth()/2),(int)(mitteY+fm.getHeight()/2)-3);

     }
     super.paint(g);
  }


  public void init()
  {
    standardWidth=130;
    width=standardWidth;
    height=40;
    toInclude="-------";
    
    initPins(1,0,1,0);
    setSize(width,height);
    initPinVisibility(true,true,true,true);
    
    element.jSetInnerBorderVisibility(false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    setPin(0,element.C_FLOWINFO,element.PIN_INPUT);
    setPin(1,element.C_FLOWINFO,element.PIN_OUTPUT);
    
    element.jSetResizable(false);
    element.jSetCaptionVisible(false);
    element.jSetCaption("Procedure");
    
    setName("#FLOWCHART_PROCEDURE#");
    variable.setValue("");
    
    if (element.jGetProjectPath().equals(""))
    {
      showMessage("Flowchart \"Procedure\" : Please save Project first!");
      return ;
    } else
    {
       projektPath=new File(element.jGetProjectPath()).getParentFile().getPath()+"/";
    }
  }

  public void xOnInit()
  {
    super.xOnInit();
    
    String strLocale=Locale.getDefault().toString();
    

    System.out.println("projektPath="+projektPath);

    String strDisplayVM="";
    if (strLocale.equalsIgnoreCase("de_DE"))
    {
      strDisplayVM="zeige VM";
    }
    else
    if (strLocale.equalsIgnoreCase("en_US"))
    {
       strDisplayVM="show VM";
    }
    else
    if (strLocale.equalsIgnoreCase("es_ES"))
    {
      strDisplayVM="show VM";
    }
    element.jClearMenuItems();
    JMenuItem item1=new JMenuItem(strDisplayVM);
    element.jAddMenuItem(item1);

    item1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt)
        {
          if (basis2!=null) basis2.vsShow();
        }
    });

    basis=element.jGetBasis();

  }
  

  public void setPropertyEditor()
  {
    element.jAddPEItem("vm-Filename",vmFilename, 0,0);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"vm-Filename");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"vm-Filename");
  }

  public void propertyChanged(Object o)
  {
    load();
  }



  public void initInputPins()
  {
    in=(VSFlowInfo)element.getPinInputReference(0);

  }
  
  public static void showMessage(String message)
  {
    JOptionPane.showMessageDialog(null,message,"Attention!",JOptionPane.ERROR_MESSAGE);
  }

  public VSBasisIF loadBasis(String vmName)
  {
    VSBasisIF result=element.jCreateBasis();
    
    String fn=projektPath+vmName;

    System.out.println("loadBasis  ="+fn);
    if (new File(fn).exists() )
    {
      result.vsLoadFromFile(fn);
    }else
    {
      result=element.jGetBasis();
    }

    
    return result;
  }
  
  public void start()
  {
    element.jSetTag(0,methodName);

    gesperrt=false;
    

    if (basis!=basis2)
    {
      basis2.vsShowFrontPanelWhenStart(false);
      basis2.vsStart();
    }

  }

  public void stop()
  {
    if (basis!=basis2)
    {
      basis2.vsShowFrontPanelWhenStart(true);
      basis2.vsStop();
    }

  }

  public void initOutputPins()
  {
    element.setPinOutputReference(1,out);
  }

  // Wird aufgerufen nachdem Return aufgerufen worden ist!
  public void returnFromMethod(Object result)
  {
    if (result!=null && resultVar.length()>0)
    {
      basis.vsEvaluate(in,resultVar+"="+result.toString()) ;
    }

    out.copyValueFrom(in);
    element.notifyPin(1);
    gesperrt=false;
  }
  
  public void process()
  {
    if (basis!=null)
    {
      //if (in.getValue()==true)
      {
        if (gesperrt==false)
        {

          if (basis2!=null)
          {
            gesperrt=true;


            ArrayList params = new ArrayList();
            if (defs!=null)
            {
              for (int i=0;i<defs.length;i++)
              {
                String definition=defs[i].trim();
                Object o=basis.vsEvaluate(in,definition);
                params.add(o);
              }
            }

            if (methodName.length()>0)
            {
              basis2.vsStartSubRoutine(element,methodName,params);
            } else
            {
              out.copyValueFrom(in);
              element.notifyPin(1);
            }

          }
          
        }

      }
    }
  }
  
  
  private void evalMethodExpression(String expression)
  {
    if (expression.length()==0) return;
    //String expression = "Test ( double a, string salafia, boolean c   )   ";

    int idx1=0;
    int idx2=0;
    
    int idx=expression.indexOf("=");
    if (idx>-1)
    {
      resultVar=expression.substring(0,idx);
      expression=expression.substring(idx+1,expression.length());
      System.out.println("Result         = "+resultVar);
      System.out.println("def expression ="+expression);
    }

    idx1=expression.indexOf("(");
    if (idx1>-1)
    {
      methodName=expression.substring(0,idx1);
      //System.out.println("MethodName="+methodName);

      idx2=expression.indexOf(")");
      if (idx2>-1)
      {
        varDef=expression.substring(idx1+1,idx2);
        varDef=varDef.trim();
       // System.out.println("Definitionen : "+varDef);

        defs=varDef.split(",");

        String val;
        for (int i=0;i<defs.length;i++)
        {
          val=defs[i].trim();
          System.out.println("value = "+val);
        }

        /*VSFlowInfo info= new VSFlowInfo();

        String val;
        for (int i=0;i<defs.length;i++)
        {
          val=defs[i].trim();

          String[] defs2=val.split(" ");

          if (defs2.length==2)
          {
              String dataType=defs2[0];
              String varName=defs2[1];

              int dt=info.getDataType(dataType);
              if (dt>-1)
              {
                info.addVariable(varName,dt);
              } else element.jShowMessage("ERROR : coud not find datatype : "+dataType);
              System.out.println(defs2[0]+" - "+defs2[1]);
          }else element.jShowMessage("ERROR : in expression!");
        } */
      } else element.jShowMessage("ERROR : coud not find \")\"");
    } else element.jShowMessage("ERROR : coud not find \"(\"");

  }
  
  public void checkPinDataType()
  {
     evalMethodExpression(variable.getValue());
  }


  private void load()
  {
    String fn=vmFilename.getValue()+".vlogic";
    System.out.println("FN="+fn);

    basis2=loadBasis(fn);
  }
  
  public void loadFromStream(java.io.FileInputStream fis)
  {
     variable.loadFromStream(fis);
     vmFilename.loadFromStream(fis);
     load();
     evalMethodExpression(variable.getValue());
  }


  public void saveToStream(java.io.FileOutputStream fos)
  {
    variable.saveToStream(fos);
    vmFilename.saveToStream(fos);
  }
  
}

