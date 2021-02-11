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

package com.github.mylibrelab.elements.front.twonumerisch.switchtwo.src.javaapplicationtwo.src.javaapplicationtwo;/*
                                                                                                                   * Copyright
                                                                                                                   * (C)
                                                                                                                   * 2020
                                                                                                                   * MyLibreLab
                                                                                                                   * Based
                                                                                                                   * on
                                                                                                                   * MyOpenLab
                                                                                                                   * by
                                                                                                                   * Carmelo
                                                                                                                   * Salafia
                                                                                                                   * www
                                                                                                                   * .
                                                                                                                   * myopenlab
                                                                                                                   * .de
                                                                                                                   * Copyright
                                                                                                                   * (C)
                                                                                                                   * 2004
                                                                                                                   * Carmelo
                                                                                                                   * Salafia
                                                                                                                   * cswi
                                                                                                                   *
                                                                                                                   * @
                                                                                                                   * gmx
                                                                                                                   * .de
                                                                                                                   *
                                                                                                                   * This
                                                                                                                   * program
                                                                                                                   * is
                                                                                                                   * free
                                                                                                                   * software:
                                                                                                                   * you
                                                                                                                   * can
                                                                                                                   * redistribute
                                                                                                                   * it
                                                                                                                   * and
                                                                                                                   * /or
                                                                                                                   * modify
                                                                                                                   * it
                                                                                                                   * under
                                                                                                                   * the
                                                                                                                   * terms
                                                                                                                   * of
                                                                                                                   * the
                                                                                                                   * GNU
                                                                                                                   * General
                                                                                                                   * Public
                                                                                                                   * License
                                                                                                                   * as
                                                                                                                   * published
                                                                                                                   * by
                                                                                                                   * the
                                                                                                                   * Free
                                                                                                                   * Software
                                                                                                                   * Foundation,
                                                                                                                   * either
                                                                                                                   * version
                                                                                                                   * 3
                                                                                                                   * of
                                                                                                                   * the
                                                                                                                   * License,
                                                                                                                   * or
                                                                                                                   * (at
                                                                                                                   * your
                                                                                                                   * option)
                                                                                                                   * any
                                                                                                                   * later
                                                                                                                   * version.
                                                                                                                   *
                                                                                                                   * This
                                                                                                                   * program
                                                                                                                   * is
                                                                                                                   * distributed
                                                                                                                   * in
                                                                                                                   * the
                                                                                                                   * hope
                                                                                                                   * that
                                                                                                                   * it
                                                                                                                   * will
                                                                                                                   * be
                                                                                                                   * useful,
                                                                                                                   * but
                                                                                                                   * WITHOUT
                                                                                                                   * ANY
                                                                                                                   * WARRANTY;
                                                                                                                   * without
                                                                                                                   * even
                                                                                                                   * the
                                                                                                                   * implied
                                                                                                                   * warranty
                                                                                                                   * of
                                                                                                                   * MERCHANTABILITY
                                                                                                                   * or
                                                                                                                   * FITNESS
                                                                                                                   * FOR
                                                                                                                   * A
                                                                                                                   * PARTICULAR
                                                                                                                   * PURPOSE.
                                                                                                                   * See
                                                                                                                   * the
                                                                                                                   * GNU
                                                                                                                   * General
                                                                                                                   * Public
                                                                                                                   * License
                                                                                                                   * for
                                                                                                                   * more
                                                                                                                   * details.
                                                                                                                   *
                                                                                                                   * You
                                                                                                                   * should
                                                                                                                   * have
                                                                                                                   * received
                                                                                                                   * a
                                                                                                                   * copy
                                                                                                                   * of
                                                                                                                   * the
                                                                                                                   * GNU
                                                                                                                   * General
                                                                                                                   * Public
                                                                                                                   * License
                                                                                                                   * along
                                                                                                                   * with
                                                                                                                   * this
                                                                                                                   * program.
                                                                                                                   * If
                                                                                                                   * not,
                                                                                                                   * see
                                                                                                                   * <http
                                                                                                                   * ://
                                                                                                                   * www
                                                                                                                   * .
                                                                                                                   * gnu
                                                                                                                   * .
                                                                                                                   * org
                                                                                                                   * /
                                                                                                                   * licenses
                                                                                                                   * />.
                                                                                                                   *
                                                                                                                   */

/**
 * @author Homer
 */
public class NewJFrame extends javax.swing.JFrame {

    /**
     * Creates new form NewJFrame
     */
    public NewJFrame() {
        initComponents();
        MultiSwitch pnl = new MultiSwitch();

        pnl.setLocation(10, 10);
        pnl.setSize(100, 200);
        getContentPane().add(pnl);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        getContentPane().setLayout(null);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 392) / 2, (screenSize.height - 358) / 2, 392, 358);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
