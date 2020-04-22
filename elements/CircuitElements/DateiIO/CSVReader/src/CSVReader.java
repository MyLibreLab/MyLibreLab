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
  private VSBoolean read;
  private VSGroup out=new VSGroup();
  
  private VS1DDouble outX = new VS1DDouble(10);
  private VS1DDouble outY = new VS1DDouble(10);


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
    setSize(32+22,32+4);
    initPinVisibility(false,true,false,true);

    element.jSetInnerBorderVisibility(true);
    
    setPin(0,ExternalIF.C_GROUP,element.PIN_OUTPUT); // Out
    setPin(1,ExternalIF.C_STRING,element.PIN_INPUT); // Path
    setPin(2,ExternalIF.C_BOOLEAN,element.PIN_INPUT); // Read

    element.jSetPinDescription(0,"Out");
    element.jSetPinDescription(1,"filename");
    element.jSetPinDescription(2,"read");
    

    String fileName=element.jGetSourcePath()+"icon.gif";
    image=element.jLoadImage(fileName);
    
    element.jSetCaptionVisible(false);
    element.jSetCaption("CSV Reader");
    setName("CSV Reader");
    
    out.list.clear();
    out.list.add(outX);
    out.list.add(outY);

  }

  public void initInputPins()
  {
    path=(VSString)element.getPinInputReference(1);
    read=(VSBoolean)element.getPinInputReference(2);
    
    if (path==null) path=new VSString();
    if (read==null) read=new VSBoolean(false);
  }
  

  public void setPropertyEditor()
  {
  }

  public void propertyChanged(Object o)
  {
  }

  public void initOutputPins()
  {
    out.list.clear();
    out.list.add(outX);
    out.list.add(outY);

    element.setPinOutputReference(0,out);
  }


  public void start()
  {

  }
  

  private String extractX(String line)
  {
      String ch;
      // gehe bis zum ";" Zeichen
      for (int i=0;i<line.length();i++)
      {
          ch=line.substring(i, i+1);

          if (ch.equals(";") )
          {
              return line.substring(0, i);
          }
      }
      return "";
  }
  
  public void loadFile(String filename)
  {
    if (new File(filename).exists())
    {
      ArrayList listeX = new ArrayList();
      ArrayList listeY = new ArrayList();
      try
      {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        String str;
        while ((str = br.readLine()) != null)
        {
          String xValue=extractX(str);
          String yValue=str.substring(xValue.length()+1);


          listeX.add(xValue);
          listeY.add(yValue);
        }
        
        outX = new VS1DDouble(listeX.size());
        outY = new VS1DDouble(listeY.size());
        
        for (int i=0;i<listeX.size();i++)
        {
          String x=(String)listeX.get(i);
          String y=(String)listeY.get(i);
          
          x=x.replace(",",".");
          y=y.replace(",",".");

          //System.out.println("x="+x+"   y="+y);
          
          outX.setValue(i,Double.parseDouble(x));
          outY.setValue(i,Double.parseDouble(y));
        }

        out.list.clear();
        out.list.add(outX);
        out.list.add(outY);
        out.setChanged(true);
        
        br.close();
      }
      catch (Exception ex)
      {
        System.out.println(ex);
      }

    } else
    {
      System.out.println("Datei \" "+filename+ " \"+nicht gefunden!");
    }

  }

  public void process()
  {
    if (read.getValue())
    {
       loadFile(path.getValue());
       element.notifyPin(0);
    }
  }
  


}

