//********************************
//* Autor : Robinson Javier Velasquez
//* Date  : Jul-24-2016
//* Email : javiervelasquez@gmail.com
//* licence : non commercial use.
//********************************

import VisualLogic.*;
import java.awt.*;
//import java.awt.event.*;
import tools.*;
import VisualLogic.variables.*;
import java.io.*;


public class Read_RPI_Temp_JV extends JVSMain
{
  private Image image;
  String s = "";
  String sysOut_N_Err= ""; // String System Response without error
  String sysOut_Err= "";   // String System Response when error
  
  
  boolean first_time;
  final String Element_Tag= "#Element:|Read_RPI_TEMP_JV|";
  final String Error_Tag="#Error:";
  final String Debug_Tag="#Debug_Msj:";  
// Properties
  // Inputs
    
  VSBoolean Enable_VM_in = new VSBoolean(false);

 
  VSBoolean Debug_Window_En_in = new VSBoolean(false);
  VSBoolean Error_in = new VSBoolean(false);
  
  // Outputs
  VSBoolean Enable_VM_out = new VSBoolean(false);
 
  VSString System_out = new VSString();
  
  VSBoolean Debug_Window_En_out = new VSBoolean(false);
  VSBoolean Error_out = new VSBoolean(false);
  
  VSDouble Temperature_RPI_Out= new VSDouble(0.0);
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
    element.jSetName("Read_RPI_TEMP_JV");
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

    setPin(2, ExternalIF.C_DOUBLE, ExternalIF.PIN_OUTPUT);
    setPin(3, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);
    setPin(4, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);
    
    setPin(5, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);

    setPin(8, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    setPin(9, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    
    element.jSetPinDescription(0, "Enable_VM_out");
    element.jSetPinDescription(1, "N/C");
    element.jSetPinDescription(2, "RPI_Temperature_Celcius_out");
    element.jSetPinDescription(3, "Debug_Window_Enable_out");
    element.jSetPinDescription(4, "Error_out");
        
    element.jSetPinDescription(5, "Enable_VM_in");
    element.jSetPinDescription(6, "N/C");
    element.jSetPinDescription(7, "N/C");
    element.jSetPinDescription(8, "Debug_Window_Enable_in");
    element.jSetPinDescription(9, "Error_in");
    
 
    element.jSetResizable(false);
    element.jSetResizeSynchron(false);

    first_time=true;
  }

  public void initInputPins()
  {
    Enable_VM_in= (VSBoolean)element.getPinInputReference(5);

    Debug_Window_En_in= (VSBoolean)element.getPinInputReference(8);
    if (Debug_Window_En_in==null){
       Debug_Window_En_in=new VSBoolean(false);
    }
    Error_in= (VSBoolean)element.getPinInputReference(9);
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0, Enable_VM_out); 

    element.setPinOutputReference(2, Temperature_RPI_Out);
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
     
        
    if ((Enable_VM_in.getValue()==true && first_time==true && Error_in.getValue()==false ) || (Enable_VM_in.getValue()==true && Error_in.getValue()==false) )  // Execute the first time and if there are error to continue trying.
    { 
      
      
      try {
          //element.jConsolePrintln("Comando ingresado: " +in.getValue());
          //element.jConsolePrintln();
                    //String cmd = "manual.bat"; //Comando 
                    //Runtime.getRuntime().exec("msg * \"El comano es: \"" +in.getValue());
                    sysOut_N_Err="";
                    sysOut_Err="";
                    

                    Process p =    Runtime.getRuntime().exec("vcgencmd measure_temp"); //gpio write <pin> 0/1
                    if (Debug_Window_En_in.getValue()) {      
                        element.jConsolePrintln(Element_Tag+Debug_Tag+"|Executing:"+"vcgencmd measure_temp|");
                        }
                    BufferedReader stdInput = new BufferedReader(new InputStreamReader(
                                        p.getInputStream()));

                        BufferedReader stdError = new BufferedReader(new InputStreamReader(
                                        p.getErrorStream()));

                        
                        while ((s = stdInput.readLine()) != null) {
                            sysOut_N_Err+=s;
                            
                            // Leemos la salida del comando
                            if (Debug_Window_En_in.getValue() && first_time==true) {

                                element.jConsolePrintln("Standard output from system:\n");
                            }
                            if (Debug_Window_En_in.getValue()){
                                element.jConsolePrintln(s);
                            }
                        System_out.setValue(sysOut_N_Err);  
                        }
                                                
                        while ((s = stdError.readLine()) != null) {
                            sysOut_Err+=s;
                            
                            // Leemos los errores si los hubiera
                            if (Debug_Window_En_in.getValue() && first_time==true){

                                element.jConsolePrintln("Error Output from system:\n");
                            }
                            if (Debug_Window_En_in.getValue()){
                                element.jConsolePrintln(s);
                            }
                             
                        System_out.setValue(sysOut_Err);        
                        }
                    if(sysOut_N_Err.contains("temp=")){
                    Error_out.setValue(false);
                   
                    int indexIni=sysOut_N_Err.indexOf("=");  //RTA FROM TERMINAL "temp=30.7'C"
                    indexIni++;
                    int indexEnd=sysOut_N_Err.indexOf("C");
                    indexEnd--;
                    
                    String TempStr="";
                    double Temperature_Out=0.0;
                    TempStr=sysOut_N_Err.substring(indexIni, indexEnd);
                    Temperature_Out=Double.parseDouble(TempStr);
                    Temperature_RPI_Out.setValue(Temperature_Out);
                    }else{
                    Temperature_RPI_Out.setValue(0.0);
                    Error_out.setValue(true); 
                    }    

                    
                    first_time=false;
                    s=" ";
                    sysOut_Err=" ";
                    sysOut_N_Err=" ";
                                        
            } catch (IOException ioe) {
                    System.out.println (ioe);
                    if (Debug_Window_En_in.getValue()) {
                        element.jConsolePrintln(Element_Tag+Error_Tag+"|Command Error:|vcgencmd measure_temp|");
                    }
                    Error_out.setValue(true);
                    
                    System_out.setValue("Command Error!!");
            }  
       
       
       
    }
   
   element.notifyPin(2); // Notify the Temperature Value
   
   element.notifyPin(4);
   Enable_VM_out.setValue(Enable_VM_in.getValue());
   //Enable_VM_in.setValue(false);
   element.notifyPin(0);

   Debug_Window_En_out.setValue(Debug_Window_En_in.getValue());
   element.notifyPin(3);
  }


}
