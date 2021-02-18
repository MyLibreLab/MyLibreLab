//********************************
//* Autor : Robinson Javier Velasquez
//* Date  : Ago-01-2016
//* Email : javiervelasquez@gmail.com
//* Description: Multiline Pane Text Elements with Horizontal Auto Scroll and enabled vertical mouse Scroll.
//* licence : non commercial use.
//********************************

import VisualLogic.*;
import VisualLogic.variables.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import tools.*;
import java.awt.geom.Rectangle2D;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class Pane_Text_Control_JV extends JVSMain
{
  private int width=250, height=155;

  private JTextPane text = new JTextPane();
  StyledDocument Text_Style = text.getStyledDocument();
  SimpleAttributeSet Alin_Temp = new SimpleAttributeSet();
  
  
  private VSString strText = new VSString();
  private VSInteger ausrichtungH = new VSInteger();
  private VSColor Element_Color_Bakground = new VSColor(Color.WHITE);

  private VSFont font=new VSFont(new Font("Dialog",0,12));
  private VSColor fontColor = new VSColor(Color.BLACK);
  private ExternalIF circuitElement;

  

   public void paint(java.awt.Graphics g)
   {
   }
   
   public void onDispose()
  {
    JPanel panel=element.getFrontPanel();
    panel.removeAll();
    
  }



  public void init()
  {
    initPins(0,0,0,0);
    setSize(width,height);
    element.jSetInnerBorderVisibility(false);
    initPinVisibility(false,false,false,false);
    
    element.jSetResizable(true);
    
    
    setName("Pane_Text_Control_JV");
    element.jSetCaptionVisible(false);
  }
  

  
  public void xOnInit()
  {

    JPanel panel =element.getFrontPanel();
    panel.setLayout(new java.awt.BorderLayout());

    //Create a text pane.
       // JTextPane textPane = createTextPane();
        JScrollPane paneScrollPane = new JScrollPane(text);
        paneScrollPane.setVerticalScrollBarPolicy(
                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        paneScrollPane.setHorizontalScrollBarPolicy(
                        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        paneScrollPane.setPreferredSize(new Dimension(width, height));
        paneScrollPane.setMinimumSize(new Dimension(10, 10));
    
    element.setAlwaysOnTop(true);

    text.setForeground(fontColor.getValue());
    text.setBackground(Element_Color_Bakground.getValue());
    text.setEditable(true);
    text.setAutoscrolls(false);
    text.setText("Multiline Text Control Element \n\nWith Horizontal and vertical Auto Scroll when needed J.V.");
    
    panel.add(paneScrollPane, java.awt.BorderLayout.CENTER);

     
    Set_Text_Style(1);
    ausrichtungH.setValue(1);
    
     text.addKeyListener(new java.awt.event.KeyAdapter()
    {
        
        public void keyReleased(java.awt.event.KeyEvent evt)
        {
          strText.setValue(((JTextPane)evt.getSource()).getText());
          circuitElement=element.getCircuitElement();
          circuitElement.Change(0,new VSString(strText.getValue()));
        }
    });
    

  }
  
  public void Set_Text_Style(int Align){  // Aling 0=Left 1=Center (default) 2=Right 3=Justified 
        
    StyleConstants.setAlignment(Alin_Temp, StyleConstants.ALIGN_CENTER);   
      
    if (Align==0){
        StyleConstants.setAlignment(Alin_Temp, StyleConstants.ALIGN_LEFT);   
    }
    if (Align==2){
        StyleConstants.setAlignment(Alin_Temp, StyleConstants.ALIGN_RIGHT);   
    }
    if (Align==3){
        StyleConstants.setAlignment(Alin_Temp, StyleConstants.ALIGN_JUSTIFIED);   
    }
    
    Text_Style.setParagraphAttributes(0, Text_Style.getLength(), Alin_Temp, false);
        
    text.setStyledDocument(Text_Style);    
  }
          
         
  public void start()
  {
    text.setText("");
  }
   

  
  
  public void setPropertyEditor()   // Translated to german from other element
  {
    element.jAddPEItem("Schriftart",font, 0,0);
    element.jAddPEItem("Beschreibung",strText, 0,0);
    element.jAddPEItem("Farbe",fontColor, 0,0);
    element.jAddPEItem("Ausrichtung Hoz",ausrichtungH, 0,3);
    element.jAddPEItem("Farbe Fondo",Element_Color_Bakground, 0,0);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Font");
    element.jSetPEItemLocale(d+1,language,"Init_Text");
    element.jSetPEItemLocale(d+2,language,"Font_Color");
    element.jSetPEItemLocale(d+3,language,"Text_Align");
    element.jSetPEItemLocale(d+4,language,"BackGround Color");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Fuente");
    element.jSetPEItemLocale(d+1,language,"Texto_Inicial");
    element.jSetPEItemLocale(d+2,language,"Color_Texto");
    element.jSetPEItemLocale(d+3,language,"Alineacion_Texto");
    element.jSetPEItemLocale(d+4,language,"Color de Fondo");

  }
  
  
  public void propertyChanged(Object o)
  {  
    
    
    if (o.equals(strText))
    {
       
    text.setText(strText.getValue());

        
    }
    if (o.equals(font))
    {
      text.setFont(font.getValue());
      
    }
    if (o.equals(fontColor))
    {
      text.setForeground(fontColor.getValue());
      
    }
    if (o.equals(ausrichtungH))
    {
      int hali=ausrichtungH.getValue();
      
      Set_Text_Style(hali);
      
    }
    if (o.equals(Element_Color_Bakground)){
    text.setBackground(Element_Color_Bakground.getValue());
    }
  }
  
  
  public void loadFromStream(java.io.FileInputStream fis)
  {
      font.loadFromStream(fis);
      text.setFont(font.getValue());
      strText.loadFromStream(fis);
      text.setText(strText.getValue());
      fontColor.loadFromStream(fis);
      text.setForeground(fontColor.getValue());
      ausrichtungH.loadFromStream(fis);
      Set_Text_Style(ausrichtungH.getValue());
      Element_Color_Bakground.loadFromStream(fis);
      text.setBackground(Element_Color_Bakground.getValue());
     //propertyChanged(null);

  }
 
  public void saveToStream(java.io.FileOutputStream fos)
  {
      font.setValue(text.getFont());
      font.saveToStream(fos);
      strText.setValue(text.getText());
      strText.saveToStream(fos);
      fontColor.setValue(text.getForeground());
      fontColor.saveToStream(fos);
      //ausrichtungH.setValue(ausrichtungH.getValue());
      ausrichtungH.saveToStream(fos);
      //Element_Color_Bakground.setValue(Element_Color_Bakground.getValue());
      Element_Color_Bakground.saveToStream(fos);
  }
  
  
}

