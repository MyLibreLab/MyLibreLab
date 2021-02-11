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

package com.github.mylibrelab.elements.front.output.datalogger.src.properties.properties;// *****************************************************************************

/**
 * @author Homer
 */
public class FrameProperties extends javax.swing.JDialog {

    public static boolean result = false;
    public static int minX = -100;
    // </editor-fold>//GEN-END:initComponents
    public static int maxX = 100;
    public static int minY = -100;
    public static int maxY = 100;
    public static int zoomX = 1;
    public static int zoomY = 1;
    public static int stepX = 20;
    public static int stepY = 10;
    public static int darstellungAs = 1; // 0 : Punkt 1 : Linie
    public static boolean showFontXAchse = true;
    public static boolean showFontYAchse = true;
    public static boolean showHelpLinesXAchse = true;
    public static boolean showHelpLinesYAchse = true;
    public static boolean xAchseBottom = false;
    public static boolean yAchseLeft = false;
    public static boolean xAchseLettersBottom = false;
    public static boolean yAchseLettersLeft = false;
    public static boolean fadenKreuzVisible = true;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.JButton jBtnCancel;
    private javax.swing.JButton jBtnOK;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBoxFadenKreuzVisible;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton10;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JRadioButton jRadioButton9;
    private javax.swing.JSpinner jSpinnerMaxX;
    private javax.swing.JSpinner jSpinnerMaxY;
    private javax.swing.JSpinner jSpinnerMinX;
    private javax.swing.JSpinner jSpinnerMinY;
    private javax.swing.JSpinner jSpinnerStepX;
    private javax.swing.JSpinner jSpinnerStepY;
    private javax.swing.JSpinner jSpinnerZoomX;
    private javax.swing.JSpinner jSpinnerZoomY;

    /**
     * Creates new form FrameProperties
     */
    public FrameProperties(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    // Liefert TRUE wenn alles OK,
    // und FALSE wenn auf Cancel gedrückt worden ist
    public static boolean execute() {
        FrameProperties frm = new FrameProperties(null, true);
        frm.jSpinnerZoomX.setValue(new Integer(zoomX));
        frm.jSpinnerZoomY.setValue(new Integer(zoomY));
        frm.jSpinnerStepX.setValue(new Integer(stepX));
        frm.jSpinnerStepY.setValue(new Integer(stepY));

        frm.jSpinnerMinX.setValue(new Integer(minX));
        frm.jSpinnerMaxX.setValue(new Integer(maxX));
        frm.jSpinnerMinY.setValue(new Integer(minY));
        frm.jSpinnerMaxY.setValue(new Integer(maxY));

        if (darstellungAs == 0)
            frm.jRadioButton1.setSelected(true);
        else
            frm.jRadioButton2.setSelected(true);

        frm.jCheckBox1.setSelected(showFontXAchse);
        frm.jCheckBox2.setSelected(showFontYAchse);
        frm.jCheckBox3.setSelected(showHelpLinesXAchse);
        frm.jCheckBox4.setSelected(showHelpLinesYAchse);

        if (xAchseBottom)
            frm.jRadioButton4.setSelected(true);
        else
            frm.jRadioButton3.setSelected(true);
        if (yAchseLeft)
            frm.jRadioButton6.setSelected(true);
        else
            frm.jRadioButton5.setSelected(true);
        if (xAchseLettersBottom)
            frm.jRadioButton8.setSelected(true);
        else
            frm.jRadioButton7.setSelected(true);
        if (yAchseLettersLeft)
            frm.jRadioButton10.setSelected(true);
        else
            frm.jRadioButton9.setSelected(true);

        frm.jCheckBoxFadenKreuzVisible.setSelected(fadenKreuzVisible);


        frm.setVisible(true);
        return result;
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSpinnerZoomX = new javax.swing.JSpinner();
        jSpinnerZoomY = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jSpinnerStepX = new javax.swing.JSpinner();
        jPanel1 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jBtnOK = new javax.swing.JButton();
        jBtnCancel = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jSpinnerStepY = new javax.swing.JSpinner();
        jPanel3 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jPanel5 = new javax.swing.JPanel();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        jRadioButton7 = new javax.swing.JRadioButton();
        jRadioButton8 = new javax.swing.JRadioButton();
        jPanel7 = new javax.swing.JPanel();
        jRadioButton9 = new javax.swing.JRadioButton();
        jRadioButton10 = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jSpinnerMinX = new javax.swing.JSpinner();
        jSpinnerMaxX = new javax.swing.JSpinner();
        jSpinnerMinY = new javax.swing.JSpinner();
        jSpinnerMaxY = new javax.swing.JSpinner();
        jCheckBoxFadenKreuzVisible = new javax.swing.JCheckBox();

        getContentPane().setLayout(null);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Eigenschaften");
        jLabel1.setText("zoom X");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 110, 60, 14);

        jLabel2.setText("zoom Y");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 140, 60, 14);

