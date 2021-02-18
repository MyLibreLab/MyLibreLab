package com.github.mylibrelab.elements.front.twonumerisch.regler.integer.jv;/*
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

public class ReglerPanel extends CustomAnalogComp_1_JV {

    private ExternalIF circuitElement;

    // aus PanelIF
    public void processPanel(int pinIndex, double value, Object obj) {
        setValue(value);
    }

    public void init() {
        super.init();

        nibbleColor.setValue(Color.RED);
        // buttonColor.setValue(new Color(200,150,150));
        max.setValue(100);
        showBackground.setValue(true);
        font.setValue(new Font("Dialog", Font.BOLD, 11));
        generateFromNumbersValues();
        generateFromValuesSubElements();
        captions.setText(getTextWithKomma());
        knobSizeInProzent.setValue(23);
        nibbleLenInProzent.setValue(32);
        nibbleCircleSizeInProzent.setValue(6);
        lineColor.setValue(new Color(255, 242, 181));
        showNibbleAsCircle.setValue(true);
        nibbleColor.setValue(new Color(0, 0, 255));
        setSize(140, 140);
    }


    public void start() {
        super.start();
        circuitElement = element.getCircuitElement();
    }



    public void processProc() {
        int val = (int) Math.round(value0.getValue());
        if (value0.getValue() > (((double) val) - 0.2) && value0.getValue() < ((double) val) + 0.2) {
            value0.setValue(val);
        } else
            value0.setValue(oldValue);

    }


}
