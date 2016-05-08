//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia (cswi@gmx.de)                         *
//*                                                                           *
//* This library is free software; you can redistribute it and/or modify      *
//* it under the terms of the GNU Lesser General Public License as published  *
//* by the Free Software Foundation; either version 2.1 of the License,       *
//* or (at your option) any later version.                                    *
//* http://www.gnu.org/licenses/lgpl.html                                     *
//*                                                                           *
//* This library is distributed in the hope that it will be useful,           *
//* but WITHOUTANY WARRANTY; without even the implied warranty of             *
//* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                      *
//* See the GNU Lesser General Public License for more details.               *
//*                                                                           *
//* You should have received a copy of the GNU Lesser General Public License  *
//* along with this library; if not, write to the Free Software Foundation,   *
//* Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA                  *
//*****************************************************************************


import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;



class BooleanModel extends javax.swing.table.DefaultTableModel
{
    public Class getColumnClass(int columnIndex) 
    {
        return java.lang.Boolean.class;
    }
}
        

/**
 *
 * @author  Carmelo
 */
public class MyTableEditor extends javax.swing.JDialog
{
    
    private DefaultTableModel modelInputs = new BooleanModel();
    private DefaultTableModel modelOutputs = new BooleanModel();
        
    /** Creates new form MyTableEditor2 */
    public MyTableEditor(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        jTable2.setModel(modelInputs);
        jTable3.setModel(modelOutputs);
                        
    }
    
    private void clearTable(DefaultTableModel model)
    {
        while(model.getRowCount()>0)
        {
            model.removeRow(0);    
        }   
        model.setRowCount(0);
    }
    
    private Boolean[] parseString(String str)
    {
        Boolean data[] = new Boolean[str.length()];
        String ch;
        Boolean val;
        for (int i=0;i<str.length();i++)
        {
            ch=str.substring(i,i+1);
            if (ch.equalsIgnoreCase("0"))
            {
               val=new Boolean(false);
            }else    
            {
                val=new Boolean(true);
            }
            
            data[i]=val; 
        }
        return data;
    }
    
    public void setInputs(String [] inputs)
    {
        clearTable(modelInputs);
        
                       
        String str;
        for (int j=0;j<inputs.length;j++)
        {
            str=inputs[j];
            Boolean[] data=parseString(str);
            modelInputs.addRow(data);
        }
    }

    public void setInputColumns(String cols)
    {
        String[] c=cols.split(",");
        
        modelInputs.setColumnCount(0);
        for (int i=0;i<c.length;i++)
        {
            modelInputs.addColumn(c[i]);
        }
        TableColumn col;
        
        int w=40;
        for (int j=0;j<modelInputs.getColumnCount();j++)
        {
            col = jTable2.getColumnModel().getColumn(j);
            col.setPreferredWidth(w);
        }
        
    }
    

    
    public String getInputColumns()
    {
        String result="";
        for (int i=0;i<modelInputs.getColumnCount()-1;i++)
        {
            result+=modelInputs.getColumnName(i)+",";
        }
        result+=modelInputs.getColumnName(modelInputs.getColumnCount()-1);
        
        return result;
    }
    
    public String getOutputColumns()
    {
        String result="";
        for (int i=0;i<modelOutputs.getColumnCount()-1;i++)
        {
            result+=modelOutputs.getColumnName(i)+",";
        }
        result+=modelOutputs.getColumnName(modelOutputs.getColumnCount()-1);
        
        return result;
    }    
    public void setOutputColumns(String cols)
    {
        String[] c=cols.split(",");
                
        modelOutputs.setColumnCount(0);
        for (int i=0;i<c.length;i++)
        {
            modelOutputs.addColumn(c[i]);
        }
        TableColumn col;
        
        int w=40;
        for (int j=0;j<modelOutputs.getColumnCount();j++)
        {
            col = jTable3.getColumnModel().getColumn(j);
            col.setPreferredWidth(w);
        }
        
    }
    
    public void setOutputs(String[] outputs)
    {
        clearTable(modelOutputs);
        
        String str;
        for (int j=0;j<outputs.length;j++)
        {
            str=outputs[j];
            Boolean[] data=parseString(str);
            modelOutputs.addRow(data);
        }
        
    }
    
    private String[] getData(DefaultTableModel model)
    {
        String [] result = new String[model.getRowCount()];
        String ch;
        for (int i=0;i<model.getRowCount();i++)
        {
            String str="";
            
            for (int k=0;k<model.getColumnCount();k++)
            {
                Object o=model.getValueAt(i,k);
                
                if (o instanceof Boolean)
                {
                    Boolean val = (Boolean)o;
                    if (val==true)
                    {
                        ch="1";
                    }else
                    {
                        ch="0";
                    }
                    str+=ch;
                } else
                {
                    str+="0";
                }
            }
            
           result[i]= new String(str);                      
            
        }        
        
        return result;        
    }
        
