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


import VisualLogic.*;
import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


class JavaElementConfigFilter extends javax.swing.filechooser.FileFilter
{
 static String fileExtension = "javaelementconfig";
 
  public boolean accept(File f) 
  {
    String s = f.getName();
    int i = s.lastIndexOf('.');
    
    if (f.isDirectory()) return true;

   if (i > 0) 
   {
     String extension = s.substring(i+1).toLowerCase();
     if (fileExtension.indexOf(extension)>-1) 
     {
        return true;
     } else 
     {
        return false;
     }
   }
  return false;
  }

 
    public String getDescription() 
    {
        return "(*.javaelementconfig)";
    }
}


class MyComboBoxRenderer extends JComboBox implements TableCellRenderer
{
    public MyComboBoxRenderer(String[] items)
    {
        super(items);
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column)
    {
        if (isSelected)
        {
            setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        }
        else
        {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        
        // Select the current value
        setSelectedItem(value);
        return this;
    }
}

class MyComboBoxEditor extends DefaultCellEditor
{
    public MyComboBoxEditor(String[] items)
    {
        super(new JComboBox(items));
    }
}


/**
 * * @author  Salafia
 */
public class DialogNewJavaComponentAssistent extends javax.swing.JDialog implements ElementPaletteIF
{
    private ElementPalette elementPalette;
    
    DefaultTableModel model1;
    DefaultTableModel model2;
    DefaultTableModel model3;
    DefaultTableModel model4;
    
    DefaultTableModel model5;
    
    FrameMain frameCircuit;
    
    /** Creates new form DialogNewJavaComponentAssistent */
    public DialogNewJavaComponentAssistent(FrameMain frameCircuit, boolean modal)
    {
        super(frameCircuit, modal);
        initComponents();   
        
        
        
        
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width-getWidth())/2, (screenSize.height-getHeight())/2);
        
        String[] values =VSDataType.getDataTypeList();
        
        
        setIconImage(frameCircuit.getIconImage());

        
        settings = new NewJavaCompSettings();
        
        model1 = (DefaultTableModel)jTable4.getModel();
        model2 = (DefaultTableModel)jTable5.getModel();
        model3 = (DefaultTableModel)jTable6.getModel();
        model4 = (DefaultTableModel)jTable7.getModel();
        
        model5 = (DefaultTableModel)jTable8.getModel();
        
        TableColumnModel modCol= jTable8.getColumnModel();
        
        TableColumn col1=modCol.getColumn(0);
        col1.setHeaderValue(java.util.ResourceBundle.getBundle("VisualLogic/DialogNewJavaComponentAssistent").getString("Name"));

        TableColumn col2=modCol.getColumn(1);
        col2.setHeaderValue(java.util.ResourceBundle.getBundle("VisualLogic/DialogNewJavaComponentAssistent").getString("DataType"));

        TableColumn col3=modCol.getColumn(2);
        col3.setHeaderValue(java.util.ResourceBundle.getBundle("VisualLogic/DialogNewJavaComponentAssistent").getString("Min(Only Numerik)"));

        TableColumn col4=modCol.getColumn(3);
        col4.setHeaderValue(java.util.ResourceBundle.getBundle("VisualLogic/DialogNewJavaComponentAssistent").getString("Max(Only Numerik)"));
        
        
        int vColIndex = 1;
        TableColumn col = jTable4.getColumnModel().getColumn(vColIndex);
        col.setCellEditor(new MyComboBoxEditor(values));
        col.setCellRenderer(new MyComboBoxRenderer(values));
        
        col = jTable5.getColumnModel().getColumn(vColIndex);
        col.setCellEditor(new MyComboBoxEditor(values));
        col.setCellRenderer(new MyComboBoxRenderer(values));
        
        col = jTable6.getColumnModel().getColumn(vColIndex);
        col.setCellEditor(new MyComboBoxEditor(values));
        col.setCellRenderer(new MyComboBoxRenderer(values));
        
        col = jTable7.getColumnModel().getColumn(vColIndex);
        col.setCellEditor(new MyComboBoxEditor(values));
        col.setCellRenderer(new MyComboBoxRenderer(values));
        
        
        col = jTable8.getColumnModel().getColumn(vColIndex);
        col.setCellEditor(new MyComboBoxEditor(values));
        col.setCellRenderer(new MyComboBoxRenderer(values));
        
        
        values =new String[]{"INPUT","OUTPUT"};
        vColIndex = 2;
        col = jTable4.getColumnModel().getColumn(vColIndex);
        col.setCellEditor(new MyComboBoxEditor(values));
        col.setCellRenderer(new MyComboBoxRenderer(values));
        
        col = jTable5.getColumnModel().getColumn(vColIndex);
        col.setCellEditor(new MyComboBoxEditor(values));
        col.setCellRenderer(new MyComboBoxRenderer(values));
        
        col = jTable6.getColumnModel().getColumn(vColIndex);
        col.setCellEditor(new MyComboBoxEditor(values));
        col.setCellRenderer(new MyComboBoxRenderer(values));
        
        col = jTable7.getColumnModel().getColumn(vColIndex);
        col.setCellEditor(new MyComboBoxEditor(values));
        col.setCellRenderer(new MyComboBoxRenderer(values));
        
        
        
        SpinnerNumberModel xmodel1 = new SpinnerNumberModel(0,0,50,1);
        jSpinner4.setModel(xmodel1);
        
        SpinnerNumberModel xmodel2 = new SpinnerNumberModel(0,0,50,1);
        jSpinner5.setModel(xmodel2);
        
