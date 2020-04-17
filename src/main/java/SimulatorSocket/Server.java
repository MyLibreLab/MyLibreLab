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

package SimulatorSocket;

import java.io.*;
import java.net.*;


/**
 *
 * @author Carmelo
 */
public class Server extends Thread
{

    private ServerSocket server = null;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;
    private MyOpenLabOwnerIF owner;
    
    private Client client=null;

    public void sendCmd(String cmd)
    {
        if (client!=null)
        {
          client.sendCmd(cmd);
        }
    }
   

    public void close()
    {
        if (client!=null)
        {
          client.close();
          client=null;
        }               
    }



    public Server(MyOpenLabOwnerIF owner)
    {
        this.owner=owner;
    }
    
    
    @Override
    public void run()
    {
        try
        {
            server = new ServerSocket(1024);
            while (true)
            {
                Socket c = server.accept();
                client=new Client(c, owner);
                client.start();
                
                owner.ownerMessage("Client accepted\n");
                
                //String str=dis.readUTF();
                //System.out.println("String="+str);
                
            }
        }
        catch (IOException ex)
        {
           close();
        }

        
    }
        

}
