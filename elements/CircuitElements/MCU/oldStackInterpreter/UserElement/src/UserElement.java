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


public class UserElement extends JVSMain
{
  private Image image;
  private VSString configCode= new VSString("");


  public VSPropertyDialog config= new VSPropertyDialog();

  
  private FileConfigParser parser;

  public void paint(java.awt.Graphics g)
  {
    if (image!=null) drawImageCentred(g,image);
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
    /*if (args!=null && args.length>=5)
    {
      sourceElementPath.setValue(args[4]);
      sourceElementIcon.setValue(args[3]);
    }*/
  }
  

    public int getInteger(String num)
    {
        try
        {
            return Integer.parseInt(num);
        }catch(Exception ex)
        {

        }
        return -1;
    }

  private void load()
  {
    //String path=element.jGetElementPath()+sourceElementPath.getValue();
    //String filename=path+"/load.txt";

      parser=new FileConfigParser(configCode.getValue());

      //String iconFilename=path+"/"+sourceElementIcon.getValue();


      setName("UserElement ["+parser.strName+"]");
      //element.jSetNameLocalized("AVRLoader ["+parser.strName+"]");
      //element.jSetCaption(parser.strName);

      //element.jSetDocFilePath(path+"/");

      String code="";
      code+="  "+parser.strProcedure;
      element.jSetTag(2, code);
  }
  
  public UserElement()
  {
      String strcode="";
      strcode+="InputPins  2\n";
      strcode+="OutputPins 1\n";
      strcode+="PIN_TYPE   0 INTEGER\n";
      strcode+="PIN_TYPE   1 INTEGER\n";
      strcode+="PIN_TYPE   2 INTEGER\n";

      strcode+="PIN_DESC   0 out\n";
      strcode+="PIN_DESC   1 inA\n";
      strcode+="PIN_DESC   2 inB\n";

      strcode+="PROCEDURE\n";
      strcode+="{\n";
      strcode+="  S_ADD\n";
      strcode+="}\n";

      configCode.setValue(strcode);
  }


  public void init()
  {
      image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
      
      load();
    
      int outC=getInteger(parser.strOutputPins);
      int inC=getInteger(parser.strInputPins);


      int maxC;

      if (inC>outC) maxC=inC; else maxC=outC;

      initPins(0,outC,0,inC);
      setSize(40,6+(10*maxC));

      initPinVisibility(false,true,false,true);

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
        }
        if (i<outC)
        {
          setPin(i,type,element.PIN_OUTPUT);
        } else
        {
          setPin(i,type,element.PIN_INPUT);
        }
        String str=parser.pinDescription[i];
        if (str!=null)
        {
          element.jSetPinDescription(i,str);
        }
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
    element.jAddPEItem("Config",config, 0,0);
  }
  
  public void propertyChanged(Object o)
  {
    if (o.equals(config))
    {
      String str=configCode.getValue();
      
      Properties frm = new Properties(element.jGetFrame(),str);
      frm.setSize(200,200);
      frm.setModal(true);
      frm.setVisible(true);

      if (frm.result==true)
      {
        String res=frm.strText;
        configCode.setValue(res);
      }

    }
    element.jRepaint();
  }


  public void loadFromStream(java.io.FileInputStream fis)
  {
    configCode.loadFromStream(fis);

    init();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    configCode.saveToStream(fos);
  }

}
