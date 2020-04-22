//********************************
//* Autor : Robinson Javier Velasquez
//* Date  : Sept-27-2016
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.SliderUI;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class Pane_Text_Control_JV extends JVSMain implements ChangeListener
        
{
  public int width=200, height=50;

  public JSlider SliderJV = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
  
  //StyledDocument Text_Style = SliderJV.getStyledDocument();
  SimpleAttributeSet Alin_Temp = new SimpleAttributeSet();
  
  
  private VSBoolean Vertical = new VSBoolean();
  private VSInteger InitValue = new VSInteger(0);
  private VSBoolean Transparent = new VSBoolean(false);
  private VSColor Element_Color_Bakground = new VSColor(Color.WHITE);

  private VSFont font=new VSFont(new Font("Dialog",1,11));
  private VSColor fontColor = new VSColor(Color.BLACK);
  private ExternalIF circuitElement;
  
  private VSInteger MinValue = new VSInteger();
  private VSInteger MaxValue = new VSInteger();
  private VSInteger Major_Step = new VSInteger();
  private VSInteger Minor_Step = new VSInteger();
  private VSBoolean Round_Near = new VSBoolean();
  private VSBoolean Invert = new VSBoolean();
  private VSBoolean HideTrack = new VSBoolean();
  
  

  // public void paint(java.awt.Graphics g)
  public void paint(java.awt.Graphics g)
   {
   super.paint(g);
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
    
    
    setName("JSlide_JV");
    element.jSetCaptionVisible(true);
    UIDefaults defaults = UIManager.getDefaults();
    defaults.put("Slider.thumbHeight", 70); // change height
    defaults.put("Slider.thumbWidth", 50); // change width
    
    
    SliderJV.putClientProperty("Slider.isFilled", Boolean.TRUE);
    SliderJV.putClientProperty("JSlider.isFilled", Boolean.TRUE);
    int temp=50;
    SliderJV.putClientProperty("Slider.thumbHeight", temp);
    temp=70;
    SliderJV.putClientProperty("Slider.thumbWidth", temp);
    
    temp=50;
    SliderJV.putClientProperty("JSlider.thumbHeight", temp);
    temp=70;
    SliderJV.putClientProperty("JSlider.thumbWidth", temp);
    SliderJV.putClientProperty(temp, temp);
    
    SliderJV.setBackground(Color.DARK_GRAY);
    SliderJV.setForeground(Color.WHITE);
    
    SliderJV.setFont(font.getValue());
    Major_Step.setValue(20);
    SliderJV.setMajorTickSpacing(Major_Step.getValue());
    Minor_Step.setValue(10);
    SliderJV.setMinorTickSpacing(Minor_Step.getValue());
    SliderJV.setPaintTicks(true);
    SliderJV.setPaintLabels(true);
    Invert.setValue(false);
    SliderJV.setInverted(Invert.getValue());
    HideTrack.setValue(false);
    SliderJV.setPaintTrack(!HideTrack.getValue());
    MaxValue.setValue(100);
    MinValue.setValue(0);
    SliderJV.setMaximum(MaxValue.getValue());
    SliderJV.setMinimum(MinValue.getValue());
    Round_Near.setValue(true);
    SliderJV.setSnapToTicks(Round_Near.getValue());
    
    SliderJV.repaint();
    element.jRepaint();
    
  }
  

  
  public void xOnInit()
  {

    JPanel panel =element.getFrontPanel();
    panel.setLayout(new java.awt.BorderLayout());
    //panel.setBackground(Color.DARK_GRAY);
    
    if(SliderJV==null){
       SliderJV = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
    }
    element.setAlwaysOnTop(true);

    //ScrollB.setForeground(fontColor.getValue());
    //ScrollB.setBackground(Element_Color_Bakground.getValue());

    panel.add(SliderJV); 
 
    
    
    SliderJV.setSize(panel.getWidth(),panel.getHeight());
    
    
    SliderJV.setValue(InitValue.getValue());
    
    SliderJV.setVisible(true);
    
    SliderJV.setExtent(0);
          
    SliderJV.addChangeListener(this);
    
   
    SliderJV.setFocusTraversalKeysEnabled(true);
    
    SliderJV.repaint();

    element.jRepaint();
    
  }
             
           
        

  
  
  public void setPropertyEditor()   // Translated to german from other element
  {
    element.jAddPEItem("Schriftart",font, 0,0);
    element.jAddPEItem("Vertical",Vertical, 0,0);
    element.jAddPEItem("Farbe",fontColor, 0,0);
    element.jAddPEItem("Transparent BackGround",Transparent, 0,1);
    element.jAddPEItem("Farbe Fondo",Element_Color_Bakground, 0,0);
    element.jAddPEItem("Init_Value",InitValue,-999999,999999);
    element.jAddPEItem("Min_Value",MinValue,-999999,999999);
    element.jAddPEItem("Max_Value",MaxValue,-999999,999999);
    element.jAddPEItem("Major_Step",Major_Step,-999999,999999);
    element.jAddPEItem("Minor_Step",Minor_Step,-999999,999999);
    element.jAddPEItem("Round to near",Round_Near,0,0);
    element.jAddPEItem("Invert",Invert,0,0);
    element.jAddPEItem("Hide Track",HideTrack,0,0);
 
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Font");
    element.jSetPEItemLocale(d+1,language,"Vertical");
    element.jSetPEItemLocale(d+2,language,"Font_Color");
    element.jSetPEItemLocale(d+3,language,"Transparent BackGround");
    element.jSetPEItemLocale(d+4,language,"BackGround Color");
    element.jSetPEItemLocale(d+5,language,"Init_Value");
    element.jSetPEItemLocale(d+6,language,"Min_Value");
    element.jSetPEItemLocale(d+7,language,"Max_Value");
    element.jSetPEItemLocale(d+8,language,"Major_Step");
    element.jSetPEItemLocale(d+9,language,"Minor_Step");
    element.jSetPEItemLocale(d+10,language,"Round to Next");
    element.jSetPEItemLocale(d+11,language,"Invert");
    element.jSetPEItemLocale(d+12,language,"Hide Track");
   
    
    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Fuente");
    element.jSetPEItemLocale(d+1,language,"Vertical");
    element.jSetPEItemLocale(d+2,language,"Color_Texto");
    element.jSetPEItemLocale(d+3,language,"Fondo Transparente");
    element.jSetPEItemLocale(d+4,language,"Color de Fondo");
    element.jSetPEItemLocale(d+5,language,"Valor Inicial");
    element.jSetPEItemLocale(d+6,language,"Min_Valor");
    element.jSetPEItemLocale(d+7,language,"Max_Valor");
    element.jSetPEItemLocale(d+8,language,"Divisiones_Mayor");
    element.jSetPEItemLocale(d+9,language,"Divisiones Menor");
    element.jSetPEItemLocale(d+10,language,"Redondear al siguiente");
    element.jSetPEItemLocale(d+11,language,"Invertir");
    element.jSetPEItemLocale(d+12,language,"Esconder Linea");

  }
  
  
  public void propertyChanged(Object o)
  {  
    if (o.equals(InitValue))
    {
       SliderJV.setValue(InitValue.getValue());
    }
    if (o.equals(Vertical))
    {
     int Height_Temp=element.jGetHeight();
     int Width_Temp=element.jGetWidth();
     
     if(Vertical.getValue()){  
     SliderJV.setOrientation(JSlider.VERTICAL);
     SliderJV.setSize(element.jGetHeight(),element.jGetWidth());
     setSize(height,width);
     }else{
     SliderJV.setOrientation(JSlider.HORIZONTAL);
     SliderJV.setSize(element.jGetWidth(),element.jGetHeight());  
     setSize(width,height);
     }
        
    }
    if (o.equals(font))
    {
      SliderJV.setFont(font.getValue());
      
    }
    if (o.equals(fontColor))
    {
      SliderJV.setForeground(fontColor.getValue());
      
    }
    if (o.equals(Transparent))
    {   
      SliderJV.setOpaque(!Transparent.getValue());
      
    }
    if (o.equals(Element_Color_Bakground)){
    SliderJV.setBackground(Element_Color_Bakground.getValue());
    }
    
    if (o.equals(Major_Step)){
    SliderJV.setMajorTickSpacing(Major_Step.getValue());
    }
    if (o.equals(Minor_Step)){
    SliderJV.setMinorTickSpacing(Minor_Step.getValue());
    }
    
    if (o.equals(Invert)){
    SliderJV.setInverted(Invert.getValue());
    }
    
    if (o.equals(HideTrack)){
     SliderJV.setPaintTrack(!HideTrack.getValue());
    }
    if (o.equals(MaxValue)){
     SliderJV.setMaximum(MaxValue.getValue());
    }
    if (o.equals(MinValue)){
     SliderJV.setMinimum(MinValue.getValue());
    }
    if (o.equals(Round_Near)){
     SliderJV.setSnapToTicks(Round_Near.getValue());
    }
    SliderJV.repaint();
  }


  public void start()
  {
      SliderJV.setValue(InitValue.getValue());
      
      circuitElement=element.getCircuitElement();
      circuitElement.Change(0,InitValue);
    
  }  
  
  public void loadFromStream(java.io.FileInputStream fis)
  {
      font.loadFromStream(fis);
      Vertical.loadFromStream(fis);
      fontColor.loadFromStream(fis);
      Transparent.loadFromStream(fis);
      Element_Color_Bakground.loadFromStream(fis);
      InitValue.loadFromStream(fis);
      
        MinValue.loadFromStream(fis);
        MaxValue.loadFromStream(fis);
        Major_Step.loadFromStream(fis);
        Minor_Step.loadFromStream(fis);
        Round_Near.loadFromStream(fis);
        Invert.loadFromStream(fis);
        HideTrack.loadFromStream(fis);
      
    if(font==null){
          
    }else{
      SliderJV.setFont(font.getValue());    
    }
    
    if(Vertical.getValue()==false || Vertical==null){

       Vertical.setValue(false);
       //width=50; height=200;
       SliderJV.setOrientation(JSlider.HORIZONTAL);
   }else{
       //width=200; height=50;
       SliderJV.setOrientation(JSlider.VERTICAL);
   }
       
      if(fontColor==null){
          fontColor.setValue(Color.WHITE);          
      }
      SliderJV.setForeground(fontColor.getValue());
      
      if(Transparent==null || Transparent.getValue()==false){
       Transparent.setValue(false);
       SliderJV.setOpaque(true); // True =  Con Color
      }else
      {
       SliderJV.setOpaque(false); // False = Transparente
      }
      
      if(Element_Color_Bakground==null){
         Element_Color_Bakground.setValue(Color.DARK_GRAY);
      }
      SliderJV.setBackground(Element_Color_Bakground.getValue());
      
    SliderJV.setMajorTickSpacing(Major_Step.getValue());
    SliderJV.setMinorTickSpacing(Minor_Step.getValue());
    SliderJV.setPaintTicks(true);
    SliderJV.setPaintLabels(true);
    SliderJV.setInverted(Invert.getValue());
    
    SliderJV.setPaintTrack(!HideTrack.getValue());
    
    SliderJV.setMinimum(MinValue.getValue());
    SliderJV.setMaximum(MaxValue.getValue());
 
    SliderJV.setSnapToTicks(Round_Near.getValue());
    
 
  }
 
  public void saveToStream(java.io.FileOutputStream fos)
  {
      font.setValue(SliderJV.getFont());
      font.saveToStream(fos);
      
      if(SliderJV.getOrientation()==JSlider.VERTICAL){
      Vertical.setValue(true);    
      }else{
      Vertical.setValue(false);    
      }
      Vertical.saveToStream(fos);
      
      fontColor.setValue(SliderJV.getForeground());
      fontColor.saveToStream(fos);
      
      Transparent.setValue(!SliderJV.isOpaque());
      Transparent.saveToStream(fos);
      
      Element_Color_Bakground.setValue(SliderJV.getBackground());
      Element_Color_Bakground.saveToStream(fos);
      
      if(InitValue==null){
          InitValue.setValue(0);
      }
      InitValue.saveToStream(fos);
      
      
        MinValue.saveToStream(fos);
        MaxValue.saveToStream(fos);
        Major_Step.saveToStream(fos);
        Minor_Step.saveToStream(fos);
        Round_Near.saveToStream(fos);
        Invert.saveToStream(fos);
        HideTrack.saveToStream(fos);
      
      

  }

    @Override
    public void stateChanged(ChangeEvent e) {
        circuitElement=element.getCircuitElement();
        circuitElement.Change(0,new VSInteger(SliderJV.getValue()));
        //System.out.println(SliderJV.getValue());
    }

}


