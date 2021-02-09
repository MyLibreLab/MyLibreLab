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

package com.github.mylibrelab.elements.circuit.Boolean.AND2.src;// *****************************************************************************

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



public class AND extends Gatter2 {

    private boolean oldValue;

    public AND() {
        super(2, "And");
    }


    public void start() {
        oldValue = false;
        out.setValue(false);
        element.notifyPin(0);
    }


    public void process() {
        boolean last = false;
        int c = 0;

        for (int i = 0; i < in.length; i++) {
            last = in[i].getValue();
            if (last == true) {
                c++;
            }
        }

        boolean value;

        if (c == in.length) {
            value = true;
        } else {
            value = false;
        }

        if (value != oldValue) {
            System.out.println("AND=" + value);
            oldValue = value;
            out.setValue(value);
            element.notifyPin(0);
        }


    }


}
