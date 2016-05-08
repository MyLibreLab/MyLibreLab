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

public class ElementPropertySetter extends JVSMain
{
  private Image image;


  private VSBoolean inVisible            = new VSBoolean(true);
  private VSInteger inLeft               = new VSInteger(0);
  private VSInteger inTop                = new VSInteger(0);
  private VSInteger inWidth              = new VSInteger(0);
  private VSInteger inHeight             = new VSInteger(0);

  private VSComboBox elList=new VSComboBox();
  private VSInteger selectedID = new VSInteger(-1);
  private VSString selectedCaption = new VSString("");
  
  private VSBasisIF basis=null;

  private javax.swing.Timer timer;

  private ExternalIF elements[]=null;
  private ExternalIF selectedElement=null;

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
    initPins(0,0,0,5);
    setSize(50,25+(5*10));
    element.jSetInnerBorderVisibility(true);
    element.jSetLeftPinsVisible(true);
    element.jSetTopPinsVisible(false);
    element.jSetRightPinsVisible(false);
    element.jSetBottomPinsVisible(false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.png");
    
    element.jInitPins();
    
    setPin(0,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    setPin(1,ExternalIF.C_INTEGER,element.PIN_INPUT);
    setPin(2,ExternalIF.C_INTEGER,element.PIN_INPUT);
    setPin(3,ExternalIF.C_INTEGER,element.PIN_INPUT);
    setPin(4,ExternalIF.C_INTEGER,element.PIN_INPUT);
    
    element.jSetPinDescription(0,"Visible");
    element.jSetPinDescription(1,"Left");
    element.jSetPinDescription(2,"Top");
    element.jSetPinDescription(3,"Width");
    element.jSetPinDescription(4,"Height");

    
    setName("ElementPropertySetter");
    
    

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
    doRefreshList();
    int index=getIndexOfElement(selectedID.getValue(),selectedCaption.getValue());

    if (index>-1)
    {

      selectedElement=elements[index];
    } else
    {
      selectedElement=null;
    }

  }

  public void initInputPins()
  {
    inVisible=(VSBoolean)element.getPinInputReference(0);
    inLeft=(VSInteger)element.getPinInputReference(1);
    inTop=(VSInteger)element.getPinInputReference(2);
    inWidth=(VSInteger)element.getPinInputReference(3);
    inHeight=(VSInteger)element.getPinInputReference(4);
  }


  public void process()
  {
    if (selectedElement!=null)
    {
      if (inVisible!=null) selectedElement.jSetVisible(inVisible.getValue());
      if (inLeft!=null) selectedElement.jSetLeft(inLeft.getValue());
      if (inTop!=null) selectedElement.jSetTop(inTop.getValue());
      if (inWidth!=null) selectedElement.jSetSize(inWidth.getValue(), selectedElement.jGetHeight());
      if (inHeight!=null) selectedElement.jSetSize(selectedElement.jGetWidth(),inHeight.getValue());
    }
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

