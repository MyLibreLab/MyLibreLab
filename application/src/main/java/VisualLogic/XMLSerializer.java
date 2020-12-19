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

package VisualLogic;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

import org.tinylog.Logger;

public class XMLSerializer {
    public static void write(Object f, String filename) {
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filename)))) {
            encoder.writeObject(f);
        } catch (IOException ex) {
            Logger.error(ex);
        }
    }

    public static Object read(String filename) throws IOException {
        Object o;
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(filename)))) {
            o = decoder.readObject();
            return o;
        } catch (IOException ex) {
            Logger.error(ex);
            throw new IOException("Could not read from file", ex);
        }
    }
}
