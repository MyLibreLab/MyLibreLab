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


public class DeGruppierer extends JVSMain
{
  private VSGroup in;
  private VSObject out[] ;
  private VSInteger anzPins=new VSInteger(2);
  private Image image;


   public void xpaint(java.awt.Graphics g)
   {
     drawImageCentred(g,image);
   }

  public void init()
  {
    initPins(0,anzPins.getValue(),0,1);
    setSize(50,15+(anzPins.getValue()*10));
    initPinVisibility(false,true,false,true);

    element.jSetInnerBorderVisibility(true);
    
    element.jSetResizable(false);

    setName("#DE-GRUPPIERER#");

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    element.jInitPins();
    setPin(anzPins.getValue(),ExternalIF.C_GROUP,element.PIN_INPUT);

  }

  public void initInputPins()
  {

    Object o = element.getPinInputReference(anzPins.getValue());
    if ( o instanceof VSGroup)
    {
      in=(VSGroup)o;
      
      if (in!=null)
      {

           int size=in.list.size();
           anzPins.setValue(size);
           initPins();

           for (int i=0;i<size;i++)
           {
               o=in.list.get(i);

               if (o instanceof VSObject)
               {
                 VSObject obj=(VSObject)o;
                 int dt=element.jGetDataType(obj);

                 setPin(i,dt,element.PIN_OUTPUT);
               }
           }
      }

    }

    
  }

  public void initOutputPins()
  {
    if (in!=null)
    {
         Object o =null;

         int size=in.list.size();

         anzPins.setValue(size);

         out = new VSObject[anzPins.getValue()];

         for (int i=0;i<size;i++)
         {
             o=in.list.get(i);

             if (o instanceof VSObject)
             {
               VSObject obj=(VSObject)o;
               element.setPinOutputReference(i,obj);
             }
         }
    }
  }
  
  public void initPins()
  {
    initPins(0,anzPins.getValue(),0,1);

    setSize(50,15+(anzPins.getValue()*10));

    setPin(anzPins.getValue(),ExternalIF.C_GROUP,element.PIN_INPUT);
  }
  
  public void checkPinDataType()
  {
    
  }


  public void process()
  {
  
    if (in!=null)
    {

      for (int i=0;i<in.list.size();i++)
      {
        element.notifyPin(i);
      }
    }
  }

  

  public void loadFromStream(java.io.FileInputStream fis)
  {
      anzPins.loadFromStream(fis);
      init();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      anzPins.saveToStream(fos);
  }


}




