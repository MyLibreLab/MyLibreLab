/* 
 * Element.java
 *
 * Created on 7. Maerz 2005, 15:39
 */
package VisualLogic;

import VisualLogic.PathPoint;
import BasisStatus.StatusEditPath;
import Peditor.BasisProperty;
import Peditor.PropertyEditor;
import SimpleFileSystem.FileSystemOutput;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import java.util.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;

import VisualLogic.variables.*;
import javax.swing.AbstractButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 *
 * @author Carmelo Salafia
 */
public class Element extends Shape implements MouseListener, MouseMotionListener, ExternalIF, PinIF, Comparable {

    // Sichtbarkeit der Pins
    public boolean highlighted = false;
    private boolean leftPinsVisible = true;
    private boolean changed = false;
    public SelectionPane layeredPane = new SelectionPane(this);
    private boolean topPinsVisible = true;
    private boolean rightPinsVisible = true;
    private boolean bottomPinsVisible = true;
    private Stroke standardStroke = new BasicStroke(1);
    private Stroke strokeDick = new BasicStroke(2);
    public ElementIF classRef;
    public ExternalIF circuitElement = null;
    public ExternalIF panelElement = null;
    private boolean elementFixed = false;
    private boolean simplePaintModus = false;
    public ElementIF mouseEventsTo = null;
    public String docPath = "";
    public boolean isAlreadyCompiled = false; // zum Compilieren von SPS Code!
    public boolean closePath = false;
    public ArrayList<PathPoint> points = new ArrayList<>();
    public int pointSize = 10;
    public int elementRefreshDifferenz = 0;
    // bis zu 10 Tags können hier von FrontElement zu CircuitElement als sog. Parameter
    // ausgetauscht werden
    public Object[] tags = new Object[10];

    private boolean docPathIsAlreadySet = false;

    @Override
    public void jSetTag(int index, Object tag) {
        if (index >= 0 && index < 10) {
            Element elA = (Element) getCircuitElement();
            Element elB = (Element) getPanelElement();

            if (elA != null) {
                elA.tags[index] = tag;
            }
            if (elB != null) {
                elB.tags[index] = tag;
            }

            this.tags[index] = tag;
        }
    }

    public void jAddPathPoint(String commando, Point p, Point p1, Point p2) {
        PathPoint path = new PathPoint();
        path.commando = commando;
        path.p = p;
        path.p1 = p1;
        path.p2 = p2;
        points.add(path);
    }

    @Override
    public String jMapFile(String filename) {
        return Tools.mapFile(filename);
    }

    @Override
    public boolean jIsPathClosed() {
        return closePath;
    }

    @Override
    public void jSetClosePath(boolean value) {
        closePath = value;
    }

    public GeneralPath parsepath(double zoomX, double zoomY) {
        PathPoint p;
        String cmd;

        GeneralPath path = new GeneralPath();

        if (points.size() > 0) {
            p = points.get(0);

            if (!p.commando.equalsIgnoreCase("MOVETO")) {
                int x = (int) (p.p.x * zoomX);
                int y = (int) (p.p.y * zoomY);
                path.moveTo(x, y);
            }
        }

        for (int i = 0; i < points.size(); i++) {
            p = points.get(i);
            cmd = p.commando;
            int x = (int) (p.p.x * zoomX);
            int y = (int) (p.p.y * zoomY);

            if (cmd.equalsIgnoreCase("MOVETO")) {
                path.moveTo(x, y);
            } else if (cmd.equalsIgnoreCase("LINETO")) {
                path.lineTo(x, y);
            } else if (cmd.equalsIgnoreCase("CURVETO")) {
                if (i > 0) {
                    PathPoint p_1 = points.get(i - 1);

                    if (p_1.commando.equalsIgnoreCase("CURVETO")) {
                        path.curveTo((int) (p_1.p2.x * zoomX), (int) (p_1.p2.y * zoomY), (int) (p.p1.x * zoomX), (int) (p.p1.y * zoomY), x, y);
                    } else {
                        path.curveTo((int) (p_1.p.x * zoomX), (int) (p_1.p.y * zoomY), (int) (p.p1.x * zoomX), (int) (p.p1.y * zoomY), x, y);
                    }
                }
            } else if (cmd.equalsIgnoreCase("QUADTO")) {
                path.quadTo(x, y, (int) (p.p1.x * zoomX), (int) (p.p1.y * zoomY));
            }
        }

        return path;
    }
    public double zoomX = 1.0;
    public double zoomY = 1.0;

    public GeneralPath jParsePath() {
        GeneralPath path = parsepath(1.0, 1.0);

        if (jIsPathEditing()) {
            Rectangle r2 = path.getBounds();

            if (points.size() == 0) {
                zoomX = 1;
                zoomY = 1;
            }

            return parsepath(zoomX, zoomY);
        } else {
            Rectangle r2 = path.getBounds();
            zoomX = ((double) jGetWidth()) / (double) r2.width;
            zoomY = ((double) jGetHeight()) / (double) r2.height;

            return parsepath(zoomX, zoomY);
        }
    }

    public void jSetRepaintDifference(int diff) {
        elementRefreshDifferenz = diff;
    }

    public MyOpenLabDriverIF jOpenDriver(String driverName, ArrayList args) {
        return Tools.driverManager.openDriver(this, driverName, args);
    }

    public void jCloseDriver(String driverName) {
        Tools.driverManager.closeDriver(this, driverName);
    }

    public Object jGetTag(int index) {
        if (index >= 0 && index < 10) {
            return tags[index];
        } else {
            return null;
        }
    }

    public Point getMittelpunkt() {
        int mx = getX() + (getWidth() / 2);
        int my = getY() + (getHeight() / 2);
        return new Point(mx, my);
    }

    public void setSimplePaintModus(boolean value) {
        simplePaintModus = value;

        /*if (value)
         {
         for (int i=0;i<subElemente.size();i++)
         {
         Object o = subElemente.get(i);
         if ( o instanceof JPanel)
         {
         JPanel panel = (JPanel)o;
         panel.setVisible(false);
         }
         }
         }else
         {
         for (int i=0;i<subElemente.size();i++)
         {
         Object o = subElemente.get(i);
         if ( o instanceof JPanel)
         {
         JPanel panel = (JPanel)o;
         panel.setVisible(true);
         }
         }
         }
         owner.repaint();*/
    }
    public ArrayList notifyWhenDestCalledList = new ArrayList(); // List of ExternalIF!
    public ArrayList menuItems = new ArrayList();
    public int elementIndex = 0;

    public void setSubPanelsVisible(boolean value) {
        JPanel panel;
        for (int i = 0; i < subElemente.size(); i++) {
            panel = (JPanel) subElemente.get(i);
            panel.setVisible(value);
        }
    }

    public void setVisible(boolean value) {
        if (value) {
            setCaptionVisible(isCaptionVisible());
        } else {
            setCaptionVisible(false);
        }
        setSubPanelsVisible(value);

        super.setVisible(value);
    }
    private String nameLocalized;
    private String xname = "";

    public void setInternName(String value) {
        this.xname = value;
    }

    public String getInternName() {
        return xname;
    }

    public void jNotifyMeForClock() {
        owner.clockList.add(this);
    }

    public String getNameLocalized() {
        return nameLocalized;
    }
    public boolean isLoading = false;
    public double fx1 = 0;
    public double fy1 = 0;
    public double fx = 0;
    public double fy = 0;
    public ArrayList propertyList = new ArrayList();
    /*public int getOriginalWidth()
     {
     return originalWidth;
     }
     public int getOriginalHeight()
     {
     return originalHeight;
     }*/
    public Basis elementBasis = null;
    public PropertyEditor propertyEditor = null;

    public Basis getElementBasis() {
        return elementBasis;
    }
    private Object tag = "";

    @Override
    public Object jGetTag() {
        return tag;
    }

    @Override
    public void jAddJButtonToButtonGroup(AbstractButton button, int group) {
        owner.owner.addJButtonToButtonGroup(button, group);
    }

    @Override
    public void jSetTag(Object tag) {
        this.tag = tag;
    }

    @Override
    public void jConsolePrintln(String value) {
        if (owner.owner.frameCircuit != null) {
            owner.owner.console.setVisible(true);
            owner.owner.console.addMessageToConsole(value + "\n");
        }
    }

    public void jConsolePrint(String value) {
        if (owner.owner.frameCircuit != null) {
            owner.owner.console.setVisible(true);
            owner.owner.console.addMessageToConsole(value);
        }
    }

    @Override
    public int jGetID() {
        return getID();
    }

    @Override
    public void jGiveMouseEventsTo(ExternalIF element) {
        Element tmp = (Element) element;

        mouseEventsTo = tmp.classRef;
    }

    @Override
    public VSBasisIF jGetBasis() {
        return owner.owner;
    }

    @Override
    public Basis jGetElementBasis() {
        return elementBasis;
    }

    @Override
    public String toString() {
        return getCaption() + " [" + getNameLocalized() + "]";
    }

    public void setNameLocalized(String nameLocalized) {
        this.nameLocalized = nameLocalized;
    }
    public String infoProgrammer;
    public String infoCopyrights;
    public String infoOther;
    public boolean loadedFromAblageFlag = false;
    public int oldID = -1;
    public int circuitElementID = -1;
    public int panelElementID = -1;
    private Font stdFont = new Font("Arial", 0, 10);
    private Font pinFont = new Font("Arial", 0, 8);
    public ArrayList datenTypListe = new ArrayList();
    public ArrayList changedList = new ArrayList();
    public boolean locked = false;
    private Graphics2D elementG = null;
    private boolean oki = false;
    public int oldX, oldY;
    public int oldDX, oldDY;
    private int oldPositionX, oldPositionY;
    private final int maxPins = 100;
    private final int pinWidth = 8;
    private final int pinHeight = 10;
    private ArrayList pinsLstTop;
    private ArrayList pinsLstRight;
    private ArrayList pinsLstBottom;
    private ArrayList pinsLstLeft;
    private int pinsTop;                    // Anzhal der oberen Pins
    private int pinsBottom;                 // Anzhal der unteren  Pins
    private int pinsLeft;                   // Anzhal der Pins Links
    private int pinsRight;                  // Anzhal der Pins Rechts
    private final Color pinColor = Color.green;
    private boolean alreadyInitialized = false;
    private boolean resizeSynchron = false;

    public boolean isResizeSynchron() {
        return resizeSynchron;
    }
    public static final int MODE_NORMAL = 0;
    public static final int MODE_XOR = 1;
    private int mode = MODE_NORMAL;
    private double aspect = 1.0f;

    public double getAspect() {
        return aspect;
    }

    public String jGetProjectPath() {
        String fn = owner.owner.fileName;
        return fn;
    }

    public String jGetProjectPathFromProject() {
        String fn = owner.owner.projectPath;
        return fn;
    }

    public JPanel getFrontPanel() {
        return this;
    }
    public SubDialog subDialog = null;

    public void jCloseFrontPanel() {
        if (subDialog != null) {
            subDialog.dispose();
            subDialog = null;
        }
    }

    public void addPublishingFiles(ArrayList list) {
        if (elementBasis != null) {
            // 1. das Element selbst
            list.add(getInternName() + "&" + mainPath);

            // 2. die VM-Datei der Sub-VM
            if (classRef != null) {
                try {
                    list.add(getInternName() + "&" + classRef.jGetVMFilename());
                } catch (java.lang.AbstractMethodError e) {
                } catch (Exception ex) {
                }
            }

            // 3. alle Sub Elements
            elementBasis.addPublishingFiles(list);
        } else {
            list.add(getInternName() + "&" + mainPath);
        }
    }

    public void jOpenVM(String filename) {
        if (owner != null && owner.owner != null && elementBasis != null) {
            owner.owner.vsShow();
            //owner.owner.frameCircuit.addBasisToVMPanel(filename);
            //owner.owner.frameCircuit.add .getFrameMain().openVLogicFileAsFrontPanel(filename);
        }
    }

