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
import tools.*;
import javax.swing.*;


public class FFT extends JVSMain
{
  private Image image;
  private VSGroup in;
  private VSGroup out = new VSGroup();
  private VS1DDouble arrX = new VS1DDouble(0);
  private VS1DDouble arrY = new VS1DDouble(0);
  private boolean changed=false;
  private boolean firstTime=true;

  private FastFourierTransform fft = new FastFourierTransform();


  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }



  public void xpaint(java.awt.Graphics g)
  {
    drawImageCentred(g,image);
  }


  /* holt den nächst besten Wert
   * der 2^1 2^2 2^3 2^4 .... entspricht */
  public int getNextValue(int value)
  {
    int i=0;
    double val;
    while((val=Math.pow(2,i++))<value) {}
    return (int)val;
  }


  public void init()
  {
    initPins(0,1,0,1);
    setSize(50,30);
    element.jSetInnerBorderVisibility(true);
    element.jSetTopPinsVisible(false);
    element.jSetBottomPinsVisible(false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");


    setPin(0,ExternalIF.C_GROUP,element.PIN_OUTPUT);
    setPin(1,ExternalIF.C_GROUP,element.PIN_INPUT);

    element.jSetPinDescription(0,"Out");
    element.jSetPinDescription(1,"In");

    element.jSetResizable(false);
    element.jSetCaptionVisible(true);
    element.jSetCaption("fft");
    setName("FFT");
    
    // Für den Degruppierer (Nur für den Datentyp)
    out.list.clear();
    out.list.add(arrX);
    out.list.add(arrY);
    element.setPinOutputReference(0,out);

   }


  public void initInputPins()
  {
    in=(VSGroup)element.getPinInputReference(1);
  }

  public void initOutputPins()
  {
    element.setPinOutputReference(0,out);
  }
  
  public void start()
  {
    changed=true;
  }
  

  public void process()
  {

    if (in !=null && in.list.size()==2)
    {
      VS1DDouble arrayX=(VS1DDouble)in.list.get(0);
      VS1DDouble arrayY=(VS1DDouble)in.list.get(1);

      //if (changed)
      {
        changed=false;
        firstTime=true;
        if (arrayY.getValue().length>0)
        {
          float[] signal= new float[getNextValue(arrayY.getValue().length)];

          for (int i=0;i<arrayY.getValue().length;i++)
          {
            signal[i]=(float)arrayY.getValue(i);
          }

          float[] res=fft.fftMag(signal);
          //float[] res=signal.clone();
          //Fourier.FCT1D(res,false);

          arrX = new VS1DDouble(res.length);
          arrY = new VS1DDouble(res.length);

          for (int i=0;i<arrX.getValue().length;i++)
          {
            arrX.setValue(i,i);
            arrY.setValue(i,(double)res[i]);
          }

          out.list.clear();
          out.list.add(arrX);
          out.list.add(arrY);
          out.setChanged(true);

        }
      }
      element.notifyPin(0);
    } /*  else
    {
      if (firstTime)
      {
       out.copyValueFrom(null);
       out.setChanged(true);
       changed=false;
       firstTime=false;
       element.notifyPin(0);
      }
    }   */

  }


}

