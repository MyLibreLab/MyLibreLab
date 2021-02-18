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

import VisualLogic.*;
import VisualLogic.variables.*;
import tools.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import javax.swing.JOptionPane;

public class Javascript extends JVSMain {

    public File tempfile;
    private Image image;

    private MyTimer theTimer =new MyTimer(this);
    private Thread timer = new Thread(theTimer);

    private VSComboBox dtPin_in_0 = new VSComboBox();
    private VSComboBox dtPin_in_1 = new VSComboBox();
    private VSComboBox dtPin_in_2 = new VSComboBox();
    private VSComboBox dtPin_in_3 = new VSComboBox();
    private VSComboBox dtPin_in_4 = new VSComboBox();
    private VSComboBox dtPin_in_5 = new VSComboBox();
    private VSComboBox dtPin_in_6 = new VSComboBox();
    private VSComboBox dtPin_in_7 = new VSComboBox();

    private VSComboBox dtPin_out_0 = new VSComboBox();
    private VSComboBox dtPin_out_1 = new VSComboBox();
    private VSComboBox dtPin_out_2 = new VSComboBox();
    private VSComboBox dtPin_out_3 = new VSComboBox();
    private VSComboBox dtPin_out_4 = new VSComboBox();
    private VSComboBox dtPin_out_5 = new VSComboBox();
    private VSComboBox dtPin_out_6 = new VSComboBox();
    private VSComboBox dtPin_out_7 = new VSComboBox();

    private VSObject in0;
    private VSObject in1;
    private VSObject in2;
    private VSObject in3;
    private VSObject in4;
    private VSObject in5;
    private VSObject in6;
    private VSObject in7;

    private VSObject out0;
    private VSObject out1;
    private VSObject out2;
    private VSObject out3;
    private VSObject out4;
    private VSObject out5;
    private VSObject out6;
    private VSObject out7;

    private ScriptEngine engine;
    private Bindings bindings;
    private CompiledScript cs;
    public VSString script = new VSString();

    private VSPropertyDialog more = new VSPropertyDialog();

    @Override
    public void onDispose() {
        
        theTimer.stop=true;
        
        if (image != null) {
            image.flush();
            image = null;
        }

    }

    @Override
    public void paint(java.awt.Graphics g) {
        if (element != null) {

            Rectangle bounds=element.jGetBounds();
            g.setColor(new Color(250,255,157));
            g.fillRect(10, 1, bounds.width-4, bounds.height-2);
            
            g.drawImage(image, 15, 30, null);
            /*
            
             g.setColor(Color.WHITE);
             g.fillRect(10,1, bounds.width-3, bounds.height-2);
            
             g.setFont(new Font("Monospaced", Font.PLAIN, 5));            
             g.setColor(Color.BLACK);
            
             int x = 20;
             int y = 20;
             
             String text = script.getValue();
            
             for (String line : text.split("\n")) {
             g.drawString(line, x, y += g.getFontMetrics().getHeight());
             }*/
            //g.drawString(caption, 20, 20);
        }

        super.paint(g);
    }

