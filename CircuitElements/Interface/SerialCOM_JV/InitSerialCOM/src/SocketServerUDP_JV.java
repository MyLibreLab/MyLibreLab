
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import jssc.SerialPortException;
import java.net.Socket;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author velasquez Javier
 */
public class SocketServerUDP_JV implements Runnable {
    
    public ServerSocket serverSocket;
    public Socket socketOut = new Socket();
    public Boolean Error=false;
    private ArduinoJSSC_JV JSSC_External;
    private Boolean EnableCR=false;
    private Boolean EnableLF=false;
    private Boolean DebugMessages=false;
    private int TimeOut=125;
    private int Port=9876;

    volatile String BufferIn="";
    volatile String COMResponseStr="";
    volatile DatagramPacket sendPacket;
    volatile DatagramPacket receivePacket;
    protected DataOutputStream salidaCliente;
    protected DataInputStream RespuestaCliente;
    BufferedReader entradaCliente;
    
    
   InetAddress IPAddress;
    
    public SocketServerUDP_JV(){
        
        
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
    
    public void SetPort(int PortIn){
        this.Port=PortIn;
    }
    public void SetJSSC(ArduinoJSSC_JV JSSCIn){
        this.JSSC_External=JSSCIn;
    }
    public Boolean GetErrorStatus(){
        return Error;
    }
    public void OpenSocket() throws IOException{
        
        serverSocket = new ServerSocket(Port);
        IPAddress =InetAddress.getLocalHost();
        Error=false;
        if(DebugMessages) System.err.println("SERVER|SOCKET_SERVER_Opened_at_Port:"+Port);
        
    }
    public void CloseSocket() throws IOException{
       if(serverSocket.isClosed()==false){
          serverSocket.close();
       } 
    }
    
    @Override
    public void run() {
     
        while(true)
         {
          try {  
                  socketOut=serverSocket.accept(); // Esperar Conección
                  if(DebugMessages) System.err.println("SERVER|SOCKET_SERVER_INCOMMING_CNX");
                  Error=false;
                  RespuestaCliente=new DataInputStream(socketOut.getInputStream());
                  salidaCliente = new DataOutputStream(socketOut.getOutputStream());
                  //entradaCliente = new BufferedReader(new InputStreamReader(socketOut.getInputStream()));
                  BufferIn="";
                  salidaCliente.writeUTF("200_OK"); //Send Confirmation
                  BufferIn=RespuestaCliente.readUTF();
                  if(DebugMessages) System.err.println("SERVER|"+BufferIn.length()+"|RECEIVED: " + BufferIn+"|");
                  JSSC_External.SetEnableCR(EnableCR);
                  JSSC_External.SetEnableLF(EnableLF);
                  JSSC_External.WriteString(BufferIn);
                  //Thread.sleep(150);
                  COMResponseStr="";
                  COMResponseStr = JSSC_External.ReadFromPort(Thread.currentThread(), TimeOut);
                  if(COMResponseStr==null) COMResponseStr="ERROR";
                  salidaCliente.writeUTF(COMResponseStr);
                  if(DebugMessages) System.err.println("SERVER|RespuestaEnviada"+COMResponseStr+"|");
                  Error=false;
                  
               }
         catch (SocketException ex) {
            //Logger.getLogger(SocketServerUDP_JV.class.getName()).log(Level.SEVERE, null, ex);
            Error=true;
        } catch (IOException E){
            Error=true;
        }
        catch (SerialPortException E){
            Error=true;
        }
        catch (InterruptedException E){
            Error=true;
        }
        catch (Exception E){
            Error=true;
        }
      }
    }
    
    
}
