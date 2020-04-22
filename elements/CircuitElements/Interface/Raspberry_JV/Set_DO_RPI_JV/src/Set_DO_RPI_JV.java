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


public class Set_DO_RPI_JV extends JVSMain
{
  private Image image;
  String s = "";
  String sysOut_N_Err= ""; // String System Response without error
  String sysOut_Err= "";   // String System Response when error
  boolean firstTime=false;
  final String Element_Tag= "#Element:|Set_DO_RPI_JV|";
  final String Error_Tag="#Error:";
  final String Debug_Tag="#Debug_Msj:";
  // Properties
  // Inputs
    
  VSBoolean Enable_VM_in = new VSBoolean(false);
  VSInteger wPi_Pin_Number_in = new VSInteger();
  VSBoolean Input_Output_Select = new VSBoolean(false);
  VSBoolean Debug_Window_En_in = new VSBoolean(false);
  VSBoolean Error_in = new VSBoolean(false);
  
  // Outputs
  VSBoolean Enable_VM_out = new VSBoolean(false);
  VSInteger wPi_Pin_Number_out = new VSInteger();
  VSString System_out = new VSString();
  VSBoolean Debug_Window_En_out = new VSBoolean(false);
  VSBoolean Error_out = new VSBoolean(false);
  String PinMode ="in"; // gpio mode <pin> in/out/pwm/clock/up/down/tri

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
    element.jSetName("Set_DO_Pin_RPI_JV");
    initPinVisibility(false,false,false,false);
    setSize(50,50);
    element.jSetLeftPins(5);
    element.jSetRightPins(5);
    element.jSetLeftPinsVisible(true);
    element.jSetRightPinsVisible(true);
    initPins(0,5,0,5); //initPins(0,2,0,4);
    element.jSetInnerBorderVisibility(false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");

     
    setPin(0, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);
    setPin(1, ExternalIF.C_INTEGER, ExternalIF.PIN_OUTPUT);
    setPin(2, ExternalIF.C_STRING, ExternalIF.PIN_OUTPUT);
    setPin(3, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);
    setPin(4, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);
    
    setPin(5, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    setPin(6, ExternalIF.C_INTEGER, ExternalIF.PIN_INPUT);
    //setPin(7, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    setPin(8, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    setPin(9, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    
    element.jSetPinDescription(0, "Enable_VM_out");
    element.jSetPinDescription(1, "wPi_Pin_Number_out");
    element.jSetPinDescription(2, "System_out");
    element.jSetPinDescription(3, "Debug_Window_Enable_out");
    element.jSetPinDescription(4, "Error_out");
        
    element.jSetPinDescription(5, "Enable_VM_in");
    element.jSetPinDescription(6, "wPi_Pin_Number_in");
    element.jSetPinDescription(7, "N/C");
    element.jSetPinDescription(8, "Debug_Window_Enable_in");
    element.jSetPinDescription(9, "Error_in");
    
 
    element.jSetResizable(false);
    element.jSetResizeSynchron(false);
    firstTime=true;
  }

  public void initInputPins()
  {
    Enable_VM_in= (VSBoolean)element.getPinInputReference(5);
    wPi_Pin_Number_in= (VSInteger)element.getPinInputReference(6);
    //Input_Output_Select= (VSBoolean)element.getPinInputReference(7);
    Debug_Window_En_in= (VSBoolean)element.getPinInputReference(8);
    if (Debug_Window_En_in==null){
       Debug_Window_En_in=new VSBoolean(false);
    }
    Error_in= (VSBoolean)element.getPinInputReference(9);
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0, Enable_VM_out); 
    element.setPinOutputReference(1, wPi_Pin_Number_out);
    element.setPinOutputReference(2, System_out);
    element.setPinOutputReference(3, Debug_Window_En_out);
    element.setPinOutputReference(4, Error_out);
  }

  public void start()
  {
  }

  public void stop()
  {
  }

  public void process()
  { 
    if (Debug_Window_En_in==null){
       Debug_Window_En_in=new VSBoolean(false);
    }  
      
    if(Error_in.getValue() && Enable_VM_in.getValue()){ //if There are error do not execute code and out the error to handle in the Error Handler VM.
       Error_out.setValue(true);
       element.notifyPin(4);
       
       if (Debug_Window_En_in.getValue()) {
          element.jConsolePrintln(Element_Tag+Error_Tag+"|Element_Not_Executed_Because_Error_in=TRUE|");
        }
    }   
      
    if (Enable_VM_in.getValue() && firstTime==true && Error_in.getValue()==false) // Only executes one time if no error.
    { 
        
      try {
          //element.jConsolePrintln("Comando ingresado: " +in.getValue());
          //element.jConsolePrintln();
                    //String cmd = "manual.bat"; //Comando 
                    //Runtime.getRuntime().exec("msg * \"El comano es: \"" +in.getValue());
                    
                    sysOut_N_Err="";
                    sysOut_Err="";
                    
                    Process p = Runtime.getRuntime().exec("gpio mode " + wPi_Pin_Number_in.getValue() + " out"); 
                    if (Debug_Window_En_in.getValue()) {      
                        element.jConsolePrintln(Element_Tag+Debug_Tag+"|Executing:"+"gpio mode " + wPi_Pin_Number_in.getValue() + " out|");
                        }
                    BufferedReader stdInput = new BufferedReader(new InputStreamReader(
                                        p.getInputStream()));

                        BufferedReader stdError = new BufferedReader(new InputStreamReader(
                                        p.getErrorStream()));

                        
                        while ((s = stdInput.readLine()) != null) {
                            sysOut_N_Err+=s;
                            sysOut_N_Err+="\n";
                            // Leemos la salida del comando
                            if (Debug_Window_En_in.getValue() && firstTime==true) {
                                firstTime=false;
                                element.jConsolePrintln("Standard output from system:\n");
                            }
                            if (Debug_Window_En_in.getValue()){
                                element.jConsolePrintln(s);
                            }
                        System_out.setValue(sysOut_N_Err);  
                        }

                        
                        while ((s = stdError.readLine()) != null) {
                            sysOut_Err+=s;
                            sysOut_Err+="\n";
                            // Leemos los errores si los hubiera
                            if (Debug_Window_En_in.getValue() && firstTime==true){
                                firstTime=false;
                                element.jConsolePrintln("Error Output from system:\n");
                            }
                            if (Debug_Window_En_in.getValue()){
                                element.jConsolePrintln(s);
                            }
                             
                        System_out.setValue(sysOut_Err);        
                        }

                    Error_out.setValue(false);
                    firstTime=false; // If no error do not execute this command more times.
                    s=" ";
                    sysOut_Err=" ";
                    sysOut_N_Err=" ";
                    
            } catch (IOException ioe) {
                    System.out.println (ioe);
                    if (Debug_Window_En_in.getValue()) {
                        element.jConsolePrintln(Element_Tag+Error_Tag+"|Command Error: gpio mode " + wPi_Pin_Number_in.getValue() + " " +PinMode+"|");
                    }
                    Error_out.setValue(true);
                    System_out.setValue("Command Error!!");
                    
                    
            }  

       
       
    }
   
   element.notifyPin(2);
   element.notifyPin(4);
   Enable_VM_out.setValue(Enable_VM_in.getValue());
   //Enable_VM_in.setValue(false);
   element.notifyPin(0);
   wPi_Pin_Number_out.setValue(wPi_Pin_Number_in.getValue());
   element.notifyPin(1);
   Debug_Window_En_out.setValue(Debug_Window_En_in.getValue());
   element.notifyPin(3);
  }
  

}
