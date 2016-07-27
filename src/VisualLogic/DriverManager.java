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

import VisualLogic.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

class MyOpenLabDriver {

    public String driverName = "";
    public String driverPath = "";
    public Object owner = null;
    public MyOpenLabDriverIF driver;

    public MyOpenLabDriver(String driverName, String driverPath) {
        this.driverName = driverName;
        this.driverPath = driverPath;

        DriverInfo info = Tools.openDriverInfo(new File(driverPath));

        if (info != null) {
            Loader loader = new Loader();
            try {
                URL url1 = new File(driverPath + info.classpath).toURI().toURL();
                URL url2 = new File(driverPath + info.Jar).toURI().toURL();

                driver = (MyOpenLabDriverIF) loader.ladeClasseDriver(new URL[]{url1, url2}, info.Classe);
                System.out.println("Driver loaded : " + driverName);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
        }
    }

}

public class DriverManager {

    // Haelt alle Treiber in der Liste driver
    private ArrayList<MyOpenLabDriver> drivers = new ArrayList<MyOpenLabDriver>();
    private FrameMain frameCircuit;

    public DriverManager(FrameMain frameCircuit) {
        this.frameCircuit = frameCircuit;
    }

    private MyOpenLabDriver findDriver(String driverName) {
        MyOpenLabDriver d;
        for (int i = 0; i < drivers.size(); i++) {
            d = drivers.get(i);
            if (d.driverName.equalsIgnoreCase(driverName)) {
                return d;
            }
        }
        return null;
    }

    // --------------    Public    ---------------------------------------------
    /*
     * If result = null : Driver not found or already Opened
     * from another Component
     */
    public MyOpenLabDriverIF openDriver(Element element, String driverName, ArrayList args) {
        try {
            MyOpenLabDriver driver = findDriver(driverName);
            //if (driver!=null && driver.owner==null)
            {
                if (driver.owner != element) {
                    driver.owner = element;
                    driver.driver.driverStart(args);
                    return driver.driver;
                }
                /*else
            {
                return null;
            }*/
            }
        } catch (Exception ex) {
            System.out.println("Errorin openDriver: " + ex);
            return null;
        }

        return null;
    }

    public void closeDriver(Element element, String driverName) {
        MyOpenLabDriver driver = findDriver(driverName);
        if (driver != null && driver.owner != null) {
            if (driver.owner == element) {
                driver.driver.driverStop();
                driver.owner = null;
            }
        }
    }

    /*
     *  if Result = true anithing OK and Driver registred!
     *  if Result = false : driver already registred
     */

    public boolean registerDriver(String driverPath) {
        File file = new File(driverPath);

        String driverName = file.getName();

        MyOpenLabDriver d = findDriver(driverName);
        if (d == null) {
            drivers.add(new MyOpenLabDriver(driverName, driverPath));
            return true;
        } else {
            return false;
        }
    }

}
