

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author velasquez Javier
 */
public class SocketClient_JV{

    private volatile String BufferSend="";
    private String BufferRTA;
    private Boolean Error;
    private int Timeout=125;
    protected DataOutputStream salidaServidor;
    protected DataInputStream RespuestaServidor;
    private Boolean DebugMessagesEnable=false;
    
    Socket ClientS;
    String BufferIn;
    private int Port = 9876; //Puerto para la conexión
    private String HostIP = "localhost"; //Host para la conexión
 
    
    public void setDebugMsgEnable(Boolean DebugEnIn){
        this.DebugMessagesEnable=DebugEnIn;
    }
    public SocketClient_JV(){

        
    }
    public void setHost(String HostIn){
    this.HostIP=HostIn;    
    }
    public void setPort(int PortIn){
        this.Port=PortIn;
    }
    
    public void setTimeout(int TimeIn){
        this.Timeout=TimeIn;
    }
    public void openSocket() throws SocketException, IOException{
        if(DebugMessagesEnable) System.err.println("CLIENT|OpenningSocket|"+HostIP+"|"+Port+"|");
        ClientS= new Socket(HostIP, Port);
        ClientS.setSoTimeout(Timeout);
        
        //clientSocket.setSoTimeout(500);
    }
    
    public String runOnce(String BufferOut) throws IOException, InterruptedException{
      
      //String sentence = inFromUser.readLine();
      BufferSend="";
      BufferSend=BufferOut;
      //BufferSend=BufferSend.trim();
      salidaServidor = new DataOutputStream(ClientS.getOutputStream());
      RespuestaServidor = new DataInputStream(ClientS.getInputStream());
      RespuestaServidor.readUTF(); // Leer Confirmación
      if(DebugMessagesEnable) System.err.println("CLIENT|SendingMSG:"+BufferSend+"|");
      salidaServidor.writeUTF(BufferSend);
      if(DebugMessagesEnable) System.err.println("CLIENT|Sent_MSJ_OK!|");
      BufferRTA="";
      if(DebugMessagesEnable) System.err.println("CLIENT|Waiting_Server_Response!|");
      BufferRTA = RespuestaServidor.readUTF(); //Mientras haya mensajes desde el cliente
      //Se muestra por pantalla el mensaje recibido
      if(DebugMessagesEnable) System.err.println("CLIENT|Buffer_From_Server:"+BufferRTA+"|");
      Error=false;
      return BufferRTA;
      
    }
    public void closeSocket() throws IOException{
     
    ClientS.close();
          
    }
    
}
