/*
 * Copyright (C) 2016 carmelosalafia
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
 */
package VisualLogic;
import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.*;



public class XMLSerializer {
    public static void write(Object f, String filename) throws Exception{
        try (XMLEncoder encoder = new XMLEncoder(
                new BufferedOutputStream(
                        new FileOutputStream(filename)))) {
            encoder.writeObject(f);
        }
    }

    public static Object read(String filename) throws Exception {
        Object o;
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(
                new FileInputStream(filename)))) {
            o = (Object)decoder.readObject();
        }
        return o;
    }
}