/*
 * Copyright (C) 2017 Javier Velasquez (javiervelasquez125@gmail.com)
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
package VisualLogic;

import VisualLogic.variables.VSserialPort;
import java.util.ArrayList;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 *
 * @author Javier Velasquez
 */
public class NewSerialDriverManager {
   
public static SerialPort serialPortTemp;
public static VSserialPort vsSerialPortTemp;
public static ArrayList <VSserialPort> serialPortsArray;

private static String PortName = "";
private static Thread ThreadIn;

private static int PortsOpened=0;

    public static int getPortsOpened() {
        PortsOpened=serialPortsArray.size();
        for(VSserialPort vsTemp:serialPortsArray){
            int i=0;
            System.out.println(i+" Element: "+vsTemp.getValue().getPortName());
        }
        
        return PortsOpened;
    }

public static FrameMain frameCircuit;

public static void NewDriverManager(FrameMain frameCircuitIn){
frameCircuit=frameCircuitIn;    
PortsOpened=0;
serialPortsArray=new ArrayList<VSserialPort>();   
}
public VSserialPort NewSerialPort(String PortNameIN) throws InterruptedException, Exception{
    VSserialPort vsSerialTemp= new VSserialPort();
        
        serialPortTemp = OpenPort(PortNameIN);
        
        vsSerialPortTemp=new VSserialPort(serialPortTemp);
        if(PortNameExist(PortNameIN)){
        UpdateSerialPortByPortName(vsSerialPortTemp,PortNameIN); // Do not add repeated ports
        }else{
        serialPortsArray.add(vsSerialPortTemp);
        }
        vsSerialTemp.setValue(serialPortTemp);
        
  return vsSerialTemp;
}

public static boolean PortNameExist(String PortNameIN){
    for(VSserialPort vsTemp:serialPortsArray){
        if(vsTemp.getValue().getPortName().equalsIgnoreCase(PortNameIN))
        {
         return true;
        }
    }
    return false;
}

public static VSserialPort FindSerialPortByPortName(String PortNameIN){
    VSserialPort vsSerialTemp= new VSserialPort();
    //System.out.println("Searching Port:"+PortNameIN);
    for(VSserialPort vsTemp:serialPortsArray){
        if(vsTemp.getValue().getPortName().equalsIgnoreCase(PortNameIN))
        {
            vsSerialTemp=vsTemp;
            //System.out.println("Port "+PortNameIN+" Found!");
            return vsSerialTemp;
            
        }
    }
    System.out.println("NewSerialDriverManager.java Line-94 - Port "+PortNameIN+" Not Found!");
    return null;
}
public static void UpdateSerialPortByPortName(VSserialPort vsPortIn, String PortNameIN){
    
    for(VSserialPort vsTemp:serialPortsArray){
        if(vsTemp.getSerialPort().getPortName().equalsIgnoreCase(PortNameIN))
        {
            serialPortsArray.remove(vsTemp);
            serialPortsArray.add(vsPortIn);  
        }
    }
    
}
public static void RemoveSerialPortByPortName(String PortNameIN){
    
    for(VSserialPort vsTemp:serialPortsArray){
        if(vsTemp.getSerialPort().getPortName().equalsIgnoreCase(PortNameIN))
        {
            if(vsTemp.getSerialPort().isOpened()){
                try {
                    vsTemp.getSerialPort().closePort();
                } catch (SerialPortException ex) {
                    //Logger.getLogger(NewSerialDriverManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            serialPortsArray.remove(vsTemp);
            
        }
    }
    
}

//______________________________________________________________________________________________

private SerialPort OpenPort(String PortName) throws SerialPortException, InterruptedException, Exception{
         
         this.PortName=PortName;
         serialPortTemp = new SerialPort(PortName);    
         if(serialPortTemp.isOpened()) serialPortTemp.closePort();
         serialPortTemp.openPort();
            
         return serialPortTemp;
    }  

private void DisposeSerialPort(){
    serialPortTemp.onDispose();
    serialPortTemp=null;
}

}
