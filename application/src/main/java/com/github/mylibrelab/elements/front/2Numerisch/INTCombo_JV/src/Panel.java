//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia (cswi@gmx.de)                         *
//* Copyright (C) 2017  Javier Velásquez (javiervelasquez125@gmail.com)                                                                           *
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
import java.util.*;
import tools.*;



public class Panel extends JVSMain
{
  private int width=120, height=30;
  private int index;
  private VSInteger initValue=new VSInteger(0);
  private VSInteger value=new VSInteger(0);
  private VSPropertyDialog more= new VSPropertyDialog();

  private VSFont fnt = new VSFont(new Font("Dialog",1,15));
  private ExternalIF circuitElement;
  private VS1DString values = new VS1DString(0);
  private JComboBox combo =  new javax.swing.JComboBox<>();
  private VSColor fontColor = new VSColor(new Color(0,0,102));
  private VSColor backColor = new VSColor(Color.gray);
  private VSBoolean centerAlign = new VSBoolean(true);
  private DefaultComboBoxModel model;

/*
   public void drawBorder(Graphics g,Rectangle bounds)
   {
     g.setColor(Color.WHITE);
     g.fillRect(bounds.x,bounds.y,bounds.width,bounds.height);

     g.setColor(Color.DARK_GRAY);
     g.drawLine(bounds.x+1,bounds.y+1,bounds.width-1,bounds.y+1);
     g.drawLine(bounds.x+1,bounds.y+1,bounds.x+1,bounds.height-1);

     g.setColor(Color.LIGHT_GRAY);
     g.drawLine(bounds.x+1,bounds.height-1,bounds.width-1,bounds.height-1);
     g.drawLine(bounds.width-1,bounds.y+1,bounds.width-1,bounds.height-1);

     g.setColor(Color.BLACK);
     g.drawRect(bounds.x,bounds.y,bounds.width,bounds.height);
   }
   

   public void drawNibble(Graphics g,int x, int y, int xx, int yy)
   {
     g.setColor(Color.LIGHT_GRAY);
     g.fillRect(x,y,xx,yy);
     
     g.setColor(Color.DARK_GRAY);
     g.drawRect(x,y,xx,yy);

     int mitteX=(xx)/2;
     
     Polygon p = new Polygon();
     p.addPoint(x+4,y+4);
     p.addPoint(x+xx-4,y+4);
     p.addPoint(x+mitteX,yy-4);
     p.addPoint(x+4,y+4);
     
     g.setColor(Color.BLACK);
     g.fillPolygon(p);
   }
*/
   
   public void paint(java.awt.Graphics g)
   {
   }
   
   private boolean isLoading=false;
   

    private void cmbProcessingActionPerformed(java.awt.event.ActionEvent evt) {

        if (!isLoading)
        {
           int index=combo.getSelectedIndex();

           if (circuitElement!=null)
           {
             value.setValue(index);
            circuitElement.Change(0,value);
           }

        }

    }


  public void init()
  {
    initPins(0,0,0,0);
    setSize(width,height);
    initPinVisibility(false,false,false,false);
    element.jSetInnerBorderVisibility(false);
    
    
    element.jSetResizable(true);
    setName("Combo_JV (INT)");
    
    values.resize(3);
    values.setValue(0,"1");
    values.setValue(1,"2");
    values.setValue(2,"3");
    
    more.setText(getTextWithKomma());
    model=new DefaultComboBoxModel();
    combo.setBackground(backColor.getValue());
    combo.setModel(model);
    //combo.setFont(fnt.getValue());
    //((JLabel)combo.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
    //((JLabel)combo.getRenderer()).setFont(fnt.getValue());
    combo.addActionListener(new java.awt.event.ActionListener()
    {
        public void actionPerformed(java.awt.event.ActionEvent evt)
        {
            cmbProcessingActionPerformed(evt);
        }
    });


  }
  public void xOnInit()
  {
    JPanel panel=element.getFrontPanel();
    panel.setLayout(new BorderLayout());
    //((JLabel)combo.getRenderer()).setFont(fnt.getValue());
    
    fillModel();
    
    panel.add(combo,BorderLayout.CENTER);
    //circuitElement=element.getCircuitElement();
    element.setAlwaysOnTop(true);
    

   
  }
  
