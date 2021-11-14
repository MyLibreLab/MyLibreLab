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

package com.github.mylibrelab.elements.front.output.scrollmage;// *****************************************************************************

// * Element of MyOpenLab Library *
// * *
// * Copyright (C) 2004 Carmelo Salafia (cswi@gmx.de) *
// * *
// * This library is free software; you can redistribute it and/or modify *
// * it under the terms of the GNU Lesser General Public License as published *
// * by the Free Software Foundation; either version 2.1 of the License, *
// * or (at your option) any later version. *
// * http://www.gnu.org/licenses/lgpl.html *
// * *
// * This library is distributed in the hope that it will be useful, *
// * but WITHOUTANY WARRANTY; without even the implied warranty of *
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. *
// * See the GNU Lesser General Public License for more details. *
// * *
// * You should have received a copy of the GNU Lesser General Public License *
// * along with this library; if not, write to the Free Software Foundation, *
// * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA *
// *****************************************************************************


import java.awt.*;

import javax.swing.*;

public class MyImage extends JPanel {
    private Image image = null;

    public MyImage(Image image) {
        if (image != null) {
            this.image = image;
        }

    }

    public MyImage() {
        this.image = null;
    }

    public void setImage(Image image) {
        try {
            this.image = image;
            int w = image.getWidth(this);
            int h = image.getHeight(this);
            this.setSize(w, h);
            this.setPreferredSize(new Dimension(w, h));
            updateUI();
        } catch (Exception e) {

        }
    }

    public void paintComponent(Graphics g) {
        try {
            super.paintComponent(g);
            if (image != null) {

                int x = getWidth() / 2 - image.getWidth(this) / 2;
                int y = getHeight() / 2 - image.getHeight(this) / 2;
                g.drawImage(image, x, y, null);

            }
        } catch (Exception e) {

        }
    }
}
