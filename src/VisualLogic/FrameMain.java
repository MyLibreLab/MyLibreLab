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
You should have received a copy of the GNU Ge/Volumes/Volume/NetBeansProjects/myopenlab_source/src/VisualLogic/FrameMain.formneral Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package VisualLogic;

import BasisStatus.StatusGummiBandX;
import BasisStatus.StatusGummiBandXBackIF;
import Peditor.PropertyEditor;
import SimulatorSocket.MyOpenLabOwnerIF;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SplashScreen;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.filechooser.FileView;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import projectfolder.MyNode;
import projectfolder.ProjectPalette;
import ParserCode.*;
import com.apple.eawt.Application;
import de.myopenlab.update.frmUpdate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.UnsupportedLookAndFeelException;

class MyButtonX extends JButton {

    public VMEditorPanel panel;

    public MyButtonX(String caption) {
        super(caption);
    }

    public MyButtonX(Icon icon) {
        super(icon);
    }
}

public class FrameMain extends javax.swing.JFrame implements MyOpenLabOwnerIF, projectfolder.ProjectPaletteIF, ElementPaletteIF, VMEditorPanelIF, StatusGummiBandXBackIF {

    private boolean oki = false;
    //public MyImage doc_image = new MyImage();
    public PanelDokumentation panelDoc;
    public static String elementPath = ""; //NOI18N
    public String activeElement = ""; //NOI18N
    public static FrameMain frm;
    public javax.swing.Timer timer;
    private String letztesVerzeichniss = "."; //NOI18N
    public static Image iconImage = null;
    public static URL userURL = null;
    public String oldCircuitDirectory = "/CircuitElements"; //NOI18N
    public String oldPanelDirectory = "/FrontElements"; //NOI18N
    private Element oldElement = null;
    public FrameDoc docFrame = null;
    public DialogVariableWatcher watcher = null;
    public JLabel layedLabel = new JLabel(""); //NOI18N
    public FrameErrorWarnings errorWarnings = new FrameErrorWarnings();
    public boolean frontMode = false;
    public ArrayList<String> projects = new ArrayList<>();
    String driverPath;
    //public Console console= new Console();
    public ArrayList<Basis> desktop = new ArrayList<>();
    public boolean modePanel = false;
    public Settings settings = new Settings();
    //private FrameMain frameMain;
    private ElementPalette elementPaletteCircuit;
    private ElementPalette elementPaletteFront;
    private projectfolder.ProjectPalette projectPalette1;
    private PropertyEditor propertyEditor;

    private void reLoadElementPaletteDipendentFromProjectType() {
        Basis basis = getAktuelleBasis();
        if (basis.projectPath != null && basis.projectPath.length() > 0) {

            ProjectProperties props = Tools.openProjectFile(new File(basis.projectPath));

            if (props.projectType.equalsIgnoreCase("SPS")) { //NOI18N
                elementPaletteCircuit.init(this, basis, elementPath, "/CircuitElements/MCU/StackInterpreter/"); //NOI18N
                elementPaletteFront.init(this, basis, elementPath, "/FrontElements/MCU/Interpreter"); //NOI18N

            } else {

                elementPaletteCircuit.init(this, basis, elementPath, "/CircuitElements"); //NOI18N
                elementPaletteFront.init(this, basis, elementPath, "/FrontElements"); //NOI18N
            }
        }

    }

    public void reloadElementPalettes() {
        // reLoadElementPaletteDipendentFromProjectType();
        Basis basis = getAktuelleBasis();

        elementPaletteCircuit.init(this, basis, elementPath, elementPaletteCircuit.aktuellesVerzeichniss);
        elementPaletteFront.init(this, basis, elementPath, elementPaletteFront.aktuellesVerzeichniss);
    }

    //------- BEGIN  INTERFACES Implementation ---------------------
    public void projectPaletteAddNewVM(MyNode node) {
        DialogVMName frmVmName = new DialogVMName(this, true);
        frmVmName.setVisible(true);

        if (DialogVMName.result) {
            String filename = node.projectPath + node.relativePath + "/" + DialogVMName.newName + ".vlogic"; //NOI18N 

            if (!new File(filename).exists()) {
                Basis basis = new Basis(this, FrameMain.elementPath);
                basis.saveToFile(filename, false);
                reloadProjectPanel();
            } else {
                Tools.showMessage(this, java.util.ResourceBundle.getBundle("VisualLogic/Messages").getString("VM already exist") + " : " + new File(filename).getName());
            }
        }

    }

    public void dirAdd(MyNode node) {
        String value = JOptionPane.showInputDialog(
                this,
                java.util.ResourceBundle.getBundle("VisualLogic/FrameCircuit").getString("NEW_FOLDER"),
                java.util.ResourceBundle.getBundle("VisualLogic/FrameCircuit").getString("FOLDER_NAME"),
                JOptionPane.QUESTION_MESSAGE);

        if (value != null && value.length() > 0) {
            File file = new File(node.projectPath + node.relativePath);
            String projectName = file.getPath();

            new File(projectName + "/" + value).mkdir(); //NOI18N
            reloadProjectPanel();

        }
    }

    public void dirDelete(MyNode node) {
        File file = new File(node.projectPath + node.relativePath);
        String projectName = file.getPath();

        if (Tools.setQuestionDialog(this, java.util.ResourceBundle.getBundle("VisualLogic/Messages").getString("Do you really want to delete this Folder with any subFolder and Data? :") + " \"" + file.getName() + "\"")) {
            Tools.deleteDirectory(file);

            if (node.getLevel() == 1) {
                int pos = Collections.binarySearch(projects, file.getPath());
                if (pos > -1) {
                    projects.remove(pos);
                }

            }

            reloadProjectPanel();
        }
    }

    public void dirRename(MyNode node) {
        if (node != null) {
            File file = new File(node.projectPath + node.relativePath);

            DialogRename frm = new DialogRename(this, true);
            frm.init(file.getName());
            frm.setVisible(true);

            if (frm.result && frm.newName.length() > 0) {
                String std = file.getParent() + "/" + frm.newName;

                String newFilename = std;

                String oldFilename = file.getAbsolutePath();

                file.renameTo(new File(std));

                if (node.getLevel() == 1) {
                    int pos = Collections.binarySearch(projects, oldFilename);
                    if (pos > -1) {
                        projects.remove(pos);
                        newFilename = new File(std).getAbsolutePath();
                        projects.add(newFilename);
                    }

                }

                reloadProjectPanel();
                /*node.setUserObject(new File(newFilename));
            projectPalette1.jTree1.updateUI();
            projectPalette1.reloadFolder(node);*/
                //reloadProjectPanelFolder(node);
            }

        }
    }
    public static final int MODE_NOPE = 0;
    public static final int MODE_COPY_DIR = 1;
    public static final int MODE_CUT_DIR = 2;
    public static final int MODE_COPY_FILE = 3;
    public static final int MODE_CUT_FILE = 4;
    public int mode = MODE_NOPE;
    public String modePath = "";

    public void dirCopy(MyNode node) {
        String projectName = node.projectPath + node.relativePath;

        mode = MODE_COPY_DIR;
        modePath = projectName;
    }

    public void dirCut(MyNode node) {
        String projectName = node.projectPath + node.relativePath;

        mode = MODE_CUT_DIR;
        modePath = projectName;
    }

