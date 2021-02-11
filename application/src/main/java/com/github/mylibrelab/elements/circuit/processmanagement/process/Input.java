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

package com.github.mylibrelab.elements.circuit.processmanagement.process;// *****************************************************************************

/**
 *
 * Beschreibung
 *
 * @version 1.0 vom 30.11.2006
 * @author
 */

public class Input extends JDialog {
    // Anfang Variablen
    private final Button button1 = new Button();
    private final Panel checkboxGroup1Panel = new Panel();
    private final CheckboxGroup checkboxGroup1 = new CheckboxGroup();
    private final Button button2 = new Button();
    private final TextField textField1 = new TextField();
    private final Label label1 = new Label();
    public static String result = "";

    // Ende Variablen

    public Input(java.awt.Frame parent, boolean modal) {
        // Frame-Initialisierung
        super(parent, modal);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            }
        });
        int frameWidth = 300;
        int frameHeight = 130;
        setSize(frameWidth, frameHeight);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (d.width - getSize().width) / 2;
        int y = (d.height - getSize().height) / 2;
        setLocation(x, y);
        Panel cp = new Panel(null);
        add(cp);
        // Anfang Komponenten

        button1.setBounds(208, 72, 75, 25);
        button1.setLabel("Cancel");
        cp.add(button1);
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        button2.setBounds(128, 72, 75, 25);
        button2.setLabel("OK");
        cp.add(button2);
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                button2ActionPerformed(evt);
            }
        });

        textField1.setBounds(8, 40, 273, 24);
        textField1.setText("");
        cp.add(textField1);
        label1.setBounds(8, 16, 117, 16);
        label1.setText("Relative Path to VM");
        label1.setFont(new Font("MS Sans Serif", Font.PLAIN, 13));
        cp.add(label1);
        // Ende Komponenten

        setResizable(false);
        setVisible(true);
    }

    // Anfang Ereignisprozeduren
    public void button1ActionPerformed(ActionEvent evt) {
        dispose();
    }

    public void button2ActionPerformed(ActionEvent evt) {
        result = textField1.getText();
        dispose();
    }

    // Ende Ereignisprozeduren

    public static void main(String[] args) {
        new Input(null, true);
    }
}
