/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools2 | Templates
 * and open the template in the editor.
 */
package de.myopenlab.update;

import VisualLogic.Settings;
import de.myopenlab.update.exception.PackageTransportationException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author salafica
 */
public class Tools2 {

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
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

    public static void getPackageZip(String urlzip, String destfilename, Settings settings) throws PackageTransportationException {

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
            throw new PackageTransportationException("Error. Could not open connection or wrong request", connectionException);
        }
    }

    /**
     * Download the package and copy its content to desfilename
     *
     * @param destfilename Where should the package get downlaoded
     * @param connection   The connection from where we want to download the package
     * @throws PackageTransportationException If the copy failed
     */
    private static void downloadPackage(String destfilename, HttpURLConnection connection) throws PackageTransportationException {
        try (InputStream in = connection.getInputStream();
             FileOutputStream out = new FileOutputStream(destfilename)) {
            copy(in, out, 1024);

        } catch (IOException copyException) {
            throw new PackageTransportationException("Error. Trying to copy package failed", copyException);
        }
    }
}
