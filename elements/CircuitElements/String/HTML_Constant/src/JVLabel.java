//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2017  Javier Velásquez                         *
//*                                                                           *
//*****************************************************************************
import VisualLogic.*;
import VisualLogic.variables.*;
import tools.*;
import java.awt.*;
import javax.swing.*;



public class JVLabel extends JVSMain
{
  private Image image=null;
  //private Image image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
  private VSString strText = new VSString();
  private VSInteger ausrichtungH = new VSInteger();
  private VSInteger ausrichtungV = new VSInteger();

  private VSFont font=new VSFont(new Font("Monospaced",0,11));
  private VSColor fontColor = new VSColor(Color.BLACK);
  private String[] values= new String[3];
  private JLabel label = new JLabel("<HTML> <HEAD> <TITLE>Example 1 JV</TITLE> </HEAD> <BODY> HTML Ausgabe Preview.<BR> <FONT SIZE=\"1\">Text Size 1.</FONT><BR> <FONT SIZE=\"2\">Text Size 2.</FONT><BR> <FONT SIZE=\"4\">Text Size 4.</FONT><BR> <FONT SIZE=\"+1\">Text Size +1 (Same Size 4).</FONT><BR> <FONT FACE=\"Arial\" SIZE=\"5\" COLOR=\"F39C12\">Formated Text JV.</FONT> </BODY> </HTML>");
  private VSString StrOut= new VSString();


  public JVLabel()
  {
    strText.setValue("HTML/Text Constant J.V.");

    myInit();
  }
  
  public void myInit()
  {

    strText.setValue(label.getText());
    font.setValue(label.getFont());
    fontColor.setValue(label.getForeground());
    //strText.setText(label.getValue());
    int hali=label.getHorizontalAlignment();
    if (hali==label.LEFT)
    {
      ausrichtungH.setValue(0);
    }
    if (hali==label.CENTER)
    {
      ausrichtungH.setValue(1);
    }
    if (hali==label.RIGHT)
    {
      ausrichtungH.setValue(2);
    }

    int vali=label.getVerticalAlignment();
    if (vali==label.TOP)
    {
      ausrichtungV.setValue(0);
    }
    if (vali==label.CENTER)
    {
      ausrichtungV.setValue(1);
    }
    if (vali==label.BOTTOM)
    {
      ausrichtungV.setValue(2);
    }

  }


  public void paint(java.awt.Graphics g)
  {
  }
   
  public void init()
  {
    initPins(0,1,0,0);
    setSize(250 ,150);

    element.jSetInnerBorderVisibility(true);
    element.jSetTopPinsVisible(false);
    element.jSetLeftPinsVisible(false);
    element.jSetBottomPinsVisible(false);
    element.jSetResizable(true);

    setPin(0,ExternalIF.C_STRING,element.PIN_OUTPUT);
    element.jSetPinDescription(0,"StringOut");
    
    
    setName("HTML/Text JV");
    

  }
  public void initInputPins()
  {
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,StrOut);
  }
  
    public void start()
  {
      StrOut.setValue(strText.getValue());
      
      element.notifyPin(0);
  }
  
  public void process()
  {

  }

  public void xOnInit()
  {
    try
    {
      JPanel panel =element.getFrontPanel();
      panel.setLayout(new java.awt.BorderLayout());

      panel.add(label, java.awt.BorderLayout.CENTER);
      element.setAlwaysOnTop(true);
    } catch(Exception ex)
    {
      System.out.println(ex);
    }

  }


  public void setPropertyEditor()
  {
    element.jAddPEItem("Schriftart",font, 0,0);
    element.jAddPEItem("Html",strText, 0,0);
    element.jAddPEItem("Farbe",fontColor, 0,0);
    element.jAddPEItem("Ausrichtung Hoz",ausrichtungH, 0,2);
    element.jAddPEItem("Ausrichtung Vert",ausrichtungV, 0,2);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Font");
    element.jSetPEItemLocale(d+1,language,"Html");
    element.jSetPEItemLocale(d+2,language,"Color");
    element.jSetPEItemLocale(d+3,language,"Align Hoz");
    element.jSetPEItemLocale(d+4,language,"Align Vert");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Fuente");
    element.jSetPEItemLocale(d+1,language,"Html");
    element.jSetPEItemLocale(d+2,language,"Color");
    element.jSetPEItemLocale(d+3,language,"Alineacion Hoz");
    element.jSetPEItemLocale(d+4,language,"Alineacion Vert");

  }
  public void propertyChanged(Object o)
  {
    //if (o.equals(strText))
    {
      label.setText(strText.getValue());
    }
    //if (o.equals(font))
    {
      label.setFont(font.getValue());
    }
    //if (o.equals(fontColor))
    {
      label.setForeground(fontColor.getValue());
    }
    //if (o.equals(ausrichtungH))
    {
      int hali=ausrichtungH.getValue();
      if (hali==0)
      {
        label.setHorizontalAlignment(label.LEFT);
      }
      if (hali==1)
      {
        label.setHorizontalAlignment(label.CENTER);
      }
      if (hali==2)
      {
        label.setHorizontalAlignment(label.RIGHT);
      }
    }

    //if (o.equals(ausrichtungV))
    {
      int vali=ausrichtungV.getValue();
      if (vali==0)
      {
        label.setVerticalAlignment(label.TOP);
      }
      if (vali==1)
      {
        label.setVerticalAlignment(label.CENTER);
      }
      if (vali==2)
      {
        label.setVerticalAlignment(label.BOTTOM);
      }
    }
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
      font.loadFromStream(fis);
      strText.loadFromStream(fis);
      fontColor.loadFromStream(fis);
      ausrichtungH.loadFromStream(fis);
      ausrichtungV.loadFromStream(fis);

     propertyChanged(null);

  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      font.saveToStream(fos);
      strText.saveToStream(fos);
      fontColor.saveToStream(fos);
      ausrichtungH.saveToStream(fos);
      ausrichtungV.saveToStream(fos);
  }
}