        SpinnerNumberModel xmodel3 = new SpinnerNumberModel(0,0,50,1);
        jSpinner6.setModel(xmodel3);
        
        SpinnerNumberModel xmodel4 = new SpinnerNumberModel(0,0,50,1);
        jSpinner7.setModel(xmodel4);
        
        JSpinner.NumberEditor editor1 = new JSpinner.NumberEditor(jSpinner4);
        jSpinner4.setEditor(editor1);
        
        JSpinner.NumberEditor editor2 = new JSpinner.NumberEditor(jSpinner5);
        jSpinner5.setEditor(editor2);
        
        JSpinner.NumberEditor editor3 = new JSpinner.NumberEditor(jSpinner6);
        jSpinner6.setEditor(editor3);
        
        JSpinner.NumberEditor editor4 = new JSpinner.NumberEditor(jSpinner7);
        jSpinner7.setEditor(editor4);
        
        jSpinner4StateChanged(null);
        jSpinner5StateChanged(null);
        jSpinner6StateChanged(null);
        jSpinner7StateChanged(null);
        
        //jRadioButton1.setSelected(true);
        
        //String filename="c:\\test.config";
        //loadConfigFile(new File(filename));
        
        settings.circuitPanelName="CircuitElement";
        settings.frontPanelName="FrontElement";

        elementPalette = new ElementPalette(frameCircuit);
        elementPalette.setGruppenAuswahlMode(true);
        
        //elementPalette.aktuellesVerzeichniss="/CircuitElements/2user-defined/";
        
        jPanel4.add(elementPalette,java.awt.BorderLayout.CENTER);
        
        check();        
        
        
        loadSettings();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        txtName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jSpinner4 = new javax.swing.JSpinner();
        jCheckBox9 = new javax.swing.JCheckBox();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jSpinner5 = new javax.swing.JSpinner();
        jCheckBox10 = new javax.swing.JCheckBox();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jSpinner6 = new javax.swing.JSpinner();
        jCheckBox11 = new javax.swing.JCheckBox();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jSpinner7 = new javax.swing.JSpinner();
        jCheckBox12 = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable8 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("VisualLogic/DialogNewJavaComponentAssistent"); // NOI18N
        setTitle(bundle.getString("Create_Java_Component")); // NOI18N
        setResizable(false);

