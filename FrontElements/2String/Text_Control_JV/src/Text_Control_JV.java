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
import tools.*;
import java.awt.geom.Rectangle2D;

public class Text_Control_JV extends JVSMain
{
  private ExternalIF panelElement;
  private Image image;
  
  private String oldValue;
  private boolean firstTime=true;
  private VSString out=new VSString();
  private VSString val;
  private boolean changed=false;

  public void paint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }

  public void init()
  {
    initPins(0,1,0,0);
    setSize(45,40);

    element.jSetInnerBorderVisibility(true);
    initPinVisibility(false,true,false,false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");

    //setPin(0,ExternalIF.C_STRING,element.PIN_INPUT);
    setPin(0, ExternalIF.C_STRING, ExternalIF.PIN_OUTPUT);
    
    element.jSetCaptionVisible(false);
    element.jSetCaption("Pane_Text_Control_JV");

    setName("Text_Control_JV");
  }

  public void changePin(int pinIndex, Object value)
  {
    changed=true;
    val=(VSString)value;
    if (val!=null)
    {
      out.setValue(val.getValue());
      element.notifyPin(0);
    }
  }

  public void initInputPins()
  {
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }



  public void process()
  {
  }


}


