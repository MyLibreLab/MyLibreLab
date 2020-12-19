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

import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import VisualLogic.variables.VSFlowInfo;

public interface ElementIF {

    void setPropertyEditor();

    // Die Methode wird aufgerufen wenn das Objekt zu zeichnen ist
    void xpaint(java.awt.Graphics g);

    // Die Methode wird aufgerufen wenn das Objekt neu erzeugt wird
    // und gibt die Schnittstelle zum internen Element an
    // mitdem man das interne Element ansteuern kann
    void beforeInit(String[] args);

    void xsetExternalIF(ExternalIF externalIF);

    void propertyChanged(Object o);

    void xonChangeElement();

    void onDispose();

    void xOnInit();

    void changePin(int pinIndex, Object value);

    String getBinDir();

    String jGetVMFilename(); // Nur Sub-VM's!

    // nachdem alle Elemente mit xonProcess() aufgerufen worden sind
    // wird es zeit das alle Variablen den Elements auf setChanged(false)
    // gesetzt werden!
    void resetValues();

    // Die Methode wird aufgerufen wenn die Maus auf dem Element gedrueckt worden ist
    void xonMousePressed(MouseEvent e);

    void xonMousePressedOnIdle(MouseEvent e);

    void xonMouseDragged(MouseEvent e);

    // Die Methode wird aufgerufen wenn die Maus auf dem Element losgelassen worden ist
    void xonMouseReleased(MouseEvent e);

    // Die Methode wird aufgerufen wenn die Maus auf dem Element bewegt worden ist
    void xonMouseMoved(MouseEvent e);

    void xonInitInputPins();

    void xonInitOutputPins();

    // Die Methode wird aufgerufen wenn die Simulation gestartet wird
    void xonStart();

    // Die Methode wird aufgerufen wenn die Simulation beendet wird
    void xonStop();

    void checkPinDataType();

    // Die Methode wird aufgerufen wenn die Inputs ausgewertet werden sollen
    // und ggf. ein Output durch onEventProceded eingeleitet werden soll
    void xonProcess();

    void processMethod(VSFlowInfo flowInfo);

    void returnFromMethod(Object result);

    void xonClock(); // wird regelm��ig aufgerufen!

    void destElementCalled();

    void elementActionPerformed(ElementActionEvent evt);

    // ruft das Eigenschaftsfenster im Externen Element
    void xopenPropertyDialog();

    void xSaveToStream(FileOutputStream fos);

    void xLoadFromStream(FileInputStream fis);

    void saveToStreamAfterXOnInit(FileOutputStream fos);

    void loadFromStreamAfterXOnInit(FileInputStream fis);

    String xgetName();
}
