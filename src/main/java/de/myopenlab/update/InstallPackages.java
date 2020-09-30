package de.myopenlab.update;

import de.myopenlab.update.exception.PackageTransportationException;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


import static VisualLogic.Tools.settings;

public class InstallPackages implements Runnable {

    public static final String MYOPENLAB_ = "myopenlab_";
    public frmUpdate owner;

    InstallPackages(frmUpdate aThis) {
        this.owner = aThis;
    }

    @Override
    public void run() {
        try {
            try {

                Path myTempDir = Files.createTempDirectory(MYOPENLAB_);
                // owner.log("create tempdir " + myTempDir.toString());

                for (MyTableRow row : owner.list1) {

                    if (row.isSelected()) {

                        String type = row.getType();

                        String domain = settings.getRepository_domain();

                        String source = domain + "/repository/get_package.php?type=" + row.getType() + "&package_name=" + row.getName();
                        source = source.replaceAll(" ", "%20");
                        String dest = myTempDir.toString() + "/" + row.getName() + ".zip";

                        owner.log("download " + row.getName() + "/package.zip");

                        unzipPackageToDestination(row, type, source, dest);
                    }
                }

                // owner.log("delete tempdir " + myTempDir.toString());
                Tools2.deleteFolder(new File(myTempDir.toString()));
            } catch (IOException ex) {
                Logger.error(ex,"Could not create temp directory {}", MYOPENLAB_);

            }
            Thread.sleep(1000);
            owner.log("finished");
            owner.initTable1();
            owner.initTable2();
            owner.owner.reinitPackage();
        } catch (InterruptedException ex) {
            Logger.error(ex,"Error. Tried to sleep thread");
        }
    }

    /**
     * Unzip a package to a defined location
     * @param row
     * @param type
     * @param source
     * @param dest
     */
    private void unzipPackageToDestination(MyTableRow row, String type, String source, String dest) {
        try {
            Tools2.getPackageZip(source, dest, settings);

            String zipFilePath = dest;

            String destDir = owner.myopenlabpath + "/Elements/" + type + "/" + row.getName();

            owner.log("unzip " + row.getName());
            UnzipFiles unzipper = new UnzipFiles();

            unzipper.unzip(zipFilePath, destDir);
        } catch (IOException | PackageTransportationException ex) {
            Logger.error(ex,"Error while trying to unzip package {} to {}",source,dest);
        }
    }
}
