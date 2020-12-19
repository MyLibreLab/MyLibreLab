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
import java.util.ArrayList;
import java.util.Stack;

import javax.script.ScriptEngine;

import VisualLogic.variables.VSFlowInfo;
import VisualLogic.variables.VSObject;

public interface VSBasisIF {

    String vsGetProjectPath();

    void vsPaint(java.awt.Graphics g, int x, int y);

    void vsSetBackgroungColor(java.awt.Color color);

    void vsLoadFromFile(String fileName);

    void vsStart();

    void vsStop();

    void vsShow();

    void vsClose();

    void vsProcess();

    ScriptEngine vsGetEngine();

    ExternalIF vsgetOwnerElement();

    void vsMousePressed(MouseEvent e);

    void vsMouseReleased(MouseEvent e);

    void vsMouseMoved(MouseEvent e);

    void vsMouseDragged(MouseEvent e);

    void vsloadFromXML(org.w3c.dom.Element nodeElement);

    void vssaveToXML(org.w3c.dom.Element nodeElement);

    void vsLoadFromStream(java.io.FileInputStream fis);

    void vsSaveToStream(java.io.FileOutputStream fos);

    int vsGetCircuitPanelComponentCount();

    int vsGetFrontPanelComponentCount();

    void vsShowFrontPanelWhenStart(boolean value);

    void vsAddTestpointValue(ExternalIF element, VSObject in);

    void vsStartSubRoutine(ExternalIF sourceElement, String name, ArrayList paramList);

    void vsReturnSubProcedure(VSFlowInfo flowInfo);

    void vsStartElement(int elementID);

    boolean varNameExist(String varname);

    Object vsEvaluate(VSFlowInfo flowInfo, String expression);

    String[] vsGetVMInfo();

    ExternalIF[] vsGetListOfPanelElements();

    String getBasisElementVersion();

    int vsGetFrontVMPanelWidth();

    int vsGetFrontVMPanelHeight();

    int getPinsLeftCount();

    int getPinsRightCount();

    String[] vsGetVariablesNames();

    Object[] vsGetVariablesValues();

    boolean vsCopyVSObjectToVariable(Object vsobject, String varname);

    boolean vsCopyVariableToVSObject(String varname, Object vsobject);

    Object vsGetTypeOF(String strValue);

    boolean vsCompareValues(Object a, Object b, String operator);

    boolean vsCompareExpression(VSFlowInfo flowInfo, String expr);

    int vsGetVariableDT(String varname);

    void vsSetVar(String varName, Object value);

    Object vsGetVar(String varName);

    Object vsGetVar(VSFlowInfo flowInfo, String varName);

    void vsNotifyMeWhenVariableChanged(ExternalIF element, String varName);

    // Member Function for Stack Implemenetation

    Stack getStack();

    String getJavascriptEditor();
}
