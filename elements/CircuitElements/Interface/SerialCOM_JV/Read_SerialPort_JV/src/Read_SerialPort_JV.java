
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
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;




public class Read_SerialPort_JV extends JVSMain
{
  private Image image;
  private Boolean PortOpened=false;
  String s = "";
  String sysOut_N_Err= ""; // String System Response without error
  String sysOut_Err= "";   // String System Response when error
  boolean firstTime=true;
  boolean started=false;
  final String Element_Tag= "#Element:|Read_SerialPort_JV|";
  final String Error_Tag="#Error:";
  final String Debug_Tag="#Debug_Msj:";
  // Properties
  // Inputs
    
  VSBoolean Enable_VM_in = new VSBoolean(false);
  VSString SerialPortNameOut = new VSString();
  VSString SerialPortNameIn = new VSString("");
  VSserialPort vsPortIn;
  VSBoolean Debug_Window_En_in = new VSBoolean(false);
  VSBoolean Error_in = new VSBoolean(false);
  
  VSInteger BytesToRead = new VSInteger(0);
  VSInteger BytesReaded_Out = new VSInteger(0);
  VSString StrBufferRTA_Out = new VSString("");
  VSInteger StartUpDelay =new VSInteger(100);
 
  VSInteger DelayBeforeRead =new VSInteger(5);
  // Outputs
  VSBoolean Enable_VM_out = new VSBoolean(false);
  VSString System_out = new VSString();
  VSBoolean TimeOut_out = new VSBoolean(false);
  VSBoolean Error_out = new VSBoolean(false);
  
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
  }

  


  public void init()
  { 
    
    firstTime=true;
    started=false;
    TransactionInit=false;
    TransactionFinished=false;
    element.jSetName("Read_SerialPort_JV");
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
 
    
    setPin(0, ExternalIF.C_INTEGER, ExternalIF.PIN_OUTPUT);
    
    setPin(1, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);
    setPin(2, ExternalIF.C_STRING, ExternalIF.PIN_OUTPUT);
    setPin(3, ExternalIF.C_STRING, ExternalIF.PIN_OUTPUT);
    setPin(4, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);
    setPin(5, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);
    
    setPin(6, ExternalIF.C_VARIANT, ExternalIF.PIN_INPUT);
    
    setPin(7, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    setPin(8, ExternalIF.C_STRING, ExternalIF.PIN_INPUT);
    setPin(9, ExternalIF.C_INTEGER, ExternalIF.PIN_INPUT);
    setPin(10, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    setPin(11, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    
    
    element.jSetPinDescription(0, "Bytes_Readed_out");
    
    element.jSetPinDescription(1, "Enable_VM_out");
    element.jSetPinDescription(2, "PortName_out");
    element.jSetPinDescription(3, "Buffer_Read_out");
    element.jSetPinDescription(4, "Timeout_out");
    element.jSetPinDescription(5, "Error_out");
        
    element.jSetPinDescription(6, "N/C");
    
    element.jSetPinDescription(7, "Enable_VM_in");
    element.jSetPinDescription(8, "Port_Name_in");
    element.jSetPinDescription(9, "Bytes_To_Read");
    element.jSetPinDescription(10, "Debug_Window_Enable_in");
    element.jSetPinDescription(11, "Error_in");
    
    element.jSetResizable(false);
    element.jSetResizeSynchron(false);
    firstTime=true;
    initInputPins();
  }

  public void initInputPins()
  {
    Enable_VM_in= (VSBoolean)element.getPinInputReference(7);
      if(Enable_VM_in==null){
      Enable_VM_in=new VSBoolean(false);
      Enable_VM_out=new VSBoolean(false);
    }
    
    SerialPortNameIn =(VSString) element.getPinInputReference(8);
    if(SerialPortNameIn==null){
    SerialPortNameIn=new VSString("");
    }
    BytesToRead = (VSInteger)element.getPinInputReference(9);
    if(BytesToRead==null){
    BytesToRead=new VSInteger(0);
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
  {
    element.setPinOutputReference(0, BytesReaded_Out); 
    
    element.setPinOutputReference(1, Enable_VM_out); 
    element.setPinOutputReference(2, SerialPortNameOut);
    element.setPinOutputReference(3, StrBufferRTA_Out);
    element.setPinOutputReference(4, TimeOut_out);
    element.setPinOutputReference(5, Error_out);
  }

  public void start()
  {
   EnableOld=false;
   TransactionInit=false;
   TransactionFinished=false;
   Error_in.setValue(false);
   System_out.setValue("");
   Error_out.setValue(false);
   PortOpened=false;
   firstTime=true;
   
   Enable_VM_out.setValue(false);
   SerialPortNameOut.setValue(SerialPortNameIn.getValue());
   System_out.setValue("");
   StrBufferRTA_Out.setValue("");
   TimeOut_out.setValue(false);
   Error_out.setValue(Error_in.getValue());
   BytesReaded_Out.setValue(0);
   
   element.notifyPin(0); // BytesReaded_Out
   element.notifyPin(1); // Enable VM Out
   element.notifyPin(2); // Serial Port Out
   element.notifyPin(3); // BufferStrRTA Out
   element.notifyPin(4); // Timeout Out
   element.notifyPin(5); // Error Out
   started=true;

  }

  public void stop()
  {
           PortOpened=false;
           EnableOld=false;
           EnableProccess=false; 
           started=false;
    
  }

  public void process()
   { 
   if(EnableProccess==false && EnableOld==false && Enable_VM_in.getValue() && started){       
      EnableProccess=true;
      Error_out.setValue(Error_in.getValue());
    }
    
   if((EnableProccess && Error_in.getValue()==false)) // Only executes one time if no error and Rising Edge Enable Input.
    { EnableProccess=false; 
     System_out.setValue("");
     if(firstTime){
      firstTime=false;    
      try { 
        Thread.currentThread().sleep(StartUpDelay.getValue());
      } catch (InterruptedException ex) {}
      }
     
     try{
       SerialPortNameOut.setValue(SerialPortNameIn.getValue());
       element.notifyPin(2); // Serial Port Out
       if(Debug_Window_En_in.getValue()){
       element.jConsolePrintln(Element_Tag+"Proccessing Read Serial Port: "+SerialPortNameIn.getValue());
       }
       vsPortIn=NewSerialDriverManager.FindSerialPortByPortName(SerialPortNameIn.getValue()); 
       
       if(vsPortIn!=null && BytesToRead.getValue()>0){
       //vsPortIn.writeBufferStr(BytesToRead.getValue());
       Thread.currentThread().sleep(DelayBeforeRead.getValue());
       BufferRTACOM="";
       BufferRTACOM=vsPortIn.ReadStrBuffer(BytesToRead.getValue());
       }else{
       BufferRTACOM="";    
       System_out.setValue(Error_Tag+Element_Tag+"Null Port!");
       if(vsPortIn==null){
       Error_out.setValue(true);
       }
       }
       StrBufferRTA_Out.setValue(BufferRTACOM);
       element.notifyPin(3); // BufferStrRTA Out
       BytesReaded_Out.setValue(BufferRTACOM.length());
       element.notifyPin(0); // BytesReaded_Out
    
       TimeOut_out.setValue(false);
       
     }catch(SerialPortException ex){
         //Logger.getLogger(SerialPort_Transaction_JV.class.getName()).log(Level.SEVERE, null, ex);
         StrBufferRTA_Out.setValue("");
         element.notifyPin(3); // BufferStrRTA Out
         Error_out.setValue(true);
         TimeOut_out.setValue(true);
         BytesReaded_Out.setValue(0);
         element.notifyPin(0); // BytesReaded_Out
         System_out.setValue(Element_Tag+Error_Tag+"SerialPortException"+ex.getMessage()+"");
         if(Debug_Window_En_in.getValue()){
         element.jConsolePrintln(System_out.getValue());    
         }
     } catch (InterruptedException ex) {
         StrBufferRTA_Out.setValue("");
         element.notifyPin(3); // BufferStrRTA Out
         //Logger.getLogger(SerialPort_Transaction_JV.class.getName()).log(Level.SEVERE, null, ex);
         Error_out.setValue(true); 
         BytesReaded_Out.setValue(0);
         element.notifyPin(0); // BytesReaded_Out
         System_out.setValue(Element_Tag+Error_Tag+"Execution_Error"+ex.getMessage());
         if(Debug_Window_En_in.getValue()){
         element.jConsolePrintln(System_out.getValue());    
         }
     }catch (SerialPortTimeoutException ex) {
         StrBufferRTA_Out.setValue("");
         element.notifyPin(3); // BufferStrRTA Out
         BytesReaded_Out.setValue(0);
         element.notifyPin(0); // BytesReaded_Out
         //Logger.getLogger(SerialPort_Transaction_JV.class.getName()).log(Level.SEVERE, null, ex);
         System_out.setValue(Element_Tag+Error_Tag+"Serial_Timeout_Error"+ex.getMessage());
         Error_out.setValue(false);
         TimeOut_out.setValue(true);
         element.notifyPin(4); // Timeout Out
         
     }
     catch (Exception e){
         //Logger.getLogger(SerialPort_Transaction_JV.class.getName()).log(Level.SEVERE, null, e);
         System.out.println(Element_Tag+Error_Tag+e.getMessage());
     }
      
   }
   
   if(Error_out.getValue() || Error_in.getValue()){
   Enable_VM_out.setValue(false);
   }else{
       Enable_VM_out.setValue(Enable_VM_in.getValue());        
   }
   if(Error_in.getValue()){
       Error_out.setValue(true);
   }
   element.notifyPin(1); // Enable VM Out
   //element.notifyPin(2); // Serial Port Out
   element.notifyPin(5); // Error Out
   EnableOld=Enable_VM_in.getValue(); 
  }
 
  public void propertyChanged(Object o)
  {   
    element.jRepaint();
  }
  
  
  public void setPropertyEditor()
  {
    //element.jAddPEItem("Message Timeout[ms]", MessageTimeOUT, 1, 5000);
    element.jAddPEItem("StartUPTime[mS]",StartUpDelay , 1, 20000);
    element.jAddPEItem("Delay_Before_Read[mS]",DelayBeforeRead , 0, Integer.MAX_VALUE);
    
    localize();
  }

  private void localize()
  {
    String language;
    int d=6;
    language="en_US";
    //element.jSetPEItemLocale(d+0,language,"Message_Timeout[mS]");
    element.jSetPEItemLocale(d+0,language,"StartUP_Delay[mS]");
    element.jSetPEItemLocale(d+1,language,"Delay_Before_Read[mS]");
    
    
    language="es_ES";
    //element.jSetPEItemLocale(d+0,language,"Timeout_de_Mensaje[mS]");
    element.jSetPEItemLocale(d+0,language,"Retardo_de_Inicio[mS]");
    element.jSetPEItemLocale(d+1,language,"Retardo_antes_de_Lectura[mS]");
    
  }
  
  public void saveToStream(java.io.FileOutputStream fos){
      //MessageTimeOUT.saveToStream(fos);
      StartUpDelay.saveToStream(fos);
      DelayBeforeRead.saveToStream(fos);
      
  }
  
  public void loadFromStream(java.io.FileInputStream fis){
      //MessageTimeOUT.loadFromStream(fis);
      StartUpDelay.loadFromStream(fis);
      DelayBeforeRead.loadFromStream(fis);

      element.jRepaint();
  }
  
  
}
