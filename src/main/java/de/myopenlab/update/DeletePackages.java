package de.myopenlab.update;

import java.io.File;
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

                        String type = row.getType();
                        String folder = frmUpdate.myopenlabpath + "/Elements/" + type + "/" + row.getName();

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
