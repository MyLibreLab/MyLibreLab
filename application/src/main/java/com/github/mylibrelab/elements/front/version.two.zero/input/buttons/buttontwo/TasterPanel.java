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

package com.github.mylibrelab.elements.front.version.two.zero.input.buttons.buttontwo;// *****************************************************************************

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



public class TasterPanel extends JVSMain {
    private boolean value = false;
    private int sizeW = 70;
    private int sizeH = 30;
    private VSBoolean down = new VSBoolean(false);;
    private VSBoolean initValue = new VSBoolean(false);
    private boolean oldDown = false;
    private javax.swing.JButton button = new javax.swing.JButton();

    private ExternalIF circuitElement;

    private VSString caption = new VSString();
    private VSFont font = new VSFont(new Font("Arial", 1, 10));
    private VSColor fontColor = new VSColor(Color.BLACK);
    private VSColor backColor = new VSColor(Color.LIGHT_GRAY);



    public void init() {
        initPins(0, 0, 0, 0);
        setSize(sizeW, sizeH);
        initPinVisibility(false, false, false, false);

        element.jSetResizable(true);
        element.jSetInnerBorderVisibility(false);
        setName("Taster");
        caption.setValue("OK");

    }

    public void xOnInit() {
        JPanel panel = element.getFrontPanel();
        panel.setLayout(new java.awt.BorderLayout());

        panel.add(button, java.awt.BorderLayout.CENTER);
        element.setAlwaysOnTop(true);


        button.setText(caption.getValue());


        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                down.setValue(true);
                circuitElement.Change(0, down);
            }
        });

        /*
         * button.addMouseListener(new java.awt.event.MouseAdapter() {
         * public void mousePressed(java.awt.event.MouseEvent evt) {
         *
         * down.setValue(true);
         * circuitElement.Change(0,down);
         * }
         * public void mouseReleased(java.awt.event.MouseEvent evt) {
         * down.setValue(false);
         * circuitElement.Change(0,down);
         * }
         * });
         */

    }



    public void initInputPins() {}

    public void initOutputPins() {}


    public void start() {
        /*
         * down.setValue(initValue.getValue());
         * element.jRepaint();
         *
         * circuitElement=element.getCircuitElement();
         * down.setValue(false);
         * circuitElement.Change(0,down);
         */

        element.jRepaint();
        circuitElement = element.getCircuitElement();
        down.setValue(false);
    }



    public void setPropertyEditor() {
        element.jAddPEItem("Schriftart", font, 0, 0);
        element.jAddPEItem("Beschriftung", caption, 0, 0);
        element.jAddPEItem("Font Color", fontColor, 0, 0);
        element.jAddPEItem("Hintergrund-Farbe", backColor, 0, 0);
        // element.jAddPEItem("Anfangs-Wert",initValue, 0,0);

        localize();
    }


    private void localize() {
        int d = 6;
        String language;

        language = "en_US";

        element.jSetPEItemLocale(d + 0, language, "Font");
        element.jSetPEItemLocale(d + 1, language, "Caption");
        element.jSetPEItemLocale(d + 2, language, "Font Color");
        element.jSetPEItemLocale(d + 3, language, "Background Color");
        element.jSetPEItemLocale(d + 4, language, "Switch");
        element.jSetPEItemLocale(d + 5, language, "Init-Value");

        language = "es_ES";

        element.jSetPEItemLocale(d + 0, language, "Font");
        element.jSetPEItemLocale(d + 1, language, "Caption");
        element.jSetPEItemLocale(d + 2, language, "Font Color");
        element.jSetPEItemLocale(d + 3, language, "Background Color");
        element.jSetPEItemLocale(d + 4, language, "Switch");
        element.jSetPEItemLocale(d + 5, language, "Init-Value");
    }

    public void propertyChanged(Object o) {
        // down.setValue(initValue.getValue());

        button.setFont(font.getValue());
        button.setText(caption.getValue());
        button.setForeground(fontColor.getValue());
        button.setBackground(backColor.getValue());
    }



    public void loadFromStream(java.io.FileInputStream fis) {
        initValue.loadFromStream(fis);
        font.loadFromStream(fis);
        caption.loadFromStream(fis);
        fontColor.loadFromStream(fis);
        backColor.loadFromStream(fis);

        down.setValue(initValue.getValue());
        button.setText(caption.getValue());
    }

    public void saveToStream(java.io.FileOutputStream fos) {
        initValue.saveToStream(fos);
        font.saveToStream(fos);
        caption.saveToStream(fos);
        fontColor.saveToStream(fos);
        backColor.saveToStream(fos);
    }
}
