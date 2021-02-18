package com.github.mylibrelab.elements.front.twostring.ausgabe;/*
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
    private int width = 150, height = 25;

    private JTextField text = new JTextField();



    public void processPanel(int pinIndex, double value, Object obj) {
        if (obj instanceof VSString) {
            String str = ((VSString) obj).getValue();
            text.setText(str);
            element.jRepaint();
        }
    }


    public void paint(java.awt.Graphics g) {}


    public void init() {
        initPins(0, 0, 0, 0);
        setSize(width, height);
        element.jSetInnerBorderVisibility(false);
        initPinVisibility(false, false, false, false);

        element.jSetResizable(true);


        setName("Ausgabe2");
    }

    public void xOnInit() {
        JPanel panel = element.getFrontPanel();
        panel.setLayout(new java.awt.BorderLayout());

        panel.add(text, java.awt.BorderLayout.CENTER);
        element.setAlwaysOnTop(true);
        text.setEditable(false);
        // Code added to avoid JText Focus Lost Error
        // text.setEnabled(false);
        text.setFocusable(false);

    }

    public void stop() {
        text.setText("");
    }


}
