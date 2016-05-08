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

package tools;

import VisualLogic.*;
import java.awt.*;
import java.awt.event.*;
import VisualLogic.variables.*;

public class JVSMain extends Object implements ElementIF
{
  public ExternalIF element;
  
  private String name;
  public void setName(String name)
  {
    this.name=name;
    element.jSetName(name);
  }
  public String getName(){ return name; }
  
  public void xOnInit()
  {

  }
  
  
  
  
  public void destElementCalled()
  {

  }
  
  public String jGetVMFilename()
  {
   return "";
  }
   
   public String getBinDir(){
    return "";
   }
   
  private boolean firstTime=true;
  public void beforeInit(String[] args)
  {

  }
  public void init()  { }
  public void start()  { }
  public void stop()  { }
  public void pause()  { }
  public void process(){}
  public void returnFromMethod(Object result){}
  public void processMethod(VSFlowInfo flowInfo){}
  public void processClock(){}
  public void elementActionPerformed(ElementActionEvent evt){}

  public void mouseDragged(MouseEvent e){}
  public void mousePressed(MouseEvent e){}
  public void mousePressedOnIdle(MouseEvent e){}
  public void mouseReleased(MouseEvent e){}
  public void mouseMoved(MouseEvent e){}
  public void openPropertyDialog(){}

  public void saveToStream(java.io.FileOutputStream fos){}
  public void loadFromStream(java.io.FileInputStream fis){}

  public void paint(java.awt.Graphics g){}
  public void setSize(int width,int height) { element.jSetSize(width,height);}

  public void changePin(int pinIndex, Object value)
  {

  }

  public void checkPinDataType()
  {

  }
  public void resetValues()
  {

  }

  public void setPropertyEditor()
  {
  }

  public void initPins(int topPins, int rightPins, int bottomPins, int leftPins)
  {
    element.jSetTopPins(topPins);
    element.jSetRightPins(rightPins);
    element.jSetBottomPins(bottomPins);
    element.jSetLeftPins(leftPins);
  }

  public void initPinVisibility(boolean top, boolean right, boolean bottom, boolean left)
  {
    element.jSetTopPinsVisible(top);
    element.jSetRightPinsVisible(right);
    element.jSetBottomPinsVisible(bottom);
    element.jSetLeftPinsVisible(left);
  }
  

  
  public void drawImageCentred(Graphics g,Image image)
  {
    if (element!=null)
    {
       Graphics2D g2 = (Graphics2D)g;
       Rectangle r=element.jGetBounds();

       int mitteX =r.width/2;
       int mitteY =r.height/2;
       int imageMitteX = image.getWidth(null)/2;
       int imageMitteY = image.getHeight(null)/2;

       g2.drawImage(image,r.x+mitteX-imageMitteX,r.y+mitteY-imageMitteY,null);
    }
   }

   public void setPin(int pinIndex, int dataType, byte io)
   {
     if (firstTime)
     {
       element.jInitPins();
       firstTime=false;
     }
    element.jSetPinDataType(pinIndex,dataType);
    element.jSetPinIO(pinIndex,io);
   }
   
  public void initInputPins()
  {

  }

  public void initOutputPins()
  {

  }


  public void onChangeElement()
  {
  }

  public void onDispose(){}

  public void propertyChanged(Object o)
  {
  }
  public void saveToStreamAfterXOnInit(java.io.FileOutputStream fos){}
  public void loadFromStreamAfterXOnInit(java.io.FileInputStream fis){}


  // interne Methoden
  public void xonChangeElement(){onChangeElement();}
  public void xsetExternalIF(ExternalIF externalIF){ element=externalIF;init();}
  public void xonInitInputPins(){initInputPins();}
  public void xonInitOutputPins(){initOutputPins();}
  public void xpaint(java.awt.Graphics g){paint(g);}
  public void xonMouseDragged(MouseEvent e){mouseDragged(e);}
  public void xonMousePressed(MouseEvent e){mousePressed(e);}
  public void xonMousePressedOnIdle(MouseEvent e){mousePressedOnIdle(e);}
  public void xonMouseReleased(MouseEvent e){mouseReleased(e);}
  public void xonMouseMoved(MouseEvent e){mouseMoved(e);}
  public void xonStart(){ start();}
  public void xonStop(){ stop();}
  public void xonProcess(){process();}
  public void xonClock(){processClock();}
  public void xopenPropertyDialog(){openPropertyDialog();}

  public void xSaveToStream(java.io.FileOutputStream fos){saveToStream(fos);}
  public void xLoadFromStream(java.io.FileInputStream fis){loadFromStream(fis);}

  public String xgetName(){return name;}
}
