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

package com.github.mylibrelab.elements.tools;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.*;

import VisualLogic.VSBasisIF;
import VisualLogic.variables.VSString;

public class MCUMainFlow extends JVSMain {
    public static int ONLY_VAR = 1;
    public static int SIMPLE = 2;
    public static int VAR_OR_CONST = 3;
    public VSString variable = new VSString("i");
    public Font font = new Font("Courier", 0, 12);
    public JTextField text = new JTextField();
    public JPanel panel = null;
    public Graphics2D g2;
    public int standardWidth = 130;
    public int width = 130;
    public int height = 53;
    public String toInclude = "";

    public MCUMainFlow() {

    }


    public String generateMCUCodeFromExpression(String param) {
        String result = "";

        Expression parser = new Expression();
        Expression.code.clear();
        java.util.Scanner scanner = new Scanner(element, new java.io.StringReader(param));
        while (scanner.ttype != scanner.TT_EOF)
            try {
                parser.yyparse(scanner, null);

                for (int i = 0; i < Expression.code.size(); i++) {
                    result += Expression.code.get(i) + "\n";
                }
                break;

            } catch (Exception ye) {
                System.err.println("Error in Element_" + element.jGetID() + "\"" + element.jGetCaption() + "\" "
                        + scanner + ": " + ye);
            }

        return result;
    }

    public void drawImageLeftAlign(java.awt.Graphics g, Image image, int left) {
        int y = 2;
        g.drawImage(image, left, y, null);
    }

    public void drawCaption(java.awt.Graphics g, String caption, int leftRightDistance) {

        variable.setValue(caption);

        Rectangle bounds = element.jGetBounds();

        g.setFont(font);

        int mitteX = bounds.x + (bounds.width) / 2;
        int mitteY = bounds.y + (bounds.height) / 2;

        FontMetrics fm = g.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(caption, g);

        g.setColor(Color.BLACK);
        g.drawString(caption, mitteX - (int) (r.getWidth() / 2), (mitteY + fm.getHeight() / 2) - 3);


        resizeWidth(g, leftRightDistance);
    }

    public void paint(java.awt.Graphics g) {
        if (element != null) {
            g2 = (Graphics2D) g;
            Rectangle bounds = element.jGetBounds();

            if (text != null) {
                int mitteX = bounds.x + (bounds.width) / 2;
                int mitteY = bounds.y + (bounds.height) / 2;

                int distanceY = 12;
                text.setLocation(bounds.x + 5, mitteY - distanceY);
                text.setSize(bounds.width - 10, 2 * distanceY);
            }
        }
    }

    public String checkProperty(String name, String value, int type) {
        if (type == SIMPLE) {
            return "";
        }
        if (type == VAR_OR_CONST) {
            if (isConst(value)) {
                return "";
            } else if (isVariable(value)) {
                return "";
            } else {
                return "no Variable or illegale const \"" + value + "\" in '" + name + "'\n";
            }
        }
        if (type == ONLY_VAR) {
            if (isVariable(value)) {
                return "";
            } else {
                return "illegale Variable \"" + value + "\" in '" + name + "'\n";
            }
        }

        return "";
    }

    // Überprüft ob der String eine Zahl von -32... + 32... (16 Bit)
    public boolean isConst(String value) {
        try {
            int num = Integer.valueOf(value);
            if (num >= -32768 && num <= 32767) {
                return true;
            }
        } catch (Exception ex) {
        }
        return false;
    }

    // Überprüft ob der String als Variable definiert worden ist
    public boolean isVariable(String value) {
        VSBasisIF basis = element.jGetBasis();

        if (basis != null) {
            int varDT = basis.vsGetVariableDT(value);
            return varDT > -1;
        }
        // oder auch nicht!
        return false;
    }

    public void resizeWidth(Graphics g2, int leftRightDistance) {
        if (g2 != null) {
            g2.setFont(font);
            String caption = variable.getValue();

            FontMetrics fm = g2.getFontMetrics();
            Rectangle2D r = fm.getStringBounds(caption, g2);
            // Rectangle2D r2 = fm.getStringBounds(toInclude,g2);

            int newWidth = (int) (r.getWidth() + 20 + leftRightDistance);
            if (newWidth == standardWidth) return;

            if (newWidth <= standardWidth) {
                newWidth = standardWidth;
            }

            if (element != null) {

                int mx = (element.jGetWidth() / 2) + element.jGetLeft();

                int newX = mx - (newWidth / 2);

                if (newWidth != width) {
                    element.jSetSize(newWidth, height);
                    element.jSetLeft(newX);

                    width = newWidth;
                    element.jRefreshVM();
                }
            }
        }
    }

    public void mousePressedOnIdle(MouseEvent e) {
        /*
         * if (e!=null && panel!=null)
         * {
         * if (e.getClickCount()==2)
         * {
         * SwingUtilities.invokeLater(new Runnable()
         * {
         * public void run()
         * {
         * panel.remove(text);
         * panel.add(text,0);
         * text.setText(variable.getValue());
         * text.setVisible(true);
         * text.updateUI();
         * element.jRepaint();
         *
         * text.requestFocus();
         * }
         * });
         *
         * }
         * }
         *
         * if (panel==null)
         * {
         * System.out.println("PANEL == NULL!!");
         * }
         */

    }

    public void xOnInit() {
        panel = element.getFrontPanel();
        panel.setLayout(null);

        panel.add(text);

        text.setFont(font);
        text.setText(variable.getValue());

        text.setVisible(false);
        // element.setAlwaysOnTop(true);


        text.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextKeyPressed(evt);
            }
        });

        text.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFocusLost(evt);
            }
        });

    }

    public void jTextKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    text.setVisible(false);
                    element.jRepaint();
                }
            });
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    variable.setValue(text.getText());
                    text.setVisible(false);
                    // panel.remove(text);
                    element.jRepaint();
                    // resizeWidth();
                    element.jRepaint();
                    element.jRefreshVM();
                    checkPinDataType();
                }
            });

        }
    }

    public void jTextFocusLost(java.awt.event.FocusEvent evt) {
        System.out.println("jTextFocusLost");
        text.setVisible(false);
        // panel.remove(text);
        element.jRepaint();

    }

    public void loadFromStream(java.io.FileInputStream fis) {
        variable.loadFromStream(fis);
    }

    public void saveToStream(java.io.FileOutputStream fos) {
        variable.saveToStream(fos);
    }

    class MCUMainFlow_Property {
        String name = "";
        VSString obj;
        int type = 0;
    }


}
