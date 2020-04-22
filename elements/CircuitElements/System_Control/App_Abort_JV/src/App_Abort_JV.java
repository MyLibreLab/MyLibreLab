//********************************
//* Autor : Robinson Javier Velasquez
//* Date  : Jul-24-2016
//* Email : javiervelasquez@gmail.com
//* licence : non commercial use.
//* Updated : JV 30-AGO-2017 
//********************************

import VisualLogic.*;
import java.awt.*;
import java.awt.event.*;
import tools.*;
import VisualLogic.variables.*;
import java.io.*;

import VisualLogic.*;
import VisualLogic.variables.*;
import tools.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.awt.geom.Rectangle2D;
import javax.swing.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class App_Abort_JV extends JVSMain
{
private Image image;
  String s = " ";
  String sysOut_N_Err= " "; // String System Response without error
  String sysOut_Err= " ";   // String System Response when error
  boolean firstTime=false;
  // Properties
  // Inputs
    
  VSBoolean Abort_VMs = new VSBoolean(false);
  VSBoolean Dispose_VM = new VSBoolean(false);

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
    element.jSetName("Contol_jFrame_JV");
    initPinVisibility(false,false,false,false);
    setSize(50,50);
    element.jSetLeftPins(0);
    element.jSetRightPins(0);
    element.jSetLeftPinsVisible(true);
    element.jSetRightPinsVisible(true);
    initPins(0,0,0,2);
    element.jSetInnerBorderVisibility(false);

     image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");

    setPin(0, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    setPin(1, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    

    

    element.jSetPinDescription(0, "Close ALL VMs (F)");
    element.jSetPinDescription(1, "Close This VM (F)");
    

   
    element.jSetResizable(false);
    element.jSetResizeSynchron(false);
    firstTime=true;
  }

  public void initInputPins()
  {
    Abort_VMs= (VSBoolean)element.getPinInputReference(0);
    Dispose_VM= (VSBoolean)element.getPinInputReference(1);
    
    

    
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
    Window topFrame = javax.swing.FocusManager.getCurrentManager().getActiveWindow(); 
    //JFrame frame = (JFrame) SwingUtilities.getRoot(element.getFrontPanel());
    
    
    
    if(topFrame!=null)
    {
        if(Dispose_VM!=null && Dispose_VM.getValue()){ 
        topFrame.dispose();
        
        //element.jGetBasis().vsStop();
        //element.jStopVM();
        //element.jGetBasis().vsClose();
        
        topFrame.dispatchEvent(new WindowEvent(topFrame, WindowEvent.WINDOW_CLOSING));
        topFrame.dispatchEvent(new WindowEvent(topFrame, WindowEvent.WINDOW_CLOSED));
        topFrame.removeAll();
        
        }
    }
    if (Abort_VMs!=null && Abort_VMs.getValue())
    {   
     System.exit(0); 
    }  
     
  }


}