    @Override
    public void init() {
        initPins(0, 8, 0, 8);
        element.jSetSize(60, 100);
        element.jSetResizable(false);

        timer.start();
        
        image = element.jLoadImage(element.jGetSourcePath() + "icon.png");

        //element.jSetInnerBorderVisibility(false);
        initPinVisibility(false, true, false, true);

        setPin(0, ExternalIF.C_VARIANT, ExternalIF.PIN_OUTPUT);
        setPin(1, ExternalIF.C_VARIANT, ExternalIF.PIN_OUTPUT);
        setPin(2, ExternalIF.C_VARIANT, ExternalIF.PIN_OUTPUT);
        setPin(3, ExternalIF.C_VARIANT, ExternalIF.PIN_OUTPUT);
        setPin(4, ExternalIF.C_VARIANT, ExternalIF.PIN_OUTPUT);
        setPin(5, ExternalIF.C_VARIANT, ExternalIF.PIN_OUTPUT);
        setPin(6, ExternalIF.C_VARIANT, ExternalIF.PIN_OUTPUT);
        setPin(7, ExternalIF.C_VARIANT, ExternalIF.PIN_OUTPUT);

        setPin(8, ExternalIF.C_VARIANT, ExternalIF.PIN_INPUT);
        setPin(9, ExternalIF.C_VARIANT, ExternalIF.PIN_INPUT);
        setPin(10, ExternalIF.C_VARIANT, ExternalIF.PIN_INPUT);
        setPin(11, ExternalIF.C_VARIANT, ExternalIF.PIN_INPUT);
        setPin(12, ExternalIF.C_VARIANT, ExternalIF.PIN_INPUT);
        setPin(13, ExternalIF.C_VARIANT, ExternalIF.PIN_INPUT);
        setPin(14, ExternalIF.C_VARIANT, ExternalIF.PIN_INPUT);
        setPin(15, ExternalIF.C_VARIANT, ExternalIF.PIN_INPUT);

        element.jSetPinDescription(0, "Out");
        element.jSetPinDescription(1, "Out");
        element.jSetPinDescription(2, "Out");
        element.jSetPinDescription(3, "Out");
        element.jSetPinDescription(4, "Out");
        element.jSetPinDescription(5, "Out");
        element.jSetPinDescription(6, "Out");
        element.jSetPinDescription(7, "Out");

        element.jSetPinDescription(8, "In");
        element.jSetPinDescription(9, "In");
        element.jSetPinDescription(10, "In");
        element.jSetPinDescription(11, "In");
        element.jSetPinDescription(12, "In");
        element.jSetPinDescription(13, "In");
        element.jSetPinDescription(14, "In");
        element.jSetPinDescription(15, "In");

        String liste[] = element.jGetDataTypeList();

        for (String liste1 : liste) {
            dtPin_in_0.addItem(liste1);
            dtPin_in_1.addItem(liste1);
            dtPin_in_2.addItem(liste1);
            dtPin_in_3.addItem(liste1);
            dtPin_in_4.addItem(liste1);
            dtPin_in_5.addItem(liste1);
            dtPin_in_6.addItem(liste1);
            dtPin_in_7.addItem(liste1);
            dtPin_out_0.addItem(liste1);
            dtPin_out_1.addItem(liste1);
            dtPin_out_2.addItem(liste1);
            dtPin_out_3.addItem(liste1);
            dtPin_out_4.addItem(liste1);
            dtPin_out_5.addItem(liste1);
            dtPin_out_6.addItem(liste1);
            dtPin_out_7.addItem(liste1);
        }

        dtPin_in_0.selectedIndex = 0;
        dtPin_in_1.selectedIndex = 0;
        dtPin_in_2.selectedIndex = 0;
        dtPin_in_3.selectedIndex = 0;
        dtPin_in_4.selectedIndex = 0;
        dtPin_in_5.selectedIndex = 0;
        dtPin_in_6.selectedIndex = 0;
        dtPin_in_7.selectedIndex = 0;

        dtPin_out_0.selectedIndex = 0;
        dtPin_out_1.selectedIndex = 0;
        dtPin_out_2.selectedIndex = 0;
        dtPin_out_3.selectedIndex = 0;
        dtPin_out_4.selectedIndex = 0;
        dtPin_out_5.selectedIndex = 0;
        dtPin_out_6.selectedIndex = 0;
        dtPin_out_7.selectedIndex = 0;

        setName("Javascript");
    }

    @Override
    public void xOnInit() {
        super.xOnInit();

    }

    @Override
    public void start() {

        bindings = new SimpleBindings();
        bindings.put("in0", in0);
        bindings.put("in1", in1);
        bindings.put("in2", in2);
        bindings.put("in3", in3);
        bindings.put("in4", in4);
        bindings.put("in5", in5);
        bindings.put("in6", in6);
        bindings.put("in7", in7);

        bindings.put("out0", out0);
        bindings.put("out1", out1);
        bindings.put("out2", out2);
        bindings.put("out3", out3);
        bindings.put("out4", out4);
        bindings.put("out5", out5);
        bindings.put("out6", out6);
        bindings.put("out7", out7);

        try {
            engine = new ScriptEngineManager().getEngineByName("javascript");

            if (engine instanceof Compilable) {
                System.out.println("Compiling....");
                Compilable compEngine = (Compilable) engine;
                
                cs = compEngine.compile(script.getValue());                
            }

        } catch (Exception ioEx) {

            element.jShowMessage(ioEx.getMessage());

        }

    }

    @Override
    public void stop() {

    }

    @Override
    public void process() {

        try {

            if (engine instanceof Compilable) {

                cs.eval(bindings);
            } else {
                engine.eval(script.getValue(), bindings);
            }

            //out.setChanged(true);
            element.notifyPin(0);
            element.notifyPin(1);
            element.notifyPin(2);
            element.notifyPin(3);
            element.notifyPin(4);
            element.notifyPin(5);
            element.notifyPin(6);
            element.notifyPin(7);

        } catch (Exception ioEx) {

            element.jShowMessage(ioEx.getMessage());

        }

    }

    @Override
    public void initInputPins() {

        in0 = (VSObject) element.getPinInputReference(8);
        in1 = (VSObject) element.getPinInputReference(9);
        in2 = (VSObject) element.getPinInputReference(10);
        in3 = (VSObject) element.getPinInputReference(11);
        in4 = (VSObject) element.getPinInputReference(12);
        in5 = (VSObject) element.getPinInputReference(13);
        in6 = (VSObject) element.getPinInputReference(14);
        in7 = (VSObject) element.getPinInputReference(15);

    }

