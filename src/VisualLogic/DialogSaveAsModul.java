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
import java.awt.Desktop;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;

/**
 *
 * @author Carmelo
 */
public class DialogSaveAsModul extends javax.swing.JDialog implements ElementPaletteIF {

    private ElementPalette elementPalette;
    private MyImage image = new MyImage();
    private String letztesVerzeichniss = ".";
    private boolean editing = false;
    private FrameMain frameCircuit;
    private boolean isCircuitElement = true;
    private boolean modeEdit = false;
    private String editBaseDir = "";

    /**
     * Creates new form DialogSaveAsModul
     */
    public DialogSaveAsModul(java.awt.Frame parent, FrameMain frameCircuit, boolean modal) {
        super(parent, modal);
        initComponents();
        
        
        this.frameCircuit = frameCircuit;
        elementPalette = new ElementPalette(frameCircuit);
        elementPalette.setGruppenAuswahlMode(true);

        //elementPalette.rootCircuitPath+="/2user-defined/";
        //elementPalette.rootFrontPath+="/2user-defined/";
        jPanel2.add(image, java.awt.BorderLayout.CENTER);
        image.setBackground(Color.WHITE);

        java.awt.event.ActionListener actionListener = new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
                result = false;
                dispose();
            }
        };

        javax.swing.KeyStroke stroke = javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(actionListener, stroke, javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);

    }

    public void savedocFile(JEditorPane pane, String filename) {
        try {
            FileWriter out = new FileWriter(new File(filename));
            pane.setContentType("text/html");
            pane.write(out);
            out.flush();
            out.close();

        } catch (IOException ioe) {
            VisualLogic.Tools.showMessage(ioe.toString());
        }

    }

    public void loadFile(JEditorPane pane, String filename) {

        try {
            pane.setPage(new URL("file:" + filename));
            pane.setContentType("text/html");
            pane.setCaretPosition(0);

        } catch (Exception e) {
            //VisualLogic.Tools.showMessage(e.toString());
        }
    }

    public void executeNewDirectory() {
        //jPanel1.setVisible(false);
        setTitle(java.util.ResourceBundle.getBundle("VisualLogic/ElementPalette").getString("New_Directory"));
        jPanel9.setVisible(false);

        jCheckBox1.setVisible(false);
    }

    public void executeEditDirectory(String directory) {
        //jPanel1.setVisible(false);
        editBaseDir=directory;
        txtName.setEnabled(false);
        setTitle(java.util.ResourceBundle.getBundle("VisualLogic/ElementPalette").getString("edit_dir"));
        jPanel9.setVisible(false);

        jCheckBox1.setVisible(false);

        DFProperties definition_def = Tools.getProertiesFromDefinitionFile(new File(directory));
        txtDE.setText(definition_def.captionDE);
        txtEN.setText(definition_def.captionEN);
        txtES.setText(definition_def.captionES);

        int index = directory.lastIndexOf("\\");

        String n = directory.substring(index + 1, directory.length());

        //Basis basis=frameCircuit.basis;
        txtIcon.setText(directory + "/" + definition_def.iconFilename);

        openImage(txtIcon.getText());

        txtName.setText(n);
    }

    private void createDocDirsIfNotExist(String baseDir) {

        File doc_de = new File(baseDir + "/doc_de");
        File doc_en = new File(baseDir + "/doc_en");
        File doc_es = new File(baseDir + "/doc_es");

        String html = "";
        html += "<html>\n";
        html += "<head>\n";
        html += "<title>#title#</title>\n";
        html += "</head>\n";
        html += "<body>\n";
        html += "#body#\n";
        html += "</body>\n";
        html += "</html>\n";

        if (!doc_de.exists()) {
            doc_de.mkdirs();

            try (PrintWriter out = new PrintWriter(baseDir + "/doc_de/index.html")) {

                String html_de = html;
                html_de = html_de.replaceAll("#title#", "Hier den Titel");
                html_de = html_de.replaceAll("#body#", "Hier den Text");
                out.println(html_de);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DialogSaveAsModul.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (!doc_en.exists()) {
            doc_en.mkdirs();

            try (PrintWriter out = new PrintWriter(baseDir + "/doc_en/index.html")) {
                String html_en = html;
                html_en = html_en.replaceAll("#title#", "Here your Title");
                html_en = html_en.replaceAll("#body#", "Here your Text");
                out.println(html_en);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DialogSaveAsModul.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (!doc_es.exists()) {
            doc_es.mkdirs();
            try (PrintWriter out = new PrintWriter(baseDir + "/doc_es/index.html")) {
                String html_es = html;
                html_es = html_es.replaceAll("#title#", "Aquí su Título");
                html_es = html_es.replaceAll("#body#", "Aquí el texto");
                out.println(html_es);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DialogSaveAsModul.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        File description_de = new File(baseDir + "/description_de.html");
        if (!description_de.exists()) {

            try (PrintWriter out = new PrintWriter(baseDir + "/description_de.html")) {

                out.println("Text here!");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DialogSaveAsModul.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        File description_en = new File(baseDir + "/description_en.html");
        if (!description_en.exists()) {

            try (PrintWriter out = new PrintWriter(baseDir + "/description_en.html")) {

                out.println("Text here!");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DialogSaveAsModul.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        File description_es = new File(baseDir + "/description_es.html");
        if (!description_es.exists()) {

            try (PrintWriter out = new PrintWriter(baseDir + "/description_es.html")) {

                out.println("Text here!");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DialogSaveAsModul.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    

    public void executeEdit(String baseDir) {

        createDocDirsIfNotExist(baseDir);

        editBaseDir = baseDir;
        setTitle(java.util.ResourceBundle.getBundle("VisualLogic/ElementPalette").getString("Menu_Edit"));
        txtName.setEnabled(false);

        modeEdit = true;
        execute();
        // 1. aus dem Verzeichniss die definition.def auslesen
        //    Icon und Hilfe auch!
        // 2. aus der definition.def die Daten anzeigen
        //    Icon und Hilfe auch!
        // 3. definition.def anhand der neuen Daten neu generieren
        //    Icon und Hilfe auch!

        DFProperties definition_def = Tools.getProertiesFromDefinitionFile(new File(baseDir));

        if (definition_def.vm.length() == 0) {
            return;
        }

        if (new File(baseDir + "/description_de.html").exists()) {
            txtDescriptionDE.setText(Tools.readFile(baseDir + "/description_de.html"));
        }
        if (new File(baseDir + "/description_en.html").exists()) {
            txtDescriptionEN.setText(Tools.readFile(baseDir + "/description_en.html"));
        }
        if (new File(baseDir + "/description_es.html").exists()) {
            txtDescriptionES.setText(Tools.readFile(baseDir + "/description_es.html"));
        }

        txtDE.setText(definition_def.captionDE);
        txtEN.setText(definition_def.captionEN);
        txtES.setText(definition_def.captionES);
        String extension = Tools.getExtension(new File(definition_def.vm));

        editing = true;

        if (definition_def.vm.length() > 0) {
            txtName.setText(definition_def.vm.substring(0, definition_def.vm.length() - extension.length() - 1));
        }

        String oldXIcon = baseDir + "/" + definition_def.iconFilename;
        txtIcon.setText(oldXIcon);
        letztesVerzeichniss = baseDir;

        // loadFile(editorDE, baseDir + "/doc.html");
        // loadFile(editorEN, baseDir + "/doc_en.html");
        // loadFile(editorES, baseDir + "/doc_es.html");
        openImage(txtIcon.getText());

        setVisible(true);

        if (result) {
            //    savedocFile(editorDE, baseDir + "/doc.html");
            //  savedocFile(editorEN, baseDir + "/doc_en.html");
            //  savedocFile(editorES, baseDir + "/doc_es.html");

            try (PrintWriter out = new PrintWriter(baseDir + "/description_de.html")) {

                out.println(txtDescriptionDE.getText());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DialogSaveAsModul.class.getName()).log(Level.SEVERE, null, ex);
            }
            try (PrintWriter out = new PrintWriter(baseDir + "/description_en.html")) {

                out.println(txtDescriptionEN.getText());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DialogSaveAsModul.class.getName()).log(Level.SEVERE, null, ex);
            }
            try (PrintWriter out = new PrintWriter(baseDir + "/description_es.html")) {

                out.println(txtDescriptionES.getText());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DialogSaveAsModul.class.getName()).log(Level.SEVERE, null, ex);
            }

            String ext = Tools.getExtension(new File(xicon));

            DFProperties definition_def2 = new DFProperties();
            File f1 = new File(oldXIcon);
            File f2 = new File(xicon);
            String ff1 = f1.getAbsolutePath();
            String ff2 = f2.getAbsolutePath();
            if (!ff1.equalsIgnoreCase(ff2)) {
                new File(oldXIcon).delete();

                try {
                    Tools.copyFile(new File(xicon), new File(baseDir + "/icon." + ext));
                } catch (Exception ex) {
                    Tools.showMessage(java.util.ResourceBundle.getBundle("VisualLogic/DialogSaveAsModul").getString("ERROR: COPYING ICON FILE!"));
                }
            }

            definition_def2.captionDE = caption_DE;
            definition_def2.captionEN = caption_EN;
            definition_def2.captionES = caption_ES;
            definition_def2.resizeSynchron = resizeProportional;
            definition_def2.classcircuit = "";

            definition_def2.iconFilename = new File(xicon).getName();
            definition_def2.classfront = definition_def.classfront;
            System.out.println("definition_def.classfront=" + definition_def2.classfront);

            /*if (circuitElement==true) {
                definition_def2.classfront="";
            }else {
                definition_def2.classfront="TRUE";
            }*/
            definition_def2.vm = xname + ".vlogic";

            Tools.saveDefinitionFile(new File(baseDir), definition_def2);
            //frameCircuit.elementPalette.loadFolder(frameCircuit.elementPalette.aktuellesVerzeichniss);
        }
    }

    public void executeNew() {

        jButton4.setVisible(false);
        jCheckBox1.setSelected(true);
        editing = false;
        execute();
        setVisible(true);
        if (result) {
            // 1. Verzeichniss für das Element generieren (unter group!)
            // 2. Icon von der Quelle zum verzeichniss kopieren
            // 3. definition.def in das Verzeichniss generieren

            String baseDir = frameCircuit.getActualBasis().getElementPath() + group + "/" + xname;

            createDocDirsIfNotExist(baseDir);

            baseDir = Tools.mapFile(baseDir);

            File file = new File(baseDir);

            if (!file.exists()) {
                boolean success = file.mkdir();
                if (!success) {
                    Tools.showMessage("Error: Directory\"" + file.getPath() + "\" not created!");
                }
            }

            String vmFile = baseDir;

            frameCircuit.getActualBasis().saveToFile(vmFile + "/" + xname + ".vlogic", false);

            //savedocFile(editorDE, baseDir + "/doc.html");
            //savedocFile(editorEN, baseDir + "/doc_en.html");
            //savedocFile(editorES, baseDir + "/doc_es.html");
            String ext = Tools.getExtension(new File(xicon));

            try {
                Tools.copyFile(new File(xicon), new File(baseDir + "/icon." + ext));
            } catch (Exception ex) {
                Tools.showMessage("Error: copying icon file!");
            }

            DFProperties definition_def = new DFProperties();
            definition_def.iconFilename = "icon." + ext;
            definition_def.captionDE = caption_DE;
            definition_def.captionEN = caption_EN;
            definition_def.captionES = caption_ES;
            definition_def.classcircuit = "";
            definition_def.resizeSynchron = resizeProportional;

            if (circuitElement == true) {
                definition_def.classfront = "";
            } else {
                definition_def.classfront = "TRUE";
            }

            definition_def.vm = xname + ".vlogic";

            Tools.saveDefinitionFile(new File(baseDir), definition_def);
        }

    }

    public void execute() {
        if (modeEdit == false) {
            Basis basis = frameCircuit.getActualBasis();

            int c = basis.getFrontBasis().getElementCount();
            if (c == 0) {
                elementPalette.aktuellesVerzeichniss = "/CircuitElements/";
                isCircuitElement = true;
            } else {
                elementPalette.aktuellesVerzeichniss = "/FrontElements/";
                isCircuitElement = false;
            }

            elementPalette.listOnlyUUIDs = true;
            elementPalette.init(this, null, basis.getElementPath(), elementPalette.aktuellesVerzeichniss);
            txtIcon.setText(basis.getElementPath() + "/element.gif");
        }

        jCheckBox1.setSelected(resizeProportional);

        openImage(txtIcon.getText());

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        txtName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtDescriptionES = new javax.swing.JTextPane();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtIcon = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        txtES = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtDE = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtEN = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtDescriptionDE = new javax.swing.JTextPane();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtDescriptionEN = new javax.swing.JTextPane();
        jPanel9 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("VisualLogic/FrameCircuit"); // NOI18N
        setTitle(bundle.getString("Als_Modul_speichern")); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("VisualLogic/DialogSaveAsModul"); // NOI18N
        jButton1.setText(bundle1.getString("CANCEL")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText(bundle1.getString("OK")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel5.setText(bundle1.getString("NAME : ")); // NOI18N

        jCheckBox1.setText(bundle1.getString("resize_proportional")); // NOI18N
        jCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel6.setText(bundle1.getString("Beschreibung_Deutsch")); // NOI18N

        jScrollPane4.setViewportView(txtDescriptionES);

        jLabel2.setText(bundle1.getString("ICON")); // NOI18N

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new java.awt.BorderLayout());

        txtIcon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIconKeyPressed(evt);
            }
        });

        jButton4.setText(bundle1.getString("EDIT ICON")); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText(bundle1.getString("Reload")); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        java.util.ResourceBundle bundle2 = java.util.ResourceBundle.getBundle("VisualLogic/frmOptions"); // NOI18N
        jButton3.setText(bundle2.getString("durchsuchen")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel1.setText(bundle1.getString("GERMAN")); // NOI18N

        txtDE.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabel3.setText(bundle1.getString("ENGLISH")); // NOI18N

        jLabel4.setText(bundle1.getString("SPANISCH")); // NOI18N

        jLabel7.setText(bundle1.getString("Beschreibung_Englisch")); // NOI18N

        jScrollPane5.setViewportView(txtDescriptionDE);

        jLabel9.setText(bundle1.getString("Beschreibng_Spanisch")); // NOI18N

        jScrollPane6.setViewportView(txtDescriptionEN);

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane4)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel5)
                            .add(jLabel2)
                            .add(jLabel1)
                            .add(jLabel3)
                            .add(jLabel4))
                        .add(22, 22, 22)
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jCheckBox1)
                            .add(jPanel4Layout.createSequentialGroup()
                                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(18, 18, 18)
                                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jPanel4Layout.createSequentialGroup()
                                        .add(jButton4)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                        .add(jButton5)
                                        .add(0, 281, Short.MAX_VALUE))
                                    .add(jPanel4Layout.createSequentialGroup()
                                        .add(txtIcon)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jButton3))))
                            .add(txtName)
                            .add(txtDE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(txtEN)
                            .add(txtES)))
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel6)
                            .add(jLabel7)
                            .add(jLabel9))
                        .add(0, 0, Short.MAX_VALUE))
                    .add(jScrollPane5)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane6))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(txtName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jLabel2)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(txtIcon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jButton3))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jButton4)
                            .add(jButton5)))
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(txtDE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(txtEN, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(txtES, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jCheckBox1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel6)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 74, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(13, 13, 13)
                .add(jLabel7)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 74, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel9)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 74, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(bundle1.getString("Settings"), jPanel4); // NOI18N

        jButton6.setText(bundle1.getString("Öffne das Deutsch Dokumentations-Verzeichnis")); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText(bundle1.getString("Öffne das Englische Dokumentations-Verzeichnis")); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText(bundle1.getString("Öffne das Spanische Dokumentations-Verzeichnis")); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel8.setText("Öffne das Dokumentations-Verzeichnis um mit Deinen HTML Editor die index.html zu bearbeiten");

        org.jdesktop.layout.GroupLayout jPanel9Layout = new org.jdesktop.layout.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel8)
                    .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jButton8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jButton6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jButton7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(140, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel8)
                .add(13, 13, 13)
                .add(jButton6)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton7)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton8)
                .addContainerGap(402, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(bundle1.getString("Dokumentation"), jPanel9); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jTabbedPane1)
                    .add(layout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(jButton2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jTabbedPane1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton1)
                    .add(jButton2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowActivated
    {//GEN-HEADEREND:event_formWindowActivated
// TODO add your handling code here:
    }//GEN-LAST:event_formWindowActivated

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed


    }//GEN-LAST:event_formWindowClosed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        String param = new File(txtIcon.getText()).getPath();

        Tools.openPaint(new File(param));


    }//GEN-LAST:event_jButton4ActionPerformed

    private void txtIconKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtIconKeyPressed
    {//GEN-HEADEREND:event_txtIconKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            openImage(txtIcon.getText());
        }
    }//GEN-LAST:event_txtIconKeyPressed

    private void openImage(String filename) {

        Image img = getToolkit().getImage(filename);
        image.setImage(img);

    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File(letztesVerzeichniss));

        ExtensionFileFilter filter = new ExtensionFileFilter();

        filter.addExtension("gif");
        filter.addExtension("png");
        filter.addExtension("jpg");

        filter.setDescription("gif, png, jpg");

        chooser.addChoosableFileFilter(filter);

        int value = chooser.showOpenDialog(this);

        if (value == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            letztesVerzeichniss = chooser.getCurrentDirectory().getPath();

            String fileName = file.getPath();
            txtIcon.setText(fileName);
            openImage(fileName);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        txtName.setText(Tools.bereinigeDateiname(txtName.getText()));

        if (modeEdit) {
            circuitElement = isCircuitElement;
            xname = txtName.getText();
            resizeProportional = jCheckBox1.isSelected();

            if (editing) {
                if (!xname.equalsIgnoreCase("")) {
                    caption_DE = txtDE.getText();
                    caption_EN = txtEN.getText();
                    caption_ES = txtES.getText();

                    xicon = txtIcon.getText();

                    //doc_DE = editorDE.getText();
                    //doc_EN = editorEN.getText();
                    //doc_ES = editorES.getText();
                    result = true;
                    dispose();
                } else {
                    Tools.showMessage("Name is invalid!");
                }

            }
        } else {
            circuitElement = isCircuitElement;
            xname = txtName.getText();
            resizeProportional = jCheckBox1.isSelected();
            group = elementPalette.aktuellesVerzeichniss;
            String baseDir = frameCircuit.getActualBasis().getElementPath() + group + xname;
            baseDir = Tools.mapFile(baseDir);

            if (editing) {
                if (!xname.equalsIgnoreCase("")) {
                    caption_DE = txtDE.getText();
                    caption_EN = txtEN.getText();
                    caption_ES = txtES.getText();

                    xicon = txtIcon.getText();

                    //doc_DE = editorDE.getText();
                    //doc_EN = editorEN.getText();
                    // doc_ES = editorES.getText();
                    result = true;
                    dispose();
                } else {
                    Tools.showMessage("Name is invalid!");
                }

            } else if (!xname.equalsIgnoreCase("") && new File(baseDir).exists() == false) {
                caption_DE = txtDE.getText();
                caption_EN = txtEN.getText();
                caption_ES = txtES.getText();

                xicon = txtIcon.getText();

                // doc_DE = editorDE.getText();
                //doc_EN = editorEN.getText();
                // doc_ES = editorES.getText();
                result = true;
                dispose();
            } else {
                Tools.showMessage("Name is invalid!");
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        result = false;
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        openImage(txtIcon.getText());
    }//GEN-LAST:event_jButton5ActionPerformed

    private void openDocDir(String lang) {
        Desktop desktop = Desktop.getDesktop();
                
        File dirToOpen = null;
        try {

            
            dirToOpen = new File(editBaseDir + "/doc_" + lang);
            
            desktop.open(dirToOpen);
        } catch (Exception ex) {
            Tools.showMessage(this, ex.toString());
        }
    }


    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        openDocDir("de");
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        openDocDir("en");
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        openDocDir("es");
    }//GEN-LAST:event_jButton8ActionPerformed

    public void onButtonClicken(String[] params) {
    }

    public static String xicon = "";
    public static String xname = "";
    public static boolean resizeProportional = true;
    public static String caption_DE = "";
    public static String caption_EN = "";
    public static String caption_ES = "";
    public static boolean circuitElement = false;

    public static String doc_DE = "";
    public static String doc_EN = "";
    public static String doc_ES = "";
    public static String group = "";

    public static boolean result = false;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField txtDE;
    private javax.swing.JTextPane txtDescriptionDE;
    private javax.swing.JTextPane txtDescriptionEN;
    private javax.swing.JTextPane txtDescriptionES;
    private javax.swing.JTextField txtEN;
    private javax.swing.JTextField txtES;
    private javax.swing.JTextField txtIcon;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables

}
