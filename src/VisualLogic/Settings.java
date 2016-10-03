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

import java.awt.Dimension;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 *
 * @author Homer
 */
public class Settings {

    private boolean showDocWindow = true;
    private String currentDirectory = ".";
    private String lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
    private Point docLocation = new Point(500, 100);
    private Dimension docDimension = new Dimension(300, 250);
    private int basisWindow_LeftWidth = 120;
    private int rightSplitterPos = 150;
    private int oldRightSplitterPos = 150;
    private String documentation_language = "";
    private String elementPalettePath = "";
    private String userdefinedElementsPath = "";

    private String language = "en_US";
    private Point docWindowLocation = new Point(100, 100);
    private Dimension docWindowDimension = new Dimension(100, 100);
    private boolean isDocWindowsVisible = true;
    private ArrayList<String> projects = new ArrayList<>(); // Obsolete!

    private boolean frontAlignToGrid = true;
    private boolean frontRasterOn = true;
    private int frontRasterX = 10;
    private int frontRasterY = 10;

    private boolean circuittAlignToGrid = true;
    private boolean circuitRasterOn = true;
    private int circuitRasterX = 10;
    private int circuitRasterY = 10;

    private boolean circuitCrossVisible = true;
    private boolean elementIDVisible = false;

    //private  String panelDirectory="/FrontElements";
    //private  String circuitDirectory="/CircuitElements";
    private int status = 0;
    private Point circuitWindowLocation = new Point(0, 0);
    private Dimension circuitWindowDimension = new Dimension(640, 480);

    private String javaEditor;
    private String HTMLEditor;
    private String jdkBin = "C:\\Programme\\Java\\jdk1.5.0_09\\bin";
    private String graphicEditor = "mspaint.exe";

    private int elementSplitterHozPosition = 400;
    private int elementSplitterPosition = 200;

    private String oldProjectPath = "";

    private String version = ""; // Important to compare this Version with old Version!

    private Point mainFrameLocation = new Point();

    private Dimension mainFrameSize = new Dimension(0, 0);

    private Point elementPaletteLocation = new Point(100, 100);
    private Dimension elementPaletteSize = new Dimension(250, 300);

    private String proxy_host = "";
    private String proxy_port = "";

    private String repository_domain = "https://myopenlab.de";
    private String repository_login_username = "";
    private String repository_login_password = "";

    private String javascript_editor = "";

    /**
     * Creates a new instance of Settings
     */
    public Settings() {
    }

    /**
     * @return the showDocWindow
     */
    public boolean isShowDocWindow() {
        return showDocWindow;
    }

    /**
     * @param showDocWindow the showDocWindow to set
     */
    public void setShowDocWindow(boolean showDocWindow) {
        this.showDocWindow = showDocWindow;
    }

    /**
     * @return the currentDirectory
     */
    public String getCurrentDirectory() {
        return currentDirectory;
    }

