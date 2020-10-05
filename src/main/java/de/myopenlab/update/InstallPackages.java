/*
 * Copyright (C) 2020 MyLibreLab
 * Based on MyOpenLab by Carmelo Salafia www.myopenlab.de
 * Copyright (C) 2004  Carmelo Salafia cswi@gmx.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.myopenlab.update;

import static VisualLogic.Tools.settings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.tinylog.Logger;

import de.myopenlab.update.exception.PackageTransportationException;

public class InstallPackages implements Runnable {

    public static final String MYOPENLAB_ = "myopenlab_";
    public FrmUpdate owner;

    InstallPackages(FrmUpdate aThis) {
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

                        String source = domain + "/repository/get_package.php?type=" + row.getType() + "&package_name="
                                + row.getName();
                        source = source.replaceAll(" ", "%20");
                        String dest = myTempDir.toString() + "/" + row.getName() + ".zip";

                        owner.log("download " + row.getName() + "/package.zip");

                        unzipPackageToDestination(row, type, source, dest);
                    }
                }

                // owner.log("delete tempdir " + myTempDir.toString());
                Tools2.deleteFolder(new File(myTempDir.toString()));
            } catch (IOException ex) {
                Logger.error(ex, "Could not create temp directory {}", MYOPENLAB_);

            }
            Thread.sleep(1000);
            owner.log("finished");
            owner.initTable1();
            owner.initTable2();
            owner.owner.reinitPackage();
        } catch (InterruptedException ex) {
            Logger.error(ex, "Error. Tried to sleep thread");
        }
    }

    /**
     * Unzip a package to a defined location
     *
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
            Logger.error(ex, "Error while trying to unzip package {} to {}", source, dest);
        }
    }
}
