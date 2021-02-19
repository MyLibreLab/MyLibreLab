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

package com.github.mylibrelab.elements.front.twonumerisch.gauge.jv;/*
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
import java.awt.event.MouseEvent;

import com.github.mylibrelab.elements.tools.CustomAnalogComp_2_JV;

import VisualLogic.PanelIF;

public class GaugePanel extends CustomAnalogComp_2_JV implements PanelIF {

    // aus PanelIF
    public void processPanel(int pinIndex, double value, Object obj) {
        setValue(value);
    }

    public void init() {
        super.init();
        // backColor.setValue(Color.WHITE);
        showBackground.setValue(true);
        textInside.setValue(false);
        element.jSetMinimumSize(50, 50);
        knobSizeInProzent.setValue(4);
        nibbleLenInProzent.setValue(25);
        nibbleColor.setPinIndex(1);
        // buttonColor.setValue(new Color(153,153,153));
        font.setValue(new Font("Dialog", Font.BOLD, 11));
        abstand.setValue(7);

    }


    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {

    }

}
