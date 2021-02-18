package com.github.mylibrelab.elements.front.twoextras.showimage;/*
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

public class Panel extends JVSMain implements PanelIF {
    private boolean on = false;
    private VSBoolean interpolation = new VSBoolean(false);
    private VSImage24 in;
    private Image img = null;

    public void onDispose() {
        if (img != null) {
            img.flush();
            img = null;
        }
    }

    public void processPanel(int pinIndex, double value, Object obj) {
        in = (VSImage24) obj;

        if (in != null) {
            img = Toolkit.getDefaultToolkit().createImage(
                    new MemoryImageSource(in.getWidth(), in.getHeight(), in.getPixels(), 0, in.getWidth()));
        } else {


        }


    }

    public void paint(java.awt.Graphics g) {
        if (element != null) {
            Rectangle bounds = element.jGetBounds();

            Graphics2D g2 = (Graphics2D) g;

            if (interpolation.getValue() == true) {
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            } else {
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            }



            if (img != null) {
                g2.drawImage(img, bounds.x, bounds.y, bounds.width, bounds.height, null);
            } else {
                g2.setColor(Color.WHITE);
                g2.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }
        }
    }

    public void setPropertyEditor() {
        element.jAddPEItem("Interpolation", interpolation, 0, 0);
        localize();
    }


    private void localize() {
        int d = 6;
        String language;

        language = "en_US";

        element.jSetPEItemLocale(d + 0, language, "Interpolation");

        language = "es_ES";

        element.jSetPEItemLocale(d + 0, language, "Interpolación");
    }

    public void propertyChanged(Object o) {
        element.jRepaint();
    }

    public void init() {
        initPins(0, 0, 0, 0);
        initPinVisibility(false, false, false, false);
        setSize(150, 150);
        element.jSetInnerBorderVisibility(false);

        // element.jSetResizeSynchron(true);
        element.jSetResizable(true);
        setName("Show Image");
    }



    public void loadFromStream(java.io.FileInputStream fis) {
        interpolation.loadFromStream(fis);
    }

    public void saveToStream(java.io.FileOutputStream fos) {
        interpolation.saveToStream(fos);
    }



}
