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

import java.awt.Dimension;
import java.io.Serializable;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 *
 * @author Homer
 */
public class Settings implements Serializable
{
    public boolean showDocWindow = true;
    public String currentDirectory=".";
    public String lookAndFeel=UIManager.getCrossPlatformLookAndFeelClassName();
    public Point docLocation = new Point(500,100);
    public Dimension docDimension = new Dimension(300,250);
    public int basisWindow_LeftWidth=120;
    public int rightSplitterPos=150;
    public String documentation_language="";
    public String elementPalettePath="";
    public String userdefinedElementsPath="";    
       
    public String language="en_US";
    public Point docWindowLocation = new Point(100,100);
    public Dimension docWindowDimension = new Dimension(100,100);
    public boolean isDocWindowsVisible=true;
    public ArrayList<String> projects = new ArrayList<String>(); // Obsolete!
   
    
    public boolean frontAlignToGrid=true;
    public boolean frontRasterOn=true;
    public int frontRasterX=10;
    public int frontRasterY=10;
    
    public boolean circuittAlignToGrid=true;
    public boolean circuitRasterOn=true;
    public int circuitRasterX=10;
    public int circuitRasterY=10;
    
    public boolean circuitCrossVisible=true;
    public boolean elementIDVisible=false;
    
    
    //public String panelDirectory="/FrontElements";
    //public String circuitDirectory="/CircuitElements";
    
    public int status=0;
    public Point circuitWindowLocation = new Point(0,0);
    public Dimension circuitWindowDimension = new Dimension(640,480);

    public String javaEditor;
    public String HTMLEditor;
    public String jdkBin="C:\\Programme\\Java\\jdk1.5.0_09\\bin";
    public String graphicEditor="mspaint.exe";
    
    public int elementSplitterHozPosition=400;
    public int elementSplitterPosition=200;
            
    public String oldProjectPath="";
    
    public String version=""; // Important to compare this Version with old Version!
    
    public Point mainFrameLocation = new Point();
    
    public Dimension mainFrameSize = new Dimension();
    
    public Point elementPaletteLocation = new Point(100, 100);
    public Dimension elementPaletteSize = new Dimension(250, 300);
    
    public String proxy_host="";
    public String proxy_port="";
    
    
    public String repository_domain="http://myopenlab.de";
    public String repository_login_username="";
    public String repository_login_password="";
    
    /** Creates a new instance of Settings */
    public Settings() {
    }
    
}
