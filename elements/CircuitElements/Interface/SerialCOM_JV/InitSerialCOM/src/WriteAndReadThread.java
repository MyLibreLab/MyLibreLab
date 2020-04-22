


import java.awt.List;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author velas
 */
class WriteAndReadThread extends Thread{

 public static LinkedList colaMensajeSerial=new LinkedList();
 private MessageSerial Message;
 public WriteAndReadThread(){
     
 }
 @Override
 public void run(){
   while(true){
       
       while(colaMensajeSerial.peekFirst()!=null){
           Message=(MessageSerial)colaMensajeSerial.getFirst();
           System.out.println("MessageReceived");
       }
       
   }  
 }
 
 public class MessageSerial extends Object{
   private String Task;
   private String BufferOut;
   private String BufferIn;
   
 }
}