    public void jShowFrontPanel(boolean modal) {

        if (subDialog == null) {
            elementBasis.getFrontBasis().isBasisResizePinVisible = false;

            /* elementBasis.getCircuitBasis().setDraehteInRunMode();
             elementBasis.getFrontBasis().setDraehteInRunMode();
             elementBasis.getCircuitBasis().initAllOutputPins();
             elementBasis.getFrontBasis().initAllOutputPins();
             elementBasis.getCircuitBasis().initAllInputPins();
             elementBasis.getFrontBasis().initAllInputPins();
             elementBasis.getCircuitBasis().start();
             elementBasis.getFrontBasis().start();        */
            elementBasis.getCircuitBasis().setOpaque(true);
            elementBasis.getFrontBasis().setOpaque(true);

            //elementBasis.getFrontBasis().setRasterOn(false);
            //JDialog.setDefaultLookAndFeelDecorated(true);
            subDialog = new SubDialog(this, null, modal, elementBasis);
            subDialog.setTitle(elementBasis.caption);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            subDialog.setLocation(screenSize.width / 2 - subDialog.getWidth() / 2, screenSize.height / 2 - subDialog.getHeight() / 2);

            subDialog.setVisible(true);

            elementBasis.getFrontBasis().sortSubPanels();
            elementBasis.getCircuitBasis().adjustAllElemnts();
            elementBasis.getFrontBasis().adjustAllElemnts();
            elementBasis.getCircuitBasis().checkPins();
        } else {
            subDialog.toFront();
        }
    }

    public void jNotifyWhenDestCalled(int pinIndex, ExternalIF element) {
        if (pinIndex < getPinCount()) {
            JPin pin = getPin(pinIndex);
            if (pin != null) {
                Draht draht = pin.draht;
                if (draht != null) {

                    Element destElement = (Element) owner.getObjectWithID(draht.destElementID);
                    if (destElement != null) {
                        destElement.notifyWhenDestCalledList.add(element);
                    }

                }
            }
        }

    }

    public void notifyPinAfter(int pinIndex) {
        if (pinIndex < getPinCount()) {
            JPin pin = getPin(pinIndex);
            if (pin != null) {
                Draht draht = pin.draht;
                if (draht != null) {
                    if (owner.owner.ownerBasis != null) {
                        Basis b = owner.owner.ownerBasis;

                        b.getCircuitBasis().processList.add(0, draht);
                    } else {
                        owner.processList.add(0, draht);
                    }
                }
            }
        }

    }

    public void notifyPin(int pinIndex) {
        //System.out.println(""+jGetCaption());
        if (pinIndex < getPinCount()) {
            JPin pin = getPin(pinIndex);
            if (pin != null) {
                Draht draht = pin.draht;
                if (draht != null) {

                    if (owner.owner.ownerBasis != null) {
                        Basis b = owner.owner.ownerBasis;
                        b.getCircuitBasis().processList.add(draht);
                    } else {
                        owner.processList.add(draht);
                    }

                }
            }
        }
    }

    public void addToProcesslist(VSObject out) {
        notifyPin(out.getPin());
    }

    public JFrame jGetFrame() {
        return owner.owner.frm;
    }

    public void setXorMode() {
        mode = MODE_XOR;
        repaint();
    }

    public void setNormalMode() {
        mode = MODE_NORMAL;
        repaint();
    }

    public int getMode() {
        return mode;
    }
    public String docFileName = "";
    public int destDrahtID = -1;
    public String mainPath;
    public String binPath;
    public String classPath;
    public String className;
    public String elementPath;
    public boolean borderVisibility = true;
    private boolean innerBorderVisibility = true;

    public boolean getInnerBorderVisibility() {
        return innerBorderVisibility;
    }

    public void setInnerBorderVisibility(boolean value) {
        if (value != innerBorderVisibility) {
            innerBorderVisibility = value;
        }
    }
    public boolean oldBorderVisibility = true;

    public void jSetBorderVisibility(boolean visible) {
        setBorderVisibility(visible);
    }

