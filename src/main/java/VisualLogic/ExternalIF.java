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
import Peditor.PropertyEditor;
import VisualLogic.variables.*;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
/**
 *
 * @author Carmelo Salafia cswi@gmx.de
 * @version 3.0
 */
public interface ExternalIF
{
    public static final int C_VARIANT         = 0;     // kann an alle anderen Pins gestoepselt werden!
    public static final int C_GROUP           = 1;
    public static final int C_BOOLEAN         = 2;
    public static final int C_INTEGER         = 3;
    public static final int C_DOUBLE          = 4;
    public static final int C_STRING          = 5;
    public static final int C_BYTE            = 6;
    public static final int C_ARRAY1D_BOOLEAN = 7;
    public static final int C_ARRAY1D_INTEGER = 8;
    public static final int C_ARRAY1D_DOUBLE  = 9;
    public static final int C_ARRAY1D_STRING  = 10;
    public static final int C_ARRAY2D_BYTE    = 11;
    public static final int C_ARRAY2D_BOOLEAN = 12;
    public static final int C_ARRAY2D_INTEGER = 13;
    public static final int C_ARRAY2D_DOUBLE  = 14;
    public static final int C_ARRAY2D_STRING  = 15;
    public static final int C_COLOR           = 16;
    public static final int C_IMAGE           = 17;
    public static final int C_ARRAY1D_BYTE    = 18;
    public static final int C_CANVAS          = 19;
    public static final int C_FONT            = 20;
    public static final int C_FLOWINFO        = 21;
    public static final int C_OBJECT          = 22;
    
    public static final int C_SHAPE_POINTS      = 0;
    public static final int C_SHAPE_RECTANGLE   = 1;
    public static final int C_SHAPE_ELLIPSE     = 2;
    public static final int C_SHAPE_LINE        = 3;
    public static final int C_SHAPE_STRING      = 4;
    public static final int C_SHAPE_IMAGE       = 5;
    
    public static final byte PIN_INPUT=1;
    public static final byte PIN_OUTPUT=2;
    public static final byte PIN_INPUT_OUTPUT=3;
    
    public abstract GeneralPath jParsePath();
    
    public abstract void jGiveMouseEventsTo(ExternalIF element);
    
    public abstract void jConsolePrintln(String value);
    public abstract void setCursor(int cursor);
    
    public abstract boolean jIsPathClosed();
    public abstract String jGetProperty(String labelName);
    
    public abstract void jSetClosePath(boolean value);
    
    public abstract MyOpenLabDriverIF jOpenDriver(String driverName, ArrayList args);
    public abstract void jCloseDriver(String driverName);
    
    public abstract void jSetTag(int index, Object tag)    ;
    public abstract Object jGetTag(int index);
    
    public abstract ArrayList<PathPoint> jGetPointList();
    
    public abstract String jIfPathNotFoundThenSearch(String path);
    
    public abstract void jSetProperties();
    public abstract void processPropertyEditor();
    public abstract void jFixElement();
    public abstract VSObject[] jGetProperties();
    public abstract VSBasisIF jGetElementBasis();
    public abstract VSBasisIF jGetBasis();
    public abstract void jInitBasis(VSBasisIF basis);
    public abstract ExternalIF[] getInputPinList(VSBasisIF basis);
    public abstract ExternalIF[] getOutputPinList(VSBasisIF basis);
    public abstract ExternalIF getElementByhName(VSBasisIF basis, String name);
    
    public abstract void jClearPE();
    public VMObject getElementOwner();
    public abstract void jAddPEItem(String label,Object referenz, double min, double max);
    public abstract void jAddPEItem(String label,Object referenz, double min, double max, boolean editable);
    public abstract void jSetPEItemLocale(int index, String language, String translation);
    public abstract JFrame jGetFrame();
    public abstract String[] jGetDataTypeList();
    public abstract int jGetDataType(String value);
    public abstract int jGetDataType(VSObject obj);
    
