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

import java.awt.event.*;
import VisualLogic.*;
import java.awt.*;

public class StatusAddElement extends Object implements StatusBasisIF {

    private VMObject vmobject;
    private String mainPath;
    private String binPath;
    private String circuitClass;
    private String panelClass;
    private Element element = null;
    private boolean first = true;
    private int startX,  startY;
    private int dummyWidth;
    private int dummyHeight;
    private Element dummyElement = null;

    public StatusAddElement(VMObject vmobject, String mainPath, String circuitClass, String panelClass, String[] args)
    {
        String binPath = "bin/";
        String pfad = mainPath + "/" + binPath;

        this.vmobject = vmobject;

        this.mainPath = mainPath;
        this.binPath = mainPath;
        this.circuitClass = circuitClass;
        this.panelClass = panelClass;

        dummyElement = vmobject.AddDualElement(mainPath, binPath, circuitClass, panelClass, args);
        dummyElement.setSelected(true);
        dummyWidth = dummyElement.getWidth();
        dummyHeight = dummyElement.getHeight();
        vmobject.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        dummyElement.processPropertyEditor();

        first = true;
    }

    private void appendMCUFlowChartElementTo(Element element, Element toAppend)
    {
        int pinsBottom = element.getPinsBottom();
        int pinsTop = toAppend.getPinsTop();

        if (pinsBottom == 1 && pinsTop == 1)
        {
            int pinBottomNr = element.getPinsTop() + element.getPinsRight() + element.getPinsBottom() - 1;
            int pinTopNr = toAppend.getPinsTop() - 1;

            if (element.getPin(pinBottomNr).draht != null)
            {
                // Element dazwischen einfügen
                
                // Alte Drähte löschen                
                Draht altDraht = element.getPin(pinBottomNr).draht;
                
                Element elementUnten=vmobject.getElementWithID(altDraht.getDestElementID());               
                //JPin pinUnten=elementUnten.getPin(altDraht.getDestPin());                
                
                vmobject.deleteDraht(altDraht);                               
                
                Draht draht1 = vmobject.addDrahtIntoCanvas(element.getID(), pinBottomNr, toAppend.getID(), pinTopNr);
                                
                element.getPin(pinBottomNr).draht = draht1;
                toAppend.getPin(pinTopNr).draht = draht1;

                draht1.addPoint(0, element.getY() + element.getHeight());
                draht1.addPoint(0, element.getY() + element.getHeight() + 10);
                draht1.addPoint(0, element.getY() + element.getHeight() + 10);
                draht1.addPoint(0, 0);               
                
                int pinBottomNrDummyElement = dummyElement.getPinsTop() + dummyElement.getPinsRight() + dummyElement.getPinsBottom() - 1;                
                int pinTopNrelementUnten = elementUnten.getPinsTop() - 1;
                
                Draht draht2 = vmobject.addDrahtIntoCanvas(dummyElement.getID(), pinBottomNrDummyElement, elementUnten.getID(), pinTopNrelementUnten);                
                
                dummyElement.getPin(pinBottomNrDummyElement).draht = draht2;
                elementUnten.getPin(pinTopNrelementUnten).draht = draht2;

                draht2.addPoint(0, dummyElement.getY() + dummyElement.getHeight());
                draht2.addPoint(0, dummyElement.getY() + dummyElement.getHeight() + 10);
                draht2.addPoint(0, dummyElement.getY() + dummyElement.getHeight() + 10);
                draht2.addPoint(0, 0);               
                
                
            } else
            {
                Draht draht1 = vmobject.addDrahtIntoCanvas(element.getID(), pinBottomNr, toAppend.getID(), pinTopNr);

                element.getPin(pinBottomNr).draht = draht1;
                toAppend.getPin(pinTopNr).draht = draht1;

                draht1.addPoint(0, element.getY() + element.getHeight());
                draht1.addPoint(0, element.getY() + element.getHeight() + 10);
                draht1.addPoint(0, element.getY() + element.getHeight() + 10);
                draht1.addPoint(0, 0);
            }
        }



    /*leftDraht = vmobject.addDrahtIntoCanvas(draht.getSourceElementID(), draht.getSourcePin(), node.getID(), 1);
    rightDraht = vmobject.addDrahtIntoCanvas(node.getID(), 3, draht.getDestElementID(), draht.getDestPin());
    node.getPin(3).draht = rightDraht;
    node.getPin(1).draht = leftDraht;
    node.getPin(1).pinIO = JPin.PIN_INPUT;
    node.getPin(3).pinIO = JPin.PIN_OUTPUT;*/

    }

