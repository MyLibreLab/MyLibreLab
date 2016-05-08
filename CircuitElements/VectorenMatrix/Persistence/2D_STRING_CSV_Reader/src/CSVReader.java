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


public class CSVReader extends JVSMain
{
  private Image image;
  private VSString path;
  private VSBoolean load;

  private VS2DString out = new VS2DString(0,0);

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
    initPins(0,1,0,2);
    setSize(32+2+20,36);
    initPinVisibility(false,true,false,true);

    element.jSetInnerBorderVisibility(true);
    
    setPin(0,ExternalIF.C_ARRAY2D_STRING,element.PIN_OUTPUT); // Out
    setPin(1,ExternalIF.C_STRING,element.PIN_INPUT); // Path
    setPin(2,ExternalIF.C_BOOLEAN,element.PIN_INPUT); // Path

    element.jSetPinDescription(0,"Out");
    element.jSetPinDescription(1,"Path");
    element.jSetPinDescription(2,"Load");

    String fileName=element.jGetSourcePath()+"icon.gif";
    image=element.jLoadImage(fileName);
    
    element.jSetCaptionVisible(false);
    element.jSetCaption("ARRAY2D_STRING_CSV_READER");
    setName("ARRAY2D_STRING_CSV_READER");
    
  }

  public void initInputPins()
  {
    path=(VSString)element.getPinInputReference(1);
    load=(VSBoolean)element.getPinInputReference(2);
    
    if (path==null) path=new VSString();
    if (load==null) load=new VSBoolean();
  }
  

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }


  public void start()
  {

  }
  

  public void loadFile(String filename) throws Exception
  {
    String[][] result=null;

    if (new File(filename).exists())
    {
      try
      {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        ArrayList liste = new ArrayList(100);

        String str;
        while ((str = br.readLine()) != null)
        {
          liste.add(str);
          System.out.println(">"+str);
        }
        br.close();

        String[] temp;
        int spaltenC=0;
        String value;
        for (int zeile=0;zeile<liste.size();zeile++)
        {
          value=((String)liste.get(zeile));
          value=value.replace(",",".");
          temp=value.split(";");

          if (zeile==0)
          {
              result=new String[temp.length][liste.size()];
              spaltenC=temp.length;
          }
          else
          {
              if (temp.length!=spaltenC) throw new Exception("Columns musst be equal!");

          }
          for (int col=0;col<temp.length;col++)
          {
             result[col][zeile]=temp[col];
          }
        }

        out.setValues(result, spaltenC,liste.size());

      }
      catch (Exception ex)
      {
        System.out.println(ex);
      }

    } else
    {
      throw new Exception("File \" "+filename+ " \"+ not found!");
    }

  }

  public void process()
  {
    try
    {
      if (load.getValue())
      {
        loadFile(path.getValue());
        element.notifyPin(0);
      }
    } catch(Exception ex)
    {
      element.jException(ex.toString());
    }
  }
  


}

