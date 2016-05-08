/*
MyOpenLab by Carmelo Salafia www.myopenlab.de
Copyright (C) 2004  Carmelo Salafia cswi@gmx.de

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package VisualLogic;

import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JOptionPane;

/**
 *
 * @author  Carmelo
 */
public class DialogUpdate extends javax.swing.JDialog
{
    public boolean downloadInProgress=false;
    public String addressX;
    public String localFileNameX;
    private FrameMain parent;
    
    /** Creates new form DialogUpdate */
    public DialogUpdate(FrameMain parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        jLabel1.setText(java.util.ResourceBundle.getBundle("VisualLogic/DialogUpdate").getString("Press_Start_Download_to_begin"));
        
        jLabel4.setText(Version.strApplicationVersion);
    }
    
    
    
    
    public void download(String address, String localFileName)
    {
        addressX=address;
        localFileNameX=localFileName;
        downloadInProgress = true;
        
        new Thread()
        {
            public void run()
            {
                InputStream inputStream = null;
                OutputStream outputStream = null;
                try
                {
                    String urlText = addressX;
                    URL url = new URL(urlText);
                    URLConnection urlConnection = url.openConnection();
                    jProgressBar1.setMaximum(urlConnection.getContentLength());
                    
                    inputStream = urlConnection.getInputStream();
                    
                    outputStream = new FileOutputStream(new File(localFileNameX));
                    
                    byte[] buffer = new byte[16384];
                    int bytesRead = -1;
                    int bytesWritten = 0;
                    
                    long deltaTime = 0;
                    while((bytesRead = inputStream.read(buffer)) > 0)
                    {
                        if (!downloadInProgress)
                        {
                            return;
                        }
                        
                        outputStream.write(buffer,0,bytesRead);
                        bytesWritten +=bytesRead;
                        outputStream.flush();
                        jProgressBar1.setValue(bytesWritten);
                    }
                    
                    jLabel1.setText(java.util.ResourceBundle.getBundle("VisualLogic/DialogUpdate").getString("Download_completed!"));
                    jButton2.setText(java.util.ResourceBundle.getBundle("VisualLogic/DialogUpdate").getString("Close"));
                    
                    Tools.showMessage(DialogUpdate.this,java.util.ResourceBundle.getBundle("VisualLogic/DialogUpdate").getString("Please_restart_the_Application!"), JOptionPane.INFORMATION_MESSAGE);
                    Tools.appResult=10;
                    dispose();
                    
                }
                catch (Exception e)
                {
                    Tools.showMessage("No Connection!");
                }
                finally
                {
                    if(outputStream != null)
                    {
                        try
                        {
                            outputStream.close();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if(inputStream != null)
                    {
                        try
                        {
                            inputStream.close();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    
                    downloadInProgress = false;
                }
                
            }
        }.start();
    }
    
    
    public boolean downloadVersionFile(String address, String localFileName)
    {
        
        boolean result=false;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try
        {
            String urlText = address;
            URL url = new URL(urlText);
            URLConnection urlConnection = url.openConnection();
            jProgressBar1.setMaximum(urlConnection.getContentLength());

            inputStream = urlConnection.getInputStream();

            outputStream = new FileOutputStream(new File(localFileName));

            byte[] buffer = new byte[16384];
            int bytesRead = -1;
            int bytesWritten = 0;
            
            long deltaTime = 0;            
            while((bytesRead = inputStream.read(buffer)) > 0)
            {
                outputStream.write(buffer,0,bytesRead);
                bytesWritten +=bytesRead;
                outputStream.flush();                        
            }
            
            result=true;

        }
        catch (Exception e)
        {
          Tools.showMessage("No Connection!");  
          result=false;
        }
        finally
        {
            if(outputStream != null)
            {
                try
                {
                    outputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if(inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        }

        return result;
    }    
    
    private boolean close()
    {
        if (downloadInProgress)
        {
            
            int result = JOptionPane.showConfirmDialog((Component)null, java.util.ResourceBundle.getBundle("VisualLogic/DialogUpdate").getString("Stop_Download?"), java.util.ResourceBundle.getBundle("VisualLogic/DialogUpdate").getString("Attention!"), JOptionPane.YES_NO_OPTION);
            
            if (result==JOptionPane.YES_OPTION)
            {
                downloadInProgress=false;
                return true;
            }
            if (result==JOptionPane.NO_OPTION)
            {
                return false;
            }
        }
        return true;
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("VisualLogic/DialogUpdate"); // NOI18N
        setTitle(bundle.getString("Update")); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText(bundle.getString("Downloading...")); // NOI18N

        jButton1.setText(bundle.getString("Start_Download")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText(bundle.getString("Cancel")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setText(bundle.getString("Aktuelle_Version_:_")); // NOI18N

        jLabel3.setText(bundle.getString("Neue_Version_:_")); // NOI18N

        jLabel4.setText("XXX");

        jLabel5.setText("???");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(230, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE))
                        .addGap(163, 163, 163))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3))
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-321)/2, (screenSize.height-162)/2, 321, 162);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        if (close())
        {
            dispose();
        }
    }//GEN-LAST:event_formWindowClosing
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
    {//GEN-HEADEREND:event_jButton2ActionPerformed
        if (close())
        {
            dispose();
        }
    }//GEN-LAST:event_jButton2ActionPerformed
    
    private double getUpdateVersion(String filename)
    {
       String str=Tools.loadTextFile(new File(filename));
       
       try
       {
           double cc=Double.valueOf(str);
           return cc;
       }catch(Exception ex)
       {
           
       }
       return 0;
    }
    
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed
        
        String supdateTXT = parent.elementPath+"/update.txt";
        
        if (downloadVersionFile("http://www.myopenlab.de/downloads/update.txt", supdateTXT))
        {

            double ver=0;
            try
            {
                ver=Double.valueOf(Version.strApplicationVersion);            
           }catch(Exception ex)
           {

           }
            double verX=getUpdateVersion(supdateTXT);

            if (verX>ver)
            {        
                jLabel5.setText(""+verX);
                download("http://www.myopenlab.de/downloads/c-exp-lab.jar", parent.elementPath+"/update.jar");
                jButton1.setEnabled(false);
                jButton2.setEnabled(true);
                jLabel1.setText(java.util.ResourceBundle.getBundle("VisualLogic/DialogUpdate").getString("Downloading...._(Please_Wait)"));
            } else
            {
                Tools.showMessage(DialogUpdate.this,java.util.ResourceBundle.getBundle("VisualLogic/DialogUpdate").getString("No_Updates_avaible"), JOptionPane.INFORMATION_MESSAGE);

                dispose();
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JProgressBar jProgressBar1;
    // End of variables declaration//GEN-END:variables
    
}
