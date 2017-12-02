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
  private SocketServerUDP_JV ServerUDP= new SocketServerUDP_JV();
  
  private Thread ServerThread;
  
  public ArduinoJSSC_JV JSSC;
  private Image image;
  private VSInteger SokectPort=new VSInteger(9876);
  String s = "";
  String sysOut_N_Err= ""; // String System Response without error
  String sysOut_Err= "";   // String System Response when error
  boolean firstTime=false;
  boolean ServerStarted=false;
  boolean PortOpened=false;
  final String Element_Tag= "#Element:|InitSerialCOM_JV|";
  final String Error_Tag="#Error:";
  final String Debug_Tag="#Debug_Msj:";
  // Properties
  // Inputs
    
  VSBoolean Enable_VM_in = new VSBoolean(false);
  VSString SerialPortNameIn = new VSString("COM3");
  VSString SerialPortOut = new VSString();
  //VSString  SerialPortOut = new VSString("COM3");
  VSComboBox BaudRateComboBox = new VSComboBox();
  VSComboBox dataBitsComboBox = new VSComboBox();
  VSComboBox stopBitsComboBox = new VSComboBox();
  VSComboBox ParityComboBox = new VSComboBox();
  
  VSBoolean ArduinoAutoRESET = new VSBoolean(true);
  
  VSBoolean Debug_Window_En_in = new VSBoolean(false);
  VSBoolean Error_in = new VSBoolean(false);
  VSInteger AutoResetTime =new VSInteger(1000);
  VSInteger MessageTimeOUT =new VSInteger(50);
  // Outputs
  VSBoolean Enable_VM_out = new VSBoolean(false);
  VSString System_out = new VSString();
  VSBoolean Debug_Window_En_out = new VSBoolean(false);
  VSBoolean Error_out = new VSBoolean(false);
  VSBoolean CR_Enable = new VSBoolean(true);
  VSBoolean LF_Enable = new VSBoolean(true);
  
  volatile Boolean EnableOld=false;
  volatile Boolean EnableProccess=false;
  Boolean ThreadCreated=false;
  
 public void xOnInit()
  {
      //element.jConsolePrintln("XonInit");
    

  }
  
  public void onDispose()
  { element.jConsolePrintln("OnDispose");
    if (image!=null)
    {
      image.flush();
      image=null;
    }
    firstTime=true;
    PortOpened=false;
    
      try {
        if(ServerStarted && ThreadCreated){
            ServerUDP.CloseSocket();
            ServerThread.destroy();
            ServerStarted=false;
            ThreadCreated=false;
        }
        this.finalize(); 
        Runtime.getRuntime().runFinalization();
      } catch (Exception ex) {
          //Logger.getLogger(InitSerialCOM_JV.class.getName()).log(Level.SEVERE, null, ex);
      } catch (Throwable ex) {
          //Logger.getLogger(InitSerialCOM_JV.class.getName()).log(Level.SEVERE, null, ex);
      }finally{
      JSSC.DisposeSerialPort();
      System.gc();    
      }

  }

  public void paint(java.awt.Graphics g)
  { //element.jConsolePrintln("Paint");
    if (image!=null) drawImageCentred(g,image);
      
    try {
          Thread.sleep(125);
      } catch (InterruptedException ex) {
          //Logger.getLogger(InitSerialCOM_JV.class.getName()).log(Level.SEVERE, null, ex);
      }
    
  }

 


  public void init()
  { ServerStarted=false;
    ThreadCreated=false;
    //element.jConsolePrintln("Init");
    element.jSetName("SerialCOM_Port_JV");
    initPinVisibility(false,false,false,false);
    setSize(50,50);
    element.jSetLeftPins(5);
    element.jSetRightPins(5);
    element.jSetLeftPinsVisible(true);
    //element.jSetTopPinsVisible(true);
    element.jSetRightPinsVisible(true);
    //element.jSetBottomPinsVisible(true);
    initPins(0,5,0,5); //initPins(0,2,0,4);
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
    /*
    try{
    String[] Lista = SerialPortList.getPortNames();    
    for(String Port: Lista){
        PortComboBox.addItem(Port);
        element.jConsolePrintln("Puertos: "+Port);
    }
    }finally{
        element.jConsolePrintln("ERROR");
    } */
    //TOP 3 Pins
    //setPin(0, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    //setPin(1, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    //setPin(2, ExternalIF.C_BYTE, ExternalIF.PIN_INPUT);
    // Right 5 Pins
    setPin(0, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);
    setPin(1, ExternalIF.C_STRING, ExternalIF.PIN_OUTPUT);
    setPin(2, ExternalIF.C_STRING, ExternalIF.PIN_OUTPUT);
    setPin(3, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);
    setPin(4, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);
    
    setPin(5, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    setPin(6, ExternalIF.C_STRING, ExternalIF.PIN_INPUT);
    setPin(7, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    setPin(8, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    setPin(9, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    
    element.jSetPinDescription(0, "Enable_VM_out");
    element.jSetPinDescription(1, "PortName_out");
    element.jSetPinDescription(2, "System_out");
    element.jSetPinDescription(3, "Debug_Window_Enable_out");
    element.jSetPinDescription(4, "Error_out");
        
    element.jSetPinDescription(5, "Enable_VM_in");
    element.jSetPinDescription(6, "PortName_in");
    element.jSetPinDescription(7, "ArduinoAutoResetEnabled_in");
    element.jSetPinDescription(8, "Debug_Window_Enable_in");
    element.jSetPinDescription(9, "Error_in");

    element.jSetResizable(false);
    element.jSetResizeSynchron(false);
    firstTime=true;
    JSSC = new ArduinoJSSC_JV();
    initInputPins();
    
  }

  public void initInputPins()
  {
    Enable_VM_in= (VSBoolean)element.getPinInputReference(5);
    if(Enable_VM_in==null){
      Enable_VM_in=new VSBoolean(false);
      Enable_VM_out=new VSBoolean(false);
    }
    SerialPortNameIn = (VSString)element.getPinInputReference(6);
    if(SerialPortNameIn==null){
        SerialPortNameIn=new VSString("");
    }
    ArduinoAutoRESET = (VSBoolean)element.getPinInputReference(7);
    if(ArduinoAutoRESET==null){
    ArduinoAutoRESET = new VSBoolean(true);
    }
    Debug_Window_En_in= (VSBoolean)element.getPinInputReference(8);
    if (Debug_Window_En_in==null){
       Debug_Window_En_in=new VSBoolean(false);
    }
    Error_in= (VSBoolean)element.getPinInputReference(9);
    if(Error_in==null){
       Error_in=new VSBoolean(false);
    }
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0, Enable_VM_out); 
    element.setPinOutputReference(1, SerialPortOut);
    element.setPinOutputReference(2, System_out);
    element.setPinOutputReference(3, Debug_Window_En_out);
    element.setPinOutputReference(4, Error_out);
  }

  public void start()
  { 
    //element.jConsolePrintln("Start");
    firstTime=true;
    Error_in.setValue(false);
    System_out.setValue("");
    Error_out.setValue(false);
    ServerStarted=false;

    if(SerialPortNameIn!=null)
    {
      try {
          
        if(ThreadCreated==false){
        //System.err.println("CrandoThreadServer");   
        ServerThread = new Thread(ServerUDP);
        ServerThread.start();
        ThreadCreated=true;
        ServerStarted=true;
        ServerUDP.SetPort(SokectPort.getValue());
        ServerUDP.setDebudMessagesEnable(Debug_Window_En_in.getValue());
        ServerUDP.OpenSocket();
        ServerUDP.SetJSSC(JSSC);
        ServerUDP.SetTimeout(MessageTimeOUT.getValue());
        ServerUDP.setEnableCR(CR_Enable.getValue());
        ServerUDP.setEnableLF(LF_Enable.getValue());
        
        //System.err.println("ThreadServerOK");
        }
        int BaudRateTemp= Integer.valueOf(BaudRateComboBox.getItem(BaudRateComboBox.selectedIndex));
        int dataBitsTemp = Integer.valueOf(dataBitsComboBox.getItem(dataBitsComboBox.selectedIndex));
        int stopBitsIndex = stopBitsComboBox.selectedIndex; //
        int ParityTemp = ParityComboBox.selectedIndex;
        int stopBitsInt = 0;
        if(stopBitsIndex==0) stopBitsInt=1;   //INDEX 0 = STOPBITS_1    = 1
        if(stopBitsIndex==1) stopBitsInt=3;   //INDEX 1 = STOPBITS_1.5  = 3
        if(stopBitsIndex==2) stopBitsInt=2;   //INDEX 2 = STOPBITS_2    = 2 
        
        if(PortOpened==false){
        JSSC.OpenPort(SerialPortNameIn.getValue());
        //System.err.println("PuertoAbiertoOK");
        }
        //JSSC.setDefaultParameters(ArduinoAutoRESET.getValue(), Thread.currentThread());
        JSSC.setParameters(ArduinoAutoRESET.getValue(), Thread.currentThread(),AutoResetTime.getValue(),BaudRateTemp,dataBitsTemp,stopBitsInt,ParityTemp);
        PortOpened=true;
        Error_out.setValue(false);
        
        if (Debug_Window_En_in.getValue()) {
            element.jConsolePrintln("Baud"+BaudRateTemp+"_dataBits:"+dataBitsTemp+"_StopBits:"+stopBitsInt+"_Paridad:"+ParityComboBox.selectedIndex);
        }
        
        
      } catch (SerialPortException ex) {
          //Logger.getLogger(InitSerialCOM_JV.class.getName()).log(Level.SEVERE, null, ex);
          Error_out.setValue(true);
          PortOpened=false;
      } catch (InterruptedException ex) {
          //Logger.getLogger(InitSerialCOM_JV.class.getName()).log(Level.SEVERE, null, ex);
          Error_out.setValue(true);
          PortOpened=false;
      } catch (Exception e){
          Error_out.setValue(true);
          PortOpened=false;
      }
      finally{
        if (Debug_Window_En_in.getValue() && Error_out.getValue()) {
            element.jConsolePrintln(Element_Tag+Error_Tag+"|ErrorOpeningPort:"+SerialPortNameIn.getValue());
            System_out.setValue(Element_Tag+Error_Tag+"|ErrorOpeningPort:"+SerialPortNameIn.getValue());
        }
      }
      
    }else{ // PORT Null
          Error_out.setValue(true);
          if (Debug_Window_En_in.getValue()) {
                    element.jConsolePrintln(Element_Tag+Error_Tag+"|Element_Not_Executed_Because_PortName=NULL|");
                    System_out.setValue(Element_Tag+Error_Tag+"|Element_Not_Executed_Because_PortName=NULL|");
                }    
    }
  }

  public void stop()
  { //element.jConsolePrintln("Stop");
    
    if(SerialPortNameIn!=null)
    {
      try {
          if(PortOpened==true){
          JSSC.ClosePort();
          PortOpened=false; 
          //System.err.println("PuertoCerradoOK");
          }
          Error_out.setValue(false);  
      } catch (SerialPortException ex) {    
      //Logger.getLogger(InitSerialCOM_JV.class.getName()).log(Level.SEVERE, null, ex);
        Error_out.setValue(true);
        
      }
      finally{
        if (Debug_Window_En_in.getValue() && Error_out.getValue()){
            element.jConsolePrintln(Element_Tag+Error_Tag+"|ErrorClosingPort:"+SerialPortNameIn.getValue());
            System_out.setValue(Element_Tag+Error_Tag+"|ErrorClosingPort:"+SerialPortNameIn.getValue());
        }  
        EnableOld=false;
        EnableProccess=false;   
      }
    }
    
  }

  public void process()
  { //element.jConsolePrintln("Proccess");
    
   if(EnableOld==false && Enable_VM_in.getValue()){
      EnableProccess=true;   
     }   

   if(EnableProccess==true && PortOpened==true && Error_in.getValue()==false) // Only executes one time if no error.
   { EnableProccess=false; 
       /*
     try{

        //setParams(BaudRate, dataBits, stopBits, Parity);
        //COMPort.setParams(9600, 8, 1, 0);
        //JSSC.WriteString("=1;");
        //Thread.sleep(5);
        //String TempStr = JSSC.ReadFromPort(Thread.currentThread(), MessageTimeOUT.getValue());
        
        //element.jConsolePrintln(Element_Tag+"READ:"+TempStr);
                
     }
     
     catch(SerialPortException e){
       Error_out.setValue(true);
       PortOpened=false;  
       System_out.setValue(Element_Tag+Error_Tag+"|Error_Reading_Port:"+JSSC.getPortName());
       if (Debug_Window_En_in.getValue()) {
           element.jConsolePrintln(System_out.getValue());
       }
     }
     catch(InterruptedException e){
       Error_out.setValue(true);
       PortOpened=false;  
       System_out.setValue(Element_Tag+Error_Tag+"|JVM_Error_Reading_Port:"+JSSC.getPortName());
       if (Debug_Window_En_in.getValue()) {
           element.jConsolePrintln(System_out.getValue());
       }
     }
     catch(Exception e){
       Error_out.setValue(true);
       PortOpened=false;  
       System_out.setValue(Element_Tag+Error_Tag+"|Error_Reading_Port:"+JSSC.getPortName()+"|PortDisconnected_OR_Wrong_Parameters");
       if (Debug_Window_En_in.getValue()) {
           element.jConsolePrintln(System_out.getValue());
       }
          
     } */
   }
   
   SerialPortOut=SerialPortNameIn;
   
   if(Error_out.getValue()){         // If there is error do not enable other VMs
   Enable_VM_out.setValue(false);
   }else{
   Enable_VM_out.setValue(Enable_VM_in.getValue());
   }
   EnableOld=Enable_VM_in.getValue();
   Debug_Window_En_out.setValue(Debug_Window_En_in.getValue());
   element.notifyPin(0);
   element.notifyPin(1);
   element.notifyPin(2); //Notify Error MSJ
   element.notifyPin(3);
   element.notifyPin(4); //Notify Error Boolean Out
  }

  
   public void setPropertyEditor()
  {
    
    element.jAddPEItem("BaudRate", BaudRateComboBox, 0,100);
    element.jAddPEItem("DataBits", dataBitsComboBox, 0,100);
    element.jAddPEItem("StopBits", stopBitsComboBox, 0,100);
    element.jAddPEItem("Partity", ParityComboBox, 0,100);
    element.jAddPEItem("Enable LF", LF_Enable, 0, 0);
    element.jAddPEItem("Enable CR", CR_Enable, 0, 0);
    element.jAddPEItem("Message_Timeout[mS]", MessageTimeOUT, 1, 1000);
    element.jAddPEItem("AutoResetTime[mS]", AutoResetTime, 1, 20000);
    element.jAddPEItem("Socket_Server_Port",SokectPort, 1, 9999);
    
    
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
    element.jSetPEItemLocale(d+4,language,"Enable LF");
    element.jSetPEItemLocale(d+5,language,"Enable CR");
    element.jSetPEItemLocale(d+6,language,"Message_Timeout[mS]");
    element.jSetPEItemLocale(d+7,language,"AutoResetTime[mS]");
    element.jSetPEItemLocale(d+8,language,"Socket_Server_Port]");

    language="es_ES";
    element.jSetPEItemLocale(d+0,language,"Tasa_De_Baudios");
    element.jSetPEItemLocale(d+1,language,"Bits_De_Datos");
    element.jSetPEItemLocale(d+2,language,"Bits_De_Parada");
    element.jSetPEItemLocale(d+3,language,"Paridad");
    element.jSetPEItemLocale(d+4,language,"Habilitar LF");
    element.jSetPEItemLocale(d+5,language,"Hbilitar CR");
    element.jSetPEItemLocale(d+6,language,"Timeout_Mensajes[mS]");
    element.jSetPEItemLocale(d+7,language,"Tiempo_de_AutoReset[mS]");
    element.jSetPEItemLocale(d+8,language,"Puerto_del_Socket_Servidor");
    
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


