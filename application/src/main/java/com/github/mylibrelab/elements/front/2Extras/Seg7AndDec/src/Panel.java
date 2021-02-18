//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia (cswi@gmx.de)                         *
//*                                                                           *
//* This library is free software; you can redistribute it and/or modify      *
//* it under the terms of the GNU Lesser General Public License as published  *
//* by the Free Software Foundation; either version 2.1 of the License,       *
//* or (at your option) any later version.                                    *
//* http://www.gnu.org/licenses/lgpl.html                                     *
//*                                                                           *
//* This library is distributed in the hope that it will be useful,           *
//* but WITHOUTANY WARRANTY; without even the implied warranty of             *
//* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                      *
//* See the GNU Lesser General Public License for more details.               *
//*                                                                           *
//* You should have received a copy of the GNU Lesser General Public License  *
//* along with this library; if not, write to the Free Software Foundation,   *
//* Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA                  *
//*****************************************************************************


import VisualLogic.*;
import VisualLogic.variables.*;
import tools.*;

import java.awt.*;
import java.awt.event.*;
import SVGViewer.*;


public class Panel extends JVSMain implements PanelIF
{
  private SVGManager svgManager = new SVGManager();
  private SVGObject svgA;
  private SVGObject svgB;
  private SVGObject svgC;
  private SVGObject svgD;
  private SVGObject svgE;
  private SVGObject svgF;
  private SVGObject svgG;
  private SVGObject svgPoint;

  private boolean value0;
  private boolean value1;
  private boolean value2;
  private boolean value3;
  private boolean value4;
  private boolean value5;
  private boolean value6;
  private boolean value7;
  private Color disabledColor = new Color(220,200,200);
  private Color enabledColor = new Color(255,0,0);
  private java.awt.Graphics gg;



  public void processPanel(int pinIndex, double value, Object obj)
  {
    boolean val = ((Boolean)obj).booleanValue();
    if (pinIndex==0) this.value0=val;
    if (pinIndex==1) this.value1=val;
    if (pinIndex==2) this.value2=val;
    if (pinIndex==3) this.value3=val;
    if (pinIndex==4) this.value4=val;
    if (pinIndex==5) this.value5=val;
    if (pinIndex==6) this.value6=val;
    if (pinIndex==7) this.value7=val;
    
  }


   public void paint(java.awt.Graphics g)
   {
     if (element!=null)
     {
         gg=g;
        Rectangle bounds=element.jGetBounds();
        if (svgA!=null)  svgA.setVisible(value0);
        if (svgB!=null)  svgB.setVisible(value1);
        if (svgC!=null)  svgC.setVisible(value2);
        if (svgD!=null)  svgD.setVisible(value3);
        if (svgE!=null)  svgE.setVisible(value4);
        if (svgF!=null)  svgF.setVisible(value5);
        if (svgG!=null)  svgG.setVisible(value6);
        if (svgPoint!=null)  svgPoint.setVisible(value7);
        svgManager.paint((Graphics2D)g,(int)bounds.getWidth(),(int)bounds.getHeight());
     }
   }
   
  public void init()
  {
    initPins(0,0,0,0);
    setSize(50,80);
    element.jSetInnerBorderVisibility(false);
    initPinVisibility(false,false,false,false);

    svgManager.loadFromFile(element.jGetSourcePath()+"image.svg");

    svgA= svgManager.getSVGObject("a");
    svgB= svgManager.getSVGObject("b");
    svgC= svgManager.getSVGObject("c");
    svgD =svgManager.getSVGObject("d");
    svgE =svgManager.getSVGObject("e");
    svgF =svgManager.getSVGObject("f");
    svgG =svgManager.getSVGObject("g");
    svgPoint =svgManager.getSVGObject("point");
    
    setName("Siebensegment-Anzeige");
    
    element.jSetResizable(true);
    element.jSetAspectRatio(80.0/50.0);
    element.jSetResizeSynchron(true);

  }


}
 
