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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 *
 * @author  Carmelo
 */
public class VMEditorPanel extends javax.swing.JPanel implements ElementPaletteIF
{
    public Basis basis;
    
    public DialogLupe lupe ;
    
    public Peditor.PropertyEditor propertyEditor;
    private Element oldElement=null;
    //private FrameElementDoc dialog;
    
    public JPanel panelCircuit=null;
    public JPanel panelFront=null;
    
    
    
    public javax.swing.Timer timer;
    private String elementPath="";
    private VMEditorPanelIF owner;
    
    public int oldElementCount=0;
    
    private VMObject vmObject;
    
    private boolean isEnabled=true;
    
    public void enabled(boolean value)
    {
        isEnabled=value;
    }
    
    public Basis getBasis()
    {
        return basis;
    }
    
    public boolean panelMode=false;
    private boolean comboIsEditing=false;
    
    
    public void setVMObject(VMObject obj)
    {
        if (getVMObject()!=null)
        {
            getVMObject().newElement=null;
        }
        vmObject=obj;
        
        owner.vmEditorPanelTabChanged(vmObject);
        
    }
    
    public VMObject getVMObject()
    {
        return vmObject;
        
    }
    public void onButtonClicken(String [] params)
    {
        getVMObject().newElement=params;
    }
    
    //public VMObject getVMObject(){return basis.getCircuitBasis();}
    
    
    JPanel docPanel = new JPanel();
    
    
    private void loadDoc(String filename, JEditorPane pane)
    {
        URL url =null;
        try
        {
            url = new URL("file://"+filename);
        }
        catch(Exception ex)
        {
            
        }
        if (new File(filename).exists())
        {
            try
            {
                pane.setPage(url);
            }
            catch (Exception e)
            {
                basis.showErrorMessage(e.toString());
            }
        }
        else
        {
            /*URL urlx = new File(elementPath).toURL();
            String s=urlx.getFile();
            s = s.substring(0,s.lastIndexOf("/"));
            pane.setText(java.util.ResourceBundle.getBundle("VisualLogic/FrameCircuit").getString("Keine Beschreibung gefunden"));*/
        }
        
    }
    
    
    public void openElementDocFile(FrameMain owner, Element element)
    {
        PanelDokumentation panel = new PanelDokumentation();
        
        try
        {
            panel.openElementDocFile(owner, element);
        }
        catch(Exception ex)
        {
            
        }
        jTabbedPane1.addTab("Doc - "+element.getNameLocalized(),panel);
        jTabbedPane1.setSelectedComponent(panel);
    }
    
    class MeinPrintStream extends PrintStream
    {
        public MeinPrintStream(JEditorPane anzeige)
        {
            super(new MeinOutputStream(anzeige));
        }
    }
    
    class MeinOutputStream extends OutputStream
    {
        private JEditorPane anzeigeText;
        
        public MeinOutputStream(JEditorPane anzeige)
        {
            this.anzeigeText=anzeige;
        }
        
        public void write(int b)
        {
            char c=(char)b;
            anzeigeText.setText(anzeigeText.getText()+String.valueOf(c));
        }
    }
    
    
    
      
    
