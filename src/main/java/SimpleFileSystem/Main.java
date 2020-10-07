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

package SimpleFileSystem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.tinylog.Logger;

/**
 * @author Carmelo Salafia
 */
public class Main {

    /**
     * Creates a new instance of Main
     */
    public Main() {}

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FileSystemOutput fso = new FileSystemOutput("c:/test.dat");



        try (FileOutputStream fos = fso.addItem("Homer")) {
            fos.write(255);
            fos.write(216);
            fos.write(2);
            fos.write(31);
            fso.postItem();

            var addedFile = fso.addItem("Simpson");
            addedFile.write(21);
            addedFile.write(239);
            fso.postItem();
        } catch (IOException ex) {
            org.tinylog.Logger.error(ex);
        }



        FileSystemInput fsIn = new FileSystemInput("c:/test.dat");

        for (int i = 0; i < fsIn.indexListSize(); i++) {
            SFileDescriptor dt = fsIn.getFileDescriptor(i);
            Logger.info("FName =" + dt.filename);
            Logger.info("Pos   =" + dt.position);
            Logger.info("FName =" + dt.filename);
            Logger.info("Size   =" + dt.size);
        }

        FileInputStream fis = fsIn.gotoItem(0);

        fis = fsIn.gotoItem(1);

        try {
            Logger.debug("Value1={}", fis.read());
            Logger.debug("Value2={}", fis.read());
        } catch (IOException ex) {
            org.tinylog.Logger.error(ex);
        }
    }
}
