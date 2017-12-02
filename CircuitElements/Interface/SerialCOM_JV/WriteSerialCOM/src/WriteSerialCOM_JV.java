
//********************************
//* Autor : Robinson Javier Velasquez
//* Date  : Nov-14-2017
//* Email : javiervelasquez@gmail.com
//* licence : non commercial use, only Educational Use.
//********************************


import VisualLogic.*;
import java.awt.*;
import tools.*;
import VisualLogic.variables.*;
import java.io.*;




public class WriteSerialCOM_JV extends JVSMain
{
  private SocketClient_JV SockectCliente;
  
  private Image image;
  private Boolean SocketCreated=false;
  String s = "";
  String sysOut_N_Err= ""; // String System Response without error
  String sysOut_Err= "";   // String System Response when error
  boolean firstTime=false;
  boolean PortOpened=false;
  final String Element_Tag= "#Element:|WriteSerialCOM_JV|";
  final String Error_Tag="#Error:";
  final String Debug_Tag="#Debug_Msj:";
  // Properties
  // Inputs
    
  VSBoolean Enable_VM_in = new VSBoolean(false);

  VSString SerialPortOut = new VSString();
  VSString SocketIP = new VSString("localhost");
  VSInteger SocketPort = new VSInteger(9876);
  VSString dataToSEND_in = new VSString();  
  
  VSBoolean Debug_Window_En_in = new VSBoolean(false);
  VSBoolean Error_in = new VSBoolean(false);
  
