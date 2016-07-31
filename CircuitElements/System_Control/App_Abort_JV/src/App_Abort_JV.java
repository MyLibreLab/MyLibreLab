//********************************
//* Autor : Robinson Javier Velasquez
//* Date  : Jul-24-2016
//* Email : javiervelasquez@gmail.com
//* licence : non commercial use.
//********************************

import VisualLogic.*;
import java.awt.*;
import java.awt.event.*;
import tools.*;
import VisualLogic.variables.*;
import java.io.*;


public class App_Abort_JV extends JVSMain
{
private Image image;
  String s = " ";
  String sysOut_N_Err= " "; // String System Response without error
  String sysOut_Err= " ";   // String System Response when error
  boolean firstTime=false;
  // Properties
  // Inputs
    
  VSBoolean Enable_VM = new VSBoolean();
  // Outputs

 
  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }

  public void paint(java.awt.Graphics g)
  {
    if (image!=null) drawImageCentred(g,image);
  }

  public void setPropertyEditor()
  {
  
    localize();
  }

  private void localize()
  {
    String language;
    int d=6;
    language="en_US";
    
    language="es_ES";
  }


  public void init()
  {
    element.jSetName("Abort_Application_JV");
    initPinVisibility(false,false,false,false);
    setSize(50,50);
    element.jSetLeftPins(0);
    element.jSetRightPins(0);
    element.jSetLeftPinsVisible(true);
    element.jSetRightPinsVisible(true);
    initPins(0,0,0,1);
    element.jSetInnerBorderVisibility(false);

     image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");

    setPin(0, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);

    

    element.jSetPinDescription(0, "Abort_Application");

    
    
 
    element.jSetResizable(false);
    element.jSetResizeSynchron(false);
    firstTime=true;
  }

  public void initInputPins()
  {
    Enable_VM= (VSBoolean)element.getPinInputReference(0);

    
  }

  public void initOutputPins()
  {


  }

  public void start()
  {
  }

  public void stop()
  {
  }

  public void process()
  {
    if (Enable_VM.getValue())
    {
        this.onDispose();
        System.exit(0);
    } 
      
     
  }


}
