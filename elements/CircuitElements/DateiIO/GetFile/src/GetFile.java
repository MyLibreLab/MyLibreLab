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
import javax.swing.filechooser.*;
import java.io.File;

public class GetFile extends JVSMain
{
  public VSString out=new VSString();
  public VSString extension = new VSString();
  public VSBoolean ok;
  private Image image;
  
  private String letztesVerzeichniss=".";

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
    initPins(0,1,0,1);
    setSize(32+22,32+4);
    initPinVisibility(false,true,false,true);
    
    String fileName=element.jGetSourcePath()+"icon.gif";
    image=element.jLoadImage(fileName);

    setPin(0,ExternalIF.C_STRING,element.PIN_OUTPUT); // Out
    setPin(1,ExternalIF.C_BOOLEAN,element.PIN_INPUT); // Path

    element.jSetPinDescription(0,"Out");
    element.jSetPinDescription(1,"Process");
    
    element.jSetCaptionVisible(true);
    element.jSetCaption("GetFile");

    setName("GetFile");
    extension.setValue("txt");
    
    out.setPin(0);
  }


  public void initInputPins()
  {
    ok=(VSBoolean)element.getPinInputReference(1);

    if (ok==null)
    {
      ok=new VSBoolean(false);
    }
  }
  
  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }
  
  public void setPropertyEditor()
  {
    element.jAddPEItem("Datei-Erweiterung",extension, 0,0);
    localize();
  }


  public void start()
  {

      out.setValue("");
      element.addToProcesslist(out);
  }

  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"File Extension");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Extensión Fichero");
  }
  public void propertyChanged(Object o)
  {
    element.jRepaint();
  }

  
  public String goAndGetFile()
  {
    JFileChooser chooser  = new JFileChooser();
    chooser.setCurrentDirectory(new java.io.File(letztesVerzeichniss));

    ExtensionFileFilter filter = new ExtensionFileFilter(  );


    String [] temp = null;
    temp = extension.getValue().split(",");

   for (int i = 0 ; i < temp.length ; i++)
   {
     filter.addExtension(temp[i]);
   }

    filter.setDescription(extension.getValue());

    chooser.addChoosableFileFilter(filter);


    int returnVal = chooser.showOpenDialog(null);

    if(returnVal == JFileChooser.APPROVE_OPTION)
    {
      letztesVerzeichniss=chooser.getCurrentDirectory().getPath();
      return chooser.getSelectedFile().getAbsolutePath();
    } else return "";
  }
  
  public void process()
  {
    if (ok.getValue())
    {
      String filename=goAndGetFile();
      out.setValue(filename);
      element.addToProcesslist(out);
    }
  }
  
  public void resetValues()
  {
    out.setChanged(false);
  }


  public void loadFromStream(java.io.FileInputStream fis)
  {
      extension.loadFromStream(fis);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      extension.saveToStream(fos);
  }


}