    @Override
    public void initOutputPins() {
        out0 = element.jCreatePinDataType(0);
        out1 = element.jCreatePinDataType(1);
        out2 = element.jCreatePinDataType(2);
        out3 = element.jCreatePinDataType(3);
        out4 = element.jCreatePinDataType(4);
        out5 = element.jCreatePinDataType(5);
        out6 = element.jCreatePinDataType(6);
        out7 = element.jCreatePinDataType(7);

        element.setPinOutputReference(0, out0);
        element.setPinOutputReference(1, out1);
        element.setPinOutputReference(2, out2);
        element.setPinOutputReference(3, out3);
        element.setPinOutputReference(4, out4);
        element.setPinOutputReference(5, out5);
        element.setPinOutputReference(6, out6);
        element.setPinOutputReference(7, out7);

        if (out0 == null) {
            out0 = new VSObject();
        }
        if (out1 == null) {
            out1 = new VSObject();
        }
        if (out2 == null) {
            out2 = new VSObject();
        }
        if (out3 == null) {
            out3 = new VSObject();
        }
        if (out4 == null) {
            out4 = new VSObject();
        }
        if (out5 == null) {
            out5 = new VSObject();
        }
        if (out6 == null) {
            out6 = new VSObject();
        }
        if (out7 == null) {
            out7 = new VSObject();
        }

        out0.setPin(0);
        out1.setPin(1);
        out2.setPin(2);
        out3.setPin(3);
        out4.setPin(4);
        out5.setPin(5);
        out6.setPin(6);
        out7.setPin(7);
    }