    public void dirFilePaste(MyNode node) {
        File destFile = new File(node.projectPath + node.relativePath);

        if (modePath.length() > 0) {
            if (mode == MODE_COPY_DIR) {
                if (node.isFolder) {
                    try {
                        String srcParent = new File(modePath).getParent();
                        String srcDirName = modePath.substring(srcParent.length(), modePath.length());
                        String dest = Tools.generateNewFileName(destFile.getAbsolutePath() + srcDirName);

                        Tools.copy(new File(modePath), new File(dest));
                        reloadProjectPanel();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } else if (mode == MODE_CUT_DIR) {
                if (node.isFolder) {
                    try {
                        String srcParent = new File(modePath).getParent();
                        String srcDirName = modePath.substring(srcParent.length(), modePath.length());
                        String dest = Tools.generateNewFileName(destFile.getAbsolutePath() + srcDirName);

                        Tools.copy(new File(modePath), new File(dest));
                        Tools.deleteDirectory(new File(modePath));
                        reloadProjectPanel();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } else if (mode == MODE_COPY_FILE) {
                if (node.isFolder) {
                    try {
                        String extension = Tools.getExtension(new File(modePath));
                        String filename = Tools.getFileNameWithoutExtension(new File(modePath));
                        String destFileName = Tools.generateNewFileName(destFile.getAbsolutePath() + "/" + filename, extension);

                        Tools.copy(new File(modePath), new File(destFileName));
                        reloadProjectPanel();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } else if (mode == MODE_CUT_FILE) {
                if (node.isFolder) {
                    try {
                        String extension = Tools.getExtension(new File(modePath));
                        String filename = Tools.getFileNameWithoutExtension(new File(modePath));
                        String destFileName = Tools.generateNewFileName(destFile.getAbsolutePath() + "/" + filename, extension);

                        Tools.copy(new File(modePath), new File(destFileName));
                        new File(modePath).delete();
                        reloadProjectPanel();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }

        }

        mode = MODE_NOPE;
        modePath = "";
    }

    public void fileCopy(MyNode node) {
        String projectName = node.projectPath + node.relativePath;

        mode = MODE_COPY_FILE;
        modePath = projectName;
    }

    public void fileCut(MyNode node) {
        String projectName = node.projectPath + node.relativePath;

        mode = MODE_CUT_FILE;
        modePath = projectName;
    }

    public String searchElement(File file) {
        boolean res = setQuestionDialogYES_NO_CANCEL(this, "Element not found : " + file.getName() + "\n" + "do you want to search the Element?");

        if (res) {
            DialogElementSearchAssistent frm = new DialogElementSearchAssistent(this, true);
            frm.init(file);

            return frm.result;
        }

        return "";
    }

    public void projectPaletteItemSelected(MyNode node) {
        String path = node.projectPath + node.relativePath;

        if (Tools.isExecutableProject(new File(path).list())) {
            openProject(new File(path));
            return;
        }

        String extension = Tools.getExtension(new File(path));
        if (extension != null && (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("gif") || extension.equalsIgnoreCase("png"))) {
            Tools.openFileImage(this, new File(path));
        } else if (extension != null && (extension.equalsIgnoreCase("txt") || extension.equalsIgnoreCase("html") || extension.equalsIgnoreCase("htm"))) {
            Tools.editFile(this, new File(path));
        } else if (extension != null && extension.equalsIgnoreCase("vlogic")) {
            globalProjectPath = node.projectPath;
            globalPath = path;

            SwingWorker worker = new SwingWorker<Object, Object>() {

                public Object doInBackground() {
                    Tools.dialogWait = new DialogWait();
                    Tools.dialogWait.setVisible(true);
                    Tools.dialogWait.setLocation(Tools.dialogWait.getLocation().x, Tools.dialogWait.getLocation().y - 150);

                    Basis oBasis = addBasisToVMPanel(globalPath, globalProjectPath, true);
                    if (oBasis != null) {
                        paneRight.setSelectedComponent(oBasis.ownerVMPanel);
                    }

                    return null;
                }

                protected void done() {
                    Tools.dialogWait.dispose();
                }
            };

            worker.execute();
        }
    }

    public void projectPaletteAction(String command, MyNode node) {

        if (command.equalsIgnoreCase("OPENFILE")) {
            String str = node.projectPath + node.relativePath;
            /*if (str.endsWith("vlogic")) {
                projectPaletteItemSelected(node);
                return;
            }*/

            if (str.endsWith("vlogic")) {
                projectPaletteItemSelected(node);
                //Tools.editFile(this, new File(node.projectPath + node.relativePath));
                return;
            }

            try {
                if (java.awt.Desktop.isDesktopSupported()) {

                    File myFile = new File(str);
                    java.awt.Desktop.getDesktop().open(myFile);
                }

            } catch (IOException ex) {
            }

        } else if (command.equalsIgnoreCase("EDITFILE")) {

            String str = new File(node.projectPath + node.relativePath).getAbsolutePath();

            String ext = Tools.getExtension(new File(str));

            if (str.endsWith("vlogic")) {
                projectPaletteItemSelected(node);
                return;
            }

            if (ext.equalsIgnoreCase("html") || ext.equalsIgnoreCase("htm")) {

                String htmlEditor = settings.getHTMLEditor();

                if (htmlEditor != null && htmlEditor.length() > 0 && new File(htmlEditor).exists()) {
                    try {
                        Runtime.getRuntime().exec(settings.getHTMLEditor() + " \"" + str + "\"");
                        return;
                    } catch (IOException ex) {
                    }
                }
            }

            if (ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("png")) {
                if (settings.getGraphicEditor() != null && settings.getGraphicEditor().length() > 0 && new File(settings.getGraphicEditor()).exists()) {
                    try {
                        Runtime.getRuntime().exec(settings.getGraphicEditor() + " \"" + str + "\"");
                        return;
                    } catch (IOException ex) {
                    }
                }
            }

            /*if (str.endsWith("vlogic")) {
            } else {
                Tools.editFile(this, new File(node.projectPath + node.relativePath));
            }*/
        } else if (command.equalsIgnoreCase("ADDVM")) {
            projectPaletteAddNewVM(node);
        } else if (command.equalsIgnoreCase("ITEMSELECTED")) {
            projectPaletteItemSelected(node);
        } else if (command.equalsIgnoreCase("FILE_COPY")) {
            fileCopy(node);
        } else if (command.equalsIgnoreCase("FILE_CUT")) {
            fileCut(node);
        } else if (command.equalsIgnoreCase("OPENFILE")) {
            reloadProjectPanel();
        } else if (command.equalsIgnoreCase("RELOAD")) {
            reloadProjectPanel();
        } else if (command.equalsIgnoreCase("DIR_ADD")) {
            dirAdd(node);
        } else if (command.equalsIgnoreCase("DIR_DELETE")) {
            dirDelete(node);
        } else if (command.equalsIgnoreCase("DIR_RENAME")) {
            dirRename(node);
        } else if (command.equalsIgnoreCase("DIR_COPY")) {
            dirCopy(node);
        } else if (command.equalsIgnoreCase("DIR_CUT")) {
            dirCut(node);
        } else if (command.equalsIgnoreCase("PASTE")) {
            dirFilePaste(node);
        } else if (command.equalsIgnoreCase("DISTRIBUTION")) {
            File file = new File(node.projectPath + node.relativePath);
            String projectPath = file.getPath();

            DialogDistributionAssistent frm = new DialogDistributionAssistent(this, true, file.getAbsolutePath(), driverPath);

            frm.setVisible(true);

            if (frm.result) {
                String mainVM = frm.mainVM;
                String destPath = frm.destPath;

                if (mainVM.length() > 0 && destPath.length() > 0) {
                    createDistribution(projectPath, destPath, mainVM);
                }

            }

            /*Basis basis = getAktuelleBasis();
        if (basis!=null)
        {
        if (basis.vmProtected) return;
        DialogDistributionAssistent frm = new DialogDistributionAssistent(this,true, basis);
        frm.setVisible(true);
        if (frm.result)
        {
        createDistribution(basis);
        }
        }*/
        }
    }

    public void projectPaletteProjectClose(MyNode node) {
        if (node != null) {
            File file = new File(node.projectPath + node.relativePath);
            String projectName = file.getPath();

            int pos = Collections.binarySearch(projects, projectName);
            if (pos > -1) {
                projects.remove(pos);
                reloadProjectPanel();
            }
        }
    }

    public void projectPaletteNewSubVM(MyNode node) {
        if (node != null) {
            DialogSubVMAssistent frm = new DialogSubVMAssistent(this, true);
            frm.setVisible(true);
            if (frm.result && frm.vmName.length() > 0) {

                // 1. Verzeichniss mit Value unter dem Project erzeugen
                String dir = node.projectPath + "/" + frm.vmName;
                if (!new File(dir).exists()) {
                    new File(dir).mkdir();

                    try {
                        Tools.copy(new File(elementPath + "/nope.html"), new File(dir + "/doc.html"));
                        Tools.copy(new File(elementPath + "/nope.html"), new File(dir + "/doc_en.html"));
                        Tools.copy(new File(elementPath + "/nope.html"), new File(dir + "/doc_es.html"));

                        Tools.copy(new File(elementPath + "/element.gif"), new File(dir + "/" + frm.vmName + ".gif"));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    File file = new File(dir + "/subvm.vlogic");

                    generateSubVM(node.projectPath, frm.pinsLeft, frm.pinsRight, dir + "/" + frm.vmName + ".vlogic");

                    //Basis basis=new Basis(this,this.elementPath);
                    //basis.saveToFile(filename,false);
                    reloadProjectPanel();
                } else {
                    Tools.showMessage(this, java.util.ResourceBundle.getBundle("VisualLogic/Messages").getString("Folder already exist") + " : " + new File(dir).getName());
                }

            }
        }
    }

    public boolean setQuestionDialogYES_NO_CANCEL(JFrame parent, String s) {
        int res = JOptionPane.showOptionDialog(parent, s, java.util.ResourceBundle.getBundle("VisualLogic/Basic").getString("attention"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (res == JOptionPane.NO_OPTION) {
            return false;
        }
        if (res == JOptionPane.CANCEL_OPTION) {
            return false;
        }
        return true;
    }

    public void projectPaletteOpenProject(MyNode node) {
        jmiOpenProjectActionPerformed(null);
    }

    public void projectPaletteDeleteProject(MyNode node) {
        if (node != null) {
            File file = new File(node.projectPath + node.relativePath);
            if (Tools.setQuestionDialog(this, java.util.ResourceBundle.getBundle("VisualLogic/Messages").getString("Do you really want to delete the Project and Remove all Project Files from disc ? :") + " \"" + file.getName() + "\"")) {
                if (Tools.deleteDirectory(file)) {
                    int pos = Collections.binarySearch(projects, file.getPath());
                    if (pos > -1) {
                        projects.remove(pos);
                        reloadProjectPanel();
                    }
                }
            }

        }
    }

    public void projectPaletteProjectProperties(MyNode node) {
        if (node != null) {
            File file = new File(node.projectPath + node.relativePath);

            if (Tools.isExecutableProject(file.list())) {
                return;
            }

            DialogProjectProperties.execute(this, file.getAbsolutePath());
        }
    }

    public void removeTab(String vmFileName) {
        for (int i = 0; i < paneRight.getComponentCount(); i++) {
            Component comp = paneRight.getComponent(i);

            System.out.println("" + comp.toString());
            if (comp instanceof VMEditorPanel) {
                VMEditorPanel pnl = (VMEditorPanel) comp;
                if (pnl.basis.fileName.equalsIgnoreCase(vmFileName)) {
                    paneRight.remove(pnl);
                    desktop.remove(pnl.basis);
                    return;
                }
            }
        }
    }

    public void projectPaletteDeleteVM(MyNode node) {
        if (node != null) {
            File file = new File(node.projectPath + node.relativePath);
            if (Tools.setQuestionDialog(this, java.util.ResourceBundle.getBundle("VisualLogic/Messages").getString("Do you really want to delete the VM") + " : \"" + file.getName() + "\"")) {
                file.delete();

                removeTab(file.getPath());
                reloadProjectPanel();
            }
        }
    }

    public void projectPaletteRenameVM(MyNode node) {
        if (node != null) {
            File file = new File(node.projectPath + node.relativePath);

            DialogRename frm = new DialogRename(this, true);

            String nm = file.getName();
            String ext = Tools.getExtension(file);

            frm.init(nm.substring(0, nm.length() - ext.length() - 1));

            frm.setVisible(true);

            if (frm.result && frm.newName.length() > 0) {
                String std = file.getParent() + "/" + frm.newName + "." + ext;

                if (!new File(std).exists()) {
                    try {
                        file.renameTo(new File(std));
                    } catch (Exception ex) {
                        Tools.showMessage(this, java.util.ResourceBundle.getBundle("VisualLogic/Messages").getString("Could not rename File!"));
                        return;
                    }

                    Basis basis = isBasisInDesktop(file.getAbsolutePath());
                    if (basis != null) {
                        basis.fileName = std;
                        basis.vmFilename = std;

                        if (basis.panelLabel != null) {
                            int index = getTabIndex(basis.ownerVMPanel);
                            if (index > -1) {
                                basis.panelLabel.setText(new File(std).getName());
                            }
                        }

                    }

                    reloadProjectPanel();

                } else {
                    Tools.showMessage(java.util.ResourceBundle.getBundle("VisualLogic/Messages").getString("File already exist."));
                }
            }
        }
    }

    public void projectPaletteAddNewProject(MyNode node) {
        jmiNewProjectActionPerformed(null);
    }

    public Basis getAktuelleBasis() {
        Component comp = paneRight.getSelectedComponent();
        if (comp != null) {
            VMEditorPanel editor = (VMEditorPanel) paneRight.getSelectedComponent();
            return editor.basis;
        }
        return null;
    }

    public VMObject getVMObject() {
        Component comp = paneRight.getSelectedComponent();
        if (comp != null) {
            VMEditorPanel editor = (VMEditorPanel) paneRight.getSelectedComponent();
            return editor.getVMObject();
        }
        return null;
    }

    public void onButtonClicken(String[] params) {
        Component comp = paneRight.getSelectedComponent();
        if (comp != null) {
            VMEditorPanel editor = (VMEditorPanel) paneRight.getSelectedComponent();
            editor.getVMObject().newElement = params;
        }
    }

    public void vmEditorPanelTabChanged(VMObject vmobject) {
        Basis basis = getAktuelleBasis();
        if (basis == null) {
            return;
        }

        jSpinner1.setValue(basis.getDebugDelay());
        propertyEditor.vmobject = null;
        propertyEditor.clear();

        if (vmobject == basis.getCircuitBasis()) {
            jPanelElementPalette.removeAll();
            jPanelElementPalette.add(elementPaletteCircuit, BorderLayout.CENTER);
            elementPaletteFront.setVisible(false);
            elementPaletteCircuit.setVisible(true);
            //elementPaletteCircuit.loadFolder(elementPaletteCircuit.aktuellesVerzeichniss);
        }
        if (vmobject == basis.getFrontBasis()) {
            jPanelElementPalette.removeAll();
            jPanelElementPalette.add(elementPaletteFront, BorderLayout.CENTER);
            elementPaletteFront.setVisible(true);
            elementPaletteCircuit.setVisible(false);
            //elementPaletteFront.loadFolder(elementPaletteFront.aktuellesVerzeichniss);
        }
        reLoadElementPaletteDipendentFromProjectType();

        listeAllElements();
    }

    //------- END INTERFACES Implementation ---------------------
    public void closeApplication() {
        saveConfiFile();

        String fileName = getUserURL().getFile() + "/projects.file";
        Tools.saveProjectsFile(new File(fileName), projects);

        if (closeAllVms() == true) {
            System.exit(Tools.appResult);
        }
    }

    public void saveConfiFile() {
        settings.setElementSplitterPosition(jSplitPane3.getDividerLocation());
        settings.setElementSplitterHozPosition(jSplitPane1.getDividerLocation());

        settings.setRightSplitterPos(jPanel7.getPreferredSize().width);

        settings.setDocumentation_language(panelDoc.selectedLanguage);

        settings.setStatus(getExtendedState());
        settings.setCircuitWindowLocation(getLocation());
        settings.setCircuitWindowDimension(getSize());

        //settings.userdefinedElementsPath=
        if (docFrame != null) {
            settings.setDocLocation(docFrame.getLocation());
            settings.setDocDimension(docFrame.getSize());
        }

        try {
            //String fileName = getUserURL().getFile() + System.getProperty("file.separator") + "Config.conf";
            //File fconf = new File(fileName);

            //ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fconf.getAbsolutePath()));
            settings.setMainFrameLocation(this.getLocation());
            settings.setMainFrameSize(this.getSize());
            //oos.writeObject(settings);
            //oos.flush();
            //oos.close();

            String fileName_xml = getUserURL().getFile() + System.getProperty("file.separator") + "config.xml";
           
            
            XMLSerializer.write(settings, fileName_xml);
            
            

        } catch (Exception e) {

        }
    }

    public Basis isBasisInDesktop(String path) {
        for (int i = 0; i < desktop.size(); i++) {
            Basis basis = desktop.get(i);

            File f1 = new File(basis.fileName);
            File f2 = new File(path);

            if (f1.getAbsolutePath().equalsIgnoreCase(f2.getAbsolutePath())) {
                return basis;
            }
        }

        return null;
    }

    class XJLabel extends JLabel {

        public VMEditorPanel editor;

        public XJLabel() {
            super();
        }
    }

    public void XaddBasisToVMPanel(Basis basis) {
        VMEditorPanel pnl = new VMEditorPanel(this, basis, "", false);

        basis.propertyEditor = propertyEditor;
        //propertyEditor.vmobject=basis;

        String caption = new File(basis.fileName).getName();
        paneRight.add(pnl);
        //paneRight.addTab("",new javax.swing.ImageIcon("Y:\\icons\\tango-icon-theme-0.7.2\\tango-icon-theme-0.7.2\\16x16\\actions\\address-book-new.png"),pnl);

        // jTabbedPane1.addTab("tab1",  jPanel1);
        int index = paneRight.getTabCount() - 1;
        paneRight.setSelectedIndex(index);

        final JPanel content = new JPanel();

        JPanel tab = new JPanel();
        tab.setLayout(new BorderLayout());
        tab.setOpaque(false);

        XJLabel tabLabel = new XJLabel();
        tabLabel.editor = pnl;

        tabLabel.setText(caption + "  ");
        //tabLabel.setVerticalAlignment(JLabel.TOP);

        basis.panelLabel = tabLabel;

        javax.swing.ImageIcon closeXIcon = new javax.swing.ImageIcon(getClass().getResource("/Bilder/Cross9x9.png"));
        Dimension closeButtonSize;

        closeButtonSize = new Dimension(closeXIcon.getIconWidth() + 3, closeXIcon.getIconHeight() + 3);

        MyButtonX tabCloseButton = new MyButtonX(closeXIcon);
        tabCloseButton.setBorderPainted(false);

        tabCloseButton.setPreferredSize(closeButtonSize);
        tabCloseButton.panel = pnl;

        ImageIcon icon = new ImageIcon(getClass().getResource("/Bilder/16x16/text-x-script.png"));
        tabLabel.setIcon(icon);
        tabLabel.setBorder(null);
        tab.setBorder(null);
        tabCloseButton.setBorder(null);

        tabCloseButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof MyButtonX) {
                    MyButtonX button = (MyButtonX) e.getSource();

                    closeVM(button.panel);
                    button.panel = null;
                }
            }
        });

        tab.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        tab.add(tabLabel, BorderLayout.WEST);
        tab.add(tabCloseButton, BorderLayout.EAST);

        /*tabLabel.setPreferredSize(new Dimension(87,15));
        tabCloseButton.setPreferredSize(new Dimension(10,15));
        tab.setPreferredSize(new Dimension(110,15));*/
        paneRight.setTabComponentAt(paneRight.getTabCount() - 1, tab);

        basis.saveForUndoRedo();
        basis.setChanged(false);
    }

    public boolean closeVM(VMEditorPanel panel) {
        String titel;
        int res;
        String str = new File(panel.basis.fileName).getName();
        if (panel.basis.isChanged()) {

            String MSG_SAVE_CHANGES_IN_VM = java.util.ResourceBundle.getBundle("VisualLogic/FrameCircuit").getString("MSG_SAVE_CHANGES_IN_VM");

            titel = MSG_SAVE_CHANGES_IN_VM + " \"" + str + "\"?";
            res = JOptionPane.showOptionDialog(this, titel, java.util.ResourceBundle.getBundle("VisualLogic/Basic").getString("attention"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (res == JOptionPane.CANCEL_OPTION) {
                return false;
            }

            if (res == JOptionPane.YES_OPTION) {
                panel.basis.save();
            }
        }

        if (panel.basis.isRunning()) {
            titel = "VM : \"" + str + "\" is running!\n Stop VM?";
            res = JOptionPane.showOptionDialog(this, titel, java.util.ResourceBundle.getBundle("VisualLogic/Basic").getString("attention"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (res == JOptionPane.NO_OPTION) {
                return false;
            }
        }
        panel.basis.stop();
        panel.basis.frameCircuit = null;
        paneRight.remove(panel);
        desktop.remove(panel.basis);
        panel.basis.close();
        panel.basis = null;

        panel = null;

        Runtime.getRuntime().gc();
        return true;
    }

    public void addMessageToConsoleErrorWarnings(String message) {
        if (errorWarnings != null) {
            errorWarnings.setVisible(true);
            errorWarnings.addMessageToConsole(message + "\n");
        }
        /*
    txtMessages.setText(txtMessages.getText()+message+"\n");
    txtMessages.setCaretPosition(txtMessages.getDocument().getLength());
    if(jSplitPane2.getDividerLocation()<10)
    {
    jSplitPane2.setDividerLocation(100);
    } */
    }

    public Basis addBasisToVMPanel(String path, String projectPath, boolean basisPanelVisible) {
        Basis Obasis = isBasisInDesktop(path);
        if (Obasis == null) {
            timer.stop();
            propertyEditor.vmobject = null;
            propertyEditor.locked = true;
            Basis basis = new Basis(this, FrameMain.elementPath);
            basis.frameCircuit = this;
            basis.propertyEditor = propertyEditor;
            desktop.add(basis);

            basis.projectPath = projectPath;

            double time1 = System.currentTimeMillis();

            basis.loadFromFile(path, false);

            double time2 = System.currentTimeMillis() - time1;

            //lblStatus.setText(java.util.ResourceBundle.getBundle("VisualLogic/Messages").getString("Basis loading Time") + time2 + " ms");
            basis.isFileLoaded = true;
            //if (basisPanelVisible)
            {
                XaddBasisToVMPanel(basis);
            }
            propertyEditor.locked = false;

            timer.start();

            reLoadElementPaletteDipendentFromProjectType();

            return basis;
        } else {
            timer.start();
            return Obasis;
        }

    }

    public void addBasisToVMPanel(Basis basis) {
        basis.frameCircuit = this;
        basis.propertyEditor = propertyEditor;
        desktop.add(basis);

        XaddBasisToVMPanel(basis);
    }

    public void initApp() {

        jmniUpdate.setVisible(false);

        System.setErr(new PrintStream(new TextAreaStream(errorWarnings)));

        //setTitle(Version.strApplicationTitle+" ["+new File(fileName).getName()+"]");
        setSize(settings.getCircuitWindowDimension());

        setSize(2000, 2000);
        setPreferredSize(new Dimension(2000, 2000));
        setBackground(Color.white);

        //frontBasis.setPreferredSize(new Dimension(500,500));
        int pos;
        pos = settings.getElementSplitterPosition();
        jSplitPane3.setDividerLocation(pos);

        pos = settings.getElementSplitterHozPosition();
        jSplitPane1.setDividerLocation(pos);

        setLocation(settings.getCircuitWindowLocation());
        setSize(settings.getCircuitWindowDimension());

       // setExtendedState(settings.getStatus());

        String ver = Version.strApplicationVersion + " " + Version.strStatus;

        setTitle(Version.strApplicationTitle + " Version-" + ver.trim());

       

        // Loesche alle tmp Dateien implements User Verzeichniss
        File verzeichniss = new File(getUserURL().getFile() + "/");

        if (verzeichniss.exists()) {
            File[] files = verzeichniss.listFiles();

            for (int i = 0; i < files.length; i++) {
                String filename = files[i].getName();

                if (filename.endsWith(".tmp")) {
                    files[i].delete();
                }
                if (filename.endsWith(".vlogic")) {
                    files[i].delete();
                }

            }
        }

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        if (this.getX() < 0 || this.getX() > screenSize.width) {
            this.setLocation(screenSize.width - 250, this.getY());
        }

        if (this.getY() < 0 || this.getY() > screenSize.height) {
            this.setLocation(this.getX(), screenSize.height - 250);
        }

        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                //closeApplication();
            }
        });

        String str = System.getProperty("java.version");

        int intVerMain = Integer.parseInt(str.substring(0, 1));
        int intVerSub = Integer.parseInt(str.substring(2, 3));

        System.out.println("Java version :" + str);

        if (intVerMain >= 1 && intVerSub >= 5) {

        } else {
            JOptionPane.showMessageDialog((Component) null, "Sie benutzen eine aeltere Java-Version: \"" + str + "\"\nSie brauchen mindestens Java 1.5 oder besser.\nDie aktuelle Version finden Sie unter \"http://java.sun.com/\"", "Achtung", JOptionPane.WARNING_MESSAGE);
        }

        //this.setMaximumSize(new Dimension(500,500));
    }

    private String createDefString(String path) {
        String result = "";

        result += "ISDIRECTORY     = TRUE\n";
        result += "CAPTION         = Eigene Elemente\n";
        result += "CAPTION_EN      = User defined Elements\n";
        result += "CAPTION_ES      = definido por el usario\n";
        result += "REDIRECT        = " + path + "\n";
        result += "ICON            = library.png\n";

        return result;
    }

    public void createUserElementSubDirs(String path) {
        path = new File(path).getAbsolutePath();

        File userdefFile1 = new File(elementPath + "/CircuitElements/2user-defined/definition.def");
        File userdefFile2 = new File(elementPath + "/FrontElements/2user-defined/definition.def");

        Tools.saveText(userdefFile1, createDefString(new File(path + "/CircuitElements").getAbsolutePath()));
        Tools.saveText(userdefFile2, createDefString(new File(path + "/FrontElements").getAbsolutePath()));

        new File(path).mkdirs();
        new File(path + "/CircuitElements").mkdirs();
        new File(path + "/FrontElements").mkdirs();

        if (!new File(path + "/CircuitElements/definition.def").exists()) {
            String src = elementPath + "/UserElementsTemplate/CircuitElements/definition.def";
            String dst = path + "/CircuitElements/definition.def";
            try {
                Tools.copyFile(new File(src), new File(dst));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (!new File(path + "/CircuitElements/icon.png").exists()) {
            String src = elementPath + "/UserElementsTemplate/CircuitElements/icon.png";
            String dst = path + "/CircuitElements/icon.png";
            try {
                Tools.copyFile(new File(src), new File(dst));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (!new File(path + "/FrontElements/definition.def").exists()) {
            String src = elementPath + "/UserElementsTemplate/FrontElements/definition.def";
            String dst = path + "/FrontElements/definition.def";
            try {
                Tools.copyFile(new File(src), new File(dst));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (!new File(path + "/FrontElements/icon.png").exists()) {
            String src = elementPath + "/UserElementsTemplate/FrontElements/icon.png";
            String dst = path + "/FrontElements/icon.png";
            try {
                Tools.copyFile(new File(src), new File(dst));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        String fileName = getUserURL().getFile() + "/userdefinedElements.path";
        Tools.saveText(new File(fileName), path);
        settings.setUserdefinedElementsPath(path);
    }

    public void handleUserElementDirectory() {
        String fileName = getUserURL().getFile() + "/userdefinedElements.path";

        if (!new File(fileName).exists()) {
            String path = DialogUserdefinedElementsHome.execute(this);

            createUserElementSubDirs(path);
        }

        String fn = getUserURL().getFile() + "/userdefinedElements.path";
        String path = Tools.loadTextFile(new File(fn));
        settings.setUserdefinedElementsPath(path);
    }

    private void copyUserdefElementsRecursive(String elPath, String actualPath) {
        File elFile = new File(elPath);
        File files[] = elFile.listFiles();

        if (!(new File(actualPath).exists())) {
            new File(actualPath).mkdir();
        }
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                // Always override old Elements!!!
                // It doesn't matter if there exist or not!
                if (files[i].isDirectory()) {
                    try {
                        String name = files[i].getName();
                        Tools.copy(files[i], new File(actualPath + "\\" + name));
                    } catch (IOException ex) {
                        //Logger.getLogger(FrameMain.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        }
    }

    public void copyUserdefElements() {
        int result = JOptionPane.showConfirmDialog(this, java.util.ResourceBundle.getBundle("VisualLogic/Messages").getString("CopyElements"), java.util.ResourceBundle.getBundle("VisualLogic/Messages").getString("Important"), JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            // Check if there are in the actual Userdef-Element-Path
            // the Elements from the Elements\\CircuitElements\\user-defined

            // Circuit Elements
            String elPath = elementPath + "\\CircuitElements\\user-defined";
            String actualPath = Tools.userElementPath + "\\CircuitElements\\";

            copyUserdefElementsRecursive(elPath, actualPath);

            // FrontPanel Elements
            String elPathFront = elementPath + "\\FrontElements\\user-defined";
            String actualPathFront = Tools.userElementPath + "\\FrontElements\\";

            copyUserdefElementsRecursive(elPathFront, actualPathFront);
        }

    }

    private ArrayList<String> listDirectory(String dir) {
        File directory = new File(dir);
        ArrayList<String> files = new ArrayList<>();
        // get all the files from a directory
        File[] fList = directory.listFiles();
        if (fList != null) {
            for (File file : fList) {

                if (!file.isDirectory()) {

                    if (file.exists()) {
                        files.add(file.getName());
                    }
                }
            }
        }

        return files;
    }

    private void initDocs() {

        jmnuDocs.removeAll();
        new File(elementPath + "/Documentation").mkdir();

        // List all Documents in their DIrectories
        File directory = new File(elementPath + "/Documentation");

        ArrayList<String> files = new ArrayList<>();
        // get all the files from a directory
        File[] fList = directory.listFiles();
        if (fList != null) {
            for (File file : fList) {

                if (file.isDirectory()) {

                    JMenu menu = new JMenu();
                    String caption = Tools.getInfoXMLCaption(file);
                    menu.setText(caption);
                    jmnuDocs.add(menu);

                    menu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/book.png"))); // NOI18N
                    ArrayList<String> files2 = listDirectory(file.getAbsolutePath());

                    for (String file2 : files2) {
                        MyMenuItem item = new MyMenuItem();

                        if (file2.endsWith(".xml")
                                || file2.endsWith(".def")
                                || file2.equalsIgnoreCase("description_de.html")
                                || file2.equalsIgnoreCase("description_en.html")
                                || file2.equalsIgnoreCase("description_es.html")) {

                        } else {
                            item.setText(file2);

                            menu.add(item);
                            item.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/book.png"))); // NOI18N

                            item.path = file.getAbsolutePath() + "/" + file2;

                            item.addActionListener(new java.awt.event.ActionListener() {
                                @Override
                                public void actionPerformed(java.awt.event.ActionEvent evt) {

                                    try {
                                        if (java.awt.Desktop.isDesktopSupported()) {
                                            MyMenuItem item = (MyMenuItem) evt.getSource();
                                            File myFile = new File(item.path);
                                            java.awt.Desktop.getDesktop().open(myFile);
                                        }
                                    } catch (IOException ex) {
                                        // no application registered for PDFs
                                    }

                                }
                            });
                        }

                    }

                }
            }
        }
    }

    public void frmDTLengend() {
        Image image = this.getIconImage();
        setIconImage(image);

        String filename = "types_de.htm";
        String strLocale = Locale.getDefault().toString();

        if (strLocale.equalsIgnoreCase("de_DE")) {
            filename = "types_de.htm";
        } else if (strLocale.equalsIgnoreCase("en_US")) {
            filename = "types_en.htm";
        } else if (strLocale.equalsIgnoreCase("es_ES")) {
            filename = "types_es.htm";
        }

        /*try
        {              
            URL url = getClass().getResource("/legend/"+filename);
            jTextPane1.setContentType("text/html");
            jTextPane1.setPage(url);            
        } catch (Exception ex) {System.out.println(ex.toString());}
         */
        java.awt.event.ActionListener actionListener = new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
                dispose();
            }
        };

        javax.swing.KeyStroke stroke = javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(actionListener, stroke, javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

    }

    public FrameMain(String args[]) {
        
         //ImageIcon icon = new ImageIcon(getClass().getResource("/Bilder/logo.png"));
        try {

            //iconImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Bilder/16x16/icon.png"));
            //setIconImage(iconImage);            ?
            iconImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Bilder/icon_16.png"));
            Image iconImage32 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Bilder/icon_32.png"));
            Image iconImage64 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Bilder/icon_64.png"));

            ArrayList<Image> images = new ArrayList<>();
            images.add(iconImage);
            images.add(iconImage32);
            images.add(iconImage64);
            setIconImages(images);
            
            // for mac os!
            Application application = Application.getApplication();
            application.setDockIconImage(iconImage64);

        } catch (Exception ex) {
            Tools.showMessage("Fehler : " + ex.toString());
        }

        //JDialog.setDefaultLookAndFeelDecorated(true);
        driverPath = elementPath + "/Drivers";

        LoadConfigFile();

        Tools.settings = settings;

        String fileName = getUserURL().getFile() + "/projects.file";

        if (!new File(fileName).exists()) {
            try {
                new File(fileName).createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        ArrayList<String> projectsX = Tools.loadProjectsFile(new File(fileName));

        //ArrayList<String> projects = new ArrayList<>();
        projects = new ArrayList<>();

        String vms = "VirtualMachines";

        projects.add(new File(elementPath + "/" + vms).getAbsolutePath());

        for (String project : projectsX) {
            File f = new File(project);

            if (!f.getName().equalsIgnoreCase(vms)) {
                projects.add(f.getAbsolutePath());
            }

        }

        handleUserElementDirectory();

        Tools.userElementPath = settings.getUserdefinedElementsPath();

        // myopenlab.path speichert immer das verzeichniss wo
        // sich MyOpenLab befindet!
        fileName = getUserURL().getFile() + "/myopenlab.path";

        if (!new File(fileName).exists()) {
            try {
                new File(fileName).createNewFile();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        String myopenlab = "";

        String value = Tools.loadTextFile(new File(fileName));
        if (value.length() > 0) {
            myopenlab = value;
        }

        String myopenlabX = new File(elementPath).getAbsolutePath();

        if (!myopenlab.equalsIgnoreCase(myopenlabX)) {
            Tools.saveText(new File(fileName), myopenlabX);
        }

        System.out.println("myopenlab Path=" + myopenlabX);

        /*DialogLanguage lan = new DialogLanguage(this, true);

        lan.execute(settings.language);

        settings.language = lan.result;      */
        //jMenuBar2.setFont(new Font("Tahoma", Font.PLAIN, 21));
        Font f = new Font("Tahoma", Font.PLAIN, 13);
        UIManager.put("Menu.font", f);
        UIManager.put("MenuItem.font", f);

        initComponents();

        panelDoc = new PanelDokumentation();
        jPanel9.add(panelDoc, BorderLayout.CENTER);

        initDocs();

        jmiSaveAsModul.setVisible(false);

        //jButton1.setVisible(false);
        initApp();

        if (!settings.getVersion().equalsIgnoreCase(Version.strApplicationVersion)) {
            copyUserdefElements();
        }

        settings.setVersion(Version.strApplicationVersion);

        // Load Drivers from "Drivers" Directory"
        Tools.driverManager = new DriverManager(this);

        ArrayList<String> drivers = Tools.listDrivers(driverPath);

        for (int i = 0; i < drivers.size(); i++) {
            Tools.driverManager.registerDriver(drivers.get(i));
        }
        // End Loading Drivers.

        setLocation(settings.getCircuitWindowLocation());
        setSize(settings.getCircuitWindowDimension());
       

        if (settings.getRightSplitterPos() < 10) {
            settings.setRightSplitterPos(10);
        }
        jPanel7.setPreferredSize(new Dimension(settings.getRightSplitterPos(), 10));
        panelDoc.setSelectedLanguage(settings.getDocumentation_language());

        int xx = settings.getRightSplitterPos();

        if (xx < 20) {
            jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/arrow_black_left.png"))); // NOI18N
        } else {
            jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/arrow_black_right.png"))); // NOI18N
        }
        jButton13.updateUI();

        if (args.length >= 2 && args[2].equalsIgnoreCase("runtime=true")) {

        } else {
            activate_DocFrame(null);
        }

        //JLayeredPane mnu = getLayeredPane();
        SpinnerNumberModel model = new SpinnerNumberModel(new Integer(100), new Integer(0), new Integer(5000), new Integer(1));
        jSpinner1.setModel(model);
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(jSpinner1, "####0000");
        jSpinner1.setEditor(editor);

        JLayeredPane mnu = getLayeredPane();

        layedLabel.setFont(new Font("TAHOMA", 1, 12));
        layedLabel.setLocation(0, 0);
        layedLabel.setSize(0, 0);
        mnu.add(layedLabel);

        //this.frameMain=frameMain;
        propertyEditor = new PropertyEditor(this);
        jTabPE.addTab(java.util.ResourceBundle.getBundle("VisualLogic/FrameCircuit").getString("Property-Editor"), propertyEditor);

        projectPalette1 = new ProjectPalette(this);
        jPanel1.add(projectPalette1);

        elementPaletteCircuit = new ElementPalette(this);
        elementPaletteCircuit.frameCircuit = this;
        elementPaletteFront = new ElementPalette(this);
        elementPaletteFront.frameCircuit = this;

        elementPaletteCircuit.init(this, null, elementPath, "/CircuitElements");
        elementPaletteFront.init(this, null, elementPath, "/FrontElements");

        /*elementPaletteCircuit.aktuellesVerzeichniss=;
        elementPaletteFront.aktuellesVerzeichniss=oldPanelDirectory;*/
        //elementPalette.aktuellesVerzeichniss=oldPanelDirectory;
        jPanelElementPalette.add(elementPaletteCircuit);
        //jPanelElementPalette.add(elementPaletteFront);

        reloadProjectPanel();

        startButtonHandler();

        //SwingUtilities.updateComponentTreeUI()
    }

    public void removePinDescription() {
        JLabel lbl = layedLabel;
        lbl.setLocation(0, 0);
        lbl.setSize(0, 0);
        lbl.setOpaque(false);
        lbl.setText("");
    }

    public void showPinDescription(JPin pin) {
        String desc = pin.getDescription();
        desc += " (" + VSDataType.getDataTypeShortCut(pin.dataType) + ")";
        JLabel lbl = layedLabel;

        lbl.setText(desc);

        Point p = getMousePosition();

        FontMetrics fm = lbl.getFontMetrics(lbl.getFont());
        int xx = fm.stringWidth(desc);
        lbl.setLocation(p.x + 10, p.y - fm.getHeight() * 2);
        lbl.setSize(xx, fm.getHeight());
        lbl.setOpaque(true);

        lbl.updateUI();
    }

    public void startButtonHandler() {
        timer = new javax.swing.Timer(300, new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                //settings.rightSplitterPos=jTabbedPane1.getWidth()+15;

                Basis basis = getAktuelleBasis();

                if (basis == null) {
                    jButton5.setEnabled(false);
                    jmiStart.setEnabled(false);
                    jmiStop.setEnabled(false);
                    jButtonStart.setEnabled(false);
                    jButtonStop.setEnabled(false);
                    jButtonPause.setEnabled(false);
                    jButtonResume.setEnabled(false);
                    jmiPause.setEnabled(false);
                    jmiResume.setEnabled(false);

                    jButtonStep.setEnabled(false);
                    jmiStep.setEnabled(false);
                    jButtonRefreshVM.setEnabled(false);
                    cmdUndo.setEnabled(false);
                    cmdRedo.setEnabled(false);
                    cmdSave.setEnabled(false);
                    jButtonDebug.setEnabled(false);

                    jButton7.setEnabled(false);
                    jButton8.setEnabled(false);
                    jButton9.setEnabled(false);
                    jSpinner1.setEnabled(false);
                    jButton12.setEnabled(false);

                    //elementPaletteCircuit.setEnabled(false);
                    //elementPaletteFront.setEnabled(false);
                    jmniDefineVariables.setEnabled(false);
                    jmniPasswordProtection.setEnabled(false);
                    jmniDeletePasswordProtection.setEnabled(false);
                    jmiEigenschaten.setEnabled(false);

                    jmiUndo.setEnabled(false);
                    jmiRedo.setEnabled(false);
                    jmiCut.setEnabled(false);
                    jmiCopy.setEnabled(false);
                    jmiPaste.setEnabled(false);
                    jmiSelectAny.setEnabled(false);

                    jmiSave.setEnabled(false);
                    jmniSaveAsSingleVM.setEnabled(false);
                    //jmniSaveAs.setEnabled(false);
                    jmiSaveAsModul.setEnabled(false);
                    jmiSaveAsJPG.setEnabled(false);

                    jmiVariableWatcher.setEnabled(false);
                    jmiShowAnalogWindow.setEnabled(false);
                    jmiShowDigitalWindow.setEnabled(false);
                    jmiShowTestpointWindow.setEnabled(false);

                    return;
                }

                jmiVariableWatcher.setEnabled(true);
                jmiShowAnalogWindow.setEnabled(true);
                jmiShowDigitalWindow.setEnabled(true);
                jmiShowTestpointWindow.setEnabled(true);

                //elementPaletteCircuit.setEnabled(true);
                //elementPaletteFront.setEnabled(true);
                jmniDeletePasswordProtection.setEnabled(true);
                jmiEigenschaten.setEnabled(true);
                jmiUndo.setEnabled(true);
                jmiRedo.setEnabled(true);

                jmiCut.setEnabled(true);
                jmiCopy.setEnabled(true);
                jmiPaste.setEnabled(true);
                jmiSelectAny.setEnabled(true);

                jmiSave.setEnabled(true);
                jmniSaveAsSingleVM.setEnabled(true);
                //jmniSaveAs.setEnabled(true);
                jmiSaveAsModul.setEnabled(true);
                jmiSaveAsJPG.setEnabled(true);

                jmniDefineVariables.setEnabled(true);
                jmniPasswordProtection.setEnabled(true);

                jButton12.setEnabled(true);
                jSpinner1.setEnabled(true);
                jButton7.setEnabled(true);
                jButton8.setEnabled(true);
                jButton9.setEnabled(true);

                if (basis != null) {
                    if (basis.projectPath.trim().length() > 0) {
                        ProjectProperties props = Tools.openProjectFile(new File(basis.projectPath));
                        if (props.projectType.equalsIgnoreCase("SPS")) {
                            jButtonDebug.setEnabled(false);
                        } else {
                            jButtonDebug.setEnabled(true);
                        }
                    }
                }

                jButton5.setEnabled(true);
                jButtonRefreshVM.setEnabled(true);
                cmdUndo.setEnabled(true);
                cmdRedo.setEnabled(true);
                cmdSave.setEnabled(true);

                if (basis.isLoading()) {
                    return;
                }

                basis.getFrontBasis().sortSubPanels();

                basis.getCircuitBasis().adjustAllElemnts();
                basis.getFrontBasis().adjustAllElemnts();

                basis.getCircuitBasis().checkPins();

                Element element = getVMObject().getSelectedElement();
                if (element != null) {
                    if (element != oldElement) {
                        comboIsEditing = true;
                        jComboBox1.setSelectedItem(element);
                        comboIsEditing = false;
                    }
                    oldElement = element;
                }
                /*if (getVMObject().getElementCount()!=oldElementCount)
                {
                oldElementCount=getVMObject().getElementCount();
                }*/

                jButtonStart.setEnabled(true);

                jmiSave.setEnabled(true);
                //jmiSaveAsModul.setEnabled(true);
                jmiSelectAny.setEnabled(true);
                jmiStart.setEnabled(true);
                //jButtonDebug.setEnabled(true);
                jmiStop.setEnabled(true);

                jButtonRefreshVM.setEnabled(true);

                jButtonPause.setEnabled(true);
                jButtonResume.setEnabled(true);
                jmiPause.setEnabled(true);
                jmiResume.setEnabled(true);
                jmiStep.setEnabled(true);

                //jComboBox1.setEnabled(true);
                //jComboBox1.setSelectedItem(""+getVMObject().owner.getDelay());
                if (getVMObject().owner.getUndoPointer() > 1) {
                    jmiUndo.setEnabled(true);
                    cmdUndo.setEnabled(true);
                } else {
                    jmiUndo.setEnabled(false);
                    cmdUndo.setEnabled(false);
                }

                if (getVMObject().owner.getUndoPointer() < getVMObject().owner.getUndoHistorySize()) {
                    jmiRedo.setEnabled(true);
                    cmdRedo.setEnabled(true);
                } else {
                    jmiRedo.setEnabled(false);
                    cmdRedo.setEnabled(false);
                }

                if (getVMObject().isRunning()) {
                    jmiStart.setEnabled(false);
                    jmiStop.setEnabled(true);
                    jButtonStart.setEnabled(false);
                    jButtonDebug.setEnabled(false);
                    jButtonStop.setEnabled(true);

                    jButtonStep.setEnabled(true);
                    jmiStep.setEnabled(true);
                    //setBarsEnabled(false);

                    jButtonRefreshVM.setEnabled(false);

                    if (getVMObject().isPaused()) {
                        jButtonPause.setEnabled(false);
                        jButtonResume.setEnabled(true);
                        jmiPause.setEnabled(false);
                        jmiResume.setEnabled(true);

                        jButtonStep.setEnabled(true);
                        jmiStep.setEnabled(true);

                    } else {
                        jmiPause.setEnabled(true);
                        jmiResume.setEnabled(false);

                        jButtonPause.setEnabled(true);
                        jButtonResume.setEnabled(false);

                        jButtonStep.setEnabled(false);
                        jmiStep.setEnabled(false);

                    }

                } else {
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

        timer.start();

        listeAllElements();
    }
    private boolean comboIsEditing = false;

    public void listeAllElements() {

        VMObject vmObject = getVMObject();
        if (vmObject != null) {
            comboIsEditing = true;
            jComboBox1.removeAllItems();

            Element element = null;
            for (int i = 0; i < vmObject.getElementCount(); i++) {
                element = vmObject.getElement(i);
                jComboBox1.addItem(element);
            }
            comboIsEditing = false;
        }
    }
    private ArrayList<String> selectedPaths = new ArrayList<String>();

    public void recursionSaveProjectStatus(MyNode node) {
        if (node instanceof MyNode) {
            MyNode nodex = (MyNode) node;

            if (nodex.isExpanded) {
                File file = new File(node.projectPath + node.relativePath);
                String path = file.getPath();

                selectedPaths.add(path);
                System.out.println("" + path);
            }
            recursionSaveProjectStatus((MyNode) nodex.getNextNode());
        }
    }

    public boolean isPathInSelectedPaths(String path) {
        for (int i = 0; i < selectedPaths.size(); i++) {
            String str = selectedPaths.get(i);

            if (str.equalsIgnoreCase(path)) {
                return true;
            }

        }
        return false;
    }

    public void saveProjectPanelStatus() {
        selectedPaths.clear();
        TreeModel model = projectPalette1.jTree1.getModel();
        recursionSaveProjectStatus((MyNode) model.getRoot());
    }

    public void recursionRestoreProjectStatus() {
        for (int i = 0; i < projectPalette1.jTree1.getRowCount(); i++) {
            TreePath selPath = projectPalette1.jTree1.getPathForRow(i);

            int c = selPath.getPathCount();
            String path = selPath.getPath()[c - 1].toString();

            if (isPathInSelectedPaths(path)) {
                projectPalette1.jTree1.expandRow(i);
            }
        }
    }

    public void reloadProjectPanel() {
        saveProjectPanelStatus();
        projectPalette1.init();

        Collections.sort(projects);
        for (String projectName : projects) {
            projectPalette1.listAllFilesFromProjectPath(projectName);
        }

        recursionRestoreProjectStatus();
        projectPalette1.expandRow(0);
    }

    public static void setLookAndFeel() {

        String nativeLF = UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(nativeLF);
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException | UnsupportedLookAndFeelException ex) {

        }

    }

    public void removeVMPanel(VMEditorPanel panel) {
        paneRight.remove(panel);
        desktop.remove(panel.basis);
    }

    public URL getUserURL() {
        return userURL;
    }

    public void LoadConfigFile() {

        boolean config_file_loaded = false;

        // Alte Config.xml löschen!
        String fileName = getUserURL().getFile() + System.getProperty("file.separator") + "Config.conf";
        File fconf = new File(fileName);
        fconf.delete();

        String fileNameXML = getUserURL().getFile() + System.getProperty("file.separator") + "config.xml";
        File fxml = new File(fileNameXML);
        if (fxml.exists()) {
            try {
                XMLDecoder d = new XMLDecoder(new BufferedInputStream(new FileInputStream(fxml.getAbsolutePath())));
                settings = (Settings) d.readObject();
                d.close();

                config_file_loaded = true;
            } catch (Exception ioe) {
                choiceLanguage(this);
            }
        }

        if (!config_file_loaded) {
            choiceLanguage(this);
        }

        this.setLocation(settings.getMainFrameLocation());

        if (settings.getLanguage().equalsIgnoreCase("de_DE")) {
            Locale.setDefault(new Locale("de", "DE"));
        }
        if (settings.getLanguage().equalsIgnoreCase("en_US")) {
            Locale.setDefault(new Locale("en", "US"));
        }
        if (settings.getLanguage().equalsIgnoreCase("es_ES")) {
            Locale.setDefault(new Locale("es", "ES"));
        }

        errorWarnings = new FrameErrorWarnings();

        System.out.println("Locale=" + settings.getLanguage());
    }
    public static String arg1;

    public static String getArg1() {
        return arg1;
    }

    public static void handleLicense() {
        String licenseFile = FrameMain.userURL.getPath() + "/Licences1.Accepted";
        File file = new File(licenseFile);

        boolean licencesAccepted = false;

        if (file.exists()) {
            licencesAccepted = true;
        } else {
            licencesAccepted = false;
        }

        if (licencesAccepted == false) {
            DialogLicence frmLicence = new DialogLicence(null, true);
            frmLicence.setVisible(true);
            if (frmLicence.result) {
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                System.exit(0);
            }
        }
    }

    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value != null && value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        setUIFont(new javax.swing.plaf.FontUIResource("Tahoma", Font.PLAIN, 11));
        
        SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash != null) {
            Graphics2D g = (Graphics2D) splash.createGraphics();
            Rectangle size = splash.getBounds();
            g.setComposite(AlphaComposite.Clear);
            g.fillRect(0, 0, size.width, size.height);
            g.setPaintMode();
        }

        try {
            FrameMain.userURL = new URL("file:" + System.getProperty("user.home") + System.getProperty("file.separator") + "VisualLogic");
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        setLookAndFeel();

        Tools.appResult = 0;

        File file = new File(FrameMain.userURL.getPath());

        if (!file.exists()) {
            boolean success = file.mkdir();
            if (!success) {
                Tools.showMessage(java.util.ResourceBundle.getBundle("VisualLogic/Messages").getString("Fehler : Verzeichniss konnte nicht erzeugt werden") + " : " + userURL.getPath());
            }
        }

        handleLicense();

        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                System.out.println("Arg[" + i + "]=" + args[i]);
            }
            FrameMain.elementPath = args[0];

            Tools.elementPath = FrameMain.elementPath;
        }

        //JFrame.setDefaultLookAndFeelDecorated(false);
        frm = new FrameMain(args);

        if (args.length >= 2) {
            System.out.println("Arg[1]=" + args[1]);

            arg1 = args[1];
            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    String projectPath = getArg1();

                    ProjectProperties props = Tools.openProjectFile(new File(projectPath));
                    frm.openFileAsFront(projectPath, props.mainVM);
                }
            });
        } else {
            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    frm.setVisible(true);
                }
            });
        }

    }

    public void openFileAsFront(String projectPath, String vmName) {
        globalPath = projectPath + "/" + vmName;
        globalProjectPath = projectPath;
        SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {

            public Object doInBackground() {
                Tools.dialogWait = new DialogWait();
                Tools.dialogWait.setVisible(true);
                Tools.dialogWait.setLocation(Tools.dialogWait.getLocation().x, Tools.dialogWait.getLocation().y - 150);

                frontMode = true;
                Basis basis = addBasisToVMPanel(globalPath, globalProjectPath, true);

                basis.startInFrontMode = true;
                basis.panelMode = true;
                basis.getFrontBasis().sortSubPanels();

                basis.getCircuitBasis().adjustAllElemnts();
                basis.getFrontBasis().adjustAllElemnts();

                basis.getCircuitBasis().checkPins();

                basis.getCircuitBasis().ProcessPinDataType();

                basis.start(false);
                return null;
            }

            protected void done() {
                Tools.dialogWait.dispose();
            }
        };

        worker.execute();


        /*Basis basis = new Basis(this, elementPath);
    basis.panelMode=true;
    addBasisToVMPanel(filename);
    /*basis.loadFromFile(filename,false);
    basis.vmFilename=filename;
    basis.fileName=filename;
    addBasisToVMPanel(basis);
    basis.frameCircuit=this;*/
        //basis.startInFrontMode=true;
        //basis.loading=false;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jmiClearWindow = new javax.swing.JMenuItem();
        jmiCopyToClip = new javax.swing.JMenuItem();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jmniCloseAllVMs = new javax.swing.JMenuItem();
        jPanel5 = new javax.swing.JPanel();
        jToolBar6 = new javax.swing.JToolBar();
        jButton10 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        cmdSave = new javax.swing.JButton();
        cmdUndo = new javax.swing.JButton();
        cmdRedo = new javax.swing.JButton();
        jButtonRefreshVM = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButtonStart = new javax.swing.JButton();
        jButtonDebug = new javax.swing.JButton();
        jButtonStop = new javax.swing.JButton();
        jButtonPause = new javax.swing.JButton();
        jButtonResume = new javax.swing.JButton();
        jButtonStep = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanelLeft = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanelPE = new javax.swing.JPanel();
        jTabPE = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanelElementPalette = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        paneRight = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jButton13 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jMenuBar2 = new javax.swing.JMenuBar();
        jmnuDatei = new javax.swing.JMenu();
        jmiNewProject = new javax.swing.JMenuItem();
        jmiOpenProject = new javax.swing.JMenuItem();
        jmnuOpenSingleVM = new javax.swing.JMenuItem();
        jmiSave = new javax.swing.JMenuItem();
        jmniSaveAsSingleVM = new javax.swing.JMenuItem();
        jmiSaveAsModul = new javax.swing.JMenuItem();
        jmniPrintVM = new javax.swing.JMenuItem();
        jmiSaveAsJPG = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jmiCloseWindow = new javax.swing.JMenuItem();
        jmnuEdit = new javax.swing.JMenu();
        jmiUndo = new javax.swing.JMenuItem();
        jmiRedo = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jmiCut = new javax.swing.JMenuItem();
        jmiCopy = new javax.swing.JMenuItem();
        jmiPaste = new javax.swing.JMenuItem();
        jmiSelectAny = new javax.swing.JMenuItem();
        jmnuVM = new javax.swing.JMenu();
        jmiStart = new javax.swing.JMenuItem();
        jmiStop = new javax.swing.JMenuItem();
        jmiPause = new javax.swing.JMenuItem();
        jmiResume = new javax.swing.JMenuItem();
        jmiStep = new javax.swing.JMenuItem();
        jmiEigenschaten = new javax.swing.JMenuItem();
        jmniDefineVariables = new javax.swing.JMenuItem();
        jmniPasswordProtection = new javax.swing.JMenuItem();
        jmniDeletePasswordProtection = new javax.swing.JMenuItem();
        jmnuExtras = new javax.swing.JMenu();
        jmniOptions = new javax.swing.JMenuItem();
        jmniChooseLanguage = new javax.swing.JMenuItem();
        jmniCreateSubVM = new javax.swing.JMenuItem();
        jmniCreateNewJavaComponent = new javax.swing.JMenuItem();
        jmniUpdater = new javax.swing.JMenuItem();
        jmnuWindow = new javax.swing.JMenu();
        jmiLegend = new javax.swing.JMenuItem();
        jmiVariableWatcher = new javax.swing.JMenuItem();
        jmiShowAnalogWindow = new javax.swing.JMenuItem();
        jmiShowDigitalWindow = new javax.swing.JMenuItem();
        jmiShowTestpointWindow = new javax.swing.JMenuItem();
        jmniShowErrorsAndWarnings = new javax.swing.JMenuItem();
        jmniCloseAllVms = new javax.swing.JMenuItem();
        jmnuHelp = new javax.swing.JMenu();
        jmnuDocs = new javax.swing.JMenu();
        jmiHomepage = new javax.swing.JMenuItem();
        jmiForum = new javax.swing.JMenuItem();
        jmiTutorials = new javax.swing.JMenuItem();
        jmiMantis = new javax.swing.JMenuItem();
        jmniUpdate = new javax.swing.JMenuItem();
        jmiInfo = new javax.swing.JMenuItem();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("VisualLogic/FrameCircuit"); // NOI18N
        jmiClearWindow.setText(bundle.getString("Fenster_bereinigen")); // NOI18N
        jmiClearWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiClearWindowActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jmiClearWindow);

        jmiCopyToClip.setText(bundle.getString("Kopieren")); // NOI18N
        jmiCopyToClip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiCopyToClipActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jmiCopyToClip);

        jmniCloseAllVMs.setText("null");
        jmniCloseAllVMs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniCloseAllVMsActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jmniCloseAllVMs);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocationByPlatform(true);
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                formWindowLostFocus(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                formComponentMoved(evt);
            }
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addWindowStateListener(new java.awt.event.WindowStateListener() {
            public void windowStateChanged(java.awt.event.WindowEvent evt) {
                formWindowStateChanged(evt);
            }
        });

        jPanel5.setPreferredSize(new java.awt.Dimension(100, 38));

        jToolBar6.setBorder(null);
        jToolBar6.setRollover(true);
        jToolBar6.setBorderPainted(false);
        jToolBar6.setOpaque(false);
        jToolBar6.setPreferredSize(new java.awt.Dimension(70, 35));

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/document-new.png"))); // NOI18N
        jButton10.setToolTipText(bundle.getString("NewProject")); // NOI18N
        jButton10.setBorderPainted(false);
        jButton10.setPreferredSize(new java.awt.Dimension(28, 25));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jToolBar6.add(jButton10);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/open24Project.gif"))); // NOI18N
        jButton3.setToolTipText(bundle.getString("openProject")); // NOI18N
        jButton3.setBorderPainted(false);
        jButton3.setPreferredSize(new java.awt.Dimension(28, 25));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar6.add(jButton3);

        cmdSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/save.png"))); // NOI18N
        cmdSave.setToolTipText(bundle.getString("VM_Speichern")); // NOI18N
        cmdSave.setBorderPainted(false);
        cmdSave.setPreferredSize(new java.awt.Dimension(28, 25));
        cmdSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSaveActionPerformed(evt);
            }
        });
        jToolBar6.add(cmdSave);

        cmdUndo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/edit-undo.png"))); // NOI18N
        cmdUndo.setToolTipText(bundle.getString("Rückgängig")); // NOI18N
        cmdUndo.setBorderPainted(false);
        cmdUndo.setPreferredSize(new java.awt.Dimension(28, 25));
        cmdUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdUndoActionPerformed(evt);
            }
        });
        jToolBar6.add(cmdUndo);

        cmdRedo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/edit-redo.png"))); // NOI18N
        cmdRedo.setToolTipText(bundle.getString("Wiederholen")); // NOI18N
        cmdRedo.setBorderPainted(false);
        cmdRedo.setPreferredSize(new java.awt.Dimension(28, 25));
        cmdRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRedoActionPerformed(evt);
            }
        });
        jToolBar6.add(cmdRedo);

        jButtonRefreshVM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/view-refresh.png"))); // NOI18N
        jButtonRefreshVM.setToolTipText(bundle.getString("Reload VM")); // NOI18N
        jButtonRefreshVM.setBorderPainted(false);
        jButtonRefreshVM.setPreferredSize(new java.awt.Dimension(28, 25));
        jButtonRefreshVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshVMActionPerformed(evt);
            }
        });
        jToolBar6.add(jButtonRefreshVM);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/preferences-desktop.png"))); // NOI18N
        jButton2.setToolTipText(bundle.getString("Options")); // NOI18N
        jButton2.setBorderPainted(false);
        jButton2.setPreferredSize(new java.awt.Dimension(28, 25));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar6.add(jButton2);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/variables.png"))); // NOI18N
        jButton5.setToolTipText(bundle.getString("variable_definition")); // NOI18N
        jButton5.setBorderPainted(false);
        jButton5.setPreferredSize(new java.awt.Dimension(28, 25));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar6.add(jButton5);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/Legende.GIF"))); // NOI18N
        jButton6.setToolTipText(bundle.getString("Datentyp-Legende")); // NOI18N
        jButton6.setBorderPainted(false);
        jButton6.setPreferredSize(new java.awt.Dimension(28, 25));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar6.add(jButton6);

        jButtonStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/play24.gif"))); // NOI18N
        jButtonStart.setToolTipText(bundle.getString("Start_VM")); // NOI18N
        jButtonStart.setBorderPainted(false);
        jButtonStart.setPreferredSize(new java.awt.Dimension(28, 25));
        jButtonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStartActionPerformed(evt);
            }
        });
        jToolBar6.add(jButtonStart);

        jButtonDebug.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/startX.png"))); // NOI18N
        jButtonDebug.setToolTipText(bundle.getString("Start_VM_(Debug_Modus)")); // NOI18N
        jButtonDebug.setBorderPainted(false);
        jButtonDebug.setPreferredSize(new java.awt.Dimension(28, 25));
        jButtonDebug.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDebugActionPerformed(evt);
            }
        });
        jToolBar6.add(jButtonDebug);

        jButtonStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/stop24.gif"))); // NOI18N
        jButtonStop.setToolTipText(bundle.getString("Stop_VM")); // NOI18N
        jButtonStop.setBorderPainted(false);
        jButtonStop.setPreferredSize(new java.awt.Dimension(28, 25));
        jButtonStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStopActionPerformed(evt);
            }
        });
        jToolBar6.add(jButtonStop);

        jButtonPause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/pause24.gif"))); // NOI18N
        jButtonPause.setToolTipText(bundle.getString("Pause_VM")); // NOI18N
        jButtonPause.setBorderPainted(false);
        jButtonPause.setPreferredSize(new java.awt.Dimension(28, 25));
        jButtonPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPauseActionPerformed(evt);
            }
        });
        jToolBar6.add(jButtonPause);

        jButtonResume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/Resume.GIF"))); // NOI18N
        jButtonResume.setToolTipText(bundle.getString("Weiter_VM")); // NOI18N
        jButtonResume.setBorderPainted(false);
        jButtonResume.setPreferredSize(new java.awt.Dimension(28, 25));
        jButtonResume.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResumeActionPerformed(evt);
            }
        });
        jToolBar6.add(jButtonResume);

        jButtonStep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/Step24.PNG"))); // NOI18N
        jButtonStep.setToolTipText(bundle.getString("Schritt_VM")); // NOI18N
        jButtonStep.setBorderPainted(false);
        jButtonStep.setPreferredSize(new java.awt.Dimension(28, 25));
        jButtonStep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStepActionPerformed(evt);
            }
        });
        jToolBar6.add(jButtonStep);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/graphDouble.gif"))); // NOI18N
        jButton9.setToolTipText(bundle.getString("NumerikGraphWindow")); // NOI18N
        jButton9.setBorderPainted(false);
        jButton9.setPreferredSize(new java.awt.Dimension(28, 25));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jToolBar6.add(jButton9);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/graphBoolean.gif"))); // NOI18N
        jButton8.setToolTipText(bundle.getString("DigitalGraphWindow")); // NOI18N
        jButton8.setBorderPainted(false);
        jButton8.setPreferredSize(new java.awt.Dimension(28, 25));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jToolBar6.add(jButton8);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/testpoint.PNG"))); // NOI18N
        jButton7.setToolTipText(bundle.getString("TestpointWindow")); // NOI18N
        jButton7.setBorderPainted(false);
        jButton7.setPreferredSize(new java.awt.Dimension(28, 25));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jToolBar6.add(jButton7);

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/utilities-terminal.png"))); // NOI18N
        jButton12.setBorderPainted(false);
        jButton12.setPreferredSize(new java.awt.Dimension(28, 25));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jToolBar6.add(jButton12);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/storage24x24.png"))); // NOI18N
        jButton4.setText(bundle.getString("FrameMain.jButton4.text")); // NOI18N
        jButton4.setToolTipText(bundle.getString("FrameMain.jButton4.toolTipText")); // NOI18N
        jButton4.setBorderPainted(false);
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar6.add(jButton4);

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/Information24.gif"))); // NOI18N
        jButton11.setToolTipText(bundle.getString("Info")); // NOI18N
        jButton11.setBorderPainted(false);
        jButton11.setPreferredSize(new java.awt.Dimension(28, 25));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jToolBar6.add(jButton11);

        jPanel3.setPreferredSize(new java.awt.Dimension(230, 30));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText(bundle.getString("Delay")); // NOI18N
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jLabel2.setPreferredSize(new java.awt.Dimension(120, 15));

        jSpinner1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jSpinner1.setMinimumSize(new java.awt.Dimension(41, 55));
        jSpinner1.setPreferredSize(new java.awt.Dimension(80, 27));
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });
        jSpinner1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jSpinner1PropertyChange(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 144, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSpinner1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .add(jSpinner1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(jToolBar6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 711, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 256, Short.MAX_VALUE)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 225, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .add(jToolBar6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel5, java.awt.BorderLayout.NORTH);

        jSplitPane3.setDividerLocation(150);
        jSplitPane3.setContinuousLayout(true);
        jSplitPane3.setMinimumSize(new java.awt.Dimension(200, 66));
        jSplitPane3.setPreferredSize(new java.awt.Dimension(550, 62));

        jPanelLeft.setMinimumSize(new java.awt.Dimension(100, 12));
        jPanelLeft.setPreferredSize(new java.awt.Dimension(300, 257));
        jPanelLeft.setLayout(new java.awt.BorderLayout());

        jSplitPane1.setDividerLocation(100);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setContinuousLayout(true);
        jSplitPane1.setMinimumSize(new java.awt.Dimension(160, 12));
        jSplitPane1.setPreferredSize(new java.awt.Dimension(202, 257));
        jSplitPane1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jSplitPane1PropertyChange(evt);
            }
        });

        jPanelPE.setPreferredSize(new java.awt.Dimension(150, 5));
        jPanelPE.setLayout(new java.awt.BorderLayout());

        jTabPE.setPreferredSize(new java.awt.Dimension(100, 5));
        jPanelPE.add(jTabPE, java.awt.BorderLayout.CENTER);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("Elements"))); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(100, 50));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jComboBox1.setMinimumSize(new java.awt.Dimension(28, 25));
        jComboBox1.setPreferredSize(new java.awt.Dimension(28, 25));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jComboBox1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jComboBox1MouseDragged(evt);
            }
        });
        jComboBox1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jComboBox1PropertyChange(evt);
            }
        });
        jPanel2.add(jComboBox1, java.awt.BorderLayout.CENTER);

        jPanelPE.add(jPanel2, java.awt.BorderLayout.NORTH);

        jSplitPane1.setRightComponent(jPanelPE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        jPanel1.setPreferredSize(new java.awt.Dimension(200, 0));
        jPanel1.addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {
            public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
            }
            public void ancestorResized(java.awt.event.HierarchyEvent evt) {
                jPanel1AncestorResized(evt);
            }
        });
        jPanel1.setLayout(new java.awt.BorderLayout());
        jSplitPane1.setTopComponent(jPanel1);

        jPanelLeft.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jSplitPane3.setLeftComponent(jPanelLeft);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanelElementPalette.setAutoscrolls(true);
        jPanelElementPalette.setFocusTraversalPolicyProvider(true);
        jPanelElementPalette.setMinimumSize(new java.awt.Dimension(50, 65));
        jPanelElementPalette.setPreferredSize(new java.awt.Dimension(150, 98));
        jPanelElementPalette.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanelElementPaletteMousePressed(evt);
            }
        });
        jPanelElementPalette.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jPanelElementPaletteComponentResized(evt);
            }
        });
        jPanelElementPalette.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jPanelElementPaletteMouseMoved(evt);
            }
        });
        jPanelElementPalette.setLayout(new java.awt.BorderLayout());
        jPanel4.add(jPanelElementPalette, java.awt.BorderLayout.NORTH);

        jPanel6.setLayout(new java.awt.BorderLayout());

        paneRight.setBackground(new java.awt.Color(237, 237, 237));
        paneRight.setPreferredSize(new java.awt.Dimension(500, 500));
        paneRight.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                paneRightMousePressed(evt);
            }
        });
        paneRight.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                paneRightStateChanged(evt);
            }
        });
        jPanel6.add(paneRight, java.awt.BorderLayout.CENTER);

        jPanel7.setFocusTraversalPolicyProvider(true);
        jPanel7.setPreferredSize(new java.awt.Dimension(300, 100));
        jPanel7.setRequestFocusEnabled(false);
        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel8.setAutoscrolls(true);
        jPanel8.setCursor(new java.awt.Cursor(java.awt.Cursor.W_RESIZE_CURSOR));
        jPanel8.setFocusable(false);
        jPanel8.setPreferredSize(new java.awt.Dimension(10, 518));
        jPanel8.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel8MouseDragged(evt);
            }
        });

        jButton13.setBackground(new java.awt.Color(204, 204, 204));
        jButton13.setText(bundle.getString("FrameMain.jButton13.text")); // NOI18N
        jButton13.setToolTipText(bundle.getString("FrameMain.jButton13.toolTipText")); // NOI18N
        jButton13.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .add(jButton13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .add(43, 43, 43)
                .add(jButton13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 43, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(439, Short.MAX_VALUE))
        );

        jPanel7.add(jPanel8, java.awt.BorderLayout.WEST);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setEnabled(false);
        jPanel9.setPreferredSize(new java.awt.Dimension(100, 518));
        jPanel9.setLayout(new java.awt.BorderLayout());
        jPanel7.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel7, java.awt.BorderLayout.EAST);

        jPanel4.add(jPanel6, java.awt.BorderLayout.CENTER);

        jSplitPane3.setRightComponent(jPanel4);

        getContentPane().add(jSplitPane3, java.awt.BorderLayout.CENTER);

        jMenuBar2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jMenuBar2.setFont(new java.awt.Font("Serif", 3, 36)); // NOI18N

        jmnuDatei.setMnemonic('D');
        jmnuDatei.setText(bundle.getString("Datei")); // NOI18N
        jmnuDatei.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnuDateiActionPerformed(evt);
            }
        });

        jmiNewProject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/document-new.png"))); // NOI18N
        jmiNewProject.setText(bundle.getString("NewProject")); // NOI18N
        jmiNewProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiNewProjectActionPerformed(evt);
            }
        });
        jmnuDatei.add(jmiNewProject);

        jmiOpenProject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/open24Project.gif"))); // NOI18N
        jmiOpenProject.setText(bundle.getString("openProject")); // NOI18N
        jmiOpenProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiOpenProjectActionPerformed(evt);
            }
        });
        jmnuDatei.add(jmiOpenProject);

        jmnuOpenSingleVM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/open24VM.gif"))); // NOI18N
        jmnuOpenSingleVM.setText(bundle.getString("VM_Oeffnen")); // NOI18N
        jmnuOpenSingleVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnuOpenSingleVMActionPerformed(evt);
            }
        });
        jmnuDatei.add(jmnuOpenSingleVM);

        jmiSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/save16.png"))); // NOI18N
        jmiSave.setText(bundle.getString("Speichern")); // NOI18N
        jmiSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiSaveActionPerformed(evt);
            }
        });
        jmnuDatei.add(jmiSave);

        jmniSaveAsSingleVM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/save16.png"))); // NOI18N
        jmniSaveAsSingleVM.setText(bundle.getString("Speichern_als...")); // NOI18N
        jmniSaveAsSingleVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniSaveAsSingleVMActionPerformed(evt);
            }
        });
        jmnuDatei.add(jmniSaveAsSingleVM);

        jmiSaveAsModul.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/save16.png"))); // NOI18N
        jmiSaveAsModul.setText(bundle.getString("Als_Modul_speichern")); // NOI18N
        jmiSaveAsModul.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiSaveAsModulActionPerformed(evt);
            }
        });
        jmnuDatei.add(jmiSaveAsModul);

        jmniPrintVM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/document-print.png"))); // NOI18N
        jmniPrintVM.setText(bundle.getString("Print VM")); // NOI18N
        jmniPrintVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniPrintVMActionPerformed(evt);
            }
        });
        jmnuDatei.add(jmniPrintVM);

        jmiSaveAsJPG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/export16.gif"))); // NOI18N
        jmiSaveAsJPG.setText(bundle.getString("Exportieren_als_JPG")); // NOI18N
        jmiSaveAsJPG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiSaveAsJPGActionPerformed(evt);
            }
        });
        jmnuDatei.add(jmiSaveAsJPG);
        jmnuDatei.add(jSeparator1);

        jmiCloseWindow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/system-log-out.png"))); // NOI18N
        jmiCloseWindow.setText(bundle.getString("Schließen")); // NOI18N
        jmiCloseWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiCloseWindowActionPerformed(evt);
            }
        });
        jmnuDatei.add(jmiCloseWindow);

        jMenuBar2.add(jmnuDatei);

        jmnuEdit.setText(bundle.getString("Bearbeiten")); // NOI18N

        jmiUndo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        jmiUndo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/edit-undo.png"))); // NOI18N
        jmiUndo.setText(bundle.getString("Rückgängig")); // NOI18N
        jmiUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiUndoActionPerformed(evt);
            }
        });
        jmnuEdit.add(jmiUndo);

        jmiRedo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        jmiRedo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/edit-redo.png"))); // NOI18N
        jmiRedo.setText(bundle.getString("Wiederholen")); // NOI18N
        jmiRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiRedoActionPerformed(evt);
            }
        });
        jmnuEdit.add(jmiRedo);
        jmnuEdit.add(jSeparator2);

        jmiCut.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        jmiCut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/edit-cut.png"))); // NOI18N
        jmiCut.setText(bundle.getString("Ausschneiden")); // NOI18N
        jmiCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiCutActionPerformed(evt);
            }
        });
        jmnuEdit.add(jmiCut);

        jmiCopy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jmiCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/edit-copy.png"))); // NOI18N
        jmiCopy.setText(bundle.getString("Kopieren")); // NOI18N
        jmiCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiCopyActionPerformed(evt);
            }
        });
        jmnuEdit.add(jmiCopy);

        jmiPaste.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        jmiPaste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/edit-paste.png"))); // NOI18N
        jmiPaste.setText(bundle.getString("Einfügen")); // NOI18N
        jmiPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiPasteActionPerformed(evt);
            }
        });
        jmnuEdit.add(jmiPaste);

        jmiSelectAny.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        jmiSelectAny.setText(bundle.getString("Alles_markieren")); // NOI18N
        jmiSelectAny.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiSelectAnyActionPerformed(evt);
            }
        });
        jmnuEdit.add(jmiSelectAny);

        jMenuBar2.add(jmnuEdit);

        jmnuVM.setText(bundle.getString("VM")); // NOI18N
        jmnuVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnuVMActionPerformed(evt);
            }
        });

        jmiStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/play16.gif"))); // NOI18N
        jmiStart.setText(bundle.getString("Start")); // NOI18N
        jmiStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiStartActionPerformed(evt);
            }
        });
        jmnuVM.add(jmiStart);

        jmiStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/stop16.gif"))); // NOI18N
        jmiStop.setText(bundle.getString("Stop")); // NOI18N
        jmiStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiStopActionPerformed(evt);
            }
        });
        jmnuVM.add(jmiStop);

        jmiPause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/pause16.gif"))); // NOI18N
        jmiPause.setText(bundle.getString("Pause")); // NOI18N
        jmiPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiPauseActionPerformed(evt);
            }
        });
        jmnuVM.add(jmiPause);

        jmiResume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/Resume16.GIF"))); // NOI18N
        jmiResume.setMnemonic('F');
        jmiResume.setText(bundle.getString("Weiter")); // NOI18N
        jmiResume.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiResumeActionPerformed(evt);
            }
        });
        jmnuVM.add(jmiResume);

        jmiStep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/Step16.PNG"))); // NOI18N
        jmiStep.setText(bundle.getString("Schritt")); // NOI18N
        jmiStep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiStepActionPerformed(evt);
            }
        });
        jmnuVM.add(jmiStep);

        jmiEigenschaten.setText(bundle.getString("Eigenschaften")); // NOI18N
        jmiEigenschaten.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiEigenschatenActionPerformed(evt);
            }
        });
        jmnuVM.add(jmiEigenschaten);

        jmniDefineVariables.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/variables16.png"))); // NOI18N
        jmniDefineVariables.setText(bundle.getString("variable_definition")); // NOI18N
        jmniDefineVariables.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniDefineVariablesActionPerformed(evt);
            }
        });
        jmnuVM.add(jmniDefineVariables);

        jmniPasswordProtection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/save.png"))); // NOI18N
        jmniPasswordProtection.setText(bundle.getString("Passwort Protection")); // NOI18N
        jmniPasswordProtection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniPasswordProtectionActionPerformed(evt);
            }
        });
        jmnuVM.add(jmniPasswordProtection);

        jmniDeletePasswordProtection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/unlock.png"))); // NOI18N
        jmniDeletePasswordProtection.setText(bundle.getString("Delete Password protection")); // NOI18N
        jmniDeletePasswordProtection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniDeletePasswordProtectionActionPerformed(evt);
            }
        });
        jmnuVM.add(jmniDeletePasswordProtection);

        jMenuBar2.add(jmnuVM);

        jmnuExtras.setText(bundle.getString("FrameMain.jmnuExtras.text_1")); // NOI18N
        jmnuExtras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnuExtrasActionPerformed(evt);
            }
        });

        jmniOptions.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/preferences-desktop.png"))); // NOI18N
        jmniOptions.setText(bundle.getString("Options")); // NOI18N
        jmniOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniOptionsActionPerformed(evt);
            }
        });
        jmnuExtras.add(jmniOptions);

        jmniChooseLanguage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/preferences-desktop-locale.png"))); // NOI18N
        jmniChooseLanguage.setText(bundle.getString("language")); // NOI18N
        jmniChooseLanguage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniChooseLanguageActionPerformed(evt);
            }
        });
        jmnuExtras.add(jmniChooseLanguage);

        jmniCreateSubVM.setText(bundle.getString("Create SubVM")); // NOI18N
        jmniCreateSubVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniCreateSubVMActionPerformed(evt);
            }
        });
        jmnuExtras.add(jmniCreateSubVM);

        jmniCreateNewJavaComponent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/document-new_Java_Element.png"))); // NOI18N
        jmniCreateNewJavaComponent.setText(bundle.getString("Create Java Component")); // NOI18N
        jmniCreateNewJavaComponent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniCreateNewJavaComponentActionPerformed(evt);
            }
        });
        jmnuExtras.add(jmniCreateNewJavaComponent);

        jmniUpdater.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/storage.png"))); // NOI18N
        jmniUpdater.setText(bundle.getString("FrameMain.jmniUpdater.text")); // NOI18N
        jmniUpdater.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniUpdaterActionPerformed(evt);
            }
        });
        jmnuExtras.add(jmniUpdater);

        jMenuBar2.add(jmnuExtras);

        jmnuWindow.setText(bundle.getString("Fenster")); // NOI18N

        jmiLegend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/legenge16.png"))); // NOI18N
        jmiLegend.setText(bundle.getString("Datentyp-Legende")); // NOI18N
        jmiLegend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiLegendActionPerformed(evt);
            }
        });
        jmnuWindow.add(jmiLegend);

        jmiVariableWatcher.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/auge.png"))); // NOI18N
        jmiVariableWatcher.setText(bundle.getString("Variable Watcher")); // NOI18N
        jmiVariableWatcher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiVariableWatcherActionPerformed(evt);
            }
        });
        jmnuWindow.add(jmiVariableWatcher);

        jmiShowAnalogWindow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/graphDouble16x16.gif"))); // NOI18N
        jmiShowAnalogWindow.setText(bundle.getString("NumerikGraphWindow")); // NOI18N
        jmiShowAnalogWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiShowAnalogWindowActionPerformed(evt);
            }
        });
        jmnuWindow.add(jmiShowAnalogWindow);

        jmiShowDigitalWindow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/graphBoolean16x16.gif"))); // NOI18N
        jmiShowDigitalWindow.setText(bundle.getString("DigitalGraphWindow")); // NOI18N
        jmiShowDigitalWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiShowDigitalWindowActionPerformed(evt);
            }
        });
        jmnuWindow.add(jmiShowDigitalWindow);

        jmiShowTestpointWindow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/testpoint.PNG"))); // NOI18N
        jmiShowTestpointWindow.setText(bundle.getString("TestpointWindow")); // NOI18N
        jmiShowTestpointWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiShowTestpointWindowActionPerformed(evt);
            }
        });
        jmnuWindow.add(jmiShowTestpointWindow);

        jmniShowErrorsAndWarnings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/process-stop.png"))); // NOI18N
        jmniShowErrorsAndWarnings.setText(bundle.getString("Show Errors and Warnings")); // NOI18N
        jmniShowErrorsAndWarnings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniShowErrorsAndWarningsActionPerformed(evt);
            }
        });
        jmnuWindow.add(jmniShowErrorsAndWarnings);

        jmniCloseAllVms.setText(bundle.getString("Close All VMs")); // NOI18N
        jmniCloseAllVms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniCloseAllVmsActionPerformed(evt);
            }
        });
        jmnuWindow.add(jmniCloseAllVms);

        jMenuBar2.add(jmnuWindow);

        jmnuHelp.setText(bundle.getString("Hilfe")); // NOI18N

        jmnuDocs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/book.png"))); // NOI18N
        jmnuDocs.setText(bundle.getString("FrameMain.jmnuDocs.text")); // NOI18N
        jmnuHelp.add(jmnuDocs);

        jmiHomepage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/applications-internet.png"))); // NOI18N
        jmiHomepage.setText(bundle.getString("FrameMain.jmiHomepage")); // NOI18N
        jmiHomepage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiHomepageActionPerformed(evt);
            }
        });
        jmnuHelp.add(jmiHomepage);

        jmiForum.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/applications-internet.png"))); // NOI18N
        jmiForum.setText(bundle.getString("FrameMain.jmiForum")); // NOI18N
        jmiForum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiForumActionPerformed(evt);
            }
        });
        jmnuHelp.add(jmiForum);

        jmiTutorials.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/applications-internet.png"))); // NOI18N
        jmiTutorials.setText(bundle.getString("FrameMain.jmiTutorials")); // NOI18N
        jmiTutorials.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiTutorialsActionPerformed(evt);
            }
        });
        jmnuHelp.add(jmiTutorials);

        jmiMantis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/applications-internet.png"))); // NOI18N
        jmiMantis.setText(bundle.getString("FrameMain.jmiMantis")); // NOI18N
        jmiMantis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiMantisActionPerformed(evt);
            }
        });
        jmnuHelp.add(jmiMantis);

        jmniUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/system-software-update.png"))); // NOI18N
        jmniUpdate.setText(bundle.getString("Update")); // NOI18N
        jmniUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniUpdateActionPerformed(evt);
            }
        });
        jmnuHelp.add(jmniUpdate);

        jmiInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/Information16.gif"))); // NOI18N
        jmiInfo.setText(bundle.getString("Info")); // NOI18N
        jmiInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiInfoActionPerformed(evt);
            }
        });
        jmnuHelp.add(jmiInfo);

        jMenuBar2.add(jmnuHelp);

        setJMenuBar(jMenuBar2);
    }// </editor-fold>//GEN-END:initComponents

    private boolean unusedPinsExist() {
        Basis basis = getAktuelleBasis();
        if (basis != null) {
            VMObject circuit = basis.getCircuitBasis();

            // Alle Element als noch nicht Compiliert markieren
            for (int i = 0; i < circuit.getElementCount(); i++) {
                Element element = circuit.getElement(i);

                for (int j = 0; j < element.getPinCount(); j++) {
                    JPin pin = element.getPin(j);

                    if (pin.draht == null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private String extraceNumber(String str, int startIndex) {
        String ch;
        for (int i = startIndex; i < str.length(); i++) {
            ch = str.substring(i, i + 1);
            if (ch.equalsIgnoreCase("%")) {
                String res = str.substring(startIndex, i);
                return res;
            }
        }
        return "";
    }

    private String eval(String str) {
        String result = "";
        Expression parser = new Expression();
        ParserCode.Expression.Scanner scanner = new ParserCode.Expression.Scanner(new java.io.StringReader(str));

        Expression.liste.clear();

        while (scanner.ttype != ParserCode.Expression.Scanner.TT_EOF) {
            try {
                parser.yyparse(scanner, null);

                for (int i = 0; i < Expression.liste.size(); i++) {
                    result += Expression.liste.get(i) + "\n";
                    //System.err.println(""+Expression.liste.get(i));
                }
                break;

            } catch (Exception ye) {
                System.err.println(scanner + ": " + ye);
            }
        }

        return result;
    }

    private String substitude(String str, Element element) {
        int index;

        while (true) {
            index = str.indexOf("%eval-property%");

            if (index > -1) {
                int idx = str.indexOf("\n", index);

                System.out.println("idx=" + idx);
                String aa = "%eval-property%";
                String str2 = str.substring(index + aa.length(), idx);
                str2 = str2.trim();

                // Zeile entfernen das Wort aus dem String!
                String strOben = str.substring(0, index);

                ElementProperty prop = element.getProperty(str2);

                String strMitte = "";
                if (prop != null) {
                    if (prop.referenz != null) {
                        strMitte = "" + eval(prop.referenz.toString());
                    }
                }
                String strUnten = str.substring(idx, str.length());

                str = strOben + strMitte + strUnten;
                System.out.println("" + str);
            }
            if (index == -1) {
                break;
            }

        }

        while (true) {
            index = str.indexOf("%get-property%");

            if (index > -1) {
                int idx = str.indexOf(" ", index);

                int idx2 = str.indexOf(" ", idx + 1);
                int idx3 = str.indexOf("\n", index);

                if (idx2 > -1) {
                    idx = idx2;
                } else if (idx3 > -1) {
                    idx = idx3;
                }

                System.out.println("idx=" + idx);
                String aa = "%get-property%";
                String str2 = str.substring(index + aa.length(), idx);
                str2 = str2.trim();

                // Zeile entfernen das Wort aus dem String!
                String strOben = str.substring(0, index);

                ElementProperty prop = element.getProperty(str2);

                String strMitte = "";
                if (prop != null) {
                    if (prop.referenz != null) {
                        strMitte = prop.referenz.toString();
                    }
                }
                String strUnten = str.substring(idx, str.length());

                str = strOben + strMitte + strUnten;
                System.out.println("" + str);
            }
            if (index == -1) {
                break;
            }

        }

        while (true) {

            index = str.indexOf("%pin");

            if (index > -1) {
                String number = extraceNumber(str, index + 4);

                // Lösche das Wort aus dem String!
                String newString = str.substring(0, index);

                JPin pin = element.getPin(Integer.parseInt(number));

                if (pin.draht != null) {
                    int id = pin.draht.getDestElementID();
                    /*if (id==element.getID())
                    {
                    newString+="element_"+element.getID()+"_pin"+number;
                    }else*/
                    {
                        int destPinNr = pin.draht.getDestPin();
                        String elementPin = "ELEMENT" + id + "_PIN" + destPinNr;

                        newString += elementPin;
                        //int regNr=suchNachEntsprechenderRegisterNr(elementPin);
                        //newString+="R"+regNr;
                    }
                } else {
                    newString += "R0";
                }

                newString += str.substring(index + 4 + number.length() + 1, str.length());
                str = newString;
            }

            if (index == -1) {
                break;
            }
        }

        while (true) {
            index = str.indexOf("%procedure");

            if (index > -1) {
                // Lösche das Wort aus dem String!
                String newString = str.substring(0, index);

                String xx = "ELEMENT" + element.getID();
                newString += xx;
                newString += str.substring(index + 11, str.length());
                str = newString;
            }

            if (index == -1) {
                break;
            }
        }

        while (true) {
            index = str.indexOf("%notify");

            if (index > -1) {
                String number = extraceNumber(str, index + 7);
                if (number.trim().equalsIgnoreCase("")) {
                    Tools.showMessage(this, "Notify Number not found!");
                    return str;
                }

                // Lösche das Wort aus dem String!
                String newString = str.substring(0, index);

                //newString+="NOTIFY";
                JPin pin = element.getPin(Integer.parseInt(number));
                if (number.trim().equalsIgnoreCase("")) {
                    Tools.showMessage(this, "Notify Number not found!");
                    return str;
                }

                if (pin.draht != null) {
                    int id = pin.draht.getDestElementID();

                    newString += "PUSH_NEXTELEMENT ELEMENT" + id + "// PUSHT die Adresse des NextElements in den Stack\n";
                } else {
                    newString += "";
                }
                newString += str.substring(index + 7 + number.length() + 1, str.length());
                str = newString;
            }

            if (index == -1) {
                break;
            }
        }

        while (true) {
            index = str.indexOf("%nextElement");

            if (index > -1) {
                String number = extraceNumber(str, index + 12);
                if (number.trim().equalsIgnoreCase("")) {
                    Tools.showMessage(this, "nextElement-PinNr not found!");
                    return str;
                }

                // Lösche das Wort aus dem String!
                String newString = str.substring(0, index);

                //newString+="NOTIFY";
                JPin pin = element.getPin(Integer.parseInt(number));
                if (number.trim().equalsIgnoreCase("")) {
                    Tools.showMessage(this, "nextElement-PinNr not found!");
                    return str;
                }

                if (pin.draht != null) {
                    int id = pin.draht.getDestElementID();
                    /*if (id==element.getID())
                    {
                    Tools.showMessage(this,"NotifyID==elementID!");
                    return str;
                    }else*/
                    {
                        int destPinNr = pin.draht.getDestPin();
                        newString += "ELEMENT" + id;
                        //newString += "ADD_NEXTELEMENT // Adress in NextElement Queue FIFO!\n";
                    }
                } else {
                    newString += "";
                }

                newString += str.substring(index + 12 + number.length() + 1, str.length());
                str = newString;
            }

            if (index == -1) {
                break;
            }
        }

        // Einheitliche Formatierung : Einrücken        
        String result = "";

        int i = 0;
        int old = 0;
        String ch = "";
        while (i < str.length()) {
            ch = str.substring(i, i + 1);
            if (ch.equalsIgnoreCase("\n")) {
                String aa = str.substring(old, i).trim() + "\n";
                result += "  " + aa;
                //System.out.println("aa="+aa);
                old = i;
            }
            i++;
        }

        return result;
    }

    // Ergebnis ist der Code der Linken, eigener und Rechten Elemente
    // PinNr ist der zu analysierende Pin des Elements
    private String recursion(VMObject circuit, int srcElementID, Element element) {
        //System.out.println(element.getCaption());
        element.isAlreadyCompiled = true;
        String code = "";
        // Alle Inputs 
        for (int j = 0; j < element.getPinCount(); j++) {
            JPin pin = element.getPin(j);

            if (pin.draht != null && pin.pinIO == JPin.PIN_INPUT) {
                if (pin.draht.getSourceElementID() == srcElementID) {
                } else {
                    Element el = circuit.getElementWithID(pin.draht.getSourceElementID());

                    if (!el.isAlreadyCompiled) {
                        code += recursion(circuit, element.getID(), el);
                    }

                }
            }
        }
        String eigenerCode = (String) element.jGetTag(2);
        //code += eigenerCode;
        //code += substitudeMCUFlowChart(eigenerCode, element);
        code += substitude(eigenerCode, element);

        // Alle Outputs
        for (int j = 0; j < element.getPinCount(); j++) {
            JPin pin = element.getPin(j);

            if (pin.draht != null && pin.pinIO == JPin.PIN_OUTPUT) {
                if (pin.draht.getDestElementID() == srcElementID) {
                } else {
                    Element el = circuit.getElementWithID(pin.draht.getDestElementID());
                    if (!el.isAlreadyCompiled) {
                        code += recursion(circuit, element.getID(), el);
                    }
                }
            }

        }

        return code;
    }

    public String generateMCUCode() {
        Basis basis = getAktuelleBasis();

        String code = "";
        code += "// *********************************************************\n";
        code += "// * Code generated from MyOpenLab (www.MyOpenLab.de)\n";
        code += "// * MyOpenLab is Freeware!\n";
        code += "// * This Code is generated from MyOpenLab v." + Version.strApplicationVersion + " " + Version.strStatus + "\n";
        code += "// * Date : " + new java.util.Date() + " \n";
        code += "// *********************************************************\n\n";

        //code+="GOTO MAIN  // JUMP To Main Routine\n\n";
        if (basis != null) {
            VMObject circuit = basis.getCircuitBasis();

            // Globals          
            code += "// GLOBALS\n";
            for (int i = 0; i < circuit.getElementCount(); i++) {
                Element element = circuit.getElement(i);
                String str = (String) element.jGetTag(4);
                if (str != null) {
                    code += str;
                }
            }
            code += "\n";

            // Alle Element als noch nicht Compiliert markieren
            for (int i = 0; i < circuit.getElementCount(); i++) {
                Element element = circuit.getElement(i);
                element.isAlreadyCompiled = false;
            }

            code += "BEGIN:\n";

            for (int i = 0; i < circuit.getElementCount(); i++) {
                Element element = circuit.getElement(i);
                if (element.isAlreadyCompiled == false) {
                    code += recursion(circuit, -1, element);
                }
            }
            code += "GOTO BEGIN\n";
        }

        return code;
    }

    /*private String substitudeMCUFlowChart(String str, Element element)
    {
    int index;
    while (true)
    {
    index = str.indexOf("%pin");
    if (index > -1)
    {
    String number = extraceNumber(str, index + 4);
    // Lösche das Wort aus dem String!
    String newString = str.substring(0, index);
    JPin pin = element.getPin(Integer.parseInt(number));
    if (pin.draht != null)
    {
    int id = pin.draht.getDestElementID();
    //int destPinNr = pin.draht.getDestPin();
    String elementPin = "ELEMENT" + id;
    newString += elementPin;
    }
    newString += str.substring(index + 4 + number.length() + 1, str.length());
    str = newString;
    }
    if (index == -1)
    {
    break;
    }
    }
    return str;
    }*/
    // Ergebnis ist der Code der Linken, eigener und Rechten Elemente
    // PinNr ist der zu analysierende Pin des Elements
    private String recursionMCUFlowChart(VMObject circuit, Element element) {
        //System.out.println(element.getCaption());
        element.isAlreadyCompiled = true;
        String code = "";

        String eigenerCode = (String) element.jGetTag(2);

        //code += substitudeMCUFlowChart(eigenerCode, element);
        code += substitude(eigenerCode, element);

        // Alle Outputs
        for (int i = 0; i < element.getPinCount(); i++) {
            JPin pin = element.getPin(i);
            if (pin.draht != null && pin.pinIO == JPin.PIN_OUTPUT) {
                int dstID = pin.draht.getDestElementID();
                //int dstPin = pin.draht.getDestPin();

                Element dstElement = circuit.getElementWithID(dstID);
                if (!dstElement.isAlreadyCompiled) {
                    code += recursionMCUFlowChart(circuit, dstElement);
                }
            }
        }

        return code;

    }

    public String generateMCUFlowChartCode() {
        Basis basis = getAktuelleBasis();

        String code = "";
        code += "// *********************************************************\n";
        code += "// * Code generated from MyOpenLab (www.MyOpenLab.de)\n";
        code += "// * MyOpenLab is Freeware!\n";
        code += "// * This Code is generated from MyOpenLab v." + Version.strApplicationVersion + " " + Version.strStatus + "\n";
        code += "// * Date : " + new java.util.Date() + " \n";
        code += "// *********************************************************\n\n";

        //code+="GOTO MAIN  // JUMP To Main Routine\n\n";
        if (basis != null) {
            VMObject circuit = basis.getCircuitBasis();

            // Remove all Errors and Warnings
            errorWarnings.clearMessages();

            // Code generierung für jedes Element auslösen!
            for (int i = 0; i < circuit.getElementCount(); i++) {
                Element element = circuit.getElement(i);

                element.classRef.xonStart();
            }
            String errors = errorWarnings.getMessages();

            if (errors.trim().length() > 0) {
                return "";
            }

            // Variablen
            code += "// Variables\n";

            String[] vars = basis.vsGetVariablesNames();

            for (int j = 0; j < vars.length; j++) {
                code += "  DIM " + vars[j] + ", SSHORT\n";
            }

            // Globals          
            code += "// GLOBALS\n";
            for (int i = 0; i < circuit.getElementCount(); i++) {
                Element element = circuit.getElement(i);
                String str = (String) element.jGetTag(4);
                if (str != null) {
                    code += str;
                }
            }
            code += "\n";

            // code += "GOTO MAIN\n";
            // Alle Element als noch nicht Compiliert markieren
            for (int i = 0; i < circuit.getElementCount(); i++) {
                Element element = circuit.getElement(i);
                element.isAlreadyCompiled = false;
            }

            int startProcID = 0;

            for (int i = 0; i < circuit.getElementCount(); i++) {
                Element element = circuit.getElement(i);
                String str = element.getInternName();

                if (element.isAlreadyCompiled == false && str.equalsIgnoreCase("#MCU-FLOWCHART-START#")) {
                    startProcID = element.getID();

                    code += recursionMCUFlowChart(circuit, element);
                }
            }

            //code += "// MAIN\n";
            //code += "MAIN:\n";
            //code += "CALL ELEMENT"+startProcID+"\n";
        }

        return code;
    }

    private boolean isElementMCUFlowChartElement(Element element) {
        String str = element.getInternName().substring(0, 13);
        if (str.equalsIgnoreCase("#MCU-FLOWCHART")) {
            return true;
        }
        return false;
    }

    public String generateMCUVMCode() {
        Basis basis = getAktuelleBasis();
        String code = "";
        code += "// *********************************************************\n";
        code += "// * Code generated from MyOpenLab (www.MyOpenLab.de)\n";
        code += "// * MyOpenLab is Freeware!\n";
        code += "// * This is a nano VM for MyOpenLab v." + Version.strApplicationVersion + " " + Version.strStatus + "\n";
        code += "// * Date : " + new java.util.Date() + " \n";
        code += "// *********************************************************\n\n\n";
        //code+="GOTO MAIN  // JUMP To Main Routine\n\n";
        if (basis != null) {
            VMObject circuit = basis.getCircuitBasis();
            for (int i = 0; i < circuit.getElementCount(); i++) {
                Element element = circuit.getElement(i);
                String eventHandler = (String) element.jGetTag(3);
                if (eventHandler != null) {
                    code += eventHandler;
                }
            }
            code += "\n";
            //code+=" // Register 0 is only for Operation Results (ADD, SUB, AND,...)\n";
            // Alles Element.Pins eine Variable zuweisen
            code += "  // Variables\n";
            for (int i = 0; i < circuit.getElementCount(); i++) {
                Element element = circuit.getElement(i);
                for (int j = 0; j < element.getPinCount(); j++) {
                    if (element.getPin(j).draht != null) {
                        if (element.getPin(j).dataType == ExternalIF.C_BOOLEAN) {
                            code += "  DIM ELEMENT" + element.getID() + "_PIN" + j + ",BYTE\n";
                        }
                        if (element.getPin(j).dataType == ExternalIF.C_INTEGER) {
                            code += "  DIM ELEMENT" + element.getID() + "_PIN" + j + ",WORD\n";
                        }

                        //code += "  DIM ELEMENT" + element.getID() + "_PIN" + j + "\n";
                    }

                }
            }
            code += "\n";
            // Element Globals
            code += "  // Element globals\n";
            for (int i = 0; i < circuit.getElementCount(); i++) {
                Element element = circuit.getElement(i);
                String str = (String) element.jGetTag(4);
                if (str != null) {
                    code += substitude(str, element);
                }
            }
            code += "\n";
            code += " GOTO MAIN\n";
            code += "\n";

            // Deklaration der Prozeduren
            for (int i = 0; i < circuit.getElementCount(); i++) {
                Element element = circuit.getElement(i);
                code += "//******************************************************\n";
                code += "//* FUNCTION " + element.getCaption() + "\n";
                code += "//******************************************************\n";
                code += "ELEMENT" + element.getID() + ":\n";
                code += "\n";
                String str = (String) element.jGetTag(2) + "\n";
                if (str != null) {
                    code += substitude(str, element);
                }
                code += "RETURN\n\n";
            }
            // Initalisationen
            code += "MAIN:\n";
            code += "  //INIT-BLOCK\n";
            for (int i = 0; i < circuit.getElementCount(); i++) {
                Element element = circuit.getElement(i);
                String str = (String) element.jGetTag(5);
                if (str != null && str.trim().length() > 0) {
                    code += "  //--INIT : ELEMENT_" + element.getID() + "-" + element.getCaption() + "\n";
                    code += substitude(str, element);
                    //System.out.println("code="+code);
                }
            }
            code += "MAIN_BEGIN:\n";
            // Main-Init Block
            code += "  //---BEGIN EVENT-Handler \n";
            for (int i = 0; i < circuit.getElementCount(); i++) {
                Element element = circuit.getElement(i);
                String eventHandler = (String) element.jGetTag(1);
                if (eventHandler != null && eventHandler.trim().length() > 0) {
                    code += "  //--EVENT-H : ELEMENT_" + element.getID() + "-" + element.getCaption() + "\n";
                    code += substitude(eventHandler, element);
                }
            }
            code += "  //---END EVENT-Handler \n";
            for (int i = 0; i < circuit.getElementCount(); i++) {
                Element element = circuit.getElement(i);
                String eventHandler = (String) element.jGetTag(0);
                if (eventHandler != null && eventHandler.equalsIgnoreCase("REGISTER_AS_EVENTHANDLER")) {
                    code += "    element_" + element.getID() + "();\n";
                }
            }

            code += "  PROCESS_NEXTELEMENT\n";
            code += "  PROCESS_TIMERS\n";

            /*code += "  MAIN_LOOP:\n";
            code += "    POP NEXTELEMENT        // GET NEXTELEMENT\n";
            code += "    CMP NEXTELEMENT,0      // if (NEXTELEMENT==0) Stack=0 -> Stack is Empty!\n";
            code += "    JMP_IF_A=B MAIN_BEGIN  // then goto MAIN\n";
            code += "    CALL NEXTELEMENT       // CALLS NEXTELEMENT procedure\n";*/
            code += "  GOTO MAIN_BEGIN\n";
            code += "MAIN_EXIT:\n";
        }
        return code;
    }

    private void jmniSaveAsSingleVMActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniSaveAsSingleVMActionPerformed
    {//GEN-HEADEREND:event_jmniSaveAsSingleVMActionPerformed
        Basis basis = getAktuelleBasis();

        if (basis != null) {
            basis.saveAs();
        }
    }//GEN-LAST:event_jmniSaveAsSingleVMActionPerformed

    private void jmniUpdateActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniUpdateActionPerformed
    {//GEN-HEADEREND:event_jmniUpdateActionPerformed

        if (Tools.appResult != 10) {
            DialogUpdate frm = new DialogUpdate(this, true);
            frm.setVisible(true);
        } else {
            Tools.showMessage(this, java.util.ResourceBundle.getBundle("VisualLogic/DialogUpdate").getString("Please_restart_the_Application!"), JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jmniUpdateActionPerformed

    private boolean isInJDKMode() {
        JavaCompiler jc = null;
        StandardJavaFileManager sjfm = null;
        try {

            jc = ToolProvider.getSystemJavaCompiler();
            sjfm = jc.getStandardFileManager(null, null, null);

        } catch (Exception ex) {
            return false;
        }

        return true;
    }

    private void jmniCreateNewJavaComponentActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniCreateNewJavaComponentActionPerformed
    {//GEN-HEADEREND:event_jmniCreateNewJavaComponentActionPerformed

        if (isInJDKMode()) {
            DialogNewJavaComponentAssistent frm = new DialogNewJavaComponentAssistent(this, true);
            frm.setVisible(true);

            if (frm.result) {
                String mappedFile = Tools.mapFile(elementPath + frm.group);
                String grp = new File(mappedFile).getAbsolutePath();

                String circuitPath = new File(Tools.userElementPath + "/CircuitElements").getAbsolutePath();
                String frontPath = new File(Tools.userElementPath + "/FrontElements").getAbsolutePath();

                /*if (grp.equalsIgnoreCase(circuitPath) || grp.equalsIgnoreCase(frontPath))
                {
                Tools.showMessage(this,java.util.ResourceBundle.getBundle("VisualLogic/Messages").getString("you can not put Element in Root Directory!"));
                return;
                }*/
                String basisDir = grp + "/" + frm.compName;
                String srcDir = grp + "/" + frm.compName + "/src";
                String binDir = grp + "/" + frm.compName + "/bin";

                String str = grp + "/" + frm.compName;
                new File(str).mkdir();
                new File(srcDir).mkdir();
                new File(binDir).mkdir();
                try {
                    copyFiles(elementPath + "/compTemplate/icon.gif", str + "/icon.gif", false);
                    copyFiles(elementPath + "/compTemplate/bin/image.gif", str + "/bin/image.gif", false);

                    copyFiles(elementPath + "/compTemplate/src/make.bat", str + "/src/make.bat", false);

                    frm.generateSrcFiles(basisDir, srcDir, frm.type);

                    if (frm.type == 0) {
                        jPanelElementPalette.removeAll();
                        jPanelElementPalette.add(elementPaletteCircuit, BorderLayout.CENTER);
                        elementPaletteFront.setVisible(false);
                        elementPaletteCircuit.setVisible(true);

                        elementPaletteCircuit.aktuellesVerzeichniss = "/CircuitElements/2user-defined/";
                        elementPaletteCircuit.loadFolder(elementPaletteCircuit.aktuellesVerzeichniss);

                    } else {
                        jPanelElementPalette.removeAll();
                        jPanelElementPalette.add(elementPaletteFront, BorderLayout.CENTER);
                        elementPaletteFront.setVisible(true);
                        elementPaletteCircuit.setVisible(false);

                        elementPaletteFront.aktuellesVerzeichniss = "/FrontElements/2user-defined/";
                        elementPaletteFront.loadFolder(elementPaletteFront.aktuellesVerzeichniss);

                    }

                    Tools.showMessage(this, java.util.ResourceBundle.getBundle("VisualLogic/Messages").getString("Don't forget to compile the new Element"));

                    //frmCodeEditor frmCode = new frmCodeEditor(this);
                    String strGrp = "";
                    if (frm.type == 0) {
                        strGrp = "/CircuitElements/";
                    } else {
                        strGrp = "/FrontElements/";
                    }

                    openEditor(Tools.userElementPath + strGrp + frm.compName);

                    //frmCode.execute(elementPath, strGrp+frm.compName, frm.compName);
                    //copyFiles(elementPath+"/compTemplate/src",srcDir,false);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                elementPaletteCircuit.loadFolder(elementPaletteCircuit.aktuellesVerzeichniss);
                elementPaletteFront.loadFolder(elementPaletteFront.aktuellesVerzeichniss);

            }
            frm.dispose();
            //frameCircuit.openJavaEditor(element);

        } else {

            String message = java.util.ResourceBundle.getBundle("VisualLogic/FrameCircuit").getString("TO_COMPILE_YOU_NEED_JDK");
            Tools.showMessage(this, message);
        }
    }//GEN-LAST:event_jmniCreateNewJavaComponentActionPerformed

    public void editModule(String filePath) {
        DialogSaveAsModul frmSave = new DialogSaveAsModul(this, this, true);

        frmSave.executeEdit(filePath);
    }

    private void jmniPrintVMActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniPrintVMActionPerformed
    {//GEN-HEADEREND:event_jmniPrintVMActionPerformed

        if (getAktuelleBasis() != null) {
            PrinterJob printJob = PrinterJob.getPrinterJob();
            printJob.setPrintable(getVMObject());
            if (printJob.printDialog()) {
                try {
                    printJob.print();
                } catch (PrinterException pe) {
                    System.out.println("Error printing: " + pe);
                }
            }
        }

    }//GEN-LAST:event_jmniPrintVMActionPerformed

    private void jComboBox1MouseDragged(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jComboBox1MouseDragged
    {//GEN-HEADEREND:event_jComboBox1MouseDragged

    }//GEN-LAST:event_jComboBox1MouseDragged

    /**
     * generate a new subVM
     *
     * @param projectPath
     * @param pinsLeft count of input-pins
     * @param pinsRight count of output-pins
     * @param vmFilepath filepath for the new subVM
     */
    public void generateSubVM(String projectPath, int pinsLeft, int pinsRight, String vmFilepath) {
        Basis basis = createNewVM();

        String filename = vmFilepath;//projectPath + "/" + vmFilename + ".vlogic";
        if (basis.isWriteable(new File(filename))) {
            addBasisToVMPanel(basis);
            basis.projectPath = projectPath;

            VMObject vm = basis.getCircuitBasis();
            if (basis.projectPath.length() == 0) {
                basis.save();
            } else {
                basis.saveToFile(filename, false);
                basis.vmFilename = filename;
                basis.fileName = filename;

                reloadProjectPanel();
            }

            int xPositionInPins = 50;
            int yPos = 50;
            for (int i = 0; i < pinsLeft; i++) {
                Element inputPin = Tools.addInputPin(vm, null);
                inputPin.setLocation(xPositionInPins, yPos);
                yPos += 50;
            }

            int xPositionOutPins = 500;
            yPos = 50;
            for (int i = 0; i < pinsRight; i++) {
                Element outputPin = Tools.addOutputPin(vm, null);
                outputPin.setLocation(xPositionOutPins, yPos);
                yPos += 50;
            }
        }
    }

    private void jmnuExtrasActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmnuExtrasActionPerformed
    {//GEN-HEADEREND:event_jmnuExtrasActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_jmnuExtrasActionPerformed

    private void jmniCreateSubVMActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniCreateSubVMActionPerformed
    {//GEN-HEADEREND:event_jmniCreateSubVMActionPerformed
        if (getAktuelleBasis() != null) {
            VMObject vm = getAktuelleBasis().getCircuitBasis();
            vm.status = new StatusGummiBandX(vm, this);

            // ruft dann binFertig() auf!!
        }

    }//GEN-LAST:event_jmniCreateSubVMActionPerformed

    class BackupDrahtInput {

        int sourceElementID;
        int sourcePin;
        int pinIndex;
    }

    class BackupDrahtOutut {

        int destElementID;
        int destPin;
        int pinIndex;
    }

    public ArrayList<LineInfo> selectAllDrahtsWhereIntersectWithRectaqngle(ArrayList<Element> elements, int x1, int y1, int x2, int y2, int align) {
        ArrayList<LineInfo> result = new ArrayList<LineInfo>();

        Element element;
        JPin pin;
        int i, j;

        for (i = 0; i < elements.size(); i++) {
            element = elements.get(i);

            for (j = 0; j < element.getPinCount(); j++) {
                pin = element.getPin(j);

                if (pin != null && pin.draht != null && pin.getPinAlign() == align) {
                    Line line = pin.draht.schneidetLinieDenDraht(x1, y1, x2, y2);
                    if (line != null) {
                        LineInfo info = new LineInfo();
                        info.line = line;
                        info.pin = pin;
                        result.add(info);
                    }
                }
            }
        }

        // Sortieren! für LEFT Lines!
        // also nach Y sortieren
        if (align == 1 || align == 3) {
            Collections.sort(result, new Comparator() {

                public int compare(Object o1, Object o2) {
                    LineInfo info1 = ((LineInfo) o1);
                    LineInfo info2 = ((LineInfo) o2);

                    Line line1 = info1.line;
                    Line line2 = info1.line;
                    if (line1.getStartPoint().y < line2.getStartPoint().y) {
                        return -1;
                    }
                    if (line1.getStartPoint().y > line2.getStartPoint().y) {
                        return 1;
                    }
                    if (line1.getStartPoint().y == line2.getStartPoint().y) {
                        return 0;
                    }

                    return 0;
                }
            });
        }

        if (align == 0 || align == 2) {
            Collections.sort(result, new Comparator() {

                public int compare(Object o1, Object o2) {
                    LineInfo info1 = ((LineInfo) o1);
                    LineInfo info2 = ((LineInfo) o2);

                    Line line1 = info1.line;
                    Line line2 = info1.line;

                    if (line1.getStartPoint().x < line2.getStartPoint().x) {
                        return -1;
                    }
                    if (line1.getStartPoint().x > line2.getStartPoint().x) {
                        return 1;
                    }
                    if (line1.getStartPoint().x == line2.getStartPoint().x) {
                        return 0;
                    }

                    return 0;
                }
            });
        }

        return result;
    }

    public void packBasis(Basis basis) {
        if (basis != null) {
            basis.getCircuitBasis().pack();
            basis.getFrontBasis().pack();
        }
    }

    public void processDraht(LineInfo info, VMObject vm) {
        Draht draht = info.line.getDraht();

        Element srcElement = vm.getElementWithID(draht.sourceElementID);
        Element dstElement = vm.getElementWithID(draht.destElementID);

        int srcPin = draht.sourcePin;
        int dstPin = draht.destPin;

        JPin srcPinX = srcElement.getPin(srcPin);
        JPin dstPinX = dstElement.getPin(dstPin);

        if (info.pin != null && info.pin.pinIO == JPin.PIN_INPUT) {
            System.out.println("X INPUT");

            BackupDrahtInput tmp = new BackupDrahtInput();
            tmp.sourceElementID = draht.sourceElementID;
            tmp.sourcePin = draht.sourcePin;

            tmp.pinIndex = IPindex++;
            backupDrahtsInputs.add(tmp);

            srcElement.getPin(srcPin).draht = null;

            String[] args = new String[1];
            args[0] = "" + info.pin.dataType;

            Element inputPin = Tools.addInputPin(vm, args);
            inputPin.setLocation(10, yx1);

            inputPin.getPin(0).draht = draht;

            draht.sourceElementID = inputPin.getID();
            draht.sourcePin = 0;

            draht.setSelected(true);
            inputPin.setSelected(true);
            yx1 += 50;
        } else if (info.pin != null && info.pin.pinIO == JPin.PIN_OUTPUT) {
            System.out.println("X OUTPUT");

            BackupDrahtOutut tmp = new BackupDrahtOutut();
            tmp.destElementID = draht.destElementID;
            tmp.destPin = draht.destPin;

            tmp.pinIndex = OPindex++;
            backupDrahtsOutputs.add(tmp);

            dstElement.getPin(dstPin).draht = null;

            String[] args = new String[1];
            args[0] = "" + info.pin.dataType;

            Element outputPin = Tools.addOutputPin(vm, args);

            outputPin.getPin(0).draht = draht;
            draht.destElementID = outputPin.getID();
            draht.destPin = 0;

            if (draht.getPolySize() < 4) {
                while (draht.getPolySize() > 0) {
                    draht.deletePoint(0);
                }

                int dx = (srcElement.getLocation().x + srcPinX.getLocation().x + dstElement.getLocation().x + dstPinX.getLocation().x) / 2;
                for (int k = 0; k < 4; k++) {
                    draht.addPoint(dx, 100);
                }
            }

            draht.setSelected(true);
            outputPin.setSelected(true);

            outputPin.setLocation(500, yx2);
            yx2 += 50;

        }

    }
    int yx1 = 20;
    int yx2 = 20;
    int IPindex = 0;
    int OPindex = 0;
    ArrayList<BackupDrahtInput> backupDrahtsInputs;
    ArrayList<BackupDrahtOutut> backupDrahtsOutputs;

    public void createSubElementFromVM(VMObject vm, Rectangle rect, String vmFilename) {
        timer.stop();

        try {
            System.out.println("binFertig!");

            int x1 = rect.x;
            int y1 = rect.y;
            int x2 = rect.width;
            int y2 = rect.height;

            ArrayList<Element> elements = vm.getSelectedElements();

            Basis basis = createNewVM();

            addBasisToVMPanel(basis);

            Element element;
            JPin pin;
            int i, j;
            LineInfo info;
            Draht draht;

            yx1 = 20;
            yx2 = 20;
            IPindex = 0;
            OPindex = 0;

            backupDrahtsInputs = new ArrayList<BackupDrahtInput>();
            backupDrahtsOutputs = new ArrayList<BackupDrahtOutut>();

            for (i = 0; i < 4; i++) {
                ArrayList<LineInfo> lineInfos = selectAllDrahtsWhereIntersectWithRectaqngle(elements, x1, y1, x2, y2, i);

                for (j = 0; j < lineInfos.size(); j++) {
                    info = lineInfos.get(j);

                    processDraht(info, vm);
                }
            }

            vm.owner.cut();
            basis.paste();

            basis.projectPath = vm.owner.projectPath;

            if (basis.projectPath.length() == 0) {
                basis.save();
            } else {
                String fileName = "";
                fileName = basis.projectPath + "/" + vmFilename + ".vlogic";
                basis.saveToFile(fileName, false);
                basis.vmFilename = fileName;
                basis.fileName = fileName;

                reloadProjectPanel();
            }

            Element subVM = null;
            try {
                String mainPath = basis.fileName;

                String frontClass = "VMPanel";

                if (basis.getFrontBasis().getSelectedElements().size() > 0) {
                    frontClass = "VMPanel";
                } else {
                    frontClass = "";
                }

                File f = new File(mainPath);

                //String path=fconf.getPath();
                String vmName = f.getName();

                String[] args = new String[3];
                args[0] = vmName; // vmName zb: "Untiled.vlogic""
                args[1] = vmName; // caption!
                args[2] = "";

                subVM = Tools.addSubVM(vm, frontClass, args);

                int dx = (x1 + x2) / 2;
                int dy = (y1 + y2) / 2;
                subVM.setLocation(dx, dy);

            } catch (Exception ex) {
                Tools.showMessage(this, "" + ex.toString());
            }

            // verbinde die subVM mit den Drähten!
            if (subVM != null) {
                // INPUTS
                int outpintCount = subVM.getRightPins();
                BackupDrahtInput tmp;
                for (i = 0; i < backupDrahtsInputs.size(); i++) {
                    tmp = backupDrahtsInputs.get(i);

                    Element srcElement = vm.getElementWithID(tmp.sourceElementID);
                    int srcPin = tmp.sourcePin;

                    draht = vm.addDrahtIntoCanvas(tmp.sourceElementID, tmp.sourcePin, subVM.getID(), outpintCount + tmp.pinIndex);

                    Point p = srcElement.getPin(srcPin).getLocation();
                    Point px = subVM.getPin(outpintCount + tmp.pinIndex).getLocation();

                    while (draht.getPolySize() > 0) {
                        draht.deletePoint(0);
                    }

                    draht.addPoint(srcElement.getLocation().x + p.x, srcElement.getLocation().y + p.y);

                    int dx = (srcElement.getLocation().x + subVM.getLocation().x) / 2;
                    int dy = 300;// wird eh automatisch gesetzt von reorderWireFrames();
                    draht.addPoint(dx, 10);
                    draht.addPoint(dx, 10);
                    draht.addPoint(10, 10);

                    srcElement.getPin(srcPin).draht = draht;
                    subVM.getPin(outpintCount + tmp.pinIndex).draht = draht;

                    vm.reorderWireFrames();
                }

                // OUTPUTS
                BackupDrahtOutut tmp2;
                for (i = 0; i < backupDrahtsOutputs.size(); i++) {
                    tmp2 = backupDrahtsOutputs.get(i);

                    Element dstElement = vm.getElementWithID(tmp2.destElementID);
                    int dstPin = tmp2.destPin;

                    draht = vm.addDrahtIntoCanvas(subVM.getID(), tmp2.pinIndex, tmp2.destElementID, tmp2.destPin);

                    Point p = dstElement.getPin(dstPin).getLocation();
                    //Point px=subVM.getPin(tmp.pinIndex).getLocation();

                    int dx = (dstElement.getLocation().x + subVM.getLocation().x) / 2;
                    int dy = 300;// wird eh automatisch gesetzt von reorderWireFrames();

                    while (draht.getPolySize() > 0) {
                        draht.deletePoint(0);
                    }

                    draht.addPoint(dstElement.getLocation().x + p.x, dstElement.getLocation().y + p.y);
                    draht.addPoint(dx, 10);
                    draht.addPoint(dx, 10);
                    draht.addPoint(10, 10);

                    dstElement.getPin(dstPin).draht = draht;
                    subVM.getPin(tmp2.pinIndex).draht = draht;
                    vm.reorderWireFrames();
                }

            }

            packBasis(basis);
        } catch (Exception ex) {
            timer.start();
            Tools.showMessage(this, "" + ex.toString());
        }
        timer.start();

    }

    public void binFertig(VMObject vm, Rectangle rect) {
        vm.setModusIdle();

        DialogVMName frm = new DialogVMName(this, true);

        frm.setVisible(true);

        if (frm.result && frm.newName.length() > 0) {
            String vmfilename = frm.newName;

            createSubElementFromVM(vm, rect, vmfilename);

        }

    }

    private void jmniShowErrorsAndWarningsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniShowErrorsAndWarningsActionPerformed
    {//GEN-HEADEREND:event_jmniShowErrorsAndWarningsActionPerformed
        if (errorWarnings != null) {
            errorWarnings.setVisible(true);
        }
    }//GEN-LAST:event_jmniShowErrorsAndWarningsActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton12ActionPerformed
    {//GEN-HEADEREND:event_jButton12ActionPerformed
        if (getAktuelleBasis() != null) {
            getAktuelleBasis().console.setVisible(true);
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton11ActionPerformed
    {//GEN-HEADEREND:event_jButton11ActionPerformed
        showInfo(this);
    }//GEN-LAST:event_jButton11ActionPerformed

    public static void copyFile(String src, String dst) throws IOException {
        Tools.copyHighSpeed(new File(src), new File(dst));
    }

    public static void copyFiles(String strPath, String dstPath, boolean javaFilter) throws IOException {
        File src = new File(strPath);
        File dest = new File(dstPath);

        if (src.isDirectory()) {
            dest.mkdirs();
            String list[] = src.list();

            for (int i = 0; i < list.length; i++) {
                if (javaFilter) {
                    if (list[i].endsWith("java") == false && list[i].endsWith("bat") == false) {
                        String dest1 = dest.getAbsolutePath() + "\\" + list[i];
                        String src1 = src.getAbsolutePath() + "\\" + list[i];
                        copyFiles(src1, dest1, javaFilter);
                    }
                } else {
                    String dest1 = dest.getAbsolutePath() + "\\" + list[i];
                    String src1 = src.getAbsolutePath() + "\\" + list[i];
                    copyFiles(src1, dest1, javaFilter);
                }
            }
        } else {
            Tools.copyHighSpeed(src, dest);
        }
    }

    private boolean isVM(String path) {
        String p1 = new File(path).getAbsolutePath();
        String p2 = new File(elementPath + "/FrontElements/Version_2_0/VMElement").getAbsolutePath();
        String p3 = new File(elementPath + "/FrontElements/Version_2_0/VMElementUniversal").getAbsolutePath();

        if (p1.equalsIgnoreCase(p2) || p1.equalsIgnoreCase(p3)) {
            return true;
        }

        return false;
    }

    public void openEditor(String path) {
        if (isVM(elementPath + path)) {
            // lade VM
        } else {
            String javaeditor = settings.getJavaEditor();

            String str = Tools.mapFile(path);
            File file = new File(str);
            DFProperties definition_def = Tools.getProertiesFromDefinitionFile(file);

            if (javaeditor == null || javaeditor.equalsIgnoreCase("") || new File(javaeditor).exists() == false) {
                codeeditor.frmCodeEditor codeForm = new codeeditor.frmCodeEditor(this);
                codeForm.execute(elementPath, path, definition_def.captionInternationalized);
            } else {
                String srcPath = elementPath + path + "/src/";

                String strFileA = "";
                String strFileB = "";
                if (definition_def.classcircuit.length() > 0) {
                    strFileA = "\"" + srcPath + definition_def.classcircuit + ".java\"";
                }

                if (definition_def.classfront.length() > 0) {
                    strFileB = "\"" + srcPath + definition_def.classfront + ".java\"";
                }

                try {
                    Runtime.getRuntime().exec(javaeditor + " " + strFileA + " " + strFileB);
                } catch (IOException ex) {
                    Tools.showMessage(java.util.ResourceBundle.getBundle("BasisStatus/StatusIdle").getString("Javaeditor_not_found!"));
                }
            }

        }
    }

    public void openElement(String path) {
        DFProperties definition_def = Tools.getProertiesFromDefinitionFile(new File(path));
        if (definition_def.vm.length() == 0) {
            // öffne Code editor

            String f1 = new File(Tools.userElementPath).getAbsolutePath();
            String f2 = new File(path).getAbsolutePath();

            String str = f2.substring(f1.length());
            openEditor(Tools.userElementPath + str);
        } else {
            addBasisToVMPanel(path + "/" + definition_def.vm, "", true);
        }
    }

    public void openJavaEditor(Element element) {
        //openEditor(Tools.userElementPath, strGrp+frm.compName);
        String strX = elementPath + element.mainPath;

        String str = Tools.mapFile(strX);

        openEditor(str);
    }

    private void jmniDeletePasswordProtectionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniDeletePasswordProtectionActionPerformed
    {//GEN-HEADEREND:event_jmniDeletePasswordProtectionActionPerformed

        Basis basis = getAktuelleBasis();
        if (basis != null) {
            if (basis.vmProtected) {
                return;
            }

            if (Tools.setQuestionDialog(this, java.util.ResourceBundle.getBundle("VisualLogic/Messages").getString("Delete password protection"))) {
                basis.vmPassword = "";
                basis.vmProtected = false;
                basis.setChanged(true);
                Tools.showMessage(this, java.util.ResourceBundle.getBundle("VisualLogic/Messages").getString("Password removed"));
            }
        }

    }//GEN-LAST:event_jmniDeletePasswordProtectionActionPerformed

    private void jmniPasswordProtectionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniPasswordProtectionActionPerformed
    {//GEN-HEADEREND:event_jmniPasswordProtectionActionPerformed
        Basis basis = getAktuelleBasis();
        if (basis != null) {
            if (basis.vmProtected) {
                return;
            }

            DialogPassword frm = new DialogPassword(this, true);
            frm.setVisible(true);
            if (frm.password.length() > 0) {
                basis.setPassword(frm.password);
                basis.setChanged(true);
            }
        }
    }//GEN-LAST:event_jmniPasswordProtectionActionPerformed

    public String generateUntiledFileName(String path) {
        String fn;
        for (int i = 0; i < 99999; i++) {
            fn = path + "Untiled" + i + ".vlogic";

            if (!(new File(fn).exists())) {
                return fn;
            }
        }
        return null;
    }

    private void createNewSingleVM() {
        Basis basis = createNewVM();

        addBasisToVMPanel(basis);
    }

    public Basis createNewVM() {
        Basis basis = new Basis(this, elementPath);

        //basis.fileName="Untilted";
        String path = getUserURL().getFile() + System.getProperty("file.separator");
        basis.fileName = generateUntiledFileName(path);
        if (basis.fileName != null) {

            basis.saveToFile(basis.fileName, false);

            //desktop.add(basis);
            basis.loading = true;

            basis.isFileLoaded = false;
            basis.loading = false;
            return basis;
        } else {
            return null;
        }
    }

    private void saveAs() {
        Basis basis = getAktuelleBasis();
        if (basis != null) {
            basis.saveAs();
        }
    }

    public int getTabIndex(VMEditorPanel panel) {
        for (int i = 0; i < paneRight.getComponentCount(); i++) {
            Component comp = paneRight.getComponentAt(i);

            if (comp.equals(panel)) {
                return i;
            }
        }
        return -1;
    }
    public String globalPath = "";
    public String globalProjectPath = "";

    private void jmnuOpenSingleVMActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmnuOpenSingleVMActionPerformed
    {//GEN-HEADEREND:event_jmnuOpenSingleVMActionPerformed

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(settings.getOldProjectPath()));
        chooser.setDialogTitle(java.util.ResourceBundle.getBundle("VisualLogic/FrameCircuit").getString("VM_Oeffnen"));

        chooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {

            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                return f.getName().toLowerCase().endsWith(".vlogic");
            }

            public String getDescription() {
                return "vlogic Dateien (vlogic)";
            }
        });

        //chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //OnlyDirectoryFilter filter= new OnlyDirectoryFilter();
        //chooser.setFileFilter(filter);
        //FileView view = new JavaFileView();
        //chooser.setFileView(view);
        //chooser.setAccessory(new LabelAccessory(chooser));
        int value = chooser.showOpenDialog(this);
        if (value == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            settings.setCurrentDirectory(chooser.getCurrentDirectory().getPath());

            globalPath = file.getAbsolutePath();

            SwingWorker worker = new SwingWorker<Object, Object>() {

                DialogWait frm;

                public Object doInBackground() {
                    Tools.dialogWait = new DialogWait();
                    Tools.dialogWait.setVisible(true);
                    Tools.dialogWait.setLocation(Tools.dialogWait.getLocation().x, Tools.dialogWait.getLocation().y - 150);

                    addBasisToVMPanel(globalPath, "", true);
                    return null;
                }

                protected void done() {
                    Tools.dialogWait.dispose();
                }
            };

            worker.execute();

        }

    }//GEN-LAST:event_jmnuOpenSingleVMActionPerformed

    private void jmiShowDigitalWindowActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmiShowDigitalWindowActionPerformed
    {//GEN-HEADEREND:event_jmiShowDigitalWindowActionPerformed
        jButton8ActionPerformed(evt);
    }//GEN-LAST:event_jmiShowDigitalWindowActionPerformed

    private void jmiShowAnalogWindowActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmiShowAnalogWindowActionPerformed
    {//GEN-HEADEREND:event_jmiShowAnalogWindowActionPerformed
        jButton9ActionPerformed(evt);
    }//GEN-LAST:event_jmiShowAnalogWindowActionPerformed

    private void jmniCloseAllVmsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniCloseAllVmsActionPerformed
    {//GEN-HEADEREND:event_jmniCloseAllVmsActionPerformed
        closeAllVms();
    }//GEN-LAST:event_jmniCloseAllVmsActionPerformed

    private void jPanel1AncestorResized(java.awt.event.HierarchyEvent evt)//GEN-FIRST:event_jPanel1AncestorResized
    {//GEN-HEADEREND:event_jPanel1AncestorResized

    }//GEN-LAST:event_jPanel1AncestorResized

    public boolean closeAllVms() {
        while (paneRight.getTabCount() > 0) {
            Component comp = paneRight.getComponentAt(0);

            if (comp instanceof VMEditorPanel) {
                VMEditorPanel panel = (VMEditorPanel) comp;

                if (closeVM(panel) == false) {
                    return false;
                }

            }
        }

        return true;
    }

    private void jmniCloseAllVMsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniCloseAllVMsActionPerformed
    {//GEN-HEADEREND:event_jmniCloseAllVMsActionPerformed
        closeAllVms();
    }//GEN-LAST:event_jmniCloseAllVMsActionPerformed

    private void paneRightStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_paneRightStateChanged
    {//GEN-HEADEREND:event_paneRightStateChanged

        Basis basis = getAktuelleBasis();
        if (basis == null) {
            return;
        }
        vmEditorPanelTabChanged(getVMObject());
        /*Basis basis = getAktuelleBasis();
    if (basis==null) return;
    if (vmobject==basis.getCircuitBasis())
    {
    if (elementPalette.vmObject==basis.getFrontBasis())
    {
    oldPanelDirectory=elementPalette.aktuellesVerzeichniss;
    }
    elementPalette.aktuellesVerzeichniss=oldCircuitDirectory;
    elementPalette.vmObject=vmobject;
    elementPalette.loadFolder(elementPalette.aktuellesVerzeichniss);
    }
    if (vmobject==basis.getFrontBasis() )
    {
    if (elementPalette.vmObject==basis.getCircuitBasis())
    {
    oldCircuitDirectory=elementPalette.aktuellesVerzeichniss;
    }
    elementPalette.aktuellesVerzeichniss=oldPanelDirectory;
    elementPalette.vmObject=vmobject;
    elementPalette.loadFolder(elementPalette.aktuellesVerzeichniss);
    }*/

    }//GEN-LAST:event_paneRightStateChanged

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton9ActionPerformed
    {//GEN-HEADEREND:event_jButton9ActionPerformed

        if (getAktuelleBasis() != null) {
            getAktuelleBasis().frameDoubleGraph.setVisible(true);
        }

    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton8ActionPerformed
    {//GEN-HEADEREND:event_jButton8ActionPerformed
        if (getAktuelleBasis() != null) {
            getAktuelleBasis().frameBooleanGraph.setVisible(true);
        }

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton7ActionPerformed
    {//GEN-HEADEREND:event_jButton7ActionPerformed

        if (getAktuelleBasis() != null) {
            getAktuelleBasis().dialogTestpoint.setVisible(true);
        }

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton10ActionPerformed
    {//GEN-HEADEREND:event_jButton10ActionPerformed
        jmiNewProjectActionPerformed(evt);
    }//GEN-LAST:event_jButton10ActionPerformed

    public void choiceLanguage(JFrame frame) {
        DialogLanguage lan = new DialogLanguage(frame, true);

        lan.execute(settings.getLanguage());

        settings.setLanguage(lan.result);
    }

    private void jmniChooseLanguageActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniChooseLanguageActionPerformed
    {//GEN-HEADEREND:event_jmniChooseLanguageActionPerformed

        String lan = settings.getLanguage();
        String old = lan;

        choiceLanguage(this);
        lan = settings.getLanguage();
        if (!old.equalsIgnoreCase(lan)) {
            Tools.showMessage(java.util.ResourceBundle.getBundle("VisualLogic/Messages").getString("Please restart the Application!"));
        }

    }//GEN-LAST:event_jmniChooseLanguageActionPerformed

    private void jmiShowTestpointWindowActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmiShowTestpointWindowActionPerformed
    {//GEN-HEADEREND:event_jmiShowTestpointWindowActionPerformed
        jButton7ActionPerformed(evt);
    }//GEN-LAST:event_jmiShowTestpointWindowActionPerformed

    private void jSpinner1PropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_jSpinner1PropertyChange
    {//GEN-HEADEREND:event_jSpinner1PropertyChange

    }//GEN-LAST:event_jSpinner1PropertyChange

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_jSpinner1StateChanged
    {//GEN-HEADEREND:event_jSpinner1StateChanged
        int value = (((Integer) jSpinner1.getValue()).intValue());

        Basis basis = getAktuelleBasis();
        if (basis != null) {
            basis.setDebugDelay(value);
        }
    }//GEN-LAST:event_jSpinner1StateChanged

    private void jmiCopyToClipActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmiCopyToClipActionPerformed
    {//GEN-HEADEREND:event_jmiCopyToClipActionPerformed
        // txtMessages.selectAll();
        //  txtMessages.copy();
    }//GEN-LAST:event_jmiCopyToClipActionPerformed

    private void jmiVariableWatcherActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmiVariableWatcherActionPerformed
    {//GEN-HEADEREND:event_jmiVariableWatcherActionPerformed
        Basis basis = getAktuelleBasis();
        if (basis != null) {
            watcher = new DialogVariableWatcher(this, false);
            watcher.execute(basis.variablenListe);
            watcher.setVisible(true);
        }
    }//GEN-LAST:event_jmiVariableWatcherActionPerformed

    public void reloadBasis() {
        Basis basis = getAktuelleBasis();
        if (basis != null) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            basis.getCircuitBasis().selectAny(true);

            basis.cut();
            basis.paste();

            basis.getCircuitBasis().selectAny(false);
            // basis.getCircuitBasis().updateUI();
            //basis.getFrontBasis().updateUI();
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void jButtonRefreshVMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefreshVMActionPerformed

        timer.stop();
        propertyEditor.locked = true;
        reloadBasis();
        timer.start();
        propertyEditor.locked = false;


        /*SwingWorker worker = new SwingWorker<Object, Object>()
    {
    //DialogWait frm;
    public Object doInBackground()
    {
    Tools.dialogWait=new DialogWait();
    Tools.dialogWait.setVisible(true);
     *Tools.dialogWait.setLocation(Tools.dialogWait.getLocation().x, Tools.dialogWait.getLocation().y-150);
    timer.stop();
    propertyEditor.locked=true;
    reloadBasis();
    return null;
    }
    protected void done()
    {
    timer.start();
    propertyEditor.locked=false;
    Tools.dialogWait.dispose();
    }
    };
    worker.execute();*/

    }//GEN-LAST:event_jButtonRefreshVMActionPerformed

    private void jmiForumActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmiForumActionPerformed
    {//GEN-HEADEREND:event_jmiForumActionPerformed

        Tools.openUrl(this, "http://myopenlab.informe.com");

    }//GEN-LAST:event_jmiForumActionPerformed

    private void jmiTutorialsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmiTutorialsActionPerformed
    {//GEN-HEADEREND:event_jmiTutorialsActionPerformed

        Tools.openUrl(this, "http://www.myopenlab.de/downloads.html");

    }//GEN-LAST:event_jmiTutorialsActionPerformed

    private void jmniDefineVariablesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniDefineVariablesActionPerformed
    {//GEN-HEADEREND:event_jmniDefineVariablesActionPerformed
        jButton5ActionPerformed(evt);
    }//GEN-LAST:event_jmniDefineVariablesActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton5ActionPerformed
    {//GEN-HEADEREND:event_jButton5ActionPerformed

        Basis basis = getAktuelleBasis();
        if (basis != null) {
            if (basis.vmProtected) {
                return;
            }
            DialogVariables dialogVariables = new DialogVariables(this, true, basis);
            dialogVariables.setVisible(true);
            basis.generateAllVariabled();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    public ArrayList<String> listAllVMsFromProject(String projectPath) {
        ArrayList<String> result = new ArrayList<String>();

        File f = new File(projectPath);

        String[] files = f.list();

        for (int i = 0; i < files.length; i++) {
            if (files[i].endsWith("vlogic")) {
                result.add(files[i]);
            }
        }
        return result;
    }

    public void generateExecutableVM(String projectPath, String destPath, String vmName) {
        Basis basis = new Basis(this, this.elementPath);
        basis.projectPath = projectPath;

        String filename = projectPath + File.separator + vmName;

        basis.loadFromFile(filename, false);

        String ext = Tools.getExtension(new File(vmName));
        String onlyName = vmName.substring(0, vmName.length() - ext.length() - 1);

        String destFilename = destPath + File.separator + onlyName + ".vlogic";

        basis.saveAsExecutable(destFilename);
    }

    public void createDistribution(String projectPath, String destPath, String mainVM) {
        File destDir = new File(destPath);

        if (!destDir.exists()) {
            destDir.mkdir();
        }
        try {

            Tools.copy(new File(elementPath + File.separator + ".." + File.separator + "DistributionStarter.jar"), new File(destDir + File.separator + "DistributionStarter.jar"));

            Tools.copy(new File(elementPath + File.separator + ".." + File.separator + "rxtxSerial.dll"), new File(destDir + File.separator + "rxtxSerial.dll"));
            Tools.copy(new File(elementPath + File.separator + ".." + File.separator + "rxtxParallel.dll"), new File(destDir + File.separator + "rxtxParallel.dll"));

            Tools.saveText(new File(destDir + File.separator + "start.bat"), "start javaw -jar DistributionStarter.jar .");
            Tools.saveText(new File(destDir + File.separator + "start_linux_distribution"), "#!/bin/sh \njava -jar DistributionStarter.jar .");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ArrayList<String> files = listAllVMsFromProject(projectPath);

        for (int i = 0; i < files.size(); i++) {
            String file = files.get(i);

            generateExecutableVM(projectPath, destPath, file);
        }

        File execFile = new File(destDir.getPath() + "/myopenlab.executable");
        try {
            execFile.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ProjectProperties props = new ProjectProperties();
        props.mainVM = mainVM;
        Tools.saveProjectFile(destDir, props);

    }

    private void jmiSaveAsJPGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiSaveAsJPGActionPerformed

        Basis basis = getAktuelleBasis();

        if (basis != null) {
            basis.ownerVMPanel.saveJpg();
        }

    }//GEN-LAST:event_jmiSaveAsJPGActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
    {//GEN-HEADEREND:event_jButton2ActionPerformed
        jmniOptionsActionPerformed(evt);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jmiHomepageActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmiHomepageActionPerformed
    {//GEN-HEADEREND:event_jmiHomepageActionPerformed

        Tools.openUrl(this, "http://myopenlab.de");

    }//GEN-LAST:event_jmiHomepageActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_jComboBox1ItemStateChanged
    {//GEN-HEADEREND:event_jComboBox1ItemStateChanged

    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox1PropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_jComboBox1PropertyChange
    {//GEN-HEADEREND:event_jComboBox1PropertyChange

    }//GEN-LAST:event_jComboBox1PropertyChange

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jComboBox1ActionPerformed
    {//GEN-HEADEREND:event_jComboBox1ActionPerformed

        Basis basis = getAktuelleBasis();

        if (basis != null) {
            if (basis.vmProtected) {
                return;
            }

            if (comboIsEditing == false && evt.getSource() instanceof JComboBox) {
                JComboBox cb = (JComboBox) evt.getSource();
                int index = cb.getSelectedIndex();
                if (index >= 0 && index <= getVMObject().getElementCount()) {
                    Element el = getVMObject().getElement(index);
                    getVMObject().disableAllElements();

                    if (el != null) {
                        el.setSelected(true);
                        el.processPropertyEditor();
                    }
                }
            }
        }

    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jmiLegendActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmiLegendActionPerformed
    {//GEN-HEADEREND:event_jmiLegendActionPerformed
        jButton6ActionPerformed(evt);
    }//GEN-LAST:event_jmiLegendActionPerformed

    private void refreshAllBasis() {
        for (int i = 0; i < paneRight.getTabCount(); i++) {
            Component comp = paneRight.getComponentAt(i);

            if (comp instanceof VMEditorPanel) {
                VMEditorPanel panel = (VMEditorPanel) comp;

                panel.basis.getCircuitBasis().setRasterOn(settings.isCircuitRasterOn());
                panel.basis.getCircuitBasis().setAlignToGrid(settings.isCircuittAlignToGrid());
                panel.basis.getCircuitBasis().setRaster(settings.getCircuitRasterX(), settings.getCircuitRasterY());

                panel.basis.getFrontBasis().setRasterOn(settings.isFrontRasterOn());
                panel.basis.getFrontBasis().setAlignToGrid(settings.isFrontAlignToGrid());
                panel.basis.getFrontBasis().setRaster(settings.getFrontRasterX(), settings.getFrontRasterY());
                panel.updateUI();
            }
        }
    }

    private void jmniOptionsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniOptionsActionPerformed
    {//GEN-HEADEREND:event_jmniOptionsActionPerformed
        DialogOptions frm = new DialogOptions(settings, this, true);

        frm.execute(settings);
        if (frm.result) {
            refreshAllBasis();
        }
    }//GEN-LAST:event_jmniOptionsActionPerformed

    public void showInfo(JFrame frame) {
        FrameInfo info = new FrameInfo(frame, true, elementPath);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int mitteX = (int) screenSize.getWidth() / 2;
        int mitteY = (int) screenSize.getHeight() / 2;

        info.setLocation(mitteX - info.getWidth() / 2, mitteY - info.getHeight() / 2);
        info.setVisible(true);
    }

    public void showElementWindow() {

    }

    private void jPanelElementPaletteComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanelElementPaletteComponentResized


    }//GEN-LAST:event_jPanelElementPaletteComponentResized

    private void jPanelElementPaletteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelElementPaletteMousePressed
// TODO add your handling code here:
    }//GEN-LAST:event_jPanelElementPaletteMousePressed

    private void jPanelElementPaletteMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelElementPaletteMouseMoved
// TODO add your handling code here:
    }//GEN-LAST:event_jPanelElementPaletteMouseMoved

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
// TODO add your handling code here:
    }//GEN-LAST:event_formComponentShown

    private void jmiClearWindowActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmiClearWindowActionPerformed
    {//GEN-HEADEREND:event_jmiClearWindowActionPerformed


    }//GEN-LAST:event_jmiClearWindowActionPerformed

    private void jSplitPane1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jSplitPane1PropertyChange

    }//GEN-LAST:event_jSplitPane1PropertyChange

    private void cmdRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRedoActionPerformed
        if (getAktuelleBasis() != null) {
            getAktuelleBasis().redo();
        }

    }//GEN-LAST:event_cmdRedoActionPerformed

    private void cmdUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdUndoActionPerformed

        if (getAktuelleBasis() != null) {
            getAktuelleBasis().undo();
        }

    }//GEN-LAST:event_cmdUndoActionPerformed

    private void formWindowStateChanged(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowStateChanged

        if (getExtendedState() == this.MAXIMIZED_BOTH) {
            System.out.println("MAX");
        }

        if (getExtendedState() == this.NORMAL) {
            System.out.println("NORMAL");

            java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            //setLocation((screenSize.width-getWidth())/2, (screenSize.height-getHeight())/2);

            Dimension size = getSize();

            int x1 = (int) size.getWidth();
            int x2 = screenSize.width;

            if (Math.abs(x1 - x2) < 20) {
                setLocation(50, 50);
                setSize(screenSize.width - 100, screenSize.height - 100);
            }
        }
        //if (getState()==MA)
        {
            //setSize(500,500);
            //System.out.println("");
        }
    }//GEN-LAST:event_formWindowStateChanged

    private void jmiRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiRedoActionPerformed
        if (getAktuelleBasis() != null) {
            getAktuelleBasis().redo();
        }

    }//GEN-LAST:event_jmiRedoActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        frmDTLengend frm = new frmDTLengend(this.getIconImage());
        frm.setVisible(true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void formComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentMoved
        //  basis.circuitPanelLeft=getLocation().x;
        //   basis.circuitPanelTop=getLocation().y;
    }//GEN-LAST:event_formComponentMoved

    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdSaveActionPerformed
    {//GEN-HEADEREND:event_cmdSaveActionPerformed
        jmiSaveActionPerformed(evt);
    }//GEN-LAST:event_cmdSaveActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        jmiOpenProjectActionPerformed(evt);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButtonDebugActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDebugActionPerformed

        Basis basis = getAktuelleBasis();
        if (basis != null) {
            ProjectProperties props = Tools.openProjectFile(new File(basis.projectPath));
            if (!props.projectType.equalsIgnoreCase("SPS")) {
                basis.start(true);
            }
        }
    }//GEN-LAST:event_jButtonDebugActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated

    }//GEN-LAST:event_formWindowActivated

    private void jmiEigenschatenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiEigenschatenActionPerformed

        Basis basis = getAktuelleBasis();
        if (basis != null) {
            if (basis.vmProtected) {
                return;
            }

            VMProperties.intVMWidth = basis.getCircuitBasis().getWidth();
            VMProperties.intVMHeight = basis.getCircuitBasis().getHeight();
            VMProperties.intElementWidth = basis.elementWidth;
            VMProperties.intElementHeight = basis.elementHeight;
            VMProperties.strName = basis.caption;
            VMProperties.strAutorName = basis.autorName;
            VMProperties.strAutorEmail = basis.autorMail;
            VMProperties.strAutorWWW = basis.autorWWW;
            String str = java.util.ResourceBundle.getBundle("VisualLogic/FrameCircuit").getString("VM_Properties");
            if (VMProperties.showDialog(this, str)) {
                basis.getCircuitBasis().setSize(VMProperties.intVMWidth, VMProperties.intVMHeight);
                basis.elementWidth = VMProperties.intElementWidth;
                basis.elementHeight = VMProperties.intElementHeight;
                basis.caption = VMProperties.strName;

                basis.autorName = VMProperties.strAutorName;
                basis.autorMail = VMProperties.strAutorEmail;
                basis.autorWWW = VMProperties.strAutorWWW;
                basis.setChanged(true);
            }
        }

    }//GEN-LAST:event_jmiEigenschatenActionPerformed

    private void jmnuVMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnuVMActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_jmnuVMActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized

        if (timer != null) {
            timer.stop();
        }
        //jSplitPane4.setDividerLocation(getWidth()-settings.rightSplitterPos);
        if (timer != null) {
            timer.start();
        }

    }//GEN-LAST:event_formComponentResized

    private void jmiCloseWindowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiCloseWindowActionPerformed

        closeApplication();
    }//GEN-LAST:event_jmiCloseWindowActionPerformed

    private void jmiStepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiStepActionPerformed
        Basis basis = getAktuelleBasis();
        if (basis != null) {
            basis.step();
        }
    }//GEN-LAST:event_jmiStepActionPerformed

    private void jmiResumeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiResumeActionPerformed
        Basis basis = getAktuelleBasis();
        if (basis != null) {
            basis.resume();
        }
    }//GEN-LAST:event_jmiResumeActionPerformed

    private void jmiPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiPauseActionPerformed

        Basis basis = getAktuelleBasis();
        if (basis != null) {
            basis.pause();
        }

    }//GEN-LAST:event_jmiPauseActionPerformed

    private void jmiStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiStopActionPerformed
        Basis basis = getAktuelleBasis();
        if (basis != null) {
            basis.stop();
        }

    }//GEN-LAST:event_jmiStopActionPerformed

    private void jmiStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiStartActionPerformed
        Basis basis = getAktuelleBasis();
        if (basis != null) {
            basis.start(false);
        }

    }//GEN-LAST:event_jmiStartActionPerformed

    private void jmiSelectAnyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiSelectAnyActionPerformed

        if (getVMObject() != null) {
            getVMObject().selectAny(true);
        }
    }//GEN-LAST:event_jmiSelectAnyActionPerformed

    private void jmiPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiPasteActionPerformed
        if (getVMObject() != null) {
            getVMObject().owner.paste();
        }
    }//GEN-LAST:event_jmiPasteActionPerformed

    private void jmiCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiCopyActionPerformed
        if (getVMObject() != null) {
            getVMObject().owner.copy();
        }
    }//GEN-LAST:event_jmiCopyActionPerformed

    private void jmiCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiCutActionPerformed
        if (getVMObject() != null) {
            getVMObject().owner.cut();
        }
    }//GEN-LAST:event_jmiCutActionPerformed

    private void jmiUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiUndoActionPerformed
        Basis basis = getAktuelleBasis();
        if (basis != null) {
            basis.undo();
        }
    }//GEN-LAST:event_jmiUndoActionPerformed

    private void jmiSaveAsModulActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiSaveAsModulActionPerformed

        Basis basis = getAktuelleBasis();
        if (basis != null) {
            if (basis.vmProtected) {
                return;
            }

            DialogSaveAsModul frmDialogSave = new DialogSaveAsModul(this, this, true);
            frmDialogSave.executeNew();
            reloadElementPalettes();
        }

        //elementPalette.loadFolder(elementPalette.aktuellesVerzeichniss);
    }//GEN-LAST:event_jmiSaveAsModulActionPerformed

    private void jmnuDateiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnuDateiActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_jmnuDateiActionPerformed

    private void jmiSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiSaveActionPerformed
        // if (isEnabled)
        {
            Basis basis = getAktuelleBasis();
            if (basis != null) {
                basis.save();
            }

        }
    }//GEN-LAST:event_jmiSaveActionPerformed

    private void jmiInfoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmiInfoActionPerformed
    {//GEN-HEADEREND:event_jmiInfoActionPerformed
        showInfo(this);
    }//GEN-LAST:event_jmiInfoActionPerformed

    public void executeMainVM(String projectPath, String mainVM) {
        globalPath = projectPath + "/" + mainVM;
        globalProjectPath = projectPath;
        SwingWorker worker = new SwingWorker<Object, Object>() {

            DialogWait frm;

            public Object doInBackground() {
                Tools.dialogWait = new DialogWait();
                Tools.dialogWait.setVisible(true);
                Tools.dialogWait.setLocation(Tools.dialogWait.getLocation().x, Tools.dialogWait.getLocation().y - 150);

                frontMode = true;
                Basis basis = addBasisToVMPanel(globalPath, globalProjectPath, false);

                //basis.startInFrontMode=true;
                //basis.panelMode=true;
                basis.getFrontBasis().sortSubPanels();

                basis.getCircuitBasis().adjustAllElemnts();
                basis.getFrontBasis().adjustAllElemnts();

                basis.getCircuitBasis().checkPins();

                basis.getCircuitBasis().ProcessPinDataType();

                basis.start(false);
                return null;
            }

            protected void done() {
                Tools.dialogWait.dispose();
            }
        };

        worker.execute();


        /*Basis basis = new Basis(this, elementPath);
    basis.panelMode=true;
    addBasisToVMPanel(filename);
    /*basis.loadFromFile(filename,false);
    basis.vmFilename=filename;
    basis.fileName=filename;
    addBasisToVMPanel(basis);
    basis.frameCircuit=this;*/
        //basis.startInFrontMode=true;
        //basis.loading=false;
    }

    public void openProject(File project) {
        String[] children = project.list();

        if (children != null && Tools.isExecutableProject(children)) {
            ProjectProperties props = Tools.openProjectFile(project);

            if (props != null) {
                int pos = Collections.binarySearch(projects, project.getPath());
                if (pos < 0) {
                    projects.add(project.getPath());
                    reloadProjectPanel();
                }
                executeMainVM(project.getPath(), props.mainVM);
            }
        } else if (children != null && Tools.isProject(children)) {

            String tmp = project.getPath();

            boolean ok = false;
            for (String item : projects) {

                if (new File(item).equals(new File(tmp))) {
                    ok = true;
                    break;
                }
            }

            if (!ok) {
                projects.add(project.getPath());
                reloadProjectPanel();
            } else {
                //Tools.showMessage(this, java.util.ResourceBundle.getBundle("VisualLogic/Messages").getString("VM already in Project list :") + "\"" + project.getName() + "\"");
            }
        } else {
            Tools.showMessage(this, "\"" + project.getName() + "\"" + java.util.ResourceBundle.getBundle("VisualLogic/Messages").getString("is not a project"));
        }

    }

    class LabelAccessory extends JLabel implements PropertyChangeListener {

        private ImageIcon iconOK = new javax.swing.ImageIcon(getClass().getResource("/Bilder/isProject.png"));
        private ImageIcon iconX = new javax.swing.ImageIcon(getClass().getResource("/Bilder/notAProject.png"));

        public LabelAccessory(JFileChooser chooser) {
            chooser.addPropertyChangeListener(this);
            setPreferredSize(new Dimension(100, 100));

            setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        }

        public void propertyChange(PropertyChangeEvent changeEvent) {
            String changeName = changeEvent.getPropertyName();
            if (changeName.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
                File file = (File) changeEvent.getNewValue();
                if (file != null) {
                    if (Tools.isProject(file.list())) {
                        setIcon(iconOK);
                    } else {
                        setIcon(iconX);
                    }
                    //else setText("Not a Project!");

                }
            }
        }
    }

        private void jmiOpenProjectActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmiOpenProjectActionPerformed
        {//GEN-HEADEREND:event_jmiOpenProjectActionPerformed

            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File(settings.getOldProjectPath()));
            chooser.setDialogTitle(java.util.ResourceBundle.getBundle("VisualLogic/Messages").getString("Open Project"));
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            OnlyDirectoryFilter filter = new OnlyDirectoryFilter();
            chooser.setFileFilter(filter);

            FileView view = new JavaFileView();

            chooser.setFileView(view);

            //chooser.setAccessory(new LabelAccessory(chooser));
            int value = chooser.showOpenDialog(this);
            if (value == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                settings.setOldProjectPath(chooser.getCurrentDirectory().getPath());
                openProject(file);
            }

            /*  if (isEnabled)
        {
        basis.stop();
        basis.getFrameMain().openFile();
        }*/
        }//GEN-LAST:event_jmiOpenProjectActionPerformed

    private void createNewProject(String projectName, boolean createMainVM, String mainVMFilename, String projectType) {
        if (!new File(projectName).exists()) {
            try {
                String str = "";

                File project = new File(projectName);
                project.mkdir();
                File projectFile = new File(project.getAbsolutePath() + "/project.myopenlab");

                projectFile.createNewFile();

                if (createMainVM) {
                    String filename = projectName + "/" + mainVMFilename + ".vlogic";
                    Basis basis = new Basis(this, this.elementPath);
                    basis.saveToFile(filename, false);

                    String strX = new File(filename).getName();
                    str += "MAINVM= " + strX + "\n";
                }

                if (projectType.equalsIgnoreCase("SPS")) {
                    str += "PROJECTTYPE= SPS\n";
                }

                Tools.saveText(projectFile, str);

                //Basis oBasis=addBasisToVMPanel(filename);
            } catch (Exception ex) {
                Tools.showMessage(java.util.ResourceBundle.getBundle("VisualLogic/Messages").getString("Could not create") + " " + projectName);
            }

        } else {
            Tools.showMessage(this, java.util.ResourceBundle.getBundle("VisualLogic/Messages").getString("Project already exist") + " : " + projectName);
        }

    }

    private void jmiNewProjectActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmiNewProjectActionPerformed
    {//GEN-HEADEREND:event_jmiNewProjectActionPerformed

        DialogNewProject frm = new DialogNewProject(this, true);
        frm.setVisible(true);
        if (frm.result) {
            String projectName = frm.projectName;

            File f = new File(projectName);
            if (!f.exists()) {

                projectName = projectName.replace("\\", "/");

                createNewProject(projectName, frm.createMainVM, frm.mainVMFilename, frm.projectType);
                projects.add(projectName);
                reloadProjectPanel();
            } else {
                Tools.showMessage(java.util.ResourceBundle.getBundle("VisualLogic/Messages").getString("Directory already exist") + " : " + f.getAbsolutePath());
            }

        }
        //basis.stop();
        //basis.getFrameMain().createNewVM();
    }//GEN-LAST:event_jmiNewProjectActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        //if (isEnabled)
        {
            closeApplication();
        }
    }//GEN-LAST:event_formWindowClosing

    private void formWindowLostFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowLostFocus

    }//GEN-LAST:event_formWindowLostFocus

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus

    }//GEN-LAST:event_formWindowGainedFocus

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained

    }//GEN-LAST:event_formFocusGained

    private void jButtonStepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStepActionPerformed

        Basis basis = getAktuelleBasis();
        if (basis != null) {
            basis.step();
        }

    }//GEN-LAST:event_jButtonStepActionPerformed

    private void jButtonResumeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResumeActionPerformed
        Basis basis = getAktuelleBasis();
        if (basis != null) {
            basis.resume();
        }
    }//GEN-LAST:event_jButtonResumeActionPerformed

    private void jButtonPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPauseActionPerformed
        Basis basis = getAktuelleBasis();
        if (basis != null) {
            basis.pause();
        }
    }//GEN-LAST:event_jButtonPauseActionPerformed

    private void jButtonStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStopActionPerformed
        Basis basis = getAktuelleBasis();
        if (basis != null) {
            basis.stop();
        }
    }//GEN-LAST:event_jButtonStopActionPerformed

    // MyOpenLabIF
    public void ownerMessage(String message) {
        //Tools.showMessage(this, message);
        System.out.println("Message=" + message);
    }

    private void runPLCSimulator(String code) {
        /*URL url = null;
        try
        {
            url = new URL("file:" + System.getProperty("user.home") + System.getProperty("file.separator") + "MyOpenLabMCU");
            
        } catch (MalformedURLException ex)
        {
            ex.printStackTrace();
        }
        if (url != null)*/

        {
            //SimulatorSocket.Server server = new SimulatorSocket.Server(this);
            //server.start();

            // ASM Datei ins MyOpenLab User Dir speichern
            String fileNameDest_ASM = getUserURL().getFile() + "\\mcu_code.asm";
            Tools.saveText(new File(fileNameDest_ASM), code);

            //String fileName = elementPath+"..\simulator";
            String jarFileDir = elementPath + "\\..\\simulator";

            String jarFile = new File(jarFileDir + "\\Simulator.jar").getAbsolutePath();

            //String jarFile="E:\\ASM_Simulation\\Simulator\\dist\\Simulator.jar";
            if (new File(jarFile).exists()) {
                ProcessBuilder builder = new ProcessBuilder("cmd", "/c", "start", "javaw", "-jar", jarFile, fileNameDest_ASM);

                builder.directory(new File(jarFileDir));
                try {
                    Process simulatorProcess = builder.start();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                Tools.showMessage(this, "Application \"Simulator\" not found.\nPlease start the Application \"Simulator\" for registering.");
                new FrameCode(this, code);
            }
        }
        /*else
        {
            new FrameCode(this, code);
        }*/

    }

    private void execSPSProject() {
        /*if (unusedPinsExist())
        {
        Tools.showMessage(this, "There are unused Pins or Element!");
        }
        else*/
        {

            //String code = generateMCUCode();
            //String code = generateMCUFlowChartCode();
            //String code = generateMCUVMCode();
            String code = generateMCUFlowChartCode();
            //new FrameCode(this, code);  

            if (code.length() > 0) {
                code = entferneReduntanteJMPs(code);
                runPLCSimulator(code);
            }

            //runPLCSimulator(code);
        }
    }

    private void jButtonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStartActionPerformed
        //startBasis();
        Basis basis = getAktuelleBasis();
        if (basis != null) {

            if (basis.projectPath != null && basis.projectPath.length() > 0) {

                ProjectProperties props = Tools.openProjectFile(new File(basis.projectPath));

                if (props.projectType.equalsIgnoreCase("SPS")) {
                    execSPSProject();
                } else {
                    basis.start(false);
                }
            } else {
                basis.start(false);
            } // Start Single VM

        }

    }//GEN-LAST:event_jButtonStartActionPerformed

    private void jmiMantisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiMantisActionPerformed
        Tools.openUrl(this, "http://mantis.myopenlab.de");

    }//GEN-LAST:event_jmiMantisActionPerformed

    public void reinitPackage() {
        reloadElementPalettes();
        initDocs();

    }

    private void jmniUpdaterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmniUpdaterActionPerformed

        showRepository();
    }//GEN-LAST:event_jmniUpdaterActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        showRepository();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void paneRightMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paneRightMousePressed

    }//GEN-LAST:event_paneRightMousePressed

    private void jPanel8MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel8MouseDragged
        Rectangle rect = getBounds();
        int w = getWidth() + rect.x - evt.getXOnScreen() - 7;

        if (w < 5) {
            w = 5;
        }
        jPanel7.setPreferredSize(new Dimension(w, 100));
        jPanel7.updateUI();
    }//GEN-LAST:event_jPanel8MouseDragged

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed

        int xx = jPanel7.getWidth();

        if (xx < 20) {

            jPanel7.setPreferredSize(new Dimension(settings.getOldRightSplitterPos(), 100));
            jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/arrow_black_right.png"))); // NOI18N
        } else {
            settings.setOldRightSplitterPos(xx);
            jPanel7.setPreferredSize(new Dimension(10, 100));
            jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/arrow_black_left.png"))); // NOI18N
        }

        jPanel7.updateUI();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void showRepository() {
        String tmp = new File(elementPath).getAbsolutePath();

        String distributiondir = tmp.substring(0, tmp.length() - "Elements".length());

        //Tools.showMessage(this, distributiondir);
        de.myopenlab.update.frmUpdate.myopenlabpath = distributiondir;

        de.myopenlab.update.frmUpdate frm2 = new frmUpdate();
        frm2.owner = this;

        frm2.setVisible(true);
    }

    int beginIndex = 0;
    int index = 0;

    private String removeJMPs(String code) {
        String ch = "";
        while (true) {
            ch = code.substring(index, index + 1);
            if (index >= code.length()) {
                return code;
            }
            if (ch.trim().length() == 0) {
                // ende des Labe erkannt
                String label = code.substring(beginIndex, index);
                System.out.println("" + label);

                // ist untendrunter das Label vorhanden?
                int lastIndex = index;
                while (true) {
                    // gehe bis keine Leerzeilen/Zeichen vorhanden                        
                    ch = code.substring(index, index + 1);
                    index++;
                    if (index >= code.length()) {
                        return code;
                    }
                    if (ch.trim().length() > 0) {
                        index--;
                        int start2 = index;
                        while (true) {
                            // und hole das erste Wort+':'
                            ch = code.substring(index, index + 1);
                            index++;
                            if (index >= code.length()) {
                                return code;
                            }
                            if (ch.trim().length() == 0) {
                                // ende des Labe erkannt
                                //String gg=code.substring(beginIndex, index);
                                String gg = code.substring(start2, index - 2);
                                if (gg.equalsIgnoreCase(label)) {
                                    String newCode = code.substring(0, beginIndex - 5);
                                    newCode += code.substring(lastIndex, code.length());
                                    code = newCode;
                                }
                                return code;
                                //System.out.println(""+gg);
                            }
                        }

                    }

                }

            }

            index++;
        }
    }

    private String entferneReduntanteJMPs(String code) {
        index = 0;
        while (true) {
            index = code.indexOf("JMP", index);
            if (index == -1) {
                break;
            }

            index += 4;
            System.out.println("" + code.length());
            if (index >= code.length()) {
                break;
            }
            // extract the Label
            String ch = "";
            beginIndex = index;

            code = removeJMPs(code);

        }
        return code;
    }

    public Image createImageOfElement(Element element) {

        File file = new File(element.elementPath + element.mainPath);
        DFProperties definition_def = Tools.getProertiesFromDefinitionFile(file);

        ImageIcon icon = new ImageIcon(element.elementPath + element.mainPath + "/" + definition_def.iconFilename);
        return icon.getImage();

    }

    private void loadDoc(Element element, String filename, JEditorPane pane) {
        URL url = null;
        String nope = element.elementPath + "/nope.html";
        if (!new File(filename).exists()) {
            filename = nope;
        }

        try {
            url = new URL("file:" + filename);
        } catch (Exception ex) {

        }

        try {
            pane.setContentType("text/html");
            pane.setPage(url);
        } catch (Exception e) {

        }

    }

    public void activate_DocFrame(Element element) {

        try {
            panelDoc.openElementDocFile(this, element);
        } catch (Exception ex) {
            Logger.getLogger(FrameMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdRedo;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdUndo;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonDebug;
    private javax.swing.JButton jButtonPause;
    private javax.swing.JButton jButtonRefreshVM;
    private javax.swing.JButton jButtonResume;
    private javax.swing.JButton jButtonStart;
    private javax.swing.JButton jButtonStep;
    private javax.swing.JButton jButtonStop;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelElementPalette;
    private javax.swing.JPanel jPanelLeft;
    private javax.swing.JPanel jPanelPE;
    private javax.swing.JPopupMenu jPopupMenu1;
    public javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSpinner jSpinner1;
    public javax.swing.JSplitPane jSplitPane1;
    public javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JTabbedPane jTabPE;
    private javax.swing.JToolBar jToolBar6;
    private javax.swing.JMenuItem jmiClearWindow;
    private javax.swing.JMenuItem jmiCloseWindow;
    private javax.swing.JMenuItem jmiCopy;
    private javax.swing.JMenuItem jmiCopyToClip;
    private javax.swing.JMenuItem jmiCut;
    private javax.swing.JMenuItem jmiEigenschaten;
    private javax.swing.JMenuItem jmiForum;
    private javax.swing.JMenuItem jmiHomepage;
    private javax.swing.JMenuItem jmiInfo;
    private javax.swing.JMenuItem jmiLegend;
    private javax.swing.JMenuItem jmiMantis;
    private javax.swing.JMenuItem jmiNewProject;
    private javax.swing.JMenuItem jmiOpenProject;
    private javax.swing.JMenuItem jmiPaste;
    private javax.swing.JMenuItem jmiPause;
    private javax.swing.JMenuItem jmiRedo;
    private javax.swing.JMenuItem jmiResume;
    private javax.swing.JMenuItem jmiSave;
    private javax.swing.JMenuItem jmiSaveAsJPG;
    private javax.swing.JMenuItem jmiSaveAsModul;
    private javax.swing.JMenuItem jmiSelectAny;
    private javax.swing.JMenuItem jmiShowAnalogWindow;
    private javax.swing.JMenuItem jmiShowDigitalWindow;
    private javax.swing.JMenuItem jmiShowTestpointWindow;
    private javax.swing.JMenuItem jmiStart;
    private javax.swing.JMenuItem jmiStep;
    private javax.swing.JMenuItem jmiStop;
    private javax.swing.JMenuItem jmiTutorials;
    private javax.swing.JMenuItem jmiUndo;
    private javax.swing.JMenuItem jmiVariableWatcher;
    private javax.swing.JMenuItem jmniChooseLanguage;
    private javax.swing.JMenuItem jmniCloseAllVMs;
    private javax.swing.JMenuItem jmniCloseAllVms;
    private javax.swing.JMenuItem jmniCreateNewJavaComponent;
    private javax.swing.JMenuItem jmniCreateSubVM;
    private javax.swing.JMenuItem jmniDefineVariables;
    private javax.swing.JMenuItem jmniDeletePasswordProtection;
    private javax.swing.JMenuItem jmniOptions;
    private javax.swing.JMenuItem jmniPasswordProtection;
    private javax.swing.JMenuItem jmniPrintVM;
    private javax.swing.JMenuItem jmniSaveAsSingleVM;
    private javax.swing.JMenuItem jmniShowErrorsAndWarnings;
    private javax.swing.JMenuItem jmniUpdate;
    private javax.swing.JMenuItem jmniUpdater;
    private javax.swing.JMenu jmnuDatei;
    private javax.swing.JMenu jmnuDocs;
    private javax.swing.JMenu jmnuEdit;
    private javax.swing.JMenu jmnuExtras;
    private javax.swing.JMenu jmnuHelp;
    private javax.swing.JMenuItem jmnuOpenSingleVM;
    private javax.swing.JMenu jmnuVM;
    private javax.swing.JMenu jmnuWindow;
    public javax.swing.JTabbedPane paneRight;
    // End of variables declaration//GEN-END:variables
}
