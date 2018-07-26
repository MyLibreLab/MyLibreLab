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
package Peditor;

import CustomColorPicker.CustomColorPicker;
import VisualLogic.DialogFontChooser;
import VisualLogic.Element;
import VisualLogic.ExtensionFileFilter;
import VisualLogic.VMObject;
import VisualLogic.variables.VSBoolean;
import VisualLogic.variables.VSByte;
import VisualLogic.variables.VSColor;
import VisualLogic.variables.VSColorAdvanced;
import VisualLogic.variables.VSComboBox;
import VisualLogic.variables.VSDouble;
import VisualLogic.variables.VSFile;
import VisualLogic.variables.VSFont;
import VisualLogic.variables.VSImage;
import VisualLogic.variables.VSInteger;
import VisualLogic.variables.VSObject;
import VisualLogic.variables.VSProperties;
import VisualLogic.variables.VSPropertyDialog;
import VisualLogic.variables.VSString;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

interface PEIF {

    public void changed();

    public Object getReference();
}

class VSNope extends VSObject {

}

class ComboBoxEditor extends JComboBox implements PEIF, ActionListener {

    private VSComboBox referenz;    
    private PropertyEditorItem item;

    @Override
    public Object getReference() {
        return referenz;
    }

    ;
            
    public void jChanged() {
        item.processChanged();
    }

    public ComboBoxEditor(PropertyEditorItem item, VSComboBox referenz) {
        super();
        this.referenz = referenz;
        this.item = item;

        for (int i = 0; i < referenz.getSize(); i++) {
            this.addItem(referenz.getItem(i));
        }
        try {
            setSelectedIndex(referenz.selectedIndex);
        } catch (Exception ex) {

        }

        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        referenz.selectedIndex = cb.getSelectedIndex();
        jChanged();
    }

    @Override
    public void changed() {
    }

}

class NopeEditor extends JPanel implements PEIF {

    @Override
    public void changed() {

    }

    @Override
    public Object getReference() {
        return null;
    }
}

class BooleanEditor extends JCheckBox implements PEIF {

    private VSBoolean referenz;
    private PropertyEditorItem item;

    @Override
    public Object getReference() {
        return referenz;
    }

    ;
    
    public void jChanged() {
        item.processChanged();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getBounds().width - 1, getBounds().height - 1);

    }

    public BooleanEditor(PropertyEditorItem item, VSBoolean referenz) {
        super();
        this.referenz = referenz;
        this.item = item;

        setSelected(referenz.getValue());

        addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jChanged();
            }
        });

    }

    @Override
    public void changed() {
        referenz.setValue(this.isSelected());
    }
}

class AdvancedColorEditor extends JPanel implements PEIF {

    private VSColorAdvanced referenz;
    private PropertyEditorItem item;
    private JFrame frame;
    private JButton button = new JButton("...");

    @Override
    public Object getReference() {
        return referenz;
    }

    ;
    
    
    public void setValue(int modus, Point p1, Point p2, Color color1, Color color2, int color1Transparency, int color2Transparency, boolean wiederholung) {
        referenz.setValue(modus, p1, p2, color1, color2, color1Transparency, color2Transparency, wiederholung);
        item.processChanged();
    }

    public void jChanged() {
        item.processChanged();
    }

    private VSColorAdvanced getRef() {
        return referenz;
    }

    public JFrame getFrame() {
        return frame;
    }

    public AdvancedColorEditor(PropertyEditorItem item, JFrame frame, VSColorAdvanced referenz, double min, double max) {
        super();
        this.referenz = referenz;
        this.item = item;

        this.frame = frame;

        button.setPreferredSize(new Dimension(20, 10));
        //button.setBackground(referenz.getValue());
        this.setLayout(new BorderLayout());
        this.add(button, BorderLayout.CENTER);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                CustomColorPicker frm = new CustomColorPicker(null, true);

                frm.modus = getRef().modus;
                frm.p1 = getRef().p1;
                frm.p2 = getRef().p2;
                frm.color1 = getRef().color1;
                frm.color2 = getRef().color2;
                frm.color1Transparency = getRef().color1Transparency;
                frm.color2Transparency = getRef().color2Transparency;
                frm.wiederholung = getRef().wiederholung;
                frm.init();
                frm.setVisible(true);
                if (frm.result) {
                    setValue(frm.modus, frm.p1, frm.p2, frm.color1, frm.color2, frm.color1Transparency, frm.color2Transparency, frm.wiederholung);
                }
                //Color bgColor = JColorChooser.showDialog(getFrame(),java.util.ResourceBundle.getBundle("Peditor/PropertyEditor").getString("Choose Background Color"),getColor());
                //if (bgColor != null)  setColor(bgColor);
                //jChanged();
            }
        });

    }

    @Override
    public void changed() {
        //referenz.setValue(button.getBackground());  
    }

}

