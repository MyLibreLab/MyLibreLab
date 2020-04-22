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
import javax.swing.Timer;

public class EventCatcher extends JVSMain
{
  private Image image;
  private VSBoolean outMousePressed= new VSBoolean(false);
  private VSBoolean outMouseReleased= new VSBoolean(false);
  private VSBoolean outMouseDragged= new VSBoolean(false);
  private VSBoolean outMouseMoved= new VSBoolean(false);
  
  private VSInteger outMouseX= new VSInteger(0);
  private VSInteger outMouseY= new VSInteger(0);
  private VSInteger outMouseButton= new VSInteger(0);
  private VSInteger outMouseClickcount= new VSInteger(0);
  
  private VSComboBox elList=new VSComboBox();
  private VSInteger selectedID = new VSInteger(-1);
  private VSString selectedCaption = new VSString("");
  
  private javax.swing.Timer timer;
  
  private VSBasisIF basis=null;

  private ExternalIF elements[]=null;

  public void paint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }

  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }
  public void init()
  {
    initPins(0,8,0,0);
    setSize(50,25+(8*10));
    element.jSetInnerBorderVisibility(true);
    element.jSetLeftPinsVisible(false);
    element.jSetTopPinsVisible(false);
    element.jSetBottomPinsVisible(false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.png");
    
    element.jInitPins();
    
    setPin(0,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    setPin(2,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    setPin(3,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    
    setPin(4,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
    setPin(5,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
    setPin(6,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
    setPin(7,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
    
    element.jSetPinDescription(0,"onMousePressed");
    element.jSetPinDescription(1,"onMousereleased");
    element.jSetPinDescription(2,"onMouseDragged");
    element.jSetPinDescription(3,"onMouseMoved");
    element.jSetPinDescription(4,"x-koordinate");
    element.jSetPinDescription(5,"y-koordinate");
    element.jSetPinDescription(6,"Button");
    element.jSetPinDescription(7,"Clickcount");
    
    setName("EventCatcher");
    
  }
  
  public void xOnInit()
  {

    timer = new javax.swing.Timer(1000, new ActionListener()
    {
        public void actionPerformed(ActionEvent evt)
        {
          doRefreshList();
        }
    });
    timer.start();


  }
  



  
  private void doRefreshList()
  {
    basis=element.jGetBasis();
    elements=basis.vsGetListOfPanelElements();

    elList.clear();
    for (int i=0;i<elements.length;i++)
    {
      elList.addItem(elements[i].jGetCaption());
    }

    elList.selectedIndex=getIndexOfElement(selectedID.getValue(),selectedCaption.getValue());
  }

  public void setPropertyEditor()
  {
    element.jAddPEItem("Element",elList, 0,0);

  }
  public void propertyChanged(Object o)
  {
    if (o.equals(elList))
    {
      int id=elements[elList.selectedIndex].jGetID();
      String caption=elements[elList.selectedIndex].jGetCaption();
      selectedID.setValue(id);
      selectedCaption.setValue(caption);
    }
  }

  public void start()
  {
    int index=getIndexOfElement(selectedID.getValue(),selectedCaption.getValue());
    if (index>-1)
    {
      elements[index].jGiveMouseEventsTo(element);
    }

  }
  

  


  public void initInputPins()
  {
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,outMousePressed);
    element.setPinOutputReference(1,outMouseReleased);
    element.setPinOutputReference(2,outMouseDragged);
    element.setPinOutputReference(3,outMouseMoved);
    element.setPinOutputReference(4,outMouseX);
    element.setPinOutputReference(5,outMouseY);
    element.setPinOutputReference(6,outMouseButton);
    element.setPinOutputReference(7,outMouseClickcount);
  }



  int x,y;


  private void aktualX(MouseEvent e)
  {
    x=e.getX();
    y=e.getY();
    outMouseX.setValue(e.getX());
    outMouseY.setValue(e.getY());
    outMouseButton.setValue(e.getButton());
    outMouseClickcount.setValue(e.getClickCount());
    
    element.notifyPin(4);
    element.notifyPin(5);
    element.notifyPin(6);
    element.notifyPin(7);
  }
  
  boolean changed=false;

  public void destElementCalled()
  {
    if (changed)
    {

      outMouseDragged.setValue(false);
      outMouseMoved.setChanged(false);
      element.notifyPin(2);
      element.notifyPin(3);
      changed=false;
    }
  }
  public void mousePressed(MouseEvent e)
  {
    outMousePressed.setValue(true);
    outMouseReleased.setValue(false);
    element.notifyPin(0);
    element.notifyPin(1);
    aktualX(e);
    //outMousePressed.setValue(false);
    //outMouseReleased.setValue(false);
  }

  public void mouseReleased(MouseEvent e)
  {
    outMousePressed.setValue(false);
    outMouseReleased.setValue(true);
    element.notifyPin(0);
    element.notifyPin(1);
    aktualX(e);
    //outMousePressed.setValue(false);
    //outMouseReleased.setValue(false);
  }
  public void mouseDragged(MouseEvent e)
  {
    outMouseDragged.setValue(true);
    element.notifyPin(2);
    aktualX(e);
    changed=true;
    element.jNotifyWhenDestCalled(2,element);
    //outMouseDragged.setValue(false);
  }

  public void mouseMoved(MouseEvent e)
  {
    //if (x!=e.getX() || y!=e.getY())
    {
      outMouseMoved.setValue(true);
      element.notifyPin(3);
      aktualX(e);
      changed=true;
      element.jNotifyWhenDestCalled(3,element);
    }
    //outMouseMoved.setValue(false);
  }


  public void process()
  {

  }


  private int getIndexOfElement(int id, String caption)
  {
    for (int i=0;i<elements.length;i++)
    {
      int idx=elements[i].jGetID();
      String captionx=elements[i].jGetCaption();

      if (id==idx && caption.equalsIgnoreCase(captionx))
      {
        return i;
      }
    }

    return -1;
  }


  public void loadFromStream(java.io.FileInputStream fis)
  {
     selectedID.loadFromStream(fis);
     selectedCaption.loadFromStream(fis);
  }


  public void saveToStream(java.io.FileOutputStream fos)
  {
    selectedID.saveToStream(fos);
    selectedCaption.saveToStream(fos);
  }


}

