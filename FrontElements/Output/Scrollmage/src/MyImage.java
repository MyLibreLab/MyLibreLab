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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;



public class MyImage extends JPanel
{
    private Image image=null;

    public MyImage(Image image)
    {
      this.image=image;
    }

    public void setImage(Image image)
    {
        this.image=image;
        int w=image.getWidth(this);
        int h=image.getHeight(this);
        this.setSize(w,h);
        this.setPreferredSize(new Dimension(w,h));
        updateUI();
    }

    public MyImage()
    {
      this.image=null;
    }


    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (image!=null)
        {
            int x=getWidth()/2-image.getWidth(this)/2;
            int y=getHeight()/2-image.getHeight(this)/2;
            g.drawImage(image,x,y,null);
        }
    }
}

