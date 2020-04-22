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
import javax.swing.*;
import java.awt.event.*;
import SVGViewer.*;


public class SchalterPanel extends JVSMain
{
  private SVGManager svgManager = new SVGManager();
  private VSBoolean initValue=new VSBoolean(false);
  private ExternalIF circuitElement;
  
  private boolean down=false;

  private boolean value=false;
  private int sizeW=27;
  private int sizeH=35;

  private SVGObject svgOnRect;
  private SVGObject svgOn;
  private SVGObject svgOffRect;
  private SVGObject svgOff;


  public void paint(java.awt.Graphics g)
  {
    if (element!=null)
    {
       Graphics2D g2=(Graphics2D)g;
       Rectangle bounds=element.jGetBounds();

       if (down)
       {
          if (svgOnRect!=null)  svgOnRect.setVisible(true);
          if (svgOn!=null)  svgOn.setVisible(true);
          if (svgOffRect!=null)  svgOffRect.setVisible(false);
          if (svgOff!=null)  svgOff.setVisible(false);
       } else
       {
          if (svgOnRect!=null)  svgOnRect.setVisible(false);
          if (svgOn!=null)  svgOn.setVisible(false);
          if (svgOffRect!=null)  svgOffRect.setVisible(true);
          if (svgOff!=null)  svgOff.setVisible(true);
       }

       svgManager.paint((Graphics2D)g,(int)bounds.getWidth(),(int)bounds.getHeight());
    }
  }
  
  private void setValue(VSBoolean bol)
  {
     down=bol.getValue();
     circuitElement=element.getCircuitElement();
     if (circuitElement!=null) circuitElement.Change(0,bol);
  }


  public void init()
  {
    initPins(0,0,0,0);
    setSize(sizeW,sizeH);
    initPinVisibility(false,false,false,false);

    element.jSetInnerBorderVisibility(false);

    element.jSetResizeSynchron(true);
    element.jSetAspectRatio((double)sizeH/(double)sizeW);
    element.jSetResizable(true);
    
    svgManager.loadFromFile(element.jGetSourcePath()+"schalter.svg");

    svgOnRect= svgManager.getSVGObject("onRect");
    svgOffRect= svgManager.getSVGObject("offRect");
    svgOn= svgManager.getSVGObject("on");
    svgOff =svgManager.getSVGObject("off");
    
    setName("Schiebeschalter");

  }
  
    public void start()
    {
      setValue(initValue);
      element.jRepaint();
    }
    
    
  public void setPropertyEditor()
  {
    element.jAddPEItem("Anfangs-Wert",initValue, 0,0);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Init-Value");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Valor Inicial");
  }

  public void propertyChanged(Object o)
  {
    setValue(initValue);
    element.jRepaint();
  }

  

  public void mousePressed(MouseEvent e)
  {
     if (down==true)
     {
        setValue(new VSBoolean(false));
        element.jRepaint();
      } else
      {
        setValue(new VSBoolean(true));
        element.jRepaint();
      }
  }


  public void loadFromStream(java.io.FileInputStream fis)
  {
      initValue.loadFromStream(fis);
      element.jRepaint();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      initValue.saveToStream(fos);
  }



}

