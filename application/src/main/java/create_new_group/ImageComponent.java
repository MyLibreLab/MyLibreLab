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

package create_new_group;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

class ImageComponent extends JPanel {

    private BufferedImage image;
    private String filename = "";

    public void setFilename(String filename) {
        try {
            this.filename = filename;
            File image2 = new File(filename);
            image = ImageIO.read(image2);
        } catch (IOException e) {
            org.tinylog.Logger.error(e);
        }
    }

    public String getFilename() {
        return filename;
    }

    public ImageComponent() {
        super();
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        if (image == null) return;
        g.drawImage(image, 2, 2, this);
    }
}
