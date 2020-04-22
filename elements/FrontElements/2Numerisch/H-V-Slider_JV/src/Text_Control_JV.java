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
import tools.*;
import java.awt.geom.Rectangle2D;


public class Text_Control_JV extends JVSMain
{
  private ExternalIF panelElement;
  private Image image;
  
  private String oldValue;
  private boolean firstTime=true;
  private VSInteger out=new VSInteger();
  private VSString outStr=new VSString();
  private VSInteger val;
  private boolean changed=false;

  public void paint(java.awt.Graphics g)
  {
    //drawImageCentred(g,image);
    drawImageCentred(g,image);
  }

  public void init()
  {
    initPins(0,2,0,0);
    setSize(70,45);

    element.jSetInnerBorderVisibility(true);
    initPinVisibility(false,false,false,true);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");

    //setPin(0,ExternalIF.C_STRING,element.PIN_INPUT);
    setPin(0, ExternalIF.C_INTEGER, ExternalIF.PIN_OUTPUT);
    setPin(1, ExternalIF.C_STRING, ExternalIF.PIN_OUTPUT);
    
    element.jSetCaptionVisible(true);
    element.jSetCaption("JSlider_JV");

    setName("JSlider_JV");
  }

  public void changePin(int pinIndex, Object value)
  {
    changed=true;
    val=(VSInteger)value;
    if (val!=null)
    {
      out.setValue(val.getValue());
      outStr.setValue(String.valueOf(val.getValue()));
      
      element.notifyPin(0);
      element.notifyPin(1);
    }
  }

  public void initInputPins()
  {
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
    element.setPinOutputReference(1,outStr);
  }



  public void process()
  {
  }


}