    public void mousePressed(MouseEvent e)
    {
        int x = e.getX();
        int y = e.getY();

        if (e.getSource() instanceof SelectionPane)
        {
            SelectionPane pane = (SelectionPane) e.getSource();
            Element el = pane.getElement();


            vmobject.owner.frameCircuit.activate_DocFrame(el);

            x = el.getX() + e.getX();
            y = el.getY() + e.getY();
        }

        vmobject.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        if (e.getButton() == MouseEvent.BUTTON3)
        {

            vmobject.deleteElement(dummyElement);
            vmobject.setModusIdle();
            vmobject.processPropertyEditor();
            vmobject.updateUI();

        } else
        {
            vmobject.setModusIdle();
            dummyElement.processPropertyEditor();
            try
            {
                if (dummyElement.classRef != null)
                {
                    dummyElement.classRef.checkPinDataType();
                }
            } catch (Exception ex)
            {

            }

            // Ermittle ob es sich um ein MCU_FLOWCHART Element handelt und 
            // ob die Koordinaten sich unterhalb eines Start Elements befinden

            // Ist es ein MCU-FLOWCHART Element?
            /*if (dummyElement.getInternName().indexOf("#MCU-FLOWCHART") > -1)
            {
                // Ist das dummyElement unter ein MCU_FLOWCHART-START ELment?
                VMObject vm = vmobject;
                for (int i = 0; i < vm.getElementCount(); i++)
                {
                    Element startElement = vm.getElement(i);

                    if (startElement.getInternName().indexOf("#MCU-FLOWCHART-START#") > -1)
                    {
                        int x1 = dummyElement.getX() + (dummyElement.getWidth() / 2);
                        int x2 = startElement.getX();
                        int x3 = startElement.getX() + startElement.getWidth();

                        int y1 = dummyElement.getY();
                        int y2 = startElement.getY() - 10;
                        
                        if (x1 >= x2 && y1 > y2 && x1 <= x3)
                        {
                            Element elementOben = getNearstElementInMouseExcludeElement(x, y, 100, y1,dummyElement);

                            if (elementOben != null)
                            {
                                appendMCUFlowChartElementTo(elementOben, dummyElement);
                                vm.reorderWireFrames();                                
                            }
                        }
                    }
                }
            }*/
            vmobject.owner.disableAllElements();
            vmobject.owner.saveForUndoRedo();
            dummyElement.setSelected(true);
        }
        vmobject.newElement = null;
    }

    public Element getNearstElementInMouseExcludeElement(int x, int y, int d, int abY, Element elementToExclude)
    {
        Element elementX;

        int dx, dy;
        int vectorDistance = 0;
        int minDitance = 99999999;

        Element minElement = null;

        for (int i = 0; i < vmobject.getElementCount(); i++)
        {
            elementX = vmobject.getElement(i);
            if (elementX != elementToExclude && elementX.getY()<abY)
            {
                Point mp = elementX.getMittelpunkt();

                dx = Math.abs(x - mp.x);
                dy = Math.abs(y - mp.y);

                vectorDistance = (int) Math.sqrt((dx * dx) + (dy * dy));
                double w2 = elementX.getWidth() / 2;
                int radius = (int) Math.sqrt(w2 * w2 + w2 * w2);


                if (vectorDistance < (d + radius) && vectorDistance < minDitance)
                {
                    minDitance = vectorDistance;
                    minElement = elementX;
                }
            }
        }

        if (minElement != null)
        {
            return minElement;
        }
        return null;
    }    
    
    
    public void mouseMoved(MouseEvent e)
    {
        try
        {
            int w = dummyWidth;
            int h = dummyHeight;
            int w2 = w / 2;
            int h2 = h / 2;

            int x = 0;
            int y = 0;

            if (e.getSource() instanceof SelectionPane)
            {
                x = e.getX();
                y = e.getY();

                SelectionPane pane = (SelectionPane) e.getSource();
                Element element = pane.getElement();

                x = element.getX() + e.getX();
                y = element.getY() + e.getY();

            } else
            {

                if (vmobject != null)
                {
                    try
                    {
                        x = vmobject.getMousePosition().x;
                        y = vmobject.getMousePosition().y;
                    } catch (Exception ex)
                    {

                    }
                }
            }


            int x1 = x - w2;
            int y1 = y - h2;

            Point p = vmobject.pointToRaster(dummyElement, x1, y1);

            dummyElement.setLocation(p.x, p.y);


        } catch (Exception ex)
        {
            vmobject.owner.showErrorMessage(ex.toString());
            vmobject.setModusIdle();
        }


    }

    public void mouseDblClick(MouseEvent e)
    {
    }

    public void elementPinMousePressed(MouseEvent e, int elementID, int pin)
    {
    }

    public void elementPinMouseMoved(MouseEvent e, int elementID, int pin)
    {
    }

    public void elementPinMouseReleased(MouseEvent e, int elementID, int pin)
    {
    }

    public void processKeyEvent(KeyEvent ke)
    {
    }

