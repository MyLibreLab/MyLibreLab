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


package SimpleFileSystem;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 
 * @author Carmelo Salafia
 */
public class Main 
{
    
    /** Creates a new instance of Main */
    public Main() 
    {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
       FileSystemOutput fso= new FileSystemOutput("c:/test.dat");
              
       FileOutputStream fos=fso.addItem("Homer");
             
       try
       {
         fos.write(255);
         fos.write(216);
         fos.write(2);
         fos.write(31);
       } catch(Exception ex)
       {
           System.out.println(ex);
       }
       fso.postItem();

       fos=fso.addItem("Simpson");
       try
       {
         fos.write(21);
         fos.write(239);
       } catch(Exception ex)
       {
           System.out.println(ex);
       }
       fso.postItem();
       
       fso.close();
       
       
       FileSystemInput fsIn = new FileSystemInput("c:/test.dat");
       
       for (int i=0;i<fsIn.indexListSize();i++)
       {
         SFileDescriptor dt=fsIn.getFileDescriptor(i);
         
         System.out.println("FName ="+dt.filename);
         System.out.println("Pos   ="+dt.position);
         System.out.println("Size   ="+dt.size);
       }
       
       FileInputStream fis= fsIn.gotoItem(0);
       

       fis= fsIn.gotoItem(1);
       
       try
       {
         System.out.println("Value1="+fis.read());
         System.out.println("Value2="+fis.read());
       } catch(Exception ex)
       {
           
       }
       
    }
    
}