    @Override
    public void setPropertyEditor() {
        element.jAddPEItem("Javascript", more, 0, 0);

        element.jAddPEItem("DT InPin0", dtPin_in_0, 0, 0);
        element.jAddPEItem("DT InPin1", dtPin_in_1, 0, 0);
        element.jAddPEItem("DT InPin2", dtPin_in_2, 0, 0);
        element.jAddPEItem("DT InPin3", dtPin_in_3, 0, 0);
        element.jAddPEItem("DT InPin4", dtPin_in_4, 0, 0);
        element.jAddPEItem("DT InPin5", dtPin_in_5, 0, 0);
        element.jAddPEItem("DT InPin6", dtPin_in_6, 0, 0);
        element.jAddPEItem("DT InPin7", dtPin_in_7, 0, 0);

        element.jAddPEItem("DT OutPin0", dtPin_out_0, 0, 0);
        element.jAddPEItem("DT OutPin1", dtPin_out_1, 0, 0);
        element.jAddPEItem("DT OutPin2", dtPin_out_2, 0, 0);
        element.jAddPEItem("DT OutPin3", dtPin_out_3, 0, 0);
        element.jAddPEItem("DT OutPin4", dtPin_out_4, 0, 0);
        element.jAddPEItem("DT OutPin5", dtPin_out_5, 0, 0);
        element.jAddPEItem("DT OutPin6", dtPin_out_6, 0, 0);
        element.jAddPEItem("DT OutPin7", dtPin_out_7, 0, 0);

    }

    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    @Override
    public void propertyChanged(Object o) {

        if (o.equals(more)) {

            try {
                tempfile = File.createTempFile("myopenlab_temp", ".js");

                try {
                    File file = tempfile;
                    FileWriter fileWriter = new FileWriter(file);

                    fileWriter.write(script.getValue());

                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String htmlEditor = element.jGetBasis().getJavascriptEditor();

                if (htmlEditor != null && htmlEditor.trim().length() == 0) {
                    JOptionPane.showMessageDialog(null, "Please set the Path of your Javascript Editor!");
                } else {

                    try {
                        Runtime.getRuntime().exec(htmlEditor + " " + tempfile.getCanonicalPath());
                    } catch (IOException ex) {

                    }
                }

                //System.out.println("Temp file : " + temp.getAbsolutePath());
            } catch (IOException ex) {
                Logger.getLogger(Javascript.class.getName()).log(Level.SEVERE, null, ex);
            }

            /*EditorDialog frm = new EditorDialog(null, true);

             frm.jTextPane1.setText(script.getValue());
             frm.setVisible(true);

             if (frm.result) {

             script.setValue(frm.jTextPane1.getText());
             }*/
        } else {
            setPinDT(0, o, dtPin_out_0, ExternalIF.PIN_OUTPUT);
            setPinDT(1, o, dtPin_out_1, ExternalIF.PIN_OUTPUT);
            setPinDT(2, o, dtPin_out_2, ExternalIF.PIN_OUTPUT);
            setPinDT(3, o, dtPin_out_3, ExternalIF.PIN_OUTPUT);
            setPinDT(4, o, dtPin_out_4, ExternalIF.PIN_OUTPUT);
            setPinDT(5, o, dtPin_out_5, ExternalIF.PIN_OUTPUT);
            setPinDT(6, o, dtPin_out_6, ExternalIF.PIN_OUTPUT);
            setPinDT(7, o, dtPin_out_7, ExternalIF.PIN_OUTPUT);

            setPinDT(8, o, dtPin_in_0, ExternalIF.PIN_INPUT);
            setPinDT(9, o, dtPin_in_1, ExternalIF.PIN_INPUT);
            setPinDT(10, o, dtPin_in_2, ExternalIF.PIN_INPUT);
            setPinDT(11, o, dtPin_in_3, ExternalIF.PIN_INPUT);
            setPinDT(12, o, dtPin_in_4, ExternalIF.PIN_INPUT);
            setPinDT(13, o, dtPin_in_5, ExternalIF.PIN_INPUT);
            setPinDT(14, o, dtPin_in_6, ExternalIF.PIN_INPUT);
            setPinDT(15, o, dtPin_in_7, ExternalIF.PIN_INPUT);

        }
        element.jRepaint();
    }

    private void setPinDT2(int pinIndex, VSComboBox dtPin, byte type) {
        int dt = element.jGetDataType(dtPin.getItem(dtPin.selectedIndex));
        setPin(pinIndex, dt, type);
    }

    private void setPinDT(int pinIndex, Object o, VSComboBox dtPin, byte type) {
        if (o.equals(dtPin)) {
            setPinDT2(pinIndex, dtPin, type);
        }
    }

    @Override
    public void loadFromStream(java.io.FileInputStream fis) {
        script.loadFromStream(fis);

        dtPin_in_0.loadFromStream(fis);
        dtPin_in_1.loadFromStream(fis);
        dtPin_in_2.loadFromStream(fis);
        dtPin_in_3.loadFromStream(fis);
        dtPin_in_4.loadFromStream(fis);
        dtPin_in_5.loadFromStream(fis);
        dtPin_in_6.loadFromStream(fis);
        dtPin_in_7.loadFromStream(fis);

        dtPin_out_0.loadFromStream(fis);
        dtPin_out_1.loadFromStream(fis);
        dtPin_out_2.loadFromStream(fis);
        dtPin_out_3.loadFromStream(fis);
        dtPin_out_4.loadFromStream(fis);
        dtPin_out_5.loadFromStream(fis);
        dtPin_out_6.loadFromStream(fis);
        dtPin_out_7.loadFromStream(fis);

        setPinDT2(0, dtPin_out_0, ExternalIF.PIN_OUTPUT);
        setPinDT2(1, dtPin_out_1, ExternalIF.PIN_OUTPUT);
        setPinDT2(2, dtPin_out_2, ExternalIF.PIN_OUTPUT);
        setPinDT2(3, dtPin_out_3, ExternalIF.PIN_OUTPUT);
        setPinDT2(4, dtPin_out_4, ExternalIF.PIN_OUTPUT);
        setPinDT2(5, dtPin_out_5, ExternalIF.PIN_OUTPUT);
        setPinDT2(6, dtPin_out_6, ExternalIF.PIN_OUTPUT);
        setPinDT2(7, dtPin_out_7, ExternalIF.PIN_OUTPUT);

        setPinDT2(8, dtPin_in_0, ExternalIF.PIN_INPUT);
        setPinDT2(9, dtPin_in_1, ExternalIF.PIN_INPUT);
        setPinDT2(10, dtPin_in_2, ExternalIF.PIN_INPUT);
        setPinDT2(11, dtPin_in_3, ExternalIF.PIN_INPUT);
        setPinDT2(12, dtPin_in_4, ExternalIF.PIN_INPUT);
        setPinDT2(13, dtPin_in_5, ExternalIF.PIN_INPUT);
        setPinDT2(14, dtPin_in_6, ExternalIF.PIN_INPUT);
        setPinDT2(15, dtPin_in_7, ExternalIF.PIN_INPUT);

    }

    @Override
    public void saveToStream(java.io.FileOutputStream fos) {
        script.saveToStream(fos);

        dtPin_in_0.saveToStream(fos);
        dtPin_in_1.saveToStream(fos);
        dtPin_in_2.saveToStream(fos);
        dtPin_in_3.saveToStream(fos);
        dtPin_in_4.saveToStream(fos);
        dtPin_in_5.saveToStream(fos);
        dtPin_in_6.saveToStream(fos);
        dtPin_in_7.saveToStream(fos);

        dtPin_out_0.saveToStream(fos);
        dtPin_out_1.saveToStream(fos);
        dtPin_out_2.saveToStream(fos);
        dtPin_out_3.saveToStream(fos);
        dtPin_out_4.saveToStream(fos);
        dtPin_out_5.saveToStream(fos);
        dtPin_out_6.saveToStream(fos);
        dtPin_out_7.saveToStream(fos);

    }

}
