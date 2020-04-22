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
import javax.swing.table.DefaultTableModel;



class ByteModel extends javax.swing.table.DefaultTableModel
{
    public Class getColumnClass(int columnIndex) 
    {
        return java.lang.Integer.class;
    }
}
        

/**
 *
 * @author  Carmelo
 */
public class MyTableEditor extends javax.swing.JDialog {
    
    private DefaultTableModel model = new ByteModel();
    
        
    /** Creates new form MyTableEditor2 */
    public MyTableEditor(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        jTable2.setModel(model);        
                        
    }
    
    private void clearTable(DefaultTableModel model)
    {
        model.setColumnCount(0);
        model.setRowCount(0);

        /*while(model.getRowCount()>0)
        {
            model.removeRow(0);    
        } */               
    }
    
     /**
     * Returns a long that contains the same n bits as the given long,with cleared upper rest.
     * @param value the value which lowest bits should be copied.
     * @param bits the number of lowest bits that should be copied.
     * @return a long value that shares the same low bits as the given value.
     */
     private static long copyBits(final long value, byte bits)
     {
       final boolean logging = false; //turn off logging here
       long converted = 0;
       long comp =1L << bits;
       while (--bits != -1)
       {
         if(((comp >>= 1) & value) != 0)
         {
            converted |= comp;
         }
         if(logging){System.out.print((comp & value)!=0?"1":"0");}
       }
         if(logging){System.out.println();
       }
        return converted;
    }

    /**
    * Converts an unsigned byte to a signed short.
    * @param value an unsigned byte value
    * @return a signed short that represents the unsigned byte's value.
    */
    public static short toSigned(final byte value)
    {
      return (short)copyBits(value, (byte)8);
    }
      
    public void setInputs(byte[] inputs)
    {
        clearTable(model);
        
        model.addColumn("vector");
        
        for (int i=0;i<inputs.length;i++)
        {          
          Integer[] data = new Integer[1];
          data[0]=new Integer(toSigned(inputs[i]));
          model.addRow(data);
        }
        
        jLabel1.setText("Rows="+inputs.length);
    }

    public void setInputColumns(String cols)
    {
        String[] c=cols.split(",");
        
        for (int i=0;i<c.length;i++)
        {
            model.addColumn(c[i]);
        }
    }
    
    public String getInputColumns()
    {
        String result="";
        for (int i=0;i<model.getColumnCount()-1;i++)
        {
            result+=model.getColumnName(i)+",";
        }
        result+=model.getColumnName(model.getColumnCount()-1);
        
        return result;
    }
    
    public static byte toUnsigned(final short value)
    {
       return (byte) (0xFF&value);
    }
    
    
    private byte[] getData(DefaultTableModel model)
    {
        byte[] result = new byte[model.getRowCount()];
        String ch;
        for (int i=0;i<model.getRowCount();i++)
        {            
             Integer o=(Integer)model.getValueAt(i,0);
             result[i]= toUnsigned((short)o.intValue());
             
        }        
        
        return result;        
    }
        
    public byte[] getInputs()
    {
        return getData(model);
    }
    
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jmiNew = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Vector");
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.add(jButton1);

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel1.add(jButton2);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null},
                {null},
                {null},
                {null}
            },
            new String []
            {
                "Title 1"
            }
        ));
        jTable2.setRowSelectionAllowed(false);
        jScrollPane2.setViewportView(jTable2);

        getContentPane().add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setText("Rows");
        jPanel2.add(jLabel1);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jMenu1.setText("Vector");
        jmiNew.setText("New");
        jmiNew.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmiNewActionPerformed(evt);
            }
        });

        jMenu1.add(jmiNew);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-198)/2, (screenSize.height-327)/2, 198, 327);
    }// </editor-fold>//GEN-END:initComponents

    private void jmiNewActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmiNewActionPerformed
    {//GEN-HEADEREND:event_jmiNewActionPerformed
        DialogNew frm = new DialogNew(null,true);
        
        frm.setVisible(true);
        
        if (frm.result)
        {                
            int n=frm.resN;
                        
            byte[] vector = new byte[n];
            
            for (int i=0;i<n;i++)
            {
                vector[i]=0;
            }

            setInputs(vector);
            
        }
    }//GEN-LAST:event_jmiNewActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed
        resultInputs=getInputs();

        resN=model.getColumnCount();
        resM=model.getRowCount();
        result=true;
        dispose();
        /*String[] inp=getInputs();
         
        System.out.println(getInputColumns());
        for (int i=0;i<inp.length;i++) {
            System.out.println(inp[i]);
        }*/
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
    {//GEN-HEADEREND:event_jButton2ActionPerformed
        result=false;
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                MyTableEditor frm = new MyTableEditor(null,true);
                
                String col1 = "Car,melo,ist,der";                
                
                //frm.setInputColumns(col1);
                
                byte[] vector = new byte[3];
                
                for (int i=0;i<3;i++)                
                {
                    vector[i]=(byte)i;
                }
                
                frm.setInputs(vector);
                frm.setVisible(true);  
                
                
                byte[] data=frm.getInputs();
 
            }
        });
    }
    
    public static byte[] resultInputs=null;    
    public static boolean result=false;
    public static int resN=0;
    public static int resM=0;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JMenuItem jmiNew;
    // End of variables declaration//GEN-END:variables
    
}
