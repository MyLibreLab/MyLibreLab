/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.myopenlab.update;

import VisualLogic.DFProperties;
import VisualLogic.DialogSaveAsModul;
import VisualLogic.FrameMain;
import VisualLogic.Tools;
import static VisualLogic.Tools.settings;
import create_new_group.Dialog_create_new_group;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.TableColumn;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import projectfolder.MyNode;
import ziputils.ZipFiles;

/**
 *
 * @author salafica
 */
public class frmUpdate extends javax.swing.JFrame {

    public VisualLogic.FrameMain owner;
    public static String myopenlabpath = "";
    private static String proxyHost = "";
    private static String proxyPort = "";
    private ArrayList<MyOpenLabRow> data = null;

    public List<MyTableRow> list1 = new ArrayList<>();
    public List<MyTableRow> list2 = new ArrayList<>();

    // HTTP GET request
    public String getStringFromUrl(String url) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        String username = settings.getRepository_login_username();
        String password = settings.getRepository_login_password();

        String userpass = username + ":" + password;
        String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());

        con.setRequestProperty("Authorization", basicAuth);

        // optional default is GET
        con.setRequestMethod("GET");

        //con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        StringBuilder response;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + "\n");
            }
        }

        return response.toString();

    }

    private ArrayList<MyOpenLabRow> getMyOpenLabRepositoryData() {

        ArrayList<MyOpenLabRow> data = new ArrayList();

        String JSON_DATA = "";
        try {

            String domain = settings.getRepository_domain();
            
            if (domain.trim().equalsIgnoreCase("http://myopenlab.de")){
                domain="https://myopenlab.de";
            }
            
            JSON_DATA = getStringFromUrl(domain + "/repository/index.php");
            System.out.println(JSON_DATA);
        } catch (Exception ex) {
            Logger.getLogger(frmUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("");
        JSONArray jsonMainArr = new JSONArray(JSON_DATA);
        for (int i = 0; i < jsonMainArr.length(); i++) {  // **line 2**
            JSONObject childJSONObject = jsonMainArr.getJSONObject(i);

            String entry_name = childJSONObject.getString("entry_name");
            String version = childJSONObject.getString("version");
            String author = childJSONObject.getString("author");
            String short_description = childJSONObject.getString("short_description");
            String dest_path = childJSONObject.getString("dest_path");
            String caption_de = childJSONObject.getString("caption");
            String caption_en = childJSONObject.getString("caption_en");
            String caption_es = childJSONObject.getString("caption_es");
            String categorie = childJSONObject.getString("categorie");
            String type = childJSONObject.getString("type");
            String date = childJSONObject.getString("date");

            MyOpenLabRow rowData = new MyOpenLabRow(entry_name, version, author, short_description, dest_path, categorie, type, date);
            rowData.setCaption_de(caption_de);
            rowData.setCaption_en(caption_en);
            rowData.setCaption_es(caption_es);
            data.add(rowData);

            System.out.println(">entry_name=" + entry_name);

            JSONObject content = childJSONObject.getJSONObject("content");

            if (content != null) {
                if (!content.isNull("item")) {
                    if (content.get("item") instanceof JSONArray) {
                        JSONArray items = content.getJSONArray("item");
                        for (int j = 0; j < items.length(); j++) {
                            if (items.get(j) instanceof JSONObject) {
                                JSONObject item = items.getJSONObject(j);

                                String name = item.getJSONObject("@attributes").getString("name");
                                String type2 = item.getJSONObject("@attributes").getString("type");

                                String caption_de2 = item.getJSONObject("@attributes").getString("caption");
                                String caption_en2 = item.getJSONObject("@attributes").getString("caption_en");
                                String caption_es2 = item.getJSONObject("@attributes").getString("caption_es");
                                //String dest_dir = item.getJSONObject("@attributes").getString("type");
                                System.out.println("name=" + name + " type=" + type2);

                                TestItem tItem = new TestItem(name, type, caption_de2, caption_en2, caption_es2);
                                rowData.items.add(tItem);

                            }
                        }
                    }

                    if (content.get("item") instanceof JSONObject) {

                        JSONObject item = content.getJSONObject("item");

                        String name = item.getJSONObject("@attributes").getString("name");
                        String type2 = item.getJSONObject("@attributes").getString("type");

                        String caption_de2 = item.getJSONObject("@attributes").getString("caption");
                        String caption_en2 = item.getJSONObject("@attributes").getString("caption_en");
                        String caption_es2 = item.getJSONObject("@attributes").getString("caption_es");
                        //String dest_dir = item.getJSONObject("@attributes").getString("type");
                        System.out.println("name=" + name + " type=" + type2);

                        TestItem tItem = new TestItem(name, type, caption_de2, caption_en2, caption_es2);

                        rowData.items.add(tItem);

                    }

                }
            }

        }

        return data;
    }

    private void setColWidth(JTable table, int colidx, int width) {
        TableColumn column = table.getColumnModel().getColumn(colidx);
        column.setMinWidth(width);
        column.setMaxWidth(width);
        column.setPreferredWidth(width);
    }

    private void setLookAndFeel() {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
        }
    }

    private ArrayList<String> FillModelWithData2(String type) {
        File directory = new File(myopenlabpath + "/Elements/" + type);

        ArrayList<String> files = new ArrayList<>();
        // get all the files from a directory
        File[] fList = directory.listFiles();
        if (fList != null) {
            for (File file : fList) {

                if (file.isDirectory()) {
                    File f = new File(file.getAbsoluteFile() + "/definition.def");
                    if (f.exists()) {
                        files.add(file.getName());
                    }
                }
            }
        }

        return files;
    }

    private ArrayList<String> FillModelWithData_for_Docs(String type) {
        File directory = new File(myopenlabpath + "/Elements/" + type);

        ArrayList<String> files = new ArrayList<>();
        // get all the files from a directory
        File[] fList = directory.listFiles();
        if (fList != null) {
            for (File file : fList) {

                if (file.isDirectory()) {
                    files.add(file.getName());
                }
            }
        }

        return files;
    }

    private void FillData2(ArrayList<String> files, MyTableModel model, String type) {
        for (MyOpenLabRow row : data) {

            try {

                if (files.indexOf(row.getEntry_name()) == -1 && row.getType().equalsIgnoreCase(type)) {

                    String url = settings.getRepository_domain() + "/repository/new/" + row.getType() + "/" + row.getEntry_name() + "/definition.def";

                    url = url.replaceAll(" ", "%20");
                    String definition_def = getStringFromUrl(url);
                    DFProperties definition = Tools.getProertiesFromDefinitionString(definition_def);

                    URL icon_url = new URL(settings.getRepository_domain() + "/repository/new/" + row.getType() + "/" + row.getEntry_name() + "/" + definition.iconFilename);
                    ImageIcon icon = new ImageIcon(icon_url);

                    MyTableRow newRow = new MyTableRow(false, icon, row.getEntry_name(), row.getCategorie(), row.getDate(), row.getAuthor(), row.getType());
                    newRow.setCaption_de(row.getCaption_de());
                    newRow.setCaption_en(row.getCaption_en());
                    newRow.setCaption_es(row.getCaption_es());
                    model.data.add(newRow);
                }
            } catch (MalformedURLException ex) {
                Logger.getLogger(frmUpdate.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(frmUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void initTable1() {

        list1.clear();

        ArrayList<String> files1 = FillModelWithData2("CircuitElements");
        ArrayList<String> files2 = FillModelWithData2("FrontElements");
        ArrayList<String> files3 = FillModelWithData_for_Docs("Documentations");
        ArrayList<String> files4 = FillModelWithData_for_Docs("VirtualMachines");

        MyTableModel model1 = new MyTableModel(list1);

        Locale locale = Locale.getDefault();
        String lang = locale.getLanguage();
        switch (lang) {
            case "de": {
                String[] HEADER = {"Installieren", "", "Package Name", "Titel", "Categorie", "Datum", "Author", "Typ"};
                model1.HEADER = HEADER;
                break;
            }
            case "en": {
                String[] HEADER = {"Install", "", "Package Name", "Caption", "Category", "Date", "Author", "Type"};
                model1.HEADER = HEADER;
                break;
            }
            case "es": {
                String[] HEADER = {"Instalar", "", "Nombre del paquete", "Subtítulo", "Categoría", "Fecha", "Autor", "Tipo"};
                model1.HEADER = HEADER;
                break;
            }
        }

        jTable1.setModel(model1);

        setColWidth(jTable1, 0, 100);
        setColWidth(jTable1, 1, 40);
        setColWidth(jTable1, 2, 250);
        setColWidth(jTable1, 3, 150);
        data = getMyOpenLabRepositoryData();

        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                String username = settings.getRepository_login_username();
                String password = settings.getRepository_login_password();

                return new PasswordAuthentication(username, password.toCharArray());
            }
        });

        FillData2(files1, model1, "CircuitElements");
        FillData2(files2, model1, "FrontElements");
        FillData2(files3, model1, "Documentations");
        FillData2(files4, model1, "VirtualMachines");

    }

    private void FillModelWithData(MyTableModel model, String type) {

        File directory = new File(myopenlabpath + "/Elements/" + type);
        ArrayList<String> files = new ArrayList<>();
        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {

            if (file.isDirectory()) {

                DFProperties definition_def = Tools.getProertiesFromDefinitionFile(file);
                if (definition_def.redirect.trim().length() == 0) {

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

                            String author = "";
                            String date = "";
                            String categorie = "";

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
                                if (name.equalsIgnoreCase("categorie")) {
                                    categorie = value;
                                }
                                if (name.equalsIgnoreCase("author")) {
                                    author = value;
                                }

                                if (name.equalsIgnoreCase("date")) {
                                    date = value;
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

                            // Zuerst das Icon aus dem Cache löschen!
                            ImageIcon icon = new ImageIcon(file.getAbsolutePath() + "/" + definition_def.iconFilename);
                            icon.getImage().flush();

                            icon = new ImageIcon(file.getAbsolutePath() + "/" + definition_def.iconFilename);

                            MyTableRow newRow = new MyTableRow(false, icon, file.getName(), categorie, date, author, type);
                            newRow.setCaption_de(caption_de);
                            newRow.setCaption_en(caption_en);
                            newRow.setCaption_es(caption_es);
                            model.data.add(newRow);
                        } catch (ParserConfigurationException ex) {
                            Logger.getLogger(frmUpdate.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SAXException ex) {
                            Logger.getLogger(frmUpdate.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(frmUpdate.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

            }
        }

        /*File directory = new File(myopenlabpath + "/Elements/" + type);
        ArrayList<String> files = new ArrayList<>();
        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {

            if (file.isDirectory()) {
                File f = new File(file.getAbsoluteFile() + "/definition.def");
                if (f.exists()) {
                    files.add(file.getName());
                }
            }
        }
        for (String file : files) {
            MyTableRow newRow = new MyTableRow(false, file, "", "", "", type);
            model.data.add(newRow);
        }*/
    }

    private void FillModelWithData_forDocumentations(MyTableModel model, String type) {

        File directory = new File(myopenlabpath + "/Elements/" + type);

        if (directory.exists()) {
            ArrayList<String> files = new ArrayList<>();
            // get all the files from a directory
            File[] fList = directory.listFiles();
            for (File file : fList) {

                if (file.isDirectory()) {
                    DFProperties definition_def = Tools.getProertiesFromDefinitionFile(file);
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

                            String author = "";
                            String date = "";
                            String categorie = "";

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
                                if (name.equalsIgnoreCase("categorie")) {
                                    categorie = value;
                                }
                                if (name.equalsIgnoreCase("author")) {
                                    author = value;
                                }

                                if (name.equalsIgnoreCase("date")) {
                                    date = value;
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

                            // Zuerst das Icon aus dem Cache löschen!
                            ImageIcon icon = new ImageIcon(file.getAbsolutePath() + "/" + definition_def.iconFilename);
                            icon.getImage().flush();

                            icon = new ImageIcon(file.getAbsolutePath() + "/" + definition_def.iconFilename);

                            MyTableRow newRow = new MyTableRow(false, icon, file.getName(), categorie, date, author, type);
                            newRow.setCaption_de(caption_de);
                            newRow.setCaption_en(caption_en);
                            newRow.setCaption_es(caption_es);
                            model.data.add(newRow);
                        } catch (ParserConfigurationException ex) {
                            Logger.getLogger(frmUpdate.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SAXException ex) {
                            Logger.getLogger(frmUpdate.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(frmUpdate.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
            }
        }
    }

    public void initTable2() {

        list2.clear();
        MyTableModel model2 = new MyTableModel(list2);

        Locale locale = Locale.getDefault();
        String lang = locale.getLanguage();
        switch (lang) {
            case "de": {
                String[] HEADER = {"", "", "Package Name", "Titel", "Categorie", "Datum", "Author", "Typ"};
                model2.HEADER = HEADER;
                break;
            }
            case "en": {
                String[] HEADER = {"", "", "Package Name", "Caption", "Category", "Date", "Author", "Type"};
                model2.HEADER = HEADER;
                break;
            }
            case "es": {
                String[] HEADER = {"", "", "Nombre del paquete", "Subtítulo", "Categoría", "Fecha", "Autor", "Tipo"};
                model2.HEADER = HEADER;
                break;
            }
        }

        jTable2.setModel(model2);

        setColWidth(jTable2, 0, 50);
        setColWidth(jTable2, 1, 40);
        setColWidth(jTable2, 2, 250);
        setColWidth(jTable2, 3, 150);

        FillModelWithData(model2, "CircuitElements");
        FillModelWithData(model2, "FrontElements");
        FillModelWithData_forDocumentations(model2, "Documentations");
        FillModelWithData_forDocumentations(model2, "VirtualMachines");

    }

    /**
     * Creates new form NewJFrame
     */
    public frmUpdate() {
        //Locale.setDefault(new Locale("de", "DE"));

        initComponents();

        if (settings.getProxy_host().trim().length() > 0) {
            System.setProperty("http.proxyHost", settings.getProxy_host().trim());
            System.out.println("http.proxyHost=" + settings.getProxy_host().trim());
        } else {
            System.setProperty("http.proxyHost", "");
        }
        if (settings.getProxy_port().trim().length() > 0) {
            System.setProperty("http.proxyPort", settings.getProxy_port().trim());
            System.out.println("http.proxyPort=" + settings.getProxy_port().trim());
        } else {
            System.setProperty("http.proxyPort", "");
        }

        if (settings.getRepository_domain().trim().length() > 0 && settings.getRepository_login_username().trim().length() > 0 && settings.getRepository_login_password().trim().length() > 0) {
            jMenuItemUploadPackage.setEnabled(true);
        } else {
            jMenuItemUploadPackage.setEnabled(false);
        }

        // Create a new trust manager that trust all certificates
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

// Activate the new trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }

        // Fenster mittig anzeigen!
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        setLookAndFeel();

        jTable1.setRowHeight(40);
        jTable2.setRowHeight(40);

        //initTable1();
        initTable2();

        ImageIcon icon = new ImageIcon(getClass().getResource("/Bilder/16x16/storage.png"));
        setIconImage(icon.getImage());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItemDownloadPAckage = new javax.swing.JMenuItem();
        jMenuItemUploadPackage = new javax.swing.JMenuItem();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("de/myopenlab/update/MainFrame"); // NOI18N
        jMenuItemDownloadPAckage.setText(bundle.getString("DOWNLOAD PACKAGE FOR REPOSITORY")); // NOI18N
        jMenuItemDownloadPAckage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDownloadPAckageActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItemDownloadPAckage);

        jMenuItemUploadPackage.setText(bundle.getString("UPLOAD PACKAGE DIRECT TO REPOSITORY")); // NOI18N
        jMenuItemUploadPackage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemUploadPackageActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItemUploadPackage);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(bundle.getString("title")); // NOI18N

        jTable2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable2.setGridColor(new java.awt.Color(230, 230, 230));
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable2MousePressed(evt);
            }
        });
        jTable2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable2KeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(jTable2);

        jButton3.setText(bundle.getString("Delete_Packages")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton2.setText(bundle.getString("SELECT ALL")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setText(bundle.getString("UNSELECT ALL")); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText(bundle.getString("INVERT")); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton9.setText(bundle.getString("REFRESH")); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel4.setText(bundle.getString("INSTALLED PACKAGES")); // NOI18N

        jButton11.setText(bundle.getString("NEUES DOCUMENT PACKAGE")); // NOI18N
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setText(bundle.getString("NEUES VM  PACKAGE")); // NOI18N
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setText(bundle.getString("NEUES CIRCUITELEMENT")); // NOI18N
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setText(bundle.getString("NEUES FRONTELEMENT")); // NOI18N
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jButton9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton14))
                            .addComponent(jButton3))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton9)
                    .addComponent(jButton11)
                    .addComponent(jButton12)
                    .addComponent(jButton13)
                    .addComponent(jButton14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("DELETE PACKAGES"), jPanel3); // NOI18N

        jPanel1.setAutoscrolls(true);
        jPanel1.setPreferredSize(new java.awt.Dimension(590, 472));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setGridColor(new java.awt.Color(230, 230, 230));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable1KeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText(bundle.getString("Install_Packages")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton6.setText(bundle.getString("SELECT ALL")); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText(bundle.getString("UNSELECT ALL")); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText(bundle.getString("INVERT")); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton10.setText(bundle.getString("REFRESH")); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel3.setText(bundle.getString("PACKAGES FROM MYOPENLAB REPOSITORY THAT ARE NOT INSTALLED")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 279, Short.MAX_VALUE)
                                .addComponent(jButton10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton8)))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(jButton7)
                    .addComponent(jButton8)
                    .addComponent(jButton10)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("INSTALL PACKAGES"), jPanel1); // NOI18N

        jLabel1.setText(bundle.getString("PROTOKOLL")); // NOI18N

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane6.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane6)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("Installed Packages");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private String getTranslation(TestItem subItem) {
        Locale locale = Locale.getDefault();
        String lang = locale.getLanguage();

        switch (lang) {
            case "de":
                return subItem.caption_de;
            case "en":
                return subItem.caption_en;
            case "es":
                return subItem.caption_es;
        }
        return "";
    }

    private void processTable1() {

        DefaultListModel listModel;
        listModel = new DefaultListModel();

        int row_index = jTable1.getSelectedRow();

        MyTableModel model2 = (MyTableModel) jTable1.getModel();

        MyTableRow itemA = model2.data.get(row_index);
        String entry_name = itemA.getName();
        String type = itemA.getType();

        MyOpenLabRow rowX = null;
        for (MyOpenLabRow row : data) {
            if (row.getEntry_name() == entry_name && row.getType() == type) {
                rowX = row;
                break;
            }
        }

        if (rowX != null) {
            for (int i = 0; i < rowX.items.size(); i++) {
                TestItem subItem = rowX.items.get(i);
                //str += subItem.name + "\n";

                listModel.addElement(getTranslation(subItem));

            }
        }

    }

    private ArrayList<String> listFilesFromDir(int stufe, String dirname) {
        File directory = new File(dirname);

        ArrayList<String> files = new ArrayList<>();
        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {

            if (file.isDirectory()) {
                files.addAll(listFilesFromDir(stufe + 1, file.getAbsolutePath()));
                File f = new File(file.getAbsoluteFile() + "/definition.def");
                if (f.exists()) {

                    String tmp = "";
                    for (int i = 0; i < stufe; i++) {
                        tmp += "+";
                    }

                    files.add(tmp + file.getName());
                }
            }
        }

        return files;
    }

    private void processTable2() {

        int row_index = jTable2.getSelectedRow();

        MyTableModel model2 = (MyTableModel) jTable2.getModel();

        MyTableRow item = model2.data.get(row_index);
        String entry_name = item.getName();
        String type = item.getType();

        String path = FrameMain.elementPath + "/" + type;

        Dialog_create_new_group frm = new Dialog_create_new_group(this, true, "edit", path + "/" + entry_name);

        frm.setVisible(true);

        initTable2();

    }

    public void log(String message) {

        jTextArea1.setText(jTextArea1.getText() + message + "\n");
        jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());

    }

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        initTable1();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        for (MyTableRow row : list1) {

            row.setSelected(!row.isSelected());
        }
        jTable1.repaint();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        for (MyTableRow row : list1) {

            row.setSelected(false);
        }
        jTable1.repaint();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        for (MyTableRow row : list1) {

            row.setSelected(true);
        }
        jTable1.repaint();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        Thread tmp = new Thread(new InstallPackages(this));
        tmp.start();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyReleased
        processTable1();
    }//GEN-LAST:event_jTable1KeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

        processTable1();
    }//GEN-LAST:event_jTable1MouseClicked

    private String createEmptyInfoXML(String package_filename) {
        String xml = "";
        xml += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        xml += "<root>\n";

        String caption_de = "";
        String caption_en = "";
        String caption_es = "";

        xml += "  <caption>" + caption_de + "</caption>\n";
        xml += "  <caption_en>" + caption_en + "</caption_en>\n";
        xml += "  <caption_es>" + caption_es + "</caption_es>\n";
        xml += "  <categorie>Education</categorie>\n";
        xml += "  <author></author>\n";
        xml += "  <email></email>\n";
        xml += "  <web></web>\n";
        xml += "  <version>1.0.0.0</version>\n";
        xml += "  <date>03.04.2016</date>\n";

        String type = "documentation";
        String dest_path = "/" + type + "/" + package_filename;
        xml += "  <dest_path>" + dest_path + "</dest_path>\n";
        xml += "  <type>" + type + "</type>\n";
        xml += "  <content>\n";
        xml += "  </content>\n";
        xml += "</root>";

        return xml;
    }


    private void jMenuItemDownloadPAckageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDownloadPAckageActionPerformed

        int row_index = jTable2.getSelectedRow();

        if (row_index > -1) {

            MyTableModel model2 = (MyTableModel) jTable2.getModel();

            MyTableRow item = model2.data.get(row_index);
            String entry_name = item.getName();
            String type = item.getType();

            String path = FrameMain.elementPath + "/" + type + "/" + entry_name;

            JFileChooser fileChooser = new JFileChooser();

            File ff = new File(path);

            fileChooser.setSelectedFile(new File(ff.getName() + ".zip"));
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                File dir = new File(path);
                String zipDirName = file.getAbsolutePath();

                ZipFiles zipFiles = new ZipFiles();

                zipFiles.zipDirectory(dir, zipDirName);
            }
        }
    }//GEN-LAST:event_jMenuItemDownloadPAckageActionPerformed

    private void jMenuItemUploadPackageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemUploadPackageActionPerformed
        try {

            int row_index = jTable2.getSelectedRow();

            File temp = File.createTempFile("myopenlab", ".zip");
            //temp.deleteOnExit();
            if (row_index > -1) {

                MyTableModel model2 = (MyTableModel) jTable2.getModel();

                MyTableRow item = model2.data.get(row_index);
                String entry_name = item.getName();
                String type = item.getType();

                String path = FrameMain.elementPath + "/" + type + "/" + entry_name;

                File file = temp;

                File dir = new File(path);
                String zipDirName = file.getAbsolutePath();

                ZipFiles zipFiles = new ZipFiles();

                zipFiles.zipDirectory(dir, zipDirName);

                String url = settings.getRepository_domain() + "/upload/upload.php";
                String charset = "UTF-8";
                File binaryFile = temp;

                String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
                String CRLF = "\r\n"; // Line separator required by multipart/form-data.
                URLConnection connection = new URL(url).openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                try (
                        OutputStream output = connection.getOutputStream();
                        PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);) {

                    // Send normal param.
                    writer.append("--" + boundary).append(CRLF);
                    writer.append("Content-Disposition: form-data; name=\"filename\"").append(CRLF);
                    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
                    writer.append(CRLF).append(entry_name).append(CRLF).flush();

                    // Send normal param.
                    writer.append("--" + boundary).append(CRLF);
                    writer.append("Content-Disposition: form-data; name=\"type\"").append(CRLF);
                    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
                    writer.append(CRLF).append(type).append(CRLF).flush();

                    // Send binary file.
                    writer.append("--" + boundary).append(CRLF);
                    writer.append("Content-Disposition: form-data; name=\"binaryFile\"; filename=\"" + binaryFile.getName() + "\"").append(CRLF);
                    writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(binaryFile.getName())).append(CRLF);
                    writer.append("Content-Transfer-Encoding: binary").append(CRLF);
                    writer.append(CRLF).flush();
                    Files.copy(binaryFile.toPath(), output);
                    output.flush(); // Important before continuing with writer!
                    writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.
                    // End of multipart/form-data.
                    writer.append("--" + boundary + "--").append(CRLF).flush();
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(frmUpdate.class.getName()).log(Level.SEVERE, null, ex);
                }
                // Request is lazily fired whenever you need to obtain information about response.
                int responseCode = ((HttpURLConnection) connection).getResponseCode();
                Tools.showMessage(this, "" + responseCode);

                String str = "";

                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        str += inputLine + "\n";
                    }
                }

                System.out.println(str);

                Tools.showMessage(this, "" + str);
            }
        } catch (IOException ex) {
            Logger.getLogger(frmUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItemUploadPackageActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        String uuid = "fe-" + UUID.randomUUID().toString();
        File tmp = new File(FrameMain.elementPath + "/FrontElements/" + uuid);

        tmp.mkdirs();

        //URL icon_url = this.getClass().getClassLoader().getResource("create_new_group/std_lib_icon_32.png");
        
         String icon32=new File(FrameMain.elementPath + "/std_lib_icon_32.png").getAbsolutePath();

        try {
            Tools.copyFileUsingStream(new File(icon32), new File(tmp + "/icon32.png"));
        } catch (IOException ex) {
            Logger.getLogger(Dialog_create_new_group.class.getName()).log(Level.SEVERE, null, ex);
        }

        String xml = createEmptyInfoXML(tmp.getName());
        Tools.saveText(new File(tmp.getAbsolutePath() + "/info.xml"), xml);

        Dialog_create_new_group frm = new Dialog_create_new_group(this, true, "edit", tmp.getAbsolutePath());
        frm.load(tmp.getAbsolutePath());
        frm.setVisible(true);

        if (frm.resultcode) {
            initTable2();
        } else {
            Tools.deleteDirectory(tmp);
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed

        String uuid = "ce-" + UUID.randomUUID().toString();
        File tmp = new File(FrameMain.elementPath + "/CircuitElements/" + uuid);

        tmp.mkdirs();

        //URL icon_url = this.getClass().getClassLoader().getResource("create_new_group/std_lib_icon_32.png");
         String icon32=new File(FrameMain.elementPath + "/std_lib_icon_32.png").getAbsolutePath();

        try {
            Tools.copyFileUsingStream(new File(icon32), new File(tmp + "/icon32.png"));
        } catch (IOException ex) {
            Logger.getLogger(Dialog_create_new_group.class.getName()).log(Level.SEVERE, null, ex);
        }

        String xml = createEmptyInfoXML(tmp.getName());

        Tools.saveText(new File(tmp.getAbsolutePath() + "/info.xml"), xml);

        Dialog_create_new_group frm = new Dialog_create_new_group(this, true, "edit", tmp.getAbsolutePath());
        frm.load(tmp.getAbsolutePath());
        frm.setVisible(true);

        if (frm.resultcode) {
            initTable2();
        } else {
            Tools.deleteDirectory(tmp);
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed

        new File(FrameMain.elementPath + "/VirtualMachines").mkdirs();

        if (!new File(FrameMain.elementPath + "/VirtualMachines/project.myopenlab").exists()) {

            try (PrintWriter out = new PrintWriter(FrameMain.elementPath + "/VirtualMachines/project.myopenlab")) {
                out.println("");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DialogSaveAsModul.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        String uuid = "vm-" + UUID.randomUUID().toString();
        File tmp = new File(FrameMain.elementPath + "/VirtualMachines/" + uuid);

        if (!tmp.exists()) {
            try {
                tmp.mkdirs();

               // URL icon_url = this.getClass().getClassLoader().getResource("create_new_group/std_lib_icon_32.png");
                
                 String icon32=new File(FrameMain.elementPath + "/std_lib_icon_32.png").getAbsolutePath();

                try {
                    Tools.copyFileUsingStream(new File(icon32), new File(tmp + "/icon32.png"));
                } catch (IOException ex) {
                    Logger.getLogger(Dialog_create_new_group.class.getName()).log(Level.SEVERE, null, ex);
                }

                String xml = createEmptyInfoXML(tmp.getName());

                Tools.saveText(new File(tmp.getAbsolutePath() + "/info.xml"), xml);

                Tools.saveText(new File(tmp.getAbsolutePath() + "/project.myopenlab"), "MAINVM= Main.vlogic");

                Tools.copy(new File(FrameMain.elementPath + "/empty.vlogic"), new File(tmp.getAbsolutePath() + "/main.vlogic"));

                Dialog_create_new_group frm = new Dialog_create_new_group(this, true, "edit", tmp.getAbsolutePath());
                frm.load(tmp.getAbsolutePath());
                frm.setVisible(true);

                if (frm.resultcode) {
                    owner.openProject(new File(FrameMain.elementPath + "/VirtualMachines"));

                    MyNode node = new MyNode(null);
                    node.projectPath = FrameMain.elementPath + "/VirtualMachines";
                    node.relativePath = "/VirtualMachines";
                    owner.projectPaletteAction("RELOAD", node);

                    initTable2();
                } else {
                    Tools.deleteDirectory(tmp);
                }

            } catch (IOException ex) {
                Logger.getLogger(frmUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            Tools.showMessage(this, java.util.ResourceBundle.getBundle("de/myopenlab/update/MainFrame").getString("VM PACKAGE ALREADY EXISTS"));
        }

    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed

        new File(FrameMain.elementPath + "/Documentations").mkdirs();

        if (!new File(FrameMain.elementPath + "/Documentations/project.myopenlab").exists()) {

            try (PrintWriter out = new PrintWriter(FrameMain.elementPath + "/Documentations/project.myopenlab")) {
                out.println("");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DialogSaveAsModul.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        String uuid = "dc-" + UUID.randomUUID().toString();
        File tmp = new File(FrameMain.elementPath + "/Documentations/" + uuid);

        if (!tmp.exists()) {
            tmp.mkdirs();

            String icon32=new File(FrameMain.elementPath + "/std_lib_icon_32.png").getAbsolutePath();
            //URL icon_url = this.getClass().getClassLoader().getResource("create_new_group/std_lib_icon_32.png");

            try {
                //Tools.showMessage(icon32);
                // Tools.showMessage(tmp + "/icon32.png");
                Tools.copyFileUsingStream(new File(icon32), new File(tmp + "/icon32.png"));
            } catch (IOException ex) {
                Logger.getLogger(Dialog_create_new_group.class.getName()).log(Level.SEVERE, null, ex);
            }

            String xml = createEmptyInfoXML(tmp.getName());

            Tools.saveText(new File(tmp.getAbsolutePath() + "/info.xml"), xml);

            Tools.saveText(new File(tmp.getAbsolutePath() + "/project.myopenlab"), "");

            //Tools.copy(new File(FrameMain.elementPath + "/empty.vlogic"), new File(tmp.getAbsolutePath() + "/main.vlogic"));
            Dialog_create_new_group frm = new Dialog_create_new_group(this, true, "edit", tmp.getAbsolutePath());
            frm.load(tmp.getAbsolutePath());
            frm.setVisible(true);

            if (frm.resultcode) {

                owner.openProject(new File(FrameMain.elementPath + "/Documentations"));

                MyNode node = new MyNode(null);
                node.projectPath = FrameMain.elementPath + "/Documentations";
                node.relativePath = "/Documentations";
                owner.projectPaletteAction("RELOAD", node);

                initTable2();
            } else {
                Tools.deleteDirectory(tmp);

            }
        } else {
            Tools.showMessage(this, java.util.ResourceBundle.getBundle("de/myopenlab/update/MainFrame").getString("DOCUMENT PACKAGE ALREADY EXISTS"));
        }

    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed

        initTable2();

    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        for (MyTableRow row : list2) {

            row.setSelected(!row.isSelected());

        }
        jTable2.repaint();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        for (MyTableRow row : list2) {

            row.setSelected(false);

        }

        jTable2.repaint();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        for (MyTableRow row : list2) {

            row.setSelected(true);
        }
        jTable2.repaint();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        Thread temp = new Thread(new DeletePackages(this));
        temp.start();

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTable2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable2KeyReleased

    }//GEN-LAST:event_jTable2KeyReleased

    private void jTable2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MousePressed
        int button = evt.getButton();

        if (button == 3) {
            jPopupMenu1.show(jTable2, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jTable2MousePressed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked

        if (evt.getClickCount() == 2) {
            processTable2();
        }
    }//GEN-LAST:event_jTable2MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        if (args.length >= 1) {
            frmUpdate.myopenlabpath = args[0];
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new frmUpdate().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenuItem jMenuItemDownloadPAckage;
    private javax.swing.JMenuItem jMenuItemUploadPackage;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

}
