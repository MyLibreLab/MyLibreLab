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
import java.net.Socket;

/**
 *
 * @author Carmelo
 */
public class Client extends Thread
{
    
    private DataInputStream dis;
    private DataOutputStream dos;
    private Socket client;
    private MyOpenLabOwnerIF owner;
    private boolean stopped=false;
    
    public Client(Socket client, MyOpenLabOwnerIF owner)
    {
        this.owner=owner;
        try
        {
            this.client = client;
            dis = new DataInputStream(client.getInputStream());
            dos = new DataOutputStream(client.getOutputStream());
        }
        catch (IOException ex)
        {
            close();
        }

        
    }

  public void sendCmd(String cmd)
    {
        try
        {
            byte[] values=cmd.getBytes();
            for (int i=0;i<values.length;i++)
            {
             dos.writeByte(values[i]);
            }            
        }
        catch (IOException ex)
        {
            close();
        }
    }    
    

    public void close()
    {
        stopped=true;
        if (client != null)
        {
            try
            {               
                dis.close();
                dos.close();
                client.close();
            }
            catch (IOException e)
            {
            }
        }
    }
    
    
    @Override
    public void run()
    {
        stopped=false;
        while(!stopped)
        {
            try
            {
                byte val = dis.readByte();
                owner.ownerMessage("" + (char)val);
            }
            catch (IOException ex)
            {
                owner.ownerMessage("Client closed\n");
                close();
            }
        }
    }
    
}
