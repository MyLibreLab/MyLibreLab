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

import java.io.File;

import org.tinylog.Logger;


public class DeletePackages implements Runnable {

    public FrmUpdate owner;


    DeletePackages(FrmUpdate aThis) {
        this.owner = aThis;
    }

    @Override
    public void run() {
        try {
            deleteEveryFolderFromList();

            Thread.sleep(1000);
            owner.log("finished");
            // owner.initTable1();
            owner.initTable2();
            owner.owner.reinitPackage();
        } catch (InterruptedException ex) {
            Logger.error(ex, "Exception while trying to sleep in thread");
            Thread.currentThread().interrupt();
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
                    String folder = FrmUpdate.myopenlabpath + "/Elements/" + type + "/" + row.getName();

                    owner.log("delete=" + folder);
                    Tools2.deleteFolder(new File(folder));
                }
            }
        } catch (SecurityException ex) {
            Logger.error(ex, "Error during deletion of packages");
        }
    }
}
