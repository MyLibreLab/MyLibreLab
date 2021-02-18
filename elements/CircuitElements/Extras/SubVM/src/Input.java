//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia (cswi@gmx.de)                         *
//* Copyright (C) 2017  Javier Velásquez (javiervelasquez125@gmail.com)                                                                          *
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
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import com.sun.jmx.mbeanserver.Introspector;
import java.util.Locale;



/**
  *
  * Beschreibung
  *
  * @version 1.0 vom 30.11.2006
  * @author
  */

public class Input extends JDialog{
  // Anfang Variablen
  private Button button1 = new Button();
  private Panel checkboxGroup1Panel = new Panel();
  private CheckboxGroup checkboxGroup1 = new CheckboxGroup();
  private Button button2 = new Button();
  private TextField textField1 = new TextField();
  private Label label1 = new Label();
  private JTextArea TextFiledHelp = new JTextArea();
  public static String result="";
  // Ende Variablen

  public Input(java.awt.Frame parent, boolean modal) {
    // Frame-Initialisierung
    super(parent, modal);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent evt) { dispose();
      //System.exit(0); 
      }
    });
    int frameWidth = 680;
    int frameHeight = 350;
    setSize(frameWidth, frameHeight);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (d.width - getSize().width) / 2;
    int y = (d.height - getSize().height) / 2 ;
    setLocation(x, y);
    JPanel cp = new JPanel(null);
    FlowLayout experimentLayout = new FlowLayout();
    cp.setLayout(experimentLayout);
    add(cp);
    
    Locale lang = Locale.getDefault();
    System.out.println("Locale ISO3:"+lang.getISO3Language()); //spa
    
    String langStr=lang.getISO3Language();
    String languageDef="en";
    
    if(langStr.equalsIgnoreCase("spa") || langStr.contains("es")){
        languageDef="es";    
    }    
    if(langStr.equalsIgnoreCase("eng") || langStr.contains("en")){
        languageDef="en";   
    }    
    if(langStr.equalsIgnoreCase("deu") ||langStr.equalsIgnoreCase("ger") || langStr.contains("de")){
        languageDef="de";    
    }    
    // Anfang Komponenten

    //label1.setBounds(8, 16, 117, 16);
    //label1.setBounds(10, 10, 280, 25);
    if(languageDef=="en" || languageDef=="de"){
    label1.setText("Relative Path Of VM With File Extension .vlogic");    
    }
    if(languageDef=="es"){
    label1.setText("Ruta relativa del VM con su Extensión \".vlogic\"");    
    }
    
    label1.setFont(new Font("Dialog", Font.BOLD, 14));
    label1.setForeground(Color.BLACK);
    cp.add(label1);
    
    //textField1.setBounds(8, 40, 273, 24);
    //textField1.setBounds(10, 45, 280, 45);
    textField1.setMinimumSize(new Dimension(670,40));
    textField1.setPreferredSize(new Dimension(670,40));
    textField1.setFont(new Font("Dialog", Font.BOLD, 13));
    textField1.setForeground(Color.red);
    textField1.setBackground(Color.LIGHT_GRAY);
    textField1.setText("");
    textField1.requestFocus();
    cp.add(textField1);
    
    //label2.setBounds(10, 90, 280, 25);
    String tempTxt="";
    if(languageDef=="en" || languageDef=="de"){
     tempTxt= "\nIf your VM is located in root project Path only write VM name and file extension.\nExample:\nYourVM.vlogic\n\nIf your VM is located inside a project's folder, write relative location, name and extension.\nExample:\n"+"/subvmFolder/subvm.vlogic";  
    }
    if(languageDef=="es"){
    tempTxt= "\nSi su VM está ubicado en la ruta principal del proyecto solo escriba el nobre del VM con la extensión del archivo.\nEjemplo:\nNombreVM.vlogic\n\nSi su VM está ubicado dentro de alguna carpeta del proyecto, escriba la ubicación relativa, nombre y extensión.\nEjemplo:\n"+"/CarpetaSubvm1/subvm1.vlogic";  
    }
    TextFiledHelp.setText(tempTxt);
    TextFiledHelp.setFont(new Font("Dialog", Font.BOLD, 12));
    TextFiledHelp.setMinimumSize(new Dimension(670,150));
    TextFiledHelp.setPreferredSize(new Dimension(670,150));
    
    cp.add(TextFiledHelp);
    
    Label labelSeparator = new Label();
    labelSeparator.setText(" ");
    labelSeparator.setPreferredSize(new Dimension(670,20));
    labelSeparator.setVisible(true);
    cp.add(labelSeparator);
    //button1.setBounds(208, 72, 75, 25);
    //button1.setBounds(200, 120, 100, 25);
    button2.setBackground(Color.GRAY);
    button2.setLabel("OK");
    button2.setFont(new Font("Dialog", Font.BOLD, 12));
    button2.setForeground(Color.BLACK);
    button2.setPreferredSize(new Dimension(100,50));
    cp.add(button2);
    button2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        button2ActionPerformed(evt);
      }
    });
    
    button1.setBackground(Color.GRAY);
    button1.setPreferredSize(new Dimension(100,50));
    button1.setLabel("Cancel");
    button1.setFont(new Font("Dialog", Font.BOLD, 12));
    button1.setForeground(Color.BLACK);
    cp.add(button1);
    button1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        button1ActionPerformed(evt);
      }
    });

    //button2.setBounds(128, 72, 75, 25);
    //button2.setBounds(50, 120, 100, 25);
    

    setResizable(false);
    setVisible(true);
  }

  // Anfang Ereignisprozeduren
  public void button1ActionPerformed(ActionEvent evt) {
    result="";
    textField1.setText("");
    dispose();
  }

  public void button2ActionPerformed(ActionEvent evt) {
    result=textField1.getText();
    if(result.endsWith(".vlogic")){
    dispose();    
    }else{
    textField1.setBackground(Color.YELLOW);
    }
    
  }

  // Ende Ereignisprozeduren

  public static void main(String[] args) {
    new Input(null,true);
  }
}