    /*public void processModus()
    {
        if (basis.modus==basis.PROCESSFIRST)
        {
            cmbProcessing.setSelectedIndex(0);
        }
        else
        {
            cmbProcessing.setSelectedIndex(1);
        }
    }*/
    
    
    /** Creates new form FrameCircuitX */
    public VMEditorPanel(VMEditorPanelIF owner, Basis basis,String elementPath, boolean panelMode)
    {
        initComponents();
        
        this.owner=owner;
        
        //lupe=new DialogLupe(this,basis,false);
        //lupe.setVisible(true);
        
        
        //mnu.add(layedLabel);
        
        //cmbProcessing.getModel();
        //cmbProcessing.addItem(java.util.ResourceBundle.getBundle("VisualLogic/FrameCircuit").getString("topFirst"));
        //cmbProcessing.addItem(java.util.ResourceBundle.getBundle("VisualLogic/FrameCircuit").getString("bottomFirst"));
        
        this.panelMode=panelMode;
        
        //MeinPrintStream ausgabe=new MeinPrintStream(txtMessages);
        //System.setErr(ausgabe);
        //System.setOut(ausgabe);
        
        //activate_DocFrame(null);
        
        this.basis=basis;
        
        basis.ownerVMPanel=this;
        this.elementPath=elementPath;
        
        //jSplitPane2.setDividerLocation(0);
        
        panelCircuit = new BasisPanel(basis.getCircuitBasis());
        panelCircuit.setVisible(!basis.vmProtected);
        
        jScrollPaneCircuitPanel.add(panelCircuit);
        jScrollPaneCircuitPanel.setViewportView(panelCircuit);
        
        panelFront = new BasisPanel(basis.getFrontBasis());
        
        
        
        jScrollPaneFrontPanel.add(panelFront);
        jScrollPaneFrontPanel.setViewportView(panelFront);
        //basis.getFrontBasis().panel=panelFront;
        
        setVMObject(basis.getCircuitBasis());
        
        
        //propertyEditor = new Peditor.PropertyEditor(this);
        
        //jTabPE.addTab(java.util.ResourceBundle.getBundle("VisualLogic/FrameCircuit").getString("Property-Editor"), propertyEditor);
        
        //oldPanelDirectory=basis.getFrameMain().settings.panelDirectory;
        //oldCircuitDirectory=basis.getFrameMain().settings.circuitDirectory;
        
        try
        {
            //setIconImage(basis.getFrameMain().iconImage);
            //iconImage= Toolkit.getDefaultToolkit().getImage();
            //setIconImage(iconImage);
            basis.vsIcon.loadImage(getClass().getResource("/Bilder/icon_16.png"));
        }
        catch(Exception ex)
        {
        }
        
        /*timer = new javax.swing.Timer(500, new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
         
                if (getBasis().isLoading()) return;
         
         
                //System.out.println("XXX");
         
                getBasis().getFrontBasis().sortSubPanels();
         
                getBasis().getCircuitBasis().adjustAllElemnts();
                getBasis().getFrontBasis().adjustAllElemnts();
         
                getBasis().getCircuitBasis().checkPins();
         
         
                Element element=getVMObject().getSelectedElement();
                if (element!=null)
                {
                    if (element!=oldElement)
                    {
                        comboIsEditing=true;
                        jComboBox1.setSelectedItem(element);
                        comboIsEditing=false;
                    }
                    oldElement=element;
                }
                if (getVMObject().getElementCount()!=oldElementCount)
                {
                    oldElementCount=getVMObject().getElementCount();
                }
         
                jButtonStart.setEnabled(true);
         
                jmiSave.setEnabled(true);
                jmiSaveAs.setEnabled(true);
                //jmiSaveAsModul.setEnabled(true);
                jmiSelectAny.setEnabled(true);
                jmiStart.setEnabled(true);
                jButtonDebug.setEnabled(true);
                jmiStop.setEnabled(true);
         
                jButtonRefreshVM.setEnabled(true);
         
                jButtonPause.setEnabled(true);
                jButtonResume.setEnabled(true);
                jmiPause.setEnabled(true);
                jmiResume.setEnabled(true);
                jmiStep.setEnabled(true);
         
                //jComboBox1.setEnabled(true);
                //jComboBox1.setSelectedItem(""+getVMObject().owner.getDelay());
         
                if (getVMObject().owner.getUndoPointer()>1)
                {
                    jmiUndo.setEnabled(true);
                    cmdUndo.setEnabled(true);
                }
                else
                {
                    jmiUndo.setEnabled(false);
                    cmdUndo.setEnabled(false);
                }
         
                if (getVMObject().owner.getUndoPointer()<getVMObject().owner.getUndoHistorySize())
                {
                    jmiRedo.setEnabled(true);
                    cmdRedo.setEnabled(true);
                }
                else
                {
                    jmiRedo.setEnabled(false);
                    cmdRedo.setEnabled(false);
                }
         
         
         
                if (getVMObject().isRunning())
                {
                    jmiStart.setEnabled(false);
                    jmiStop.setEnabled(true);
                    jButtonStart.setEnabled(false);
                    jButtonDebug.setEnabled(false);
                    jButtonStop.setEnabled(true);
         
                    jButtonStep.setEnabled(true);
                    jmiStep.setEnabled(true);
                    //setBarsEnabled(false);
         
                    jButtonRefreshVM.setEnabled(false);
         
                    if (getVMObject().isPaused())
                    {
                        jButtonPause.setEnabled(false);
                        jButtonResume.setEnabled(true);
                        jmiPause.setEnabled(false);
                        jmiResume.setEnabled(true);
         
                        jButtonStep.setEnabled(true);
                        jmiStep.setEnabled(true);
         
                    }
                    else
                    {
                        jmiPause.setEnabled(true);
                        jmiResume.setEnabled(false);
         
                        jButtonPause.setEnabled(true);
                        jButtonResume.setEnabled(false);
         
                        jButtonStep.setEnabled(false);
                        jmiStep.setEnabled(false);
         
                    }
         
                }
                else
                {
                    jmiStart.setEnabled(true);
                    jmiStop.setEnabled(false);
                    jButtonStart.setEnabled(true);
                    jButtonStop.setEnabled(false);
                    jButtonPause.setEnabled(false);
                    jButtonResume.setEnabled(false);
                    jmiPause.setEnabled(false);
                    jmiResume.setEnabled(false);
         
                    jButtonStep.setEnabled(false);
                    jmiStep.setEnabled(false);
                }
            }
        });
         
        timer.start();*/
        
        //listeAllElements();
        
    }
    
    
    public static void copyFiles(String strPath, String dstPath, boolean javaFilter) throws IOException
    {
        File src = new File(strPath);
        File dest = new File(dstPath);
        
        if (src.isDirectory())
        {
            dest.mkdirs();
            String list[] = src.list();
            
            for (int i = 0; i < list.length; i++)
            {
                if (javaFilter)
                {
                    if (list[i].endsWith(".java")==false && list[i].endsWith(".bat")==false)
                    {
                        //String dest1 = dest.getAbsolutePath() + "\\" + list[i];
                        String dest1 = dest.getAbsolutePath() + File.separator + list[i];
                        //String src1 = src.getAbsolutePath() + "\\" + list[i];
                        String src1 = src.getAbsolutePath() + File.separator + list[i];
                        copyFiles(src1 , dest1,javaFilter);
                    }
                }
                else
                {
                    String dest1 = dest.getAbsolutePath() + "\\" + list[i];
                    String src1 = src.getAbsolutePath() + "\\" + list[i];
                    copyFiles(src1 , dest1,javaFilter);
                }
            }
        }
        else
        {
            Tools.copyHighSpeed(src, dest);
        }
    }
    
    
    public static void copyFile(String src, String dst) throws IOException
    {
        Tools.copyHighSpeed(new File(src), new File(dst));
    }
    
    
    
