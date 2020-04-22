
import VisualLogic.variables.VSserialPort;

import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPortException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author velas
 */
    
 public class SerialServer implements Runnable {
    

    public Boolean Error=false;
    private VSserialPort vsSerialExternal;
    private Boolean EnableCR=false;
    private Boolean EnableLF=false;
    private Boolean DebugMessages=false;
    private int TimeOut=125;
    private int TimeBeforeRead=50;

    public void setTimeBeforeRead(int TimeBeforeRead) {
        this.TimeBeforeRead = TimeBeforeRead;
    }


    volatile String BufferIn="";
    volatile String COMResponseStr="";

    
    public SerialServer(){
        
        
    }
    public void setDebudMessagesEnable(Boolean DebugMsjEn){
        this.DebugMessages=DebugMsjEn;
    }
    public void setEnableCR(Boolean EnCRin){
        this.EnableCR=EnCRin;
    }
    public void setEnableLF(Boolean EnLFin){
        this.EnableLF=EnLFin;
    }
    public void SetTimeout(int timeout){
        this.TimeOut=timeout;
    }
    

    public void SetJSSC(VSserialPort JSSCIn){
        this.vsSerialExternal=JSSCIn;
    }
    public Boolean GetErrorStatus(){
        return Error;
    }

    
    @Override
    public void run() {
     
        while(true)
         {
             
            try {
                while(vsSerialExternal.getSerialPort().getOutputBufferBytesCount()>0){
                    
                } } catch (SerialPortException ex) {
                Logger.getLogger(SerialServer.class.getName()).log(Level.SEVERE, null, ex);
            }
    
         }
    }
    
    
    
}
