import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;




/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author carmelo
 */
public class DialogPins extends javax.swing.JDialog {

    public String result = "";
    private ArrayList<JPanel> pin_panels = new ArrayList();
    
    public static boolean inArray(String[] arr, String targetValue) {
	for(String s: arr){
		if(s.equals(targetValue))
			return true;
	}
	return false;
}


    private void erstellePinCombos(String capatibilities) {
        int counter = 10;

        String cap_rows[] = capatibilities.split("\n");

        jLabel1.setText(cap_rows[0]);

        int a_counter=0;
        for (int i = 1; i < cap_rows.length; i++) {

            String row = cap_rows[i];

            String cap_cols[] = row.split("=");

            if (cap_cols.length == 2) {

                String pinNumber = cap_cols[0].trim();
                String pinSettings = cap_cols[1].trim();

                if (pinSettings.equalsIgnoreCase("NOT SUPPORTED")) {

                } else {
                    String options[] = pinSettings.split(";");

                    JPanel panel = new JPanel();
                    JLabel label_active = new JLabel("Active");
                    JLabel label = new JLabel("Pin " + pinNumber);
                    
                    
                    JLabel label_analog = new JLabel("Ax ");
                    if (inArray(options, "ANALOG_INPUT")) {
                        label_analog.setText("A"+a_counter);
                        a_counter++;
                    }else {
                        label_analog.setText("");
                    }
                    
                    JLabel label_min = new JLabel("Min Pulse");
                    JLabel label_max = new JLabel("Max Pulse");

                    JCheckBox active = new JCheckBox();
                    JComboBox combo = new JComboBox();
                    JSpinner spinner_min = new JSpinner();
                    JSpinner spinner_max = new JSpinner();

                    combo.setModel(new javax.swing.DefaultComboBoxModel(options));

                    spinner_min.setModel(new javax.swing.SpinnerNumberModel(0, 0, 10000, 1));
                    spinner_min.setEditor(new javax.swing.JSpinner.NumberEditor(spinner_min, ""));

                    spinner_max.setModel(new javax.swing.SpinnerNumberModel(0, 0, 10000, 1));
                    spinner_max.setEditor(new javax.swing.JSpinner.NumberEditor(spinner_max, ""));

                    ((JSpinner.DefaultEditor) spinner_min.getEditor()).getTextField().addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                            if (Character.isLetter(e.getKeyChar())) {
                                e.consume();
                            }
                        }
                    });

                    ((JSpinner.DefaultEditor) spinner_max.getEditor()).getTextField().addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                            if (Character.isLetter(e.getKeyChar())) {
                                e.consume();
                            }
                        }
                    });

                    panel.setBounds(0, counter, 640, 30);
                    panel.setLayout(null);

                    label.setBounds(10, 0, 40, 30);
                    active.setBounds(50, 0, 20, 30);
                    
                    label_active.setBounds(70, 0, 40, 30);
                    label_analog.setBounds(110, 0, 40, 30);
                    
                    combo.setBounds(140, 0, 150, 30);

                    label_min.setBounds(300, 0, 80, 30);
                    spinner_min.setBounds(370, 0, 100, 30);

                    label_max.setBounds(480, 0, 80, 30);
                    spinner_max.setBounds(540, 0, 100, 30);

                    active.setName("active");
                    combo.setName("type");
                    spinner_min.setName("min");
                    spinner_max.setName("max");
                    label_min.setName("label_min");
                    label_max.setName("label_max");

                    panel.add(label_active);
                    panel.add(label);
                    panel.add(label_analog);
                    panel.add(label_min);
                    panel.add(label_max);
                    panel.add(active);
                    panel.add(combo);
                    panel.add(spinner_min);
                    panel.add(spinner_max);
                    panel.setName("c_pin_" + pinNumber);

                    label_min.setVisible(false);
                    label_max.setVisible(false);

                    spinner_min.setVisible(false);
                    spinner_max.setVisible(false);

                    combo.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            JComboBox combo = (JComboBox) e.getSource();

                            JPanel panel = (JPanel) combo.getParent();

                            JLabel label_min = (JLabel) getComponentByName(panel, "label_min");
                            JLabel label_max = (JLabel) getComponentByName(panel, "label_max");

                            JSpinner spinner_min = (JSpinner) getComponentByName(panel, "min");
                            JSpinner spinner_max = (JSpinner) getComponentByName(panel, "max");

                            if (combo.getSelectedItem().equals("SERVO_OUTPUT")) {

                                label_min.setVisible(true);
                                label_max.setVisible(true);
                                spinner_min.setVisible(true);
                                spinner_max.setVisible(true);
                            } else {
                                label_min.setVisible(false);
                                label_max.setVisible(false);
                                spinner_min.setVisible(false);
                                spinner_max.setVisible(false);
                            }
                        }
                    });

                    jPanel1.add(panel);
                    counter += 38;

                    pin_panels.add(panel);

                }
            }

        }

        jPanel1.setPreferredSize(new Dimension(200, counter + 10));
    }

    private Component getComponentByName(JPanel panel, String name) {

        Component[] comps = panel.getComponents();
        for (Component comp : comps) {
            String comp_name = comp.getName();
            //System.out.println("comp_name = "+comp_name);
            if (comp_name != null && comp_name.equalsIgnoreCase(name)) {
                return comp;
            }
        }
        return null;
    }

    private void setPinComboValues(String settings) {

        String rows[] = settings.split("\n");

        for (String row : rows) {
            String cols[] = row.split("=");

            if (cols.length == 2) {

                String pinActive = cols[0].trim().substring(0, 1);
                String pinNumber = cols[0].trim().substring(1);
                String pinSettings = cols[1].trim();

                for (JPanel panel : pin_panels) {
                    String pinNo = panel.getName().substring(6).trim();

                    if (pinNo.equals(pinNumber)) {
                        // Found!
                        System.out.println("found Pin " + pinNo);

                        String value = pinSettings.replaceAll(";", "").trim();

                        System.out.println("Set " + value);

                        JCheckBox active = (JCheckBox) getComponentByName(panel, "active");
                        JComboBox combo = (JComboBox) getComponentByName(panel, "type");

                        JSpinner combo_min = (JSpinner) getComponentByName(panel, "min");
                        JSpinner combo_max = (JSpinner) getComponentByName(panel, "max");

                        if (value.indexOf("SERVO_OUTPUT") > -1) {
                            combo.setSelectedItem("SERVO_OUTPUT");

                            String params = value.substring("SERVO_OUTPUT".length() + 1);
                            params = params.replace("(", "");
                            params = params.replace(")", "");
                            System.out.println("params=" + params);

                            String[] splittedParams = params.split(",");

                            if (splittedParams.length == 2) {
                                combo_min.setValue(Integer.parseInt(splittedParams[0]));
                                combo_max.setValue(Integer.parseInt(splittedParams[1]));
                            }

                        } else {
                            combo.setSelectedItem(value);
                        }

                        if (pinActive.equals("a")) {
                            active.setSelected(true);
                        } else {
                            active.setSelected(false);
                        }

                        break;
                    }
                }
            }
        }
    }

    /**
     * Creates new form DialogPins
     */
    public DialogPins(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Firmata Pin Editor");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 733, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanel1);

        jPanel2.setPreferredSize(new java.awt.Dimension(500, 45));

        jButton2.setText("OK");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setText("Cancel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(350, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 215, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        setSize(new java.awt.Dimension(751, 560));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String res = "";

        for (JPanel panel : pin_panels) {
            String pinNo = panel.getName().substring(6).trim();

            JCheckBox active = (JCheckBox) getComponentByName(panel, "active");
            JComboBox combo = (JComboBox) getComponentByName(panel, "type");

            JSpinner spinner_min = (JSpinner) getComponentByName(panel, "min");
            JSpinner spinner_max = (JSpinner) getComponentByName(panel, "max");

            String value = (String) combo.getSelectedItem();

            String ac = "i";
            if (active.isSelected()) {
                ac = "a";
            }
            if (value.equals("SERVO_OUTPUT")) {

                res += ac + pinNo + "=" + value + "(" + spinner_min.getValue() + "," + spinner_max.getValue() + ");\n";
            } else {
                res += ac + pinNo + "=" + value + ";\n";
            }

        }

        System.out.println("res=" + res);
        result = res;
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DialogPins.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogPins.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogPins.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogPins.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                String capatibilities = "Firmata Protocol Version: 2.3 (StandardFirmata.ino)\n"
                        + "0=NOT SUPPORTED\n"
                        + "1=NOT SUPPORTED\n"
                        + "2=DIGITAL_INPUT;DIGITAL_OUTPUT;SERVO_OUTPUT;\n"
                        + "3=DIGITAL_INPUT;DIGITAL_OUTPUT;PWM_OUTPUT;SERVO_OUTPUT;\n"
                        + "4=DIGITAL_INPUT;DIGITAL_OUTPUT;SERVO_OUTPUT;\n"
                        + "5=DIGITAL_INPUT;DIGITAL_OUTPUT;PWM_OUTPUT;SERVO_OUTPUT;\n"
                        + "6=DIGITAL_INPUT;DIGITAL_OUTPUT;PWM_OUTPUT;SERVO_OUTPUT;\n"
                        + "7=DIGITAL_INPUT;DIGITAL_OUTPUT;SERVO_OUTPUT;\n"
                        + "8=DIGITAL_INPUT;DIGITAL_OUTPUT;SERVO_OUTPUT;\n"
                        + "9=DIGITAL_INPUT;DIGITAL_OUTPUT;PWM_OUTPUT;SERVO_OUTPUT;\n"
                        + "10=DIGITAL_INPUT;DIGITAL_OUTPUT;PWM_OUTPUT;SERVO_OUTPUT;\n"
                        + "11=DIGITAL_INPUT;DIGITAL_OUTPUT;PWM_OUTPUT;SERVO_OUTPUT;\n"
                        + "12=DIGITAL_INPUT;DIGITAL_OUTPUT;SERVO_OUTPUT;\n"
                        + "13=DIGITAL_INPUT;DIGITAL_OUTPUT;SERVO_OUTPUT;\n"
                        + "14=DIGITAL_INPUT;DIGITAL_OUTPUT;ANALOG_INPUT;\n"
                        + "15=DIGITAL_INPUT;DIGITAL_OUTPUT;ANALOG_INPUT;\n"
                        + "16=DIGITAL_INPUT;DIGITAL_OUTPUT;ANALOG_INPUT;\n"
                        + "17=DIGITAL_INPUT;DIGITAL_OUTPUT;ANALOG_INPUT;\n"
                        + "18=DIGITAL_INPUT;DIGITAL_OUTPUT;ANALOG_INPUT;UNKNOWN;\n"
                        + "19=DIGITAL_INPUT;DIGITAL_OUTPUT;ANALOG_INPUT;UNKNOWN;";

                String settings = "a2=DIGITAL_OUTPUT;\n"
                        + "a3=SERVO_OUTPUT(100,2000);\n"
                        + "a10=PWM_OUTPUT(100,200);\n"
                        + "i13=DIGITAL_OUTPUT;\n"
                        + "i14=ANALOG_INPUT;\n"
                        + "i15=ANALOG_INPUT;\n"
                        + "i16=ANALOG_INPUT;\n"
                        + "i19=ANALOG_INPUT;";

                DialogPins dialog = new DialogPins(new javax.swing.JFrame(), true);
                dialog.init(capatibilities, settings);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    public void init(String capatibilities, String settings) {
        erstellePinCombos(capatibilities);

        setPinComboValues(settings);
    }
}