    public void copyAllFiles(String sourceDir, String destDir)
    {
        File f = new File(sourceDir);
        File[] files=f.listFiles();
        if (files!=null)
        {
            for (int i=0;i<files.length;i++)
            {
                String str=files[i].getPath();
                try
                {
                    new File(destDir).mkdirs();
                    File srcFile = new File(str);
                    if (srcFile.isDirectory()==false)
                    {
                        Tools.copyFile(srcFile, new File(destDir+"/"+srcFile.getName()));
                    }
                    else
                    {
                        String sss=destDir+"/"+srcFile.getPath();
                        copyAllFiles(sss,destDir);
                        //new File(destDir+"/"+srcFile.getPath()).mkdirs();
                    }
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    private void createBatchFile(String destFilename, String mainVM)
    {
        try
        {
            BufferedWriter out = new BufferedWriter(new FileWriter(destFilename));
            
            out.write("javaw -splash:splash.png -jar c-exp-lab.jar Elements \"./"+mainVM+"\"");
            out.close();
        }
        catch(Exception ex)
        {
            Tools.showMessage(ex.toString());
        }
        
    }
    
    public void createDistribution(String destPath)
    {
        Basis basis=getBasis();
        ArrayList list = new ArrayList();
        basis.addPublishingFiles(list);
        
        String elementPath=basis.getElementPath();
        //String destPath="C:/Dokumente und Einstellungen/Salafia/Desktop/test1";
        new File(destPath).mkdir();
        String destElementPath=destPath+"/Elements";
        new File(destElementPath).mkdir();
        try
        {
            copyFile(elementPath+"/../splash.png",destElementPath+"/../splash.png");
            copyFile(elementPath+"/../license.txt",destElementPath+"/../license.txt");
            copyFile(elementPath+"/../lgpl.txt",destElementPath+"/../lgpl.txt");
            
            copyFiles(elementPath+"/CircuitElements/Extras/SubVM",destElementPath+"/CircuitElements/Extras/SubVM", true);
            copyFiles(elementPath+"/CircuitElements/SubVM",destElementPath+"/CircuitElements/SubVM", true);
            
            copyFiles(elementPath+"/tools",destElementPath+"/tools", true);
            copyFiles(elementPath+"/MyParser",destElementPath+"/MyParser", true);
            copyFiles(elementPath+"/MyGraph",destElementPath+"/MyGraph", true);
            copyFiles(elementPath+"/VisualLogic",destElementPath+"/VisualLogic", true);
            copyFiles(elementPath+"/SVGViewer",destElementPath+"/SVGViewer", true);
            copyFiles(elementPath+"/../c-exp-lab.jar",destElementPath+"/../c-exp-lab.jar", true);
            copyFiles(elementPath+"/../lib",destElementPath+"/../lib", true);
            String mainVM=new File(getBasis().fileName).getName();
            createBatchFile(destElementPath+"/../start.bat", mainVM);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        
        // Kopiere aktuelle VM!
        String str="";
        
        try
        {
            str=getBasis().fileName;
            Tools.copyFile(new File(str),new File(destElementPath+"/../"+new File(str).getName()));
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        
        String strName="";
        for (int i=0;i<list.size();i++)
        {
            str=(String)list.get(i);
            if (!str.trim().equalsIgnoreCase(""))
            {
                String res[]=str.split("&");
                
                if (res.length!=2)
                {
                    System.out.println("Error!");
                }
                if (res.length==2)
                {
                    strName=res[0];
                    str=res[1];
                    
                    if (strName.trim().equalsIgnoreCase("VM-Element"))
                    {
                        // Kopiere das VM-File
                        if (str.endsWith(".vlogic"))
                        {
                            String fn1=new File(elementPath+""+str).getParentFile().getPath();
                            String fn2=new File(destElementPath+""+str).getParentFile().getPath();
                            //new File(fn2).mkdirs();
                            
                            try
                            {
                                copyFiles(fn1,fn2,false);
                            }
                            catch (IOException ex)
                            {
                                ex.printStackTrace();
                            }
                        }
                        else
                        {
                            try
                            {
                                if (new File(destElementPath+str+"/bin").exists()==false)
                                {
                                    copyFiles(elementPath+str+"/bin",destElementPath+str+"/bin",false);
                                }
                            }
                            catch (IOException ex)
                            {
                                ex.printStackTrace();
                            }
                        }
                        
                    }
                    else
                    {
                        if (str.endsWith(".vlogic"))
                        {
                            try
                            {
                                Tools.copyFile(new File(str),new File(destElementPath+"/../"+new File(str).getName()));
                            }
                            catch (IOException ex)
                            {
                                ex.printStackTrace();
                            }
                        }
                        else
                        {
                            String strX=str;
                            
                            
                            DFProperties defFile=Tools.getProertiesFromDefinitionFile(new File(elementPath+str));
                            if (!defFile.redirect.equalsIgnoreCase(""))
                            {
                                strX="/"+defFile.redirect;
                                try
                                {
                                    copyFiles(elementPath+str,destElementPath+str,false);
                                }
                                catch (IOException ex)
                                {
                                    ex.printStackTrace();
                                }
                            }
                            
                            try
                            {
                                if (new File(destElementPath+strX+"/bin").exists()==false)
                                {
                                    copyFiles(elementPath+strX+"/bin",destElementPath+strX+"/bin",false);
                                    new File(destElementPath+str+"/binX/doc.html").delete();
                                    new File(destElementPath+str+"/binX/doc_en.html").delete();
                                    new File(destElementPath+str+"/binX/doc_es.html").delete();
                                    
                                }
                            }
                            catch (IOException ex)
                            {
                                ex.printStackTrace();
                            }
                        }
                    }
                    System.out.println(""+i+": Name,Path = "+strName+"   |   "+str);
                }
            }
            
        }
        
    }
    public void saveJpg()
    {
        if (basis.vmProtected) return;
        
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File(basis.letztesVerzeichniss));
        
        chooser.setDialogTitle(java.util.ResourceBundle.getBundle("VisualLogic/FrameCircuit").getString("Exportieren_als_JPG"));
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        
        SimpleFilter filter = new SimpleFilter("jpg", java.util.ResourceBundle.getBundle("VisualLogic/FrameCircuit").getString("JPEG_Image_Files"));
        chooser.setFileFilter(filter);
        
        int value = chooser.showSaveDialog(this);
        if (value == JFileChooser.APPROVE_OPTION)
        {
            File file = chooser.getSelectedFile();
            
            String fileName=file.getPath();
            String s = file.getName();
            int i = s.lastIndexOf('.');
            
            if (i > 0)
            {
            }
            else
            {
                fileName+=".jpg";
            }
            
            try
            {
                getVMObject().saveAsJPEG(fileName);
                                
            }
            catch (Exception ex)
            {
                basis.showErrorMessage(java.util.ResourceBundle.getBundle("VisualLogic/FrameCircuit").getString("error_save_JPG"));
            }
        }
        
    }
    
    public void clearWindow()
    {
        while(basis.getCircuitBasis().getElementCount()>0)
        {
            basis.getCircuitBasis().deleteElement(basis.getCircuitBasis().getElement(0));
        }
    }
    
    public void startBasis()
    {
        if (!getBasis().isLoading())
        {
            basis.start(false);
            
            VMObject frontBasis = basis.getFrontBasis();
            frontBasis.startPanel();
        }
    }
    
    public void startBasisDebug()
    {
        if (!getBasis().isLoading())
        {
            basis.start(true);
            VMObject frontBasis = basis.getFrontBasis();
            frontBasis.startPanel();
        }
    }
    
    public void stopBasis()
    {
        if (!getBasis().isLoading())
        {
            basis.stop();
            VMObject frontBasis = basis.getFrontBasis();
            frontBasis.stop();
        }
    }
    
    public void resumeBasis()
    {
        if (!getBasis().isLoading())
        {
            basis.resume();
            VMObject frontBasis = basis.getFrontBasis();
            frontBasis.resume();
        }
    }
    
    public void pauseBasis()
    {
        if (!getBasis().isLoading())
        {
            basis.pause();
            VMObject frontBasis = basis.getFrontBasis();
            frontBasis.pause();
        }
    }
    
    public void stepBasis()
    {
        if (!getBasis().isLoading())
        {
            basis.step();
            VMObject frontBasis = basis.getFrontBasis();
            frontBasis.step();
        }
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPaneCircuitPanel = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPaneFrontPanel = new javax.swing.JScrollPane();

        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jScrollPaneCircuitPanel.setBackground(new java.awt.Color(102, 102, 102));
        jScrollPaneCircuitPanel.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPaneCircuitPanel.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPaneCircuitPanel.setMinimumSize(new java.awt.Dimension(60, 24));
        jScrollPaneCircuitPanel.setPreferredSize(new java.awt.Dimension(100, 20));
        jScrollPaneCircuitPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jScrollPaneCircuitPanelComponentResized(evt);
            }
        });
        jScrollPaneCircuitPanel.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jScrollPaneCircuitPanelPropertyChange(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 555, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 248, Short.MAX_VALUE)
        );

        jScrollPaneCircuitPanel.setViewportView(jPanel1);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("VisualLogic/FrameCircuit"); // NOI18N
        jTabbedPane1.addTab(bundle.getString("Circuit"), jScrollPaneCircuitPanel); // NOI18N
        jTabbedPane1.addTab(bundle.getString("Frontpanel"), jScrollPaneFrontPanel); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    
    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_jTabbedPane1StateChanged
    {//GEN-HEADEREND:event_jTabbedPane1StateChanged
        if (basis!=null)
        {
            if (jTabbedPane1.getSelectedIndex()==0)
            {
                setVMObject(basis.getCircuitBasis()) ;
                //listeAllElements();
            }
            else
                if (jTabbedPane1.getSelectedIndex()==1)
                {
                setVMObject(basis.getFrontBasis());
                //listeAllElements();
                }
        }
    }//GEN-LAST:event_jTabbedPane1StateChanged
    
    private void jScrollPaneCircuitPanelPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_jScrollPaneCircuitPanelPropertyChange
    {//GEN-HEADEREND:event_jScrollPaneCircuitPanelPropertyChange
        
    }//GEN-LAST:event_jScrollPaneCircuitPanelPropertyChange
    
    private void jScrollPaneCircuitPanelComponentResized(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_jScrollPaneCircuitPanelComponentResized
    {//GEN-HEADEREND:event_jScrollPaneCircuitPanelComponentResized
        
    }//GEN-LAST:event_jScrollPaneCircuitPanelComponentResized

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    public javax.swing.JScrollPane jScrollPaneCircuitPanel;
    public javax.swing.JScrollPane jScrollPaneFrontPanel;
    public javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
    
}
