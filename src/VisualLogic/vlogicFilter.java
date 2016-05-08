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
import java.util.Hashtable;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.filechooser.*;

public class vlogicFilter extends FileFilter 
{
 static String fileExtension = "vlogic";
 
  public boolean accept(File f) 
  {
    String s = f.getName();
    int i = s.lastIndexOf('.');
    
    if (f.isDirectory()) return true;

   if (i > 0) 
   {
     String extension = s.substring(i+1).toLowerCase();
     if (fileExtension.indexOf(extension)>-1) 
     {
        return true;
     } else 
     {
        return false;
     }
   }
  return false;
  }

 
    public String getDescription() 
    {
        return "Visual Logic dateien (*.vlogic)";
    }
}