    public void mouseDragged(MouseEvent e)
    {
    }

    public void mouseReleased(MouseEvent e)
    {
    }

    public void mouseClicked(MouseEvent e)
    {
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }

    public void draw(java.awt.Graphics g)
    {
    }
}
/*package BasisStatus;
import VisualLogic.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.SwingWorker;
public class StatusAddElement extends Object implements StatusBasisIF
{
private VMObject vmobject;      
private String mainPath;
private String binPath;
private String circuitClass;    
private String panelClass;
private String[] args;
private Element element=null;       
private boolean first=true;    
private int startX,startY;            
private int dummyWidth;
private int dummyHeight;   
private DummyElement dummyElement=null;
public StatusAddElement(VMObject vmobject,String mainPath, String circuitClass, String panelClass,  String[] args)
{        
String pfad=mainPath+"/"+binPath;
this.vmobject=vmobject;
this.mainPath=mainPath;
this.binPath=mainPath;
this.circuitClass=circuitClass;
this.panelClass=panelClass;  
this.args=args;
vmobject.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)); 
dummyElement= new DummyElement();
dummyWidth=0;
dummyHeight=0;        
dummyElement.setSize(50,50);
vmobject.add(dummyElement,0);
first=true;
}
public void mousePressed(MouseEvent e)
{
int x=e.getX();
int y=e.getY();
if (e.getSource() instanceof SelectionPane )
{
SelectionPane pane = (SelectionPane)e.getSource();
Element element=pane.getElement();
//vmobject.owner.frameCircuit.activate_DocFrame(element);
x=element.getX()+e.getX();
y=element.getY()+e.getY();
}
vmobject.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));  
if (e.getButton()==e.BUTTON3)
{               
vmobject.remove(dummyElement);
//vmobject.deleteElement(dummyElement);
vmobject.setModusIdle();             
vmobject.processPropertyEditor();
vmobject.updateUI();
}else
{      
SwingWorker worker = new SwingWorker<Object, Object>() 
{                      
public Object doInBackground() 
{ 
Tools.dialogWait = new DialogWait();
Tools.dialogWait.setVisible(true);
Tools.dialogWait.setLocation(Tools.dialogWait.getLocation().x, Tools.dialogWait.getLocation().y-150);
addElement();
return null;
} 
protected void done() 
{                    
vmobject.owner.propertyEditor.locked=false;
if (element!=null)element.processPropertyEditor();
Tools.dialogWait.dispose();
vmobject.setModusIdle();
}
};
worker.execute();
}
vmobject.newElement=null;
}
public void addElement()
{
Point p=dummyElement.getLocation();
vmobject.remove(dummyElement);
vmobject.owner.propertyEditor.vmobject=null;
vmobject.owner.propertyEditor.locked=true;
String binPath="bin/";
Element element=vmobject.AddDualElement(mainPath,binPath, circuitClass, panelClass, args);
this.element=element;
element.setLocation(p);  
try
{
if (element.classRef!=null)
{
element.classRef.checkPinDataType();
}
} catch(Exception ex)
{
}
vmobject.owner.disableAllElements();
vmobject.owner.saveForUndoRedo();
element.setSelected(true); 
}
public void mouseMoved(MouseEvent e)
{        
try
{
int w=dummyWidth;
int h=dummyHeight;
int w2=w/2;
int h2=h/2;
int x=0;
int y=0;
if (e.getSource() instanceof SelectionPane )
{
x=e.getX();
y=e.getY();        
SelectionPane pane = (SelectionPane)e.getSource();
Element element=pane.getElement();                                
x=element.getX()+e.getX();
y=element.getY()+e.getY();                                        
}else
{
if (vmobject!=null )
{
try
{
x=vmobject.getMousePosition().x;
y=vmobject.getMousePosition().y;
} catch(Exception ex)
{
}
}
}
int x1=x-w2;
int y1=y-h2;            
Point p=vmobject.pointToRaster(x1,y1);
dummyElement.setLocation(p.x,p.y);
} catch (Exception ex)
{
vmobject.owner.showErrorMessage(ex.toString());
vmobject.setModusIdle();
}
}
public void mouseDblClick(MouseEvent e){}
public  void elementPinMousePressed(MouseEvent e, int elementID,int pin){}
public void elementPinMouseMoved(MouseEvent e, int elementID,int pin){}
public void elementPinMouseReleased(MouseEvent e, int elementID,int pin){}
public void processKeyEvent(KeyEvent ke){}
public void mouseDragged(MouseEvent e){}    
public void mouseReleased(MouseEvent e){}
public void mouseClicked(MouseEvent e){}    
public void mouseEntered(MouseEvent e){}
public void mouseExited(MouseEvent e){}    
public void draw(java.awt.Graphics g){}
}
 */
