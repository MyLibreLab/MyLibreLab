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

public class XLabel extends JVSMain
{

  
  private VSFile GifFile = new VSFile("");
  private VSBoolean enableBorder = new VSBoolean(true);

  final private JLabel label = new JLabel("Label");
  
  public XLabel()
  {
    myInit();
  }
  
  public void myInit()
  {
    
    try{
        //System.err.println("myInit Try:"+strText.getValue());    
        ImageIcon image = new ImageIcon(GifFile.getValue());
        //System.err.println("OK");
        label.setIcon(image);
        //label.repaint();
        //if(image.equals(null)!=true) element.jSetSize(image.getIconWidth(), image.getIconHeight());
        element.jSetInnerBorderVisibility(enableBorder.getValue());
        element.jRepaint();
        }catch(Exception e){
        //System.err.println("ERROR");    
        }

  }


  public void paint(java.awt.Graphics g)
  {
      if(element!=null){
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.yellow);
        Stroke restore = g2.getStroke();
        //g2.setStroke(new BasicStroke(5));
        //g2.drawLine(0, 0, element.jGetWidth(),0);
        //label.setSize(new Dimension(element.jGetWidth(),element.jGetWidth()));
    
      }
      
  }
   
  public void init()
  {
    initPins(0,0,0,0);
    setSize(40,40);
    initPinVisibility(false,false,false,false);
    element.jSetResizable(true);
    element.jSetInnerBorderVisibility(enableBorder.getValue());
    setSize(180,180);
    //label.setPreferredSize(new Dimension(element.jGetWidth(),element.jGetWidth()));
    //label.setSize(new Dimension(element.jGetWidth(),element.jGetWidth()));
    label.setVisible(true);
    GifFile.addExtension("gif");
    GifFile.addExtension("GIF");
    GifFile.addExtension("png");
    GifFile.addExtension("jpg");
    GifFile.addExtension("jpeg");
    GifFile.addExtension("JPEG");
    
    
    
    
    setName("GIF Image JV");
    
    /*
    if(icon!=null){
        try{
        ImageIcon image = new ImageIcon(
                        getClass().getResource(
                            "/" + "animated-fire-image-0257.gif"));
        label.setIcon(image);
        }catch(Exception e){
            
        }
    }
    */
    
    
      
    
   

  }


  public void xOnInit()
  {
    try
    {
      JPanel panel =element.getFrontPanel();
      panel.setLayout(new java.awt.BorderLayout());
      //ImageIcon icon = new ImageIcon();
      
      panel.add(label, java.awt.BorderLayout.CENTER);
      
      label.setText("");
      
      
      element.setAlwaysOnTop(true);
    } catch(Exception ex)
    {
      System.out.println(ex);
    }
    
        
    

  }


  public void setPropertyEditor()
  {
    element.jAddPEItem("Border Visible",enableBorder, 0,0);
    element.jAddPEItem("Beschreibung",GifFile, 0,0);
    
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Border Visible");
    element.jSetPEItemLocale(d+1,language,"GIF Image Path");


    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Marco Visible");
    element.jSetPEItemLocale(d+1,language,"Ruta de la Imagen GIF");


  }
  public void propertyChanged(Object o)
  {
      //JFileChooser pathDialog = new JFileChooser();
        //      pathDialog.showOpenDialog(null);
          //    GifPath.setValue(pathDialog.getSelectedFile().getAbsolutePath());
              
      myInit();
      //System.err.println("File Value"+GifFile.getValue());
    
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
      enableBorder.loadFromStream(fis);
      GifFile.loadFromStream(fis);
      
     propertyChanged(null);

  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      enableBorder.saveToStream(fos);
      GifFile.saveToStream(fos);
      
  }
}
