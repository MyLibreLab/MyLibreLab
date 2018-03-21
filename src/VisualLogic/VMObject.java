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

import BasisStatus.StatusEditPath;
import BasisStatus.StatusNOP;
import BasisStatus.StatusNone;
import SimpleFileSystem.FileSystemInput;
import SimpleFileSystem.FileSystemOutput;
import BasisStatus.StatusAddElement;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.awt.image.*;
//import com.sun.image.codec.jpeg.*;

import VisualLogic.variables.*;
import VisualLogic.variables.VSString;
import BasisStatus.StatusMoveElements;
import BasisStatus.StatusAddWire;
import BasisStatus.StatusIdle;
import BasisStatus.StatusRun;
import BasisStatus.StatusGummiBand;
import BasisStatus.StatusBasisIF;
import BasisStatus.StatusResizeElement;
import BasisStatus.StatusResizeBasis;
import Peditor.BasisProperty;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import javax.imageio.ImageIO;

/**
 *
 * @author Homer
 */
public class VMObject extends JPanel implements MouseListener, MouseMotionListener, VMObjectIF, ElementIF, Runnable, Printable {

    private ExternalIF element;
    private final int maxElements = 2000;
    public ArrayList propertyList = new ArrayList();
    public ArrayList clockList = new ArrayList();
    /*public ArrayList subRoutineList = new ArrayList();
    public SubRoutine searchSubRoutine(String name)
    {
    name=name.trim();
    for (int i=0;i<subRoutineList.size();i++)
    {
    SubRoutine rtn=(SubRoutine)subRoutineList.get(i);
    if (rtn.name.trim().equalsIgnoreCase(name))
    {
    return rtn;
    }
    }
    return null;
    }*/
    public DialogWait frmWait = null;
    private boolean isThreadFertig = true;

    private boolean getIsThreadFertig() {
        return isThreadFertig;
    }
    private Object elementReferences[] = new Object[maxElements];
    // fuer Basis als Element!
    private Image elementImage = null;
    private Element elTopBar = null;
    private Element elLeftBar = null;
    private Element elRightBar = null;
    private Element elBottomBar = null;
    private VSInteger vsWidth = new VSInteger(500);
    private VSInteger vsHeight = new VSInteger(500);
    private VSProperties vsProperties = new VSProperties();
    private VSColor vsColor = new VSColor(Color.LIGHT_GRAY);
    private VSString vsCaption = new VSString("");
    private VSBoolean vsShowToolbar = new VSBoolean(false);
    private VSBoolean vsUnDecorate = new VSBoolean(false);
    private VSBoolean vsAlwaysOnTop = new VSBoolean(false);
    private VSComboBox vsWindowsPosition = new VSComboBox();
    
    private VSInteger vsCustomXwindowPos = new VSInteger(0);
    private VSInteger vsCustomYwindowPos = new VSInteger(0);
    
    private int elementsCount = 0;
    private ArrayList elList;
    private boolean borderVisible = true;
    public ArrayList drahtLst;
    private String elementPath;
    private ArrayList elementListe;    
    private boolean aktuellIstBasis = false;
    private boolean graphikLocked = false;    
    private int width = 500;  // Default Value!
    private int height = 500; // Default Value!
    
    public Element aktuellesElement = null;
    public Point subLocation = new Point(0, 0);
    public boolean isBasisResizePinVisible = true;

    public boolean isBorderVisible() {
        return borderVisible;
    }

    public void setBorderVisible(boolean value) {
        borderVisible = value;
    }
    public ArrayList processList = new ArrayList();

    //public ArrayList processList = new ArrayList();
    public ArrayList getElementList() {
        return elList;
    }

    public Rectangle getVMBounds() {
        int minX = 9999;
        int minY = 9999;
        int maxX = -9999;
        int maxY = -9999;

        Element element;
        for (int i = 0; i < getElementCount(); i++) {
            element = getElement(i);

            if (element.getInternName().equalsIgnoreCase("#PININPUT_INTERNAL#") || element.getInternName().equalsIgnoreCase("#PINOUTPUT_INTERNAL#")) {

            } else {
                if (element.getX() < minX) {
                    minX = element.getX();
                }
                if (element.getY() < minY) {
                    minY = element.getY();
                }

                if (element.getX() + element.getWidth() > maxX) {
                    maxX = element.getX() + element.getWidth();
                }
                if (element.getY() + element.getHeight() > maxY) {
                    maxY = element.getY() + element.getHeight();
                }
            }
        }

        return new Rectangle(minX, minY, maxX, maxY);
    }

    public void moveAllElements(int x, int y) {

        Element element;
        for (int i = 0; i < getElementCount(); i++) {
            element = getElement(i);
            if (element.getInternName().equalsIgnoreCase("#PININPUT_INTERNAL#") || element.getInternName().equalsIgnoreCase("#PINOUTPUT_INTERNAL#")) {

            } else {
                Point p = element.getLocation();
                element.setLocation(p.x + x, p.y + y);
            }
        }
    }

    public void moveAllDrahts(int x, int y) {

        Draht draht;
        for (int i = 0; i < getDrahtCount(); i++) {
            draht = getDraht(i);

            for (int j = 0; j < draht.getPolySize(); j++) {
                PolyPoint p = draht.getPoint(j);

                p.setLocation(p.getX() + x, p.getY() + y);
            }

        }
    }

    private void moveOutputPins(int x) {
        int y = 50;
        Element element;
        for (int i = 0; i < getElementCount(); i++) {
            element = getElement(i);
            if (element.getInternName().equalsIgnoreCase("#PINOUTPUT_INTERNAL#")) {
                element.setLocation(x, y);
                y += 50;
            }
        }
    }

    public void reorderInputsOutputs() {
        Element element;
        for (int i = 0; i < getElementCount(); i++) {
            element = getElement(i);
            if (element.getInternName().equalsIgnoreCase("#PININPUT_INTERNAL#")) {
                Draht draht = element.getPin(0).draht;

                if (draht != null) {
                    PolyPoint p1 = draht.getPoint(1);
                    PolyPoint p2 = draht.getPoint(2);

                    JPin srcPin = element.getPin(0);

                    Element destElement = getElement(draht.getDestElementID());
                    JPin destPin = destElement.getPin(draht.getDestPin());

                    int d1 = element.getLocation().x + srcPin.getX();
                    int d2 = destElement.getLocation().x + destPin.getX();
                    int dx = (d1 + d2) / 2;
                    p1.setLocation(dx, 10);
                    p2.setLocation(dx, 10);
                }
            } else if (element.getInternName().equalsIgnoreCase("#PINOUTPUT_INTERNAL#")) {
                Draht draht = element.getPin(0).draht;

                if (draht != null) {
                    PolyPoint p1 = draht.getPoint(1);
                    PolyPoint p2 = draht.getPoint(2);

                    JPin dstPin = element.getPin(0);

                    Element srcElement = getElement(draht.getSourceElementID());
                    JPin srcPin = srcElement.getPin(draht.getSourcePin());

                    int d1 = element.getLocation().x + dstPin.getX();
                    int d2 = srcElement.getLocation().x + srcPin.getX();
                    int dx = (d1 + d2) / 2;
                    p1.setLocation(dx, 10);
                    p2.setLocation(dx, 10);
                }
            }

        }
    }

    public void pack() {
        Rectangle rect = getVMBounds();

        moveAllElements(-rect.x + 100, -rect.y + 50);
        moveAllDrahts(-rect.x + 100, -rect.y + 50);

        moveOutputPins(rect.width - rect.x + 2 * 100 - 50);

        reorderInputsOutputs();

        reorderWireFrames();
    }
    /* public int getWidth() {return width;}
    public int getHeight() {return height;}
    public void setSize(int w, int h)
    {
    if (panel!=null)
    {
    width=w;
    height=h;
    panel.setSize(new Dimension(width,height));
    panel.setPreferredSize(new Dimension(width,height));
    panel.revalidate();
    }
    }*/
    private Thread thread = null;
    public String[] newElement = null;
    // zustaende
    public StatusIdle leerLauf; // leerLauf faengt die Ereignisse implements Leerlauf
    private StatusAddWire addWireFrame;
    private StatusGummiBand gummiBand;
    private StatusResizeBasis resizeBasis;
    private StatusAddElement addElementModus;
    private StatusMoveElements moveElements;
    private StatusEditPath splineEdit;
    private StatusResizeElement resizeElement;
    public StatusBasisIF status;
    private StatusRun modusRun;
    public boolean stop = true;
    private boolean pause = false;

    public void setModusNop() {
        status = new StatusNOP();
    }
    private int processCounter = 0; // fuer den JProgrammBar um zu Visualisieren das was am laufen ist!
    // werden nur genutzt, wenn die Basis als Element benutzt wird!
    private Font stdFont = new Font("Courier", 0, 8);
    public String settingsTitel = "";
    private ArrayList topPins = new ArrayList();
    private ArrayList rightPins = new ArrayList();
    private ArrayList bottomPins = new ArrayList();
    private ArrayList leftPins = new ArrayList();
    private static long time1;
    private static long time2;
    private static int counter = 0;
    private boolean alignToGrid = false;

    public boolean isAlignToGrid() {
        return alignToGrid;
    }

    public void setAlignToGrid(boolean value) {
        alignToGrid = value;
    }
    private boolean rasterOn = false;

    public boolean isRasterOn() {
        return rasterOn;
    }

    public void setRasterOn(boolean value) {
        rasterOn = value;
    }
    private int rasterX = 10;
    private int rasterY = 10;

    public int getRasterX() {
        return rasterX;
    }

    public int getRasterY() {
        return rasterY;
    }

    public void setRaster(int x, int y) {
        rasterX = x;
        rasterY = y;
    }
    public Basis owner;

    public Basis getOwner() {
        return owner;
    }

    public void adjustAllElemnts() {
        for (int i = 0; i < getElementCount(); i++) {
            getElement(i).adjustSubElements();
        }
    }

    public ExternalIF[] getElementList(VSBasisIF basis, String elementName) {
        ArrayList liste = new ArrayList();

        VMObject vm = ((Basis) basis).getCircuitBasis();
        int c = 0;
        for (int i = 0; i < vm.getComponentCount(); i++) {
            Component comp = vm.getComponent(i);
            if (comp instanceof Element) {
                Element elementx = (Element) comp;
                String str = elementx.getInternName();
                if (str.equalsIgnoreCase(elementName)) {
                    liste.add(elementx);
                    //System.out.println("element="+element.jGetTag(0));
                }
            }
        }

        Collections.sort(liste);
        ExternalIF[] result = new ExternalIF[liste.size()];
        for (int i = 0; i < liste.size(); i++) {
            result[i] = (ExternalIF) liste.get(i);
        }
        return result;
    }

    public boolean errorExist() {
        Draht draht;
        for (int i = 0; i < drahtLst.size(); i++) {
            draht = getDraht(i);

            if (draht.isValid() == false) {
                return true;
            }
        }
        return false;
    }

    public void checkPins() {
        if (!owner.isLoading()) {

            Draht draht;
            for (int i = 0; i < drahtLst.size(); i++) {
                draht = getDraht(i);

                Element sourceElement = (Element) getObjectWithID(draht.sourceElementID);
                Element destElement = (Element) getObjectWithID(draht.destElementID);

                if (sourceElement != null && destElement != null) {
                    JPin sourcePin = sourceElement.getPin(draht.sourcePin);
                    JPin destPin = destElement.getPin(draht.destPin);

                    if (sourcePin != null && destPin != null) {
                        int sdt = sourcePin.dataType;
                        int ddt = destPin.dataType;

                        if (sdt == ExternalIF.C_VARIANT || ddt == ExternalIF.C_VARIANT) {
                            draht.setValid(true);
                        } else if (sdt == ddt) {
                            draht.setValid(true);
                        } else {
                            draht.setValid(false);
                        }
                    } else {
                        draht.setValid(false);
                    }
                } else {
                    draht.setValid(false);
                }
            }
        }
    }

    public void addPublishingFiles(ArrayList list) {
        Element el;
        for (int i = 0; i < getElementCount(); i++) {
            el = getElement(i);
            el.addPublishingFiles(list);
        }
    }
    private Color backgroundColor = Color.GRAY;

    public Color getBackground() {
        return backgroundColor;
    }

    public void setBackground(Color color) {
        backgroundColor = color;
        super.setBackground(color);
    }

    public void propertyChanged(Object referenz) {
        setSize(vsWidth.getValue(), vsHeight.getValue());

        if (this == owner.getFrontBasis()) {
            setBackground(vsColor.getValue());
            owner.caption = vsCaption.getValue();
            owner.showToolBar = vsShowToolbar.getValue();
            owner.unDecorated = vsUnDecorate.getValue();
            owner.AlwaysOnTop = vsAlwaysOnTop.getValue();
            owner.WindowsPosition=vsWindowsPosition;
            owner.CustomXwindowPos=vsCustomXwindowPos.getValue();
            owner.CustomYwindowPos=vsCustomYwindowPos.getValue();
            if(referenz.equals(vsWindowsPosition)){
            processPropertyEditor(); //Added v3.12.0    
            }
        }

        repaint();
        
    }

    public void processpropertyChangedToAllElements(Object o) {

        int c = 0;
        for (int i = 0; i < getComponentCount(); i++) {
            Component comp = getComponent(i);
            if (comp instanceof Element) {
                Element element = (Element) comp;

                try {
                    element.classRef.propertyChanged(o);
                } catch (Exception ex) {

                }

            }
        }

        repaint();
    }

