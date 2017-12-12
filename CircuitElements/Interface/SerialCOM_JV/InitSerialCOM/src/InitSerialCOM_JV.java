//********************************
//* Autor : Robinson Javier Velasquez
//* Date  : Nov-13-2017
//* Email : javiervelasquez@gmail.com
//* licence : non commercial use.
//********************************


import VisualLogic.*;
import java.awt.*;
import tools.*;
import VisualLogic.variables.*;
import jssc.SerialPortException;


public class InitSerialCOM_JV extends JVSMain
{

  private Image image;
  String s = "";
  String sysOut_N_Err= ""; // String System Response without error
  String sysOut_Err= "";   // String System Response when error
  boolean started=false;
  boolean PortOpened=false;
  boolean firstTime=true;
  boolean BaudRateExternalEnable=false;
  
  final String Element_Tag= "#Element:|InitSerialCOM_JV|";
  final String Error_Tag="#Error:";
  final String Debug_Tag="#Debug_Msj:";
  // Properties
  // Inputs
    
  VSBoolean Enable_VM_in = new VSBoolean(false);
  VSString SerialPortNameIn = new VSString("");
  VSString SerialPortNameOut = new VSString("");  //VSString  SerialPortNameOut = new VSString("COM3");
  VSComboBox BaudRateComboBox = new VSComboBox();
  VSComboBox dataBitsComboBox = new VSComboBox();
  VSComboBox stopBitsComboBox = new VSComboBox();
  VSComboBox ParityComboBox = new VSComboBox();
  
  VSBoolean ArduinoAutoRESET = new VSBoolean(true);
  
  VSBoolean Debug_Window_En_in = new VSBoolean(false);
  VSBoolean Error_in = new VSBoolean(false);
  VSInteger AutoResetTime =new VSInteger(1500);
  VSInteger BaudRate_External =null;
  VSInteger MessageTimeOUT =new VSInteger(50);
  // Outputs
  VSBoolean Enable_VM_out = new VSBoolean(false);
  VSString System_out = new VSString();
  VSBoolean Debug_Window_En_out = new VSBoolean(false);
  VSBoolean Error_out = new VSBoolean(false);
  VSBoolean CR_Enable = new VSBoolean(true);
  VSBoolean LF_Enable = new VSBoolean(true);
  VSBoolean RTS_Enable = new VSBoolean(false);
  VSBoolean DTR_Enable = new VSBoolean(false);
  
  volatile Boolean EnableOld=false;
  volatile Boolean EnableProccess=false;

  
  NewSerialDriverManager serialDriverJV;
  VSserialPort vsSerial;
          
  
  public void onDispose()
  { 
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }

  public void paint(java.awt.Graphics g)
  { //element.jConsolePrintln("Paint");
    if (image!=null) {
    drawImageCentred(g,image);
    }

  }

 


