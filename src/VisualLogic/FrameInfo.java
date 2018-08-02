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
import javax.swing.JTextPane;
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
        
        
        jTextPaneContributors.setBackground(Color.WHITE);
        jTextPaneContributors.setAlignmentX(JTextPane.CENTER_ALIGNMENT);
        
        
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width-getWidth())/2, (screenSize.height-getHeight())/2);       
                
        //jLabel4.setText(Version.strApplicationTitle);
        
        
        
        
        java.awt.event.ActionListener actionListener = new java.awt.event.ActionListener() 
        {
          public void actionPerformed(java.awt.event.ActionEvent actionEvent) 
          {                
                dispose();            
          }
        };
        
        javax.swing.KeyStroke stroke = javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(actionListener, stroke, javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        
        jLabelVers.setText("Build "+Version.strApplicationVersion+ " "+Version.strStatus);
        
        
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
        jTextPaneContributors = new javax.swing.JTextPane();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabelVers = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("VisualLogic/FrameInfo"); // NOI18N
        setTitle(bundle.getString("Titel")); // NOI18N
        setBackground(java.awt.SystemColor.control);
        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(664, 450));
        setResizable(false);

        jButton1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jButton1.setText(bundle.getString("close")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.setPreferredSize(new java.awt.Dimension(90, 25));
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
        jEditorPane1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jEditorPane1.setName("jEditorPane1"); // NOI18N
        jEditorPane1.setPreferredSize(new java.awt.Dimension(100, 100));
        jScrollPane1.setViewportView(jEditorPane1);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("Lizenz"), jPanel5); // NOI18N

        jPanel7.setName("jPanel7"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jEditorPane2.setEditable(false);
        jEditorPane2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jEditorPane2.setText(bundle.getString("FrameInfo.jEditorPane2.text")); // NOI18N
        jEditorPane2.setToolTipText(bundle.getString("FrameInfo.jEditorPane2.toolTipText")); // NOI18N
        jEditorPane2.setName("jEditorPane2"); // NOI18N
        jScrollPane2.setViewportView(jEditorPane2);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("Copyrights"), jPanel7); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(639, 400));

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        jTextPaneContributors.setEditable(false);
        jTextPaneContributors.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jTextPaneContributors.setText(bundle.getString("FrameInfo.jTextPaneContributors.text")); // NOI18N
        jTextPaneContributors.setName("jTextPaneContributors"); // NOI18N
        jScrollPane4.setViewportView(jTextPaneContributors);

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel3.setText(bundle.getString("FrameInfo.jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/logoUcatolica.png"))); // NOI18N
        jLabel1.setText(bundle.getString("FrameInfo.jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel1MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane4)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(4, 4, 4))
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
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("FrameInfo.jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        jTabbedPane1.setSelectedIndex(2);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setPreferredSize(new java.awt.Dimension(481, 90));

        jLabelVers.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabelVers.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelVers.setText(bundle.getString("FrameInfo.jLabelVers.text")); // NOI18N
        jLabelVers.setName("jLabelVers"); // NOI18N

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/myopenlab_logo_Oficial.png"))); // NOI18N
        jLabel2.setText(bundle.getString("FrameInfo.jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelVers, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabelVers))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 644, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        setSize(new java.awt.Dimension(680, 499));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jLabel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MousePressed
        Tools.openUrl(parent,"https://www.myopenlab.de");
    }//GEN-LAST:event_jLabel1MousePressed
    

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JEditorPane jEditorPane2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JTextPane jTextPaneContributors;
    // End of variables declaration//GEN-END:variables
    
}
