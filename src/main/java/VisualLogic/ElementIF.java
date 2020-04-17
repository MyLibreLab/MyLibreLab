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

import VisualLogic.variables.VSFlowInfo;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public interface ElementIF 
{    
    
    public abstract void setPropertyEditor();
    
    // Die Methode wird aufgerufen wenn das Objekt zu zeichnen ist
    public abstract void xpaint(java.awt.Graphics g); 
    
    // Die Methode wird aufgerufen wenn das Objekt neu erzeugt wird 
    // und gibt die Schnittstelle zum internen Element an 
    // mitdem man das interne Element ansteuern kann    
    public abstract void beforeInit(String[] args);
    public abstract void xsetExternalIF(ExternalIF externalIF);    
    
    public abstract void propertyChanged(Object o);
    public abstract void xonChangeElement();        
    public abstract void onDispose();    
    public abstract void xOnInit();
    public abstract void changePin(int pinIndex, Object value);
    
    
    
    public abstract String getBinDir();
    public abstract String jGetVMFilename(); // Nur Sub-VM's!
    
    // nachdem alle Elemente mit xonProcess() aufgerufen worden sind
    // wird es zeit das alle Variablen den Elements auf setChanged(false)
    // gesetzt werden!
    public abstract void resetValues();
        
    
    // Die Methode wird aufgerufen wenn die Maus auf dem Element gedrueckt worden ist
    public abstract void xonMousePressed(MouseEvent e);
    public abstract void xonMousePressedOnIdle(MouseEvent e);
    
    public abstract void xonMouseDragged(MouseEvent e);
    
    // Die Methode wird aufgerufen wenn die Maus auf dem Element losgelassen worden ist    
    public abstract void xonMouseReleased(MouseEvent e); 

    // Die Methode wird aufgerufen wenn die Maus auf dem Element bewegt worden ist
    public abstract void xonMouseMoved(MouseEvent e);
        
    public abstract void xonInitInputPins();
    public abstract void xonInitOutputPins();
    
    // Die Methode wird aufgerufen wenn die Simulation gestartet wird
    public abstract void xonStart();
    
    // Die Methode wird aufgerufen wenn die Simulation beendet wird
    public abstract void xonStop();    
        
    
    public abstract void checkPinDataType();
    
    
    // Die Methode wird aufgerufen wenn die Inputs ausgewertet werden sollen
    // und ggf. ein Output durch onEventProceded eingeleitet werden soll
    public abstract void xonProcess();  
    public abstract void processMethod(VSFlowInfo flowInfo);  
    public abstract void returnFromMethod(Object result);
    public abstract void xonClock(); // wird regelm‰ﬂig aufgerufen!
    public abstract void destElementCalled();
    public abstract void elementActionPerformed(ElementActionEvent evt);
    
    // ruft das Eigenschaftsfenster im Externen Element
    public abstract void xopenPropertyDialog();    
    
    public abstract void xSaveToStream(FileOutputStream fos);
    public abstract void xLoadFromStream(FileInputStream fis);

    public abstract void saveToStreamAfterXOnInit(FileOutputStream fos);
    public abstract void loadFromStreamAfterXOnInit(FileInputStream fis);
    
    public abstract String xgetName();
}