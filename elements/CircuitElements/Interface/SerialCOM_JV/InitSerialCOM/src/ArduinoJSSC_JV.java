/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;
/**
 *
 * @author velas
 */
public class ArduinoJSSC_JV {
public static SerialPort serialPort;
private static Boolean ErrorOpenningPort=false;
private static Boolean ErrorWrittingPort=false;
private static Boolean ErrorSettingPort=false;
private static Boolean ErrorReadingPort=false;
private static Boolean ErrorClosingPort=false;
private static Boolean DebugMsjEnable=true;
private static Boolean PortOpened=false;
private static Boolean ArduinoAutoReset=true; // Autoreset Arduino ON?
private static String PortName = "";
private static Thread ThreadIn;
public static final Boolean ArduinoAutoResetON=true;
public static final Boolean ArduinoAutoResetOFF=false;
public Boolean EnableCR=false;
public Boolean EnableLF=false;
   
public void ArduinoJSSC_JV(){
    
}
public void SetEnableCR(Boolean EnableCRin){
    this.EnableCR=EnableCRin;
}
public void SetEnableLF(Boolean EnableLFin){
    this.EnableLF=EnableLFin;
}
public void DisposeSerialPort(){
    serialPort.onDispose();
    serialPort=null;
}
public void OpenPort(String PortName) throws SerialPortException, InterruptedException, Exception{
         
         this.PortName=PortName;
         serialPort = new SerialPort(PortName);    
         if(serialPort.isOpened()) serialPort.closePort();
         serialPort.openPort();
         ErrorOpenningPort=false;
         PortOpened=true;      
    }
public void setDefaultParameters (Boolean ArduinoAutoReset, Thread FatherThreadIn) throws SerialPortException, InterruptedException{
    if(PortOpened){
        this.ThreadIn=FatherThreadIn;
        this.ArduinoAutoReset=ArduinoAutoReset;
        
        serialPort.setParams(9600, 8, 1, 0);
        System.out.println("Parameters Configured OK");
        ErrorSettingPort=false;
        if(ArduinoAutoReset){ 
            ThreadIn.sleep(5000);
         } // Si el Arduino tiene AutoRest se debe esperar 5 Segundos mientras inicia.
        
    }    
}
public void setParameters (Boolean ArduinoAutoReset, Thread FatherThreadIn,int time, int BaudR, int dataBits, int StopBits, int Parity) throws SerialPortException, InterruptedException{
    if(PortOpened){
        this.ThreadIn=FatherThreadIn;
        this.ArduinoAutoReset=ArduinoAutoReset;
        
        //serialPort.setParams(9600, 8, 1, 0);
        serialPort.setParams(BaudR,dataBits,StopBits,Parity);
        //System.out.println("Parameters Configured OK");
        ErrorSettingPort=false;
        if(ArduinoAutoReset){ 
            ThreadIn.sleep(time);
         } // Si el Arduino tiene AutoRest se debe esperar 5 Segundos mientras inicia.
        
    }    
}
public void WriteString(String BufferStr) throws SerialPortException{
  
  if(BufferStr==null){
   BufferStr="";   
  }  
  if(EnableLF) BufferStr+="\n";
  if(EnableCR) BufferStr+="\r";
  if(PortOpened){  
    serialPort.writeBytes(BufferStr.getBytes());  
    ErrorWrittingPort=false;
  }
}

public String ReadFromPort(Thread FatherThreadIn,long TimeSleep) throws SerialPortException, InterruptedException, NullPointerException{
String BufferTemp = "";
if(PortOpened){
    this.ThreadIn=FatherThreadIn;
    serialPort.purgePort(SerialPort.PURGE_RXCLEAR);
    serialPort.purgePort(SerialPort.PURGE_TXCLEAR);
    ThreadIn.sleep(TimeSleep); 
    //Thread.sleep(TimeSleep);
    BufferTemp = new String((serialPort.readBytes()));
    ErrorReadingPort=false;
    if(BufferTemp==null){
    BufferTemp=new String("");
    }
    return BufferTemp;
}
return new String("");
}

public String ReadBytesFromPort(int Bytes,int TimeOut) throws SerialPortException, InterruptedException, SerialPortTimeoutException, NullPointerException{
String BufferTemp = "";
if(PortOpened){
      
    BufferTemp = new String((serialPort.readBytes(Bytes,TimeOut)));
    ErrorReadingPort=false;
    if(DebugMsjEnable) System.out.println("Reading Port "+PortName + " OK");
    if(BufferTemp==null){
    BufferTemp=new String("");
    throw new SerialPortTimeoutException(serialPort.getPortName(),"ReadBytesFromPort",TimeOut);
    }
    return BufferTemp; 
}
return new String("");
}

public void ClosePort() throws SerialPortException{
  
 if(PortOpened){   
    
    serialPort.closePort();
    ErrorClosingPort=false;
    PortOpened=false;
 }
 } 
public boolean GetPortOpened(){
    
    return serialPort.isOpened();
}
public String getPortName(){
    if(PortName==null){
    PortName="";    
    }
    return PortName;
}


}