  VSBoolean ArduinoAutoRESET = new VSBoolean(true);
  VSInteger StartUpTime =new VSInteger(1500);
  VSInteger MessageTimeOUT =new VSInteger(125);
  // Outputs
  VSBoolean Enable_VM_out = new VSBoolean(false);
  VSString System_out = new VSString();
  VSBoolean TimeOut_out = new VSBoolean(false);
  VSBoolean Error_out = new VSBoolean(false);
  final String TestStr="=3;";
  volatile String BufferRTACOM;
  volatile Boolean TransactionInit=false;
  volatile Boolean TransactionFinished=false;
  volatile Boolean EnableOld=false;
  volatile Boolean EnableProccess=false;
  
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
    try {
          Thread.sleep(125);
      } catch (InterruptedException ex) {
          //Logger.getLogger(InitSerialCOM_JV.class.getName()).log(Level.SEVERE, null, ex);
      }
  }

  


  public void init()
  { 
    
    SockectCliente = new SocketClient_JV();
    TransactionInit=false;
    TransactionFinished=false;
    element.jSetName("Write_COM_Port_JV");
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
    element.jSetPinDescription(1, "DataReceived_out");
    element.jSetPinDescription(2, "System_msj_out");
    element.jSetPinDescription(3, "Timeout_out");
    element.jSetPinDescription(4, "Error_out");
        
    element.jSetPinDescription(5, "Enable_VM_in");
    element.jSetPinDescription(6, "DataToSend_in");
    element.jSetPinDescription(7, "ArduinoAutoResetEnabled_in");
    element.jSetPinDescription(8, "Debug_Window_Enable_in");
    element.jSetPinDescription(9, "Error_in");
    
    element.jSetResizable(false);
    element.jSetResizeSynchron(false);
    firstTime=true;
    initInputPins();
  }

  public void initInputPins()
  {
    Enable_VM_in= (VSBoolean)element.getPinInputReference(5);
      if(Enable_VM_in==null){
      Enable_VM_in=new VSBoolean(false);
      Enable_VM_out=new VSBoolean(false);
    }
    
    dataToSEND_in = (VSString)element.getPinInputReference(6);
    if(dataToSEND_in==null){
    dataToSEND_in=new VSString("");
    }
    ArduinoAutoRESET = (VSBoolean)element.getPinInputReference(7);
    if(ArduinoAutoRESET==null){
    ArduinoAutoRESET=new VSBoolean(true);
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
    element.setPinOutputReference(3, TimeOut_out);
    element.setPinOutputReference(4, Error_out);
  }

  public void start()
  {
   EnableOld=false;
   TransactionInit=false;
   TransactionFinished=false;
   Error_in.setValue(false);
   System_out.setValue("");
   Error_out.setValue(false);
   
   if(ArduinoAutoRESET.getValue()){
   try {
        Thread.sleep(StartUpTime.getValue()); // Wait while Server Starts
    } catch (InterruptedException ex) {
        System_out.setValue(Element_Tag+Error_Tag+"Execution_Error_Sleeping_Thread|"+ex.getMessage());
    }
   }
  }

  public void stop()
  {
      if(SocketCreated==true){
          try {
              SockectCliente.closeSocket();
              Error_out.setValue(false);
          } catch (IOException ex) {
          Error_out.setValue(true);
          System_out.setValue(Element_Tag+Error_Tag+"Error_Closing_SOCKET"+ex.getMessage());
          //Logger.getLogger(WriteSerialCOM_JV.class.getName()).log(Level.SEVERE, null, ex);
          }
           SocketCreated=false;
           EnableOld=false;
           EnableProccess=false; 
   }
       
  }

  public void process()
  { 
   if(EnableProccess==false && EnableOld==false && Enable_VM_in.getValue()){
      EnableProccess=true;   
     }
         
   if(EnableProccess && Error_in.getValue()==false) // Only executes one time if no error and Rising Edge Enable Input.
   { EnableProccess=false; 
     try{
       //element.jConsolePrintln(Element_Tag+"ProccessWRITE_1");
       SockectCliente.setHost(SocketIP.getValue());
       SockectCliente.setPort(SocketPort.getValue());
       SockectCliente.setDebugMsgEnable(Debug_Window_En_in.getValue());
       SockectCliente.openSocket();
       SockectCliente.setTimeout(MessageTimeOUT.getValue());
       BufferRTACOM=SockectCliente.runOnce(dataToSEND_in.getValue());
       SerialPortOut.setValue(BufferRTACOM);
       SockectCliente.closeSocket();
       //SockectCliente.closeSocket();
       //element.jConsolePrintln("Recibido:"+TempStr);
       Error_out.setValue(false);
       TimeOut_out.setValue(false);
       System_out.setValue("");
     }catch(IOException ex){
         SerialPortOut.setValue("");
         Error_out.setValue(false);
         TimeOut_out.setValue(true);
         System_out.setValue(Element_Tag+Error_Tag+"I/O_Error_or_Timeout_SOCKET"+ex.getMessage()+"_Wrong_Parameters_or_server_turnedOFF?");
     } catch (InterruptedException ex) {
         Error_out.setValue(true); 
         SerialPortOut.setValue("");
         System_out.setValue(Element_Tag+Error_Tag+"Execution_Error_Opening_SOCKET"+ex.getMessage());
     }
     
     if(Error_out.getValue() && Debug_Window_En_in.getValue()){
        element.jConsolePrintln(System_out.getValue());
     }
     
   }
   //SerialPortOut=COMPort;
   if(Error_out.getValue()){
   Enable_VM_out.setValue(false);    
   }else{
   Enable_VM_out.setValue(Enable_VM_in.getValue());        
   }
   EnableOld=Enable_VM_in.getValue();
   //Enable_VM_out.setValue(EnableProccess);
   
   element.notifyPin(0);
   element.notifyPin(1);
   element.notifyPin(2); //Notify Error MSJ
   element.notifyPin(3);
   element.notifyPin(4); //Notify Error Boolean Out
  }
 
  public void propertyChanged(Object o)
  {   
    element.jRepaint();
  }
  
  
  public void setPropertyEditor()
  {
    
    
    element.jAddPEItem("Message Timeout[ms]", MessageTimeOUT, 1, 5000);
    element.jAddPEItem("AutoResetTime[mS]",StartUpTime , 1, 20000);
    element.jAddPEItem("IP_Sockect_Client",SocketIP , 0, 0);
    element.jAddPEItem("Port_Socket_Client",SocketPort , 1, 20000);
    localize();
  }

  private void localize()
  {
    String language;
    int d=6;
    language="en_US";
    element.jSetPEItemLocale(d+0,language,"Message_Timeout[mS]");
    element.jSetPEItemLocale(d+1,language,"AutoResetTime[mS]");
    element.jSetPEItemLocale(d+2,language,"IP_Sockect_Client");
    element.jSetPEItemLocale(d+3,language,"Port_Socket_Client");
    
    language="es_ES";
    element.jSetPEItemLocale(d+0,language,"Timeout_Mensajes[mS]");
    element.jSetPEItemLocale(d+1,language,"Tiempo_de_AutoReset[mS]");
    element.jSetPEItemLocale(d+2,language,"IP_Socket_Cliente");
    element.jSetPEItemLocale(d+3,language,"Puerto_Socket_Cliente");
    
  }
  
  public void saveToStream(java.io.FileOutputStream fos){
      MessageTimeOUT.saveToStream(fos);
      StartUpTime.saveToStream(fos);
      SocketIP.saveToStream(fos);
      SocketPort.saveToStream(fos);
  }
  
  public void loadFromStream(java.io.FileInputStream fis){
      MessageTimeOUT.loadFromStream(fis);
      StartUpTime.loadFromStream(fis);
      SocketIP.loadFromStream(fis);
      SocketPort.loadFromStream(fis);
      
      element.jRepaint();
  }
  
  
}
