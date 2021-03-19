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

package elements.front.twoextras.scrollimage;/*
                                                                    * Copyright (C) 2020 MyLibreLab
                                                                    * Based on MyOpenLab by Carmelo Salafia
                                                                    * www.myopenlab.de
                                                                    * Copyright (C) 2004 Carmelo Salafia cswi@gmx.de
                                                                    *
                                                                    * This program is free software: you can
                                                                    * redistribute it and/or modify
                                                                    * it under the terms of the GNU General Public
                                                                    * License as published by
                                                                    * the Free Software Foundation, either version 3 of
                                                                    * the License, or
                                                                    * (at your option) any later version.
                                                                    *
                                                                    * This program is distributed in the hope that it
                                                                    * will be useful,
                                                                    * but WITHOUT ANY WARRANTY; without even the implied
                                                                    * warranty of
                                                                    * MERCHANTABILITY or FITNESS FOR A PARTICULAR
                                                                    * PURPOSE. See the
                                                                    * GNU General Public License for more details.
                                                                    *
                                                                    * You should have received a copy of the GNU General
                                                                    * Public License
                                                                    * along with this program. If not, see
                                                                    * <http://www.gnu.org/licenses/>.
                                                                    *
                                                                    */

import java.awt.*;

import javax.swing.*;

public class MyImage extends JPanel {
    private Image image = null;

    public MyImage(Image image) {
        this.image = image;
    }

    public void setImage(Image image) {
        this.image = image;
        int w = image.getWidth(this);
        int h = image.getHeight(this);
        this.setSize(w, h);
        this.setPreferredSize(new Dimension(w, h));
        updateUI();
    }

    public MyImage() {
        this.image = null;
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            int x = getWidth() / 2 - image.getWidth(this) / 2;
            int y = getHeight() / 2 - image.getHeight(this) / 2;
            g.drawImage(image, x, y, null);
        }
    }
}