class ColorEditor extends JPanel implements PEIF {

    private VSColor referenz;
    private PropertyEditorItem item;
    private JFrame frame;
    private JButton button = new JButton("...");

    @Override
    public Object getReference() {
        return referenz;
    }

    ;
    
    
    private void setColor(Color color) {
        referenz.setValue(color);
        button.setBackground(referenz.getValue());
        item.processChanged();
    }

    public void jChanged() {
        item.processChanged();
    }

    private Color getColor() {
        return referenz.getValue();
    }

    public JFrame getFrame() {
        return frame;
    }

    public ColorEditor(PropertyEditorItem item, JFrame frame, VSColor referenz, double min, double max) {
        super();
        this.referenz = referenz;
        this.item = item;

        this.frame = frame;

        button.setPreferredSize(new Dimension(20, 10));
        button.setBackground(referenz.getValue());
        this.setLayout(new BorderLayout());
        this.add(button, BorderLayout.CENTER);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Color bgColor = JColorChooser.showDialog(getFrame(), java.util.ResourceBundle.getBundle("Peditor/PropertyEditor").getString("Choose Background Color"), getColor());
                if (bgColor != null) {
                    setColor(bgColor);
                }
                jChanged();
            }
        });

    }

    @Override
    public void changed() {
        referenz.setValue(button.getBackground());
    }

}

class ReadonlySelector extends JButton implements PEIF {

    private VSObject referenz;
    private PropertyEditorItem item;

    @Override
    public Object getReference() {
        return referenz;
    }
    
    
    private void callElementPropertyMethode() {

    }

    public void jChanged() {

    }

    public ReadonlySelector(PropertyEditorItem item, VSObject referenz) {
        super();
        this.item = item;
        this.referenz = referenz;

        setText("...");

        //System.out.println("ref="+referenz.class.ge());
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                callElementPropertyMethode();
                jChanged();
            }
        });

    }

    @Override
    public void changed() {
    }
}

class OpenPropertyDialogEditor extends JPanel implements PEIF {

    private VSPropertyDialog referenz;
    private Element element;
    private JButton button = new JButton("...");
    private JLabel label = new JLabel("XXXXX");
    private PropertyEditorItem item;

    @Override
    public Object getReference() {
        return referenz;
    }
    
    
    private void callElementPropertyMethode() {
        if (element != null) {
            if (element.classRef != null) {
                element.classRef.xopenPropertyDialog();
            }
        }
    }

    public void jChanged() {
        item.processChanged();
        label.setText(referenz.getText());
    }

    public OpenPropertyDialogEditor(PropertyEditorItem item, Element element, VSPropertyDialog referenz) {
        super();
        this.item = item;
        this.element = element;
        this.referenz = referenz;

        button.setPreferredSize(new Dimension(20, 10));
        this.setLayout(new BorderLayout());
        this.add(label, BorderLayout.CENTER);
        button.setPreferredSize(new Dimension(20, 20));
        this.add(button, BorderLayout.EAST);

        label.setText(referenz.getText());

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                callElementPropertyMethode();
                jChanged();
            }
        });

    }

    @Override
    public void changed() {
    }
}

class FileEditor extends JPanel implements PEIF {

    private VSFile referenz;
    private static String letztesVerzeichniss = ".";
    private PropertyEditorItem item;
    private String path = "";
    private JFrame frame;
    private JLabel label = new JLabel("   ");
    private JButton button = new JButton("...");

    @Override
    public Object getReference() {
        return referenz;
    }

    ;
    
    private void mySetFile(String filename) {
        if (referenz != null) {
            referenz.setValue(filename);
        }

        path = filename;
        if (item.element != null) {
            item.element.classRef.propertyChanged(referenz);
        }
        if (item.element != null) {
            item.element.propertyChanged(referenz);
        }
    }

    public JFrame getFrame() {
        return frame;
    }

    public void jChanged() {
        if (item != null) {
            item.processChanged();
        }
    }