    public void setBorderVisibility(boolean visible) {
        if (borderVisibility != visible) {
            borderVisibility = visible;
            repaint();
        }
    }
    public VMObject getElementOwner(){
        return this.owner;
    }
    private String[] args;
    public VMObject owner;
    public String definitionPath = "";
    public DFProperties definition_def = null;
    private String description = "";

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        description = value;
    }
    private int yDescriptionDistance = 0;

    public ArrayList<PathPoint> jGetPointList() {
        return points;
    }

    public String replaceMe(String str) {
        StringBuffer s = new StringBuffer(str);
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == '\\') {
                s.deleteCharAt(i);
                s.insert(i, "/");
            }
        }

        return "" + s.toString();

    }

    public String jIfPathNotFoundThenSearch(String path) {
        String pfadX = Tools.mapFile(path);
        File fileA = new File(pfadX);
        if (!fileA.exists()) {

            return owner.owner.frameCircuit.searchElement(new File(pfadX));
        }

        return "";
    }

    private String getKorrectClassPaths(String elementPath, String mainPath, String classPath) {
        String result = "";

        /*String[] paths=classPath.split(";");
         for (int i=0;i<paths.length;i++)
         {            
         String str=paths[i];
         result+="\""+elementPath+mainPath+"/"+str+"\";";
         }*/
        result += elementPath + mainPath + "/" + classPath;

        return result;
    }

    public void removeReference() {
        classRef = null;
    }
    
    private String korrigiereFileSeparator(String path) {
        path = path.replace("\\", File.separator);
        path = path.replace("/", File.separator);
        
        return path;
    }

    /**
     * Creates a new instance of Element
     */
    public Element(int id, VMObject owner, String elementPath, String mainPath, String binPath, String className, String definitionPath, String[] args) throws Exception {
        super(id, owner);
        this.owner = owner;
        this.args = args;

        // mainPath=replaceMe(mainPath);
        // definitionPath=replaceMe(definitionPath);
        String pathX = elementPath + mainPath;

        pathX = korrigiereFileSeparator(pathX);
        

        String result = jIfPathNotFoundThenSearch(pathX);
        if (result.length() > 0) {
            String f1 = new File(elementPath).getAbsolutePath();
            String f2 = new File(result).getAbsolutePath();

            String str = f2.substring(f1.length());

            str = replaceMe(str);

            mainPath = str;
            definitionPath = str;
        }

        if (owner == owner.owner.getCircuitBasis()) {
            borderVisibility = true;
        } else {
            borderVisibility = false;
        }

        this.setLayout(null);

        pinsLstTop = new ArrayList();
        pinsLstRight = new ArrayList();
        pinsLstBottom = new ArrayList();
        pinsLstLeft = new ArrayList();

        setBackground(new Color(10, 200, 50, 0));
        setOpaque(false);

        this.elementPath = elementPath;
        this.classPath = mainPath + "/" + binPath;

        this.mainPath = mainPath;
        this.binPath = binPath;
        this.className = className;

        loadStandardValues();

        Loader loader = new Loader();

        String path = mainPath + "/" + binPath;
        path = korrigiereFileSeparator(path);

        if (className.trim().equalsIgnoreCase("")) {
            File file = new File(elementPath + path);
            className = file.getName();
            className = className.substring(0, className.length() - 4);
            this.className = className;
        }

        if (definitionPath.length() == 0) {
            this.definitionPath = mainPath;

            if (args != null && args.length > 1) {
                if (args.length == 6 && args[5].equalsIgnoreCase("LOADER")) {
                    String f = new File(args[4]).getPath();
                    this.definitionPath = f;

                } else {
                    String f = new File(args[0]).getParent();
                    this.definitionPath = f;
                }

                if (this.definitionPath == null) {
                    this.definitionPath = "";
                }
            }
        } else {
            this.definitionPath = definitionPath;
        }
        
        File fileX = new File(korrigiereFileSeparator(elementPath + this.definitionPath));
        definition_def = Tools.getProertiesFromDefinitionFile(fileX);

        String myClassPath = "";

        if (definition_def.redirect.length() > 0) {
            myClassPath = "/" + definition_def.redirect;
            classPath = myClassPath + "/" + binPath;
        } else {
            myClassPath = mainPath;
        }

        myClassPath=korrigiereFileSeparator(myClassPath);
        

        if (className.equalsIgnoreCase("DUMMY")) {
            classRef = null; // also Dummy!
        } else {
            try {
                if (!definition_def.classPath.equalsIgnoreCase("")) {
                    URL url1 = new File(elementPath).toURI().toURL();
                    URL url2 = new File(elementPath + myClassPath + "/bin/").toURI().toURL();

                    String classPaths = getKorrectClassPaths(elementPath, mainPath, definition_def.classPath);
                    URL url3 = new File(classPaths).toURI().toURL();

                    URL url4 = null;

                    if (definition_def.classPath2.trim().length() > 0) {
                        classPaths = getKorrectClassPaths(elementPath, mainPath, definition_def.classPath2);
                        url4 = new File(classPaths).toURI().toURL();
                    }

                    classRef = (ElementIF) loader.ladeClasseDriver(new URL[]{url1, url2, url3, url4}, this.className);
                } else {
                    String pfad = Tools.mapFile(elementPath + myClassPath + "/bin");
                    //File file = new File(pfad);
                    classRef = (ElementIF) loader.ladeClasse(elementPath, pfad, this.className);
                }

                if (classRef != null) {
                    classRef.beforeInit(args);
                }
                if (classRef != null) {
                    classRef.xsetExternalIF(this);
                }

            } catch (Exception ex) {
                owner.owner.showErrorMessage("Error loading Element!" + this.className + " ex=" + ex.toString());
                classRef = null; // also Dummy!
            } catch (java.lang.AbstractMethodError ex) {
                owner.owner.showErrorMessage("Error loading Element!" + this.className + " ex=" + ex.toString());
                classRef = null; // also Dummy!
            }
        }

        if (!alreadyInitialized) {
            initPins(true); // Initialisiere alle Pins!!!
        }

        if (classRef != null) {
            classRef.xonStop();
        }

        if (!docPathIsAlreadySet) {
            //String s = elementPath + path;
            docPath = elementPath + path;
            if (definition_def.vm.length() > 0) {
                docPath = elementPath + this.definitionPath + "/";
            }
        }

        /*URL url = new File(s).toURL();
         s = url.getFile();
        
         if (!docPathIsAlreadySet){

         String strLocale = Locale.getDefault().toString();

         docFileName = s.substring(0, s.lastIndexOf("/")) + "/doc.html";

         if (strLocale.equalsIgnoreCase("de_DE"))
         {
         docFileName = s.substring(0, s.lastIndexOf("/")) + "/doc.html";
         }
         else if (strLocale.equalsIgnoreCase("en_US"))
         {
         docFileName = s.substring(0, s.lastIndexOf("/")) + "/doc_en.html";
         }
         else if (strLocale.equalsIgnoreCase("es_ES"))
         {
         docFileName = s.substring(0, s.lastIndexOf("/")) + "/doc_es.html";
         }
         }*/
        //File fileX= new File(elementPath+mainPath);
        //DFProperties definition_def =Tools.getProertiesFromDefinitionFile(fileX);
        setNameLocalized(definition_def.captionInternationalized);

        isLoading = true;
        loadProperties();
        isLoading = false;

        /*enableEvents(AWTEvent.FOCUS_EVENT_MASK);        // catch Focus-Events
         enableEvents(AWTEvent.KEY_EVENT_MASK);          // catch KeyEvents
         enableEvents(AWTEvent.MOUSE_EVENT_MASK);        // catch MouseEvents
         enableEvents(AWTEvent.COMPONENT_EVENT_MASK);    // catch ComponentEvents */
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        this.add(layeredPane, 0);
        layeredPane.setVisible(false);
        setCaptionVisible(isCaptionVisible());
        owner.add(lblName);
    }

    public void jLoadProperties() {
        //owner.owner.propertyEditor=null;
        isLoading = true;
        loadProperties();
        isLoading = false;
        //   processPropertyEditor();
    }

    public void loadProperties() {
        try {
            if (classRef != null) {
                classRef.setPropertyEditor();
            }
        } catch (Exception ex) {
            //System.out.println("loadProperties() "+ex);
        }
    }

    public void resetValuesPanelElement() {
        Element el = (Element) getPanelElement();

        if (el.classRef != null) {
            el.classRef.resetValues();
        }
    }

    public void processPanelElement() {
        Element el = (Element) getPanelElement();

        if (el.classRef != null) {
            el.classRef.xonProcess();
        }
    }

    public void jProcessPanel(int pinIndex, double value, Object obj) {
        try {
            ((PanelIF) classRef).processPanel(pinIndex, value, obj);
        } catch (Exception ex) {
            owner.owner.showErrorMessage("PanelIF existiert nicht : " + ex);
        }
    }

    public void moveElement(int srcIndex, int dstIndex) {
        ArrayList elemente = getVMObject().getElementList();
        Element temp = getVMObject().getElement(srcIndex);
        elemente.remove(temp);
        elemente.add(dstIndex, temp);
        getVMObject().repaint();
    }

    public void inDenVordergrund() {
       try{
        getVMObject().remove(this);
        getVMObject().add(this, 0);
        owner.sortSubPanels();
        repaint();
       }catch (IllegalArgumentException e){
           System.out.println("Error VisualLogic.Element.inDenVordergrund(Element.java:906) "+e.getMessage());   
       }catch (Exception e){
           System.out.println("Error VisualLogic.Element.inDenVordergrund "+e.getMessage());
       }
    }

    public void inDenHintergrund() {
        getVMObject().remove(this);
        int c = getVMObject().getComponentCount();
        getVMObject().add(this, c);
        owner.sortSubPanels();
        repaint();
    }

    public int getIndexOfComponent(VMObject vm, Component comp) {
        Component c = null;
        for (int i = 0; i < vm.getComponentCount(); i++) {
            c = vm.getComponent(i);

            if (c.equals(comp)) {
                return i;
            }
        }
        return -1;
    }

    public void eineEbeneNachVorne() {
        int index = getIndexOfComponent(getVMObject(), this);
        if (index > 0) {
            Element element = owner.getFirstElement(this);
            getVMObject().remove(this);
            index = owner.getComponentZOrder(element);
            getVMObject().add(this, index - 1);
            owner.sortSubPanels();
            repaint();
        }
    }

    public void eineEbeneNachHinten() {
        int index = getIndexOfComponent(getVMObject(), this);

        if (index < getVMObject().getComponentCount()) {

            Element element = owner.getLastElement(this);
            getVMObject().remove(this);
            index = owner.getComponentZOrder(element);

            getVMObject().add(this, index + 1);
            owner.sortSubPanels();
            repaint();
        }
    }

    private void setPanelElementWhereOldID(Element frontElement) {
        VMObject circuitBasis = owner.owner.getCircuitBasis();

        for (int i = 0; i < circuitBasis.getElementCount(); i++) {
            Element circuitElement = circuitBasis.getElement(i);

            if (circuitElement.loadedFromAblageFlag && circuitElement.panelElementID == frontElement.oldID) {
                circuitElement.loadedFromAblageFlag = false;
                frontElement.loadedFromAblageFlag = false;
                circuitElement.panelElementID = frontElement.getID();
                frontElement.circuitElementID = circuitElement.getID();
                break;
            }
        }
    }

    public void loadFromStream(FileInputStream fis, boolean fromAblage) {
        try {
            DataInputStream stream = new DataInputStream(fis);

            circuitElementID = stream.readInt();
            panelElementID = stream.readInt();

            boolean topPinsVisible = stream.readBoolean();
            boolean rightPinsVisible = stream.readBoolean();
            boolean bottomPinsVisible = stream.readBoolean();
            boolean leftPinsVisible = stream.readBoolean();

            int topPins = stream.readInt();  //getTopPins()
            int rightPins = stream.readInt();  //getRightPins()
            int bottomPins = stream.readInt();  //getBottomPins()
            int leftPins = stream.readInt();  //getLeftPins()

            int left = stream.readInt();
            int top = stream.readInt();
            int width = stream.readInt();
            int height = stream.readInt();

            setCaption(stream.readUTF());
            boolean vis = stream.readBoolean();
            setCaptionVisible(vis);

            double dblVersion = owner.getFileVersion();
            if (dblVersion >= 3.09) {
                boolean visWhenRun = stream.readBoolean();
                setVisibleWhenRun(visWhenRun);
            }

            String strName = stream.readUTF();
            setInternName(strName);
            String description = stream.readUTF();

            vis = stream.readBoolean();

            //setNameVisible(vis);
            int destDrahtID = stream.readInt();
            setVisible(stream.readBoolean());

            setLocation(left, top);
            setSize(width, height);

            layeredPane.setAlwaysOnTop(true);
            layeredPane.setVisible(false);

            if (classRef != null) {
                loadProperties();

                try {
                    classRef.xLoadFromStream(fis);

                    if (this.owner == owner.owner.getCircuitBasis()) {
                        if (panelElementID == -1 && circuitElementID != -1) {
                            classRef.xOnInit();
                            //classRef.loadFromStreamAfterXOnInit(fis);
                        }
                    }

                    if (this.owner == owner.owner.getFrontBasis()) {
                        if (fromAblage) {
                            setPanelElementWhereOldID(this);
                        }

                        VMObject frontBasis = owner.owner.getFrontBasis();
                        VMObject circuitBasis = owner.owner.getCircuitBasis();

                        if (circuitElementID != -1) {
                            circuitElement = (Element) circuitBasis.getObjectWithID(circuitElementID);

                            Element el = (Element) circuitElement;
                            if (el.panelElementID != -1) {
                                el.panelElement = (Element) frontBasis.getObjectWithID(el.panelElementID);
                            }
                        }

                        if (circuitElement != null) {
                            ((Element) circuitElement).classRef.xOnInit();
                        }
                        classRef.xOnInit();

                        if (circuitElement != null) {
                            ((Element) circuitElement).classRef.loadFromStreamAfterXOnInit(fis);
                        }
                        classRef.loadFromStreamAfterXOnInit(fis);

                        //owner.owner.verknuepfeElemente(fromAblage);
                        if (elementBasis != null) {
                            elementBasis.getCircuitBasis().processpropertyChangedToAllElements(null);
                            elementBasis.getFrontBasis().processpropertyChangedToAllElements(null);
                        }

                    }

                } catch (Exception ex) {
                    owner.owner.showErrorMessage("Error loading Component <" + strName + "> :" + ex);
                }
            } else {
                try {
                    jSetTopPinsVisible(topPinsVisible);
                    jSetRightPinsVisible(rightPinsVisible);
                    jSetBottomPinsVisible(bottomPinsVisible);
                    jSetLeftPinsVisible(leftPinsVisible);

                    jSetTopPins(topPins);
                    jSetRightPins(rightPins);
                    jSetBottomPins(bottomPins);
                    jSetLeftPins(leftPins);

                    setCaptionVisible(true);
                    setCaption("NOT FOUND!");

                    initPins(true);
                    alreadyInitialized = true;
                } catch (Exception ex) {
                }
            }

        } catch (Exception ex) {
            owner.owner.showErrorMessage("Error in Element.loadFromStream() :" + ex.toString());
        }
    }

    public void saveToStream(FileSystemOutput fsOut) {

        try {
            FileOutputStream fos = fsOut.addItem("Element");
            DataOutputStream dos = new DataOutputStream(fos);

            dos.writeUTF(classPath);
            dos.writeUTF(className);
            dos.writeInt(getID());                   // id
            dos.writeUTF(definitionPath);

            dos.writeUTF(mainPath);
            dos.writeUTF(binPath);

            dos.writeInt(getNameID());
            dos.writeInt(circuitElementID);
            dos.writeInt(panelElementID);

            dos.writeBoolean(topPinsVisible);
            dos.writeBoolean(rightPinsVisible);
            dos.writeBoolean(bottomPinsVisible);
            dos.writeBoolean(leftPinsVisible);

            dos.writeInt(getTopPins());
            dos.writeInt(getRightPins());
            dos.writeInt(getBottomPins());
            dos.writeInt(getLeftPins());

            dos.writeInt(getX());                    // Left
            dos.writeInt(getY());                    // Top
            dos.writeInt(getWidth());                // Width
            dos.writeInt(getHeight());               // Height
            dos.writeUTF(getCaption());              // Beschriftung
            dos.writeBoolean(isCaptionVisible());    // ShowBeschriftung
            dos.writeBoolean(isVisibleWhenRun());    // VisibleWhenRun

            String str = getInternName();
            dos.writeUTF(str);           // Name
            dos.writeUTF(getDescription());          // Description

            boolean vis = isCaptionVisible();
            dos.writeBoolean(vis);
            dos.writeInt(destDrahtID);
            dos.writeBoolean(isVisible());           // visible

            if (classRef != null) {
                try {
                    classRef.xSaveToStream(fos);

                    if (owner == owner.owner.getFrontBasis()) {
                        if (circuitElement != null) {
                            ((Element) circuitElement).classRef.saveToStreamAfterXOnInit(fos);
                        }

                        classRef.saveToStreamAfterXOnInit(fos);
                    }
                } catch (Exception ex) {
                    owner.owner.showErrorMessage("Error in Method Element.saveToStream() : " + ex.toString());
                }
            }
            fsOut.postItem();

        } catch (Exception ex) {
            owner.owner.showErrorMessage("Error in Element.saveToStream() :" + ex.toString());
        }

    }

    public void amRasterAusrichten() {
        Point p;
        p = owner.pointToRaster(this, getX(), getY());
        setLocation(p.x, p.y);

        p = owner.pointToRaster(this, getWidth(), getHeight());
        setSize(p.x, p.y);
    }

    public JPin getPin(int index) {
        JPin ret = null;
        try {
            int c = 0;
            for (int i = 0; i < getTopPins(); i++) {
                if (c == index) {
                    return (JPin) pinsLstTop.get(i);
                }
                c++;
            }
            if (ret == null) {
                for (int i = 0; i < getRightPins(); i++) {
                    if (c == index) {
                        return (JPin) pinsLstRight.get(i);
                    }
                    c++;
                }
            }
            if (ret == null) {
                for (int i = 0; i < getBottomPins(); i++) {
                    if (c == index) {
                        return (JPin) pinsLstBottom.get(i);
                    }
                    c++;
                }
            }
            if (ret == null) {
                for (int i = 0; i < getLeftPins(); i++) {
                    if (c == index) {
                        return (JPin) pinsLstLeft.get(i);
                    }
                    c++;
                }
            }

        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    /*public void setExternalInputPin(int index, VSBasisIF basis, VSObject in)
     {
     ExternalIF[] inputs=geInputPinList(basis);
     inputs[index].setPinOutputReference(0,in);
     }*/
    public ExternalIF getElementByName(VSBasisIF basis, String elementName) {

        VMObject vm = ((Basis) basis).getCircuitBasis();
        int c = 0;
        for (int i = 0; i < vm.getComponentCount(); i++) {
            Component comp = vm.getComponent(i);
            if (comp instanceof Element) {
                Element element = (Element) comp;
                String str = element.getInternName();
                if (str.equalsIgnoreCase(elementName)) {
                    return (ExternalIF) element;
                }
            }
        }

        return null;
    }

    @Override
    public ExternalIF[] getOutputPinList(VSBasisIF basis) {
        return owner.owner.getCircuitBasis().getElementList(basis, "#PINOUTPUT_INTERNAL#");
    }

    @Override
    public ExternalIF getElementByhName(VSBasisIF basis, String name) {
        return getElementByName(basis, name);
    }

    @Override
    public ExternalIF[] getInputPinList(VSBasisIF basis) {
        return owner.owner.getCircuitBasis().getElementList(basis, "#PININPUT_INTERNAL#");
    }

    public int getPinCount() {
        return getTopPins() + getRightPins() + getBottomPins() + getLeftPins();
    }

    public Point getPinPosition(int index) {
        JPin pin = getPin(index);
        if (pin != null) {
            Point p = new Point();
            p.x = getX() + pin.getX() + (pin.getWidth() / 2);
            p.y = getY() + pin.getY() + (pin.getHeight() / 2);

            return p;
        } else {
            return new Point(0, 0);
        }
    }

    // *********************** Begin ExternalIF ***********************
    @Override
    public java.awt.Rectangle jGetBounds() {
        Rectangle rec = new Rectangle();
        int pw = pinWidth;
        int ph = pinHeight;

        if (leftPinsVisible) {
            rec.x = pw;
        } else {
            rec.x = 0;
        }
        if (topPinsVisible) {
            rec.y = ph;
        } else {
            rec.y = 0;
        }

        if (rightPinsVisible) {
            rec.width = getRealWidth() - rec.x - pw;
        } else {
            rec.width = getRealWidth() - rec.x;
        }
        if (bottomPinsVisible) {
            rec.height = getRealHeight() - rec.y - ph;
        } else {
            rec.height = getRealHeight() - rec.y;
        }

        return rec;
    }

    @Override
    public VSBasisIF jCreateBasis() {
        Basis b = new Basis(owner.owner.getFrameMain(), owner.owner.getElementPath());

        this.elementBasis = b;
        b.fileName = owner.owner.fileName;

        if (owner.owner.ownerBasis != null) {
            b.ownerBasis = owner.owner.ownerBasis;
        } else {
            b.ownerBasis = owner.owner;
        }

        //System.out.println("" + b);
        b.ownerElement = this;
        b.canSaveForUndo = false;

        //  b.projectPath=ow  ????????????
        //Element el=(Element)getPanelElement();
        b.getFrontBasis().setRasterOn(false);
        if (circuitElement != null) {
            ((Element) circuitElement).elementBasis = b;
        }

        if (panelElement != null) {
            ((Element) panelElement).elementBasis = b;
        }

        b.setEditable(false);
        b.getFrontBasis().setModusNone(this);

        b.propertyEditor = owner.owner.propertyEditor;

        //b.getCircuitBasis().panel=new JPanel();
        //b.getFrontBasis().panel=el;
        b.getCircuitBasis().setLayout(null);
        b.getFrontBasis().setLayout(null);
        b.getCircuitBasis().setOpaque(false);
        b.getFrontBasis().setOpaque(false);
        b.getFrontBasis().isBasisResizePinVisible = false;

        b.getFrontBasis().setLocation(0, 0);
        b.getFrontBasis().setSize(100, 100);
        b.getFrontBasis().setBorderVisible(false);

        //b.getFrontBasis().setSize(el.getWidth(),el.getHeight());
        if (args != null && args.length >= 1) {
            String path = "/" + args[0];
            String s = path;

            String strLocale = Locale.getDefault().toString();

            docFileName = s.substring(0, s.lastIndexOf("/")) + "/doc.html";

            if (strLocale.equalsIgnoreCase("de_DE")) {
                docFileName = s.substring(0, s.lastIndexOf("/")) + "/doc.html";
            } else if (strLocale.equalsIgnoreCase("en_US")) {
                docFileName = s.substring(0, s.lastIndexOf("/")) + "/doc_en.html";
            } else if (strLocale.equalsIgnoreCase("es_ES")) {
                docFileName = s.substring(0, s.lastIndexOf("/")) + "/doc_es.html";
            }
        }

        return b;

    }

    @Override
    public void jInitBasis(VSBasisIF basis) {
        //Element el=(Element)getPanelElement();

        Element el = this;
        el.setLayout(null);
        el.add(((Basis) basis).getFrontBasis());
        el.layeredPane.setAlwaysOnTop(true);
        el.layeredPane.setVisible(true);
    }

    @Override
    public void jProcess() {
        if (classRef != null) {
            classRef.xonProcess();
        }
    }

    @Override
    public void jSetProperties() {
        if (elementBasis != null) {
            VMObject vm = null;
            if (this.owner == this.owner.owner.getCircuitBasis()) {
                vm = elementBasis.getCircuitBasis();
            } else {
                vm = elementBasis.getFrontBasis();
            }

            //jClearPE();
            propertyList.clear();
            isLoading = true;

            //propertyList.add(new BasisProperty(owner,));
            if (classRef != null) {
                classRef.setPropertyEditor();
            }

            for (int i = 0; i < vm.propertyList.size(); i++) {
                BasisProperty prop = (BasisProperty) vm.propertyList.get(i);

                Element theElement = vm.getElementWithID(prop.elementID);
                ElementProperty elProp = (ElementProperty) theElement.propertyList.get(prop.propertyIndex);

                jAddPEItem(elProp.label, elProp.referenz, elProp.min, elProp.max,elProp.editable);
            }
            //jAddPEItem("Modal",this, 0,0);
            isLoading = false;
        }
    }

    @Override
    public VSBasisIF jAssignBasis() {

        /* Element el=(Element)getCircuitElement();
         Basis b = el.elementBasis;
         b.getCircuitBasis().panel=el;
         b.getFrontBasis().panel=el;
         b.getCircuitBasis().setLayout(null);
         b.getFrontBasis().setLayout(null);
         this.add(b.getFrontBasis());
         return b;*/
        return null;
    }

    @Override
    public void jRefreshVM() {
        owner.reorderWireFrames();
    }

    public void refreshSubElement(Graphics g) {
        Rectangle clip = g.getClipBounds();
        int x = clip.x;
        int y = clip.y;
        int xx = x + clip.width;
        int yy = y + clip.height;

        //System.out.println(clip);
        boolean oki = false;
        for (int i = 0; i < subElemente.size(); i++) {
            Object o = subElemente.get(i);
            if (o instanceof JPanel) {
                JPanel panel = (JPanel) o;

                Point p = panel.getLocation();
                if (p.x >= x && p.y >= y && p.x + panel.getWidth() <= xx && p.y + panel.getHeight() <= yy) {
                    oki = true;
                }
            }
        }

        if (oki) {
            repaint();
            //owner.repaint(p.x,p.y, panel.getWidth(),panel.getHeight());
        }

    }

    @Override
    public void repaint() {

        if (lblName != null && owner != null) {
            Point p = lblName.getLocation();
            owner.repaint(p.x, p.y + 10, lblName.getWidth(), lblName.getHeight());
        }

        if (subElemente != null && owner != null) {
            // Repaint Subelements
            for (int i = 0; i < subElemente.size(); i++) {
                Object o = subElemente.get(i);
                if (o instanceof JPanel) {
                    JPanel panel = (JPanel) o;
                    Point p = panel.getLocation();
                    owner.repaint(p.x - 20, p.y - 20, panel.getWidth() + 20, panel.getHeight() + 20);
                }
            }
        }

        super.redimLabel();
        if (owner != null) {
            int ed = elementRefreshDifferenz;
            owner.repaint(getX() - ed, getY() - ed, getX() + getWidth() + 2 * ed, getX() + getHeight() + 2 * ed);
        } else {
            super.repaint();
        }

    }

    @Override
    public void jRepaint() {
        repaint();
    }

    @Override
    public void jSetInfo(String programmer, String copyrights, String other) {
        this.infoProgrammer = programmer;
        this.infoCopyrights = copyrights;
        this.infoOther = other;
    }

    @Override
    public void jStopVM() {
        getVMObject().stop();
    }

    @Override
    public Point jGetLocation() {
        return new Point(getX(), getY());
    }

    @Override
    public void jClearSubElements() {

        for (int i = 0; i < subElemente.size(); i++) {
            Object o = subElemente.get(i);
            if (o instanceof JPanel) {
                owner.remove((JPanel) o);
            }
        }
        subElemente.clear();
    }

    @Override
    public void jSetSubElementVisible(int index, boolean value) {
        SubElement sub = (SubElement) subElemente.get(index);
        sub.visible = value;
    }

    @Override
    public ElementIF jGetSubElement(int index) {
        return (ElementIF) subElemente.get(index);
    }

    @Override
    public int jGetSubElementCount() {
        return subElemente.size();
    }

    @Override
    public void jDeleteSubElement(int index) {
        subElemente.remove(index);
    }

    @Override
    public void jClearMenuItems() {
        menuItems.clear();
    }

    @Override
    public void jAddMenuItem(JMenuItem item) {
        menuItems.add(item);
    }

    public void jAddComponent(Component component) {
        this.add(component);

    }

    @Override
    public void jAddSubElement(ElementIF subElement) {
        SubElement subEl = new SubElement();
        subEl.classRef = subElement;
        subElemente.add(subEl);
    }

    @Override
    public void jAddSubElement2(JPanel panel) {
        subElemente.add(panel);
        owner.add(panel, 0);
    }

    public void adjustSubElements() {
        /*if (!( owner.getStatus() instanceof StatusRun))
         {
         JPanel panel;
         for (int i=0; i<subElemente.size();i++)
         {
         panel=(JPanel)subElemente.get(i);
         owner.remove(panel);
         }
         for (int i=0; i<subElemente.size();i++)
         {
         panel=(JPanel)subElemente.get(i);
         owner.add(panel,0);
         }
         }*/
    }

    @Override
    public String jGetProperty(String labelName) {
        Object o = getProperty(labelName).referenz;
        if (o != null) {
            return o.toString();
        } else {
            return "";
        }
    }

    public ElementProperty getProperty(String labelName) {
        for (int i = 0; i < propertyList.size(); i++) {
            ElementProperty prop = (ElementProperty) propertyList.get(i);

            if (labelName.equalsIgnoreCase(prop.label)) {
                return prop;
            }
        }
        return null;
    }

    public void processPropertyEditor() {
        jClearPE();
        
        propertyList.clear();
        jLoadProperties();
        
        if (owner.owner.frameCircuit != null) {
            //owner.owner.frameCircuit
        }

        if (owner.owner.propertyEditor != null) {
            owner.owner.propertyEditor.setElement(this);

            if (elementBasis != null) {

                VMObject vm = null;
                if (this.owner == this.owner.owner.getCircuitBasis()) {
                    vm = elementBasis.getCircuitBasis();
                } else {
                    vm = elementBasis.getFrontBasis();
                }

                if (vm != null) {
                    for (int i = 0; i < propertyList.size(); i++) {
                        ElementProperty prop = (ElementProperty) propertyList.get(i);

                        //System.out.println(""+prop.label+" XX");
                        jAddPEItem(prop.label, prop.referenz, prop.min, prop.max,prop.editable);
                    }

                    /*for (int i=0;i<vm.propertyList.size();i++)
                     {
                     BasisProperty prop= (BasisProperty)vm.propertyList.get(i);
                     Element theElement=vm.getElementWithID(prop.elementID);
                     if (theElement!=null)
                     {
                     ElementProperty elProp=(ElementProperty)theElement.propertyList.get(prop.propertyIndex);
                     jAddPEItem(elProp.label,elProp.referenz,elProp.min,elProp.max);
                     }
                     }*/
                }
            } else {
                for (int i = 0; i < propertyList.size(); i++) {
                    ElementProperty prop = (ElementProperty) propertyList.get(i);

                    jAddPEItem(prop.label, prop.referenz, prop.min, prop.max,prop.editable);
                }

            }
            owner.owner.propertyEditor.reorderItems();
        }
    }

    public void setCursor(int cursor) {
        owner.setCursor(Cursor.getPredefinedCursor(cursor));
    }

    public PropertyEditor getPE(){
        return owner.owner.propertyEditor;
    }
    public void jClearPE() {
        //propertyList.clear();
        if (owner.owner.propertyEditor != null) {
            owner.owner.propertyEditor.clear();
        }
    }

    public VSObject[] jGetProperties() {
        VSObject[] result = new VSObject[propertyList.size()];

        for (int i = 0; i < propertyList.size(); i++) {
            ElementProperty prop = (ElementProperty) propertyList.get(i);
            result[i] = (VSObject) prop.referenz;
        }

        return result;
    }

    public void jAddPEItem(String label, Object referenz, double min, double max) {
        if (isLoading == true) {
            propertyList.add(new ElementProperty(label, referenz, min, max));
        } else {
            owner.owner.propertyEditor.addItem(label, referenz, min, max, true);
        }
    }
    public void jAddPEItem(String label, Object referenz, double min, double max, boolean editable) {
        if (isLoading == true) {
            propertyList.add(new ElementProperty(label, referenz, min, max, editable));
        } else {
            owner.owner.propertyEditor.addItem(label, referenz, min, max, editable);
        }
    }

    public void jSetPEItemLocale(int index, String language, String translation) {

        ElementProperty prop = (ElementProperty) propertyList.get(index - 6);
        //Peditor.PropertyEditorItem item =owner.owner.propertyEditor.getItem(index);

        if (prop != null) {
            String strLocale = Locale.getDefault().toString();

            if (strLocale.equalsIgnoreCase(language)) {
                //ElementProperty prop = (ElementProperty)propertyList.get(0);
                prop.label = translation;
                //System.out.println("label="+item.label.getText());
                //item.label.setText(translation);
            }
        }
    }

    public void setAlwaysOnTop(boolean value) {
        layeredPane.setAlwaysOnTop(value);
    }

    public void setSelected(boolean value) {
        if (selected != value) {
            selected = value;

            layeredPane.setVisible(selected);
            layeredPane.setVisible(selected);

            if (owner.owner.debugMode == false) {
                if (getPanelElement() != null) {
                    Element el = (Element) getPanelElement();
                    if (el != null) {
                        el.setSelected(value);
                    }
                }
            }

            if (getCircuitElement() != null) {
                Element el = (Element) this.getCircuitElement();
                if (el != null) {
                    el.setSelected(value);
                }
            }

        }

        repaint();

    }

    @Override
    public ExternalIF getCircuitElement() {
        return circuitElement;
    }

    @Override
    public ExternalIF getPanelElement() {
        return panelElement;
    }

    @Override
    public ExternalIF setPanelElement(String classFilename) {
        /*VMObject frontBasis = panelElementgetFrontBasis();
         Element element = frontBasis.addElement(classPath,classFilename);
         element.circuitElement=this;
         this.panelElement=element;
         return element;*/
        return null;
    }

    @Override
    public void jNameVisible(boolean value) {
        setCaptionVisible(value);
    }

    @Override
    public int jIsPinIO(int pinIndex) {
        return getPin(pinIndex).pinIO;
    }

    @Override
    public VSObject jCreatePinDataType(int pinIndex) {
        JPin pin = getPin(pinIndex);
        VSObject tmp = VSDataType.createPinDataType(pin.dataType);
        return tmp;
    }

    @Override
    public int jGetAnzahlPinsLeft() {
        return getLeftPins();
    }

    @Override
    public int jGetAnzahlPinsRight() {
        return getRightPins();
    }

    /*
     * zeigt eine Nachricht im Nachrichtenfenster an
     * und unterbricht das Programm.
     */
    @Override
    public void jException(String text) {
        owner.owner.stop();

        if (owner.owner.frameCircuit != null) {
            owner.owner.frameCircuit.addMessageToConsoleErrorWarnings("Exception : \"" + text + "\" in Element : \"" + getInternName() + "\" Name :\"" + getNameLocalized() + "\" Application abborded!");
        }

    }

    /*
     * zeigt eine Nachricht im Nachrichtenfenster an
     * ohne das Programm zu unterbrechen
     */
    @Override
    public void jShowMessage(String text) {
        owner.beendeWaitDialog();
        if (owner.owner.frameCircuit != null) {
            owner.owner.frameCircuit.addMessageToConsoleErrorWarnings("{\n" + text + "\n} in Element : \"" + jGetCaption() + "\" (" + getInternName() + ")");
        }
    }

    @Override
    public void jPrintln(String text) {
        jConsolePrintln(text);
    }

    @Override
    public void jPrint(String text) {
        jConsolePrint(text);
    }

    @Override
    public void jSetDocFilePath(String path) {
        this.docPath = path;

        /*String strLocale = Locale.getDefault().toString();
       
        
         if (strLocale.equalsIgnoreCase("de_DE"))
         {
         docFileName = path + "/doc.html";
         }
         else if (strLocale.equalsIgnoreCase("en_US"))
         {
         docFileName = path + "/doc_en.html";
         if (!new File(docFileName).exists())
         {
         docFileName = path + "/doc.html";
         }
            
         }
         else if (strLocale.equalsIgnoreCase("es_ES"))
         {
         docFileName = path + "/doc_es.html";
         if (!new File(docFileName).exists())
         {
         docFileName = path + "/doc.html";
         }
            
         }*/
        docPathIsAlreadySet = true;
    }

    @Override
    public void jSetName(String name) {
        setInternName(name);
    }

    @Override
    public void jSetNameLocalized(String label) {
        setNameLocalized(label);
    }

    @Override
    public void jSetChanged(VSObject obj) {
        obj.setChanged(true);
        //changedList.add(obj) ;
    }

    @Override
    public void jSetRasterized(boolean value) {
        //rasterized=value;
    }

    @Override
    public void jSetResizable(boolean value) {
        setResizable(value);
    }

    @Override
    public void jSetAspectRatio(double value) {
        aspect = value;

    }

    @Override
    public byte jGetPinIO(int pinIndex) {
        JPin pin = getPin(pinIndex);
        return pin.pinIO;
    }

    @Override
    public void jSetPinVisible(int pinIndex, boolean visible) {
        JPin pin = getPin(pinIndex);
        pin.setVisible(visible);
    }

    @Override
    public void jSetPinIO(int pinIndex, byte io) {
        JPin pin = getPin(pinIndex);

        if (io == 0 || io == 1 || io == 2 || io == 3) {
            pin.pinIO = io;
        } else {
            System.out.println("PinIO ist auserhalb von 0 - 3");
        }
    }

    public void jSetSubElementPosition(int index, int x, int y) {
        SubElement el = (SubElement) subElemente.get(index);
        if (el != null) {
            el.x = x;
            el.y = y;
        }
    }

    public void jSetSubElementSize(int index, int width, int height) {
        SubElement el = (SubElement) subElemente.get(index);
        if (el != null) {
            el.width = width;
            el.height = height;
        }
    }

    /* Zaehlt alle Output-Pins
     */
    public int countOutputPins() {
        int count = 0;
        for (int i = 0; i < getPinCount(); i++) {
            JPin pin = getPin(i);
            if (pin.pinIO == pin.PIN_OUTPUT) {
                count++;
            }
        }
        return count;
    }

    /* Zaehlt alle Input-Pins
     */
    public int countInputPins() {
        int count = 0;
        for (int i = 0; i < getPinCount(); i++) {
            JPin pin = getPin(i);
            if (pin.pinIO == pin.PIN_INPUT) {
                count++;
            }
        }
        return count;
    }

    /* Generiert fï¿½r alle Output-Pins den jeweiligen Datentyp
     * und liefert die Liste aller Objekte zurï¿½ck
     */
    public Object[] jInitAllOutputs() {
        Object[] temp = new Object[countOutputPins()];
        int counter = 0;
        for (int i = 0; i < getPinCount(); i++) {
            JPin pin = getPin(i);

            if (pin.pinIO == pin.PIN_OUTPUT) {
                pin.object = VSDataType.createPinDataType(pin.dataType);
                temp[counter++] = pin.object;
            }
        }
        return temp;
    }

    public Object[] jInitAllInputs() {
        Object[] temp = new Object[countInputPins()];

        int count = 0;
        for (int i = 0; i < getPinCount(); i++) {
            JPin pin = getPin(i);

            if (pin.pinIO == pin.PIN_INPUT) {
                temp[count++] = getPinInputReference(i);
            }
        }
        return temp;
    }

    @Override
    public void jResizeRightPins(int newAnzahlPins) {
    }

    @Override
    public void jInitPins() {
        initPins(true);
        alreadyInitialized = true;
    }

    @Override
    public int jGetPinDrahtSourceDataType(int pinIndex) {
        JPin pin = getPin(pinIndex);
        if (pin.draht != null) {
            Element sourceElement = (Element) owner.getObjectWithID(pin.draht.sourceElementID);
            if (sourceElement != null) {
                JPin sPin = sourceElement.getPin(pin.draht.sourcePin);
                return sPin.dataType;
            } else {
                return -1;
            }
        } else {
            return ExternalIF.C_VARIANT;
        }

    }

    @Override
    public boolean hasPinWire(int pinIndex) {
        JPin pin = getPin(pinIndex);
        return pin.draht != null;
    }

    @Override
    public Color jGetDTColor(int dataType) {
        return VSDataType.getCoorFromDataType(dataType);
    }

    @Override
    public int jGetPinDataType(int pinIndex) {
        return getPin(pinIndex).dataType;
    }

    // Erwartet eine Konstante aus ElementIF
    @Override
    public void jSetPinDataType(int pinIndex, int dataType) {
        getPin(pinIndex).dataType = dataType;
    }

    @Override
    public void jSetResizeSynchron(boolean value) {
        this.resizeSynchron = value;
        double w = (double) getWidth();
        double h = (double) getHeight();
        aspect = h / w;
    }

    @Override
    public void jSetPointPin(int pin) {
        getPin(pin).setPointPin();
    }

    @Override
    public void jSetDescription(int yDistance, String desc) {
        this.yDescriptionDistance = yDistance;
        setDescription(desc);
    }

    @Override
    public String[] jGetDataTypeList() {
        return VSDataType.getDataTypeList();
    }

    public int jGetDataType(String value) {
        String[] list = jGetDataTypeList();
        for (int i = 0; i < list.length; i++) {
            if (list[i].equalsIgnoreCase(value)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int jGetDataType(VSObject obj) {
        return VSDataType.getDataType(obj);
    }

    @Override
    public String jGetCaption() {
        return getCaption();
    }

    @Override
    public void jSetCaptionVisible(boolean value) {
        setCaptionVisible(value);
    }

    @Override
    public void jSetCaption(String caption) {
        setInterNalCaption(caption); // setzt das Caption fï¿½r das einfï¿½gen und nummerieren von neuen Elementen
        setCaption(caption);
    }

    // 0 = Oben
    // 1 = Unten
    @Override
    public void jSetNamePosition(int position) {
        this.setNamePosition(position);
    }

    @Override
    public void jSetPinDescription(int pin, String description) {
        getPin(pin).setDescription(description);
    }

    @Override
    public String jGetElementPath() {
        return owner.getElementPath();
    }

    /*public String jGetSourcePath()
     {
     String str=new File(elementPath+classPath).getParentFile().getAbsolutePath();
     return str+"/";
     }*/
    @Override
    public String jGetSourcePath() {
        String cPath = classPath;
        // lï¿½sche alles bin zum ersten "/" von Rechts!

        for (int i = classPath.length() - 1; i > 1; i--) {
            String ch = classPath.substring(i, i + 1);
            if (ch.equalsIgnoreCase("/")) {
                cPath = classPath.substring(1, i + 1);
                break;
            }

        }
        return elementPath + "/" + cPath;
    }

    public Image jLoadImage(String fileName) {
        Image image = null;
        if (getVMObject().owner.getFrameMain() != null && (new File(fileName).exists())) {
            image = Toolkit.getDefaultToolkit().getImage(fileName);
            MediaTracker mc = new MediaTracker(getVMObject().owner.getFrameMain());
            mc.addImage(image, 0);

            try {
                mc.waitForID(0);
            } catch (InterruptedException ex) {
                jException("Fehler in Methode Element.jLoadImage()" + ex.toString());
            }

        }
        return image;
    }
    public boolean processAutomatic = false;

    public void jProcessAutomatic(boolean value) {
        processAutomatic = value;
    }

    public void jSetLeftPinsVisible(boolean bVal) {
        if (leftPinsVisible != bVal) {
            leftPinsVisible = bVal;
        }
    }

    public void jSetTopPinsVisible(boolean bVal) {
        if (topPinsVisible != bVal) {
            topPinsVisible = bVal;
        }
    }

    public void jSetRightPinsVisible(boolean bVal) {
        if (rightPinsVisible != bVal) {
            rightPinsVisible = bVal;
        }
    }

    public void jSetBottomPinsVisible(boolean bVal) {
        if (bottomPinsVisible != bVal) {
            bottomPinsVisible = bVal;
        }
    }

    public boolean jIsPathEditing() {
        if (owner.getStatus() instanceof StatusEditPath) {
            StatusEditPath status = (StatusEditPath) owner.getStatus();
            if (status.element.equals(this)) {
                return true;
            }
        }
        return false;
    }

    public int jGetWidth() {
        return getWidth();
    }

    public int jGetHeight() {
        return getHeight();
    }

    public void jSetTop(int value) {
        setTop(value);
    }

    public void jSetLeft(int value) {
        setLeft(value);
    }

    public int jGetLeft() {
        return getX();
    }

    public void jSetSize(int width, int height) {
        if (height > 600) {
            //System.out.println("XXXX");
        }
        this.setSize(width, height);

    }

    public void jSetMinimumSize(int width, int height) {
        super.setMinimumSize(width, height);
    }

    public void jFixElement() {
        setOriginalWidthHeight();
        elementFixed = true;
    }

    public void setOriginalWidthHeight() {
        Basis basis = elementBasis;
        if (basis != null) {
            VMObject vm = basis.getFrontBasis();

            Component comp = null;
            for (int i = 0; i < vm.getComponentCount(); i++) {
                comp = vm.getComponent(i);
                if (comp instanceof Element) {
                    Element el = (Element) comp;

                    if (el.fx1 == 0 || el.fy1 == 0 || el.fx == 0 || el.fy == 0) {
                        el.fx1 = (((double) el.getX()) / ((double) this.getWidth()));
                        el.fy1 = (((double) el.getY()) / ((double) this.getHeight()));

                        el.fx = ((double) el.getWidth() / (double) this.getWidth());
                        el.fy = ((double) el.getHeight() / (double) this.getHeight());
                    }
                }
            }
        }
    }

    public void resizeBasisElements(int width, int height) {
        Basis basis = elementBasis;
        if (basis != null) {
            VMObject vm = basis.getFrontBasis();

            Component comp = null;
            for (int i = 0; i < vm.getComponentCount(); i++) {
                comp = vm.getComponent(i);
                if (comp instanceof Element) {
                    Element el = (Element) comp;

                    double dx1 = el.fx1 * ((double) width);
                    double dy1 = el.fy1 * ((double) height);

                    double dx2 = el.fx * ((double) width);
                    double dy2 = el.fy * ((double) height);

                    el.setLocation((int) (dx1), (int) (dy1));
                    el.setSize((int) (dx2), (int) (dy2));
                }
            }
        }
    }

    public void setSize(int width, int height) {
        int d = 8;
        if (width < d) {
            width = d;
        }
        if (height < d) {
            height = d;
        }

        if (width > 4000) {
            width = 4000;
        }
        if (height > 4000) {
            height = 4000;
        }

        if (isResizeSynchron()) {
            height = (int) (((double) width) * aspect);
        }

        if (owner != null) {
            if (owner == this.owner.owner.getFrontBasis() && elementBasis != null) {
                elementBasis.getFrontBasis().setSize(width, height);
            }
        }
        super.setSize(width, height);

        if (elementBasis != null) {
            if (elementFixed) {
                resizeBasisElements(width, height);
            }
        }

        if (layeredPane != null) {
            layeredPane.setSize(width, height);
        }
        if (mode == MODE_NORMAL) {
            initPins(false);
        }
    }

    public void Change(int pinIndex, Object value) {
        if (classRef != null) {
            classRef.changePin(pinIndex, value);
        }
    }

    public void jSetInnerBorderVisibility(boolean visible) {
        if (getInnerBorderVisibility() != visible) {
            setInnerBorderVisibility(visible);
            repaint();
        }
    }

    public int getTopPins() {
        return pinsLstTop.size();
    }

    public int getRightPins() {
        return pinsLstRight.size();
    }

    public int getBottomPins() {
        return pinsLstBottom.size();
    }

    public int getLeftPins() {
        return pinsLstLeft.size();
    }

    public void jSetLeftPins(int value) {
        if (value < pinsLeft) {
            while (pinsLstLeft.size() > value) {
                int c = pinsLstLeft.size() - 1;

                JPin pin = (JPin) pinsLstLeft.get(c);
                if (pin.draht != null) {
                    owner.deleteDraht(pin.draht);
                }
                this.remove(pin);
                pinsLstLeft.remove(pin);
            }

        } else {
            int c = value - pinsLeft;
            for (int i = 0; i < c; i++) {
                JPin pin = new JPin(this, pinsLstTop.size() + pinsLstRight.size() + pinsLstBottom.size() + pinsLstLeft.size(), 3);
                this.add(pin);
                pinsLstLeft.add(pin);
            }
        }

        // die draht.pinIndex fuer die Linken Pins mï¿½ssen auf die neuen Pin Index umgestellt werden!
        //ist hier nicht noetig!
        pinsLeft = value;
        initPins(true);
    }

    public void jSetRightPins(int value) {
        if (value < pinsRight) {
            while (pinsLstRight.size() > value) {
                int c = pinsLstRight.size() - 1;

                JPin pin = (JPin) pinsLstRight.get(c);
                if (pin.draht != null) {
                    owner.deleteDraht(pin.draht);
                }
                this.remove(pin);
                pinsLstRight.remove(pin);
            }
            //owner.reorderWireFrames();
        } else {
            int c = value - pinsRight;
            for (int i = 0; i < c; i++) {
                JPin pin = new JPin(this, pinsLstTop.size() + pinsLstRight.size(), 1);
                this.add(pin);
                pinsLstRight.add(pin);
            }
        }
        // die draht.pinIndex fï¿½r die Linken Pins mï¿½ssen auf die neuen Pin Index umgestellt werden!
        for (int i = 0; i < pinsLstLeft.size(); i++) {
            JPin pin = (JPin) pinsLstLeft.get(i);
            if (pin.draht != null) {
                pin.draht.destPin = pinsLstRight.size() + i;
            }
        }

        pinsRight = value;
        initPins(true);
    }

    public void jSetTopPins(int value) {

        if (value < pinsTop) {
            while (pinsLstTop.size() > value) {
                int c = pinsLstTop.size() - 1;

                JPin pin = (JPin) pinsLstTop.get(c);
                if (pin.draht != null) {
                    owner.deleteDraht(pin.draht);
                }
                pinsLstTop.remove(pin);
                this.remove(pin);
            }
        } else {
            int c = value - pinsTop;
            for (int i = 0; i < c; i++) {
                JPin pin = new JPin(this, pinsLstTop.size(), 0);
                this.add(pin);
                pinsLstTop.add(pin);
            }
        }

        pinsTop = value;
        initPins(true);
        //  owner.reorderWireFrames();
    }

    public void jSetBottomPins(int value) {
        if (value < pinsBottom) {
            while (pinsLstBottom.size() > value) {
                int c = pinsLstBottom.size() - 1;

                JPin pin = (JPin) pinsLstBottom.get(c);
                if (pin.draht != null) {
                    owner.deleteDraht(pin.draht);
                }
                this.remove(pin);
                pinsLstBottom.remove(pin);
            }

        } else {
            int c = value - pinsBottom;
            for (int i = 0; i < c; i++) {
                JPin pin = new JPin(this, pinsLstTop.size() + pinsLstRight.size() + pinsLstBottom.size(), 2);
                this.add(pin);
                pinsLstBottom.add(pin);
            }
        }

        pinsBottom = value;
        initPins(true);
    }

    public int getPinsLeft() {
        return pinsLeft;
    }

    public int getPinsTop() {
        return pinsTop;
    }

    public int getPinsRight() {
        return pinsRight;
    }

    public int getPinsBottom() {
        return pinsBottom;
    }

    /* liefert eine Referenz auf das Object des jeweiligen Pin
     */
    public Object getPinInputReference(int pinIndex) {

        JPin pin = getPin(pinIndex);

        if (pin.draht != null && owner.owner.started == true) {
            return pin.draht.pinSouce.object;
        } else {
            return null;
        }
    }

    /* setzt eine Referenz im jeweiligen Pin
     */
    public void setPinOutputReference(int pinIndex, Object referenz) {
        JPin pin = getPin(pinIndex);
        pin.object = referenz;

        /*((VSObject)referenz).setPin(pinIndex);
         ((VSObject)referenz).setElement((ExternalIF)this);        */
    }

    public void writePin(int index, double value) {
        writeInformationToDest(index, value);
    }

    public void writePinObj(int index, Object object) {
        writeInformationToDest(index, object);
    }

    @Override
    public void writePinBoolean(int index, boolean value) {
        writeInformationToDest(index, new Boolean(value));
    }

    @Override
    public void writePinInteger(int index, int value) {
        writeInformationToDest(index, new Integer(value));
    }

    @Override
    public void writePinDouble(int index, double value) {
        writeInformationToDest(index, new Double(value));
    }

    @Override
    public void writePinString(int index, String value) {
        writeInformationToDest(index, value);
    }

    @Override
    public boolean readPinBoolean(int index) {
        JPin pin = getPin(index);
        Object obj = pin.object;
        if (obj instanceof Boolean) {
            Boolean value = (Boolean) obj;
            return value;
        } else {
            return false;
        }
    }

    @Override
    public int readPinInteger(int index) {
        JPin pin = getPin(index);
        Object obj = pin.object;
        if (obj instanceof Integer) {
            Integer value = (Integer) obj;
            return value;
        } else {
            return 0;
        }
    }

    @Override
    public double readPinDouble(int index) {
        JPin pin = getPin(index);
        Object obj = pin.object;
        if (obj instanceof Double) {
            Double value = (Double) obj;
            return value;
        } else {
            return 0.0;
        }
    }

    @Override
    public String readPinString(int index) {
        JPin pin = getPin(index);
        Object obj = pin.object;
        if (obj instanceof String) {
            String value = (String) obj;
            return value;
        } else {
            return "";
        }
    }

    @Override
    public double readPin(int index) {
        JPin pin = getPin(index);
        return pin.value;
    }

    @Override
    public Object readPinObj(int index) {
        JPin pin = getPin(index);
        return pin.object;
    }

    // Sorgt dafï¿½r das die Werte ins Ziel-Element eingetragen werden
    public void writeInformationToDest(int pin, double value) {
        JPin p = getPin(pin);
        //p.value=value;
        Draht draht = p.draht;

        if (draht != null) {
            draht.leiteInformation(value);
        }
    }

    public void writeInformationToDest(int pin, Object o) {
        JPin p = getPin(pin);
        //p.object=o;
        Draht draht = p.draht;
        if (draht != null) {
            draht.leiteInformation(o);
        }
    }

    // ***********************  Ende ExternalIF ***********************
    // *********************** Begin PinIF ***********************
    @Override
    public void PinMousePressed(MouseEvent e, JPin pin, int pinIndex) {
        owner.elementPinMousePressed(e, this.getID(), pinIndex);
    }

    @Override
    public void PinMouseReleased(MouseEvent e, JPin pin, int pinIndex) {
        owner.elementPinMouseReleased(e, this.getID(), pinIndex);
    }

    @Override
    public void PinMouseClicked(MouseEvent e, JPin pin, int pinIndex) {
        owner.elementPinMouseClicked(e, this.getID(), pinIndex);
    }

    @Override
    public void PinMouseEntered(MouseEvent e, JPin pin, int pinIndex) {
        owner.elementPinMouseEntered(e, this.getID(), pinIndex);
    }

    @Override
    public void PinMouseExited(MouseEvent e, JPin pin, int pinIndex) {
        owner.elementPinMouseExited(e, this.getID(), pinIndex);
    }

    @Override
    public void PinMouseDragged(MouseEvent e, JPin pin, int pinIndex) {
        owner.elementPinMouseDragged(e, this.getID(), pinIndex);
    }

    @Override
    public void PinMouseMoved(MouseEvent e, JPin pin, int pinIndex) {
        owner.elementPinMouseMoved(e, this.getID(), pinIndex);
    }

    //*********************** Ende PinIF ***********************
    private void initPins(boolean newPins) {
        if (pinsLstTop == null || pinsLstRight == null || pinsLstBottom == null || pinsLstLeft == null) {
            return;
        }

        int w = getWidth();
        int h = getHeight();

        double x = 0, y = 0;
        JPin pin = null;
        int abzuege;
        double pinDistance = 0;
        int c = 0;
        // ******************* Zeichne die oberen Pins *******************
        abzuege = 0;
        if (leftPinsVisible || rightPinsVisible) {
            abzuege = 1;
        }
        if (leftPinsVisible && rightPinsVisible) {
            abzuege = 2;
        }

        pinDistance = ((w - abzuege * pinWidth) - (pinsTop * pinWidth)) / (pinsTop + 1);
        if (pinDistance <= 0) {
            pinDistance = 0;
        }
        //if (pinsTop*pinWidth>w) System.out.println("Achtung: die Anzahl der Pins ist zu gross fuer das Element!");

        if (leftPinsVisible) {
            x = pinDistance + pinWidth;
        } else {
            x = pinDistance;
        }
        if (pinsTop == 1) {
            x = (w / 2) - (pinWidth / 2);
        }
        for (int i = 0; i < pinsLstTop.size(); i++) {
            pin = (JPin) pinsLstTop.get(i);

            pin.setLocation((int) x, 0);
            pin.setSize(pinWidth, pinHeight);
            pin.pinIndex = c++;

            x += pinDistance + pinWidth;
        }

        // ******************* Zeichne die rechten Pins *******

        /*if (leftPinsVisible) x=pinDistance+pinWidth;
         else x=pinDistance;
         if (pinsTop==1) x=(w/2)-(pinWidth/2);
         for (int i=0;i<pinsLstTop.size();i++)
         {
         pin = (JPin)pinsLstTop.get(i);
         pin.setLocation((int)x,0);
         pin.setSize(pinWidth,pinHeight);
         pin.pinIndex=c++;
         x+=pinDistance+pinWidth;
         }*/
        // ******************* Zeichne die rechten Pins *******************
        abzuege = 0;
        if (topPinsVisible || bottomPinsVisible) {
            abzuege = 1;
        }
        if (topPinsVisible && bottomPinsVisible) {
            abzuege = 2;
        }

        pinDistance = ((double) (h - abzuege * pinHeight) - (pinsRight * pinHeight)) / (double) (pinsRight + 1);
        if (pinDistance <= 0) {
            pinDistance = 0;
        }
        //if (pinsRight*pinHeight>h) System.out.println("Achtung: die Anzahl der Pins ist zu gross fuer das Element!");

        if (topPinsVisible) {
            y = pinDistance + pinHeight;
        } else {
            y = pinDistance;
        }
        if (pinsRight == 1) {
            y = ((double) h / 2.0) - ((double) pinHeight / 2.0);
        }
        for (int i = 0; i < pinsLstRight.size(); i++) {
            pin = (JPin) pinsLstRight.get(i);

            pin.setLocation(w - pinWidth, (int) y);
            pin.setSize(pinWidth, pinHeight);
            pin.pinIndex = c++;
            y += pinDistance + pinHeight;
        }

        // ******************* Zeichne die unteren  Pins *******************
        abzuege = 0;
        if (leftPinsVisible || rightPinsVisible) {
            abzuege = 1;
        }
        if (leftPinsVisible && rightPinsVisible) {
            abzuege = 2;
        }

        pinDistance = ((w - abzuege * pinWidth) - (pinsBottom * pinWidth)) / (pinsBottom + 1);
        if (pinDistance <= 0) {
            pinDistance = 0;
        }
        //if (pinsBottom*pinWidth>w) System.out.println("Achtung: die Anzahl der Pins ist zu gross fuer das Element!");

        if (leftPinsVisible) {
            x = pinDistance + pinWidth;
        } else {
            x = pinDistance;
        }
        if (pinsBottom == 1) {
            x = (w / 2) - (pinWidth / 2);
        }

        for (int i = 0; i < pinsLstBottom.size(); i++) {
            pin = (JPin) pinsLstBottom.get(i);

            pin.setLocation((int) x, h - pinHeight);
            pin.setSize(pinWidth, pinHeight);
            pin.pinIndex = c++;
            x += pinDistance + pinWidth;
        }

        // ******************* Zeichne die linken Pins *******************
        abzuege = 0;
        if (topPinsVisible || bottomPinsVisible) {
            abzuege = 1;
        }
        if (topPinsVisible && bottomPinsVisible) {
            abzuege = 2;
        }

        pinDistance = ((double) (h - abzuege * pinHeight) - (pinsLeft * pinHeight)) / (double) (pinsLeft + 1);
        if (pinDistance <= 0) {
            pinDistance = 0;
        }
        //if (pinsLeft*pinHeight>h) System.out.println("Achtung: die Anzahl der Pins ist zu gross fuer das Element!");

        //if (topPinsVisible)  y=pinDistance+pinHeight; else y=pinDistance;
        if (topPinsVisible) {
            y = pinDistance + pinHeight;
        } else {
            y = pinDistance;
        }
        if (pinsLeft == 1) {
            y = ((double) h / 2.0) - ((double) pinHeight / 2.0);
        }

        for (int i = 0; i < pinsLstLeft.size(); i++) {
            pin = (JPin) pinsLstLeft.get(i);

            pin.setLocation(0, (int) y);
            pin.setSize(pinWidth, pinHeight);
            pin.pinIndex = c++;
            y += pinDistance + pinHeight;
        }

    }

    private void loadStandardValues() {
        pinsTop = 0;
        pinsRight = 0;
        pinsBottom = 0;
        pinsLeft = 0;
    }

    public JPin getPinWhereInMouse(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        for (int i = getPinCount() - 1; i >= 0; i--) {
            JPin pin = getPin(i);
            int x1 = pin.getX();
            int y1 = pin.getY();
            int x2 = x1 + pin.getWidth();
            int y2 = y1 + pin.getHeight();

            if (x > x1 && x < x2 && y > y1 && y < y2) {
                e.setSource(pin);
                e.translatePoint(-pin.getRealX(), -pin.getRealY());
                return pin;
            }
        }
        return null;
    }

    public void mouseClicked(MouseEvent e) {
        JPin pin = getPinWhereInMouse(e);
        if (pin != null) {
            pin.mouseClicked(e);
        } else if (e.getClickCount() == 2) {
            owner.elementMouseDblClick(e);
        } else {
            owner.elementMouseClicked(e);
        }
    }

    //When the mouse cursor enters the canvas make it the two
    //straigth lines type
    public void mouseEntered(MouseEvent e) {
        e.setSource(layeredPane);
        owner.elementMouseEntered(e);
    }

    //When mouse exits canvas set to default type
    public void mouseExited(MouseEvent e) {
        e.setSource(layeredPane);
        owner.elementMouseExited(e);
    }

    //mouse listener for when the mouse button is released
    public void mouseReleased(MouseEvent e) {
        JPin pin = getPinWhereInMouse(e);
        if (pin != null) {
            pin.mouseReleased(e);
        } else {
            e.setSource(layeredPane);
            owner.elementMouseReleased(e);
        }
    }

    public void mousePressed(MouseEvent e) {
        JPin pin = getPinWhereInMouse(e);
        if (pin != null) {
            pin.mousePressed(e);
        } else {
            e.setSource(layeredPane);
            owner.elementMousePressed(e);
        }
    }

    public void mouseDragged(MouseEvent e) {
        JPin pin = getPinWhereInMouse(e);
        if (pin != null) {
            pin.mouseDragged(e);
        } else {
            e.setSource(layeredPane);
            owner.elementMouseDragged(e);
        }
        /*int x=e.getX();
         int y=e.getY();
         this.setLocation(x,y);*/
    }

    public void mouseMoved(MouseEvent e) {
        JPin pin = getPinWhereInMouse(e);
        if (pin != null) {
            pin.mouseMoved(e);
        } else {
            e.setSource(layeredPane);
            //e.setSource(this);
            owner.elementMouseMoved(e);
        }

    }

    private void drawPinsLines(Graphics g) {
        int w = getWidth();
        int h = getHeight();

        // Zeichne die Markierungen fï¿½r die Pins
        g.setColor(Color.gray);

        g.drawLine(pinWidth, 0, pinWidth, h - 1);
        g.drawLine(w - pinWidth - 1, 0, w - pinWidth - 1, h - 1);
        g.drawLine(0, pinHeight, w - 1, pinHeight);
        g.drawLine(0, h - pinHeight - 1, w - 1, h - pinHeight - 1);
    }

    public void drawPins(Graphics2D g) {
        int x = 0;
        int y = 0;
        boolean drawFrame = true;

        if ((getVMObject().getStatus() instanceof BasisStatus.StatusRun)) {
            drawFrame = false;
        } else {
            drawFrame = true;
        }
        {
            JPin pin = null;
            for (int i = 0; i < getPinCount(); i++) {
                pin = getPin(i);
                //pin.setLocation(x,y);
                //pin.setSize(10,10);

                //pin.paint(x,y,g,drawFrame);
            }

        }
    }

    public void drawSubElements(Graphics2D gx, boolean translate) {
        try {
            for (int i = 0; i < subElemente.size(); i++) {
                Object o = subElemente.get(i);
                if (o instanceof JPanel) {
                    JPanel panel = (JPanel) o;
                    if (panel.isVisible()) {
                        Graphics2D g = (Graphics2D) gx.create();

                        if (translate) {
                            g.setClip(null);
                            g.translate(-getX(), -getY());
                        }

                        g.setFont(panel.getFont());
                        g.translate(panel.getLocation().x, panel.getLocation().y);
                        /*g.setColor(Color.RED);
                         g.fillRect(0,0,panel.getWidth(),panel.getHeight());*/
                        g.setColor(Color.black);
                        panel.paint(g);
                        if (translate) {
                            g.translate(-panel.getLocation().x, -panel.getLocation().y);
                            g.translate(getX(), getY());
                        }
                        g.dispose();
                    }
                }
            }
        } catch (Exception ex) {
        }

    }

    public boolean drawElement(Graphics2D g) {

        Rectangle r = jGetBounds();
        if (g != null) {
            //Graphics2D g = (Graphics2D)gx.create();

            //g.clipRect(getX(), getY(),getWidth(),getHeight());
            //g.translate(getX(),getY());
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (classRef != null) {
                try {
                    classRef.xpaint(g);
                } catch (Exception ex) {
                    owner.owner.showErrorMessage("Error in Element \"" + this.jGetCaption() + "\" Method : xpaint() : " + ex.toString());
                } catch (java.lang.NoSuchFieldError ex) {
                    owner.owner.showErrorMessage("Error in Element \"" + this.jGetCaption() + "\" Method : xpaint() : " + ex.toString());
                }
            } else {
                g.setColor(Color.red);
                g.fillRect(r.x + 1, r.y + 1, r.width - 2, r.height - 2);

                g.setColor(Color.black);
                g.drawLine(r.x + 1, r.y + 1, r.x + r.width - 2, r.y + r.height - 2);
                g.drawLine(r.x + r.width - 2, r.y + 1, r.x + 1, r.y + r.height - 2);
            }

            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            drawPins(g);
            //g.translate(-getX(),-getY());
            // g.dispose();
        }

        return true;

    }

    public int getMaxLengthPinDescription(Graphics g) {
        double maxWidth = 0;

        for (int i = 0; i < getPinCount(); i++) {
            JPin pin = getPin(i);

            String desc = pin.getDescription();
            desc += " (" + VSDataType.getDataTypeShortCut(pin.dataType) + ")";

            FontMetrics fm = g.getFontMetrics();
            Rectangle2D r = fm.getStringBounds(desc, g);

            if (pin.pinAlign == 1) {
                // Pins Rechts
                if (r.getWidth() > maxWidth) {
                    maxWidth = r.getWidth();
                }
            }
            if (pin.pinAlign == 3) {
                // Pins links
                if (r.getWidth() > maxWidth) {
                    maxWidth = r.getWidth();
                }
            }
        }

        return (int) maxWidth;
    }

    public int getMaxLengthPinDescriptionVertikalTop(Graphics g) {
        double maxWidth = 0;

        for (int i = 0; i < getPinCount(); i++) {
            JPin pin = getPin(i);

            String desc = pin.getDescription();
            desc += " (" + VSDataType.getDataTypeShortCut(pin.dataType) + ")";

            FontMetrics fm = g.getFontMetrics();
            Rectangle2D r = fm.getStringBounds(desc, g);

            if (pin.pinAlign == 0) {
                if (r.getWidth() > maxWidth) {
                    maxWidth = r.getWidth();
                }
            }
        }

        return (int) maxWidth;
    }

    public int getMaxLengthPinDescriptionVertikalBottom(Graphics g) {
        double maxWidth = 0;

        for (int i = 0; i < getPinCount(); i++) {
            JPin pin = getPin(i);

            String desc = pin.getDescription();
            desc += " (" + VSDataType.getDataTypeShortCut(pin.dataType) + ")";

            FontMetrics fm = g.getFontMetrics();
            Rectangle2D r = fm.getStringBounds(desc, g);

            if (pin.pinAlign == 2) {
                // Pins Oben
                if (r.getWidth() > maxWidth) {
                    maxWidth = r.getWidth();
                }
            }
        }

        return (int) maxWidth;
    }

    private void drawPin(Graphics g, int x, int y, int width, int height) {
        int my = height / 2;

        //g.drawRect(x,y,width,height);
        g.drawLine(x, y + my, x + width, y + my);
    }

    public Image getElementImage() {
        boolean isSelected = isSelected();
        setSelected(false);

        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(getWidth(), getHeight(), java.awt.image.BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D g = image.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        print(g);

        g.dispose();

        setSelected(isSelected);

        return image;
    }

    public void draw90GradRotatedLabel(Graphics g, String text, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        FontMetrics fm = getFontMetrics(g.getFont());

        //.getAscent(), (getHeight()/2)+(getFontMetrics(g.getFont()).stringWidth(text)/2 ));
        g2d.translate(x + 5, y);
        g2d.rotate(-Math.PI / 2);
        g2d.drawString(text, 0, 0);
        g2d.dispose();
    }

    public void draw90GradRotatedLabel2(Graphics g, String text, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        FontMetrics fm = getFontMetrics(g.getFont());

        g2d.translate(x, y + fm.stringWidth(text));
        g2d.rotate(-Math.PI / 2);
        g2d.drawString(text, 0, 0);
        g2d.dispose();
    }

    public Image getIconImage() {
        if (definition_def != null) {
            String fn = elementPath + definitionPath + "/" + definition_def.iconFilename;

            Image result = Toolkit.getDefaultToolkit().getImage(fn);

            MediaTracker mc = new MediaTracker(this);
            mc.addImage(result, 0);
            try {
                mc.waitForID(0);
            } catch (Exception exe) {

            }
            return result;
            //System.out.println("fn"+fn);
        }

        return null;
    }

    public Image getImage() {
        Image elImage = getElementImage();

        BufferedImage dummy = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);

        Graphics gdummy = dummy.createGraphics();

        int maxDescriptionWidth = getMaxLengthPinDescription(gdummy);
        int lblWidth = lblName.getWidth();

        int maxWidth = 0;
        int maxHeight = 0;

        if ((maxDescriptionWidth * 2) + getWidth() > lblWidth) {
            maxWidth = (maxDescriptionWidth * 2) + getWidth();
        } else {
            maxWidth = (maxDescriptionWidth * 2) + getWidth() + lblWidth;
        }

        maxWidth += 20;

        maxHeight = getHeight() + lblName.getHeight() + 5 + 10;

        int maxYTop = getMaxLengthPinDescriptionVertikalTop(gdummy);
        int maxYBottom = getMaxLengthPinDescriptionVertikalBottom(gdummy);
        maxHeight += maxYTop;
        maxHeight += maxYBottom;
        maxHeight += 15; // Für die Überschrift!

        int mitteX = maxWidth / 2;
        int mitteY = maxHeight / 2;

        BufferedImage image = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, maxWidth, maxHeight);

        int imgWidth = elImage.getWidth(null);
        int imgHeight = elImage.getHeight(null);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(elImage, mitteX - imgWidth / 2, mitteY - imgHeight / 2, null);

        g.setColor(Color.BLACK);
        g.setFont(stdFont);

        int obenY = 10;

        for (int i = 0; i < getPinCount(); i++) {
            JPin pin = getPin(i);

            String desc = pin.getDescription();
            desc += " (" + VSDataType.getDataTypeShortCut(pin.dataType) + ")";

            /*if (pin.pinIO==JPin.PIN_INPUT) desc+="-IN";else
             if (pin.pinIO==JPin.PIN_OUTPUT) desc+="-OUT";else
             if (pin.pinIO==JPin.PIN_INPUT_OUTPUT) desc+="-IO";*/
            //String desc=pin.getDescription();
            //desc+=" ("+VSDataType.getDataTypeShortCut(pin.dataType)+")";
            FontMetrics fm = g.getFontMetrics();
            Rectangle2D r = fm.getStringBounds(desc, g);

            if (pin.pinAlign == 0) // Pins Oben
            {
                int px = pin.getLocation().x;
                draw90GradRotatedLabel(g, desc, mitteX - (imgWidth / 2) + px + 3, mitteY - imgHeight / 2 - 5);
            }

            if (pin.pinAlign == 1)// Pins Rechts
            {
                int py = pin.getLocation().y + 14;
                g.drawString(desc, 5 + (mitteX + (imgWidth / 2)), mitteY - imgHeight / 2 + py - 5);
            }

            if (pin.pinAlign == 2)// Pins Unten
            {
                int px = pin.getLocation().x;
                draw90GradRotatedLabel2(g, desc, mitteX - (imgWidth / 2) + px + 7, mitteY + imgHeight / 2 + 5);
            }

            if (pin.pinAlign == 3)// Pins Links
            {
                int py = pin.getLocation().y + 14;
                g.drawString(desc, (mitteX - (imgWidth / 2) - 5) - (int) r.getWidth(), mitteY - imgHeight / 2 + py - 5);
            }

        }

        //g.drawString(lblName.getText(),mitteX-lblName.getWidth()/2,maxHeight-10);
        //g.setColor(Color.LIGHT_GRAY);
        //g.drawRect(0, 0, maxWidth - 1, maxHeight - 1);
        return image;
    }

    public synchronized void mypaint(Graphics2D g) {

        if (!getVMObject().isGraphicLocked() && g != null && isVisible()) {
            g.setStroke(standardStroke);

            if (mode == MODE_XOR) {
                g.setXORMode(Color.RED);
                g.setColor(Color.WHITE);
                g.drawRect(getX(), getY(), getWidth() - 1, getHeight() - 1);
            }
            if (mode == MODE_NORMAL) {
                if (borderVisibility) {
                    g.setColor(new Color(200, 200, 200));
                    g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                }
                if (innerBorderVisibility) {
                    g.setColor(Color.BLACK);
                    Rectangle r = jGetBounds();
                    g.drawRect(r.x, r.y, r.width - 1, r.height - 1);
                }

                drawElement(g);

                if (highlighted) {
                    g.setColor(Color.RED);
                    g.setStroke(strokeDick);
                    g.drawRect(1, 1, getWidth() - 2, getHeight() - 2);
                }

                g.setStroke(standardStroke);
                if (Tools.settings.isElementIDVisible()) {
                    g.setColor(Color.RED);
                    g.setFont(new Font("Arial", Font.BOLD, 10));
                    g.drawString("" + getID(), getWidth() / 2 - 10, getHeight() / 2 + 5);
                }

                /*if (getInternName().equalsIgnoreCase("###SPLINE###"))
                 {
                 if (owner.getStatus() instanceof StatusEditPath)
                 {
                 g.setColor(Color.RED);
                 g.setStroke(standardStroke);
                 int d=pointSize;
                 int d2=d/2;
                 Point point;
                 for (int i=0;i<points.size();i++)
                 {
                 point=points.get(i);
                 g.drawOval(point.x-d2,point.y-d2,d,d);
                 if (i%2==0)
                 {
                 Point point2=points.get(i+1);
                 g.drawLine(point.x,point.y,point2.x,point2.y);
                 }                                
                 }
                 }
                 }*/
 /*if (isCaptionVisible())
                 {
                 Graphics gcopy = g.create();
                 Point pp = getLocation();
                 gcopy.translate(-pp.x,-pp.y);
                 gcopy.setClip(null);
                 Point p=lblName.getLocation();
                 gcopy.setFont(lblName.getFont());
                 gcopy.setColor(Color.BLACK);
                 gcopy.drawString(lblName.getText(),p.x,p.y+10);
                 gcopy.translate(pp.x,pp.y);
                 gcopy.dispose();
                 }
                 drawSubElements(g,true);
                 g.setStroke(standardStroke);
                 g.setFont(stdFont);*/
                // g.setStroke(standardStroke);
                // g.setFont(stdFont);
                //g.setColor(Color.black);
                //g.drawString(""+getVMObject().getObjectIndex(getID()),getX()+5,getY()-5);
                //g.drawString(""+getID(),getX()+5,getY()-5);

                /*if (isSelected())
                 {
                 g.setColor(new Color(255,70,70,200));
                 int x=getLocation().x;
                 int y=getLocation().y;
                 g.setStroke(strokeDick);
                 g.drawRect(0,0, getWidth()-1,getHeight()-1);
                 g.setStroke(standardStroke);
                 Color colDisabled=new Color(120,120,120);
                 }*/
                //drawSubElements(g);

                /*if (showPinDescription) {
                 for (int i=0;i<getPinCount();i++) {
                 JPin pin = getPin(i);
                 String desc=pin.getDescription();
                 desc+=" ("+VSDataType.getDataTypeShortCut(pin.dataType)+")";
                 //g.setFont(pinFont);
                 FontMetrics fm = g.getFontMetrics();
                 Rectangle2D r = fm.getStringBounds(desc,g);
                 if (pin.pinAlign==1) {
                 // Pins Rechts
                 g.setColor(Color.WHITE);
                 g.fillRect(getX()+getWidth()+5,getY()+pin.getLocation().y, (int)r.getWidth(),(int)r.getHeight()+2);
                 g.setColor(Color.BLACK);
                 g.drawString(desc, getX()+getWidth()+5,getY()+pin.getLocation().y+10);
                 } else {
                 // Pins links
                 g.setColor(Color.WHITE);
                 g.fillRect(getX()-r.getBounds().width-5,getY()+pin.getLocation().y, (int)r.getWidth(),(int)r.getHeight()+2);
                 g.setColor(Color.BLACK);
                 g.drawString(desc, getX()-r.getBounds().width-5,getY()+pin.getLocation().y+10);
                 }
                 }
                 }*/
            }
        }
    }

    public void propertyChanged(Object o) {
        if (o == vLeft) {
            setLocation(vLeft.getValue(), vTop.getValue());
            jRepaint();
        }
        if (o == vTop) {
            setLocation(vLeft.getValue(), vTop.getValue());
            jRepaint();
        }
        if (o == vWidth) {
            setSize(vWidth.getValue(), vHeight.getValue());
            jRepaint();
        }
        if (o == vHeight) {
            setSize(vWidth.getValue(), vHeight.getValue());
            jRepaint();
        }
        if (o == vCaption) {
            setCaption(vCaption.getValue());
            jRepaint();
        }
        if (o == vVisible) {
            setVisibleWhenRun(vVisible.getValue());
        }
        if (o == vShowCaption) {
            setCaptionVisible(vShowCaption.getValue());
            jRepaint();
        }

        if (elementBasis != null) {
            elementBasis.getCircuitBasis().processpropertyChangedToAllElements(o);
            elementBasis.getFrontBasis().processpropertyChangedToAllElements(o);
        }
        
    }

    public void paintComponent(Graphics2D g) {
        paintComponent((Graphics) g);
    }

    public void paintEditing(Graphics2D g) {
        if (jIsPathEditing()) {
            g.setStroke(standardStroke);

            int d = 7;
            int d2 = d / 2;
            int smallD = 5;

            Point point;
            for (int i = 0; i < points.size(); i++) {
                PathPoint p = points.get(i);
                point = p.p;

                if (i == 0) {
                    g.setColor(Color.RED);

                    g.fillOval((int) (point.x * zoomX) - d2, (int) (point.y * zoomY) - d2, d, d);
                    g.setColor(Color.BLACK);
                    g.drawOval((int) (point.x * zoomX) - d2, (int) (point.y * zoomX) - d2, d, d);
                } else if (i == points.size() - 1) {
                    g.setColor(Color.GREEN);
                    g.fillOval((int) (point.x * zoomX) - d2, (int) (point.y * zoomY) - d2, d, d);
                    g.setColor(Color.BLACK);
                    g.drawOval((int) (point.x * zoomX) - d2, (int) (point.y * zoomY) - d2, d, d);
                } else {
                    g.setColor(Color.RED);
                    g.drawOval((int) (point.x * zoomX) - d2, (int) (point.y * zoomY) - d2, d, d);
                }
                if (p.commando.equalsIgnoreCase("CURVETO")) {

                    g.setColor(Color.RED);

                    g.drawOval((int) (p.p1.x * zoomX) - smallD / 2, (int) (p.p1.y * zoomY) - smallD / 2, smallD, smallD);
                    g.drawOval((int) (p.p2.x * zoomX) - smallD / 2, (int) (p.p2.y * zoomY) - smallD / 2, smallD, smallD);

                    g.drawLine((int) (p.p.x * zoomX), (int) (p.p.y * zoomY), (int) (p.p1.x * zoomX), (int) (p.p1.y * zoomY));
                    g.drawLine((int) (p.p.x * zoomX), (int) (p.p.y * zoomY), (int) (p.p2.x * zoomX), (int) (p.p2.y * zoomY));
                }
            }

        }

    }

    public synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (owner.owner.vmProtected) {
            if (owner.owner.getCircuitBasis() == owner) {
                return;
            }
        }
        /* if (simplePaintModus)
         {
         setBackground(Color.RED);
         }else*/
        {
            mypaint((Graphics2D) g);
        }

        paintEditing((Graphics2D) g);

        /*g.setColor(Color.RED);
         g.fillRect(0,0,20,20);
         g.setColor(Color.BLACK);
         //int index=owner.getComponentZOrder(this);
         g.drawString(""+getID(),3,10);*/
    }

    //public void ch
    protected void processComponentEvent(ComponentEvent co) {
        if (co.getID() == ComponentEvent.COMPONENT_SHOWN) {
            requestFocus();
        }

        super.processComponentEvent(co);
    }

    // with that Method you can set Focus when
    // the you clicked with Mouse into Canvas!
    protected void processMouseEvent(MouseEvent me) {

        switch (me.getID()) {
            case MouseEvent.MOUSE_PRESSED:
                requestFocus();
        }
        super.processMouseEvent(me);
    }

    // with that Method you can handle your Keys
    protected void processKeyEvent(KeyEvent ke) {
        owner.processKeyEvent(ke);
        //super.processKeyEvent(ke);
    }

    // with that Method you can handle the Focus
    protected void processFocusEvent(FocusEvent fe) {

        switch (fe.getID()) {
            case FocusEvent.FOCUS_GAINED:
                break;

            case FocusEvent.FOCUS_LOST:
                break;
        }
        //repaint();

        super.processFocusEvent(fe);
    }

    public int compareTo(Object o) {
        Element element = (Element) o;
        if (element.getY() > this.getY()) {
            return -1;
        }
        if (element.getY() < this.getY()) {
            return +1;
        }
        if (element.getY() == this.getY()) {
            return 0;
        }
        return 0;
    }

    public void jSetVisible(boolean value) {
        setVisible(value);
    }
}
