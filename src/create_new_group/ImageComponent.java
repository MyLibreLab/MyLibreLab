package create_new_group;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;

class ImageComponent extends JPanel {
  
    private BufferedImage image;
    private String filename="";
    
    public void setFilename(String filename){
        try{
            this.filename=filename;
            File image2 = new File(filename);
            image = ImageIO.read(image2);

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    
    public String getFilename() {
        return filename;
    }
    
    
    public ImageComponent(){
        super();        
    }
      
    @Override
    public void paint(Graphics g){
        super.paintComponent(g);
        if(image == null) return;
        g.drawImage(image, 2, 2, this);        
    }

}