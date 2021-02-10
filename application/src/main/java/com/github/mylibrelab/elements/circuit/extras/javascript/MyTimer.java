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

package com.github.mylibrelab.elements.circuit.Extras.Javascript.src;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author salafica
 */
public class MyTimer implements Runnable {

    public boolean stop = false;
    public Javascript owner;


    public MyTimer(Javascript owner) {
        this.owner = owner;
    }

    @Override
    public void run() {

        stop = false;
        while (!stop) {

            // System.out.println("timer...");

            if (owner.tempfile != null) {
                try {
                    String content = Javascript.readFile(owner.tempfile.getAbsolutePath(), StandardCharsets.UTF_8);

                    if (!content.equals(owner.script.getValue())) {
                        owner.script.setValue(content);
                        owner.element.jRepaint();
                    }

                } catch (IOException ex) {
                    Logger.getLogger(Javascript.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MyTimer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
}
