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
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.awt.geom.Rectangle2D;
import javax.swing.*;
import java.util.*;

public class SubVM extends JVSMain
{
  private Image image;
  private VSObject[] in     = new VSObject[0];
  private VSObject[] myOuts = new VSObject[0];
  private VSObject[] inX    = new VSObject[0];
  private VSObject[] outX   = new VSObject[0];
  private boolean changed=false;
  private VSBasisIF basis;
  private ExternalIF[] inputs;
  private ExternalIF[] outputs;
  private VSString relativeFilename = new VSString("");
  private String filenameX="";
  private String elementNameX="";
  private String elementIconX="";
  private VSString elementName=new VSString("");
  private VSString elementIcon=new VSString("");
  private ExternalIF panelElement;
  private VSString version= new VSString("");
  private String projektPath="";
  

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
    if (image!=null) drawImageCentred(g,image);
  }

  // Notwendig für die doc.html & ect...
  public String getBinDir()
  {
    return element.jGetSourcePath();
  }

  // Nur Sub-VM's!}
  public String jGetVMFilename()
  {
    String fn=projektPath+relativeFilename.getValue();
    return fn;
  }

  
  public void init()
  {
    //initPins(0,50,0,50);
    initPins(0,0,0,0);
    setSize(35,35);
    
    initPinVisibility(false,true,false,true);
    image=element.jLoadImage(element.jGetSourcePath()+"icon.png");

    element.jSetCaptionVisible(true);

    setName("Logic-Sub-VM");
  }

    public static void showMessage(String message)
    {
       JOptionPane.showMessageDialog(null,message,"Attention!",JOptionPane.ERROR_MESSAGE);
    }

    private String openFile()
    {
     Input frm = new Input(element.jGetFrame(),true);
     if (frm.result==null) frm.result="";
     return frm.result;
    }
  
  public void xOnInit()
  {

    String strLocale=Locale.getDefault().toString();

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
        public void actionPerformed(java.awt.event.ActionEvent evt) {

            //String fn=projektPath+relativeFilename.getValue();
            //element.jOpenVM(fn);
            if (basis!=null) basis.vsShow();
        }
    });


    if (element.jGetProjectPath().equals(""))
    {
      image=null;
      initPins(0,0,0,0);
      element.jSetCaption("");
      elementName.setValue(element.jGetCaption());

      showMessage("Please save Project first!");
      return;
    } else
    {

       projektPath=new File(element.jGetProjectPath()).getParentFile().getPath()+"/";
       //showMessage(projektPath);
    }

    if (relativeFilename.getValue().trim().equals(""))
    {
      String filename=openFile();
      relativeFilename.setValue(filename);
    }
    
    basis=element.jCreateBasis();
    String fn=projektPath+relativeFilename.getValue();
    
    if (relativeFilename.getValue().trim().equals(""))
    {
      image=null;
      initPins(0,0,0,0);
      element.jSetCaption("");
      elementName.setValue(element.jGetCaption());
      return;
    }
    
    //showMessage(fn);
    if (new File(fn).exists() )
    {
      basis.vsLoadFromFile(fn);
      element.jSetInfo("","",fn);

      if (elementName.getValue().equals(""))
      {
        element.jSetCaption(new File(fn).getName());
        elementName.setValue(element.jGetCaption());
      }

    } else
    {
      showMessage("VM \""+fn+"\"not found!");
      image=null;
      initPins(0,0,0,0);
      element.jSetCaption("");
      elementName.setValue(element.jGetCaption());

      return;
    }



    element.jSetProperties();
    //panelElement=element.getPanelElement();


    //if (panelElement!=null) panelElement.jSetCaption(elementName.getValue());
    //showMessage(elementName.getValue());
    //String iconFilename=getIconFromDefinitionDef(new File(getBinDir()));
    // showMessage(getBinDir()+"/"+iconFilename);
    //onDispose();
    //image=element.jLoadImage(getBinDir()+"/"+iconFilename);

    inputs=element.getInputPinList(basis);
    outputs=element.getOutputPinList(basis);

    int leftC=inputs.length;
    int rightC=outputs.length;
    int max=leftC;
    if (rightC>max) max=rightC;

    initPins(0,rightC,0,leftC);

    int width=40+20;
    int height=0;

    if (max*10<=40) height=40; else height=max*10;
    setSize(width, height);

    int dt=0;
    for (int i=0;i<rightC;i++)
    {
      dt=outputs[i].jGetPinDataType(0); // Pin hat nur ein Pin!
      element.jSetPinDescription(i,outputs[i].jGetCaption());
      setPin(i,dt,element.PIN_OUTPUT);
    }

    for (int i=0;i<inputs.length;i++)
    {
      dt=inputs[i].jGetPinDataType(0); // Pin hat nur ein Pin!
      element.jSetPinDescription(rightC+i,inputs[i].jGetCaption());
      setPin(rightC+i,dt,element.PIN_INPUT);
    }

  }

  public void initInputPins()
  {
    in= new VSObject[inputs.length];

    for (int i=0;i<inputs.length;i++)
    {
      in[i]=(VSObject)element.getPinInputReference(outputs.length+i);
    }
  }
  

  public void initOutputPins()
  {
    outX=new VSObject[inputs.length];
    
    for (int i=0;i<inputs.length;i++)
    {
      outX[i]=element.jCreatePinDataType(outputs.length+i);
      inputs[i].setPinOutputReference(0,outX[i]);
    }

    myOuts=new VSObject[outputs.length];

    for (int i=0;i<outputs.length;i++)
    {
      myOuts[i]=element.jCreatePinDataType(i);
      element.setPinOutputReference(i,myOuts[i]);
      
      ArrayList list = new ArrayList();
      list.add(myOuts[i]);
      list.add(new Integer(i));
      list.add(element);
      outputs[i].jSetTag(list);
    }

  }

  public void start()
  {
    if (basis!=null) basis.vsStart();
  }

  public void stop()
  {
    if (basis!=null)
    {
      basis.vsStop();
      element.jCloseFrontPanel();
    }
  }

  public void changePin(int pinIndex, Object value)
  {
      changed=true;
  }

  VSObject myInput;

  public void elementActionPerformed(ElementActionEvent evt)
  {

        if (evt.getSourcePinIndex()>=outputs.length)
        {

          int index = evt.getSourcePinIndex()-outputs.length;

          //System.out.println("index="+index);

          if (outX[index]!=null && in[index]!=null)
          {
            outX[index].copyValueFrom(in[index]);
            if (inputs[index]!=null)
            {
             inputs[index].notifyPin(0);
            }
          }
        }

  }
  
  public void process()
  {

  }
  

  public void loadFromStream(java.io.FileInputStream fis)
  {
    try
    {
      relativeFilename.loadFromStream(fis);
      elementName.loadFromStream(fis);
      elementIcon.loadFromStream(fis);
      
      //loadFromStreamAfterXOnInit(fis);
    } catch(Exception ex)
    {

    }
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    relativeFilename.saveToStream(fos);
    elementName.saveToStream(fos);
    elementIcon.saveToStream(fos);

    
    //saveToStreamAfterXOnInit(fos);
  }



  public void saveToStreamAfterXOnInit(java.io.FileOutputStream fos)
  {
    String basisVersion=basis.getBasisElementVersion();
    version.setValue(basisVersion);
    //showMessage("Saving Circuit-Element :"+ element.jGetCaption() +" basisVersion  "+version.getValue());

    version.saveToStream(fos);
    
    VSObject[] props=element.jGetProperties();

    for (int i=0;i<props.length;i++)
    {
      props[i].saveToStream(fos);
    }
  }

  public void loadFromStreamAfterXOnInit(java.io.FileInputStream fis)
  {
    String basisVersion=basis.getBasisElementVersion();
    version.loadFromStream(fis);


    //showMessage("Loading Circuit-Element :"+ element.jGetCaption() +" basisVersion  "+version.getValue());
    //showMessage("Element , basisVersion  "+element.jGetCaption()+" , " +basisVersion);


    if (version.getValue().equalsIgnoreCase(basisVersion))
    {
      try
      {
        VSObject[] props=element.jGetProperties();

        for (int i=0;i<props.length;i++)
        {
          props[i].loadFromStream(fis);
        }

      } catch(Exception ex)
      {

      }
     } else
     {
       showMessage("Version of Element "+ element.jGetCaption() +" is wrong, Element Properties not loaded!");
       showMessage("Version/basisVersion : "+version.getValue()+"  ,  "+basisVersion);
     }
  }

  private static String extractClassName(String line)
  {
      String ch;
      // gehe bis zum "=" Zeichen
      for (int i=0;i<line.length();i++)
      {
          ch=line.substring(i, i+1);

          if (ch.equals("="))
          {
              return line.substring(0, i);
          }
      }
      return "";
  }

  public void setPropertyEditor()
  {

  }

  public void propertyChanged(Object o)
  {
  }

    public String getIconFromDefinitionDef(File file)
    {
        String str;

        try {
            BufferedReader input = new BufferedReader(new FileReader(file.getAbsolutePath()+"/"+"definition.def"));
            String inputString;
            while ((inputString = input.readLine()) != null) {
                String elementClass=extractClassName(inputString);
                String elementName=inputString.substring(elementClass.length());

                elementName=elementName.trim();
                elementName=elementName.substring(1);
                elementClass=elementClass.trim();
                elementName=elementName.trim();

                if (elementClass.equalsIgnoreCase("icon"))
                {
                    return elementName;
                }
            }
            input.close();
        }catch(Exception ex)
        {
            //Tools.showMessage(ex.toString());
        }
        return "";
    }
}



