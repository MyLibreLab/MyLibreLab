/*
 * ElementActionEvent.java
 *
 * Created on 27. November 2006, 15:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package VisualLogic;

/**
 *
 * @author Salafia
 */
public class ElementActionEvent
{
    
    private int pinIndex=-1;
    private ExternalIF element=null;
    
    /** Creates a new instance of ElementActionEvent */
    public ElementActionEvent(int pinIndex, ExternalIF element) 
    {
        this.pinIndex=pinIndex;
        this.element=element;
    }
    
    public int getSourcePinIndex()
    {
        return pinIndex;
    }
    
    public ExternalIF getElement()
    {
        return element;
    }
    
}
