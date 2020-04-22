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


public class IDCT extends JVSMain
{
  private Image image;
  private VSGroup in;
  private VSGroup out = new VSGroup();
  private VS1DDouble arrX = new VS1DDouble(0);
  private VS1DDouble arrY = new VS1DDouble(0);
  private boolean changed=false;
  private boolean firstTime=true;



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
    element.jSetCaption("dct");
    setName("DCT");
    
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
  
  
    public static double[] dct1D(double[] values)
    {
        double[] result=new double[values.length];

        double summ=0.0;
        double ck;
        double  N=(double)values.length;
        double f2=1.0/Math.sqrt(2.0);
        double ff=Math.sqrt(2.0/N);
        double x1=0;
        double PI=Math.PI;
        double N2=2.0*N;
        double val=0;

        for (double u=0;u<N;u+=1.0)
        {
            summ=0.0;
            x1=PI*u;
            for (double n=0;n<N;n+=1.0)
            {
                val=values[(int)n];
                summ+=val*Math.cos( (x1*(2*n+1.0)) / N2 );
            }
            if (u==0.0) ck=f2; else ck=1.0;
            result[(int)u]=ff*ck*summ;
        }

        return result;
    }



    public static double[] inverseDCT1D(double[] values)
    {
        double[] result=new double[values.length];

        double summ=0.0;
        double ck;
        double N=(double)values.length;
        double f2=1.0/Math.sqrt(2.0);
        double N2=2.0*N;
        double PI=Math.PI;
        double x1=Math.sqrt(2.0/N);
        double n2_1;

        for (double n=0.0;n<N;n+=1.0)
        {
            summ=0.0;
            n2_1=2.0*n+1.0;

            for (double u=0.0;u<N;u+=1.0)
            {
                double val=values[(int)u];
                if (u==0.0) ck=f2; else ck=1.0;
                summ+=val*ck*Math.cos( u*PI*n2_1/N2 );
            }
            result[(int)n]=x1*summ;
        }

        return result;
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
          double[] signal= new double[getNextValue(arrayY.getValue().length)];

          for (int i=0;i<arrayY.getValue().length;i++)
          {
            signal[i]=(double)arrayY.getValue(i);
          }

          double[] res=inverseDCT1D(signal);

          //float[] res=fft.fftMag(signal);

          arrX = new VS1DDouble(res.length);
          arrY = new VS1DDouble(res.length);

          for (int i=0;i<arrX.getValue().length;i++)
          {
            arrX.setValue(i,i);
            arrY.setValue(i,res[i]);
          }

          out.list.clear();
          out.list.add(arrX);
          out.list.add(arrY);
          out.setChanged(true);
          element.notifyPin(0);
        }
      }
    }/*   else
    {
      if (firstTime)
      {
       out.copyValueFrom(null);
       out.setChanged(true);
       changed=false;
       firstTime=false;
       element.notifyPin(0);
      }
    }  */

  }


}

