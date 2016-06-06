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
package projectfolder;

import VisualLogic.Tools;
import java.awt.Component;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
 
class MyRenderer extends DefaultTreeCellRenderer {

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        MyNode node = (MyNode) value;

        node.isExpanded = expanded;

        if (expanded) {
            setIcon(node.iconExpanded);
        } else {
            setIcon(node.iconCollapsed);
        }

        if (node.isRoot()) {
            setText(node.toString());
        } else if (node.isProject) {
            String caption = "";
            File file = new File(node.projectPath + "/info.xml");
            if (file.exists()) {
                caption = Tools.getInfoXMLCaption(new File(node.projectPath));
            } else {
                caption = new File(node.projectPath).getName();
            }

            setText(caption);
        } else if (node.isFolder) {
            String caption = "";
            File file = new File(node.projectPath + node.relativePath + "/info.xml");
            if (file.exists()) {
                caption = Tools.getInfoXMLCaption(new File(node.projectPath + node.relativePath));
            } else {
                caption = new File(node.projectPath + node.relativePath).getName();
            }
            setText(caption);
        } else {
            setText(new File(node.projectPath + node.relativePath).getName());
        }

        /*Object o=node.getUserObject();
        if (o instanceof File)
        {
            File f=(File)o;
            
            //String extension = getExtension(f);
            //String fn=f.getName().substring(0,f.getName().length()-extension.length()-1);
            
            setText(f.getName());
        }*/
        return this;
    }
}

/**
 *
 * @author Carmelo
 */
public class ProjectPalette extends javax.swing.JPanel {

    private MyNode projects = new MyNode(java.util.ResourceBundle.getBundle("projectfolder/ProjectPalette").getString("Projects"));
    private ProjectPaletteIF owner;
    private final ImageIcon iconVM = new javax.swing.ImageIcon(getClass().getResource("/projectfolder/text-x-script.png"));

    private final ImageIcon iconDirCollapsed = new javax.swing.ImageIcon(getClass().getResource("/projectfolder/folder.png"));
    private final ImageIcon iconDirExpanded = new javax.swing.ImageIcon(getClass().getResource("/projectfolder/folder-open.png"));

    private final ImageIcon iconExecCollapsed = new javax.swing.ImageIcon(getClass().getResource("/projectfolder/execfolder.png"));
    private final ImageIcon iconExecExpanded = new javax.swing.ImageIcon(getClass().getResource("/projectfolder/execfolder.png"));

    private final ImageIcon rootCollapsed = new javax.swing.ImageIcon(getClass().getResource("/projectfolder/user-home.png"));
    private final ImageIcon rootExpanded = new javax.swing.ImageIcon(getClass().getResource("/projectfolder/user-home.png"));

    private final ImageIcon iconProjectCollapsed = new javax.swing.ImageIcon(getClass().getResource("/projectfolder/package-x-generic.png"));
    private final ImageIcon iconProjectExpanded = new javax.swing.ImageIcon(getClass().getResource("/projectfolder/package-x-generic.png"));

    private final ImageIcon iconImage = new javax.swing.ImageIcon(getClass().getResource("/projectfolder/image-x-generic.png"));
    private final ImageIcon iconHTML = new javax.swing.ImageIcon(getClass().getResource("/projectfolder/text-html.png"));

    private final ImageIcon iconOther = new javax.swing.ImageIcon(getClass().getResource("/projectfolder/page_deny.gif"));

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public void init() {
        projects = new MyNode(java.util.ResourceBundle.getBundle("projectfolder/ProjectPalette").getString("Projects"));

        projects.projectPath = java.util.ResourceBundle.getBundle("projectfolder/ProjectPalette").getString("Projects");
        projects.iconCollapsed = rootCollapsed;
        projects.iconExpanded = rootExpanded;

        jTree1.setModel(new DefaultTreeModel(projects));
    }

