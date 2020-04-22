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

public class VMElement extends JVSMain
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
  private VSString filename = new VSString("");
  private String filenameX="";
  private String elementNameX="";
  private String elementIconX="";
  private VSString elementName=new VSString("");
  private VSString elementIcon=new VSString("");
  private ExternalIF panelElement;
  private VSString version= new VSString("");
  

  public void onDispose()
  {
   if (image!=null)
   {
     image.flush();
     image=null;
   }
    if (basis!=null) basis.vsClose();

  }

  public void paint(java.awt.Graphics g)
  {
    if (image!=null) drawImageCentred(g,image);
  }

  public void beforeInit(String[] args)
  {
    if (args!=null)
    {
       if (args.length>=1)
       {
         filenameX=args[0];
       }

       if (args.length>=2)
       {
         elementNameX=args[1];
       }

       if (args.length>=3)
       {
         elementIconX=args[2];
       }
    }
  }
  
  public String jGetVMFilename()
  {
   return filename.getValue();
  }

  
  public String getBinDir()
  {
    String fn=element.jMapFile(element.jGetElementPath()+filename.getValue());
    return new File(fn).getParentFile().getPath();
  }
  
  
  public void init()
  {
    initPins(0,50,0,50);
    setSize(35,25);
    
    initPinVisibility(false,true,false,true);
    image=element.jLoadImage(element.jGetSourcePath()+"VMElement.gif");


    element.jSetCaptionVisible(true);

    setName("VM-Element");

  }
  
  
  private String openFile()
  {
    JFileChooser chooser = new JFileChooser();
    
    chooser.setCurrentDirectory(new java.io.File("."));


    chooser.setDialogTitle("Open VM");
    //chooser.setDialogType (JFileChooser.SAVE_DIALOG);

    vlogicFilter filter= new vlogicFilter();

    chooser.setFileFilter(filter);

    int value = chooser.showOpenDialog(null);

    if (value == JFileChooser.APPROVE_OPTION)
    {
      File file = chooser.getSelectedFile();
      String fileName=file.getPath();
      
      if (fileName.endsWith(".vlogic"))
      {
        return fileName;
      } else return "";
    }

     return "";
  }
  
    public static void showMessage(String message)
    {
       JOptionPane.showMessageDialog(null,message,"Attention!",JOptionPane.ERROR_MESSAGE);
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

            //String fn=element.jGetElementPath()+filename.getValue();
            //element.jOpenVM(fn);
            if (basis!=null) basis.vsShow();
        }
    });


    basis=element.jCreateBasis();


    
    if (filenameX=="")
    {
      if (filename.getValue().trim()=="")
      {
        filename.setValue(openFile());
      }
    } else
    {
      filename.setValue(filenameX);
    }

    if (filename.getValue().trim()!="")
    {
      try
      {
        String fileX=element.jGetElementPath()+filename.getValue();
        File file=new File(fileX);
        String result=element.jIfPathNotFoundThenSearch(file.getParent());
        if (result.length()>0)
        {
          fileX=result+"/"+file.getName();
          
          String f1 = new File(element.jGetElementPath()).getAbsolutePath();
          String f2 = new File(fileX).getAbsolutePath();

          String str = f2.substring(f1.length());


          filename.setValue(str);
        }

        basis.vsLoadFromFile(fileX);
      } catch (Exception ex)
      {
        showMessage("Error loading VMElement:"+filename.getValue());
      }

    }
    
    if (elementNameX!="")
    {
      elementName.setValue(elementNameX);
    }


    if (elementIconX!="")
    {
      elementIcon.setValue(elementIconX);
    }


    element.jSetProperties();
    

    panelElement=element.getPanelElement();

    element.jSetCaption(elementName.getValue());
    if (panelElement!=null) panelElement.jSetCaption(elementName.getValue());


    //showMessage(elementName.getValue());
    
    element.jSetTag(0,new File(getBinDir()));
    DefinitionDef def=getIconFromDefinitionDef(new File(getBinDir()));
    
    if (def.imagefilename.length()==0)
    {
      def.imagefilename=def.iconfilename;
    }
    String iconFilename=def.imagefilename;

    //System.out.println("def.showInnerborder="+def.showInnerborder);
    element.jSetInnerBorderVisibility(def.showInnerborder);
    
    //element.(def.fullResize);

   // showMessage(getBinDir()+"/"+iconFilename);
   if (image!=null)
   {
     image.flush();
     image=null;
   }

    image=element.jLoadImage(getBinDir()+"/"+iconFilename);



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


    String[] infos=basis.vsGetVMInfo();
    
    String fn=filename.getValue();
    infos[2]=fn;
    element.jSetInfo(infos[0],infos[1],infos[2]);
    
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
    if (basis!=null) basis.vsStop();
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
      filename.loadFromStream(fis);
      elementName.loadFromStream(fis);
      elementIcon.loadFromStream(fis);
      
      //loadFromStreamAfterXOnInit(fis);
    } catch(Exception ex)
    {

    }
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    //showMessage("save "+filename.getValue());
    filename.saveToStream(fos);
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

    public DefinitionDef getIconFromDefinitionDef(File file)
    {

        String str;
        
        DefinitionDef def = new DefinitionDef();

        try {
            BufferedReader input = new BufferedReader(new FileReader(file.getAbsolutePath()+"/"+"definition.def"));
            String inputString;
            while ((inputString = input.readLine()) != null)
            {
                String elementClass=extractClassName(inputString);
                String elementName=inputString.substring(elementClass.length());

                elementName=elementName.trim();
                elementName=elementName.substring(1);
                elementClass=elementClass.trim();
                elementName=elementName.trim();

                if (elementClass.equalsIgnoreCase("icon"))
                {
                    def.iconfilename=elementName;
                }else
                if (elementClass.equalsIgnoreCase("elementimage"))
                {
                    def.imagefilename=elementName;
                }else
                if (elementClass.equalsIgnoreCase("SHOWINNERBORDER"))
                {
                    if (elementName.equalsIgnoreCase("true"))
                    {
                        def.showInnerborder=true;
                    }else def.showInnerborder=false;
                }
                if (elementClass.equalsIgnoreCase("SIZESYNCHRON"))
                {
                    if (elementName.equalsIgnoreCase("true"))
                    {
                        def.resizeSynchron=true;
                    }else def.resizeSynchron=false;
                }

            }


            input.close();
        }catch(Exception ex)
        {
            //Tools.showMessage(ex.toString());
        }
        return def;
    }

  
}


class DefinitionDef
{
  public String iconfilename="";
  public String imagefilename="";
  public boolean showInnerborder=true;
  public boolean resizeSynchron=false;
}


