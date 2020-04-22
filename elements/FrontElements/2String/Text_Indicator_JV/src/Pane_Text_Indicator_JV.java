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

public class Pane_Text_Indicator_JV extends JVSMain implements PanelIF
{
  private int width=250, height=155;

  private JTextPane text = new JTextPane();
  StyledDocument Text_Style = text.getStyledDocument();
  SimpleAttributeSet Alin_Temp = new SimpleAttributeSet();
  
  
  private VSString strText = new VSString();
  private VSInteger ausrichtungH = new VSInteger();
  private VSColor Element_Color_Bakground = new VSColor(new Color(214,217,223));

  private VSFont font=new VSFont(new Font("Dialog",0,12));
  private VSColor fontColor = new VSColor(Color.BLACK);
  private VSBoolean Label_Use = new VSBoolean();


  public void processPanel(int pinIndex, double value, Object obj)
  {
    if (obj instanceof VSString)
    {
      String str=((VSString)obj).getValue();
      
      Set_Text_Style(ausrichtungH.getValue());
      
      if(Label_Use.getValue()){
           str=strText.getValue();
      }else{
      strText.setValue(str);
      }
      
      text.setText(str);

    }
  }
  

   public void paint(java.awt.Graphics g)
   {
   }


  public void init()
  {
    initPins(0,0,0,0);
    setSize(width,height);
    element.jSetInnerBorderVisibility(false);
    initPinVisibility(false,false,false,false);
    
    element.jSetResizable(true);
    
    text.setText("Multiline Text Indicator Element \n\nWith Horizontal and vertical Auto Scroll when needed J.V.");
    
    ausrichtungH.setValue(1);
    setName("Pane_Text_Indicator_JV");
    element.jSetCaptionVisible(false);
  }
  

  
  public void xOnInit()
  {
    try
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
    text.setEditable(false);
    text.setAutoscrolls(false);
    
    if(Label_Use.getValue()){
           strText.setValue(text.getText());
      }
    else{
     text.setText("Multiline Text Indicator J.V.");   
    }
    panel.add(paneScrollPane, java.awt.BorderLayout.CENTER);
    
    //Code added to avoid JText Focus Lost Error
    text.setEditable(false);
    //text.setEnabled(false);
    text.setDisabledTextColor(fontColor.getValue());
    FocusListener FocusAr[]=text.getFocusListeners();
    if (FocusAr==null){
        
    }else{
        for(int i=0;i<FocusAr.length;i++){
            text.removeFocusListener(FocusAr[i]);
        }
            
    }
    
    

     
    Set_Text_Style(1);
   
     
     element.jRepaint();
    }catch(Exception ex)
    {
      System.out.println(ex);
    } 
     
    

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
          
         
  public void stop()
  {
    if(Label_Use.getValue()){
          text.setText(strText.getValue());
    }else{
    //text.setText("");    
    }
    
  }
   
   public void onDispose()
  {
    JPanel panel=element.getFrontPanel();
    panel.removeAll();
  }

  
  
  public void setPropertyEditor()   // Translated to german from other element
  {
    element.jAddPEItem("Schriftart",font, 0,0);
    element.jAddPEItem("Beschreibung",strText, 0,0);
    element.jAddPEItem("Farbe",fontColor, 0,0);
    element.jAddPEItem("Ausrichtung Hoz",ausrichtungH, 0,3);
    element.jAddPEItem("Farbe Fondo",Element_Color_Bakground, 0,0);
    element.jAddPEItem("Use_as_Label",Label_Use, 0,1);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Font");
    element.jSetPEItemLocale(d+1,language,"Text");
    element.jSetPEItemLocale(d+2,language,"Text_Color");
    element.jSetPEItemLocale(d+3,language,"Align Hoz");
    element.jSetPEItemLocale(d+4,language,"BackGround Color");
    element.jSetPEItemLocale(d+5,language, "Use as Label");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Fuente");
    element.jSetPEItemLocale(d+1,language,"Texto_Inicial");
    element.jSetPEItemLocale(d+2,language,"Color_Texto");
    element.jSetPEItemLocale(d+3,language,"Alineacion_Texto");
    element.jSetPEItemLocale(d+4,language,"Color_de_Fondo");
    element.jSetPEItemLocale(d+5,language,"Usar_como_Label");
   

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
  
   @Override
  public void loadFromStream(java.io.FileInputStream fis)
  {
      font.loadFromStream(fis);
      text.setFont(font.getValue());
      strText.loadFromStream(fis);
      //text.setText(strText.getValue());
      fontColor.loadFromStream(fis);
      text.setForeground(fontColor.getValue());
      ausrichtungH.loadFromStream(fis);
      Set_Text_Style(ausrichtungH.getValue());
      Element_Color_Bakground.loadFromStream(fis);
      text.setBackground(Element_Color_Bakground.getValue());
      Label_Use.loadFromStream(fis);
      if(Label_Use.getValue()){
          text.setText(strText.getValue());
      }
     //propertyChanged(null);

  }
   @Override 
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
      Label_Use.saveToStream(fos);
      
  }
  
  
}

