package de.myopenlab.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeletePackages implements Runnable {

    public frmUpdate owner;

    DeletePackages(frmUpdate aThis) {
        this.owner = aThis;
    }
  

    @Override
    public void run() {
        try {
            try {
                
                for (MyTableRow row : owner.list2) {
                    
                    if (row.isSelected()) {
                        
                        String type=row.getType();
                        String folder = frmUpdate.myopenlabpath + "/Elements/"+type+"/" + row.getName();
                        
                        owner.log("delete=" + folder);
                        Tools2.deleteFolder(new File(folder));
                        
                    }
                }
                
            } catch (Exception ex) {
                Logger.getLogger(frmUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Thread.sleep(1000);
            owner.log("finished");
            //owner.initTable1();
            owner.initTable2();
            owner.owner.reinitPackage();
        } catch (InterruptedException ex) {
            Logger.getLogger(DeletePackages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