    public abstract void jNotifyMeForClock();
    
    public abstract void jClearMenuItems();
    public abstract void jAddMenuItem(JMenuItem item);
    
    public abstract void jOpenVM(String filename);
    
    public abstract String jMapFile(String filename);
    
    public abstract boolean jIsPathEditing();
    
    public abstract void jShowFrontPanel(boolean modal);
    public abstract void jCloseFrontPanel();
    
    public abstract String jGetProjectPath();
    public abstract String jGetProjectPathFromProject();
    
    public abstract void jAddJButtonToButtonGroup(AbstractButton button, int group);
    
    public abstract int jGetID();
    
    public abstract void jProcess();
    
    public abstract JPanel getFrontPanel();
  /* Setzt den Pin implements Eingabe/Ausgabe-Modus
   **/
    public abstract void jSetPinIO(int pinIndex, byte io);
    public abstract void jSetPinVisible(int pinIndex, boolean visible);
    
    public abstract byte jGetPinIO(int pinIndex);
    
    
    public abstract Object jGetTag();
    public abstract void jSetTag(Object tag);
    
    public abstract void setAlwaysOnTop(boolean value);
    public abstract java.awt.Rectangle jGetBounds();
    
    public abstract void addToProcesslist(VSObject out);
    public abstract void jNotifyWhenDestCalled(int pinIndex, ExternalIF element);
    public abstract void notifyPinAfter(int pinIndex);
    public abstract void notifyPin(int pinIndex);
    public abstract void jProcessAutomatic(boolean value);
    
  /*
   * Erzwings das Neuzeichnen des Elements
   */
    public abstract void jRepaint();
    
    
    public abstract void jException(String text);
    public abstract void jShowMessage(String text);
    public abstract void jPrintln(String text);
    public abstract void jPrint(String text);
    
    public abstract void jRefreshVM();
    
  /* Setzt die Anzahl der Pins fuer die jeweilige Region
   * Top-Right-Bottom-Left
   * Die Pins werden in dieser reihenfolge erzeugt
   * und bekommen ein entsprechendes index
   */
    public abstract void jSetTopPins(int value);
    public abstract void jSetRightPins(int value);
    public abstract void jSetBottomPins(int value);
    public abstract void jSetLeftPins(int value);
    
    public abstract void jSetChanged(VSObject obj);
    
    public abstract void jSetNamePosition(int position);
    public abstract void jSetDescription(int yDistance,String desc);
    public abstract void jSetPinDescription(int pin, String description);
    
    
    public abstract void jSetName(String name);
    public abstract void jSetNameLocalized(String label);
    public abstract void jSetDocFilePath(String path);
    
    public abstract String jGetCaption();
    public abstract void jSetCaption(String caption);
    public abstract void jSetCaptionVisible(boolean value);
    
    public abstract void jSetVisible(boolean value);
    
    public abstract void jLoadProperties();
    
    public abstract void jSetLeftPinsVisible(boolean bVal);
    public abstract void jSetTopPinsVisible(boolean bVal);
    public abstract void jSetRightPinsVisible(boolean bVal);
    public abstract void jSetBottomPinsVisible(boolean bVal);
    
    public abstract void jSetBorderVisibility(boolean visible);
    
    public abstract VSObject jCreatePinDataType(int pinIndex);
    
    public abstract String jGetSourcePath();
    public abstract String jGetElementPath();
    
    public abstract void jSetRasterized(boolean value);
    public abstract void jSetResizable(boolean value);
    public abstract void jNameVisible(boolean value);
    
    public abstract VSBasisIF jCreateBasis();
    public abstract VSBasisIF jAssignBasis();
    
    public abstract int jIsPinIO(int pinIndex);
    
    public abstract Image jLoadImage(String fileName);
    
    public abstract void jSetPointPin(int pin);
    
    public abstract int jGetAnzahlPinsLeft();
    public abstract int jGetAnzahlPinsRight();
    
    
    public abstract void resetValuesPanelElement();
    public abstract void processPanelElement();
    
