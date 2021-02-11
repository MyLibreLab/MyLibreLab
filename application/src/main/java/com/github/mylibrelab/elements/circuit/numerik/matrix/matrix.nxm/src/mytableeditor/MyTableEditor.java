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

package com.github.mylibrelab.elements.circuit.numerik.matrix.matrix.nxm.src.mytableeditor;// *****************************************************************************

import javax.swing.table.DefaultTableModel;

class DoubleModel extends javax.swing.table.DefaultTableModel {
    public Class getColumnClass(int columnIndex) {
        return java.lang.Double.class;
    }
}


/**
 *
 * @author Carmelo
 */
public class MyTableEditor extends javax.swing.JDialog {

    private final DefaultTableModel model = new DoubleModel();


    /** Creates new form MyTableEditor2 */
    public MyTableEditor(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        jTable2.setModel(model);

    }

    private void clearTable(DefaultTableModel model) {
        model.setColumnCount(0);
        model.setRowCount(0);

        /*
         * while(model.getRowCount()>0)
         * {
         * model.removeRow(0);
         * }
         */
    }

    public void setInputs(double[][] inputs, int zeilen, int spalten) {
        clearTable(model);

        for (int j = 0; j < spalten; j++) {
            model.addColumn("a" + j);
        }
        for (int i = 0; i < zeilen; i++) {
            Double[] data = new Double[spalten];

            for (int j = 0; j < spalten; j++) {
                data[j] = new Double(inputs[j][i]);
            }
            model.addRow(data);
        }

    }

    public void setInputColumns(String cols) {
        String[] c = cols.split(",");

        for (int i = 0; i < c.length; i++) {
            model.addColumn(c[i]);
        }
    }

    public String getInputColumns() {
        String result = "";
        for (int i = 0; i < model.getColumnCount() - 1; i++) {
            result += model.getColumnName(i) + ",";
        }
        result += model.getColumnName(model.getColumnCount() - 1);

        return result;
    }


    private double[][] getData(DefaultTableModel model) {
        double[][] result = new double[model.getColumnCount()][model.getRowCount()];
        String ch;
        for (int i = 0; i < model.getRowCount(); i++) {
            String str = "";

            for (int k = 0; k < model.getColumnCount(); k++) {
                Double o = (Double) model.getValueAt(i, k);
                result[k][i] = o.doubleValue();
            }
        }

        return result;
    }

    public double[][] getInputs() {
        return getData(model);
    }


    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.add(jButton1);

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel1.add(jButton2);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        {},
                        {},
                        {},
                        {}
                },
                new String[] {

                }));
        jTable2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane2.setViewportView(jTable2);

        getContentPane().add(jScrollPane2, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 419) / 2, (screenSize.height - 327) / 2, 419, 327);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_jButton1ActionPerformed
    {// GEN-HEADEREND:event_jButton1ActionPerformed
        resultInputs = getInputs();

        result = true;
        dispose();
        /*
         * String[] inp=getInputs();
         *
         * System.out.println(getInputColumns());
         * for (int i=0;i<inp.length;i++) {
         * System.out.println(inp[i]);
         * }
         */
    }// GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_jButton2ActionPerformed
    {// GEN-HEADEREND:event_jButton2ActionPerformed
        result = false;
        dispose();
    }// GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                MyTableEditor frm = new MyTableEditor(null, true);

                String col1 = "Car,melo,ist,der";

                // frm.setInputColumns(col1);

                double[][] matrix = new double[3][3];

                for (int i = 0; i < 3; i++)
                    for (int j = 0; j < 3; j++) {
                        matrix[i][j] = i + j;
                    }

                frm.setInputs(matrix, 3, 3);
                frm.setVisible(true);


                double[][] data = frm.getInputs();

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        System.out.print("" + data[i][j] + ",");
                        // =i+j;
                    }
                    System.out.println();
                }
            }
        });
    }

    public static double[][] resultInputs = null;
    public static boolean result = false;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables

}
