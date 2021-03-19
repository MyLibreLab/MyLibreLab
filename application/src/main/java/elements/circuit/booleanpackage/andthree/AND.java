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

package elements.circuit.booleanpackage.andthree;// *****************************************************************************

import elements.tools.Gatter3;

public class AND extends Gatter3 {
    private boolean oldValue;

    public AND() {
        super(2, "And");
    }


    public void start() {
        super.start();
        oldValue = false;
        out.setValue(false);
        element.notifyPin(0);
    }


    public void process() {
        boolean last = false;
        int c = 0;



        for (int i = 0; i < in.length; i++) {
            if (in[i] != null) {
                last = in[i].getValue();
                if (last == true) {
                    c++;
                }

            }
        }

        boolean value;

        value = c == in.length;



        if (value != oldValue) {

            oldValue = value;
            started = true;
            time1 = System.nanoTime();
        }


    }

    public void processClock() {
        if (started) {
            time2 = System.nanoTime();
            long diff = time2 - time1;
            if (diff >= delay.getValue() * 1000) {
                out.setValue(oldValue);
                element.notifyPin(0);
                started = false;
            }
        }
    }

}
