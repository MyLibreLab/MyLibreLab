
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



public class Get_Bytes_at_Port_JV extends JVSMain
{

  private Image image;
  private Boolean PortOpened=false;
  String s = "";
  String sysOut_N_Err= ""; // String System Response without error
  String sysOut_Err= "";   // String System Response when error
  boolean firstTime=true;
  boolean started=false;
  final String Element_Tag= "#Element:|Get_Bytes_at_Port_JV|";
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
  
  VSString StrBufferOut_In = new VSString("");
   VSBoolean Debug_Window_En_out = new VSBoolean(false);
  VSInteger StartUpDelay =new VSInteger(5);
  VSBoolean MessageFilterEn =new VSBoolean(false);
  VSInteger DelayBeforeRead =new VSInteger(50);
  // Outputs
  VSBoolean Enable_VM_out = new VSBoolean(false);
  VSString System_out = new VSString();
 
  VSBoolean Error_out = new VSBoolean(false);
  VSInteger BytesAtPortOut= new VSInteger(0);
  VSInteger BytesAtPortIndicatorOut= new VSInteger(0);
  
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
    element.jSetName("Get_Bytes_at_Port_JV");
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
 
    setPin(0, ExternalIF.C_INTEGER, ExternalIF.PIN_OUTPUT); // Simetría
    
    setPin(1, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);
    setPin(2, ExternalIF.C_STRING, ExternalIF.PIN_OUTPUT);
    setPin(3, ExternalIF.C_INTEGER, ExternalIF.PIN_OUTPUT);
    setPin(4, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);
    setPin(5, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);
    setPin(6, ExternalIF.C_VARIANT, ExternalIF.PIN_OUTPUT); // 
    
