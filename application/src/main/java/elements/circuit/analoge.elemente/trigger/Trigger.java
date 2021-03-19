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

package elements.circuit.analoge.elemente.trigger;/*
                                                                         * Copyright (C) 2020 MyLibreLab
                                                                         * Based on MyOpenLab by Carmelo Salafia
                                                                         * www.myopenlab.de
                                                                         * Copyright (C) 2004 Carmelo Salafia
                                                                         * cswi@gmx.de
                                                                         *
                                                                         * This program is free software: you can
                                                                         * redistribute it and/or modify
                                                                         * it under the terms of the GNU General Public
                                                                         * License as published by
                                                                         * the Free Software Foundation, either version
                                                                         * 3 of the License, or
                                                                         * (at your option) any later version.
                                                                         *
                                                                         * This program is distributed in the hope that
                                                                         * it will be useful,
                                                                         * but WITHOUT ANY WARRANTY; without even the
                                                                         * implied warranty of
                                                                         * MERCHANTABILITY or FITNESS FOR A PARTICULAR
                                                                         * PURPOSE. See the
                                                                         * GNU General Public License for more details.
                                                                         *
                                                                         * You should have received a copy of the GNU
                                                                         * General Public License
                                                                         * along with this program. If not, see
                                                                         * <http://www.gnu.org/licenses/>.
                                                                         *
                                                                         */

import java.awt.*;

import elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VS1DDouble;
import VisualLogic.variables.VSGroup;

public class Trigger extends JVSMain {
    private Image image;
    private VSGroup in;
    private final VSGroup out = new VSGroup();

    private VS1DDouble arrayX = new VS1DDouble(0);
    private VS1DDouble arrayY = new VS1DDouble(0);
    private VS1DDouble arrX = new VS1DDouble(0);
    private VS1DDouble arrY = new VS1DDouble(0);



    public void onDispose() {
        if (image != null) {
            image.flush();
            image = null;
        }
    }

    public void paint(java.awt.Graphics g) {
        drawImageCentred(g, image);
    }


    public void trigger(int schwelle) {
        if (arrayY != null) {
            int idx = 0;
            double val;
            double val2;

            int valueDistance = 2;
            for (int i = 0; i < arrayY.getValue().length - valueDistance; i++) {
                val = arrayY.getValue(i);
                val2 = arrayY.getValue(i + valueDistance);
                if (val < 0.0 && val2 >= 0.0) {
                    idx = i;
                    break;
                }
            }
            if (idx > 0) {

                int c = 0;
                for (int x = idx; x < arrayY.getValue().length; x++) {
                    arrY.setValue(c, arrayY.getValue(x));
                    c++;
                }
                for (int x = arrayY.getValue().length - idx; x < arrayY.getValue().length; x++) {
                    arrY.setValue(x, 0);
                }

            }
        }
    }


    public void init() {
        initPins(0, 1, 0, 1);
        setSize(50, 30);
        element.jSetInnerBorderVisibility(true);
        element.jSetTopPinsVisible(false);
        element.jSetBottomPinsVisible(false);

        image = element.jLoadImage(element.jGetSourcePath() + "icon.gif");


        setPin(0, ExternalIF.C_GROUP, element.PIN_OUTPUT);
        setPin(1, ExternalIF.C_GROUP, element.PIN_INPUT);

        element.jSetPinDescription(0, "Out");
        element.jSetPinDescription(1, "In");

        element.jSetResizable(false);
        element.jSetCaptionVisible(true);
        element.jSetCaption("trigger");
        setName("Trigger");

        out.list.clear();
        out.list.add(arrX);
        out.list.add(arrY);
        element.setPinOutputReference(0, out);
    }



    public void initInputPins() {
        in = (VSGroup) element.getPinInputReference(1);
    }

    public void initOutputPins() {
        element.setPinOutputReference(0, out);
    }


    public void process() {

        if (in != null && in.list.size() == 2) {
            arrayX = (VS1DDouble) in.list.get(0);
            arrayY = (VS1DDouble) in.list.get(1);



            arrX = new VS1DDouble(arrayX.getValue().length);
            arrY = new VS1DDouble(arrayY.getValue().length);

            trigger(0);

            for (int i = 0; i < arrX.getValue().length; i++) {
                arrX.setValue(i, arrayX.getValue(i));
            }

            out.list.clear();
            out.list.add(arrX);
            out.list.add(arrY);
            out.setChanged(true);
            element.notifyPin(0);
        }

    }


}
