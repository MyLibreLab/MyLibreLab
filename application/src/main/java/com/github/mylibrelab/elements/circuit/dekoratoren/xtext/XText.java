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

package com.github.mylibrelab.elements.circuit.dekoratoren.xtext;// *****************************************************************************

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.Locale;

import javax.swing.JOptionPane;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.variables.VSColor;
import VisualLogic.variables.VSFont;
import VisualLogic.variables.VSInteger;
import VisualLogic.variables.VSPropertyDialog;
import VisualLogic.variables.VSString;

public class XText extends JVSMain {
    private final Image image = null;
    private final VSString strText = new VSString();
    private final VSPropertyDialog ausrichtung = new VSPropertyDialog();
    private final VSFont font = new VSFont(new Font("Monospaced", 0, 11));
    private final VSColor fontColor = new VSColor(Color.BLACK);
    private final String[] values = new String[3];
    private final VSInteger ausrichtungIndex = new VSInteger(0);


    public XText() {
        String strLocale = Locale.getDefault().toString();

        if (strLocale.equalsIgnoreCase("de_DE")) {
            strText.setValue("Text");
            values[0] = "Links";
            values[1] = "Mitte";
            values[2] = "Rechts";
        }
        if (strLocale.equalsIgnoreCase("en_US")) {
            strText.setValue("Text");
            values[0] = "Left";
            values[1] = "Center";
            values[2] = "Right";
        }
        if (strLocale.equalsIgnoreCase("es_ES")) {
            strText.setValue("Text");
            values[0] = "Left";
            values[1] = "Center";
            values[2] = "Right";
        }


    }

    public void paint(java.awt.Graphics g) {
        if (element != null) {
            Graphics2D g2 = (Graphics2D) g;

            g2.setColor(fontColor.getValue());
            g2.setFont(font.getValue());
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Rectangle r = element.jGetBounds();
            FontMetrics fm = g2.getFontMetrics(font.getValue());

            int x = r.x;

            int strLen = (int) fm.getStringBounds(strText.getValue(), g).getWidth();

            switch (ausrichtungIndex.getValue()) {
                case 0:
                    x = r.x;
                    break;
                case 1:
                    x = r.width / 2 - strLen / 2;
                    break;
                case 2:
                    x = r.width - strLen;
                    break;
            }

            g2.drawString(strText.getValue(), x, r.y + fm.getHeight());
        }
    }

    public void init() {
        initPins(0, 0, 0, 0);
        setSize(125, 30);
        initPinVisibility(false, false, false, false);
        element.jSetResizable(true);
        element.jSetInnerBorderVisibility(false);


        setName("Text");

        ausrichtung.setText(values[ausrichtungIndex.getValue()]);

    }

    public void setPropertyEditor() {
        element.jAddPEItem("Schriftart", font, 0, 0);
        element.jAddPEItem("Beschreibung", strText, 0, 0);
        element.jAddPEItem("Farbe", fontColor, 0, 0);
        element.jAddPEItem("Ausrichtung", ausrichtung, 0, 0);
        localize();
    }


    private void localize() {
        int d = 6;
        String language;

        language = "en_US";

        element.jSetPEItemLocale(d + 0, language, "Font");
        element.jSetPEItemLocale(d + 1, language, "Text");
        element.jSetPEItemLocale(d + 2, language, "Color");
        element.jSetPEItemLocale(d + 3, language, "Align");

        language = "es_ES";

        element.jSetPEItemLocale(d + 0, language, "Fuente");
        element.jSetPEItemLocale(d + 1, language, "Nombre");
        element.jSetPEItemLocale(d + 2, language, "Color");
        element.jSetPEItemLocale(d + 3, language, "Alineación");
    }

    public void propertyChanged(Object o) {
        if (o.equals(ausrichtung)) {

            String strAuswahl = "Auswahl";
            String strAlign = "Ausrichtung";

            String strLocale = Locale.getDefault().toString();

            if (strLocale.equalsIgnoreCase("de_DE")) {
            }
            if (strLocale.equalsIgnoreCase("en_US")) {
                strAuswahl = "Choice";
                strAlign = "Align";
            }
            if (strLocale.equalsIgnoreCase("es_ES")) {
                strAuswahl = "Choice";
                strAlign = "Align";
            }

            String res = (String) JOptionPane.showInputDialog(null, strAuswahl, strAlign, JOptionPane.QUESTION_MESSAGE,
                    null, values, values[ausrichtungIndex.getValue()]);

            if (res != null) {
                for (int i = 0; i < values.length; i++) {
                    if (values[i].equals(res)) {
                        ausrichtungIndex.setValue(i);
                        break;
                    }
                }
                ausrichtung.setText(values[ausrichtungIndex.getValue()]);
            }

        }
        element.jRepaint();
    }

    public void loadFromStream(java.io.FileInputStream fis) {
        font.loadFromStream(fis);
        strText.loadFromStream(fis);
        fontColor.loadFromStream(fis);
        ausrichtungIndex.loadFromStream(fis);

        ausrichtung.setText(values[ausrichtungIndex.getValue()]);
    }

    public void saveToStream(java.io.FileOutputStream fos) {
        font.saveToStream(fos);
        strText.saveToStream(fos);
        fontColor.saveToStream(fos);
        ausrichtungIndex.saveToStream(fos);

    }
}
