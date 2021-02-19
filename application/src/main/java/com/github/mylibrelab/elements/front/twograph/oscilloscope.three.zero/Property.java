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

package com.github.mylibrelab.elements.front.twograph.oscilloscope.three.zero;/*
                                                                               * Copyright (C) 2020 MyLibreLab
                                                                               * Based on MyOpenLab by Carmelo Salafia
                                                                               * www.myopenlab.de
                                                                               * Copyright (C) 2004 Carmelo Salafia
                                                                               * cswi@gmx.de
                                                                               *
                                                                               * This program is free software: you can
                                                                               * redistribute it and/or modify
                                                                               * it under the terms of the GNU General
                                                                               * Public License as published by
                                                                               * the Free Software Foundation, either
                                                                               * version 3 of the License, or
                                                                               * (at your option) any later version.
                                                                               *
                                                                               * This program is distributed in the hope
                                                                               * that it will be useful,
                                                                               * but WITHOUT ANY WARRANTY; without even
                                                                               * the implied warranty of
                                                                               * MERCHANTABILITY or FITNESS FOR A
                                                                               * PARTICULAR PURPOSE. See the
                                                                               * GNU General Public License for more
                                                                               * details.
                                                                               *
                                                                               * You should have received a copy of the
                                                                               * GNU General Public License
                                                                               * along with this program. If not, see
                                                                               * <http://www.gnu.org/licenses/>.
                                                                               *
                                                                               */

import java.util.ArrayList;

import VisualLogic.variables.VSObject;

/**
 *
 * @author Carmelo
 */
public class Property {
    public String name;
    public double min;
    public double max;

    public VSObject vsProperty;

    private ArrayList list = new ArrayList();

    public void addLocation(String language, String text) {
        list.add(new PropertyLanguage(language, text));
    }

    public String getLocation(String language) {
        PropertyLanguage prop;
        for (int i = 0; i < list.size(); i++) {
            prop = (PropertyLanguage) list.get(i);
            if (prop.language.equalsIgnoreCase(language)) {
                return prop.text;
            }
        }

        return "";
    }


}
