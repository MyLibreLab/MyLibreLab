package de.myopenlab.update;

import org.tinylog.Logger;

import java.io.File;
import java.util.logging.Level;


public class DeletePackages implements Runnable {

    public frmUpdate owner;


    DeletePackages(frmUpdate aThis) {
        this.owner = aThis;
    }

    @Override
    public void run() {
        try {
            deleteEveryFolderFromList();

            Thread.sleep(1000);
            owner.log("finished");
            //owner.initTable1();
            owner.initTable2();
            owner.owner.reinitPackage();
        } catch (InterruptedException ex) {
            Logger.error(ex,"Exception while trying to sleep in thread");
        }
    }

    /**
     * Trys to delete every folder in owner#list2
     */
    private void deleteEveryFolderFromList() {
        try {

            for (MyTableRow row : owner.list2) {

                if (row.isSelected()) {

                    String type = row.getType();
                    String folder = frmUpdate.myopenlabpath + "/Elements/" + type + "/" + row.getName();

                    owner.log("delete=" + folder);
                    Tools2.deleteFolder(new File(folder));
                }
            }
        } catch (SecurityException ex) {
            Logger.error(ex,"Error during deletion of packages");
        }
    }
}
