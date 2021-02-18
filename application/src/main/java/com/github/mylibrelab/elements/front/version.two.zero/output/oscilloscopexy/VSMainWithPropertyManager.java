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

package com.github.mylibrelab.elements.front.version.two.zero.output.oscilloscopexy;// *****************************************************************************

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



public class VSMainWithPropertyManager extends JVSMain {
    private Properties properties = new Properties();
    private VSPropertyManager pManager;

    public void addProp(String propertyName, String language, String text, double min, double max) {
        Property prop;
        prop = new Property();
        prop.name = propertyName;
        prop.addLocation(language, text);
        prop.min = min;
        prop.max = max;
        properties.add(prop);
    }


    public void applyComponent(Component component) {
        pManager = new VSPropertyManager(properties, component);
    }

    public void setPropertyEditor() {
        String language = "DE";
        for (int i = 0; i < properties.size(); i++) {
            Property p = properties.get(i);
            element.jAddPEItem(p.getLocation(language), p.vsProperty, p.min, p.max);
        }
        localize();
    }


    private void localize() {
        int d = 6;
        String language;

        language = "en_US";

        String text;
        for (int i = 0; i < properties.size(); i++) {
            Property p = properties.get(i);
            text = p.getLocation(language);
            if (text.length() == 0) text = p.getLocation("DE");
            element.jSetPEItemLocale(d + i, language, text);
        }

        language = "es_ES";


        for (int i = 0; i < properties.size(); i++) {
            Property p = properties.get(i);
            text = p.getLocation(language);
            if (text.length() == 0) text = p.getLocation("DE");
            element.jSetPEItemLocale(d + i, language, text);
        }
    }

    public void setproperties() {
        for (int i = 0; i < properties.size(); i++) {
            Property p = properties.get(i);

            if (p.vsProperty instanceof VSString) {
                pManager.setMethodValue(p.name, ((VSString) p.vsProperty).getValue());
            } else if (p.vsProperty instanceof VSBoolean) {
                pManager.setMethodValue(p.name, ((VSBoolean) p.vsProperty).getValue());
            } else if (p.vsProperty instanceof VSInteger) {
                pManager.setMethodValue(p.name, ((VSInteger) p.vsProperty).getValue());
            } else if (p.vsProperty instanceof VSDouble) {
                pManager.setMethodValue(p.name, ((VSDouble) p.vsProperty).getValue());
            } else if (p.vsProperty instanceof VSFont) {
                pManager.setMethodValue(p.name, ((VSFont) p.vsProperty).getValue());
            } else if (p.vsProperty instanceof VSColor) {
                pManager.setMethodValue(p.name, ((VSColor) p.vsProperty).getValue());
            }

        }

    }

    public void loadFromStream(java.io.FileInputStream fis) {
        try {

            for (int i = 0; i < properties.size(); i++) {
                Property p = properties.get(i);
                p.vsProperty.loadFromStream(fis);
            }
            setproperties();
        } catch (Exception ex) {
            element.jException(ex.toString());
        }

    }

    public void saveToStream(java.io.FileOutputStream fos) {
        try {
            for (int i = 0; i < properties.size(); i++) {
                Property p = properties.get(i);
                p.vsProperty.saveToStream(fos);
            }

        } catch (Exception ex) {
            element.jException(ex.toString());
        }

    }



}
