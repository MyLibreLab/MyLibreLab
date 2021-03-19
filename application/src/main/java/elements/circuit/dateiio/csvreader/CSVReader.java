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

package elements.circuit.dateiio.csvreader;// *****************************************************************************

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.variables.VS1DDouble;
import VisualLogic.variables.VSBoolean;
import VisualLogic.variables.VSGroup;
import VisualLogic.variables.VSString;

public class CSVReader extends JVSMain {
    private Image image;
    private VSString path;
    private VSBoolean read;
    private final VSGroup out = new VSGroup();

    private VS1DDouble outX = new VS1DDouble(10);
    private VS1DDouble outY = new VS1DDouble(10);


    public void onDispose() {
        if (image != null) {
            image.flush();
            image = null;
        }
    }

    public void paint(java.awt.Graphics g) {
        drawImageCentred(g, image);
    }

    public void init() {
        initPins(0, 1, 0, 2);
        setSize(32 + 22, 32 + 4);
        initPinVisibility(false, true, false, true);

        element.jSetInnerBorderVisibility(true);

        setPin(0, ExternalIF.C_GROUP, element.PIN_OUTPUT); // Out
        setPin(1, ExternalIF.C_STRING, element.PIN_INPUT); // Path
        setPin(2, ExternalIF.C_BOOLEAN, element.PIN_INPUT); // Read

        element.jSetPinDescription(0, "Out");
        element.jSetPinDescription(1, "filename");
        element.jSetPinDescription(2, "read");


        String fileName = element.jGetSourcePath() + "icon.gif";
        image = element.jLoadImage(fileName);

        element.jSetCaptionVisible(false);
        element.jSetCaption("CSV Reader");
        setName("CSV Reader");

        out.list.clear();
        out.list.add(outX);
        out.list.add(outY);

    }

    public void initInputPins() {
        path = (VSString) element.getPinInputReference(1);
        read = (VSBoolean) element.getPinInputReference(2);

        if (path == null) path = new VSString();
        if (read == null) read = new VSBoolean(false);
    }


    public void setPropertyEditor() {}

    public void propertyChanged(Object o) {}

    public void initOutputPins() {
        out.list.clear();
        out.list.add(outX);
        out.list.add(outY);

        element.setPinOutputReference(0, out);
    }


    public void start() {

    }


    private String extractX(String line) {
        String ch;
        // gehe bis zum ";" Zeichen
        for (int i = 0; i < line.length(); i++) {
            ch = line.substring(i, i + 1);

            if (ch.equals(";")) {
                return line.substring(0, i);
            }
        }
        return "";
    }

    public void loadFile(String filename) {
        if (new File(filename).exists()) {
            ArrayList listeX = new ArrayList();
            ArrayList listeY = new ArrayList();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
                String str;
                while ((str = br.readLine()) != null) {
                    String xValue = extractX(str);
                    String yValue = str.substring(xValue.length() + 1);


                    listeX.add(xValue);
                    listeY.add(yValue);
                }

                outX = new VS1DDouble(listeX.size());
                outY = new VS1DDouble(listeY.size());

                for (int i = 0; i < listeX.size(); i++) {
                    String x = (String) listeX.get(i);
                    String y = (String) listeY.get(i);

                    x = x.replace(",", ".");
                    y = y.replace(",", ".");

                    // System.out.println("x="+x+" y="+y);

                    outX.setValue(i, Double.parseDouble(x));
                    outY.setValue(i, Double.parseDouble(y));
                }

                out.list.clear();
                out.list.add(outX);
                out.list.add(outY);
                out.setChanged(true);

                br.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }

        } else {
            System.out.println("Datei \" " + filename + " \"+nicht gefunden!");
        }

    }

    public void process() {
        if (read.getValue()) {
            loadFile(path.getValue());
            element.notifyPin(0);
        }
    }



}
