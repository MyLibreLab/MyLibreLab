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


public class cmdWrite_JV extends JVSMain
{
  private Image image;
  String s = " ";
  String sysOut_N_Err= " "; // String System Response without error
  String sysOut_Err= " ";   // String System Response when error
  boolean firstTime=false;
  // Properties
  // Inputs
    
  VSBoolean enable = new VSBoolean(false);
  VSString CommandIn = new VSString();
  VSBoolean Debug_En = new VSBoolean(false);
  // Outputs

  VSString Response_Out = new VSString();
  VSBoolean Error_out = new VSBoolean(false);

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
    element.jSetName("cmdWrite_JV");
    initPinVisibility(false,false,false,false);
    setSize(50,50);
    element.jSetLeftPins(3);
    element.jSetRightPins(2);
    element.jSetLeftPinsVisible(true);
    element.jSetRightPinsVisible(true);
    initPins(0,2,0,3);
    element.jSetInnerBorderVisibility(false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");

    setPin(0, ExternalIF.C_STRING, ExternalIF.PIN_OUTPUT);
    setPin(1, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);
    setPin(2, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    setPin(3, ExternalIF.C_STRING, ExternalIF.PIN_INPUT);
    setPin(4, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    
    element.jSetPinDescription(0, "System_Out");
    element.jSetPinDescription(1, "Error_Out");
    element.jSetPinDescription(2, "Enable_Element");
    element.jSetPinDescription(3, "Command_Input");
    element.jSetPinDescription(4, "Enable_Debug_Window");
    
    
 
    element.jSetResizable(false);
    element.jSetResizeSynchron(false);
    firstTime=true;
  }

  public void initInputPins()
  {
    enable= (VSBoolean)element.getPinInputReference(2);
    CommandIn= (VSString)element.getPinInputReference(3);
    Debug_En= (VSBoolean)element.getPinInputReference(4);
    if(Debug_En==null){
       Debug_En=new VSBoolean(false);
    }
    
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0, Response_Out);
    element.setPinOutputReference(1, Error_out);
  }

  public void start()
  {
  }

  public void stop()
  {
  }

  public void process()
  {
    if(Debug_En==null){
       Debug_En=new VSBoolean(false);
    }
    
    if (enable.getValue() && enable!=null )
    { 
      
      try {
          //element.jConsolePrintln("Comando ingresado: " +in.getValue());
          //element.jConsolePrintln();
                    //String cmd = "manual.bat"; //Comando 
                    //Runtime.getRuntime().exec("msg * \"El comano es: \"" +in.getValue());
                    
                    if(CommandIn.getValue().length()<=0){
                       CommandIn.setValue("java -version");
                    }
                    Process p = Runtime.getRuntime().exec(CommandIn.getValue()); 
                    
                    BufferedReader stdInput = new BufferedReader(new InputStreamReader(
                                        p.getInputStream()));

                        BufferedReader stdError = new BufferedReader(new InputStreamReader(
                                        p.getErrorStream()));

                        
                        while ((s = stdInput.readLine()) != null) {
                            sysOut_N_Err+=s;
                            sysOut_N_Err+="\n";
                            // Leemos la salida del comando
                            if (Debug_En.getValue() && firstTime==true) {
                                firstTime=false;
                                element.jConsolePrintln("Standard output from system:\n");
                            }
                            if (Debug_En.getValue()){
                                element.jConsolePrintln(s);
                            }
                        Response_Out.setValue(sysOut_N_Err);
                        }

                        
                        while ((s = stdError.readLine()) != null) {
                            sysOut_Err+=s;
                            sysOut_Err+="\n";
                            // Leemos los errores si los hubiera
                            if (Debug_En.getValue() && firstTime==true){
                                firstTime=false;
                                element.jConsolePrintln("Error Output from system:\n");
                            }
                            if (Debug_En.getValue()){
                                element.jConsolePrintln(s);
                            }
                             
                        Response_Out.setValue(sysOut_Err);
                        
                        }

                    Error_out.setValue(false);
                    s=" ";
                    sysOut_Err=" ";
                    sysOut_N_Err=" ";
                    
            } catch (IOException ioe) {
                    System.out.println (ioe);
                    if (Debug_En.getValue()) {
                        element.jConsolePrintln("Error ejecutando comando "+CommandIn.getValue());
                    }
                    Error_out.setValue(true);
                    Response_Out.setValue("Command Error!!");
            }  
       
       element.notifyPin(0);
       element.notifyPin(1);
       //Debug_En.setValue(false);
    }
  }


}
