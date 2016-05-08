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

import de.myopenlab.update.frmUpdate;
import java.awt.*;
import java.util.Locale;
import javax.swing.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Homer
 */
public class Tools
{

    public static int appResult = 0;
    public static DriverManager driverManager;
    public static DialogWait dialogWait;
    public static String elementPath = "";
    public static String userElementPath = "";

    /** Creates a new instance of Tools */
    public Tools()
    {
    }

    public static String bereinigeDateiname(String name) {
        
        return name.replaceAll("[^A-Za-z0-9+-]", "_");
        
    }
    
    public static String readFile(String filename) {
        String content = null;
        File file = new File(filename); //for ex foo.txt
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(DialogSaveAsModul.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return content;
    }
    
    
    public static String mapFile(String fileName)
    {
        fileName = new File(fileName).getAbsolutePath();

        int index1 = fileName.indexOf("CircuitElements\\2user-defined");
        if (index1 > 0)
        {

            String f1 = new File(elementPath + "\\CircuitElements\\2user-defined").getAbsolutePath();
            String f2 = new File(fileName).getAbsolutePath();

            String str = f2.substring(f1.length());
            fileName = userElementPath + "\\CircuitElements\\" + str;
        }

        int index2 = fileName.indexOf("FrontElements\\2user-defined");
        if (index2 > 0)
        {

            String f1 = new File(elementPath + "\\FrontElements\\2user-defined").getAbsolutePath();
            String f2 = new File(fileName).getAbsolutePath();

            String str = f2.substring(f1.length());
            fileName = userElementPath + "\\FrontElements\\" + str;
        }

        return fileName;

    }
    public static Settings settings;

    public static boolean setQuestionDialog(JFrame parent, String s)
    {
        int res = JOptionPane.showOptionDialog(parent, s, java.util.ResourceBundle.getBundle("VisualLogic/Basic").getString("attention"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (res == JOptionPane.NO_OPTION)
        {
            return false;
        }
        if (res == JOptionPane.CANCEL_OPTION)
        {
            return false;
        }
        return true;
    }

    public static String generateNewFileName(String filename)
    {
        String newFilename;

        newFilename = filename;
        if ((new File(newFilename)).exists())
        {
            int i = 1;
            while (true)
            {
                newFilename = filename + "_" + i;
                if (!(new File(newFilename)).exists())
                {
                    break;
                }
                i++;
            }
        }

        return newFilename;
    }

    public static boolean compileFile(String elementPath, String srcFile, String destPath, String classpath)
    {

        JavaCompiler jc = null;
        StandardJavaFileManager sjfm = null;
        try
        {

            jc = ToolProvider.getSystemJavaCompiler();
            sjfm = jc.getStandardFileManager(null, null, null);

        }
        catch (Exception ex)
        {
            showMessage("To compile you need Java JDK 1.6 or higher.\nPlease start with JDK.");
            return false;
        }

        try
        {
            File javaFile = new File(srcFile);
            // getJavaFileObjects' param is a vararg
            Iterable fileObjects = sjfm.getJavaFileObjects(javaFile);

            ArrayList<String> options = new ArrayList<String>();
            options.add("-d");
            options.add(destPath + "/bin");
            options.add("-cp");
            options.add(destPath + "/src" + ";.;" + elementPath + ";" + destPath + "/" + classpath);


            StringWriter out = new StringWriter();
            jc.getTask(out, sjfm, null, options, null, fileObjects).call();


            if (!out.toString().trim().equals(""))
            {
                System.err.println(out.toString());
            }
            else
            {
                System.err.println("File \"" + new File(srcFile).getName() + "\" compiled.\n");
            }
            out.close();


            // Add more compilation tasks
            sjfm.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return false;

    }

    public static String generateNewFileName(String filename, String extension)
    {
        String newFilename;

        newFilename = filename + "." + extension;
        if ((new File(newFilename)).exists())
        {
            int i = 1;
            while (true)
            {
                newFilename = filename + "_" + i + "." + extension;
                if (!(new File(newFilename)).exists())
                {
                    break;
                }
                i++;
            }
        }

        return newFilename;
    }

    public static void openPaint(File file)
    {
        String cmd = "";
        cmd = "cmd /c start " + Tools.settings.graphicEditor + " \"" + file.getAbsolutePath() + "\"";

        try
        {
            Runtime.getRuntime().exec(cmd);
        }
        catch (IOException bx)
        {
            Tools.showMessage(bx.toString());
        } 
    }

    public static boolean isProject(String[] children)
    {
        if (children != null)
        {
            for (int i = 0; i < children.length; i++)
            {
                if (children[i].equalsIgnoreCase("project.myopenlab"))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isExecutableProject(String[] children)
    {
        if (children != null)
        {
            for (int i = 0; i < children.length; i++)
            {
                if (children[i].equalsIgnoreCase("myopenlab.executable"))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static ArrayList<String> readListFromFile(String filename)
    {
        ArrayList<String> result = new ArrayList<String>();
        BufferedReader br;
        try
        {
            br = new BufferedReader(new FileReader(filename));

            String line = null;
            try
            {
                while ((line = br.readLine()) != null)
                {
                    result.add(line);
                }

            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }

        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }

        return result;
    }

    public static ArrayList<String> listDrivers(String driverPath)
    {
        ArrayList<String> result = new ArrayList<String>();

        File[] files = new File(driverPath).listFiles();

        for (int i = 0; i < files.length; i++)
        {
            File f = files[i];
            if (f.isDirectory())
            {
                DriverInfo props = Tools.openDriverInfo(f);

                if (props != null)
                {
                    result.add(f.getAbsolutePath());
                }
            }
        }

        return result;
    }

    public static void showMessage(String message)
    {
        JOptionPane.showMessageDialog(null, message, java.util.ResourceBundle.getBundle("VisualLogic/Basic").getString("attention"), JOptionPane.ERROR_MESSAGE);
    }

    public static void showMessage(Component parent, String message)
    {
        JOptionPane.showMessageDialog(parent, message, java.util.ResourceBundle.getBundle("VisualLogic/Basic").getString("attention"), JOptionPane.ERROR_MESSAGE);
    }

    public static void showMessage(Component parent, String message, int messageType)
    {
        JOptionPane.showMessageDialog(parent, message, java.util.ResourceBundle.getBundle("VisualLogic/Basic").getString("attention"), messageType);
    }

    public static void jException(Basis basis, String text)
    {
        basis.stop();
        if (basis.frameCircuit != null)
        {
            basis.frameCircuit.addMessageToConsoleErrorWarnings(text);
        }
    }

    public static void openUrl(JFrame parent, String strURL)
    {
        try
        {
            Desktop.getDesktop().browse(new URI(strURL));
        }
        catch (URISyntaxException ex)
        {
            showMessage(parent, ex.toString());
        }
        catch (IOException ex)
        {
            showMessage(parent, ex.toString());
        }

    }

    public static boolean editFile(JFrame parent, File file)
    {

        try
        {
            Desktop.getDesktop().edit(file);
            return true;
        }
        catch (IOException ex)
        {
            showMessage("Operation System not Support edit for this Type of File");
            return false;
        }

    }

    public static void openFileImage(JFrame parent, File file)
    {
        FrameImageViewer frm = new FrameImageViewer(file);
        frm.setIconImage(parent.getIconImage());
        frm.setVisible(true);

    }

    /* public static void openFile(JFrame parent,File file) {
    // Bug in Java 6-> nicht nutzen!
    if (Desktop.getDesktop().isDesktopSupported())
    {
    try 
    {
    Desktop.getDesktop().open(file);
    } catch (IOException ex) {
    showMessage(parent,ex.toString());
    } catch (java.lang.IllegalArgumentException ex) {
    showMessage(parent,ex.toString());
    }
    }
    }*/
    public static String extractClassName(String line)
    {
        String ch;
        // gehe bis zum "=" Zeichen
        for (int i = 0; i < line.length(); i++)
        {
            ch = line.substring(i, i + 1);

            if (ch.equals("="))
            {
                return line.substring(0, i);
            }
        }
        return "";
    }

    public static void runApplication(String app, String param) throws IOException
    {

        String cmd = "cmd /c start " + app + " " + param;

        Runtime.getRuntime().exec(cmd);
    }

    public static void copyFile(File source, File dest) throws IOException
    {
        FileChannel in = null, out = null;
        try
        {
            in = new FileInputStream(source).getChannel();
            out = new FileOutputStream(dest).getChannel();

            long size = in.size();
            MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0, size);

            out.write(buf);

        }
        finally
        {
            if (in != null)
            {
                in.close();
            }
            if (out != null)
            {
                out.close();
            }
        }
    }

    public static ProjectProperties openProjectFile(File file)
    {
        ProjectProperties tmp = new ProjectProperties();        

        try
        {
            BufferedReader input = new BufferedReader(new FileReader(file.getAbsolutePath() + "/" + "project.myopenlab"));
            String inputString;
            while ((inputString = input.readLine()) != null)
            {
                
                if (inputString.trim().length()>0)
                {
                    String elementClass = extractClassName(inputString);
                    String elementName = inputString.substring(elementClass.length());

                    elementName = elementName.trim();

                    elementName = elementName.substring(1);
                    elementClass = elementClass.trim();
                    elementName = elementName.trim();

                    if (elementClass.equalsIgnoreCase("MAINVM"))
                    {
                        tmp.mainVM = elementName;
                    }
                    if (elementClass.equalsIgnoreCase("PROJECTTYPE"))
                    {
                        tmp.projectType= elementName;
                    }
                }

            }

            input.close();
        }
        catch (Exception ex)
        {
            Tools.showMessage(ex.toString());
        }
        return tmp;
    }

    public static void saveProjectFile(File file, ProjectProperties props)
    {
        try
        {
            BufferedWriter out = new BufferedWriter(new FileWriter(file.getAbsolutePath() + "/" + "project.myopenlab"));

            out.write("MAINVM          = " + props.mainVM);
            out.newLine();
            out.write("PROJECTTYPE     = " + props.projectType);
            out.newLine();

            out.close();
        }
        catch (Exception ex)
        {
            Tools.showMessage(ex.toString());
        }

    }

    public static DriverInfo openDriverInfo(File file)
    {
        DriverInfo tmp = new DriverInfo();

        String str;

        try
        {
            BufferedReader input = new BufferedReader(new FileReader(file.getAbsolutePath() + "/" + "driver.info"));
            String inputString;
            while ((inputString = input.readLine()) != null)
            {
                if (!inputString.equalsIgnoreCase(""))
                {
                    String elementClass = extractClassName(inputString);
                    String elementName = inputString.substring(elementClass.length());

                    elementName = elementName.trim();
                    elementName = elementName.substring(1);
                    elementClass = elementClass.trim();
                    elementName = elementName.trim();

                    if (elementClass.equalsIgnoreCase("JAR"))
                    {
                        tmp.Jar = elementName;
                    }
                    if (elementClass.equalsIgnoreCase("CLASS"))
                    {
                        tmp.Classe = elementName;
                    }
                    if (elementClass.equalsIgnoreCase("CLASSPATH"))
                    {
                        tmp.classpath = elementName;
                    }
                    if (elementClass.equalsIgnoreCase("CLASSPATH2"))
                    {
                        tmp.classpath2 = elementName;
                    }
                    if (elementClass.equalsIgnoreCase("Copyrights"))
                    {
                        tmp.Copyrights = elementName;
                    }
                    if (elementClass.equalsIgnoreCase("Website"))
                    {
                        tmp.Website = elementName;
                    }
                    if (elementClass.equalsIgnoreCase("Licence"))
                    {
                        tmp.Lizenz = elementName;
                    }
                }
            }

            input.close();
        }
        catch (Exception ex)
        {
            Tools.showMessage(ex.toString());
        }
        return tmp;
    }

    public static void saveDefinitionFile(File file, DFProperties definition_def)
    {
        try
        {
            BufferedWriter out = new BufferedWriter(new FileWriter(file.getAbsolutePath() + "/" + "definition.def"));

            String value = "";
            String full = "";

            if (definition_def.isDirectory)
            {
                value = "TRUE";
            }
            else
            {
                value = "FALSE";
            }
            if (definition_def.resizeSynchron)
            {
                full = "TRUE";
            }
            else
            {
                full = "FALSE";
            }

            out.write("ISDIRECTORY     = " + value);
            out.newLine();
            out.write("REDIRECT        = " + definition_def.redirect);
            out.newLine();
            out.write("LOADER          = " + definition_def.loader);
            out.newLine();
            out.write("CAPTION         = " + definition_def.captionDE);
            out.newLine();
            out.write("CAPTION_EN      = " + definition_def.captionEN);
            out.newLine();
            out.write("CAPTION_ES      = " + definition_def.captionES);
            out.newLine();
            out.write("VM              = " + definition_def.vm);
            out.newLine();
            out.write("VM_DIR_EDITABLE = " + definition_def.vm_dir_editable);
            out.newLine();
            out.write("CLASSCIRCUIT    = " + definition_def.classcircuit);
            out.newLine();
            out.write("CLASSFRONT      = " + definition_def.classfront);
            out.newLine();
            out.write("ICON            = " + definition_def.iconFilename);
            out.newLine();
            out.write("CLASSPATH       = " + definition_def.classPath);
            out.newLine();
            out.write("CLASSPATH2      = " + definition_def.classPath2);
            out.newLine();
            out.write("ELEMENTIMAGE    = " + definition_def.elementImage);
            out.newLine();
            out.write("RESIZESYNCHRON  = " + full);
            out.newLine();
            if (definition_def.showInnerborder)
            {
                value = "TRUE";
            }
            else
            {
                value = "FALSE";
            }
            out.write("SHOWINNERBORDER = " + value);
            out.close();
        }
        catch (Exception ex)
        {
            Tools.showMessage(ex.toString());
        }

    }

    public static DFProperties getProertiesFromDefinitionFile(File file)
    {
        DFProperties tmp = new DFProperties();

        String str;

        try
        {
            BufferedReader input = new BufferedReader(new FileReader(file.getAbsolutePath() + "/" + "definition.def"));
            String inputString;
            while ((inputString = input.readLine()) != null)
            {
                String elementClass = extractClassName(inputString);
                String elementName = inputString.substring(elementClass.length());

                elementName = elementName.trim();
                if (elementClass.trim().length() > 0)
                {

                    elementName = elementName.substring(1);
                    elementClass = elementClass.trim();
                    elementName = elementName.trim();

                    if (elementClass.equalsIgnoreCase("LOADER"))
                    {
                        tmp.loader = elementName;
                    }
                    else if (elementClass.equalsIgnoreCase("RESIZESYNCHRON"))
                    {
                        if (elementName.equalsIgnoreCase("true"))
                        {
                            tmp.resizeSynchron = true;
                        }
                        else
                        {
                            tmp.resizeSynchron = false;
                        }
                    }
                    else if (elementClass.equalsIgnoreCase("SHOWINNERBORDER"))
                    {
                        if (elementName.equalsIgnoreCase("true"))
                        {
                            tmp.showInnerborder = true;
                        }
                        else
                        {
                            tmp.showInnerborder = false;
                        }
                    }
                    else if (elementClass.equalsIgnoreCase("isdirectory"))
                    {
                        if (elementName.equalsIgnoreCase("true"))
                        {
                            tmp.isDirectory = true;
                        }
                        else
                        {
                            tmp.isDirectory = false;
                        }
                    }
                    else if (elementClass.equalsIgnoreCase("vm"))
                    {
                        tmp.vm = elementName;
                    }
                    else if (elementClass.equalsIgnoreCase("vm_dir_editable"))
                    {
                        tmp.vm_dir_editable = elementName;
                    }
                    else if (elementClass.equalsIgnoreCase("classcircuit"))
                    {
                        tmp.classcircuit = elementName;
                    }
                    else if (elementClass.equalsIgnoreCase("REDIRECT"))
                    {
                        tmp.redirect = elementName;
                    }
                    else if (elementClass.equalsIgnoreCase("classfront"))
                    {
                        tmp.classfront = elementName;
                    }
                    else if (elementClass.equalsIgnoreCase("caption"))
                    {
                        tmp.captionDE = elementName;
                    }
                    else if (elementClass.equalsIgnoreCase("caption_en"))
                    {
                        tmp.captionEN = elementName;
                    }
                    else if (elementClass.equalsIgnoreCase("caption_es"))
                    {
                        tmp.captionES = elementName;
                    }
                    else if (elementClass.equalsIgnoreCase("icon"))
                    {
                        tmp.iconFilename = elementName;
                    }
                    if (elementClass.equalsIgnoreCase("CLASSPATH"))
                    {
                        tmp.classPath = elementName;
                    }
                    else if (elementClass.equalsIgnoreCase("CLASSPATH2"))
                    {
                        tmp.classPath2 = elementName;
                    }
                    else if (elementClass.equalsIgnoreCase("ELEMENTIMAGE"))
                    {
                        tmp.elementImage = elementName;
                    }
                }
            }

            Locale loc = Locale.getDefault();
            String strLocale = loc.toString();

            tmp.captionInternationalized = tmp.captionDE; // Standard is German

            if (strLocale.equalsIgnoreCase("en_US"))
            {
                tmp.captionInternationalized = tmp.captionEN;
            }

            if (strLocale.equalsIgnoreCase("es_ES"))
            {
                tmp.captionInternationalized = tmp.captionES;
            }


            input.close();
        }
        catch (Exception ex)
        {
        //Tools.showMessage(ex.toString());
        }
        return tmp;
    }
    
    
    
    public static DFProperties getProertiesFromDefinitionString(String definition_def)
    {
        DFProperties tmp = new DFProperties();

        try
        {                        
            
            String[] lines = definition_def.split("\n");
            
            for (String inputString : lines) {
                

                String elementClass = extractClassName(inputString);
                String elementName = inputString.substring(elementClass.length());

                elementName = elementName.trim();
                if (elementClass.trim().length() > 0)
                {

                    elementName = elementName.substring(1);
                    elementClass = elementClass.trim();
                    elementName = elementName.trim();

                    if (elementClass.equalsIgnoreCase("LOADER"))
                    {
                        tmp.loader = elementName;
                    }
                    else if (elementClass.equalsIgnoreCase("RESIZESYNCHRON"))
                    {
                        if (elementName.equalsIgnoreCase("true"))
                        {
                            tmp.resizeSynchron = true;
                        }
                        else
                        {
                            tmp.resizeSynchron = false;
                        }
                    }
                    else if (elementClass.equalsIgnoreCase("SHOWINNERBORDER"))
                    {
                        if (elementName.equalsIgnoreCase("true"))
                        {
                            tmp.showInnerborder = true;
                        }
                        else
                        {
                            tmp.showInnerborder = false;
                        }
                    }
                    else if (elementClass.equalsIgnoreCase("isdirectory"))
                    {
                        if (elementName.equalsIgnoreCase("true"))
                        {
                            tmp.isDirectory = true;
                        }
                        else
                        {
                            tmp.isDirectory = false;
                        }
                    }
                    else if (elementClass.equalsIgnoreCase("vm"))
                    {
                        tmp.vm = elementName;
                    }
                    else if (elementClass.equalsIgnoreCase("vm_dir_editable"))
                    {
                        tmp.vm_dir_editable = elementName;
                    }
                    else if (elementClass.equalsIgnoreCase("classcircuit"))
                    {
                        tmp.classcircuit = elementName;
                    }
                    else if (elementClass.equalsIgnoreCase("REDIRECT"))
                    {
                        tmp.redirect = elementName;
                    }
                    else if (elementClass.equalsIgnoreCase("classfront"))
                    {
                        tmp.classfront = elementName;
                    }
                    else if (elementClass.equalsIgnoreCase("caption"))
                    {
                        tmp.captionDE = elementName;
                    }
                    else if (elementClass.equalsIgnoreCase("caption_en"))
                    {
                        tmp.captionEN = elementName;
                    }
                    else if (elementClass.equalsIgnoreCase("caption_es"))
                    {
                        tmp.captionES = elementName;
                    }
                    else if (elementClass.equalsIgnoreCase("icon"))
                    {
                        tmp.iconFilename = elementName;
                    }
                    if (elementClass.equalsIgnoreCase("CLASSPATH"))
                    {
                        tmp.classPath = elementName;
                    }
                    else if (elementClass.equalsIgnoreCase("CLASSPATH2"))
                    {
                        tmp.classPath2 = elementName;
                    }
                    else if (elementClass.equalsIgnoreCase("ELEMENTIMAGE"))
                    {
                        tmp.elementImage = elementName;
                    }
                }
            }

            Locale loc = Locale.getDefault();
            String strLocale = loc.toString();

            tmp.captionInternationalized = tmp.captionDE; // Standard is German

            if (strLocale.equalsIgnoreCase("en_US"))
            {
                tmp.captionInternationalized = tmp.captionEN;
            }

            if (strLocale.equalsIgnoreCase("es_ES"))
            {
                tmp.captionInternationalized = tmp.captionES;
            }

            
        }
        catch (Exception ex)
        {
        //Tools.showMessage(ex.toString());
        }
        return tmp;
    }
    

    // Liefert eine zahl > -1 wenn erfolgreich fuer den PolyLine Index
    public static int isPointInDrahtPoint(Draht draht, int x, int y)
    {
        PolyPoint p;
        for (int i = 0; i < draht.getPolySize(); i++)
        {
            p = draht.getPoint(i);

            if (x > p.getX() - 5 && y > p.getY() - 5 && x < p.getX() + 5 && y < p.getY() + 5)
            {
                return i;
            }
        }
        return -1;
    }

    // ermittelt den Index of the PolyPoint implements Draht
    // der sich in der Naechsten Umbegung von p1 befindet
    // liefert -1 falls Punkt nicht gefunden!
    public static int getPolyPointIndex(Draht draht, Point p1)
    {
        for (int i = 0; i < draht.getPolySize(); i++)
        {
            PolyPoint p = draht.getPoint(i);
            if (Math.abs(p.getX() - p1.x) < 2 && Math.abs(p.getY() - p1.y) < 2)
            {
                return i;
            }
        }
        return -1;
    }

    public static boolean deleteDirectory(File path)
    {
        if (path.exists())
        {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++)
            {
                if (files[i].isDirectory())
                {
                    deleteDirectory(files[i]);
                }
                else
                {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    public static String getFileNameWithoutExtension(File file)
    {
        String nm = file.getName();
        String ext = getExtension(file);

        return nm.substring(0, nm.length() - ext.length() - 1);
    }

    public static String getExtension(File f)
    {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1)
        {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static void copyHighSpeed(File source, File target) throws IOException
    {
        FileChannel sourceChannel = new FileInputStream(source).getChannel();
        FileChannel targetChannel = new FileOutputStream(target).getChannel();
        sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);
        sourceChannel.close();
        targetChannel.close();
    }

    /**
     * Copy files and/or directories.
     *
     * @param src source file or directory
     * @param dest destination file or directory
     * @exception IOException if operation fails
     */
    public static void copy(File src, File dest) throws IOException
    {

        FileInputStream source = null;
        FileOutputStream destination = null;
        byte[] buffer;
        int bytes_read;

        // Make sure the specified source exists and is readable.
        if (!src.exists())
        {
            throw new IOException("source not found: " + src);
        }
        if (!src.canRead())
        {
            throw new IOException("source is unreadable: " + src);
        }

        if (src.isFile())
        {
            if (!dest.exists())
            {
                File parentdir = parent(dest);
                if (!parentdir.exists())
                {
                    parentdir.mkdir();
                }
            }
            else if (dest.isDirectory())
            {
                dest = new File(dest + File.separator + src);
            }
        }
        else if (src.isDirectory())
        {
            if (dest.isFile())
            {
                throw new IOException("cannot copy directory " + src + " to file " + dest);
            }

            if (!dest.exists())
            {
                dest.mkdir();
            }
        }

        // The following line requires that the file already
        // exists!!  Thanks to Scott Downey (downey@telestream.com)
        // for pointing this out.  Someday, maybe I'll find out
        // why java.io.File.canWrite() behaves like this.  Is it
        // intentional for some odd reason?
        //if (!dest.canWrite())
        //throw new IOException("destination is unwriteable: " + dest);

        // If we've gotten this far everything is OK and we can copy.
        if (src.isFile())
        {
            try
            {
                source = new FileInputStream(src);
                destination = new FileOutputStream(dest);
                buffer = new byte[1024];
                while (true)
                {
                    bytes_read = source.read(buffer);
                    if (bytes_read == -1)
                    {
                        break;
                    }
                    destination.write(buffer, 0, bytes_read);
                }
            }
            finally
            {
                if (source != null)
                {
                    try
                    {
                        source.close();
                    }
                    catch (IOException e)
                    {
                        ;
                    }
                }
                if (destination != null)
                {
                    try
                    {
                        destination.close();
                    }
                    catch (IOException e)
                    {
                        ;
                    }
                }
            }
        }
        else if (src.isDirectory())
        {
            String targetfile, target, targetdest;
            String[] files = src.list();

            for (int i = 0; i < files.length; i++)
            {
                targetfile = files[i];
                target = src + File.separator + targetfile;
                targetdest = dest + File.separator + targetfile;


                if ((new File(target)).isDirectory())
                {
                    copy(new File(target), new File(targetdest));
                }
                else
                {

                    try
                    {
                        source = new FileInputStream(target);
                        destination = new FileOutputStream(targetdest);
                        buffer = new byte[1024];

                        while (true)
                        {
                            bytes_read = source.read(buffer);
                            if (bytes_read == -1)
                            {
                                break;
                            }
                            destination.write(buffer, 0, bytes_read);
                        }
                    }
                    finally
                    {
                        if (source != null)
                        {
                            try
                            {
                                source.close();
                            }
                            catch (IOException e)
                            {
                                ;
                            }
                        }
                        if (destination != null)
                        {
                            try
                            {
                                destination.close();
                            }
                            catch (IOException e)
                            {
                                ;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * File.getParent() can return null when the file is specified without
     * a directory or is in the root directory. This method handles those cases.
     *
     * @param f the target File to analyze
     * @return the parent directory as a File
     */
    private static File parent(File f)
    {
        String dirname = f.getParent();
        if (dirname == null)
        {
            if (f.isAbsolute())
            {
                return new File(File.separator);
            }
            else
            {
                return new File(System.getProperty("user.dir"));
            }
        }
        return new File(dirname);
    }

    public static Polygon[] getLRDrahts(Draht draht, Line line, Point p)
    {
        Polygon poly = draht.getPolygon();

        PolyPoint startPoly = draht.getPoint(0);
        PolyPoint endPoly = draht.getPoint(draht.getPolySize() - 1);

        Point startP = new Point(startPoly.getX(), startPoly.getY());
        Point endP = new Point(endPoly.getX(), endPoly.getY());

        Polygon leftLine = Tools.copyPolygon(poly, startP, line.getStartPoint());
        Polygon rightLine = Tools.copyPolygon(poly, line.getEndPoint(), endP);


        int[] xvalues = new int[rightLine.npoints + 1];
        int[] yvalues = new int[rightLine.npoints + 1];

        System.arraycopy(rightLine.xpoints, 0, xvalues, 1, rightLine.npoints);
        System.arraycopy(rightLine.ypoints, 0, yvalues, 1, rightLine.npoints);

        xvalues[0] = p.x;
        yvalues[0] = line.getStartPoint().y;
        leftLine.addPoint(p.x, line.getStartPoint().y);

        rightLine = new Polygon(xvalues, yvalues, xvalues.length);
        /*myOut(poly);
        myOut(leftLine);
        myOut(rightLine);*/

        Polygon[] result = new Polygon[2];
        result[0] = leftLine;
        result[1] = rightLine;

        return result;
    }

    public static Polygon[] getTBDrahts(Draht draht, Line line, Point p)
    {
        Polygon poly = draht.getPolygon();

        PolyPoint startPoly = draht.getPoint(0);
        PolyPoint endPoly = draht.getPoint(draht.getPolySize() - 1);

        Point startP = new Point(startPoly.getX(), startPoly.getY());
        Point endP = new Point(endPoly.getX(), endPoly.getY());

        Polygon leftLine = Tools.copyPolygon(poly, startP, line.getStartPoint());
        Polygon rightLine = Tools.copyPolygon(poly, line.getEndPoint(), endP);

        int[] xvalues = new int[rightLine.npoints + 1];
        int[] yvalues = new int[rightLine.npoints + 1];

        System.arraycopy(rightLine.xpoints, 0, xvalues, 1, rightLine.npoints);
        System.arraycopy(rightLine.ypoints, 0, yvalues, 1, rightLine.npoints);

        xvalues[0] = line.getStartPoint().x;
        yvalues[0] = p.y;
        leftLine.addPoint(line.getStartPoint().x, p.y);


        rightLine = new Polygon(xvalues, yvalues, xvalues.length);
        /*myOut(poly);
        myOut(leftLine);
        myOut(rightLine);*/

        Polygon[] result = new Polygon[2];
        result[0] = leftLine;
        result[1] = rightLine;

        return result;
    }

    public static Element addSubVM(VMObject vmobject, String frontClass, String args[])
    {
        Element element;
        //VMElement

        element = vmobject.AddDualElement("/FrontElements/Version_2_0/VMElementUniversal", "bin", "VMElement", frontClass, args);

        return element;
    }

    /*public static Element addS(VMObject vmobject)
    {
    Element element;
    element=vmobject.AddDualElement("/CircuitElements/Extras/SubVM","bin", "SubVM", "", null);
    return element;
    }*/
    public static Element addNode(VMObject vmobject)
    {
        Element element;
        element = vmobject.AddDualElement("/CircuitElements/Node", "bin", "Node", "", null);

        return element;
    }

    public static Element addInputPin(VMObject vmobject, String[] args)
    {
        Element element;
        element = vmobject.AddDualElement("/CircuitElements/Pins/InputPin", "bin", "CElement", "", args);

        return element;
    }

    public static Element addOutputPin(VMObject vmobject, String[] args)
    {
        Element element;
        element = vmobject.AddDualElement("/CircuitElements/Pins/OutputPin", "bin", "CElement", "", args);

        return element;
    }

    public static Element addTestpoint(VMObject vmobject)
    {
        Element element;
        element = vmobject.AddDualElement("/CircuitElements/TP", "bin", "TP", "", null);

        return element;
    }

    public static void copyPoints(Polygon poly, Draht draht)
    {
        int[] xvalues = poly.xpoints;
        int[] yvalues = poly.ypoints;

        for (int i = 0; i < poly.npoints; i++)
        {
            draht.addPoint(xvalues[i], yvalues[i]);
        }
    }

    // Result is the Node-Element!
    public static void addNodeIntoLine(VMObject vmobject, Element node, Point p, Line line)
    {
        if (line == null)
        {
            return;
        }

        vmobject.owner.loading = true;
        Draht drahtX = line.getDraht();

        Element elSrc = vmobject.getElementWithID(drahtX.getSourceElementID());
        JPin pinSrc = elSrc.getPin(drahtX.getSourcePin());

        if (line.getDirection() == Line.HORIZONTAL)
        {
            vmobject.owner.loading = true;
            Polygon[] lrDrahts = getLRDrahts(drahtX, line, p);
            Polygon leftPoly = lrDrahts[0];
            Polygon rightPoly = lrDrahts[1];
            node.setLocation(p.x - (node.getWidth() / 2), line.myStart.y - (node.getHeight() / 2));

            // 1. Delete old Draht
            // 2. verbinde LDraht mit Source und Node
            // 3. verbinde RDraht mit Node und Dest


            Draht leftDraht;
            Draht rightDraht;


            Draht draht = drahtX.clone();
            vmobject.deleteDraht(drahtX);

            if (line.myStart.x < line.myEnd.x)
            {
                leftDraht = vmobject.addDrahtIntoCanvas(draht.getSourceElementID(), draht.getSourcePin(), node.getID(), 3);
                rightDraht = vmobject.addDrahtIntoCanvas(node.getID(), 1, draht.getDestElementID(), draht.getDestPin());

                node.getPin(1).draht = rightDraht;
                node.getPin(3).draht = leftDraht;

                node.getPin(3).pinIO = JPin.PIN_INPUT;
                node.getPin(1).pinIO = JPin.PIN_OUTPUT;

            }
            else
            {
                leftDraht = vmobject.addDrahtIntoCanvas(draht.getSourceElementID(), draht.getSourcePin(), node.getID(), 1);
                rightDraht = vmobject.addDrahtIntoCanvas(node.getID(), 3, draht.getDestElementID(), draht.getDestPin());
                node.getPin(3).draht = rightDraht;
                node.getPin(1).draht = leftDraht;

                node.getPin(1).pinIO = JPin.PIN_INPUT;
                node.getPin(3).pinIO = JPin.PIN_OUTPUT;

            }

            Element srcElement = vmobject.getElementWithID(draht.getSourceElementID());
            Element dstElement = vmobject.getElementWithID(draht.getDestElementID());
            JPin srcPin = srcElement.getPin(draht.getSourcePin());
            JPin dstPin = dstElement.getPin(draht.getDestPin());

            srcPin.draht = leftDraht;
            dstPin.draht = rightDraht;
            //Element destElement = vmobject.getElementWithID(draht.getDestElementID());
            //if (dstElement.getInternName().equalsIgnoreCase("###NODE###"))
            {
                //JPin destPin=destElement.getPin(rightDraht.destPin);
                dstPin.pinIO = JPin.PIN_INPUT;
                srcPin.pinIO = JPin.PIN_OUTPUT;
            }



            node.getPin(0).dataType = pinSrc.dataType;
            node.getPin(1).dataType = pinSrc.dataType;
            node.getPin(2).dataType = pinSrc.dataType;
            node.getPin(3).dataType = pinSrc.dataType;


            copyPoints(leftPoly, leftDraht);
            copyPoints(rightPoly, rightDraht);

            vmobject.owner.loading = false;

        //vmobject.reorderWireFrames();

        }
        else
        {
            vmobject.owner.loading = true;
            Polygon[] lrDrahts = getTBDrahts(drahtX, line, p);

            Polygon poly1 = lrDrahts[0];
            Polygon poly2 = lrDrahts[1];

            node.setLocation(line.myStart.x - (node.getWidth() / 2), p.y - (node.getHeight() / 2));

            // 1. Delete old Draht
            // 2. verbinde LDraht mit Source und Node
            // 3. verbinde RDraht mit Node und Dest


            Draht draht = drahtX.clone();
            vmobject.deleteDraht(drahtX);

            Draht draht1;
            Draht draht2;

            if (line.myStart.y > line.myEnd.y)
            {
                draht1 = vmobject.addDrahtIntoCanvas(draht.getSourceElementID(), draht.getSourcePin(), node.getID(), 2);
                draht2 = vmobject.addDrahtIntoCanvas(node.getID(), 0, draht.getDestElementID(), draht.getDestPin());
                node.getPin(0).draht = draht2;
                node.getPin(2).draht = draht1;

                node.getPin(2).pinIO = JPin.PIN_INPUT;
                node.getPin(0).pinIO = JPin.PIN_OUTPUT;

            }
            else
            {
                draht1 = vmobject.addDrahtIntoCanvas(draht.getSourceElementID(), draht.getSourcePin(), node.getID(), 0);
                draht2 = vmobject.addDrahtIntoCanvas(node.getID(), 2, draht.getDestElementID(), draht.getDestPin());
                node.getPin(0).draht = draht1;
                node.getPin(2).draht = draht2;

                node.getPin(2).pinIO = JPin.PIN_OUTPUT;
                node.getPin(0).pinIO = JPin.PIN_INPUT;

            }


            Element srcElement = vmobject.getElementWithID(draht.getSourceElementID());
            Element dstElement = vmobject.getElementWithID(draht.getDestElementID());
            JPin srcPin = srcElement.getPin(draht.getSourcePin());
            JPin dstPin = dstElement.getPin(draht.getDestPin());

            srcPin.draht = draht1;
            dstPin.draht = draht2;
            //if (dstElement.getInternName().equalsIgnoreCase("###NODE###"))
            {
                //JPin destPin=destElement.getPin(rightDraht.destPin);
                dstPin.pinIO = JPin.PIN_INPUT;
                srcPin.pinIO = JPin.PIN_OUTPUT;
            }


            node.getPin(0).dataType = pinSrc.dataType;
            node.getPin(1).dataType = pinSrc.dataType;
            node.getPin(2).dataType = pinSrc.dataType;
            node.getPin(3).dataType = pinSrc.dataType;



            copyPoints(poly1, draht1);
            copyPoints(poly2, draht2);

            vmobject.owner.loading = false;
        //vmobject.deleteDraht(draht);

        }
        vmobject.owner.loading = false;

        vmobject.owner.saveForUndoRedo();


    }

    public static Polygon copyPolygon(Polygon source, Point startPoint, Point endPoint)
    {
        int[] xvalues = source.xpoints;
        int[] yvalues = source.ypoints;
        Polygon result = new Polygon();
        int x, y;
        boolean started = false;
        for (int i = 0; i < source.npoints; i++)
        {
            x = xvalues[i];
            y = yvalues[i];

            if (started == false && startPoint.x == x && startPoint.y == y)
            {
                started = true;
            }
            if (started)
            {
                result.addPoint(x, y);
            }
            if (started == true && endPoint.x == x && endPoint.y == y)
            {
                return result;
            }

        }

        return result;
    }

    public static void saveProjectsFile(File file, ArrayList<String> liste)
    {
        try
        {
            BufferedWriter out = new BufferedWriter(new FileWriter(file));

            for (int i = 0; i < liste.size(); i++)
            {
                out.write(liste.get(i));
                out.newLine();
            }

            out.close();
        }
        catch (Exception ex)
        {
            Tools.showMessage(ex.toString());
        }

    }

    public static String loadTextFile(File file)
    {
        String result = "";

        if (file.exists())
        {
            try
            {
                BufferedReader input = new BufferedReader(new FileReader(file));
                String inputString;
                while ((inputString = input.readLine()) != null)
                {
                    result = inputString;
                    break;
                }
                input.close();
            }
            catch (Exception ex)
            {
                Tools.showMessage(ex.toString());
            }
        }
        return result;
    }

    public static void saveText(File file, String text)
    {
        try
        {
            BufferedWriter out = new BufferedWriter(new FileWriter(file));

            for (int i = 0; i < text.length(); i++)
            {
                String ch = text.substring(i, i + 1);
                if (ch.equalsIgnoreCase("\n"))
                {
                    out.newLine();
                }
                else
                {
                    out.write(ch);
                }
            }


            out.close();
        }
        catch (Exception ex)
        {
            Tools.showMessage(ex.toString());
        }

    }

    public static ArrayList<String> loadProjectsFile(File file)
    {
        ArrayList<String> liste = new ArrayList<String>();

        String str;

        try
        {
            BufferedReader input = new BufferedReader(new FileReader(file));
            String inputString;
            while ((inputString = input.readLine()) != null)
            {
                liste.add(inputString);
            }
            input.close();
        }
        catch (Exception ex)
        {
            Tools.showMessage(ex.toString());
        }
        return liste;
    }


    public static String getInfoXMLCaption(File file) {
        File f = new File(file.getAbsoluteFile() + "/info.xml");
        if (f.exists()) {
            //files.add(file.getName());

            //String directory = myopenlabpath + "/Elements/" + type;
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document;
                document = builder.parse(f.getAbsoluteFile());

                NodeList nodes = document.getFirstChild().getChildNodes();

                String caption = "";
                String caption_de = "";
                String caption_en = "";
                String caption_es = "";
    
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node node = nodes.item(i);

                    String name = node.getNodeName();
                    String value = node.getTextContent();

                    if (name.equalsIgnoreCase("caption")) {
                        caption_de = value;
                    }
                    if (name.equalsIgnoreCase("caption_en")) {
                        caption_en = value;
                    }
                    if (name.equalsIgnoreCase("caption_es")) {
                        caption_es = value;
                    }

                    Locale locale = Locale.getDefault();
                    String lang = locale.getLanguage();
                    switch (lang) {
                        case "de": {
                            caption = caption_de;
                        }
                        case "en": {
                            caption = caption_en;
                        }
                        case "es": {
                            caption = caption_es;
                        }
                    }

                }

                return caption;
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(frmUpdate.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXException ex) {
                Logger.getLogger(frmUpdate.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(frmUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return "";
    }
    
    
    
    public static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }
}