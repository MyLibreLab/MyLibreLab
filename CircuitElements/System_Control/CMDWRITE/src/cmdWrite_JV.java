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
    
  VSBoolean input0 = new VSBoolean(false);
  VSString input1 = new VSString();
  VSBoolean input2 = new VSBoolean(false);
  // Outputs

  VSString output0 = new VSString();
  VSBoolean output1 = new VSBoolean(false);

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
    input0= (VSBoolean)element.getPinInputReference(2);
    input1= (VSString)element.getPinInputReference(3);
    input2= (VSBoolean)element.getPinInputReference(4);
    
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0, output0);
    element.setPinOutputReference(1, output1);
  }

  public void start()
  {
  }

  public void stop()
  {
  }

  public void process()
  {
    if (input0.getValue())
    { 
      
      try {
          //element.jConsolePrintln("Comando ingresado: " +in.getValue());
          //element.jConsolePrintln();
                    //String cmd = "manual.bat"; //Comando 
                    //Runtime.getRuntime().exec("msg * \"El comano es: \"" +in.getValue());
                    Process p = Runtime.getRuntime().exec(input1.getValue()); 
                    
                    BufferedReader stdInput = new BufferedReader(new InputStreamReader(
                                        p.getInputStream()));

                        BufferedReader stdError = new BufferedReader(new InputStreamReader(
                                        p.getErrorStream()));

                        
                        while ((s = stdInput.readLine()) != null) {
                            sysOut_N_Err+=s;
                            sysOut_N_Err+="\n";
                            // Leemos la salida del comando
                            if (input2.getValue() && firstTime==true) {
                                firstTime=false;
                                element.jConsolePrintln("Standard output from system:\n");
                            }
                            if (input2.getValue()){
                                element.jConsolePrintln(s);
                            }
                        output0.setValue(sysOut_N_Err);  
                        }

                        
                        while ((s = stdError.readLine()) != null) {
                            sysOut_Err+=s;
                            sysOut_Err+="\n";
                            // Leemos los errores si los hubiera
                            if (input2.getValue() && firstTime==true){
                                firstTime=false;
                                element.jConsolePrintln("Error Output from system:\n");
                            }
                            if (input2.getValue()){
                                element.jConsolePrintln(s);
                            }
                             
                        output0.setValue(sysOut_Err);        
                        }

                    output1.setValue(false);
                    
            } catch (IOException ioe) {
                    System.out.println (ioe);
                    if (input2.getValue()) {
                        element.jConsolePrintln("Error ejecutando comando "+input1.getValue());
                    }
                    output1.setValue(true);
                    output0.setValue("Command Error!!");
            }  

       element.notifyPin(0);
       element.notifyPin(1);
       input2.setValue(false);
    }
  }


}
