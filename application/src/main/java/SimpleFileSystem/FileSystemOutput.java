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

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.tinylog.Logger;

/**
 * @author Carmelo Salafia FileSystemOutput ist f�r das schreiben der Datensaetze und ihrer
 *         IndexListe verantwortlich. erzeugt ein kuenstliches Dateisystem!
 */

public class FileSystemOutput implements AutoCloseable {
    // TODO why do we have here a global FileOutputStream?
    private final List<SFileDescriptor> liste = new ArrayList<>();
    private FileOutputStream fos = null;
    private SFileDescriptor oldItem;

    /**
     * Creates a new instance of SimpleFileSystem Erzeugt dabei ein eigenes Filesystem anhand des
     * filename
     */
    public FileSystemOutput(String filename) {
        try {
            fos = new FileOutputStream(new File(filename));

            try (DataOutputStream dos = new DataOutputStream(fos)) {
                dos.writeLong(123479); // platzhalter f�r die Position der IndexListe!

            } catch (IOException ioException) {
                Logger.error(ioException);
            }

        } catch (FileNotFoundException ex) {
            org.tinylog.Logger.error(ex);
            System.out.println("Error in Methode createFile()" + ex.toString());
        }
    }

    /*
     * Fuegt einen Stream Datensatz hinzu und liefert den dazugeh�rigen Stream
     */
    public FileOutputStream addItem(String filename) {
        try {
            SFileDescriptor dt = new SFileDescriptor();

            dt.filename = filename;
            dt.position = fos.getChannel().position();
            dt.size = 0;
            liste.add(dt);

            oldItem = dt;
        } catch (IOException ex) {
            org.tinylog.Logger.error(ex);
            System.out.println("Error in Methode addItem()" + ex.toString());
        }
        return fos;
    }

    /*
     * postItem sorgt daf�r das der datensatz richtig abgeschlossen wird und muss nach jedem neuen
     * Datensatz aufgerufen werden!
     */
    public void postItem() {
        if (oldItem != null) {
            try {
                long position = fos.getChannel().position();
                oldItem.size = position - oldItem.position;
            } catch (IOException ex) {
                org.tinylog.Logger.error(ex);
            }
        }
    }

    /*
     * --- nur f�r interne Zwecke --- schreibt die Liste der FileDiscriptoren am ende der datei!
     */
    public void addIndexList() {
        DataOutputStream dos = new DataOutputStream(fos);

        try {

            long pos = fos.getChannel().position();
            dos.writeLong(liste.size()); // IndexSize!
            for (SFileDescriptor dt : liste) {
                dos.writeByte(dt.filename.length());
                for (int j = 0; j < dt.filename.length(); j++)
                    dos.writeChar(dt.filename.charAt(j));

                dos.writeLong(dt.position);
                dos.writeLong(dt.size);
            }

            fos.getChannel().position(0);
            dos.writeLong(pos);
        } catch (IOException ex) {
            org.tinylog.Logger.error(ex);
            System.out.println("Error in Methode addIndexList()" + ex.toString());
        }
    }

    /*
     * sorgt daf�r das die datei erfolgreich abgeschlossen wird! und muss nachdem alle Datens�tze
     * geschrieben worden sind aufgerufen werden!
     */
    @Override
    public void close() {
        addIndexList();
        try {
            fos.flush();
            fos.close();
        } catch (IOException ex) {
            org.tinylog.Logger.error(ex);
            System.out.println("Error in Methode close()" + ex.toString());
        }
    }
}
