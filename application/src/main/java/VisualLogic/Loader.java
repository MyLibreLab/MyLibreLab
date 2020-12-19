/*
 * Copyright (C) 2020 MyLibreLab
 * Based on MyOpenLab by Carmelo Salafia www.myopenlab.de
 * Copyright (C) 2004  Carmelo Salafia cswi@gmx.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package VisualLogic;
// import com.sun.org.omg.CORBA.ExcDescriptionSeqHelper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.tinylog.Logger;

public class Loader {
    URLClassLoader cl;

    public Object ladeClasse(String elementPath, String pfad, String klassename) {
        URL url;
        Object o = null;

        try {
            url = new File(pfad).toURI().toURL();

            URL url2 = new File(elementPath).toURI().toURL();
            // URL url3 = new File("Y:\\java\\carmelo's exp Lab
            // 3\\Distribution\\Elements\\Drivers\\K8055\\bin\\TWUsb.jar").toURI().toURL();

            cl = new URLClassLoader(new URL[] {url, url2}, Thread.currentThread().getContextClassLoader());

            Class<?> c = cl.loadClass(klassename);

            o = c.newInstance();
        } catch (UnsupportedClassVersionError | InstantiationException | MalformedURLException | IllegalAccessException
                | ClassNotFoundException ex) {
            org.tinylog.Logger.error(ex);
        }
        return o;
    }

    public Object ladeClasseDriver(URL[] urls, String klassename) {
        Object o = null;
        try (URLClassLoader cl = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader())) {
            Class<?> c = cl.loadClass(klassename);
            o = c.getDeclaredConstructor().newInstance();
        } catch (UnsupportedClassVersionError | NoSuchMethodException | ClassNotFoundException | InstantiationException
                | InvocationTargetException | IllegalAccessException | IOException e) {
            Logger.error(e);
        }
        return o;
    }
}
