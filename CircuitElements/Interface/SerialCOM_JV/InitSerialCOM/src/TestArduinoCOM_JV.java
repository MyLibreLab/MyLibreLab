/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author velas
 */


import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

public class TestArduinoCOM_JV {
    
public static void main(String[] args) throws SerialPortException, InterruptedException, SerialPortTimeoutException, Exception {
        //SerialPort serialPort = new SerialPort("/dev/ttyACM0");
     String javaLibPath = System.getProperty("java.library.path");
     System.out.println(javaLibPath);
     
     ArduinoJSSC_JV ComArduino=new ArduinoJSSC_JV();
     ArduinoJSSC_JV ComArduino2=new ArduinoJSSC_JV();
     
     ComArduino.OpenPort("COM3");
     ComArduino.setDefaultParameters(ComArduino.ArduinoAutoResetON,Thread.currentThread());
     
     ComArduino.WriteString("FUNCIONA!;\n");
     
     System.out.println("RESPONSE 1: "+ComArduino.ReadFromPort(Thread.currentThread(),50));
     
     ComArduino.WriteString("FUNCIONA!;\n");
     
     System.out.println("RESPONSE 2: "+ComArduino.ReadBytesFromPort(12,50));
     
     ComArduino.WriteString("FUNCIONA!;\n");
     
     System.out.println("RESPONSE 3: "+ComArduino.ReadBytesFromPort(13,50));
     
     ComArduino.ClosePort();
    }
    
}