    setPin(7, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    setPin(8, ExternalIF.C_STRING, ExternalIF.PIN_INPUT);
    setPin(9, ExternalIF.C_VARIANT, ExternalIF.PIN_INPUT);
    setPin(10, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    setPin(11, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
    
    element.jSetPinDescription(0, "Bytes_At_Port_out");
    
    element.jSetPinDescription(1, "Enable_VM_out");
    element.jSetPinDescription(2, "PortName_out");
    element.jSetPinDescription(3, "Bytes_At_Port_out");
    element.jSetPinDescription(4, "Debug_Window_Enable_out");
    element.jSetPinDescription(5, "Error_out");    
    element.jSetPinDescription(6, "N/C");
    
    element.jSetPinDescription(7, "Enable_VM_in");
    element.jSetPinDescription(8, "Port_Name_in");
    element.jSetPinDescription(9, "N/C");
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
    element.setPinOutputReference(0, BytesAtPortIndicatorOut);
    element.setPinOutputReference(1, Enable_VM_out); 
    element.setPinOutputReference(2, SerialPortNameOut);
    element.setPinOutputReference(3, BytesAtPortOut);
    element.setPinOutputReference(4,Debug_Window_En_out);
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
   Debug_Window_En_out.setValue(Debug_Window_En_in.getValue());
   BytesAtPortOut.setValue(0);
   BytesAtPortIndicatorOut.setValue(0); // Duplicado para no usar Nodos
   Error_out.setValue(Error_in.getValue());
   
   element.notifyPin(0); // BytesAtPortIndicatorOut
   element.notifyPin(1); // Enable VM Out
   element.notifyPin(2); // Serial Port Out
   element.notifyPin(3); // BytesAtPortOut
   element.notifyPin(4); // DebugWindowEnable
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
      Debug_Window_En_out.setValue(Debug_Window_En_in.getValue());
      element.notifyPin(3); // Debug Enable Out   
      firstTime=false;    
      try { 
        Thread.currentThread().sleep(StartUpDelay.getValue());
      } catch (InterruptedException ex) {}
      }
     
     try{
       SerialPortNameOut.setValue(SerialPortNameIn.getValue());
       element.notifyPin(2); // Serial Port Out
       
       vsPortIn=NewSerialDriverManager.FindSerialPortByPortName(SerialPortNameIn.getValue()); 
       
       if(vsPortIn!=null){
        
       Thread.currentThread().sleep(DelayBeforeRead.getValue());
       BytesAtPortOut.setValue(vsPortIn.GetBytesAtPort()); 
       BytesAtPortIndicatorOut.setValue(BytesAtPortOut.getValue());
       element.notifyPin(3); // BytesAtPortOut
       element.notifyPin(0); // BytesAtPortIndicatorOut Duplicated to avoid use "Node" element
       if(Debug_Window_En_in.getValue()){
       element.jConsolePrintln(Element_Tag+"_Serial Port: "+SerialPortNameIn.getValue()+"_TOTAL_BYTES:"+BytesAtPortOut.getValue());
       }

       }else{
       System_out.setValue(Error_Tag+Element_Tag+"Null Port!");
       if(Debug_Window_En_in.getValue()){
       element.jConsolePrintln(Element_Tag+Error_Tag+"Null Port! "+SerialPortNameIn.getValue());
       }
       Error_out.setValue(true);
       }
       
     }catch(SerialPortException ex){
         //Logger.getLogger(SerialPort_Transaction_JV.class.getName()).log(Level.SEVERE, null, ex); 
         Error_out.setValue(true);
         System_out.setValue(Element_Tag+Error_Tag+"SerialPortException"+ex.getMessage()+"");
         BytesAtPortOut.setValue(0); 
         BytesAtPortIndicatorOut.setValue(0);
         element.notifyPin(3); // BytesAtPortOut
         element.notifyPin(0); // BytesAtPortIndicatorOut Duplicated to avoid use "Node" element
         if(Debug_Window_En_in.getValue()){
         element.jConsolePrintln(System_out.getValue());    
         }
     }catch (Exception e){
         BytesAtPortOut.setValue(0); 
         BytesAtPortIndicatorOut.setValue(0);
         element.notifyPin(3); // BytesAtPortOut
         element.notifyPin(0); // BytesAtPortIndicatorOut Duplicated to avoid use "Node" element
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
   element.notifyPin(3); // BytesAtPortOut
  }
 
  public void propertyChanged(Object o)
  {   
    element.jRepaint();
  }
  
  
  public void setPropertyEditor()
  {
    //element.jAddPEItem("Message Filter Enable", MessageFilterEn, 0, 0);
    element.jAddPEItem("StartUPTime[mS]",StartUpDelay , 1, 20000);
    element.jAddPEItem("Delay_Before_Read[mS]",DelayBeforeRead , 0, Integer.MAX_VALUE);
    
    localize();
  }

  private void localize()
  {
    String language;
    int d=6;
    language="en_US";
    //element.jSetPEItemLocale(d+0,language,"Message_Filter_Enable");
    element.jSetPEItemLocale(d+0,language,"StartUP_Delay[mS]");
    element.jSetPEItemLocale(d+1,language,"Delay_Before_Read[mS]");
    
    
    language="es_ES";
    //element.jSetPEItemLocale(d+0,language,"Activar_Filtro_de_Mensajes");
    element.jSetPEItemLocale(d+0,language,"Retardo_de_Inicio[mS]");
    element.jSetPEItemLocale(d+1,language,"Retardo_antes_de_Lectura[mS]");
    
  }
  
  public void saveToStream(java.io.FileOutputStream fos){
      //MessageFilterEn.saveToStream(fos);
      StartUpDelay.saveToStream(fos);
      DelayBeforeRead.saveToStream(fos);
      
  }
  
  public void loadFromStream(java.io.FileInputStream fis){
      //MessageFilterEn.loadFromStream(fis);
      StartUpDelay.loadFromStream(fis);
      DelayBeforeRead.loadFromStream(fis);

      element.jRepaint();
  }
  
  
}