    private String myGetFile() {
        if (referenz != null) {
            return referenz.getValue();
        } else {
            return "";
        }
    }

    public FileEditor(PropertyEditorItem item, JFrame frame, VSFile referenz) {
        super();
        this.referenz = referenz;
        this.item = item;
        this.frame = frame;

        this.setLayout(new BorderLayout());

        button.setPreferredSize(new Dimension(20, 10));

        add(label, java.awt.BorderLayout.CENTER);
        add(button, java.awt.BorderLayout.EAST);

        button.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JFileChooser chooser = new JFileChooser();

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int mitteX = (int) screenSize.getWidth() / 2;
                int mitteY = (int) screenSize.getHeight() / 2;

                chooser.setLocation(mitteX - getWidth() / 2 - 200, mitteY - getHeight() / 2 - 200);

                chooser.setCurrentDirectory(new java.io.File(letztesVerzeichniss));

                ExtensionFileFilter filter = new ExtensionFileFilter();

                VSFile ref = ((VSFile) getReference());
                for (int i = 0; i < ref.getExtensionsCount(); i++) {
                    filter.addExtension(ref.getExtension(i));
                }

                filter.setDescription(ref.getDescription());

                chooser.addChoosableFileFilter(filter);

                int returnVal = chooser.showOpenDialog(null);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    letztesVerzeichniss = chooser.getCurrentDirectory().getPath();
                    mySetFile(chooser.getSelectedFile().getAbsolutePath());
                    jChanged();
                }
            }
        });

    }

    @Override
    public void changed() {
        referenz.setValue(path);
    }

}

class FontEditor extends JPanel implements PEIF {

    public VSFont referenz;
    private PropertyEditorItem item;
    private JLabel label = new JLabel("");
    private JFrame frame = null;
    private JButton button = new JButton("...");

    @Override
    public Object getReference() {
        return referenz;
    }

    ;
    
    private void mySetFont(Font font) {
        referenz.setValue(font);
        //setFont(font);
        label.setText(font.getName());
        if (item.element != null) {
            item.element.classRef.propertyChanged(referenz);
        }
        if (item.element != null) {
            item.element.propertyChanged(referenz);
        }
    }

    public void jChanged() {
        item.processChanged();
    }

    private Font myGetFont() {
        return referenz.getValue();
    }

    public JFrame getFrame() {
        return frame;
    }

    public FontEditor(PropertyEditorItem item, JFrame frame, VSFont referenz) {
        super();
        this.referenz = referenz;
        this.item = item;
        this.frame = frame;

        this.setLayout(new BorderLayout());

        button.setPreferredSize(new Dimension(20, 10));

        add(label, java.awt.BorderLayout.CENTER);
        add(button, java.awt.BorderLayout.EAST);

        label.setText(referenz.getValue().getName());

        button.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DialogFontChooser chooser = new DialogFontChooser(getFrame(), true);

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int mitteX = (int) screenSize.getWidth() / 2;
                int mitteY = (int) screenSize.getHeight() / 2;

                chooser.setFont(myGetFont());
                chooser.setLocation(mitteX - getWidth() / 2 - 200, mitteY - getHeight() / 2 - 200);

                chooser.setVisible(true);
                if (DialogFontChooser.result) {
                    mySetFont(chooser.getNewFont());
                    jChanged();
                }
            }
        });

    }

    @Override
    public void changed() {
        referenz.setValue(myGetFont());
    }

}

class DoubleEditor extends JTextField implements PEIF {

    private VSDouble referenz;
    private double min;
    private double max;
    private PropertyEditorItem item;

    @Override
    public Object getReference() {
        return referenz;
    }
    
    
    public void jchanged() {
        item.processChanged();
    }

    public DoubleEditor(PropertyEditorItem item, VSDouble referenz, double min, double max) {
        super();
        this.item = item;
        this.min = min;
        this.max = max;
        this.referenz = referenz;

        setText("" + referenz.getValue());
        this.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == 10) {
                    jchanged();
                }
            }
        });

    }

    @Override
    public void changed() {
        try {
            String txt = getText();

            double value = Double.valueOf(txt);

            if (value < min) {
                value = min;
            }
            if (value > max) {
                value = max;
            }

            referenz.setValue(value);
        } catch (NumberFormatException nfe) {
            setText("");
        }
    }
}

class IntegerEditor extends JTextField implements PEIF {

