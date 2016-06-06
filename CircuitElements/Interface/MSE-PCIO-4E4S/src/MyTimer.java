/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author salafica
 */
class MyTimer extends Thread {
 
    public boolean stop = false;
    public PCIO owner;
    public int delay = 100;

    @Override
    public void run() {

        stop = false;
        while (!stop) {
            
            owner.send(new byte[]{'R','I', 'A'});
            
            owner.send(new byte[]{'A','0'});
            owner.send(new byte[]{'A','1'});
            owner.send(new byte[]{'A','2'});
            owner.send(new byte[]{'A','3'});
            
            
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
            }
        }
    }

    
}

