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

import java.io.File;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;



public class JavaFileView extends FileView
{
    
    private Icon projectIcon= new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/folder16x16_project.png"));
    private Icon dirIcon= new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/folder16x16.png"));
    private Icon executableIcon= new javax.swing.ImageIcon(getClass().getResource("/Bilder/16x16/projectExecutable16x16.png"));
    
    public JavaFileView()
    {
        
    }
    
    /**
     * If .java file, add length to name
     */
    public String getName(File file)
    {
        return super.getName(file);
    }
    
    /**
     * Return special icons for .java and .class files
     */
    public Icon getIcon(File file)
    {
        // default icons for all directories
                
        if (file.isDirectory() && FileSystemView.getFileSystemView().isDrive(file)==false)
        {
            if (new File(file.getPath()+"/myopenlab.executable").exists())
            {
                return executableIcon;
            }
            if (new File(file.getPath()+"/project.myopenlab").exists())
            {
                return projectIcon;
            }
            else
            {
                return dirIcon;
            }
        }
        return null;
    }
}