    public void processPropertyEditor() {
        if (owner.frameCircuit != null && owner.propertyEditor != null) {
            owner.propertyEditor.clear();
            owner.propertyEditor.setVMObject(this);
            vsWidth.setValue(getWidth());
            vsHeight.setValue(getHeight());
            vsColor.setValue(getBackground());
            vsCaption.setValue(owner.caption);
            vsShowToolbar.setValue(owner.showToolBar);
            vsUnDecorate.setValue(owner.unDecorated);
            vsAlwaysOnTop.setValue(owner.AlwaysOnTop);
            vsWindowsPosition=owner.WindowsPosition;
            vsCustomXwindowPos.setValue(owner.CustomXwindowPos);
            vsCustomYwindowPos.setValue(owner.CustomYwindowPos);

         
            owner.propertyEditor.addItem(java.util.ResourceBundle.getBundle("VisualLogic/VMObject").getString("Width"), vsWidth, 20, 5000, true);
            owner.propertyEditor.addItem(java.util.ResourceBundle.getBundle("VisualLogic/VMObject").getString("Height"), vsHeight, 20, 5000, true);

            owner.propertyEditor.addItem("Properties", vsProperties, 0, 0, true);
            boolean EditableTemp=false;
            if(owner.WindowsPosition.selectedIndex==5){ // IF user select "CUSTOM" enable editable X and Y Loation
            EditableTemp=true;    
            }else{
            EditableTemp=false;    
            }

            if (this == owner.getFrontBasis()) {
                owner.propertyEditor.addItem(java.util.ResourceBundle.getBundle("VisualLogic/VMObject").getString("ShowToolbar"), vsShowToolbar, 0, 0, true);
                owner.propertyEditor.addItem(java.util.ResourceBundle.getBundle("VisualLogic/VMObject").getString("unDecorateFrame"), vsUnDecorate, 0, 0, true);
                owner.propertyEditor.addItem(java.util.ResourceBundle.getBundle("VisualLogic/VMObject").getString("AlwaysOnTop"), vsAlwaysOnTop, 0, 0, true);
                owner.propertyEditor.addItem(java.util.ResourceBundle.getBundle("VisualLogic/VMObject").getString("WindowPosition"), vsWindowsPosition, 0, 100, true);
                owner.propertyEditor.addItem(java.util.ResourceBundle.getBundle("VisualLogic/VMObject").getString("XcustomPos"), vsCustomXwindowPos, Integer.MIN_VALUE, Integer.MAX_VALUE, EditableTemp);
                owner.propertyEditor.addItem(java.util.ResourceBundle.getBundle("VisualLogic/VMObject").getString("YcustomPos"), vsCustomYwindowPos, Integer.MIN_VALUE, Integer.MAX_VALUE, EditableTemp);
                owner.propertyEditor.addItem(java.util.ResourceBundle.getBundle("VisualLogic/VMObject").getString("Color"), vsColor, 20, 5000, true);
                owner.propertyEditor.addItem(java.util.ResourceBundle.getBundle("VisualLogic/VMObject").getString("Title"), vsCaption, 20, 5000, true);
                owner.propertyEditor.addItem(java.util.ResourceBundle.getBundle("VisualLogic/VMObject").getString("Icon"), owner.vsIcon, 0, 0, true);
            }

            owner.propertyEditor.reorderItems();
        }

    }

    public void ProcessDeGruppierer() {

        /*setDraehteInRunMode();
    initAllOutputPins();
    initAllInputPins();*/
    }

    public void ProcessPinDataType() {
        //if (owner.isLoading()) return;

        ProcessDeGruppierer();

        Element element = null;
        for (int i = 0; i < getElementCount(); i++) {
            element = getElement(i);

            if (element.classRef != null) {
                element.classRef.checkPinDataType();
            }
        }

    }

    public void processElementChanged() {
        Element element = null;
        for (int i = 0; i < getElementCount(); i++) {
            element = getElement(i);

            try {
                element.classRef.xonChangeElement();
            } catch (Exception ex) {
                //Tools.showErrorMessage(""+ex.toString());
            }
        }

    }

    /**
     * Creates a new instance of Basis
     */
    public VMObject(Basis owner, String elementPath) {

        this.owner = owner;
        elList = new ArrayList();
        drahtLst = new ArrayList();
        leerLauf = new StatusIdle(this);
        status = leerLauf;
        elementListe = new ArrayList();
        this.elementPath = elementPath;
        initElementReferences(); // alle Referenzen auf null setzen!
        time1 = System.currentTimeMillis();
        setSize(800, 460);

        setPreferredSize(new Dimension(800, 460));

        this.setBackground(Color.white);

        setDoubleBuffered(false);

        addMouseListener(this);
        addMouseMotionListener(this);

        setLocation(0, 0);
        setLayout(null);

        enableEvents(AWTEvent.FOCUS_EVENT_MASK);        // catch Focus-Events
        enableEvents(AWTEvent.KEY_EVENT_MASK);          // catch KeyEvents
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);        // catch MouseEvents
        enableEvents(AWTEvent.COMPONENT_EVENT_MASK);    // catch ComponentEvents
        
        
        DropTargetListener dropTargetListener = new DropTargetListener() {

            // Die Maus betritt die Komponente mit
            // einem Objekt
            public void dragEnter(DropTargetDragEvent e) {
                //System.out.println(""+e.getTransferable().toString());
                e.acceptDrag(DnDConstants.ACTION_COPY);
            }

            // Die Komponente wird verlassen
            public void dragExit(DropTargetEvent e) {
            }

            // Die Maus bewegt sich über die Komponente
            public void dragOver(DropTargetDragEvent e) {

                e.acceptDrag(DnDConstants.ACTION_COPY);
            }

            public void drop(DropTargetDropEvent e) {
                try {
                    Transferable t = e.getTransferable();

                    if (e.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        e.acceptDrop(e.getDropAction());

                        String s;
                        s = (String) t.getTransferData(DataFlavor.stringFlavor);

                        System.out.println("Drop : " + s);

                        dragged(s);

                        e.dropComplete(true);
                    } else {
                        e.rejectDrop();
                    }
                } catch (java.io.IOException e2) {
                } catch (UnsupportedFlavorException e2) {
                }

            }

            // Jemand hat die Art des Drops (Move, Copy, Link)
            // geändert
            public void dropActionChanged(DropTargetDragEvent e) {
            }
        };
        DropTarget dropTarget = new DropTarget(this, dropTargetListener);

    }

    public void dragged(String filename) {
        if (getOwner().getCircuitBasis() == this) {
            Element subVM = null;
            try {
                File f = new File(filename);

                if (f.isDirectory()) {
                    return;
                }
                if (!Tools.getExtension(f).equalsIgnoreCase("VLOGIC")) {
                    return;
                }
                if (f.getAbsolutePath().equalsIgnoreCase(new File(this.owner.fileName).getAbsolutePath())) {
                    return;
                }

                String p1 = new File(owner.projectPath).getAbsolutePath();

                String p2 = new File(filename).getAbsolutePath();

                String arrg = p2.substring(p1.length(), p2.length());

                // if (p1.equals(p2))
                {
                    //String path=f.getPath();
                    String vmName = f.getName();

                    String[] args = new String[3];
                    args[0] = arrg; // vmName zb: "Untiled.vlogic""
                    args[1] = vmName; // caption!
                    args[2] = "";

                    String vmPanel = "";

                    JFrame parent = null;
                    if (owner.frameCircuit != null) {
                        parent = owner.frameCircuit;
                    }
                    if (Tools.setQuestionDialog(parent, "Do you want to Add Panel?")) {
                        vmPanel = "VMPanel";
                    }

                    subVM = Tools.addSubVM(this, vmPanel, args);

                    Point p = this.getMousePosition();
                    if (p == null) {
                        p = new Point(40, 40);
                    }
                    subVM.setLocation(p.x - (subVM.getWidth() / 2), p.y - subVM.getHeight() / 2);

                }
            } catch (Exception ex) {
                Tools.showMessage(this, "" + ex.toString());
            }
        }

    }

//--------------------------------------------------------------
    protected void processComponentEvent(ComponentEvent co) {
        if (co.getID() == ComponentEvent.COMPONENT_SHOWN) {
            requestFocus();
        }

        super.processComponentEvent(co);
    }

    protected void processMouseEvent(MouseEvent me) {
        switch (me.getID()) {
            case MouseEvent.MOUSE_PRESSED:
                requestFocus();
        }
        super.processMouseEvent(me);
    }

    protected void processKeyEvent(KeyEvent ke) {
        if (getStatus() != null) {
            getStatus().processKeyEvent(ke);
        }
        super.processKeyEvent(ke); // diese Zeile nicht mehr hinzufügen!!!
    }

    protected void processFocusEvent(FocusEvent fe) {

        /*switch(fe.getID())
        {
        case FocusEvent.FOCUS_GAINED:
        break;
        case FocusEvent.FOCUS_LOST:
        break;Focus
        }*/
        repaint();

        super.processFocusEvent(fe);
    }

