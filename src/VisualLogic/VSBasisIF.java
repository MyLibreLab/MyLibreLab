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
import VisualLogic.variables.VSObject;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Stack;
import javax.script.ScriptEngine;

public interface VSBasisIF 
{
    
    public abstract String vsGetProjectPath();
    
    public abstract void vsPaint(java.awt.Graphics g,int x,int y);
    public abstract void vsSetBackgroungColor(java.awt.Color color);
    public abstract void vsLoadFromFile(String fileName);
    public abstract void vsStart();
    public abstract void vsStop();
    public abstract void vsShow();
    public abstract void vsClose();
    public abstract void vsProcess();
    public abstract ScriptEngine vsGetEngine();
    public abstract ExternalIF vsgetOwnerElement();
    public abstract void vsMousePressed(MouseEvent e) ;
    public abstract void vsMouseReleased(MouseEvent e);
    public abstract void vsMouseMoved(MouseEvent e);
    public abstract void vsMouseDragged(MouseEvent e);
    public abstract void vsloadFromXML(org.w3c.dom.Element nodeElement);
    public abstract void vssaveToXML(org.w3c.dom.Element nodeElement);
    public abstract void vsLoadFromStream(java.io.FileInputStream fis);
    public abstract void vsSaveToStream(java.io.FileOutputStream fos);
    public abstract int vsGetCircuitPanelComponentCount();
    public abstract int vsGetFrontPanelComponentCount();
    public abstract void vsShowFrontPanelWhenStart(boolean value);
    public abstract void vsAddTestpointValue(ExternalIF element,VSObject in);
    
    public abstract void vsStartSubRoutine(ExternalIF sourceElement, String name, ArrayList paramList);
    public abstract void vsReturnSubProcedure(VSFlowInfo flowInfo);
    
    public abstract void vsStartElement(int elementID);
    public abstract boolean varNameExist(String varname);
    
    public abstract Object vsEvaluate(VSFlowInfo flowInfo,String expression);
    
    public abstract String[] vsGetVMInfo();
    
    public abstract ExternalIF[] vsGetListOfPanelElements();
    public abstract String getBasisElementVersion();
    public abstract int vsGetFrontVMPanelWidth();
    public abstract int vsGetFrontVMPanelHeight();
    public abstract int getPinsLeftCount();
    public abstract int getPinsRightCount();
    
    public abstract String[] vsGetVariablesNames();   
    public abstract Object[] vsGetVariablesValues();    
    public abstract boolean vsCopyVSObjectToVariable(Object vsobject, String varname);
    public abstract boolean vsCopyVariableToVSObject(String varname, Object vsobject);
    public abstract Object vsGetTypeOF(String strValue);
    public abstract boolean vsCompareValues(Object a, Object b, String operator);
    public abstract boolean vsCompareExpression(VSFlowInfo flowInfo,String expr);
    public abstract int vsGetVariableDT(String varname);
    public abstract void vsSetVar(String varName,Object value);
    public abstract Object vsGetVar(String varName);
    public abstract Object vsGetVar(VSFlowInfo flowInfo, String varName);
    public abstract void vsNotifyMeWhenVariableChanged(ExternalIF element, String varName);
    
    
    // Member Function for Stack Implemenetation
    
    public abstract Stack getStack();    
    
    public String getJavascriptEditor();
    
}
