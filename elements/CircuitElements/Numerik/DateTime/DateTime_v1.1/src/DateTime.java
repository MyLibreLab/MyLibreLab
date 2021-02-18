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
import java.util.Calendar;
import tools.*;
import java.awt.event.*;
import javax.swing.*;


public class DateTime extends JVSMain
{
  private Image image;
  private Calendar rightNow = Calendar.getInstance();
  private VSBoolean amPmFOrmat = new VSBoolean(false);
  private VSInteger out= new VSInteger();
  private VSComboBox param=new VSComboBox();
  private javax.swing.Timer timer;
  private int oldValue=-1;
  
  private boolean stop=true;
  
  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }
  
  public void paint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }

  public void init()
  {
    initPins(0,1,0,0);
    setSize(50,30);
    
    element.jSetInnerBorderVisibility(false);
    element.jSetTopPinsVisible(false);
    element.jSetBottomPinsVisible(false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    setPin(0,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
    element.jSetPinDescription(0,"Out");
    setName("DateTime");
    
    param.addItem("HOUR");
    param.addItem("MIN");
    param.addItem("SEC");
    param.addItem("YEAR");
    param.addItem("MONTH");
    param.addItem("DAY_OF_MONTH");
    param.addItem("DAY_OF_WEEK");
    

    timer = new javax.swing.Timer(500, new ActionListener()
    {
        public void actionPerformed(ActionEvent evt)
        {
          verarbeitung();
        }
    });

    timer.start();

  }
  
  public void setPropertyEditor()
  {
    element.jAddPEItem("AM/PM",amPmFOrmat, 0,0);
    element.jAddPEItem("Type",param, 0,10);

  }



  public void start()
  {
    stop=false;
    oldValue=-1;
    verarbeitung();
  }
  public void stop()
  {
    stop=true;
  }


  public void initInputPins()
  {
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }

  public void verarbeitung()
  {
     if (stop==false)
     {
       rightNow = Calendar.getInstance();

       int val=0;
       
       switch (param.selectedIndex)
       {
           case 0 : {
            val=rightNow.get(rightNow.HOUR_OF_DAY);
            
            if (amPmFOrmat.getValue()==true) val-=12;
           }break;
           case 1 : val=rightNow.get(rightNow.MINUTE); break;
           case 2 : val=rightNow.get(rightNow.SECOND); break;
           case 3 : val=rightNow.get(rightNow.YEAR); break;
           case 4 : val=rightNow.get(rightNow.MONTH)+1; break;
           case 5 : val=rightNow.get(rightNow.DAY_OF_MONTH); break;
           case 6 : val=rightNow.get(rightNow.DAY_OF_WEEK); break;
       }

       if (oldValue!=val)
       {
         oldValue=val;
         out.setValue(val);
         element.notifyPin(0);
       }
     }
  }


  public void loadFromStream(java.io.FileInputStream fis)
  {
      param.loadFromStream(fis);
      amPmFOrmat.loadFromStream(fis);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    param.saveToStream(fos);
    amPmFOrmat.saveToStream(fos);
  }


}


