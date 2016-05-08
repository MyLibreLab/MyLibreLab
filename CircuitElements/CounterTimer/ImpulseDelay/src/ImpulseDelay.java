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



public class ImpulseDelay extends JVSMain
{
  private Image image;
  private VSBoolean in;
  private VSBoolean out= new VSBoolean();
  private Color color=Color.BLACK;
  private javax.swing.Timer timer;
  private boolean started=false;
  private VSInteger interval = new VSInteger(500);
  private VSObject vv=null;

  public ImpulseDelay()
  {
  }

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
    initPins(0,1,0,1);
    setSize(50,34);
    element.jSetInnerBorderVisibility(true);
    element.jSetTopPinsVisible(false);
    element.jSetBottomPinsVisible(false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.png");
    
    element.jInitPins();
    
    setPin(0,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_BOOLEAN,element.PIN_INPUT);
    
    setName("Delay");
    out.setPin(0);
    
    
  }

  public void start()
  {
    started=false;
    if (timer!=null)
    {
      timer.stop();
      timer.setDelay(interval.getValue());
    }

  }
  public void stop()
  {
    if (timer!=null) timer.stop();
    started=false;
  }
  
  

  // Diese Methode ist Impulse sicher (zb: Boolean Taster)
  // Deshalb das out Array!
  public void process()
  {
    if (in instanceof VSBoolean && started==false)
    {
        started=true;
        timer = new javax.swing.Timer(interval.getValue(), new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
              processX();
            }
        });


        timer.start();
        System.out.println("******** PROCESS DELAY="+timer.getDelay());
    }

  }
  

  private boolean changed=false;
  
  public void destElementCalled()
  {
    if (changed)
    {
      System.out.println("CANCELLED---------------------<");
      out.setValue(false);
      element.notifyPin(0);
      changed=false;
    }
  }

  public void processX()
  {
    if (started)
    {
      if (in instanceof VSBoolean)
      {
          out.setValue(true);
          started=false;
          changed=true;
          element.notifyPin(0);
          element.jNotifyWhenDestCalled(0,element);
          timer.stop();
          System.out.println("------------------>PROCESSED<");
      }
    }
  }



  public void setPropertyEditor()
  {
    element.jAddPEItem("Delay",interval, 0,1000000);
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Delay");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Delay");
  }
  

  public void initInputPins()
  {
    in=(VSBoolean)element.getPinInputReference(1);
    
    if (in==null) in= new VSBoolean(false);
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }

  public void loadFromStream(java.io.FileInputStream fis)
  {
      interval.loadFromStream(fis);
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      interval.saveToStream(fos);
  }

}