    private VSInteger referenz;
    private int min;
    private int max;
    public PropertyEditorItem item;

    @Override
    public Object getReference() {
        return referenz;
    }
    
    
    public void pChanged() {
        item.processChanged();
    }

    public IntegerEditor(PropertyEditorItem item, VSInteger referenz, int min, int max) {
        super();
        this.min = min;
        this.max = max;
        this.item = item;
        this.referenz = referenz;
        setText("" + referenz.getValue());

        this.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == 10) {
                    pChanged();
                }
            }
        });

    }

    @Override
    public void changed() {
        try {

            referenz.setValue(Integer.valueOf(getText()));

            if (referenz.getValue() < min) {
                referenz.setValue(min);
            }
            if (referenz.getValue() > max) {
                referenz.setValue(max);
            }

        } catch (NumberFormatException nfe) {
            setText("0");
        }
    }

}

class ByteEditor extends JTextField implements PEIF {

    private VSByte referenz;
    private int min;
    private int max;
    public PropertyEditorItem item;

    @Override
    public Object getReference() {
        return referenz;
    }
    
    
    public void pChanged() {
        item.processChanged();
    }

    public ByteEditor(PropertyEditorItem item, VSByte referenz, int min, int max) {
        super();
        this.min = min;
        this.max = max;
        this.item = item;
        this.referenz = referenz;
        setText("" + VSByte.toSigned(referenz.getValue()));

        this.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == 10) {
                    pChanged();
                }
            }
        });

    }

    @Override
    public void changed() {
        try {
            short val = (short) (Integer.parseInt(getText()));
            referenz.setValue(VSByte.toUnsigned(val));

            if (VSByte.toSigned(referenz.getValue()) < min) {
                referenz.setValue(VSByte.toUnsigned((short) min));
            }
            if (VSByte.toSigned(referenz.getValue()) > max) {
                referenz.setValue(VSByte.toUnsigned((short) max));
            }

        } catch (NumberFormatException nfe) {
            setText("0");
        }
    }

}

class ImageEditor extends JPanel implements PEIF {

    private VSImage referenz;
    private static String letztesVerzeichniss = ".";
    private PropertyEditorItem item;
    private String path = "";
    private JFrame frame;
    private JLabel label = new JLabel("   ");
    private JButton button = new JButton("...");

    @Override
    public Object getReference() {
        return referenz;
    }

    ;
    
    private void mySetFile(String filename) {
        if (referenz != null) {
            referenz.loadImage(filename);
        }

        path = filename;
        if (item.element != null) {
            item.element.classRef.propertyChanged(referenz);
        }
        if (item.element != null) {
            item.element.propertyChanged(referenz);
        }
    }

    public JFrame getFrame() {
        return frame;
    }

    public void jChanged() {
        if (item != null) {
            item.processChanged();
        }
    }

    public ImageEditor(PropertyEditorItem item, JFrame frame, VSImage referenz) {
        super();
        this.referenz = referenz;
        this.item = item;
        this.frame = frame;

        this.setLayout(new BorderLayout());

        button.setPreferredSize(new Dimension(20, 10));

        add(label, java.awt.BorderLayout.CENTER);
        add(button, java.awt.BorderLayout.EAST);

        button.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JFileChooser chooser = new JFileChooser();

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int mitteX = (int) screenSize.getWidth() / 2;
                int mitteY = (int) screenSize.getHeight() / 2;

                chooser.setLocation(mitteX - getWidth() / 2 - 200, mitteY - getHeight() / 2 - 200);

                chooser.setCurrentDirectory(new java.io.File(letztesVerzeichniss));

                ExtensionFileFilter filter = new ExtensionFileFilter();

                filter.addExtension("gif");
                filter.addExtension("png");
                filter.addExtension("jpg");
                //filter.setDescription(ref.getDescription());

                chooser.addChoosableFileFilter(filter);

                int returnVal = chooser.showOpenDialog(null);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    letztesVerzeichniss = chooser.getCurrentDirectory().getPath();
                    mySetFile(chooser.getSelectedFile().getAbsolutePath());
                    jChanged();
                }
            }
        });

    }

    @Override
    public void changed() {
        referenz.loadImage(path);
    }

}

class PropertiesEditor extends JButton implements PEIF, ActionListener {

    private VSProperties referenz;
    private PropertyEditorItem item;
    private JFrame frame;
    private VMObject vmobject;

