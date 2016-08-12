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
import java.awt.geom.Rectangle2D;



public class SetterEval extends MainFlow
{
  private Image image;
  private VSFlowInfo in;
  private VSFlowInfo out= new VSFlowInfo();
  private Color color=Color.BLACK;
  private javax.swing.Timer timer;
  private boolean started=false;
  private VSBasisIF basis;
  private VSObject vv=null;



  @Override
  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }


  public String getValue()
  {
    return variable.getValue();
  }

  @Override
  public void paint(java.awt.Graphics g)
  {
  if (element!=null)
     {
        Rectangle bounds=element.jGetBounds();
        Graphics2D g2 = (Graphics2D) g;

        g2.setFont(font);

        int mitteX=bounds.x+(bounds.width)/2;
        int mitteY=bounds.y+(bounds.height)/2;

        int distanceY=10;

        g2.setColor(new Color(204,204,255));
        g2.fillRect(bounds.x,mitteY-distanceY,bounds.width,2*distanceY);
        g2.setColor(Color.BLACK);
        g2.drawRect(bounds.x,mitteY-distanceY,bounds.width,2*distanceY);

        String caption=getValue();

        FontMetrics fm = g2.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(caption,g2);

        g2.setColor(Color.BLACK);
        g.drawString(caption,mitteX-(int)(r.getWidth()/2),(int)(mitteY+fm.getHeight()/2)-3);

     }    //drawImageCentred(g,image);
    
     super.paint(g);
  }

  @Override
  public void init()
  {
    standardWidth=130;
    width=standardWidth;
    height=40;
    toInclude="____=";

    initPins(1,0,1,0);
    setSize(width,height);
    element.jSetInnerBorderVisibility(false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.png");
    
    element.jInitPins();
    
    setPin(0,ExternalIF.C_FLOWINFO,element.PIN_INPUT);
    setPin(1,ExternalIF.C_FLOWINFO,element.PIN_OUTPUT);

    
    variable.setValue("i=i+1");
    setName("#FLOWCHART_TURN_SetterEval#");

  }
  
  @Override
  public void xOnInit()
  {
    super.xOnInit();
  }
  
  @Override
  public void start()
  {
    basis=element.jGetBasis();
  }


  @Override
  public void initInputPins()
  {
    in=(VSFlowInfo)element.getPinInputReference(0);
    if (in==null) in= new VSFlowInfo();
  }

  @Override
  public void initOutputPins()
  {
    element.setPinOutputReference(1,out);
  }
  
  

  

  @Override
  public void process()
  {
    if (basis!=null)
    {
      basis.vsEvaluate(in,variable.getValue());
      out.copyValueFrom(in);
      element.notifyPin(1);

    }

  }
  



}

