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
import java.awt.*;
import tools.*;
import java.awt.geom.Rectangle2D;


public class Output extends MainFlow
{
  private Image image;
  private VSBasisIF basis;
  private VSFlowInfo in=null;
  private VSFlowInfo out = new VSFlowInfo();


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

        String caption="println("+variable.getValue()+")";

        FontMetrics fm = g2.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(caption,g2);

        g2.setColor(Color.BLACK);
        g.drawString(caption,mitteX-(int)(r.getWidth()/2),(int)(mitteY+fm.getHeight()/2)-3);

     }
     super.paint(g);
  }


  @Override
  public void init()
  {
    standardWidth=130;
    width=standardWidth;
    height=40;
    toInclude="Println()----";
    
    initPins(1,0,1,0);
    setSize(width,height);
    initPinVisibility(true,true,true,true);
    
    element.jSetInnerBorderVisibility(false);

    image=element.jLoadImage(element.jGetSourcePath()+"icon.gif");
    
    setPin(0,ExternalIF.C_FLOWINFO,ExternalIF.PIN_INPUT);
    setPin(1,ExternalIF.C_FLOWINFO,ExternalIF.PIN_OUTPUT);
    
    element.jSetResizable(false);
    element.jSetCaptionVisible(false);
    element.jSetCaption("Output");
    
    setName("#FLOWCHART_Output#");
  }

  @Override
  public void xOnInit()
  {
    super.xOnInit();
  }

  @Override
  public void initInputPins()
  {
    in=(VSFlowInfo)element.getPinInputReference(0);
    if (in==null) in=new VSFlowInfo();
  }


  @Override
  public void start()
  {
    basis=element.jGetBasis();
  }

  @Override
  public void initOutputPins()
  {
    element.setPinOutputReference(1,out);
  }

  @Override
  public void process()
  {
    basis.vsEvaluate(in,"basis.baseprintln("+variable.getValue()+")");
    //Object o= basis.vsGetVar(toInclude)
    //element.jPrintln(o.toString());
    out.copyValueFrom(in);
    element.notifyPin(1);
  }
  
  @Override
  public void loadFromStream(java.io.FileInputStream fis)
  {
    variable.loadFromStream(fis);
  }


  @Override
  public void saveToStream(java.io.FileOutputStream fos)
  {
    variable.saveToStream(fos);
  }
  
}