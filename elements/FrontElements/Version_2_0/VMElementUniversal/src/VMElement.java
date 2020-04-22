//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia (cswi@gmx.de)                         *
//* Copyright (C) 2018  Javier Velásquez (javiervelasquez125@gmail.com)       *
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
  private String projectPath="";
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
  }

  public void paint(java.awt.Graphics g)
  {
    if (image!=null) drawImageCentred(g,image);
  }

  // Params :
  // 1 : vmName ohne Path da im Project!
  // 2 : name des Elements
  // 3 : icon ohne path da im Project!

  public void beforeInit(String[] args)
  {
    if (args!=null)
    {

       if (args.length>=1)
       {
         filenameX=args[0]; // ohne Path! nur VMName zb: "Untiled.vlogic"
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


   public static String getFileNameWithoutExtension(File file)
    {
        String nm=file.getName();
        String ext=getExtension(file);

        return nm.substring(0,nm.length()-ext.length()-1);
    }

    public static String getExtension(File f)
    {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1)
        {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
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

            if (basis!=null) basis.vsShow();
        }
    });


    basis=element.jCreateBasis();


    projectPath=element.jGetProjectPathFromProject();
    
    
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
        String str=projectPath+"/"+filename.getValue();
        
        basis.vsLoadFromFile(str);
        
        
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

    element.jSetTag(0,new File(element.jGetProjectPath()));

    String iconFilename=getFileNameWithoutExtension(new File(filename.getValue()));

     boolean iconFound=false;
    //String str=projectPath+"/"+filename.getValue();
    String str=projectPath+File.separator+filename.getValue();
    //String iconFile=new File(str).getParent()+"\\"+iconFilename;
    String iconFile=new File(str).getParent()+File.separator+iconFilename;
    
    if (new File(iconFile+".jpg").exists())
    {
      iconFilename=iconFilename+".jpg";
      iconFound=true;
    }
    if (new File(iconFile+".gif").exists())
    {
      iconFilename=iconFilename+".gif";
      iconFound=true;
    }
    if (new File(iconFile+".png").exists())
    {
       iconFilename=iconFilename+".png";
       iconFound=true;
    }
    
    onDispose();
    
    

    
    //image=element.jLoadImage(new File(str).getParent()+"\\"+iconFilename);
    if(iconFound){
    System.out.println("icon="+iconFilename);   
    image=element.jLoadImage(new File(str).getParent()+File.separator+iconFilename);    
    }else{
    System.out.println("subVM icon:"+iconFilename+".gif Not Found"); 
    image=element.jLoadImage(element.jGetSourcePath()+"bin/VMElement.gif");
    //System.out.println(element.jGetSourcePath()+"bin/VMElement.gif");
    }
    
    CaptionDef def=getIconFromDefinitionDef();
    

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
      if(filename!=null){
      filename.setValue(filename.getValue().replace("\\", "/")); //3.12.0 r93    
      }
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
    if(filename!=null){
    filename.setValue(filename.getValue().replace("\\", "/")); //3.12.0 r93    
    }
    filename.saveToStream(fos);
    elementName.saveToStream(fos);
    elementIcon.saveToStream(fos);
    
    //saveToStreamAfterXOnInit(fos);
  }


  public void propertyChanged(Object o)
  {
     if (o ==filename)
     {
      xOnInit();
     }
  }

  public void setPropertyEditor()
  {
    element.jAddPEItem("VM Name",filename, 0,0);
    localize();
  }

  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"VM Name");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"VM Name");
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

    public CaptionDef getIconFromDefinitionDef()
    {

        String str;
        
        CaptionDef def = new CaptionDef();


        str=projectPath+"/"+filename.getValue();
        String filename=new File(str).getParent()+"\\config.txt";

        try
        {
            BufferedReader input = new BufferedReader(new FileReader(filename));
            String inputString;
            while ((inputString = input.readLine()) != null)
            {
                String elementClass=extractClassName(inputString);
                String elementName=inputString.substring(elementClass.length());

                elementName=elementName.trim();
                elementName=elementName.substring(1);
                elementClass=elementClass.trim();
                elementName=elementName.trim();

                if (elementClass.equalsIgnoreCase("CAPTIONDE"))
                {
                    def.captionDE=elementName;
                }else
                if (elementClass.equalsIgnoreCase("CAPTIONEN"))
                {
                    def.captionEN=elementName;
                }else
                if (elementClass.equalsIgnoreCase("CAPTIONES"))
                {
                   def.captionES=elementName;
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