    @Override
    public Object getReference() {
        return referenz;
    }
    
    
    public void pChanged() {
        item.processChanged(); 
    }

    public PropertiesEditor(PropertyEditorItem item, JFrame frame, VMObject vmobject, VSProperties referenz) {
        super();
        this.referenz = referenz;
        this.item = item;
        this.frame = frame;
        this.vmobject = vmobject;

        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        DialogPropertiesChoice frm = new DialogPropertiesChoice(frame, true, vmobject);

        frm.execute();
        if (frm.result) {
            vmobject.owner.setChanged(true);
            int newVersion = 0;
            try {
                newVersion = Integer.parseInt(vmobject.owner.basisVersion);
            } catch (Exception ex) {
            }
            newVersion++;
            vmobject.owner.basisVersion = "" + newVersion;

            vmobject.propertyList = (ArrayList) frm.props.clone();
        }
    }

    @Override
    public void changed() {
        //referenz.setValue(getText());
    }
}

class StringEditor extends JTextField implements PEIF {

    private VSString referenz;
    private PropertyEditorItem item;

    @Override
    public Object getReference() {
        return referenz;
    }

    ;
    
    public void pChanged() {
        item.processChanged();
    }

    public StringEditor(PropertyEditorItem item, VSString referenz) {
        super();
        this.referenz = referenz;
        this.item = item;
        setText(referenz.getValue());

        this.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == 10) {
                    pChanged();
                }
            }
        });

    }

    @Override
    public void changed() {
        referenz.setValue(getText());
    }
}

/**
 *
 * @author Homer
 */
public class PropertyEditor extends javax.swing.JPanel {

    ArrayList liste = new ArrayList();
    private final int itemHeight = 25;
    public Element element;
    public VMObject vmobject;
    private JFrame frame;
    public boolean locked = false;
    private JPanel myGUI = null;

    public int mode = 0;

    public void setElement(Element element) {
        this.element = element;
        this.vmobject = element.owner;
        element.initVars();

        addItem(java.util.ResourceBundle.getBundle("Peditor/PropertyEditor").getString("caption"), element.vCaption, 0, 0, true);
        addItem(java.util.ResourceBundle.getBundle("Peditor/PropertyEditor").getString("Show_Caption"), element.vShowCaption, 0, 0, true);
        addItem(java.util.ResourceBundle.getBundle("Peditor/PropertyEditor").getString("Left"), element.vLeft, 0, 999999, true);
        addItem(java.util.ResourceBundle.getBundle("Peditor/PropertyEditor").getString("Top"), element.vTop, 0, 999999, true);

        if (element.owner == element.owner.owner.getFrontBasis()) {
            addItem(java.util.ResourceBundle.getBundle("Peditor/PropertyEditor").getString("Visible"), element.vVisible, 0, 0, true);
        }

        boolean editable = element.isResizable();
        addItem(java.util.ResourceBundle.getBundle("Peditor/PropertyEditor").getString("Width"), element.vWidth, 0, 99999, editable);
        addItem(java.util.ResourceBundle.getBundle("Peditor/PropertyEditor").getString("Height"), element.vHeight, 0, 99999, editable);
        //addItem("", new VSNope(), 0, 0);
    }

    public void setVMObject(VMObject vmobject) {
        this.vmobject = vmobject;
        this.element = null;
    }

