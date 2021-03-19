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

package elements.circuit.vectorenmatrix.vectorstring.src.mytableeditor;// *****************************************************************************

/*
 * DialogNew.java
 *
 * Created on 8. März 2007, 12:27
 */
import javax.swing.JOptionPane;

/**
 *
 * @author Carmelo
 */
public class DialogNew extends javax.swing.JDialog {

    /** Creates new form DialogNew */
    public DialogNew(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        init(0);
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Vector");
        jLabel2.setText("Rows : ");

        jTextField2.setText("jTextField2");

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, jButton2,
                                                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90,
                                                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                                .add(jLabel2)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(jTextField2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 118,
                                                        Short.MAX_VALUE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90,
                                                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .add(10, 10, 10)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jButton1)
                                        .add(jLabel2)
                                        .add(jTextField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jButton2)
                                .addContainerGap()));
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 282) / 2, (screenSize.height - 100) / 2, 282, 100);
    }// </editor-fold>//GEN-END:initComponents

    public static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "error", JOptionPane.ERROR_MESSAGE);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_jButton2ActionPerformed
    {// GEN-HEADEREND:event_jButton2ActionPerformed
        result = false;
        dispose();
    }// GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_jButton1ActionPerformed
    {// GEN-HEADEREND:event_jButton1ActionPerformed
        try {
            resN = Integer.valueOf(jTextField2.getText()).intValue();
        } catch (Exception ex) {
            showMessage("Values musst be numbers!");
            return;
        }
        result = true;
        dispose();
    }// GEN-LAST:event_jButton1ActionPerformed

    public void init(int n) {
        jTextField2.setText("" + n);
    }

    public static boolean result = false;
    public static int resN = 3;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables

}