  public void init()
  { //element.jConsolePrintln("Init!");
    element.jSetName("Open_COM_Port_JV");
    initPinVisibility(false,false,false,false);
    setSize(50,60);
    element.jSetLeftPins(5);
    element.jSetRightPins(5);
    element.jSetTopPins(1);
    element.jSetBottomPins(1);
    element.jSetLeftPinsVisible(true);
    element.jSetTopPinsVisible(true);
    element.jSetRightPinsVisible(true);
    element.jSetBottomPinsVisible(true);
    initPins(1,5,1,5); //initPins(0,2,0,4);
    element.jSetInnerBorderVisibility(false);
    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    BaudRateComboBox.addItem("256000");
    BaudRateComboBox.addItem("128000");
    BaudRateComboBox.addItem("115200");
    BaudRateComboBox.addItem("57600");
    BaudRateComboBox.addItem("38400");
    BaudRateComboBox.addItem("19200");
    BaudRateComboBox.addItem("14400");
    BaudRateComboBox.addItem("9600");
    BaudRateComboBox.addItem("4800");
    BaudRateComboBox.addItem("1200");
    BaudRateComboBox.addItem("600");
    BaudRateComboBox.addItem("300");
    BaudRateComboBox.addItem("110");
    BaudRateComboBox.setPinIndex(7);
    BaudRateComboBox.setPin(7);
    
    
    dataBitsComboBox.addItem("8");
    dataBitsComboBox.addItem("7");
    dataBitsComboBox.addItem("6");
    dataBitsComboBox.addItem("5");
    dataBitsComboBox.setPinIndex(0);
    
    ParityComboBox.addItem("NONE"); // None = 0
    ParityComboBox.addItem("ODD");
    ParityComboBox.addItem("EVEN");
    ParityComboBox.addItem("MARK");
    ParityComboBox.addItem("SPACE");
    ParityComboBox.setPinIndex(0);
    
    stopBitsComboBox.addItem("1.0");
    stopBitsComboBox.addItem("1.5");
    stopBitsComboBox.addItem("2.0");
    stopBitsComboBox.setPinIndex(0);
    

    // Top 1 Pins
    setPin(0, ExternalIF.C_INTEGER, ExternalIF.PIN_INPUT);
    
    // Right 5 Pins
    setPin(1, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);
    setPin(2, ExternalIF.C_STRING, ExternalIF.PIN_OUTPUT);
    setPin(3, ExternalIF.C_VARIANT, ExternalIF.PIN_OUTPUT);
    setPin(4, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);
    setPin(5, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);
    
    //Bottom 1 Pin
    setPin(6, ExternalIF.C_VARIANT, ExternalIF.PIN_OUTPUT);
    
    //Left 5 Pins
    setPin(7, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    setPin(8, ExternalIF.C_STRING, ExternalIF.PIN_INPUT);
    setPin(9, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    setPin(10, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    setPin(11, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    
    element.jSetPinDescription(0, "BaudRate_External_in");
    
    element.jSetPinDescription(1, "Enable_VM_out");
    element.jSetPinDescription(2, "PortName_out");
    element.jSetPinDescription(3, "N/C");
    element.jSetPinDescription(4, "Debug_Window_Enable_out");
    element.jSetPinDescription(5, "Error_out");
    
    element.jSetPinDescription(6, "N/C");
    
    
    element.jSetPinDescription(7, "Enable_VM_in");
    element.jSetPinDescription(8, "PortName_in");
    element.jSetPinDescription(9, "ArduinoAutoResetEnabled_in");
    element.jSetPinDescription(10, "Debug_Window_Enable_in");
    element.jSetPinDescription(11, "Error_in");

    element.jSetResizable(false);
    element.jSetResizeSynchron(false);

    serialDriverJV = new NewSerialDriverManager();
    vsSerial=new VSserialPort();
    started=false;
    firstTime=true;
    
    //initInputPins();
    
  }

  public void initInputPins()
  { //element.jConsolePrintln("Init Input Pins!");
    BaudRate_External=(VSInteger) element.getPinInputReference(0);
    if(BaudRate_External==null){
    BaudRate_External=new VSInteger();
    BaudRateExternalEnable=false;
    }else{
    BaudRateExternalEnable=true;    
    }
    
    Enable_VM_in= (VSBoolean)element.getPinInputReference(7);
    if(Enable_VM_in==null){
      Enable_VM_in=new VSBoolean(false);
      Enable_VM_out=new VSBoolean(false);
    }
    SerialPortNameIn = (VSString)element.getPinInputReference(8);
    if(SerialPortNameIn==null){
        SerialPortNameIn=new VSString("");
    }
    ArduinoAutoRESET = (VSBoolean)element.getPinInputReference(9);
    if(ArduinoAutoRESET==null){
    ArduinoAutoRESET = new VSBoolean(true);
    }
    Debug_Window_En_in= (VSBoolean)element.getPinInputReference(10);
    if (Debug_Window_En_in==null){
       Debug_Window_En_in=new VSBoolean(false);
    }
    Error_in= (VSBoolean)element.getPinInputReference(11);
    if(Error_in==null){
       Error_in=new VSBoolean(false);
    }
  }

  public void initOutputPins()
  { //element.jConsolePrintln("Init Output Pins!");
    element.setPinOutputReference(1, Enable_VM_out); 
    element.setPinOutputReference(2, SerialPortNameOut);
    //element.setPinOutputReference(3, System_out);
    element.setPinOutputReference(4, Debug_Window_En_out);
    element.setPinOutputReference(5, Error_out);
  }

  public void start()
  { //element.jConsolePrintln("start!");
    Error_in.setValue(false);
    
    Error_out.setValue(false);
    PortOpened=false;
    
    Enable_VM_out.setValue(false);
    SerialPortNameOut.setValue(SerialPortNameIn.getValue());
    System_out.setValue("");
    Debug_Window_En_out.setValue(Debug_Window_En_in.getValue());
    Error_out.setValue(Error_in.getValue());
    element.notifyPin(1); // Enable VM Out
    element.notifyPin(2); //Serial PortNameOut
    //element.notifyPin(2); // SystemOut
    element.notifyPin(4); // Debug Enable Out
    element.notifyPin(5); // Error Out
    started=true;
    firstTime=true;

  }

 
  
  public void stop()
  { //element.jConsolePrintln("Stop");
    String ErrorData="";
    Error_out.setValue(false);
    
    if(SerialPortNameIn!=null)
    {
     if(vsSerial!=null){
      try {
          if(vsSerial.isOpened()){
          //JSSC.ClosePort();
          NewSerialDriverManager.RemoveSerialPortByPortName(SerialPortNameIn.getValue());
          PortOpened=false; 
          vsSerial=null;
          //System.err.println("PuertoCerradoOK");
          }
          Error_out.setValue(false);  
       
      } 
      catch (Exception e){
        
      }
      
     }
    }
     
    
  }

  public void process()
  { //element.jConsolePrintln("Proccess");
    
   if(EnableOld==false && Enable_VM_in.getValue() && started){
      EnableProccess=true; 
      //element.jConsolePrintln("Enable Proccess!");
    }   

   if(EnableProccess==true && Error_in.getValue()==false && PortOpened==false) // Only executes one time if no error.
   { EnableProccess=false; 
   
    if(firstTime){
    firstTime=false;
    Enable_VM_out.setValue(false);
    element.notifyPin(1); // Enable VM Out
    SerialPortNameOut.setValue(SerialPortNameIn.getValue());
    element.notifyPin(2); //Serial PortNameOut
    Debug_Window_En_out.setValue(Debug_Window_En_in.getValue());
    element.notifyPin(4); // Debug Enable Out
    }
    if(SerialPortNameIn!=null)
    {
      String BufferError="";
      try {
        int BaudRateTemp=0;
        //element.jConsolePrintln("Doing Proccess!");
        if(BaudRateExternalEnable){
        BaudRateTemp=BaudRate_External.getValue();
        }else{
        BaudRateTemp= Integer.valueOf(BaudRateComboBox.getItem(BaudRateComboBox.selectedIndex));
        }
        int dataBitsTemp = Integer.valueOf(dataBitsComboBox.getItem(dataBitsComboBox.selectedIndex));
        int stopBitsIndex = stopBitsComboBox.selectedIndex; //
        int ParityTemp = ParityComboBox.selectedIndex;
        int stopBitsInt = 0;
        if(stopBitsIndex==0) stopBitsInt=1;   //INDEX 0 = STOPBITS_1    = 1
        if(stopBitsIndex==1) stopBitsInt=3;   //INDEX 1 = STOPBITS_1.5  = 3
        if(stopBitsIndex==2) stopBitsInt=2;   //INDEX 2 = STOPBITS_2    = 2 
        
        if(PortOpened==false){
        //JSSC.OpenPort(SerialPortNameIn.getValue());
        vsSerial=serialDriverJV.NewSerialPort(SerialPortNameIn.getValue());
        //System.err.println("SerialPortNameIn.getValue()"+SerialPortNameIn.getValue());
        if(vsSerial.getValue().isOpened()){
        PortOpened=true;
        //vsSerial.getSerialPort().closePort();
        }else{
        PortOpened=false;
        vsSerial.getSerialPort().openPort();
        PortOpened=vsSerial.getValue().isOpened();
        }
        vsSerial.setEnableCR(CR_Enable.getValue());
        vsSerial.setEnableLF(LF_Enable.getValue());
        vsSerial.setMessageTimeOut(MessageTimeOUT.getValue());
        
        if (Debug_Window_En_in.getValue()) {
            element.jConsolePrintln(Element_Tag+"|Port:_" +vsSerial.getSerialPort().getPortName()+"_Opened OK_Total Ports:_"+NewSerialDriverManager.getPortsOpened()+"|");
        }
        vsSerial.setParameters(ArduinoAutoRESET.getValue(), Thread.currentThread(),AutoResetTime.getValue(),BaudRateTemp,dataBitsTemp,stopBitsInt,ParityTemp);
        NewSerialDriverManager.UpdateSerialPortByPortName(vsSerial,SerialPortNameIn.getValue());
        }
        Error_out.setValue(false);
        
        if (Debug_Window_En_in.getValue()) {
            element.jConsolePrintln(Element_Tag+"|Port_Configured_OK_"+"Baud:"+BaudRateTemp+"_dataBits:"+dataBitsTemp+"_StopBits:"+stopBitsInt+"_Paridad:"+ParityComboBox.selectedIndex+"|");
        }
        
      } catch (SerialPortException ex) {
          //Logger.getLogger(InitSerialCOM_JV.class.getName()).log(Level.SEVERE, null, ex);
          BufferError+=ex.getMessage()+"\n";
          Error_out.setValue(true);
          element.notifyPin(5); // Error Out
          PortOpened=false;
      } catch (InterruptedException ex) {
          //Logger.getLogger(InitSerialCOM_JV.class.getName()).log(Level.SEVERE, null, ex);
          BufferError+=ex.getMessage()+"\n";
          Error_out.setValue(true);
          element.notifyPin(5); // Error Out
          PortOpened=false;
      } catch (Exception ex){
          //Logger.getLogger(InitSerialCOM_JV.class.getName()).log(Level.SEVERE, null, e);
          BufferError+=ex.getMessage()+"\n";
          Error_out.setValue(true);
          element.notifyPin(5); // Error Out
          PortOpened=false;
      }
      finally{
        if (Debug_Window_En_in.getValue() && Error_out.getValue()) {
            element.jConsolePrintln(Element_Tag+Error_Tag+"|ErrorOpeningPort:"+SerialPortNameIn.getValue()+BufferError);
            System_out.setValue(Element_Tag+Error_Tag+"|ErrorOpeningPort:"+SerialPortNameIn.getValue()+BufferError);
        }
      }
      
    }else{ // PORT Null
          Error_out.setValue(true);
          element.notifyPin(5); // Error Out
          if (Debug_Window_En_in.getValue()){
                    element.jConsolePrintln(Element_Tag+Error_Tag+"|Element_Not_Executed_Because_PortName=NULL|");
                    System_out.setValue(Element_Tag+Error_Tag+"|Element_Not_Executed_Because_PortName=NULL|");
          }    
    }
    
   if(vsSerial!=null){
    try{    
    PortOpened=vsSerial.getSerialPort().isOpened();
    } catch (Exception e){
    PortOpened=false;
    Error_out.setValue(true); 
    element.notifyPin(5); // Error Out
    }
   }else{
   PortOpened=false;
   Error_out.setValue(true); 
   element.notifyPin(5); // Error Out  
   if (Debug_Window_En_in.getValue()){
                    element.jConsolePrintln(Element_Tag+Error_Tag+"|Element_Not_Executed_Because_Port=NULL|");
                    System_out.setValue(Element_Tag+Error_Tag+"|Element_Not_Executed_Because_Port=NULL|");
          }
   }  
 
  }
  if(Error_out.getValue() || Error_in.getValue() || PortOpened==false){         // If there is error do not enable other VMs
   Enable_VM_out.setValue(false);
   element.notifyPin(1); // Enable VM Out
   }else{
        if(PortOpened){
        Enable_VM_out.setValue(Enable_VM_in.getValue());
        element.notifyPin(1); // Enable VM Out
        }
   }
  if(Error_in.getValue()){
       Error_out.setValue(true);
       element.notifyPin(5); // Error Out
   }
   
  EnableOld=Enable_VM_in.getValue(); 
}

  
   public void setPropertyEditor()
  { //element.jConsolePrintln("Set Property Editor!");
    element.jAddPEItem("BaudRate", BaudRateComboBox, 0,100);
    element.jAddPEItem("DataBits", dataBitsComboBox, 0,100);
    element.jAddPEItem("StopBits", stopBitsComboBox, 0,100);
    element.jAddPEItem("Partity", ParityComboBox, 0,100);
    element.jAddPEItem("Enable_LF", LF_Enable, 0, 0);
    element.jAddPEItem("Enable_CR", CR_Enable, 0, 0);
    element.jAddPEItem("Message_Timeout[mS]", MessageTimeOUT, 1, 1000);
    element.jAddPEItem("AutoReset_Delay[mS]", AutoResetTime, 1, 20000);
    
    localize();
  }

  private void localize()
  { //element.jConsolePrintln("Localize");
    String language;
    int d=6;
    language="en_US";
    element.jSetPEItemLocale(d+0,language,"BaudRate");
    element.jSetPEItemLocale(d+1,language,"DataBits");
    element.jSetPEItemLocale(d+2,language,"StopBits");
    element.jSetPEItemLocale(d+3,language,"Parity");
    element.jSetPEItemLocale(d+4,language,"Enable_LF");
    element.jSetPEItemLocale(d+5,language,"Enable_CR");
    element.jSetPEItemLocale(d+6,language,"Message_Timeout[mS]");
    element.jSetPEItemLocale(d+7,language,"AutoReset_Delay[mS]");
    

    language="es_ES";
    element.jSetPEItemLocale(d+0,language,"Tasa_De_Baudios");
    element.jSetPEItemLocale(d+1,language,"Bits_De_Datos");
    element.jSetPEItemLocale(d+2,language,"Bits_De_Parada");
    element.jSetPEItemLocale(d+3,language,"Paridad");
    element.jSetPEItemLocale(d+4,language,"Habilitar_LF");
    element.jSetPEItemLocale(d+5,language,"Habilitar_CR");
    element.jSetPEItemLocale(d+6,language,"Timeout_Mensajes[mS]");
    element.jSetPEItemLocale(d+7,language,"Retardo_de_AutoReset[mS]");
    
  }
  public void propertyChanged(Object o)
  {   
    element.jRepaint();
  }
  
  public void saveToStream(java.io.FileOutputStream fos){
    //element.jConsolePrintln("SaveToStream");   
    BaudRateComboBox.saveToStream(fos);
    dataBitsComboBox.saveToStream(fos);
    stopBitsComboBox.saveToStream(fos);
    ParityComboBox.saveToStream(fos);
    AutoResetTime.saveToStream(fos);
    MessageTimeOUT.saveToStream(fos);
    CR_Enable.saveToStream(fos);
    LF_Enable.saveToStream(fos);
    
    
  }
  
  public void loadFromStream(java.io.FileInputStream fis){
    //element.jConsolePrintln("LoadFromStream");
    BaudRateComboBox.loadFromStream(fis);
    dataBitsComboBox.loadFromStream(fis);
    stopBitsComboBox.loadFromStream(fis);
    ParityComboBox.loadFromStream(fis);
    AutoResetTime.loadFromStream(fis);
    MessageTimeOUT.loadFromStream(fis);
    CR_Enable.loadFromStream(fis);
    LF_Enable.loadFromStream(fis);
    
    element.jRepaint();
  }
  
}


