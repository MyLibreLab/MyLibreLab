package VisualLogic;

import java.io.File;
import java.net.URL;

public class MyOpenLabDriver {
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