    public abstract void jProcessPanel(int pinIndex, double value, Object obj);
    
    public abstract ExternalIF getCircuitElement();
    public abstract ExternalIF setPanelElement(String classFilename);
    
    public abstract ExternalIF getPanelElement();
    
    
    public abstract void jStopVM();
    
    
    public abstract void jInitPins();
    public abstract void jResizeRightPins(int newAnzahlPins);
    
  /*
   * setzt die Groesse des Elements
   */
    public abstract void jSetSize(int width,int height);
    public abstract void jSetLeft(int value);
    public abstract void jSetTop(int value);
    public abstract int jGetLeft();
    public abstract void jSetMinimumSize(int width, int height);
    
    
    
  /* liefert die aktuelle Breite des Elements
   */
    public abstract int jGetWidth();
    public abstract void jSetRepaintDifference(int diff);
    
    
    public abstract void jSetInfo(String programmer, String copyrights, String other);
    
    
  /* liefert die aktuelle Hoehe des Elements
   */
    public abstract int jGetHeight();
    
    public abstract Point jGetLocation();
    
    public abstract void jAddSubElement(ElementIF subElement);
    public abstract void jAddSubElement2(JPanel panel);
    public abstract void jDeleteSubElement(int index);
    public abstract void jSetSubElementPosition(int index,int x, int y);
    public abstract void jSetSubElementSize(int index,int width,int height);
    public abstract void jSetSubElementVisible(int index,boolean value);
    public abstract int jGetSubElementCount();
    public abstract ElementIF jGetSubElement(int index);
    
    
    // Loescht alle Sub Elemente
    public abstract void jClearSubElements();
    
  /* wenn auf True wird die Breite und die Hoehe gleich beim Resizing
   * des Elements
   */
    public abstract void jSetResizeSynchron(boolean value);
    public abstract void jSetAspectRatio(double value);
    
  /*
   * gibt an ob der innere Rahmen abgezeigt werden soll
   */
    public abstract void jSetInnerBorderVisibility(boolean visible);
    
  /* Events fuer die Processverarbeitung
   * Liefert das Pin das die Information versenden moechte
   * Dieser Event wird intern vom Object aufgerufen!
   */
    //public abstract void onEventProceded(int pin);
    
    public abstract void Change(int pinIndex, Object value);
    
  /*
   * Setzt den Datentyp fuer den jeweiligen Pin
   */
    public abstract int jGetPinDrahtSourceDataType(int pinIndex);
    public abstract void jSetPinDataType(int pinIndex,int dataType);
    public abstract int jGetPinDataType(int pinIndex);
    public abstract Color jGetDTColor(int dataType);
    public abstract boolean hasPinWire(int pinIndex);
    
  /*
   * beschreibt ein Pin
   */
    public abstract void writePin(int index,double value);
    public abstract void writePinObj(int index,Object o);
    public abstract void writePinBoolean(int index,boolean value);
    public abstract void writePinInteger(int index,int value);
    public abstract void writePinDouble(int index,double value);
    public abstract void writePinString(int index,String value);
    
    
   /* liefert eine Referenz auf das Object des jeweiligen Pin
    */
    public abstract Object getPinInputReference(int pinIndex)   ;
    
   /* setzt eine Referenz im jeweiligen Pin
    */
    public abstract void setPinOutputReference(int pinIndex, Object referenz);
    
    
    
    
    /**
     * liest den double-Wert eines Pins
     * Diese Methode wird nicht mehr verwendet!
     * @param index Pin Index
     * @return Nothing
     * @deprecated
     */
    public abstract double readPin(int index);
    /**
     *
     * @param index
     * @return
     */
    public abstract Object readPinObj(int index);
    public abstract boolean readPinBoolean(int index);
    public abstract int readPinInteger(int index);
    public abstract double readPinDouble(int index);
    public abstract String readPinString(int index);
}
