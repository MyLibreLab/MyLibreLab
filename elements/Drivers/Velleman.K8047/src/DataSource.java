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



import java.util.ArrayList;
import VisualLogic.*;
import VisualLogic.variables.*;

/**
 *
 * @author Carmelo
 */
public class DataSource extends Thread
{
    private boolean stop=false;
    private K8047d k8047;
    public MyOpenLabDriverOwnerIF owner;
    
    private int data[] = new int[8];
    private double ch1, ch2, ch3, ch4;
    public int gain_ch1, gain_ch2, gain_ch3, gain_ch4;
    
    private double[][] values;
    public int abtastIntervall=1000;
    
        
    public DataSource(K8047d k8047, MyOpenLabDriverOwnerIF owner)
    {
      this.k8047=k8047;
      this.owner=owner;
    }

    public void stopMe()
    {
        stop=true;
    }
    
    
  // Gain:
  // 1  = 30V
  // 2  = 15V
  // 5  = 6V
  // 10 = 3V
  // Result ist der Faktor! der immer mit 3V multipliziert werden muss
  private double convertGain(int gain)
  {
      switch (gain)
      {
          case 1  : return 10.0;
          case 2  : return 5.0;
          case 5  : return 2.0;
          case 10 : return 1.0;
      }
      return 10;
  }
  
  public void setIntervall(int intervall)
  {
    this.abtastIntervall=intervall;
  }
    
  private double getVolt(int value, int gain)
  {
   return ((3.0*convertGain(gain))/255.0)*value;
  }
    @Override
    public void run()
    {
        stop=false;

        int LSB;
        int MSB;
        int time;

        double stime = System.currentTimeMillis();
        double TotalTime ;
        while(!stop)
        {
            TotalTime = System.currentTimeMillis() - stime;
            
            try{ Thread.sleep(1); } catch(Exception ex){}
            
            if (TotalTime>abtastIntervall)
            {
                k8047.ReadData(data);

                LSB=data[0];
                MSB=data[1];

                time=LSB;
                time|=(MSB<<8);


                stime = System.currentTimeMillis();
                ch1=getVolt(data[2], gain_ch1);
                ch2=getVolt(data[3], gain_ch2);
                ch3=getVolt(data[4], gain_ch3);
                ch4=getVolt(data[5], gain_ch4);
                
                if (owner!=null)
                {
                  owner.getCommand("DAC1",new Double(ch1));
                  owner.getCommand("DAC2",new Double(ch2));
                  owner.getCommand("DAC3",new Double(ch3));
                  owner.getCommand("DAC4",new Double(ch4));
                }
            }

        }
            
            
    }
}