  private void fillModel()
  {
    isLoading=true;
    //((JLabel)combo.getRenderer()).setFont(fnt.getValue()); // No Funciona
    combo.setFont(fnt.getValue());
    combo.setForeground(fontColor.getValue());
    combo.setBackground(backColor.getValue());
    
    combo.setEnabled(true);
    combo.setEditable(false);
    if(centerAlign.getValue()){
    ((JLabel)combo.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
    }else{
    ((JLabel)combo.getRenderer()).setHorizontalAlignment(SwingConstants.LEADING);
    }
    model.removeAllElements();
    for (int i=0;i<values.getLength();i++)
    {
      model.addElement(values.getValue(i));
    }
    
    int index=initValue.getValue();
    if (index>model.getSize()-1)
    {
     index=model.getSize()-1;
     initValue.setValue(index);
    }
    combo.setSelectedIndex(index);
    isLoading=false;
  }
  

  public void setPropertyEditor()
  {
    element.jAddPEItem("Anfangswert",initValue, 0,999999);
    element.jAddPEItem("Mehr",more, 0,0);
    element.jAddPEItem("Font",fnt, 0,0);
    element.jAddPEItem("Font Farbe",fontColor, 0,0);
    element.jAddPEItem("Font Centered",centerAlign, 0,0);
    element.jAddPEItem("Background Farbe",backColor, 0,0);

    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Init-Value");
    element.jSetPEItemLocale(d+1,language,"More");
    element.jSetPEItemLocale(d+2,language,"Font");
    element.jSetPEItemLocale(d+3,language,"Font Color");
    element.jSetPEItemLocale(d+4,language,"Font Centered");
    element.jSetPEItemLocale(d+5,language,"backGround Color");
    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Valor Inicial");
    element.jSetPEItemLocale(d+1,language,"Mas");
    element.jSetPEItemLocale(d+2,language,"Fuente");
    element.jSetPEItemLocale(d+3,language,"Color Fuente");
    element.jSetPEItemLocale(d+4,language,"Centrar Fuente");
    element.jSetPEItemLocale(d+5,language,"Color de Fondo");
    
  }
  
  
  private String getTextWithKomma()
  {
      String str="";

      for (int i=0;i<values.getLength();i++)
      {
        str+=values.getValue(i)+",";
      }
      
      return str;
  }
  
  private String getTextWithN()
  {
      String str="";

      for (int i=0;i<values.getLength();i++)
      {
        str+=values.getValue(i)+"\n";
      }

      return str;
  }

  
  public void propertyChanged(Object o)
  {
    //((JLabel)combo.getRenderer()).setFont(fnt.getValue()); 
    //((JLabel)combo.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
    if (o.equals(more))
    {
      String str=getTextWithN();
      

      Properties frm = new Properties(element.jGetFrame(),str);
      frm.setSize(200,200);
      frm.setModal(true);
      frm.setVisible(true);
      
      if (frm.result==true)
      {
        String res=frm.strText;
        
        ArrayList liste=new ArrayList();
        
        int lastI=0;
        // Generiere neues Array!
        for (int i=0;i<res.length();i++)
        {
          char ch=res.charAt(i);

          if (ch=='\n')
          {
           liste.add(res.substring(lastI,i));
           lastI=i+1;
          }
        }
        if (lastI>0)liste.add(res.substring(lastI));
        
        values.resize(liste.size());
        
        for (int i=0;i<liste.size();i++)
        {
          values.setValue(i,(String)liste.get(i));
        }

        more.setText(getTextWithKomma());
        
      }
      

    }
    
    fillModel();

  }


  public void mouseMoved(MouseEvent e)
  {
  }
  
  public void mousePressed(MouseEvent e)
  {
  }


  public void start()
  {
    index=initValue.getValue();
    if (index>model.getSize())
    {
     index=model.getSize()-1;
     initValue.setValue(index);
    }
    //combo.setSelectedIndex(index);
    
    circuitElement=element.getCircuitElement();
    circuitElement.Change(0,initValue);

  }
  
  public void process()
 {

  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
      initValue.loadFromStream(fis);
      values.loadFromStream(fis);
      fnt.loadFromStream(fis);
      fontColor.loadFromStream(fis);
      centerAlign.loadFromStream(fis);
      backColor.loadFromStream(fis);
      
      index=initValue.getValue();
      more.setText(getTextWithKomma());
      
      
      fillModel();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      initValue.saveToStream(fos);
      values.saveToStream(fos);
      fnt.saveToStream(fos);
      fontColor.saveToStream(fos);
      centerAlign.saveToStream(fos);
      backColor.saveToStream(fos);
  }



}

