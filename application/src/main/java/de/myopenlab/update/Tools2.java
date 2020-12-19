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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import VisualLogic.Settings;
import de.myopenlab.update.exception.PackageTransportationException;

/**
 * @author salafica
 */
public class Tools2 {

    public static void deleteFolder(File folder) throws SecurityException {
        File[] files = folder.listFiles();
        if (files != null) { // some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    public static void copy(InputStream input, OutputStream output, int bufferSize) throws IOException {

        byte[] buf = new byte[bufferSize];
        int n = input.read(buf);
        while (n >= 0) {
            output.write(buf, 0, n);
            n = input.read(buf);
        }
        output.flush();
    }

    public static void getPackageZip(String urlzip, String destfilename, Settings settings)
            throws PackageTransportationException {

        try {
            URL url = new URL(urlzip);


            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            String username = settings.getRepository_login_username();
            String password = settings.getRepository_login_password();

            String userpass = username + ":" + password;
            String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());

            connection.setRequestProperty("Authorization", basicAuth);

            connection.setRequestMethod("GET");
            downloadPackage(destfilename, connection);
        } catch (MalformedURLException urlException) {
            throw new PackageTransportationException("Error. URL of repository was mallformed.", urlException);
        } catch (IOException connectionException) {
            throw new PackageTransportationException("Error. Could not open connection or wrong request",
                    connectionException);
        }
    }

    /**
     * Download the package and copy its content to desfilename
     *
     * @param destfilename Where should the package get downlaoded
     * @param connection The connection from where we want to download the package
     * @throws PackageTransportationException If the copy failed
     */
    private static void downloadPackage(String destfilename, HttpURLConnection connection)
            throws PackageTransportationException {
        try (InputStream in = connection.getInputStream(); FileOutputStream out = new FileOutputStream(destfilename)) {
            copy(in, out, 1024);

        } catch (IOException copyException) {
            throw new PackageTransportationException("Error. Trying to copy package failed", copyException);
        }
    }
}
