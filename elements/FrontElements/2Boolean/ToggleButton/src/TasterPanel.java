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
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import tools.*;
import java.awt.geom.Rectangle2D;



public class TasterPanel extends JVSMain
{
  private boolean value=false;
  private int sizeW=70;
  private int sizeH=30;
  private VSBoolean down=new VSBoolean(false);;
  private VSBoolean initValue=new VSBoolean(false);
  private VSBoolean paintBorder=new VSBoolean(true);
  private VSBoolean paintFocus=new VSBoolean(true);


  private boolean oldDown=false;
  private VSImage image =new VSImage();
  private VSImage selectedImage =new VSImage();
  private VSInteger textAlign=new VSInteger(0);
  private javax.swing.JToggleButton button = new JToggleButton();
  
  private ExternalIF circuitElement;
  
  private VSString caption = new VSString();
  private VSFont font=new VSFont(new Font("Arial",1,10));
  private VSColor fontColor = new VSColor(Color.BLACK);
  private VSColor backColor = new VSColor(Color.LIGHT_GRAY);
  private VSInteger gruppe = new VSInteger(0);


  public void onDispose()
  {
   if (image.getImage()!=null)image.getImage().flush();
   if (selectedImage.getImage()!=null)selectedImage.getImage().flush();

  }

  public void init()
  {
    initPins(0,0,0,0);
    setSize(sizeW,sizeH);
    initPinVisibility(false,false,false,false);

    element.jSetResizable(true);
    element.jSetInnerBorderVisibility(false);
    setName("Taster");
    caption.setValue("OK");
    
  }
  
  
    private void jToggleButton1ItemStateChanged(java.awt.event.ItemEvent evt)
    {
      down.setValue(button.isSelected());
      circuitElement.Change(0,down);
    }


  public void xOnInit()
  {
    JPanel panel =element.getFrontPanel();
    panel.setLayout(new java.awt.BorderLayout());

    panel.add(button, java.awt.BorderLayout.CENTER);
    element.setAlwaysOnTop(true);
    

    button.setText(caption.getValue());


    button.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            jToggleButton1ItemStateChanged(evt);
        }
    });


/*    button.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
              down.setValue(true);
              circuitElement.Change(0,down);
            }
        });*/

    /*button.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mousePressed(java.awt.event.MouseEvent evt) {

             down.setValue(true);
            circuitElement.Change(0,down);
        }
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            down.setValue(false);
            circuitElement.Change(0,down);
        }
    }); */

  }
  


  public void initInputPins()
  {
  }

  public void initOutputPins()
  {
  }
  
  
  public void start()
  {
  


    circuitElement=element.getCircuitElement();
    /*down.setValue(false);
    circuitElement.Change(0,down);*/
    
    element.jAddJButtonToButtonGroup(button,gruppe.getValue());
    
    element.jRepaint();
    circuitElement=element.getCircuitElement();
//    down.setValue(false);

    button.setSelected(initValue.getValue());
    down.setValue(button.isSelected());
    circuitElement.Change(0,down);

  }




  public void setPropertyEditor()
  {
    element.jAddPEItem("Schriftart",font, 0,0);
    element.jAddPEItem("Beschriftung",caption, 0,0);
    element.jAddPEItem("Font Color",fontColor, 0,0);
    element.jAddPEItem("Hintergrund-Farbe",backColor, 0,0);
    element.jAddPEItem("Bild enabled",selectedImage, 0,0);
    element.jAddPEItem("Bild disabled", image, 0,0);
    element.jAddPEItem("Text Ausrichtung",textAlign, 0,1);
    element.jAddPEItem("Gruppe",gruppe, 0,19);
    element.jAddPEItem("Anfangs-Wert",initValue, 0,0);
    element.jAddPEItem("Rand zeichnen",paintBorder, 0,0);
    element.jAddPEItem("Focus zeichnen",paintFocus, 0,0);
    

    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Font");
    element.jSetPEItemLocale(d+1,language,"Caption");
    element.jSetPEItemLocale(d+2,language,"Font Color");
    element.jSetPEItemLocale(d+3,language,"Background Color");
    element.jSetPEItemLocale(d+4,language,"Switch");
    element.jSetPEItemLocale(d+5,language,"Init-Value");
    element.jSetPEItemLocale(d+6,language,"Image enabled");
    element.jSetPEItemLocale(d+7,language,"Image disabled");
    element.jSetPEItemLocale(d+8,language,"Text align");
    element.jSetPEItemLocale(d+9,language,"Group");
    element.jSetPEItemLocale(d+10,language,"Init Value");
    element.jSetPEItemLocale(d+11,language,"Paint Border");
    element.jSetPEItemLocale(d+12,language,"Paint Focus");
    

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Font");
    element.jSetPEItemLocale(d+1,language,"Caption");
    element.jSetPEItemLocale(d+2,language,"Font Color");
    element.jSetPEItemLocale(d+3,language,"Background Color");
    element.jSetPEItemLocale(d+4,language,"Switch");
    element.jSetPEItemLocale(d+5,language,"Init-Value");
    element.jSetPEItemLocale(d+6,language,"Image enabled");
    element.jSetPEItemLocale(d+7,language,"Image disabled");
    element.jSetPEItemLocale(d+8,language,"Text align");
    element.jSetPEItemLocale(d+9,language,"Group");
    element.jSetPEItemLocale(d+10,language,"Init Value");
    element.jSetPEItemLocale(d+11,language,"Paint Border");
    element.jSetPEItemLocale(d+12,language,"Paint Focus");
  }

  public void propertyChanged(Object o)
  {
    //down.setValue(initValue.getValue());

    button.setFont(font.getValue());
    button.setText(caption.getValue());
    button.setForeground(fontColor.getValue());
    button.setBackground(backColor.getValue());
    
    if (textAlign.getValue()==0)
    {
      button.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    } else
    {
      button.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    }

    if (image.getImage()!=null)
    {
      button.setIcon(new javax.swing.ImageIcon(image.getImage()));
    }
    
    if (selectedImage.getImage()!=null)
    {
      button.setSelectedIcon(new javax.swing.ImageIcon(selectedImage.getImage()));
    }

    button.setBorderPainted(paintBorder.getValue());
    
    button.setFocusPainted(paintFocus.getValue());

  }



  public void loadFromStream(java.io.FileInputStream fis)
  {
      initValue.loadFromStream(fis);
      font.loadFromStream(fis);
      caption.loadFromStream(fis);
      fontColor.loadFromStream(fis);
      backColor.loadFromStream(fis);
      image.loadFromStream(fis);
      selectedImage.loadFromStream(fis);
      textAlign.loadFromStream(fis);
      gruppe.loadFromStream(fis);
      
      paintBorder.loadFromStream(fis);
      paintFocus.loadFromStream(fis);
      
      propertyChanged(null);
      

  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      initValue.saveToStream(fos);
      font.saveToStream(fos);
      caption.saveToStream(fos);
      fontColor.saveToStream(fos);
      backColor.saveToStream(fos);
      image.saveToStream(fos);
      selectedImage.saveToStream(fos);

      textAlign.saveToStream(fos);
      gruppe.saveToStream(fos);
      paintBorder.saveToStream(fos);
      paintFocus.saveToStream(fos);
  }
}