        getContentPane().add(jSpinnerZoomX);
        jSpinnerZoomX.setBounds(70, 110, 80, 20);

        getContentPane().add(jSpinnerZoomY);
        jSpinnerZoomY.setBounds(70, 140, 80, 20);

        jLabel3.setText("Font-StepX");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(170, 110, 80, 14);

        getContentPane().add(jSpinnerStepX);
        jSpinnerStepX.setBounds(240, 110, 80, 20);

        jPanel1.setBorder(new javax.swing.border.TitledBorder("Schrift einblenden"));
        jCheckBox1.setText("Schrift X-Achse");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jPanel1.add(jCheckBox1);

        jCheckBox2.setText("Schrift Y-Achse");
        jPanel1.add(jCheckBox2);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(20, 170, 140, 90);

        jPanel2.setBorder(new javax.swing.border.TitledBorder("Hilfslinien einblenden"));
        jCheckBox3.setText("X-Achse");
        jPanel2.add(jCheckBox3);

        jCheckBox4.setText("Y-Achse");
        jPanel2.add(jCheckBox4);

        getContentPane().add(jPanel2);
        jPanel2.setBounds(180, 170, 140, 90);

        jBtnOK.setMnemonic('O');
        jBtnOK.setText("OK");
        jBtnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnOKActionPerformed(evt);
            }
        });

        getContentPane().add(jBtnOK);
        jBtnOK.setBounds(120, 480, 90, 23);

        jBtnCancel.setMnemonic('b');
        jBtnCancel.setText("Abbrechen");
        jBtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCancelActionPerformed(evt);
            }
        });

        getContentPane().add(jBtnCancel);
        jBtnCancel.setBounds(220, 480, 100, 23);

        jLabel4.setText("Font-StepY");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(170, 140, 70, 14);

        getContentPane().add(jSpinnerStepY);
        jSpinnerStepY.setBounds(240, 140, 80, 20);

        jPanel3.setBorder(new javax.swing.border.TitledBorder("Werte einblenden als"));
        buttonGroup5.add(jRadioButton1);
        jRadioButton1.setText("Punkte");
        jPanel3.add(jRadioButton1);

        buttonGroup5.add(jRadioButton2);
        jRadioButton2.setText("Linie");
        jPanel3.add(jRadioButton2);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(20, 400, 140, 59);

        jPanel4.setBorder(new javax.swing.border.TitledBorder("X-Achse anordnung"));
        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText("mittig");
        jPanel4.add(jRadioButton3);

        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setText("unten");
        jPanel4.add(jRadioButton4);

        getContentPane().add(jPanel4);
        jPanel4.setBounds(20, 270, 140, 59);

        jPanel5.setBorder(new javax.swing.border.TitledBorder("Y-Achse anordnung"));
        buttonGroup2.add(jRadioButton5);
        jRadioButton5.setText("mittig");
        jPanel5.add(jRadioButton5);

        buttonGroup2.add(jRadioButton6);
        jRadioButton6.setText("links");
        jPanel5.add(jRadioButton6);

        getContentPane().add(jPanel5);
        jPanel5.setBounds(180, 270, 140, 59);

        jPanel6.setBorder(new javax.swing.border.TitledBorder("X-Achse Zahnelreihe"));
        buttonGroup3.add(jRadioButton7);
        jRadioButton7.setText("mittig");
        jPanel6.add(jRadioButton7);

        buttonGroup3.add(jRadioButton8);
        jRadioButton8.setText("unten");
        jPanel6.add(jRadioButton8);

        getContentPane().add(jPanel6);
        jPanel6.setBounds(20, 330, 140, 59);

        jPanel7.setBorder(new javax.swing.border.TitledBorder("Y-Achse Zahnelreihe"));
        buttonGroup4.add(jRadioButton9);
        jRadioButton9.setText("mittig");
        jPanel7.add(jRadioButton9);

        buttonGroup4.add(jRadioButton10);
        jRadioButton10.setText("links");
        jPanel7.add(jRadioButton10);

        getContentPane().add(jPanel7);
        jPanel7.setBounds(180, 330, 140, 59);

        jLabel5.setText("minX");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(10, 30, 40, 14);

        jLabel6.setText("maxX");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(10, 60, 40, 14);

        jLabel7.setText("minY");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(170, 30, 50, 14);

        jLabel8.setText("maxY");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(170, 60, 60, 14);

        getContentPane().add(jSpinnerMinX);
        jSpinnerMinX.setBounds(70, 30, 80, 20);

        getContentPane().add(jSpinnerMaxX);
        jSpinnerMaxX.setBounds(70, 60, 80, 20);

        getContentPane().add(jSpinnerMinY);
        jSpinnerMinY.setBounds(240, 30, 80, 20);

        getContentPane().add(jSpinnerMaxY);
        jSpinnerMaxY.setBounds(240, 60, 80, 20);

        jCheckBoxFadenKreuzVisible.setText("Fadenkreuz sichtbar");
        getContentPane().add(jCheckBoxFadenKreuzVisible);
        jCheckBoxFadenKreuzVisible.setBounds(180, 410, 140, 23);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 343) / 2, (screenSize.height - 538) / 2, 343, 538);
    }

    private void jBtnOKActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBtnOKActionPerformed
        zoomX = ((Integer) jSpinnerZoomX.getValue()).intValue();
        zoomY = ((Integer) jSpinnerZoomY.getValue()).intValue();
        stepX = ((Integer) jSpinnerStepX.getValue()).intValue();
        stepY = ((Integer) jSpinnerStepY.getValue()).intValue();

        minX = ((Integer) jSpinnerMinX.getValue()).intValue();
        maxX = ((Integer) jSpinnerMaxX.getValue()).intValue();
        minY = ((Integer) jSpinnerMinY.getValue()).intValue();
        maxY = ((Integer) jSpinnerMaxY.getValue()).intValue();

        if (jRadioButton1.isSelected())
            darstellungAs = 0;
        else
            darstellungAs = 1;

        fadenKreuzVisible = jCheckBoxFadenKreuzVisible.isSelected();

        showFontXAchse = jCheckBox1.isSelected();
        showFontYAchse = jCheckBox2.isSelected();
        showHelpLinesXAchse = jCheckBox3.isSelected();
        showHelpLinesYAchse = jCheckBox4.isSelected();

        xAchseBottom = jRadioButton4.isSelected();
        yAchseLeft = jRadioButton6.isSelected();
        xAchseLettersBottom = jRadioButton8.isSelected();
        yAchseLettersLeft = jRadioButton10.isSelected();
        result = true;
        dispose();
    }// GEN-LAST:event_jBtnOKActionPerformed

    private void jBtnCancelActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBtnCancelActionPerformed
        result = false;
        dispose();
    }// GEN-LAST:event_jBtnCancelActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jCheckBox1ActionPerformed
     // End of variables declaration//GEN-END:variables

}