    /**
     * @param currentDirectory the currentDirectory to set
     */
    public void setCurrentDirectory(String currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

    /**
     * @return the lookAndFeel
     */
    public String getLookAndFeel() {
        return lookAndFeel;
    }

    /**
     * @param lookAndFeel the lookAndFeel to set
     */
    public void setLookAndFeel(String lookAndFeel) {
        this.lookAndFeel = lookAndFeel;
    }

    /**
     * @return the docLocation
     */
    public Point getDocLocation() {
        return docLocation;
    }

    /**
     * @param docLocation the docLocation to set
     */
    public void setDocLocation(Point docLocation) {
        this.docLocation = docLocation;
    }

    /**
     * @return the docDimension
     */
    public Dimension getDocDimension() {
        return docDimension;
    }

    /**
     * @param docDimension the docDimension to set
     */
    public void setDocDimension(Dimension docDimension) {
        this.docDimension = docDimension;
    }

    /**
     * @return the basisWindow_LeftWidth
     */
    public int getBasisWindow_LeftWidth() {
        return basisWindow_LeftWidth;
    }

    /**
     * @param basisWindow_LeftWidth the basisWindow_LeftWidth to set
     */
    public void setBasisWindow_LeftWidth(int basisWindow_LeftWidth) {
        this.basisWindow_LeftWidth = basisWindow_LeftWidth;
    }

    /**
     * @return the rightSplitterPos
     */
    public int getRightSplitterPos() {
        return rightSplitterPos;
    }

    /**
     * @param rightSplitterPos the rightSplitterPos to set
     */
    public void setRightSplitterPos(int rightSplitterPos) {
        this.rightSplitterPos = rightSplitterPos;
    }

    /**
     * @return the documentation_language
     */
    public String getDocumentation_language() {
        return documentation_language;
    }

    /**
     * @param documentation_language the documentation_language to set
     */
    public void setDocumentation_language(String documentation_language) {
        this.documentation_language = documentation_language;
    }

    /**
     * @return the elementPalettePath
     */
    public String getElementPalettePath() {
        return elementPalettePath;
    }

    /**
     * @param elementPalettePath the elementPalettePath to set
     */
    public void setElementPalettePath(String elementPalettePath) {
        this.elementPalettePath = elementPalettePath;
    }

    /**
     * @return the userdefinedElementsPath
     */
    public String getUserdefinedElementsPath() {
        return userdefinedElementsPath;
    }

    /**
     * @param userdefinedElementsPath the userdefinedElementsPath to set
     */
    public void setUserdefinedElementsPath(String userdefinedElementsPath) {
        this.userdefinedElementsPath = userdefinedElementsPath;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the docWindowLocation
     */
    public Point getDocWindowLocation() {
        return docWindowLocation;
    }

    /**
     * @param docWindowLocation the docWindowLocation to set
     */
    public void setDocWindowLocation(Point docWindowLocation) {
        this.docWindowLocation = docWindowLocation;
    }

    /**
     * @return the docWindowDimension
     */
    public Dimension getDocWindowDimension() {
        return docWindowDimension;
    }

    /**
     * @param docWindowDimension the docWindowDimension to set
     */
    public void setDocWindowDimension(Dimension docWindowDimension) {
        this.docWindowDimension = docWindowDimension;
    }

    /**
     * @return the isDocWindowsVisible
     */
    public boolean isIsDocWindowsVisible() {
        return isDocWindowsVisible;
    }

    /**
     * @param isDocWindowsVisible the isDocWindowsVisible to set
     */
    public void setIsDocWindowsVisible(boolean isDocWindowsVisible) {
        this.isDocWindowsVisible = isDocWindowsVisible;
    }

    /**
     * @return the projects
     */
    public ArrayList<String> getProjects() {
        return projects;
    }

    /**
     * @param projects the projects to set
     */
    public void setProjects(ArrayList<String> projects) {
        this.projects = projects;
    }

    /**
     * @return the frontAlignToGrid
     */
    public boolean isFrontAlignToGrid() {
        return frontAlignToGrid;
    }

    /**
     * @param frontAlignToGrid the frontAlignToGrid to set
     */
    public void setFrontAlignToGrid(boolean frontAlignToGrid) {
        this.frontAlignToGrid = frontAlignToGrid;
    }

    /**
     * @return the frontRasterOn
     */
    public boolean isFrontRasterOn() {
        return frontRasterOn;
    }

    /**
     * @param frontRasterOn the frontRasterOn to set
     */
    public void setFrontRasterOn(boolean frontRasterOn) {
        this.frontRasterOn = frontRasterOn;
    }

    /**
     * @return the frontRasterX
     */
    public int getFrontRasterX() {
        return frontRasterX;
    }

    /**
     * @param frontRasterX the frontRasterX to set
     */
    public void setFrontRasterX(int frontRasterX) {
        this.frontRasterX = frontRasterX;
    }

    /**
     * @return the frontRasterY
     */
    public int getFrontRasterY() {
        return frontRasterY;
    }

    /**
     * @param frontRasterY the frontRasterY to set
     */
    public void setFrontRasterY(int frontRasterY) {
        this.frontRasterY = frontRasterY;
    }

    /**
     * @return the circuittAlignToGrid
     */
    public boolean isCircuittAlignToGrid() {
        return circuittAlignToGrid;
    }

    /**
     * @param circuittAlignToGrid the circuittAlignToGrid to set
     */
    public void setCircuittAlignToGrid(boolean circuittAlignToGrid) {
        this.circuittAlignToGrid = circuittAlignToGrid;
    }

    /**
     * @return the circuitRasterOn
     */
    public boolean isCircuitRasterOn() {
        return circuitRasterOn;
    }

    /**
     * @param circuitRasterOn the circuitRasterOn to set
     */
    public void setCircuitRasterOn(boolean circuitRasterOn) {
        this.circuitRasterOn = circuitRasterOn;
    }

    /**
     * @return the circuitRasterX
     */
    public int getCircuitRasterX() {
        return circuitRasterX;
    }

    /**
     * @param circuitRasterX the circuitRasterX to set
     */
    public void setCircuitRasterX(int circuitRasterX) {
        this.circuitRasterX = circuitRasterX;
    }

    /**
     * @return the circuitRasterY
     */
    public int getCircuitRasterY() {
        return circuitRasterY;
    }

    /**
     * @param circuitRasterY the circuitRasterY to set
     */
    public void setCircuitRasterY(int circuitRasterY) {
        this.circuitRasterY = circuitRasterY;
    }

    /**
     * @return the circuitCrossVisible
     */
    public boolean isCircuitCrossVisible() {
        return circuitCrossVisible;
    }

    /**
     * @param circuitCrossVisible the circuitCrossVisible to set
     */
    public void setCircuitCrossVisible(boolean circuitCrossVisible) {
        this.circuitCrossVisible = circuitCrossVisible;
    }

    /**
     * @return the elementIDVisible
     */
    public boolean isElementIDVisible() {
        return elementIDVisible;
    }

    /**
     * @param elementIDVisible the elementIDVisible to set
     */
    public void setElementIDVisible(boolean elementIDVisible) {
        this.elementIDVisible = elementIDVisible;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the circuitWindowLocation
     */
    public Point getCircuitWindowLocation() {
        return circuitWindowLocation;
    }

    /**
     * @param circuitWindowLocation the circuitWindowLocation to set
     */
    public void setCircuitWindowLocation(Point circuitWindowLocation) {
        this.circuitWindowLocation = circuitWindowLocation;
    }

    /**
     * @return the circuitWindowDimension
     */
    public Dimension getCircuitWindowDimension() {
        return circuitWindowDimension;
    }

    /**
     * @param circuitWindowDimension the circuitWindowDimension to set
     */
    public void setCircuitWindowDimension(Dimension circuitWindowDimension) {
        this.circuitWindowDimension = circuitWindowDimension;
    }

    /**
     * @return the javaEditor
     */
    public String getJavaEditor() {
        return javaEditor;
    }

    /**
     * @param javaEditor the javaEditor to set
     */
    public void setJavaEditor(String javaEditor) {
        this.javaEditor = javaEditor;
    }

    /**
     * @return the HTMLEditor
     */
    public String getHTMLEditor() {
        return HTMLEditor;
    }

    /**
     * @param HTMLEditor the HTMLEditor to set
     */
    public void setHTMLEditor(String HTMLEditor) {
        this.HTMLEditor = HTMLEditor;
    }

    /**
     * @return the jdkBin
     */
    public String getJdkBin() {
        return jdkBin;
    }

    /**
     * @param jdkBin the jdkBin to set
     */
    public void setJdkBin(String jdkBin) {
        this.jdkBin = jdkBin;
    }

    /**
     * @return the graphicEditor
     */
    public String getGraphicEditor() {
        return graphicEditor;
    }

    /**
     * @param graphicEditor the graphicEditor to set
     */
    public void setGraphicEditor(String graphicEditor) {
        this.graphicEditor = graphicEditor;
    }

    /**
     * @return the elementSplitterHozPosition
     */
    public int getElementSplitterHozPosition() {
        return elementSplitterHozPosition;
    }

    /**
     * @param elementSplitterHozPosition the elementSplitterHozPosition to set
     */
    public void setElementSplitterHozPosition(int elementSplitterHozPosition) {
        this.elementSplitterHozPosition = elementSplitterHozPosition;
    }

    /**
     * @return the elementSplitterPosition
     */
    public int getElementSplitterPosition() {
        return elementSplitterPosition;
    }

    /**
     * @param elementSplitterPosition the elementSplitterPosition to set
     */
    public void setElementSplitterPosition(int elementSplitterPosition) {
        this.elementSplitterPosition = elementSplitterPosition;
    }

    /**
     * @return the oldProjectPath
     */
    public String getOldProjectPath() {
        return oldProjectPath;
    }

    /**
     * @param oldProjectPath the oldProjectPath to set
     */
    public void setOldProjectPath(String oldProjectPath) {
        this.oldProjectPath = oldProjectPath;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the mainFrameLocation
     */
    public Point getMainFrameLocation() {
        return mainFrameLocation;
    }

    /**
     * @param mainFrameLocation the mainFrameLocation to set
     */
    public void setMainFrameLocation(Point mainFrameLocation) {
        this.mainFrameLocation = mainFrameLocation;
    }

    /**
     * @return the mainFrameSize
     */
    public Dimension getMainFrameSize() {
        return mainFrameSize;
    }

    /**
     * @param mainFrameSize the mainFrameSize to set
     */
    public void setMainFrameSize(Dimension mainFrameSize) {
        this.mainFrameSize = mainFrameSize;
    }

    /**
     * @return the elementPaletteLocation
     */
    public Point getElementPaletteLocation() {
        return elementPaletteLocation;
    }

    /**
     * @param elementPaletteLocation the elementPaletteLocation to set
     */
    public void setElementPaletteLocation(Point elementPaletteLocation) {
        this.elementPaletteLocation = elementPaletteLocation;
    }

    /**
     * @return the elementPaletteSize
     */
    public Dimension getElementPaletteSize() {
        return elementPaletteSize;
    }

    /**
     * @param elementPaletteSize the elementPaletteSize to set
     */
    public void setElementPaletteSize(Dimension elementPaletteSize) {
        this.elementPaletteSize = elementPaletteSize;
    }

    /**
     * @return the proxy_host
     */
    public String getProxy_host() {
        return proxy_host;
    }

    /**
     * @param proxy_host the proxy_host to set
     */
    public void setProxy_host(String proxy_host) {
        this.proxy_host = proxy_host;
    }

    /**
     * @return the proxy_port
     */
    public String getProxy_port() {
        return proxy_port;
    }

    /**
     * @param proxy_port the proxy_port to set
     */
    public void setProxy_port(String proxy_port) {
        this.proxy_port = proxy_port;
    }

    /**
     * @return the repository_domain
     */
    public String getRepository_domain() {
        return repository_domain;
    }

    /**
     * @param repository_domain the repository_domain to set
     */
    public void setRepository_domain(String repository_domain) {
        this.repository_domain = repository_domain;
    }

    /**
     * @return the repository_login_username
     */
    public String getRepository_login_username() {
        return repository_login_username;
    }

    /**
     * @param repository_login_username the repository_login_username to set
     */
    public void setRepository_login_username(String repository_login_username) {
        this.repository_login_username = repository_login_username;
    }

    /**
     * @return the repository_login_password
     */
    public String getRepository_login_password() {
        return repository_login_password;
    }

    /**
     * @param repository_login_password the repository_login_password to set
     */
    public void setRepository_login_password(String repository_login_password) {
        this.repository_login_password = repository_login_password;
    }

    /**
     * @return the javascript_editor
     */
    public String getJavascript_editor() {
        return javascript_editor;
    }

    /**
     * @param javascript_editor the javascript_editor to set
     */
    public void setJavascript_editor(String javascript_editor) {
        this.javascript_editor = javascript_editor;
    }

    /**
     * @return the oldRightSplitterPos
     */
    public int getOldRightSplitterPos() {
        return oldRightSplitterPos;
    }

    /**
     * @param oldRightSplitterPos the oldRightSplitterPos to set
     */
    public void setOldRightSplitterPos(int oldRightSplitterPos) {
        this.oldRightSplitterPos = oldRightSplitterPos;
    }

}
