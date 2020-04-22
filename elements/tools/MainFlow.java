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
//* See the GNU Lesser General Public License for more details.              *
//*                                                                           *
//* You should have received a copy of the GNU Lesser General Public License  *
//* along with this library; if not, write to the Free Software Foundation,   *
//* Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA                  *
//*****************************************************************************

package tools;

import VisualLogic.*;
import VisualLogic.variables.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import java.util.*;
import java.awt.geom.Rectangle2D;

public class MainFlow extends JVSMain
{
  public VSString variable = new VSString("i");
  public Font font = new Font("Courier",0,12);
  public JTextField text = new JTextField();
  public JPanel panel =null;
  public Graphics2D g2;
  public int standardWidth=130;
  public int width=130;
  public int height=53;
  public String toInclude="";

  public MainFlow()
  {

  }

  public void paint(java.awt.Graphics g)
  {
    if (element!=null)
    {
       g2=(Graphics2D)g;
       Rectangle bounds=element.jGetBounds();

        if (text!=null)
        {
          int mitteX=bounds.x+(bounds.width)/2;
          int mitteY=bounds.y+(bounds.height)/2;

          int distanceY=12;
          text.setLocation(bounds.x+5, mitteY-distanceY);
          text.setSize(bounds.width-10,2*distanceY);
        }

    }
  }
  
  public void resizeWidth()
  {
     if (g2!=null)
     {
       g2.setFont(font);
       String caption=variable.getValue();

       FontMetrics fm = g2.getFontMetrics();
       Rectangle2D   r = fm.getStringBounds(caption,g2);
       Rectangle2D   r2 = fm.getStringBounds(toInclude,g2);

       int newWidth=(int)(r.getWidth()+r2.getWidth()+40);
       if (newWidth==standardWidth) return;

       if (element!=null)
       {

         int mx=(element.jGetWidth()/2)+element.jGetLeft();

         int newX=mx-(newWidth/2);

         if (newWidth!=width)
         {
           element.jSetSize(newWidth,height);
           element.jSetLeft(newX);

           width=newWidth;
           element.jRefreshVM();
         }
       }
     }

  }


   /*public void resizeWidth()
   {
     if (g2!=null)
     {
       g2.setFont(font);
       String caption=variable.getValue();

       FontMetrics fm = g2.getFontMetrics();
       Rectangle2D   r = fm.getStringBounds(caption,g2);
       Rectangle2D   r2 = fm.getStringBounds(toInclude,g2);

       int newWidth=(int)(r.getWidth()+r2.getWidth());
       if (newWidth>standardWidth)
       {
         int diff=(width-newWidth)/2;
         element.jSetSize(newWidth,height);
         element.jSetLeft(element.jGetLeft()+diff);
         width=newWidth;
       } else
       {
         int diff=(width-standardWidth)/2;
         width=standardWidth;
         element.jSetSize(width,height);
         element.jSetLeft(element.jGetLeft()+diff);
       }
     }

   }*/

  public void mousePressedOnIdle(MouseEvent e)
  {

    if (e!=null && panel!=null)
    {
      if (e.getClickCount()==2)
      {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
              panel.remove(text);
              panel.add(text,0);
              text.setText(variable.getValue());
              text.setVisible(true);
              text.updateUI();
              element.jRepaint();

              text.requestFocus();
            }
        });

      }
    }
    
    if (panel==null)
    {
      System.out.println("PANEL == NULL!!");
    }

  }

  public void xOnInit()
  {
    panel =element.getFrontPanel();
    panel.setLayout(null);

    panel.add(text);
    
    text.setFont(font);
    text.setText(variable.getValue());

    text.setVisible(false);
    //element.setAlwaysOnTop(true);


    text.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            jTextKeyPressed(evt);
        }
    });

    text.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusLost(java.awt.event.FocusEvent evt) {
            jTextFocusLost(evt);
        }
    });

  }
  

  public void jTextKeyPressed(java.awt.event.KeyEvent evt)
  {
    if (evt.getKeyCode()==evt.VK_ESCAPE)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
              text.setVisible(false);
              element.jRepaint();
            }
        });
    }
    
    if (evt.getKeyCode()==evt.VK_ENTER)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
              variable.setValue(text.getText());
              text.setVisible(false);
              //panel.remove(text);
              element.jRepaint();
              resizeWidth();
              element.jRepaint();
              element.jRefreshVM();
              checkPinDataType();
            }
        });

    }
  }
  
  public void jTextFocusLost(java.awt.event.FocusEvent evt)
  {
      System.out.println("jTextFocusLost");
      text.setVisible(false);
      //panel.remove(text);
      element.jRepaint();

  }


  public void loadFromStream(java.io.FileInputStream fis)
  {
     variable.loadFromStream(fis);
  }


  public void saveToStream(java.io.FileOutputStream fos)
  {
    variable.saveToStream(fos);
  }

}

