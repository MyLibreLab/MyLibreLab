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
import java.text.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import java.io.*;

import java.util.ArrayList;

import VisualLogic.variables.*;


public class CSVWriter extends JVSMain
{
  private Image image;
  private VS2DString in;
  private VSString path;
  private VSBoolean store;


  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }

  public void paint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }

  public void init()
  {
    initPins(0,0,0,3);
    setSize(32+2+10,36);
    initPinVisibility(false,false,false,true);

    element.jSetInnerBorderVisibility(true);
    
    setPin(0,ExternalIF.C_ARRAY2D_STRING,element.PIN_INPUT); // in
    setPin(1,ExternalIF.C_STRING,element.PIN_INPUT); // Path
    setPin(2,ExternalIF.C_BOOLEAN,element.PIN_INPUT); // Store

    element.jSetPinDescription(0,"In");
    element.jSetPinDescription(1,"Path");
    element.jSetPinDescription(2,"Store");

    String fileName=element.jGetSourcePath()+"icon.gif";
    image=element.jLoadImage(fileName);
    
    element.jSetCaptionVisible(false);
    element.jSetCaption("ARRAY2D_STRING_CSV_WRITER");
    setName("ARRAY2D_STRING_CSV_WRITER");
    
  }

  public void initInputPins()
  {
    in=(VS2DString)element.getPinInputReference(0);
    path=(VSString)element.getPinInputReference(1);
    store=(VSBoolean)element.getPinInputReference(2);
    
    if (in==null) in=new VS2DString(0,0);
    if (path==null) path=new VSString();
    if (store==null) store=new VSBoolean(false);
  }
  


  public void saveFile(String filename) throws Exception
  {
      BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));

      String str,value;
      for (int row=0;row<in.getRows();row++)
      {
        str="";

        for (int col=0;col<in.getColumns();col++)
        {
          value=in.getValue(col,row);
          if (value==null) value="";
          value=value.replace(".",",");
          str+=""+value+";";
        }
        br.write(str);
        br.newLine();
      }
      
      br.close();
  }

  public void process()
  {
    try
    {
      if (store.getValue())
      {
        saveFile(path.getValue());
      }
    } catch(Exception ex)
    {
      element.jException(ex.toString());
    }
  }
  


}

