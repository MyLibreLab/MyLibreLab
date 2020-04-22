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
import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.Color;


public class Loader extends JVSMain
{
  private Image image;
  
  private boolean fileNotFound;
  private VSString sourceElementPath= new VSString();
  private VSString sourceElementIcon= new VSString();
  

  
  private FileConfigParser parser;

  public void paint(java.awt.Graphics g)
  {
     if (parser!=null && parser.strPaint.trim().length()>0)
     {
       try
       {
         ScriptEngineManager manager = new ScriptEngineManager();
         ScriptEngine engine = manager.getEngineByName("js");

         engine.put("g", g);
         engine.put("element", element);
         Object result = engine.eval(""+parser.strPaint);
       }
       catch (ScriptException ex)
       {
         System.out.println(ex);
       }
     }else
     {
      if (image!=null) drawImageCentred(g,image);
     }
  }
  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }

  public void beforeInit(String[] args)
  {
    if (args!=null && args.length>=5)
    {
      sourceElementPath.setValue(args[4]);
      sourceElementIcon.setValue(args[3]);
    }
    
  }
  

    public int getInteger(String num)
    {
        try
        {
            return Integer.parseInt(num);
        }catch(Exception ex)
        {

        }
        return 0;
    }

  private void load()
  {
    String path=element.jGetElementPath()+sourceElementPath.getValue();
    String filename=path+"/load.txt";

    if (new File(filename).exists())
    {

      System.out.println("-------------->>>>X"+filename);
    
      parser=new FileConfigParser(filename);

      String iconFilename=path+"/"+sourceElementIcon.getValue();
      image=element.jLoadImage(iconFilename);


      setName("AVRLoader ["+parser.strName+"]");
      //element.jSetNameLocalized("AVRLoader ["+parser.strName+"]");
      //element.jSetCaption(parser.strName);

      element.jSetDocFilePath(path+"/");

      String code="";
      
      // GLOBALS
      code="";
      code+="  "+parser.strGlobals;
      element.jSetTag(4, code);


      // INIT CODE
      code="";
      code+="  "+parser.strInit;
      element.jSetTag(5, code);

      // EVENT_HANDLER CODE
      code="";
      code+="  "+parser.strEventHandler;
      element.jSetTag(1, code);

      // PROCEDURE CODE
      code="";
      code+="  "+parser.strProcedure;
      element.jSetTag(2, code);
      
      fileNotFound=false;
    } else fileNotFound=true;

  }


  public void init()
  {
    if (sourceElementPath.getValue().length()>0)
    {
      //element.jSetNameLocalized("asdfasdasd");
    }

    load();
    
    if (!fileNotFound)
    {
      int top=getInteger(parser.strTopPins);
      int right=getInteger(parser.strRightPins);
      int bottom=getInteger(parser.strBottomPins);
      int left=getInteger(parser.strLeftPins);
      
      int width=getInteger(parser.strWidth);
      int height=getInteger(parser.strHeight);

      boolean PINS_VISIBLE_TOP= Boolean.valueOf(parser.strPINS_VISIBLE_TOP);
      boolean PINS_VISIBLE_RIGHT= Boolean.valueOf(parser.strPINS_VISIBLE_RIGHT);
      boolean PINS_VISIBLE_BOTTOM= Boolean.valueOf(parser.strPINS_VISIBLE_BOTTOM);
      boolean PINS_VISIBLE_LEFT= Boolean.valueOf(parser.strPINS_VISIBLE_LEFT);

      initPins(top,right,bottom,left);
      setSize(width, height);


      //if ()
      initPinVisibility(PINS_VISIBLE_TOP,PINS_VISIBLE_RIGHT,PINS_VISIBLE_BOTTOM,PINS_VISIBLE_LEFT);


      element.jSetInnerBorderVisibility(false);

      for (int i=0;i<50;i++)
      {
        int pinType=parser.pinTypes[i];
        int type=ExternalIF.C_INTEGER;

        if (pinType==0) break;

        switch(pinType)
        {
          case 1: type=ExternalIF.C_INTEGER;break;
          case 2: type=ExternalIF.C_BOOLEAN;break;
          case 3: type=ExternalIF.C_DOUBLE;break;
          case 4: type=ExternalIF.C_FLOWINFO;break;
        }
        if (parser.pinInputOutput[i].equalsIgnoreCase("OUTPUT"))
        {
          setPin(i,type,element.PIN_OUTPUT);
        } else
        if (parser.pinInputOutput[i].equalsIgnoreCase("INPUT"))
        {
          setPin(i,type,element.PIN_INPUT);
        }

        String str=parser.pinDescription[i];
        if (str!=null)
        {
          element.jSetPinDescription(i,str);
        }

      }

      for (int i=0;i<parser.propertiesC;i++)
      {

        int dt=parser.properties[i].dt;
        
        if (dt==parser.DT_BYTE)
        {
          parser.properties[i].vsObject= new VSInteger(0);
        }else
        if (dt==parser.DT_BOOLEAN)
        {
          parser.properties[i].vsObject= new VSBoolean(false);
        }else
        if (dt==parser.DT_WORD)
        {
          parser.properties[i].vsObject= new VSInteger(0);
        }else
        if (dt==parser.DT_STRING)
        {
          parser.properties[i].vsObject= new VSString("");
        }else
        if (dt==parser.DT_STRING)
        {
          parser.properties[i].vsObject= new VSFlowInfo();
        }


      }
      
      
        // Ersetze alle %get-property% durch den entsprechenden Eintrag im PropertyEditor
        int index=0;
        String str=parser.strPaint;
        while (true)
        {
            index = str.indexOf("%get-property%");

            if (index > -1)
            {
                int idx=str.indexOf("\n",index);

                //System.out.println("idx="+idx);
                String aa="%get-property%";
                String str2=str.substring(index+aa.length(), idx);
                str2=str2.trim();

                // Zeile entfernen das Wort aus dem String!
                String strOben = str.substring(0, index);

                String strMitte = "XXX";
                /*ElementProperty prop= element.getProperty(str2);


                if (prop!=null)
                {
                    if (prop.referenz!=null)
                    {
                      strMitte=""+prop.referenz.toString();
                    }
                } */
                String strUnten= str.substring(idx, str.length());

                str=strOben+strMitte+strUnten;
                System.out.println(""+str);
            }
            if (index == -1)
            {
                break;
            }
        }
        parser.strPaint=str;


    }
    
  }
  

  public void initInputPins()
  {
  }
  public void initOutputPins()
  {
  }
  public void start()
  {
  }

  public void process()
  {
  }
  public void setPropertyEditor()
  {
    if (!fileNotFound)
    {

      for (int i=0;i<parser.propertiesC;i++)
      {
        String name=parser.properties[i].name;
        int dt=parser.properties[i].dt;
        int min=parser.properties[i].min;
        int max=parser.properties[i].max;

        if (parser.properties[i].vsObject !=null)
        {

          element.jAddPEItem(name,parser.properties[i].vsObject, min, max);
        }

      }


    }
    //element.jAddPEItem("DT Input-Pin",dtPin, 0,0);
  }
  public void propertyChanged(Object o)
  {
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
    sourceElementPath.loadFromStream(fis);
    sourceElementIcon.loadFromStream(fis);

    init();

    for (int i=0;i<parser.propertiesC;i++)
    {
      parser.properties[i].vsObject.loadFromStream(fis);
    }
    element.jLoadProperties();
  }



  public void saveToStream(java.io.FileOutputStream fos)
  {
    sourceElementPath.saveToStream(fos);
    sourceElementIcon.saveToStream(fos);
    
    for (int i=0;i<parser.propertiesC;i++)
    {
      parser.properties[i].vsObject.saveToStream(fos);
    }

  }

}



