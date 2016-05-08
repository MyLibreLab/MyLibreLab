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

package VisualLogic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.JPanel;



public class MyImage extends JPanel
{
    private Image image=null;
    
    public MyImage(Image image)
    {
        this.image=image;
    }
    
    public Image getImage()
    {
        return image;
    }
    
    public void setImage(Image image)
    {
        if (this.image!=null)
        {
            this.image.flush();
            this.image=null;
        }
        
        this.image=image;
        int w=image.getWidth(this);
        int h=image.getHeight(this);
        this.setSize(w,h);
        this.setPreferredSize(new Dimension(w,h));
        
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                updateUI();
            }
        });
        
        
    }
    
    public MyImage()
    {
        this.image=null;
    }
    
    
    public void loadImage(String fileName)
    {
        Image img=null;
        if (new File(fileName).exists())
        {
            img=Toolkit.getDefaultToolkit().getImage(fileName);
            MediaTracker mc = new MediaTracker(this);
            mc.addImage(img,0);
            
            try
            {
                mc.waitForID(0);
            }
            catch (InterruptedException ex)
            {
                System.out.println(ex);
            }
            
        }    
        setImage(img);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (image!=null)
        {
            g.setColor(Color.WHITE);
            g.fillRect(0,0,5000,5000);
            int x=getWidth()/2-image.getWidth(this)/2;
            int y=getHeight()/2-image.getHeight(this)/2;
            g.drawImage(image,x,y,null);
        }
    }
}
