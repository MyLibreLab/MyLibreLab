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

package elements.circuit.extras.fontconstpackage;// *****************************************************************************

import java.awt.Font;
import java.awt.Image;

import elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VSFont;

public class FontConst extends JVSMain {
    private Image image;

    public VSFont out = new VSFont(new Font("Arial", Font.PLAIN, 10));
    public VSFont font = new VSFont(new Font("Arial", Font.PLAIN, 10));

    private final boolean changed = false;

    public void onDispose() {
        if (image != null) {
            image.flush();
            image = null;
        }
    }

    public void paint(java.awt.Graphics g) {
        if (image != null) drawImageCentred(g, image);
    }


    public void init() {
        initPins(0, 1, 0, 0);
        setSize(45, 34);
        initPinVisibility(false, true, false, false);

        String fileName = element.jGetSourcePath() + "icon.gif";
        image = element.jLoadImage(fileName);

        setPin(0, ExternalIF.C_FONT, element.PIN_OUTPUT);

        element.jSetPinDescription(0, "out");

        setName("Font");
    }



    public void setPropertyEditor() {
        element.jAddPEItem("Font", font, 0, 0);
        localize();
    }

    private void localize() {
        int d = 6;
        String language;

        language = "en_US";

        element.jSetPEItemLocale(d + 0, language, "Font");

        language = "es_ES";

        element.jSetPEItemLocale(d + 0, language, "Font");
    }

    public void propertyChanged(Object o) {
        element.jRepaint();
    }

    public void initInputPins() {}

    public void initOutputPins() {
        element.setPinOutputReference(0, out);
    }

    public void start() {
        out.setValue(font.getValue());
        out.setChanged(true);
        element.notifyPin(0);
    }

    public void process() {}


    public void loadFromStream(java.io.FileInputStream fis) {
        font.loadFromStream(fis);
    }

    public void saveToStream(java.io.FileOutputStream fos) {
        font.saveToStream(fos);
    }


}
