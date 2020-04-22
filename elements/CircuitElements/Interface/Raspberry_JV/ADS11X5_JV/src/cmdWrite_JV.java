//********************************
//* Autor : Robinson Javier Velasquez
//* Date  : Jul-31-2018
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
  
  VSComboBox Module = new VSComboBox();
  VSString address = new VSString("0x48");
  VSString busPort = new VSString("1");
  VSComboBox gain = new VSComboBox();
  
  VSInteger Analog0= new VSInteger(0);
  VSInteger Analog1= new VSInteger(0);
  VSInteger Analog2= new VSInteger(0);
  VSInteger Analog3= new VSInteger(0);
  boolean lastEnable=false;

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
    element.jAddPEItem("Module", Module, 0, 10);
    element.jAddPEItem("I2C Address", address, 0, 10);
    element.jAddPEItem("I2C Bus Port", busPort, 0, 5);
    element.jAddPEItem("Gain", gain, 0, 100);
        
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
    element.jSetName("ADS11X5_JV");
    initPinVisibility(false,false,false,false);
    setSize(50,70);
    element.jSetLeftPins(2);
    element.jSetRightPins(6);
    element.jSetLeftPinsVisible(true);
    element.jSetRightPinsVisible(true);
    initPins(0,6,0,2);
    element.jSetInnerBorderVisibility(false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");

    
    
    setPin(0, ExternalIF.C_INTEGER, ExternalIF.PIN_OUTPUT);
    setPin(1, ExternalIF.C_INTEGER, ExternalIF.PIN_OUTPUT);
    setPin(2, ExternalIF.C_INTEGER, ExternalIF.PIN_OUTPUT);
    setPin(3, ExternalIF.C_INTEGER, ExternalIF.PIN_OUTPUT);
    setPin(4, ExternalIF.C_STRING, ExternalIF.PIN_OUTPUT);
    setPin(5, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);
    
    setPin(6, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    setPin(7, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    
    
    element.jSetPinDescription(0, "A0 Out");
    element.jSetPinDescription(1, "A1 Out");
    element.jSetPinDescription(2, "A2 Out");
    element.jSetPinDescription(3, "A3 Out");
    element.jSetPinDescription(4, "System_Out");
    element.jSetPinDescription(5, "Error_Out");
    
    element.jSetPinDescription(6, "Enable_Element");
    element.jSetPinDescription(7, "Enable_Debug_Window");
    
    Module.addItem("ADS1115 (16bit)");
    Module.addItem("ADS1105 (12bit)");
    
    gain.addItem("1");
    gain.addItem("2");
    gain.addItem("4");
    gain.addItem("8");
    gain.addItem("16");
    gain.addItem("2/3");
 
    element.jSetResizable(false);
    element.jSetResizeSynchron(false);
    firstTime=true;
  }

  public void initInputPins()
  {
    enable= (VSBoolean)element.getPinInputReference(6);
    Debug_En= (VSBoolean)element.getPinInputReference(7);
    if(Debug_En==null){
       Debug_En=new VSBoolean(false);
    }
    
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0, Analog0);
    element.setPinOutputReference(1, Analog1);
    element.setPinOutputReference(2, Analog2);
    element.setPinOutputReference(3, Analog3);
    element.setPinOutputReference(4, Response_Out);
    element.setPinOutputReference(5, Error_out);
  }

  public void start()
  {
      lastEnable=false;
  }

  public void stop()
  {
  }

  public void process()
  {
    if(Debug_En==null){
       Debug_En=new VSBoolean(false);
    }
    
    if (enable.getValue() && enable!=null && lastEnable==false)
    { 
      
      try {
          //element.jConsolePrintln("Comando ingresado: " +in.getValue());
          //element.jConsolePrintln();
                    //String cmd = "manual.bat"; //Comando 
                    //Runtime.getRuntime().exec("msg * \"El comano es: \"" +in.getValue());
                    String path=element.jGetSourcePath();
                    path += "adc.py";
                    //element.jConsolePrintln(path);
                    //String commandADC="sudo python "+path+" 1115 0x48 1 1";
                    String ModuleTemp ="";
                    if(Module.selectedIndex==0){
                    ModuleTemp ="1115";    
                    }else{
                    ModuleTemp ="1105";    
                    }
                    
                    String commandADC="sudo python "+path+" "+ModuleTemp+
                            " "+address.getValue()+" "+ busPort + " "+gain.getItem(gain.selectedIndex);
                    if (Debug_En.getValue()){
                    element.jConsolePrintln(commandADC);    
                    }
                    
                    
                    Process p = Runtime.getRuntime().exec(commandADC); 
                    
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
                    
                    try{    
                    //|   4686 |   4615 |   5237 |   6354 |
                    String tmp = sysOut_N_Err;
        
                    if(tmp.contains("|") && tmp.length()>=36){
                    tmp=tmp.trim();    
                    tmp=tmp.replaceAll(" ", "");
                    int firstSeparator=tmp.indexOf("|");
                    int secondSeparator=tmp.indexOf("|",(firstSeparator+1));
                    int thirdSeparator=tmp.indexOf("|",(secondSeparator+1));
                    int fourthSeparator=tmp.indexOf("|",(thirdSeparator+1));
                    int lastSeparator=tmp.lastIndexOf("|");
                    //System.err.println("Split L: " + firstSeparator + " " + secondSeparator + " " + thirdSeparator
                     //+ " " + fourthSeparator + " "+lastSeparator);

                    int val0=0,val1=0,val2=0,val3=0;
                    String valStr0="",valStr1="",valStr2="",valStr3="";
                    valStr0 = tmp.substring(firstSeparator+1,secondSeparator);
                    valStr1 = tmp.substring(secondSeparator+1, thirdSeparator);
                    valStr2 = tmp.substring(thirdSeparator+1,fourthSeparator);
                    valStr3 = tmp.substring(fourthSeparator+1,lastSeparator);
                    //System.err.println("Split L: "+valStr0 + valStr1 + valStr2 + valStr3);

                    val0 = Integer.valueOf(valStr0);
                    val1 = Integer.valueOf(valStr1);
                    val2 = Integer.valueOf(valStr2);
                    val3 = Integer.valueOf(valStr3);
                    //System.err.println("Split INT: "+val0 +"_"+ val1 +"_"+ val2 +"_"+ val3);
                    Analog0.setValue(val0);
                    Analog1.setValue(val1);
                    Analog2.setValue(val2);
                    Analog3.setValue(val3);
                    element.notifyPin(0);
                    element.notifyPin(1);
                    element.notifyPin(2);
                    element.notifyPin(3);
                    }else{
                       Error_out.setValue(true);
                       if (Debug_En.getValue()) {
                          element.jConsolePrintln("I2C Device communication Parameters or Library not Installed error: "+ sysOut_N_Err +" Command:" +  CommandIn.getValue());
                       }
                       Response_Out.setValue("I2C Device communication Parameters or Library not Installed error!! "+sysOut_N_Err);  
                      }
                    }catch(Exception e){
                     Error_out.setValue(true);
                     System.out.println (e);
                     if (Debug_En.getValue()) {
                        element.jConsolePrintln("Error Response Format: "+ sysOut_N_Err +" Command:" +  CommandIn.getValue());
                     }
                     Response_Out.setValue("Response Format Error!! "+sysOut_N_Err);
                    }

                    
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
       
       element.notifyPin(4);
       element.notifyPin(5);
       //Debug_En.setValue(false);
    } lastEnable=enable.getValue();
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
     Module.loadFromStream(fis);
     address.loadFromStream(fis);
     busPort.loadFromStream(fis);
     gain.loadFromStream(fis);
     
     //init();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      Module.saveToStream(fos);
      address.saveToStream(fos);
      busPort.saveToStream(fos);
      gain.saveToStream(fos);
     
  }

  
}