    public void listAllFilesFromProjectPath(String path) {

        File file = new File(path);

        if (file.exists()) {
            File[] files = file.listFiles();

            MyNode root = new MyNode(file);
            root.isProject = true;
            root.projectPath = file.getPath();

            if (file.isDirectory()) {
                root.isFolder = true;
            } else {
                root.isFolder = false;
            }

            root.iconExpanded = iconProjectExpanded;
            root.iconCollapsed = iconProjectCollapsed;

            projects.add(root);

            if (isExecutableProject(file)) {
                root.iconExpanded = iconExecExpanded;
                root.iconCollapsed = iconExecCollapsed;

                return;
            }
            rec(root, path, "");
        }

        jTree1.updateUI();

    }

    public static boolean isProject(File file) {
        if (file != null) {
            if (new File(file.getPath() + "/project.myopenlab").exists()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isExecutableProject(File file) {
        if (file != null) {
            if (new File(file.getPath() + "/myopenlab.executable").exists()) {
                return true;
            }
        }
        return false;
    }

    public void rec(MyNode root, String projectPath, String path) {
        File fileX = new File(projectPath + path);

        File file = new File(fileX.getAbsolutePath());

        File[] files = file.listFiles();

        if (files == null || files.length == 0) {
            return;
        }

        Arrays.sort(files, new Comparator() {
            @Override
            public int compare(final Object o1, final Object o2) {
                final File f1 = (File) o1;
                final File f2 = (File) o2;
                if (f1.isDirectory() == f2.isDirectory()) {
                    return f1.compareTo(f2);
                } else if (f1.isDirectory()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        for (File f : files) {
            MyNode node = new MyNode(file);
            root.add(node);

            if (f.exists()) {
                if (f.isDirectory()) {
                    node.iconCollapsed = iconDirCollapsed;
                    node.iconExpanded = iconDirExpanded;

                    node.isFolder = true;
                    node.projectPath = projectPath;
                    node.relativePath = path + "/" + f.getName();
                    rec(node, projectPath, node.relativePath);
                } else {
                    String extension = getExtension(f);

                    node.isFile = true;

                    node.projectPath = projectPath;
                    node.relativePath = path + "/" + f.getName();

                    if (extension != null && extension.equalsIgnoreCase("vlogic")) {
                        node.iconCollapsed = iconVM;
                        node.iconExpanded = iconVM;
                    } else if (extension != null && (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("gif") || extension.equalsIgnoreCase("png"))) {
                        node.iconCollapsed = iconImage;
                        node.iconExpanded = iconImage;
                    } else if (extension != null && (extension.equalsIgnoreCase("html") || extension.equalsIgnoreCase("htm") || extension.equalsIgnoreCase("txt"))) {
                        node.iconCollapsed = iconHTML;
                        node.iconExpanded = iconHTML;
                    } else {
                        root.remove(node);

                        //node.iconCollapsed=iconOther;
                        //node.iconExpanded=iconOther;
                    }

                }
            }
        }
    }

    public void expandRow(int row) {
        jTree1.expandRow(row);
    }

    public void expandAll() {
        int row = 0;
        while (row < jTree1.getRowCount()) {
            jTree1.expandRow(row);
            row++;
        }
    }

    /**
     * Creates new form ProjectPalette
     */
    public ProjectPalette(ProjectPaletteIF owner) {
        initComponents();

        this.owner = owner;

        MyRenderer renderer = new MyRenderer();
        /*renderer.setOpenIcon(null);
        renderer.setClosedIcon(null);
        renderer.setLeafIcon(null);*/
        jTree1.setCellRenderer(renderer);

        //listAllFiles("C:/Dokumente und Einstellungen/Carmelo/Desktop/test");
        //listAllFiles("C:/Dokumente und Einstellungen/Carmelo/Desktop/Carmelo");
        //jTree1.expandRow(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupMenuProject = new javax.swing.JPopupMenu();
        jmniAddVM = new javax.swing.JMenuItem();
        jmniAddSubVM = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JSeparator();
        jmniDelProject = new javax.swing.JMenuItem();
        jmniProjectProperties = new javax.swing.JMenuItem();
        jmniCloseproject = new javax.swing.JMenuItem();
        jmniMakeDistribution = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jmniAddDirectory = new javax.swing.JMenuItem();
        jmniDeleteDirectory = new javax.swing.JMenuItem();
        jmniRenameDirectory = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        jmniCopyDir = new javax.swing.JMenuItem();
        jmniCutDir = new javax.swing.JMenuItem();
        jmniPasteDir = new javax.swing.JMenuItem();
        popupMenuFile = new javax.swing.JPopupMenu();
        jmniOpenFile = new javax.swing.JMenuItem();
        jmniEditFile = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        jmniDelVM = new javax.swing.JMenuItem();
        jmniRename = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        jmniCopyFile = new javax.swing.JMenuItem();
        jmniCutFile = new javax.swing.JMenuItem();
        popupMenuRoot = new javax.swing.JPopupMenu();
        jmniAddNewProject = new javax.swing.JMenuItem();
        jmniOpenroject = new javax.swing.JMenuItem();
        jmniReload = new javax.swing.JMenuItem();
        popupMenuDir = new javax.swing.JPopupMenu();
        jmniAddDirectoryDir = new javax.swing.JMenuItem();
        jmniDeleteDirectoryDir = new javax.swing.JMenuItem();
        jmniRenameDirectoryDir = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jmniCopy = new javax.swing.JMenuItem();
        jmniCut = new javax.swing.JMenuItem();
        jmniPaste = new javax.swing.JMenuItem();
        popupMenuFolder = new javax.swing.JPopupMenu();
        jmniAddVM1 = new javax.swing.JMenuItem();
        jmniAddSubVM1 = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JSeparator();
        jmniAddDirectory1 = new javax.swing.JMenuItem();
        jmniDeleteDirectory1 = new javax.swing.JMenuItem();
        jmniRenameDirectory1 = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JSeparator();
        jmniCopyDir1 = new javax.swing.JMenuItem();
        jmniCutDir1 = new javax.swing.JMenuItem();
        jmniPasteDir1 = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new JTree(projects);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("projectfolder/ProjectPalette"); // NOI18N
        jmniAddVM.setText(bundle.getString("Add_New_VM")); // NOI18N
        jmniAddVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniAddVMActionPerformed(evt);
            }
        });
        popupMenuProject.add(jmniAddVM);

        jmniAddSubVM.setText(bundle.getString("Add_Sub_VM")); // NOI18N
        jmniAddSubVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniAddSubVMActionPerformed(evt);
            }
        });
        popupMenuProject.add(jmniAddSubVM);
        popupMenuProject.add(jSeparator6);

        jmniDelProject.setText(bundle.getString("Delete_Project")); // NOI18N
        jmniDelProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniDelProjectActionPerformed(evt);
            }
        });
        popupMenuProject.add(jmniDelProject);

        jmniProjectProperties.setText(bundle.getString("Project_Properties")); // NOI18N
        jmniProjectProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniProjectPropertiesActionPerformed(evt);
            }
        });
        popupMenuProject.add(jmniProjectProperties);

        jmniCloseproject.setText(bundle.getString("Close_Project")); // NOI18N
        jmniCloseproject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniCloseprojectActionPerformed(evt);
            }
        });
        popupMenuProject.add(jmniCloseproject);

        jmniMakeDistribution.setText(bundle.getString("Create_Distribution")); // NOI18N
        jmniMakeDistribution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniMakeDistributionActionPerformed(evt);
            }
        });
        popupMenuProject.add(jmniMakeDistribution);
        popupMenuProject.add(jSeparator1);

        jmniAddDirectory.setText(bundle.getString("Add_Folder")); // NOI18N
        jmniAddDirectory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniAddDirectoryActionPerformed(evt);
            }
        });
        popupMenuProject.add(jmniAddDirectory);

        jmniDeleteDirectory.setText(bundle.getString("Delete")); // NOI18N
        jmniDeleteDirectory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniDeleteDirectoryActionPerformed(evt);
            }
        });
        popupMenuProject.add(jmniDeleteDirectory);

        jmniRenameDirectory.setText(bundle.getString("Rename")); // NOI18N
        jmniRenameDirectory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniRenameDirectoryActionPerformed(evt);
            }
        });
        popupMenuProject.add(jmniRenameDirectory);
        popupMenuProject.add(jSeparator3);

        jmniCopyDir.setText(bundle.getString("Copy")); // NOI18N
        jmniCopyDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniCopyDirActionPerformed(evt);
            }
        });
        popupMenuProject.add(jmniCopyDir);

        jmniCutDir.setText(bundle.getString("Cut")); // NOI18N
        jmniCutDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniCutDirActionPerformed(evt);
            }
        });
        popupMenuProject.add(jmniCutDir);

        jmniPasteDir.setText(bundle.getString("Paste")); // NOI18N
        jmniPasteDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniPasteDirActionPerformed(evt);
            }
        });
        popupMenuProject.add(jmniPasteDir);

        jmniOpenFile.setText(bundle.getString("Open")); // NOI18N
        jmniOpenFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniOpenFileActionPerformed(evt);
            }
        });
        popupMenuFile.add(jmniOpenFile);

        jmniEditFile.setText(bundle.getString("Edit")); // NOI18N
        jmniEditFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniEditFileActionPerformed(evt);
            }
        });
        popupMenuFile.add(jmniEditFile);
        popupMenuFile.add(jSeparator5);

        jmniDelVM.setText(bundle.getString("Delete")); // NOI18N
        jmniDelVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniDelVMActionPerformed(evt);
            }
        });
        popupMenuFile.add(jmniDelVM);

        jmniRename.setText(bundle.getString("Rename")); // NOI18N
        jmniRename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniRenameActionPerformed(evt);
            }
        });
        popupMenuFile.add(jmniRename);
        popupMenuFile.add(jSeparator4);

        jmniCopyFile.setText(bundle.getString("Copy")); // NOI18N
        jmniCopyFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniCopyFileActionPerformed(evt);
            }
        });
        popupMenuFile.add(jmniCopyFile);

        jmniCutFile.setText(bundle.getString("Cut")); // NOI18N
        jmniCutFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniCutFileActionPerformed(evt);
            }
        });
        popupMenuFile.add(jmniCutFile);

        jmniAddNewProject.setText(bundle.getString("New_Project")); // NOI18N
        jmniAddNewProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniAddNewProjectActionPerformed(evt);
            }
        });
        popupMenuRoot.add(jmniAddNewProject);

        jmniOpenroject.setText(bundle.getString("Open_Project")); // NOI18N
        jmniOpenroject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniOpenrojectActionPerformed(evt);
            }
        });
        popupMenuRoot.add(jmniOpenroject);

        jmniReload.setText(bundle.getString("Reload")); // NOI18N
        jmniReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniReloadActionPerformed(evt);
            }
        });
        popupMenuRoot.add(jmniReload);

        jmniAddDirectoryDir.setText(bundle.getString("Add_Folder")); // NOI18N
        jmniAddDirectoryDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniAddDirectoryDirActionPerformed(evt);
            }
        });
        popupMenuDir.add(jmniAddDirectoryDir);

        jmniDeleteDirectoryDir.setText(bundle.getString("Delete")); // NOI18N
        jmniDeleteDirectoryDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniDeleteDirectoryDirActionPerformed(evt);
            }
        });
        popupMenuDir.add(jmniDeleteDirectoryDir);

        jmniRenameDirectoryDir.setText(bundle.getString("Rename")); // NOI18N
        jmniRenameDirectoryDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniRenameDirectoryDirActionPerformed(evt);
            }
        });
        popupMenuDir.add(jmniRenameDirectoryDir);
        popupMenuDir.add(jSeparator2);

        jmniCopy.setText(bundle.getString("Copy")); // NOI18N
        jmniCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniCopyActionPerformed(evt);
            }
        });
        popupMenuDir.add(jmniCopy);

        jmniCut.setText(bundle.getString("Cut")); // NOI18N
        jmniCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniCutActionPerformed(evt);
            }
        });
        popupMenuDir.add(jmniCut);

        jmniPaste.setText(bundle.getString("Paste")); // NOI18N
        jmniPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniPasteActionPerformed(evt);
            }
        });
        popupMenuDir.add(jmniPaste);

        jmniAddVM1.setText(bundle.getString("Add_New_VM")); // NOI18N
        jmniAddVM1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniAddVM1ActionPerformed(evt);
            }
        });
        popupMenuFolder.add(jmniAddVM1);

        jmniAddSubVM1.setText(bundle.getString("Add_Sub_VM")); // NOI18N
        jmniAddSubVM1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniAddSubVM1ActionPerformed(evt);
            }
        });
        popupMenuFolder.add(jmniAddSubVM1);
        popupMenuFolder.add(jSeparator7);

        jmniAddDirectory1.setText(bundle.getString("Add_Folder")); // NOI18N
        jmniAddDirectory1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniAddDirectory1ActionPerformed(evt);
            }
        });
        popupMenuFolder.add(jmniAddDirectory1);

        jmniDeleteDirectory1.setText(bundle.getString("Delete")); // NOI18N
        jmniDeleteDirectory1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniDeleteDirectory1ActionPerformed(evt);
            }
        });
        popupMenuFolder.add(jmniDeleteDirectory1);

        jmniRenameDirectory1.setText(bundle.getString("Rename")); // NOI18N
        jmniRenameDirectory1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniRenameDirectory1ActionPerformed(evt);
            }
        });
        popupMenuFolder.add(jmniRenameDirectory1);
        popupMenuFolder.add(jSeparator9);

        jmniCopyDir1.setText(bundle.getString("Copy")); // NOI18N
        jmniCopyDir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniCopyDir1ActionPerformed(evt);
            }
        });
        popupMenuFolder.add(jmniCopyDir1);

        jmniCutDir1.setText(bundle.getString("Cut")); // NOI18N
        jmniCutDir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniCutDir1ActionPerformed(evt);
            }
        });
        popupMenuFolder.add(jmniCutDir1);

        jmniPasteDir1.setText(bundle.getString("Paste")); // NOI18N
        jmniPasteDir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmniPasteDir1ActionPerformed(evt);
            }
        });
        popupMenuFolder.add(jmniPasteDir1);

        jScrollPane1.setBorder(null);

        jTree1.setDragEnabled(true);
        jTree1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTree1MousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jmniOpenFileActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniOpenFileActionPerformed
    {//GEN-HEADEREND:event_jmniOpenFileActionPerformed
        if (selectedNode != null) {
            owner.projectPaletteAction("OPENFILE", selectedNode);
        }
    }//GEN-LAST:event_jmniOpenFileActionPerformed

    private void jmniEditFileActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniEditFileActionPerformed
    {//GEN-HEADEREND:event_jmniEditFileActionPerformed
        if (selectedNode != null) {
            owner.projectPaletteAction("EDITFILE", selectedNode);
        }
    }//GEN-LAST:event_jmniEditFileActionPerformed

    private void jmniCopyFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmniCopyFileActionPerformed
        if (selectedNode != null) {
            owner.projectPaletteAction("FILE_COPY", selectedNode);
        }
    }//GEN-LAST:event_jmniCopyFileActionPerformed

    private void jmniCutFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmniCutFileActionPerformed
        if (selectedNode != null) {
            owner.projectPaletteAction("FILE_CUT", selectedNode);
        }
    }//GEN-LAST:event_jmniCutFileActionPerformed

    private void jmniCopyDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmniCopyDirActionPerformed
        jmniCopyActionPerformed(evt);
    }//GEN-LAST:event_jmniCopyDirActionPerformed

    private void jmniPasteDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmniPasteDirActionPerformed
        jmniPasteActionPerformed(evt);
    }//GEN-LAST:event_jmniPasteDirActionPerformed

    private void jmniCutDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmniCutDirActionPerformed
        jmniCutActionPerformed(evt);
    }//GEN-LAST:event_jmniCutDirActionPerformed

    private void jmniPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmniPasteActionPerformed
        if (selectedNode != null) {
            owner.projectPaletteAction("PASTE", selectedNode);
        }
    }//GEN-LAST:event_jmniPasteActionPerformed

    private void jmniCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmniCutActionPerformed
        if (selectedNode != null) {
            owner.projectPaletteAction("DIR_CUT", selectedNode);
        }
    }//GEN-LAST:event_jmniCutActionPerformed

    private void jmniCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmniCopyActionPerformed
        if (selectedNode != null) {
            owner.projectPaletteAction("DIR_COPY", selectedNode);
        }
    }//GEN-LAST:event_jmniCopyActionPerformed

    private void jmniReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmniReloadActionPerformed

        if (selectedNode != null) {
            owner.projectPaletteAction("RELOAD", selectedNode);
        }
    }//GEN-LAST:event_jmniReloadActionPerformed

    private void jmniRenameDirectoryDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmniRenameDirectoryDirActionPerformed
        jmniRenameDirectoryActionPerformed(evt);
    }//GEN-LAST:event_jmniRenameDirectoryDirActionPerformed

    private void jmniDeleteDirectoryDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmniDeleteDirectoryDirActionPerformed
        jmniDeleteDirectoryActionPerformed(evt);
    }//GEN-LAST:event_jmniDeleteDirectoryDirActionPerformed

    private void jmniAddDirectoryDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmniAddDirectoryDirActionPerformed
        jmniAddDirectoryActionPerformed(evt);
    }//GEN-LAST:event_jmniAddDirectoryDirActionPerformed

    private void jmniRenameDirectoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmniRenameDirectoryActionPerformed

        if (selectedNode != null) {
            owner.projectPaletteAction("DIR_RENAME", selectedNode);
        }

    }//GEN-LAST:event_jmniRenameDirectoryActionPerformed

    private void jmniDeleteDirectoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmniDeleteDirectoryActionPerformed
        if (selectedNode != null) {
            owner.projectPaletteAction("DIR_DELETE", selectedNode);
        }

    }//GEN-LAST:event_jmniDeleteDirectoryActionPerformed

    private void jmniAddDirectoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmniAddDirectoryActionPerformed
        if (selectedNode != null) {
            owner.projectPaletteAction("DIR_ADD", selectedNode);
        }

    }//GEN-LAST:event_jmniAddDirectoryActionPerformed

    private void jmniMakeDistributionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniMakeDistributionActionPerformed
    {//GEN-HEADEREND:event_jmniMakeDistributionActionPerformed
        if (selectedNode != null) {
            owner.projectPaletteAction("DISTRIBUTION", selectedNode);
        }

    }//GEN-LAST:event_jmniMakeDistributionActionPerformed

    private void jmniAddSubVMActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniAddSubVMActionPerformed
    {//GEN-HEADEREND:event_jmniAddSubVMActionPerformed

        if (selectedNode != null) {
            owner.projectPaletteNewSubVM(selectedNode);
        }

    }//GEN-LAST:event_jmniAddSubVMActionPerformed

    private void jmniRenameActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniRenameActionPerformed
    {//GEN-HEADEREND:event_jmniRenameActionPerformed
        if (selectedNode != null) {
            owner.projectPaletteRenameVM(selectedNode);
        }
    }//GEN-LAST:event_jmniRenameActionPerformed

    private void jmniCloseprojectActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniCloseprojectActionPerformed
    {//GEN-HEADEREND:event_jmniCloseprojectActionPerformed
        if (selectedNode != null) {
            owner.projectPaletteProjectClose(selectedNode);
        }
    }//GEN-LAST:event_jmniCloseprojectActionPerformed

    private void jmniProjectPropertiesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniProjectPropertiesActionPerformed
    {//GEN-HEADEREND:event_jmniProjectPropertiesActionPerformed
        if (selectedNode != null) {
            owner.projectPaletteProjectProperties(selectedNode);
        }
    }//GEN-LAST:event_jmniProjectPropertiesActionPerformed

    private void jmniOpenrojectActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniOpenrojectActionPerformed
    {//GEN-HEADEREND:event_jmniOpenrojectActionPerformed
        if (selectedNode != null) {
            owner.projectPaletteOpenProject(selectedNode);
        }
    }//GEN-LAST:event_jmniOpenrojectActionPerformed

    private void jmniAddNewProjectActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniAddNewProjectActionPerformed
    {//GEN-HEADEREND:event_jmniAddNewProjectActionPerformed
        if (selectedNode != null) {
            owner.projectPaletteAddNewProject(selectedNode);
        }
    }//GEN-LAST:event_jmniAddNewProjectActionPerformed

    private void jmniDelVMActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniDelVMActionPerformed
    {//GEN-HEADEREND:event_jmniDelVMActionPerformed
        if (selectedNode != null) {
            owner.projectPaletteDeleteVM(selectedNode);
        }
    }//GEN-LAST:event_jmniDelVMActionPerformed

    private void jmniDelProjectActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniDelProjectActionPerformed
    {//GEN-HEADEREND:event_jmniDelProjectActionPerformed
        if (selectedNode != null) {
            owner.projectPaletteDeleteProject(selectedNode);
        }
    }//GEN-LAST:event_jmniDelProjectActionPerformed

    private void jmniAddVMActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmniAddVMActionPerformed
    {//GEN-HEADEREND:event_jmniAddVMActionPerformed

        if (selectedNode != null) {
            owner.projectPaletteAction("ADDVM", selectedNode);
        }


    }//GEN-LAST:event_jmniAddVMActionPerformed

    MyNode selectedNode = null;


    private void jTree1MousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jTree1MousePressed
    {//GEN-HEADEREND:event_jTree1MousePressed

        int button = evt.getButton();

        int selRow = jTree1.getRowForLocation(evt.getX(), evt.getY());
        TreePath selPath = jTree1.getPathForLocation(evt.getX(), evt.getY());

        
            if (button == 3) {
                if (selRow != -1) {
                    jTree1.setSelectionPath(selPath);

                    Object o = selPath.getLastPathComponent();
                    if (o instanceof MyNode) {
                        MyNode node = (MyNode) o;
                        if (node.getLevel() == 0) {
                            selectedNode = node;
                            popupMenuRoot.show(jTree1, evt.getX(), evt.getY());
                        } else if (node.getLevel() == 1) {
                            selectedNode = node;
                            popupMenuProject.show(jTree1, evt.getX(), evt.getY());
                        } else if (node.isFolder) {
                            selectedNode = node;
                            popupMenuFolder.show(jTree1, evt.getX(), evt.getY());
                        } else {
                            selectedNode = node;
                            popupMenuFile.show(jTree1, evt.getX(), evt.getY());
                        }
                    }
                }

        } else if (selRow != -1) {
            if (evt.getClickCount() == 2) {
                Object o = selPath.getLastPathComponent();
                owner.projectPaletteAction("ITEMSELECTED", (MyNode) o);
            }
        }

    }//GEN-LAST:event_jTree1MousePressed

    private void jmniAddVM1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmniAddVM1ActionPerformed
        jmniAddVMActionPerformed(evt);
    }//GEN-LAST:event_jmniAddVM1ActionPerformed

    private void jmniAddSubVM1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmniAddSubVM1ActionPerformed
        jmniAddSubVMActionPerformed(evt);
    }//GEN-LAST:event_jmniAddSubVM1ActionPerformed

    private void jmniAddDirectory1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmniAddDirectory1ActionPerformed
        jmniAddDirectoryActionPerformed(evt);
    }//GEN-LAST:event_jmniAddDirectory1ActionPerformed

    private void jmniDeleteDirectory1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmniDeleteDirectory1ActionPerformed
        jmniDeleteDirectoryActionPerformed(evt);
    }//GEN-LAST:event_jmniDeleteDirectory1ActionPerformed

    private void jmniRenameDirectory1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmniRenameDirectory1ActionPerformed
        jmniRenameDirectoryActionPerformed(evt);
    }//GEN-LAST:event_jmniRenameDirectory1ActionPerformed

    private void jmniCopyDir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmniCopyDir1ActionPerformed
        jmniCopyDirActionPerformed(evt);
    }//GEN-LAST:event_jmniCopyDir1ActionPerformed

    private void jmniCutDir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmniCutDir1ActionPerformed
        jmniCutDirActionPerformed(evt);
    }//GEN-LAST:event_jmniCutDir1ActionPerformed

    private void jmniPasteDir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmniPasteDir1ActionPerformed
        jmniPasteDirActionPerformed(evt);
    }//GEN-LAST:event_jmniPasteDir1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator9;
    public javax.swing.JTree jTree1;
    private javax.swing.JMenuItem jmniAddDirectory;
    private javax.swing.JMenuItem jmniAddDirectory1;
    private javax.swing.JMenuItem jmniAddDirectoryDir;
    private javax.swing.JMenuItem jmniAddNewProject;
    private javax.swing.JMenuItem jmniAddSubVM;
    private javax.swing.JMenuItem jmniAddSubVM1;
    private javax.swing.JMenuItem jmniAddVM;
    private javax.swing.JMenuItem jmniAddVM1;
    private javax.swing.JMenuItem jmniCloseproject;
    private javax.swing.JMenuItem jmniCopy;
    private javax.swing.JMenuItem jmniCopyDir;
    private javax.swing.JMenuItem jmniCopyDir1;
    private javax.swing.JMenuItem jmniCopyFile;
    private javax.swing.JMenuItem jmniCut;
    private javax.swing.JMenuItem jmniCutDir;
    private javax.swing.JMenuItem jmniCutDir1;
    private javax.swing.JMenuItem jmniCutFile;
    private javax.swing.JMenuItem jmniDelProject;
    private javax.swing.JMenuItem jmniDelVM;
    private javax.swing.JMenuItem jmniDeleteDirectory;
    private javax.swing.JMenuItem jmniDeleteDirectory1;
    private javax.swing.JMenuItem jmniDeleteDirectoryDir;
    private javax.swing.JMenuItem jmniEditFile;
    private javax.swing.JMenuItem jmniMakeDistribution;
    private javax.swing.JMenuItem jmniOpenFile;
    private javax.swing.JMenuItem jmniOpenroject;
    private javax.swing.JMenuItem jmniPaste;
    private javax.swing.JMenuItem jmniPasteDir;
    private javax.swing.JMenuItem jmniPasteDir1;
    private javax.swing.JMenuItem jmniProjectProperties;
    private javax.swing.JMenuItem jmniReload;
    private javax.swing.JMenuItem jmniRename;
    private javax.swing.JMenuItem jmniRenameDirectory;
    private javax.swing.JMenuItem jmniRenameDirectory1;
    private javax.swing.JMenuItem jmniRenameDirectoryDir;
    private javax.swing.JPopupMenu popupMenuDir;
    private javax.swing.JPopupMenu popupMenuFile;
    private javax.swing.JPopupMenu popupMenuFolder;
    private javax.swing.JPopupMenu popupMenuProject;
    private javax.swing.JPopupMenu popupMenuRoot;
    // End of variables declaration//GEN-END:variables

}
