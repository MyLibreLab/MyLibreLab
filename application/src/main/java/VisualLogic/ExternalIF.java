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
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import VisualLogic.variables.VSObject;

/**
 * @author Carmelo Salafia cswi@gmx.de
 * @version 3.0
 */
public interface ExternalIF {
    int C_VARIANT = 0; // kann an alle anderen Pins gestoepselt werden!
    int C_GROUP = 1;
    int C_BOOLEAN = 2;
    int C_INTEGER = 3;
    int C_DOUBLE = 4;
    int C_STRING = 5;
    int C_BYTE = 6;
    int C_ARRAY1D_BOOLEAN = 7;
    int C_ARRAY1D_INTEGER = 8;
    int C_ARRAY1D_DOUBLE = 9;
    int C_ARRAY1D_STRING = 10;
    int C_ARRAY2D_BYTE = 11;
    int C_ARRAY2D_BOOLEAN = 12;
    int C_ARRAY2D_INTEGER = 13;
    int C_ARRAY2D_DOUBLE = 14;
    int C_ARRAY2D_STRING = 15;
    int C_COLOR = 16;
    int C_IMAGE = 17;
    int C_ARRAY1D_BYTE = 18;
    int C_CANVAS = 19;
    int C_FONT = 20;
    int C_FLOWINFO = 21;
    int C_OBJECT = 22;

    int C_SHAPE_POINTS = 0;
    int C_SHAPE_RECTANGLE = 1;
    int C_SHAPE_ELLIPSE = 2;
    int C_SHAPE_LINE = 3;
    int C_SHAPE_STRING = 4;
    int C_SHAPE_IMAGE = 5;

    byte PIN_INPUT = 1;
    byte PIN_OUTPUT = 2;
    byte PIN_INPUT_OUTPUT = 3;

    GeneralPath jParsePath();

    void jGiveMouseEventsTo(ExternalIF element);

    void jConsolePrintln(String value);

    void setCursor(int cursor);

    boolean jIsPathClosed();

    String jGetProperty(String labelName);

    void jSetClosePath(boolean value);

    MyOpenLabDriverIF jOpenDriver(String driverName, ArrayList args);

    void jCloseDriver(String driverName);

    void jSetTag(int index, Object tag);

    Object jGetTag(int index);

    List<PathPoint> jGetPointList();

    String jIfPathNotFoundThenSearch(String path);

    void jSetProperties();

    void processPropertyEditor();

    void jFixElement();

    VSObject[] jGetProperties();

    VSBasisIF jGetElementBasis();

    VSBasisIF jGetBasis();

    void jInitBasis(VSBasisIF basis);

    ExternalIF[] getInputPinList(VSBasisIF basis);

    ExternalIF[] getOutputPinList(VSBasisIF basis);

    ExternalIF getElementByhName(VSBasisIF basis, String name);

    void jClearPE();

    VMObject getElementOwner();

    void jAddPEItem(String label, Object referenz, double min, double max);

    void jAddPEItem(String label, Object referenz, double min, double max, boolean editable);

    void jSetPEItemLocale(int index, String language, String translation);

    JFrame jGetFrame();

    String[] jGetDataTypeList();

    int jGetDataType(String value);

    int jGetDataType(VSObject obj);

    void jNotifyMeForClock();

    void jClearMenuItems();

    void jAddMenuItem(JMenuItem item);

    void jOpenVM(String filename);

    String jMapFile(String filename);

    boolean jIsPathEditing();

    void jShowFrontPanel(boolean modal);

    void jCloseFrontPanel();

    String jGetProjectPath();

    String jGetProjectPathFromProject();

    void jAddJButtonToButtonGroup(AbstractButton button, int group);

    int jGetID();

    void jProcess();

    JPanel getFrontPanel();

    /*
     * Setzt den Pin implements Eingabe/Ausgabe-Modus
     **/
    void jSetPinIO(int pinIndex, byte io);

    void jSetPinVisible(int pinIndex, boolean visible);

    byte jGetPinIO(int pinIndex);

    Object jGetTag();

    void jSetTag(Object tag);

    void setAlwaysOnTop(boolean value);

    java.awt.Rectangle jGetBounds();

    void addToProcesslist(VSObject out);

    void jNotifyWhenDestCalled(int pinIndex, ExternalIF element);

    void notifyPinAfter(int pinIndex);

    void notifyPin(int pinIndex);

    void jProcessAutomatic(boolean value);

    /*
     * Erzwings das Neuzeichnen des Elements
     */
    void jRepaint();

    void jException(String text);

    void jShowMessage(String text);

    void jPrintln(String text);

    void jPrint(String text);

    void jRefreshVM();

    /*
     * Setzt die Anzahl der Pins fuer die jeweilige Region Top-Right-Bottom-Left Die Pins werden in
     * dieser reihenfolge erzeugt und bekommen ein entsprechendes index
     */
    void jSetTopPins(int value);