    public String[] getInputs()
    {
        return getData(modelInputs);
    }
    
    public String[] geOutputs()
    {
        return getData(modelOutputs);
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jToolBar1 = new javax.swing.JToolBar();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jMenuBar2 = new javax.swing.JMenuBar();
        mnuInputs = new javax.swing.JMenu();
        mniEditInputColumns = new javax.swing.JMenuItem();
        mniEraseInData = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        mniGenInputData = new javax.swing.JMenuItem();
        mnuOutputs = new javax.swing.JMenu();
        mniEditOutputColumns = new javax.swing.JMenuItem();
        mniEraseOutputData = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Data editor");
        jToolBar1.setFloatable(false);
        getContentPane().add(jToolBar1, java.awt.BorderLayout.NORTH);

        jSplitPane1.setDividerLocation(250);
        jSplitPane1.setContinuousLayout(true);
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable2.setCellSelectionEnabled(true);
        jTable2.setSelectionBackground(new java.awt.Color(204, 204, 204));
        jScrollPane2.setViewportView(jTable2);

        jSplitPane1.setLeftComponent(jScrollPane2);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable3.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable3.setCellSelectionEnabled(true);
        jTable3.setSelectionBackground(new java.awt.Color(204, 204, 204));
        jScrollPane4.setViewportView(jTable3);

        jSplitPane1.setRightComponent(jScrollPane4);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.add(jButton1);

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel1.add(jButton2);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        mnuInputs.setText("Inputs");
        mniEditInputColumns.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edit.gif")));
        mniEditInputColumns.setText("Edit Columns");
        mniEditInputColumns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniEditInputColumnsActionPerformed(evt);
            }
        });

        mnuInputs.add(mniEditInputColumns);

        mniEraseInData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erase.png")));
        mniEraseInData.setText("Erase Data");
        mniEraseInData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniEraseInDataActionPerformed(evt);
            }
        });

        mnuInputs.add(mniEraseInData);

        mnuInputs.add(jSeparator1);

        mniGenInputData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon_generate.png")));
        mniGenInputData.setText("Generate Input Binary data");
        mniGenInputData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniGenInputDataActionPerformed(evt);
            }
        });

        mnuInputs.add(mniGenInputData);

        jMenuBar2.add(mnuInputs);

        mnuOutputs.setText("Outputs");
        mniEditOutputColumns.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edit.gif")));
        mniEditOutputColumns.setText("Edit Columns");
        mniEditOutputColumns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniEditOutputColumnsActionPerformed(evt);
            }
        });

        mnuOutputs.add(mniEditOutputColumns);

        mniEraseOutputData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erase.png")));
        mniEraseOutputData.setText("Erase  Data");
        mniEraseOutputData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniEraseOutputDataActionPerformed(evt);
            }
        });

        mnuOutputs.add(mniEraseOutputData);

        jMenuBar2.add(mnuOutputs);

        setJMenuBar(jMenuBar2);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-590)/2, (screenSize.height-365)/2, 590, 365);
    }// </editor-fold>//GEN-END:initComponents

    private void mniEraseOutputDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniEraseOutputDataActionPerformed
      int ok = JOptionPane.showConfirmDialog(this,"do you really wan't to delete output data ?", 
                                                  "Delete output data", 
                                                  JOptionPane.YES_NO_OPTION);
       if (ok == JOptionPane.YES_OPTION)                              
       {        
           eraseData(modelOutputs);
       }
    }//GEN-LAST:event_mniEraseOutputDataActionPerformed

    private void mniEditOutputColumnsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniEditOutputColumnsActionPerformed
       String cols=test(jTable3,modelOutputs);
       
       if ((cols != null) && (cols.length() > 0) && (cols.length()<=255))
       {
            String[] values=geOutputs();
            setOutputColumns(cols);
            setOutputs(values);
       }     
    }//GEN-LAST:event_mniEditOutputColumnsActionPerformed

    private void mniEraseInDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniEraseInDataActionPerformed
      int ok = JOptionPane.showConfirmDialog(this,"do you really wan't to delete input data ?", 
                                                  "Delete input data", 
                                                  JOptionPane.YES_NO_OPTION);
       if (ok == JOptionPane.YES_OPTION)                              
       {        
           eraseData(modelInputs);
       }
    }//GEN-LAST:event_mniEraseInDataActionPerformed

    private void mniEditInputColumnsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniEditInputColumnsActionPerformed

       String cols=test(jTable2,modelInputs);
       
       if ((cols != null) && (cols.length() > 0) && (cols.length()<=255)) 
       {
            String[] values=getInputs();
            setInputColumns(cols);
            setInputs(values);
            
            int x=(int)Math.pow(2,jTable2.getColumnCount());
            int y=(int)Math.pow(2,jTable3.getColumnCount());
            
            if (x>jTable2.getRowCount())
            {
                int diff=x-jTable2.getRowCount();
                
                for (int i=0;i<diff;i++)
                {
                    Boolean[] data=new Boolean[jTable2.getColumnCount()];
                    modelInputs.addRow(data);                    
                }                
                
                diff=x-jTable3.getRowCount();
                
                for (int i=0;i<diff;i++)
                {
                    Boolean[] data=new Boolean[jTable2.getColumnCount()];
                    modelOutputs.addRow(data);
                }                
                
            }
            
            if (x<jTable2.getRowCount())
            {
                int diff=jTable2.getRowCount()-x;
                
                for (int i=0;i<diff;i++)
                {                    
                    modelInputs.removeRow(jTable2.getRowCount()-1-i);                    
                }
                
                diff=jTable3.getRowCount()-x;
                
                for (int i=0;i<diff;i++)
                {                                        
                    modelOutputs.removeRow(jTable3.getRowCount()-1-i);
                }
                
            }
        
   
       }     

    }//GEN-LAST:event_mniEditInputColumnsActionPerformed

    private void mniGenInputDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniGenInputDataActionPerformed

       int ok = JOptionPane.showConfirmDialog(this,"do you really wan't to override \nand generate Input data ?", 
                                                  "Override input data", 
                                                  JOptionPane.YES_NO_OPTION);
       if (ok == JOptionPane.YES_OPTION)                              
       {            
            int digits=modelInputs.getColumnCount();
            int x=(int)Math.pow(2,digits);

            String[] values= new String[x];
            for (int i=0;i<x;i++)
            {
                //System.out.println(""+IntToBin(i,digits));
                values[i]=IntToBin(i,digits);
            }

            setInputs(values);
       }
        

    }//GEN-LAST:event_mniGenInputDataActionPerformed

    
    public void eraseData(DefaultTableModel model)
    {
           for (int col=0;col<model.getColumnCount();col++) 
           for (int row=0;row<model.getRowCount();row++)
           {
                model.setValueAt(new Boolean(false),row,col);
           }        
    }
    
    private String test(JTable table,DefaultTableModel model)
    {
        TableColumn col;
        String str="";
        
        int colCount=model.getColumnCount();
        if (colCount>0)
        {
            for (int j=0;j<colCount-1;j++)
            {            
                str+=table.getColumnName(j)+",";
            }
            str+=table.getColumnName(colCount-1);
        }
        
        
        String cols = (String)JOptionPane.showInputDialog(
                        this,
                        "Columns : ",
                        "Edit Columns",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        str);

                        
        return cols;
    }
    
    public void shiftRight(String [] inputs)
    {
        String str;
        for (int j=0;j<inputs.length;j++)
        {
           str=inputs[j];        
           inputs[j]="0"+str;
        }
    }
    
    


    
    
 private String IntToBin(int value, int digits) 
 { 
    String result="";
    for(int i=digits-1;i>=0;i--) 
    { 
      if(((value>>i)&1)!=0) result+="1";
      else result+="0";
    } 
    
    return result;
  } 
    
    
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        result=false;
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        resultInputs=getInputs();
        resultOutputs=geOutputs();
        resultInputColumns=getInputColumns();
        resultOutputColumns=getOutputColumns();
        inputColumnsCount=modelInputs.getColumnCount();
        outputColumnsCount=modelOutputs.getColumnCount();
        result=true;
        dispose();
        /*String[] inp=getInputs();
        
        System.out.println(getInputColumns());
        for (int i=0;i<inp.length;i++) {
            System.out.println(inp[i]);
        }*/
    }//GEN-LAST:event_jButton1ActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                MyTableEditor frm = new MyTableEditor(null,true);
                
                String col1 = "A,B";
                String col2 = "S1";
                
                frm.setInputColumns(col1);
                frm.setOutputColumns(col2);
                
                String[] inputs= new String[4];
                inputs[0] = new String("00");
                inputs[1] = new String("01");
                inputs[2] = new String("10");
                inputs[3] = new String("11");
                
                String[] outputs= new String[4];
                outputs[0] = new String("1");
                outputs[1] = new String("0");
                outputs[2] = new String("0");
                outputs[3] = new String("1");
                
                frm.setInputs(inputs);
                frm.setOutputs(outputs);
                frm.setVisible(true);
            }
        });
    }
    
    public static String[] resultInputs=null;
    public static String[] resultOutputs=null;        
    public static String resultInputColumns="";
    public static String resultOutputColumns="";    
    public static int inputColumnsCount=0;
    public static int outputColumnsCount=0;
    
    public static boolean result=false;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JMenuItem mniEditInputColumns;
    private javax.swing.JMenuItem mniEditOutputColumns;
    private javax.swing.JMenuItem mniEraseInData;
    private javax.swing.JMenuItem mniEraseOutputData;
    private javax.swing.JMenuItem mniGenInputData;
    private javax.swing.JMenu mnuInputs;
    private javax.swing.JMenu mnuOutputs;
    // End of variables declaration//GEN-END:variables
    
}
