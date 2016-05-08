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

import java.awt.*;
import java.io.File;
import java.net.*;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.html.*;
/**
 *
 * @author  Homer
 */
public class FrameInfo extends javax.swing.JDialog {
    
    private JFrame parent;
    private String elementPath;
            
    private String addPoints(String str)
    {
        String result="";
        for (int i=0;i<str.length();i++)
        {            
            char ch=str.charAt(i);
            
            if (ch!='.')  
            {
                result+=ch+"."; 
            }else 
            {
                //result+=ch;
            }
        }
        return result;                
    }
    
    
    private void fillTable()
    {
        

    DefaultTableModel model = new DefaultTableModel()
    { 
        public boolean isCellEditable(int row, int column) 
        { 
             return false; 
        } 
    };         
        
        model.addColumn("Name");
        model.addColumn("Value");
        Properties props=System.getProperties();
        
        Enumeration items=props.propertyNames();

        while(items.hasMoreElements())
        {
            String key=items.nextElement().toString();
            //System.out.println(key+" , "+System.getProperty(key));
            String[] item = new String[2];
            item[0]=key;
            item[1]=System.getProperty(key);
            model.addRow(item);
        }

        jTable1.setModel(model);
        
        
    }
    
    /** Creates new form frmInfo */
    public FrameInfo(JFrame parent, boolean modal, String elementPath) {
        super(parent, modal);
        initComponents();
        
        this.elementPath=elementPath;
        this.parent=parent;
        
        
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width-getWidth())/2, (screenSize.height-getHeight())/2);       
                
        //jLabel4.setText(Version.strApplicationTitle);
        
        jButton3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        
        java.awt.event.ActionListener actionListener = new java.awt.event.ActionListener() 
        {
          public void actionPerformed(java.awt.event.ActionEvent actionEvent) 
          {                
                dispose();            
          }
        };
        
        javax.swing.KeyStroke stroke = javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(actionListener, stroke, javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        
        jLabelVers.setText(Version.strApplicationVersion+ " "+Version.strStatus);
        
        
        fillTable();
        
        
        listDrivers();
        
        /*jLabelVersion.setText(System.getProperty("java.version"));
        
        jLabelBetriebssystem.setText(System.getProperty("os.name")+" version "+System.getProperty("os.version")+ " running on "+System.getProperty("os.arch"));
        jLabelVM.setText(System.getProperty("java.vm.name")+" version"+System.getProperty("java.vm.version"));
        jLabelVendor.setText(System.getProperty("java.vm.vendor")); 
        jLabelJavaHome.setText(System.getProperty("java.home")); 
        jLabelUserHome.setText(System.getProperty("user.home")); */
            
        try
        {              
            Locale loc = Locale.getDefault();
            String strLocale=loc.toString();
            
            URL url = null;
            
            System.out.println(strLocale);
            
            if (strLocale.equalsIgnoreCase("de_DE"))
            {
                url = getClass().getResource("/VisualLogic/license.txt");
            }else
            if (strLocale.equalsIgnoreCase("en_US"))
            {
                url = getClass().getResource("/VisualLogic/license.txt");
            }else
            if (strLocale.equalsIgnoreCase("es_ES"))
            {
                url = getClass().getResource("/VisualLogic/license.txt");
            }
            
            jEditorPane1.setContentType("text/html");
            jEditorPane1.setPage(url);
            
        } catch (Exception ex) 
        {            
            Tools.showMessage("Liesmich.html wurde nicht gefunden!");
        }
        
        jEditorPane1.addHyperlinkListener (
        new HyperlinkListener () {
      public void hyperlinkUpdate(HyperlinkEvent e) {
        // Das aendern des Mauszeigers geht ab 
        // Java 1.3 auch automatisch 
        if (e.getEventType() == 
                           HyperlinkEvent.EventType.ENTERED) {
           ((JEditorPane) e.getSource()).setCursor(
              Cursor.getPredefinedCursor(
                  Cursor.HAND_CURSOR));
        } else
          if (e.getEventType() == 
                           HyperlinkEvent.EventType.EXITED) {
           ((JEditorPane) e.getSource()).setCursor(
               Cursor.getPredefinedCursor(
                  Cursor.DEFAULT_CURSOR));
          } else
           // Hier wird auf ein Klick reagiert
            if (e.getEventType() == 
                              HyperlinkEvent.EventType.ACTIVATED) {
              JEditorPane pane = (JEditorPane) e.getSource(); 
              if (e instanceof HTMLFrameHyperlinkEvent) {
                HTMLFrameHyperlinkEvent evt = 
                                       (HTMLFrameHyperlinkEvent)e;
                HTMLDocument doc = 
                     (HTMLDocument)pane.getDocument();
                doc.processHTMLFrameHyperlinkEvent(evt);
              } else try {
                       // Normaler Link
                       pane.setPage(e.getURL());
                     } catch (Throwable t) {
                         t.printStackTrace();
                     }
            }
       }
      });

    }
    
    
    public void listDrivers()
    {
        
        DefaultTableModel model = (DefaultTableModel)jTable2.getModel();
        
        while(model.getRowCount()>0) model.removeRow(0);

        for (int i=0;i<model.getColumnCount();i++)
        {
            TableColumn col = jTable2.getColumnModel().getColumn(i);            
            col.setPreferredWidth(150);            
        }
        
        
        String driverPath=elementPath+"/Drivers";
        
        File[] files=new File(driverPath).listFiles();
        
        for (int i=0;i<files.length;i++)
        {
            File f=files[i];
            if (f.isDirectory())
            {
                DriverInfo info=Tools.openDriverInfo(f);
                
                if (info!=null)
                {                    
                    String[] item = new String[5];
                    item[0]=f.getName();
                    item[1]=info.Copyrights;
                    item[2]=info.Website;
                    item[3]=info.Lizenz;
                    item[4]=new File( driverPath+"/"+f.getName() ).getAbsolutePath();
                    
                    
                    model.addRow(item);                   
                }
            }
        }
        // End Loading Drivers.
        
    }
 
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPane2 = new javax.swing.JEditorPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabelVers = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("VisualLogic/FrameInfo"); // NOI18N
        setTitle(bundle.getString("Titel")); // NOI18N
        setBackground(java.awt.SystemColor.control);
        setName("Form"); // NOI18N
        setResizable(false);

        jButton1.setText(bundle.getString("close")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel5.setName("jPanel5"); // NOI18N

        jScrollPane1.setAlignmentX(0.0F);
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jEditorPane1.setEditable(false);
        jEditorPane1.setBorder(null);
        jEditorPane1.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jEditorPane1.setName("jEditorPane1"); // NOI18N
        jEditorPane1.setPreferredSize(new java.awt.Dimension(100, 100));
        jScrollPane1.setViewportView(jEditorPane1);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("Lizenz"), jPanel5); // NOI18N

        jPanel7.setName("jPanel7"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jEditorPane2.setEditable(false);
        jEditorPane2.setText(bundle.getString("FrameInfo.jEditorPane2.text")); // NOI18N
        jEditorPane2.setName("jEditorPane2"); // NOI18N
        jScrollPane2.setViewportView(jEditorPane2);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("Copyrights"), jPanel7); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        jTextPane2.setEditable(false);
        jTextPane2.setText(bundle.getString("contributor")); // NOI18N
        jTextPane2.setName("jTextPane2"); // NOI18N
        jScrollPane4.setViewportView(jTextPane2);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("contributors"), jPanel2); // NOI18N

        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel6.setName("jPanel6"); // NOI18N
        jPanel6.setLayout(new java.awt.BorderLayout());

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Name", "Value"
            }
        ));
        jTable1.setName("jTable1"); // NOI18N
        jScrollPane3.setViewportView(jTable1);

        jPanel6.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(bundle.getString("Java_Details"), jPanel6); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        jScrollPane5.setName("jScrollPane5"); // NOI18N

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"AAAAAAAAAAAAAAAAAAAAAA", "CCC", "ERRR", null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Module", "Copyrights", "Website", "license", "Path"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable2.setName("jTable2"); // NOI18N
        jScrollPane5.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("FrameInfo.jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        jPanel3.setName("jPanel3"); // NOI18N

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/myopenlab_logo_2016.png"))); // NOI18N
        jButton2.setBorder(null);
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.setDefaultCapable(false);
        jButton2.setFocusPainted(false);
        jButton2.setFocusable(false);
        jButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton2.setName("jButton2"); // NOI18N
        jButton2.setPreferredSize(new java.awt.Dimension(500, 79));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/icon_2.png"))); // NOI18N
        jButton3.setToolTipText(bundle.getString("FrameInfo.jButton3.toolTipText")); // NOI18N
        jButton3.setBorderPainted(false);
        jButton3.setFocusPainted(false);
        jButton3.setFocusable(false);
        jButton3.setName("jButton3"); // NOI18N
        jButton3.setPreferredSize(new java.awt.Dimension(56, 41));
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        jLabel6.setText(bundle.getString("FrameInfo.jLabel6.text")); // NOI18N
        jLabel6.setAutoscrolls(true);
        jLabel6.setName("jLabel6"); // NOI18N

        jLabelVers.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jLabelVers.setText(bundle.getString("FrameInfo.jLabelVers.text")); // NOI18N
        jLabelVers.setName("jLabelVers"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelVers, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabelVers))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jButton3MouseClicked
    {//GEN-HEADEREND:event_jButton3MouseClicked
        Tools.openUrl(parent,"http://www.myopenlab.de"); 
    }//GEN-LAST:event_jButton3MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed
    

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JEditorPane jEditorPane2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelVers;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextPane jTextPane2;
    // End of variables declaration//GEN-END:variables
    
}
