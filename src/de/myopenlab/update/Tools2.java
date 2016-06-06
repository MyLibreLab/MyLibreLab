/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools2 | Templates
 * and open the template in the editor.
 */
package de.myopenlab.update;

import VisualLogic.Settings;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
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

    public static void getPackageZip(String urlzip, String destfilename, Settings settings) throws Exception {

        URL url = new URL(urlzip);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        String username = settings.getRepository_login_username();
        String password = settings.getRepository_login_password();

        String userpass = username + ":" + password;
        String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());

        connection.setRequestProperty("Authorization", basicAuth);

        connection.setRequestMethod("GET");
        InputStream in = connection.getInputStream();
        FileOutputStream out = new FileOutputStream(destfilename);

        copy(in, out, 1024);

    }
}
