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


package MyGraph;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 *
 * @author Homer
 */
public class Turn90Label extends JPanel
{
 
    public String text="";
    private Font stdFont = new Font("Arial",0,12);
    
    public void setFont(Font font)
    {
        stdFont=font;
        setPreferredSize(new Dimension(getFontMetrics(stdFont).getHeight(),10));
    }
    public String getText()
    {
        return text;        
    }
    public void setText(String text)
    {
        this.text=text;
        setPreferredSize(new Dimension(getFontMetrics(stdFont).getHeight(),10));
    }
    
    
        public Turn90Label( String s ) 
        {
           super( );
           text=s;
           setPreferredSize(new Dimension(getFontMetrics(stdFont).getHeight(),10));
        }

        
        public void paintComponent(Graphics g) {
           Graphics2D g2d = (Graphics2D)g;                   
           //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

           g.setColor(getForeground());
           g.setFont(stdFont);           
           g2d.translate(getFontMetrics(g.getFont()).getAscent(), (getHeight()/2)+(getFontMetrics(g.getFont()).stringWidth(text)/2 ));
           g2d.rotate( Math.toRadians(-90) );
           g2d.drawString(text, 0, 0);           
        }
} 

