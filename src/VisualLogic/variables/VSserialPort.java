/*
 * Copyright (C) 2017 velas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package VisualLogic.variables;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

/**
 *
 * @author velas
 */
public class VSserialPort extends VSObject {
    
    private SerialPort serialPort;
    private boolean enableCR=true;
    private boolean enableLF=true;
    private int TimeBeforeRead=50;
    private int MessageTimeOut=100;

    public void setMessageTimeOut(int MessageTimeOut) {
        this.MessageTimeOut = MessageTimeOut;
    }

    public void setTimeBeforeRead(int TimeBeforeRead) {
        this.TimeBeforeRead = TimeBeforeRead;
    }

    public SerialPort getSerialPort() {
        return serialPort;
    }

    public boolean isEnableCR() {
        return enableCR;
    }

    public boolean isEnableLF() {
        return enableLF;
    }
    
    public void setEnableCR(boolean EnableIn){
    this.enableCR=EnableIn;        
    }
    public void setEnableLF(boolean EnableIn){
    this.enableLF=EnableIn;        
    }
    
    public void setDefaultParameters (Boolean ArduinoAutoReset, Thread ThreadIn) throws SerialPortException, InterruptedException{
    if(serialPort.isOpened()){
        serialPort.setParams(9600, 8, 1, 0);
        if(ArduinoAutoReset){ 
            ThreadIn.sleep(5000);
         } // Si el Arduino tiene AutoRest se debe esperar 5 Segundos mientras inicia.
    }    
   }

    public void setParameters(boolean ArduinoAutoRESET,
            Thread ThreadIn,int AutoResetTimeIn, int BaudRateIn,
            int dataBitsIn, int stopBitsIn, int ParityIn, boolean RTSin,boolean DTRin) throws SerialPortException{
        if(serialPort.isOpened()){
        serialPort.setParams(BaudRateIn, dataBitsIn, stopBitsIn, ParityIn,RTSin,DTRin);
        }
    }
    public void setParameters(boolean ArduinoAutoRESET,
            Thread ThreadIn,int AutoResetTimeIn, int BaudRateIn,
            int dataBitsIn, int stopBitsIn, int ParityIn) throws SerialPortException, InterruptedException{
        if(serialPort.isOpened()){
        serialPort.setParams(BaudRateIn, dataBitsIn, stopBitsIn, ParityIn);
        if(ArduinoAutoRESET){
        ThreadIn.sleep(AutoResetTimeIn);
        }
        }
    }

    
   public int GetBytesAtPort() throws SerialPortException{
     int BytesCount=0;  
     if(serialPort.isOpened()){
     BytesCount=serialPort.getInputBufferBytesCount();
     }
     return BytesCount;
   }    
   public boolean isOpened(){
   return serialPort.isOpened();    
   }
   public void CloseCOMPort() throws SerialPortException{
    if(serialPort.isOpened()){
       serialPort.closePort();
    }
    }
   
    public VSserialPort()
    {
        
    }
    public VSserialPort(SerialPort serialPortIn)
    {
        this.serialPort=serialPortIn;
    }
    public void writeBufferStr(String InStr) throws SerialPortException{
      if(InStr!=null){
        if(enableCR){
            InStr+="\r";
        }
        if(enableLF){
            InStr+="\n";
        }
        serialPort.writeString(InStr);
        //System.out.println("BufferOut:"+InStr+"|");
      }
    }
    public String ReadStrBuffer(int ByteCount) throws SerialPortException, SerialPortTimeoutException{
        String bufferTemp="";
        bufferTemp=serialPort.readString(ByteCount, MessageTimeOut);
        return bufferTemp;
    }
    public String ReadStrBuffer(Thread ThreadWait) throws SerialPortException, SerialPortTimeoutException, InterruptedException{
        String bufferTemp="";
        ThreadWait.sleep(TimeBeforeRead);
        bufferTemp=serialPort.readString(serialPort.getInputBufferBytesCount(),MessageTimeOut);
        return bufferTemp;
    }
    @Override
    public String toString()
    {
        return serialPort.getName();
    }
    
    
    public void setValue(SerialPort value)
    {
       if (value!=null)
        {
            this.serialPort=value;
            setChanged(true);
        }
    }
    
    public SerialPort getValue()
    {
        return this.serialPort;
    }

    @Override
    public void copyValueFrom(Object in)
    {
        if (in!=null)
        {
            VSserialPort temp =(VSserialPort)in;
            serialPort=temp.serialPort;
            setChanged(temp.isChanged());
        }
        else serialPort=null;
    }
    @Override
    public void copyReferenceFrom(Object in)
    {
      copyValueFrom(in);
    }    
        
    
    @Override
    public boolean equals(VSObject obj)
    {
        VSserialPort temp =(VSserialPort)obj;
        if (temp.serialPort.getPortName().equalsIgnoreCase(serialPort.getPortName()))  return true;
        else return false;
    }
    
    @Override
    public boolean isBigger(VSObject obj)
    {
        
        return false;
    }
    
    @Override
    public boolean isSmaller(VSObject obj)
    {
        return false;
    }
    
    
    @Override
    public void loadFromStream(java.io.FileInputStream fis)
    {
        try
        {
            java.io.DataInputStream dis = new java.io.DataInputStream(fis);
            
            serialPort.setName(dis.readUTF());
        }
        catch(Exception ex)
        {
            
        }
        
    }
    
    @Override
    public void saveToStream(java.io.FileOutputStream fos)
    {
        try
        {
            java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);
            dos.writeUTF(serialPort.getName());
            
        }
        catch(Exception ex)
        {
           System.err.println("Fehler in VSDouble.saveToStream() : "+ex.toString());
        }
    }
    public void loadFromXML(String name,org.w3c.dom.Element nodeElement)
    {
        serialPort.setName(nodeElement.getAttribute("VSserialPort"+name));
    }
    
    public void saveToXML(String name, org.w3c.dom.Element nodeElement)
    {
        nodeElement.setAttribute("VSserialPort"+name, serialPort.getName());
    }
    
}
