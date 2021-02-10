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

package com.github.mylibrelab.elements.front.Version_2_0.Output.Gauge.src;// *****************************************************************************



public class GaugePanel extends CustomAnalogComp2 implements PanelIF {

    // aus PanelIF
    public void processPanel(int pinIndex, double value, Object obj) {
        setValue(value);
    }

    public void init() {
        super.init();
        backColor.setValue(Color.WHITE);
        showBackground.setValue(true);
        textInside.setValue(true);
        element.jSetMinimumSize(50, 50);
        knobSizeInProzent.setValue(3);
        nibbleLenInProzent.setValue(45);
        nibbleColor.setValue(Color.RED);
        buttonColor.setValue(Color.BLACK);
    }


    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {

    }

}
