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

package VisualLogic.variables;

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class VSImage extends VSObject {
    private Image image;
    private byte[] imageBytes = null;
    private final ImageLoader il = new ImageLoader();

    public VSImage() {}

    public void loadImage(String fileName) {

        image = il.loadImage(Toolkit.getDefaultToolkit().getImage(fileName));

        File f = new File(fileName);
        int fileSize = (int) f.length();
        try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(fileName))) {

            imageBytes = new byte[(int) fileSize];

            input.read(imageBytes);

        } catch (IOException ex) {
            org.tinylog.Logger.error(ex);
        }
    }

    public void loadImage(URL url) {

        image = il.loadImage(Toolkit.getDefaultToolkit().getImage(url));


        // int fileSize = (int) f.length();
        try {
            URLConnection myConnection = url.openConnection();
            int length = myConnection.getContentLength();
            try (BufferedInputStream input = new BufferedInputStream(myConnection.getInputStream())) {
                imageBytes = new byte[length];
                // TODO ignored result here meybe to consume the stream?
                input.read(imageBytes);
            } catch (IOException exception) {
                Logger.error(exception);
            }

        } catch (IOException ex) {
            org.tinylog.Logger.error(ex);
        }
    }

    public Image getImage() {
        return image;
    }

    public void copyValueFrom(Object in) {
        if (in != null) {
            VSImage temp = (VSImage) in;
            image = temp.getImage();
            imageBytes = temp.imageBytes;
            setChanged(temp.isChanged());
        } else
            image = null;
    }

    public void copyReferenceFrom(Object in) {
        copyValueFrom(in);
    }

    public int getWidth() {
        return image.getWidth(il);
    }

    public int getHeight() {
        return image.getHeight(il);
    }

    public void loadFromStream(java.io.FileInputStream fis) {

        try (java.io.DataInputStream dis = new java.io.DataInputStream(fis)) {
            int size = dis.readInt();
            if (size > 0) {
                imageBytes = new byte[size];

                if (image != null) {
                    image.flush();
                }
                fis.read(imageBytes);
                image = java.awt.Toolkit.getDefaultToolkit().createImage(imageBytes);
            } else {
                image = null;
            }
        } catch (IOException ex) {
            org.tinylog.Logger.error(ex);
        }

        il.loadImage(image);
    }

    public void saveToStream(java.io.FileOutputStream fos) {

        java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);

        try {
            if (imageBytes != null) {
                dos.writeInt(imageBytes.length);
                if (imageBytes.length > 0) {
                    fos.write(imageBytes);
                }
            } else {
                dos.writeInt(0);
            }
        } catch (IOException ex) {
            org.tinylog.Logger.error(ex);
        }
    }

    public void loadFromXML(String name, org.w3c.dom.Element nodeElement) {}

    public void saveToXML(String name, org.w3c.dom.Element nodeElement) {}
}


class ImageLoader extends Component {

    public ImageLoader() {

    }

    public Image loadImage(Image img) {
        MediaTracker mc = new MediaTracker(this);
        mc.addImage(img, 0);

        try {
            mc.waitForID(0);
        } catch (InterruptedException ex) {
            org.tinylog.Logger.error(ex);
            Thread.currentThread().interrupt();
        }
        return img;
    }
}
