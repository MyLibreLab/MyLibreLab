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
import javax.swing.*;
import java.text.*;
import tools.*;
import java.awt.geom.Rectangle2D;
import java.io.*;

public class VMPanel extends JVSMain
{
  private int width=60, height=150;
  private ExternalIF circuitElement;
  private VSBasisIF basis;
  private VSInteger xwidth = new VSInteger(0);
  private VSInteger xheight = new VSInteger(0);
  private VSString version= new VSString("");

  public void beforeInit(String[] args)
  {

  }


  public void init()
  {
    initPins(0,0,0,0);

    initPinVisibility(false,false,false,false);
    element.jSetInnerBorderVisibility(false);

    element.jSetResizable(true);
    element.jSetMinimumSize(10,10);
  }
  
  public void xOnInit()
  {
    basis=element.jGetElementBasis();
    element.jInitBasis(basis);

    //element.jClearPE();
    element.jSetProperties();

    if (basis!=null )
    {
      String[] infos=basis.vsGetVMInfo();

      
       File file=(File)element.jGetTag(0);

       String fn=file.getPath();
       infos[2]=fn;
       element.jSetInfo(infos[0],infos[1],infos[2]);

      element.jSetInfo(infos[0],infos[1],infos[2]);
      if (basis.vsGetFrontPanelComponentCount()==0)
      {
        element.jSetVisible(false);
      } else
      {
        int w=basis.vsGetFrontVMPanelWidth();
        int h=basis.vsGetFrontVMPanelHeight();
        element.jSetSize(w,h);
        
        //file=(File)element.jGetTag(0);

        //System.out.println()

        //System.out.println("filename = : "+file.getPath());
        //DefinitionDef def=getIconFromDefinitionDef(file);
        //System.out.println("resizesynchron="+def.resizeSynchron);
        //element.jSetResizeSynchron(def.resizeSynchron);
        element.jFixElement();
      }
    }

  }
  
  public String getBinDir()
  {
    return element.jGetSourcePath();
    //return new File(element.jGetElementPath()+filename.getValue()).getParentFile().getPath();
  }


  public void saveToStreamAfterXOnInit(java.io.FileOutputStream fos)
  {

    String basisVersion=basis.getBasisElementVersion();
    version.setValue(basisVersion);


    //showMessage("Saving Front-Element :"+ element.jGetCaption() +" basisVersion  "+version.getValue());
    
    version.saveToStream(fos);

    VSObject[] props=element.jGetProperties();

    JPanel panel = (JPanel)element;
    int w=panel.getWidth();
    int h=panel.getHeight();

    xwidth.setValue(w);
    xheight.setValue(h);

    for (int i=0;i<props.length;i++)
    {
      props[i].saveToStream(fos);
    }
    xwidth.saveToStream(fos);
    xheight.saveToStream(fos);
    

   //showMessage("Props saved!");
  }
  
  
  public static void showMessage(String message)
  {
     JOptionPane.showMessageDialog(null,message,"Attention!",JOptionPane.ERROR_MESSAGE);
  }

  public void loadFromStreamAfterXOnInit(java.io.FileInputStream fis)
  {

    String basisVersion=basis.getBasisElementVersion();
    version.loadFromStream(fis);

    
    //showMessage("Loading Front-Element :"+ element.jGetCaption() +" basisVersion  "+version.getValue());

    if (version.getValue().equalsIgnoreCase(basisVersion))
    {

      try
      {
        VSObject[] props=element.jGetProperties();


        for (int i=0;i<props.length;i++)
        {
          props[i].loadFromStream(fis);
        }
        xwidth.loadFromStream(fis);
        xheight.loadFromStream(fis);

        element.jSetSize(xwidth.getValue(),xheight.getValue());
        //showMessage("Props LoadedYYYYYYYYy!");

      } catch(Exception ex)
      {
       element.jSetSize(70,70);
      }
    }
  }
    
  public void loadFromStream(java.io.FileInputStream fis)
  {
//    loadFromStreamAfterXOnInit(fis);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
//    saveToStreamAfterXOnInit(fos);
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

   /* public CaptionDef getIconFromDefinitionDef(File file)
    {

        String str;

        CaptionDef def = new CaptionDef();

        String projectPath=element.jGetProjectPathFromProject();

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
    }*/

}


