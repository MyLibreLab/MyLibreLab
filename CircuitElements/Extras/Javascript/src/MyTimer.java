
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author salafica 
 */
public class MyTimer implements Runnable {

    public boolean stop = false;
    public Javascript owner;    
    
    
    public MyTimer(Javascript owner) {
        this.owner= owner;
    }

    @Override
    public void run() {

        stop = false;
        while (!stop) { 
            
            //System.out.println("timer...");

            if (owner.tempfile!=null){
              try {
                    String content = Javascript.readFile(owner.tempfile.getAbsolutePath(), StandardCharsets.UTF_8);
                    
                    if (!content.equals(owner.script.getValue())) {
                        owner.script.setValue(content);
                        owner.element.jRepaint();
                    }
                        
                } catch (IOException ex) {
                    Logger.getLogger(Javascript.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            }
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MyTimer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
}
