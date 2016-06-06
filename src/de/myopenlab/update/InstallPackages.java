package de.myopenlab.update;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import VisualLogic.Tools;
import static VisualLogic.Tools.settings;

public class InstallPackages implements Runnable {

    public frmUpdate owner;

    InstallPackages(frmUpdate aThis) {
        this.owner = aThis;
    }

    @Override
    public void run() {
        try {
            try {

                Path myTempDir = Files.createTempDirectory("myopenlab_");
                // owner.log("create tempdir " + myTempDir.toString());

                for (MyTableRow row : owner.list1) {

                    if (row.isSelected()) {

                        String type = row.getType();

                        
                        String domain=settings.getRepository_domain();
                        
                        String source = domain+"/repository/get_package.php?type="+row.getType()+"&package_name="+row.getName();
                        source=source.replaceAll(" ", "%20");
                        String dest = myTempDir.toString() + "/" + row.getName() + ".zip";

                        owner.log("download " + row.getName() + "/package.zip");

                        try {
                            Tools2.getPackageZip(source, dest, settings);

                            String zipFilePath = dest;

                            String destDir = owner.myopenlabpath + "/Elements/" + type + "/" + row.getName();

                            owner.log("unzip " + row.getName());
                            UnzipFiles unzipper = new UnzipFiles();

                            unzipper.unzip(zipFilePath, destDir);
                        } catch (Exception ex) {
                            // some errors occurred
                            ex.printStackTrace();
                        }

                    }
                }

                // owner.log("delete tempdir " + myTempDir.toString());
                Tools2.deleteFolder(new File(myTempDir.toString()));
            } catch (Exception ex) {
                Logger.getLogger(frmUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
            Thread.sleep(1000);
            owner.log("finished");
            owner.initTable1();
            owner.initTable2();
            owner.owner.reinitPackage();
        } catch (InterruptedException ex) {
            Logger.getLogger(InstallPackages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
