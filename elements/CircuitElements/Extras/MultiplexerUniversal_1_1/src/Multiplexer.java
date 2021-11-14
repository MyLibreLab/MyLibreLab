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
import javax.swing.*;


public class Multiplexer extends JVSMain
{
  public VSInteger inAdress;
  public VSObject[] in;
  public VSObject out=null;
  
  private Font font = new Font("Arial",0,8);
  
  private int anzIn=2;
  
  private boolean setted=false;

  private Image image;
  
  private VSInteger anzPins = new VSInteger(2);


  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }

  public void draw90GradRotatedLabel(Graphics g, String text, int x, int y)
  {
    Graphics2D g2d = (Graphics2D)g.create();
    JPanel panel = element.getFrontPanel();
    
    FontMetrics fm = panel.getFontMetrics(g.getFont());

    //.getAscent(), (getHeight()/2)+(getFontMetrics(g.getFont()).stringWidth(text)/2 ));
    g2d.translate(x+5,y);
    g2d.rotate(-Math.PI/2);
    g2d.drawString(text, 0,0);
    g2d.dispose();
  }
  
  public void paint(java.awt.Graphics g)
  {
    if (image!=null)
    {
    // drawImageCentred(g,image);
    }
    
    Rectangle rec=element.jGetBounds();
    int mx=rec.width/2;
    int my=rec.height/2;
    
    g.setFont(font);
    g.setColor(Color.BLACK);

    g.drawString("Y", rec.width,my+5);
    
    g.drawString("Adress", rec.x+3,10);
    
    anzIn= anzPins.getValue();
    
    double d=(double)rec.height/(double)(anzIn+1);
    
    for (int i=0;i<anzIn;i++)
    {
      g.drawString("x"+i, rec.x+3,4+(int)(d*1.4+i*d));
    }
    
    draw90GradRotatedLabel(g,"U-MUX", 28,my+20);
    
    g.drawRect(rec.x,rec.y,rec.width,rec.height-1);
    
  }

 public void initPins()
  {
    anzIn= anzPins.getValue();
    initPins(0,1,0,anzIn+1);
    setSize(57,25+(10*anzIn));
    initPinVisibility(false,true,false,true);
    element.jSetInnerBorderVisibility(false);
    image=element.jLoadImage(element.jGetSourcePath()+"image.png");

    setPin(0,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
    element.jSetPinDescription(0,"Y");

    setPin(1,ExternalIF.C_INTEGER,element.PIN_INPUT);
    element.jSetPinDescription(1,"Adress");

    for (int i=0;i<anzIn;i++)
    {
      setPin(2+i,ExternalIF.C_VARIANT,element.PIN_INPUT);
      element.jSetPinDescription(2+i,"x"+i);
    }

  }



 public void init()
  {
    initPins();

    element.jSetCaptionVisible(true);
    element.jSetCaption("MuxltiplexerUniversal 1.1");
    setName("MuxltiplexerUniversal 1.1");
    //element.jSetInfo("Carmelo Salafia","Open Source 2007","");
  }


  public void setPropertyEditor()
  {
    element.jAddPEItem("Input-Pins",anzPins, 2,50);
    localize();
  }

  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"Input-Pins");

    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Input-Pins");
  }

  public void propertyChanged(Object o)
  {
    if (o == anzPins)
    {
      //element.jSetSize(element.jGetWidth(), 20+(anzPins.getValue())*10);


      initPins();
      //element.jSetRightPins(anzPins.getValue());
      //element.jSetLeftPins(1);

      //setOutPinsDataTypeAndIO(anzPins);
      element.jRepaint();
      element.jRefreshVM();
    }

  }



  public void checkPinDataType()
  {
    boolean pinIn=false;
    int dt=element.C_VARIANT;
    for (int i=0;i<anzIn;i++)
    {
      pinIn=element.hasPinWire(2+i);
      
      if (pinIn==true)
      {
        dt=element.jGetPinDrahtSourceDataType(2+i);
      }
    }
    
    element.jSetPinDataType(0,dt);
    
    for (int i=0;i<anzIn;i++)
    {
      element.jSetPinDataType(2+i,dt);
    }
    element.jRepaint();
  }



  public void initInputPins()
  {
    inAdress=(VSInteger)element.getPinInputReference(1);
    
    if (inAdress==null) inAdress=new VSInteger(0);

    in= new VSObject[anzIn];
     
    for (int i=0;i<anzIn;i++)
    {
      in[i]=(VSObject)element.getPinInputReference(2+i);
    }
  }

  public void initOutputPins()
  {
    out=element.jCreatePinDataType(0);
    element.setPinOutputReference(0,out);
  }

  public void start()
  {
  }
  
  public void stop()
  {
  }
  
  public void process()
  {
    int adr =  inAdress.getValue();
    if (adr>=0 && adr<anzIn)
    {
      VSObject obj = in[adr];
      out.copyReferenceFrom(obj);
    }else
    {
      out.copyReferenceFrom(null);
    }
    element.notifyPin(0);
  }


  public void loadFromStream(java.io.FileInputStream fis)
  {
      anzPins.loadFromStream(fis);
      initPins();
      element.jRepaint();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
    anzPins.setValue(anzIn);
    anzPins.saveToStream(fos);

  }


}
