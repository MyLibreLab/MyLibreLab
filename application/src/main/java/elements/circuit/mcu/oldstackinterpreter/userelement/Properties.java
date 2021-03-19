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

package elements.circuit.mcu.oldstackinterpreter.userelement;// *****************************************************************************

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Properties extends JDialog {
    private final JScrollPane scroll = new JScrollPane();
    private final JTextArea text = new JTextArea();
    private final JButton cmdOK = new JButton("OK");
    private final JButton cmdCancel = new JButton("Abbrechen");
    private final JPanel panel = new JPanel();
    public static boolean result = false;
    public static String strText = "";

    public Properties(JFrame frame, String input) {
        super(frame, true);
        this.setLayout(new java.awt.BorderLayout());

        strText = input;

        scroll.add(text);
        scroll.setViewportView(text);

        getContentPane().add(scroll, java.awt.BorderLayout.CENTER);
        getContentPane().add(panel, java.awt.BorderLayout.SOUTH);

        panel.add(cmdOK);
        panel.add(cmdCancel);

        text.setText(input);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        cmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                result = true;
                strText = text.getText();
                dispose();
            }
        });

        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                result = false;
                dispose();
            }
        });

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int mitteX = (int) screenSize.getWidth() / 2;
        int mitteY = (int) screenSize.getHeight() / 2;

        setLocation(mitteX - getWidth() / 2 - 200, mitteY - getHeight() / 2 - 200);



    }

}
