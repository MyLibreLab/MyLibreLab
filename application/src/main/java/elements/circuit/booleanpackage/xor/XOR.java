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

package elements.circuit.booleanpackage.xor;// *****************************************************************************

import elements.tools.Gatter;

public class XOR extends Gatter {
    private boolean oldValue;

    public XOR() {
        super(2, "XOR");
    }

    public static boolean XOR(boolean a, boolean b) {
        return ((!a && b) || (a && !b));
    }



    boolean a, b;

    public void process() {

        if (inA != null) {
            a = inA.getValue();
        } else
            a = false;

        if (inB != null) {
            b = inB.getValue();
        } else
            b = false;

        out.setValue(XOR(a, b));
        if (out.getValue() != oldValue) {
            oldValue = out.getValue();
            element.notifyPin(0);
        }

    }

}