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
import java.awt.event.*;
import java.util.HashMap;
import java.util.Iterator;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import java.util.*;
import org.jfree.chart.JFreeChart;



public class Fuzzy extends JVSMain
{
  private Image image;
  private VSDouble[] inputs;
  private VSDouble[] outputs;

  private ArrayList<MyNode> inputList;
  private ArrayList<MyNode> outputList;
  
  private VSPropertyDialog text = new VSPropertyDialog();
  public  VSBoolean showGraphs= new VSBoolean(false);
  private VSString textVar = new VSString("");
  public  VSPropertyDialog assistent= new VSPropertyDialog();


  public void paint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }

  public void onDispose()
  {
    System.out.println("FUZZY DISPOSED");
    
    if (image!=null)
    {
      image.flush();
      image=null;
    }
    
    boolean old = showGraphs.getValue();
    showGraphs.setValue(false);
    propertyChanged(showGraphs);
    showGraphs.setValue(old);
    

  }
  
  FuzzyRuleSet fuzzyRuleSet ;
  
  
  ArrayList<DialogChart> liste = new ArrayList<DialogChart>();
  
  
  public DialogChart getChartDialog(Variable var, MyNode node, int type)
  {
    
    for (int i=0;i<liste.size();i++)
    {
      DialogChart frm = liste.get(i);
      
      if (frm!=null)
      {
        if (type==0)
        {
          if (frm.var!=null && frm.var.equals(var))  return frm;
        }else
        if (type==1)
        {

          if (frm.node!=null && frm.node.equals(node)) return frm;
        }

      }


    }
    
    return null;
  }
  
  public void init()
  {

    image=element.jLoadImage(element.jGetSourcePath()+"icon.png");

    // Load from 'FCL' file

    FIS fis =null;
    
    try
    {
      if (textVar.getValue().length()==0)
      {

        String fileName = element.jGetSourcePath()+"fuzzy.fcl";
        fis = FIS.load(fileName,false);
        textVar.setValue(fis.toStringFCL());

      }else
      {
        fis = FIS.createFromString(textVar.getValue(),false);
      }

    } catch(Exception ex)
    {
       element.jException(" There are Errors in the FLC! : "+ex);
       return;
    }


    if( fis == null )
    { // Error while loading?
        //System.err.println("Error);
        return;
    }
    // Show ruleset
    fuzzyRuleSet = fis.getFuzzyRuleSet();



    Iterator item=fuzzyRuleSet.variablesIterator();
    

    inputList=new ArrayList<MyNode>();
    outputList=new ArrayList<MyNode>();

    for ( Iterator<Variable> flavoursIter = item; flavoursIter.hasNext(); )
    {
        Variable var=flavoursIter.next();

        MyNode node = new MyNode();
        node.description=var.getName();

        if (var.isOutputVarable())
        {
          outputList.add(node);
        }else
        {
          inputList.add(node);
        }

    }
    initInOutputs();
    
  }



  public void initInOutputs()
  {
    int inCount=inputList.size();
    int outCount=outputList.size();
    int max=0;
    
    if (inCount>outCount)
    {
      max=inCount;
    } else
    {
      max=outCount;
    }
    
    initPins(0,outCount,0,inCount);
    setSize(50,32+max*10);
    element.jSetInnerBorderVisibility(true);
    element.jSetTopPinsVisible(false);
    element.jSetBottomPinsVisible(false);


    element.jInitPins();
    
    Iterator item=fuzzyRuleSet.variablesIterator();
    
    int c=0;
    for (int i=0;i<outputList.size();i++ )
    {
        MyNode node = outputList.get(i);

        node.index=c;
        node.obj=new VSDouble(0.0);
        

        setPin(c,ExternalIF.C_DOUBLE,element.PIN_OUTPUT);
        element.jSetPinDescription(c,node.description);
        c++;
    }
    for (int i=0;i<inputList.size();i++ )
    {
        MyNode node = inputList.get(i);
        node.index=c;
        node.obj=null;

        setPin(c,ExternalIF.C_DOUBLE,element.PIN_INPUT);
        element.jSetPinDescription(c,node.description);
        c++;
    }


    setName("FuzzyControl");
  }
  
  public void setPropertyEditor()
  {
    element.jAddPEItem("show Graphs",showGraphs, 0,0);
    element.jAddPEItem("Fuzzy Logic Language (flc)",text, 0,0);
    //element.jAddPEItem("Assistent for (flc)",assistent, 0,0);


    localize();
  }

  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"show Graphs");
    element.jSetPEItemLocale(d+1,language,"Fuzzy Logic Language (flc)");
    //element.jSetPEItemLocale(d+2,language,"Assistent for flc");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"show Graphs");
    element.jSetPEItemLocale(d+1,language,"Fuzzy Logic Language (flc)");
    //element.jSetPEItemLocale(d+2,language,"Assistent for flc");
  }

  public void propertyChanged(Object o)
  {
    /*if (o.equals(assistent))
    {
       DialogEditor frm = new DialogEditor(null,true);
       frm.setVisible(true);
       if (frm.result.length()>0)
       {
         String code=frm.result;
         textVar.setValue(code);
       }
    }*/
    
    if (o.equals(showGraphs))
    {

      for( Iterator<Variable> it = fuzzyRuleSet.variablesIterator(); it.hasNext(); )
      {
          Variable var = it.next();

          JFreeChart chart=var.chart(false);

          DialogChart frm=getChartDialog(var, null, 0);
          if (showGraphs.getValue())
          {
            if (frm==null)
            {
              String str="INPUT : ";
              if (var.isOutputVarable()) str="OUTPUT : ";
              frm = new DialogChart(null, false, str+var.getName() );
              frm.init(chart, var, null);
              liste.add(frm);
            }


            frm.setVisible(true);
          }else
          {
            if (frm!=null)
            {
              liste.remove(frm);
              frm.close();
            }
          }

      }
      
      processOutCharts();
      

      //init();
    }
    if (o.equals(text))
    {
      NewJDialog frm = new NewJDialog(null,true);
      frm.jEditorPane1.setText(textVar.getValue());
      frm.setVisible(true);
      
      if (frm.result)
      {
        System.out.println("##############################");
        textVar.setValue(frm.text);
        
        init();
      }
     //loadImage(file.getValue());
    }
  }



  public void initInputPins()
  {
  
    for (int i=0;i<inputList.size();i++ )
    {
        MyNode node = inputList.get(i);

        node.obj=(VSDouble)element.getPinInputReference(node.index);
        if (node.obj==null)
        {
          node.obj= new VSDouble(0.0);
        }
    }
    
  }

  public void initOutputPins()
  {
    for (int i=0;i<outputList.size();i++ )
    {
        MyNode node = outputList.get(i);
        element.setPinOutputReference(node.index,node.obj);
    }

  }


  public void start()
  {
  }
  
  public MyNode holeNode(int index)
  {
    for (int i=0;i<inputList.size();i++ )
    {
        MyNode node = inputList.get(i);
        
        if (index==node.index)
        {
          return node;
        }
    }
    
    return null;
  }
  
  public void loadFromStream(java.io.FileInputStream fis)
  {
    textVar.loadFromStream(fis);
    showGraphs.loadFromStream(fis);
    init();
    
    propertyChanged(showGraphs);
    //processOutCharts();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    textVar.saveToStream(fos);
    showGraphs.saveToStream(fos);
  }



  private void processOutCharts()
  {

      for (int i=0;i<outputList.size();i++ )
      {
        MyNode node = outputList.get(i);
        
        if (node!=null)
        {
          JFreeChart chart=fuzzyRuleSet.getVariable(node.description).chartDefuzzifier(false);

          DialogChart frm=getChartDialog(null, node, 1);
          if (showGraphs.getValue())
          {
            if (frm==null)
            {
              frm = new DialogChart(null, false, "DEFUZZIFIER : "+node.description);
              frm.init(chart, null, node);
              liste.add(frm);
            }
            frm.updateChart(chart, null, node);
            frm.setVisible(true);

          }else
          {
            if (frm!=null)
            {
              liste.remove(frm);
              frm.close();
            }
          }

        }
      }

  }
  
  public void elementActionPerformed(ElementActionEvent evt)
  {

    int idx=evt.getSourcePinIndex();

    MyNode node=holeNode(idx);
    if (node!=null)
    {
      fuzzyRuleSet.setVariable(node.description, node.obj.getValue());
      fuzzyRuleSet.evaluate();


      processOutCharts();

      for (int i=0;i<outputList.size();i++ )
      {
        node = outputList.get(i);

       // JFreeChart chart=fuzzyRuleSet.getVariable(node.description).chartDefuzzifier(true);

        double value=fuzzyRuleSet.getVariable(node.description).getLatestDefuzzifiedValue();
        node.obj.setValue(value);
        element.notifyPin(node.index);
      }

    }
    


  }


}

class MyNode
{
  String description="";
  VSDouble obj=null;
  int index=-1;
}

