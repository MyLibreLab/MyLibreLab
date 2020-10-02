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

package VisualLogic;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * @author Homer
 */
public class DialogFontChooser extends javax.swing.JDialog {
    private SimpleAttributeSet attributes = new SimpleAttributeSet();
    public static boolean result = false;
    public static Font newFont = null;
    private boolean init = true;

    /**
     * Creates new form FrameFontChooser
     */
    public DialogFontChooser(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        jTextPane1.setBackground(Color.WHITE);
        init = true;
        String arfonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        fontName.removeAllItems();
        for (int i = 0; i < arfonts.length; ++i) {
            fontName.addItem(arfonts[i]);
        }
        init = false;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify
     * this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fontName = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        fontBold = new javax.swing.JCheckBox();
        fontItalic = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        fontSize = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("VisualLogic/DialogFontChooser"); // NOI18N
        setTitle(bundle.getString("Title")); // NOI18N

        fontName.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Serif", "SansSerif", "Monospaced"}));
        fontName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fontNameActionPerformed(evt);
            }
        });

        jLabel1.setText(bundle.getString("Font_:")); // NOI18N

        jLabel2.setText(bundle.getString("Size_:")); // NOI18N

        fontBold.setText(bundle.getString("Bold")); // NOI18N
        fontBold.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        fontBold.setMargin(new java.awt.Insets(0, 0, 0, 0));
        fontBold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fontBoldActionPerformed(evt);
            }
        });

        fontItalic.setText(bundle.getString("Italic")); // NOI18N
        fontItalic.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        fontItalic.setMargin(new java.awt.Insets(0, 0, 0, 0));
        fontItalic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fontItalicActionPerformed(evt);
            }
        });

        jTextPane1.setText(bundle.getString("Here's_a_sample_of_this_font.")); // NOI18N
        jScrollPane1.setViewportView(jTextPane1);

        jButton1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jButton1.setText(bundle.getString("Cancel")); // NOI18N
        jButton1.setPreferredSize(new java.awt.Dimension(80, 25));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jButton2.setText(bundle.getString("OK")); // NOI18N
        jButton2.setPreferredSize(new java.awt.Dimension(60, 25));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton2MousePressed(evt);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton2MouseReleased(evt);
            }
        });

        fontSize.setText("12");
        fontSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fontSizeActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup().addContainerGap()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(layout.createSequentialGroup().add(jLabel1)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(fontName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 123,
                                                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel2)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(fontSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35,
                                                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(fontBold)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(fontItalic)
                                        .addContainerGap(71, Short.MAX_VALUE))
                                .add(layout.createSequentialGroup().add(0, 0, Short.MAX_VALUE).add(layout
                                        .createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 407,
                                                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(layout.createSequentialGroup()
                                                .add(jButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .add(31, 31, 31).add(jButton1,
                                                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                        .add(0, 0, Short.MAX_VALUE)))));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup().addContainerGap()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel1)
                                .add(fontName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(jLabel2)
                                .add(fontSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(fontBold).add(fontItalic))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(jButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap()));

        setSize(new java.awt.Dimension(451, 274));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2MouseReleased(java.awt.event.MouseEvent evt)// GEN-FIRST:event_jButton2MouseReleased
    {// GEN-HEADEREND:event_jButton2MouseReleased
     // TODO add your handling code here:
    }// GEN-LAST:event_jButton2MouseReleased

    private void jButton2MousePressed(java.awt.event.MouseEvent evt)// GEN-FIRST:event_jButton2MousePressed
    {// GEN-HEADEREND:event_jButton2MousePressed
     // TODO add your handling code here:
    }// GEN-LAST:event_jButton2MousePressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_jButton2ActionPerformed
    {// GEN-HEADEREND:event_jButton2ActionPerformed
        result = true;
        newFont = jTextPane1.getFont();
        dispose();
    }// GEN-LAST:event_jButton2ActionPerformed

    private void fontItalicActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_fontItalicActionPerformed
    {// GEN-HEADEREND:event_fontItalicActionPerformed
        process();
    }// GEN-LAST:event_fontItalicActionPerformed

    private void fontBoldActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_fontBoldActionPerformed
    {// GEN-HEADEREND:event_fontBoldActionPerformed
        process();
    }// GEN-LAST:event_fontBoldActionPerformed

    private void fontSizeActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_fontSizeActionPerformed
    {// GEN-HEADEREND:event_fontSizeActionPerformed

        process();
    }// GEN-LAST:event_fontSizeActionPerformed

    private void fontNameActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_fontNameActionPerformed
    {// GEN-HEADEREND:event_fontNameActionPerformed
        process();
    }// GEN-LAST:event_fontNameActionPerformed

    private void process() {
        if (init == false) {
            if (!StyleConstants.getFontFamily(attributes).equals(fontName.getSelectedItem())) {
                StyleConstants.setFontFamily(attributes, (String) fontName.getSelectedItem());
            }
            try {
                if (StyleConstants.getFontSize(attributes) != Integer.parseInt(fontSize.getText())) {
                    StyleConstants.setFontSize(attributes, Integer.parseInt(fontSize.getText()));
                }
            } catch (Exception ex) {
                fontSize.setText("12");
                StyleConstants.setFontSize(attributes, Integer.parseInt(fontSize.getText()));
            }
            // Check to see if the font should be bold
            if (StyleConstants.isBold(attributes) != fontBold.isSelected()) {
                StyleConstants.setBold(attributes, fontBold.isSelected());
            }
            // Check to see if the font should be italic
            if (StyleConstants.isItalic(attributes) != fontItalic.isSelected()) {
                StyleConstants.setItalic(attributes, fontItalic.isSelected());
            }

            String name = StyleConstants.getFontFamily(attributes);
            boolean bold = StyleConstants.isBold(attributes);
            boolean ital = StyleConstants.isItalic(attributes);
            int size = StyleConstants.getFontSize(attributes);

            // Bold and italic don't work properly in beta 4.
            Font f = new Font(name, (bold ? Font.BOLD : 0) + (ital ? Font.ITALIC : 0), size);

            jTextPane1.setFont(f);
        }
    }

    public Font getNewFont() {
        return newFont;
    }

    private void selectFont(String name) {
        String nm;
        for (int i = 0; i < fontName.getItemCount(); i++) {
            nm = fontName.getItemAt(i).toString();
            if (name.equalsIgnoreCase(nm)) {
                fontName.setSelectedIndex(i);
                return;
            }
        }
    }

    public void setFont(Font font) {
        selectFont(font.getName());
        fontSize.setText("" + font.getSize());
        fontBold.setSelected(font.isBold());
        fontItalic.setSelected(font.isItalic());
        jTextPane1.setFont(font);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_jButton1ActionPerformed
    {// GEN-HEADEREND:event_jButton1ActionPerformed
        result = false;
        dispose();
    }// GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox fontBold;
    private javax.swing.JCheckBox fontItalic;
    private javax.swing.JComboBox fontName;
    private javax.swing.JTextField fontSize;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}