    void jSetRightPins(int value);

    void jSetBottomPins(int value);

    void jSetLeftPins(int value);

    void jSetChanged(VSObject obj);

    void jSetNamePosition(int position);

    void jSetDescription(int yDistance, String desc);

    void jSetPinDescription(int pin, String description);

    void jSetName(String name);

    void jSetNameLocalized(String label);

    void jSetDocFilePath(String path);

    String jGetCaption();

    void jSetCaption(String caption);

    void jSetCaptionVisible(boolean value);

    void jSetVisible(boolean value);

    void jLoadProperties();

    void jSetLeftPinsVisible(boolean bVal);

    void jSetTopPinsVisible(boolean bVal);

    void jSetRightPinsVisible(boolean bVal);

    void jSetBottomPinsVisible(boolean bVal);

    void jSetBorderVisibility(boolean visible);

    VSObject jCreatePinDataType(int pinIndex);

    String jGetSourcePath();

    String jGetElementPath();

    void jSetRasterized(boolean value);

    void jSetResizable(boolean value);

    void jNameVisible(boolean value);

    VSBasisIF jCreateBasis();

    VSBasisIF jAssignBasis();

    int jIsPinIO(int pinIndex);

    Image jLoadImage(String fileName);

    void jSetPointPin(int pin);

    int jGetAnzahlPinsLeft();

    int jGetAnzahlPinsRight();

    void resetValuesPanelElement();

    void processPanelElement();

    void jProcessPanel(int pinIndex, double value, Object obj);

    ExternalIF getCircuitElement();

    ExternalIF setPanelElement(String classFilename);

    ExternalIF getPanelElement();

    void jStopVM();

    void jInitPins();

    void jResizeRightPins(int newAnzahlPins);

    /*
     * setzt die Groesse des Elements
     */
    void jSetSize(int width, int height);

    void jSetLeft(int value);

    void jSetTop(int value);

    int jGetLeft();

    void jSetMinimumSize(int width, int height);

    /*
     * liefert die aktuelle Breite des Elements
     */
    int jGetWidth();

    void jSetRepaintDifference(int diff);

    void jSetInfo(String programmer, String copyrights, String other);

    /*
     * liefert die aktuelle Hoehe des Elements
     */
    int jGetHeight();

    Point jGetLocation();

    void jAddSubElement(ElementIF subElement);

    void jAddSubElement2(JPanel panel);

    void jDeleteSubElement(int index);

    void jSetSubElementPosition(int index, int x, int y);

    void jSetSubElementSize(int index, int width, int height);

    void jSetSubElementVisible(int index, boolean value);

    int jGetSubElementCount();

    ElementIF jGetSubElement(int index);

    // Loescht alle Sub Elemente
    void jClearSubElements();

    /*
     * wenn auf True wird die Breite und die Hoehe gleich beim Resizing des Elements
     */
    void jSetResizeSynchron(boolean value);

    void jSetAspectRatio(double value);

    /*
     * gibt an ob der innere Rahmen abgezeigt werden soll
     */
    void jSetInnerBorderVisibility(boolean visible);

    /*
     * Events fuer die Processverarbeitung Liefert das Pin das die Information versenden moechte Dieser
     * Event wird intern vom Object aufgerufen!
     */
    // public abstract void onEventProceded(int pin);

    void Change(int pinIndex, Object value);

    /*
     * Setzt den Datentyp fuer den jeweiligen Pin
     */
    int jGetPinDrahtSourceDataType(int pinIndex);

    void jSetPinDataType(int pinIndex, int dataType);

    int jGetPinDataType(int pinIndex);

    Color jGetDTColor(int dataType);

    boolean hasPinWire(int pinIndex);

    /*
     * beschreibt ein Pin
     */
    void writePin(int index, double value);

    void writePinObj(int index, Object o);

    void writePinBoolean(int index, boolean value);

    void writePinInteger(int index, int value);

    void writePinDouble(int index, double value);

    void writePinString(int index, String value);

    /*
     * liefert eine Referenz auf das Object des jeweiligen Pin
     */
    Object getPinInputReference(int pinIndex);

    /*
     * setzt eine Referenz im jeweiligen Pin
     */
    void setPinOutputReference(int pinIndex, Object referenz);

    /**
     * liest den double-Wert eines Pins Diese Methode wird nicht mehr verwendet!
     *
     * @param index Pin Index
     * @return Nothing
     * @deprecated
     */
    @Deprecated
    double readPin(int index);

    /**
     * @param index
     * @return
     */
    Object readPinObj(int index);

    boolean readPinBoolean(int index);

    int readPinInteger(int index);

    double readPinDouble(int index);

    String readPinString(int index);
}
