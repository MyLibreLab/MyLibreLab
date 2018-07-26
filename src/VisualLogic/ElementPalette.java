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

import create_new_group.Dialog_create_new_group;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

class MyButton extends JButton {

    public String filePath;
    public String caption;
}

class CommandAction extends AbstractAction {

    String[] params;
    ElementPaletteIF elementPalette;

    public CommandAction(String[] params, ElementPaletteIF elementPalette) {
        super("");
        this.params = params;
        this.elementPalette = elementPalette;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        elementPalette.onButtonClicken(params);
    }
}

public class ElementPalette extends javax.swing.JPanel {

    public boolean listOnlyUUIDs = false;
    public String thePath = "";
    private boolean areVMsEditable = false;
    private String elementPath;
    public FrameMain frameCircuit = null;
    private ElementPaletteIF elementPalette;
    public String activeElement;
    public String aktuellesVerzeichniss;
    public VMObject vmObject = null;
    private ElementPalette frm = this;
    private MyButton aktiveButton = null;
    public static int MODE_NONE = 0;
    public static int MODE_COPY = 1;
    public static int MODE_CUT = 2;
    public int modus = MODE_NONE;
    private String aktuellesVerz = "";
    private String toCopyPath = "";
    private boolean modusCut = false;
    private boolean gruppenAuswahlMode = false;
    public String rootPath;
    private FrameMain owner = null;
    private Basis basis = null;

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (int i = 0; i < jPanelButtons.getComponentCount(); i++) {
            jPanelButtons.getComponent(i).setEnabled(enabled);
        }
    }

    public ElementPalette(FrameMain owner) {
        this.owner = owner;
        initComponents();
    }

    public void init(ElementPaletteIF elementPalette, Basis basis, String elementPath, String rootPath) {

        this.basis = basis;
        this.elementPalette = elementPalette;
        this.elementPath = elementPath;
        this.aktuellesVerzeichniss = rootPath;
        this.rootPath = rootPath;

        if (basis != null) {

            if (rootPath.contains("CircuitElements")) {

                if (basis.elementPaletteCircuitElementsOldPath.length() > 0) {
                    aktuellesVerzeichniss = basis.elementPaletteCircuitElementsOldPath;
                }
            } else if (basis.elementPaletteFrontElementsOldPath.length() > 0) {
                aktuellesVerzeichniss = basis.elementPaletteFrontElementsOldPath;
            }
        }

        loadFolder(aktuellesVerzeichniss);

    }

    public void setGruppenAuswahlMode(boolean value) {
        gruppenAuswahlMode = value;
    }

    public String getEndung(String fileName) {
        char ch;

        for (int i = 0; i < fileName.length(); i++) {
            ch = fileName.charAt(i);

            if (ch == '.') {
                return fileName.substring(i + 1);
            }
        }
        return null;
    }

    public Vector getSortDefinitionFile(File file) {
        Vector list = new Vector();
        try {
            BufferedReader input = new BufferedReader(new FileReader(file.getAbsolutePath() + "/" + "sort.def"));
            String inputString;
            while ((inputString = input.readLine()) != null) {
                list.add(inputString);
            }
            input.close();
        } catch (Exception ex) {
            //System.out.println("FEHLER : "+ex.getMessage());
        }
        return list;
    }

    public BufferedImage loadTransparentImage(String fileName) {
        Image image = Toolkit.getDefaultToolkit().getImage(fileName);
        image = Transparency.makeColorTransparent(image, new Color(0).white);

        MediaTracker mc = new MediaTracker(this);
        mc.addImage(image, 0);

        try {
            mc.waitForID(0);
        } catch (InterruptedException ex) {
        }

        BufferedImage bufferedimage = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);

        Graphics g = bufferedimage.createGraphics();
        int x = (32 / 2) - (image.getWidth(this) / 2);
        int y = (32 / 2) - (image.getHeight(this) / 2);
        g.drawImage(image, x, y, this);
        return bufferedimage;
    }

    private int SucheStringInFiles_ResultIDX(String str, File[] files) {
        for (int i = 0; i < files.length; i++) {
            String val = files[i].getName();
            if (str.equalsIgnoreCase(val)) {
                return i;
            }
        }
        return -1;
    }

    private MyButton createBackButton() {
        MyButton btn = new MyButton();
        btn.setPreferredSize(new Dimension(38, 38));
        btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/back16.gif")));

        btn.setEnabled(true);
        if (aktuellesVerzeichniss.equalsIgnoreCase(rootPath)) {
            btn.setEnabled(false);
        }

        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    if (!aktuellesVerzeichniss.equalsIgnoreCase(rootPath)) {
                        String str = aktuellesVerzeichniss.substring(0, aktuellesVerzeichniss.lastIndexOf("/"));
                        loadFolder(str);
                    }
                } catch (Exception ex) {
                    Tools.showMessage(ex.toString());
                }

            }
        });

        return btn;
    }

    private void reihenfolgeSortieren(File f, File files[]) {
        Vector lstReihenfolge = getSortDefinitionFile(f);

        // Sortiere die Files nach der lstReihenfolge!
        for (int i = 0; i < lstReihenfolge.size(); i++) {
            String str = (String) lstReihenfolge.get(i);
            int oldidx = SucheStringInFiles_ResultIDX(str, files);

            if (oldidx > -1) {
                File temp = files[oldidx];
                files[oldidx] = files[i];
                files[i] = temp;
            }
        }
    }

    public void reorderButtons() {
        Component c;
        int d = 0;
        if (jToggleButton1.isSelected()) {
            d = 100;
        }

        int w = 38 + d;
        int h = 38;
        int x = 2;
        int y = 2;
        for (int i = 0; i < jPanelButtons.getComponentCount(); i++) {
            c = jPanelButtons.getComponent(i);

            //((JButton)c).setBorderPainted(false);
            /*if (x + w > jPanelButtons.getWidth()) {
                x = 0;
                y += h;
            }*/
            c.setLocation(x, y);
            c.setSize(w, h);
            x += w;
        }

        /*if (x + w > jPanelButtons.getWidth()) {
            
            if (getParent()!=null){
            getParent().getParent().setPreferredSize(new Dimension(x, h+40));
            }
           
        }else {
            if (getParent()!=null){
            getParent().getParent().setPreferredSize(new Dimension(x, 20));
            }
        }*/
        jPanelButtons.setPreferredSize(new Dimension(x, h));
    }

    private void addButton(JButton button) {
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBackground(new Color(242,242,242,250));
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1,true));
        jPanelButtons.add(button);

        if (jToggleButton1.isSelected()) {
            button.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        } else {
            button.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        }

    }

    public void loadFolder(String path) {

        if (basis != null) {

            if (rootPath.contains("CircuitElements")) {
                basis.elementPaletteCircuitElementsOldPath = path;

            } else {
                basis.elementPaletteFrontElementsOldPath = path;
            }
        }

        jPanelButtons.removeAll();
        jPanelButtons.repaint();

        thePath = path;

        File f;

        String str = Tools.mapFile(elementPath + path);
        f = new File(str);

        aktuellesVerzeichniss = path;

        MyButton backbtn = createBackButton();
        backbtn.setBackground(new Color(255, 255, 255));
        addButton(backbtn);
        //jPanelButtons.add(backbtn);

        if (f.exists()) {
            File files[];
            if (listOnlyUUIDs == true) {
                files = f.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        int cnt = name.split("-").length;
                        if (cnt == 6) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
            } else {
                files = f.listFiles();
            }

            aktuellesVerzeichniss = path;

            String[] slipttedPath = path.split("/");

            String xxx = "";
            String ppp = "";
            for (String slipttedPath1 : slipttedPath) {
                ppp += "/" + slipttedPath1;
                String strx = Tools.mapFile(elementPath + ppp);
                File fx = new File(strx);
                DFProperties thisDirProps = Tools.getProertiesFromDefinitionFile(fx);
                String caption = "";
                Locale locale = Locale.getDefault();
                String lang = locale.getLanguage();
                switch (lang) {
                    case "de": {
                        caption = thisDirProps.captionDE;
                        break;
                    }
                    case "en": {
                        caption = thisDirProps.captionEN;
                        break;
                    }
                    case "es": {
                        caption = thisDirProps.captionES;
                        break;
                    }
                }
                xxx += caption + "/";
            }

            jLabel1.setText(xxx);
            //jLabel1.setToolTipText(path);

            reihenfolgeSortieren(f, files);

            DFProperties thisDirProps = Tools.getProertiesFromDefinitionFile(f);


            /*if (thisDirProps.redirect.trim().length()>0)
             {              
             f = new File(thisDirProps.redirect.trim());              
             }*/
            if (thisDirProps.vm_dir_editable.length() > 0) {
                areVMsEditable = Boolean.valueOf(thisDirProps.vm_dir_editable);
            } else {
                areVMsEditable = false;
            }

            for (int i = 0; i < files.length; i++) {
                File file = files[i];

                if (file.isDirectory()) {
                    // Oeffne die Datei und interpretiere diese
                    DFProperties props = Tools.getProertiesFromDefinitionFile(file);

                    if (!gruppenAuswahlMode && !props.isDirectory && (props.classcircuit.length() > 0 || props.classfront.length() > 0 || props.vm.length() > 0 || props.loader.length() > 0)) {
                        MyButton btn = new MyButton();

                        btn.setPreferredSize(new Dimension(38, 38));

                        // Lade das Icons
                        String imagePath = file.getAbsolutePath() + "/" + props.iconFilename;
                        BufferedImage image = loadTransparentImage(imagePath);

                        String[] params = new String[6];
                        Action actionCmd = new CommandAction(params, elementPalette);

                        if (props.elementImage.length() == 0) {
                            props.elementImage = props.iconFilename;
                        }
                        if (props.loader.length() > 0) {
                            String filename = file.getName();

                            params[0] = props.loader;
                            params[1] = props.classcircuit;
                            params[2] = props.classfront;
                            params[3] = props.elementImage;
                            params[4] = path + "/" + filename;
                            params[5] = "LOADER";

                        } else if (props.vm.length() > 0) {
                            String filename = file.getName();

                            params[0] = path + "/" + filename;
                            params[1] = props.vm;
                            params[2] = props.captionInternationalized;
                            params[3] = params[0] + "/" + props.elementImage;
                            params[4] = props.classfront;
                            params[5] = "VM";
                        } else {
                            String filename = file.getName();

                            params[0] = path + "/" + filename;
                            params[1] = props.classcircuit;
                            params[2] = props.classfront;
                            params[3] = "";
                            params[4] = "";
                            params[5] = "NORMAL";
                        }

                        btn.setAction(actionCmd);
                        Image img = createImage(image.getSource());
                        // setzen des Icons zum Button
                        ImageIcon icon = new ImageIcon(img);
                        btn.setIcon(icon);
                        btn.setBackground(new Color(255, 255, 255));
                        btn.setToolTipText(props.captionInternationalized);

                        btn.filePath = file.getAbsolutePath();
                        btn.caption = props.captionInternationalized;

                        if (jToggleButton1.isSelected()) {
                            btn.setText(btn.caption);
                        }

                        addButton(btn);
                        //jPanelButtons.add(btn);

                        btn.addMouseListener(new java.awt.event.MouseAdapter() {
                            public void mousePressed(java.awt.event.MouseEvent e) {
                                if (e.getButton() == e.BUTTON3) {
                                    MyButton button = (MyButton) e.getSource();
                                    aktiveButton = button;
                                    if (areVMsEditable) {
                                        jPopupMenu1.show(button, e.getX(), getY() + e.getY());
                                    }
                                }
                            }

                        });

                        btn.filePath = file.getAbsolutePath();

                    }

                    if (props.isDirectory) {
                        // is Directory=true
                        MyButton btn = new MyButton();
                        btn.setPreferredSize(new Dimension(38, 38));

                        // Laden des Icons
                        String imagePath = file.getAbsolutePath() + "/" + props.iconFilename;
                        BufferedImage image = loadTransparentImage(imagePath);

                        // Lade den Ordner
                        BufferedImage folder = loadTransparentImage(elementPath + "/" + "arrow.png");

                        // Folder und Icon verknüpfen!
                        //folder.getGraphics().drawImage(image,4,7,null);
                        image.getGraphics().drawImage(folder, 0, 0, null);

                        // setzen des Icons zum Button
                        Image img = createImage(image.getSource());
                        //Image img = createImage(image.getSource());
                        ImageIcon icon = new ImageIcon(img);
                        btn.setIcon(icon);

                        btn.setToolTipText(props.captionInternationalized);
                        btn.setActionCommand(file.getName());
                        //jPanelButtons.add(btn);
                        if (jToggleButton1.isSelected()) {
                            btn.setText(props.captionInternationalized);
                        }
                        addButton(btn);
                        btn.filePath = file.getAbsolutePath();
                        btn.caption = props.captionInternationalized;
                        btn.addMouseListener(new java.awt.event.MouseAdapter() {
                            @Override
                            public void mousePressed(java.awt.event.MouseEvent e) {
                                if (e.getButton() == MouseEvent.BUTTON3) {
                                    MyButton button = (MyButton) e.getSource();
                                    aktiveButton = button;

                                    //if (areVMsEditable) {
                                    jPopupMenu3.show(button, e.getX(), getY() + e.getY());
                                    //}

                                }
                            }
                        });

                        btn.addActionListener(new java.awt.event.ActionListener() {
                            @Override
                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                String cmd = evt.getActionCommand();

                                MyButton button = (MyButton) evt.getSource();
                                loadFolder(thePath + "/" + cmd);
                            }
                        });

                    }
                }
                /* synchronized (getTreeLock()) {
                    validateTree();
                }*/

            }
        } else {
            //Tools.showMessage("Elements Directory not found!");
        }

        reorderButtons();
        //jPanelButtons.setPreferredSize(new Dimension(0,600));
        /*if (jPanelButtons.getComponentCount()>0)
         {
         int index=jPanelButtons.getComponentCount()-1;
         System.out.println("index="+index);

         Component c = jPanelButtons.getComponent(index);
         Point p=c.getLocation();
         System.out.println("y="+p.y);
         jPanelButtons.setPreferredSize(new Dimension(jPanelButtons.getWidth(),p.y));
         }*/
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jmiEditVMDefinition = new javax.swing.JMenuItem();
        jmiEditVM = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jmiCut = new javax.swing.JMenuItem();
        jmiCopy = new javax.swing.JMenuItem();
        jmiDelete = new javax.swing.JMenuItem();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jmiAddVM = new javax.swing.JMenuItem();
        jmiNewDir = new javax.swing.JMenuItem();
        jmiPaste = new javax.swing.JMenuItem();
        jPopupMenu3 = new javax.swing.JPopupMenu();
        jmiEditDir = new javax.swing.JMenuItem();
        jmiDeleteDir = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanelButtons = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jButton1 = new javax.swing.JButton();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("VisualLogic/ElementPalette"); // NOI18N
        jmiEditVMDefinition.setText(bundle.getString("Menu_Edit")); // NOI18N
        jmiEditVMDefinition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiEditVMDefinitionActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jmiEditVMDefinition);

        jmiEditVM.setText(bundle.getString("EditVM")); // NOI18N
        jmiEditVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiEditVMActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jmiEditVM);
        jPopupMenu1.add(jSeparator1);

        jmiCut.setText(bundle.getString("Menu_Cut")); // NOI18N
        jmiCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiCutActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jmiCut);

        jmiCopy.setText(bundle.getString("Menu_Copy")); // NOI18N
        jmiCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiCopyActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jmiCopy);

        jmiDelete.setText(bundle.getString("Menu_delete")); // NOI18N
        jmiDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiDeleteActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jmiDelete);

        jmiAddVM.setText("Add New VM");
        jmiAddVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiAddVMActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jmiAddVM);

        jmiNewDir.setText(bundle.getString("New_Directory")); // NOI18N
        jmiNewDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiNewDirActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jmiNewDir);

        jmiPaste.setText(bundle.getString("Menu_paste")); // NOI18N
        jmiPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiPasteActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jmiPaste);

        jmiEditDir.setText(bundle.getString("edit_dir")); // NOI18N
        jmiEditDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiEditDirActionPerformed(evt);
            }
        });
        jPopupMenu3.add(jmiEditDir);

        jmiDeleteDir.setText(bundle.getString("Menu_delete")); // NOI18N
        jmiDeleteDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiDeleteDirActionPerformed(evt);
            }
        });
        jPopupMenu3.add(jmiDeleteDir);

        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(200, 115));

        jPanel2.setPreferredSize(new java.awt.Dimension(100, 25));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("jLabel1");
        jLabel1.setPreferredSize(new java.awt.Dimension(34, 25));

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE))
        );

        jScrollPane1.setBorder(null);
        jScrollPane1.setFocusable(false);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 70));

        jPanelButtons.setAutoscrolls(true);
        jPanelButtons.setMinimumSize(new java.awt.Dimension(9, 39));
        jPanelButtons.setPreferredSize(new java.awt.Dimension(200, 50));
        jPanelButtons.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanelButtonsMousePressed(evt);
            }
        });
        jPanelButtons.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jPanelButtonsComponentResized(evt);
            }
        });
        jPanelButtons.setLayout(null);

        jButton7.setText("XXX");
        jButton7.setContentAreaFilled(false);
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton7MousePressed(evt);
            }
        });
        jPanelButtons.add(jButton7);
        jButton7.setBounds(2, 2, 51, 23);

        jButton8.setBackground(new java.awt.Color(240, 240, 24));
        jButton8.setText("XXX");
        jButton8.setBorder(null);
        jButton8.setContentAreaFilled(false);
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton8MousePressed(evt);
            }
        });
        jPanelButtons.add(jButton8);
        jButton8.setBounds(57, 2, 19, 15);

        jButton10.setText("XXX");
        jButton10.setContentAreaFilled(false);
        jButton10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton10MousePressed(evt);
            }
        });
        jPanelButtons.add(jButton10);
        jButton10.setBounds(112, 2, 51, 23);

        jScrollPane1.setViewportView(jPanelButtons);

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/Text.png"))); // NOI18N
        jToggleButton1.setToolTipText(bundle.getString("Show_Item_Names")); // NOI18N
        jToggleButton1.setContentAreaFilled(false);
        jToggleButton1.setPreferredSize(new java.awt.Dimension(25, 23));
        jToggleButton1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jToggleButton1StateChanged(evt);
            }
        });
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/view-refresh.png"))); // NOI18N
        jButton1.setToolTipText("Reload");
        jButton1.setContentAreaFilled(false);
        jButton1.setPreferredSize(new java.awt.Dimension(25, 23));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 802, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(jToggleButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 34, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jToggleButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 802, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 109, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 63, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jmiEditVMActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmiEditVMActionPerformed
    {//GEN-HEADEREND:event_jmiEditVMActionPerformed

        if (aktiveButton != null) {
            String str = Tools.mapFile(aktiveButton.filePath);
            frameCircuit.openElement(str);

            //frameCircuit.getActualBasis().openVLogicFileAsFrontPanel(aktiveButton.filePath+"/"+definition_def.vm);
        }

    }//GEN-LAST:event_jmiEditVMActionPerformed

    private void jmiEditDirActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmiEditDirActionPerformed
    {//GEN-HEADEREND:event_jmiEditDirActionPerformed

        /*Dialog_create_new_group frm = new Dialog_create_new_group(frameCircuit, true, "edit", elementPath + aktuellesVerzeichniss);

        
        if (frm.load(aktiveButton.filePath)) {
            frm.setVisible(true);

            loadFolder(aktuellesVerzeichniss);
        } else {
            String msg = java.util.ResourceBundle.getBundle("VisualLogic/ElementPalette").getString("This Group is not editable");
            Tools.showMessage(msg);
        }*/
        Dialog_create_new_group frm2 = new Dialog_create_new_group(this.owner, true, "edit", aktiveButton.filePath);
        frm2.setVisible(true);

        /*DialogSaveAsModul frmSave = new DialogSaveAsModul(frameCircuit, frameCircuit, true);

        frmSave.executeEdit(aktiveButton.filePath);*/
        loadFolder(aktuellesVerzeichniss);

        /*DialogSaveAsModul frm2 = new DialogSaveAsModul(frameCircuit, frameCircuit, true);

        String oldPath = aktiveButton.filePath;
        frm2.executeEdit(oldPath);*/
 /*if (frm2.result) {
            String newPath = elementPath + aktuellesVerzeichniss + "/" + frm2.xname;

            //File file=new File(newPath);

            //if (new File(oldPath).getAbsolutePath().equalsIgnoreCase(new File(newPath).getAbsolutePath()))
            {
                String ext = Tools.getExtension(new File(frm2.xicon));

                String newIcon = oldPath + "/icon." + ext;
                File f1 = new File(frm2.xicon);
                File f2 = new File(newIcon);
                String ff1 = f1.getAbsolutePath();
                String ff2 = f2.getAbsolutePath();
                if (!ff1.equalsIgnoreCase(ff2)) {
                    //new File(oldXIcon).delete();
                    try {
                        Tools.copyFile(new File(frm2.xicon), new File(newIcon));
                    } catch (Exception ex) {
                        //Tools.showMessage("Error: copying icon file!");
                    }
                }

                DFProperties props = new DFProperties();
                props.isDirectory = true;
                props.captionDE = frm2.caption_DE;
                props.captionEN = frm2.caption_EN;
                props.captionES = frm2.caption_ES;
                props.iconFilename = "icon." + ext;
                props.vm_dir_editable = "TRUE";
                Tools.saveDefinitionFile(new File(newPath), props);
                loadFolder(aktuellesVerzeichniss);
            }
        }*/

    }//GEN-LAST:event_jmiEditDirActionPerformed

    private void jmiNewDirActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmiNewDirActionPerformed
    {//GEN-HEADEREND:event_jmiNewDirActionPerformed

        //Dialog_create_new_group frm = new Dialog_create_new_group(frameCircuit, true, "add", elementPath + aktuellesVerzeichniss);
        //frm.setVisible(true);
        //loadFolder(aktuellesVerzeichniss);
        DialogSaveAsModul frm = new DialogSaveAsModul(frameCircuit, frameCircuit, true);

        frm.executeNewDirectory();
        frm.setVisible(true);

        if (frm.result) {
            String newPath = elementPath + aktuellesVerzeichniss + "/" + frm.xname;
            File file = new File(newPath);

            if (!file.exists()) {
                boolean success = file.mkdir();
                if (!success) {
                    Tools.showMessage("Error: Directory\"" + file.getPath() + "\" not created!");
                    return;
                }

                String ext = Tools.getExtension(new File(frm.xicon));

                String newIcon = file.getPath() + "/icon." + ext;
                try {
                    Tools.copyFile(new File(frm.xicon), new File(newIcon));
                } catch (Exception ex) {
                }

                DFProperties props = new DFProperties();
                props.isDirectory = true;
                props.captionDE = frm.caption_DE;
                props.captionEN = frm.caption_EN;
                props.captionES = frm.caption_ES;
                props.iconFilename = "icon." + ext;
                props.vm_dir_editable = "TRUE";
                Tools.saveDefinitionFile(file, props);
            } else {
                Tools.showMessage("Directory already exist : \"" + newPath + "\"");
            }
            loadFolder(aktuellesVerzeichniss);
        }
    }//GEN-LAST:event_jmiNewDirActionPerformed

    private void jmiDeleteDirActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmiDeleteDirActionPerformed
    {//GEN-HEADEREND:event_jmiDeleteDirActionPerformed
        int result = JOptionPane.showConfirmDialog((Component) null, java.util.ResourceBundle.getBundle("VisualLogic/ElementPalette").getString("really_delete") + " : " + aktiveButton.caption, java.util.ResourceBundle.getBundle("VisualLogic/ElementPalette").getString("Attention"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            String path = aktiveButton.filePath;
            Tools.deleteDirectory(new File(path));
            loadFolder(aktuellesVerzeichniss);
        }

    }//GEN-LAST:event_jmiDeleteDirActionPerformed

    private void jmiEditVMDefinitionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmiEditVMDefinitionActionPerformed
    {//GEN-HEADEREND:event_jmiEditVMDefinitionActionPerformed

        DFProperties props = Tools.getProertiesFromDefinitionFile(new File(aktiveButton.filePath));

        if (props.vm.endsWith("vlogic")) {
            if (aktiveButton != null && frameCircuit != null) {
                frameCircuit.editModule(aktiveButton.filePath);
                loadFolder(aktuellesVerzeichniss);
            }
        } else {
            codeeditor.frmDefinitonDefEditor frm2 = new codeeditor.frmDefinitonDefEditor(frameCircuit, true);
            frm2.execute(aktiveButton.filePath);
            loadFolder(aktuellesVerzeichniss);
        }


    }//GEN-LAST:event_jmiEditVMDefinitionActionPerformed

    private void jmiCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiCutActionPerformed
        modus = MODE_COPY;
        modusCut = true;
        toCopyPath = aktiveButton.filePath;
    }//GEN-LAST:event_jmiCutActionPerformed

    private void jmiDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiDeleteActionPerformed

        int result = JOptionPane.showConfirmDialog((Component) null, java.util.ResourceBundle.getBundle("VisualLogic/ElementPalette").getString("really_delete") + " : " + aktiveButton.caption, java.util.ResourceBundle.getBundle("VisualLogic/ElementPalette").getString("Attention"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            String path = aktiveButton.filePath;
            Tools.deleteDirectory(new File(path));
            loadFolder(aktuellesVerzeichniss);
        }


    }//GEN-LAST:event_jmiDeleteActionPerformed

    private void jmiPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiPasteActionPerformed

        if (modus == MODE_COPY) {
            String str = toCopyPath.substring(toCopyPath.lastIndexOf("\\"), toCopyPath.length());
            String path = elementPath + aktuellesVerzeichniss + str;
            if (new File(path).exists() == false) {
                try {
                    Tools.copy(new File(toCopyPath), new File(path));
                    loadFolder(aktuellesVerzeichniss);
                } catch (IOException ex) {
                    Tools.showMessage(ex.toString());
                }
            } else {
                Tools.showMessage(java.util.ResourceBundle.getBundle("VisualLogic/ElementPalette").getString("Element ist bereits vorhanden"));
            }

            if (modusCut) {
                Tools.deleteDirectory(new File(toCopyPath));
            }
            modusCut = false;
            modus = MODE_NONE;
        }


    }//GEN-LAST:event_jmiPasteActionPerformed

    private void jmiCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiCopyActionPerformed
        toCopyPath = aktiveButton.filePath;
        modus = MODE_COPY;
        modusCut = false;
    }//GEN-LAST:event_jmiCopyActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        loadFolder(aktuellesVerzeichniss);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jToggleButton1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jToggleButton1StateChanged

    }//GEN-LAST:event_jToggleButton1StateChanged

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        loadFolder(aktuellesVerzeichniss);
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }


    private void jmiAddVMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiAddVMActionPerformed

        String name = JOptionPane.showInputDialog("Please enter Element Name");

        if (name != null) {
            name = Tools.bereinigeDateiname(name);

            //String name="test";
            new File(elementPath + aktuellesVerzeichniss + "/" + name).mkdir();
            String filename = elementPath + aktuellesVerzeichniss + "/" + name + "/" + name + ".vlogic"; //NOI18N 

            if (!new File(filename).exists()) {

                String icon_filename = elementPath + aktuellesVerzeichniss + "/" + name + "/icon32.png";

                try {
                    copyFileUsingStream(new File(elementPath + "/element.png"), new File(icon_filename));
                } catch (IOException ex) {
                    Logger.getLogger(Dialog_create_new_group.class.getName()).log(Level.SEVERE, null, ex);
                }

                Basis basis = new Basis(owner, FrameMain.elementPath);
                basis.saveToFile(filename, false);
                loadFolder(aktuellesVerzeichniss);

                DFProperties definition_def = new DFProperties();
                definition_def.iconFilename = "icon32.png";

                definition_def.captionDE = "";
                definition_def.captionEN = "";
                definition_def.captionES = "";
                definition_def.classcircuit = "";
                definition_def.resizeSynchron = false;
                definition_def.vm = name + ".vlogic";

                if (aktuellesVerzeichniss.contains("CircuitElements")) {
                    definition_def.classfront = "";
                } else {
                    definition_def.classfront = "TRUE";
                }

                Tools.saveDefinitionFile(new File(elementPath + aktuellesVerzeichniss + "/" + name), definition_def);

                loadFolder(aktuellesVerzeichniss);

                DialogSaveAsModul frmx = new DialogSaveAsModul(owner, owner, true);

                frmx.executeEdit(elementPath + aktuellesVerzeichniss + "/" + name);

            } else {
                Tools.showMessage(this, java.util.ResourceBundle.getBundle("VisualLogic/Messages").getString("VM already exist") + " : " + new File(filename).getName());
            }

            loadFolder(aktuellesVerzeichniss);
        }
    }//GEN-LAST:event_jmiAddVMActionPerformed

    private void jPanelButtonsComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanelButtonsComponentResized
        reorderButtons();
    }//GEN-LAST:event_jPanelButtonsComponentResized

    private void jPanelButtonsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelButtonsMousePressed

        if (!aktuellesVerzeichniss.equalsIgnoreCase(rootPath)) {

            if (evt.getButton() == 3) {
                if (modus == MODE_COPY) {
                    jmiPaste.setEnabled(true);
                } else {
                    jmiPaste.setEnabled(false);
                }
                jPopupMenu2.show(jPanelButtons, evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_jPanelButtonsMousePressed

    private void jButton10MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton10MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton10MousePressed

    private void jButton8MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8MousePressed

    private void jButton7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7MousePressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    public javax.swing.JPanel jPanelButtons;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JPopupMenu jPopupMenu3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JMenuItem jmiAddVM;
    private javax.swing.JMenuItem jmiCopy;
    private javax.swing.JMenuItem jmiCut;
    private javax.swing.JMenuItem jmiDelete;
    private javax.swing.JMenuItem jmiDeleteDir;
    private javax.swing.JMenuItem jmiEditDir;
    private javax.swing.JMenuItem jmiEditVM;
    private javax.swing.JMenuItem jmiEditVMDefinition;
    private javax.swing.JMenuItem jmiNewDir;
    private javax.swing.JMenuItem jmiPaste;
    // End of variables declaration//GEN-END:variables
}
