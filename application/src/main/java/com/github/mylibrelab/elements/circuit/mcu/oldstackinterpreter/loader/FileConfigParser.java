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

package com.github.mylibrelab.elements.circuit.mcu.oldstackinterpreter.loader;// *****************************************************************************

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 *
 * @author Carmelo
 */
public class FileConfigParser {

    private final ArrayList<String> liste = new ArrayList();
    private int pc = 0;

    public String strName = "";
    public String strProcedure = "";

    public String strInputPins = "";
    public String strOutputPins = "";

    public int[] pinTypes = new int[100];
    public String[] pinDescription = new String[100];

    public final int PIN_NOP = 0;
    public final int PIN_INTEGER = 1;
    public final int PIN_BOOLEAN = 2;
    public final int PIN_DOUBLE = 3;


    private String getBlock() {
        boolean begin = false;
        String str = "";

        while (pc < liste.size()) {
            String inputString = liste.get(pc);

            if (inputString.equalsIgnoreCase("}")) {
                begin = false;
                return str;
            }
            if (begin) {
                str += inputString + "\n";
            }
            if (inputString.equalsIgnoreCase("{")) {
                begin = true;
                str = "";
            }
            pc++;
        }

        return "";
    }

    public int getInteger(String num) {
        try {
            return Integer.parseInt(num);
        } catch (Exception ex) {

        }
        return -1;
    }

    public void showMessage(String message) {
        System.out.println(message);
    }


    private String[] reduceTokens(String[] tokens) {
        ArrayList<String> result = new ArrayList();

        for (int i = 0; i < tokens.length; i++) {
            if (!tokens[i].trim().equalsIgnoreCase("")) {
                result.add(tokens[i].trim());
            }
        }

        String[] res = new String[result.size()];
        for (int i = 0; i < result.size(); i++) {
            res[i] = result.get(i);
        }

        return res;
    }

    private void interp() {
        String inputString;
        pc = 0;

        while (pc < liste.size()) {
            inputString = liste.get(pc);

            String[] tokenX = inputString.split("\\s"); // Trenner ist das leerzeichen

            String[] tokens = reduceTokens(tokenX);

            if (tokens.length > 0) {
                String token = tokens[0].trim();


                if (token.equalsIgnoreCase("PIN_TYPE")) {
                    if (tokens.length == 3) {
                        String strPinIndex = tokens[1];
                        String strType = tokens[2];

                        int pinIndex = getInteger(strPinIndex);

                        if (pinIndex > -1) {
                            int pinType = PIN_NOP; // nicht belegt, also Pin ist frei!
                            if (strType.equalsIgnoreCase("INTEGER"))
                                pinType = PIN_INTEGER;
                            else if (strType.equalsIgnoreCase("BOOLEAN"))
                                pinType = PIN_BOOLEAN;
                            else if (strType.equalsIgnoreCase("DOUBLE"))
                                pinType = PIN_DOUBLE;
                            else {
                                showMessage("PIN_TYPE : 2 Param is not correct!");
                            }
                            pinTypes[pinIndex] = pinType;
                        } else {
                            showMessage("PIN_TYPE : 1 Param is not correct!");
                        }
                    } else {
                        showMessage("PIN_TYPE : 2 Params excepted!");
                    }
                    strName = tokens[1].trim();
                } else if (token.equalsIgnoreCase("PIN_DESC")) {
                    if (tokens.length == 3) {
                        String strPinIndex = tokens[1];
                        String strDescription = tokens[2];

                        int pinIndex = getInteger(strPinIndex);

                        if (pinIndex > -1) {
                            pinDescription[pinIndex] = strDescription;
                        } else {
                            showMessage("PIN_DESC : 1 Param is not correct!");
                        }
                    } else {
                        showMessage("PIN_DESC : 2 Params excepted!");
                    }
                    strName = tokens[1].trim();
                } else if (token.equalsIgnoreCase("NAME")) {
                    strName = tokens[1].trim();
                } else if (token.equalsIgnoreCase("InputPins")) {
                    strInputPins = tokens[1].trim();
                } else if (token.equalsIgnoreCase("OutPutPins")) {
                    strOutputPins = tokens[1].trim();
                } else if (inputString.equalsIgnoreCase("PROCEDURE")) {
                    strProcedure = getBlock();
                }
            }
            pc++;
        }

    }

    public void loadTextFile(File file) {
        if (file.exists()) {
            try {
                BufferedReader input = new BufferedReader(new FileReader(file));
                String inputString;
                while ((inputString = input.readLine()) != null) {
                    if (inputString == null) {
                        break;
                    }
                    inputString = inputString.trim();
                    liste.add(inputString);
                }
                input.close();
            } catch (Exception ex) {
                showMessage(ex.toString());
            }
        }
    }

    public FileConfigParser(String filename) {
        loadTextFile(new File(filename));
        interp();
    }

}
