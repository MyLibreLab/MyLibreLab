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
package tools;

import javax.swing.*;
import java.awt.*;

public class Properties extends JDialog
{
  private JScrollPane scroll = new JScrollPane();
  private JTextArea text=new JTextArea();
  private JButton cmdOK= new JButton("OK");
  private JButton cmdCancel= new JButton("Abbrechen");
  private JPanel panel = new JPanel();
  public static boolean result=false;
  public static String strText="";
  
  public Properties(JFrame frame,String input)
  {
    super(frame,true);
    this.setLayout(new java.awt.BorderLayout());

    strText=input;

    scroll.add(text);
    scroll.setViewportView(text);
    
    getContentPane().add(scroll,java.awt.BorderLayout.CENTER);
    getContentPane().add(panel,java.awt.BorderLayout.SOUTH);

    panel.add(cmdOK);
    panel.add(cmdCancel);

    text.setText(input);
    this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

    cmdOK.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
         result=true;
         strText=text.getText();
         dispose();
      }
    });

    cmdCancel.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
         result=false;
         dispose();
      }
    });
    
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int mitteX=(int)screenSize.getWidth() / 2;
    int mitteY=(int)screenSize.getHeight() / 2;

    setLocation(mitteX-getWidth()/2-200,mitteY-getHeight()/2-200);

    
    
  }
  
}