//--------------------------------------------------------------
    public void setAllComponentsOpaque(boolean value) {
        for (int i = 0; i < getElementCount(); i++) {
            Element element = getElement(i);

            element.setOpaque(value);
            for (int j = 0; j < element.getPinCount(); j++) {
                JPin pin = element.getPin(j);
                pin.setOpaque(value);
            }

        }
    }

    public void saveAsJPEG(String sImgFilename) throws Exception {

        BufferedImage img = getVMImage();

        try {
            ImageIO.write(img, "jpeg", new File(sImgFilename));
            /*FileOutputStream out = new FileOutputStream(new File(sImgFilename));
            JPEGImageEncoder enc = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam prm = enc.getDefaultJPEGEncodeParam(img);
            prm.setQuality(0.9f, true);
            enc.setJPEGEncodeParam(prm);
            enc.encode(img);

            out.flush();
            out.close();
            out = null;
            enc = null;
            prm = null;*/
        } catch (Exception ex) {
            throw new Exception(java.util.ResourceBundle.getBundle("VisualLogic/VMObject").getString("\nError:_Image_storing_to_'") + sImgFilename + java.util.ResourceBundle.getBundle("VisualLogic/VMObject").getString("'_failed:_") + ex.getMessage());
        }
        if (img != null) {
            img.flush();
        }
    }

    public BufferedImage getVMImage() {
        int iWidth = this.getWidth();
        int iHeight = this.getHeight();

        int dist = 30;
        Rectangle rec = getMinMaxBounds();

        BufferedImage tempImg = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = tempImg.createGraphics();

        print(g);
        g.dispose();

        rec.x = rec.x - dist;
        rec.y = rec.y - dist;
        rec.width = rec.width + dist * 2;
        rec.height = rec.height + dist * 2;

        if (rec.x < 0) {
            rec.x = 0;
        }
        if (rec.y < 0) {
            rec.y = 0;
        }
        if (rec.width > getWidth()) {
            rec.width = getWidth();
        }
        if (rec.height > getHeight()) {
            rec.height = getHeight();
        }

        BufferedImage img = tempImg.getSubimage(rec.x, rec.y, rec.width, rec.height);

        return img;
    }

    public boolean isPaused() {
        return pause;
    }

    public void run() {
        isThreadFertig = false;
        while (!stop) {
            try {
                Thread.sleep(owner.delay);
            } catch (Exception exe) {
            }

            processAllElements();
        }
        isThreadFertig = true;
    }

    /*public void setProgress()
    {
    //if (processCounter==10) labelProgress.setText("_");
    //if (processCounter==20) labelProgress.setText("@");
    //if (processCounter>100) processCounter=0;
    //processCounter++;
    }*/
 /*protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2D = (Graphics2D)g;
    g2D.scale(zoom,zoom);
    }*/
    public boolean isGraphicLocked() {
        return graphikLocked;
    }

    public void lockGraphics() {
        graphikLocked = true;
    }

    public void unlockGraphics() {
        graphikLocked = false;
        repaint();
    }

    public boolean isRunning() {
        if (status != null && status.equals(modusRun)) {
            return true;
        } else {
            return false;
        }
    }

    public void openVlogicFile(String fileName) {
        //owner.getFrameMain().openVLogicFile(fileName);
    }

    public void setElementDocVisible(boolean value) {
        //owner.getFrameMain().setElementDocVisible(value);
    }

    /*public void drawTitel(Graphics2D g)
    {
    Element el = (Element)element;
    Font fnt = new Font("Courier",0,10);
    g.setFont(fnt);
    FontMetrics fm = g.getFontMetrics();
    if (basisTitel!=null)
    {
    Rectangle2D  r = fm.getStringBounds(basisTitel,g);
    Rectangle rx = el.jGetBounds();
    g.setColor(Color.black);
    int w2=(int)(rx.x+(rx.width/2));
    int s2=(int)(r.getWidth()/2);
    g.drawString(basisTitel,w2-s2,10);
    }
    }*/
    public void setPropertyEditor() {

    }

    // Die Methode wird aufgerufen wenn das Objekt zu zeichnen ist
    public void xpaint(java.awt.Graphics g) {
        /*Element el = (Element)element;
    if (elementImage!=null)
    {
    g.drawImage(elementImage,10,10,el.getWidth()-20,el.getHeight()-20,this);
    }*/

        //drawTitel((Graphics2D)g);
    }

    // Die Methode wird aufgerufen wenn das Objekt neu erzeugt wird
    // und gibt die Schnittstelle zum internen Element an
    // mitdem man das interne Element ansteuern kann
    public void xsetExternalIF(ExternalIF externalIF) {

        /*   this.element =externalIF;
    Element exElement = (Element)element;
    topPins.clear();
    rightPins.clear();
    bottomPins.clear();
    leftPins.clear();
    exElement.setCaptionVisible(true);
    exElement.setCaption(owner.caption);
    getPinsAllXBar(java.util.ResourceBundle.getBundle("VisualLogic/VMObject").getString("#PINOUTPUT_INTERNAL#"), rightPins);
    sortPinsForY(rightPins);
    getPinsAllXBar(java.util.ResourceBundle.getBundle("VisualLogic/VMObject").getString("#PININPUT_INTERNAL#"), leftPins);
    sortPinsForY(leftPins);
    externalIF.jSetTopPinsVisible(false);
    externalIF.jSetBottomPinsVisible(false);
    //if (leftPins.size()==0)   externalIF.jSetLeftPinsVisible(false);
    //if (rightPins.size()==0)  externalIF.jSetRightPinsVisible(false);
    element.jSetSize(owner.elementWidth, owner.elementHeight);
    externalIF.jSetTopPins(topPins.size());
    externalIF.jSetRightPins(rightPins.size());
    externalIF.jSetBottomPins(bottomPins.size());
    externalIF.jSetLeftPins(leftPins.size());
    externalIF.jInitPins();
    int pinCount=0;
    //Outputs
    for (int i=0;i<rightPins.size();i++)
    {
    Element elx = (Element)rightPins.get(i);
    JPin pin=exElement.getPin(pinCount++);
    if (elx.isCaptionVisible()) pin.setDescription(elx.getCaption());
    int dt=elx.getPin(0).dataType;
    pin.dataType=dt;
    pin.pinIO=JPin.PIN_OUTPUT;
    }
    //Inputs
    for (int i=0;i<leftPins.size();i++)
    {
    Element elx = (Element)leftPins.get(i);
    JPin pin=exElement.getPin(pinCount++);
    if (elx.isNameVisible()) pin.setDescription(elx.getCaption());
    pin.dataType=elx.getPin(0).dataType;
    pin.pinIO=JPin.PIN_INPUT;
    }
         */
    }

    public void jSetChanged(boolean value) {
    }

    public void changePin(int pinIndex, Object value) {

    }

    public void resetValues() {

    }

    public void xonChangeElement() {

    }

    // Die Methode wird aufgerufen wenn die Maus auf dem Element gedrï¿½ckt worden ist
    public void xonMousePressed(MouseEvent e) {

    }

    public void xonMouseDragged(MouseEvent e) {

    }

    // Die Methode wird aufgerufen wenn die Maus auf dem Element losgelassen worden ist
    public void xonMouseReleased(MouseEvent e) {

    }

    // Die Methode wird aufgerufen wenn die Maus auf dem Element bewegt worden ist
    public void xonMouseMoved(MouseEvent e) {

    }

    public void xonInitInputPins() {
        for (int i = 0; i < leftPins.size(); i++) {
            Element elx = (Element) leftPins.get(i);
            JPin pinIntern = elx.getPin(0);

            Object reference = element.getPinInputReference(rightPins.size() + i);
            pinIntern.object = reference;
        }
    }

    public void xonInitOutputPins() {
        //Outputs
        for (int i = 0; i < rightPins.size(); i++) {
            Element elx = (Element) rightPins.get(i);
            Draht draht = elx.getPin(0).draht;
            if (draht != null) {
                Element elSource = (Element) getObjectWithID(draht.getSourceElementID());
                JPin pin = elSource.getPin(draht.getSourcePin());

                Object ref = pin.object;
                element.setPinOutputReference(i, ref);
            }
        }

    }

    public void checkPinDataType() {

    }

    // Die Methode wird aufgerufen wenn die Simulation gestartet wird
    public void xonStart() {

        owner.getCircuitBasis().setDraehteInRunMode();
        owner.getFrontBasis().setDraehteInRunMode();

        owner.getCircuitBasis().initAllOutputPins();
        owner.getFrontBasis().initAllOutputPins();

        owner.getCircuitBasis().initAllInputPins();
        owner.getFrontBasis().initAllInputPins();

        modusRun = new StatusRun(this);
        modusRun.start();
    }

    // Die Methode wird aufgerufen wenn die Simulation beendet wird
    public void xonStop() {

        owner.getCircuitBasis().setDraehteInStopMode();
        owner.getFrontBasis().setDraehteInStopMode();

        setModusIdle();
    }

    // Die Methode wird aufgerufen wenn die Inputs ausgewertet werden sollen
    // und ggf. ein Output durch onEventProceded eingeleitet werden soll
    public void xonProcess() {
        processAllElements();
    }

    public String xgetName() {
        //return elementName;
        return "";
    }

    // ruft das Eigenschaftsfenster im Externen Element
    public void xopenPropertyDialog() {

    }

    public void saveToStreamAfterXOnInit(FileOutputStream fos) {

    }

    public void loadFromStreamAfterXOnInit(FileInputStream fis) {

    }

    public void xSaveToStream(FileOutputStream fos) {

    }

    public void onDispose() {
    }

    public void xOnInit() {
    }

    public void xLoadFromStream(FileInputStream fis) {

    }

    public void xloadFromXML(org.w3c.dom.Element nodeElement) {

    }

    public void xsaveToXML(org.w3c.dom.Element nodeElement) {

    }

    public String getElementPath() {
        return elementPath;
    }

    public void initElementReferences() {
        for (int i = 0; i < maxElements; i++) {
            elementReferences[i] = null;
        }
    }

    public void SelectElement(int elementid) {
        for (int i = 0; i < getElementCount(); i++) {
            Element el = getElement(i);
            if (el.getID() == elementid) {
                el.setSelected(true);
            }
        }
    }

    public void processAddElement(String mainPath, String circuitClass, String panelClass, String[] args) {
        addElementModus = new StatusAddElement(this, mainPath, circuitClass, panelClass, args);
        status = addElementModus;
    }

    // ************************* Element handling ***************************
    public int getElementCount() {
        return elList.size();
    }

    public Element getElement(int index) {
        return (Element) elList.get(index);
    }

    public Element getElementWithID(int id) {
        Object o = getObjectWithID(id);
        if (o instanceof Element) {
            Element el = (Element) o;
            return el;
        } else {
            return null;
        }
    }

    public Object getObjectWithID(int idIndex) {
        return elementReferences[idIndex];
    }

    /* Liefert eine Zahl >=0
     * alsonsten -1 -> Object nicht gefunden
     */
    public int getObjectIndex(int id) {
        Element el = (Element) getObjectWithID(id);
        return elList.indexOf(el);
    }

    // sucht in elementReferences Array nach einen freien Speicherplatz
    // und liefert die Nummer des Speichers
    // liefert -1 wenn es keine freien Speicherzellen mehr gibt
    public int getObjectID() {
        for (int i = 0; i < maxElements; i++) {
            if (elementReferences[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public void reserveObjectID(int idIndex, Object obj) {
        elementReferences[idIndex] = obj;
    }

    public void removeFromElementReferences(int idIndex) {
        elementReferences[idIndex] = null;
    }

    public Element addElement(String mainPath, String binPath, String klassenName, String[] args) {
        if ((getStatus() instanceof StatusRun) == false) {
            owner.setChanged(true);

            Element element = addElementIntoCanvas(mainPath, binPath, klassenName, args);
            element.setLocation(10, 10);
            return element;
        } else {
            return null;
        }

        //StatusAddElement addElementModus=new StatusAddElement(this, pfad, klassenName);
        //status=addElementModus;
        //return addElementModus.getElement();
    }

    /*
     * sucht ob die num schon existiert!
     */
    public boolean existInternalID(String elementName, int num) {
        for (int i = 0; i < getElementCount(); i++) {
            Element el = getElement(i);

            if (el.getNameLocalized().equalsIgnoreCase(elementName) && el.getNameID() == num) {
                return true;
            }
        }
        return false;
    }

    public int getElementCaptionNumber(Element element) {
        for (int j = 1; j < maxElements; j++) {
            int num = j;
            if (existInternalID(element.getNameLocalized(), num) == false) {
                return num;
            }
        }
        return -1;
    }

    private Point getNewElementLocation() {
        boolean einerIstDrin = false;
        int x = 0;
        int y = 0;
        for (y = 10; y < 500; y += 20) {
            for (x = 10; x < 500; x += 20) {
                einerIstDrin = false;
                for (int i = 0; i < getElementCount(); i++) {
                    Element element = getElement(i);

                    Point p = element.getLocation();
                    int w = element.getWidth();
                    int h = element.getHeight();
                    // is Point in Element?
                    if (x >= p.x && y >= p.y && x <= p.x + w && y <= p.y + h) {
                        einerIstDrin = true;
                    }
                }
                if (einerIstDrin == false) {
                    return new Point(x, y);
                }
            }
        }
        return new Point(20, 20);
    }

    public void wrappElement(Element element) {
        //remove(element);
        /*Wrapper wrapper=new Wrapper();
    wrapper.setBackground(Color.RED);
    wrapper.add(element);
    element.wrapper=wrapper;
    wrapper.setLocation(element.getLocation());
    wrapper.setSize(element.getSize());
    element.setLocation(0,0);
    add(wrapper,0);*/
    }

    public Element addElementIntoCanvas(String mainPath, String binPath, String circuitClass, String[] args) {
        owner.setChanged(true);
        String path = mainPath + "/" + binPath;
        try {
            lockGraphics();
            int id = getObjectID();
            Element element = new Element(id, this, elementPath, mainPath, binPath, circuitClass, "", args);
            reserveObjectID(id, element);
            //System.out.println("Element.id="+id);
            elList.add(element);

            add(element, 0);
            //wrappElement(element);
            //reorderWireFrames();
            unlockGraphics();

            element.setLocation(500, 500);

            Point p = getNewElementLocation();
            element.setLocation(p.x, p.y);
            int num = getElementCaptionNumber(element);

            element.setCaption(element.getNameLocalized() + num);
            element.setNameID(num);

            return element;
        } catch (Exception ex) {
            owner.showErrorMessage(circuitClass + java.util.ResourceBundle.getBundle("VisualLogic/VMObject").getString("Element_konnte_nicht_erfolgreich_erzeugt_werden_:") + ex.toString());
            unlockGraphics();
        }

        return null;
    }

    // ************************* End Element handling ***************************
    public Draht addDrahtIntoCanvas(int sourceElementID, int sourcePin, int destElementID, int destPin) {

        Draht dr = null;
        int id = getObjectID();
        try {
            dr = new Draht(id, this, sourceElementID, sourcePin, destElementID, destPin);
            drahtLst.add(dr);
        } catch (Exception ex) {
            owner.showErrorMessage(java.util.ResourceBundle.getBundle("VisualLogic/VMObject").getString("Draht_konnte_nicht_erfolgreich_erzeugt_werden_:") + ex.toString());
        }
        reserveObjectID(id, dr);
        return dr;

    }

    public Draht addDraht(int id, int sourceElementID, int sourcePin, int destElementID, int destPin) {
        Draht dr = null;
        try {
            dr = new Draht(id, this, sourceElementID, sourcePin, destElementID, destPin);

            Element element = null;

            /*          element=(Element)getObjectWithID(sourceElementID);
            element.getPin(sourcePin).draht=dr;
            element=(Element)getObjectWithID(destElementID);
            element.getPin(destPin).draht=dr;*/
            drahtLst.add(dr);
        } catch (Exception ex) {
            owner.showErrorMessage(java.util.ResourceBundle.getBundle("VisualLogic/VMObject").getString("Draht_konnte_nicht_erfolgreich_erzeugt_werden_:") + ex.toString());
        }
        return dr;
    }

    public int getDrahtCount() {
        return drahtLst.size();
    }

    public Draht getDraht(int index) {
        return (Draht) drahtLst.get(index);
    }

    public Draht getDraht(int sourceElementID, int sourcePin, int destElementID, int destPin) {
        Draht draht;
        for (int i = 0; i < drahtLst.size(); i++) {
            draht = (Draht) drahtLst.get(i);
            if (draht.getSourceElementID() == sourceElementID && draht.getSourcePin() == sourcePin
                    || draht.getDestElementID() == destElementID && draht.getDestPin() == destPin) {
                return draht;
            }

        }
        return null;
    }

    public void konkat(Element element, Draht drahtA, Draht drahtB) {
        int sourceElementID = -1;
        int sourcePin = -1;
        int destElementID = -1;
        int destPin = -1;

        if (drahtA.getSourceElementID() == element.getID()) {
            sourceElementID = drahtA.getDestElementID();
            sourcePin = drahtA.getDestPin();
        } else {
            sourceElementID = drahtA.getSourceElementID();
            sourcePin = drahtA.getSourcePin();
        }

        if (drahtB.getSourceElementID() == element.getID()) {
            destElementID = drahtB.getDestElementID();
            destPin = drahtB.getDestPin();
        } else {
            destElementID = drahtB.getSourceElementID();
            destPin = drahtB.getSourcePin();
        }

        Draht newDraht = addDrahtIntoCanvas(sourceElementID, sourcePin, destElementID, destPin);

        if (drahtA.getSourceElementID() == element.getID()) {
            drahtA.copyPointsNegiert(0, 1, newDraht);
        } else {
            drahtA.copyPoints(0, drahtA.getPolySize() - 1, newDraht);
        }

        if (drahtB.getSourceElementID() == element.getID()) {
            drahtB.copyPoints(1, drahtB.getPolySize(), newDraht);
        } else {
            drahtB.copyPointsNegiert(1, 0, newDraht);
        }

        deleteElement(element);

        Element sourceElement = (Element) getObjectWithID(sourceElementID);
        sourceElement.getPin(sourcePin).draht = newDraht;

        Element destElement = (Element) getObjectWithID(destElementID);
        destElement.getPin(destPin).draht = newDraht;

    }

    public void deleteDraht(Draht draht) {
        owner.setChanged(true);
        if (draht != null) {
            int sourcePin = draht.getSourcePin();
            Element sourceElement = (Element) getObjectWithID(draht.getSourceElementID());

            int destPin = draht.getDestPin();
            Object o = getObjectWithID(draht.getDestElementID());;
            JPin p1 = null;
            if (o instanceof Element) {
                Element destElement = (Element) o;

                if (destElement != null) {
                    p1 = destElement.getPin(destPin);
                    if (p1 != null) {
                        p1.value = 0;
                        p1.draht = null;
                        p1.object = null;
                    }
                }

            }
            if (sourceElement != null) {
                p1 = sourceElement.getPin(sourcePin);
                if (p1 != null) {
                    p1.value = 0;
                    p1.draht = null;
                    p1.object = null;
                }
            }
        }

        drahtLst.remove(draht);
        draht = null;

        ProcessPinDataType();
        processElementChanged();
    }

    public Line isPointOverLine(Point p) {
        Draht draht = null;
        Line line = null;
        for (int i = 0; i < drahtLst.size(); i++) {
            draht = (Draht) drahtLst.get(i);

            line = draht.isOverLine(p);
            if (line != null) {
                return line;
            }
        }
        return null;
    }

    public void deleteOtherPanelElement(Element el) {
        if (el.panelElement != null) {
            VMObject frontBasis = owner.getFrontBasis();
            Element elc = (Element) el.panelElement;

            frontBasis.remove(elc.lblName);
            frontBasis.remove(elc);

            frontBasis.elList.remove(elc);
            elc.jClearSubElements();
            elc.panelElementID = -1;
            elc.circuitElementID = -1;
            try {
                if (elc.classRef != null) {
                    elc.classRef.onDispose();
                    elc.removeAll();
                }
            } catch (Exception ex) {
            }

            frontBasis.removeFromElementReferences(elc.getID());
        }

        if (el.circuitElement != null) {
            Element elc = (Element) el.circuitElement;
            VMObject circuitBasis = owner.getCircuitBasis();

            circuitBasis.remove(elc.lblName);
            circuitBasis.remove(elc);
            circuitBasis.elList.remove(elc);
            elc.jClearSubElements();
            elc.panelElementID = -1;
            elc.circuitElementID = -1;

            try {
                if (elc.classRef != null) {
                    elc.classRef.onDispose();
                    elc.removeAll();
                }
            } catch (Exception ex) {
            }

            circuitBasis.removeFromElementReferences(elc.getID());
        }

        removeFromElementReferences(el.getID());
        try{
        this.remove(el.lblName);
        this.remove(el);
        }catch(Exception e){
            System.out.println("Error at VisualLogic.VMObject.deleteOtherPanelElement(VMObject.java:1487)");   
        }
        try {
            if (el.classRef != null) {
                el.classRef.onDispose();
                el.removeAll();
            }
        } catch (Exception ex) {
        }

        this.elList.remove(el);
        el.removeReference();

        if (el.panelElement != null) {
            Element elc = (Element) el.panelElement;
            elc.removeReference();
        }

        if (el.circuitElement != null) {
            Element elc = (Element) el.circuitElement;
            elc.removeReference();
        }

        el.jClearSubElements();
    }

    public void deleteElement(Element el) {
        owner.setChanged(true);
        boolean ok = false;

        int pinTopNr = el.getPinsTop() - 1;
        int pinBottomNr = el.getPinsTop() + el.getPinsRight() + el.getPinsBottom() - 1;

        JPin pinTop = el.getPin(pinTopNr);
        JPin pinBottom = el.getPin(pinBottomNr);

        int elPinTopID = 0;
        int elPinTopPin = 0;

        int elPinBottomID = 0;
        int elPinBottomPin = 0;
        if (pinTop != null && pinTop.draht != null) {
            elPinTopID = pinTop.draht.getSourceElementID();
            elPinTopPin = pinTop.draht.getSourcePin();
        }

        if (pinBottom != null && pinBottom.draht != null) {
            elPinBottomID = pinBottom.draht.getDestElementID();
            elPinBottomPin = pinBottom.draht.getDestPin();
        }

        if (pinTop != null && pinBottom != null && pinTop.draht != null && pinBottom.draht != null) {
            ok = true;
        }

        deleteOtherPanelElement(el);

        deleteElementDrahte(el);

        /*if (el.getInternName().indexOf("#MCU-FLOWCHART") > -1)
        {

            // Sind die drähte oben und unten Belegt,
            // dann Element löschen und ein Draht dazwischen legen.
            if (ok == true)
            {
                Draht draht = addDrahtIntoCanvas(elPinTopID, elPinTopPin, elPinBottomID, elPinBottomPin);

                Element el1 = getElementWithID(elPinTopID);
                if (el1!=null)
                {
                    el1.getPin(elPinTopPin).draht = draht;
                }

                Element el2 = getElementWithID(elPinBottomID);
                if (el2!=null)
                {
                    el2.getPin(elPinBottomPin).draht = draht;
                }

                //reorderWireFrames();
            }
        }*/
        el.removeReference();
    }

    public void deleteElementDrahte(Element el) {
        // 1. loese von dem zu loeschendem Element alle ziel Pins.draht
        for (int j = 0; j < el.getPinCount(); j++) {
            JPin pin = el.getPin(j);
            if (pin.draht != null) {
                deleteDraht(pin.draht);
                pin.draht = null;
            }
        }
    }

    private void rufeXOnInit(Element element) {
        try {
            element.classRef.xOnInit();
        } catch (Exception ex) {
        }
    }

    public Element AddDualElement(String mainPath, String binPath, String circuitClass, String panelClass, String[] args) {
        Element aktiveElement;
        Element element;

        VMObject circuitvmobject = owner.getCircuitBasis();
        VMObject frontvmobject = owner.getFrontBasis();

        String pfad = mainPath + "/" + binPath;

        if (this == circuitvmobject) {
            aktiveElement = addElementIntoCanvas(mainPath, binPath, circuitClass, args);
            aktiveElement.circuitElementID = aktiveElement.getID();

            System.out.println(aktiveElement.owner.owner.getFrontBasis().getElementCount());

            if (!panelClass.trim().equalsIgnoreCase("")) {
                element = frontvmobject.addElementIntoCanvas(mainPath, binPath, panelClass, args);
                element.circuitElement = aktiveElement;
                element.circuitElementID = aktiveElement.getID();
                aktiveElement.panelElementID = element.getID();
                aktiveElement.panelElement = element;

                rufeXOnInit(aktiveElement);
                rufeXOnInit(element);
            } else {
                rufeXOnInit(aktiveElement);
            }
        } else {
            aktiveElement = addElementIntoCanvas(mainPath, binPath, panelClass, args);

            if (!circuitClass.trim().equalsIgnoreCase("")) {
                element = circuitvmobject.addElementIntoCanvas(mainPath, binPath, circuitClass, args);

                element.panelElement = aktiveElement;
                element.panelElementID = aktiveElement.getID();
                aktiveElement.circuitElement = element;
                aktiveElement.circuitElementID = element.getID();

                rufeXOnInit(element);
                rufeXOnInit(aktiveElement);
            } else {
                rufeXOnInit(aktiveElement);
            }
        }

        return aktiveElement;

    }

    public void deleteAllSelected() {
        owner.setChanged(true);

        // Alle Markierten Elemente loeschen
        ArrayList deletedList = new ArrayList();

        for (int i = 0; i < elList.size(); i++) {
            Element el = (Element) elList.get(i);
            if (el.isSelected() == true) {
                deletedList.add(el);
            }
        }

        while (deletedList.size() > 0) {
            Element el = (Element) deletedList.get(0);
            deleteElement(el);
            deletedList.remove(0);
        }

        deletedList.clear();
        deletedList = null;

        //Alle Markierten Draehte loeschen
        int i = 0;
        do {
            if (drahtLst.size() == 0) {
                break;
            }
            Draht draht = (Draht) drahtLst.get(i);
            if (draht.isSelected() == true) {
                // Delete Draht und die Referenzen implements Element
                deleteDraht(draht);

                drahtLst.remove(draht);
                draht = null;
                i = -1;
            }
            i++;
        } while (i < drahtLst.size());

        // Alle Markierten PolyPoint loeschen
        i = 0;
        do {
            if (drahtLst.size() == 0) {
                break;
            }
            Draht draht = (Draht) drahtLst.get(i);
            for (int j = 0; j < draht.getPolySize(); j++) {
                PolyPoint p = draht.getPoint(j);
                if (p.isSelected()) {
                    draht.deletePoint(j);
                    i = -1;
                }
            }
            i++;
        } while (i < drahtLst.size());

        processPropertyEditor();
        updateUI();

    }

    public Element getSelectedElement() {
        for (int i = 0; i < elList.size(); i++) {
            Element el = (Element) elList.get(i);
            if (el.isSelected() == true) {
                return el;
            }
        }
        return null;
    }

    public int getSelectedCount() {
        int c = 0;
        for (int i = 0; i < elList.size(); i++) {
            Element el = (Element) elList.get(i);
            if (el.isSelected() == true) {
                c++;
            }
        }

        return c;
    }

    public ArrayList<Element> getSelectedElements() {
        ArrayList<Element> result = new ArrayList<Element>();

        int c = 0;
        for (int i = 0; i < elList.size(); i++) {
            Element el = (Element) elList.get(i);
            if (el.isSelected() == true) {
                result.add(el);
            }
        }
        return result;
    }

    public void selectAny(boolean value) {
        for (int i = 0; i < elList.size(); i++) {
            Element element = (Element) elList.get(i);
            element.setSelected(value);
        }
        for (int i = 0; i < drahtLst.size(); i++) {
            Draht draht = (Draht) drahtLst.get(i);
            draht.setSelected(value);

            draht.selectAnyPoints(value);
        }
    }

    public void step() {
        processMyAll();
    }

    public void startPanel() {
        setModusRun();
        disableAllElements();
        this.stop = false;
        this.pause = false;
    }

    public void setDraehteInRunMode() {
        // setzt alle Drï¿½hte implements Run modus
        for (int i = 0; i < getDrahtCount(); i++) {
            Draht draht = getDraht(i);
            draht.setRunMode();
        }
    }

    public void setDraehteInStopMode() {
        // setzt alle Drï¿½hte implements Run modus
        for (int i = 0; i < getDrahtCount(); i++) {
            Draht draht = getDraht(i);
            draht.setStopMode();
        }
    }

    public void initAllOutputPins() {
        // zuerst die Outpins Initialisieren
        for (int i = 0; i < getElementCount(); i++) {
            Element element = getElement(i);
            try {
                if (element.classRef != null) {
                    element.classRef.xonInitOutputPins();
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }

    public void initAllInputPins() {
        for (int i = 0; i < getElementCount(); i++) {
            Element element = getElement(i);
            try {
                if (element.classRef != null) {
                    element.classRef.xonInitInputPins();
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }

        }
    }

    public void start() {
        this.stop = false;
        this.pause = false;
        setModusRun();
        disableAllElements();

        if (owner.frameCircuit != null) {
            thread = new Thread(this);
            //EventQueue.invokeLater(thread);
            thread.setPriority(Thread.NORM_PRIORITY);
            thread.start();
        }
        //processAllElements();
    }

    public void pause() {
        this.pause = true;
    }

    public void resume() {
        this.pause = false;
    }

    public void stop() {
        //if (this.stop!=true)
        {
            this.stop = true;
            this.pause = true;

            processList.clear();
            if (modusRun != null) {
                modusRun.stop();
            }

            setModusIdle();
        }
    }

    private void callAutomatikProcess() {
        Element element;
        for (int i = 0; i < getElementCount(); i++) {
            element = getElement(i);
            if (element != null) {
                if (element.elementBasis != null) {
                    element.elementBasis.xonProcess();
                }

                // if (element.classRef!=null) element.classRef.xonProcess();
            }
        }
    }
    Draht ddd;

    private void displayWireStatus(Draht draht) {

        if (owner.debugMode) {
            ddd = draht;

            ddd.setOn();

            try {
                Thread.sleep(owner.getDebugDelay());
            } catch (Exception ex) {
            }
            ddd.setOff();
        }
    }

    public void processClock() {
        //System.out.println("clockList.size()="+clockList.size());
        for (int i = 0; i < clockList.size(); i++) {
            Element element = (Element) clockList.get(i);

            if (element != null && element.classRef != null) {
                try {
                    element.classRef.xonClock();
                } catch (Exception ex) {
                    System.out.println("proprocessClock() error : " + ex);
                }
            }
        }

        for (int i = 0; i < getElementCount(); i++) {
            Element element = getElement(i);
            if (element.elementBasis != null) {
                element.elementBasis.xonClock();
            }
        }
    }

    public void sortSubPanels() {
        if (owner.isLoading()) {
            return;
        }
        VMObject vm = this;
        for (int i = 0; i < vm.getElementCount(); i++) {
            Element element = vm.getElement(i);

            for (int j = 0; j < element.subElemente.size(); j++) {
                Component oo = (Component) element.subElemente.get(j);
                vm.remove(oo);
            }

            vm.remove(element.lblName);

            int index = vm.getComponentZOrder(element);
            vm.add(element.lblName, index);
            for (int j = 0; j < element.subElemente.size(); j++) {
                Component oo = (Component) element.subElemente.get(j);
                vm.add(oo, index + j);
            }
        }

    }

    public Element getFirstElement(Element el) {
        Element element;
        boolean oki = false;
        for (int i = getComponentCount() - 1; i >= 0; i--) {
            Component o = getComponent(i);

            if (o instanceof Element) {
                element = (Element) o;
                if (el.equals(element) && oki == false) {
                    oki = true;
                } else if (oki) {
                    return element;
                }
            }
        }
        return null;
    }

    public Element getLastElement(Element el) {
        Element element;
        boolean oki = false;
        for (int i = 0; i < getComponentCount(); i++) {
            Component o = getComponent(i);

            if (o instanceof Element) {
                element = (Element) o;
                if (el.equals(element) && oki == false) {
                    oki = true;
                } else if (oki) {
                    return element;
                }
            }
        }
        return null;
    }

    public void processAllElements() {
        if (pause == false) {
            processClock();

            processMyAll();
        } else {
            try {
                Thread.sleep(1);
            } catch (Exception ex) {
            }
        }
    }
    int pauseC = 0;

    private void processMyAll() {
        if (processList.size() == 0) {
            try {
                Thread.sleep(1);
                //System.out.println("PAUSE"+pauseC++);

            } catch (Exception ex) {
            }
            return;
        }

        if (owner.dialogTestpoint != null) {
            owner.dialogTestpoint.process();
        }
        if (owner.frameBooleanGraph != null) {
            owner.frameBooleanGraph.process();
        }
        if (owner.frameDoubleGraph != null) {
            owner.frameDoubleGraph.process();
        }

        Draht draht = (Draht) processList.get(0);

        if (draht != null) {
            VMObject b = draht.getVMObject();
            Element elementSource = b.getElementWithID(draht.sourceElementID);
            Element elementDest = b.getElementWithID(draht.destElementID);

            JPin pin = draht.pinSouce;

            if (owner.debugMode && owner.frameCircuit != null && owner.frameCircuit.watcher != null) {
                owner.frameCircuit.watcher.refreshList();
            }

            if (owner.debugMode && this.equals(owner.getCircuitBasis())) {

                if (elementSource != null) {
                    elementSource.setSelected(true);
                }
                try {
                    Thread.sleep(owner.getDebugDelay());
                } catch (Exception ex) {
                }

                if (pin.object instanceof VSBoolean) {
                    boolean bol = ((VSBoolean) pin.object).getValue();
                    if (bol) {
                        draht.setOn();
                    } else {
                        draht.setOff();
                    }
                } else {
                    displayWireStatus(draht);
                }

                if (elementSource != null) {
                    elementSource.setSelected(false);
                }
                if (elementDest != null) {
                    elementDest.setSelected(true);
                }
            }
            if (elementDest != null && elementDest.classRef != null) {
                try {
                    elementDest.classRef.xonProcess();
                } catch (java.lang.AbstractMethodError ex) {
                    Tools.jException(owner, "Error in xonProcess : " + ex + ", " + elementDest.getNameLocalized());
                } catch (Exception ex) {
                    Tools.jException(owner, "Error in xonProcess : " + ex + ", " + elementDest.getNameLocalized());
                }

                try {
                    elementDest.classRef.elementActionPerformed(new ElementActionEvent(draht.destPin, element));
                } catch (java.lang.AbstractMethodError ex) {
                    System.out.println("elementActionPerformed : " + ex);
                } catch (Exception ex) {
                    System.out.println("elementActionPerformed : " + ex);
                }

                try {
                    int size = elementDest.notifyWhenDestCalledList.size();
                    if (size > 0) {
                        for (int i = 0; i < size; i++) {
                            //Element dst = getElementWithID(draht.sourceElementID);
                            Element dst = (Element) elementDest.notifyWhenDestCalledList.get(i);
                            if (dst != null && dst.classRef != null) {
                                dst.classRef.destElementCalled();
                            }
                        }
                    }
                } catch (java.lang.AbstractMethodError ex) {
                    System.out.println("destElementCalled : " + ex);
                } catch (Exception ex) {
                    System.out.println("destElementCalled : " + ex);
                }

            }

        }

        if (processList.size() > 0) {
            processList.remove(0);
        }
    }

    public void disableAllElements() {
        for (int i = 0; i < elList.size(); i++) {
            Element el = (Element) elList.get(i);
            if (el.isSelected()) {
                el.setSelected(false);
            }
        }

        Draht draht;
        for (int i = 0; i < drahtLst.size(); i++) {
            draht = (Draht) drahtLst.get(i);
            if (draht.isSelected()) {
                draht.setSelected(false);

                for (int j = 0; j < draht.getPolySize(); j++) {
                    PolyPoint p = draht.getPoint(j);
                    draht.deselectAllPoints();
                }
            }
        }

        repaint();

    }

    public StatusBasisIF getStatus() {
        return status;
    }

    public void setModusNone(Element element) {
        status = new StatusNone(this, element);
    }

    public void setModusIdle() {

        addWireFrame = null;
        gummiBand = null;
        addElementModus = null;
        moveElements = null;
        resizeElement = null;
        modusRun = null;

        if (owner.isEditable()) {
            status = leerLauf;
            if (owner.frameCircuit != null) {
                owner.frameCircuit.listeAllElements();
            }
        }
    }

    public void setModusRun() {
        modusRun = new StatusRun(this);
        modusRun.start();
        status = modusRun;
    }

    public void setModusAddWireFrame(int elementID, int pin) {
        if (owner.isEditable()) {
            addWireFrame = new StatusAddWire(this, getGraphics(), elementID, pin);
            status = addWireFrame;
            owner.setChanged(true);
        }
    }

    public void setModusResizeElement(Element element, int rect) {
        if (owner.isEditable()) {
            resizeElement = new StatusResizeElement(this, element, rect);
            status = resizeElement;
            owner.setChanged(true);
        }
    }

    public void setModusResizeBasis(int x, int y) {
        if (owner.isEditable()) {
            resizeBasis = new StatusResizeBasis(this, x, y);
            status = resizeBasis;
        }
    }

    public void setModusGummiband(int x, int y) {
        if (owner.isEditable()) {
            gummiBand = new StatusGummiBand(this, x, y);
            status = gummiBand;
        }
    }

    public void setModusMoveElements(MouseEvent e) {
        if (owner.isEditable()) {
            moveElements = new StatusMoveElements(this, e);
            status = moveElements;
            owner.setChanged(true);
        }
    }

    public void setModusPathEditor(MouseEvent e, Element element) {
        if (owner.isEditable()) {
            splineEdit = new StatusEditPath(this, element);
            status = splineEdit;
            owner.setChanged(true);
        }
    }

    public PolyPoint getPolyPointOverPoint(int x, int y) {
        Draht draht;

        for (int i = 0; i < drahtLst.size(); i++) {
            draht = (Draht) drahtLst.get(i);

            for (int j = 0; j < draht.getPolySize(); j++) {
                PolyPoint p = draht.getPoint(j);

                if (p.getX() > x - 10 && p.getY() > y - 10 && p.getX() < x + 10 && p.getY() < y + 10) {
                    return p;
                }
            }
        }
        return null;
    }

    public boolean isPointOverPolyPoint(int x, int y) {
        Draht draht;

        for (int i = 0; i < drahtLst.size(); i++) {
            draht = (Draht) drahtLst.get(i);

            for (int j = 0; j < draht.getPolySize(); j++) {
                PolyPoint p = draht.getPoint(j);

                if (p.getX() > x - 10 && p.getY() > y - 10 && p.getX() < x + 10 && p.getY() < y + 10) {
                    return true;
                }
            }
        }
        return false;
    }

    public void markAllinRect(int x, int y, int xx, int yy) {
        // Markiere alle Elemente
        Element element;
        for (int i = 0; i < elList.size(); i++) {
            element = (Element) elList.get(i);

            if (element.isVisible() && element.getX() > x && element.getY() > y && (element.getX() + element.getWidth()) < xx && (element.getY() + element.getHeight()) < yy) {
                element.setSelected(true);
            }

        }

        // Ist eine Draht im Bereich x,y,xx,yy
        // an einem Selected Element?
        // Markiere alle PolyPoints
        // Merke : Sind alle Punkte des Drahtes markiert worden,
        // dann Markiere sie alle, ansonsten markiere keine davon
        Draht draht;
        for (int i = 0; i < drahtLst.size(); i++) {
            draht = (Draht) drahtLst.get(i);

            int c = 0;
            for (int j = 0; j < draht.getPolySize(); j++) {
                PolyPoint p = draht.getPoint(j);

                Element src = getElementWithID(draht.sourceElementID);
                Element dst = getElementWithID(draht.destElementID);
                if (src.isSelected() && dst.isSelected() && p.getX() > x && p.getY() > y && p.getX() < xx && p.getY() < yy) {
                    p.setSelected(true);
                    draht.setSelected(true);
                    c++;
                }
            }

            if (c != draht.getPolySize()) {
                for (int j = 0; j < draht.getPolySize(); j++) {
                    PolyPoint p = draht.getPoint(j);
                    p.setSelected(false);
                    draht.setSelected(false);
                }
            }

        }

    }

    private static void swap(ArrayList list, int i, int j) {
        Object T;
        T = list.get(i);
        list.set(i, list.get(j));
        list.set(j, T);
    }

    /* erkennt welche Wire's recursive sind und markiert diese
     * als Resursive Drï¿½hte, denn diese mï¿½ssen beim Sortieren
     * ignoriert werden.
     */
    public void recognizeResursiveWires(Element element) {
        /*element.locked=true;
    for (int i=0;i<element.getPinCount();i++)
    {
    JPin pin = element.getPin(i);
    if (pin.draht!=null)
    {
    int sourceElementID;
    int srcPinID;
    if (pin.draht.getDestElementID()==element.getID())
    {
    sourceElementID=pin.draht.getSourceElementID();
    srcPinID=pin.draht.getSourcePin();
    }
    else
    {
    sourceElementID=pin.draht.getDestElementID();
    srcPinID=pin.draht.getDestPin();
    }
    Element srcEl = (Element)getObjectWithID(sourceElementID);
    JPin srcPin=srcEl.getPin(srcPinID);
    if (srcPin.pinIO==JPin.PIN_INPUT)
    {
    if (srcEl.locked==false)
    {
    recognizeResursiveWires(srcEl);
    srcPin.draht.resursive=false;
    }
    else
    {
    srcPin.draht.resursive=true;
    }
    }
    }
    }
    element.locked=false;*/
    }
    public boolean linksOK = false;
    public boolean rechtsOK = false;
    public boolean topOK = false;
    public boolean bottomOK = false;

    public void isNodeVerschiebbarL(Element node) {
        if (node.getInternName().equalsIgnoreCase("###NODE###")) {
            JPin pin = node.getPin(3);
            if (pin == null || pin.draht == null) {
                linksOK = true;
                return;
            }

            Polygon poly = pin.draht.getPolygon();
            if (pin.draht.getDestElementID() == node.getID()) {
                if (pin.draht.getPolySize() > 2) {
                    linksOK = true;
                    return;
                }
                Element LElement = getElementWithID(pin.draht.getSourceElementID());
                isNodeVerschiebbarL(LElement);
            } else {
                if (pin.draht.getPolySize() > 2) {
                    linksOK = true;
                    return;
                }
                Element LElement = getElementWithID(pin.draht.getDestElementID());
                isNodeVerschiebbarL(LElement);
            }
        }
    }

    public Element[] getAllTestpointElementsDouble() {
        Element[] nodes = getAllTestpointElements();
        ArrayList liste = new ArrayList();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].getPin(0).dataType == ExternalIF.C_DOUBLE) {
                liste.add(nodes[i]);
            }
        }
        Element[] result = new Element[liste.size()];
        for (int i = 0; i < liste.size(); i++) {
            result[i] = (Element) liste.get(i);
        }

        return result;
    }

    public Element[] getAllTestpointElementsBoolean() {
        Element[] nodes = getAllTestpointElements();
        ArrayList liste = new ArrayList();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].getPin(0).dataType == ExternalIF.C_BOOLEAN) {
                liste.add(nodes[i]);
            }
        }
        Element[] result = new Element[liste.size()];
        for (int i = 0; i < liste.size(); i++) {
            result[i] = (Element) liste.get(i);
        }

        return result;
    }

    public Element[] getAllTestpointElements() {
        ArrayList temp = new ArrayList();
        Element element;
        for (int i = 0; i < getElementCount(); i++) {
            element = getElement(i);
            if (element.getInternName().equalsIgnoreCase("###NODE###") && element.jGetTag(0) instanceof String) {
                String str = (String) element.jGetTag(0);

                if (str.equalsIgnoreCase("###TESTPOINT###")) {
                    temp.add(element);
                }
            }
        }

        Element[] result = new Element[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            result[i] = (Element) temp.get(i);
        }

        return result;
    }

    public void isNodeVerschiebbarR(Element node) {
        if (node.getInternName().equalsIgnoreCase("###NODE###")) {
            JPin pin = node.getPin(1);
            if (pin == null || pin.draht == null) {
                rechtsOK = true;
                return;
            }

            if (pin.draht.getSourceElementID() == node.getID()) {
                if (pin.draht.getPolySize() > 2) {
                    rechtsOK = true;
                    return;
                }
                Element RElement = getElementWithID(pin.draht.getDestElementID());

                isNodeVerschiebbarR(RElement);
            } else {
                if (pin.draht.getPolySize() > 2) {
                    rechtsOK = true;
                    return;
                }
                Element RElement = getElementWithID(pin.draht.getSourceElementID());
                isNodeVerschiebbarR(RElement);
            }

        }
    }

    public void isNodeVerschiebbarT(Element node) {
        if (node.getInternName().equalsIgnoreCase("###NODE###")) {
            JPin pin = node.getPin(0);
            if (pin == null || pin.draht == null) {
                topOK = true;
                return;
            }

            Polygon poly = pin.draht.getPolygon();

            if (pin.draht.getDestElementID() == node.getID()) {
                if (pin.draht.getPolySize() > 2) {
                    topOK = true;
                    return;
                }
                Element TElement = getElementWithID(pin.draht.getSourceElementID());
                isNodeVerschiebbarT(TElement);
            } else {
                if (node.getPin(0).draht.getPolySize() > 2) {
                    topOK = true;
                    return;
                }
                Element TElement = getElementWithID(pin.draht.getDestElementID());
                isNodeVerschiebbarT(TElement);
            }
        }
    }

    public void isNodeVerschiebbarB(Element node) {
        if (node.getInternName().equalsIgnoreCase("###NODE###")) {
            JPin pin = node.getPin(2);
            if (pin == null || pin.draht == null) {
                bottomOK = true;
                return;
            }

            if (pin.draht.getSourceElementID() == node.getID()) {
                if (pin.draht.getPolySize() > 2) {
                    bottomOK = true;
                    return;
                }
                Element BElement = getElementWithID(pin.draht.getDestElementID());

                isNodeVerschiebbarB(BElement);
            } else {
                if (pin.draht.getPolySize() > 2) {
                    bottomOK = true;
                    return;
                }
                Element BElement = getElementWithID(pin.draht.getSourceElementID());
                isNodeVerschiebbarB(BElement);
            }

        }
    }

    public void orderNodesL(Element node, int y) {
        if (node.getInternName().equalsIgnoreCase("###NODE###")) {
            node.setLocation(node.getLocation().x, y);

            if (node.getPin(3).draht != null) {
                Polygon poly = node.getPin(3).draht.getPolygon();
                Element LElement;
                if (poly.npoints > 2) {
                    return;
                }
                if (node.getPin(3).draht.getDestElementID() == node.getID()) {
                    LElement = getElementWithID(node.getPin(3).draht.getSourceElementID());
                } else {
                    LElement = getElementWithID(node.getPin(3).draht.getDestElementID());
                }
                orderNodesL(LElement, y);
            }
        }
    }

    public void orderNodesT(Element node, int x) {
        if (node.getInternName().equalsIgnoreCase("###NODE###")) {
            node.setLocation(x, node.getLocation().y);

            if (node.getPin(0).draht != null) {
                Polygon poly = node.getPin(0).draht.getPolygon();
                Element TElement;
                if (poly.npoints > 2) {
                    return;
                }
                if (node.getPin(0).draht.getDestElementID() == node.getID()) {
                    TElement = getElementWithID(node.getPin(0).draht.getSourceElementID());
                } else {
                    TElement = getElementWithID(node.getPin(0).draht.getDestElementID());
                }
                orderNodesT(TElement, x);
            }
        }
    }

    public void orderNodesB(Element node, int x) {
        if (node.getInternName().equalsIgnoreCase("###NODE###")) {
            node.setLocation(x, node.getLocation().y);

            if (node.getPin(2).draht != null) {
                Polygon poly = node.getPin(2).draht.getPolygon();
                Element BElement;

                if (poly.npoints > 2) {
                    return;
                }
                if (node.getPin(2).draht.getSourceElementID() == node.getID()) {
                    BElement = getElementWithID(node.getPin(2).draht.getDestElementID());
                } else {
                    BElement = getElementWithID(node.getPin(2).draht.getSourceElementID());
                }

                orderNodesB(BElement, x);
            }
        }
    }

    public void orderNodesR(Element node, int y) {
        if (node.getInternName().equalsIgnoreCase("###NODE###")) {
            node.setLocation(node.getLocation().x, y);

            if (node.getPin(1).draht != null) {
                Polygon poly = node.getPin(1).draht.getPolygon();
                Element RElement;

                if (poly.npoints > 2) {
                    return;
                }
                if (node.getPin(1).draht.getSourceElementID() == node.getID()) {
                    RElement = getElementWithID(node.getPin(1).draht.getDestElementID());
                } else {
                    RElement = getElementWithID(node.getPin(1).draht.getSourceElementID());
                }

                orderNodesR(RElement, y);
            }
        }
    }

    public void reorderNodes(Element nodeXXX) {
        JPin pin;
        Element srcElement = null;
        JPin srcPin = null;
        int value = 0;

        for (int i = 0; i < getElementCount(); i++) {
            Element node = getElement(i);

            if (node.getInternName().equalsIgnoreCase("###NODE###")) {
                int w2 = node.getWidth() / 2;
                int h2 = node.getHeight() / 2;

                for (int j = 0; j < 4; j++) {
                    pin = node.getPin(j);

                    if (pin != null && pin.draht != null && pin.draht.getPolySize() == 2) {
                        if (pin.getPinAlign() == 3 || pin.getPinAlign() == 1) {
                            Polygon poly = pin.draht.getPolygon();
                            //if (poly.xpoints[0]<poly.xpoints[poly.npoints-1])
                            {
                                if (pin.pinIO == JPin.PIN_INPUT) {
                                    srcElement = (Element) getObjectWithID(pin.draht.getSourceElementID());
                                    srcPin = srcElement.getPin(pin.draht.getSourcePin());
                                    value = srcElement.getLocation().y + srcPin.getLocation().y + 5;
                                    node.setLocation(node.getLocation().x, value - h2);
                                } else if (pin.pinIO == JPin.PIN_OUTPUT) {
                                    srcElement = (Element) getObjectWithID(pin.draht.getDestElementID());
                                    srcPin = srcElement.getPin(pin.draht.getDestPin());
                                    value = srcElement.getLocation().y + srcPin.getLocation().y + 5;
                                    node.setLocation(node.getLocation().x, value - h2);
                                }
                            }
                        }


                        /*if (pin.getPinAlign()==3 || pin.getPinAlign()==1)
                    {
                    Polygon poly =pin.draht.getPolygon();
                    if (pin.pinIO==JPin.PIN_INPUT)
                    {
                    srcElement = (Element)getObjectWithID(pin.draht.getSourceElementID());
                    scrPin= srcElement.getPin(pin.draht.getSourcePin());
                    if (srcElement.getInternName().equalsIgnoreCase("###NODE###") && srcElement!=node && (scrPin.getPinAlign()==1 || scrPin.getPinAlign()==3))
                    {
                    srcElement.setLocation(srcElement.getLocation().x,node.getLocation().y);
                    reorderNodes(srcElement);
                    }
                    }else
                    if (pin.pinIO==JPin.PIN_OUTPUT)
                    {
                    srcElement = (Element)getObjectWithID(pin.draht.getDestElementID());
                    scrPin= srcElement.getPin(pin.draht.getDestPin());
                    if (srcElement.getInternName().equalsIgnoreCase("###NODE###") && srcElement!=node && (scrPin.getPinAlign()==1 || scrPin.getPinAlign()==3))
                    {
                    srcElement.setLocation(srcElement.getLocation().x,node.getLocation().y);
                    //reorderNodes(srcElement);
                    }
                    }
                    }*/
 /*if (pin.getPinAlign()==0 || pin.getPinAlign()==2)
                    {
                    Element srcElement=null;
                    Polygon poly =pin.draht.getPolygon();
                    if (pin.pinIO==JPin.PIN_INPUT)
                    {
                    srcElement = (Element)getObjectWithID(pin.draht.getSourceElementID());
                    element.setLocation(poly.xpoints[0]-w2,element.getLocation().y);
                    }else
                    if (pin.pinIO==JPin.PIN_OUTPUT)
                    {
                    srcElement = (Element)getObjectWithID(pin.draht.getDestElementID());
                    //element.setLocation(element.getLocation().x,poly.ypoints[poly.npoints-1]-15);
                    element.setLocation(poly.xpoints[poly.npoints-1]-w2,element.getLocation().y);
                    }
                    }*/
                    }
                }
            }
        }
    }

    private void FlowChartElementRekursivAbsteigenUndElementeInDerMitteAnordnen(Element elementX) {
        if (elementX == null | elementX.isAlreadyCompiled) {
            return;
        }

        int pinBottomNr = elementX.getPinsTop() + elementX.getPinsRight() + elementX.getPinsBottom() - 1;

        elementX.isAlreadyCompiled = true;

        JPin pin = elementX.getPin(pinBottomNr);

        if (pin != null && pin.pinIO == JPin.PIN_OUTPUT && pin.pinAlign == 2) // also nur Unteres Pin!
        {
            Draht draht = pin.draht;

            if (draht != null) {
                Element elementUnten = getElementWithID(draht.getDestElementID());
                if (elementUnten != null && elementUnten.isAlreadyCompiled == false) {
                    int mx1 = elementX.getX() + (elementX.getWidth() / 2);
                    int mx2 = elementUnten.getWidth() / 2;

                    elementUnten.setLocation(mx1 - mx2, elementX.getY() + elementX.getHeight() + 15);
                    FlowChartElementRekursivAbsteigenUndElementeInDerMitteAnordnen(elementUnten);
                }
            }
        }
    }

    private void reorderFlowChartLines() {
        VMObject vm = this;

        for (int i = 0; i < vm.getElementCount(); i++) {
            Element elementX = vm.getElement(i);

            if (elementX.getInternName().indexOf("#FLOWCHART") > -1 | elementX.getInternName().indexOf("#MCU-FLOWCHART") > -1) {
                int pinBottomNr = elementX.getPinsTop() + elementX.getPinsRight() + elementX.getPinsBottom() - 1;

                JPin pin = elementX.getPin(pinBottomNr);

                if (pin != null && pin.pinIO == JPin.PIN_OUTPUT && pin.pinAlign == 2) // also nur Unteres Pin!
                {
                    Draht draht = pin.draht;

                    if (draht != null && draht.getPolySize() == 4) {
                        Element elementUnten = getElementWithID(draht.getDestElementID());

                        if (elementUnten.getY() > elementX.getY() + elementX.getHeight()) {
                            int distance = Math.abs(elementUnten.getY() - elementX.getY());

                            if (distance < 150) {
                                PolyPoint point1 = draht.getPoint(1);
                                PolyPoint point2 = draht.getPoint(2);

                                int m1 = elementX.getY() + elementX.getHeight();
                                int m2 = elementUnten.getY();
                                int my = (m1 + m2) / 2;
                                point1.setLocation(10, my);
                                point2.setLocation(10, my);
                            }
                        }
                    }
                }
            }
            /*if (elementX.getInternName().indexOf("#FLOWCHART-START#") > -1)
            {
                FlowChartElementRekursivAbsteigenUndElementeInDerMitteAnordnen(elementX);
            }*/
        }

        for (int i = 0; i < vm.getElementCount(); i++) {
            Element elementX = vm.getElement(i);
            elementX.isAlreadyCompiled = false;
        }

    }

    public void reorderWireFrames() {
        Draht draht;
        Element element;
        int sourcePin;
        int destPin;

        ProcessPinDataType();
        processElementChanged();

        reorderFlowChartLines();

        if (elList.size() > 0) {
            //for (int i=0;i<elList.size();i++)
            {
                Element el = getElement(0);
                recognizeResursiveWires(el);
            }
        }

        for (int i = 0; i < drahtLst.size(); i++) {
            draht = (Draht) drahtLst.get(i);

            element = (Element) getObjectWithID(draht.getSourceElementID());
            if (element == null) {
                return;
            }
            sourcePin = draht.getSourcePin();

            int ax = 0, ay = 0;
            JPin pin = element.getPin(sourcePin);
            if (pin != null) {
                switch (pin.getPinAlign()) {
                    case 0: // top
                    {
                        ax = element.getX() + pin.getX() + pin.getWidth() / 2;
                        ay = element.getY() + pin.getY();

                        if (draht.getPolySize() > 2) {
                            PolyPoint pp = draht.getFirstPoint();
                            pp.setLeft(ax);
                        }
                        break;
                    }
                    case 1: //right
                    {
                        ax = element.getX() + pin.getX() + pin.getWidth();
                        ay = element.getY() + pin.getY() + pin.getHeight() / 2;

                        if (draht.getPolySize() > 2) {
                            PolyPoint pp = draht.getFirstPoint();
                            pp.setTop(ay);
                        }

                        break;
                    }
                    case 2: // bottom
                    {
                        ax = element.getX() + pin.getX() + pin.getWidth() / 2;
                        ay = element.getY() + pin.getY() + pin.getHeight();

                        if (draht.getPolySize() > 2) {
                            PolyPoint pp = draht.getFirstPoint();
                            pp.setLeft(ax);
                        }
                        break;
                    }
                    case 3: //left
                    {
                        ax = element.getX() + pin.getX();
                        ay = element.getY() + pin.getY() + pin.getHeight() / 2;

                        if (draht.getPolySize() > 2) {
                            PolyPoint pp = draht.getFirstPoint();
                            pp.setTop(ay);
                        }

                        break;
                    }
                }
            }
            draht.setPoint(0, ax, ay);

            element = (Element) getObjectWithID(draht.getDestElementID());
            destPin = draht.getDestPin();

            if (element != null) {
                pin = element.getPin(destPin);

                if (pin != null) {
                    switch (pin.getPinAlign()) {
                        case 0: //top
                        {
                            ax = element.getX() + pin.getX() + pin.getWidth() / 2;
                            ay = element.getY() + pin.getY();

                            if (draht.getPolySize() > 2) {
                                PolyPoint pp = draht.getLastPoint();
                                pp.setLeft(ax);
                            }
                            break;
                        }
                        case 1: //right
                        {
                            ax = element.getX() + pin.getX() + pin.getWidth();
                            ay = element.getY() + pin.getY() + pin.getHeight() / 2;

                            if (draht.getPolySize() > 2) {
                                PolyPoint pp = draht.getLastPoint();
                                pp.setTop(ay);
                            }

                            break;
                        }
                        case 2: //bottom
                        {
                            ax = element.getX() + pin.getX() + pin.getWidth() / 2;
                            ay = element.getY() + pin.getY() + pin.getHeight();
                            if (draht.getPolySize() > 2) {
                                PolyPoint pp = draht.getLastPoint();
                                ;
                                pp.setLeft(ax);
                            }

                            break;
                        }
                        case 3: //left
                        {
                            ax = element.getX() + pin.getX();
                            ay = element.getY() + pin.getY() + pin.getHeight() / 2;

                            if (draht.getPolySize() > 2) {
                                PolyPoint pp = draht.getLastPoint();
                                pp.setTop(ay);
                            }
                            break;
                        }
                    }
                }
            }
            draht.setPoint(draht.getPolySize() - 1, ax, ay);
        }

        //reorderNodes(null);
        repaint();
    }

    public Point ermittleMitteXY(ArrayList pins) {
        int mittelX = 0;
        int mittelY = 0;
        for (int i = 0; i < pins.size(); i++) {
            Element el = (Element) pins.get(i);
            mittelX += el.getLocation().x;
            mittelY += el.getLocation().y;
        }
        return new Point(mittelX / pins.size(), mittelY / pins.size());
    }

    public Element getPinsXBar(String barName) {
        for (int i = 0; i < getElementCount(); i++) {
            Element el = getElement(i);

            if (el != null) {
                String name = el.classRef.xgetName();
                if (name.equals(barName)) {
                    return el;
                }
            }
        }
        return null;
    }

    public void sortPinsForX(ArrayList liste) {
        Element element = null;
        Element el1 = null;
        Element el2 = null;

        for (int j = liste.size() - 1; j >= 0; j--) {
            for (int i = 0; i < j; i++) {
                el1 = (Element) liste.get(i);
                el2 = (Element) liste.get(i + 1);

                int x1 = el1.getX();
                int x2 = el2.getX();

                if (x1 > x2) {
                    Element a = (Element) liste.get(i);
                    Element b = (Element) liste.get(i + 1);
                    liste.set(i, b);
                    liste.set(i + 1, a);
                }
            }
        }

    }

    public void sortPinsForY(ArrayList liste) {
        Element element = null;
        Element el1 = null;
        Element el2 = null;

        for (int j = liste.size() - 1; j >= 0; j--) {
            for (int i = 0; i < j; i++) {
                el1 = (Element) liste.get(i);
                el2 = (Element) liste.get(i + 1);

                int y1 = el1.getY();
                int y2 = el2.getY();

                if (y1 > y2) {
                    Element a = (Element) liste.get(i);
                    Element b = (Element) liste.get(i + 1);
                    liste.set(i, b);
                    liste.set(i + 1, a);
                }
            }
        }

    }

    public void getPinsAllXBar(String barName, ArrayList liste) {
        for (int i = 0; i < getElementCount(); i++) {
            Element el = getElement(i);

            if (el != null) {
                String name = el.classRef.xgetName();
                if (name.equals(barName)) {
                    liste.add(el);
                }
            }
        }

    }

    public Rectangle getMinMaxBounds() {
        int minX = 99999999;
        int minY = 99999999;
        int maxX = 10;
        int maxY = 10;

        // zuerst die Elemente
        for (int i = 0; i < getElementCount(); i++) {
            Element el = getElement(i);

            if (el.getX() < minX) {
                minX = el.getX();
            }
            if (el.getY() < minY) {
                minY = el.getY();
            }
            if (el.getX() + el.getWidth() > maxX) {
                maxX = el.getX() + el.getWidth();
            }
            if (el.getY() + el.getHeight() > maxY) {
                maxY = el.getY() + el.getHeight();
            }
        }

        // und nun die Draehte
        for (int i = 0; i < getDrahtCount(); i++) {
            Draht draht = getDraht(i);

            Rectangle r = draht.getBounds();

            if (r.x < minX) {
                minX = r.x;
            }
            if (r.y < minY) {
                minY = r.y;
            }
            if (r.width > maxX) {
                maxX = r.width;
            }
            if (r.height > maxY) {
                maxY = r.height;
            }
        }

        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    // ***************** Begin BasisIF ********************
    // Diese Events kommen aus Element und Element bekommt diese aus jedem Pin
    public void elementPinMousePressed(MouseEvent e, int elementID, int pin) {
        if (status != null) {
            status.elementPinMousePressed(e, elementID, pin);
        }
    }

    public void elementPinMouseReleased(MouseEvent e, int elementID, int pin) {
        if (status != null) {
            status.elementPinMouseReleased(e, elementID, pin);
        }
    }

    public void elementPinMouseClicked(MouseEvent e, int elementID, int pin) {

    }

    public void elementPinMouseEntered(MouseEvent e, int elementID, int pin) {

    }

    public void elementPinMouseExited(MouseEvent e, int elementID, int pin) {

    }

    public void elementPinMouseDragged(MouseEvent e, int elementID, int pin) {

    }

    public void elementPinMouseMoved(MouseEvent e, int elementID, int pin) {
        if (status != null) {
            status.elementPinMouseMoved(e, elementID, pin);
        }
    }

    // ***************** Ende BasisIF ********************
    // ****************Begin Events auf den Element ******************
    public void elementProcessKeyEvent(KeyEvent ke) {
        if (status != null) {
            status.processKeyEvent(ke);
        }
    }

    public void elementMouseDblClick(MouseEvent e) {
        if (status != null) {
            status.mouseDblClick(e);
        }
    }

    public void elementMouseClicked(MouseEvent e) {
        if (status != null) {
            status.mouseClicked(e);
        }
    }

    public void elementMouseEntered(MouseEvent e) {
        if (status != null) {
            status.mouseEntered(e);
        }
    }

    public void elementMouseExited(MouseEvent e) {
        if (status != null) {
            status.mouseExited(e);
        }
    }

    public void elementMouseReleased(MouseEvent e) {
        if (status != null) {
            status.mouseReleased(e);
        }
    }

    public void elementMousePressed(MouseEvent e) {
        if (status != null) {
            status.mousePressed(e);
        }
    }

    public void elementMouseDragged(MouseEvent e) {
        if (status != null) {
            status.mouseDragged(e);
        }
    }

    public void elementMouseMoved(MouseEvent e) {
        if (status != null) {
            status.mouseMoved(e);
        }
    }

    // **************** Ende Events auf den Element ******************
    public Point pointToRaster(Element element, int x, int y) {
        Point p = new Point(x, y);

        if (isAlignToGrid() && element.rasterized) {
            int cellWidth = getRasterX();
            int cellHeight = getRasterY();

            int fx = (int) ((double) x + (double) cellWidth / 2.0) / cellWidth;
            int fy = (int) ((double) y + (double) cellHeight / 2.0) / cellHeight;

            p.x = fx * cellWidth;
            p.y = fy * cellHeight;
        }

        return p;
    }

    public Point pointToRaster(int x, int y) {
        Point p = new Point(x, y);

        if (isAlignToGrid()) {
            int cellWidth = getRasterX();
            int cellHeight = getRasterY();

            int fx = (int) ((double) x + (double) cellWidth / 2.0) / cellWidth;
            int fy = (int) ((double) y + (double) cellHeight / 2.0) / cellHeight;

            p.x = fx * cellWidth;
            p.y = fy * cellHeight;
        }

        return p;
    }

    public Element getElementWhereInMouse(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        for (int i = elList.size() - 1; i >= 0; i--) {
            Element el = (Element) elList.get(i);
            if (el.isVisible()) {
                int x1 = el.getX();
                int y1 = el.getY();
                int x2 = x1 + el.getWidth();
                int y2 = y1 + el.getHeight();

                if (x >= x1 && y > y1 && x < x2 && y < y2) {
                    e.setSource(el);
                    e.translatePoint(-el.getRealX(), -el.getRealY());
                    return el;
                }
            }
        }
        return null;
    }

    public Element getElementWhereInMouse(int x, int y, int pinSize) {

        for (int i = elList.size() - 1; i >= 0; i--) {
            Element el = (Element) elList.get(i);
            if (el.isVisible()) {
                int x1 = el.getX() + pinSize;
                int y1 = el.getY() + pinSize;
                int x2 = x1 + el.getWidth() - pinSize * 2;
                int y2 = y1 + el.getHeight() - pinSize * 2;

                if (x > x1 && x < x2 && y > y1 && y < y2) {
                    return el;
                }
            }
        }
        return null;
    }

    public void saveElements(FileSystemOutput fsOut, boolean onlySelected) {
        try {
            for (int i = getComponentCount() - 1; i >= 0; i--) {
                Component comp = getComponent(i);

                if (comp instanceof Element) {
                    Element el = (Element) comp;
                    if (onlySelected) {
                        if (el.isSelected()) {
                            el.saveToStream(fsOut);
                        }
                    } else {
                        el.saveToStream(fsOut);
                    }
                }
            }
            fsOut.postItem();
        } catch (Exception ex) {
            owner.showErrorMessage("" + ex.toString());
        }
    }

    public void saveDraehte(FileSystemOutput fsOut, boolean onlySelected) {
        try {
            FileOutputStream fos = fsOut.addItem(java.util.ResourceBundle.getBundle("VisualLogic/VMObject").getString("Element"));
            DataOutputStream dos = new DataOutputStream(fos);

            for (int i = 0; i < drahtLst.size(); i++) {
                Draht draht = (Draht) drahtLst.get(i);

                if (onlySelected) {
                    if (draht.isSelected()) {
                        dos.writeInt(draht.sourceElementID);
                        dos.writeInt(draht.destElementID);
                        dos.writeInt(draht.sourcePin);
                        dos.writeInt(draht.destPin);
                        dos.writeInt(draht.getID());
                        draht.saveToStream(dos);
                    }
                } else {
                    dos.writeInt(draht.sourceElementID);
                    dos.writeInt(draht.destElementID);
                    dos.writeInt(draht.sourcePin);
                    dos.writeInt(draht.destPin);
                    dos.writeInt(draht.getID());
                    draht.saveToStream(dos);
                }
            }
            fsOut.postItem();
        } catch (Exception ex) {
            owner.showErrorMessage("" + ex.toString());
        }
    }

    public int getSelectetElementCount() {
        int count = 0;
        for (int i = 0; i < elList.size(); i++) {
            Element element = getElement(i);
            if (element.isSelected()) {
                count++;
            }
        }
        return count;
    }

    public int getSelectedDrahtCount() {
        int count = 0;
        for (int i = 0; i < drahtLst.size(); i++) {
            Draht draht = getDraht(i);
            if (draht.isSelected()) {
                count++;
            }
        }
        return count;
    }

    public void saveToStream(FileSystemOutput fsOut, String filename, boolean onlySelected) {
        try {
            FileOutputStream fos = fsOut.addItem(filename);
            DataOutputStream dos = new DataOutputStream(fos);

            dos.writeInt(getWidth());
            dos.writeInt(getHeight());

            int r = getBackground().getRed();
            int g = getBackground().getGreen();
            int b = getBackground().getBlue();

            dos.writeInt(r);
            dos.writeInt(g);
            dos.writeInt(b);

            dos.writeInt(propertyList.size());
            for (int i = 0; i < propertyList.size(); i++) {
                BasisProperty prop = (BasisProperty) propertyList.get(i);

                dos.writeInt(prop.elementID);
                dos.writeInt(prop.propertyIndex);
            }
            if (onlySelected) {
                dos.writeInt(getSelectetElementCount());
            } else {
                dos.writeInt(elList.size());
            }

            if (onlySelected) {
                dos.writeInt(getSelectedDrahtCount());
            } else {
                dos.writeInt(drahtLst.size());
            }

            fsOut.postItem();

            saveElements(fsOut, onlySelected);
            saveDraehte(fsOut, onlySelected);

        } catch (Exception ex) {
            owner.showErrorMessage("" + ex.toString());
        }

    }

    public void readElements(int size, FileSystemInput fsIn, boolean fromAblage, ArrayList ElemetTabelle) {
        int id = -1;

        for (int i = 0; i < size; i++) {

            try {
                FileInputStream fis = fsIn.gotoItem(owner.fileCount++);
                DataInputStream stream = new DataInputStream(fis);

                String classPfad = stream.readUTF(); //classPath
                String className = stream.readUTF(); //className
                int oldid = stream.readInt();        //id

                String definitionPath = "";

                if (fileVersion >= 3.10) {
                    definitionPath = stream.readUTF();
                }

                String mainPath = stream.readUTF();
                String binPath = stream.readUTF();

                int nameID = stream.readInt();

                VisualLogic.Element element = null;

                Integer oldElementID = -1;
                Integer newElementID = -1;

                if (fromAblage) {
                    id = getObjectID();

                    oldElementID = new Integer(oldid);
                    newElementID = new Integer(id);

                    ElemetTabelle.add(oldElementID);
                    ElemetTabelle.add(newElementID);
                } else {
                    id = oldid;
                }

                String[] args = null;
                element = new Element(id, this, elementPath, mainPath, binPath, className, definitionPath, args);
                reserveObjectID(id, element);

                int idnum = nameID;

                element.definitionPath = definitionPath;

                element.setNameID(idnum);

                if (fromAblage) {
                    element.oldID = oldElementID;
                }
                element.loadedFromAblageFlag = fromAblage;

                element.loadFromStream(fis, fromAblage);

                elList.add(element);
                this.add(element, 0);
                element.setSelected(false);

                if (progressBar != null) {
                    String str;
                    if (owner.getCircuitBasis() == this) {
                        str = "CircuitBasis";
                    } else {
                        str = "FrontBasis";
                    }

                    Tools.dialogWait.label2.setText(element.className + ":" + "\"" + element.getCaption() + "\"" + " -> " + str + " : " + elementsCount);
                    elementsCount++;
                    progressBar.setValue(progress++);
                }

            } catch (Exception ex) {
                beendeWaitDialog();
                owner.showErrorMessage(java.util.ResourceBundle.getBundle("VisualLogic/VMObject").getString("Element_konnte_nicht_erfolgreich_erzeugt_werden_:") + ex.toString());
            }

        }

    }

    public void verknuepfeDraehte() {
        for (int i = 0; i < getDrahtCount(); i++) {
            Draht draht = getDraht(i);

            Element element = (Element) getObjectWithID(draht.sourceElementID);
            element.getPin(draht.sourcePin).draht = draht;

            element = (Element) getObjectWithID(draht.destElementID);
            element.getPin(draht.destPin).draht = draht;
        }
    }

    public boolean pinExist(int destElementID, int destPin) {
        Element element = getElementWithID(destElementID);

        if (destPin < element.getPinCount()) {
            return true;
        } else {
            return false;

        }
    }

    public void readDrahts(int size, FileSystemInput fsIn, boolean fromAblage, ArrayList ElemetTabelle) {
        FileInputStream fis = fsIn.gotoItem(owner.fileCount++);
        DataInputStream stream = new DataInputStream(fis);
        try {

            for (int i = 0; i < size; i++) {
                if (progressBar != null) {
                    progressBar.setValue(progress++);
                }

                int sourceElementID = -1;
                int destElementID = -1;
                int sourcePin = -1;
                int destPin = -1;
                int oldid = -1;

                sourceElementID = stream.readInt();
                destElementID = stream.readInt();
                sourcePin = stream.readInt();
                destPin = stream.readInt();
                oldid = stream.readInt();

                int oki = 0;
                int id = -1;

                if (fromAblage) {
                    id = getObjectID();
                    boolean srcOK = false;
                    boolean dstOK = false;
                    for (int k = 0; k < ElemetTabelle.size();) {
                        Integer oldElementID = (Integer) ElemetTabelle.get(k++);
                        Integer newElementID = (Integer) ElemetTabelle.get(k++);

                        if (srcOK == false && sourceElementID == oldElementID.intValue()) {
                            sourceElementID = newElementID.intValue();
                            srcOK = true;
                        }
                        if (dstOK == false && destElementID == oldElementID.intValue()) {
                            destElementID = newElementID.intValue();
                            dstOK = true;
                        }
                        if (srcOK && dstOK) {
                            break;
                        }
                    }
                    //if (getElementWithID(sourceElementID)!=null)
                    {
                        Draht draht = addDraht(id, sourceElementID, sourcePin, destElementID, destPin);
                        /*Element element=(Element)getObjectWithID(sourceElementID);
                        element.getPin(sourcePin).draht=draht;
                        element=(Element)getObjectWithID(destElementID);
                        element.getPin(destPin).draht=draht;*/

                        reserveObjectID(id, draht);
                        draht.loadFromStream(stream);
                        draht.setSelected(true);
                        draht.selectAnyPoints(true);
                    }

                } else if (getElementWithID(sourceElementID) != null && getElementWithID(destElementID) != null && pinExist(sourceElementID, sourcePin) && pinExist(destElementID, destPin)) {
                    id = oldid;
                    Draht draht = addDraht(id, sourceElementID, sourcePin, destElementID, destPin);
                    reserveObjectID(id, draht);
                    draht.loadFromStream(stream);
                }
            }
        } catch (Exception ex) {
            beendeWaitDialog();
            owner.showErrorMessage("" + ex.toString());
        }

    }
    //JProgressBar progress =null;
    JLabel label = null;
    JPanel statusPanel = null;
    private double fileVersion = 0;

    public double getFileVersion() {
        return fileVersion;
    }
    /*public void createWaitWindow()
    {
    if (owner.frameCircuit!=null)
    {
    frmWait = new DialogWait();
    frmWait.stop=false;
    // frmWait.setLocation(frmWait.getX(),frmWait.getY()-100);
    frmWait.setVisible(true);
    String strText;
    if (this==owner.getCircuitBasis())
    {
    strText=java.util.ResourceBundle.getBundle("VisualLogic/Basic").getString("loadingCircuitVM");
    }
    else
    {
    strText=java.util.ResourceBundle.getBundle("VisualLogic/Basic").getString("loadingPanelVM");
    }
    frmWait.label1.setText(strText);
    }
    }*/
    public JProgressBar progressBar;
    public int progress = 0;

    public void loadFromStream(FileSystemInput fsIn, boolean fromAblage, String ver) {
        if (owner.frameCircuit != null) {
            //Tools.dialogWait= new DialogWait();

            owner.frameCircuit.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (Tools.dialogWait != null) {
                progressBar = Tools.dialogWait.jProgressBar1;
            }

            String strText;
            if (this == owner.getCircuitBasis()) {
                strText = java.util.ResourceBundle.getBundle("VisualLogic/Basic").getString("loadingCircuitVM");
            } else {
                strText = java.util.ResourceBundle.getBundle("VisualLogic/Basic").getString("loadingPanelVM");
            }
            if (Tools.dialogWait != null) {
                Tools.dialogWait.label1.setText(strText);
            }

            progress = 0;
        }

        ArrayList ElemetTabelle = new ArrayList();
        try {
            FileInputStream fis = fsIn.gotoItem(owner.fileCount++);
            DataInputStream stream = new DataInputStream(fis);

            int w = stream.readInt();
            int h = stream.readInt();
            int r = stream.readInt();
            int g = stream.readInt();
            int b = stream.readInt();

            double dblVersion = Double.parseDouble(ver);
            fileVersion = dblVersion;
            if (dblVersion >= 3.01) {
                propertyList.clear();
                int count = stream.readInt();
                for (int i = 0; i < count; i++) {
                    int elementID = stream.readInt();
                    int propertyIndex = stream.readInt();
                    BasisProperty prop = new BasisProperty(this, elementID, propertyIndex);
                    propertyList.add(prop);
                }
            }

            if (fromAblage == false) {
                setSize(w, h);
                setBackground(new Color(r, g, b));
            }

            int elsize = stream.readInt();
            int drsize = stream.readInt();

            if (progressBar != null) {
                progressBar.setMaximum(elsize + drsize);
            }
            elementsCount = 0;
            readElements(elsize, fsIn, fromAblage, ElemetTabelle);

            readDrahts(drsize, fsIn, fromAblage, ElemetTabelle);
        } catch (Exception ex) {
            beendeWaitDialog();
            owner.showErrorMessage(java.util.ResourceBundle.getBundle("VisualLogic/VMObject").getString("Fehler_in_Basis.loadFromStream()_:") + ex.toString());
        }

        if (fromAblage) {
            for (int k = 0; k < ElemetTabelle.size();) {
                Integer elementOldid = (Integer) ElemetTabelle.get(k);
                Integer elementId = (Integer) ElemetTabelle.get(k + 1);

                SelectElement(elementId.intValue());
                k += 2;
            }
        }

        ElemetTabelle.clear();
        ElemetTabelle = null;

        beendeWaitDialog();
    }

    public void beendeWaitDialog() {
        if (owner.frameCircuit != null) {
            owner.frameCircuit.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            if (progressBar != null) {
                progressBar.setValue(0);
                progressBar = null;
            }
        }
    }

    public void korrigiereFehler() {
        for (int i = 0; i < getElementCount(); i++) {
            Element el = getElement(i);

            for (int j = 0; j < el.getPinCount(); j++) {
                JPin pin = el.getPin(j);
                if (pin.draht != null) {
                    Draht draht = pin.draht;

                    Element sourceElement = (Element) getObjectWithID(draht.getSourceElementID());
                    Element destElement = (Element) getObjectWithID(draht.getDestElementID());

                    if (draht.getSourcePin() > sourceElement.getPinCount() - 1) {
                        deleteDraht(draht);
                    }
                    if (draht.getDestPin() > destElement.getPinCount() - 1) {
                        deleteDraht(draht);
                    }

                }
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        /*if (e.getClickCount() == 2)
    {
    Element el=getElementWhereInMouse(e);
    if (el!=null)
    {
    el.mouseDblClick(e);
    }
    } else
    {
    if (status!=null) status.mouseClicked(e);
    }*/
    }

    //When the mouse cursor enters the canvas make it the two
    //straigth lines type
    public void mouseEntered(MouseEvent e) {
        if (status != null) {
            status.mouseEntered(e);
        }
    }

    //When mouse exits canvas set to default type
    public void mouseExited(MouseEvent e) {
        if (status != null) {
            status.mouseExited(e);
        }
    }

    //mouse listener for when the mouse button is released
    public void mouseReleased(MouseEvent e) {
        aktuellIstBasis = false;
        if (status != null) {
            status.mouseReleased(e);
        }

        /*if (aktuellesElement!=null)
    {
    e.setSource(aktuellesElement);
    e.translatePoint(-aktuellesElement.getRealX(),-aktuellesElement.getRealY());
    aktuellesElement.mouseReleased(e);
    aktuellesElement=null;
    }
    else
    {
    Element el=getElementWhereInMouse(e);
    if (el!=null) el.mouseReleased(e);
    else
    if (status!=null) status.mouseReleased(e);
    }*/
    }

    public void mousePressed(MouseEvent e) {
        /* Element el=getElementWhereInMouse(e);
        if (el!=null)
        {
        aktuellesElement=el;
        el.mousePressed(e);
        aktuellIstBasis=false;
        }
        else*/
        {
            aktuellIstBasis = true;
            if (status != null) {
                status.mousePressed(e);
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
        /*if (aktuellesElement!=null)
        {
        e.setSource(aktuellesElement);
        //e.translatePoint(-aktuellesElement.getRealX(),-aktuellesElement.getRealY());
        aktuellesElement.mouseDragged(e);
        }
        else*/
        {
            if (!aktuellIstBasis) {
                Element el = getElementWhereInMouse(e);

                if (el != null) {
                    el.mouseDragged(e);
                }
            } else if (status != null) {
                status.mouseDragged(e);
            }

        }
    }

    public Line getLineInNaehe(Point p) {
        for (int i = 0; i < drahtLst.size(); i++) {
            Draht draht = getDraht(i);

            Line line = draht.getLineInDerNaehe(p);
            if (line != null) {
                return line;
            }
        }
        return null;
    }

    public void mouseMoved(MouseEvent e) {

        if (status != null) {
            status.mouseMoved(e);
        }
    }

    // begin VSBasisIF
    public void vsPaint(java.awt.Graphics g, int x, int y) {
        if (g != null) {
            subLocation.x = x;
            subLocation.y = y;
            paint(g);
        }
    }

    public void vsSetBackgroungColor(Color color) {
        setBackground(color);
    }

    public void vsStart() {
        this.start();
    }

    public void vsStop() {
        this.stop();
    }

    public void vsProcess() {
        this.processAllElements();
    }

    public void vsLoadFromFile(String fileName) {
        //loadFromXML(fileName,false);
    }

    public void vsMousePressed(MouseEvent e) {
        //e.setSource(this);
        this.mousePressed(e);
    }

    public void vsMouseReleased(MouseEvent e) {
        //e.setSource(this);
        this.mouseReleased(e);
    }

    public void vsMouseMoved(MouseEvent e) {
        //MouseEvent newE = new MouseEvent(this.owner,e.getID(),e.getWhen(),e.getModifiers(),e.getX(),e.getY(),e.getClickCount(),false);
        //e.setSource(this);
        //this.mouseMoved(e);
    }

    public void vsMouseDragged(MouseEvent e) {
        //MouseEvent newE = new MouseEvent(this.owner,e.getID(),e.getWhen(),e.getModifiers(),e.getX(),e.getY(),e.getClickCount(),false);
        //e.setSource(this);
        this.mouseDragged(e);
    }

    public void vsloadFromXML(org.w3c.dom.Element nodeElement) {
        //loadFromXML(nodeElements, false);
    }

    public void vssaveToXML(org.w3c.dom.Element nodeElement) {

    }

    /*
    public void repaint(Rectangle r)
    {
    repaint(r);
    }*/
 /*public void paint(Graphics g)
    {
    }*/
    private void paintGrid(Graphics g) {
        int x, y;

        g.setColor(new Color(170, 170, 170));
        if (owner != null) {
            if (owner.getCircuitBasis() == this) {
                g.setColor(new Color(230, 230, 230));
            }
        }

        for (x = 0; x < getWidth(); x += rasterX) {
            g.drawRect(x, 0, x, getHeight());
        }
        for (y = 0; y < getHeight(); y += rasterY) {
            g.drawRect(0, y, getWidth(), y);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (owner.vmProtected) {
            if (owner.getCircuitBasis() == this) {
                return;
            }
        }

        if (!graphikLocked && g != null) {
            Graphics2D g2 = (Graphics2D) g;


            /*VMObject vm = this;
            for (int i = 0; i < vm.getElementCount(); i++)
            {
                Element element = vm.getElement(i);

                if (element.getInternName().indexOf("#MCU-FLOWCHART-START#") > -1)
                {
                    int mx = element.getWidth() / 2;

                    Stroke standard = new BasicStroke(1);
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.setStroke(standard);

                    g2.drawLine(element.getX() + mx, element.getY() + element.getHeight(), element.getX() + mx, element.getY() + getHeight());
                //g.drawLine(element.getX(), element.getY() + element.getHeight(), element.getX(), element.getY() + getHeight());
                //g.drawLine(element.getX() + element.getWidth(), element.getY() + element.getHeight(), element.getX() + element.getWidth(), element.getY() + getHeight());
                }
            }*/
            if (rasterOn) {
                paintGrid(g);
            }

            // als Standardfont!
            g.setFont(stdFont);

            if (status != null) {
                status.draw(g);
            }

            for (int i = 0; i < drahtLst.size(); i++) {
                Draht draht = (Draht) drahtLst.get(i);
                if (draht != null) {
                    draht.draw(g);
                }
            }

            if (isBasisResizePinVisible) {
                g.setColor(Color.BLACK);
                g.fillRect(getWidth() - 9, getHeight() - 9, 9, 9);
                g.setColor(new Color(180, 180, 180));
                g.drawRect(getWidth() - 9, getHeight() - 9, 9, 9);
            }

            /*for (int i=0;i<getElementCount();i++)
        {
        Element element = getElement(i);
        element.refreshSubElement(g2);
        } */
        }

    }

    public void properyItemFocusGained() {
        //System.out.println("XXXXXXXXXX");
    }

    public void init(String[] args) {
    }

    public void beforeInit(String[] args) {
    }

    public String getBinDir() {
        return "";
    }

    public void elementActionPerformed(ElementActionEvent evt) {

    }

    public void destElementCalled() {
    }

    public String jGetVMFilename() {
        return "";
    }

    public void xonMousePressedOnIdle(MouseEvent e) {
    }

    public void xonClock() {
    }

    public void processMethod(VSFlowInfo flowInfo) {
    }

    public void returnFromMethod(Object result) {
    }

    public JPin getNearstPin(int x, int y, int d) {
        Element element;

        int dx, dy;
        int vectorDistance = 0;
        int minDitance = 99999999;

        JPin minPin = null;
        JPin pin;

        for (int i = 0; i < getElementCount(); i++) {
            element = getElement(i);

            for (int j = 0; j < element.getPinCount(); j++) {
                pin = element.getPin(j);
                if (pin != null) {
                    Point mp = getMittenPunktOfPin(pin);
                    dx = Math.abs(x - mp.x);
                    dy = Math.abs(y - mp.y);

                    vectorDistance = (int) Math.sqrt((dx * dx) + (dy * dy));
                    double w2 = element.getWidth() / 2;
                    int radius = (int) Math.sqrt(w2 * w2 + w2 * w2);
                    radius = 0;

                    if (vectorDistance < (d + radius) && vectorDistance < minDitance) {
                        minDitance = vectorDistance;
                        minPin = pin;
                    }
                }

            }
        }

        if (minPin != null) {
            return minPin;
        }
        return null;
    }

    public Element getNearstElementInMouse(int x, int y, int d) {
        Element element;

        int dx, dy;
        int vectorDistance = 0;
        int minDitance = 99999999;

        Element minElement = null;

        for (int i = 0; i < getElementCount(); i++) {
            element = getElement(i);
            Point mp = element.getMittelpunkt();

            dx = Math.abs(x - mp.x);
            dy = Math.abs(y - mp.y);

            vectorDistance = (int) Math.sqrt((dx * dx) + (dy * dy));
            double w2 = element.getWidth() / 2;
            int radius = (int) Math.sqrt(w2 * w2 + w2 * w2);

            if (vectorDistance < (d + radius) && vectorDistance < minDitance) {
                minDitance = vectorDistance;
                minElement = element;
            }
        }

        if (minElement != null) {
            return minElement;
        }
        return null;
    }

    public Element getNearstElementInMouseExcludeElement(int x, int y, int d, Element elementToExclude) {
        Element element;

        int dx, dy;
        int vectorDistance = 0;
        int minDitance = 99999999;

        Element minElement = null;

        for (int i = 0; i < getElementCount(); i++) {
            element = getElement(i);
            if (element != elementToExclude) {
                Point mp = element.getMittelpunkt();

                dx = Math.abs(x - mp.x);
                dy = Math.abs(y - mp.y);

                vectorDistance = (int) Math.sqrt((dx * dx) + (dy * dy));
                double w2 = element.getWidth() / 2;
                int radius = (int) Math.sqrt(w2 * w2 + w2 * w2);

                if (vectorDistance < (d + radius) && vectorDistance < minDitance) {
                    minDitance = vectorDistance;
                    minElement = element;
                }
            }
        }

        if (minElement != null) {
            return minElement;
        }
        return null;
    }

    public Point getMittenPunktOfPin(JPin pin) {
        if (pin == null) {
            return null;
        }
        Element el = pin.element;

        int x = el.getX() + pin.getLocation().x;
        int y = el.getY() + pin.getLocation().y;

        int mx = x + (pin.getWidth() / 2);
        int my = y + (pin.getHeight() / 2);

        return new Point(mx, my);
    }

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {

        if (pageIndex > 0) {
            return (NO_SUCH_PAGE);
        } else {
            Image image = getVMImage();

            Graphics2D g2 = (Graphics2D) graphics;
            int x = (int) pageFormat.getImageableX();
            int y = (int) pageFormat.getImageableY();
            int w = (int) pageFormat.getImageableWidth();
            int h = (int) pageFormat.getImageableHeight();

            int h2 = h / 2;
            double asp = ((double) image.getWidth(this)) / ((double) image.getHeight(this));
            h = (int) (((double) w) / asp);

            int ih2 = (int) h / 2;

            int hints = BufferedImage.SCALE_SMOOTH;

            Image scaledImage = image.getScaledInstance(w, h, hints);

            y = h2 - ih2;
            g2.drawImage(scaledImage, x, y, w, h, this);

            //g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            // Turn off double buffering
            //componentToBePrinted.paint(g2d);
            // Turn double buffering back on*/
            return (PAGE_EXISTS);
        }

    }

    public void setProperexterntyEditor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