        jButton1.setText(bundle.getString("Cancel")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText(bundle.getString("generate_new_Java_Element")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Element Palette"));
        jPanel4.setLayout(new java.awt.BorderLayout());

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText(bundle.getString("Front_Component")); // NOI18N
        jRadioButton2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });
        jRadioButton2.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                jRadioButton2CaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText(bundle.getString("Circuit_Component")); // NOI18N
        jRadioButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButton1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioButton1StateChanged(evt);
            }
        });
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        txtName.setText("newElement1");

        jLabel5.setText(bundle.getString("Dest_Directory_:_")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jRadioButton1)
                        .addGap(11, 11, 11)
                        .addComponent(jRadioButton2)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab(bundle.getString("Location"), jPanel3); // NOI18N

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Rezize options"));

        jCheckBox3.setText(bundle.getString("CircuitElement_resizable")); // NOI18N
        jCheckBox3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox3.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jCheckBox4.setText(bundle.getString("FrontElement_resizable")); // NOI18N
        jCheckBox4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox4.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jCheckBox5.setText(bundle.getString("CircuitElement_Resize_with_Aspectration")); // NOI18N
        jCheckBox5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox5.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jCheckBox6.setText(bundle.getString("FrontElement_Resize_with_Aspectration")); // NOI18N
        jCheckBox6.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox6.setMargin(new java.awt.Insets(0, 0, 0, 0));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox3)
                    .addComponent(jCheckBox5)
                    .addComponent(jCheckBox6)
                    .addComponent(jCheckBox4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Pins"));

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Name", "Datatype", "I/O"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable4.setRowSelectionAllowed(false);
        jScrollPane4.setViewportView(jTable4);

        jLabel6.setText(bundle.getString("pins_:")); // NOI18N

        jSpinner4.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner4StateChanged(evt);
            }
        });

        jCheckBox9.setSelected(true);
        jCheckBox9.setText(bundle.getString("PinsVisible")); // NOI18N
        jCheckBox9.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox9.setMargin(new java.awt.Insets(0, 0, 0, 0));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox9)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("Top"), jPanel6); // NOI18N

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Name", "Datatype", "I/O"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable5.setRowSelectionAllowed(false);
        jScrollPane6.setViewportView(jTable5);

        jLabel7.setText(bundle.getString("pins_:")); // NOI18N

        jSpinner5.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner5StateChanged(evt);
            }
        });

        jCheckBox10.setSelected(true);
        jCheckBox10.setText(bundle.getString("PinsVisible")); // NOI18N
        jCheckBox10.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox10.setMargin(new java.awt.Insets(0, 0, 0, 0));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox10)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("Right"), jPanel11); // NOI18N

        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Name", "Datatype", "I/O"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable6.setRowSelectionAllowed(false);
        jScrollPane7.setViewportView(jTable6);

        jLabel8.setText(bundle.getString("pins_:")); // NOI18N

        jSpinner6.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner6StateChanged(evt);
            }
        });

        jCheckBox11.setSelected(true);
        jCheckBox11.setText(bundle.getString("PinsVisible")); // NOI18N
        jCheckBox11.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox11.setMargin(new java.awt.Insets(0, 0, 0, 0));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox11)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("Bottom"), jPanel12); // NOI18N

        jTable7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Name", "Datatype", "I/O"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable7.setRowSelectionAllowed(false);
        jScrollPane8.setViewportView(jTable7);

        jLabel9.setText(bundle.getString("pins_:")); // NOI18N

        jSpinner7.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner7StateChanged(evt);
            }
        });

        jCheckBox12.setSelected(true);
        jCheckBox12.setText(bundle.getString("PinsVisible")); // NOI18N
        jCheckBox12.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox12.setMargin(new java.awt.Insets(0, 0, 0, 0));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox12)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jSpinner7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("Left"), jPanel13); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Border"));

        jCheckBox1.setText(bundle.getString("show_InnerBoder")); // NOI18N
        jCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jCheckBox2.setText(bundle.getString("show_OuterBorder")); // NOI18N
        jCheckBox2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox2.setMargin(new java.awt.Insets(0, 0, 0, 0));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTextField1.setText("CircuitElement");

        jTextField2.setText("FrontElement");

        jLabel1.setText(bundle.getString("CircuitPanel_Element_Name_:_")); // NOI18N

        jLabel2.setText(bundle.getString("FrontPanel_Element_Name_:")); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane2.addTab(bundle.getString("Settings"), jPanel10); // NOI18N

        jTable8.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "itemName", "Datatype", "Min (only Numerik)", "Max (only Numerik)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable8.setRowSelectionAllowed(false);
        jScrollPane5.setViewportView(jTable8);

        jLabel3.setText(bundle.getString("Here_you_can_define_the_Properties_that_appear_in_the_Property-Editor")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab(bundle.getString("Properties"), jPanel1); // NOI18N

        jButton4.setText(bundle.getString("Save_Settings")); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton3.setText(bundle.getString("Load_Settings")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 111, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton4ActionPerformed
    {//GEN-HEADEREND:event_jButton4ActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        
        chooser.setDialogTitle(java.util.ResourceBundle.getBundle("VisualLogic/DialogNewJavaComponentAssistent").getString("Save_Settings"));
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        
        JavaElementConfigFilter filter= new JavaElementConfigFilter();
        
        chooser.setFileFilter(filter);
        
        int value = chooser.showSaveDialog(this);
        
        if (value == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String filename=file.getPath();
            
            String s = file.getName();
            int i = s.lastIndexOf('.');
            
            if (i > 0)
            {
            }
            else
            {
                filename+=".javaelementconfig";
            }
                        
            saveSettings();            
            saveConfiFile(new File(filename));                         
        }                
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton3ActionPerformed
    {//GEN-HEADEREND:event_jButton3ActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setDialogTitle(java.util.ResourceBundle.getBundle("VisualLogic/DialogNewJavaComponentAssistent").getString("Load_Settings"));              
        chooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter()
        {
            public boolean accept(File f)
            {
                if (f.isDirectory()) return true;
                return f.getName().toLowerCase().endsWith(".javaelementconfig");
            }
            public String getDescription()
            { return java.util.ResourceBundle.getBundle("VisualLogic/DialogNewJavaComponentAssistent").getString("JavaElement_Settings_Data_(javaelementconfig)"); }
        });
        
        
        int value = chooser.showOpenDialog(this);
        if (value == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            
            String filename=file.getAbsolutePath();
            
            loadConfigFile(new File(filename));
            loadSettings();
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jRadioButton2CaretPositionChanged(java.awt.event.InputMethodEvent evt)//GEN-FIRST:event_jRadioButton2CaretPositionChanged
    {//GEN-HEADEREND:event_jRadioButton2CaretPositionChanged
// TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton2CaretPositionChanged

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jRadioButton1ActionPerformed
    {//GEN-HEADEREND:event_jRadioButton1ActionPerformed
        check();
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton1StateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_jRadioButton1StateChanged
    {//GEN-HEADEREND:event_jRadioButton1StateChanged
        
    }//GEN-LAST:event_jRadioButton1StateChanged

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jRadioButton2ActionPerformed
    {//GEN-HEADEREND:event_jRadioButton2ActionPerformed
        check();
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    public void check()
    {
        if (jRadioButton1.isSelected())
        {
            elementPalette.aktuellesVerzeichniss="/CircuitElements/2user-defined/";
            elementPalette.init(this,null, FrameMain.elementPath,elementPalette.aktuellesVerzeichniss);
            
            jCheckBox4.setEnabled(false);
            jCheckBox6.setEnabled(false);
        }
        else
        {
            elementPalette.aktuellesVerzeichniss="/FrontElements/2user-defined/";
            elementPalette.init(this,null, FrameMain.elementPath,elementPalette.aktuellesVerzeichniss);
            
            jCheckBox4.setEnabled(true);
            jCheckBox6.setEnabled(true);
            
        }
    }
    
    
        
    private void jSpinner7StateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_jSpinner7StateChanged
    {//GEN-HEADEREND:event_jSpinner7StateChanged
        int value=(Integer)jSpinner7.getValue();
        model4.setRowCount(value);
    }//GEN-LAST:event_jSpinner7StateChanged
    
    private void jSpinner6StateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_jSpinner6StateChanged
    {//GEN-HEADEREND:event_jSpinner6StateChanged
        int value=(Integer)jSpinner6.getValue();
        model3.setRowCount(value);
    }//GEN-LAST:event_jSpinner6StateChanged
    
    private void jSpinner5StateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_jSpinner5StateChanged
    {//GEN-HEADEREND:event_jSpinner5StateChanged
        int value=(Integer)jSpinner5.getValue();
        model2.setRowCount(value);
    }//GEN-LAST:event_jSpinner5StateChanged
    
    private void jSpinner4StateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_jSpinner4StateChanged
    {//GEN-HEADEREND:event_jSpinner4StateChanged
        int value=(Integer)jSpinner4.getValue();
        model1.setRowCount(value);
    }//GEN-LAST:event_jSpinner4StateChanged
    
    
    private void loadSettings()
    {
        try
        {
            jTextField1.setText(settings.circuitPanelName);
            jTextField2.setText(settings.frontPanelName);
            
            jCheckBox3.setSelected(settings.circuitElementResizable);
            jCheckBox5.setSelected(settings.circuitElementResizableWithAspect);
            
            jCheckBox4.setSelected(settings.frontElementResizable);
            jCheckBox6.setSelected(settings.frontElementResizableWithAspect);
            
            //jRadioButton1.setSelected(settings.createCircuitAndFrontElements);
            //jRadioButton2.setSelected(settings.createOnyCircuitElement);
            //jRadioButton3.setSelected(settings.createOnyFrontElement);
            
            jCheckBox1.setSelected(settings.showInnerBorder);
            jCheckBox2.setSelected(settings.showOuterBorder);
            
            if (settings.pins.size()==4)
            {
                PinsSettings item1=settings.pins.get(0);
                jSpinner4.setValue(new Integer(item1.pinsCount));
                jCheckBox9.setSelected(item1.pinsVisible);
                loadItems(model1, item1);


                PinsSettings item2=settings.pins.get(1);
                jSpinner5.setValue(new Integer(item2.pinsCount));
                jCheckBox10.setSelected(item2.pinsVisible);
                loadItems(model2, item2);

                PinsSettings item3=settings.pins.get(2);
                jSpinner6.setValue(new Integer(item3.pinsCount));
                jCheckBox11.setSelected(item3.pinsVisible);
                loadItems(model3, item3);

                PinsSettings item4=settings.pins.get(3);
                jSpinner7.setValue(new Integer(item4.pinsCount));
                jCheckBox12.setSelected(item4.pinsVisible);
                loadItems(model4, item4);
            }
                        
            loadProperties(model5, settings.properties);
        }
        catch(Exception ex)
        {
            Tools.showMessage(this,java.util.ResourceBundle.getBundle("VisualLogic/DialogNewJavaComponentAssistent").getString("Error_loading_Configuration!"));
        }
    }
    
    
    private void saveSettings()
    {
        settings.circuitPanelName=jTextField1.getText();
        settings.frontPanelName=jTextField2.getText();
        
        settings.circuitElementResizable=jCheckBox3.isSelected();
        settings.circuitElementResizableWithAspect=jCheckBox5.isSelected();
        
        settings.frontElementResizable=jCheckBox4.isSelected();
        settings.frontElementResizableWithAspect=jCheckBox6.isSelected();
        
        //settings.createCircuitAndFrontElements=jRadioButton1.isSelected();
        //settings.createOnyCircuitElement=jRadioButton2.isSelected();
        //settings.createOnyFrontElement=jRadioButton3.isSelected();
        
        settings.showInnerBorder=jCheckBox1.isSelected();
        settings.showOuterBorder=jCheckBox2.isSelected();
        
        settings.pins.clear();
        
        PinsSettings item1=new PinsSettings();
        item1.pinsCount=(Integer)jSpinner4.getValue();
        item1.pinsVisible=jCheckBox9.isSelected();
        saveItems(model1, item1);
        settings.pins.add(item1);
        
        PinsSettings item2=new PinsSettings();
        item2.pinsCount=(Integer)jSpinner5.getValue();
        item2.pinsVisible=jCheckBox10.isSelected();
        saveItems(model2, item2);
        settings.pins.add(item2);
        
        PinsSettings item3=new PinsSettings();
        item3.pinsCount=(Integer)jSpinner6.getValue();
        item3.pinsVisible=jCheckBox11.isSelected();
        saveItems(model3, item3);
        settings.pins.add(item3);
        
        PinsSettings item4=new PinsSettings();
        item4.pinsCount=(Integer)jSpinner7.getValue();
        item4.pinsVisible=jCheckBox12.isSelected();
        saveItems(model4, item4);
        settings.pins.add(item4);
                        
        saveProperties(model5, settings.properties);
    }
    
    public int getIntFromDT(String dt)
    {
        String[] values=VSDataType.getDataTypeList();
        
        for (int i=0;i<values.length;i++)
        {
            if (dt.equalsIgnoreCase(values[i]))
            {
                return i;
            }
        }
        return -1;
    }
    
    private void saveItems(DefaultTableModel model, PinsSettings item)
    {
        item.items.clear();
        
        for (int row=0;row<model.getRowCount();row++)
        {
            String strName="";
            String strDT="C_VARIANT";
            int intMin=0;
            int intMax=0;
            String strIO="INPUT";
            int intDT=0;
            
            if (model.getValueAt(row, 0)!=null)
            {
                strName=(String)model.getValueAt(row, 0);
            }
            if (model.getValueAt(row, 1)!=null)
            {
                strDT=(String)model.getValueAt(row, 1);
                
                intDT=getIntFromDT(strDT);
                
            }
            if (model.getValueAt(row, 2)!=null)
            {
                strIO=(String)model.getValueAt(row, 2);
            }
            
            if (strIO.length()==0) strIO="INPUT";

            
            PinsSettingsItem newItem=new PinsSettingsItem();
            newItem.name=strName;
            newItem.dt=strDT;
            newItem.intDT=intDT;
            newItem.io=strIO;
            
            item.items.add(newItem);
        }
        
    }
    
    
    private void saveProperties(DefaultTableModel model, ArrayList<ElementPropertyX> items)
    {
        items.clear();
        
        for (int row=0;row<model.getRowCount();row++)
        {
            String strName="";
            String strDT="C_VARIANT";
            int intMin=0;
            int intMax=0;
            int intDT=-1;
            
            if (model.getValueAt(row, 0)!=null)
            {
                strName=(String)model.getValueAt(row, 0);
            }
            
            if (model.getValueAt(row, 1)!=null)
            {
                strDT=(String)model.getValueAt(row, 1);
                
                intDT=getIntFromDT(strDT);                
            }
            
            if (model.getValueAt(row, 2)!=null)
            {
                intMin=(Integer)model.getValueAt(row, 2);
            }
            
            if (model.getValueAt(row, 3)!=null)
            {
                intMax=(Integer)model.getValueAt(row, 3);
            }
                        
            ElementPropertyX newItem=new ElementPropertyX();
            newItem.name=strName;
            newItem.dt=strDT;
            newItem.intDT=intDT;            
            newItem.min=intMin;
            newItem.max=intMax;
            
            items.add(newItem);
        }
        
    }
    
    
    private void loadProperties(DefaultTableModel model, ArrayList<ElementPropertyX> items)
    {                
        model.setRowCount(0);
        model.setRowCount(50);
        
        for (int row=0;row<items.size();row++)
        {
            
            ElementPropertyX itemX = items.get(row);
            
            model.setValueAt(itemX.name,row,0);
            model.setValueAt(itemX.dt,row,1);
            model.setValueAt(itemX.min,row,2);
            model.setValueAt(itemX.max,row,3);            
        }
        
    }
    
    
    private void loadItems(DefaultTableModel model, PinsSettings item)
    {
        
        for (int row=0;row<item.items.size();row++)
        {            
            PinsSettingsItem itemX = item.items.get(row);
            
            model.setValueAt(itemX.name,row,0);
            model.setValueAt(itemX.dt,row,1);
            model.setValueAt(itemX.io,row,2);            
        }
        
    }    
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
    {//GEN-HEADEREND:event_jButton2ActionPerformed

        group=elementPalette.aktuellesVerzeichniss;
        compName=txtName.getText();
        
        if (jRadioButton1.isSelected())
        {
            type=0;
        }
        else
        {
            type=1;
        }
        
        if (compName.length()==0)
        {
            Tools.showMessage(this,java.util.ResourceBundle.getBundle("VisualLogic/Messages").getString("Name is recommended"));
            return;
        }
        
        String str=Tools.mapFile(FrameMain.elementPath+group+compName);
        
        if (new File(str).exists())
        {
            
            Tools.showMessage(this, java.util.ResourceBundle.getBundle("VisualLogic/DialogNewJavaComponentAssistent").getString("Directory_already_exists")+" : "+compName);
            return;
        }
                
        saveSettings();
        /*String filename="c:\\test.config";
        saveConfiFile(new File(filename));   */
        result=true;
        setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed
    
    //generateCircuitJavaFile("c:\\temp");
    
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed
        result=false;
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed
    
    
    
    public void loadConfigFile(File file)
    {
        try
        {
            if (file.exists())
            {
                ObjectInputStream ois= new ObjectInputStream(new FileInputStream(file.getAbsolutePath()));
                settings=(NewJavaCompSettings)ois.readObject();
                ois.close();
            }
            
        }
        catch(Exception ioe)
        {
            System.out.println(""+ioe.toString());
        }
    }
    
    
    public void saveConfiFile(File file)
    {
        
        try
        {
            
            ObjectOutputStream oos= new ObjectOutputStream(new FileOutputStream(file.getAbsolutePath()));
            
            oos.writeObject(settings);
            
            oos.flush();
            oos.close();
            
        }
        catch(Exception e)
        {
            System.out.println(""+e.toString());
        }
    }
    
    public void createVSObjects(BufferedWriter out, String ioX)
    {
        try
        {
            int c=0;
                                    
            for (int pc=0;pc<settings.pins.size();pc++)
            {
                PinsSettings ps=settings.pins.get(pc);
                
                for (int i=0;i<ps.items.size();i++)
                {
                    PinsSettingsItem item=ps.items.get(i);
                    if (item.io.equalsIgnoreCase(ioX))
                    {
                        String vsObject=VSDataType.getPinDataTypeWithDefaultType(item.intDT);
                        String vsType=VSDataType.getPinDataType(item.intDT);
                        
                        out.write("  "+vsType+" "+ioX+c+" = new "+vsObject+";");out.newLine();
                        c++;
                    }
                }
                
            }
        }
        catch(Exception ex)
        {
            Tools.showMessage(this,ex.toString());
        }
    }
    
    public void createSetPin(BufferedWriter out)
    {
        try
        {
            int c=0;
            for (int pc=0;pc<settings.pins.size();pc++)
            {
                PinsSettings ps=settings.pins.get(pc);
                
                for (int i=0;i<ps.items.size();i++)
                {
                    PinsSettingsItem item=ps.items.get(i);
                    
                    String strIO="INPUT";
                    
                    if (item.io.equalsIgnoreCase("INPUT"))
                    {
                        strIO="PIN_INPUT";
                    }
                    else
                    {
                        strIO="PIN_OUTPUT";
                    }
                    
                    out.write("    setPin("+c+", ExternalIF."+item.dt+", ExternalIF."+strIO+");");out.newLine();
                    c++;
                }
            }
            
            c=0;
            for (int pc=0;pc<settings.pins.size();pc++)
            {
                PinsSettings ps=settings.pins.get(pc);
                
                for (int i=0;i<ps.items.size();i++)
                {
                    PinsSettingsItem item=ps.items.get(i);
                    
                    out.write("    element.jSetPinDescription("+c+", \""+item.name+"\");");out.newLine();
                    c++;
                }
                
            }
            
            
        }
        catch(Exception ex)
        {
            Tools.showMessage(this,ex.toString());
        }
    }
    
    
    public void createVSObjectsForProperties(BufferedWriter out)
    {
        try
        {
            int c=0;
                        
            for (int i=0;i<settings.properties.size();i++)
            {
                ElementPropertyX item=settings.properties.get(i);
                String vsObject=VSDataType.getPinDataTypeWithDefaultType(item.intDT);
                String vsType=VSDataType.getPinDataType(item.intDT);

                if (item.name.length()>0)
                {
                  out.write("  "+vsType+" prop"+item.name+" = new "+vsObject+";");out.newLine();
                }
            }

        }
        catch(Exception ex)
        {
            Tools.showMessage(this,ex.toString());
        }
    }
    
    public void createsetPropertyEditorItems(BufferedWriter out)
    {
        try
        {
            for (int i=0;i<settings.properties.size();i++)
            {
                ElementPropertyX item=settings.properties.get(i);
                String vsObject=VSDataType.getPinDataTypeWithDefaultType(item.intDT);
                String vsType=VSDataType.getPinDataType(item.intDT);

                if (item.name.length()>0)
                {
                  //element.jAddPEItem("Farbe",col, 0,0);
                  out.write("    element.jAddPEItem(\""+item.name+"\", prop"+item.name+", "+item.min+", "+item.max+");");out.newLine();
                }
            }
        }
        catch(Exception ex)
        {
            Tools.showMessage(this,ex.toString());
        }
    }
    
    public void createsetPropertyEditorItemsLocalized(BufferedWriter out)
    {
        try
        {
            for (int i=0;i<settings.properties.size();i++)
            {
                ElementPropertyX item=settings.properties.get(i);

                if (item.name.length()>0)
                {                  
                  out.write("    element.jSetPEItemLocale(d+"+i+", language,\""+item.name+"\");");out.newLine();
                }
            }
        }
        catch(Exception ex)
        {
            Tools.showMessage(this,ex.toString());
        }
    }
    
    
    
    public void create_InitInputPins(BufferedWriter out)
    {
        try
        {
            int c=0;
            int cc=0;
            for (int pc=0;pc<settings.pins.size();pc++)
            {
                PinsSettings ps=settings.pins.get(pc);
                
                for (int i=0;i<ps.items.size();i++)
                {
                    PinsSettingsItem item=ps.items.get(i);
                    
                    if (item.io.equalsIgnoreCase("input"))
                    {
                        String vsType=VSDataType.getPinDataType(item.intDT);
                        out.write("    input"+cc+"= ("+vsType+")element.getPinInputReference("+c+");");out.newLine();
                        cc++;
                    }
                    c++;
                }
            }
            
        }
        catch(Exception ex)
        {
            Tools.showMessage(this,ex.toString());
        }
    }
    
    public void create_InitOutputPins(BufferedWriter out)
    {
        try
        {
            int c=0;
            int cc=0;
            for (int pc=0;pc<settings.pins.size();pc++)
            {
                PinsSettings ps=settings.pins.get(pc);
                
                for (int i=0;i<ps.items.size();i++)
                {
                    PinsSettingsItem item=ps.items.get(i);
                    
                    if (item.io.equalsIgnoreCase("output"))
                    {
                        String vsType=VSDataType.getPinDataType(item.intDT);
                        
                        out.write("    element.setPinOutputReference("+c+", output"+cc+");");out.newLine();
                        cc++;
                    }
                    c++;
                }
            }
            
        }
        catch(Exception ex)
        {
            Tools.showMessage(this,ex.toString());
        }
    }
    
    
    public void generateDefinitionDef(String destPath, int type)
    {
        try
        {
            String filename=destPath+"/"+"definition.def";
            BufferedWriter out = new BufferedWriter(new FileWriter(new File(filename)));
                        
            String elementName=txtName.getText();
            
            out.write("ISDIRECTORY  = FALSE");out.newLine(); 
            out.write("CAPTION      = DE_"+elementName);out.newLine();
            out.write("CAPTION_EN   = EN_"+elementName);out.newLine();
            out.write("CAPTION_ES   = ES_"+elementName);out.newLine();                        
            out.write("ClassCircuit = "+settings.circuitPanelName);out.newLine();
            if (type==1)
            {
                out.write("ClassFront   = "+settings.frontPanelName);out.newLine();
            }
            out.write("Icon         = icon.gif");out.newLine();
            
            out.close();
        }
        catch(Exception ex)
        {
            Tools.showMessage(this, ex.toString());
        }
        
    }
    
    // type = 0 fuer CircuitElement
    // type = 1 fuer PanelElement
    public void generateSrcFiles(String basisDestPath, String srcPath, int type)
    {
        generateCircuitJavaFile(basisDestPath, srcPath, type);
        
        if (type==1) // also FrontPanel
        {
            generateFrontJavaFile(basisDestPath,srcPath);
        }
        generateDefinitionDef(basisDestPath,type);
    }
    
    public void generateCircuitJavaFile(String basisDestPath, String srcPath, int type)
    {
        try
        {
            String filename=srcPath+"/"+settings.circuitPanelName+".java";
            BufferedWriter out = new BufferedWriter(new FileWriter(new File(filename)));
            
            out.write("//********************************");out.newLine();
            out.write("//* Autor :");out.newLine();
            out.write("//* Date  :");out.newLine();
            out.write("//* Email : ");out.newLine();
            out.write("//* licence : ");out.newLine();
            out.write("//********************************");out.newLine();
            
            out.write("");out.newLine();
            out.write("import VisualLogic.*;");out.newLine();
            out.write("import java.awt.*;");out.newLine();
            out.write("import java.awt.event.*;");out.newLine();
            out.write("import tools.*;");out.newLine();
            out.write("import VisualLogic.variables.*;");out.newLine();
            out.write("");out.newLine();
            
            out.write("public class "+settings.circuitPanelName+" extends JVSMain");out.newLine();
            out.write("{");out.newLine();
            
            if (type==1) // PanelElement
            {
                out.write("private ExternalIF panelElement;");out.newLine();
            }
                        
            out.write("  private Image image;");out.newLine();
            
            out.write("  // Properties");out.newLine();
            createVSObjectsForProperties(out);
            
            out.write("  // Inputs");out.newLine();
            out.write("");out.newLine();
            createVSObjects(out,"input");
            out.write("");out.newLine();
            
            out.write("  // Outputs");out.newLine();
            out.write("");out.newLine();
            createVSObjects(out,"output");
            out.write("");out.newLine();
            
            out.write("  public void onDispose()");out.newLine();
            out.write("  {");out.newLine();
            out.write("    if (image!=null) image.flush();");out.newLine();
            out.write("    image=null;");out.newLine();
            out.write("  }");out.newLine();
            out.newLine();
            
            out.write("  public void paint(java.awt.Graphics g)");out.newLine();
            out.write("  {");out.newLine();
            out.write("    if (image!=null) drawImageCentred(g,image);");out.newLine();
            out.write("  }");out.newLine();
            out.newLine();

            out.write("  public void setPropertyEditor()");out.newLine();
            out.write("  {");out.newLine();
            createsetPropertyEditorItems(out);
            out.write("  ");out.newLine();
            out.write("    localize();");out.newLine();
            out.write("  }");out.newLine();
            out.newLine();
                        
            out.write("  private void localize()");out.newLine();
            out.write("  {");out.newLine();
            out.write("    String language;");out.newLine();
            out.write("    int d=6;");out.newLine();
            out.write("    language=\"en_US\";");out.newLine();
            createsetPropertyEditorItemsLocalized(out);
            out.write("    ");out.newLine();
            out.write("    language=\"es_ES\";");out.newLine();            
            createsetPropertyEditorItemsLocalized(out);
            out.write("  }");out.newLine();
            out.newLine();
                        
            out.write("  public void xOnInit()");out.newLine();
            out.write("  {");out.newLine();
            
            if (type==1) // PanelElement
            {
                out.write("    panelElement=element.getPanelElement();");out.newLine();
            }
            out.write("  }");out.newLine();
            out.newLine();
            
            
            out.write("  public void init()");out.newLine();
            out.write("  {");out.newLine();

            int pinsTop=0;
            int pinsRight=0;
            int pinsBottom=0;
            int pinsLeft=0;

            boolean pinsTopVisible=false;
            boolean pinsRightVisible=false;
            boolean pinsBottomVisible=false;
            boolean pinsLeftVisible=false;
            
            if (settings.pins.size()==4)
            {
                pinsTop=settings.pins.get(0).pinsCount;
                pinsRight=settings.pins.get(1).pinsCount;
                pinsBottom=settings.pins.get(2).pinsCount;
                pinsLeft=settings.pins.get(3).pinsCount;

                pinsTopVisible=settings.pins.get(0).pinsVisible;
                pinsRightVisible=settings.pins.get(1).pinsVisible;
                pinsBottomVisible=settings.pins.get(2).pinsVisible;
                pinsLeftVisible=settings.pins.get(3).pinsVisible;

                out.write("    initPins("+pinsTop+","+pinsRight+","+pinsBottom+","+pinsLeft+");");out.newLine();
                out.write("    initPinVisibility("+pinsTopVisible+","+pinsRightVisible+","+pinsBottomVisible+","+pinsLeftVisible+");");out.newLine();
            } else
            {
                out.write("    initPins(0,0,0,0);");out.newLine();
            }
            int w=0;
            int h=0;
            if (pinsLeft>pinsRight)
            {
                h=pinsLeft*10;
            }else
            {
                h=pinsRight*10;
            }
            if (pinsTop>pinsBottom)
            {
                w=pinsTop*10;
            }else
            {
                w=pinsBottom*10;
            }
            out.write("    setSize(30+"+w+",30+"+h+");");out.newLine();            
            out.write("    image=element.jLoadImage(element.jGetSourcePath()+\"image.gif\");");out.newLine();
            
            out.write("");out.newLine();
            createSetPin(out);
            
            String strBol="false";
            
            strBol=""+settings.circuitElementResizable;
            out.write("");out.newLine();
            out.write("    element.jSetResizable("+strBol+");");out.newLine();
            strBol=""+settings.circuitElementResizableWithAspect;
            out.write("    element.jSetResizeSynchron("+strBol+");");out.newLine();
            
            out.write("  }");out.newLine();
            out.newLine();
            
            out.write("  public void initInputPins()");out.newLine();
            out.write("  {");out.newLine();
            create_InitInputPins(out);
            out.write("  }");out.newLine();
            out.newLine();
            
            out.write("  public void initOutputPins()");out.newLine();
            out.write("  {");out.newLine();
            create_InitOutputPins(out);
            out.write("  }");out.newLine();
            out.newLine();
            
            out.write("  public void start()");out.newLine();
            out.write("  {");out.newLine();
            out.write("  }");out.newLine();
            out.newLine();
            
            out.write("  public void stop()");out.newLine();
            out.write("  {");out.newLine();
            out.write("  }");out.newLine();
            out.newLine();
                        
            
            out.write("  public void process()");out.newLine();
            out.write("  {");out.newLine();
            out.write("  }");out.newLine();
            out.newLine();

            
            out.write("  public void elementActionPerformed(ElementActionEvent evt)");out.newLine();
            out.write("  {");out.newLine();
            out.write("    int idx=evt.getSourcePinIndex();");out.newLine();
            out.write("    switch (idx)");out.newLine();
            out.write("    {");out.newLine();
            out.write("      //case 0: DoSomething(); break;");out.newLine();
            out.write("    }");out.newLine();
            out.write("  }");out.newLine();
            out.newLine();

            out.write("  public void loadFromStream(java.io.FileInputStream fis)");out.newLine();
            out.write("  {");out.newLine();
            out.write("  //xxx.loadFromStream(fis);");out.newLine();
            out.write("  }");out.newLine();
            out.newLine();
            
            out.write("  public void saveToStream(java.io.FileOutputStream fos)");out.newLine();
            out.write("  {");out.newLine();
            out.write("  //xxx.saveToStream(fos);");out.newLine();
            out.write("  }");out.newLine();            
            out.newLine();
                        
            out.write("}");out.newLine();
            out.close();
        }
        catch(Exception ex)
        {
            Tools.showMessage(this, ex.toString());
        }
        
    }
    
    public void generateFrontJavaFile(String basisDestPath, String srcPath)
    {
        
        try
        {
            String filename=srcPath+"/"+settings.frontPanelName+".java";
            BufferedWriter out = new BufferedWriter(new FileWriter(new File(filename)));
            
            out.write("import VisualLogic.*;");out.newLine();
            out.write("import java.awt.*;");out.newLine();
            out.write("import java.awt.event.*;");out.newLine();
            out.write("import tools.*;");out.newLine();
            out.write("import VisualLogic.variables.*;");out.newLine();
            out.write("");out.newLine();
            
            out.write("public class "+settings.frontPanelName+" extends JVSMain implements PanelIF ");out.newLine();
            out.write("{");out.newLine();
            
            out.write("private ExternalIF circuitElement;");out.newLine();
            
            out.write("  private Image image;");out.newLine();
                        
            out.write("  // Inputs");out.newLine();
            out.write("");out.newLine();
            createVSObjects(out,"input");
            out.write("");out.newLine();
            
            out.write("  // Outputs");out.newLine();
            out.write("");out.newLine();
            createVSObjects(out,"output");
            out.write("");out.newLine();
           
            
            out.write("  public void processPanel(int pinIndex, double value, Object obj)");out.newLine();
            out.write("  {");out.newLine();
            out.write("    //if (value==0.0) setOn(false); else setOn(true);");out.newLine();
            out.write("  }");out.newLine();
            
            out.write("  public void onDispose() {}");out.newLine();
            
            out.write("  public void paint(java.awt.Graphics g) {}");out.newLine();
            
            out.write("  public void xOnInit()");out.newLine();
            out.write("  {");out.newLine();
            
            out.write("    circuitElement=element.getCircuitElement();");out.newLine();
            out.write("  }");out.newLine();
            
            
            out.write("  public void init()");out.newLine();
            out.write("  {");out.newLine();
            out.write("    initPins(0,0,0,0);");out.newLine();
            out.write("    setSize(50,50);");out.newLine();
            out.write("    initPinVisibility(false,false,false,false);");out.newLine();
            out.write("");out.newLine();
            
            String strBol="false";
            
            strBol=""+settings.circuitElementResizable;
            out.write("");out.newLine();
            out.write("    element.jSetResizable("+strBol+");");out.newLine();
            strBol=""+settings.circuitElementResizableWithAspect;
            out.write("    element.jSetResizeSynchron("+strBol+");");out.newLine();
            out.write("  }");out.newLine();
            
            
            
            out.write("  public void loadFromStream(java.io.FileInputStream fis)");out.newLine();
            out.write("  {");out.newLine();
            out.write("  //xxx.loadFromStream(fis);");out.newLine();
            out.write("  }");out.newLine();
            
            out.write("  public void saveToStream(java.io.FileOutputStream fos)");out.newLine();
            out.write("  {");out.newLine();
            out.write("  //xxx.saveToStream(fos);");out.newLine();
            out.write("  }");out.newLine();
            
            out.write("  public void process()");out.newLine();
            out.write("  {");out.newLine();
            out.write("  }");out.newLine();
            
            out.write("}");out.newLine();
            out.close();
        }
        catch(Exception ex)
        {
            Tools.showMessage(this, ex.toString());
        }
        
    }

    public void onButtonClicken(String[] params)
    {
    }
    
    
    
    public  NewJavaCompSettings settings ;
           
    public static boolean result=false;
    public static String group="";
    public static String compName="";
    public static int type=0;
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox10;
    private javax.swing.JCheckBox jCheckBox11;
    private javax.swing.JCheckBox jCheckBox12;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JSpinner jSpinner5;
    private javax.swing.JSpinner jSpinner6;
    private javax.swing.JSpinner jSpinner7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTable jTable7;
    private javax.swing.JTable jTable8;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
    
}