    /**
     * Creates new form PropertyEditor
     */
    public PropertyEditor(JFrame frame) {

        initComponents();
        this.frame = frame;
        clear();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        leftPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        rightPanel = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();

        setDoubleBuffered(false);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(null);
        jScrollPane1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jScrollPane1ComponentResized(evt);
            }
        });

        jSplitPane1.setBorder(null);
        jSplitPane1.setForeground(new java.awt.Color(250, 250, 150));
        jSplitPane1.setAutoscrolls(true);
        jSplitPane1.setMinimumSize(new java.awt.Dimension(100, 2));
        jSplitPane1.setPreferredSize(new java.awt.Dimension(134, 100));
        jSplitPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jSplitPane1MouseReleased(evt);
            }
        });
        jSplitPane1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jSplitPane1PropertyChange(evt);
            }
        });
        jSplitPane1.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                jSplitPane1VetoableChange(evt);
            }
        });

        leftPanel.setBackground(new java.awt.Color(255, 255, 255));
        leftPanel.setMinimumSize(new java.awt.Dimension(20, 0));
        leftPanel.setPreferredSize(new java.awt.Dimension(100, 10));
        leftPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                leftPanelComponentResized(evt);
            }
        });
        leftPanel.setLayout(null);

        jLabel1.setText("jLabel1");
        jLabel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        leftPanel.add(jLabel1);
        jLabel1.setBounds(0, 0, 100, 30);

        jLabel2.setText("jLabel2");
        leftPanel.add(jLabel2);
        jLabel2.setBounds(0, 30, 100, 30);

        jLabel3.setText("jLabel3");
        leftPanel.add(jLabel3);
        jLabel3.setBounds(0, 60, 100, 30);

        jSplitPane1.setLeftComponent(leftPanel);

        rightPanel.setBackground(new java.awt.Color(255, 255, 255));
        rightPanel.setPreferredSize(new java.awt.Dimension(30, 0));
        rightPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                rightPanelComponentResized(evt);
            }
        });
        rightPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                rightPanelKeyPressed(evt);
            }
        });
        rightPanel.setLayout(null);

        jTextField1.setText("jTextField1");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTextField1PropertyChange(evt);
            }
        });
        rightPanel.add(jTextField1);
        jTextField1.setBounds(0, 0, 130, 30);

        jTextField2.setText("jTextField2");
        rightPanel.add(jTextField2);
        jTextField2.setBounds(0, 30, 130, 30);

        jComboBox1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jComboBox1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jComboBox1FocusLost(evt);
            }
        });
        rightPanel.add(jComboBox1);
        jComboBox1.setBounds(0, 60, 130, 30);

        jSplitPane1.setRightComponent(rightPanel);

        jScrollPane1.setViewportView(jSplitPane1);
        jSplitPane1.getAccessibleContext().setAccessibleName("");

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        reorderItems();
    }//GEN-LAST:event_formComponentResized

    private void jSplitPane1VetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_jSplitPane1VetoableChange

    }//GEN-LAST:event_jSplitPane1VetoableChange

    private void jSplitPane1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jSplitPane1PropertyChange

    }//GEN-LAST:event_jSplitPane1PropertyChange

    private void jSplitPane1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSplitPane1MouseReleased

    }//GEN-LAST:event_jSplitPane1MouseReleased

    private void leftPanelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_leftPanelComponentResized

        reorderItems();
    }//GEN-LAST:event_leftPanelComponentResized

    private void rightPanelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rightPanelKeyPressed

    }//GEN-LAST:event_rightPanelKeyPressed

    private void rightPanelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_rightPanelComponentResized
        reorderItems();
    }//GEN-LAST:event_rightPanelComponentResized

    private void jComboBox1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBox1FocusLost

    }//GEN-LAST:event_jComboBox1FocusLost

    private void jTextField1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTextField1PropertyChange

    }//GEN-LAST:event_jTextField1PropertyChange

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jScrollPane1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane1ComponentResized
        reorderItems();
    }//GEN-LAST:event_jScrollPane1ComponentResized

    public void reorderItems() {
        if (vmobject != null && vmobject.owner.isLoading() == false) {
            PropertyEditorItem item = null;
            int y = -1;
            for (int i = 0; i < liste.size(); i++) {
                item = (PropertyEditorItem) liste.get(i);

                item.label.setSize(new Dimension(leftPanel.getWidth() + 2, itemHeight));
                item.label.setFont(Font.getFont("Tahoma"));
                item.component.setSize(new Dimension(rightPanel.getWidth() + 1, itemHeight));

                item.label.setLocation(-1, y);
                item.component.setLocation(0, y);

                y += itemHeight - 1;
            }
            jSplitPane1.setPreferredSize(new Dimension(100, y));

            updateUI();
        }
    }
    
    public void clear() {
        liste.clear();
        leftPanel.removeAll();
        rightPanel.removeAll();
        repaint();
    }

    public PropertyEditorItem getItem(int index) {
        if (index < liste.size()) {
            return (PropertyEditorItem) liste.get(index);
        } else {
            return null;
        }
    }

    public void addItem(String label, Object value, double min, double max, boolean editable) {
        boolean loading = vmobject.owner.isLoading();
        if (!loading && vmobject.owner.frameCircuit != null && locked == false) {
            PropertyEditorItem item = new PropertyEditorItem(this.mode, vmobject, element, frame, leftPanel, rightPanel, label, value, min, max, editable);
            liste.add(item);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JPanel rightPanel;
    // End of variables declaration//GEN-END:variables

}
