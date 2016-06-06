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
package BasisStatus;

import VisualLogic.*;
import VisualLogic.Element;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class StatusIdle extends Object implements StatusBasisIF {

    private VMObject vmObject;
    private Line myLine = null;
    private PolyPoint px1 = null;
    private PolyPoint px2 = null;
    private JMenu mnuReihenfolge;
    private Line aktuelleLinie = null;
    private Point aktuellePosition;
    private int polyLineIndex;
    public Element aktuellesElement;
    private JPin lastOverPin = null;
    private JPopupMenu popupmenu = new JPopupMenu();
    private JPopupMenu popDraht = new JPopupMenu();
    public Element elx = null;

    public VMObject getBasis() {
        return this.vmObject;
    }

    private String rightePath(String str) {
        return str.replaceAll("/", "\\\\");
    }

    public StatusIdle(VMObject basis) {
        this.vmObject = basis;

        // PopupMenu Draht
        JMenuItem jmiAddNode = new JMenuItem(java.util.ResourceBundle.getBundle("BasisStatus/StatusIdle").getString("AddNode"));
        JMenuItem jmiAddTestpoint = new JMenuItem(java.util.ResourceBundle.getBundle("BasisStatus/StatusIdle").getString("AddTestpoint"));
        popDraht.add(jmiAddNode);
        popDraht.add(jmiAddTestpoint);

        jmiAddNode.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNode(aktuellePosition, aktuelleLinie);
            }
        });

        jmiAddTestpoint.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTestpoint(aktuellePosition, aktuelleLinie);
            }
        });

        // PopupMenu Element
        JMenuItem menuitem1 = new JMenuItem(java.util.ResourceBundle.getBundle("BasisStatus/StatusIdle").getString("Loeschen"));
        JMenuItem menuitem3 = new JMenuItem(java.util.ResourceBundle.getBundle("BasisStatus/StatusIdle").getString("am_Raster_ausrichten"));
        JMenuItem menuitem4 = new JMenuItem(java.util.ResourceBundle.getBundle("BasisStatus/StatusIdle").getString("Code-Editor"));
        //menuitem4.setEnabled(false);
        JMenuItem menuitemElementProperties = new JMenuItem(java.util.ResourceBundle.getBundle("BasisStatus/StatusIdle").getString("ElementProperties"));
        JMenuItem menuitemDocEditor = new JMenuItem(java.util.ResourceBundle.getBundle("BasisStatus/StatusIdle").getString("DocEditor"));
        //menuitemDocEditor.setEnabled(false);
        JMenuItem menuitem5 = new JMenuItem(java.util.ResourceBundle.getBundle("BasisStatus/StatusIdle").getString("Element_Info"));
        

        mnuReihenfolge = new JMenu(java.util.ResourceBundle.getBundle("BasisStatus/StatusIdle").getString("Reihenfolge"));

        JMenuItem jmiReienfolge1 = new JMenuItem(java.util.ResourceBundle.getBundle("BasisStatus/StatusIdle").getString("In_den_Vordergrund"));
        JMenuItem jmiReienfolge2 = new JMenuItem(java.util.ResourceBundle.getBundle("BasisStatus/StatusIdle").getString("In_den_Hintergrund"));
        JMenuItem jmiReienfolge3 = new JMenuItem(java.util.ResourceBundle.getBundle("BasisStatus/StatusIdle").getString("Eine_Ebene_nach_vorne"));
        JMenuItem jmiReienfolge4 = new JMenuItem(java.util.ResourceBundle.getBundle("BasisStatus/StatusIdle").getString("Eine_Ebene_nach_hinten"));

        mnuReihenfolge.add(jmiReienfolge1);
        mnuReihenfolge.add(jmiReienfolge2);
        mnuReihenfolge.add(jmiReienfolge3);
        mnuReihenfolge.add(jmiReienfolge4);

        popupmenu.add(menuitem3);
        popupmenu.add(menuitem1);
        popupmenu.add(menuitem4);
        popupmenu.add(menuitemDocEditor);
        popupmenu.add(menuitemElementProperties);
        popupmenu.add(menuitem5);
        
        popupmenu.add(mnuReihenfolge);

        jmiReienfolge1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (aktuellesElement.classRef != null) {
                    aktuellesElement.inDenVordergrund();
                }
            }
        });
        jmiReienfolge2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (aktuellesElement.classRef != null) {
                    aktuellesElement.inDenHintergrund();
                }
            }
        });
        jmiReienfolge3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (aktuellesElement.classRef != null) {
                    aktuellesElement.eineEbeneNachVorne();
                }

            }
        });
        jmiReienfolge4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (aktuellesElement.classRef != null) {
                    aktuellesElement.eineEbeneNachHinten();
                }
            }
        });

        menuitemDocEditor.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                if (aktuellesElement.classRef != null) {
                    for (int i = 0; i < getBasis().getElementCount(); i++) {
                        Element element = getBasis().getElement(i);
                        if (element.isSelected()) {
                            String htmlEditor = getBasis().owner.getFrameMain().settings.getHTMLEditor();

                            if (htmlEditor == null || htmlEditor.equalsIgnoreCase("") || new File(htmlEditor).exists() == false) {
                                Tools.showMessage(java.util.ResourceBundle.getBundle("BasisStatus/StatusIdle").getString("HTML-Editor_not_found!"));
                            } else {
                                //String binPath = element.elementPath + element.mainPath + "/bin/";

                                String docFileName = "";

                                String mainPath = element.docPath;

                                //Custom button text
                                Object[] options = {"DE", "EN", "ES"};
                                int n = JOptionPane.showOptionDialog(null,
                                        java.util.ResourceBundle.getBundle("BasisStatus/StatusIdle").getString("LANGUAGE"),
                                        java.util.ResourceBundle.getBundle("BasisStatus/StatusIdle").getString("QUESTION"),
                                        JOptionPane.YES_NO_CANCEL_OPTION,
                                        JOptionPane.QUESTION_MESSAGE,
                                        null,
                                        options,
                                        options[2]);

                                if (n == 0) {
                                    docFileName = mainPath + "doc.html";
                                }
                                if (n == 1) {
                                    docFileName = mainPath + "doc_en.html";
                                }
                                if (n == 2) {
                                    docFileName = mainPath + "doc_es.html";
                                }

                                if (!new File(docFileName).exists()) {
                                    if (n == 0) {
                                        docFileName = mainPath + "doc_de/index.html";
                                    }
                                    if (n == 1) {
                                        docFileName = mainPath + "doc_en/index.html";
                                    }
                                    if (n == 2) {
                                        docFileName = mainPath + "doc_es/index.html";
                                    }
                                }
                                
                                
                                if (n > -1) {
                                    File f = new File(docFileName);
                                    f.getParentFile().mkdirs();
                                                                        
                                    if (f.isDirectory())
                                    {
                                        f.delete();
                                    }
                                    
                                    if (f.exists() == false) {
                                        try {
                                            f.createNewFile();
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                    try {
                                        Runtime.getRuntime().exec(htmlEditor + " " + docFileName);
                                    } catch (IOException ex) {
                                        Tools.showMessage("can not open: "+htmlEditor);
                                    }
                                }
                            }
                            break;
                        }
                    }
                }

            }
        });

        menuitemElementProperties.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {

                if (aktuellesElement.classRef != null) {
                    for (int i = 0; i < getBasis().getElementCount(); i++) {
                        Element element = getBasis().getElement(i);
                        if (element.isSelected()) {
                            codeeditor.frmDefinitonDefEditor frm = new codeeditor.frmDefinitonDefEditor(getBasis().owner.frameCircuit, true);
                            frm.execute(element.elementPath + element.mainPath);
                            break;
                        }
                    }
                }

            }
        });

        menuitem1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {

                getBasis().owner.deleteAnythingSelected();
                getBasis().owner.saveForUndoRedo();

            }
        });
        menuitem3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (aktuellesElement.classRef != null) {
                    for (int i = 0; i < getBasis().getElementCount(); i++) {
                        Element element = getBasis().getElement(i);
                        if (element.isSelected()) {
                            element.amRasterAusrichten();
                        }
                    }
                    getBasis().reorderWireFrames();
                }
            }
        });

        menuitem4.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (aktuellesElement != null) {

                    String strX = FrameMain.elementPath + aktuellesElement.definitionPath;
                    File file = new File(strX);
                    if (file.exists()) {
                        DFProperties thisDirProps = Tools.getProertiesFromDefinitionFile(file);

                        if (thisDirProps.vm.contains(".vlogic")) {
                             getBasis().owner.frameCircuit.openElement(strX);
                        } else {
                            getBasis().owner.frameCircuit.openJavaEditor(aktuellesElement);
                        }
                    } else {
                        Tools.showMessage(null, "Not Possible!");
                    }
                }
            }
        });

        menuitem5.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showElementInfo();
            }
        });


    }

    @Override
    public void mouseDblClick(MouseEvent e) {
        if (vmObject.owner.vmProtected) {
            return;
        }
        if (aktuellesElement != null) {

            if (aktuellesElement.getInternName().equalsIgnoreCase("###GRAPHICSPATH###")) {
                vmObject.setModusPathEditor(e, aktuellesElement);
                return;
            }

            if (vmObject == vmObject.owner.getFrontBasis()) {
                if (aktuellesElement.circuitElement != null) {

                    vmObject.owner.disableAllElements();
                    vmObject.owner.ownerVMPanel.jTabbedPane1.setSelectedIndex(0);

                    ((Element) aktuellesElement.circuitElement).setSelected(true);
                    ((Element) aktuellesElement.circuitElement).processPropertyEditor();
                }
            } else if (aktuellesElement.panelElement != null) {

                vmObject.owner.disableAllElements();
                vmObject.owner.ownerVMPanel.jTabbedPane1.setSelectedIndex(1);

                ((Element) aktuellesElement.panelElement).setSelected(true);
                ((Element) aktuellesElement.panelElement).processPropertyEditor();
            }
        }
    }

    public void showElementInfo() {
        if (aktuellesElement != null) {
            BasisStatus.frmElementInfo frm = new BasisStatus.frmElementInfo(null, true);

            Element el = aktuellesElement;
            frm.init(el.getInternName(), el.classPath, el.infoProgrammer, el.infoCopyrights, el.infoOther, el.getID());

            frm.setVisible(true);
        }
    }

    public void openFile(String fileName) {
        vmObject.openVlogicFile(fileName);
    }

    @Override
    public void elementPinMouseReleased(MouseEvent e, int elementID, int pin) {

    }

    @Override
    public void elementPinMousePressed(MouseEvent e, int elementID, int pin) {
        if (vmObject.owner.vmProtected) {
            return;
        }
        // wenn auf Pin die Maustaste losgelassen wurde, dann Wireframe ziehen 
        JPin xpin = ((Element) vmObject.getObjectWithID(elementID)).getPin(pin);
        if (xpin != null && xpin.draht == null && (xpin.pinIO == JPin.PIN_OUTPUT || xpin.pinIO == JPin.PIN_INPUT_OUTPUT)) //if (xpin!=null && xpin.draht==null && xpin.pinIO==JPin.PIN_OUTPUT)
        {
            vmObject.setModusAddWireFrame(elementID, pin);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    @Override
    public void elementPinMouseMoved(MouseEvent e, int elementID, int pin) {
        if (vmObject.owner.vmProtected) {
            return;
        }
        Element element = (Element) vmObject.getObjectWithID(elementID);
        lastElement = element;

        mouseMoved(e);
    }

    public void finalize(Element destElement, int destPin) {
        /*Point p = destElement.getPinPosition(destPin);        
    xPoints[nPoints]=p.x;
    yPoints[nPoints]=p.y;
    nPoints++;       
    Draht draht = basis.addNode(sourceElement,sourcePin,destElement,destPin);                
    for (int i=0;i<nPoints;i++)
    {
    int x = xPoints[i];
    int y = yPoints[i];
    draht.setPoint(x,y);
    }      
    basis.repaint();*/
    }

    public void vergroesereAllSelectedElements(int x, int y) {
        for (int i = 0; i < getBasis().getElementCount(); i++) {
            Element element = getBasis().getElement(i);
            if (element.isSelected()) {
                int xx = element.getWidth();
                int yy = element.getHeight();
                element.setSize(xx + x, yy + y);
            }
        }
        /*if (vmObject==vmObject.owner.getCircuitBasis())
    {
    // Move All Selected Draehte
    Draht draht;
    for (int i=0;i<vmObject.getDrahtCount();i++) 
    {
    draht = vmObject.getDraht(i);
    for (int j=0;j<draht.getPolySize();j++)
    {
    PolyPoint p = draht.getPoint(j);
    if (p.isSelected()==true)
    {
    int nX,nY;
    draht.setPoint(j,p.getX()+x,p.getY()+y);
    }
    }
    }     
    }
    vmObject.reorderWireFrames();*/
    }

    public void verschiebeAllSelectedElements(int x, int y) {
        for (int i = 0; i < getBasis().getElementCount(); i++) {
            Element element = getBasis().getElement(i);
            if (element.isSelected()) {
                int xx = element.getX();
                int yy = element.getY();
                element.setLocation(xx + x, yy + y);
            }
        }
        if (vmObject == vmObject.owner.getCircuitBasis()) {
            // Move All Selected Draehte
            Draht draht;
            for (int i = 0; i < vmObject.getDrahtCount(); i++) {
                draht = vmObject.getDraht(i);

                for (int j = 0; j < draht.getPolySize(); j++) {

                    PolyPoint p = draht.getPoint(j);
                    if (p.isSelected() == true) {
                        int nX, nY;

                        draht.setPoint(j, p.getX() + x, p.getY() + y);
                    }
                }
            }
        }
        vmObject.reorderWireFrames();

    }
    private boolean isShiftPressed = false;
    private boolean isControlPressed = false;

    @Override
    public void processKeyEvent(KeyEvent ke) {
        if (vmObject.owner.vmProtected) {
            return;
        }
        int code = ke.getKeyCode();

        if (ke.getID() == KeyEvent.KEY_RELEASED) {
            isShiftPressed = false;
            isControlPressed = false;
        }

        if (ke.getID() == KeyEvent.KEY_PRESSED) {

            if (code == KeyEvent.VK_SHIFT) {
                isShiftPressed = true;
            }

            if (code == KeyEvent.VK_CONTROL) {
                isControlPressed = true;
            }


            /*if (code==ke.VK_F1)
            {            
            aktuellesElement=vmObject.getSelectedElement();
            if (aktuellesElement!=null) 
            {
            vmObject.owner.frameCircuit.openElementDocFile(aktuellesElement.docFileName,aktuellesElement);
            }
            }*/
            if (code == KeyEvent.VK_DELETE) {
                vmObject.owner.deleteAnythingSelected();
                getBasis().owner.saveForUndoRedo();
            }

            aktuellesElement = vmObject.getSelectedElement();
            if (aktuellesElement != null) {
                int x = aktuellesElement.getX();
                int y = aktuellesElement.getY();

                if (isShiftPressed && !isControlPressed && code == KeyEvent.VK_LEFT) {
                    verschiebeAllSelectedElements(-1, 0);
                } else if (isShiftPressed && !isControlPressed && code == KeyEvent.VK_RIGHT) {
                    verschiebeAllSelectedElements(+1, 0);
                } else if (isShiftPressed && !isControlPressed && code == KeyEvent.VK_UP) {
                    verschiebeAllSelectedElements(0, -1);
                } else if (isShiftPressed && !isControlPressed && code == KeyEvent.VK_DOWN) {
                    verschiebeAllSelectedElements(0, +1);
                }

                if (vmObject == vmObject.owner.getFrontBasis()) {
                    if (isShiftPressed && isControlPressed && code == KeyEvent.VK_LEFT) {
                        vergroesereAllSelectedElements(-1, 0);
                    } else if (isShiftPressed && isControlPressed && code == KeyEvent.VK_RIGHT) {
                        vergroesereAllSelectedElements(+1, 0);
                    } else if (isShiftPressed && isControlPressed && code == KeyEvent.VK_UP) {
                        vergroesereAllSelectedElements(0, -1);
                    } else if (isShiftPressed && isControlPressed && code == KeyEvent.VK_DOWN) {
                        vergroesereAllSelectedElements(0, +1);
                    }
                }

            }
        }

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (vmObject.owner.vmProtected) {
            return;
        }
        Point p = new Point(e.getX(), e.getY());

        if (myLine != null) {
            Draht draht = myLine.getDraht();

            int direction = myLine.getDirection();
            if (direction == 1) {
                // Vertikal                
                if (px1 != null && px2 != null) {
                    px1.setLocation(p.x, px1.getY());
                    px2.setLocation(p.x, px2.getY());
                }
            } else // Horizontal
             if (px1 != null && px2 != null) {
                    px1.setLocation(px1.getX(), p.y);
                    px2.setLocation(px2.getX(), p.y);
                }
            vmObject.reorderWireFrames();

            return;
        }

        if (e.getSource() instanceof Element || e.getSource() instanceof SelectionPane) {
            if (e.getSource() instanceof Element) {
                Element element = (Element) e.getSource();
                element.setSelected(true);
                e.setSource(element.layeredPane);
            }

            vmObject.setModusMoveElements(e);
        } else if (e.getX() >= vmObject.getWidth() - 10 && e.getY() >= vmObject.getHeight() - 10 && e.getX() <= vmObject.getWidth() && e.getY() <= vmObject.getHeight()) {
            vmObject.setModusResizeBasis(e.getX(), e.getY());
        } else {
            vmObject.setModusGummiband(e.getX(), e.getY());
        }

    }

    private int getResizeRect(Element element, int x, int y) {

        if (element.resizable) {
            int dist = 6;

            // Left-Top
            if (x > 0 && y > 0 && x < dist && y < dist) {
                return 1;
            } else // Top
            {
                if (x > element.getWidth() / 2 - dist / 2 && y > 0 && x < element.getWidth() / 2 + dist && y < dist) {
                    return 2;
                } else // Top-Width
                {
                    if (x > element.getWidth() - dist && y > 0 && x < element.getWidth() && y < dist) {
                        return 3;
                    } else // Left
                    {
                        if (x > 0 && y > element.getHeight() / 2 - dist / 2 && x < dist && y < element.getHeight() / 2 + dist / 2) {
                            return 4;
                        } else // Width
                        {
                            if (x > element.getWidth() - dist && y > element.getHeight() / 2 - dist / 2 && x < element.getWidth() + dist && y < element.getHeight() / 2 + dist / 2) {
                                return 5;
                            } else //Left-Height
                            {
                                if (x > 0 && y > element.getHeight() - dist && x < dist && y < element.getHeight() + dist) {
                                    return 6;
                                }
                            }
                        }
                    }
                }
            }
            // Height
            if (x > element.getWidth() / 2 - dist / 2 && y > element.getHeight() - dist && x < element.getWidth() / 2 + dist / 2 && y < element.getHeight() + dist) {
                return 7;
            } else // Width-Height
            {
                if (x > element.getWidth() - 10 && y > element.getHeight() - 10 && x < element.getWidth() && y < element.getHeight()) {
                    return 8;
                } else {
                    return 0;
                }
            }

        }
        return 0;
    }

    public Element getAktuellesElement() {
        return aktuellesElement;
    }

    private void addTestpoint(Point p, Line line) {
        if (line != null && p != null) {
            Draht draht = line.getDraht();
            Element node = Tools.addTestpoint(vmObject);
            Tools.addNodeIntoLine(vmObject, node, p, line);
            vmObject.reorderWireFrames();
            vmObject.setCursor(Cursor.getDefaultCursor());
        }
    }

    private void addNode(Point p, Line line) {
        if (line != null && p != null) {
            Draht draht = line.getDraht();
            Element node = Tools.addNode(vmObject);
            Tools.addNodeIntoLine(vmObject, node, p, line);
            vmObject.reorderWireFrames();
            vmObject.setCursor(Cursor.getDefaultCursor());
        }
    }

    /*
     * wurde aus ein PolyLine Punkt gedrueckt
     **/
    public void mousePressed(MouseEvent e) {
        if (vmObject.owner.vmProtected) {
            return;
        }
        Point p = new Point(e.getX(), e.getY());

        aktuelleLinie = null;
        aktuellePosition = p;

        Line line = vmObject.getLineInNaehe(p);
        if (line != null && isControlPressed) {
            addNode(p, line);

            return;
        }

        if (line != null && e.getButton() == 3) {
            aktuelleLinie = line;
            popDraht.show(vmObject, e.getX(), e.getY());
        }

        myLine = line;
        //myLine = vmObject.getLineInNaehe(p);
        if (myLine != null && !(e.getSource() instanceof SelectionPane)) {
            vmObject.owner.disableAllElements();
            px1 = myLine.p1;
            px2 = myLine.p2;
            return;
        }
        if (e.getSource() instanceof SelectionPane) {
            SelectionPane pane = (SelectionPane) e.getSource();
            Element element = pane.getElement();

            if (element.isSelected() == false) {
                vmObject.owner.disableAllElements();
            }

            element.setSelected(true);
        }

        if (e.getSource() instanceof SelectionPane && e.getButton() == e.BUTTON1) {
            SelectionPane pane = (SelectionPane) e.getSource();
            Element element = pane.getElement();

            if (getBasis().owner.frameCircuit != null) {
                getBasis().owner.frameCircuit.activate_DocFrame(element);
            }

            if (e.getButton() == e.BUTTON1) {
                if (element != null) {
                    element.processPropertyEditor();
                    if (element.classRef != null) {
                        element.classRef.xonMousePressedOnIdle(e);
                    }

                }
            }

            if (element.isSelected()) {
                aktuellesElement = element;

                int rect = getResizeRect(element, e.getX(), e.getY());

                if (rect > 0) {
                    vmObject.setModusResizeElement(element, rect);
                    return;
                }
            }
        }
        // Ueberpruefe ob die Maus ueber einer Line steht
        {
            if (e.getSource() instanceof SelectionPane) {
                SelectionPane pane = (SelectionPane) e.getSource();
                Element element = pane.getElement();
                aktuellesElement = element;

                if (e.getButton() == e.BUTTON3) {
                    if (vmObject.owner.getFrontBasis() == vmObject) {
                        mnuReihenfolge.setEnabled(true);
                    } else {
                        mnuReihenfolge.setEnabled(false);
                    }

                    Element el = getAktuellesElement();
                    if (el != null) {
                        JMenuItem item;
                        for (int i = 0; i < el.menuItems.size(); i++) {
                            item = (JMenuItem) el.menuItems.get(i);
                            if (item != null) {
                                popupmenu.add(item);
                            }
                        }
                    }

                    popupmenu.addPopupMenuListener(new PopupMenuListener() {

                        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                        }

                        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                            removeElementPopupMenuItems();
                        }

                        public void popupMenuCanceled(PopupMenuEvent e) {
                            removeElementPopupMenuItems();
                        }
                    });

                    popupmenu.show(vmObject, element.getX() + e.getX(), element.getY() + e.getY());

                }

            } else {
                
                if (getBasis().owner.frameCircuit != null) {
                getBasis().owner.frameCircuit.activate_DocFrame(null);
            }
                
                vmObject.owner.disableAllElements();
                Draht draht;

                vmObject.requestFocus();
                vmObject.requestFocus();

                if (e.getButton() == e.BUTTON1) {
                    vmObject.processPropertyEditor();
                }

                for (int i = 0; i < vmObject.getDrahtCount(); i++) {
                    draht = vmObject.getDraht(i);

                    polyLineIndex = Tools.isPointInDrahtPoint(draht, e.getX(), e.getY());
                    if (polyLineIndex > -1) {
                        //System.out.println("Punkt befindet sich in PolyLine Punkt:"+polyLineIndex);                    

                        //System.out.println("");                    
                        break;
                    }
                }
            }
        }

    }

    public void removeElementPopupMenuItems() {
        Element el = getAktuellesElement();
        if (el != null) {
            JMenuItem item;
            for (int i = 0; i < el.menuItems.size(); i++) {
                item = (JMenuItem) el.menuItems.get(i);
                if (item != null) {
                    popupmenu.remove(item);
                }
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (vmObject.owner.vmProtected) {
            return;
        }
        vmObject.owner.setChanged(true);
        myLine = null;
        int x = e.getX();
        int y = e.getY();

        isShiftPressed = false;
        isControlPressed = false;

        Point p = new Point(x, y);

        Line line = vmObject.getLineInNaehe(p);
        if (line != null && !(e.getSource() instanceof SelectionPane)) {
            Draht draht = line.getDraht();

            vmObject.reorderWireFrames();

            //basis.getCommandProcessor().submit(cmdWire);
            if (e.getModifiers() == InputEvent.CTRL_MASK + InputEvent.BUTTON1_MASK) {
                // ermittle die zwei PolyPoints der Linie implements Draht
                Point p1 = line.getStartPoint();
                Point p2 = line.getEndPoint();

                int p1Index = Tools.getPolyPointIndex(draht, p1);
                int p2Index = Tools.getPolyPointIndex(draht, p2);

                int mx = (p1.x + p2.x) / 2;
                int my = (p1.y + p2.y) / 2;

                if (p1Index < p2Index) {
                    PolyPoint px = draht.insertPoint(p2Index, x, y);
                } else {
                    PolyPoint px = draht.insertPoint(p1Index, x, y);
                }

                vmObject.repaint();
                // erzeuge zwischen den 2 polyPoints ein Verteiler-Element
            } else {
                draht.setSelected(true);
            }

        }

    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
    private Element lastElement = null;
    private Line lastLine = null;

    public void mouseMoved(MouseEvent e) {
        if (vmObject.owner.vmProtected) {
            return;
        }
        Point p = new Point(e.getX(), e.getY());

        //JLabel lbl = getBasis().owner.frameCircuit.layedLabel;
        if (vmObject.newElement != null) {
            String[] params = vmObject.newElement;

            String mainPath = params[0];
            String circuitClass = params[1];
            String frontClass = params[2];
            String icon = params[3];

            String[] args = null;

            if (params.length == 6) {
                if (params[5].equalsIgnoreCase("LOADER")) {
                    args = params;
                }
            }
            if (circuitClass.endsWith("vlogic")) {
                String theName = frontClass;
                args = new String[3];
                args[0] = mainPath + "/" + circuitClass;
                args[1] = theName;
                args[2] = icon;
                mainPath = "/FrontElements/Version_2_0/VMElement";
                circuitClass = "VMElement";

                if (params[4].length() > 0) {
                    frontClass = "VMPanel";
                } else {
                    frontClass = "";
                }
            }

            try {
                vmObject.processAddElement(mainPath, circuitClass, frontClass, args);
            } catch (Exception ex) {
                vmObject.newElement = null;
            }
        }

        if (e.getX() >= vmObject.getWidth() - 10 && e.getY() >= vmObject.getHeight() - 10 && e.getX() <= vmObject.getWidth() && e.getY() <= vmObject.getHeight()) {
            vmObject.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
            return;
        }

        if (vmObject != null) {
            vmObject.setCursor(Cursor.getDefaultCursor());
        }
        if (lastOverPin != null) {
            JPin pin = lastOverPin;

            Element element = (Element) pin.pinIF;
            //lastOverPin.paint(element.getX(),element.getY(), g2, false);
            lastOverPin.setHighlighted(false);

            lastOverPin = null;
        }

        if (e.getSource() instanceof JPin) {
            JPin pin = (JPin) e.getSource();

            getBasis().owner.frameCircuit.showPinDescription(pin);

            if (pin.draht == null) {
                Element element = (Element) pin.pinIF;

                //Graphics2D g2 = (Graphics2D)vmObject.getGraphics();
                pin.setHighlighted(true);
                //g2.drawOval(element.getX()+pin.getX()+1,element.getY()+pin.getY()+1,7,7);

                lastOverPin = pin;

                if (vmObject != null) {
                    vmObject.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                }
                return;
            } else {
                //basis.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            }
        } else {
            getBasis().owner.frameCircuit.removePinDescription();
        }

        if (e.getSource() instanceof SelectionPane) {
            SelectionPane pane = (SelectionPane) e.getSource();
            Element element = pane.getElement();
            lastElement = element;

            if (element.isSelected() && !element.isResizeSynchron()) {
                lastElement = element;
                if (vmObject != null) {
                    int rect = getResizeRect(element, e.getX(), e.getY());
                    switch (rect) {
                        case 0:
                            vmObject.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                            break;
                        case 1:
                            vmObject.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
                            break;
                        case 2:
                            vmObject.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                            break;
                        case 3:
                            vmObject.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
                            break;
                        case 4:
                            vmObject.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                            break;
                        case 5:
                            vmObject.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                            break;
                        case 6:
                            vmObject.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
                            break;
                        case 7:
                            vmObject.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
                            break;
                        case 8:
                            vmObject.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
                            break;
                    }
                }
            } else if (element.isResizeSynchron()) {
                int rect = getResizeRect(element, e.getX(), e.getY());
                if (rect == 8) {
                    vmObject.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
                }
            }

        }

        if ((e.getSource() instanceof VMObject) && lastElement != null) {
            lastElement = null;
        }


        /*if (vmObject==vmObject.owner.getCircuitBasis())
        {
        if (e.getSource() instanceof Element )
        {            
        elx=(Element)e.getSource();
        java.awt.EventQueue.invokeLater(new Runnable()
        {
        public void run()
        {
        if (elx!=null)
        {
        elx.highlighted=false;
        elx.repaint();
        elx=null;
        }
        if (elx!=null)
        {
        elx.highlighted=true;
        elx.repaint();
        }
        }
        });                        
        }else
        {
        if (elx!=null)
        {
        elx.highlighted=false;
        elx.repaint();
        elx=null;
        }
        }
        }*/
        if (e.getSource() instanceof VMObject) {

            //Point p=new Point(e.getX(),e.getY());
            Line line = vmObject.getLineInNaehe(p);

            if (lastLine != null) {
                lastLine.getDraht().setHighLight(false);
                lastLine = null;
            }

            if (line != null && !(e.getSource() instanceof Element)) {
                line.getDraht().setHighLight(true);
                lastLine = line;
            }

            if (line != null && !(e.getSource() instanceof Element)) {
                //line.
                int direction = line.getDirection();
                if (direction == 1) {
                    // Vertikal                
                    if (vmObject != null) {
                        vmObject.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                    }
                } else if (vmObject != null) {
                    vmObject.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                }

            } else {

            }
        }

    }

    public void draw(Graphics g) {

    }
}
