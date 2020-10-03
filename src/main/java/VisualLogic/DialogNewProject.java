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

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileView;

/**
 * @author Carmelo
 */
public class DialogNewProject extends javax.swing.JDialog {
    private FrameMain parent;

    /**
     * Creates new form DialogNewProject
     */
    public DialogNewProject(FrameMain parent, boolean modal) {
        super(parent, modal);

        this.parent = parent;
        initComponents();

        String path = new File(parent.settings.getOldProjectPath()).getAbsolutePath();
        jTextField2.setText(path);

        make();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify
     * this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cancelBtn = new javax.swing.JButton();
        Ok_btn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        FinalProjectPath = new javax.swing.JTextField();
        ExploreFolder_btn = new javax.swing.JButton();
        jCheckBox2 = new javax.swing.JCheckBox();
        MainVMName = new javax.swing.JTextField();
        jCheckSPSProjekt = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("VisualLogic/DialogNewProject"); // NOI18N
        setTitle(bundle.getString("New_Project")); // NOI18N

        cancelBtn.setText(bundle.getString("Cancel")); // NOI18N
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        Ok_btn.setText(bundle.getString("OK")); // NOI18N
        Ok_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ok_btnActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("NAME_AND_LOCATION"))); // NOI18N

        jLabel1.setText(bundle.getString("Project_Name_:_")); // NOI18N

        jTextField1.setText(bundle.getString("project1")); // NOI18N
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
        });
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }

            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField2KeyPressed(evt);
            }

            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jLabel2.setText(bundle.getString("Project_Location_:_")); // NOI18N

        jLabel3.setText(bundle.getString("Project_Folder_:")); // NOI18N

        FinalProjectPath.setEditable(false);

        ExploreFolder_btn.setText(bundle.getString("Browse...")); // NOI18N
        ExploreFolder_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExploreFolder_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup().addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                                .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                                .addComponent(FinalProjectPath, javax.swing.GroupLayout.DEFAULT_SIZE, 336,
                                        Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ExploreFolder_btn).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup().addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2).addComponent(ExploreFolder_btn).addComponent(jTextField2,
                                        javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3).addComponent(FinalProjectPath,
                                        javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        jCheckBox2.setSelected(true);
        jCheckBox2.setText(bundle.getString("Create_Main_VM")); // NOI18N
        jCheckBox2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox2.setMargin(new java.awt.Insets(0, 0, 0, 0));

        MainVMName.setText("Main");

        jCheckSPSProjekt.setText(bundle.getString("MYOPENLAB_SPS_PROJEKT")); // NOI18N
        jCheckSPSProjekt.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckSPSProjekt.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckSPSProjekt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckSPSProjektActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createSequentialGroup().addComponent(jCheckBox2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(MainVMName, javax.swing.GroupLayout.PREFERRED_SIZE, 182,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jCheckSPSProjekt).addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                        layout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(Ok_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jCheckBox2)
                                .addComponent(MainVMName, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jCheckSPSProjekt))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cancelBtn).addComponent(Ok_btn))
                        .addContainerGap()));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void make() {
        // jTextField3.setText(jTextField2.getText()+"\\"+jTextField1.getText()); //NOI18N
        FinalProjectPath.setText(jTextField2.getText() + File.separator + jTextField1.getText()); // NOI18N
    }

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt)// GEN-FIRST:event_jTextField2KeyReleased
    {// GEN-HEADEREND:event_jTextField2KeyReleased
        make();
    }// GEN-LAST:event_jTextField2KeyReleased

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt)// GEN-FIRST:event_jTextField1KeyReleased
    {// GEN-HEADEREND:event_jTextField1KeyReleased
        make();
    }// GEN-LAST:event_jTextField1KeyReleased

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt)// GEN-FIRST:event_jTextField1KeyPressed
    {// GEN-HEADEREND:event_jTextField1KeyPressed

    }// GEN-LAST:event_jTextField1KeyPressed

    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt)// GEN-FIRST:event_jTextField1MouseClicked
    {// GEN-HEADEREND:event_jTextField1MouseClicked

    }// GEN-LAST:event_jTextField1MouseClicked

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_jTextField1ActionPerformed
    {// GEN-HEADEREND:event_jTextField1ActionPerformed

    }// GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2KeyPressed(java.awt.event.KeyEvent evt)// GEN-FIRST:event_jTextField2KeyPressed
    {// GEN-HEADEREND:event_jTextField2KeyPressed

    }// GEN-LAST:event_jTextField2KeyPressed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_jTextField2ActionPerformed
    {// GEN-HEADEREND:event_jTextField2ActionPerformed

    }// GEN-LAST:event_jTextField2ActionPerformed

    private void ExploreFolder_btnActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_ExploreFolder_btnActionPerformed
    {// GEN-HEADEREND:event_ExploreFolder_btnActionPerformed

        JFileChooser chooser = new JFileChooser() {
            public void approveSelection() {
                if (getSelectedFile().isDirectory()) {
                    // beep
                    super.approveSelection();
                }
            }
        };

        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        // OnlyDirectoryFilter filter= new OnlyDirectoryFilter();
        // chooser.setFileFilter(filter);

        chooser.setDialogTitle(
                java.util.ResourceBundle.getBundle("VisualLogic/DialogNewProject").getString("Browse..."));
        // chooser.setDialogType(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);

        chooser.setCurrentDirectory(new File(System.getProperty("user.home"))); // NOI18N

        // chooser.setFileFilter(filter);
        FileView view = new JavaFileView();
        chooser.setFileView(view);

        int value = chooser.showOpenDialog(this);

        if (value == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            String fileName = file.getPath();
            jTextField2.setText(fileName);
            make();
        }
        Ok_btn.requestFocus();
    }// GEN-LAST:event_ExploreFolder_btnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_cancelBtnActionPerformed
    {// GEN-HEADEREND:event_cancelBtnActionPerformed
        result = false;
        dispose();
    }// GEN-LAST:event_cancelBtnActionPerformed

    private void Ok_btnActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_Ok_btnActionPerformed
    {// GEN-HEADEREND:event_Ok_btnActionPerformed

        if (jCheckSPSProjekt.isSelected()) {
            projectType = "SPS"; // NOI18N
        }
        projectName = FinalProjectPath.getText();
        if (!new File(projectName).exists()) {
            result = true;
            createMainVM = jCheckBox2.isSelected();

            if (createMainVM) {
                mainVMFilename = MainVMName.getText();

                if (mainVMFilename.length() == 0) {
                    mainVMFilename = "Main";
                }
            }

            parent.settings.setOldProjectPath(jTextField2.getText());
            dispose();
        } else {
            cancelBtn.requestFocus();
            Tools.showMessage(java.util.ResourceBundle.getBundle("VisualLogic/DialogNewProject")
                    .getString("Folder_already_exist_:_") + "\"" + projectName + "\"");
        }
    }// GEN-LAST:event_Ok_btnActionPerformed

    private void jCheckSPSProjektActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCheckSPSProjektActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jCheckSPSProjektActionPerformed

    public static String projectName = "";
    public static boolean result = false;
    public static boolean createMainVM = true;
    public static String mainVMFilename = "Main";
    public static String projectType = "";

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ExploreFolder_btn;
    private javax.swing.JTextField FinalProjectPath;
    private javax.swing.JTextField MainVMName;
    private javax.swing.JButton Ok_btn;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JCheckBox jCheckBox2;
    public static javax.swing.JCheckBox jCheckSPSProjekt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
