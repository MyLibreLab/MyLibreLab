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


public class Write_DO_RPI_JV extends JVSMain
{
  private Image image;
  String s = "";
  String sysOut_N_Err= ""; // String System Response without error
  String sysOut_Err= "";   // String System Response when error
  boolean Refresh_DO_State=false;
  boolean Old_DO_State=false;
  boolean first_time;
  final String Element_Tag= "#Element:|Write_DIO_RPI_JV|";
  final String Error_Tag="#Error:";
  final String Debug_Tag="#Debug_Msj:";  
// Properties
  // Inputs
    
  VSBoolean Enable_VM_in = new VSBoolean(false);
  VSInteger wPi_Pin_Number_in = new VSInteger();
  VSBoolean State_DO_in = new VSBoolean(false);
  VSBoolean Debug_Window_En_in = new VSBoolean(false);
  VSBoolean Error_in = new VSBoolean(false);
  
  // Outputs
  VSBoolean Enable_VM_out = new VSBoolean(false);
  VSInteger wPi_Pin_Number_out = new VSInteger();
  VSString System_out = new VSString();
  
  VSBoolean Debug_Window_En_out = new VSBoolean(false);
  VSBoolean Error_out = new VSBoolean(false);
  
  VSBoolean State_DO_out= new VSBoolean(false);
  String DO_State ="in"; // gpio mode <pin> in/out/pwm/clock/up/down/tri

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
    element.jSetName("Set_DIO_Pin_RPI_JV");
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
    setPin(2, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);
    setPin(3, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);
    setPin(4, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);
    
    setPin(5, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    setPin(6, ExternalIF.C_INTEGER, ExternalIF.PIN_INPUT);
    setPin(7, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    setPin(8, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    setPin(9, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    
    element.jSetPinDescription(0, "Enable_VM_out");
    element.jSetPinDescription(1, "wPi_DO_Pin_Number_out");
    element.jSetPinDescription(2, "DO_State_out");
    element.jSetPinDescription(3, "Debug_Window_Enable_out");
    element.jSetPinDescription(4, "Error_out");
        
    element.jSetPinDescription(5, "Enable_VM_in");
    element.jSetPinDescription(6, "wPi_DO_Pin_Number_in");
    element.jSetPinDescription(7, "DO_State_in");
    element.jSetPinDescription(8, "Debug_Window_Enable_in");
    element.jSetPinDescription(9, "Error_in");
    
 
    element.jSetResizable(false);
    element.jSetResizeSynchron(false);
    Refresh_DO_State=true;
    Old_DO_State=false;
    first_time=true;
  }

  public void initInputPins()
  {
    Enable_VM_in= (VSBoolean)element.getPinInputReference(5);
    wPi_Pin_Number_in= (VSInteger)element.getPinInputReference(6);
    State_DO_in= (VSBoolean)element.getPinInputReference(7);
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
    element.setPinOutputReference(2, State_DO_out);
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
    if(Error_in.getValue()==true && Enable_VM_in.getValue()==true){ //if There are error do not execute code and out the error to handle in the Error Handler VM.
       Error_out.setValue(true);
       
       if (Debug_Window_En_in.getValue()==true) {
          element.jConsolePrintln(Element_Tag+Error_Tag+"|Element_Not_Executed_Because_Error_in=TRUE|");
        }
    }
      
    if(Enable_VM_in.getValue()==true && State_DO_in.getValue()!=Old_DO_State){ // Refresh DO State only if is different.
       Refresh_DO_State=true;    
    }else{
        Refresh_DO_State=false;  
    }
    
        
    if ((Enable_VM_in.getValue()==true && first_time==true && Error_in.getValue()==false ) || (Enable_VM_in.getValue()==true && Refresh_DO_State==true && Error_in.getValue()==false) )  // Execute the first time and if there are error to continue trying.
    { 
      
      Refresh_DO_State=false;
      Old_DO_State=State_DO_in.getValue();
      try {
          //element.jConsolePrintln("Comando ingresado: " +in.getValue());
          //element.jConsolePrintln();
                    //String cmd = "manual.bat"; //Comando 
                    //Runtime.getRuntime().exec("msg * \"El comano es: \"" +in.getValue());
                    sysOut_N_Err="";
                    sysOut_Err="";
                    
                    if(State_DO_in.getValue()==true){
                         DO_State="1";
                        }else{
                        DO_State="0";
                    }
                    Process p =    Runtime.getRuntime().exec("gpio write " + wPi_Pin_Number_in.getValue() + " " +DO_State ); //gpio write <pin> 0/1
                    if (Debug_Window_En_in.getValue()) {      
                        element.jConsolePrintln(Element_Tag+Debug_Tag+"|Executing:"+"gpio write " + wPi_Pin_Number_in.getValue() + " " +DO_State +"|");
                        }
                    BufferedReader stdInput = new BufferedReader(new InputStreamReader(
                                        p.getInputStream()));

                        BufferedReader stdError = new BufferedReader(new InputStreamReader(
                                        p.getErrorStream()));

                        
                        while ((s = stdInput.readLine()) != null) {
                            sysOut_N_Err+=s;
                            sysOut_N_Err+="\n";
                            // Leemos la salida del comando
                            if (Debug_Window_En_in.getValue() && first_time==true) {
                                Refresh_DO_State=false;
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
                            if (Debug_Window_En_in.getValue() && first_time==true){
                                Refresh_DO_State=false;
                                element.jConsolePrintln("Error Output from system:\n");
                            }
                            if (Debug_Window_En_in.getValue()){
                                element.jConsolePrintln(s);
                            }
                             
                        System_out.setValue(sysOut_Err);        
                        }

                    Error_out.setValue(false);
                    first_time=false;
                    s=" ";
                    sysOut_Err=" ";
                    sysOut_N_Err=" ";
                                        
            } catch (IOException ioe) {
                    System.out.println (ioe);
                    if (Debug_Window_En_in.getValue()) {
                        element.jConsolePrintln(Element_Tag+Error_Tag+"|Command Error: gpio write "+ wPi_Pin_Number_in.getValue() + " " +DO_State+"|" );
                    }
                    Error_out.setValue(true);
                    
                    System_out.setValue("Command Error!!");
            }  
       
       
       
    }
   State_DO_out.setValue(State_DO_in.getValue());
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
