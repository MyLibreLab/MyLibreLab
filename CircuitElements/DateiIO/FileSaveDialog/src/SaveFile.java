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

public class SaveFile extends JVSMain
{
  public VSBoolean okay=new VSBoolean(false);
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
    initPins(0,2,0,1);
    setSize(32+22,32+4);
    initPinVisibility(false,true,false,true);
    
    String fileName=element.jGetSourcePath()+"icon.gif";
    image=element.jLoadImage(fileName);

    setPin(0,ExternalIF.C_STRING,element.PIN_OUTPUT); // Out
    setPin(1,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT); // Okay
    setPin(2,ExternalIF.C_BOOLEAN,element.PIN_INPUT); // Path

    element.jSetPinDescription(0,"filename");
    element.jSetPinDescription(1,"store");
    element.jSetPinDescription(2,"execute");
    
    element.jSetCaptionVisible(false);
    element.jSetCaption("FileSave Dialog");

    setName("FileSave Dialog");
    extension.setValue("txt");

  }


  public void initInputPins()
  {
    ok=(VSBoolean)element.getPinInputReference(2);

    if (ok==null)
    {
      ok=new VSBoolean(false);
    }
  }
  
  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
    element.setPinOutputReference(1,okay);
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


    public boolean setQuestionDialog(JFrame parent, String s)
    {
        int res = JOptionPane.showOptionDialog(parent, s, "Attention",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,null, null,null);
        if(res == JOptionPane.NO_OPTION) return false;
        return true;
    }

    private boolean isWriteable(File f)
    {
        if(f.exists())
        {
            return setQuestionDialog(element.jGetFrame(), "File already exist! OverrideFile?");
        }
        return true;
    }

  
  public String goAndGetFile()
  {
    JFileChooser chooser  = new JFileChooser();
    chooser.setCurrentDirectory(new java.io.File(letztesVerzeichniss));

    ExtensionFileFilter filter = new ExtensionFileFilter(  );

    filter.setDescription(extension.getValue());
    filter.addExtension(extension.getValue());
    chooser.addChoosableFileFilter(filter);


    int returnVal = chooser.showSaveDialog(element.jGetFrame());

    if(returnVal == JFileChooser.APPROVE_OPTION)
    {
      letztesVerzeichniss=chooser.getCurrentDirectory().getPath();
      
      File file = chooser.getSelectedFile();
      String fileName=file.getPath();

      String s = file.getName();
      int i = s.lastIndexOf('.');

      if (i > 0)
      {
      }
      else
      {
          fileName+="."+extension.getValue();
      }

      return fileName;
    } else return "";
  }
  
  
  boolean changed=false;
  
  public void destElementCalled()
  {
    if (changed)
    {
      System.out.println("CANCELLED");
      okay.setValue(false);
      element.notifyPin(1);
      changed=false;
    }
  }

  public void process()
  {
    if (ok.getValue())
    {
      String filename=goAndGetFile();

      if (filename!="")
      {
      
       if(isWriteable(new File(filename)))
       {
          out.setValue(filename);
          element.notifyPin(0);
          okay.setValue(true);
          element.notifyPin(1);
          changed=true;
          element.jNotifyWhenDestCalled(1,element);
        }
      }
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


