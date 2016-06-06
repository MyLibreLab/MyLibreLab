/*
MyOpenLab by Carmelo Salafia www.myopenlab.de
Copyright (C) 2004  Carmelo Salafia cswi@gmx.de

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


package MyGraph;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 *
 * @author Homer
 */
public class Turn90Label extends JPanel
{
 
    public String text="";
    private Font stdFont = new Font("Arial",0,12);
    
    @Override
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

