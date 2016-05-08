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

package codeeditor;

import VisualLogic.CommandIF;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.StyledEditorKit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.Toolkit;

/**
 *
 * @author  Homer
 */
public class PanelEditor extends javax.swing.JPanel implements CommandIF {
    public String filename="";
    private MyEditorPane pane ;
    private EditorKit editorKit;
    private boolean changed=false;
    private JFrame frame=null;
    private String oldSearch="";
    DialogSearch mSearchDialog=null;
    
    UndoManager undomanager = new UndoManager();
    
    
    public boolean isChanged() {
        return changed;
    }
    
    public PanelEditor getPanel() {
        return this;
    }
    /** Creates new form PanelEditor */
    public PanelEditor(JFrame frame) {
        initComponents();
        
        undomanager.setLimit( 1000 );
        
        this.frame=frame;
        pane = new MyEditorPane();
        
        
        pane.setSelectedTextColor(Color.WHITE);
        pane.setSelectionColor(Color.LIGHT_GRAY);
        
        jScrollPane1.add(pane);
        jScrollPane1.setViewportView(pane);
        
        pane.setFont(new java.awt.Font("Monospaced", 0, 12));
        
        pane.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                paneKeyPressed(evt);
            }
        });
        editorKit = new StyledEditorKit() {
            public Document createDefaultDocument() {
                return new JavaSyntaxDocument(getPanel());
            }
        };
        //pane.setEditorKit(editorKit);
        
        /*pane.setEditorKitForContentType("text/java", editorKit);
        JEditorPane.registerEditorKitForContentType("text/java", "editorKit");
        pane.setContentType("text/java");*/
        
        //pane.setContentType("text/text");
        
        //jButton2.setVisible(false);
        
    }
    
    
    public void paneKeyPressed(java.awt.event.KeyEvent evt) {
        changed=true;
        
        if (evt.getKeyCode()==evt.VK_F3) {
            if (oldSearch=="") {
                openSearchDialog();
            } else searchText(oldSearch);
        }
        
        if (evt.isControlDown() && evt.getKeyCode()==evt.VK_F) {
            openSearchDialog();
        }
    }
    
    public void openSearchDialog() {
        if(mSearchDialog==null){
            mSearchDialog = new DialogSearch(frame,false);
        }
        mSearchDialog.init(this, pane.getSelectedText());
        
        oldSearch="";
        mSearchDialog.setVisible(true);
    }
    
    private void searchText(String text) {
        String str=pane.getText();
        
        int pos1=str.indexOf(text,pane.getCaretPosition());
        
        this.updateUI();
        if(pos1>=0){
        	pane.select(pos1,pos1+text.length());
        } else {
        	// nothing found
        	Toolkit.getDefaultToolkit().beep();
        }
        //pane.requestFocus();
    }
    
    public void saveFile() {
        if (filename.length()>0) {
            try {
                FileWriter out = new FileWriter(new File(filename));
                pane.write(out);
                out.flush();
                out.close();
                changed=false;
                
            } catch(IOException ioe) {
                VisualLogic.Tools.showMessage(ioe.toString());
            }
        }
    }
    
    public void cut() {
        pane.cut();
    }
    public void copy() {
        pane.copy();
    }
    public void paste() {
        pane.paste();
    }
    
    public void undo() {
        try {
            if (undomanager.canUndo()) {
                undomanager.undo();
            }
        } catch (CannotUndoException e) { }
        
    }
    
    
    public void redo() {
        try {
            if (undomanager.canRedo()) {
                undomanager.redo();
            }
        } catch (CannotUndoException e) { }
        
    }
    
    public boolean undoable=true;
    public boolean isUndoable() {
        return undoable;
    }
    
    
    public void loadFile(String filename) {
        
        this.filename=filename;
        try {
            pane.setPage(new URL("file:"+filename));
            
            //System.out.println("filename="+filename);
            
            changed=false;
            pane.setCaretPosition(0);
            
        } catch(Exception e) {
            VisualLogic.Tools.showMessage(e.toString());
        }
        
        Document doc = pane.getDocument();
        
        // Listen for undo and redo events
        doc.addUndoableEditListener(new UndoableEditListener() {
            public void undoableEditHappened(UndoableEditEvent evt) {
                if (isUndoable()) {
                    undomanager.addEdit(evt.getEdit());
                }
            }
        });
        
        // Bind the undo action to ctl-Z
        pane.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");
        
        // Bind the redo action to ctl-Y
        pane.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");
        
        // Create an undo action and add it to the text component
        pane.getActionMap().put("Undo", new AbstractAction("Undo") {
            public void actionPerformed(ActionEvent evt) {
                undo();
            }
        });
        
        // Create a redo action and add it to the text component
        pane.getActionMap().put("Redo",  new AbstractAction("Redo") {
            public void actionPerformed(ActionEvent evt) {
                redo();
            }
        });
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    
    public boolean onClose() {
        
        if (isChanged()) {
            
            int result = JOptionPane.showConfirmDialog(frame, java.util.ResourceBundle.getBundle("codeeditor/PanelEditor").getString("Data_Changed_save"), "",JOptionPane.YES_NO_CANCEL_OPTION);
            
            if (result==JOptionPane.YES_OPTION) {
                saveFile();
                return true;
            }
            
            if (result==JOptionPane.NO_OPTION) {
                return true;
            }
            if (result==JOptionPane.CANCEL_OPTION) {
                return false;
            }
        }
        return true;
    }
    
    
    public void close() {
        if (onClose()) {
            getParent().remove(this);
        }
    }
    
    
    public void sendCommand(String commando, Object value) {
        if (commando.equalsIgnoreCase("TEXT") && value instanceof String) {
            String text=(String)value;
            
            searchText(text);
            oldSearch=text;
        }
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    
}
