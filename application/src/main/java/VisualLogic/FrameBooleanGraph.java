/*
 * Copyright (C) 2020 MyLibreLab
 * Based on MyOpenLab by Carmelo Salafia www.myopenlab.de
 * Copyright (C) 2004  Carmelo Salafia cswi@gmx.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package VisualLogic;

import java.awt.Color;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

class TheGraphBoolean {
    public double[] yValues;
    public String name = "";
    public double value = 0;

    public int yPos = 0;

    public TheGraphBoolean(int bufflen) {
        yValues = new double[bufflen];
    }
}


/**
 * @author Carmelo
 */
public class FrameBooleanGraph extends javax.swing.JFrame {
    private final Basis basis;
    private final int faktor = 20;
    private final int distance = 5;
    private TheGraphBoolean[] graphs;
    public double[] xValues;
    public int abtastFreq = 10;
    public int refreshFreq = 250;
    private int internalC = 0;
    private int refreshC = 0;
    private boolean dontRefresh = false;
    private int bufflen = 500;
    private int newbufflen = bufflen;

    /**
     * Creates new form FrameBooleanGraph
     */
    public FrameBooleanGraph(Basis basis) {
        initComponents();
        this.basis = basis;

        SpinnerNumberModel model1 = new SpinnerNumberModel(abtastFreq, 1, 5000, 1);
        jSpinner1.setModel(model1);

        SpinnerNumberModel model2 = new SpinnerNumberModel(refreshFreq, 1, 5000, 1);
        jSpinner2.setModel(model2);

        SpinnerNumberModel model3 = new SpinnerNumberModel(bufflen, 100, 5000, 10);
        jSpinner3.setModel(model3);

        JSpinner.NumberEditor editor1 = new JSpinner.NumberEditor(jSpinner1);
        jSpinner1.setEditor(editor1);

        JSpinner.NumberEditor editor2 = new JSpinner.NumberEditor(jSpinner2);
        jSpinner2.setEditor(editor2);

        JSpinner.NumberEditor editor3 = new JSpinner.NumberEditor(jSpinner3);
        jSpinner3.setEditor(editor3);

        // setIconImage(basis.getFrameMain().iconImage);
        javax.swing.ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/Bilder/graphBoolean16x16.gif"));
        setIconImage(icon.getImage());
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify
     * this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        myGraph1 = new MyGraph.MyGraph();
        jPanel1 = new javax.swing.JPanel();
        jSpinner1 = new javax.swing.JSpinner();
        jSpinner2 = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jSpinner3 = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("VisualLogic/FrameBooleanGraph"); // NOI18N
        setTitle(bundle.getString("Digital_Graph_Window")); // NOI18N
        setAlwaysOnTop(true);
        setLocationByPlatform(true);

        myGraph1.setXAxisText("");
        myGraph1.setYAxisText("");
        myGraph1.setAutoScroll(java.lang.Boolean.TRUE);
        myGraph1.setCoordinatesVisible(java.lang.Boolean.FALSE);
        myGraph1.setMaxX(new java.lang.Double(500.0));
        myGraph1.setMaxY(new java.lang.Double(100.0));
        myGraph1.setMinX(new java.lang.Double(-10.0));
        myGraph1.setMinY(new java.lang.Double(-10.0));
        myGraph1.setNullLineVisible(java.lang.Boolean.FALSE);

        jSpinner1.setPreferredSize(new java.awt.Dimension(25, 20));
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });

        jSpinner2.setPreferredSize(new java.awt.Dimension(25, 20));
        jSpinner2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner2StateChanged(evt);
            }
        });

        jLabel1.setText(bundle.getString("Sample_Freq.")); // NOI18N

        jLabel2.setText(bundle.getString("Refresh_Freq.")); // NOI18N

        jButton1.setText(bundle.getString("Reset")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jSpinner3.setPreferredSize(new java.awt.Dimension(25, 20));
        jSpinner3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner3StateChanged(evt);
            }
        });

        jLabel3.setText("Buffer");

        jCheckBox1.setText("Trigger");
        jCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jSpinner1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65,
                                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jSpinner2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 66,
                                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jSpinner3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72,
                                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jCheckBox1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jButton1)
                        .addContainerGap(24, Short.MAX_VALUE)));
        jPanel1Layout
                .setVerticalGroup(
                        jPanel1Layout
                                .createParallelGroup(
                                        org.jdesktop.layout.GroupLayout.LEADING)
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jLabel1)
                                        .add(jSpinner1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(jLabel2)
                                        .add(jSpinner2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(jLabel3)
                                        .add(jSpinner3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(jCheckBox1).add(jButton1)));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(myGraph1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup().add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(myGraph1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jSpinner3StateChanged(javax.swing.event.ChangeEvent evt)// GEN-FIRST:event_jSpinner3StateChanged
    {// GEN-HEADEREND:event_jSpinner3StateChanged

        newbufflen = (Integer) jSpinner3.getValue();
    }// GEN-LAST:event_jSpinner3StateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_jButton1ActionPerformed
    {// GEN-HEADEREND:event_jButton1ActionPerformed
        init();
    }// GEN-LAST:event_jButton1ActionPerformed

    private void jSpinner2StateChanged(javax.swing.event.ChangeEvent evt)// GEN-FIRST:event_jSpinner2StateChanged
    {// GEN-HEADEREND:event_jSpinner2StateChanged
        refreshFreq = (Integer) jSpinner2.getValue();
    }// GEN-LAST:event_jSpinner2StateChanged

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt)// GEN-FIRST:event_jSpinner1StateChanged
    {// GEN-HEADEREND:event_jSpinner1StateChanged
        abtastFreq = (Integer) jSpinner1.getValue();
    }// GEN-LAST:event_jSpinner1StateChanged

    // Returns the Columns Index
    // else -1
    private int getColumnIndex(String columnTitle) {
        for (int i = 0; i < graphs.length; i++) {
            if (graphs[i].name.equalsIgnoreCase(columnTitle)) {
                return i;
            }
        }
        return -1;
    }

    private Element[] tpNodes = null;

    public void init() {
        if (dontRefresh) return;

        tpNodes = basis.getCircuitBasis().getAllTestpointElementsBoolean();
        myGraph1.graph.generateGraphs(tpNodes.length);
        counter = 0;
        xValues = new double[bufflen];
        graphs = new TheGraphBoolean[tpNodes.length];
        int c = 0;
        for (int i = 0; i < tpNodes.length; i++) {
            myGraph1.graph.graphRenderer[i].pointType = 1;
            myGraph1.graph.graphRenderer[i].pointType = 1;

            graphs[i] = new TheGraphBoolean(bufflen);

            myGraph1.graph.graphRenderer[i].xValues = xValues;
            myGraph1.graph.graphRenderer[i].yValues = graphs[i].yValues;

            graphs[i].name = tpNodes[i].jGetCaption();
            graphs[i].yPos = c;
            graphs[i].yValues[0] = c;
            graphs[i].value = c;

            myGraph1.graph.graphRenderer[i].caption = tpNodes[i].jGetCaption();
            myGraph1.graph.graphRenderer[i].captionX = 10;
            myGraph1.graph.graphRenderer[i].captionY = c + faktor / 2;
            myGraph1.graph.graphRenderer[i].captionColor = Color.GREEN;

            c += faktor + distance;
        }
        myGraph1.graph.back.positionX = 0;
        myGraph1.setMaxX((double) bufflen);
        myGraph1.setMaxY((double) c);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                myGraph1.graph.init();
                myGraph1.updateUI();
            }
        });

        internalC = 0;
        refreshC = 0;
    }

    public void stop() {

    }

    private int counter = 0;

    public void addValue(String colTitle, boolean value) {
        double val;
        int column = getColumnIndex(colTitle);

        if (column > -1) {
            if (value)
                // TODO ?
                val = 1 * faktor;
            else
                val = 0;

            val += graphs[column].yPos;

            graphs[column].value = val;
        }
    }

    public void process() {
        if (tpNodes == null) return;
        if (tpNodes.length == 0) return;
        if (dontRefresh) return;

        if (internalC >= abtastFreq) {
            internalC = 0;
            next();
        }

        // graphs[column].value=val;
        internalC++;

        if (refreshC >= refreshFreq) {
            refreshC = 0;
            if (jCheckBox1.isSelected()) {
                if (counter > bufflen) init();
            }

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    myGraph1.graph.init();
                    myGraph1.updateUI();
                }
            });
        }
        refreshC++;
    }

    private void sdfsd() {
        /*
         * basis.dataHistory.getEntry() LinkedList<DataEntry> list=basis.dataHistory.list;
         *
         * DataEntry obj; for (int i=0;i<list.size();i++) { obj=list.get(i); if (obj.value instanceof
         * VSDouble) { // System.out.println(""+obj.value.toString()); } }
         */
    }

    public void next() {
        dontRefresh = true;

        if (graphs != null) {
            if (counter < bufflen) {
                fillBuffer(xValues, counter, counter);
                for (int i = 0; i < graphs.length; i++) {
                    fillBuffer(graphs[i].yValues, counter, graphs[i].value);
                    myGraph1.graph.graphRenderer[i].xValues = xValues;
                    myGraph1.graph.graphRenderer[i].yValues = graphs[i].yValues;
                }
            } else {
                scrollBuffer(xValues, counter, counter);
                for (int i = 0; i < graphs.length; i++) {
                    scrollBuffer(graphs[i].yValues, counter, graphs[i].value);
                    myGraph1.graph.graphRenderer[i].xValues = xValues;
                    myGraph1.graph.graphRenderer[i].yValues = graphs[i].yValues;
                }
            }
        }

        counter++;
        dontRefresh = false;

        if (newbufflen != bufflen) {
            bufflen = newbufflen;
            init();
        }
    }

    public void fillBuffer(double[] values, int counter, double value) {
        for (int i = counter; i < bufflen; i++)
            values[i] = value;
    }

    public void scrollBuffer(double[] values, int counter, double value) {
        System.arraycopy(values, 1, values, 0, bufflen - 1);
        values[bufflen - 1] = value;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private MyGraph.MyGraph myGraph1;
    // End of variables declaration//GEN-END:variables
}
